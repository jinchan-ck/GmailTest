package com.android.ex.photo.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import com.android.ex.photo.fragments.PhotoViewFragment;
import com.android.ex.photo.util.ImageUtils;

public class PhotoBitmapLoader extends AsyncTaskLoader<Bitmap>
{
  private Bitmap mBitmap;
  private String mPhotoUri;

  public PhotoBitmapLoader(Context paramContext, String paramString)
  {
    super(paramContext);
    this.mPhotoUri = paramString;
  }

  public void deliverResult(Bitmap paramBitmap)
  {
    if ((isReset()) && (paramBitmap != null))
      onReleaseResources(paramBitmap);
    Bitmap localBitmap = this.mBitmap;
    this.mBitmap = paramBitmap;
    if (isStarted())
      super.deliverResult(paramBitmap);
    if ((localBitmap != null) && (localBitmap != paramBitmap) && (!localBitmap.isRecycled()))
      onReleaseResources(localBitmap);
  }

  public Bitmap loadInBackground()
  {
    Context localContext = getContext();
    if ((localContext != null) && (this.mPhotoUri != null))
    {
      Bitmap localBitmap = ImageUtils.createLocalBitmap(localContext.getContentResolver(), Uri.parse(this.mPhotoUri), PhotoViewFragment.sPhotoSize.intValue());
      if (localBitmap != null)
        localBitmap.setDensity(160);
      return localBitmap;
    }
    return null;
  }

  public void onCanceled(Bitmap paramBitmap)
  {
    super.onCanceled(paramBitmap);
    onReleaseResources(paramBitmap);
  }

  protected void onReleaseResources(Bitmap paramBitmap)
  {
    if ((paramBitmap != null) && (!paramBitmap.isRecycled()))
      paramBitmap.recycle();
  }

  protected void onReset()
  {
    super.onReset();
    onStopLoading();
    if (this.mBitmap != null)
    {
      onReleaseResources(this.mBitmap);
      this.mBitmap = null;
    }
  }

  protected void onStartLoading()
  {
    if (this.mBitmap != null)
      deliverResult(this.mBitmap);
    if ((takeContentChanged()) || (this.mBitmap == null))
      forceLoad();
  }

  protected void onStopLoading()
  {
    cancelLoad();
  }

  public void setPhotoUri(String paramString)
  {
    this.mPhotoUri = paramString;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.loaders.PhotoBitmapLoader
 * JD-Core Version:    0.6.2
 */