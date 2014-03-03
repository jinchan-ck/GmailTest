package com.android.mail.compose;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;

public class EmptyService extends Service
{
  private static final String TAG = LogTag.getLogTag();

  public IBinder onBind(Intent paramIntent)
  {
    LogUtils.i(TAG, "onBind()", new Object[0]);
    return null;
  }

  public void onCreate()
  {
    LogUtils.v(TAG, "onCreate()", new Object[0]);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.EmptyService
 * JD-Core Version:    0.6.2
 */