package com.google.android.gm;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import java.util.Set;

public class SenderInfoFetcher
{
  private static final int CONTACT_ID_COLUMN = 3;
  private static final int EMAIL_COLUMN = 1;
  private static final int PHOTO_ID_COLUMN = 4;
  private static final String[] PRESENCE_CLASS_NAMES = { "gm-offline", "gm-invisible", "gm-away", "gm-idle", "gm-busy", "gm-online" };
  private static final String[] PROJECTION = { "_id", "data1", "contact_presence", "contact_id", "photo_id" };
  private static final int STATUS_COLUMN = 2;
  private final String[] mAddresses;
  private ContentResolver mResolver;

  public SenderInfoFetcher(ContentResolver paramContentResolver, Set<String> paramSet)
  {
    this.mResolver = paramContentResolver;
    this.mAddresses = ((String[])paramSet.toArray(new String[paramSet.size()]));
  }

  private static final void appendJsString(String paramString, StringBuilder paramStringBuilder)
  {
    if (paramString == null)
    {
      paramStringBuilder.append("null");
      return;
    }
    paramStringBuilder.append('\'');
    Utils.jsEscape(paramString, paramStringBuilder);
    paramStringBuilder.append('\'');
  }

  public String getUpdateJson()
  {
    int i = this.mAddresses.length;
    if (i == 0)
      return null;
    StringBuilder localStringBuilder1 = new StringBuilder().append("mimetype").append("='").append("vnd.android.cursor.item/email_v2").append("' AND ").append("data1").append(" IN (");
    for (int j = 0; j < i; j++)
    {
      if (j != 0)
        localStringBuilder1.append(',');
      localStringBuilder1.append('?');
    }
    localStringBuilder1.append(')');
    Cursor localCursor = this.mResolver.query(ContactsContract.Data.CONTENT_URI, PROJECTION, localStringBuilder1.toString(), this.mAddresses, null);
    if (localCursor != null)
    {
      StringBuilder localStringBuilder2 = new StringBuilder().append('[');
      if (localCursor.moveToFirst())
        while (!localCursor.isAfterLast())
        {
          if (!localCursor.isFirst())
            localStringBuilder2.append(',');
          appendJsString(localCursor.getString(1), localStringBuilder2);
          localStringBuilder2.append(',');
          boolean bool1 = localCursor.isNull(2);
          String str1 = null;
          if (!bool1)
          {
            int k = localCursor.getInt(2);
            str1 = null;
            if (k >= 0)
            {
              int m = PRESENCE_CLASS_NAMES.length;
              str1 = null;
              if (k <= m)
                str1 = PRESENCE_CLASS_NAMES[k];
            }
          }
          appendJsString(str1, localStringBuilder2);
          localStringBuilder2.append(',');
          boolean bool2 = localCursor.isNull(4);
          String str2 = null;
          if (!bool2)
          {
            long l = localCursor.getLong(3);
            str2 = Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, l), "photo").toString();
          }
          appendJsString(str2, localStringBuilder2);
          localCursor.moveToNext();
        }
      localCursor.close();
      localStringBuilder2.append(']');
      return localStringBuilder2.toString();
    }
    return null;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.SenderInfoFetcher
 * JD-Core Version:    0.6.2
 */