package com.google.android.gm.downloadprovider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class DownloadService extends Service
{
  private ArrayList<DownloadInfo> mDownloads;
  private boolean mInitialCleanupNeeded = true;
  private CharArrayBuffer mNewChars;
  private DownloadManagerContentObserver mObserver;
  private boolean mPendingUpdate;
  private UpdateThread mUpdateThread;
  private CharArrayBuffer oldChars;

  private void deleteDownload(int paramInt)
  {
    DownloadInfo localDownloadInfo = (DownloadInfo)this.mDownloads.get(paramInt);
    if (localDownloadInfo.mStatus == 192)
      localDownloadInfo.mStatus = 490;
    while (true)
    {
      this.mDownloads.remove(paramInt);
      return;
      if ((localDownloadInfo.mDestination != 0) && (localDownloadInfo.mFileName != null))
        new File(localDownloadInfo.mFileName).delete();
    }
  }

  private void insertDownload(Cursor paramCursor, int paramInt, boolean paramBoolean1, boolean paramBoolean2, long paramLong)
  {
    int i = paramCursor.getColumnIndexOrThrow("status");
    int j = paramCursor.getColumnIndexOrThrow("numfailed");
    int k = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("method"));
    int m = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("_id"));
    String str1 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("uri"));
    boolean bool1;
    boolean bool2;
    label400: DownloadInfo localDownloadInfo;
    String str12;
    label526: String str13;
    label987: String str14;
    if (paramCursor.getInt(paramCursor.getColumnIndexOrThrow("no_integrity")) == 1)
    {
      bool1 = true;
      String str2 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hint"));
      String str3 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("_data"));
      String str4 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("mimetype"));
      int n = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("destination"));
      int i1 = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("visibility"));
      int i2 = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("control"));
      int i3 = paramCursor.getInt(i);
      int i4 = paramCursor.getInt(j);
      int i5 = 0xFFFFFFF & k;
      int i6 = k >> 28;
      long l = paramCursor.getLong(paramCursor.getColumnIndexOrThrow("lastmod"));
      String str5 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("notificationpackage"));
      String str6 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("notificationclass"));
      String str7 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("notificationextras"));
      String str8 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("cookiedata"));
      String str9 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("useragent"));
      String str10 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("referer"));
      int i7 = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("total_bytes"));
      int i8 = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("current_bytes"));
      String str11 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("etag"));
      if (paramCursor.getInt(paramCursor.getColumnIndexOrThrow("scanned")) != 1)
        break label1390;
      bool2 = true;
      localDownloadInfo = new DownloadInfo(m, str1, bool1, str2, str3, str4, n, i1, i2, i3, i4, i5, i6, l, str5, str6, str7, str8, str9, str10, i7, i8, str11, bool2);
      if (Constants.LOGVV)
      {
        Log.v("DownloadManager", "Service adding new entry");
        Log.v("DownloadManager", "ID      : " + localDownloadInfo.mId);
        StringBuilder localStringBuilder1 = new StringBuilder().append("URI     : ");
        if (localDownloadInfo.mUri == null)
          break label1396;
        str12 = "yes";
        Log.v("DownloadManager", str12);
        Log.v("DownloadManager", "NO_INTEG: " + localDownloadInfo.mNoIntegrity);
        Log.v("DownloadManager", "HINT    : " + localDownloadInfo.mHint);
        Log.v("DownloadManager", "FILENAME: " + localDownloadInfo.mFileName);
        Log.v("DownloadManager", "MIMETYPE: " + localDownloadInfo.mMimeType);
        Log.v("DownloadManager", "DESTINAT: " + localDownloadInfo.mDestination);
        Log.v("DownloadManager", "VISIBILI: " + localDownloadInfo.mVisibility);
        Log.v("DownloadManager", "CONTROL : " + localDownloadInfo.mControl);
        Log.v("DownloadManager", "STATUS  : " + localDownloadInfo.mStatus);
        Log.v("DownloadManager", "FAILED_C: " + localDownloadInfo.mNumFailed);
        Log.v("DownloadManager", "RETRY_AF: " + localDownloadInfo.mRetryAfter);
        Log.v("DownloadManager", "REDIRECT: " + localDownloadInfo.mRedirectCount);
        Log.v("DownloadManager", "LAST_MOD: " + localDownloadInfo.mLastMod);
        Log.v("DownloadManager", "PACKAGE : " + localDownloadInfo.mPackage);
        Log.v("DownloadManager", "CLASS   : " + localDownloadInfo.mClass);
        StringBuilder localStringBuilder2 = new StringBuilder().append("COOKIES : ");
        if (localDownloadInfo.mCookies == null)
          break label1404;
        str13 = "yes";
        Log.v("DownloadManager", str13);
        Log.v("DownloadManager", "AGENT   : " + localDownloadInfo.mUserAgent);
        StringBuilder localStringBuilder3 = new StringBuilder().append("REFERER : ");
        if (localDownloadInfo.mReferer == null)
          break label1412;
        str14 = "yes";
        label1060: Log.v("DownloadManager", str14);
        Log.v("DownloadManager", "TOTAL   : " + localDownloadInfo.mTotalBytes);
        Log.v("DownloadManager", "CURRENT : " + localDownloadInfo.mCurrentBytes);
        Log.v("DownloadManager", "ETAG    : " + localDownloadInfo.mETag);
        Log.v("DownloadManager", "SCANNED : " + localDownloadInfo.mMediaScanned);
      }
      this.mDownloads.add(paramInt, localDownloadInfo);
      if ((localDownloadInfo.mStatus != 0) || ((localDownloadInfo.mDestination != 0) && (localDownloadInfo.mDestination != 2)) || (localDownloadInfo.mMimeType == null))
        break label1420;
      Intent localIntent = new Intent("android.intent.action.VIEW");
      localIntent.setDataAndType(Uri.fromParts("file", "", null), localDownloadInfo.mMimeType);
      if (getPackageManager().resolveActivity(localIntent, 65536) != null)
        break label1420;
      Log.d("DownloadManager", "no application to handle MIME type " + localDownloadInfo.mMimeType);
      localDownloadInfo.mStatus = 406;
      Uri localUri2 = ContentUris.withAppendedId(Downloads.Impl.CONTENT_URI, localDownloadInfo.mId);
      ContentValues localContentValues3 = new ContentValues();
      localContentValues3.put("status", Integer.valueOf(406));
      getContentResolver().update(localUri2, localContentValues3, null, null);
      localDownloadInfo.sendIntentIfRequested(localUri2, this);
    }
    label1390: label1396: label1404: 
    do
    {
      do
      {
        return;
        bool1 = false;
        break;
        bool2 = false;
        break label400;
        str12 = "no";
        break label526;
        str13 = "no";
        break label987;
        str14 = "no";
        break label1060;
        if (!localDownloadInfo.canUseNetwork(paramBoolean1, paramBoolean2))
          break label1587;
      }
      while (!localDownloadInfo.isReadyToStart(paramLong));
      if (Constants.LOGV)
        Log.v("DownloadManager", "Service spawning thread to handle new download " + localDownloadInfo.mId);
      if (localDownloadInfo.mHasActiveThread)
        throw new IllegalStateException("Multiple threads on same download on insert");
      if (localDownloadInfo.mStatus != 192)
      {
        localDownloadInfo.mStatus = 192;
        ContentValues localContentValues2 = new ContentValues();
        localContentValues2.put("status", Integer.valueOf(localDownloadInfo.mStatus));
        getContentResolver().update(ContentUris.withAppendedId(Downloads.Impl.CONTENT_URI, localDownloadInfo.mId), localContentValues2, null, null);
      }
      DownloadThread localDownloadThread = new DownloadThread(this, localDownloadInfo);
      localDownloadInfo.mHasActiveThread = true;
      localDownloadThread.start();
      return;
    }
    while ((localDownloadInfo.mStatus != 0) && (localDownloadInfo.mStatus != 190) && (localDownloadInfo.mStatus != 192));
    label1412: label1420: label1587: localDownloadInfo.mStatus = 193;
    Uri localUri1 = ContentUris.withAppendedId(Downloads.Impl.CONTENT_URI, localDownloadInfo.mId);
    ContentValues localContentValues1 = new ContentValues();
    localContentValues1.put("status", Integer.valueOf(193));
    getContentResolver().update(localUri1, localContentValues1, null, null);
  }

  private long nextAction(int paramInt, long paramLong)
  {
    DownloadInfo localDownloadInfo = (DownloadInfo)this.mDownloads.get(paramInt);
    if (Downloads.Impl.isStatusCompleted(localDownloadInfo.mStatus))
      return -1L;
    if (localDownloadInfo.mStatus != 193)
      return 0L;
    if (localDownloadInfo.mNumFailed == 0)
      return 0L;
    long l = localDownloadInfo.restartTime();
    if (l <= paramLong)
      return 0L;
    return l - paramLong;
  }

  private void removeSpuriousFiles(Context paramContext)
  {
    File[] arrayOfFile = new File(paramContext.getCacheDir() + "/download").listFiles();
    if (arrayOfFile == null);
    while (true)
    {
      return;
      HashSet localHashSet = new HashSet();
      int i = 0;
      if (i < arrayOfFile.length)
      {
        if (arrayOfFile[i].getName().equals("lost+found"));
        while (true)
        {
          i++;
          break;
          if (!arrayOfFile[i].getName().equalsIgnoreCase("recovery"))
            localHashSet.add(arrayOfFile[i].getPath());
        }
      }
      Cursor localCursor = getContentResolver().query(Downloads.Impl.CONTENT_URI, new String[] { "_data" }, null, null, null);
      if (localCursor != null)
      {
        if (localCursor.moveToFirst())
          do
            localHashSet.remove(localCursor.getString(0));
          while (localCursor.moveToNext());
        localCursor.close();
      }
      Iterator localIterator = localHashSet.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if (Constants.LOGV)
          Log.v("DownloadManager", "deleting spurious file " + str);
        new File(str).delete();
      }
    }
  }

  private boolean scanFile(Cursor paramCursor, int paramInt)
  {
    DownloadInfo localDownloadInfo = (DownloadInfo)this.mDownloads.get(paramInt);
    try
    {
      if (Constants.LOGV)
        Log.v("DownloadManager", "Scanning file " + localDownloadInfo.mFileName);
      Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
      localIntent.setData(Uri.fromFile(new File(localDownloadInfo.mFileName)));
      sendBroadcast(localIntent);
      if (paramCursor != null)
      {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("scanned", Integer.valueOf(1));
        getContentResolver().update(ContentUris.withAppendedId(Downloads.Impl.CONTENT_URI, paramCursor.getLong(paramCursor.getColumnIndexOrThrow("_id"))), localContentValues, null, null);
      }
      return true;
    }
    finally
    {
    }
  }

  private boolean shouldScanFile(int paramInt)
  {
    DownloadInfo localDownloadInfo = (DownloadInfo)this.mDownloads.get(paramInt);
    return (!localDownloadInfo.mMediaScanned) && (localDownloadInfo.mDestination == 0) && (Downloads.Impl.isStatusSuccess(localDownloadInfo.mStatus));
  }

  private String stringFromCursor(String paramString1, Cursor paramCursor, String paramString2)
  {
    int i = paramCursor.getColumnIndexOrThrow(paramString2);
    if (paramString1 == null)
      return paramCursor.getString(i);
    if (this.mNewChars == null)
      this.mNewChars = new CharArrayBuffer(128);
    paramCursor.copyStringToBuffer(i, this.mNewChars);
    int j = this.mNewChars.sizeCopied;
    if (j != paramString1.length())
      return paramCursor.getString(i);
    if ((this.oldChars == null) || (this.oldChars.sizeCopied < j))
      this.oldChars = new CharArrayBuffer(j);
    char[] arrayOfChar1 = this.oldChars.data;
    char[] arrayOfChar2 = this.mNewChars.data;
    paramString1.getChars(0, j, arrayOfChar1, 0);
    for (int k = j - 1; k >= 0; k--)
      if (arrayOfChar1[k] != arrayOfChar2[k])
        return new String(arrayOfChar2, 0, j);
    return paramString1;
  }

  private void trimDatabase()
  {
    Cursor localCursor = getContentResolver().query(Downloads.Impl.CONTENT_URI, new String[] { "_id" }, "status >= '200'", null, "lastmod");
    if (localCursor == null)
    {
      Log.e("DownloadManager", "null cursor in trimDatabase");
      return;
    }
    int i;
    int j;
    if (localCursor.moveToFirst())
    {
      i = localCursor.getCount() - 1000;
      j = localCursor.getColumnIndexOrThrow("_id");
    }
    while (true)
    {
      if (i > 0)
      {
        getContentResolver().delete(ContentUris.withAppendedId(Downloads.Impl.CONTENT_URI, localCursor.getLong(j)), null, null);
        if (localCursor.moveToNext());
      }
      else
      {
        localCursor.close();
        return;
      }
      i--;
    }
  }

  private void updateDownload(Cursor paramCursor, int paramInt, boolean paramBoolean1, boolean paramBoolean2, long paramLong)
  {
    DownloadInfo localDownloadInfo = (DownloadInfo)this.mDownloads.get(paramInt);
    int i = paramCursor.getColumnIndexOrThrow("status");
    int j = paramCursor.getColumnIndexOrThrow("numfailed");
    localDownloadInfo.mId = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("_id"));
    localDownloadInfo.mUri = stringFromCursor(localDownloadInfo.mUri, paramCursor, "uri");
    boolean bool1;
    if (paramCursor.getInt(paramCursor.getColumnIndexOrThrow("no_integrity")) == 1)
    {
      bool1 = true;
      localDownloadInfo.mNoIntegrity = bool1;
      localDownloadInfo.mHint = stringFromCursor(localDownloadInfo.mHint, paramCursor, "hint");
      localDownloadInfo.mFileName = stringFromCursor(localDownloadInfo.mFileName, paramCursor, "_data");
      localDownloadInfo.mMimeType = stringFromCursor(localDownloadInfo.mMimeType, paramCursor, "mimetype");
      localDownloadInfo.mDestination = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("destination"));
      localDownloadInfo.mVisibility = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("visibility"));
    }
    while (true)
    {
      try
      {
        localDownloadInfo.mControl = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("control"));
        localDownloadInfo.mStatus = paramCursor.getInt(i);
        localDownloadInfo.mNumFailed = paramCursor.getInt(j);
        int k = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("method"));
        localDownloadInfo.mRetryAfter = (0xFFFFFFF & k);
        localDownloadInfo.mRedirectCount = (k >> 28);
        localDownloadInfo.mLastMod = paramCursor.getLong(paramCursor.getColumnIndexOrThrow("lastmod"));
        localDownloadInfo.mPackage = stringFromCursor(localDownloadInfo.mPackage, paramCursor, "notificationpackage");
        localDownloadInfo.mClass = stringFromCursor(localDownloadInfo.mClass, paramCursor, "notificationclass");
        localDownloadInfo.mCookies = stringFromCursor(localDownloadInfo.mCookies, paramCursor, "cookiedata");
        localDownloadInfo.mUserAgent = stringFromCursor(localDownloadInfo.mUserAgent, paramCursor, "useragent");
        localDownloadInfo.mReferer = stringFromCursor(localDownloadInfo.mReferer, paramCursor, "referer");
        localDownloadInfo.mTotalBytes = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("total_bytes"));
        localDownloadInfo.mCurrentBytes = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("current_bytes"));
        localDownloadInfo.mETag = stringFromCursor(localDownloadInfo.mETag, paramCursor, "etag");
        if (paramCursor.getInt(paramCursor.getColumnIndexOrThrow("scanned")) == 1)
        {
          bool2 = true;
          localDownloadInfo.mMediaScanned = bool2;
          if ((!localDownloadInfo.canUseNetwork(paramBoolean1, paramBoolean2)) || (!localDownloadInfo.isReadyToRestart(paramLong)))
            return;
          if (Constants.LOGV)
            Log.v("DownloadManager", "Service spawning thread to handle updated download " + localDownloadInfo.mId);
          if (!localDownloadInfo.mHasActiveThread)
            break label556;
          throw new IllegalStateException("Multiple threads on same download on update");
          bool1 = false;
          break;
        }
      }
      finally
      {
      }
      boolean bool2 = false;
    }
    label556: localDownloadInfo.mStatus = 192;
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("status", Integer.valueOf(localDownloadInfo.mStatus));
    getContentResolver().update(ContentUris.withAppendedId(Downloads.Impl.CONTENT_URI, localDownloadInfo.mId), localContentValues, null, null);
    DownloadThread localDownloadThread = new DownloadThread(this, localDownloadInfo);
    localDownloadInfo.mHasActiveThread = true;
    localDownloadThread.start();
  }

  private void updateFromProvider()
  {
    try
    {
      this.mPendingUpdate = true;
      if (this.mUpdateThread == null)
      {
        this.mUpdateThread = new UpdateThread();
        this.mUpdateThread.start();
      }
      return;
    }
    finally
    {
    }
  }

  private boolean visibleNotification(int paramInt)
  {
    return ((DownloadInfo)this.mDownloads.get(paramInt)).hasCompletionNotification();
  }

  public IBinder onBind(Intent paramIntent)
  {
    throw new UnsupportedOperationException("Cannot bind to Download Manager Service");
  }

  public void onCreate()
  {
    super.onCreate();
    if (Constants.LOGVV)
      Log.v("DownloadManager", "Service onCreate");
    this.mDownloads = Lists.newArrayList();
    this.mObserver = new DownloadManagerContentObserver();
    getContentResolver().registerContentObserver(Downloads.Impl.CONTENT_URI, true, this.mObserver);
    updateFromProvider();
  }

  public void onDestroy()
  {
    getContentResolver().unregisterContentObserver(this.mObserver);
    if (Constants.LOGVV)
      Log.v("DownloadManager", "Service onDestroy");
    super.onDestroy();
  }

  public void onStart(Intent paramIntent, int paramInt)
  {
    super.onStart(paramIntent, paramInt);
    if (Constants.LOGVV)
      Log.v("DownloadManager", "Service onStart");
    updateFromProvider();
  }

  private class DownloadManagerContentObserver extends ContentObserver
  {
    public DownloadManagerContentObserver()
    {
      super();
    }

    public void onChange(boolean paramBoolean)
    {
      if (Constants.LOGVV)
        Log.v("DownloadManager", "Service ContentObserver received notification");
      DownloadService.this.updateFromProvider();
    }
  }

  private class UpdateThread extends Thread
  {
    public UpdateThread()
    {
      super();
    }

    public void run()
    {
      Process.setThreadPriority(10);
      if (DownloadService.this.mInitialCleanupNeeded)
      {
        DownloadService.this.trimDatabase();
        DownloadService.this.removeSpuriousFiles(DownloadService.this);
        DownloadService.access$102(DownloadService.this, false);
      }
      int i = 0;
      long l1 = 9223372036854775807L;
      while (true)
      {
        synchronized (DownloadService.this)
        {
          if (DownloadService.this.mUpdateThread != this)
            throw new IllegalStateException("multiple UpdateThreads in DownloadService");
        }
        if (!DownloadService.this.mPendingUpdate)
        {
          DownloadService.access$402(DownloadService.this, null);
          if (i == 0)
            DownloadService.this.stopSelf();
          AlarmManager localAlarmManager;
          if (l1 != 9223372036854775807L)
          {
            localAlarmManager = (AlarmManager)DownloadService.this.getSystemService("alarm");
            if (localAlarmManager != null)
              break label173;
            Log.e("DownloadManager", "couldn't get alarm manager");
          }
          while (true)
          {
            DownloadService.access$602(DownloadService.this, null);
            DownloadService.access$702(DownloadService.this, null);
            return;
            label173: if (Constants.LOGV)
              Log.v("DownloadManager", "scheduling retry in " + l1 + "ms");
            Intent localIntent = new Intent("android.intent.action.DOWNLOAD_WAKEUP");
            localIntent.setClassName("com.android.providers.downloads", DownloadReceiver.class.getName());
            localAlarmManager.set(0, l1 + System.currentTimeMillis(), PendingIntent.getBroadcast(DownloadService.this, 0, localIntent, 1073741824));
          }
        }
        DownloadService.access$502(DownloadService.this, false);
        boolean bool1 = Helpers.isNetworkAvailable(DownloadService.this);
        boolean bool2 = Helpers.isNetworkRoaming(DownloadService.this);
        long l2 = System.currentTimeMillis();
        Cursor localCursor = DownloadService.this.getContentResolver().query(Downloads.Impl.CONTENT_URI, null, null, null, "_id");
        if (localCursor == null)
        {
          Log.e("DownloadManager", "couldn't get download cursor");
        }
        else
        {
          localCursor.moveToFirst();
          int j = 0;
          i = 0;
          l1 = 9223372036854775807L;
          boolean bool3 = localCursor.isAfterLast();
          int k = localCursor.getColumnIndexOrThrow("_id");
          while ((!bool3) || (j < DownloadService.this.mDownloads.size()))
            if (bool3)
            {
              if (Constants.LOGVV)
              {
                int i1 = ((DownloadInfo)DownloadService.this.mDownloads.get(j)).mId;
                Log.v("DownloadManager", "Array update: trimming " + i1 + " @ " + j);
              }
              if (DownloadService.this.shouldScanFile(j))
                DownloadService.this.scanFile(null, j);
              DownloadService.this.deleteDownload(j);
            }
            else
            {
              int m = localCursor.getInt(k);
              if (j == DownloadService.this.mDownloads.size())
              {
                DownloadService.this.insertDownload(localCursor, j, bool1, bool2, l2);
                if (Constants.LOGVV)
                  Log.v("DownloadManager", "Array update: appending " + m + " @ " + j);
                if ((DownloadService.this.shouldScanFile(j)) && (!DownloadService.this.scanFile(localCursor, j)))
                  i = 1;
                if (DownloadService.this.visibleNotification(j))
                  i = 1;
                long l5 = DownloadService.this.nextAction(j, l2);
                if (l5 == 0L)
                  i = 1;
                while (true)
                {
                  j++;
                  localCursor.moveToNext();
                  bool3 = localCursor.isAfterLast();
                  break;
                  if ((l5 > 0L) && (l5 < l1))
                    l1 = l5;
                }
              }
              int n = ((DownloadInfo)DownloadService.this.mDownloads.get(j)).mId;
              if (n < m)
              {
                if (Constants.LOGVV)
                  Log.v("DownloadManager", "Array update: removing " + n + " @ " + j);
                if (DownloadService.this.shouldScanFile(j))
                  DownloadService.this.scanFile(null, j);
                DownloadService.this.deleteDownload(j);
              }
              else
              {
                if (n == m)
                {
                  DownloadService.this.updateDownload(localCursor, j, bool1, bool2, l2);
                  if ((DownloadService.this.shouldScanFile(j)) && (!DownloadService.this.scanFile(localCursor, j)))
                    i = 1;
                  if (DownloadService.this.visibleNotification(j))
                    i = 1;
                  long l4 = DownloadService.this.nextAction(j, l2);
                  if (l4 == 0L)
                    i = 1;
                  while (true)
                  {
                    j++;
                    localCursor.moveToNext();
                    bool3 = localCursor.isAfterLast();
                    break;
                    if ((l4 > 0L) && (l4 < l1))
                      l1 = l4;
                  }
                }
                if (Constants.LOGVV)
                  Log.v("DownloadManager", "Array update: inserting " + m + " @ " + j);
                DownloadService.this.insertDownload(localCursor, j, bool1, bool2, l2);
                if ((DownloadService.this.shouldScanFile(j)) && (!DownloadService.this.scanFile(localCursor, j)))
                  i = 1;
                if (DownloadService.this.visibleNotification(j))
                  i = 1;
                long l3 = DownloadService.this.nextAction(j, l2);
                if (l3 == 0L)
                  i = 1;
                while (true)
                {
                  j++;
                  localCursor.moveToNext();
                  bool3 = localCursor.isAfterLast();
                  break;
                  if ((l3 > 0L) && (l3 < l1))
                    l1 = l3;
                }
              }
            }
          localCursor.close();
        }
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.downloadprovider.DownloadService
 * JD-Core Version:    0.6.2
 */