package com.google.android.gm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.android.mail.providers.Account;
import com.android.mail.utils.AccountUtils;
import com.google.android.gm.ui.FolderSelectionActivityGmail;
import com.google.android.gm.ui.MailboxSelectionActivityGmail;

public class CreateShortcutActivityGmail extends Activity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Account[] arrayOfAccount = AccountUtils.getSyncingAccounts(this);
    Intent localIntent = getIntent();
    if ((arrayOfAccount != null) && (arrayOfAccount.length == 1))
    {
      localIntent.setClass(this, FolderSelectionActivityGmail.class);
      localIntent.setFlags(1107296256);
      localIntent.setAction("android.intent.action.CREATE_SHORTCUT");
      localIntent.putExtra("account-shortcut", arrayOfAccount[0]);
    }
    while (true)
    {
      startActivity(localIntent);
      finish();
      return;
      localIntent.setClass(this, MailboxSelectionActivityGmail.class);
      localIntent.setFlags(33554432);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.CreateShortcutActivityGmail
 * JD-Core Version:    0.6.2
 */