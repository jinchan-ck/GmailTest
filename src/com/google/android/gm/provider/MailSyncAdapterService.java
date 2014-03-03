package com.google.android.gm.provider;

import android.accounts.Account;
import android.app.Service;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.SQLException;
import android.os.Bundle;
import android.os.IBinder;
import android.util.EventLog;
import com.google.android.common.LoggingThreadedSyncAdapter;

public class MailSyncAdapterService extends Service
{
  private static SyncAdapterImpl sSyncAdapter;
  private static final Object sSyncAdapterLock = new Object();

  public IBinder onBind(Intent paramIntent)
  {
    synchronized (sSyncAdapterLock)
    {
      IBinder localIBinder = sSyncAdapter.getSyncAdapterBinder();
      return localIBinder;
    }
  }

  public void onCreate()
  {
    super.onCreate();
    synchronized (sSyncAdapterLock)
    {
      if (sSyncAdapter == null)
        sSyncAdapter = new SyncAdapterImpl(this);
      return;
    }
  }

  private static class SyncAdapterImpl extends LoggingThreadedSyncAdapter
  {
    private volatile MailEngine mMailEngine;

    public SyncAdapterImpl(Context paramContext)
    {
      super(false, true);
    }

    protected void onLogSyncDetails(long paramLong1, long paramLong2, SyncResult paramSyncResult)
    {
      if (this.mMailEngine != null)
      {
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = "Gmail";
        arrayOfObject[1] = Long.valueOf(paramLong1);
        arrayOfObject[2] = Long.valueOf(paramLong2);
        arrayOfObject[3] = this.mMailEngine.getMailSync().getStats(paramSyncResult);
        EventLog.writeEvent(203001, arrayOfObject);
      }
    }

    public void onPerformLoggedSync(Account paramAccount, Bundle paramBundle, String paramString, ContentProviderClient paramContentProviderClient, SyncResult paramSyncResult)
    {
      this.mMailEngine = MailEngine.getOrMakeMailEngineSync(MailProvider.getMailProvider().getContext(), paramAccount.name);
      try
      {
        this.mMailEngine.performBackgroundSync(paramSyncResult, paramBundle);
        return;
      }
      catch (SQLException localSQLException)
      {
        LogUtils.e("Gmail", localSQLException, "Mail sync failed", new Object[0]);
        paramSyncResult.databaseError = true;
      }
    }

    public void onSyncCanceled(Thread paramThread)
    {
      if (this.mMailEngine != null)
        this.mMailEngine.onSyncCanceled();
      super.onSyncCanceled(paramThread);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.MailSyncAdapterService
 * JD-Core Version:    0.6.2
 */