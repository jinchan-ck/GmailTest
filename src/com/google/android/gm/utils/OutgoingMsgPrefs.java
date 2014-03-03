package com.google.android.gm.utils;

import android.database.Cursor;
import java.util.concurrent.ConcurrentHashMap;

public class OutgoingMsgPrefs
{
  public static ConcurrentHashMap<String, String[]> ACCOUNT_OUTGOING_PREFS = new ConcurrentHashMap();
  private static String VALUE_COL = "value";

  public static void addOrUpdateDisplayName(String paramString1, String paramString2)
  {
    String[] arrayOfString = ensurePrefs(paramString1);
    arrayOfString[1] = paramString2;
    ACCOUNT_OUTGOING_PREFS.put(paramString1, arrayOfString);
  }

  public static void addOrUpdateReplyTo(String paramString1, String paramString2)
  {
    String[] arrayOfString = ensurePrefs(paramString1);
    arrayOfString[0] = paramString2;
    ACCOUNT_OUTGOING_PREFS.put(paramString1, arrayOfString);
  }

  private static String[] ensurePrefs(String paramString)
  {
    if (ACCOUNT_OUTGOING_PREFS == null);
    for (String[] arrayOfString = null; ; arrayOfString = (String[])ACCOUNT_OUTGOING_PREFS.get(paramString))
    {
      if (arrayOfString == null)
        arrayOfString = new String[2];
      return arrayOfString;
    }
  }

  public static void instantiateOutgoingPrefs(String paramString, Cursor paramCursor)
  {
    String[] arrayOfString = new String[2];
    try
    {
      int i;
      String str;
      int k;
      if (paramCursor.moveToFirst())
      {
        i = paramCursor.getColumnIndexOrThrow(VALUE_COL);
        int j = paramCursor.getColumnIndexOrThrow("name");
        str = paramCursor.getString(j);
        k = -1;
        if (!str.equals("sx_rt"))
          break label103;
        k = 0;
      }
      while (true)
      {
        if (k >= 0)
          arrayOfString[k] = paramCursor.getString(i);
        if (paramCursor.moveToNext())
          break;
        ACCOUNT_OUTGOING_PREFS.put(paramString, arrayOfString);
        return;
        label103: boolean bool = str.equals("sx_dn");
        if (bool)
          k = 1;
      }
    }
    finally
    {
      paramCursor.close();
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.utils.OutgoingMsgPrefs
 * JD-Core Version:    0.6.2
 */