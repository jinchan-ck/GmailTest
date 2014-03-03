package com.android.ex.photo.provider;

public final class PhotoContract
{
  public static abstract interface PhotoQuery
  {
    public static final String[] PROJECTION = { "uri", "_display_name", "contentUri", "thumbnailUri", "contentType" };
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.provider.PhotoContract
 * JD-Core Version:    0.6.2
 */