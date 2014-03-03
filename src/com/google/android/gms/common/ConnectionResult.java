package com.google.android.gms.common;

import android.app.PendingIntent;

public final class ConnectionResult
{
  public static final ConnectionResult RESULT_SUCCESS = new ConnectionResult(0, null);
  private final PendingIntent mPendingIntent;
  private final int mStatusCode;

  public ConnectionResult(int paramInt, PendingIntent paramPendingIntent)
  {
    this.mStatusCode = paramInt;
    this.mPendingIntent = paramPendingIntent;
  }

  public int getErrorCode()
  {
    return this.mStatusCode;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gms.common.ConnectionResult
 * JD-Core Version:    0.6.2
 */