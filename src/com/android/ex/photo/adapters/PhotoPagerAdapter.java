package com.android.ex.photo.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import com.android.ex.photo.Intents;
import com.android.ex.photo.Intents.PhotoViewIntentBuilder;
import com.android.ex.photo.fragments.PhotoViewFragment;

public class PhotoPagerAdapter extends BaseCursorPagerAdapter
{
  private int mContentUriIndex;
  private int mThumbnailUriIndex;

  public PhotoPagerAdapter(Context paramContext, FragmentManager paramFragmentManager, Cursor paramCursor)
  {
    super(paramContext, paramFragmentManager, paramCursor);
  }

  public Fragment getItem(Context paramContext, Cursor paramCursor, int paramInt)
  {
    String str1 = paramCursor.getString(this.mContentUriIndex);
    String str2 = paramCursor.getString(this.mThumbnailUriIndex);
    Intents.PhotoViewIntentBuilder localPhotoViewIntentBuilder = Intents.newPhotoViewFragmentIntentBuilder(this.mContext);
    localPhotoViewIntentBuilder.setResolvedPhotoUri(str1).setThumbnailUri(str2);
    return new PhotoViewFragment(localPhotoViewIntentBuilder.build(), paramInt, this);
  }

  public String getPhotoUri(Cursor paramCursor)
  {
    return paramCursor.getString(this.mContentUriIndex);
  }

  public Cursor swapCursor(Cursor paramCursor)
  {
    if (paramCursor != null)
      this.mContentUriIndex = paramCursor.getColumnIndex("contentUri");
    for (this.mThumbnailUriIndex = paramCursor.getColumnIndex("thumbnailUri"); ; this.mThumbnailUriIndex = -1)
    {
      return super.swapCursor(paramCursor);
      this.mContentUriIndex = -1;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.adapters.PhotoPagerAdapter
 * JD-Core Version:    0.6.2
 */