package com.google.android.gm;

import android.app.Application;
import android.preference.PreferenceManager;
import com.google.android.gm.persistence.Persistence;

public class GmailApplication extends Application
{
  public void onCreate()
  {
    PreferenceManager.setDefaultValues(this, Persistence.getInstance().getSharedPreferencesName(), 0, 2131099649, false);
    PreferenceManager.setDefaultValues(this, Persistence.getInstance().getSharedPreferencesName(), 0, 2131099651, false);
    PreferenceManager.setDefaultValues(this, Persistence.getInstance().getSharedPreferencesName(), 0, 2131099648, false);
  }

  public void onTerminate()
  {
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.GmailApplication
 * JD-Core Version:    0.6.2
 */