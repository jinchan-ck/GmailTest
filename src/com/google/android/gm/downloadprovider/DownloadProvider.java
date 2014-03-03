package com.google.android.gm.downloadprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public final class DownloadProvider extends ContentProvider
{
  private static final String DB_NAME = "downloads.db";
  private static final String DB_TABLE = "downloads";
  private static final int DB_VERSION = 100;
  private static final int DB_VERSION_NOP_UPGRADE_FROM = 31;
  private static final int DB_VERSION_NOP_UPGRADE_TO = 100;
  private static final int DOWNLOADS = 1;
  private static final int DOWNLOADS_ID = 2;
  private static final String DOWNLOAD_LIST_TYPE = "vnd.android.cursor.dir/download";
  private static final String DOWNLOAD_TYPE = "vnd.android.cursor.item/download";
  private static final String[] sAppReadableColumnsArray;
  private static HashSet<String> sAppReadableColumnsSet;
  private static final UriMatcher sURIMatcher = new UriMatcher(-1);
  private SQLiteOpenHelper mOpenHelper = null;

  static
  {
    sURIMatcher.addURI("gmail-downloads", "download", 1);
    sURIMatcher.addURI("gmail-downloads", "download/#", 2);
    sAppReadableColumnsArray = new String[] { "_id", "entity", "_data", "mimetype", "visibility", "destination", "control", "status", "lastmod", "notificationpackage", "notificationclass", "total_bytes", "current_bytes", "title", "description" };
    sAppReadableColumnsSet = new HashSet();
    for (int i = 0; i < sAppReadableColumnsArray.length; i++)
      sAppReadableColumnsSet.add(sAppReadableColumnsArray[i]);
  }

  private static final void copyBoolean(String paramString, ContentValues paramContentValues1, ContentValues paramContentValues2)
  {
    Boolean localBoolean = paramContentValues1.getAsBoolean(paramString);
    if (localBoolean != null)
      paramContentValues2.put(paramString, localBoolean);
  }

  private static final void copyInteger(String paramString, ContentValues paramContentValues1, ContentValues paramContentValues2)
  {
    Integer localInteger = paramContentValues1.getAsInteger(paramString);
    if (localInteger != null)
      paramContentValues2.put(paramString, localInteger);
  }

  private static final void copyString(String paramString, ContentValues paramContentValues1, ContentValues paramContentValues2)
  {
    String str = paramContentValues1.getAsString(paramString);
    if (str != null)
      paramContentValues2.put(paramString, str);
  }

  private void createTable(SQLiteDatabase paramSQLiteDatabase)
  {
    try
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE downloads(_id INTEGER PRIMARY KEY AUTOINCREMENT,uri TEXT, method INTEGER, entity TEXT, no_integrity BOOLEAN, hint TEXT, otaupdate BOOLEAN, _data TEXT, mimetype TEXT, destination INTEGER, no_system BOOLEAN, visibility INTEGER, control INTEGER, status INTEGER, numfailed INTEGER, lastmod BIGINT, notificationpackage TEXT, notificationclass TEXT, notificationextras TEXT, cookiedata TEXT, useragent TEXT, referer TEXT, total_bytes INTEGER, current_bytes INTEGER, etag TEXT, uid INTEGER, otheruid INTEGER, title TEXT, description TEXT, scanned BOOLEAN);");
      return;
    }
    catch (SQLException localSQLException)
    {
      Log.e("DownloadManager", "couldn't create table in downloads database");
      throw localSQLException;
    }
  }

  private void dropTable(SQLiteDatabase paramSQLiteDatabase)
  {
    try
    {
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS downloads");
      return;
    }
    catch (SQLException localSQLException)
    {
      Log.e("DownloadManager", "couldn't drop table in downloads database");
      throw localSQLException;
    }
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getWritableDatabase();
    int i = sURIMatcher.match(paramUri);
    switch (i)
    {
    default:
      Log.d("DownloadManager", "deleting unknown/invalid URI: " + paramUri);
      throw new UnsupportedOperationException("Cannot delete URI: " + paramUri);
    case 1:
    case 2:
    }
    ArrayList localArrayList = Lists.newArrayList();
    String str;
    if (paramString != null)
      if (i == 1)
        str = "( " + paramString + " )";
    while (true)
    {
      if (i == 2)
      {
        long l = Long.parseLong((String)paramUri.getPathSegments().get(1));
        str = str + " ( _id = ? ) ";
        localArrayList.add(Long.toString(l));
      }
      if (paramArrayOfString != null)
        localArrayList.addAll(Arrays.asList(paramArrayOfString));
      int j = localSQLiteDatabase.delete("downloads", str, (String[])localArrayList.toArray(new String[localArrayList.size()]));
      getContext().getContentResolver().notifyChange(paramUri, null);
      return j;
      str = "( " + paramString + " ) AND ";
      continue;
      str = "";
    }
  }

  public String getType(Uri paramUri)
  {
    switch (sURIMatcher.match(paramUri))
    {
    default:
      if (Constants.LOGV)
        Log.v("DownloadManager", "calling getType on an unknown URI: " + paramUri);
      throw new IllegalArgumentException("Unknown URI: " + paramUri);
    case 1:
      return "vnd.android.cursor.dir/download";
    case 2:
    }
    return "vnd.android.cursor.item/download";
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getWritableDatabase();
    if (sURIMatcher.match(paramUri) != 1)
    {
      Log.d("DownloadManager", "calling insert on an unknown/invalid URI: " + paramUri);
      throw new IllegalArgumentException("Unknown/Invalid URI " + paramUri);
    }
    ContentValues localContentValues = new ContentValues();
    copyString("uri", paramContentValues, localContentValues);
    copyString("entity", paramContentValues, localContentValues);
    copyBoolean("no_integrity", paramContentValues, localContentValues);
    copyString("hint", paramContentValues, localContentValues);
    copyString("mimetype", paramContentValues, localContentValues);
    Integer localInteger1 = paramContentValues.getAsInteger("destination");
    if (localInteger1 != null)
    {
      if ((getContext().checkCallingPermission("android.permission.ACCESS_DOWNLOAD_MANAGER_ADVANCED") != 0) && (localInteger1.intValue() != 0) && (localInteger1.intValue() != 2))
        throw new SecurityException("unauthorized destination code");
      localContentValues.put("destination", localInteger1);
    }
    Integer localInteger2 = paramContentValues.getAsInteger("visibility");
    if (localInteger2 == null)
      if (localInteger1.intValue() == 0)
        localContentValues.put("visibility", Integer.valueOf(1));
    while (true)
    {
      copyInteger("control", paramContentValues, localContentValues);
      localContentValues.put("status", Integer.valueOf(190));
      localContentValues.put("lastmod", Long.valueOf(System.currentTimeMillis()));
      String str1 = paramContentValues.getAsString("notificationpackage");
      String str2 = paramContentValues.getAsString("notificationclass");
      int i;
      if ((str1 != null) && (str2 != null))
      {
        i = Binder.getCallingUid();
        if (i == 0);
      }
      try
      {
        if (getContext().getPackageManager().getApplicationInfo(str1, 0).uid == i)
        {
          localContentValues.put("notificationpackage", str1);
          localContentValues.put("notificationclass", str2);
        }
        label329: copyString("notificationextras", paramContentValues, localContentValues);
        copyString("cookiedata", paramContentValues, localContentValues);
        copyString("useragent", paramContentValues, localContentValues);
        copyString("referer", paramContentValues, localContentValues);
        if (getContext().checkCallingPermission("android.permission.ACCESS_DOWNLOAD_MANAGER_ADVANCED") == 0)
          copyInteger("otheruid", paramContentValues, localContentValues);
        localContentValues.put("uid", Integer.valueOf(Binder.getCallingUid()));
        if (Binder.getCallingUid() == 0)
          copyInteger("uid", paramContentValues, localContentValues);
        copyString("title", paramContentValues, localContentValues);
        copyString("description", paramContentValues, localContentValues);
        if (Constants.LOGVV)
        {
          Log.v("DownloadManager", "initiating download with UID " + localContentValues.getAsInteger("uid"));
          if (localContentValues.containsKey("otheruid"))
            Log.v("DownloadManager", "other UID " + localContentValues.getAsInteger("otheruid"));
        }
        Context localContext = getContext();
        localContext.startService(new Intent(localContext, DownloadService.class));
        long l = localSQLiteDatabase.insert("downloads", null, localContentValues);
        if (l != -1L)
        {
          localContext.startService(new Intent(localContext, DownloadService.class));
          Uri localUri = Uri.parse(Downloads.Impl.CONTENT_URI + "/" + l);
          localContext.getContentResolver().notifyChange(paramUri, null);
          return localUri;
          localContentValues.put("visibility", Integer.valueOf(2));
          continue;
          localContentValues.put("visibility", localInteger2);
          continue;
        }
        Log.d("DownloadManager", "couldn't insert into downloads database");
        return null;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        break label329;
      }
    }
  }

  public boolean onCreate()
  {
    this.mOpenHelper = new DatabaseHelper(getContext());
    return true;
  }

  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    Cursor localCursor2;
    Cursor localCursor3;
    Cursor localCursor1;
    int i;
    if (Constants.LOGVV)
    {
      Log.v("DownloadManager", "openFile uri: " + paramUri + ", mode: " + paramString + ", uid: " + Binder.getCallingUid());
      localCursor2 = query(Downloads.Impl.CONTENT_URI, new String[] { "_id" }, null, null, "_id");
      if (localCursor2 == null)
      {
        Log.v("DownloadManager", "null cursor in openFile");
        localCursor3 = query(paramUri, new String[] { "_data" }, null, null, null);
        if (localCursor3 != null)
          break label283;
        Log.v("DownloadManager", "null cursor in openFile");
      }
    }
    else
    {
      localCursor1 = query(paramUri, new String[] { "_data" }, null, null, null);
      if (localCursor1 == null)
        break label376;
      i = localCursor1.getCount();
    }
    while (true)
      if (i != 1)
      {
        if (localCursor1 != null)
          localCursor1.close();
        if (i == 0)
        {
          throw new FileNotFoundException("No entry for " + paramUri);
          if (!localCursor2.moveToFirst())
            Log.v("DownloadManager", "empty cursor in openFile");
          while (true)
          {
            localCursor2.close();
            break;
            do
              Log.v("DownloadManager", "row " + localCursor2.getInt(0) + " available");
            while (localCursor2.moveToNext());
          }
          label283: if (!localCursor3.moveToFirst())
            Log.v("DownloadManager", "empty cursor in openFile");
          while (true)
          {
            localCursor3.close();
            break;
            String str2 = localCursor3.getString(0);
            Log.v("DownloadManager", "filename in openFile: " + str2);
            if (new File(str2).isFile())
              Log.v("DownloadManager", "file exists in openFile");
          }
          label376: i = 0;
        }
        else
        {
          throw new FileNotFoundException("Multiple items at " + paramUri);
        }
      }
    localCursor1.moveToFirst();
    String str1 = localCursor1.getString(0);
    localCursor1.close();
    if (str1 == null)
      throw new FileNotFoundException("No filename found.");
    if (!Helpers.isFilenameValid(getContext(), str1))
      throw new FileNotFoundException("Invalid filename.");
    if (!"r".equals(paramString))
      throw new FileNotFoundException("Bad mode for " + paramUri + ": " + paramString);
    ParcelFileDescriptor localParcelFileDescriptor = ParcelFileDescriptor.open(new File(str1), 268435456);
    if (localParcelFileDescriptor == null)
    {
      if (Constants.LOGV)
        Log.v("DownloadManager", "couldn't open file");
      throw new FileNotFoundException("couldn't open file");
    }
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("lastmod", Long.valueOf(System.currentTimeMillis()));
    update(paramUri, localContentValues, null, null);
    return localParcelFileDescriptor;
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    if (Constants.LOGVV)
      Log.v("DownloadManager", "Gmail DownloadProvider servicing database query: " + paramUri);
    SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getReadableDatabase();
    SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
    ArrayList localArrayList = Lists.newArrayList();
    StringBuilder localStringBuilder;
    label211: label250: Object localObject;
    switch (sURIMatcher.match(paramUri))
    {
    default:
      if (Constants.LOGV)
        Log.v("DownloadManager", "querying unknown URI: " + paramUri);
      throw new IllegalArgumentException("Unknown URI: " + paramUri);
    case 1:
      localSQLiteQueryBuilder.setTables("downloads");
      if (Constants.LOGVV)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("starting query, database is ");
        if (localSQLiteDatabase != null)
          localStringBuilder.append("not ");
        localStringBuilder.append("null; ");
        if (paramArrayOfString1 == null)
        {
          localStringBuilder.append("projection is null; ");
          localStringBuilder.append("selection is ");
          localStringBuilder.append(paramString1);
          localStringBuilder.append("; ");
          if (paramArrayOfString2 != null)
            break label539;
          localStringBuilder.append("selectionArgs is null; ");
          localStringBuilder.append("sort is ");
          localStringBuilder.append(paramString2);
          localStringBuilder.append(".");
          Log.v("DownloadManager", localStringBuilder.toString());
        }
      }
      else
      {
        if (paramArrayOfString2 != null)
          localArrayList.addAll(Arrays.asList(paramArrayOfString2));
        localObject = localSQLiteQueryBuilder.query(localSQLiteDatabase, paramArrayOfString1, paramString1, (String[])localArrayList.toArray(new String[localArrayList.size()]), null, null, paramString2);
        if (localObject != null)
        {
          ReadOnlyCursorWrapper localReadOnlyCursorWrapper = new ReadOnlyCursorWrapper((Cursor)localObject);
          localObject = localReadOnlyCursorWrapper;
        }
        if (localObject == null)
          break label620;
        localContentResolver = getContext().getContentResolver();
        ((Cursor)localObject).setNotificationUri(localContentResolver, paramUri);
        if (Constants.LOGVV)
          Log.v("DownloadManager", "created cursor " + localObject + " on behalf of " + Binder.getCallingPid());
      }
      break;
    case 2:
    }
    label539: label620: 
    while (!Constants.LOGV)
    {
      ContentResolver localContentResolver;
      return localObject;
      localSQLiteQueryBuilder.setTables("downloads");
      localSQLiteQueryBuilder.appendWhere("_id= ?");
      localArrayList.add(paramUri.getPathSegments().get(1));
      break;
      if (paramArrayOfString1.length == 0)
      {
        localStringBuilder.append("projection is empty; ");
        break label211;
      }
      for (int i = 0; i < paramArrayOfString1.length; i++)
      {
        localStringBuilder.append("projection[");
        localStringBuilder.append(i);
        localStringBuilder.append("] is ");
        localStringBuilder.append(paramArrayOfString1[i]);
        localStringBuilder.append("; ");
      }
      break label211;
      if (paramArrayOfString2.length == 0)
      {
        localStringBuilder.append("selectionArgs is empty; ");
        break label250;
      }
      for (int j = 0; j < paramArrayOfString2.length; j++)
      {
        localStringBuilder.append("selectionArgs[");
        localStringBuilder.append(j);
        localStringBuilder.append("] is ");
        localStringBuilder.append(paramArrayOfString2[j]);
        localStringBuilder.append("; ");
      }
      break label250;
    }
    Log.v("DownloadManager", "query failed in downloads database");
    return localObject;
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getWritableDatabase();
    String str1 = paramContentValues.getAsString("_data");
    if (str1 != null)
    {
      Cursor localCursor = query(paramUri, new String[] { "title" }, null, null, null);
      if ((!localCursor.moveToFirst()) || (localCursor.getString(0) == null))
        paramContentValues.put("title", new File(str1).getName());
      localCursor.close();
    }
    int i = sURIMatcher.match(paramUri);
    switch (i)
    {
    default:
      Log.d("DownloadManager", "updating unknown/invalid URI: " + paramUri);
      throw new UnsupportedOperationException("Cannot update URI: " + paramUri);
    case 1:
    case 2:
    }
    ArrayList localArrayList = Lists.newArrayList();
    String str2;
    String[] arrayOfString;
    if (paramString != null)
      if (i == 1)
      {
        str2 = "( " + paramString + " )";
        if (i == 2)
        {
          long l = Long.parseLong((String)paramUri.getPathSegments().get(1));
          str2 = str2 + " ( _id = ? ) ";
          localArrayList.add(Long.toString(l));
        }
        if (paramContentValues.size() <= 0)
          break label380;
        if (paramArrayOfString != null)
          localArrayList.addAll(Arrays.asList(paramArrayOfString));
        arrayOfString = (String[])localArrayList.toArray(new String[localArrayList.size()]);
      }
    label380: for (int j = localSQLiteDatabase.update("downloads", paramContentValues, str2, arrayOfString); ; j = 0)
    {
      getContext().getContentResolver().notifyChange(paramUri, null);
      return j;
      str2 = "( " + paramString + " ) AND ";
      break;
      str2 = "";
      break;
    }
  }

  private final class DatabaseHelper extends SQLiteOpenHelper
  {
    public DatabaseHelper(Context arg2)
    {
      super("downloads.db", null, 100);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      if (Constants.LOGVV)
        Log.v("DownloadManager", "populating new database");
      DownloadProvider.this.createTable(paramSQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      if (paramInt1 == 31)
      {
        if (paramInt2 == 100)
          return;
        paramInt1 = 100;
      }
      Log.i("DownloadManager", "Upgrading downloads database from version " + paramInt1 + " to " + paramInt2 + ", which will destroy all old data");
      DownloadProvider.this.dropTable(paramSQLiteDatabase);
      DownloadProvider.this.createTable(paramSQLiteDatabase);
    }
  }

  private class ReadOnlyCursorWrapper extends CursorWrapper
    implements CrossProcessCursor
  {
    private CrossProcessCursor mCursor;

    public ReadOnlyCursorWrapper(Cursor arg2)
    {
      super();
      this.mCursor = ((CrossProcessCursor)localCursor);
    }

    public boolean commitUpdates()
    {
      throw new SecurityException("Download manager cursors are read-only");
    }

    public boolean deleteRow()
    {
      throw new SecurityException("Download manager cursors are read-only");
    }

    public void fillWindow(int paramInt, CursorWindow paramCursorWindow)
    {
      this.mCursor.fillWindow(paramInt, paramCursorWindow);
    }

    public CursorWindow getWindow()
    {
      return this.mCursor.getWindow();
    }

    public boolean onMove(int paramInt1, int paramInt2)
    {
      return this.mCursor.onMove(paramInt1, paramInt2);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.downloadprovider.DownloadProvider
 * JD-Core Version:    0.6.2
 */