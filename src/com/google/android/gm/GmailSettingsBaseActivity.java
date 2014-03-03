package com.google.android.gm;

import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class GmailSettingsBaseActivity extends PreferenceActivity
{
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
      return false;
    case 2131361924:
      Utils.showHelp(this);
      return true;
    case 2131361938:
    }
    Utils.showAbout(this);
    return true;
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    MenuInflater localMenuInflater = getMenuInflater();
    paramMenu.clear();
    localMenuInflater.inflate(2131623945, paramMenu);
    return super.onPrepareOptionsMenu(paramMenu);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.GmailSettingsBaseActivity
 * JD-Core Version:    0.6.2
 */