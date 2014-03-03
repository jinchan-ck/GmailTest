package com.google.android.gm;

import android.content.Intent;
import android.os.Bundle;

public class ComposeActivityShim extends GmailBaseActivity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    localIntent.setClass(this, ComposeActivity.class);
    localIntent.setFlags(33554432);
    startActivity(localIntent);
    finish();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ComposeActivityShim
 * JD-Core Version:    0.6.2
 */