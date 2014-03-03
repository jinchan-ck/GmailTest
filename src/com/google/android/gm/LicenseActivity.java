package com.google.android.gm;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.LogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class LicenseActivity extends Activity
{
  private WebView mWebView;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968651);
    ActionBar localActionBar = getActionBar();
    if (localActionBar != null)
    {
      localActionBar.setDisplayHomeAsUpEnabled(true);
      if (Gmail.isRunningICSOrLater())
        localActionBar.setHomeButtonEnabled(true);
    }
    this.mWebView = ((WebView)findViewById(2131689476));
    try
    {
      InputStream localInputStream = getResources().openRawResource(2131165184);
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
      int i = localInputStream.available();
      char[] arrayOfChar = new char[i];
      int j = 0;
      int k = 0;
      do
      {
        j += k;
        k = localBufferedReader.read(arrayOfChar, j, i - j);
      }
      while ((k > -1) && (j + k < i));
      localBufferedReader.close();
      String str = new String(arrayOfChar);
      this.mWebView.loadData(str, "text/html", null);
      return;
    }
    catch (IOException localIOException)
    {
      LogUtils.e("Gmail", localIOException, "Error reading licence file", new Object[0]);
      finish();
    }
  }

  protected void onDestroy()
  {
    this.mWebView.destroy();
    super.onDestroy();
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() == 16908332)
    {
      finish();
      return true;
    }
    return false;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LicenseActivity
 * JD-Core Version:    0.6.2
 */