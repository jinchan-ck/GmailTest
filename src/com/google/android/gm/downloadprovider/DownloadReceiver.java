package com.google.android.gm.downloadprovider;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import java.io.File;

public class DownloadReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (paramIntent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
    {
      if (Constants.LOGVV)
        Log.v("DownloadManager", "Receiver onBoot");
      Intent localIntent5 = new Intent(paramContext, DownloadService.class);
      paramContext.startService(localIntent5);
    }
    label563: Cursor localCursor2;
    do
    {
      do
      {
        NetworkInfo localNetworkInfo;
        do
        {
          return;
          if (!paramIntent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE"))
            break;
          if (Constants.LOGVV)
            Log.v("DownloadManager", "Receiver onConnectivity");
          localNetworkInfo = (NetworkInfo)paramIntent.getParcelableExtra("networkInfo");
        }
        while ((localNetworkInfo == null) || (!localNetworkInfo.isConnected()));
        Intent localIntent4 = new Intent(paramContext, DownloadService.class);
        paramContext.startService(localIntent4);
        return;
        if (paramIntent.getAction().equals("android.intent.action.DOWNLOAD_WAKEUP"))
        {
          if (Constants.LOGVV)
            Log.v("DownloadManager", "Receiver retry");
          Intent localIntent3 = new Intent(paramContext, DownloadService.class);
          paramContext.startService(localIntent3);
          return;
        }
        if ((paramIntent.getAction().equals("android.intent.action.DOWNLOAD_OPEN")) || (paramIntent.getAction().equals("android.intent.action.DOWNLOAD_LIST")))
        {
          Cursor localCursor1;
          String str4;
          Intent localIntent2;
          if (Constants.LOGVV)
          {
            if (paramIntent.getAction().equals("android.intent.action.DOWNLOAD_OPEN"))
              Log.v("DownloadManager", "Receiver open for " + paramIntent.getData());
          }
          else
          {
            localCursor1 = paramContext.getContentResolver().query(paramIntent.getData(), null, null, null, null);
            if (localCursor1 != null)
              if (localCursor1.moveToFirst())
              {
                int i = localCursor1.getInt(localCursor1.getColumnIndexOrThrow("status"));
                int j = localCursor1.getInt(localCursor1.getColumnIndexOrThrow("visibility"));
                if ((Downloads.Impl.isStatusCompleted(i)) && (j == 1))
                {
                  ContentValues localContentValues1 = new ContentValues();
                  localContentValues1.put("visibility", Integer.valueOf(0));
                  paramContext.getContentResolver().update(paramIntent.getData(), localContentValues1, null, null);
                }
                if (!paramIntent.getAction().equals("android.intent.action.DOWNLOAD_OPEN"))
                  break label563;
                int n = localCursor1.getColumnIndexOrThrow("_data");
                int i1 = localCursor1.getColumnIndexOrThrow("mimetype");
                String str3 = localCursor1.getString(n);
                str4 = localCursor1.getString(i1);
                Uri localUri = Uri.parse(str3);
                if (localUri.getScheme() == null)
                {
                  File localFile = new File(str3);
                  localUri = Uri.fromFile(localFile);
                }
                localIntent2 = new Intent("android.intent.action.VIEW");
                localIntent2.setDataAndType(localUri, str4);
                localIntent2.setFlags(268435456);
              }
          }
          String str1;
          String str2;
          do
          {
            try
            {
              paramContext.startActivity(localIntent2);
              localCursor1.close();
              NotificationManager localNotificationManager = (NotificationManager)paramContext.getSystemService("notification");
              if (localNotificationManager == null)
                break;
              localNotificationManager.cancel((int)ContentUris.parseId(paramIntent.getData()));
              return;
              Log.v("DownloadManager", "Receiver list for " + paramIntent.getData());
            }
            catch (ActivityNotFoundException localActivityNotFoundException)
            {
              while (true)
                Log.d("DownloadManager", "no activity for " + str4, localActivityNotFoundException);
            }
            int k = localCursor1.getColumnIndexOrThrow("notificationpackage");
            int m = localCursor1.getColumnIndexOrThrow("notificationclass");
            str1 = localCursor1.getString(k);
            str2 = localCursor1.getString(m);
          }
          while ((str1 == null) || (str2 == null));
          Intent localIntent1 = new Intent("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED");
          localIntent1.setClassName(str1, str2);
          if (paramIntent.getBooleanExtra("multiple", true))
            localIntent1.setData(Downloads.Impl.CONTENT_URI);
          while (true)
          {
            paramContext.sendBroadcast(localIntent1);
            break;
            localIntent1.setData(paramIntent.getData());
          }
        }
      }
      while (!paramIntent.getAction().equals("android.intent.action.DOWNLOAD_HIDE"));
      if (Constants.LOGVV)
        Log.v("DownloadManager", "Receiver hide for " + paramIntent.getData());
      localCursor2 = paramContext.getContentResolver().query(paramIntent.getData(), null, null, null, null);
    }
    while (localCursor2 == null);
    if (localCursor2.moveToFirst())
    {
      int i2 = localCursor2.getInt(localCursor2.getColumnIndexOrThrow("status"));
      int i3 = localCursor2.getInt(localCursor2.getColumnIndexOrThrow("visibility"));
      if ((Downloads.Impl.isStatusCompleted(i2)) && (i3 == 1))
      {
        ContentValues localContentValues2 = new ContentValues();
        localContentValues2.put("visibility", Integer.valueOf(0));
        paramContext.getContentResolver().update(paramIntent.getData(), localContentValues2, null, null);
      }
    }
    localCursor2.close();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.downloadprovider.DownloadReceiver
 * JD-Core Version:    0.6.2
 */