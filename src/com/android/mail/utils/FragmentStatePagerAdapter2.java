package com.android.mail.utils;

import android.app.Fragment;
import android.app.Fragment.SavedState;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class FragmentStatePagerAdapter2 extends PagerAdapter
{
  private FragmentTransaction mCurTransaction = null;
  private Fragment mCurrentPrimaryItem = null;
  private boolean mEnableSavedStates;
  private final FragmentManager mFragmentManager;
  private SparseArrayCompat<Fragment> mFragments = new SparseArrayCompat();
  private ArrayList<Fragment.SavedState> mSavedState = new ArrayList();

  public FragmentStatePagerAdapter2(FragmentManager paramFragmentManager, boolean paramBoolean)
  {
    this.mFragmentManager = paramFragmentManager;
    this.mEnableSavedStates = paramBoolean;
  }

  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    Fragment localFragment = (Fragment)paramObject;
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction();
    if (this.mEnableSavedStates)
    {
      while (this.mSavedState.size() <= paramInt)
        this.mSavedState.add(null);
      this.mSavedState.set(paramInt, this.mFragmentManager.saveFragmentInstanceState(localFragment));
    }
    this.mFragments.delete(paramInt);
    this.mCurTransaction.remove(localFragment);
  }

  public void finishUpdate(ViewGroup paramViewGroup)
  {
    if (this.mCurTransaction != null)
    {
      this.mCurTransaction.commitAllowingStateLoss();
      this.mCurTransaction = null;
      this.mFragmentManager.executePendingTransactions();
    }
  }

  public Fragment getFragmentAt(int paramInt)
  {
    return (Fragment)this.mFragments.get(paramInt);
  }

  public abstract Fragment getItem(int paramInt);

  public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    Fragment localFragment1 = (Fragment)this.mFragments.get(paramInt);
    if (localFragment1 != null)
      return localFragment1;
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction();
    Fragment localFragment2 = getItem(paramInt);
    if ((this.mEnableSavedStates) && (this.mSavedState.size() > paramInt))
    {
      Fragment.SavedState localSavedState = (Fragment.SavedState)this.mSavedState.get(paramInt);
      if (localSavedState != null)
        localFragment2.setInitialSavedState(localSavedState);
    }
    if (localFragment2 != this.mCurrentPrimaryItem)
      setItemVisible(localFragment2, false);
    this.mFragments.put(paramInt, localFragment2);
    this.mCurTransaction.add(paramViewGroup.getId(), localFragment2);
    return localFragment2;
  }

  public boolean isViewFromObject(View paramView, Object paramObject)
  {
    return ((Fragment)paramObject).getView() == paramView;
  }

  public void notifyDataSetChanged()
  {
    SparseArrayCompat localSparseArrayCompat = new SparseArrayCompat(this.mFragments.size());
    int i = 0;
    if (i < this.mFragments.size())
    {
      int j = this.mFragments.keyAt(i);
      Fragment localFragment = (Fragment)this.mFragments.valueAt(i);
      int k = getItemPosition(localFragment);
      if (k != -2)
        if (k < 0)
          break label88;
      label88: for (int m = k; ; m = j)
      {
        localSparseArrayCompat.put(m, localFragment);
        i++;
        break;
      }
    }
    this.mFragments = localSparseArrayCompat;
    super.notifyDataSetChanged();
  }

  public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader)
  {
    if (paramParcelable != null)
    {
      Bundle localBundle = (Bundle)paramParcelable;
      localBundle.setClassLoader(paramClassLoader);
      this.mFragments.clear();
      if (this.mEnableSavedStates)
      {
        Parcelable[] arrayOfParcelable = localBundle.getParcelableArray("states");
        this.mSavedState.clear();
        if (arrayOfParcelable != null)
          for (int j = 0; j < arrayOfParcelable.length; j++)
            this.mSavedState.add((Fragment.SavedState)arrayOfParcelable[j]);
      }
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if (str.startsWith("f"))
        {
          int i = Integer.parseInt(str.substring(1));
          Fragment localFragment = this.mFragmentManager.getFragment(localBundle, str);
          if (localFragment != null)
          {
            setItemVisible(localFragment, false);
            this.mFragments.put(i, localFragment);
          }
          else
          {
            Log.w("FragmentStatePagerAdapter", "Bad fragment at key " + str);
          }
        }
      }
    }
  }

  public Parcelable saveState()
  {
    boolean bool = this.mEnableSavedStates;
    Bundle localBundle = null;
    if (bool)
    {
      int k = this.mSavedState.size();
      localBundle = null;
      if (k > 0)
      {
        localBundle = new Bundle();
        Fragment.SavedState[] arrayOfSavedState = new Fragment.SavedState[this.mSavedState.size()];
        this.mSavedState.toArray(arrayOfSavedState);
        localBundle.putParcelableArray("states", arrayOfSavedState);
      }
    }
    for (int i = 0; i < this.mFragments.size(); i++)
    {
      int j = this.mFragments.keyAt(i);
      Fragment localFragment = (Fragment)this.mFragments.valueAt(i);
      if (localBundle == null)
        localBundle = new Bundle();
      String str = "f" + j;
      this.mFragmentManager.putFragment(localBundle, str, localFragment);
    }
    return localBundle;
  }

  public void setItemVisible(Fragment paramFragment, boolean paramBoolean)
  {
    FragmentCompat.setMenuVisibility(paramFragment, paramBoolean);
    FragmentCompat.setUserVisibleHint(paramFragment, paramBoolean);
  }

  public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    Fragment localFragment = (Fragment)paramObject;
    if (localFragment != this.mCurrentPrimaryItem)
    {
      if (this.mCurrentPrimaryItem != null)
        setItemVisible(this.mCurrentPrimaryItem, false);
      if (localFragment != null)
        setItemVisible(localFragment, true);
      this.mCurrentPrimaryItem = localFragment;
    }
  }

  public void startUpdate(ViewGroup paramViewGroup)
  {
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.FragmentStatePagerAdapter2
 * JD-Core Version:    0.6.2
 */