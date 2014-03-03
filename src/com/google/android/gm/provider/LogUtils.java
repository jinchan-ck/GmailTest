package com.google.android.gm.provider;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LogUtils extends com.android.mail.utils.LogUtils
{
  private static Boolean sDebugLoggingEnabledForTests = null;

  public static String contentUriToString(Uri paramUri)
  {
    if (isDebugLoggingEnabled("Gmail"))
      return paramUri.toString();
    List localList = paramUri.getPathSegments();
    Uri.Builder localBuilder = new Uri.Builder().scheme(paramUri.getScheme()).authority(paramUri.getAuthority()).query(paramUri.getQuery()).fragment(paramUri.getFragment()).appendPath(String.valueOf(((String)localList.get(0)).hashCode()));
    for (int i = 1; i < localList.size(); i++)
      localBuilder.appendPath((String)localList.get(i));
    return localBuilder.toString();
  }

  public static String labelSetToString(Set<String> paramSet)
  {
    if ((isDebugLoggingEnabled("Gmail")) || (paramSet == null))
    {
      if (paramSet != null)
        return paramSet.toString();
      return "";
    }
    StringBuilder localStringBuilder = new StringBuilder("[");
    int i = 0;
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (i > 0)
        localStringBuilder.append(", ");
      localStringBuilder.append(sanitizeLabelName(str));
      i++;
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }

  public static String sanitizeLabelName(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      paramString = "";
    while ((isDebugLoggingEnabled("Gmail")) || (Gmail.isSystemLabel(paramString)))
      return paramString;
    return "userlabel:" + String.valueOf(paramString.hashCode());
  }

  static void setDebugLoggingEnabledForTests(boolean paramBoolean)
  {
    sDebugLoggingEnabledForTests = Boolean.valueOf(paramBoolean);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.LogUtils
 * JD-Core Version:    0.6.2
 */