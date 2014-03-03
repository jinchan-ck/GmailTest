package com.google.android.gm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class GoogleMailSwitchReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    boolean bool = paramIntent.getBooleanExtra("useGoogleMail", true);
    PackageManager localPackageManager = paramContext.getPackageManager();
    ComponentName localComponentName1 = new ComponentName("com.google.android.gm", "com.google.android.gm.ConversationListActivityGmail");
    ComponentName localComponentName2 = new ComponentName("com.google.android.gm", "com.google.android.gm.ConversationListActivityGoogleMail");
    ComponentName localComponentName3 = new ComponentName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
    ComponentName localComponentName4 = new ComponentName("com.google.android.gm", "com.google.android.gm.ComposeActivityGoogleMail");
    if (bool)
    {
      localPackageManager.setComponentEnabledSetting(localComponentName1, 2, 1);
      localPackageManager.setComponentEnabledSetting(localComponentName2, 1, 0);
      localPackageManager.setComponentEnabledSetting(localComponentName3, 2, 1);
      localPackageManager.setComponentEnabledSetting(localComponentName4, 1, 0);
      return;
    }
    localPackageManager.setComponentEnabledSetting(localComponentName1, 1, 1);
    localPackageManager.setComponentEnabledSetting(localComponentName2, 2, 0);
    localPackageManager.setComponentEnabledSetting(localComponentName3, 1, 1);
    localPackageManager.setComponentEnabledSetting(localComponentName4, 2, 0);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.GoogleMailSwitchReceiver
 * JD-Core Version:    0.6.2
 */