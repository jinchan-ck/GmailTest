package com.google.android.gm;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

public class LabelsActivity extends GmailBaseActivity
  implements LabelListFragment.LabelListCallbacks, LabelSettingsObservable, LabelsActivityController.ControllableLabelsActivity
{
  private LabelsActivityController mController;

  public String getHelpContext()
  {
    return "gm_lsa";
  }

  public ArrayList<String> getIncludedLabels()
  {
    return this.mController.getIncludedLabels();
  }

  public int getNumberOfSyncDays()
  {
    return this.mController.getNumberOfSyncDays();
  }

  public ArrayList<String> getPartialLabels()
  {
    return this.mController.getPartialLabels();
  }

  public void notifyChanged()
  {
    this.mController.notifyChanged();
  }

  public void onBackPressed()
  {
    if (!this.mController.handleBackPressed())
      super.onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mController = ControllerFactory.forActivity(this);
    this.mController.initialize(paramBundle);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    return (this.mController.handleCreateOptionsMenu(paramMenu)) || (super.onCreateOptionsMenu(paramMenu));
  }

  public void onLabelListResumed(LabelListFragment paramLabelListFragment)
  {
    this.mController.handleLabelListResumed(paramLabelListFragment);
  }

  public void onLabelSelected(String paramString)
  {
    this.mController.handleLabelSelected(paramString);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    return (this.mController.handleOptionsItemSelected(paramMenuItem)) || (super.onOptionsItemSelected(paramMenuItem));
  }

  public void onPause()
  {
    super.onPause();
    this.mController.handlePause();
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return (this.mController.handlePrepareOptionsMenu(paramMenu)) || (super.onPrepareOptionsMenu(paramMenu));
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.mController.handleSaveInstanceState(paramBundle);
  }

  public void registerObserver(LabelSettingsObserver paramLabelSettingsObserver)
  {
    this.mController.registerObserver(paramLabelSettingsObserver);
  }

  public void setIncludedLabels(ArrayList<String> paramArrayList)
  {
    this.mController.setIncludedLabels(paramArrayList);
  }

  public void setPartialLabels(ArrayList<String> paramArrayList)
  {
    this.mController.setPartialLabels(paramArrayList);
  }

  public void unregisterObserver(LabelSettingsObserver paramLabelSettingsObserver)
  {
    this.mController.unregisterObserver(paramLabelSettingsObserver);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelsActivity
 * JD-Core Version:    0.6.2
 */