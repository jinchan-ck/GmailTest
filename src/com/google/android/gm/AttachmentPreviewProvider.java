package com.google.android.gm;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;

public class AttachmentPreviewProvider extends ContentProvider
{
  public static final String BASE_URI = "content://com.google.android.gm.attachmentspreviews/";
  static final String PREVIEW_NAME = "attachment-preview";
  private static String sPreviewContentType = null;

  public static void setPreviewContentType(String paramString)
  {
    sPreviewContentType = paramString;
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException();
  }

  public String getType(Uri paramUri)
  {
    if ("attachment-preview".equals(paramUri.getLastPathSegment()))
      return sPreviewContentType;
    return null;
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    throw new UnsupportedOperationException();
  }

  public boolean onCreate()
  {
    return true;
  }

  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    String str = paramUri.getLastPathSegment();
    if (!"r".equals(paramString))
      throw new FileNotFoundException("Bad mode for " + paramUri + ": " + paramString);
    return ParcelFileDescriptor.open(getContext().getFileStreamPath(str), 268435456);
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    return null;
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.AttachmentPreviewProvider
 * JD-Core Version:    0.6.2
 */