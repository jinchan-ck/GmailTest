package com.android.ex.photo.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.LruCache;
import android.view.View;

public abstract class BaseFragmentPagerAdapter extends PagerAdapter
{
  private FragmentTransaction mCurTransaction = null;
  private Fragment mCurrentPrimaryItem = null;
  private LruCache<String, Fragment> mFragmentCache = new FragmentCache(5);
  private final FragmentManager mFragmentManager;

  public BaseFragmentPagerAdapter(FragmentManager paramFragmentManager)
  {
    this.mFragmentManager = paramFragmentManager;
  }

  public void destroyItem(View paramView, int paramInt, Object paramObject)
  {
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction();
    Fragment localFragment = (Fragment)paramObject;
    String str = localFragment.getTag();
    if (str == null)
      str = makeFragmentName(paramView.getId(), paramInt);
    this.mFragmentCache.put(str, localFragment);
    this.mCurTransaction.detach(localFragment);
  }

  public void finishUpdate(View paramView)
  {
    if (this.mCurTransaction != null)
    {
      this.mCurTransaction.commitAllowingStateLoss();
      this.mCurTransaction = null;
      this.mFragmentManager.executePendingTransactions();
    }
  }

  public abstract Fragment getItem(int paramInt);

  public Object instantiateItem(View paramView, int paramInt)
  {
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction();
    String str = makeFragmentName(paramView.getId(), paramInt);
    this.mFragmentCache.remove(str);
    Fragment localFragment = this.mFragmentManager.findFragmentByTag(str);
    if (localFragment != null)
      this.mCurTransaction.attach(localFragment);
    while (true)
    {
      if (localFragment != this.mCurrentPrimaryItem)
        localFragment.setMenuVisibility(false);
      return localFragment;
      localFragment = getItem(paramInt);
      this.mCurTransaction.add(paramView.getId(), localFragment, makeFragmentName(paramView.getId(), paramInt));
    }
  }

  public boolean isViewFromObject(View paramView, Object paramObject)
  {
    View localView = ((Fragment)paramObject).getView();
    for (Object localObject = paramView; (localObject instanceof View); localObject = ((View)localObject).getParent())
      if (localObject == localView)
        return true;
    return false;
  }

  protected String makeFragmentName(int paramInt1, int paramInt2)
  {
    return "android:switcher:" + paramInt1 + ":" + paramInt2;
  }

  public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader)
  {
  }

  public Parcelable saveState()
  {
    return null;
  }

  public void setPrimaryItem(View paramView, int paramInt, Object paramObject)
  {
    Fragment localFragment = (Fragment)paramObject;
    if (localFragment != this.mCurrentPrimaryItem)
    {
      if (this.mCurrentPrimaryItem != null)
        this.mCurrentPrimaryItem.setMenuVisibility(false);
      if (localFragment != null)
        localFragment.setMenuVisibility(true);
      this.mCurrentPrimaryItem = localFragment;
    }
  }

  public void startUpdate(View paramView)
  {
  }

  private class FragmentCache extends LruCache<String, Fragment>
  {
    public FragmentCache(int arg2)
    {
      super();
    }

    protected void entryRemoved(boolean paramBoolean, String paramString, Fragment paramFragment1, Fragment paramFragment2)
    {
      if ((paramBoolean) || ((paramFragment2 != null) && (paramFragment1 != paramFragment2)))
        BaseFragmentPagerAdapter.this.mCurTransaction.remove(paramFragment1);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.adapters.BaseFragmentPagerAdapter
 * JD-Core Version:    0.6.2
 */