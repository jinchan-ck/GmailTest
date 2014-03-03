package com.google.android.gm.contentprovider;

import android.net.Uri;

public final class GmailAccess
{
  public static final String[] LABEL_PROJECTION = { "_id", "labelUri", "canonicalName", "name", "numConversations", "numUnreadConversations", "text_color", "background_color" };

  public static Uri getLabelsUri(String paramString)
  {
    return Uri.parse("content://com.google.android.gm/" + paramString + "/labels");
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.contentprovider.GmailAccess
 * JD-Core Version:    0.6.2
 */