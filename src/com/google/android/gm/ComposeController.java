package com.google.android.gm;

import android.text.TextWatcher;

public abstract interface ComposeController extends TextWatcher
{
  public abstract void doAttach();

  public abstract void doDiscard(boolean paramBoolean);

  public abstract void doSave(boolean paramBoolean);

  public abstract void doSend(boolean paramBoolean);

  public abstract void onAttachmentsChanged();

  public abstract void onUiChanged();

  public abstract void setComposeMode(int paramInt);

  public abstract void updateHideOrShowCcBcc(boolean paramBoolean);
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ComposeController
 * JD-Core Version:    0.6.2
 */