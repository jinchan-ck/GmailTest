package com.android.ex.photo;

import android.app.ActionBar;
import android.app.ActionBar.OnMenuVisibilityListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import com.android.ex.photo.adapters.PhotoPagerAdapter;
import com.android.ex.photo.fragments.PhotoViewFragment;
import com.android.ex.photo.loaders.PhotoPagerLoader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PhotoViewActivity extends Activity
  implements LoaderManager.LoaderCallbacks<Cursor>, ViewPager.OnPageChangeListener, PhotoViewPager.OnInterceptTouchListener, ActionBar.OnMenuVisibilityListener
{
  public static int sMemoryClass;
  private long mActionBarHideDelayTime;
  private Runnable mActionBarHideRunnable = new Runnable()
  {
    public void run()
    {
      PhotoViewActivity.this.setLightsOutMode(true);
    }
  };
  private PhotoPagerAdapter mAdapter;
  private int mAlbumCount = -1;
  private Set<CursorChangedListener> mCursorListeners = new HashSet();
  private boolean mFullScreen;
  private final Handler mHandler = new Handler();
  private boolean mIsEmpty;
  private boolean mIsPaused = true;
  private int mPhotoIndex;
  private String mPhotosUri;
  private String[] mProjection;
  private boolean mRestartLoader;
  private Set<OnScreenListener> mScreenListeners = new HashSet();
  private PhotoViewPager mViewPager;

  private void cancelActionBarHideRunnable()
  {
    this.mHandler.removeCallbacks(this.mActionBarHideRunnable);
  }

  private void notifyCursorListeners(Cursor paramCursor)
  {
    try
    {
      Iterator localIterator = this.mCursorListeners.iterator();
      while (localIterator.hasNext())
        ((CursorChangedListener)localIterator.next()).onCursorChanged(paramCursor);
    }
    finally
    {
    }
  }

  private void postActionBarHideRunnableWithDelay()
  {
    this.mHandler.postDelayed(this.mActionBarHideRunnable, this.mActionBarHideDelayTime);
  }

  private void setFullScreen(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i;
    if (paramBoolean1 != this.mFullScreen)
    {
      i = 1;
      this.mFullScreen = paramBoolean1;
      if (!this.mFullScreen)
        break label83;
      setLightsOutMode(true);
      cancelActionBarHideRunnable();
    }
    while (true)
    {
      if (i == 0)
        return;
      Iterator localIterator = this.mScreenListeners.iterator();
      while (localIterator.hasNext())
        ((OnScreenListener)localIterator.next()).onFullScreenChanged(this.mFullScreen);
      i = 0;
      break;
      label83: setLightsOutMode(false);
      if (paramBoolean2)
        postActionBarHideRunnableWithDelay();
    }
  }

  private void setLightsOutMode(boolean paramBoolean)
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      if (paramBoolean);
      for (int j = 1285; ; j = 1280)
      {
        this.mViewPager.setSystemUiVisibility(j);
        return;
      }
    }
    ActionBar localActionBar = getActionBar();
    if (paramBoolean)
    {
      localActionBar.hide();
      if (!paramBoolean)
        break label70;
    }
    label70: for (int i = 1; ; i = 0)
    {
      this.mViewPager.setSystemUiVisibility(i);
      return;
      localActionBar.show();
      break;
    }
  }

  public void addCursorListener(CursorChangedListener paramCursorChangedListener)
  {
    try
    {
      this.mCursorListeners.add(paramCursorChangedListener);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void addScreenListener(OnScreenListener paramOnScreenListener)
  {
    this.mScreenListeners.add(paramOnScreenListener);
  }

  public Cursor getCursor()
  {
    if (this.mAdapter == null)
      return null;
    return this.mAdapter.getCursor();
  }

  public Cursor getCursorAtProperPosition()
  {
    if (this.mViewPager == null)
      return null;
    int i = this.mViewPager.getCurrentItem();
    Cursor localCursor = this.mAdapter.getCursor();
    if (localCursor == null)
      return null;
    localCursor.moveToPosition(i);
    return localCursor;
  }

  public boolean isFragmentActive(Fragment paramFragment)
  {
    if ((this.mViewPager == null) || (this.mAdapter == null));
    while (this.mViewPager.getCurrentItem() != this.mAdapter.getItemPosition(paramFragment))
      return false;
    return true;
  }

  public boolean isFragmentFullScreen(Fragment paramFragment)
  {
    if ((this.mViewPager == null) || (this.mAdapter == null) || (this.mAdapter.getCount() == 0))
      return this.mFullScreen;
    return (this.mFullScreen) || (this.mViewPager.getCurrentItem() != this.mAdapter.getItemPosition(paramFragment));
  }

  public void onBackPressed()
  {
    if (this.mFullScreen)
    {
      toggleFullScreen();
      return;
    }
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sMemoryClass = ((ActivityManager)getApplicationContext().getSystemService("activity")).getMemoryClass();
    Intent localIntent = getIntent();
    int i = -1;
    if (paramBundle != null)
    {
      i = paramBundle.getInt("com.google.android.apps.plus.PhotoViewFragment.ITEM", -1);
      this.mFullScreen = paramBundle.getBoolean("com.google.android.apps.plus.PhotoViewFragment.FULLSCREEN", false);
    }
    if (localIntent.hasExtra("photos_uri"))
      this.mPhotosUri = localIntent.getStringExtra("photos_uri");
    if (localIntent.hasExtra("projection"));
    for (this.mProjection = localIntent.getStringArrayExtra("projection"); ; this.mProjection = null)
    {
      if ((localIntent.hasExtra("photo_index")) && (i < 0))
        i = localIntent.getIntExtra("photo_index", -1);
      this.mPhotoIndex = i;
      setContentView(R.layout.photo_activity_view);
      this.mAdapter = new PhotoPagerAdapter(this, getFragmentManager(), null);
      this.mViewPager = ((PhotoViewPager)findViewById(R.id.photo_view_pager));
      this.mViewPager.setAdapter(this.mAdapter);
      this.mViewPager.setOnPageChangeListener(this);
      this.mViewPager.setOnInterceptTouchListener(this);
      getLoaderManager().initLoader(1, null, this);
      ActionBar localActionBar = getActionBar();
      localActionBar.setDisplayHomeAsUpEnabled(true);
      this.mActionBarHideDelayTime = getResources().getInteger(R.integer.action_bar_delay_time_in_millis);
      localActionBar.addOnMenuVisibilityListener(this);
      localActionBar.setDisplayOptions(0, 8);
      return;
    }
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    if (paramInt == 1)
      return new PhotoPagerLoader(this, Uri.parse(this.mPhotosUri), this.mProjection);
    return null;
  }

  public void onFragmentVisible(PhotoViewFragment paramPhotoViewFragment)
  {
    updateActionBar(paramPhotoViewFragment);
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (paramLoader.getId() == 1)
    {
      if ((paramCursor != null) && (paramCursor.getCount() != 0))
        break label31;
      this.mIsEmpty = true;
    }
    while (true)
    {
      updateActionItems();
      return;
      label31: this.mAlbumCount = paramCursor.getCount();
      if (this.mIsPaused)
      {
        this.mRestartLoader = true;
        return;
      }
      this.mIsEmpty = false;
      int i = this.mPhotoIndex;
      if (i < 0)
        i = 0;
      this.mAdapter.swapCursor(paramCursor);
      notifyCursorListeners(paramCursor);
      this.mViewPager.setCurrentItem(i, false);
      setViewActivated();
    }
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
  }

  public void onMenuVisibilityChanged(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      cancelActionBarHideRunnable();
      return;
    }
    postActionBarHideRunnableWithDelay();
  }

  public void onNewPhotoLoaded()
  {
  }

  public void onPageScrollStateChanged(int paramInt)
  {
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
    this.mPhotoIndex = paramInt;
    setViewActivated();
  }

  protected void onPause()
  {
    this.mIsPaused = true;
    super.onPause();
  }

  protected void onResume()
  {
    super.onResume();
    setFullScreen(this.mFullScreen, false);
    this.mIsPaused = false;
    if (this.mRestartLoader)
    {
      this.mRestartLoader = false;
      getLoaderManager().restartLoader(1, null, this);
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("com.google.android.apps.plus.PhotoViewFragment.ITEM", this.mViewPager.getCurrentItem());
    paramBundle.putBoolean("com.google.android.apps.plus.PhotoViewFragment.FULLSCREEN", this.mFullScreen);
  }

  public PhotoViewPager.InterceptType onTouchIntercept(float paramFloat1, float paramFloat2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    Iterator localIterator = this.mScreenListeners.iterator();
    while (localIterator.hasNext())
    {
      OnScreenListener localOnScreenListener = (OnScreenListener)localIterator.next();
      if (!bool1)
        bool1 = localOnScreenListener.onInterceptMoveLeft(paramFloat1, paramFloat2);
      if (!bool2)
        bool2 = localOnScreenListener.onInterceptMoveRight(paramFloat1, paramFloat2);
      localOnScreenListener.onViewActivated();
    }
    if (bool1)
    {
      if (bool2)
        return PhotoViewPager.InterceptType.BOTH;
      return PhotoViewPager.InterceptType.LEFT;
    }
    if (bool2)
      return PhotoViewPager.InterceptType.RIGHT;
    return PhotoViewPager.InterceptType.NONE;
  }

  public void removeCursorListener(CursorChangedListener paramCursorChangedListener)
  {
    try
    {
      this.mCursorListeners.remove(paramCursorChangedListener);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void removeScreenListener(OnScreenListener paramOnScreenListener)
  {
    this.mScreenListeners.remove(paramOnScreenListener);
  }

  public void setViewActivated()
  {
    Iterator localIterator = this.mScreenListeners.iterator();
    while (localIterator.hasNext())
      ((OnScreenListener)localIterator.next()).onViewActivated();
  }

  public void toggleFullScreen()
  {
    if (!this.mFullScreen);
    for (boolean bool = true; ; bool = false)
    {
      setFullScreen(bool, true);
      return;
    }
  }

  protected void updateActionBar(PhotoViewFragment paramPhotoViewFragment)
  {
    int i = 1 + this.mViewPager.getCurrentItem();
    int j;
    String str;
    if (this.mAlbumCount >= 0)
    {
      j = 1;
      Cursor localCursor = getCursorAtProperPosition();
      if (localCursor == null)
        break label102;
      str = localCursor.getString(localCursor.getColumnIndex("_display_name"));
      label49: if ((!this.mIsEmpty) && (j != 0) && (i > 0))
        break label108;
    }
    label102: label108: Resources localResources;
    int k;
    Object[] arrayOfObject;
    for (Object localObject = null; ; localObject = localResources.getString(k, arrayOfObject))
    {
      ActionBar localActionBar = getActionBar();
      localActionBar.setDisplayOptions(8, 8);
      localActionBar.setTitle(str);
      localActionBar.setSubtitle((CharSequence)localObject);
      return;
      j = 0;
      break;
      str = null;
      break label49;
      localResources = getResources();
      k = R.string.photo_view_count;
      arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(i);
      arrayOfObject[1] = Integer.valueOf(this.mAlbumCount);
    }
  }

  protected void updateActionItems()
  {
  }

  public static abstract interface CursorChangedListener
  {
    public abstract void onCursorChanged(Cursor paramCursor);
  }

  public static abstract interface OnScreenListener
  {
    public abstract void onFullScreenChanged(boolean paramBoolean);

    public abstract boolean onInterceptMoveLeft(float paramFloat1, float paramFloat2);

    public abstract boolean onInterceptMoveRight(float paramFloat1, float paramFloat2);

    public abstract void onViewActivated();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.PhotoViewActivity
 * JD-Core Version:    0.6.2
 */