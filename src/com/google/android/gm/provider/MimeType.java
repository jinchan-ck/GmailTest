package com.google.android.gm.provider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import com.google.android.gsf.Gservices;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;

public class MimeType
{
  static final String EML_ATTACHMENT_CONTENT_TYPE = "application/eml";
  static final String GENERIC_MIMETYPE = "application/octet-stream";
  private static final Set<String> UNACCEPTABLE_ATTACHMENT_TYPES = ImmutableSet.of("application/zip", "application/x-gzip", "application/x-bzip2", "application/x-compress", "application/x-compressed", "application/x-tar", new String[0]);
  private static Set<String> sGviewSupportedTypes = ImmutableSet.of("application/pdf", "application/vnd.ms-powerpoint", "image/tiff", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.openxmlformats-officedocument.presentationml.presentation", new String[0]);

  private static String getFilenameExtension(String paramString)
  {
    boolean bool = TextUtils.isEmpty(paramString);
    String str = null;
    if (!bool)
    {
      int i = paramString.lastIndexOf('.');
      str = null;
      if (i > 0)
      {
        int j = -1 + paramString.length();
        str = null;
        if (i < j)
          str = paramString.substring(i + 1).toLowerCase();
      }
    }
    return str;
  }

  static String inferMimeType(String paramString1, String paramString2)
  {
    String str1 = getFilenameExtension(paramString1);
    if (TextUtils.isEmpty(str1));
    do
    {
      return paramString2;
      if (("text/plain".equalsIgnoreCase(paramString2)) || ("application/octet-stream".equalsIgnoreCase(paramString2)));
      for (int i = 1; ; i = 0)
      {
        String str2;
        if (i == 0)
        {
          boolean bool = TextUtils.isEmpty(paramString2);
          str2 = null;
          if (!bool);
        }
        else
        {
          str2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(str1);
        }
        if (TextUtils.isEmpty(str2))
          break;
        return str2;
      }
      if (str1.equals("eml"))
        return "application/eml";
    }
    while (!TextUtils.isEmpty(paramString2));
    return "application/octet-stream";
  }

  public static boolean isBlocked(String paramString)
  {
    return UNACCEPTABLE_ATTACHMENT_TYPES.contains(paramString);
  }

  public static boolean isPreviewable(Context paramContext, String paramString)
  {
    String str = Gservices.getString(paramContext.getContentResolver(), "gmail_gview_supported_types");
    if (str != null)
      sGviewSupportedTypes = ImmutableSet.copyOf(TextUtils.split(str, ","));
    return sGviewSupportedTypes.contains(paramString);
  }

  public static boolean isViewable(Context paramContext, Uri paramUri, String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0) || ("null".equals(paramString)));
    while (true)
    {
      return false;
      if (!isBlocked(paramString))
      {
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setDataAndType(paramUri, paramString);
        try
        {
          PackageManager localPackageManager = paramContext.getPackageManager();
          if (localPackageManager.queryIntentActivities(localIntent, 65536).size() > 0)
            return true;
        }
        catch (UnsupportedOperationException localUnsupportedOperationException)
        {
        }
      }
    }
    return false;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.MimeType
 * JD-Core Version:    0.6.2
 */