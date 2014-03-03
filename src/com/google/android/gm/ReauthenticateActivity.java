package com.google.android.gm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ReauthenticateActivity extends GmailBaseActivity
  implements AccountHelper.CredentialsCallback
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    AccountHelper.promptForCredentials(this, getIntent().getData().getQueryParameter("account"), this);
  }

  public void onCredentialsResult(boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = -1; ; i = 0)
    {
      setResult(i);
      finish();
      return;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ReauthenticateActivity
 * JD-Core Version:    0.6.2
 */