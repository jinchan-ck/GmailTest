package com.google.android.gm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AccountsChangedReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if ("android.accounts.LOGIN_ACCOUNTS_CHANGED".equals(paramIntent.getAction()))
    {
      paramIntent.setClass(paramContext, MailIntentService.class);
      paramContext.startService(paramIntent);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.AccountsChangedReceiver
 * JD-Core Version:    0.6.2
 */