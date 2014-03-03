package com.google.android.gm.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import com.google.android.gm.persistence.Persistence;

public class ExperimentalPrefsFragment extends PreferenceFragment
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getPreferenceManager().setSharedPreferencesName(Persistence.getInstance().getSharedPreferencesName());
    addPreferencesFromResource(2131099649);
    setHasOptionsMenu(true);
  }

  public void onPrepareOptionsMenu(Menu paramMenu)
  {
    paramMenu.clear();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.preference.ExperimentalPrefsFragment
 * JD-Core Version:    0.6.2
 */