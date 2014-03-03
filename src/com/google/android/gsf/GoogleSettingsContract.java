package com.google.android.gsf;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public final class GoogleSettingsContract
{
  public static final String AUTHORITY = "com.google.settings";
  private static final String TAG = "GoogleSettings";

  public static class NameValueTable
    implements BaseColumns
  {
    public static final String NAME = "name";
    public static final String VALUE = "value";

    public static Uri getUriFor(Uri paramUri, String paramString)
    {
      return Uri.withAppendedPath(paramUri, paramString);
    }

    protected static boolean putString(ContentResolver paramContentResolver, Uri paramUri, String paramString1, String paramString2)
    {
      try
      {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("name", paramString1);
        localContentValues.put("value", paramString2);
        paramContentResolver.insert(paramUri, localContentValues);
        return true;
      }
      catch (SQLException localSQLException)
      {
        Log.e("GoogleSettings", "Can't set key " + paramString1 + " in " + paramUri, localSQLException);
        return false;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.e("GoogleSettings", "Can't set key " + paramString1 + " in " + paramUri, localIllegalArgumentException);
      }
      return false;
    }
  }

  public static final class Partner extends GoogleSettingsContract.NameValueTable
  {
    public static final String CLIENT_ID = "client_id";
    public static final Uri CONTENT_URI = Uri.parse("content://com.google.settings/partner");
    public static final String DATA_STORE_VERSION = "data_store_version";
    public static final String LOGGING_ID2 = "logging_id2";
    public static final String MAPS_CLIENT_ID = "maps_client_id";
    public static final String MARKET_CHECKIN = "market_checkin";
    public static final String MARKET_CLIENT_ID = "market_client_id";
    public static final String NETWORK_LOCATION_OPT_IN = "network_location_opt_in";
    public static final String RLZ = "rlz";
    public static final String USE_LOCATION_FOR_SERVICES = "use_location_for_services";
    public static final String VOICESEARCH_CLIENT_ID = "voicesearch_client_id";
    public static final String YOUTUBE_CLIENT_ID = "youtube_client_id";

    public static int getInt(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      String str = getString(paramContentResolver, paramString);
      if (str != null);
      try
      {
        int i = Integer.parseInt(str);
        return i;
        return paramInt;
      }
      catch (NumberFormatException localNumberFormatException)
      {
      }
      return paramInt;
    }

    public static long getLong(ContentResolver paramContentResolver, String paramString, long paramLong)
    {
      String str = getString(paramContentResolver, paramString);
      if (str != null);
      try
      {
        long l = Long.parseLong(str);
        return l;
        return paramLong;
      }
      catch (NumberFormatException localNumberFormatException)
      {
      }
      return paramLong;
    }

    public static String getString(ContentResolver paramContentResolver, String paramString)
    {
      Cursor localCursor = null;
      try
      {
        localCursor = paramContentResolver.query(CONTENT_URI, new String[] { "value" }, "name=?", new String[] { paramString }, null);
        localObject2 = null;
        if (localCursor != null)
        {
          boolean bool = localCursor.moveToNext();
          localObject2 = null;
          if (bool)
          {
            String str = localCursor.getString(0);
            localObject2 = str;
          }
        }
        return localObject2;
      }
      catch (SQLException localSQLException)
      {
        Log.e("GoogleSettings", "Can't get key " + paramString + " from " + CONTENT_URI, localSQLException);
        Object localObject2 = null;
        return null;
      }
      finally
      {
        if (localCursor != null)
          localCursor.close();
      }
    }

    public static String getString(ContentResolver paramContentResolver, String paramString1, String paramString2)
    {
      String str = getString(paramContentResolver, paramString1);
      if (str == null)
        str = paramString2;
      return str;
    }

    public static Uri getUriFor(String paramString)
    {
      return getUriFor(CONTENT_URI, paramString);
    }

    public static boolean putInt(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      return putString(paramContentResolver, paramString, String.valueOf(paramInt));
    }

    public static boolean putString(ContentResolver paramContentResolver, String paramString1, String paramString2)
    {
      return putString(paramContentResolver, CONTENT_URI, paramString1, paramString2);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gsf.GoogleSettingsContract
 * JD-Core Version:    0.6.2
 */