package com.android.mail.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.android.mail.providers.Attachment;
import com.google.common.collect.ImmutableMap.Builder;
import java.text.DecimalFormat;
import java.util.Map;

public class AttachmentUtils
{
  private static Map<String, String> sDisplayNameMap;

  public static String convertToHumanReadableSize(Context paramContext, long paramLong)
  {
    if (paramLong == 0L)
      return "";
    if (paramLong < 1024L)
      return paramLong + paramContext.getString(2131427430);
    if (paramLong < 1048576L)
      return paramLong / 1024L + paramContext.getString(2131427431);
    DecimalFormat localDecimalFormat = new DecimalFormat("0.#");
    return localDecimalFormat.format((float)paramLong / 1048576.0F) + paramContext.getString(2131427432);
  }

  public static String getDisplayType(Context paramContext, Attachment paramAttachment)
  {
    String str1 = getMimeTypeDisplayName(paramContext, paramAttachment.contentType);
    if (!TextUtils.isEmpty(paramAttachment.contentType));
    for (int i = paramAttachment.contentType.indexOf('/'); ; i = -1)
    {
      if ((str1 == null) && (i > 0))
        str1 = getMimeTypeDisplayName(paramContext, paramAttachment.contentType.substring(0, i));
      if (str1 == null)
      {
        String str2 = Utils.getFileExtension(paramAttachment.name);
        if ((str2 != null) && (str2.length() > 1) && (str2.indexOf('.') == 0))
        {
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = str2.substring(1).toUpperCase();
          str1 = paramContext.getString(2131427441, arrayOfObject);
        }
      }
      if (str1 == null)
        str1 = "";
      return str1;
    }
  }

  public static String getIdentifier(Attachment paramAttachment)
  {
    Uri localUri = paramAttachment.contentUri;
    if (localUri != null)
      return localUri.toString();
    return null;
  }

  public static String getMimeTypeDisplayName(Context paramContext, String paramString)
  {
    try
    {
      if (sDisplayNameMap == null)
      {
        String str2 = paramContext.getString(2131427437);
        String str3 = paramContext.getString(2131427438);
        String str4 = paramContext.getString(2131427439);
        sDisplayNameMap = new ImmutableMap.Builder().put("image", paramContext.getString(2131427433)).put("audio", paramContext.getString(2131427435)).put("video", paramContext.getString(2131427434)).put("text", paramContext.getString(2131427436)).put("application/pdf", paramContext.getString(2131427440)).put("application/msword", str2).put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", str2).put("application/vnd.ms-powerpoint", str3).put("application/vnd.openxmlformats-officedocument.presentationml.presentation", str3).put("application/vnd.ms-excel", str4).put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", str4).build();
      }
      String str1 = (String)sDisplayNameMap.get(paramString);
      return str1;
    }
    finally
    {
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.AttachmentUtils
 * JD-Core Version:    0.6.2
 */