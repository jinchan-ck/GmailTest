package com.google.android.gm.preference;

import android.app.Activity;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;

public class GmailPreferenceFragment extends PreferenceFragment
  implements Preference.OnPreferenceChangeListener
{
  protected String getRingtoneSummary(Uri paramUri, Activity paramActivity)
  {
    Ringtone localRingtone = RingtoneManager.getRingtone(getActivity(), paramUri);
    if (localRingtone != null)
      return localRingtone.getTitle(paramActivity);
    return paramActivity.getResources().getString(2131427742);
  }

  protected void initializeCheckBox(String paramString, boolean paramBoolean)
  {
    CheckBoxPreference localCheckBoxPreference = (CheckBoxPreference)findPreference(paramString);
    if (localCheckBoxPreference != null)
      localCheckBoxPreference.setChecked(paramBoolean);
  }

  protected void initializeCheckBoxAndSummary(String paramString, boolean paramBoolean, CharSequence paramCharSequence)
  {
    CheckBoxPreference localCheckBoxPreference = (CheckBoxPreference)getPreferenceScreen().findPreference(paramString);
    localCheckBoxPreference.setChecked(paramBoolean);
    localCheckBoxPreference.setSummary(paramCharSequence);
  }

  protected void initializeEditText(String paramString1, String paramString2)
  {
    EditTextPreference localEditTextPreference = (EditTextPreference)findPreference(paramString1);
    if (localEditTextPreference != null)
      localEditTextPreference.setText(paramString2);
  }

  protected void initializePreferenceScreen(String paramString1, String paramString2)
  {
    PreferenceScreen localPreferenceScreen = (PreferenceScreen)findPreference(paramString1);
    if (localPreferenceScreen != null)
      localPreferenceScreen.setTitle(paramString2);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setHasOptionsMenu(true);
  }

  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    paramMenu.clear();
    paramMenuInflater.inflate(2131820555, paramMenu);
  }

  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    return false;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.preference.GmailPreferenceFragment
 * JD-Core Version:    0.6.2
 */