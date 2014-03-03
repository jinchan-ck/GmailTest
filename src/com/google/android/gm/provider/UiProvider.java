package com.google.android.gm.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Pair;
import android.util.SparseArray;
import com.android.mail.providers.Attachment;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.ListParams;
import com.android.mail.providers.Message;
import com.android.mail.providers.ReplyFromAccount;
import com.android.mail.providers.UIProvider;
import com.android.mail.providers.UIProvider.ConversationColumns;
import com.android.mail.providers.UIProvider.FolderColumns;
import com.android.mail.providers.UIProvider.MessageColumns;
import com.android.mail.providers.UIProviderValidator;
import com.android.mail.utils.DelayedTaskHandler;
import com.android.mail.utils.MatrixCursorWithExtra;
import com.google.android.gm.AccountHelper;
import com.google.android.gm.AccountHelper.AccountResultsCallback;
import com.google.android.gm.EmailAddress;
import com.google.android.gm.LabelOperations;
import com.google.android.gm.LabelOperations.Operation;
import com.google.android.gm.Utils;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.provider.uiprovider.AccountState;
import com.google.android.gm.provider.uiprovider.ConversationState;
import com.google.android.gm.provider.uiprovider.MessageState;
import com.google.android.gm.provider.uiprovider.UIAttachment;
import com.google.android.gm.provider.uiprovider.UIAttachment.UriParser;
import com.google.android.gm.provider.uiprovider.UIConversationCursor;
import com.google.android.gm.provider.uiprovider.UILabelCursor;
import com.google.android.gm.provider.uiprovider.UIMessageCursor;
import com.google.android.gm.utils.CustomFromUtils;
import com.google.android.gsf.Gservices;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;

