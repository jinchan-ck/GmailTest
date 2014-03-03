package com.android.mail.providers;

import android.content.Context;
import android.content.Intent;
import com.google.android.gm.AddAccountActivity;

public class GmailAccountCacheProvider extends MailAppProvider
{
  protected String getAuthority()
  {
    return "com.google.android.gm2.accountcache";
  }

  protected Intent getNoAccountsIntent(Context paramContext)
  {
    return new Intent(paramContext, AddAccountActivity.class);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.GmailAccountCacheProvider
 * JD-Core Version:    0.6.2
 */