package com.google.android.gm;

import android.content.Intent;
import android.os.Bundle;

public class AutoSendActivity extends com.android.mail.compose.AutoSendActivity
{
  public void onCreate(Bundle paramBundle)
  {
    Intent localIntent = getIntent();
    if ((localIntent.getExtras() != null) && (localIntent.getExtras().containsKey("com.google.android.gm.extra.ACCOUNT")))
      localIntent.putExtra("fromAccountString", localIntent.getStringExtra("com.google.android.gm.extra.ACCOUNT"));
    super.onCreate(paramBundle);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.AutoSendActivity
 * JD-Core Version:    0.6.2
 */