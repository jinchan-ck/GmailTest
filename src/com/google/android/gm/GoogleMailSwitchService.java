package com.google.android.gm;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

public class GoogleMailSwitchService extends IntentService
{
  private static final ComponentName GMAIL_WIDGET_PROVIDER_COMPONENT_NAME = new ComponentName("com.google.android.gm", "com.google.android.gm.widget.GmailWidgetProvider");
  private static final ComponentName GOOGLE_MAIL_WIDGET_PROVIDER_COMPONENT_NAME = new ComponentName("com.google.android.gm", "com.google.android.gm.widget.GoogleMailWidgetProvider");

  public GoogleMailSwitchService()
  {
    super("GoogleMailSwitchService");
  }

  private void validateGmailWidgets()
  {
    validateWidgetProvider();
    Utils.enableLabelShortcutActivity(this);
  }

  private void validateWidgetProvider()
  {
    PackageManager localPackageManager = getPackageManager();
    boolean bool = Utils.haveGoogleMailActivitiesBeenEnabled(this);
    ComponentName localComponentName1;
    if (bool)
    {
      localComponentName1 = GOOGLE_MAIL_WIDGET_PROVIDER_COMPONENT_NAME;
      if (!bool)
        break label50;
    }
    label50: for (ComponentName localComponentName2 = GMAIL_WIDGET_PROVIDER_COMPONENT_NAME; ; localComponentName2 = GOOGLE_MAIL_WIDGET_PROVIDER_COMPONENT_NAME)
    {
      localPackageManager.setComponentEnabledSetting(localComponentName2, 2, 1);
      localPackageManager.setComponentEnabledSetting(localComponentName1, 1, 1);
      return;
      localComponentName1 = GMAIL_WIDGET_PROVIDER_COMPONENT_NAME;
      break;
    }
  }

  protected void onHandleIntent(Intent paramIntent)
  {
    String str = paramIntent.getAction();
    if ("android.intent.action.BOOT_COMPLETED".equals(str))
      validateGmailWidgets();
    while (!"android.intent.action.MY_PACKAGE_REPLACED".equals(str))
      return;
    GoogleMailDeviceStartupReceiver.enableReceiver(this);
    validateGmailWidgets();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.GoogleMailSwitchService
 * JD-Core Version:    0.6.2
 */