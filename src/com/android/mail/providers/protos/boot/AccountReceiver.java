package com.android.mail.providers.protos.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.android.mail.providers.MailAppProvider;

public class AccountReceiver extends BroadcastReceiver
{
  private static final Uri GMAIL_ACCOUNTS_URI = Uri.parse("content://com.android.gmail.ui/accounts");

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    MailAppProvider.addAccountsForUriAsync(GMAIL_ACCOUNTS_URI);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.protos.boot.AccountReceiver
 * JD-Core Version:    0.6.2
 */