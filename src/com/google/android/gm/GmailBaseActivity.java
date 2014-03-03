package com.google.android.gm;

import android.app.Activity;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.provider.MailEngine;
import com.google.android.gm.provider.MailEngine.MailEngineResultCallback;

public abstract class GmailBaseActivity extends Activity
  implements ApplicationMenuHandler.HelpCallback, RestrictedActivity
{
  private static GmailBaseActivity sForegroundInstance;
  private NdefMessage mForegroundNdef;
  private NfcAdapter mNfcAdapter;
  MailEngine.MailEngineResultCallback mOnMailEnginePrepared = new MailEngine.MailEngineResultCallback()
  {
    public void onMailEngineResult(MailEngine paramAnonymousMailEngine)
    {
    }
  };
  private final UiHandler mUiHandler = new UiHandler();

  public Context getContext()
  {
    return this;
  }

  public String getHelpContext()
  {
    return "gm";
  }

  public UiHandler getUiHandler()
  {
    return this.mUiHandler;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    if (this.mNfcAdapter != null)
    {
      String str = Persistence.getInstance().getActiveAccount(this);
      if (str != null)
        MailEngine.getOrMakeMailEngineAsync(this, str, this.mOnMailEnginePrepared);
    }
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
 * Qualified Name:     com.google.android.gm.GmailBaseActivity
 * JD-Core Version:    0.6.2
 */