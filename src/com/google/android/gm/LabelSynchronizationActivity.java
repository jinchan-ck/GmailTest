package com.google.android.gm;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;
import com.android.mail.widget.BaseGmailWidgetProvider;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.Settings;
import com.google.android.gm.provider.Label;
import com.google.android.gm.provider.LabelManager;
import com.google.android.gm.provider.LogUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;

public class LabelSynchronizationActivity extends GmailBaseListActivity
  implements View.OnClickListener, AdapterView.OnItemClickListener
{
  private Account mAccount;
  private String mAccountName;
  private String mCurrentOption;
  private Folder mFolder;
  private Gmail mGmail = null;
  private ArrayList<String> mIncludedLabels;
  private String mLabelName;
  private ArrayList<String> mPartialLabels;
  private boolean mPerformActionsInternally;
  private Gmail.Settings mSettings = null;
  private String mSyncAll;
  private String mSyncNone;
  private String mSyncPartial;
  private int mWidgetIdToUpdate = 1;

  public void onClick(View paramView)
  {
    setResult(0);
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968647);
    Intent localIntent = getIntent();
    this.mPerformActionsInternally = localIntent.getBooleanExtra("perform-actions-internally", false);
    if (localIntent.getExtras().containsKey("update-widgetid-on-sync-change"))
    {
      this.mWidgetIdToUpdate = localIntent.getIntExtra("update-widgetid-on-sync-change", -1);
      this.mFolder = ((Folder)localIntent.getParcelableExtra("folder"));
      this.mLabelName = this.mFolder.uri.getLastPathSegment();
      this.mAccount = ((Account)localIntent.getParcelableExtra("account"));
      this.mAccountName = this.mAccount.name;
      if (this.mPerformActionsInternally)
        break label216;
      this.mIncludedLabels = localIntent.getStringArrayListExtra("included-labels");
      this.mPartialLabels = localIntent.getStringArrayListExtra("partial-labels");
    }
    Label localLabel;
    for (int i = localIntent.getIntExtra("num-of-sync-days", 0); ; i = (int)this.mSettings.getConversationAgeDays())
    {
      localLabel = LabelManager.getLabel(this, this.mAccountName, this.mLabelName);
      if (localLabel != null)
        break label300;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = this.mLabelName;
      arrayOfObject[1] = this.mAccountName;
      LogUtils.e("Gmail", "Unable to get label: %s for account: %s", arrayOfObject);
      finish();
      return;
      this.mLabelName = localIntent.getStringExtra("folder");
      this.mAccountName = localIntent.getStringExtra("account");
      break;
      label216: this.mGmail = LongShadowUtils.getContentProviderMailAccess(getContentResolver());
      this.mSettings = this.mGmail.getSettings(this, this.mAccountName);
      this.mIncludedLabels = Lists.newArrayList();
      this.mIncludedLabels.addAll(this.mSettings.getLabelsIncluded());
      this.mPartialLabels = Lists.newArrayList();
      this.mPartialLabels.addAll(this.mSettings.getLabelsPartial());
    }
    label300: setTitle(localLabel.getName());
    Resources localResources = getResources();
    this.mSyncNone = localResources.getString(2131427554);
    this.mSyncPartial = Utils.formatPlural(this, 2131755032, i);
    this.mSyncAll = localResources.getString(2131427553);
    String[] arrayOfString;
    if (localLabel.getForceSyncAllOrPartial())
    {
      arrayOfString = new String[2];
      arrayOfString[0] = this.mSyncPartial;
      arrayOfString[1] = this.mSyncAll;
      if (!this.mIncludedLabels.contains(this.mLabelName))
        break label528;
      this.mCurrentOption = this.mSyncAll;
    }
    label401: for (int j = 0; ; j++)
    {
      int k = arrayOfString.length;
      int m = 0;
      if (j < k)
      {
        if (arrayOfString[j].equals(this.mCurrentOption))
          m = j;
      }
      else
      {
        setListAdapter(new ArrayAdapter(this, 2130968648, arrayOfString));
        getListView().setChoiceMode(1);
        getListView().setItemChecked(m, true);
        getListView().setOnItemClickListener(this);
        ((Button)findViewById(2131689649)).setOnClickListener(this);
        return;
        arrayOfString = new String[3];
        arrayOfString[0] = this.mSyncNone;
        arrayOfString[1] = this.mSyncPartial;
        arrayOfString[2] = this.mSyncAll;
        break;
        if (this.mPartialLabels.contains(this.mLabelName))
        {
          this.mCurrentOption = this.mSyncPartial;
          break label401;
        }
        this.mCurrentOption = this.mSyncNone;
        break label401;
      }
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    String str = (String)getListAdapter().getItem(paramInt);
    if (str.equals(this.mCurrentOption))
    {
      finish();
      return;
    }
    this.mIncludedLabels.remove(this.mLabelName);
    this.mPartialLabels.remove(this.mLabelName);
    if (str.equals(this.mSyncAll))
    {
      this.mIncludedLabels.add(this.mLabelName);
      if (this.mPerformActionsInternally)
        break label183;
      Intent localIntent = new Intent();
      localIntent.putExtra("included-labels", this.mIncludedLabels);
      localIntent.putExtra("partial-labels", this.mPartialLabels);
      setResult(-1, localIntent);
    }
    while (true)
    {
      if (this.mWidgetIdToUpdate != -1)
        BaseGmailWidgetProvider.updateWidget(this, this.mWidgetIdToUpdate, this.mAccount, this.mFolder);
      finish();
      return;
      if (!str.equals(this.mSyncPartial))
        break;
      this.mPartialLabels.add(this.mLabelName);
      break;
      label183: this.mSettings.setLabelsIncluded(this.mIncludedLabels);
      this.mSettings.setLabelsPartial(this.mPartialLabels);
      this.mGmail.setSettings(this.mAccountName, this.mSettings);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelSynchronizationActivity
 * JD-Core Version:    0.6.2
 */