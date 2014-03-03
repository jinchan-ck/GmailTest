package com.google.android.gm;

import android.content.Intent;
import android.os.Bundle;

public class ConversationListActivityShim extends GmailBaseActivity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    localIntent.setClass(this, ConversationListActivity.class);
    localIntent.setFlags(33554432);
    String str = Utils.getVersionCode(this);
    if ((str != null) && (Persistence.getInstance(this).getShouldShowWhatsNew(this, str)))
      localIntent.putExtra("show-whats-new", true);
    startActivity(localIntent);
    finish();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ConversationListActivityShim
 * JD-Core Version:    0.6.2
 */