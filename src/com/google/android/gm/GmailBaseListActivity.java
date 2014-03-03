package com.google.android.gm;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;

public abstract class GmailBaseListActivity extends ListActivity
  implements RestrictedActivity
{
  private final UiHandler mUiHandler = new UiHandler();

  public Context getContext()
  {
    return this;
  }

  public UiHandler getUiHandler()
  {
    return this.mUiHandler;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mUiHandler.setEnabled(true);
  }

  protected void onResume()
  {
    super.onResume();
    this.mUiHandler.setEnabled(true);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.mUiHandler.setEnabled(false);
  }

  protected void onStart()
  {
    super.onStart();
    this.mUiHandler.setEnabled(true);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.GmailBaseListActivity
 * JD-Core Version:    0.6.2
 */