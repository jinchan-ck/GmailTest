package com.google.android.gm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class EmptyService extends Service
{
  private static final String TAG = "EmptyService";

  public IBinder onBind(Intent paramIntent)
  {
    Log.i("EmptyService", "onBind()");
    return null;
  }

  public void onCreate()
  {
    Log.v("EmptyService", "onCreate()");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.EmptyService
 * JD-Core Version:    0.6.2
 */