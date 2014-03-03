package com.android.mail.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;

public final class MailPrefs
{
  private static MailPrefs sInstance;
  private final SharedPreferences mPrefs;

  private MailPrefs(Context paramContext)
  {
    this.mPrefs = paramContext.getSharedPreferences("UnifiedEmail", 0);
  }

  private static String createWidgetPreferenceValue(Account paramAccount, Folder paramFolder)
  {
    return paramAccount.uri.toString() + " " + paramFolder.uri.toString();
  }

  public static MailPrefs get(Context paramContext)
  {
    if (sInstance == null)
      sInstance = new MailPrefs(paramContext);
    return sInstance;
  }

  public void clearWidgets(int[] paramArrayOfInt)
  {
    SharedPreferences.Editor localEditor = this.mPrefs.edit();
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++)
    {
      int k = paramArrayOfInt[j];
      localEditor.remove("widget-account-" + k);
    }
    localEditor.apply();
  }

  public void configureWidget(int paramInt, Account paramAccount, Folder paramFolder)
  {
    this.mPrefs.edit().putString("widget-account-" + paramInt, createWidgetPreferenceValue(paramAccount, paramFolder)).apply();
  }

  public boolean getShouldShowWhatsNew(Context paramContext)
  {
    int i = this.mPrefs.getInt("whats-new-last-shown-version", 0);
    int j = paramContext.getResources().getInteger(2131296309);
    boolean bool = false;
    if (j > i)
      bool = true;
    return bool;
  }

  public String getWidgetConfiguration(int paramInt)
  {
    return this.mPrefs.getString("widget-account-" + paramInt, null);
  }

  public boolean isWidgetConfigured(int paramInt)
  {
    return this.mPrefs.contains("widget-account-" + paramInt);
  }

  public void setHasShownWhatsNew(int paramInt)
  {
    this.mPrefs.edit().putInt("whats-new-last-shown-version", paramInt).apply();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.preferences.MailPrefs
 * JD-Core Version:    0.6.2
 */