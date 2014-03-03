package com.google.android.common;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.content.SyncStats;
import android.database.SQLException;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Process;
import android.util.EventLog;

public abstract class LoggingThreadedSyncAdapter extends AbstractThreadedSyncAdapter
{
  public LoggingThreadedSyncAdapter(Context paramContext, boolean paramBoolean)
  {
    super(paramContext, paramBoolean);
  }

  public LoggingThreadedSyncAdapter(Context paramContext, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramContext, paramBoolean1, paramBoolean2);
  }

  protected void onLogSyncDetails(long paramLong1, long paramLong2, SyncResult paramSyncResult)
  {
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = "Sync";
    arrayOfObject[1] = Long.valueOf(paramLong1);
    arrayOfObject[2] = Long.valueOf(paramLong2);
    arrayOfObject[3] = "";
    EventLog.writeEvent(203001, arrayOfObject);
  }

  public abstract void onPerformLoggedSync(Account paramAccount, Bundle paramBundle, String paramString, ContentProviderClient paramContentProviderClient, SyncResult paramSyncResult);

  public void onPerformSync(Account paramAccount, Bundle paramBundle, String paramString, ContentProviderClient paramContentProviderClient, SyncResult paramSyncResult)
  {
    int i = Process.myUid();
    long l1 = TrafficStats.getUidTxBytes(i);
    long l2 = TrafficStats.getUidRxBytes(i);
    try
    {
      onPerformLoggedSync(paramAccount, paramBundle, paramString, paramContentProviderClient, paramSyncResult);
      l3 = TrafficStats.getUidTxBytes(i) - l1;
      l4 = TrafficStats.getUidRxBytes(i);
      onLogSyncDetails(l3, l4 - l2, paramSyncResult);
      return;
    }
    catch (SQLException localSQLException)
    {
      while (true)
      {
        SyncStats localSyncStats = paramSyncResult.stats;
        localSyncStats.numParseExceptions = (1L + localSyncStats.numParseExceptions);
        long l3 = TrafficStats.getUidTxBytes(i) - l1;
        long l4 = TrafficStats.getUidRxBytes(i);
      }
    }
    finally
    {
      onLogSyncDetails(TrafficStats.getUidTxBytes(i) - l1, TrafficStats.getUidRxBytes(i) - l2, paramSyncResult);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.LoggingThreadedSyncAdapter
 * JD-Core Version:    0.6.2
 */