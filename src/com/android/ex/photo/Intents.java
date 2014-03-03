package com.android.ex.photo;

import android.content.Context;
import android.content.Intent;
import com.android.ex.photo.fragments.PhotoViewFragment;

public class Intents
{
  public static PhotoViewIntentBuilder newPhotoViewFragmentIntentBuilder(Context paramContext)
  {
    return new PhotoViewIntentBuilder(paramContext, PhotoViewFragment.class, null);
  }

  public static PhotoViewIntentBuilder newPhotoViewIntentBuilder(Context paramContext, Class<? extends PhotoViewActivity> paramClass)
  {
    return new PhotoViewIntentBuilder(paramContext, paramClass, null);
  }

  public static class PhotoViewIntentBuilder
  {
    private final Intent mIntent;
    private Integer mPhotoIndex;
    private String mPhotosUri;
    private String[] mProjection;
    private String mResolvedPhotoUri;
    private String mThumbnailUri;

    private PhotoViewIntentBuilder(Context paramContext, Class<?> paramClass)
    {
      this.mIntent = new Intent(paramContext, paramClass);
    }

    public Intent build()
    {
      this.mIntent.setAction("android.intent.action.VIEW");
      this.mIntent.setFlags(524288);
      if (this.mPhotoIndex != null)
        this.mIntent.putExtra("photo_index", this.mPhotoIndex.intValue());
      if (this.mPhotosUri != null)
        this.mIntent.putExtra("photos_uri", this.mPhotosUri);
      if (this.mResolvedPhotoUri != null)
        this.mIntent.putExtra("resolved_photo_uri", this.mResolvedPhotoUri);
      if (this.mProjection != null)
        this.mIntent.putExtra("projection", this.mProjection);
      if (this.mThumbnailUri != null)
        this.mIntent.putExtra("thumbnail_uri", this.mThumbnailUri);
      return this.mIntent;
    }

    public PhotoViewIntentBuilder setPhotoIndex(Integer paramInteger)
    {
      this.mPhotoIndex = paramInteger;
      return this;
    }

    public PhotoViewIntentBuilder setPhotosUri(String paramString)
    {
      this.mPhotosUri = paramString;
      return this;
    }

    public PhotoViewIntentBuilder setProjection(String[] paramArrayOfString)
    {
      this.mProjection = paramArrayOfString;
      return this;
    }

    public PhotoViewIntentBuilder setResolvedPhotoUri(String paramString)
    {
      this.mResolvedPhotoUri = paramString;
      return this;
    }

    public PhotoViewIntentBuilder setThumbnailUri(String paramString)
    {
      this.mThumbnailUri = paramString;
      return this;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.Intents
 * JD-Core Version:    0.6.2
 */