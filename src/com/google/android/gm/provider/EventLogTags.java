package com.google.android.gm.provider;

import android.util.EventLog;

public class EventLogTags
{
  public static final int GMAIL_PERF_END = 206002;

  public static void writeGmailPerfEnd(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = paramString;
    arrayOfObject[1] = Integer.valueOf(paramInt1);
    arrayOfObject[2] = Integer.valueOf(paramInt2);
    arrayOfObject[3] = Integer.valueOf(paramInt3);
    EventLog.writeEvent(206002, arrayOfObject);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.EventLogTags
 * JD-Core Version:    0.6.2
 */