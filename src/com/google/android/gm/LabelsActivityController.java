package com.google.android.gm;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public abstract interface LabelsActivityController extends LabelSettingsObservable
{
  public abstract boolean handleBackPressed();

  public abstract boolean handleCreateOptionsMenu(Menu paramMenu);

  public abstract void handleLabelListResumed(LabelListFragment paramLabelListFragment);

  public abstract void handleLabelSelected(String paramString);

  public abstract boolean handleOptionsItemSelected(MenuItem paramMenuItem);

  public abstract void handlePause();

  public abstract boolean handlePrepareOptionsMenu(Menu paramMenu);

  public abstract void handleSaveInstanceState(Bundle paramBundle);

  public abstract void initialize(Bundle paramBundle);

  public static abstract interface ControllableLabelsActivity extends RestrictedActivity
  {
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelsActivityController
 * JD-Core Version:    0.6.2
 */