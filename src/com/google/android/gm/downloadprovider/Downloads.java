package com.google.android.gm.downloadprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.BaseColumns;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public final class Downloads
{
  public static final String ACTION_DOWNLOAD_COMPLETED = "android.intent.action.DOWNLOAD_COMPLETED";
  public static final String ACTION_NOTIFICATION_CLICKED = "android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";
  public static final String COLUMN_APP_DATA = "entity";
  public static final String COLUMN_CONTROL = "control";
  public static final String COLUMN_COOKIE_DATA = "cookiedata";
  public static final String COLUMN_CURRENT_BYTES = "current_bytes";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_DESTINATION = "destination";
  public static final String COLUMN_FILE_NAME_HINT = "hint";
  public static final String COLUMN_LAST_MODIFICATION = "lastmod";
  public static final String COLUMN_MIME_TYPE = "mimetype";
  public static final String COLUMN_NOTIFICATION_CLASS = "notificationclass";
  public static final String COLUMN_NOTIFICATION_EXTRAS = "notificationextras";
  public static final String COLUMN_NOTIFICATION_PACKAGE = "notificationpackage";
  public static final String COLUMN_NO_INTEGRITY = "no_integrity";
  public static final String COLUMN_OTHER_UID = "otheruid";
  public static final String COLUMN_REFERER = "referer";
  public static final String COLUMN_STATUS = "status";
  public static final String COLUMN_TITLE = "title";
  public static final String COLUMN_TOTAL_BYTES = "total_bytes";
  public static final String COLUMN_URI = "uri";
  public static final String COLUMN_USER_AGENT = "useragent";
  public static final String COLUMN_VISIBILITY = "visibility";
  public static final String CONTENT_AUTHORITY = "gmail-downloads";
  public static final Uri CONTENT_URI = Uri.parse("content://gmail-downloads/download");
  public static final int CONTROL_PAUSED = 1;
  public static final int CONTROL_RUN = 0;
  public static final int DESTINATION_CACHE_PARTITION = 1;
  public static final int DESTINATION_CACHE_PARTITION_NOROAMING = 3;
  public static final int DESTINATION_CACHE_PARTITION_PURGEABLE = 2;
  public static final int DESTINATION_EXTERNAL = 0;
  private static final int DOWNLOADS_COLUMN_CURRENT_BYTES = 5;
  private static final int DOWNLOADS_COLUMN_FILENAME = 3;
  private static final int DOWNLOADS_COLUMN_ID = 0;
  private static final int DOWNLOADS_COLUMN_LAST_MODIFICATION = 4;
  private static final int DOWNLOADS_COLUMN_STATUS = 2;
  private static final int DOWNLOADS_COLUMN_URI = 1;
  private static final String[] DOWNLOADS_PROJECTION = { "_id", "entity", "status", "_data", "lastmod", "current_bytes" };
  public static final int DOWNLOAD_DESTINATION_CACHE = 2;
  public static final int DOWNLOAD_DESTINATION_CACHE_PURGEABLE = 3;
  public static final int DOWNLOAD_DESTINATION_EXTERNAL = 1;
  public static final long DOWNLOAD_ID_INVALID = -1L;
  public static final String PERMISSION_ACCESS = "android.permission.ACCESS_DOWNLOAD_MANAGER";
  public static final String PERMISSION_ACCESS_ADVANCED = "android.permission.ACCESS_DOWNLOAD_MANAGER_ADVANCED";
  public static final String PERMISSION_CACHE = "android.permission.ACCESS_CACHE_FILESYSTEM";
  public static final String PERMISSION_SEND_INTENTS = "android.permission.SEND_DOWNLOAD_COMPLETED_INTENTS";
  public static final int STATUS_BAD_REQUEST = 400;
  public static final int STATUS_CANCELED = 490;
  public static final int STATUS_DEVICE_NOT_FOUND_ERROR = 499;
  private static final int STATUS_DOWNLOADED_UPDATE = 4;
  private static final int STATUS_DOWNLOADING_UPDATE = 3;
  public static final int STATUS_FILE_ERROR = 492;
  public static final int STATUS_HTTP_DATA_ERROR = 495;
  public static final int STATUS_HTTP_EXCEPTION = 496;
  public static final int STATUS_INSUFFICIENT_SPACE_ERROR = 498;
  private static final int STATUS_INVALID = 0;
  public static final int STATUS_LENGTH_REQUIRED = 411;
  public static final int STATUS_NOT_ACCEPTABLE = 406;
  public static final int STATUS_PENDING = 190;
  public static final int STATUS_PENDING_PAUSED = 191;
  public static final int STATUS_PRECONDITION_FAILED = 412;
  public static final int STATUS_RUNNING = 192;
  public static final int STATUS_RUNNING_PAUSED = 193;
  public static final int STATUS_SUCCESS = 200;
  public static final int STATUS_TOO_MANY_REDIRECTS = 497;
  public static final int STATUS_UNHANDLED_HTTP_CODE = 494;
  public static final int STATUS_UNHANDLED_REDIRECT = 493;
  public static final int STATUS_UNKNOWN_ERROR = 491;
  private static final String TAG = "GmailDownload";
  public static final int VISIBILITY_HIDDEN = 2;
  public static final int VISIBILITY_VISIBLE = 0;
  public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
  public static final String _DATA = "_data";

  private static final int getStatusOfDownload(Cursor paramCursor, long paramLong)
  {
    int i = paramCursor.getInt(2);
    SystemClock.elapsedRealtime();
    if (!Impl.isStatusCompleted(i))
    {
      long l1 = paramCursor.getLong(4);
      long l2 = System.currentTimeMillis();
      if ((l2 < l1) || (l2 - l1 > paramLong))
        return 0;
      return 3;
    }
    if (Impl.isStatusError(i))
      return 0;
    if (paramCursor.getString(3) == null)
      return 0;
    return 4;
  }

  public static boolean isStatusClientError(int paramInt)
  {
    return (paramInt >= 400) && (paramInt < 500);
  }

  public static boolean isStatusCompleted(int paramInt)
  {
    return ((paramInt >= 200) && (paramInt < 300)) || ((paramInt >= 400) && (paramInt < 600));
  }

  public static boolean isStatusError(int paramInt)
  {
    return (paramInt >= 400) && (paramInt < 600);
  }

  public static boolean isStatusInformational(int paramInt)
  {
    return (paramInt >= 100) && (paramInt < 200);
  }

  public static boolean isStatusServerError(int paramInt)
  {
    return (paramInt >= 500) && (paramInt < 600);
  }

  public static boolean isStatusSuccess(int paramInt)
  {
    return (paramInt >= 200) && (paramInt < 300);
  }

  public static boolean isStatusSuspended(int paramInt)
  {
    return (paramInt == 191) || (paramInt == 193);
  }

  public static final class ById extends Downloads.DownloadBase
  {
    public static void deleteDownload(Context paramContext, long paramLong)
    {
      paramContext.getContentResolver().delete(getDownloadUri(paramLong), null, null);
    }

    public static void deleteDownloads(Context paramContext, ArrayList<Long> paramArrayList)
    {
      ContentResolver localContentResolver = paramContext.getContentResolver();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("_id IN (");
      int i = paramArrayList.size();
      for (int j = 0; j < i; j++)
      {
        if (j > 0)
          localStringBuilder.append(", ");
        Long localLong = (Long)paramArrayList.get(j);
        localStringBuilder.append("'" + localLong.toString() + "'");
      }
      localStringBuilder.append(')');
      localContentResolver.delete(Downloads.Impl.CONTENT_URI, localStringBuilder.toString(), null);
    }

    private static Uri getDownloadUri(long paramLong)
    {
      return Uri.parse(Downloads.Impl.CONTENT_URI + "/" + paramLong);
    }

    public static String getMimeTypeForId(Context paramContext, long paramLong)
    {
      ContentResolver localContentResolver = paramContext.getContentResolver();
      Cursor localCursor = null;
      try
      {
        localCursor = localContentResolver.query(getDownloadUri(paramLong), new String[] { "mimetype" }, null, null, null);
        boolean bool = localCursor.moveToNext();
        Object localObject2 = null;
        if (bool)
        {
          String str = localCursor.getString(0);
          localObject2 = str;
        }
        return localObject2;
      }
      finally
      {
        if (localCursor != null)
          localCursor.close();
      }
    }

    // ERROR //
    public static final Downloads.StatusInfo getStatus(Context paramContext, long paramLong)
    {
      // Byte code:
      //   0: lload_1
      //   1: invokestatic 20	com/google/android/gm/downloadprovider/Downloads$ById:getDownloadUri	(J)Landroid/net/Uri;
      //   4: astore_3
      //   5: aload_0
      //   6: invokevirtual 16	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
      //   9: aload_3
      //   10: invokestatic 112	com/google/android/gm/downloadprovider/Downloads:access$000	()[Ljava/lang/String;
      //   13: aconst_null
      //   14: aconst_null
      //   15: aconst_null
      //   16: invokevirtual 91	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
      //   19: astore 4
      //   21: aload 4
      //   23: ifnull +17 -> 40
      //   26: aload 4
      //   28: invokeinterface 97 1 0
      //   33: istore 7
      //   35: iload 7
      //   37: ifne +17 -> 54
      //   40: aload 4
      //   42: ifnull +10 -> 52
      //   45: aload 4
      //   47: invokeinterface 104 1 0
      //   52: aconst_null
      //   53: areturn
      //   54: iconst_0
      //   55: ifne +208 -> 263
      //   58: new 114	com/google/android/gm/downloadprovider/Downloads$StatusInfo
      //   61: dup
      //   62: invokespecial 115	com/google/android/gm/downloadprovider/Downloads$StatusInfo:<init>	()V
      //   65: astore 17
      //   67: aload 17
      //   69: astore 8
      //   71: aload 4
      //   73: lconst_0
      //   74: invokestatic 119	com/google/android/gm/downloadprovider/Downloads:access$100	(Landroid/database/Cursor;J)I
      //   77: istore 11
      //   79: iload 11
      //   81: iconst_3
      //   82: if_icmpeq +187 -> 269
      //   85: iload 11
      //   87: iconst_4
      //   88: if_icmpne +89 -> 177
      //   91: goto +178 -> 269
      //   94: aload 8
      //   96: iload 12
      //   98: putfield 123	com/google/android/gm/downloadprovider/Downloads$StatusInfo:completed	Z
      //   101: aload 8
      //   103: aload 4
      //   105: iconst_3
      //   106: invokeinterface 101 2 0
      //   111: putfield 127	com/google/android/gm/downloadprovider/Downloads$StatusInfo:filename	Ljava/lang/String;
      //   114: aload 8
      //   116: aload 4
      //   118: iconst_0
      //   119: invokeinterface 131 2 0
      //   124: putfield 135	com/google/android/gm/downloadprovider/Downloads$StatusInfo:id	J
      //   127: aload 8
      //   129: aload 4
      //   131: iconst_2
      //   132: invokeinterface 139 2 0
      //   137: putfield 143	com/google/android/gm/downloadprovider/Downloads$StatusInfo:statusCode	I
      //   140: aload 8
      //   142: aload 4
      //   144: iconst_5
      //   145: invokeinterface 131 2 0
      //   150: putfield 146	com/google/android/gm/downloadprovider/Downloads$StatusInfo:bytesSoFar	J
      //   153: aload 4
      //   155: ifnull +10 -> 165
      //   158: aload 4
      //   160: invokeinterface 104 1 0
      //   165: aload 8
      //   167: pop
      //   168: aload 8
      //   170: areturn
      //   171: iconst_0
      //   172: istore 12
      //   174: goto -80 -> 94
      //   177: aload 4
      //   179: iconst_4
      //   180: invokeinterface 131 2 0
      //   185: pop2
      //   186: aload 8
      //   188: aload 4
      //   190: iconst_2
      //   191: invokeinterface 139 2 0
      //   196: putfield 143	com/google/android/gm/downloadprovider/Downloads$StatusInfo:statusCode	I
      //   199: aload 8
      //   201: aload 4
      //   203: iconst_5
      //   204: invokeinterface 131 2 0
      //   209: putfield 146	com/google/android/gm/downloadprovider/Downloads$StatusInfo:bytesSoFar	J
      //   212: aload 4
      //   214: ifnull +10 -> 224
      //   217: aload 4
      //   219: invokeinterface 104 1 0
      //   224: aload 8
      //   226: pop
      //   227: aload 8
      //   229: areturn
      //   230: astore 5
      //   232: aload 5
      //   234: astore 6
      //   236: aload 4
      //   238: ifnull +10 -> 248
      //   241: aload 4
      //   243: invokeinterface 104 1 0
      //   248: aload 6
      //   250: athrow
      //   251: astore 9
      //   253: aload 9
      //   255: astore 6
      //   257: aload 8
      //   259: pop
      //   260: goto -24 -> 236
      //   263: aconst_null
      //   264: astore 8
      //   266: goto -195 -> 71
      //   269: iload 11
      //   271: iconst_4
      //   272: if_icmpne -101 -> 171
      //   275: iconst_1
      //   276: istore 12
      //   278: goto -184 -> 94
      //
      // Exception table:
      //   from	to	target	type
      //   26	35	230	finally
      //   58	67	230	finally
      //   71	79	251	finally
      //   94	153	251	finally
      //   177	212	251	finally
    }

    public static ParcelFileDescriptor openDownload(Context paramContext, long paramLong, String paramString)
      throws FileNotFoundException
    {
      return paramContext.getContentResolver().openFileDescriptor(getDownloadUri(paramLong), paramString);
    }

    public static InputStream openDownloadStream(Context paramContext, long paramLong)
      throws FileNotFoundException, IOException
    {
      return paramContext.getContentResolver().openInputStream(getDownloadUri(paramLong));
    }
  }

  public static final class ByUri extends Downloads.DownloadBase
  {
    private static final String[] PROJECTION = { "_id", "current_bytes", "total_bytes" };
    private static final String QUERY_WHERE_APP_DATA_CLAUSE = "entity=?";
    private static final String QUERY_WHERE_CLAUSE = "notificationpackage=? AND notificationclass=?";

    private static final Cursor getCurrentOtaDownloads(Context paramContext, String paramString)
    {
      return paramContext.getContentResolver().query(Downloads.Impl.CONTENT_URI, Downloads.DOWNLOADS_PROJECTION, "entity=?", new String[] { paramString }, null);
    }

    public static final int getProgressColumnCurrentBytes()
    {
      return 1;
    }

    public static final int getProgressColumnId()
    {
      return 0;
    }

    public static final int getProgressColumnTotalBytes()
    {
      return 2;
    }

    public static final Cursor getProgressCursor(Context paramContext, long paramLong)
    {
      Uri localUri = Uri.withAppendedPath(Downloads.Impl.CONTENT_URI, String.valueOf(paramLong));
      return paramContext.getContentResolver().query(localUri, PROJECTION, null, null, null);
    }

    // ERROR //
    public static final Downloads.StatusInfo getStatus(Context paramContext, String paramString, long paramLong)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore 4
      //   3: lconst_0
      //   4: lstore 5
      //   6: aload_0
      //   7: aload_1
      //   8: invokestatic 74	com/google/android/gm/downloadprovider/Downloads$ByUri:getCurrentOtaDownloads	(Landroid/content/Context;Ljava/lang/String;)Landroid/database/Cursor;
      //   11: astore 7
      //   13: iconst_0
      //   14: istore 8
      //   16: aload 7
      //   18: ifnull +237 -> 255
      //   21: aload 7
      //   23: invokeinterface 80 1 0
      //   28: ifeq +227 -> 255
      //   31: aload 4
      //   33: ifnonnull +258 -> 291
      //   36: new 82	com/google/android/gm/downloadprovider/Downloads$StatusInfo
      //   39: dup
      //   40: invokespecial 83	com/google/android/gm/downloadprovider/Downloads$StatusInfo:<init>	()V
      //   43: astore 16
      //   45: aload 16
      //   47: astore 17
      //   49: aload 7
      //   51: lload_2
      //   52: invokestatic 87	com/google/android/gm/downloadprovider/Downloads:access$100	(Landroid/database/Cursor;J)I
      //   55: istore 23
      //   57: iload 23
      //   59: iconst_3
      //   60: if_icmpeq +238 -> 298
      //   63: iload 23
      //   65: iconst_4
      //   66: if_icmpne +89 -> 155
      //   69: goto +229 -> 298
      //   72: aload 17
      //   74: iload 24
      //   76: putfield 91	com/google/android/gm/downloadprovider/Downloads$StatusInfo:completed	Z
      //   79: aload 17
      //   81: aload 7
      //   83: iconst_3
      //   84: invokeinterface 95 2 0
      //   89: putfield 98	com/google/android/gm/downloadprovider/Downloads$StatusInfo:filename	Ljava/lang/String;
      //   92: aload 17
      //   94: aload 7
      //   96: iconst_0
      //   97: invokeinterface 102 2 0
      //   102: putfield 106	com/google/android/gm/downloadprovider/Downloads$StatusInfo:id	J
      //   105: aload 17
      //   107: aload 7
      //   109: iconst_2
      //   110: invokeinterface 110 2 0
      //   115: putfield 114	com/google/android/gm/downloadprovider/Downloads$StatusInfo:statusCode	I
      //   118: aload 17
      //   120: aload 7
      //   122: iconst_5
      //   123: invokeinterface 102 2 0
      //   128: putfield 117	com/google/android/gm/downloadprovider/Downloads$StatusInfo:bytesSoFar	J
      //   131: aload 7
      //   133: ifnull +10 -> 143
      //   136: aload 7
      //   138: invokeinterface 120 1 0
      //   143: aload 17
      //   145: pop
      //   146: aload 17
      //   148: areturn
      //   149: iconst_0
      //   150: istore 24
      //   152: goto -80 -> 72
      //   155: aload 7
      //   157: iconst_4
      //   158: invokeinterface 102 2 0
      //   163: lstore 26
      //   165: iload 8
      //   167: ifeq +18 -> 185
      //   170: lload 26
      //   172: lload 5
      //   174: lcmp
      //   175: ifge +10 -> 185
      //   178: aload 17
      //   180: astore 4
      //   182: goto -166 -> 16
      //   185: iconst_1
      //   186: istore 8
      //   188: lload 26
      //   190: lstore 5
      //   192: aload 17
      //   194: aload 7
      //   196: iconst_2
      //   197: invokeinterface 110 2 0
      //   202: putfield 114	com/google/android/gm/downloadprovider/Downloads$StatusInfo:statusCode	I
      //   205: aload 17
      //   207: aload 7
      //   209: iconst_5
      //   210: invokeinterface 102 2 0
      //   215: putfield 117	com/google/android/gm/downloadprovider/Downloads$StatusInfo:bytesSoFar	J
      //   218: aload 17
      //   220: astore 4
      //   222: goto -206 -> 16
      //   225: astore 10
      //   227: iload 8
      //   229: pop
      //   230: lload 5
      //   232: pop2
      //   233: aload 10
      //   235: astore 14
      //   237: aload 4
      //   239: pop
      //   240: aload 7
      //   242: ifnull +10 -> 252
      //   245: aload 7
      //   247: invokeinterface 120 1 0
      //   252: aload 14
      //   254: athrow
      //   255: aload 7
      //   257: ifnull +10 -> 267
      //   260: aload 7
      //   262: invokeinterface 120 1 0
      //   267: aload 4
      //   269: pop
      //   270: aload 4
      //   272: areturn
      //   273: astore 18
      //   275: iload 8
      //   277: pop
      //   278: lload 5
      //   280: pop2
      //   281: aload 18
      //   283: astore 14
      //   285: aload 17
      //   287: pop
      //   288: goto -48 -> 240
      //   291: aload 4
      //   293: astore 17
      //   295: goto -246 -> 49
      //   298: iload 23
      //   300: iconst_4
      //   301: if_icmpne -152 -> 149
      //   304: iconst_1
      //   305: istore 24
      //   307: goto -235 -> 72
      //
      // Exception table:
      //   from	to	target	type
      //   21	31	225	finally
      //   36	45	225	finally
      //   49	57	273	finally
      //   72	131	273	finally
      //   155	165	273	finally
      //   192	218	273	finally
    }

    public static final void removeAllDownloadsByPackage(Context paramContext, String paramString1, String paramString2)
    {
      paramContext.getContentResolver().delete(Downloads.Impl.CONTENT_URI, "notificationpackage=? AND notificationclass=?", new String[] { paramString1, paramString2 });
    }
  }

  public static class DownloadBase
  {
    public static long startDownloadByUri(Context paramContext, String paramString1, String paramString2, boolean paramBoolean1, int paramInt, boolean paramBoolean2, boolean paramBoolean3, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
    {
      ContentResolver localContentResolver = paramContext.getContentResolver();
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("uri", paramString1);
      localContentValues.put("cookiedata", paramString2);
      int i;
      int j;
      if (paramBoolean1)
      {
        i = 0;
        localContentValues.put("visibility", Integer.valueOf(i));
        if (paramString3 != null)
          localContentValues.put("title", paramString3);
        localContentValues.put("entity", paramString1);
        if (paramString4 != null)
          localContentValues.put("hint", paramString4);
        j = 0;
        switch (paramInt)
        {
        default:
        case 1:
        case 2:
        case 3:
        }
      }
      while (true)
      {
        localContentValues.put("destination", Integer.valueOf(j));
        localContentValues.put("no_integrity", Boolean.valueOf(paramBoolean3));
        if ((paramString5 != null) && (paramString6 != null))
        {
          localContentValues.put("notificationpackage", paramString5);
          localContentValues.put("notificationclass", paramString6);
          if (paramString7 != null)
            localContentValues.put("notificationextras", paramString7);
        }
        Uri localUri = localContentResolver.insert(Downloads.Impl.CONTENT_URI, localContentValues);
        long l = -1L;
        if (localUri != null)
          l = Long.parseLong(localUri.getLastPathSegment());
        return l;
        i = 2;
        break;
        j = 0;
        continue;
        if (paramBoolean2)
        {
          j = 1;
        }
        else
        {
          j = 3;
          continue;
          j = 2;
        }
      }
    }
  }

  public static final class Impl
    implements BaseColumns
  {
    public static final String ACTION_DOWNLOAD_COMPLETED = "android.intent.action.DOWNLOAD_COMPLETED";
    public static final String ACTION_NOTIFICATION_CLICKED = "android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";
    public static final String COLUMN_APP_DATA = "entity";
    public static final String COLUMN_CONTROL = "control";
    public static final String COLUMN_COOKIE_DATA = "cookiedata";
    public static final String COLUMN_CURRENT_BYTES = "current_bytes";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_FILE_NAME_HINT = "hint";
    public static final String COLUMN_LAST_MODIFICATION = "lastmod";
    public static final String COLUMN_MIME_TYPE = "mimetype";
    public static final String COLUMN_NOTIFICATION_CLASS = "notificationclass";
    public static final String COLUMN_NOTIFICATION_EXTRAS = "notificationextras";
    public static final String COLUMN_NOTIFICATION_PACKAGE = "notificationpackage";
    public static final String COLUMN_NO_INTEGRITY = "no_integrity";
    public static final String COLUMN_OTHER_UID = "otheruid";
    public static final String COLUMN_REFERER = "referer";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TOTAL_BYTES = "total_bytes";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_USER_AGENT = "useragent";
    public static final String COLUMN_VISIBILITY = "visibility";
    public static final Uri CONTENT_URI = Uri.parse("content://gmail-downloads/download");
    public static final int CONTROL_PAUSED = 1;
    public static final int CONTROL_RUN = 0;
    public static final int DESTINATION_CACHE_PARTITION = 1;
    public static final int DESTINATION_CACHE_PARTITION_NOROAMING = 3;
    public static final int DESTINATION_CACHE_PARTITION_PURGEABLE = 2;
    public static final int DESTINATION_EXTERNAL = 0;
    public static final String PERMISSION_ACCESS = "android.permission.ACCESS_DOWNLOAD_MANAGER";
    public static final String PERMISSION_ACCESS_ADVANCED = "android.permission.ACCESS_DOWNLOAD_MANAGER_ADVANCED";
    public static final String PERMISSION_CACHE = "android.permission.ACCESS_CACHE_FILESYSTEM";
    public static final String PERMISSION_SEE_ALL_EXTERNAL = "android.permission.SEE_ALL_EXTERNAL";
    public static final String PERMISSION_SEND_INTENTS = "android.permission.SEND_DOWNLOAD_COMPLETED_INTENTS";
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_CANCELED = 490;
    public static final int STATUS_DEVICE_NOT_FOUND_ERROR = 499;
    public static final int STATUS_FILE_ERROR = 492;
    public static final int STATUS_HTTP_DATA_ERROR = 495;
    public static final int STATUS_HTTP_EXCEPTION = 496;
    public static final int STATUS_INSUFFICIENT_SPACE_ERROR = 498;
    public static final int STATUS_LENGTH_REQUIRED = 411;
    public static final int STATUS_NOT_ACCEPTABLE = 406;
    public static final int STATUS_PENDING = 190;
    public static final int STATUS_PENDING_PAUSED = 191;
    public static final int STATUS_PRECONDITION_FAILED = 412;
    public static final int STATUS_RUNNING = 192;
    public static final int STATUS_RUNNING_PAUSED = 193;
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_TOO_MANY_REDIRECTS = 497;
    public static final int STATUS_UNHANDLED_HTTP_CODE = 494;
    public static final int STATUS_UNHANDLED_REDIRECT = 493;
    public static final int STATUS_UNKNOWN_ERROR = 491;
    public static final int VISIBILITY_HIDDEN = 2;
    public static final int VISIBILITY_VISIBLE = 0;
    public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
    public static final String _DATA = "_data";

    public static boolean isStatusClientError(int paramInt)
    {
      return (paramInt >= 400) && (paramInt < 500);
    }

    public static boolean isStatusCompleted(int paramInt)
    {
      return ((paramInt >= 200) && (paramInt < 300)) || ((paramInt >= 400) && (paramInt < 600));
    }

    public static boolean isStatusError(int paramInt)
    {
      return (paramInt >= 400) && (paramInt < 600);
    }

    public static boolean isStatusInformational(int paramInt)
    {
      return (paramInt >= 100) && (paramInt < 200);
    }

    public static boolean isStatusServerError(int paramInt)
    {
      return (paramInt >= 500) && (paramInt < 600);
    }

    public static boolean isStatusSuccess(int paramInt)
    {
      return (paramInt >= 200) && (paramInt < 300);
    }

    public static boolean isStatusSuspended(int paramInt)
    {
      return (paramInt == 191) || (paramInt == 193);
    }
  }

  public static final class StatusInfo
  {
    public long bytesSoFar = -1L;
    public boolean completed = false;
    public String filename = null;
    public long id = -1L;
    public int statusCode = -1;

    public boolean isComplete()
    {
      return Downloads.Impl.isStatusCompleted(this.statusCode);
    }

    public boolean isSuccessful()
    {
      return Downloads.Impl.isStatusSuccess(this.statusCode);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.downloadprovider.Downloads
 * JD-Core Version:    0.6.2
 */