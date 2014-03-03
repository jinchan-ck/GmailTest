package com.android.mail.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.net.Uri;
import android.text.TextUtils;
import com.android.mail.ConversationListContext;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;

public class SuppressNotificationReceiver extends BroadcastReceiver
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private Context mContext;
  private AbstractActivityController mController;
  private String mMimeType;

  public boolean activate(Context paramContext, AbstractActivityController paramAbstractActivityController)
  {
    Account localAccount = paramAbstractActivityController.getCurrentAccount();
    this.mContext = paramContext;
    this.mController = paramAbstractActivityController;
    IntentFilter localIntentFilter = new IntentFilter("com.android.mail.action.update_notification");
    localIntentFilter.setPriority(0);
    if (localAccount != null)
      this.mMimeType = localAccount.mimeType;
    while (true)
    {
      try
      {
        localIntentFilter.addDataType(this.mMimeType);
        paramContext.registerReceiver(this, localIntentFilter);
        return true;
      }
      catch (IntentFilter.MalformedMimeTypeException localMalformedMimeTypeException)
      {
        String str = LOG_TAG;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mMimeType;
        LogUtils.wtf(str, "Malformed mimetype: %s", arrayOfObject);
        continue;
      }
      LogUtils.d(LOG_TAG, "Registering receiver with no mime type", new Object[0]);
    }
  }

  public boolean activated()
  {
    return this.mContext != null;
  }

  public void deactivate()
  {
    try
    {
      if (this.mContext != null)
      {
        this.mContext.unregisterReceiver(this);
        this.mContext = null;
        this.mMimeType = null;
      }
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
  }

  public boolean notificationsDisabledForAccount(Account paramAccount)
  {
    return (this.mContext != null) && (TextUtils.equals(paramAccount.mimeType, this.mMimeType));
  }

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (!"com.android.mail.action.update_notification".equals(paramIntent.getAction()));
    label12: Folder localFolder;
    Uri localUri2;
    do
    {
      Account localAccount;
      Uri localUri1;
      do
      {
        ConversationListContext localConversationListContext;
        do
        {
          break label12;
          do
            return;
          while (!this.mController.isConversationListVisible());
          localConversationListContext = this.mController.getCurrentListContext();
          if (localConversationListContext == null)
          {
            LogUtils.e(LOG_TAG, "unexpected null context", new Object[0]);
            return;
          }
        }
        while (ConversationListContext.isSearchResult(localConversationListContext));
        localAccount = localConversationListContext.account;
        localFolder = localConversationListContext.folder;
        if ((localAccount == null) || (localFolder == null))
        {
          LogUtils.e(LOG_TAG, "SuppressNotificationReceiver.onReceive: account=%s, folder=%s", new Object[] { localAccount, localFolder });
          return;
        }
        localUri1 = (Uri)paramIntent.getParcelableExtra("notification_extra_account");
      }
      while (!localAccount.uri.equals(localUri1));
      localUri2 = (Uri)paramIntent.getParcelableExtra("notification_extra_folder");
    }
    while ((!localFolder.uri.equals(localUri2)) || (paramIntent.getIntExtra("notification_updated_unread_count", 0) == 0));
    LogUtils.i(LOG_TAG, "Aborting broadcast of intent %s, folder uri is %s", new Object[] { paramIntent, localUri2 });
    abortBroadcast();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.SuppressNotificationReceiver
 * JD-Core Version:    0.6.2
 */