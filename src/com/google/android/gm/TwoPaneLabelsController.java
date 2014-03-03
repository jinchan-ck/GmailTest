package com.google.android.gm;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.google.android.gm.persistence.Persistence;

public class TwoPaneLabelsController extends BaseLabelsController
{
  public TwoPaneLabelsController(LabelsActivityController.ControllableLabelsActivity paramControllableLabelsActivity)
  {
    super(paramControllableLabelsActivity);
  }

  public void handleLabelListResumed(LabelListFragment paramLabelListFragment)
  {
    toggleUpButton(true);
  }

  public void initialize(Bundle paramBundle)
  {
    super.initialize(paramBundle);
    this.mActivity.setContentView(2130968679);
    if (paramBundle == null)
    {
      if (this.mDefaultLabel == null)
        this.mDefaultLabel = Persistence.getAccountInbox(this.mContext, this.mAccount);
      showManageLabelList();
      showLabelSettings(this.mDefaultLabel);
    }
  }

  protected void showLabelSettings(String paramString)
  {
    FragmentTransaction localFragmentTransaction = this.mActivity.getFragmentManager().beginTransaction();
    localFragmentTransaction.setTransition(4099);
    localFragmentTransaction.replace(2131689713, LabelSettingsFragment.newInstance(this.mAccount, paramString));
    localFragmentTransaction.commitAllowingStateLoss();
  }

  protected void showManageLabelList()
  {
    FragmentTransaction localFragmentTransaction = this.mActivity.getFragmentManager().beginTransaction();
    localFragmentTransaction.replace(2131689658, LabelListFragment.newInstance(this.mAccount, this.mDefaultLabel, 1));
    localFragmentTransaction.commitAllowingStateLoss();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.TwoPaneLabelsController
 * JD-Core Version:    0.6.2
 */