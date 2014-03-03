package com.google.android.gm.preference;

import android.content.Context;
import android.content.Intent;

public final class PreferenceUtils
{
  public static void validateNotificationsForAccount(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent("com.google.android.gm.intent.VALIDATE_ACCOUNT_NOTIFICATIONS");
    localIntent.putExtra("account", paramString);
    paramContext.sendBroadcast(localIntent);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.preference.PreferenceUtils
 * JD-Core Version:    0.6.2
 */