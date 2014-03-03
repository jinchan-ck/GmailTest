package com.google.android.gm.provider;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.text.TextUtils;
import com.google.android.gm.Utils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class MailProvider extends ContentProvider
  implements OnAccountsUpdateListener
{
  private static volatile MailProvider sThis;
  private static final UriMatcher sUrlMatcher = new UriMatcher(-1);
  private String mBoundAccount = null;
  private MailEngine mBoundEngine;
  private ContentResolver mContentResolver;
  private Handler mHandler;

  static
  {
    sUrlMatcher.addURI("gmail-ls", "*/conversations", 1);
    sUrlMatcher.addURI("gmail-ls", "*/conversations/labels", 16);
    sUrlMatcher.addURI("gmail-ls", "*/conversations/#/labels", 3);
    sUrlMatcher.addURI("gmail-ls", "*/conversations/#/labels/*", 4);
    sUrlMatcher.addURI("gmail-ls", "*/conversations/#/attachments", 21);
    sUrlMatcher.addURI("gmail-ls", "*/conversations/#/messages", 5);
    sUrlMatcher.addURI("gmail-ls", "*/messages", 6);
    sUrlMatcher.addURI("gmail-ls", "*/messages/#", 7);
    sUrlMatcher.addURI("gmail-ls", "*/messages/server/#", 8);
    sUrlMatcher.addURI("gmail-ls", "*/messages/#/labels", 9);
    sUrlMatcher.addURI("gmail-ls", "*/messages/#/labels/*", 10);
    sUrlMatcher.addURI("gmail-ls", "*/messages/labels", 17);
    sUrlMatcher.addURI("gmail-ls", "*/messages/#/attachments/*/*/*", 11);
    sUrlMatcher.addURI("gmail-ls", "*/messages/#/attachments/*/*/*/download", 12);
    sUrlMatcher.addURI("gmail-ls", "*/labels/lastTouched", 22);
    sUrlMatcher.addURI("gmail-ls", "*/labels/*/#", 18);
    sUrlMatcher.addURI("gmail-ls", "*/labels/*", 13);
    sUrlMatcher.addURI("gmail-ls", "*/label/#", 19);
    sUrlMatcher.addURI("gmail-ls", "*/settings", 14);
    sUrlMatcher.addURI("gmail-ls", "*/unread/*", 15);
    sUrlMatcher.addURI("gmail-ls", "*/status", 20);
  }

  static AttachmentRequest attachmentRequestForUri(MailEngine paramMailEngine, Uri paramUri)
  {
    MailSync.Message localMessage = paramMailEngine.getLocalMessage(Long.parseLong((String)paramUri.getPathSegments().get(2)), false);
    if (localMessage == null)
    {
      LogUtils.w("Gmail", "Message not found", new Object[0]);
      return null;
    }
    Gmail.Attachment localAttachment = localMessage.getAttachmentOrNull((String)paramUri.getPathSegments().get(4));
    if (localAttachment == null)
    {
      LogUtils.w("Gmail", "Attachment not found", new Object[0]);
      return null;
    }
    Gmail.AttachmentRendition localAttachmentRendition = Gmail.AttachmentRendition.valueOf((String)paramUri.getPathSegments().get(5));
    boolean bool = Boolean.valueOf((String)paramUri.getPathSegments().get(6)).booleanValue();
    AttachmentRequest localAttachmentRequest = new AttachmentRequest();
    localAttachmentRequest.message = localMessage;
    localAttachmentRequest.attachment = localAttachment;
    localAttachmentRequest.rendition = localAttachmentRendition;
    localAttachmentRequest.saveToSd = bool;
    return localAttachmentRequest;
  }

  static MailProvider getMailProvider()
  {
    return sThis;
  }

  private void notifyDatasetChanged(String paramString)
  {
    this.mContentResolver.notifyChange(Gmail.getBaseUri(paramString), null);
  }

  private long sendOrSaveDraft(MailEngine paramMailEngine, long paramLong, ContentValues paramContentValues)
  {
    ContentValues localContentValues = new ContentValues(paramContentValues);
    boolean bool = localContentValues.getAsBoolean("save").booleanValue();
    localContentValues.remove("save");
    long l = localContentValues.getAsLong("refMessageId").longValue();
    localContentValues.remove("refMessageId");
    return paramMailEngine.sendOrSaveDraft(paramLong, bool, l, localContentValues, null);
  }

  public void bindAccount(String paramString)
  {
    try
    {
      MailEngine.clearMailEngines();
      this.mBoundAccount = null;
      this.mBoundEngine = getOrMakeMailEngine(paramString);
      this.mBoundAccount = paramString;
      return;
    }
    finally
    {
    }
  }

  public int bulkInsert(Uri paramUri, ContentValues[] paramArrayOfContentValues)
  {
    int i = sUrlMatcher.match(paramUri);
    String str1 = (String)paramUri.getPathSegments().get(0);
    String str2 = paramUri.getQueryParameter("suppressUINotifications");
    if ((str2 != null) && (Boolean.valueOf(str2).booleanValue()));
    MailEngine localMailEngine;
    for (boolean bool = true; ; bool = false)
    {
      localMailEngine = getOrMakeMailEngine(str1);
      switch (i)
      {
      default:
        throw new IllegalArgumentException(paramUri.toString());
      case 16:
      case 17:
      }
    }
    localMailEngine.setLabelOnConversationsBulk(paramArrayOfContentValues, bool);
    return 0;
    localMailEngine.setLabelOnLocalMessageBulk(paramArrayOfContentValues, bool);
    return 0;
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    int i = sUrlMatcher.match(paramUri);
    String str1 = (String)paramUri.getPathSegments().get(0);
    MailEngine localMailEngine = getOrMakeMailEngine(str1);
    MailIndexerService.onContentProviderAccess(str1);
    switch (i)
    {
    case 5:
    case 8:
    case 9:
    case 11:
    default:
      throw new IllegalArgumentException(paramUri.toString());
    case 10:
      if (!TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("selection must be empty");
      long l4 = Long.parseLong((String)paramUri.getPathSegments().get(2));
      String str3 = paramUri.getLastPathSegment();
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = str3;
      arrayOfObject3[1] = Long.valueOf(l4);
      LogUtils.d("Gmail", "MailProvider.delete(): removing label %s from local message %d", arrayOfObject3);
      if (!Gmail.isLabelUserSettable(str3))
        throw new IllegalArgumentException("label is not user-settable: " + str3);
      localMailEngine.setLabelOnLocalMessage(l4, str3, false);
      return 1;
    case 4:
      long l2 = Long.parseLong((String)paramUri.getPathSegments().get(2));
      String str2 = paramUri.getLastPathSegment();
      if ((!TextUtils.equals(paramString, "maxMessageId")) || (paramArrayOfString.length != 1))
        throw new IllegalArgumentException("selection must be 'maxMessageId', selection args must contain max message id");
      long l3 = Long.parseLong(paramArrayOfString[0]);
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = str2;
      arrayOfObject2[1] = Long.valueOf(l2);
      LogUtils.d("Gmail", "MailProvider.delete(): removing label %s from conversation %d", arrayOfObject2);
      if (!Gmail.isLabelUserSettable(str2))
        throw new IllegalArgumentException("label is not user-settable: " + str2);
      localMailEngine.setLabelOnConversation(l2, l3, str2, false);
      return 1;
    case 7:
      if (!TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("selection must be empty");
      long l1 = Long.parseLong(paramUri.getLastPathSegment());
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Long.valueOf(l1);
      LogUtils.d("Gmail", "MailProvider.delete(): removing local message %d", arrayOfObject1);
      return localMailEngine.expungeLocalMessage(l1);
    case 6:
      if (!TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("selection must be empty");
      if (paramArrayOfString == null)
        throw new IllegalArgumentException("selection Args must be specified");
      ArrayList localArrayList = Lists.newArrayList();
      int j = paramArrayOfString.length;
      for (int k = 0; k < j; k++)
        localArrayList.add(Long.valueOf(Long.parseLong(paramArrayOfString[k])));
      return localMailEngine.expungeLocalMessages(localArrayList);
    case 12:
    }
    AttachmentRequest localAttachmentRequest = attachmentRequestForUri(localMailEngine, paramUri);
    if (localAttachmentRequest == null)
      return 0;
    return localMailEngine.getAttachmentManager().cancelDownloadRequest(localAttachmentRequest.message.conversationId, localAttachmentRequest.message.messageId, localAttachmentRequest.attachment.partId, localAttachmentRequest.rendition, localAttachmentRequest.saveToSd);
  }

  MailEngine getOrMakeMailEngine(String paramString)
  {
    if (this.mBoundAccount != null)
    {
      if (this.mBoundAccount.equals(paramString))
        return this.mBoundEngine;
      throw new IllegalArgumentException("Must request bound account");
    }
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    return MailEngine.getOrMakeMailEngine(getContext(), paramString);
  }

  public String getType(Uri paramUri)
  {
    switch (sUrlMatcher.match(paramUri))
    {
    default:
    case 1:
    case 11:
    }
    AttachmentRequest localAttachmentRequest;
    do
    {
      return null;
      return "com.google.android.gm/conversations";
      localAttachmentRequest = attachmentRequestForUri(getOrMakeMailEngine((String)paramUri.getPathSegments().get(0)), paramUri);
    }
    while (localAttachmentRequest == null);
    return localAttachmentRequest.attachment.contentType;
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    if (LogUtils.isLoggable("Gmail", 3))
    {
      Object[] arrayOfObject4 = new Object[2];
      arrayOfObject4[0] = LogUtils.contentUriToString(paramUri);
      arrayOfObject4[1] = paramContentValues;
      LogUtils.d("Gmail", "MailProvider.insert: %s(%s)", arrayOfObject4);
    }
    int i = sUrlMatcher.match(paramUri);
    String str1 = (String)paramUri.getPathSegments().get(0);
    MailEngine localMailEngine = getOrMakeMailEngine(str1);
    MailIndexerService.onContentProviderAccess(str1);
    switch (i)
    {
    default:
      throw new IllegalArgumentException(paramUri.toString());
    case 9:
      long l4 = Long.parseLong((String)paramUri.getPathSegments().get(2));
      if (paramContentValues.size() != 1)
        throw new IllegalArgumentException(paramContentValues.toString());
      if (!paramContentValues.containsKey("canonicalName"))
        throw new IllegalArgumentException("values must have 'canonicalName'");
      String str3 = paramContentValues.getAsString("canonicalName");
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = str3;
      arrayOfObject3[1] = Long.valueOf(l4);
      LogUtils.d("Gmail", "MailProvider.insert(): adding label %s to local message %d", arrayOfObject3);
      if (!Gmail.isLabelUserSettable(str3))
        throw new IllegalArgumentException("label is not user-settable: " + str3);
      localMailEngine.setLabelOnLocalMessage(l4, str3, true);
      return Gmail.getMessageLabelUri(str1, l4, str3);
    case 6:
      long l3 = sendOrSaveDraft(localMailEngine, 0L, paramContentValues);
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Long.valueOf(l3);
      LogUtils.d("Gmail", "MailProvider.insert(): added local message %d", arrayOfObject2);
      return Gmail.getMessageByIdUri(str1, l3);
    case 3:
    }
    long l1 = Long.parseLong((String)paramUri.getPathSegments().get(2));
    if (!paramContentValues.containsKey("canonicalName"))
      throw new IllegalArgumentException("values must have 'canonicalName'");
    String str2 = paramContentValues.getAsString("canonicalName");
    if (!paramContentValues.containsKey("maxMessageId"))
      throw new IllegalArgumentException("values must have 'maxMessageId'");
    long l2 = paramContentValues.getAsLong("maxMessageId").longValue();
    if (paramContentValues.size() != 2)
      throw new IllegalArgumentException(paramContentValues.toString());
    Object[] arrayOfObject1 = new Object[3];
    arrayOfObject1[0] = str2;
    arrayOfObject1[1] = Long.valueOf(l1);
    arrayOfObject1[2] = Long.valueOf(l2);
    LogUtils.d("Gmail", "MailProvider.insert(): adding label %s to conversation %d,maxMessageId %d", arrayOfObject1);
    if (!Gmail.isLabelUserSettable(str2))
      throw new IllegalArgumentException("label is not user-settable: " + str2);
    localMailEngine.setLabelOnConversation(l1, l2, str2, true);
    return Gmail.getConversationLabelUri(str1, l1, str2);
  }

  public void onAccountsUpdated(Account[] paramArrayOfAccount)
  {
    HashSet localHashSet1;
    int j;
    try
    {
      if (this.mBoundAccount != null)
        return;
      localHashSet1 = Sets.newHashSet();
      int i = paramArrayOfAccount.length;
      j = 0;
      if (j < i)
      {
        Account localAccount = paramArrayOfAccount[j];
        if (!localAccount.type.equals("com.google"))
          break label430;
        localHashSet1.add(localAccount.name);
      }
    }
    finally
    {
    }
    HashSet localHashSet2 = Sets.newHashSet();
    Iterator localIterator1 = localHashSet1.iterator();
    while (localIterator1.hasNext())
    {
      String str3 = (String)localIterator1.next();
      localHashSet2.add(MailEngine.getDbName(str3));
      localHashSet2.add(MailEngine.getInternalDbName(str3));
    }
    int k = 0;
    Context localContext = getContext();
    String[] arrayOfString = localContext.databaseList();
    int m = arrayOfString.length;
    int n = 0;
    label161: String str1;
    if (n < m)
    {
      str1 = arrayOfString[n];
      if ((!str1.startsWith("mailstore")) || (!str1.endsWith(".db")))
        break label442;
    }
    label430: label436: label442: for (int i1 = 1; ; i1 = 0)
    {
      if ((i1 != 0) && (!localHashSet2.contains(str1)))
      {
        LogUtils.i("Gmail", "Deleting mail db %s because there is no account for it", new Object[] { str1 });
        localContext.deleteDatabase(str1);
        if (MailEngine.getMailEngine(str1.substring(1 + "mailstore".length(), str1.lastIndexOf(".db"))) != null)
        {
          k = 1;
          break label436;
          if (k != 0)
          {
            Utils.cancelAllNotifications(localContext);
            LogUtils.i("Gmail", "Restarting because we deleted an account.", new Object[0]);
            System.exit(-1);
          }
          while (true)
          {
            return;
            List localList = MailEngine.getMailEngines(getContext());
            HashSet localHashSet3 = Sets.newHashSet();
            Iterator localIterator2 = localList.iterator();
            while (localIterator2.hasNext())
              localHashSet3.add(((MailEngine)localIterator2.next()).getAccountName());
            localHashSet1.removeAll(localHashSet3);
            Iterator localIterator3 = localHashSet1.iterator();
            while (localIterator3.hasNext())
            {
              String str2 = (String)localIterator3.next();
              LogUtils.d("Gmail", "Creating mailengine for account %s", new Object[] { str2 });
              getOrMakeMailEngine(str2);
            }
          }
          j++;
          break;
        }
      }
      n++;
      break label161;
    }
  }

  public boolean onCreate()
  {
    sThis = this;
    HandlerThread localHandlerThread = new HandlerThread("AccountManager Listener", 10);
    localHandlerThread.start();
    this.mHandler = new Handler(localHandlerThread.getLooper());
    this.mContentResolver = getContext().getContentResolver();
    AccountManager.get(getContext()).addOnAccountsUpdatedListener(this, this.mHandler, true);
    Intent localIntent = new Intent("com.google.android.gm.intent.ACTION_PROVIDER_CREATED");
    getContext().sendBroadcast(localIntent);
    return true;
  }

  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    if (LogUtils.isLoggable("Gmail", 3))
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = LogUtils.contentUriToString(paramUri);
      LogUtils.d("Gmail", "MailProvider.openFile: %s", arrayOfObject);
    }
    int i = sUrlMatcher.match(paramUri);
    MailEngine localMailEngine = getOrMakeMailEngine((String)paramUri.getPathSegments().get(0));
    switch (i)
    {
    default:
      throw new IllegalArgumentException("Unsupported uri in openFile: " + paramUri.toString());
    case 11:
    }
    AttachmentRequest localAttachmentRequest = attachmentRequestForUri(localMailEngine, paramUri);
    if (localAttachmentRequest == null)
      throw new FileNotFoundException();
    try
    {
      ParcelFileDescriptor localParcelFileDescriptor = localMailEngine.getAttachmentManager().openAttachment(localAttachmentRequest.message.conversationId, localAttachmentRequest.message.messageId, localAttachmentRequest.attachment, localAttachmentRequest.rendition, localAttachmentRequest.saveToSd, paramString);
      return localParcelFileDescriptor;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
    }
    return localMailEngine.openLocalAttachment(localAttachmentRequest.attachment);
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    if (LogUtils.isLoggable("Gmail", 3))
    {
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = LogUtils.contentUriToString(paramUri);
      arrayOfObject[1] = paramString1;
      arrayOfObject[2] = Arrays.toString(paramArrayOfString2);
      LogUtils.d("Gmail", "MailProvider.query: %s(%s, %s)", arrayOfObject);
    }
    if (!TextUtils.isEmpty(paramString2))
      throw new IllegalArgumentException("sortOrder must be empty");
    int i = sUrlMatcher.match(paramUri);
    String str1 = (String)paramUri.getPathSegments().get(0);
    if ((this.mBoundAccount != null) && (!str1.equals(this.mBoundAccount)))
      bindAccount(str1);
    MailEngine localMailEngine = getOrMakeMailEngine(str1);
    int j = 1;
    MailIndexerService.onContentProviderAccess(str1);
    Object localObject;
    switch (i)
    {
    case 2:
    case 3:
    case 4:
    case 9:
    case 10:
    case 15:
    case 16:
    case 17:
    default:
      localObject = null;
    case 1:
    case 5:
    case 21:
    case 6:
    case 7:
    case 8:
    case 12:
    case 11:
    case 13:
    case 18:
    case 19:
    case 14:
    case 20:
    }
    while (true)
    {
      if ((localObject != null) && (j != 0))
      {
        ContentResolver localContentResolver2 = this.mContentResolver;
        Uri localUri2 = Gmail.getBaseUri(str1);
        ((Cursor)localObject).setNotificationUri(localContentResolver2, localUri2);
      }
      return localObject;
      if ((paramString1 == null) || (paramString1.length() == 0))
        paramString1 = "label:^i";
      String str6 = paramUri.getQueryParameter("limit");
      Integer localInteger;
      if (str6 != null)
      {
        localInteger = Integer.valueOf(str6);
        label308: if ((Gmail.isRunningICSOrLater()) || (Binder.getCallingPid() == Process.myPid()))
          break label350;
      }
      label350: for (boolean bool3 = true; ; bool3 = false)
      {
        localObject = localMailEngine.getConversationCursorForQuery(paramArrayOfString1, paramString1, paramArrayOfString2, localInteger, bool3);
        break;
        localInteger = null;
        break label308;
      }
      localObject = localMailEngine.getMessageCursorForConversationId(paramArrayOfString1, Long.parseLong((String)paramUri.getPathSegments().get(2)), "1".equals(paramUri.getQueryParameter("useCache")), "1".equals(paramUri.getQueryParameter("useMatrixCursor")));
      continue;
      long l2 = Long.parseLong((String)paramUri.getPathSegments().get(2));
      localObject = localMailEngine.getAttachmentManager().queryForConversation(l2, paramArrayOfString1);
      ContentResolver localContentResolver4 = this.mContentResolver;
      Uri localUri3 = Gmail.getAttachmentsForConversationUri(str1, l2);
      ((Cursor)localObject).setNotificationUri(localContentResolver4, localUri3);
      j = 0;
      continue;
      localObject = localMailEngine.getMessageCursorForMessageId(paramArrayOfString1, Long.parseLong(paramString1));
      ContentResolver localContentResolver3 = this.mContentResolver;
      ((Cursor)localObject).setNotificationUri(localContentResolver3, paramUri);
      j = 0;
      continue;
      localObject = localMailEngine.getMessageCursorForLocalMessageId(paramArrayOfString1, ContentUris.parseId(paramUri));
      continue;
      localObject = localMailEngine.getMessageCursorForMessageId(paramArrayOfString1, ContentUris.parseId(paramUri));
      continue;
      AttachmentRequest localAttachmentRequest2 = attachmentRequestForUri(localMailEngine, paramUri);
      if (localAttachmentRequest2 == null)
        return null;
      localObject = localMailEngine.getAttachmentManager().queryAndStartDownloadingAttachment(localAttachmentRequest2.message.conversationId, localAttachmentRequest2.message.messageId, localAttachmentRequest2.attachment, localAttachmentRequest2.rendition, localAttachmentRequest2.saveToSd, paramArrayOfString1);
      continue;
      AttachmentRequest localAttachmentRequest1 = attachmentRequestForUri(localMailEngine, paramUri);
      if (localAttachmentRequest1 == null)
        return null;
      String[] arrayOfString1;
      MatrixCursor localMatrixCursor;
      MatrixCursor.RowBuilder localRowBuilder;
      int m;
      label648: String str5;
      if (paramArrayOfString1 != null)
      {
        arrayOfString1 = paramArrayOfString1;
        localMatrixCursor = new MatrixCursor(arrayOfString1);
        localRowBuilder = localMatrixCursor.newRow();
        String[] arrayOfString2 = arrayOfString1;
        int k = arrayOfString2.length;
        m = 0;
        if (m >= k)
          break label768;
        str5 = arrayOfString2[m];
        if (!"_display_name".equals(str5))
          break label716;
        localRowBuilder.add(localAttachmentRequest1.attachment.name);
      }
      while (true)
      {
        m++;
        break label648;
        arrayOfString1 = new String[2];
        arrayOfString1[0] = "_display_name";
        arrayOfString1[1] = "_size";
        break;
        label716: if (("_size".equals(str5)) && (localAttachmentRequest1.rendition == Gmail.AttachmentRendition.BEST))
          localRowBuilder.add(Integer.valueOf(localAttachmentRequest1.attachment.size));
        else
          localRowBuilder.add(null);
      }
      label768: localObject = localMatrixCursor;
      continue;
      if (Long.valueOf((String)paramUri.getPathSegments().get(2)).longValue() != 0L);
      for (boolean bool2 = true; ; bool2 = false)
      {
        String str3 = paramUri.getQueryParameter("before");
        String str4 = paramUri.getQueryParameter("limit");
        List localList = paramUri.getQueryParameters("canonicalName");
        LabelQueryBuilder localLabelQueryBuilder = localMailEngine.getLabelQueryBuilder(paramArrayOfString1).filterCanonicalName(localList).showHidden(bool2);
        if (str3 != null)
          localLabelQueryBuilder.setRecent(Long.parseLong(str3), Integer.parseInt(str4));
        localObject = localLabelQueryBuilder.query();
        break;
      }
      String str2 = (String)paramUri.getPathSegments().get(2);
      if (Long.valueOf((String)paramUri.getPathSegments().get(3)).longValue() != 0L);
      for (boolean bool1 = true; ; bool1 = false)
      {
        localObject = localMailEngine.getLabelQueryBuilder(paramArrayOfString1).filterCanonicalName(ImmutableList.of(str2)).showHidden(bool1).query();
        break;
      }
      long l1 = Long.valueOf((String)paramUri.getPathSegments().get(2)).longValue();
      localObject = localMailEngine.getLabelQueryBuilder(paramArrayOfString1).labelId(l1).query();
      continue;
      localObject = localMailEngine.getPublicSettingsCursor(paramArrayOfString1);
      continue;
      localObject = localMailEngine.getStatusCursor(paramArrayOfString1);
      ContentResolver localContentResolver1 = this.mContentResolver;
      Uri localUri1 = Gmail.getStatusUri(str1);
      ((Cursor)localObject).setNotificationUri(localContentResolver1, localUri1);
      j = 0;
    }
  }

  public void shutdown()
  {
    MailEngine.clearMailEngines();
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    if (!TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("selection must be empty");
    int i = sUrlMatcher.match(paramUri);
    String str = (String)paramUri.getPathSegments().get(0);
    MailEngine localMailEngine = getOrMakeMailEngine(str);
    MailIndexerService.onContentProviderAccess(str);
    int j;
    switch (i)
    {
    default:
      throw new IllegalArgumentException(paramUri.toString());
    case 7:
      sendOrSaveDraft(localMailEngine, ContentUris.parseId(paramUri), paramContentValues);
      j = 1;
    case 14:
      boolean bool;
      do
      {
        return j;
        bool = localMailEngine.setPublicSettings(paramContentValues);
        j = 0;
      }
      while (!bool);
      notifyDatasetChanged(str);
      return 1;
    case 22:
    }
    return localMailEngine.updateLabelsLastTouched(paramContentValues);
  }

  static class AttachmentRequest
  {
    public Gmail.Attachment attachment;
    public MailSync.Message message;
    public Gmail.AttachmentRendition rendition;
    public boolean saveToSd;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.MailProvider
 * JD-Core Version:    0.6.2
 */