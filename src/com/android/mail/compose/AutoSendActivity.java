package com.android.mail.compose;

import android.content.Intent;
import android.os.Bundle;

public class AutoSendActivity extends ComposeActivity
{
  private boolean mDontSaveOrSend = false;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mDontSaveOrSend = getIntent().getBooleanExtra("dontSendOrSave", false);
    sendOrSaveWithSanityChecks(false, true);
  }

  protected boolean sendOrSaveWithSanityChecks(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mDontSaveOrSend)
      return false;
    return super.sendOrSaveWithSanityChecks(paramBoolean1, paramBoolean2, false, true);
  }

  protected boolean showEmptyTextWarnings()
  {
    return false;
  }

  protected boolean showSendConfirmation()
  {
    return false;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.AutoSendActivity
 * JD-Core Version:    0.6.2
 */