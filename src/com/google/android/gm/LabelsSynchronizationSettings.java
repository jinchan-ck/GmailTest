package com.google.android.gm;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.view.View;
import android.view.Window;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.LabelMap;
import com.google.android.gm.provider.Gmail.Settings;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class LabelsSynchronizationSettings extends GmailSettingsBaseActivity
{
  private String mAccount;
  private Gmail mGmail;
  private Set<String> mIncludedLabels;
  private Gmail.LabelMap mLabelMap;
  private Set<String> mPartialLabels;
  private Gmail.Settings mSettings;
  private String mSyncAllString;

  private Preference buildLabelPreference(String paramString)
  {
    CharSequence localCharSequence = LongShadowUtils.getHumanLabelName(this, this.mLabelMap, paramString);
    String str1;
    int i;
    if ("^^out".equals(paramString))
    {
      str1 = "";
      i = 2131361958;
    }
    while (true)
    {
      return new LabelPreference(this, (int)this.mSettings.conversationAgeDays, localCharSequence, paramString, str1, i);
      if (this.mIncludedLabels.contains(paramString))
      {
        str1 = this.mSyncAllString;
        i = 2131361958;
      }
      else if (this.mPartialLabels.contains(paramString))
      {
        String str2 = getString(2131296438);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Long.valueOf(this.mSettings.conversationAgeDays);
        str1 = String.format(str2, arrayOfObject);
        i = 2131361959;
      }
      else
      {
        str1 = "";
        i = 2131361960;
      }
    }
  }

  private void rebuildList()
  {
    PreferenceGroup localPreferenceGroup1 = (PreferenceGroup)findPreference("number-picker");
    localPreferenceGroup1.removeAll();
    localPreferenceGroup1.addPreference(new IntegerPickerPreference(this, null, (int)this.mSettings.conversationAgeDays));
    PreferenceGroup localPreferenceGroup2 = (PreferenceGroup)findPreference("label-list");
    localPreferenceGroup2.removeAll();
    this.mSyncAllString = getString(2131296437);
    this.mIncludedLabels = Sets.newHashSet(this.mSettings.labelsIncluded);
    this.mPartialLabels = Sets.newHashSet(this.mSettings.labelsPartial);
    Iterator localIterator1 = Gmail.LabelMap.getSortedUserMeaningfulSystemLabels().iterator();
    while (localIterator1.hasNext())
    {
      String str2 = (String)localIterator1.next();
      if (shouldShowLabel(str2))
        localPreferenceGroup2.addPreference(buildLabelPreference(str2));
    }
    Iterator localIterator2 = this.mLabelMap.getSortedUserLabels().iterator();
    while (localIterator2.hasNext())
    {
      String str1 = (String)localIterator2.next();
      if (shouldShowLabel(str1))
        localPreferenceGroup2.addPreference(buildLabelPreference(str1));
    }
  }

  private boolean shouldShowLabel(String paramString)
  {
    if ((!Gmail.LabelMap.getForcedIncludedLabels().contains(paramString)) && (!Gmail.LabelMap.getForcedUnsyncedLabels().contains(paramString)));
    for (boolean bool = true; ; bool = false)
    {
      if ((bool) && (Gmail.LabelMap.getPriorityInboxRelatedUserLabels().contains(paramString)))
        bool = Utils.getPriorityInboxServerEnabled(this.mAccount);
      return bool;
    }
  }

  public void onConversationAgeDaysChanged(int paramInt)
  {
    this.mSettings.conversationAgeDays = paramInt;
    rebuildList();
  }

  public void onCreate(Bundle paramBundle)
  {
    requestWindowFeature(7);
    super.onCreate(paramBundle);
    getWindow().setFeatureInt(7, 2130903054);
    findViewById(2131361845).setVisibility(0);
    Utils.setTitleWithAccount(this, getString(2131296413), Persistence.getInstance(this).getActiveAccount(this));
    this.mAccount = WaitActivity.waitIfNeededAndGetAccount(this);
    if (this.mAccount == null)
      return;
    this.mGmail = LongShadowUtils.getContentProviderMailAccess(getContentResolver());
    this.mSettings = this.mGmail.getSettings(this.mAccount);
    this.mLabelMap = LongShadowUtils.getLabelMap(getContentResolver(), this.mAccount);
    addPreferencesFromResource(2131034112);
    rebuildList();
  }

  public void onPause()
  {
    super.onPause();
    this.mGmail.setSettings(this.mAccount, this.mSettings);
  }

  void onSyncSettingChanged(CharSequence paramCharSequence, int paramInt)
  {
    HashSet localHashSet1 = Sets.newHashSet();
    HashSet localHashSet2 = Sets.newHashSet();
    for (String str2 : this.mSettings.labelsIncluded)
      if ((!str2.equals(paramCharSequence)) || ((str2.equals(paramCharSequence)) && (paramInt == 2131296437)))
        localHashSet2.add(str2);
    for (String str1 : this.mSettings.labelsPartial)
      if ((!str1.equals(paramCharSequence)) || ((str1.equals(paramCharSequence)) && (paramInt == 2131296438)))
        localHashSet1.add(str1);
    if (paramInt == 2131296438)
      localHashSet1.add(paramCharSequence);
    while (true)
    {
      this.mSettings.labelsIncluded = ((String[])localHashSet2.toArray(new String[localHashSet2.size()]));
      this.mSettings.labelsPartial = ((String[])localHashSet1.toArray(new String[localHashSet1.size()]));
      rebuildList();
      return;
      if (paramInt == 2131296437)
        localHashSet2.add(paramCharSequence);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelsSynchronizationSettings
 * JD-Core Version:    0.6.2
 */