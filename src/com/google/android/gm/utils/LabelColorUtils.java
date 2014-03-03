package com.google.android.gm.utils;

import android.database.Cursor;
import android.graphics.Color;
import com.google.android.gm.provider.MailStore.CustomLabelColorPreference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LabelColorUtils
{
  public static ConcurrentHashMap<String, ConcurrentHashMap<String, String[]>> ACCOUNT_CUSTOM_LABEL_COLORS = new ConcurrentHashMap();
  public static String CUSTOM_BACKGROUND_COLOR = "background_color";
  public static String CUSTOM_COLOR_INDEX = "color_index";
  public static String CUSTOM_TEXT_COLOR = "text_color";
  private static final String[] DEFAULT_COLORS = { "#eeeeee", "#888888" };
  public static int DEFAULT_COLOR_ID = -1;
  private static final String[][] LABEL_COLORS = { { "#f1f5ec", "#006633" }, { "#dee5f2", "#5a6986" }, { "#e0ecff", "#206cff" }, { "#dfe2ff", "#0000cc" }, { "#e0d5f9", "#5229a3" }, { "#fde9f4", "#854f61" }, { "#ffe3e3", "#cc0000" }, { "#fff0e1", "#ec7000" }, { "#fadcb3", "#b36d00" }, { "#f3e7b3", "#ab8b00" }, { "#ffffd4", "#636330" }, { "#f9ffef", "#64992c" }, { "#f1f5ec", "#006633" }, { "#5a6986", "#dee5f2" }, { "#206cff", "#e0ecff" }, { "#0000cc", "#dfe2ff" }, { "#5229a3", "#e0d5f9" }, { "#854f61", "#fde9f4" }, { "#cc0000", "#ffe3e3" }, { "#ec7000", "#fff0e1" }, { "#b36d00", "#fadcb3" }, { "#ab8b00", "#f3e7b3" }, { "#636330", "#ffffd4" }, { "#64992c", "#f9ffef" }, { "#006633", "#f1f5ec" } };
  private static final int[] MUTED_COLORS = { -16751053, -16751053 };
  private static final String[] MUTED_COLOR_STRINGS = { "#ff006633", "#ff006633" };
  private static final int NUM_COLORS = LABEL_COLORS.length;

  public static void addOrUpdateCustomLabelColor(String paramString1, String paramString2, MailStore.CustomLabelColorPreference paramCustomLabelColorPreference)
  {
    ConcurrentHashMap localConcurrentHashMap = (ConcurrentHashMap)ACCOUNT_CUSTOM_LABEL_COLORS.get(paramString1);
    if (localConcurrentHashMap == null)
    {
      localConcurrentHashMap = new ConcurrentHashMap();
      ACCOUNT_CUSTOM_LABEL_COLORS.put(paramString1, localConcurrentHashMap);
    }
    String[] arrayOfString = new String[2];
    arrayOfString[0] = paramCustomLabelColorPreference.backgroundColor;
    arrayOfString[1] = paramCustomLabelColorPreference.textColor;
    localConcurrentHashMap.put(paramString2, arrayOfString);
  }

  public static int getDefaultLabelBackgroundColor()
  {
    return Color.parseColor(LABEL_COLORS[0][0]);
  }

  public static int[] getLabelColorInts(int paramInt, String paramString)
  {
    int[] arrayOfInt = new int[2];
    String[] arrayOfString = getLabelColorStrings(paramInt, paramString);
    arrayOfInt[0] = Color.parseColor(arrayOfString[0]);
    arrayOfInt[1] = Color.parseColor(arrayOfString[1]);
    return arrayOfInt;
  }

  public static int[] getLabelColorInts(String paramString1, String paramString2)
  {
    try
    {
      int[] arrayOfInt = getLabelColorInts(Integer.parseInt(paramString1), paramString2);
      return arrayOfInt;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return null;
  }

  public static String[] getLabelColorStrings(int paramInt, String paramString)
  {
    String[] arrayOfString;
    if (paramInt == DEFAULT_COLOR_ID)
      arrayOfString = DEFAULT_COLORS;
    while (true)
    {
      return arrayOfString;
      if (paramInt >= 0)
        break;
      Map localMap = (Map)ACCOUNT_CUSTOM_LABEL_COLORS.get(paramString);
      if (localMap != null);
      for (arrayOfString = (String[])localMap.get(paramInt + ""); arrayOfString == null; arrayOfString = DEFAULT_COLORS)
        return DEFAULT_COLORS;
    }
    if (paramInt >= NUM_COLORS)
      return DEFAULT_COLORS;
    return LABEL_COLORS[paramInt];
  }

  public static int[] getMutedColorInts()
  {
    return MUTED_COLORS;
  }

  public static void instantiateCustomLabelColors(String paramString, Cursor paramCursor)
  {
    if (paramCursor.moveToFirst())
    {
      ConcurrentHashMap localConcurrentHashMap = new ConcurrentHashMap();
      int i = paramCursor.getColumnIndexOrThrow(CUSTOM_COLOR_INDEX);
      int j = paramCursor.getColumnIndexOrThrow(CUSTOM_BACKGROUND_COLOR);
      int k = paramCursor.getColumnIndexOrThrow(CUSTOM_TEXT_COLOR);
      do
      {
        String str = paramCursor.getString(i);
        String[] arrayOfString = new String[2];
        arrayOfString[0] = paramCursor.getString(j);
        arrayOfString[1] = paramCursor.getString(k);
        localConcurrentHashMap.put(str, arrayOfString);
      }
      while (paramCursor.moveToNext());
      ACCOUNT_CUSTOM_LABEL_COLORS.put(paramString, localConcurrentHashMap);
    }
    paramCursor.close();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.utils.LabelColorUtils
 * JD-Core Version:    0.6.2
 */