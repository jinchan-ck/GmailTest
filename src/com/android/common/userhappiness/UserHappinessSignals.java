package com.android.common.userhappiness;

import android.content.Context;
import android.content.Intent;

public class UserHappinessSignals
{
  public static void userAcceptedImeText(Context paramContext)
  {
    Intent localIntent = new Intent("com.android.common.speech.LOG_EVENT");
    localIntent.putExtra("app_name", "voiceime");
    localIntent.putExtra("extra_event", 21);
    localIntent.putExtra("", paramContext.getPackageName());
    localIntent.putExtra("timestamp", System.currentTimeMillis());
    paramContext.sendBroadcast(localIntent);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.android.common.userhappiness.UserHappinessSignals
 * JD-Core Version:    0.6.2
 */