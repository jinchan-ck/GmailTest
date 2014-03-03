package com.android.ex.photo.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.ex.photo.PhotoViewActivity;
import com.android.ex.photo.PhotoViewActivity.CursorChangedListener;
import com.android.ex.photo.PhotoViewActivity.OnScreenListener;
import com.android.ex.photo.R.drawable;
import com.android.ex.photo.R.id;
import com.android.ex.photo.R.layout;
import com.android.ex.photo.adapters.PhotoPagerAdapter;
import com.android.ex.photo.loaders.PhotoBitmapLoader;
import com.android.ex.photo.util.ImageUtils;
import com.android.ex.photo.util.ImageUtils.ImageSize;
import com.android.ex.photo.views.PhotoView;
import com.android.ex.photo.views.ProgressBarWrapper;

public class PhotoViewFragment extends Fragment
  implements LoaderManager.LoaderCallbacks<Bitmap>, View.OnClickListener, PhotoViewActivity.OnScreenListener, PhotoViewActivity.CursorChangedListener
{
  public static Integer sPhotoSize;
  private PhotoPagerAdapter mAdapter;
  private PhotoViewActivity mCallback;
  private TextView mEmptyText;
  private boolean mFullScreen;
  private Intent mIntent;
  private View mPhotoPreviewAndProgress;
  private ImageView mPhotoPreviewImage;
  private ProgressBarWrapper mPhotoProgressBar;
  private PhotoView mPhotoView;
  private final int mPosition;
  private boolean mProgressBarNeeded = true;
  private String mResolvedPhotoUri;
  private ImageView mRetryButton;
  private String mThumbnailUri;

  public PhotoViewFragment()
  {
    this.mPosition = -1;
    this.mProgressBarNeeded = true;
  }

  public PhotoViewFragment(Intent paramIntent, int paramInt, PhotoPagerAdapter paramPhotoPagerAdapter)
  {
    this.mIntent = paramIntent;
    this.mPosition = paramInt;
    this.mAdapter = paramPhotoPagerAdapter;
    this.mProgressBarNeeded = true;
  }

  private void bindPhoto(Bitmap paramBitmap)
  {
    if (this.mPhotoView != null)
      this.mPhotoView.bindPhoto(paramBitmap);
  }

  private void resetPhotoView()
  {
    if (this.mPhotoView != null)
      this.mPhotoView.bindPhoto(null);
  }

  private void setViewVisibility()
  {
    setFullScreen(this.mCallback.isFragmentFullScreen(this));
  }

  public TextView getEmptyText()
  {
    return this.mEmptyText;
  }

  public ProgressBarWrapper getPhotoProgressBar()
  {
    return this.mPhotoProgressBar;
  }

  public ImageView getRetryButton()
  {
    return this.mRetryButton;
  }

  public boolean isPhotoBound()
  {
    return (this.mPhotoView != null) && (this.mPhotoView.isPhotoBound());
  }

  public boolean isProgressBarNeeded()
  {
    return this.mProgressBarNeeded;
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    this.mCallback = ((PhotoViewActivity)paramActivity);
    if (this.mCallback == null)
      throw new IllegalArgumentException("Activity must be a derived class of PhotoViewActivity");
    DisplayMetrics localDisplayMetrics;
    ImageUtils.ImageSize localImageSize;
    if (sPhotoSize == null)
    {
      localDisplayMetrics = new DisplayMetrics();
      WindowManager localWindowManager = (WindowManager)getActivity().getSystemService("window");
      localImageSize = ImageUtils.sUseImageSize;
      localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
    }
    switch (1.$SwitchMap$com$android$ex$photo$util$ImageUtils$ImageSize[localImageSize.ordinal()])
    {
    default:
      sPhotoSize = Integer.valueOf(Math.min(localDisplayMetrics.heightPixels, localDisplayMetrics.widthPixels));
      return;
    case 1:
    }
    sPhotoSize = Integer.valueOf(800 * Math.min(localDisplayMetrics.heightPixels, localDisplayMetrics.widthPixels) / 1000);
  }

  public void onClick(View paramView)
  {
    this.mCallback.toggleFullScreen();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      Bundle localBundle = paramBundle.getBundle("com.android.mail.photo.fragments.PhotoViewFragment.INTENT");
      if (localBundle != null)
        this.mIntent = new Intent().putExtras(localBundle);
    }
    if (this.mIntent != null)
    {
      this.mResolvedPhotoUri = this.mIntent.getStringExtra("resolved_photo_uri");
      this.mThumbnailUri = this.mIntent.getStringExtra("thumbnail_uri");
    }
  }

  public Loader<Bitmap> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    switch (paramInt)
    {
    default:
      return null;
    case 1:
      return new PhotoBitmapLoader(getActivity(), this.mResolvedPhotoUri);
    case 2:
    }
    return new PhotoBitmapLoader(getActivity(), this.mThumbnailUri);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(R.layout.photo_fragment_view, paramViewGroup, false);
    this.mPhotoView = ((PhotoView)localView.findViewById(R.id.photo_view));
    this.mPhotoView.setOnClickListener(this);
    this.mPhotoView.setFullScreen(this.mFullScreen, false);
    this.mPhotoView.enableImageTransforms(true);
    this.mPhotoPreviewAndProgress = localView.findViewById(R.id.photo_preview);
    this.mPhotoPreviewImage = ((ImageView)localView.findViewById(R.id.photo_preview_image));
    ProgressBar localProgressBar = (ProgressBar)localView.findViewById(R.id.indeterminate_progress);
    this.mPhotoProgressBar = new ProgressBarWrapper((ProgressBar)localView.findViewById(R.id.determinate_progress), localProgressBar, true);
    this.mEmptyText = ((TextView)localView.findViewById(R.id.empty_text));
    this.mRetryButton = ((ImageView)localView.findViewById(R.id.retry_button));
    setViewVisibility();
    return localView;
  }

  public void onCursorChanged(Cursor paramCursor)
  {
    Loader localLoader;
    if ((paramCursor.moveToPosition(this.mPosition)) && (!isPhotoBound()))
    {
      localLoader = getLoaderManager().getLoader(1);
      if (localLoader != null);
    }
    else
    {
      return;
    }
    PhotoBitmapLoader localPhotoBitmapLoader = (PhotoBitmapLoader)localLoader;
    this.mResolvedPhotoUri = this.mAdapter.getPhotoUri(paramCursor);
    localPhotoBitmapLoader.setPhotoUri(this.mResolvedPhotoUri);
    localPhotoBitmapLoader.forceLoad();
  }

  public void onDestroyView()
  {
    if (this.mPhotoView != null)
    {
      this.mPhotoView.clear();
      this.mPhotoView = null;
    }
    super.onDestroyView();
  }

  public void onDetach()
  {
    this.mCallback = null;
    super.onDetach();
  }

  public void onFullScreenChanged(boolean paramBoolean)
  {
    setViewVisibility();
  }

  public boolean onInterceptMoveLeft(float paramFloat1, float paramFloat2)
  {
    if (!this.mCallback.isFragmentActive(this));
    while ((this.mPhotoView == null) || (!this.mPhotoView.interceptMoveLeft(paramFloat1, paramFloat2)))
      return false;
    return true;
  }

  public boolean onInterceptMoveRight(float paramFloat1, float paramFloat2)
  {
    if (!this.mCallback.isFragmentActive(this));
    while ((this.mPhotoView == null) || (!this.mPhotoView.interceptMoveRight(paramFloat1, paramFloat2)))
      return false;
    return true;
  }

  public void onLoadFinished(Loader<Bitmap> paramLoader, Bitmap paramBitmap)
  {
    if (getView() == null)
      return;
    switch (paramLoader.getId())
    {
    default:
    case 1:
      while (true)
      {
        if (!this.mProgressBarNeeded)
          this.mPhotoProgressBar.setVisibility(8);
        if (paramBitmap != null)
          this.mCallback.onNewPhotoLoaded();
        setViewVisibility();
        return;
        if (paramBitmap != null)
        {
          bindPhoto(paramBitmap);
          this.mPhotoPreviewAndProgress.setVisibility(8);
          this.mProgressBarNeeded = false;
        }
      }
    case 2:
    }
    if (isPhotoBound())
    {
      this.mPhotoPreviewAndProgress.setVisibility(8);
      this.mProgressBarNeeded = false;
      return;
    }
    this.mPhotoPreviewImage.setVisibility(0);
    if (paramBitmap == null)
    {
      this.mPhotoPreviewImage.setImageResource(R.drawable.default_image);
      this.mPhotoPreviewImage.setScaleType(ImageView.ScaleType.CENTER);
    }
    while (true)
    {
      getLoaderManager().initLoader(1, null, this);
      break;
      this.mPhotoPreviewImage.setImageBitmap(paramBitmap);
    }
  }

  public void onLoaderReset(Loader<Bitmap> paramLoader)
  {
  }

  public void onPause()
  {
    super.onPause();
    this.mCallback.removeCursorListener(this);
    this.mCallback.removeScreenListener(this);
    resetPhotoView();
  }

  public void onResume()
  {
    this.mCallback.addScreenListener(this);
    this.mCallback.addCursorListener(this);
    getLoaderManager().initLoader(2, null, this);
    super.onResume();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mIntent != null)
      paramBundle.putParcelable("com.android.mail.photo.fragments.PhotoViewFragment.INTENT", this.mIntent.getExtras());
  }

  public void onViewActivated()
  {
    if (!this.mCallback.isFragmentActive(this))
    {
      resetViews();
      return;
    }
    if (!isPhotoBound())
      getLoaderManager().restartLoader(2, null, this);
    this.mCallback.onFragmentVisible(this);
  }

  public void resetViews()
  {
    if (this.mPhotoView != null)
      this.mPhotoView.resetTransformations();
  }

  public void setFullScreen(boolean paramBoolean)
  {
    this.mFullScreen = paramBoolean;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.fragments.PhotoViewFragment
 * JD-Core Version:    0.6.2
 */