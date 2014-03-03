package com.google.android.gm;

import android.app.Service;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.IBinder;
import com.google.android.gm.provider.LabelList;
import com.google.android.gm.provider.LabelManager;

public class ShortcutEnablerService extends Service
{
  private String mAccount;
  private DataSetObserver mDataSetObserver = null;
  private LabelList mLabelList;

  private void enableShortcutAndFinish()
  {
    new AsyncTask()
    {
      protected Void doInBackground(Void[] paramAnonymousArrayOfVoid)
      {
        Utils.enableLabelShortcutActivity(ShortcutEnablerService.this);
        return null;
      }

      protected void onPostExecute(Void paramAnonymousVoid)
      {
        ShortcutEnablerService.this.stopService();
      }
    }
    .execute(new Void[0]);
  }

  private void stopService()
  {
    if ((this.mLabelList != null) && (this.mDataSetObserver != null))
    {
      this.mLabelList.unregisterDataSetObserver(this.mDataSetObserver);
      this.mDataSetObserver = null;
      this.mLabelList.unregisterForLabelChanges();
    }
    stopSelf();
  }

  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    super.onStartCommand(paramIntent, paramInt1, paramInt2);
    getPackageManager();
    this.mAccount = paramIntent.getStringExtra("account-name");
    if (this.mAccount == null)
      return 2;
    if (Utils.shortcutActivityEnabled(this))
    {
      stopSelf();
      return 2;
    }
    new AsyncTask()
    {
      protected LabelList doInBackground(Void[] paramAnonymousArrayOfVoid)
      {
        return LabelManager.getLabelList(ShortcutEnablerService.this, ShortcutEnablerService.this.mAccount, null, false);
      }

      protected void onPostExecute(LabelList paramAnonymousLabelList)
      {
        ShortcutEnablerService.access$102(ShortcutEnablerService.this, paramAnonymousLabelList);
        ShortcutEnablerService.this.mLabelList.registerForLabelChanges();
        ShortcutEnablerService.access$202(ShortcutEnablerService.this, new DataSetObserver()
        {
          public void onChanged()
          {
            if (Utils.shortcutActivityEnabled(ShortcutEnablerService.this))
              ShortcutEnablerService.this.stopService();
            while (ShortcutEnablerService.this.mLabelList.get("^i") == null)
              return;
            ShortcutEnablerService.this.enableShortcutAndFinish();
          }
        });
        ShortcutEnablerService.this.mLabelList.registerDataSetObserver(ShortcutEnablerService.this.mDataSetObserver);
        if (ShortcutEnablerService.this.mLabelList.get("^i") != null)
          ShortcutEnablerService.this.enableShortcutAndFinish();
      }
    }
    .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
    return 2;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ShortcutEnablerService
 * JD-Core Version:    0.6.2
 */