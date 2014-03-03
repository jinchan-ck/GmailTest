package com.google.android.gm;

import android.accounts.Account;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.provider.Settings.System;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import android.util.Log;
import android.util.TimingLogger;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.common.userhappiness.UserHappinessSignals;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.Attachment;
import com.google.android.gm.provider.Gmail.LabelMap;
import com.google.android.gm.provider.Gmail.MessageCursor;
import com.google.android.gm.provider.Gmail.MessageModification;
import com.google.android.gm.provider.MailEngine;
import com.google.android.gm.utils.CustomFromUtils;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ComposeActivity extends GmailBaseActivity
  implements TextWatcher, AdapterView.OnItemSelectedListener, ComposeController, DialogInterface.OnClickListener
{
  private static int ACCOUNT_ADDRESS = 0;
  private static int ACCOUNT_DISPLAY = 0;
  static final String AUTO_SEND_ACTION = "com.google.android.gm.action.AUTO_SEND";
  static final int COMPOSE = -1;
  private static final String EXTRA_ACTION = "action";
  private static final String EXTRA_DRAFT_ID = "draftId";
  private static final String EXTRA_FOCUS_SELECTION_END = "focusSelectionEnd";
  private static final String EXTRA_FOCUS_SELECTION_START = "focusSelectionStart";
  private static final String EXTRA_IN_REFERENCE_TO_MESSAGE_ID = "in-reference-to";
  static final String EXTRA_SHOW_CC_BCC = "showCcBcc";
  static final int FORWARD = 2;
  static final String HEADER_SEPARATOR = "<br type='attribution'>";
  private static final int HEADER_SEPARATOR_LENGTH = 0;
  private static int IS_CUSTOMFROM = 0;
  private static int REAL_ACCOUNT = 0;
  static final int REPLY = 0;
  static final int REPLY_ALL = 1;
  private static final int RESULT_CREATE_ACCOUNT = 2;
  private static final int RESULT_PICK_ATTACHMENT = 1;
  private static Spinner mAccountSpinner;
  protected String mAccount;
  private boolean mAccountSpinnerReady = false;
  private List<String> mAccounts;
  ArrayList<SendOrSaveTask> mActiveTasks = Lists.newArrayList();
  private EmailAddressAdapter mAddressAdapter;
  protected boolean mAttachmentAddedOrRemoved = false;
  private ComposeArea mComposeArea;
  private int mCurrentMode = -1;
  private String[] mCurrentReplyFromAccount;
  private long mDraftId = 0L;
  private Object mDraftIdLock;
  private String mDraftSender;
  private boolean mForward = false;
  private Gmail mGmail;
  private boolean mIsDraft = false;
  private ComposeLayout mLayoutImpl;
  private boolean mMessageIsForwardOrReply = false;
  private Persistence mPrefs;
  private String mRecipient;
  private long mRefMessageId = 0L;
  private List<String[]> mReplyFromAccounts;
  private boolean mReplyFromChanged = false;
  private Handler mSendSaveTaskHandler = null;
  private boolean mTextChanged;

  static
  {
    if (!ComposeActivity.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      HEADER_SEPARATOR_LENGTH = "<br type='attribution'>".length();
      REAL_ACCOUNT = 2;
      ACCOUNT_DISPLAY = 0;
      ACCOUNT_ADDRESS = 1;
      IS_CUSTOMFROM = 3;
      return;
    }
  }

  private void asyncInitFromSpinner()
  {
    AccountHelper.getSyncingAccounts(this, new AccountManagerCallback()
    {
      public void run(AccountManagerFuture<Account[]> paramAnonymousAccountManagerFuture)
      {
        try
        {
          ComposeActivity localComposeActivity = ComposeActivity.this;
          ComposeActivity.access$002(ComposeActivity.this, AccountHelper.mergeAccountLists(ComposeActivity.this.mAccounts, (Account[])paramAnonymousAccountManagerFuture.getResult(), false));
          ComposeActivity.this.createReplyFromCache();
          ArrayList localArrayList = new ArrayList();
          Iterator localIterator = ComposeActivity.this.mAccounts.iterator();
          while (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            if (LongShadowUtils.getLabelMap(this.val$contentResolver, str).labelsSynced())
              localArrayList.add(str);
          }
          Persistence.getInstance(localComposeActivity).cacheConfiguredGoogleAccounts(localComposeActivity, true, localArrayList);
          ComposeActivity.this.initFromSpinner();
          return;
        }
        catch (IOException localIOException)
        {
        }
        catch (AuthenticatorException localAuthenticatorException)
        {
        }
        catch (OperationCanceledException localOperationCanceledException)
        {
        }
      }
    });
  }

  private void checkInvalidEmails(String[] paramArrayOfString, List<String> paramList)
  {
    this.mComposeArea.checkInvalidEmails(paramArrayOfString, paramList);
  }

  public static void compose(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent(paramContext, ComposeActivity.class);
    localIntent.putExtra("action", -1);
    localIntent.putExtra("account", paramString);
    paramContext.startActivity(localIntent);
  }

  private void createReplyFromCache()
  {
    this.mReplyFromAccounts = new ArrayList();
    if (this.mMessageIsForwardOrReply);
    for (List localList1 = Collections.singletonList(this.mAccount); ; localList1 = this.mAccounts)
    {
      Iterator localIterator1 = localList1.iterator();
      while (localIterator1.hasNext())
      {
        String str = (String)localIterator1.next();
        Collection localCollection = CustomFromUtils.getCustomReplyFrom(str);
        this.mReplyFromAccounts.add(new String[] { str, str, str, "false" });
        if (localCollection != null)
        {
          Iterator localIterator2 = localCollection.iterator();
          while (localIterator2.hasNext())
          {
            String[] arrayOfString1 = (String[])localIterator2.next();
            if (arrayOfString1[ACCOUNT_DISPLAY] != null)
            {
              List localList2 = this.mReplyFromAccounts;
              String[] arrayOfString2 = new String[4];
              arrayOfString2[0] = arrayOfString1[ACCOUNT_DISPLAY];
              arrayOfString2[1] = arrayOfString1[ACCOUNT_ADDRESS];
              arrayOfString2[2] = str;
              arrayOfString2[3] = "true";
              localList2.add(arrayOfString2);
            }
          }
        }
      }
    }
  }

  private void discardChanges()
  {
    this.mTextChanged = false;
    this.mAttachmentAddedOrRemoved = false;
    this.mReplyFromChanged = false;
  }

  private void doDiscard()
  {
    new AlertDialog.Builder(this).setTitle(2131296318).setMessage(2131296319).setPositiveButton(2131296282, this).setNegativeButton(2131296283, null).create().show();
  }

  private void doDiscardWithoutConfirmation(boolean paramBoolean)
  {
    synchronized (this.mDraftIdLock)
    {
      if (this.mDraftId != 0L)
      {
        Gmail.MessageModification.expungeMessage(getContentResolver(), this.mAccount, this.mDraftId);
        this.mDraftId = 0L;
      }
      if (paramBoolean)
        Toast.makeText(this, 2131296340, 0).show();
      discardChanges();
      finish();
      return;
    }
  }

  public static void draft(Context paramContext, String paramString, long paramLong)
  {
    Intent localIntent = new Intent(paramContext, ComposeActivity.class);
    localIntent.putExtra("action", -1);
    localIntent.putExtra("account", paramString);
    localIntent.putExtra("draftId", paramLong);
    paramContext.startActivity(localIntent);
  }

  private void finishOnCreateAfterAccountSelected(Bundle paramBundle, Intent paramIntent)
  {
    this.mComposeArea.setBody("", true);
    ((TextView)findViewById(2131361810)).setText(this.mAccount);
    showFromSpinner(false);
    TimingLogger localTimingLogger = new TimingLogger("Gmail", "ComposeActivity.initDraftIdAndUi");
    Bundle localBundle = paramIntent.getExtras();
    if (paramBundle != null)
    {
      this.mDraftId = paramBundle.getLong("draftId");
      this.mLayoutImpl.hideOrShowCcBcc(paramBundle.getBoolean("showCcBcc", false), true);
    }
    int i;
    long l;
    while (true)
    {
      localTimingLogger.addSplit("get values");
      setQuotedSectionVisibility(false);
      i = -1;
      if ((localBundle != null) && (localBundle.containsKey("action")))
        i = localBundle.getInt("action");
      l = 0L;
      if (this.mDraftId != 0L)
        break label312;
      if ((localBundle == null) || (!localBundle.containsKey("in-reference-to")))
        break;
      l = localBundle.getLong("in-reference-to");
      if (($assertionsDisabled) || (i != -1))
        break;
      throw new AssertionError();
      if ((localBundle != null) && (localBundle.containsKey("draftId")))
        this.mDraftId = localBundle.getLong("draftId");
    }
    localTimingLogger.addSplit("more values");
    if (l != 0L)
    {
      initFromRefMessage(l, i);
      localTimingLogger.addSplit("initFromRefMessage");
      localTimingLogger.addSplit("fill ui");
      if (l == 0L)
        break label321;
    }
    label312: label321: for (boolean bool = true; ; bool = false)
    {
      this.mMessageIsForwardOrReply = bool;
      this.mLayoutImpl.updateComposeMode(i);
      initSpinnerFromCache();
      if (paramBundle == null)
        this.mComposeArea.requestFocus();
      localTimingLogger.addSplit("finish");
      localTimingLogger.dumpToLog();
      return;
      initFromExtras(getIntent());
      localTimingLogger.addSplit("initFromExtras");
      break;
      l = initFromDraftCursor();
      break;
    }
  }

  public static void forward(Context paramContext, String paramString, long paramLong)
  {
    Intent localIntent = new Intent(paramContext, ComposeActivity.class);
    localIntent.putExtra("action", 2);
    localIntent.putExtra("account", paramString);
    localIntent.putExtra("in-reference-to", paramLong);
    paramContext.startActivity(localIntent);
  }

  private void hideOrShowFromSpinner()
  {
    if (mAccountSpinner.getCount() > 1);
    for (boolean bool = true; ; bool = false)
    {
      showFromSpinner(bool);
      return;
    }
  }

  private long initFromDraftCursor()
  {
    long l = 0L;
    Gmail.MessageCursor localMessageCursor = this.mGmail.getMessageCursorForMessageId(this.mAccount, this.mDraftId);
    this.mIsDraft = true;
    while (true)
    {
      try
      {
        if (localMessageCursor.next())
        {
          l = localMessageCursor.getRefMessageId();
          this.mForward = localMessageCursor.getForward();
          if (this.mForward)
          {
            i = 2;
            Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(localMessageCursor.getFromAddress());
            if ((arrayOfRfc822Token == null) || (arrayOfRfc822Token.length <= 0))
              continue;
            this.mDraftSender = arrayOfRfc822Token[0].getAddress();
            this.mComposeArea.initFromCursor(localMessageCursor);
            this.mCurrentMode = i;
            updateSendOptions();
          }
        }
        else
        {
          return l;
        }
        if (l == 0L)
          break label185;
        if (localMessageCursor.getToAddresses().length + localMessageCursor.getCcAddresses().length + localMessageCursor.getBccAddresses().length > 1)
        {
          i = 1;
          continue;
          this.mDraftSender = localMessageCursor.getFromAddress();
          continue;
        }
      }
      finally
      {
        localMessageCursor.release();
        localMessageCursor.getCursor().close();
      }
      int i = 0;
      continue;
      label185: i = -1;
    }
  }

  private void initFromRefMessage(long paramLong, int paramInt)
  {
    TimingLogger localTimingLogger = new TimingLogger("Gmail", "ComposeActivity.initFromRefMessage");
    this.mRefMessageId = paramLong;
    Gmail.MessageCursor localMessageCursor = this.mGmail.getMessageCursorForMessageId(this.mAccount, paramLong);
    localTimingLogger.addSplit("get ref message");
    try
    {
      if (localMessageCursor.next())
      {
        if (paramInt == 2)
          this.mForward = true;
        ArrayList localArrayList = new ArrayList(Arrays.asList(localMessageCursor.getToAddresses()));
        localArrayList.addAll(Arrays.asList(localMessageCursor.getCcAddresses()));
        this.mRecipient = getMatchingRecipient(this.mAccount, localArrayList);
        this.mComposeArea.initFromRefMessage(localMessageCursor, paramInt, this.mForward);
        localTimingLogger.addSplit("initComposeAreaFromRefMessage");
        localTimingLogger.addSplit("add attachments");
      }
      localMessageCursor.getCursor().close();
      localTimingLogger.addSplit("finish");
      localTimingLogger.dumpToLog();
      return;
    }
    finally
    {
      localMessageCursor.getCursor().close();
    }
  }

  private void initFromSpinner()
  {
    FromAddressSpinnerAdapter localFromAddressSpinnerAdapter = new FromAddressSpinnerAdapter(this);
    int i = 0;
    String str;
    int j;
    label47: int k;
    label61: String[] arrayOfString;
    if (this.mIsDraft)
    {
      str = this.mDraftSender;
      if ((this.mRecipient != null) && (!this.mAccount.equals(this.mRecipient)))
        break label166;
      j = 1;
      k = 0;
      Iterator localIterator = this.mReplyFromAccounts.iterator();
      if (!localIterator.hasNext())
        break label191;
      arrayOfString = (String[])localIterator.next();
      localFromAddressSpinnerAdapter.add(arrayOfString);
      if (j == 0)
        break label172;
      if ((arrayOfString[REAL_ACCOUNT].equals(this.mAccount)) && (arrayOfString[ACCOUNT_ADDRESS].equals(str)))
        i = k;
    }
    while (true)
    {
      k++;
      break label61;
      if (this.mCurrentMode == -1)
      {
        str = CustomFromUtils.getDefaultCustomFrom(this.mAccount);
        break;
      }
      str = CustomFromUtils.getReplyFromAddress(this.mAccount, this.mRecipient);
      break;
      label166: j = 0;
      break label47;
      label172: if (str.equals(arrayOfString[ACCOUNT_ADDRESS]))
        i = k;
    }
    label191: mAccountSpinner = (Spinner)findViewById(2131361807);
    mAccountSpinner.setAdapter(localFromAddressSpinnerAdapter);
    mAccountSpinner.setSelection(i, false);
    mAccountSpinner.setOnItemSelectedListener(this);
    this.mCurrentReplyFromAccount = ((String[])this.mReplyFromAccounts.get(i));
    hideOrShowFromSpinner();
    this.mAccountSpinnerReady = true;
  }

  private void initSpinnerFromCache()
  {
    List localList = Persistence.getInstance(this).getCachedConfiguredGoogleAccounts(this, true);
    if ((localList != null) && (localList.size() > 0))
    {
      this.mAccounts = localList;
      createReplyFromCache();
      initFromSpinner();
    }
  }

  private boolean isBlank()
  {
    return this.mComposeArea.isBlank();
  }

  private void onSwitchState()
  {
    updateSendOptions();
    updateMessage(this.mCurrentMode);
    this.mComposeArea.requestFocus();
  }

  public static void reply(Context paramContext, String paramString, long paramLong, boolean paramBoolean)
  {
    Intent localIntent = new Intent(paramContext, ComposeActivity.class);
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      localIntent.putExtra("action", i);
      localIntent.putExtra("account", paramString);
      localIntent.putExtra("in-reference-to", paramLong);
      paramContext.startActivity(localIntent);
      return;
    }
  }

  private void saveIfNeeded()
  {
    if (this.mAccount == null);
    while (!shouldSave())
      return;
    doSave(true);
  }

  private void sendOrSave(Spanned paramSpanned, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((!paramBoolean1) && (ActivityManager.isUserAMonkey()))
      return;
    String[] arrayOfString1 = this.mComposeArea.getToAddresses();
    String[] arrayOfString2 = this.mComposeArea.getCcAddresses();
    String[] arrayOfString3 = this.mComposeArea.getBccAddresses();
    SendOrSaveCallback local3 = new SendOrSaveCallback()
    {
      public long getMessageId()
      {
        synchronized (ComposeActivity.this.mDraftIdLock)
        {
          long l = ComposeActivity.this.mDraftId;
          return l;
        }
      }

      public void initializeSendOrSave(ComposeActivity.SendOrSaveTask paramAnonymousSendOrSaveTask)
      {
        synchronized (ComposeActivity.this.mActiveTasks)
        {
          if (ComposeActivity.this.mActiveTasks.size() == 0)
            ComposeActivity.this.startService(new Intent(ComposeActivity.this, EmptyService.class));
          ComposeActivity.this.mActiveTasks.add(paramAnonymousSendOrSaveTask);
          return;
        }
      }

      public void notifyMessageIdAllocated(long paramAnonymousLong)
      {
        synchronized (ComposeActivity.this.mDraftIdLock)
        {
          ComposeActivity.access$502(ComposeActivity.this, paramAnonymousLong);
          return;
        }
      }

      public void sendOrSaveFinished(ComposeActivity.SendOrSaveTask paramAnonymousSendOrSaveTask)
      {
        ComposeActivity.this.discardChanges();
        synchronized (ComposeActivity.this.mActiveTasks)
        {
          ComposeActivity.this.mActiveTasks.remove(paramAnonymousSendOrSaveTask);
          int i = ComposeActivity.this.mActiveTasks.size();
          if (i == 0)
            ComposeActivity.this.stopService(new Intent(ComposeActivity.this, EmptyService.class));
          return;
        }
      }
    };
    String str1 = this.mAccount;
    String str2 = this.mAccount;
    if (this.mAccountSpinnerReady)
    {
      str2 = ((String[])this.mReplyFromAccounts.get(mAccountSpinner.getSelectedItemPosition()))[ACCOUNT_ADDRESS];
      str1 = ((String[])this.mReplyFromAccounts.get(mAccountSpinner.getSelectedItemPosition()))[REAL_ACCOUNT];
    }
    if (this.mSendSaveTaskHandler == null)
    {
      HandlerThread localHandlerThread = new HandlerThread("Background tasks");
      localHandlerThread.start();
      this.mSendSaveTaskHandler = new Handler(localHandlerThread.getLooper());
    }
    sendOrSaveInternal(this, this.mAccount, str1, str2, paramSpanned, arrayOfString1, arrayOfString2, arrayOfString3, this.mComposeArea.getSubject(), this.mComposeArea.getQuotedText(), this.mComposeArea.getAttachments(), this.mRefMessageId, local3, this.mSendSaveTaskHandler, paramBoolean1, this.mForward);
    if ((this.mRecipient != null) && (this.mRecipient.equals(this.mAccount)))
      this.mRecipient = str1;
    this.mAccount = str1;
    if ((paramBoolean2) && ((0x80 & getChangingConfigurations()) == 0))
      if (!paramBoolean1)
        break label294;
    label294: for (int i = 2131296338; ; i = 2131296339)
    {
      Toast.makeText(this, i, 1).show();
      discardChanges();
      updateUi();
      if (paramBoolean1)
        break;
      finish();
      return;
    }
  }

  static void sendOrSaveInternal(Context paramContext, String paramString1, String paramString2, String paramString3, Spanned paramSpanned, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString4, CharSequence paramCharSequence, List<Gmail.Attachment> paramList, long paramLong, SendOrSaveCallback paramSendOrSaveCallback, Handler paramHandler, boolean paramBoolean1, boolean paramBoolean2)
  {
    ContentValues localContentValues = new ContentValues();
    Gmail.MessageModification.putToAddresses(localContentValues, paramArrayOfString1);
    Gmail.MessageModification.putCcAddresses(localContentValues, paramArrayOfString2);
    Gmail.MessageModification.putBccAddresses(localContentValues, paramArrayOfString3);
    Gmail.MessageModification.putSubject(localContentValues, paramString4);
    String str1 = Html.toHtml(paramSpanned);
    boolean bool;
    StringBuilder localStringBuilder;
    if (!TextUtils.isEmpty(paramCharSequence))
    {
      bool = true;
      localStringBuilder = new StringBuilder(str1);
      if (bool)
      {
        if (!paramBoolean2)
          break label162;
        localStringBuilder.append(paramCharSequence);
      }
    }
    while (true)
    {
      Gmail.MessageModification.putBody(localContentValues, localStringBuilder.toString());
      Gmail.MessageModification.putCustomFromAddress(localContentValues, paramString3);
      Gmail.MessageModification.putAttachments(localContentValues, paramList);
      SendOrSaveMessage localSendOrSaveMessage = new SendOrSaveMessage(paramString1, paramString2, localContentValues, paramLong, paramBoolean1);
      SendOrSaveTask localSendOrSaveTask = new SendOrSaveTask(paramContext, localSendOrSaveMessage, paramSendOrSaveCallback);
      paramSendOrSaveCallback.initializeSendOrSave(localSendOrSaveTask);
      paramHandler.post(localSendOrSaveTask);
      return;
      bool = false;
      break;
      label162: String str2 = paramCharSequence.toString();
      int i = str2.indexOf("<br type='attribution'>");
      if (i >= 0)
      {
        localStringBuilder.append(str2.substring(0, i + HEADER_SEPARATOR_LENGTH));
        int j = localStringBuilder.length();
        Gmail.MessageModification.putForward(localContentValues, paramBoolean2);
        Gmail.MessageModification.putIncludeQuotedText(localContentValues, bool);
        Gmail.MessageModification.putQuoteStartPos(localContentValues, j);
      }
      else
      {
        Log.w("Gmail", "Couldn't find quoted text");
        localStringBuilder.append(str2);
      }
    }
  }

  private void setQuotedSectionVisibility(boolean paramBoolean)
  {
    this.mComposeArea.setQuotedSectionVisibility(paramBoolean);
  }

  private boolean shouldSave()
  {
    while (true)
    {
      synchronized (this.mDraftIdLock)
      {
        if ((!this.mTextChanged) && (!this.mAttachmentAddedOrRemoved))
        {
          if (!this.mReplyFromChanged)
            break label45;
          break label40;
          return bool;
        }
      }
      label40: boolean bool = true;
      continue;
      label45: bool = false;
    }
  }

  private void showFromSpinner(boolean paramBoolean)
  {
    View localView1 = findViewById(2131361808);
    int i;
    View localView2;
    if (paramBoolean)
    {
      i = 8;
      localView1.setVisibility(i);
      localView2 = findViewById(2131361806);
      if (!paramBoolean)
        break label49;
    }
    label49: for (int j = 0; ; j = 8)
    {
      localView2.setVisibility(j);
      return;
      i = 0;
      break;
    }
  }

  private void showRecipientErrorDialog(String paramString)
  {
    this.mComposeArea.showRecipientErrorDialog(paramString);
  }

  private void showSendConfirmDialog(int paramInt, DialogInterface.OnClickListener paramOnClickListener)
  {
    new AlertDialog.Builder(this).setMessage(paramInt).setTitle(2131296495).setIcon(17301543).setPositiveButton(2131296489, paramOnClickListener).setNegativeButton(2131296490, this).show();
  }

  private void switchToForward()
  {
    if (this.mCurrentMode != 2)
    {
      this.mCurrentMode = 2;
      this.mForward = true;
      onSwitchState();
    }
  }

  private void switchToReply()
  {
    if (this.mCurrentMode != 0)
    {
      this.mCurrentMode = 0;
      this.mForward = false;
      onSwitchState();
    }
  }

  private void switchToReplyAll()
  {
    if (this.mCurrentMode != 1)
    {
      this.mCurrentMode = 1;
      this.mForward = false;
      onSwitchState();
    }
  }

  private long updateFromDraftCursor()
  {
    long l = 0L;
    Gmail.MessageCursor localMessageCursor1 = this.mGmail.getMessageCursorForMessageId(this.mAccount, this.mDraftId);
    Gmail.MessageCursor localMessageCursor2 = null;
    try
    {
      boolean bool1 = localMessageCursor1.next();
      localMessageCursor2 = null;
      if (bool1)
      {
        l = localMessageCursor1.getRefMessageId();
        boolean bool2 = l < 0L;
        localMessageCursor2 = null;
        if (bool2)
          localMessageCursor2 = this.mGmail.getLocalMessageCursorForQuery(this.mAccount, String.valueOf(l));
        this.mComposeArea.updateFromCursor(localMessageCursor1, localMessageCursor2, this.mCurrentMode);
        updateSendOptions();
      }
      return l;
    }
    finally
    {
      localMessageCursor1.release();
      localMessageCursor1.getCursor().close();
      if (localMessageCursor2 != null)
      {
        localMessageCursor2.release();
        localMessageCursor2.getCursor().close();
      }
    }
  }

  private void updateMessage(int paramInt)
  {
    if (this.mIsDraft)
    {
      updateFromDraftCursor();
      return;
    }
    initFromRefMessage(this.mRefMessageId, this.mCurrentMode);
  }

  private void updateSendOptions()
  {
    this.mLayoutImpl.updateComposeMode(this.mCurrentMode);
  }

  public void afterTextChanged(Editable paramEditable)
  {
    this.mTextChanged = true;
    updateUi();
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void doAttach()
  {
    Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
    localIntent.addCategory("android.intent.category.OPENABLE");
    if (Settings.System.getInt(getContentResolver(), "com.google.android.gm.allowAddAnyAttachment", 0) != 0)
      localIntent.setType("*/*");
    while (true)
    {
      startActivityForResult(Intent.createChooser(localIntent, getText(2131296337)), 1);
      return;
      localIntent.setType("image/*");
    }
  }

  public void doDiscard(boolean paramBoolean)
  {
    synchronized (this.mDraftIdLock)
    {
      long l = this.mDraftId;
      if ((l != 0L) || (shouldSave()))
      {
        doDiscard();
        return;
      }
    }
    doDiscardWithoutConfirmation(true);
  }

  public void doSave(boolean paramBoolean)
  {
    if (shouldSave())
      sendOrSaveWithSanityChecks(this.mComposeArea.getBodyText(), true, paramBoolean);
  }

  public void doSend(boolean paramBoolean)
  {
    this.mLayoutImpl.enableSend(false);
    UserHappinessSignals.userAcceptedImeText(getBaseContext());
    if (!sendOrSaveWithSanityChecks(this.mComposeArea.getBodyText(), false, paramBoolean))
      this.mLayoutImpl.enableSend(true);
  }

  public Spanned getBodyText()
  {
    return this.mComposeArea.getBodyText();
  }

  protected String getInitialAccount()
  {
    return WaitActivity.waitIfNeededAndGetAccount(this);
  }

  protected String getMatchingRecipient(String paramString, List<String> paramList)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator1 = paramList.iterator();
    while (localIterator1.hasNext())
    {
      Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize((String)localIterator1.next());
      for (int j = 0; j < arrayOfRfc822Token.length; j++)
        localHashSet.add(arrayOfRfc822Token[j].getAddress());
    }
    String str = paramString;
    Collection localCollection = CustomFromUtils.getCustomReplyFrom(paramString);
    int i = 0;
    if (localCollection != null)
    {
      Iterator localIterator2 = localCollection.iterator();
      while (localIterator2.hasNext())
      {
        String[] arrayOfString = (String[])localIterator2.next();
        if (localHashSet.contains(arrayOfString[ACCOUNT_ADDRESS]))
        {
          str = arrayOfString[ACCOUNT_ADDRESS];
          i++;
        }
      }
    }
    if (i > 1)
      str = CustomFromUtils.getDefaultCustomFrom(paramString);
    return str;
  }

  protected void initFromExtras(Intent paramIntent)
  {
    this.mComposeArea.initFromExtras(paramIntent);
  }

  protected final void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt2 != -1);
    do
    {
      return;
      if (paramInt1 == 1)
      {
        this.mComposeArea.addAttachmentAndUpdateView(paramIntent);
        return;
      }
    }
    while (paramInt1 != 2);
    this.mAccount = this.mPrefs.getActiveAccount(this);
    finishOnCreateAfterAccountSelected(null, getIntent());
  }

  public void onAttachmentsChanged()
  {
    this.mAttachmentAddedOrRemoved = true;
    updateUi();
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    switch (paramInt)
    {
    default:
      return;
    case -1:
      doDiscardWithoutConfirmation(true);
      return;
    case -2:
    }
    this.mLayoutImpl.enableSend(true);
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    this.mLayoutImpl.onOrientationChanged(paramConfiguration);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mAccount = getInitialAccount();
    this.mRecipient = this.mAccount;
    if (this.mAccount == null)
      return;
    this.mPrefs = Persistence.getInstance(this);
    this.mComposeArea = new ComposeArea(this, this, this.mPrefs, this.mAccount);
    this.mComposeArea.renderComposeArea();
    this.mLayoutImpl = ComposeLayout.newInstance(this, this);
    this.mLayoutImpl.setComposeArea(this.mComposeArea);
    this.mLayoutImpl.setupLayout();
    this.mDraftIdLock = new Object();
    this.mAccountSpinnerReady = false;
    this.mGmail = LongShadowUtils.getContentProviderMailAccess(getContentResolver());
    this.mForward = false;
    Intent localIntent = getIntent();
    if (localIntent.getExtras() != null)
      this.mCurrentMode = localIntent.getExtras().getInt("action");
    finishOnCreateAfterAccountSelected(paramBundle, localIntent);
    this.mTextChanged = false;
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    super.onCreateOptionsMenu(paramMenu);
    return this.mLayoutImpl.onCreateOptionsMenu(paramMenu, this.mMessageIsForwardOrReply);
  }

  protected void onDestroy()
  {
    if (this.mAddressAdapter != null)
      this.mAddressAdapter.unregister();
    super.onDestroy();
  }

  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    String[] arrayOfString = (String[])mAccountSpinner.getSelectedItem();
    if ((!arrayOfString[REAL_ACCOUNT].equals(this.mCurrentReplyFromAccount[REAL_ACCOUNT])) || (!arrayOfString[ACCOUNT_ADDRESS].equals(this.mCurrentReplyFromAccount[ACCOUNT_ADDRESS])))
    {
      this.mLayoutImpl.enableSave(true);
      this.mCurrentReplyFromAccount = arrayOfString;
      this.mReplyFromChanged = true;
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
      while (true)
      {
        synchronized (this.mDraftIdLock)
        {
          if ((this.mDraftId != 0L) && (!shouldSave()))
          {
            finish();
            return true;
          }
          if (shouldSave())
            doDiscard(true);
        }
        doDiscardWithoutConfirmation(true);
      }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public void onNothingSelected(AdapterView<?> paramAdapterView)
  {
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
      return super.onOptionsItemSelected(paramMenuItem);
    case 2131361824:
      doSend(true);
      return true;
    case 2131361825:
      doSave(true);
      return true;
    case 2131361920:
      this.mLayoutImpl.hideOrShowCcBcc(false, true);
      return true;
    case 2131361921:
      this.mLayoutImpl.hideOrShowCcBcc(true, true);
      return true;
    case 2131361923:
      doDiscard(true);
      return true;
    case 2131361922:
      doAttach();
      return true;
    case 2131361937:
      Utils.showSettings(this);
      return true;
    case 2131361938:
      Utils.showAbout(this);
      return true;
    case 2131361924:
    }
    Utils.showHelp(this);
    return true;
  }

  protected void onPause()
  {
    super.onPause();
    saveIfNeeded();
    this.mComposeArea.dismissAllDialogs();
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    super.onPrepareOptionsMenu(paramMenu);
    boolean bool = this.mLayoutImpl.onPrepareOptionsMenu(paramMenu);
    updateSendOptions();
    return bool;
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    if (paramBundle != null)
    {
      if (paramBundle.containsKey("focusSelectionStart"))
      {
        int i = paramBundle.getInt("focusSelectionStart");
        int j = paramBundle.getInt("focusSelectionEnd");
        EditText localEditText = (EditText)getCurrentFocus();
        int k = localEditText.getText().length();
        if ((i < k) && (j < k))
          localEditText.setSelection(i, j);
      }
      this.mComposeArea.onRestoreInstanceState(paramBundle);
    }
  }

  protected void onResume()
  {
    super.onResume();
    asyncInitFromSpinner();
    updateUi();
  }

  public final void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    saveIfNeeded();
    paramBundle.putLong("draftId", this.mDraftId);
    Bundle localBundle = this.mComposeArea.onSaveInstanceState(paramBundle);
    View localView = getCurrentFocus();
    if ((localView != null) && ((localView instanceof EditText)))
    {
      EditText localEditText = (EditText)localView;
      localBundle.putInt("focusSelectionStart", localEditText.getSelectionStart());
      localBundle.putInt("focusSelectionEnd", localEditText.getSelectionEnd());
    }
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void onUiChanged()
  {
    updateUi();
  }

  protected boolean sendOrSaveWithSanityChecks(final Spanned paramSpanned, final boolean paramBoolean1, final boolean paramBoolean2)
  {
    String[] arrayOfString1 = this.mComposeArea.getToAddresses();
    String[] arrayOfString2 = this.mComposeArea.getCcAddresses();
    String[] arrayOfString3 = this.mComposeArea.getBccAddresses();
    if ((!paramBoolean1) && (arrayOfString1.length == 0) && (arrayOfString2.length == 0) && (arrayOfString3.length == 0))
    {
      showRecipientErrorDialog(getString(2131296341));
      return false;
    }
    ArrayList localArrayList = new ArrayList();
    checkInvalidEmails(arrayOfString1, localArrayList);
    checkInvalidEmails(arrayOfString2, localArrayList);
    checkInvalidEmails(arrayOfString3, localArrayList);
    if (localArrayList.size() > 0)
    {
      String str = getString(2131296342);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localArrayList.get(0);
      showRecipientErrorDialog(String.format(str, arrayOfObject));
      return false;
    }
    DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        ComposeActivity.this.sendOrSave(paramSpanned, paramBoolean1, paramBoolean2);
      }
    };
    if ((!paramBoolean1) && (showEmptyTextWarnings()))
    {
      boolean bool = this.mComposeArea.isSubjectEmpty();
      int i;
      if (TextUtils.getTrimmedLength(paramSpanned) == 0)
      {
        i = 1;
        if ((i == 0) || ((this.mForward) && (!this.mComposeArea.isBodyEmpty())))
          break label235;
      }
      label235: for (int j = 1; ; j = 0)
      {
        if (!bool)
          break label241;
        showSendConfirmDialog(2131296492, local2);
        return true;
        i = 0;
        break;
      }
      label241: if (j != 0)
      {
        showSendConfirmDialog(2131296493, local2);
        return true;
      }
      if (showSendConfirmation())
      {
        showSendConfirmDialog(2131296494, local2);
        return true;
      }
    }
    sendOrSave(paramSpanned, paramBoolean1, paramBoolean2);
    return true;
  }

  public void setComposeMode(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return;
    case 0:
      switchToReply();
      return;
    case 1:
      switchToReplyAll();
      return;
    case 2:
    }
    switchToForward();
  }

  protected boolean showEmptyTextWarnings()
  {
    return true;
  }

  protected boolean showSendConfirmation()
  {
    return Persistence.getInstance(this).getConfirmSend(this);
  }

  public void updateHideOrShowCcBcc(boolean paramBoolean)
  {
    this.mLayoutImpl.hideOrShowCcBcc(paramBoolean, false);
  }

  public void updateUi()
  {
    ComposeLayout localComposeLayout = this.mLayoutImpl;
    if ((shouldSave()) && (!isBlank()));
    for (boolean bool = true; ; bool = false)
    {
      localComposeLayout.enableSave(bool);
      return;
    }
  }

  private static class FromAddressSpinnerAdapter extends ArrayAdapter<String[]>
  {
    private LayoutInflater mInflater;

    public FromAddressSpinnerAdapter(Context paramContext)
    {
      super(2130903057, 2131361841);
    }

    private LayoutInflater getInflater()
    {
      if (this.mInflater == null)
        this.mInflater = ((LayoutInflater)getContext().getSystemService("layout_inflater"));
      return this.mInflater;
    }

    public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      String[] arrayOfString = (String[])getItem(paramInt);
      if (arrayOfString[ComposeActivity.IS_CUSTOMFROM].equals("true"))
      {
        if ((paramView != null) && (paramView.findViewById(2131361842) != null));
        for (View localView2 = paramView; ; localView2 = getInflater().inflate(2130903052, null))
        {
          ((TextView)localView2.findViewById(2131361841)).setText(arrayOfString[ComposeActivity.ACCOUNT_DISPLAY]);
          TextView localTextView = (TextView)localView2.findViewById(2131361842);
          Resources localResources = getContext().getResources();
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = arrayOfString[ComposeActivity.ACCOUNT_ADDRESS];
          localTextView.setText(localResources.getString(2131296256, arrayOfObject));
          return localView2;
        }
      }
      if ((paramView != null) && (paramView.findViewById(2131361842) == null));
      for (View localView1 = paramView; ; localView1 = getInflater().inflate(2130903056, null))
      {
        ((TextView)localView1.findViewById(2131361841)).setText(arrayOfString[ComposeActivity.ACCOUNT_DISPLAY]);
        return localView1;
      }
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      String[] arrayOfString = (String[])getItem(paramInt);
      if (arrayOfString[ComposeActivity.IS_CUSTOMFROM].equals("true"))
      {
        if ((paramView != null) && (paramView.findViewById(2131361842) != null));
        for (View localView2 = paramView; ; localView2 = getInflater().inflate(2130903053, null))
        {
          ((TextView)localView2.findViewById(2131361841)).setText(arrayOfString[ComposeActivity.ACCOUNT_DISPLAY]);
          TextView localTextView = (TextView)localView2.findViewById(2131361842);
          Resources localResources = getContext().getResources();
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = arrayOfString[ComposeActivity.ACCOUNT_ADDRESS];
          localTextView.setText(localResources.getString(2131296256, arrayOfObject));
          return localView2;
        }
      }
      if ((paramView != null) && (paramView.findViewById(2131361842) == null));
      for (View localView1 = paramView; ; localView1 = getInflater().inflate(2130903057, null))
      {
        ((TextView)localView1.findViewById(2131361841)).setText(arrayOfString[ComposeActivity.ACCOUNT_ADDRESS]);
        return localView1;
      }
    }
  }

  static abstract interface SendOrSaveCallback
  {
    public abstract long getMessageId();

    public abstract void initializeSendOrSave(ComposeActivity.SendOrSaveTask paramSendOrSaveTask);

    public abstract void notifyMessageIdAllocated(long paramLong);

    public abstract void sendOrSaveFinished(ComposeActivity.SendOrSaveTask paramSendOrSaveTask);
  }

  static class SendOrSaveMessage
  {
    String mAccount;
    long mRefMessageId;
    boolean mSave;
    String mSelectedAccount;
    ContentValues mValues;

    public SendOrSaveMessage(String paramString1, String paramString2, ContentValues paramContentValues, long paramLong, boolean paramBoolean)
    {
      this.mAccount = paramString1;
      this.mSelectedAccount = paramString2;
      this.mValues = paramContentValues;
      this.mRefMessageId = paramLong;
      this.mSave = paramBoolean;
    }
  }

  static class SendOrSaveTask
    implements Runnable
  {
    private final Context mContext;
    private final ComposeActivity.SendOrSaveCallback mSendOrSaveCallback;
    private final ComposeActivity.SendOrSaveMessage mSendOrSaveMessage;

    public SendOrSaveTask(Context paramContext, ComposeActivity.SendOrSaveMessage paramSendOrSaveMessage, ComposeActivity.SendOrSaveCallback paramSendOrSaveCallback)
    {
      this.mContext = paramContext;
      this.mSendOrSaveCallback = paramSendOrSaveCallback;
      this.mSendOrSaveMessage = paramSendOrSaveMessage;
    }

    private static void appendAddresses(ArrayList<String> paramArrayList, String paramString)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        String[] arrayOfString = TextUtils.split(paramString, Gmail.EMAIL_SEPARATOR_PATTERN);
        int i = arrayOfString.length;
        for (int j = 0; j < i; j++)
          paramArrayList.add(Gmail.getEmailFromAddressString(arrayOfString[j]));
      }
    }

    static ArrayList<String> getRecipientsList(ComposeActivity.SendOrSaveMessage paramSendOrSaveMessage)
    {
      String str1 = (String)paramSendOrSaveMessage.mValues.get("toAddresses");
      String str2 = (String)paramSendOrSaveMessage.mValues.get("ccAddresses");
      String str3 = (String)paramSendOrSaveMessage.mValues.get("bccAddresses");
      ArrayList localArrayList = new ArrayList();
      appendAddresses(localArrayList, str1);
      appendAddresses(localArrayList, str2);
      appendAddresses(localArrayList, str3);
      return localArrayList;
    }

    void incrementRecipientsTimesContacted(ArrayList<String> paramArrayList)
    {
      ArrayList localArrayList1 = new ArrayList();
      StringBuilder localStringBuilder = new StringBuilder();
      String[] arrayOfString1 = new String[paramArrayList.size()];
      localArrayList1.addAll(paramArrayList);
      Arrays.fill(arrayOfString1, "?");
      localStringBuilder.append("data1 IN (").append(TextUtils.join(",", arrayOfString1)).append(")");
      ContentResolver localContentResolver = this.mContext.getContentResolver();
      Cursor localCursor = localContentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, new String[] { "contact_id" }, localStringBuilder.toString(), (String[])localArrayList1.toArray(new String[0]), null);
      ArrayList localArrayList2 = new ArrayList();
      try
      {
        if (localCursor.moveToNext())
          localArrayList2.add(Long.valueOf(localCursor.getLong(0)));
      }
      finally
      {
        localCursor.close();
      }
      localStringBuilder.setLength(0);
      localArrayList1.clear();
      String[] arrayOfString2 = new String[localArrayList2.size()];
      Iterator localIterator = localArrayList2.iterator();
      while (localIterator.hasNext())
        localArrayList1.add(String.valueOf((Long)localIterator.next()));
      Arrays.fill(arrayOfString2, "?");
      localStringBuilder.append("_id IN (").append(TextUtils.join(",", arrayOfString2)).append(")");
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("last_time_contacted", Long.valueOf(System.currentTimeMillis()));
      localContentResolver.update(ContactsContract.Contacts.CONTENT_URI, localContentValues, localStringBuilder.toString(), (String[])localArrayList1.toArray(new String[0]));
    }

    public void run()
    {
      ComposeActivity.SendOrSaveMessage localSendOrSaveMessage = this.mSendOrSaveMessage;
      String str = localSendOrSaveMessage.mSelectedAccount;
      long l1 = this.mSendOrSaveCallback.getMessageId();
      if (!str.equals(localSendOrSaveMessage.mAccount))
      {
        if (l1 != 0L)
        {
          Gmail.MessageModification.expungeMessage(this.mContext.getContentResolver(), localSendOrSaveMessage.mAccount, l1);
          l1 = 0L;
        }
        if (MailEngine.getOrMakeMailEngine(this.mContext, str) == null)
        {
          Log.wtf("Gmail", "MailEngine couldn't be instantiated");
          str = localSendOrSaveMessage.mAccount;
        }
      }
      if (l1 != 0L)
        Gmail.MessageModification.sendOrSaveExistingMessage(this.mContext.getContentResolver(), str, l1, localSendOrSaveMessage.mValues, localSendOrSaveMessage.mSave);
      while (true)
      {
        if (!localSendOrSaveMessage.mSave)
          incrementRecipientsTimesContacted(getRecipientsList(localSendOrSaveMessage));
        this.mSendOrSaveCallback.sendOrSaveFinished(this);
        return;
        ContentResolver localContentResolver = this.mContext.getContentResolver();
        ContentValues localContentValues = localSendOrSaveMessage.mValues;
        long l2 = localSendOrSaveMessage.mRefMessageId;
        boolean bool = localSendOrSaveMessage.mSave;
        Long localLong = Long.valueOf(Gmail.MessageModification.sendOrSaveNewMessage(localContentResolver, str, localContentValues, l2, bool));
        this.mSendOrSaveCallback.notifyMessageIdAllocated(localLong.longValue());
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ComposeActivity
 * JD-Core Version:    0.6.2
 */