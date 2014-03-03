package com.android.mail.ui;

import android.app.Activity;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;

public abstract class AbstractMailActivity extends Activity
  implements RestrictedActivity
{
  private static AbstractMailActivity sForegroundInstance;
  private NdefMessage mForegroundNdef;
  private NfcAdapter mNfcAdapter;
  private final UiHandler mUiHandler = new UiHandler();

  public Context getActivityContext()
  {
    return this;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    if (this.mNfcAdapter != null);
    this.mUiHandler.setEnabled(true);
  }

  protected void onPause()
  {
    super.onPause();
    try
    {
      if ((this.mNfcAdapter != null) && (this.mForegroundNdef != null))
        this.mNfcAdapter.disableForegroundNdefPush(this);
      sForegroundInstance = null;
      return;
    }
    finally
    {
    }
  }

  protected void onResume()
  {
    super.onResume();
    try
    {
      sForegroundInstance = this;
      if ((this.mNfcAdapter != null) && (this.mForegroundNdef != null))
        this.mNfcAdapter.enableForegroundNdefPush(this, this.mForegroundNdef);
      this.mUiHandler.setEnabled(true);
      return;
    }
    finally
    {
    }
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
 * Qualified Name:     com.android.mail.ui.AbstractMailActivity
 * JD-Core Version:    0.6.2
 */