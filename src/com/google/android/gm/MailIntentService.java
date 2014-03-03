package com.google.android.gm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.Bundle;
import com.google.android.gm.perf.Timer;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.provider.AttachmentManager;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.LogUtils;
import com.google.android.gm.provider.MailEngine;
import com.google.android.gm.provider.MailSync;
import com.google.android.gsf.GoogleLoginServiceConstants;
import java.io.IOException;
import java.util.ArrayList;

public class MailIntentService extends IntentService
{
  public MailIntentService()
  {
    super("MailIntentService");
  }

  private void handleAccountList(Account[] paramArrayOfAccount, boolean paramBoolean)
  {
    Utils.cacheGoogleAccountList(this, false, paramArrayOfAccount);
    if (!paramBoolean);
    while (true)
    {
      return;
      int i = paramArrayOfAccount.length;
      for (int j = 0; j < i; j++)
        MailEngine.getOrMakeMailEngineSync(this, paramArrayOfAccount[j].name).sendNotificationIntents(true);
    }
  }

  private void handleLocaleChanged(Intent paramIntent)
  {
    Utils.cancelAndResendNotifications(this);
  }

  private void handleProviderChangedIntent(Intent paramIntent)
  {
    Persistence localPersistence = Persistence.getInstance();
    String str = paramIntent.getExtras().getString("account");
    if (!localPersistence.getEnableNotifications(this, str));
    while (!localPersistence.shouldNotifyForLabel(this, str, paramIntent.getExtras().getString("notificationLabel")))
      return;
    Utils.setNewEmailIndicator(this, paramIntent);
  }

  private void postSendErrorNotification(Intent paramIntent)
  {
    String str1 = paramIntent.getStringExtra("account");
    String str2 = paramIntent.getStringExtra("extraMessageSubject");
    long l = paramIntent.getLongExtra("extraConversationId", -1L);
    Utils.createErrorNotification(getBaseContext(), str1, str2, l, 2131755042, 2131427803);
  }

  private void sendAccountsChangedNotification(boolean paramBoolean)
  {
    if (paramBoolean)
      Timer.startTiming("MIS.sendInitialNotifications");
    AccountManager localAccountManager = AccountManager.get(this);
    String[] arrayOfString = new String[1];
    arrayOfString[0] = GoogleLoginServiceConstants.featureForService("mail");
    AccountManagerFuture localAccountManagerFuture = localAccountManager.getAccountsByTypeAndFeatures("com.google", arrayOfString, null, null);
    try
    {
      handleAccountList((Account[])localAccountManagerFuture.getResult(), paramBoolean);
      return;
    }
    catch (OperationCanceledException localOperationCanceledException)
    {
      LogUtils.w("Gmail", localOperationCanceledException, "Unexpected exception trying to get accounts.", new Object[0]);
      return;
    }
    catch (IOException localIOException)
    {
      LogUtils.w("Gmail", localIOException, "Unexpected exception trying to get accounts.", new Object[0]);
      return;
    }
    catch (AuthenticatorException localAuthenticatorException)
    {
      LogUtils.w("Gmail", localAuthenticatorException, "Unexpected exception trying to get accounts.", new Object[0]);
      return;
    }
    finally
    {
      if (paramBoolean)
        Timer.stopTiming("MIS.sendInitialNotifications");
      stopSelf();
    }
  }

  protected void onHandleIntent(Intent paramIntent)
  {
    String str1;
    String str2;
    try
    {
      LogUtils.v("Gmail", "Handling intent %s", new Object[] { paramIntent });
      Utils.haveGoogleMailActivitiesBeenEnabled(this);
      str1 = paramIntent.getAction();
      if (("android.intent.action.BOOT_COMPLETED".equals(str1)) || ("android.intent.action.DEVICE_STORAGE_OK".equals(str1)) || ("android.intent.action.MY_PACKAGE_REPLACED".equals(str1)))
      {
        sendAccountsChangedNotification(true);
        return;
      }
      if (!"android.intent.action.DOWNLOAD_COMPLETE".equals(str1))
        break label227;
      Long localLong = Long.valueOf(paramIntent.getLongExtra("extra_download_id", -1L));
      str2 = AttachmentManager.getAccountFromDownloadId(localLong.longValue());
      if (str2 == null)
      {
        DownloadManager localDownloadManager = (DownloadManager)getSystemService("download");
        long[] arrayOfLong = new long[1];
        arrayOfLong[0] = localLong.longValue();
        localDownloadManager.remove(arrayOfLong);
        return;
      }
    }
    catch (SQLException localSQLException)
    {
      LogUtils.e("Gmail", localSQLException, "Error handling intent %s", new Object[] { paramIntent });
      return;
    }
    MailEngine localMailEngine = MailEngine.getOrMakeMailEngineSync(this, str2);
    Resources localResources = getBaseContext().getResources();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(localResources.getString(2131427771));
    localArrayList.add(localResources.getString(2131427772));
    paramIntent.putStringArrayListExtra(AttachmentManager.ATTACHMENT_FROM, localArrayList);
    localMailEngine.getAttachmentManager().handleDownloadManagerIntent(paramIntent);
    return;
    label227: if ("com.google.android.gm.intent.CLEAR_ALL_NEW_MAIL_NOTIFICATIONS".equals(str1))
    {
      Utils.clearAllNotfications(this);
      return;
    }
    if ("com.google.android.gm.intent.CLEAR_NEW_MAIL_NOTIFICATIONS".equals(str1))
    {
      Utils.clearLabelNotification(this, paramIntent.getStringExtra("account"), paramIntent.getStringExtra("label"));
      return;
    }
    if ("com.android.mail.action.update_notification".equals(str1))
    {
      handleProviderChangedIntent(paramIntent);
      return;
    }
    if ("android.accounts.LOGIN_ACCOUNTS_CHANGED".equals(str1))
    {
      Utils.enableShortcutIntentFilter(this);
      sendAccountsChangedNotification(false);
      return;
    }
    if ("android.intent.action.LOCALE_CHANGED".equals(str1))
    {
      handleLocaleChanged(paramIntent);
      return;
    }
    if ("com.google.android.gm.intent.VALIDATE_ACCOUNT_NOTIFICATIONS".equals(str1))
    {
      Utils.validateAccountNotifications(this, paramIntent.getStringExtra("account"));
      return;
    }
    if ("com.google.android.gm.intent.ACTION_POST_SEND_ERROR".equals(str1))
    {
      postSendErrorNotification(paramIntent);
      return;
    }
    if ("com.google.android.gm.intent.ACTION_PROVIDER_CREATED".equals(str1))
    {
      Intent localIntent = new Intent("com.android.mail.ACTION_NOTIFY_DATASET_CHANGED");
      localIntent.putExtra("update-all-widgets", true);
      localIntent.setType("application/gmail-ls");
      sendBroadcast(localIntent);
      return;
    }
    if ("com.google.android.gm.intent.ACTION_UPGRADE_SYNC_WINDOW".equals(str1))
    {
      MailSync localMailSync = MailEngine.getOrMakeMailEngineSync(this, paramIntent.getStringExtra("account")).getMailSync();
      long l = Gmail.getDefaultConversationAgeDays(this);
      if (localMailSync.getConversationAgeDays() < l)
      {
        Persistence.getInstance().setUpgradeSyncWindow(this, true);
        sendBroadcast(new Intent("com.google.android.gm.intent.ACTION_DISPLAY_SYNC_WINDOW_UPGRADE"));
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.MailIntentService
 * JD-Core Version:    0.6.2
 */