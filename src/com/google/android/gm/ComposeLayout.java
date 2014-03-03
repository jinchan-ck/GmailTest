package com.google.android.gm;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.Menu;

public abstract class ComposeLayout
{
  public static ComposeLayout newInstance(Activity paramActivity, ComposeController paramComposeController)
  {
    return new DefaultComposeLayout(paramActivity, paramComposeController);
  }

  public abstract void enableSave(boolean paramBoolean);

  public abstract void enableSend(boolean paramBoolean);

  public abstract void hideOrShowCcBcc(boolean paramBoolean1, boolean paramBoolean2);

  public abstract boolean onCreateOptionsMenu(Menu paramMenu, boolean paramBoolean);

  public abstract void onOrientationChanged(Configuration paramConfiguration);

  public abstract boolean onPrepareOptionsMenu(Menu paramMenu);

  public abstract void setComposeArea(ComposeArea paramComposeArea);

  public abstract void setupButtons();

  public abstract void setupLayout();

  public abstract void updateComposeMode(int paramInt);
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ComposeLayout
 * JD-Core Version:    0.6.2
 */