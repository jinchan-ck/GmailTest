package com.google.android.gm;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Persistence
{
  private static final String ACTION_STRIP_ACTION_REPLY_ALL = "action-strip-action-reply-all";
  private static final String ALLOW_BATCH = "allow-batch";
  private static final String AUTO_ADVANCE = "auto-advance";
  private static final String AUTO_ADVANCE_MODE_NEWER = "newer";
  private static final String AUTO_ADVANCE_MODE_OLDER = "older";
  public static final String CACHED_GOOGLE_ACCOUNT_LIST = "cache-google-accounts";
  public static final String CACHED_GOOGLE_ACCOUNT_SYNCED_LIST = "cache-google-accounts-synced";
  private static final String COLUMN_FIRST_TIME = "first_time";
  private static final String COLUMN_USER_NAME = "user_name";
  private static final String CONFIRM_ACTIONS = "confirm-actions";
  private static final String CONFIRM_ARCHIVE_TOKEN = "archive";
  private static final String CONFIRM_DELETE = "confirm-delete";
  private static final String CONFIRM_DELETE_TOKEN = "delete";
  private static final String CONFIRM_SEND_TOKEN = "send";
  private static final String DATABASE_FILE = "gmail.db";
  private static final int DATABASE_VERSION = 18;
  private static final int DATABASE_VERSION_TO_UPGRADE = 17;
  private static final String ENABLE_NOTIFICATIONS = "enable-notifications";
  private static final String FAST_SWITCHING = "fast-switching";
  public static final String LAST_VERSION_PROMOTED = "last-version-promoed";
  private static final String MESSAGE_TEXT_SIZE_KEY = "message-text";
  private static final String PRE_ECLAIR_SIGNATURE_KEY = "signature-key";
  private static final String PRE_FROYO_VIBRATE_KEY = "vibrate";
  static final String PRIORITY_INBOX_KEY = "priority-inbox";
  private static final String RINGTONE = "ringtone";
  public static final String SHARED_PREFERENCES_NAME = "Gmail";
  private static final String SIGNATURE = "signature";
  private static final String TABLE_ACCOUNTS = "accounts";
  private static final String TABLE_LABELS = "labels";
  private static final String TABLE_PREFERENCES = "preferences";
  private static final String UNOBTRUSIVE = "unobtrusive";
  private static final String VIBRATE_WHEN = "vibrateWhen";
  private static final String WORK_IN_PROGRESS = "work-in-progress";
  private static String mActiveAccount = null;
  private static SQLiteDatabase mDatabase;
  private static final Lock mDbInitializationLock = new ReentrantLock();
  private static final Condition mInitialized = mDbInitializationLock.newCondition();
  private static Persistence mInstance = null;
  private static SharedPreferences sSharedPrefs;
  private static Set<String> sSupressWhatsNewSet = null;
  private static final String sUR1VersionCode = "148";

  static
  {
    mDatabase = null;
  }

  private static void bootstrapDatabase(Context paramContext, SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    if (paramSQLiteDatabase != null)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS preferences (user_name STRING,first_time INTEGER DEFAULT 1);");
      if (paramString != null)
        paramSQLiteDatabase.insert("preferences", "user_name", new ContentValues(1));
    }
  }

  private static void exec(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    ppp("SQL:" + paramString);
    paramSQLiteDatabase.execSQL(paramString);
  }

  private boolean getBoolean(Context paramContext, String paramString1, String paramString2, boolean paramBoolean)
  {
    return getPreferences(paramContext).getBoolean(makeKey(paramString1, paramString2), paramBoolean);
  }

  private boolean getBoolean(Context paramContext, String paramString, boolean paramBoolean)
  {
    return getBoolean(paramContext, paramString, paramBoolean, true);
  }

  private boolean getBoolean(Context paramContext, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean2)
      return getBoolean(paramContext, getActiveAccount(paramContext), paramString, paramBoolean1);
    return getPreferences(paramContext).getBoolean(paramString, paramBoolean1);
  }

  private static SQLiteDatabase getDatabase()
  {
    mDbInitializationLock.lock();
    try
    {
      while (mDatabase == null)
        mInitialized.await();
    }
    catch (InterruptedException localInterruptedException)
    {
      Log.i("Gmail", "getDatabase interrupted" + localInterruptedException);
      return null;
      return mDatabase;
    }
    finally
    {
      mDbInitializationLock.unlock();
    }
  }

  public static Persistence getInstance(Context paramContext)
  {
    if (mInstance == null)
    {
      mInstance = new Persistence();
      initDatabaseInBackground(paramContext);
    }
    return mInstance;
  }

  public static SharedPreferences getPreferences(Context paramContext)
  {
    if (sSharedPrefs == null)
      sSharedPrefs = paramContext.getSharedPreferences("Gmail", 0);
    return sSharedPrefs;
  }

  private String getString(Context paramContext, String paramString1, String paramString2)
  {
    return getString(paramContext, paramString1, paramString2, true);
  }

  private String getString(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    return getPreferences(paramContext).getString(makeKey(paramString1, paramString2), paramString3);
  }

  private String getString(Context paramContext, String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramBoolean)
      return getString(paramContext, getActiveAccount(paramContext), paramString1, paramString2);
    return getPreferences(paramContext).getString(paramString1, paramString2);
  }

  // ERROR //
  private static void initDatabase(Context paramContext)
  {
    // Byte code:
    //   0: getstatic 135	com/google/android/gm/Persistence:mDbInitializationLock	Ljava/util/concurrent/locks/Lock;
    //   3: invokeinterface 220 1 0
    //   8: aload_0
    //   9: ldc 50
    //   11: iconst_0
    //   12: aconst_null
    //   13: invokevirtual 267	android/content/Context:openOrCreateDatabase	(Ljava/lang/String;ILandroid/database/sqlite/SQLiteDatabase$CursorFactory;)Landroid/database/sqlite/SQLiteDatabase;
    //   16: astore_2
    //   17: aload_2
    //   18: ldc_w 269
    //   21: aconst_null
    //   22: invokevirtual 273	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   25: astore 4
    //   27: aload 4
    //   29: ifnull +10 -> 39
    //   32: aload 4
    //   34: invokeinterface 278 1 0
    //   39: aload_2
    //   40: invokevirtual 282	android/database/sqlite/SQLiteDatabase:getVersion	()I
    //   43: bipush 18
    //   45: if_icmpeq +102 -> 147
    //   48: aload_2
    //   49: invokevirtual 285	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   52: aload_2
    //   53: invokevirtual 282	android/database/sqlite/SQLiteDatabase:getVersion	()I
    //   56: istore 6
    //   58: aconst_null
    //   59: astore 7
    //   61: iload 6
    //   63: bipush 17
    //   65: if_icmpne +63 -> 128
    //   68: aload_2
    //   69: ldc 88
    //   71: iconst_1
    //   72: anewarray 287	java/lang/String
    //   75: dup
    //   76: iconst_0
    //   77: ldc 32
    //   79: aastore
    //   80: aconst_null
    //   81: aconst_null
    //   82: aconst_null
    //   83: aconst_null
    //   84: aconst_null
    //   85: invokevirtual 291	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   88: astore 8
    //   90: aload 8
    //   92: invokeinterface 295 1 0
    //   97: istore 10
    //   99: aconst_null
    //   100: astore 7
    //   102: iload 10
    //   104: ifeq +17 -> 121
    //   107: aload 8
    //   109: iconst_0
    //   110: invokeinterface 298 2 0
    //   115: astore 11
    //   117: aload 11
    //   119: astore 7
    //   121: aload 8
    //   123: invokeinterface 278 1 0
    //   128: aload_2
    //   129: invokestatic 302	com/google/android/gm/Persistence:upgradeDatabase	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   132: aload_0
    //   133: aload_2
    //   134: aload 7
    //   136: invokestatic 304	com/google/android/gm/Persistence:bootstrapDatabase	(Landroid/content/Context;Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;)V
    //   139: aload_2
    //   140: invokevirtual 307	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   143: aload_2
    //   144: invokevirtual 310	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   147: aload_2
    //   148: putstatic 126	com/google/android/gm/Persistence:mDatabase	Landroid/database/sqlite/SQLiteDatabase;
    //   151: getstatic 143	com/google/android/gm/Persistence:mInitialized	Ljava/util/concurrent/locks/Condition;
    //   154: invokeinterface 313 1 0
    //   159: getstatic 135	com/google/android/gm/Persistence:mDbInitializationLock	Ljava/util/concurrent/locks/Lock;
    //   162: invokeinterface 239 1 0
    //   167: return
    //   168: astore_3
    //   169: iconst_0
    //   170: ifeq +9 -> 179
    //   173: aconst_null
    //   174: invokeinterface 278 1 0
    //   179: aload_3
    //   180: athrow
    //   181: astore_1
    //   182: getstatic 135	com/google/android/gm/Persistence:mDbInitializationLock	Ljava/util/concurrent/locks/Lock;
    //   185: invokeinterface 239 1 0
    //   190: aload_1
    //   191: athrow
    //   192: astore 9
    //   194: aload 8
    //   196: invokeinterface 278 1 0
    //   201: aload 9
    //   203: athrow
    //   204: astore 5
    //   206: aload_2
    //   207: invokevirtual 310	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   210: aload 5
    //   212: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   17	27	168	finally
    //   8	17	181	finally
    //   32	39	181	finally
    //   39	52	181	finally
    //   143	147	181	finally
    //   147	159	181	finally
    //   173	179	181	finally
    //   179	181	181	finally
    //   206	213	181	finally
    //   90	99	192	finally
    //   107	117	192	finally
    //   52	58	204	finally
    //   68	90	204	finally
    //   121	128	204	finally
    //   128	143	204	finally
    //   194	204	204	finally
  }

  private static void initDatabaseInBackground(Context paramContext)
  {
    if (Looper.getMainLooper() == Looper.myLooper())
    {
      new AsyncTask()
      {
        protected Void doInBackground(Void[] paramAnonymousArrayOfVoid)
        {
          Persistence.initDatabase(this.val$context);
          return null;
        }
      }
      .execute(new Void[0]);
      return;
    }
    initDatabase(paramContext);
  }

  private boolean isPresent(Context paramContext, String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
      paramString = makeKey(getActiveAccount(paramContext), paramString);
    return getPreferences(paramContext).contains(paramString);
  }

  private String makeKey(String paramString1, String paramString2)
  {
    if (paramString1 != null)
      return paramString1 + "-" + paramString2;
    return paramString2;
  }

  private String migrateVibrateValue(Context paramContext)
  {
    boolean bool2;
    if (isPresent(paramContext, "vibrate", true))
      bool2 = getBoolean(paramContext, "vibrate", false, true);
    while (bool2)
    {
      return paramContext.getResources().getString(2131296405);
      boolean bool1 = isPresent(paramContext, "vibrate", false);
      bool2 = false;
      if (bool1)
        bool2 = getBoolean(paramContext, "vibrate", false, false);
    }
    return paramContext.getResources().getString(2131296406);
  }

  private String migrateVibrateValue(Context paramContext, String paramString)
  {
    if (getBoolean(paramContext, paramString, "vibrate", false))
      return paramContext.getResources().getString(2131296405);
    return paramContext.getResources().getString(2131296406);
  }

  private static void ppp(String paramString)
  {
  }

  private void setBoolean(Context paramContext, String paramString, Boolean paramBoolean)
  {
    setBoolean(paramContext, getActiveAccount(paramContext), paramString, paramBoolean);
  }

  private void setBoolean(Context paramContext, String paramString1, String paramString2, Boolean paramBoolean)
  {
    SharedPreferences.Editor localEditor = getPreferences(paramContext).edit();
    localEditor.putBoolean(makeKey(paramString1, paramString2), paramBoolean.booleanValue());
    SharedPreferencesCompat.apply(localEditor);
  }

  private static void setColumn(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, int paramInt)
  {
    ContentValues localContentValues = new ContentValues(1);
    localContentValues.put(paramString2, Integer.valueOf(paramInt));
    Cursor localCursor = paramSQLiteDatabase.query(paramString1, new String[] { paramString2 }, null, null, null, null, null);
    if (localCursor.moveToFirst())
      paramSQLiteDatabase.update(paramString1, localContentValues, null, null);
    while (true)
    {
      localCursor.close();
      return;
      paramSQLiteDatabase.insert(paramString1, paramString2, localContentValues);
    }
  }

  private static void setColumn(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, String paramString3)
  {
    ContentValues localContentValues = new ContentValues(1);
    localContentValues.put(paramString2, paramString3);
    Cursor localCursor = paramSQLiteDatabase.query(paramString1, new String[] { paramString2 }, null, null, null, null, null);
    boolean bool = localCursor.moveToFirst();
    String str = null;
    int i = 0;
    if (bool)
    {
      i = 1;
      str = localCursor.getString(0);
    }
    localCursor.close();
    if (i != 0)
    {
      if (((str == null) && (paramString3 != null)) || ((str != null) && (!str.equals(paramString3))))
        paramSQLiteDatabase.update(paramString1, localContentValues, null, null);
      return;
    }
    paramSQLiteDatabase.insert(paramString1, paramString2, localContentValues);
  }

  private void setString(Context paramContext, String paramString1, String paramString2)
  {
    setString(paramContext, getActiveAccount(paramContext), paramString1, paramString2);
  }

  private void setString(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    SharedPreferences.Editor localEditor = getPreferences(paramContext).edit();
    localEditor.putString(makeKey(paramString1, paramString2), paramString3);
    SharedPreferencesCompat.apply(localEditor);
  }

  private static void upgradeDatabase(SQLiteDatabase paramSQLiteDatabase)
  {
    Log.w("Gmail", "Upgrading database from version " + paramSQLiteDatabase.getVersion() + " to " + 18 + ", which will destroy all old data");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS accounts");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS labels");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS preferences");
    paramSQLiteDatabase.setVersion(18);
  }

  public void cacheConfiguredGoogleAccounts(Context paramContext, boolean paramBoolean, List<String> paramList)
  {
    if (paramBoolean);
    for (String str = "cache-google-accounts"; ; str = "cache-google-accounts-synced")
    {
      setString(paramContext, null, str, TextUtils.join(" ", paramList));
      return;
    }
  }

  public boolean getActionStripActionReplyAll(Context paramContext)
  {
    return getBoolean(paramContext, "action-strip-action-reply-all", false);
  }

  public String getActiveAccount(Context paramContext)
  {
    Cursor localCursor;
    if (mActiveAccount == null)
    {
      SQLiteDatabase localSQLiteDatabase = getDatabase();
      if (localSQLiteDatabase != null)
        localCursor = localSQLiteDatabase.query("preferences", new String[] { "user_name" }, null, null, null, null, null);
    }
    try
    {
      if (localCursor.moveToNext())
        mActiveAccount = localCursor.getString(0);
      return mActiveAccount;
    }
    finally
    {
      localCursor.close();
    }
  }

  public boolean getAllowBatch(Context paramContext)
  {
    return getBoolean(paramContext, "allow-batch", true);
  }

  public String getAutoAdvanceMode(Context paramContext)
  {
    return getString(paramContext, "auto-advance", paramContext.getResources().getString(2131296400));
  }

  public boolean getAutoAdvanceModeNewer(Context paramContext)
  {
    return "newer".equals(getAutoAdvanceMode(paramContext));
  }

  public boolean getAutoAdvanceModeOlder(Context paramContext)
  {
    return "older".equals(getAutoAdvanceMode(paramContext));
  }

  public List<String> getCachedConfiguredGoogleAccounts(Context paramContext, boolean paramBoolean)
  {
    if (paramBoolean);
    for (String str = "cache-google-accounts"; ; str = "cache-google-accounts-synced")
      return ImmutableList.of(TextUtils.split(getString(paramContext, str, "", false), " "));
  }

  public String getConfirmActions(Context paramContext)
  {
    String str = getString(paramContext, "confirm-actions", null);
    boolean bool2;
    if (str == null)
    {
      if (!isPresent(paramContext, "confirm-delete", true))
        break label45;
      bool2 = getBoolean(paramContext, "confirm-delete", false, true);
    }
    while (bool2)
    {
      str = "delete";
      return str;
      label45: boolean bool1 = isPresent(paramContext, "confirm-delete", false);
      bool2 = false;
      if (bool1)
        bool2 = getBoolean(paramContext, "confirm-delete", false, false);
    }
    return paramContext.getResources().getString(2131296398);
  }

  public boolean getConfirmArchive(Context paramContext)
  {
    return getConfirmActions(paramContext).contains("archive");
  }

  public boolean getConfirmDelete(Context paramContext)
  {
    String str1 = getActiveAccount(paramContext);
    String str2 = makeKey(str1, "confirm-delete");
    String str3 = makeKey(str1, "confirm-actions");
    SharedPreferences localSharedPreferences = getPreferences(paramContext);
    if (localSharedPreferences.contains(str3))
      return getConfirmActions(paramContext).contains("delete");
    if (localSharedPreferences.contains("confirm-delete"))
      return getBoolean(paramContext, "confirm-delete", false, false);
    if (localSharedPreferences.contains(str2))
      return getBoolean(paramContext, "confirm-delete", false, true);
    return getConfirmActions(paramContext).contains("delete");
  }

  public boolean getConfirmSend(Context paramContext)
  {
    return getConfirmActions(paramContext).contains("send");
  }

  public boolean getEnableNotifications(Context paramContext)
  {
    return getBoolean(paramContext, "enable-notifications", true);
  }

  public boolean getEnableNotifications(Context paramContext, String paramString)
  {
    return getBoolean(paramContext, paramString, "enable-notifications", true);
  }

  public boolean getFastSwitching(Context paramContext)
  {
    return getBoolean(paramContext, null, "fast-switching", false);
  }

  public String getMessageTextSize(Context paramContext)
  {
    return getString(paramContext, "message-text", paramContext.getResources().getString(2131296402));
  }

  public boolean getPriorityInboxDefault(Context paramContext)
  {
    return getPriorityInboxDefault(paramContext, getActiveAccount(paramContext));
  }

  public boolean getPriorityInboxDefault(Context paramContext, String paramString)
  {
    return (Utils.getPriorityInboxServerEnabled(paramString)) && (getBoolean(paramContext, paramString, "priority-inbox", false));
  }

  public String getRingtone(Context paramContext)
  {
    return getRingtone(paramContext, getActiveAccount(paramContext));
  }

  public String getRingtone(Context paramContext, String paramString)
  {
    String str = getString(paramContext, paramString, "ringtone", null);
    if (str == null)
      str = getString(paramContext, "ringtone", null, false);
    if (str == null)
      str = Settings.System.DEFAULT_NOTIFICATION_URI.toString();
    return str;
  }

  public boolean getShouldShowWhatsNew(Context paramContext, String paramString)
  {
    String str = getString(paramContext, "last-version-promoed", "", false);
    if (!paramString.equals(str))
    {
      if (sSupressWhatsNewSet == null)
      {
        String[] arrayOfString = paramContext.getResources().getStringArray(2131492873);
        sSupressWhatsNewSet = Sets.newHashSet();
        sSupressWhatsNewSet.addAll(Lists.newArrayList(arrayOfString));
      }
      if (!sSupressWhatsNewSet.contains(str))
        return true;
    }
    return false;
  }

  public String getSignature(Context paramContext)
  {
    String str = getString(paramContext, "signature", null, true);
    if (str == null)
      str = getString(paramContext, "signature-key", null, false);
    if (str == null)
      str = "";
    return str;
  }

  public boolean getUnobtrusive(Context paramContext)
  {
    return getBoolean(paramContext, "unobtrusive", true);
  }

  public boolean getUnobtrusive(Context paramContext, String paramString)
  {
    return getBoolean(paramContext, paramString, "unobtrusive", true);
  }

  public String getVibrateWhen(Context paramContext)
  {
    return getString(paramContext, "vibrateWhen", migrateVibrateValue(paramContext));
  }

  public String getVibrateWhen(Context paramContext, String paramString)
  {
    return getString(paramContext, paramString, "vibrateWhen", migrateVibrateValue(paramContext, paramString));
  }

  public boolean getWorkInProgress(Context paramContext)
  {
    return getBoolean(paramContext, null, "work-in-progress", false);
  }

  public void setActionStripActionReplyAll(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, "action-strip-action-reply-all", Boolean.valueOf(paramBoolean));
  }

  public void setActiveAccount(Context paramContext, String paramString)
  {
    if ((mActiveAccount != null) && (mActiveAccount.equals(paramString)));
    while (true)
    {
      return;
      int i = 1;
      SQLiteDatabase localSQLiteDatabase = getDatabase();
      if (localSQLiteDatabase == null)
        continue;
      Cursor localCursor = localSQLiteDatabase.query("preferences", new String[] { "user_name" }, null, null, null, null, null);
      try
      {
        if (localCursor.moveToNext())
        {
          String str = localCursor.getString(0);
          if (str != null)
          {
            boolean bool = str.equals(paramString);
            if (bool)
              i = 0;
          }
        }
        localCursor.close();
        if (i == 0)
          continue;
        setColumn(localSQLiteDatabase, "preferences", "user_name", paramString);
        mActiveAccount = paramString;
        return;
      }
      finally
      {
        localCursor.close();
      }
    }
  }

  public void setAllowBatch(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, "allow-batch", Boolean.valueOf(paramBoolean));
  }

  public void setAutoAdvanceMode(Context paramContext, String paramString)
  {
    setString(paramContext, "auto-advance", paramString);
  }

  public void setConfirmActions(Context paramContext, String paramString)
  {
    setString(paramContext, "confirm-actions", paramString);
  }

  public void setEnableNotifications(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, "enable-notifications", Boolean.valueOf(paramBoolean));
  }

  public void setFastSwitching(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, null, "fast-switching", Boolean.valueOf(paramBoolean));
  }

  public void setFirstTime(boolean paramBoolean)
  {
    SQLiteDatabase localSQLiteDatabase = getDatabase();
    if (localSQLiteDatabase != null)
      if (!paramBoolean)
        break label24;
    label24: for (int i = 1; ; i = 0)
    {
      setColumn(localSQLiteDatabase, "preferences", "first_time", i);
      return;
    }
  }

  public void setHasShownWhatsNew(Context paramContext, String paramString)
  {
    setString(paramContext, null, "last-version-promoed", paramString);
  }

  public void setMessageTextSize(Context paramContext, String paramString)
  {
    setString(paramContext, "message-text", paramString);
  }

  public void setPriorityInboxDefault(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, "priority-inbox", Boolean.valueOf(paramBoolean));
  }

  public void setRingtone(Context paramContext, String paramString)
  {
    setString(paramContext, "ringtone", paramString);
  }

  public void setSignature(Context paramContext, String paramString)
  {
    setString(paramContext, "signature", paramString);
  }

  public void setUnobtrusive(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, "unobtrusive", Boolean.valueOf(paramBoolean));
  }

  public void setVibrateWhen(Context paramContext, String paramString)
  {
    setString(paramContext, "vibrateWhen", paramString);
  }

  public void setWorkInProgress(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, null, "work-in-progress", Boolean.valueOf(paramBoolean));
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.Persistence
 * JD-Core Version:    0.6.2
 */