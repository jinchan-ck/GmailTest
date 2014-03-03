package com.google.android.gm.contentprovider;

import android.net.Uri;
import android.net.Uri.Builder;

public class PrivateGmailAccess
{
  private static Uri getLabelUri(String paramString)
  {
    return Uri.parse("content://com.google.android.gm/" + paramString + "/label/");
  }

  public static Uri getLabelUriForId(String paramString, long paramLong)
  {
    return getLabelUri(paramString).buildUpon().appendPath(Long.toString(paramLong)).build();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.contentprovider.PrivateGmailAccess
 * JD-Core Version:    0.6.2
 */