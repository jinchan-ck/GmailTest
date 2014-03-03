package com.android.mail.utils;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.Log;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUtils
{
  private static final Pattern DATE_CLEANUP_PATTERN_WRONG_TIMEZONE = Pattern.compile("GMT([-+]\\d{4})$");
  private static Boolean sDebugLoggingEnabledForTests = null;

  public static boolean buildPreventsDebugLogging()
  {
    return true;
  }

  public static String byteToHex(int paramInt)
  {
    return byteToHex(new StringBuilder(), paramInt).toString();
  }

  public static StringBuilder byteToHex(StringBuilder paramStringBuilder, int paramInt)
  {
    int i = paramInt & 0xFF;
    paramStringBuilder.append("0123456789ABCDEF".charAt(i >> 4));
    paramStringBuilder.append("0123456789ABCDEF".charAt(i & 0xF));
    return paramStringBuilder;
  }

  public static String cleanUpMimeDate(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return paramString;
    return DATE_CLEANUP_PATTERN_WRONG_TIMEZONE.matcher(paramString).replaceFirst("$1");
  }

  public static String contentUriToString(String paramString, Uri paramUri)
  {
    if (isDebugLoggingEnabled(paramString))
      return paramUri.toString();
    List localList = paramUri.getPathSegments();
    Uri.Builder localBuilder = new Uri.Builder().scheme(paramUri.getScheme()).authority(paramUri.getAuthority()).query(paramUri.getQuery()).fragment(paramUri.getFragment()).appendPath(String.valueOf(((String)localList.get(0)).hashCode()));
    for (int i = 1; i < localList.size(); i++)
      localBuilder.appendPath((String)localList.get(i));
    return localBuilder.toString();
  }

  public static int d(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 3))
      return Log.d(paramString1, String.format(paramString2, paramArrayOfObject));
    return 0;
  }

  public static int d(String paramString1, Throwable paramThrowable, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 3))
      return Log.d(paramString1, String.format(paramString2, paramArrayOfObject), paramThrowable);
    return 0;
  }

  public static int e(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 6))
      return Log.e(paramString1, String.format(paramString2, paramArrayOfObject));
    return 0;
  }

  public static int e(String paramString1, Throwable paramThrowable, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 6))
      return Log.e(paramString1, String.format(paramString2, paramArrayOfObject), paramThrowable);
    return 0;
  }

  public static int i(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 4))
      return Log.i(paramString1, String.format(paramString2, paramArrayOfObject));
    return 0;
  }

  public static int i(String paramString1, Throwable paramThrowable, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 4))
      return Log.i(paramString1, String.format(paramString2, paramArrayOfObject), paramThrowable);
    return 0;
  }

  protected static boolean isDebugLoggingEnabled(String paramString)
  {
    if (buildPreventsDebugLogging())
      return false;
    if (sDebugLoggingEnabledForTests != null)
      return sDebugLoggingEnabledForTests.booleanValue();
    return Log.isLoggable(paramString, 3);
  }

  public static boolean isLoggable(String paramString, int paramInt)
  {
    if (3 > paramInt)
      return false;
    return Log.isLoggable(paramString, paramInt);
  }

  static void setDebugLoggingEnabledForTests(boolean paramBoolean)
  {
    sDebugLoggingEnabledForTests = Boolean.valueOf(paramBoolean);
  }

  public static int v(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 2))
      return Log.v(paramString1, String.format(paramString2, paramArrayOfObject));
    return 0;
  }

  public static int v(String paramString1, Throwable paramThrowable, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 2))
      return Log.v(paramString1, String.format(paramString2, paramArrayOfObject), paramThrowable);
    return 0;
  }

  public static int w(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 5))
      return Log.w(paramString1, String.format(paramString2, paramArrayOfObject));
    return 0;
  }

  public static int w(String paramString1, Throwable paramThrowable, String paramString2, Object[] paramArrayOfObject)
  {
    if (isLoggable(paramString1, 5))
      return Log.w(paramString1, String.format(paramString2, paramArrayOfObject), paramThrowable);
    return 0;
  }

  public static int wtf(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    return Log.wtf(paramString1, String.format(paramString2, paramArrayOfObject), new Error());
  }

  public static int wtf(String paramString1, Throwable paramThrowable, String paramString2, Object[] paramArrayOfObject)
  {
    return Log.wtf(paramString1, String.format(paramString2, paramArrayOfObject), paramThrowable);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.LogUtils
 * JD-Core Version:    0.6.2
 */