public class UiProvider extends ContentProvider
  implements MailEngine.MailEngineResultCallback
{
  private static final Uri ACCOUNTS_URI;
  private static final Uri BASE_AUTH_URI;
  private static final Uri BASE_GVIEW_URI;
  private static final Uri BASE_SETTINGS_URI;
  private static final String[] CONVERSATION_QUERY_LOCAL_ONLY_SELECTION_ARGS;
  private static final Integer GMAIL_CAPABILITIES = Integer.valueOf(1572863);
  private static final ImmutableSet<String> INVALID_ACCOUNT_NAMES;
  private static final ImmutableSet<String> INVARIANT_LABELS;
  private static final int SEARCH_HASHCODE;
  private static final int[] UI_PROVIDER_MESSAGE_TEXT_SIZE_VALUES = { -2, -1, 0, 1, 2 };
  private static final String[] UI_PROVIDER_REQUIRED_LABELS;
  private static final int[] UI_PROVIDER_SNAP_HEADER_MODE_VALUES = { 0, 1, 2 };
  private static int sAccountNotificationDelayMs;
  private static final Map<String, AccountState> sAccountStateMap = Maps.newHashMap();
  private static String sGmailQuote;
  private static UiProvider sInstance;
  private static Map<String, CharSequence> sSystemLabelNameMap;
  private static final UriMatcher sUrlMatcher;
  private final Map<String, AccountChangedNotifier> mAccountChangeNotifiers = Maps.newHashMap();
  private volatile boolean mAccountsFullyInitialized = false;
  private final Set<String> mAccountsPendingInitialization = Sets.newHashSet();
  private ContentResolver mContentResolver;
  private Gmail mGmail;
  private int mLastSequence = -1;
  private boolean mMailEnginesInitialized = false;
  private final List<ConversationLabelOperation> mPreviousOperationUndoOps = Lists.newArrayList();
  private Handler mUiProviderHandler;

  static
  {
    UI_PROVIDER_REQUIRED_LABELS = new String[] { "^^out" };
    ACCOUNTS_URI = Uri.parse("content://com.android.gmail.ui/accounts");
    BASE_SETTINGS_URI = Uri.parse("setting://gmail/");
    BASE_AUTH_URI = Uri.parse("auth://gmail/");
    BASE_GVIEW_URI = Uri.parse("gview://preview");
    CONVERSATION_QUERY_LOCAL_ONLY_SELECTION_ARGS = new String[] { "SELECTION_ARGUMENT_DO_NOT_BECOME_ACTIVE_NETWORK_CURSOR" };
    INVALID_ACCOUNT_NAMES = ImmutableSet.of("null");
    SEARCH_HASHCODE = "search".hashCode();
    sUrlMatcher = new UriMatcher(-1);
    INVARIANT_LABELS = ImmutableSet.of("^u", "^t", "^o");
    sUrlMatcher.addURI("com.android.gmail.ui", "accounts", 1);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/account", 2);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/labels", 3);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/label/*", 18);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/conversations/*", 4);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/conversationsForLabel/*", 5);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/conversationMessages/#", 6);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/messageAttachments/#/#", 22);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/messageAttachment/#/#/*", 23);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/messages", 7);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/sendNewMessage", 8);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/saveNewMessage", 9);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/expungeMessage", 24);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/message/#", 10);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/message/save", 11);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/message/send", 12);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/undo", 15);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/refresh", 17);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/refresh/*", 16);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/conversation/#", 13);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/conversationInlineResource/#/*", 14);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/search", 19);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/searchConversations", 20);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/recentFolders", 21);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/defaultRecentFolders", 25);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/cookie", 26);
    sUrlMatcher.addURI("com.android.gmail.ui", "*/settings", 27);
  }

  private int addRemoveLabel(String[] paramArrayOfString, String paramString, LabelOperations paramLabelOperations)
  {
    if (paramLabelOperations.count() == 0)
      return 0;
    MailEngine localMailEngine = getOrMakeMailEngine(paramString);
    ArrayList localArrayList1 = Lists.newArrayList();
    ArrayList localArrayList2 = Lists.newArrayList();
    List localList = paramLabelOperations.getOperationList();
    ArrayList localArrayList3 = new ArrayList();
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str2 = paramArrayOfString[j];
      com.google.android.gm.ConversationInfo localConversationInfo2 = localMailEngine.getConversationForId(Gmail.CONVERSATION_PROJECTION_LIMITED, str2, paramString, this.mGmail);
      if (localConversationInfo2 != null)
        localArrayList3.add(localConversationInfo2);
    }
    Iterator localIterator = localArrayList3.iterator();
    while (localIterator.hasNext())
    {
      com.google.android.gm.ConversationInfo localConversationInfo1 = (com.google.android.gm.ConversationInfo)localIterator.next();
      long l1 = localConversationInfo1.getLocalMessageId();
      long l2 = localConversationInfo1.getServerMessageId();
      long l3 = localConversationInfo1.getConversationId();
      int k = localList.size();
      for (int m = 0; m < k; m++)
      {
        Map localMap = getRawOperations(localConversationInfo1, ((LabelOperations.Operation)localList.get(m)).mLabel, ((LabelOperations.Operation)localList.get(m)).mAdd);
        int n = localMap.size();
        String[] arrayOfString = (String[])localMap.keySet().toArray(new String[n]);
        int i1 = 0;
        if (i1 < n)
        {
          ContentValues localContentValues = new ContentValues();
          String str1 = arrayOfString[i1];
          boolean bool = ((Boolean)localMap.get(str1)).booleanValue();
          if (l1 != 0L)
          {
            localContentValues.put("canonicalName", str1);
            localContentValues.put("_id", Long.valueOf(l1));
            localContentValues.put("messageId", Long.valueOf(l2));
            localContentValues.put("conversation", Long.valueOf(l3));
            localContentValues.put("add_label_action", Boolean.valueOf(bool));
            localArrayList2.add(localContentValues);
          }
          while (true)
          {
            i1++;
            break;
            localContentValues.put("_id", Long.valueOf(l3));
            localContentValues.put("canonicalName", str1);
            localContentValues.put("maxMessageId", Long.valueOf(localConversationInfo1.getMaxMessageId()));
            localContentValues.put("add_label_action", Boolean.valueOf(bool));
            localArrayList1.add(localContentValues);
          }
        }
      }
    }
    if (localArrayList1.size() > 0)
      this.mGmail.addOrRemoveLabelOnConversationBulk(paramString, (ContentValues[])localArrayList1.toArray(new ContentValues[localArrayList1.size()]), true);
    if (localArrayList2.size() > 0)
      this.mGmail.addOrRemoveLabelOnMessageBulk(paramString, (ContentValues[])localArrayList2.toArray(new ContentValues[localArrayList2.size()]), true);
    return localArrayList3.size();
  }

  private void addUndoOperation(int paramInt, ConversationLabelOperation paramConversationLabelOperation)
  {
    List localList = this.mPreviousOperationUndoOps;
    if (paramInt != -1);
    try
    {
      if (paramInt > this.mLastSequence)
      {
        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = Integer.valueOf(this.mPreviousOperationUndoOps.size());
        arrayOfObject[1] = Integer.valueOf(paramInt);
        arrayOfObject[2] = Integer.valueOf(this.mLastSequence);
        LogUtils.w("Gmail", "About to clean %d undo operations. sequenceNum:%d mLastSequence: %d", arrayOfObject);
        this.mPreviousOperationUndoOps.clear();
        this.mLastSequence = paramInt;
      }
      this.mPreviousOperationUndoOps.add(paramConversationLabelOperation);
      return;
    }
    finally
    {
    }
  }

  static void broadcastAccountChangeNotification(Context paramContext, String paramString)
  {
    UiProvider localUiProvider = sInstance;
    if (localUiProvider != null)
      localUiProvider.scheduleAccountChangeNotification(paramContext, paramString);
  }

  static void broadcastAccountFolderChangeNotification(Context paramContext, String paramString1, boolean paramBoolean, String paramString2)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    if (paramBoolean)
    {
      localContentResolver.notifyChange(getAccountSearchUri(paramString1), null, false);
      return;
    }
    localContentResolver.notifyChange(getAccountLabelNotificationUri(paramString1, paramString2), null, false);
  }

  static void broadcastFolderNotifications(Context paramContext, Gmail.LabelMap paramLabelMap, String paramString, Set<Long> paramSet)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      Long localLong = (Long)localIterator.next();
      if (localLong != null)
      {
        String str = paramLabelMap.getCanonicalName(localLong.longValue());
        if (str != null)
          localContentResolver.notifyChange(getAccountLabelNotificationUri(paramString, str), null, false);
      }
    }
    if (paramSet.size() > 0)
    {
      localContentResolver.notifyChange(getAccountFoldersUri(paramString), null, false);
      localContentResolver.notifyChange(getRecentFoldersUri(paramString), null, false);
    }
  }

  private AttachmentCursor createAttachmentCursor(String paramString, long paramLong1, long paramLong2, String[] paramArrayOfString, int paramInt)
  {
    AttachmentCursor localAttachmentCursor = new AttachmentCursor(paramString, paramLong1, paramLong2, paramArrayOfString, paramInt);
    getConversationState(paramString, paramLong1).addAttachmentCursor(localAttachmentCursor);
    return localAttachmentCursor;
  }

  private ConversationLabelOperation createLabelOperations(String paramString, long paramLong, List<Folder> paramList, List<String> paramList1)
  {
    ConversationLabelOperation localConversationLabelOperation = new ConversationLabelOperation(paramString, paramLong, null);
    HashSet localHashSet1 = new HashSet();
    Iterator localIterator1 = paramList1.iterator();
    while (localIterator1.hasNext())
      localHashSet1.add((String)localIterator1.next());
    HashSet localHashSet2 = new HashSet();
    int i;
    Iterator localIterator2;
    if (paramList1.size() == 0)
    {
      i = 1;
      if (paramList == null)
        paramList = new ArrayList();
      localIterator2 = paramList.iterator();
    }
    while (true)
    {
      label105: if (!localIterator2.hasNext())
        break label243;
      String str2 = (String)((Folder)localIterator2.next()).uri.getPathSegments().get(2);
      localHashSet2.add(str2);
      if ((i != 0) || (!localHashSet1.contains(str2)));
      for (int m = 1; ; m = 0)
      {
        if ((m == 0) || (!isUserSettableLabel(str2)))
          break label219;
        Label localLabel2 = LabelManager.getLabel(getContext(), paramString, str2);
        if (localLabel2 == null)
          break label221;
        localConversationLabelOperation.add(localLabel2, true);
        break label105;
        i = 0;
        break;
      }
      label219: continue;
      label221: LogUtils.e("Gmail", "Couldn't create label for canonical name: %s", new Object[] { str2 });
    }
    label243: int j;
    Iterator localIterator3;
    if ((paramList == null) || (paramList.size() == 0))
    {
      j = 1;
      localIterator3 = paramList1.iterator();
    }
    while (true)
    {
      label270: if (!localIterator3.hasNext())
        break label385;
      String str1 = (String)localIterator3.next();
      if ((j != 0) || (!localHashSet2.contains(str1)));
      for (int k = 1; ; k = 0)
      {
        if ((k == 0) || (!isUserSettableLabel(str1)))
          break label361;
        Label localLabel1 = LabelManager.getLabel(getContext(), paramString, str1);
        if (localLabel1 == null)
          break label363;
        localConversationLabelOperation.add(localLabel1, false);
        break label270;
        j = 0;
        break;
      }
      label361: continue;
      label363: LogUtils.e("Gmail", "Couldn't create label for canonical name: %s", new Object[] { str1 });
    }
    label385: return localConversationLabelOperation;
  }

  private ConversationLabelOperation createLabelOperationsForUIOperation(String paramString1, long paramLong, String paramString2)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramString2;
    arrayOfObject[1] = Long.valueOf(paramLong);
    LogUtils.d("Gmail", "Received operation %s for conversation %d", arrayOfObject);
    ConversationLabelOperation localConversationLabelOperation = new ConversationLabelOperation(paramString1, paramLong, null);
    if ("archive".equals(paramString2))
      localConversationLabelOperation.add(LabelManager.getLabel(getContext(), paramString1, "^i"), false);
    Label localLabel;
    do
    {
      do
      {
        return localConversationLabelOperation;
        if ("mute".equals(paramString2))
        {
          localConversationLabelOperation.add(LabelManager.getLabel(getContext(), paramString1, "^g"), true);
          return localConversationLabelOperation;
        }
        if (("report_spam".equals(paramString2)) || ("report_not_spam".equals(paramString2)))
        {
          localConversationLabelOperation.add(LabelManager.getLabel(getContext(), paramString1, "^s"), "report_spam".equals(paramString2));
          return localConversationLabelOperation;
        }
      }
      while (!"report_phishing".equals(paramString2));
      localLabel = LabelManager.getLabel(getContext(), paramString1, "^p");
    }
    while (localLabel == null);
    localConversationLabelOperation.add(localLabel, true);
    return localConversationLabelOperation;
  }

  private int delete(String paramString1, String paramString2, int paramInt)
  {
    ConversationLabelOperation localConversationLabelOperation = new ConversationLabelOperation(paramString2, Long.parseLong(paramString1), null);
    localConversationLabelOperation.add(LabelManager.getLabel(getContext(), paramString2, "^k"), true);
    addUndoOperation(paramInt, (ConversationLabelOperation)localConversationLabelOperation.undoOperation());
    return addRemoveLabel(new String[] { paramString1 }, paramString2, localConversationLabelOperation);
  }

  private int expungeMessage(String paramString, ContentValues paramContentValues)
  {
    int i;
    if (!paramContentValues.containsKey("_id"))
      i = 0;
    MailEngine localMailEngine;
    MailSync.Message localMessage;
    do
    {
      return i;
      localMailEngine = getOrMakeMailEngine(paramString);
      long l = paramContentValues.getAsLong("_id").longValue();
      localMessage = localMailEngine.getLocalMessage(l, false);
      i = localMailEngine.expungeLocalMessage(l);
    }
    while (localMessage == null);
    this.mContentResolver.notifyChange(getConversationMessageListUri(localMailEngine.getAccountName(), localMessage.conversationId), null, false);
    return i;
  }

  private static Uri getAboutUri(String paramString)
  {
    return BASE_SETTINGS_URI.buildUpon().appendQueryParameter("preference_fragment_id", Long.toString(2131689743L)).appendQueryParameter("account", paramString).build();
  }

  private static Uri getAccountBaseNotificationUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.uiinternal/" + paramString + "/notification");
  }

  private static Uri getAccountConversationSearchUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/searchConversations");
  }

  private Cursor getAccountCookieCursor(String paramString, String[] paramArrayOfString)
  {
    int i = 0;
    MailEngine localMailEngine = getOrMakeMailEngine(paramString);
    String[] arrayOfString = UIProviderValidator.validateAccountCookieProjection(paramArrayOfString);
    MatrixCursor localMatrixCursor;
    try
    {
      String str3 = localMailEngine.getAuthToken();
      str1 = str3;
      if (str1 != null)
      {
        str2 = Urls.getCookieString(paramString, str1);
        localMatrixCursor = new MatrixCursor(arrayOfString, 1);
        localRowBuilder = localMatrixCursor.newRow();
        int j = arrayOfString.length;
        while (true)
        {
          if (i >= j)
            break label160;
          if (!TextUtils.equals(arrayOfString[i], "cookie"))
            break;
          localRowBuilder.add(str2);
          i++;
        }
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        LogUtils.e("Gmail", localIOException, "IOException retrieving auth token", new Object[0]);
        str1 = null;
      }
    }
    catch (MailEngine.AuthenticationException localAuthenticationException)
    {
      while (true)
      {
        MatrixCursor.RowBuilder localRowBuilder;
        LogUtils.e("Gmail", localAuthenticationException, "AuthenticationException retrieving auth token", new Object[0]);
        String str1 = null;
        continue;
        String str2 = null;
        continue;
        localRowBuilder.add(null);
      }
    }
    label160: return localMatrixCursor;
  }

  private static Uri getAccountCookieQueryUri(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("content://com.android.gmail.ui").buildUpon();
    localBuilder.appendEncodedPath(paramString).appendEncodedPath("cookie");
    return localBuilder.build();
  }

  private Cursor getAccountCursor(String paramString, String[] paramArrayOfString)
  {
    MailEngine localMailEngine = getOrMakeMailEngine(paramString);
    if ((localMailEngine != null) && (isAccountNameValid(localMailEngine.getAccountName())))
      return getAccountsCursorForMailEngines(paramArrayOfString, Collections.singletonList(localMailEngine));
    Object[] arrayOfObject = new Object[1];
    if (localMailEngine == null);
    for (String str = "null MailEngine"; ; str = localMailEngine.getAccountName())
    {
      arrayOfObject[0] = str;
      LogUtils.e("Gmail", "Invalid mailEngine. %s", arrayOfObject);
      return null;
    }
  }

  private static Uri getAccountExpungeMessageUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/expungeMessage");
  }

  private static Uri getAccountFoldersUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/labels");
  }

  private Cursor getAccountLabelCursor(String paramString1, String paramString2, String[] paramArrayOfString)
  {
    MailEngine localMailEngine = getOrMakeMailEngine(paramString1);
    return getUiLabelCursor(localMailEngine, paramString1, UIProviderValidator.validateFolderProjection(paramArrayOfString), localMailEngine.getLabelQueryBuilder(Gmail.LABEL_PROJECTION).filterCanonicalName(Collections.singletonList(paramString2)).showHidden(false).query());
  }

  public static Uri getAccountLabelNotificationUri(String paramString1, String paramString2)
  {
    Uri.Builder localBuilder = getAccountUri(paramString1).buildUpon();
    localBuilder.appendPath("label");
    localBuilder.appendPath(paramString2);
    return localBuilder.build();
  }

  public static Uri getAccountLabelUri(String paramString1, String paramString2)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString1 + "/label/" + Uri.encode(paramString2));
  }

  private Cursor getAccountLabelsCursor(String paramString, String[] paramArrayOfString)
  {
    MailEngine localMailEngine = getOrMakeMailEngine(paramString);
    return getUiLabelCursor(localMailEngine, paramString, UIProviderValidator.validateFolderProjection(paramArrayOfString), localMailEngine.getLabelQueryBuilder(Gmail.LABEL_PROJECTION).showHidden(false).query());
  }

  public static com.android.mail.providers.Account getAccountObject(Context paramContext, String paramString)
  {
    MatrixCursor localMatrixCursor = new MatrixCursor(UIProvider.ACCOUNTS_PROJECTION, 1);
    MatrixCursor.RowBuilder localRowBuilder = localMatrixCursor.newRow();
    populateAccountCursorRow(paramContext, paramString, null, UIProvider.ACCOUNTS_PROJECTION, localRowBuilder);
    localMatrixCursor.moveToFirst();
    return new com.android.mail.providers.Account(localMatrixCursor);
  }

  private static Uri getAccountRefreshUri(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("content://com.android.gmail.ui").buildUpon();
    localBuilder.appendEncodedPath(paramString).appendEncodedPath("refresh");
    return localBuilder.build();
  }

  private static Uri getAccountSaveDraftUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/saveNewMessage");
  }

  private static Uri getAccountSearchUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/search");
  }

  private static Uri getAccountSendMessageUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/sendNewMessage");
  }

  private static Uri getAccountSettingUri(String paramString)
  {
    return BASE_SETTINGS_URI.buildUpon().appendQueryParameter("account", paramString).build();
  }

  private static Uri getAccountUndoUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/undo");
  }

  public static Uri getAccountUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/account");
  }

  private static Uri getAccountViewIntentProxyUri(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("content://com.android.gmail.ui").buildUpon();
    localBuilder.appendEncodedPath("proxy");
    return localBuilder.build();
  }

  private Cursor getAccountsCursor(String[] paramArrayOfString)
  {
    Cursor localCursor = getAccountsCursorForMailEngines(paramArrayOfString, MailEngine.getMailEngines(getContext()));
    try
    {
      if (!this.mMailEnginesInitialized)
      {
        initializeMailEngines();
        this.mMailEnginesInitialized = true;
      }
      return localCursor;
    }
    finally
    {
    }
  }

  private Cursor getAccountsCursorForMailEngines(String[] paramArrayOfString, List<MailEngine> paramList)
  {
    String[] arrayOfString = UIProviderValidator.validateAccountProjection(paramArrayOfString);
    Bundle localBundle = new Bundle();
    int i;
    MatrixCursorWithExtra localMatrixCursorWithExtra;
    Iterator localIterator;
    if (this.mAccountsFullyInitialized)
    {
      i = 1;
      localBundle.putInt("accounts_loaded", i);
      localMatrixCursorWithExtra = new MatrixCursorWithExtra(arrayOfString, paramList.size(), localBundle);
      localIterator = paramList.iterator();
    }
    while (true)
    {
      if (!localIterator.hasNext())
        break label149;
      MailEngine localMailEngine = (MailEngine)localIterator.next();
      MatrixCursor.RowBuilder localRowBuilder = localMatrixCursorWithExtra.newRow();
      String str = localMailEngine.getAccountName();
      if (isAccountNameValid(str))
      {
        populateAccountCursorRow(getContext(), str, localMailEngine, arrayOfString, localRowBuilder);
        continue;
        i = 0;
        break;
      }
      LogUtils.e("Gmail", "Invalid MailEngine account name: %s", new Object[] { str });
    }
    label149: return localMatrixCursorWithExtra;
  }

  private static Uri getAccountsUri()
  {
    return Uri.parse("content://com.android.gmail.ui/");
  }

  private static Uri getComposeUri(String paramString)
  {
    return Uri.parse("gmail2from://gmail-ls/account/" + paramString);
  }

  public static Uri getConversationBaseUri(String paramString)
  {
    return Uri.parse(Urls.getUriString(paramString));
  }

  public static Conversation getConversationForConversationCursor(Context paramContext, String paramString, Gmail.ConversationCursor paramConversationCursor)
  {
    long l1 = paramConversationCursor.getConversationId();
    String str1 = paramConversationCursor.getFromSnippetInstructions();
    String str2 = paramConversationCursor.getSnippet();
    int i = paramConversationCursor.getNumMessages();
    com.android.mail.providers.ConversationInfo localConversationInfo = new com.android.mail.providers.ConversationInfo();
    UIConversationCursor.generateConversationInfo(str1, i, str2, localConversationInfo, paramConversationCursor.getLabels().containsKey("^u"));
    Uri localUri1 = getConversationUri(paramString, l1);
    String str3 = paramConversationCursor.getSubject();
    long l2 = paramConversationCursor.getDateMs();
    boolean bool1 = paramConversationCursor.hasAttachments();
    Uri localUri2 = getConversationMessageListUri(paramString, l1);
    String str4 = parseSendersFromSnippetInstructions(str1, paramContext);
    int j = getConversationPriority(paramConversationCursor);
    boolean bool2;
    boolean bool3;
    String str5;
    int k;
    boolean bool4;
    boolean bool5;
    boolean bool6;
    Uri localUri3;
    Uri localUri4;
    if (!paramConversationCursor.getLabels().containsKey("^u"))
    {
      bool2 = true;
      bool3 = paramConversationCursor.getLabels().containsKey("^t");
      str5 = getSerializedFolderString(paramString, paramConversationCursor);
      k = getConversationPersonalLevel(paramConversationCursor);
      bool4 = paramConversationCursor.getLabels().containsKey("^s");
      bool5 = paramConversationCursor.getLabels().containsKey("^p");
      bool6 = paramConversationCursor.getLabels().containsKey("^g");
      localUri3 = getAccountUri(paramString);
      localUri4 = getConversationBaseUri(paramString);
      if (paramConversationCursor.isSynced())
        break label261;
    }
    label261: for (boolean bool7 = true; ; bool7 = false)
    {
      return Conversation.create(l1, localUri1, str3, l2, str2, bool1, localUri2, str4, i, 0, 0, j, bool2, bool3, str5, 0, k, bool4, bool5, bool6, localUri3, localConversationInfo, localUri4, bool7);
      bool2 = false;
      break;
    }
  }

  public static Uri getConversationMessageListUri(String paramString, long paramLong)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/conversationMessages/" + paramLong);
  }

  private static int getConversationPersonalLevel(Gmail.ConversationCursor paramConversationCursor)
  {
    Gmail.PersonalLevel localPersonalLevel = paramConversationCursor.getPersonalLevel();
    if (localPersonalLevel == Gmail.PersonalLevel.NOT_TO_ME);
    do
    {
      return 0;
      if (localPersonalLevel == Gmail.PersonalLevel.ONLY_TO_ME)
        return 2;
    }
    while (localPersonalLevel != Gmail.PersonalLevel.TO_ME_AND_OTHERS);
    return 1;
  }

  private static int getConversationPriority(Gmail.ConversationCursor paramConversationCursor)
  {
    if (paramConversationCursor.getLabels().containsKey("^io_im"))
      return 1;
    return 0;
  }

  private ConversationState getConversationState(String paramString, long paramLong)
  {
    return getOrCreateAccountState(paramString).getOrCreateConversationState(getContext(), paramLong);
  }

  public static Uri getConversationUri(String paramString, long paramLong)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/conversation/" + paramLong);
  }

  private Cursor getConversationsForLabel(String paramString1, Uri paramUri, String paramString2, String[] paramArrayOfString, Integer paramInteger, boolean paramBoolean)
  {
    return getConversationsForQuery(paramString1, paramUri, null, paramString2, paramArrayOfString, paramInteger, paramBoolean);
  }

  private Cursor getConversationsForLabelId(String paramString, Uri paramUri, long paramLong, String[] paramArrayOfString, Integer paramInteger, boolean paramBoolean)
  {
    String str = getOrMakeMailEngine(paramString).getLabelMap().getCanonicalName(paramLong);
    if (str == null)
    {
      LogUtils.e("Gmail", "Unknown canonical name: %s", new Object[] { str });
      return null;
    }
    return getConversationsForLabel(paramString, paramUri, str, paramArrayOfString, paramInteger, paramBoolean);
  }

  private Cursor getConversationsForQuery(String paramString1, Uri paramUri, String paramString2, String paramString3, String[] paramArrayOfString, Integer paramInteger, boolean paramBoolean)
  {
    MailEngine localMailEngine = getOrMakeMailEngine(paramString1);
    String[] arrayOfString1 = UIProviderValidator.validateConversationProjection(paramArrayOfString);
    if (paramBoolean);
    for (String[] arrayOfString2 = null; ; arrayOfString2 = CONVERSATION_QUERY_LOCAL_ONLY_SELECTION_ARGS)
      return getUiConversationCursor(paramString1, localMailEngine, paramUri, arrayOfString1, localMailEngine.getConversationCursorForQuery(null, Utils.makeQueryString(paramString2, paramString3), arrayOfString2, paramInteger, false), paramBoolean);
  }

  private static Uri getDefaultRecentFoldersUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/defaultRecentFolders");
  }

  private static EmailAddress getEmailAddress(String paramString)
  {
    return EmailAddress.getEmailAddress(paramString);
  }

  private Cursor getFakeSearchFolder(Uri paramUri, String paramString1, String paramString2)
  {
    AccountState localAccountState = getOrCreateAccountState(paramString1);
    Uri.Builder localBuilder = getAccountConversationSearchUri(paramString1).buildUpon();
    localBuilder.appendQueryParameter("query", paramString2);
    Uri localUri = localBuilder.build();
    MatrixCursor localMatrixCursor = new MatrixCursor(UIProvider.FOLDERS_PROJECTION, 1);
    MatrixCursor.RowBuilder localRowBuilder = localMatrixCursor.newRow();
    String[] arrayOfString = UIProvider.FOLDERS_PROJECTION;
    int i = arrayOfString.length;
    int j = 0;
    if (j < i)
    {
      String str = arrayOfString[j];
      if (TextUtils.equals(str, "_id"))
        localRowBuilder.add(Integer.valueOf(SEARCH_HASHCODE));
      while (true)
      {
        j++;
        break;
        if (TextUtils.equals(str, "folderUri"))
          localRowBuilder.add(paramUri);
        else if (TextUtils.equals(str, "name"))
          localRowBuilder.add("search");
        else if (TextUtils.equals(str, "unreadCount"))
          localRowBuilder.add(Integer.valueOf(0));
        else if (TextUtils.equals(str, "totalCount"))
          localRowBuilder.add(Integer.valueOf(localAccountState.getNumSearchResults(paramString2)));
        else if (TextUtils.equals(str, UIProvider.FolderColumns.HAS_CHILDREN))
          localRowBuilder.add(Integer.valueOf(0));
        else if (TextUtils.equals(str, UIProvider.FolderColumns.CAPABILITIES))
          localRowBuilder.add(Integer.valueOf(608));
        else if (TextUtils.equals(str, UIProvider.FolderColumns.SYNC_WINDOW))
          localRowBuilder.add(Integer.valueOf(0));
        else if (TextUtils.equals(str, "conversationListUri"))
          localRowBuilder.add(localUri);
        else if (TextUtils.equals(str, "childFoldersListUri"))
          localRowBuilder.add(null);
        else if (TextUtils.equals(str, "refreshUri"))
          localRowBuilder.add(localUri);
        else if (TextUtils.equals(str, "syncStatus"))
          localRowBuilder.add(Integer.valueOf(0));
        else if (TextUtils.equals(str, "lastSyncResult"))
          localRowBuilder.add(Integer.valueOf(0));
        else if (TextUtils.equals(str, "type"))
          localRowBuilder.add(Integer.valueOf(0));
        else if (TextUtils.equals(str, "iconResId"))
          localRowBuilder.add(Integer.valueOf(0));
        else if (TextUtils.equals(str, "bgColor"))
          localRowBuilder.add(null);
        else if (TextUtils.equals(str, "fgColor"))
          localRowBuilder.add(null);
        else if (TextUtils.equals(str, "loadMoreUri"))
          localRowBuilder.add(null);
        else if (TextUtils.equals(str, "hierarchicalDesc"))
          localRowBuilder.add("search");
        else
          LogUtils.wtf("Gmail", "unexpected column: %s", new Object[] { str });
      }
    }
    return localMatrixCursor;
  }

  public static int getFolderType(boolean paramBoolean, String paramString)
  {
    int i;
    if ("^t".equals(paramString))
      i = 7;
    boolean bool;
    do
    {
      do
      {
        return i;
        i = 0;
      }
      while (!paramBoolean);
      if (("^i".equals(paramString)) || ("^iim".equals(paramString)))
        return 1;
      if ("^r".equals(paramString))
        return 2;
      if ("^^out".equals(paramString))
        return 3;
      if ("^f".equals(paramString))
        return 4;
      if ("^k".equals(paramString))
        return 5;
      if ("^s".equals(paramString))
        return 6;
      if ("^all".equals(paramString))
        return 9;
      bool = "^im".equals(paramString);
      i = 0;
    }
    while (!bool);
    return 8;
  }

  private static Uri getHelpUri(Context paramContext)
  {
    return Uri.parse(Gservices.getString(paramContext.getContentResolver(), "gmail_context_sensitive_help_url", "http://support.google.com/mobile/?hl=%locale%"));
  }

  public static Uri getLabelConversationListFromNameUri(String paramString1, String paramString2)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString1 + "/conversationsForLabel/" + Uri.encode(paramString2));
  }

  public static Uri getLabelConversationListUri(String paramString, int paramInt)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/conversations/" + paramInt);
  }

  private static final Pair<Long, CharSequence> getLabelFromCanonical(Context paramContext, Gmail.LabelMap paramLabelMap, String paramString1, String paramString2, boolean paramBoolean)
  {
    Object localObject = null;
    if (paramBoolean)
      localObject = getSystemLabelName(paramContext, paramString2);
    if (paramLabelMap != null)
      try
      {
        long l2 = paramLabelMap.getLabelId(paramString2);
        if (localObject == null)
          localObject = paramLabelMap.getName(l2);
        Pair localPair = Pair.create(Long.valueOf(l2), localObject);
        return localPair;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
      }
    Label localLabel = LabelManager.getLabel(paramContext, paramString1, paramString2);
    if (localLabel != null)
    {
      long l1 = localLabel.getId();
      if (localObject == null)
        localObject = localLabel.getName();
      return Pair.create(Long.valueOf(l1), localObject);
    }
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = LogUtils.sanitizeLabelName(paramString2);
    LogUtils.e("Gmail", "Couldn't find label: %s", arrayOfObject);
    return Pair.create(Long.valueOf(-1L), localObject);
  }

  public static Uri getLabelRefreshUri(String paramString, Uri paramUri)
  {
    Uri.Builder localBuilder = getAccountRefreshUri(paramString).buildUpon();
    localBuilder.appendPath(paramUri.toString());
    return localBuilder.build();
  }

  public static Uri getMessageAttachmentUri(String paramString1, long paramLong1, long paramLong2, long paramLong3, String paramString2, String paramString3, String paramString4)
  {
    Uri.Builder localBuilder = Uri.parse("content://com.android.gmail.ui/" + paramString1 + "/messageAttachment/" + paramLong1 + "/" + paramLong3 + "/" + paramString2).buildUpon();
    localBuilder.appendQueryParameter("serverMessageId", Long.toString(paramLong2));
    if (!TextUtils.isEmpty(paramString3))
      localBuilder.appendQueryParameter("mimeType", paramString3);
    if (!TextUtils.isEmpty(paramString4))
      localBuilder.appendQueryParameter("gmailJoinedAttachment", paramString4);
    return localBuilder.build();
  }

  private Cursor getMessageAttachments(UIAttachment.UriParser paramUriParser, String[] paramArrayOfString)
  {
    long l1 = paramUriParser.mConversationId;
    String str1 = paramUriParser.mAccount;
    String str2 = paramUriParser.mPartId;
    long l2 = paramUriParser.mLocalMessageId;
    long l3 = paramUriParser.mMessageId;
    List localList1 = paramUriParser.mContentTypeQueryParameters;
    String[] arrayOfString = UIProviderValidator.validateAttachmentProjection(paramArrayOfString);
    ConversationState localConversationState = getConversationState(str1, l1);
    getOrMakeMailEngine(str1);
    MessageState localMessageState = localConversationState.getOrCreateMessageState(paramUriParser);
    Object localObject;
    UIAttachment localUIAttachment2;
    if (localMessageState != null)
    {
      localObject = localMessageState.getMessageAttachments();
      if ((localObject != null) && (((List)localObject).size() > 0) && (!TextUtils.isEmpty(str2)))
      {
        LogUtils.d("Gmail", "Looking for attachment partId: %s", new Object[] { str2 });
        Iterator localIterator3 = ((List)localObject).iterator();
        while (localIterator3.hasNext())
        {
          localUIAttachment2 = (UIAttachment)localIterator3.next();
          if (str2.equalsIgnoreCase(localUIAttachment2.getPartId()))
            LogUtils.d("Gmail", "Found attachment", new Object[0]);
        }
      }
    }
    while (true)
    {
      if (localUIAttachment2 != null);
      ArrayList localArrayList;
      for (List localList2 = Collections.singletonList(localUIAttachment2); ; localList2 = Collections.emptyList())
      {
        localObject = localList2;
        if ((localObject == null) || (((List)localObject).size() <= 0) || (localList1 == null) || (localList1.isEmpty()))
          break label367;
        localArrayList = new ArrayList();
        Iterator localIterator1 = ((List)localObject).iterator();
        while (true)
        {
          if (!localIterator1.hasNext())
            break label363;
          UIAttachment localUIAttachment1 = (UIAttachment)localIterator1.next();
          Iterator localIterator2 = localList1.iterator();
          if (localIterator2.hasNext())
          {
            String str3 = (String)localIterator2.next();
            String str4 = localUIAttachment1.getContentType();
            if ((str4 == null) || (!str4.startsWith(str3)))
              break;
            localArrayList.add(localUIAttachment1);
          }
        }
        LogUtils.e("Gmail", "Couldn't find Message State, returning empty attachment list", new Object[0]);
        localObject = Collections.emptyList();
        break;
      }
      label363: localObject = localArrayList;
      label367: return getUiAttachmentsCursorForUIAttachments(str1, l1, l3, l2, arrayOfString, (List)localObject);
      localUIAttachment2 = null;
    }
  }

  public static Uri getMessageAttachmentsUri(String paramString, long paramLong1, long paramLong2, long paramLong3)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/messageAttachments/" + paramLong1 + "/" + paramLong3).buildUpon().appendQueryParameter("serverMessageId", Long.toString(paramLong2)).build();
  }

  public static Uri getMessageByIdUri(String paramString, long paramLong)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/message/" + paramLong);
  }

  private Cursor getMessageForId(String paramString, long paramLong)
  {
    MailEngine localMailEngine = getOrMakeMailEngine(paramString);
    Persistence localPersistence = Persistence.getInstance();
    Cursor localCursor = localMailEngine.getMessageCursorForLocalMessageId(Gmail.MESSAGE_PROJECTION, paramLong);
    if (localCursor == null)
      return null;
    return new UIMessageCursor(getContext(), localCursor, paramString, localPersistence, sGmailQuote, UIProvider.MESSAGE_PROJECTION);
  }

  private Cursor getMessagesForConversation(String paramString, long paramLong, String[] paramArrayOfString, boolean paramBoolean)
  {
    MailEngine localMailEngine = getOrMakeMailEngine(paramString);
    if (!paramBoolean);
    Persistence localPersistence;
    String[] arrayOfString;
    Cursor localCursor;
    for (boolean bool = true; ; bool = false)
    {
      localPersistence = Persistence.getInstance();
      arrayOfString = UIProviderValidator.validateMessageProjection(paramArrayOfString);
      localCursor = localMailEngine.getMessageCursorForConversationId(Gmail.MESSAGE_PROJECTION, paramLong, bool, false);
      if (localCursor != null)
        break;
      return null;
    }
    UIMessageCursor localUIMessageCursor = new UIMessageCursor(getContext(), localCursor, paramString, localPersistence, sGmailQuote, arrayOfString);
    localUIMessageCursor.setNotificationUri(this.mContentResolver, getConversationMessageListUri(paramString, paramLong));
    return localUIMessageCursor;
  }

  private AccountState getOrCreateAccountState(String paramString)
  {
    synchronized (sAccountStateMap)
    {
      if (sAccountStateMap.containsKey(paramString))
      {
        localAccountState = (AccountState)sAccountStateMap.get(paramString);
        return localAccountState;
      }
      MailEngine localMailEngine = getOrMakeMailEngine(paramString);
      AccountState localAccountState = new AccountState(paramString, this.mUiProviderHandler, localMailEngine);
      sAccountStateMap.put(paramString, localAccountState);
    }
  }

  private MailEngine getOrMakeMailEngine(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    return MailEngine.getOrMakeMailEngine(getContext(), paramString);
  }

  private static Map<String, Boolean> getRawOperations(com.google.android.gm.ConversationInfo paramConversationInfo, Label paramLabel, boolean paramBoolean)
  {
    HashMap localHashMap = Maps.newHashMap();
    String str = paramLabel.getCanonicalName();
    localHashMap.put(str, Boolean.valueOf(paramBoolean));
    boolean bool1;
    int i;
    label182: int j;
    if (("^k".equals(str)) || ("^g".equals(str)))
    {
      if (!paramBoolean)
      {
        bool1 = true;
        localHashMap.put("^i", Boolean.valueOf(bool1));
      }
    }
    else
    {
      if (("^p".equals(str)) && (paramBoolean))
      {
        localHashMap.put("^i", Boolean.FALSE);
        localHashMap.put("^s", Boolean.TRUE);
      }
      if (("^i".equals(str)) && (paramBoolean))
      {
        localHashMap.put("^s", Boolean.FALSE);
        localHashMap.put("^k", Boolean.FALSE);
      }
      if (((!"^^important".equals(str)) || (!paramBoolean)) && ((!"^^unimportant".equals(str)) || (paramBoolean)))
        break label378;
      i = 1;
      if ((!"^^unimportant".equals(str)) || (!paramBoolean))
      {
        boolean bool2 = "^^important".equals(str);
        j = 0;
        if (bool2)
        {
          j = 0;
          if (paramBoolean);
        }
      }
      else
      {
        j = 1;
      }
      if (i == 0)
        break label384;
      localHashMap.put("^im", Boolean.TRUE);
      localHashMap.put("^io_im", Boolean.TRUE);
      localHashMap.put("^imi", Boolean.TRUE);
      localHashMap.put("^imn", Boolean.FALSE);
      localHashMap.put("^io_ns", Boolean.FALSE);
      if (paramConversationInfo.getLabels().containsKey("^i"))
        localHashMap.put("^iim", Boolean.TRUE);
    }
    while (true)
    {
      if ((localHashMap.containsKey("^i")) && (paramConversationInfo.isImportant()))
        localHashMap.put("^iim", Boolean.valueOf(((Boolean)localHashMap.get("^i")).booleanValue()));
      return localHashMap;
      bool1 = false;
      break;
      label378: i = 0;
      break label182;
      label384: if (j != 0)
      {
        localHashMap.put("^imn", Boolean.TRUE);
        localHashMap.put("^im", Boolean.FALSE);
        localHashMap.put("^io_im", Boolean.FALSE);
        localHashMap.put("^iim", Boolean.FALSE);
        localHashMap.put("^imi", Boolean.FALSE);
        localHashMap.put("^io_ns", Boolean.FALSE);
      }
    }
  }

  private static Uri getReauthenticateUri(String paramString)
  {
    return BASE_AUTH_URI.buildUpon().appendQueryParameter("account", paramString).build();
  }

  private static Uri getRecentFoldersUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/recentFolders");
  }

  private Cursor getRecentLabelsCursor(String paramString, String[] paramArrayOfString)
  {
    MailEngine localMailEngine = getOrMakeMailEngine(paramString);
    String[] arrayOfString = UIProviderValidator.validateFolderProjection(paramArrayOfString);
    LabelQueryBuilder localLabelQueryBuilder = localMailEngine.getLabelQueryBuilder(Gmail.LABEL_PROJECTION).showHidden(false);
    localLabelQueryBuilder.setRecent(System.currentTimeMillis(), 10);
    return getUiLabelCursor(localMailEngine, paramString, arrayOfString, localLabelQueryBuilder.query());
  }

  public static Uri getSaveMessageUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/message/save");
  }

  public static Uri getSendMessageUri(String paramString)
  {
    return Uri.parse("content://com.android.gmail.ui/" + paramString + "/message/send");
  }

  private static String getSerializedFolderString(String paramString, Gmail.ConversationCursor paramConversationCursor)
  {
    return getSerializedFolderString(paramString, paramConversationCursor.getLabels(), null, null);
  }

  public static String getSerializedFolderString(String paramString, Map<String, Label> paramMap, SparseArray<String> paramSparseArray, Map<String, String[]> paramMap1)
  {
    new ArrayList(paramMap.size());
    StringBuilder localStringBuilder = new StringBuilder(250);
    Set localSet = paramMap.entrySet();
    HashSet localHashSet = new HashSet();
    Iterator localIterator1 = localSet.iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      String str4 = ((Label)localEntry.getValue()).getCanonicalName();
      if ((LongShadowUtils.isUserLabel(str4)) || (Gmail.isDisplayableSystemLabel(str4)))
        localHashSet.add(localEntry);
    }
    Object localObject1 = null;
    int i = localHashSet.size();
    Iterator localIterator2 = localHashSet.iterator();
    int j = 0;
    Label localLabel;
    String str1;
    if (localIterator2.hasNext())
    {
      localLabel = (Label)((Map.Entry)localIterator2.next()).getValue();
      str1 = localLabel.getCanonicalName();
      if (paramSparseArray == null)
        break label454;
    }
    label454: for (Object localObject2 = (String)paramSparseArray.get((int)localLabel.getId()); ; localObject2 = localObject1)
    {
      String str2;
      String str3;
      if (localObject2 == null)
      {
        if ((paramMap1 == null) || (!paramMap1.containsKey(str1)))
          break label366;
        String[] arrayOfString = (String[])paramMap1.get(str1);
        str2 = arrayOfString[0];
        str3 = arrayOfString[1];
      }
      while (true)
      {
        localObject2 = Folder.createAsString((int)localLabel.getId(), getAccountLabelUri(paramString, localLabel.getCanonicalName()), localLabel.getName(), false, 0, 0, getLabelConversationListUri(paramString, (int)localLabel.getId()), null, 0, 0, null, 0, 0, getFolderType(localLabel.isSystemLabel(), localLabel.getCanonicalName()), -1L, str2, str3, null, null, null);
        if (paramSparseArray != null)
          paramSparseArray.put((int)localLabel.getId(), localObject2);
        localStringBuilder.append((String)localObject2);
        int k = i - 1;
        if (j < k)
          localStringBuilder.append("^**^");
        j++;
        localObject1 = localObject2;
        break;
        label366: str2 = localLabel.getBackgroundColor() + "";
        str3 = localLabel.getTextColor() + "";
        if (paramMap1 != null)
          paramMap1.put(str1, new String[] { str2, str3 });
      }
      return localStringBuilder.toString();
    }
  }

  public static Folder getSparseFolderObject(Context paramContext, MailEngine paramMailEngine, String paramString1, String paramString2)
  {
    Cursor localCursor = null;
    MatrixCursor localMatrixCursor = new MatrixCursor(Gmail.LABEL_PROJECTION, 1);
    Gmail.LabelMap localLabelMap;
    boolean bool;
    Pair localPair;
    MatrixCursor.RowBuilder localRowBuilder;
    int j;
    label64: String str;
    if (paramMailEngine != null)
    {
      localLabelMap = paramMailEngine.getLabelMap();
      bool = Gmail.isSystemLabel(paramString2);
      localPair = getLabelFromCanonical(paramContext, localLabelMap, paramString1, paramString2, bool);
      localRowBuilder = localMatrixCursor.newRow();
      String[] arrayOfString = Gmail.LABEL_PROJECTION;
      int i = arrayOfString.length;
      j = 0;
      if (j >= i)
        break label230;
      str = arrayOfString[j];
      if (!"_id".equals(str))
        break label112;
      localRowBuilder.add(localPair.first);
    }
    while (true)
    {
      j++;
      break label64;
      localLabelMap = null;
      break;
      label112: if ("canonicalName".equals(str))
      {
        localRowBuilder.add(paramString2);
      }
      else
      {
        if ("systemLabel".equals(str))
        {
          if (bool);
          for (int k = 1; ; k = 0)
          {
            localRowBuilder.add(Integer.valueOf(k));
            break;
          }
        }
        if ("name".equals(str))
          localRowBuilder.add(localPair.second);
        else if ("color".equals(str))
          localRowBuilder.add("2147483647");
        else
          localRowBuilder.add(null);
      }
    }
    try
    {
      label230: localCursor = getUiLabelCursor(paramMailEngine, paramString1, UIProvider.FOLDERS_PROJECTION, localMatrixCursor);
      localCursor.moveToFirst();
      Folder localFolder = new Folder(localCursor);
      return localFolder;
    }
    finally
    {
      if (localCursor != null)
        localCursor.close();
    }
  }

  private static CharSequence getSystemLabelName(Context paramContext, String paramString)
  {
    if (!Gmail.isSystemLabel(paramString))
      return null;
    try
    {
      if (sSystemLabelNameMap == null)
        sSystemLabelNameMap = Label.getSystemLabelNameMap(paramContext);
      CharSequence localCharSequence = (CharSequence)sSystemLabelNameMap.get(paramString);
      return localCharSequence;
    }
    finally
    {
    }
  }

  private Cursor getUiAttachmentsCursorForUIAttachments(String paramString, long paramLong1, long paramLong2, long paramLong3, String[] paramArrayOfString, List<UIAttachment> paramList)
  {
    AttachmentCursor localAttachmentCursor = createAttachmentCursor(paramString, paramLong1, paramLong2, paramArrayOfString, paramList.size());
    Iterator localIterator = paramList.iterator();
    if (localIterator.hasNext())
    {
      UIAttachment localUIAttachment = (UIAttachment)localIterator.next();
      LogUtils.d("Gmail", "adding attachment to cursor %s", new Object[] { localUIAttachment });
      MatrixCursor.RowBuilder localRowBuilder = localAttachmentCursor.newRow();
      String str1 = localUIAttachment.getPartId();
      String str2 = localUIAttachment.getContentType();
      Uri localUri1 = getMessageAttachmentUri(paramString, paramLong1, paramLong2, paramLong3, str1, str2, localUIAttachment.getJoinedAttachmentInfo());
      int i = paramArrayOfString.length;
      int j = 0;
      label124: String str3;
      if (j < i)
      {
        str3 = paramArrayOfString[j];
        if (!TextUtils.equals(str3, "uri"))
          break label163;
        localRowBuilder.add(localUri1);
      }
      while (true)
      {
        j++;
        break label124;
        break;
        label163: if (TextUtils.equals(str3, "_display_name"))
        {
          localRowBuilder.add(localUIAttachment.getName());
        }
        else if (TextUtils.equals(str3, "_size"))
        {
          localRowBuilder.add(Integer.valueOf(localUIAttachment.getSize()));
        }
        else if (TextUtils.equals(str3, "contentType"))
        {
          localRowBuilder.add(str2);
        }
        else if (TextUtils.equals(str3, "state"))
        {
          localRowBuilder.add(Integer.valueOf(localUIAttachment.getStatus()));
        }
        else if (TextUtils.equals(str3, "destination"))
        {
          localRowBuilder.add(Integer.valueOf(localUIAttachment.getDestination()));
        }
        else if (TextUtils.equals(str3, "downloadedSize"))
        {
          localRowBuilder.add(Integer.valueOf(localUIAttachment.getDownloadedSize()));
        }
        else
        {
          if (TextUtils.equals(str3, "contentUri"))
          {
            if (localUIAttachment.getDestination() == 1);
            for (Uri localUri2 = localUIAttachment.getExternalFilePath(); ; localUri2 = Gmail.getAttachmentUri(paramString, paramLong3, localUIAttachment, Gmail.AttachmentRendition.BEST, false))
            {
              localRowBuilder.add(localUri2);
              break;
            }
          }
          if (TextUtils.equals(str3, "thumbnailUri"))
            localRowBuilder.add(Gmail.getAttachmentUri(paramString, paramLong3, localUIAttachment, Gmail.AttachmentRendition.SIMPLE, false));
          else if (TextUtils.equals(str3, "previewIntentUri"))
            if (MimeType.isPreviewable(getContext(), str2))
              localRowBuilder.add(BASE_GVIEW_URI.buildUpon().appendQueryParameter("account", paramString).appendQueryParameter("serverMessageId", Long.toHexString(paramLong2)).appendQueryParameter("attId", str1).appendQueryParameter("mimeType", str2).build().toString());
            else
              localRowBuilder.add(null);
        }
      }
    }
    return localAttachmentCursor;
  }

  private Cursor getUiConversationCursor(String paramString, MailEngine paramMailEngine, Uri paramUri, String[] paramArrayOfString, Cursor paramCursor, boolean paramBoolean)
  {
    Object localObject;
    if (paramCursor == null)
      localObject = null;
    do
    {
      return localObject;
      localObject = new UIConversationCursor(paramCursor, paramString, paramArrayOfString);
    }
    while ((paramUri == null) || (!paramBoolean));
    getOrCreateAccountState(paramString).cacheConversationCursor(paramUri, (UIConversationCursor)localObject);
    return localObject;
  }

  private static Cursor getUiLabelCursor(MailEngine paramMailEngine, String paramString, String[] paramArrayOfString, Cursor paramCursor)
  {
    if (paramCursor == null)
      return null;
    return new UILabelCursor(paramCursor, paramMailEngine, paramString, paramArrayOfString);
  }

  private static Uri getUpdateSettingsUri(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("content://com.android.gmail.ui").buildUpon();
    localBuilder.appendEncodedPath(paramString).appendEncodedPath("settings");
    return localBuilder.build();
  }

  private void initializeMailEngines()
  {
    final Context localContext = getContext();
    new AccountHelper(localContext).asyncGetAccountsInfo(new AccountHelper.AccountResultsCallback()
    {
      public void exec(android.accounts.Account[] paramAnonymousArrayOfAccount)
      {
        int i;
        int j;
        if ((paramAnonymousArrayOfAccount != null) && (paramAnonymousArrayOfAccount.length > 0))
        {
          i = paramAnonymousArrayOfAccount.length;
          j = 0;
        }
        while (j < i)
        {
          android.accounts.Account localAccount = paramAnonymousArrayOfAccount[j];
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = localAccount.name;
          LogUtils.d("Gmail", "Getting MailEngine for account: %s", arrayOfObject);
          UiProvider.this.mAccountsPendingInitialization.add(localAccount.name);
          MailEngine.getOrMakeMailEngineAsync(localContext, localAccount.name, UiProvider.this);
          j++;
          continue;
          UiProvider.this.updateAccountsIntializedStatus();
        }
      }
    });
  }

  private void intializeLoaderHandler()
  {
    HandlerThread localHandlerThread = new HandlerThread("UIProvider Thread", 10);
    localHandlerThread.start();
    this.mUiProviderHandler = new Handler(localHandlerThread.getLooper());
  }

  private static boolean isAccountNameValid(String paramString)
  {
    return (!TextUtils.isEmpty(paramString)) && (!INVALID_ACCOUNT_NAMES.contains(paramString.toLowerCase()));
  }

  private static final boolean isUserSettableLabel(String paramString)
  {
    return (Gmail.isLabelUserSettable(paramString)) && (!INVARIANT_LABELS.contains(paramString));
  }

  static void notifyAccountChanged(Context paramContext, String paramString)
  {
    paramContext.getContentResolver().notifyChange(getAccountUri(paramString), null, false);
    notifyAccountListChanged(paramContext);
  }

  public static void notifyAccountListChanged(Context paramContext)
  {
    paramContext.getContentResolver().notifyChange(ACCOUNTS_URI, null, false);
  }

  static void notifyAttachmentChanged(String paramString, long paramLong)
  {
    synchronized (sAccountStateMap)
    {
      AccountState localAccountState = (AccountState)sAccountStateMap.get(paramString);
      if (localAccountState != null)
        localAccountState.notifyAttachmentChange(paramLong);
      return;
    }
  }

  public static void notifyMessageAttachmentsChanged(Context paramContext, String paramString, long paramLong1, long paramLong2, long paramLong3, Set<String> paramSet)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    localContentResolver.notifyChange(getMessageAttachmentsUri(paramString, paramLong1, paramLong2, paramLong3), null, false);
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
      localContentResolver.notifyChange(getMessageAttachmentUri(paramString, paramLong1, paramLong2, paramLong3, (String)localIterator.next(), null, null), null, false);
  }

  static void onAttachmentDownloadFinished(String paramString1, long paramLong1, long paramLong2, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3)
  {
    UiProvider localUiProvider = sInstance;
    if (localUiProvider != null)
      localUiProvider.onAttachmentDownloadFinishedImpl(paramString1, paramLong1, paramLong2, paramString2, paramInt1, paramInt2, paramInt3, paramString3);
  }

  private void onAttachmentDownloadFinishedImpl(String paramString1, long paramLong1, long paramLong2, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3)
  {
    MessageState localMessageState = getConversationState(paramString1, paramLong1).getOrCreateMessageState(paramLong2);
    if (localMessageState == null)
    {
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Long.valueOf(paramLong2);
      LogUtils.e("Gmail", "couldn't find message %d in update AttachmentState", arrayOfObject3);
      return;
    }
    UIAttachment localUIAttachment = localMessageState.getMessageAttachment(paramString2);
    if (localUIAttachment == null)
    {
      List localList = localMessageState.getMessageAttachments();
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = Long.valueOf(paramLong2);
      arrayOfObject2[1] = paramString2;
      arrayOfObject2[2] = Arrays.toString(localList.toArray(new UIAttachment[localList.size()]));
      LogUtils.e("Gmail", "couldn't find attachment %d %s in update AttachmentState.  attachments: %s", arrayOfObject2);
      return;
    }
    if ((paramInt3 == 404) && (localUIAttachment.downloadCompletedSuccessfully()))
    {
      LogUtils.e("Gmail", "Attempt to make successful download a failure", new Object[0]);
      return;
    }
    Object[] arrayOfObject1 = new Object[3];
    arrayOfObject1[0] = Integer.valueOf(paramInt2);
    arrayOfObject1[1] = Integer.valueOf(paramInt1);
    arrayOfObject1[2] = paramString3;
    LogUtils.d("Gmail", "Updating attachment state %d/%d/%s", arrayOfObject1);
    localUIAttachment.updateState(paramInt2, paramInt1, paramString3);
  }

  public static void onConversationChanged(Context paramContext, String paramString, long paramLong)
  {
    paramContext.getContentResolver().notifyChange(getConversationMessageListUri(paramString, paramLong), null, false);
  }

  static String parseSendersFromSnippetInstructions(String paramString, Context paramContext)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    TextUtils.SimpleStringSplitter localSimpleStringSplitter = new TextUtils.SimpleStringSplitter('\n');
    localSimpleStringSplitter.setString(paramString);
    Object localObject1 = new String[8];
    int i = localObject1.length;
    String str1;
    int j;
    if (paramContext != null)
    {
      str1 = paramContext.getString(2131427483);
      j = 0;
    }
    while (true)
    {
      int i2;
      if (localSimpleStringSplitter.hasNext())
      {
        i2 = j + 1;
        localObject1[j] = localSimpleStringSplitter.next();
        if (i2 != i)
          break label367;
        String[] arrayOfString = new String[i * 2];
        System.arraycopy(localObject1, 0, arrayOfString, 0, i);
        i *= 2;
        localObject1 = arrayOfString;
        j = i2;
        continue;
        str1 = "me";
        break;
      }
      int k = 0;
      if (k < j)
      {
        int m = k + 1;
        Object localObject2 = localObject1[k];
        if ("".equals(localObject2));
        while (true)
        {
          k = m;
          break;
          if (!"e".equals(localObject2))
            if ("n".equals(localObject2))
            {
              m++;
            }
            else if ("d".equals(localObject2))
            {
              m++;
            }
            else if ((!"s".equals(localObject2)) && (!"f".equals(localObject2)))
            {
              int n = m + 1;
              localObject1[m];
              int i1 = n + 1;
              String str3 = localObject1[n];
              if (str3.length() == 0)
                str3 = str1.toString();
              localSpannableStringBuilder.append(str3 + ", ");
              m = i1;
            }
        }
      }
      String str2 = localSpannableStringBuilder.toString().trim();
      if ((!TextUtils.isEmpty(str2)) && (str2.length() > 1) && (str2.charAt(-1 + str2.length()) == ','))
        str2 = str2.substring(0, -1 + str2.length());
      return str2;
      label367: j = i2;
    }
  }

  private Cursor performRefresh(String paramString1, String paramString2)
  {
    MailEngine localMailEngine = getOrMakeMailEngine(paramString1);
    Cursor localCursor;
    if (paramString2 != null)
    {
      UIConversationCursor localUIConversationCursor = getOrCreateAccountState(paramString1).getConversationCursor(Uri.parse(paramString2));
      if (localUIConversationCursor != null)
        localCursor = localUIConversationCursor.getWrappedCursor();
    }
    while (true)
    {
      localMailEngine.performRefresh(localCursor);
      return null;
      localCursor = null;
      continue;
      localCursor = null;
    }
  }

  private Cursor performUndo(Uri paramUri, String[] paramArrayOfString)
  {
    ImmutableList localImmutableList;
    HashSet localHashSet;
    synchronized (this.mPreviousOperationUndoOps)
    {
      localImmutableList = ImmutableList.copyOf(this.mPreviousOperationUndoOps);
      this.mPreviousOperationUndoOps.clear();
      localHashSet = Sets.newHashSet();
      if (localImmutableList.isEmpty())
        break label181;
      Iterator localIterator1 = localImmutableList.iterator();
      if (localIterator1.hasNext())
      {
        ConversationLabelOperation localConversationLabelOperation = (ConversationLabelOperation)localIterator1.next();
        localHashSet.add(localConversationLabelOperation.mAccount);
        localConversationLabelOperation.performOperation();
      }
    }
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(localImmutableList.size());
    LogUtils.w("Gmail", "Performed undo on %d operations", arrayOfObject);
    Iterator localIterator2 = localHashSet.iterator();
    while (localIterator2.hasNext())
    {
      String str = (String)localIterator2.next();
      this.mContentResolver.notifyChange(Gmail.getBaseUri(str), null, false);
      continue;
      label181: LogUtils.e("Gmail", "Requested to perform an undo when with no saved undo operations", new Object[0]);
    }
    return new MatrixCursor(paramArrayOfString, 0);
  }

  private static void populateAccountCursorRow(Context paramContext, String paramString, MailEngine paramMailEngine, String[] paramArrayOfString, MatrixCursor.RowBuilder paramRowBuilder)
  {
    android.accounts.Account localAccount = new android.accounts.Account(paramString, "com.google");
    Persistence localPersistence = Persistence.getInstance();
    int i = paramArrayOfString.length;
    int j = 0;
    label639: int i9;
    if (j < i)
    {
      int k = UIProvider.getAccountColumn(paramArrayOfString[j]);
      switch (k)
      {
      default:
        throw new IllegalStateException("Unexpected column: " + k);
      case 0:
        paramRowBuilder.add(Integer.valueOf(localAccount.hashCode()));
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 22:
      case 23:
      case 24:
      case 25:
      case 21:
      case 26:
        while (true)
        {
          j++;
          break;
          paramRowBuilder.add(paramString);
          continue;
          paramRowBuilder.add(Integer.valueOf(0));
          continue;
          paramRowBuilder.add(getAccountUri(paramString));
          continue;
          paramRowBuilder.add(GMAIL_CAPABILITIES);
          continue;
          paramRowBuilder.add(getAccountFoldersUri(paramString));
          continue;
          paramRowBuilder.add(null);
          continue;
          paramRowBuilder.add(getAccountSearchUri(paramString));
          continue;
          List localList = CustomFromUtils.getCustomReplyFroms(getAccountUri(paramString));
          if (localList != null)
          {
            JSONArray localJSONArray = new JSONArray();
            Iterator localIterator = localList.iterator();
            while (localIterator.hasNext())
              localJSONArray.put(((ReplyFromAccount)localIterator.next()).serialize());
            paramRowBuilder.add(localJSONArray.toString());
          }
          else
          {
            paramRowBuilder.add(null);
            continue;
            paramRowBuilder.add(getAccountSaveDraftUri(paramString));
            continue;
            paramRowBuilder.add(getAccountSendMessageUri(paramString));
            continue;
            paramRowBuilder.add(getAccountExpungeMessageUri(paramString));
            continue;
            paramRowBuilder.add(getAccountUndoUri(paramString));
            continue;
            paramRowBuilder.add(getAccountSettingUri(paramString));
            continue;
            int i12 = 0;
            if (paramMailEngine != null)
            {
              boolean bool = paramMailEngine.isBackgroundSyncInProgress();
              i12 = 0;
              if (bool)
                i12 = 4;
              if (paramMailEngine.isLiveQueryInProgress())
                i12 |= 2;
              if (paramMailEngine.isHandlingUserRefresh())
                i12 |= 1;
              if ((paramMailEngine.labelsSynced()) && (requiredLabelsPresent(paramMailEngine)))
                break label639;
            }
            for (int i13 = 1; ; i13 = 0)
            {
              if (i13 != 0)
                i12 |= 8;
              if (!paramMailEngine.labelsSynchronizationStateInitialized())
                i12 |= 32;
              if (!paramMailEngine.automaticSyncEnabled())
                i12 |= 16;
              paramRowBuilder.add(Integer.valueOf(i12));
              break;
            }
            paramRowBuilder.add(getHelpUri(paramContext));
            continue;
            paramRowBuilder.add(getAboutUri(paramString));
            continue;
            paramRowBuilder.add(getReauthenticateUri(paramString));
            continue;
            paramRowBuilder.add(getComposeUri(paramString));
            continue;
            paramRowBuilder.add("application/gmail-ls");
            continue;
            paramRowBuilder.add(getRecentFoldersUri(paramString));
            continue;
            paramRowBuilder.add(getDefaultRecentFoldersUri(paramString));
            continue;
            paramRowBuilder.add(getAccountRefreshUri(paramString));
            continue;
            paramRowBuilder.add(getAccountViewIntentProxyUri(paramString));
            continue;
            paramRowBuilder.add(getAccountCookieQueryUri(paramString));
            continue;
            paramRowBuilder.add(Integer.valueOf(0));
            continue;
            String str4 = localPersistence.getSignature(paramContext, paramString);
            if (TextUtils.isEmpty(str4))
              str4 = null;
            paramRowBuilder.add(str4);
          }
        }
      case 27:
        int i11;
        if (localPersistence.getHasUserSetAutoAdvanceSetting(paramContext))
          if (localPersistence.getAutoAdvanceModeNewer(paramContext))
            i11 = 2;
        while (true)
        {
          paramRowBuilder.add(Integer.valueOf(i11));
          break;
          if (localPersistence.getAutoAdvanceModeOlder(paramContext))
          {
            i11 = 1;
          }
          else
          {
            i11 = 3;
            continue;
            i11 = 0;
          }
        }
      case 28:
        String str3 = localPersistence.getMessageTextSize(paramContext);
        String[] arrayOfString2 = paramContext.getResources().getStringArray(2131558417);
        i9 = 0;
        label903: if (i9 < arrayOfString2.length)
          if (!TextUtils.equals(str3, arrayOfString2[i9]))
            break;
        break;
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 29:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      }
    }
    while (true)
    {
      if (i9 != -1);
      for (int i10 = UI_PROVIDER_MESSAGE_TEXT_SIZE_VALUES[i9]; ; i10 = 0)
      {
        paramRowBuilder.add(Integer.valueOf(i10));
        break;
        i9++;
        break label903;
      }
      if (localPersistence.getActionStripActionReplyAll(paramContext));
      for (int i8 = 1; ; i8 = 0)
      {
        paramRowBuilder.add(Integer.valueOf(i8));
        break;
      }
      if (localPersistence.getHideCheckboxes(paramContext));
      for (int i7 = 1; ; i7 = 0)
      {
        paramRowBuilder.add(Integer.valueOf(i7));
        break;
      }
      if (localPersistence.getConfirmDelete(paramContext));
      for (int i6 = 1; ; i6 = 0)
      {
        paramRowBuilder.add(Integer.valueOf(i6));
        break;
      }
      if (localPersistence.getConfirmArchive(paramContext));
      for (int i5 = 1; ; i5 = 0)
      {
        paramRowBuilder.add(Integer.valueOf(i5));
        break;
      }
      if (localPersistence.getConfirmSend(paramContext));
      for (int i4 = 1; ; i4 = 0)
      {
        paramRowBuilder.add(Integer.valueOf(i4));
        break;
      }
      paramRowBuilder.add(getAccountLabelUri(paramString, Persistence.getAccountInbox(paramContext, paramString)));
      break;
      String str2 = Persistence.getAccountInbox(paramContext, paramString);
      if (paramMailEngine != null);
      for (Gmail.LabelMap localLabelMap = paramMailEngine.getLabelMap(); ; localLabelMap = null)
      {
        paramRowBuilder.add(getLabelFromCanonical(paramContext, localLabelMap, paramString, str2, Gmail.isSystemLabel(str2)).second);
        break;
      }
      String str1 = localPersistence.getSnapHeaderMode(paramContext);
      String[] arrayOfString1 = paramContext.getResources().getStringArray(2131558420);
      int i2 = 0;
      label1214: if (i2 < arrayOfString1.length)
        if (!TextUtils.equals(str1, arrayOfString1[i2]));
      while (true)
      {
        if (i2 != -1);
        for (int i3 = UI_PROVIDER_SNAP_HEADER_MODE_VALUES[i2]; ; i3 = 0)
        {
          paramRowBuilder.add(Integer.valueOf(i3));
          break;
          i2++;
          break label1214;
        }
        if (CustomFromUtils.replyFromDefaultAddress(getAccountUri(paramString)));
        for (int i1 = 1; ; i1 = 0)
        {
          paramRowBuilder.add(Integer.valueOf(i1));
          break;
        }
        paramRowBuilder.add(Integer.valueOf(Gservices.getInt(paramContext.getContentResolver(), "gmail_max_attachment_size_bytes", 26214400)));
        break;
        paramRowBuilder.add(Integer.valueOf(localPersistence.getSwipeIntegerPreference(paramContext)));
        break;
        if (localPersistence.getPriorityInboxArrowsEnabled(paramContext, paramString));
        for (int n = 1; ; n = 0)
        {
          paramRowBuilder.add(Integer.valueOf(n));
          break;
        }
        paramRowBuilder.add(Uri.EMPTY);
        break;
        int m;
        if (localPersistence.isConversationOverviewModeSet(paramContext))
          if (localPersistence.getConversationOverviewMode(paramContext))
            m = 0;
        while (true)
        {
          paramRowBuilder.add(Integer.valueOf(m));
          break;
          m = 1;
          continue;
          m = -1;
        }
        paramRowBuilder.add(getUpdateSettingsUri(paramString));
        break;
        return;
        i2 = -1;
      }
      i9 = -1;
    }
  }

  private final void populateRecentLabels(MailEngine paramMailEngine, String paramString)
  {
    LogUtils.d("Gmail", "UiProvider.populateRecentLabels()", new Object[0]);
    String[] arrayOfString = { "^t", "^f", "^r" };
    long l = System.currentTimeMillis();
    ContentValues localContentValues = new ContentValues(arrayOfString.length);
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = arrayOfString[j];
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = str;
      arrayOfObject[1] = Long.valueOf(l);
      LogUtils.d("Gmail", "Marking %s with %d", arrayOfObject);
      localContentValues.put(str, Long.valueOf(l));
    }
    paramMailEngine.updateLabelsLastTouched(localContentValues);
    this.mContentResolver.notifyChange(getRecentFoldersUri(paramString), null, false);
  }

  private static boolean requiredLabelsPresent(MailEngine paramMailEngine)
  {
    Gmail.LabelMap localLabelMap = paramMailEngine.getLabelMap();
    String[] arrayOfString = UI_PROVIDER_REQUIRED_LABELS;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
      if (!localLabelMap.labelPresent(arrayOfString[j]))
        return false;
    return true;
  }

  private long saveDraft(MailEngine paramMailEngine, Bundle paramBundle)
  {
    if (paramBundle.containsKey("_id"));
    for (long l = paramBundle.getLong("_id"); ; l = 0L)
    {
      Bundle localBundle = (Bundle)paramBundle.getParcelable("opened_fds");
      return sendOrSaveDraft(paramMailEngine, l, true, translateMessage(paramBundle), localBundle);
    }
  }

  private void scheduleAccountChangeNotification(Context paramContext, String paramString)
  {
    Handler localHandler = this.mUiProviderHandler;
    if (localHandler == null)
    {
      LogUtils.w("Gmail", "Attempting to schedule notification before initialization", new Object[0]);
      return;
    }
    synchronized (this.mAccountChangeNotifiers)
    {
      AccountChangedNotifier localAccountChangedNotifier = (AccountChangedNotifier)this.mAccountChangeNotifiers.get(paramString);
      if (localAccountChangedNotifier == null)
      {
        localAccountChangedNotifier = new AccountChangedNotifier(paramContext, localHandler.getLooper(), paramString);
        this.mAccountChangeNotifiers.put(paramString, localAccountChangedNotifier);
      }
      localAccountChangedNotifier.scheduleTask();
      return;
    }
  }

  private long sendMessage(MailEngine paramMailEngine, Bundle paramBundle)
  {
    if (paramBundle.containsKey("_id"));
    for (long l = paramBundle.getLong("_id"); ; l = 0L)
    {
      Bundle localBundle = (Bundle)paramBundle.getParcelable("opened_fds");
      return sendOrSaveDraft(paramMailEngine, l, false, translateMessage(paramBundle), localBundle);
    }
  }

  private long sendOrSaveDraft(MailEngine paramMailEngine, long paramLong, boolean paramBoolean, ContentValues paramContentValues, Bundle paramBundle)
  {
    ContentValues localContentValues = new ContentValues(paramContentValues);
    long l1 = localContentValues.getAsLong("refMessageId").longValue();
    localContentValues.remove("refMessageId");
    long l2 = paramMailEngine.sendOrSaveDraft(paramLong, paramBoolean, l1, localContentValues, paramBundle);
    MailSync.Message localMessage = paramMailEngine.getLocalMessage(l2, false);
    if (localMessage != null)
      this.mContentResolver.notifyChange(getConversationMessageListUri(paramMailEngine.getAccountName(), localMessage.conversationId), null, false);
    return l2;
  }

  private ContentValues translateMessage(Bundle paramBundle)
  {
    boolean bool1 = true;
    long l1 = 0L;
    long l2;
    ContentValues localContentValues;
    ArrayList localArrayList;
    label195: Attachment localAttachment;
    String str4;
    if (paramBundle.containsKey("_id"))
    {
      l2 = paramBundle.getLong("_id");
      localContentValues = new ContentValues();
      localContentValues.put("toAddresses", uiProviderToGmailRecipients(paramBundle.getString("toAddresses")));
      localContentValues.put("ccAddresses", uiProviderToGmailRecipients(paramBundle.getString("ccAddresses")));
      localContentValues.put("bccAddresses", uiProviderToGmailRecipients(paramBundle.getString("bccAddresses")));
      localContentValues.put("subject", paramBundle.getString("subject"));
      localContentValues.put("snippet", paramBundle.getString("snippet"));
      localContentValues.put("replyToAddresses", paramBundle.getString("replyToAddress"));
      localContentValues.put("fromAddress", paramBundle.getString("fromAddress"));
      localContentValues.put("customFromAddress", paramBundle.getString("customFrom"));
      String str1 = paramBundle.getString("joinedAttachmentInfos");
      if (TextUtils.isEmpty(str1))
        break label304;
      localArrayList = Lists.newArrayList();
      Iterator localIterator = Attachment.fromJSONArray(str1).iterator();
      if (!localIterator.hasNext())
        break label288;
      localAttachment = (Attachment)localIterator.next();
      Uri localUri = localAttachment.uri;
      if ((localUri == null) || (!"com.android.gmail.ui".equals(localUri.getAuthority())))
        break label510;
      str4 = localUri.getQueryParameter("gmailJoinedAttachment");
      if (str4 == null)
        break label510;
    }
    while (true)
    {
      if (str4 == null);
      for (String str5 = localAttachment.toJoinedString(); ; str5 = str4)
      {
        localArrayList.add(str5);
        break label195;
        l2 = l1;
        break;
        label288: localContentValues.put("joinedAttachmentInfos", TextUtils.join("\n", localArrayList));
        label304: String str2 = paramBundle.getString("bodyHtml");
        if (TextUtils.isEmpty(str2))
        {
          String str3 = paramBundle.getString("bodyText");
          if (!TextUtils.isEmpty(str3))
            str2 = Html.toHtml(new SpannedString(str3));
        }
        localContentValues.put("body", str2);
        if (paramBundle.getInt("appendRefMessageContent", 0) != 0);
        for (boolean bool2 = bool1; ; bool2 = false)
        {
          localContentValues.put("includeQuotedText", Boolean.valueOf(bool2));
          if (paramBundle.containsKey("quotedTextStartPos"))
            localContentValues.put("quoteStartPos", Integer.valueOf(paramBundle.getInt("quotedTextStartPos")));
          if ((l2 == l1) && (paramBundle.containsKey("refMessageId")))
            break;
          localContentValues.put("refMessageId", Long.valueOf(l1));
          return localContentValues;
        }
        l1 = Long.parseLong(Uri.parse(paramBundle.getString("refMessageId")).getLastPathSegment());
        if (paramBundle.getInt("draftType") == 4);
        while (true)
        {
          localContentValues.put("forward", Boolean.valueOf(bool1));
          break;
          bool1 = false;
        }
      }
      label510: str4 = null;
    }
  }

  private static String uiProviderToGmailRecipients(String paramString)
  {
    if (paramString == null)
      return null;
    return TextUtils.join("\n", Message.tokenizeAddresses(paramString));
  }

  private void updateAccountsIntializedStatus()
  {
    if (this.mAccountsPendingInitialization.size() == 0)
    {
      this.mAccountsFullyInitialized = true;
      notifyAccountListChanged(getContext());
    }
  }

  private int updateAttachmentState(Uri paramUri, ContentValues paramContentValues)
  {
    Integer localInteger1 = paramContentValues.getAsInteger("state");
    Integer localInteger2 = paramContentValues.getAsInteger("destination");
    if ((localInteger1 == null) && (localInteger2 == null));
    UIAttachment.UriParser localUriParser;
    ConversationState localConversationState;
    MailEngine localMailEngine;
    do
    {
      return 0;
      localUriParser = UIAttachment.UriParser.parse(paramUri);
      localConversationState = getConversationState(localUriParser.mAccount, localUriParser.mConversationId);
      localMailEngine = getOrMakeMailEngine(localUriParser.mAccount);
    }
    while (localInteger1 == null);
    UIAttachment localUIAttachment = localConversationState.getOrCreateMessageState(localUriParser).getMessageAttachment(localUriParser.mPartId);
    if (localUIAttachment == null)
    {
      LogUtils.e("Gmail", "couldn't find attachment in update AttachmentState", new Object[0]);
      return 0;
    }
    int i = localInteger1.intValue();
    switch (i)
    {
    case 1:
    case 3:
    default:
      return 0;
    case 0:
      if (localUIAttachment.getDestination() != 1)
        break;
    case 2:
    case 4:
    }
    for (boolean bool2 = true; ; bool2 = false)
    {
      localUIAttachment.updateState(0, 0, null);
      MailProvider.AttachmentRequest localAttachmentRequest2 = MailProvider.attachmentRequestForUri(localMailEngine, Gmail.getAttachmentUri(localUriParser.mAccount, localUriParser.mLocalMessageId, localUIAttachment, Gmail.AttachmentRendition.BEST, bool2));
      if (localAttachmentRequest2 == null)
        break;
      return localMailEngine.getAttachmentManager().cancelDownloadRequest(localAttachmentRequest2.message.conversationId, localAttachmentRequest2.message.messageId, localUriParser.mPartId, localAttachmentRequest2.rendition, localAttachmentRequest2.saveToSd);
      int j;
      if (localInteger2 != null)
      {
        j = localInteger2.intValue();
        label254: if (j != 1)
          break label405;
      }
      label405: for (boolean bool1 = true; ; bool1 = false)
      {
        localUIAttachment.updateState(2, j, null);
        MailProvider.AttachmentRequest localAttachmentRequest1 = MailProvider.attachmentRequestForUri(localMailEngine, Gmail.getAttachmentUri(localUriParser.mAccount, localUriParser.mLocalMessageId, localUIAttachment, Gmail.AttachmentRendition.BEST, bool1));
        if (localAttachmentRequest1 == null)
          break;
        AttachmentManager localAttachmentManager = localMailEngine.getAttachmentManager();
        if (i == 4)
          localAttachmentManager.resetAttachmentRequest(localAttachmentRequest1.message.conversationId, localAttachmentRequest1.message.messageId, localAttachmentRequest1.attachment.partId, localAttachmentRequest1.rendition, localAttachmentRequest1.saveToSd);
        localAttachmentManager.queryAndStartDownloadingAttachment(localAttachmentRequest1.message.conversationId, localAttachmentRequest1.message.messageId, localAttachmentRequest1.attachment, localAttachmentRequest1.rendition, localAttachmentRequest1.saveToSd, null);
        return 1;
        j = 0;
        break label254;
      }
    }
  }

  private int updateConversation(String paramString1, String paramString2, ContentValues paramContentValues, int paramInt)
  {
    MailEngine localMailEngine = getOrMakeMailEngine(paramString2);
    ConversationLabelOperation localConversationLabelOperation = null;
    long l = Long.parseLong(paramString1);
    String str2;
    int i;
    boolean bool2;
    label141: int k;
    if (paramContentValues.containsKey("operation"))
    {
      str2 = paramContentValues.getAsString("operation");
      if ("discard_drafts".equals(str2))
      {
        i = 1;
        if (localConversationLabelOperation == null)
          localConversationLabelOperation = new ConversationLabelOperation(paramString2, l, null);
        if (paramContentValues.containsKey(UIProvider.ConversationColumns.STARRED))
          localConversationLabelOperation.add(LabelManager.getLabel(getContext(), paramString2, "^t"), paramContentValues.getAsBoolean(UIProvider.ConversationColumns.STARRED).booleanValue());
        if (paramContentValues.containsKey(UIProvider.ConversationColumns.READ))
        {
          Label localLabel = LabelManager.getLabel(getContext(), paramString2, "^u");
          if (paramContentValues.getAsBoolean(UIProvider.ConversationColumns.READ).booleanValue())
            break label381;
          bool2 = true;
          localConversationLabelOperation.add(localLabel, bool2);
        }
        if (paramContentValues.containsKey("viewed"))
          localConversationLabelOperation.add(LabelManager.getLabel(getContext(), paramString2, "^o"), true);
        if (paramContentValues.containsKey(UIProvider.ConversationColumns.PRIORITY))
        {
          if (paramContentValues.getAsInteger(UIProvider.ConversationColumns.PRIORITY).intValue() != 1)
            break label387;
          k = 1;
          label203: if (k == 0)
            break label393;
        }
      }
    }
    label387: label393: for (String str1 = "^^important"; ; str1 = "^^unimportant")
    {
      localConversationLabelOperation.add(LabelManager.getLabel(getContext(), paramString2, str1), true);
      int j = 0;
      if (i != 0)
        j = localMailEngine.expungeDraftMessages(l);
      Boolean localBoolean = paramContentValues.getAsBoolean("suppress_undo");
      if ((localBoolean == null) || (!localBoolean.booleanValue()))
        addUndoOperation(paramInt, (ConversationLabelOperation)localConversationLabelOperation.undoOperation());
      return j + addRemoveLabel(new String[] { paramString1 }, paramString2, localConversationLabelOperation);
      localConversationLabelOperation = createLabelOperationsForUIOperation(paramString2, l, str2);
      i = 0;
      break;
      boolean bool1 = paramContentValues.containsKey("rawFolders");
      localConversationLabelOperation = null;
      i = 0;
      if (!bool1)
        break;
      List localList = localMailEngine.getLabelsForConversation(getContext(), paramString1, this.mGmail);
      localConversationLabelOperation = createLabelOperations(paramString2, l, Folder.getFoldersArray(paramContentValues.getAsString("rawFolders")), localList);
      i = 0;
      break;
      label381: bool2 = false;
      break label141;
      k = 0;
      break label203;
    }
  }

  private int updateLabelsLastTouched(MailEngine paramMailEngine, String paramString, ContentValues paramContentValues)
  {
    Set localSet1 = paramContentValues.keySet();
    ContentValues localContentValues = new ContentValues(paramContentValues.size());
    long l = System.currentTimeMillis();
    Iterator localIterator = localSet1.iterator();
    while (localIterator.hasNext())
      localContentValues.put(Uri.parse((String)localIterator.next()).getLastPathSegment(), Long.valueOf(l));
    Set localSet2 = localContentValues.keySet();
    if (LogUtils.isLoggable("Gmail", 2))
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = LogUtils.labelSetToString(localSet2);
      LogUtils.v("Gmail", "Updating last touched for labels: %s", arrayOfObject);
    }
    for (MailCore.Label localLabel : paramMailEngine.getLabelsForCanonicalNames((String[])localSet2.toArray(new String[localSet2.size()])))
      if (localLabel != null)
        paramMailEngine.clearNewUnreadMailForNotificationLabelIfNeeded(localLabel);
    int k = paramMailEngine.updateLabelsLastTouched(localContentValues);
    this.mContentResolver.notifyChange(getRecentFoldersUri(paramString), null, false);
    return k;
  }

  private int updateMessageState(String paramString, Uri paramUri, ContentValues paramContentValues)
  {
    int i = 1;
    MailEngine localMailEngine = getOrMakeMailEngine(paramString);
    long l = Long.parseLong(paramUri.getLastPathSegment());
    Integer localInteger1 = paramContentValues.getAsInteger(UIProvider.MessageColumns.READ);
    Integer localInteger2 = paramContentValues.getAsInteger(UIProvider.MessageColumns.STARRED);
    Integer localInteger3 = paramContentValues.getAsInteger("alwaysShowImages");
    if ((localInteger3 == null) && (localInteger1 == null) && (localInteger2 == null))
      return 0;
    MailSync.Message localMessage = localMailEngine.getLocalMessage(l, false);
    if (localMessage == null)
    {
      Object[] arrayOfObject = new Object[i];
      arrayOfObject[0] = Long.valueOf(l);
      LogUtils.e("Gmail", "Error finding message for localMessageId: %d", arrayOfObject);
      return 0;
    }
    if (localInteger1 != null)
      if (localInteger1.intValue() == 0)
      {
        int i2 = i;
        ContentValues localContentValues2 = new ContentValues();
        localContentValues2.put("canonicalName", "^u");
        localContentValues2.put("_id", Long.valueOf(l));
        localContentValues2.put("messageId", Long.valueOf(localMessage.messageId));
        localContentValues2.put("conversation", Long.valueOf(localMessage.conversationId));
        localContentValues2.put("add_label_action", Boolean.valueOf(i2));
        Gmail localGmail2 = this.mGmail;
        ContentValues[] arrayOfContentValues2 = new ContentValues[i];
        arrayOfContentValues2[0] = localContentValues2;
        localGmail2.addOrRemoveLabelOnMessageBulk(paramString, arrayOfContentValues2, i);
      }
    label252: int m;
    for (int k = i; ; m = 0)
    {
      if (localInteger2 != null)
      {
        if (localInteger2.intValue() != 0)
        {
          int n = i;
          ContentValues localContentValues1 = new ContentValues();
          localContentValues1.put("canonicalName", "^t");
          localContentValues1.put("_id", Long.valueOf(l));
          localContentValues1.put("messageId", Long.valueOf(localMessage.messageId));
          localContentValues1.put("conversation", Long.valueOf(localMessage.conversationId));
          localContentValues1.put("add_label_action", Boolean.valueOf(n));
          Gmail localGmail1 = this.mGmail;
          ContentValues[] arrayOfContentValues1 = new ContentValues[i];
          arrayOfContentValues1[0] = localContentValues1;
          localGmail1.addOrRemoveLabelOnMessageBulk(paramString, arrayOfContentValues1, i);
          k = i;
        }
      }
      else
      {
        if ((localInteger3 != null) && (localInteger3.intValue() != 0))
        {
          Persistence localPersistence = Persistence.getInstance();
          String str = getEmailAddress(localMessage.fromAddress).getAddress();
          localPersistence.setDisplayImagesFromSender(getContext(), str);
          k = i;
        }
        if (k == 0)
          break label428;
      }
      while (true)
      {
        return i;
        int i3 = 0;
        break;
        int i1 = 0;
        break label252;
        label428: int j = 0;
      }
    }
  }

  private void updateSearchResultCount(String paramString1, int paramInt, String paramString2)
  {
    getOrCreateAccountState(paramString1).setNumSearchResults(paramString2, paramInt);
    this.mContentResolver.notifyChange(getAccountSearchUri(paramString1), null, false);
  }

  private int updateSettings(ContentValues paramContentValues)
  {
    Persistence localPersistence = Persistence.getInstance();
    if (paramContentValues.containsKey("auto_advance"))
      localPersistence.setAutoAdvanceMode(getContext(), paramContentValues.getAsString("auto_advance"));
    while (!paramContentValues.containsKey("conversation_view_mode"))
    {
      this.mContentResolver.notifyChange(getAccountsUri(), null, false);
      return 1;
    }
    if (paramContentValues.getAsInteger("conversation_view_mode").intValue() == 0);
    for (boolean bool = true; ; bool = false)
    {
      localPersistence.setConversationOverviewMode(getContext(), bool);
      break;
    }
  }

  public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> paramArrayList)
    throws OperationApplicationException
  {
    HashSet localHashSet = Sets.newHashSet();
    Iterator localIterator1 = paramArrayList.iterator();
    while (localIterator1.hasNext())
      localHashSet.add((String)((ContentProviderOperation)localIterator1.next()).getUri().getPathSegments().get(0));
    MailEngine localMailEngine;
    if (localHashSet.size() == 1)
    {
      Iterator localIterator2 = localHashSet.iterator();
      if (localIterator2.hasNext())
        localMailEngine = getOrMakeMailEngine((String)localIterator2.next());
    }
    while (true)
    {
      if (localMailEngine != null)
        localMailEngine.beginTransaction(true);
      try
      {
        ContentProviderResult[] arrayOfContentProviderResult = super.applyBatch(paramArrayList);
        if (localMailEngine != null)
          localMailEngine.setTransactionSuccessful();
        return arrayOfContentProviderResult;
        localMailEngine = null;
        continue;
        localMailEngine = null;
      }
      finally
      {
        if (localMailEngine != null)
          localMailEngine.endTransaction();
      }
    }
  }

  public Bundle call(String paramString1, String paramString2, Bundle paramBundle)
  {
    String str = (String)Uri.parse(paramString2).getPathSegments().get(0);
    MailEngine localMailEngine = getOrMakeMailEngine(str);
    paramBundle.containsKey("_id");
    MailIndexerService.onContentProviderAccess(str);
    long l = -1L;
    if (TextUtils.equals(paramString1, "send_message"))
      l = sendMessage(localMailEngine, paramBundle);
    while (l != -1L)
    {
      Bundle localBundle = new Bundle();
      localBundle.putParcelable("messageUri", getMessageByIdUri(str, l));
      return localBundle;
      if (TextUtils.equals(paramString1, "save_message"))
        l = saveDraft(localMailEngine, paramBundle);
      else
        LogUtils.wtf("Gmail", "Unexpected Content provider method: %s", new Object[] { paramString1 });
    }
    return null;
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    if (LogUtils.isLoggable("Gmail", 3))
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = LogUtils.contentUriToString(paramUri);
      LogUtils.d("Gmail", "UiProvider.delete: %s", arrayOfObject);
    }
    int i = sUrlMatcher.match(paramUri);
    String str1 = (String)paramUri.getPathSegments().get(0);
    switch (i)
    {
    default:
      return 0;
    case 13:
    }
    String str2 = (String)paramUri.getPathSegments().get(2);
    int j = -1;
    String str3 = paramUri.getQueryParameter("seq");
    if (str3 != null)
      j = Integer.parseInt(str3);
    return delete(str2, str1, j);
  }

  public String getType(Uri paramUri)
  {
    if (LogUtils.isLoggable("Gmail", 3))
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = LogUtils.contentUriToString(paramUri);
      LogUtils.d("Gmail", "UiProvider.getType: %s", arrayOfObject);
    }
    switch (sUrlMatcher.match(paramUri))
    {
    default:
      return null;
    case 23:
    }
    return paramUri.getQueryParameter("mimeType");
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    if (LogUtils.isLoggable("Gmail", 3))
    {
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = LogUtils.contentUriToString(paramUri);
      arrayOfObject2[1] = paramContentValues;
      LogUtils.d("Gmail", "UiProvider.insert: %s(%s)", arrayOfObject2);
    }
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = LogUtils.contentUriToString(paramUri);
    arrayOfObject1[1] = paramContentValues;
    LogUtils.wtf("Gmail", "Unexpected UiProvider.insert: %s(%s)", arrayOfObject1);
    return null;
  }

  public boolean onCreate()
  {
    Context localContext = getContext();
    this.mContentResolver = localContext.getContentResolver();
    this.mGmail = new Gmail(this.mContentResolver);
    sAccountNotificationDelayMs = localContext.getResources().getInteger(2131296317);
    sGmailQuote = localContext.getResources().getString(2131427593);
    intializeLoaderHandler();
    sInstance = this;
    return true;
  }

  public void onMailEngineResult(MailEngine paramMailEngine)
  {
    this.mAccountsPendingInitialization.remove(paramMailEngine.getAccountName());
    updateAccountsIntializedStatus();
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    if (LogUtils.isLoggable("Gmail", 3))
    {
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = LogUtils.contentUriToString(paramUri);
      arrayOfObject[1] = paramString1;
      arrayOfObject[2] = Arrays.toString(paramArrayOfString2);
      LogUtils.d("Gmail", "UiProvider.query: %s(%s, %s)", arrayOfObject);
    }
    int i = sUrlMatcher.match(paramUri);
    if (i == 1)
    {
      localObject = getAccountsCursor(paramArrayOfString1);
      ContentResolver localContentResolver9 = this.mContentResolver;
      Uri localUri7 = ACCOUNTS_URI;
      ((Cursor)localObject).setNotificationUri(localContentResolver9, localUri7);
      return localObject;
    }
    String str1 = (String)paramUri.getPathSegments().get(0);
    int j = 1;
    Object localObject = null;
    switch (i)
    {
    case 7:
    case 8:
    case 9:
    case 11:
    case 12:
    case 13:
    case 14:
    case 24:
    case 25:
    default:
    case 2:
    case 3:
    case 18:
    case 4:
    case 5:
    case 6:
    case 10:
    case 15:
    case 16:
    case 17:
    case 19:
    case 20:
    case 21:
    case 22:
    case 23:
    case 26:
    }
    while (true)
    {
      if ((localObject != null) && (j != 0))
      {
        ContentResolver localContentResolver1 = this.mContentResolver;
        Uri localUri1 = getAccountBaseNotificationUri(str1);
        ((Cursor)localObject).setNotificationUri(localContentResolver1, localUri1);
        return localObject;
        localObject = getAccountCursor(str1, paramArrayOfString1);
        ContentResolver localContentResolver8 = this.mContentResolver;
        ((Cursor)localObject).setNotificationUri(localContentResolver8, paramUri);
        j = 0;
        continue;
        localObject = getAccountLabelsCursor(str1, paramArrayOfString1);
        ContentResolver localContentResolver7 = this.mContentResolver;
        Uri localUri6 = getAccountFoldersUri(str1);
        ((Cursor)localObject).setNotificationUri(localContentResolver7, localUri6);
        j = 0;
        continue;
        String str14 = paramUri.getLastPathSegment();
        localObject = getAccountLabelCursor(str1, str14, paramArrayOfString1);
        ContentResolver localContentResolver6 = this.mContentResolver;
        Uri localUri5 = getAccountLabelNotificationUri(str1, str14);
        ((Cursor)localObject).setNotificationUri(localContentResolver6, localUri5);
        j = 0;
        continue;
      }
      break;
      String str10 = paramUri.getQueryParameter("limit");
      try
      {
        Integer localInteger2 = Integer.valueOf(Integer.parseInt(str10));
        localInteger1 = localInteger2;
        boolean bool2 = true;
        String str11 = paramUri.getQueryParameter("use_network");
        if (str11 != null)
          bool2 = Boolean.parseBoolean(str11);
        String str12 = paramUri.getQueryParameter("all_notifications");
        boolean bool3 = false;
        if (str12 != null)
          bool3 = Boolean.parseBoolean(str12);
        String str13;
        if (i == 4)
          str13 = (String)paramUri.getPathSegments().get(2);
        while (true)
        {
          try
          {
            long l2 = Long.parseLong(str13);
            localObject = getConversationsForLabelId(str1, paramUri, l2, paramArrayOfString1, localInteger1, bool2);
            if (localObject == null)
            {
              LogUtils.e("Gmail", "Returning an empty cursor instead of a null cursor", new Object[0]);
              localObject = new MatrixCursor(paramArrayOfString1, 0);
            }
            if ((!bool3) || (localObject == null))
              break;
            ContentResolver localContentResolver5 = this.mContentResolver;
            Uri localUri4 = Gmail.getBaseUri(str1);
            ((Cursor)localObject).setNotificationUri(localContentResolver5, localUri4);
            j = 0;
          }
          catch (NumberFormatException localNumberFormatException3)
          {
            LogUtils.e("Gmail", localNumberFormatException3, "Unable to parse label id %s", new Object[] { str13 });
            MatrixCursor localMatrixCursor = new MatrixCursor(paramArrayOfString1, 0);
            return localMatrixCursor;
          }
          localObject = getConversationsForLabel(str1, paramUri, (String)paramUri.getPathSegments().get(2), paramArrayOfString1, localInteger1, bool2);
        }
        String str8 = (String)paramUri.getPathSegments().get(2);
        try
        {
          long l1 = Long.parseLong(str8);
          boolean bool1 = true;
          String str9 = paramUri.getQueryParameter("listParams");
          if (str9 != null)
          {
            ListParams localListParams = ListParams.newinstance(str9);
            if (localListParams != null)
              bool1 = localListParams.mUseNetwork;
          }
          localObject = getMessagesForConversation(str1, l1, paramArrayOfString1, bool1);
          j = 0;
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          return null;
        }
        localObject = getMessageForId(str1, Long.parseLong(paramUri.getLastPathSegment()));
        continue;
        localObject = performUndo(paramUri, paramArrayOfString1);
        continue;
        if (i == 16);
        for (String str7 = paramUri.getLastPathSegment(); ; str7 = null)
        {
          localObject = performRefresh(str1, str7);
          break;
        }
        String str5 = paramUri.getQueryParameter("query");
        String str6 = paramUri.getQueryParameter("folder");
        if (str6 != null)
          ((String)Uri.parse(str6).getPathSegments().get(2));
        while (true)
        {
          localObject = getFakeSearchFolder(paramUri, str1, str5);
          ContentResolver localContentResolver4 = this.mContentResolver;
          Uri localUri3 = paramUri.buildUpon().clearQuery().build();
          ((Cursor)localObject).setNotificationUri(localContentResolver4, localUri3);
          j = 0;
          break;
        }
        String str2 = paramUri.getQueryParameter("query");
        String str3 = paramUri.getQueryParameter("folder");
        if (str3 != null);
        for (String str4 = (String)Uri.parse(str3).getPathSegments().get(2); ; str4 = null)
        {
          localObject = getConversationsForQuery(str1, paramUri, str2, str4, paramArrayOfString1, null, true);
          updateSearchResultCount(str1, ((Cursor)localObject).getCount(), str2);
          break;
        }
        localObject = getRecentLabelsCursor(str1, paramArrayOfString1);
        ContentResolver localContentResolver3 = this.mContentResolver;
        Uri localUri2 = getRecentFoldersUri(str1);
        ((Cursor)localObject).setNotificationUri(localContentResolver3, localUri2);
        j = 0;
        continue;
        localObject = getMessageAttachments(UIAttachment.UriParser.parse(paramUri), paramArrayOfString1);
        ContentResolver localContentResolver2 = this.mContentResolver;
        ((Cursor)localObject).setNotificationUri(localContentResolver2, paramUri);
        j = 0;
        continue;
        localObject = getAccountCookieCursor(str1, paramArrayOfString1);
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        while (true)
          Integer localInteger1 = null;
      }
    }
  }

  public void shutdown()
  {
    sInstance = null;
    sAccountStateMap.clear();
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    if (LogUtils.isLoggable("Gmail", 3))
    {
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = LogUtils.contentUriToString(paramUri);
      arrayOfObject2[1] = paramContentValues;
      LogUtils.d("Gmail", "UiProvider.update: %s(%s)", arrayOfObject2);
    }
    int i = sUrlMatcher.match(paramUri);
    String str1 = (String)paramUri.getPathSegments().get(0);
    MailEngine localMailEngine = getOrMakeMailEngine(str1);
    MailIndexerService.onContentProviderAccess(str1);
    switch (i)
    {
    case 11:
    case 12:
    case 14:
    case 15:
    case 16:
    case 17:
    case 18:
    case 19:
    case 20:
    case 26:
    default:
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = (LogUtils.contentUriToString(paramUri) + " and values are : " + paramContentValues.toString());
      LogUtils.wtf("Gmail", "Unexpected UiProvider.update: %s", arrayOfObject1);
    case 13:
    case 25:
      while (true)
      {
        return 0;
        String str2 = (String)paramUri.getPathSegments().get(2);
        int j = -1;
        String str3 = paramUri.getQueryParameter("seq");
        if (str3 != null)
          j = Integer.parseInt(str3);
        return updateConversation(str2, str1, paramContentValues, j);
        LogUtils.d("Gmail", "update: running populateRecentLabels.", new Object[0]);
        populateRecentLabels(localMailEngine, str1);
      }
    case 21:
      return updateLabelsLastTouched(localMailEngine, str1, paramContentValues);
    case 23:
      return updateAttachmentState(paramUri, paramContentValues);
    case 22:
      return 0;
    case 10:
      return updateMessageState(str1, paramUri, paramContentValues);
    case 24:
      return expungeMessage(str1, paramContentValues);
    case 27:
    }
    return updateSettings(paramContentValues);
  }

  private static class AccountChangedNotifier extends DelayedTaskHandler
  {
    private final Uri mNotificationUri;
    private final ContentResolver mResolver;

    AccountChangedNotifier(Context paramContext, Looper paramLooper, String paramString)
    {
      super(UiProvider.sAccountNotificationDelayMs);
      this.mResolver = paramContext.getContentResolver();
      this.mNotificationUri = UiProvider.getAccountBaseNotificationUri(paramString);
    }

    protected void performTask()
    {
      this.mResolver.notifyChange(this.mNotificationUri, null, false);
    }
  }

  private class AttachmentCursor extends MatrixCursor
  {
    private final String mAccount;
    private final long mConversationId;
    private final long mMessageId;

    public AttachmentCursor(String paramLong1, long arg3, long arg5, String[] paramInt, int arg8)
    {
      super(i);
      this.mAccount = paramLong1;
      this.mConversationId = ???;
      this.mMessageId = ???;
    }

    public void close()
    {
      synchronized (UiProvider.sAccountStateMap)
      {
        AccountState localAccountState = (AccountState)UiProvider.sAccountStateMap.get(this.mAccount);
        if (localAccountState != null)
        {
          ConversationState localConversationState = localAccountState.getConversationState(this.mConversationId);
          if (localConversationState != null)
            localConversationState.handleCursorClose(this);
        }
        super.close();
        return;
      }
    }
  }

  private class ConversationLabelOperation extends LabelOperations
  {
    final String mAccount;
    final long mConversationId;

    private ConversationLabelOperation(String paramLong, long arg3)
    {
      this.mAccount = paramLong;
      Object localObject;
      this.mConversationId = localObject;
    }

    private void performOperation()
    {
      UiProvider localUiProvider = UiProvider.this;
      String[] arrayOfString = new String[1];
      arrayOfString[0] = Long.toString(this.mConversationId);
      localUiProvider.addRemoveLabel(arrayOfString, this.mAccount, this);
    }

    protected LabelOperations createNewInstance()
    {
      return new ConversationLabelOperation(UiProvider.this, this.mAccount, this.mConversationId);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.UiProvider
 * JD-Core Version:    0.6.2
 */