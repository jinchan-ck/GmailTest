package com.android.mail.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;

public class MimeType
{
  static final String EML_ATTACHMENT_CONTENT_TYPE = "application/eml";
  static final String GENERIC_MIMETYPE = "application/octet-stream";
  private static final String LOG_TAG = LogTag.getLogTag();
  private static final Set<String> UNACCEPTABLE_ATTACHMENT_TYPES = ImmutableSet.of("application/zip", "application/x-gzip", "application/x-bzip2", "application/x-compress", "application/x-compressed", "application/x-tar", new String[0]);
  private static Set<String> sGviewSupportedTypes = ImmutableSet.of("application/pdf", "application/vnd.ms-powerpoint", "image/tiff", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.openxmlformats-officedocument.presentationml.presentation", new String[0]);

  public static boolean isBlocked(String paramString)
  {
    return UNACCEPTABLE_ATTACHMENT_TYPES.contains(paramString);
  }

  public static boolean isInstallable(String paramString)
  {
    return "application/vnd.android.package-archive".equals(paramString);
  }

  public static boolean isViewable(Context paramContext, Uri paramUri, String paramString)
  {
    boolean bool = true;
    if ((paramString == null) || (paramString.length() == 0) || ("null".equals(paramString)))
    {
      String str1 = LOG_TAG;
      Object[] arrayOfObject1 = new Object[bool];
      arrayOfObject1[0] = paramUri;
      LogUtils.d(str1, "Attachment with null content type. '%s", arrayOfObject1);
      return false;
    }
    if (isBlocked(paramString))
    {
      String str3 = LOG_TAG;
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = paramString;
      arrayOfObject3[bool] = paramUri;
      LogUtils.d(str3, "content type '%s' is blocked. '%s", arrayOfObject3);
      return false;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setFlags(524289);
    if (paramUri != null)
      Utils.setIntentDataAndTypeAndNormalize(localIntent, paramUri, paramString);
    while (true)
    {
      try
      {
        PackageManager localPackageManager = paramContext.getPackageManager();
        List localList = localPackageManager.queryIntentActivities(localIntent, 65536);
        if (localList.size() == 0)
        {
          String str2 = LOG_TAG;
          Object[] arrayOfObject2 = new Object[4];
          arrayOfObject2[0] = paramString;
          arrayOfObject2[bool] = paramUri;
          arrayOfObject2[2] = localIntent.getType();
          arrayOfObject2[3] = localIntent.getData();
          LogUtils.w(str2, "Unable to find supporting activity. mime-type: %s, uri: %s, normalized mime-type: %s normalized uri: %s", arrayOfObject2);
        }
        if (localList.size() > 0)
        {
          return bool;
          Utils.setIntentTypeAndNormalize(localIntent, paramString);
          continue;
        }
      }
      catch (UnsupportedOperationException localUnsupportedOperationException)
      {
        return false;
      }
      bool = false;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.MimeType
 * JD-Core Version:    0.6.2
 */