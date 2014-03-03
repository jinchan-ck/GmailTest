package com.google.android.gm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.gm.provider.LogUtils;

public class GoogleMailPackageReplaceReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    LogUtils.v("Gmail", "Received intent %s", new Object[] { paramIntent });
    paramIntent.setClass(paramContext, GoogleMailSwitchService.class);
    paramContext.startService(paramIntent);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.GoogleMailPackageReplaceReceiver
 * JD-Core Version:    0.6.2
 */