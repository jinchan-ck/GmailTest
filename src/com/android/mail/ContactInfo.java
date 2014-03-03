package com.android.mail;

import android.graphics.Bitmap;
import android.net.Uri;

public class ContactInfo
{
  public final Uri contactUri;
  public final Bitmap photo;
  public final Integer status;

  public ContactInfo(Uri paramUri, Integer paramInteger, Bitmap paramBitmap)
  {
    this.contactUri = paramUri;
    this.status = paramInteger;
    this.photo = paramBitmap;
  }

  public String toString()
  {
    return "{status=" + this.status + " photo=" + this.photo + "}";
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ContactInfo
 * JD-Core Version:    0.6.2
 */