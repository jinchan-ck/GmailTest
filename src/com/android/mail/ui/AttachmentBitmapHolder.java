package com.android.mail.ui;

import android.content.ContentResolver;
import android.graphics.Bitmap;

public abstract interface AttachmentBitmapHolder
{
  public abstract boolean bitmapSetToDefault();

  public abstract ContentResolver getResolver();

  public abstract int getThumbnailHeight();

  public abstract int getThumbnailWidth();

  public abstract void setThumbnail(Bitmap paramBitmap);

  public abstract void setThumbnailToDefault();
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.AttachmentBitmapHolder
 * JD-Core Version:    0.6.2
 */