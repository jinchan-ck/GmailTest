package com.google.android.gm;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.preference.PreferenceUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.Settings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseLabelsController
  implements LabelsActivityController
{
  protected String mAccount;
  protected final ActionBar mActionBar;
  protected final LabelsActivityController.ControllableLabelsActivity mActivity;
  protected final Context mContext;
  protected String mDefaultLabel;
  Gmail mGmail;
  private boolean mHomeIsBack = false;
  private List<String> mIncludedLabels;
  protected boolean mLabelListVisbile = true;
  private int mNumOfSyncDays;
  private final List<LabelSettingsObserver> mObservers;
  private List<String> mPartialLabels;
  Gmail.Settings mSettings;

  public BaseLabelsController(LabelsActivityController.ControllableLabelsActivity paramControllableLabelsActivity)
  {
    this.mActivity = paramControllableLabelsActivity;
    this.mObservers = Lists.newArrayList();
    this.mContext = this.mActivity.getContext();
    this.mActionBar = this.mActivity.getActionBar();
  }

  public ArrayList<String> getIncludedLabels()
  {
    return Lists.newArrayList(this.mIncludedLabels);
  }

  public int getNumberOfSyncDays()
  {
    return this.mNumOfSyncDays;
  }

  public ArrayList<String> getPartialLabels()
  {
    return Lists.newArrayList(this.mPartialLabels);
  }

  public boolean handleBackPressed()
  {
    this.mActionBar.setSubtitle(this.mAccount);
    this.mLabelListVisbile = true;
    toggleUpButton(false);
    this.mActivity.invalidateOptionsMenu();
    return false;
  }

  public boolean handleCreateOptionsMenu(Menu paramMenu)
  {
    this.mActivity.getMenuInflater().inflate(2131820551, paramMenu);
    return true;
  }

  public void handleLabelSelected(String paramString)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("label", paramString);
    this.mActivity.setResult(-1, localIntent);
    showLabelSettings(paramString);
  }

  public boolean handleOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
    case 16908332:
    }
    do
      return ApplicationMenuHandler.handleApplicationMenu(paramMenuItem, this.mContext, (ApplicationMenuHandler.HelpCallback)this.mActivity);
    while (!this.mHomeIsBack);
    this.mActivity.onBackPressed();
    return true;
  }

  public void handlePause()
  {
    new AsyncTask()
    {
      protected Void doInBackground(Void[] paramAnonymousArrayOfVoid)
      {
        BaseLabelsController.this.mSettings.setLabelsIncluded(BaseLabelsController.this.mIncludedLabels);
        BaseLabelsController.this.mSettings.setLabelsPartial(BaseLabelsController.this.mPartialLabels);
        BaseLabelsController.this.mGmail.setSettings(BaseLabelsController.this.mAccount, BaseLabelsController.this.mSettings);
        PreferenceUtils.validateNotificationsForAccount(BaseLabelsController.this.mContext, BaseLabelsController.this.mAccount);
        return null;
      }
    }
    .execute(new Void[0]);
  }

  public boolean handlePrepareOptionsMenu(Menu paramMenu)
  {
    return true;
  }

  public void handleSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putBoolean("label-list-visible", this.mLabelListVisbile);
  }

  public void initialize(Bundle paramBundle)
  {
    Intent localIntent = this.mActivity.getIntent();
    this.mAccount = localIntent.getStringExtra("account_key");
    this.mDefaultLabel = localIntent.getStringExtra("label");
    this.mAccount = new AccountHelper(this.mActivity).validateAccountName(this.mAccount);
    if (this.mAccount == null)
    {
      this.mActivity.finish();
      return;
    }
    this.mGmail = LongShadowUtils.getContentProviderMailAccess(this.mActivity.getContentResolver());
    this.mSettings = this.mGmail.getSettings(this.mContext, this.mAccount);
    this.mIncludedLabels = this.mSettings.getLabelsIncluded();
    this.mPartialLabels = this.mSettings.getLabelsPartial();
    this.mNumOfSyncDays = ((int)this.mSettings.getConversationAgeDays());
    this.mActionBar.setSubtitle(this.mAccount);
    toggleUpButton(false);
  }

  public void notifyChanged()
  {
    Iterator localIterator = Lists.newArrayList(this.mObservers).iterator();
    while (localIterator.hasNext())
      ((LabelSettingsObserver)localIterator.next()).onChanged();
  }

  public void registerObserver(LabelSettingsObserver paramLabelSettingsObserver)
  {
    this.mObservers.add(paramLabelSettingsObserver);
  }

  public void setIncludedLabels(ArrayList<String> paramArrayList)
  {
    this.mIncludedLabels = paramArrayList;
  }

  public void setPartialLabels(ArrayList<String> paramArrayList)
  {
    this.mPartialLabels = paramArrayList;
  }

  protected abstract void showLabelSettings(String paramString);

  protected void toggleUpButton(boolean paramBoolean)
  {
    this.mHomeIsBack = paramBoolean;
    this.mActivity.getActionBar().setDisplayHomeAsUpEnabled(paramBoolean);
    if (Gmail.isRunningICSOrLater())
      this.mActivity.getActionBar().setHomeButtonEnabled(paramBoolean);
  }

  public void unregisterObserver(LabelSettingsObserver paramLabelSettingsObserver)
  {
    this.mObservers.remove(paramLabelSettingsObserver);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.BaseLabelsController
 * JD-Core Version:    0.6.2
 */