package com.google.android.gm;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.google.android.gm.provider.Label;
import com.google.android.gm.provider.LabelManager;

public class OnePaneLabelsController extends BaseLabelsController
{
  private boolean mFromShortcut;

  public OnePaneLabelsController(LabelsActivityController.ControllableLabelsActivity paramControllableLabelsActivity)
  {
    super(paramControllableLabelsActivity);
  }

  public void handleLabelListResumed(LabelListFragment paramLabelListFragment)
  {
    if (paramLabelListFragment.isManageLabelMode())
    {
      this.mActionBar.setTitle(2131427408);
      this.mActionBar.setSubtitle(2131427671);
    }
    while (true)
    {
      toggleUpButton(true);
      return;
      this.mActionBar.setTitle(2131427670);
      this.mActionBar.setSubtitle(this.mAccount);
    }
  }

  public void initialize(Bundle paramBundle)
  {
    boolean bool1 = true;
    super.initialize(paramBundle);
    this.mActivity.setContentView(2130968661);
    if (this.mDefaultLabel != null);
    for (boolean bool2 = bool1; ; bool2 = false)
    {
      this.mFromShortcut = bool2;
      if (paramBundle != null)
        break label62;
      if (!this.mFromShortcut)
        break;
      showLabelSettings(this.mDefaultLabel);
      return;
    }
    showManageLabelList();
    return;
    label62: this.mLabelListVisbile = paramBundle.getBoolean("label-list-visible", bool1);
    if (!this.mLabelListVisbile);
    while (true)
    {
      toggleUpButton(bool1);
      return;
      bool1 = false;
    }
  }

  protected void showLabelSettings(String paramString)
  {
    Label localLabel = LabelManager.getLabel(this.mContext, this.mAccount, paramString);
    if (localLabel == null)
    {
      this.mActivity.onBackPressed();
      return;
    }
    FragmentTransaction localFragmentTransaction = this.mActivity.getFragmentManager().beginTransaction();
    if (!this.mFromShortcut)
    {
      localFragmentTransaction.addToBackStack(null);
      localFragmentTransaction.setTransition(4097);
    }
    localFragmentTransaction.replace(2131689648, LabelSettingsFragment.newInstance(this.mAccount, paramString));
    localFragmentTransaction.commitAllowingStateLoss();
    this.mActionBar.setTitle(localLabel.getName());
    this.mActionBar.setSubtitle(2131427671);
    this.mLabelListVisbile = false;
    toggleUpButton(true);
    this.mActivity.invalidateOptionsMenu();
  }

  protected void showManageLabelList()
  {
    FragmentTransaction localFragmentTransaction = this.mActivity.getFragmentManager().beginTransaction();
    localFragmentTransaction.replace(2131689648, LabelListFragment.newInstance(this.mAccount, null, 1, 1));
    localFragmentTransaction.commitAllowingStateLoss();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.OnePaneLabelsController
 * JD-Core Version:    0.6.2
 */