package com.android.mail.browse;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.ui.AbstractConversationViewFragment;
import com.android.mail.ui.ActivityController;
import com.android.mail.ui.ConversationViewFragment;
import com.android.mail.utils.FragmentStatePagerAdapter2;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;

public class ConversationPagerAdapter extends FragmentStatePagerAdapter2
  implements ViewPager.OnPageChangeListener
{
  private static final String BUNDLE_DETACHED_MODE = ConversationPagerAdapter.class.getName() + "-detachedmode";
  private static final String LOG_TAG = LogTag.getLogTag();
  private final Account mAccount;
  private final Bundle mCommonFragmentArgs;
  private ActivityController mController;
  private boolean mDetachedMode = false;
  private final Folder mFolder;
  private final DataSetObserver mFolderObserver = new FolderObserver(null);
  private final Conversation mInitialConversation;
  private final DataSetObserver mListObserver = new ListObserver(null);
  private ViewPager mPager;
  private Resources mResources;
  private boolean mSafeToNotify;
  private boolean mSanitizedHtml;
  private boolean mSingletonMode = true;

  public ConversationPagerAdapter(Resources paramResources, FragmentManager paramFragmentManager, Account paramAccount, Folder paramFolder, Conversation paramConversation)
  {
    super(paramFragmentManager, false);
    this.mResources = paramResources;
    this.mCommonFragmentArgs = AbstractConversationViewFragment.makeBasicArgs(paramAccount, paramFolder);
    this.mInitialConversation = paramConversation;
    this.mAccount = paramAccount;
    this.mFolder = paramFolder;
    this.mSanitizedHtml = this.mAccount.supportsCapability(128);
  }

  private AbstractConversationViewFragment getConversationViewFragment(Conversation paramConversation)
  {
    if (this.mSanitizedHtml)
      return ConversationViewFragment.newInstance(this.mCommonFragmentArgs, paramConversation);
    return SecureConversationViewFragment.newInstance(this.mCommonFragmentArgs, paramConversation);
  }

  private Cursor getCursor()
  {
    if (this.mController == null)
    {
      LogUtils.i(LOG_TAG, "Pager adapter has a null controller. If the conversation view is going away, this is fine.  Otherwise, the state is inconsistent", new Object[0]);
      return null;
    }
    return this.mController.getConversationListCursor();
  }

  private Conversation getDefaultConversation()
  {
    if (this.mController != null);
    for (Conversation localConversation = this.mController.getCurrentConversation(); ; localConversation = null)
    {
      if (localConversation == null)
        localConversation = this.mInitialConversation;
      return localConversation;
    }
  }

  public void finishUpdate(ViewGroup paramViewGroup)
  {
    super.finishUpdate(paramViewGroup);
    this.mSafeToNotify = true;
  }

  public int getConversationPosition(Conversation paramConversation)
  {
    int i = -2;
    if (isPagingDisabled())
      if (getCursor() != null);
    Cursor localCursor;
    boolean bool;
    do
    {
      do
      {
        return i;
        if (paramConversation != getDefaultConversation())
        {
          LogUtils.d(LOG_TAG, "unable to find conversation in singleton mode. c=%s", new Object[] { paramConversation });
          return i;
        }
        return 0;
        localCursor = getCursor();
      }
      while ((localCursor == null) || (paramConversation == null));
      bool = Utils.disableConversationCursorNetworkAccess(localCursor);
      i = -2;
      int j = -1;
      long l;
      do
      {
        j++;
        if (!localCursor.moveToPosition(j))
          break;
        l = localCursor.getLong(0);
      }
      while (paramConversation.id != l);
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramConversation.subject;
      arrayOfObject[1] = Integer.valueOf(j);
      LogUtils.d(str, "pager adapter found repositioned convo '%s' at pos=%d", arrayOfObject);
      i = j;
    }
    while (!bool);
    Utils.enableConversationCursorNetworkAccess(localCursor);
    return i;
  }

  public int getCount()
  {
    if (isPagingDisabled())
    {
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = getCursor();
      LogUtils.d(str, "IN CPA.getCount, returning 1 (effective singleton). cursor=%s", arrayOfObject);
      return 1;
    }
    Cursor localCursor = getCursor();
    if (localCursor == null)
      return 0;
    return localCursor.getCount();
  }

  public Fragment getItem(int paramInt)
  {
    Conversation localConversation;
    if (isPagingDisabled())
    {
      if (paramInt != 0)
      {
        String str4 = LOG_TAG;
        Object[] arrayOfObject4 = new Object[1];
        arrayOfObject4[0] = Integer.valueOf(paramInt);
        LogUtils.wtf(str4, "pager cursor is null and position is non-zero: %d", arrayOfObject4);
      }
      localConversation = getDefaultConversation();
    }
    for (localConversation.position = 0; ; localConversation.position = paramInt)
    {
      AbstractConversationViewFragment localAbstractConversationViewFragment = getConversationViewFragment(localConversation);
      String str1 = LOG_TAG;
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = localAbstractConversationViewFragment;
      arrayOfObject1[1] = localConversation.subject;
      LogUtils.d(str1, "IN PagerAdapter.getItem, frag=%s subj=%s", arrayOfObject1);
      return localAbstractConversationViewFragment;
      Cursor localCursor = getCursor();
      if (localCursor == null)
      {
        String str3 = LOG_TAG;
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = Integer.valueOf(paramInt);
        LogUtils.wtf(str3, "unable to get ConversationCursor, pos=%d", arrayOfObject3);
        return null;
      }
      if (!localCursor.moveToPosition(paramInt))
      {
        String str2 = LOG_TAG;
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = Integer.valueOf(paramInt);
        arrayOfObject2[1] = localCursor;
        LogUtils.wtf(str2, "unable to seek to ConversationCursor pos=%d (%s)", arrayOfObject2);
        return null;
      }
      localConversation = new Conversation(localCursor);
    }
  }

  public int getItemPosition(Object paramObject)
  {
    if (!(paramObject instanceof AbstractConversationViewFragment))
      LogUtils.wtf(LOG_TAG, "getItemPosition received unexpected item: %s", new Object[] { paramObject });
    return getConversationPosition(((AbstractConversationViewFragment)paramObject).getConversation());
  }

  public CharSequence getPageTitle(int paramInt)
  {
    int i = this.mPager.getCurrentItem();
    if (isPagingDisabled())
      return null;
    if (paramInt == i)
    {
      int k = getCount();
      if (this.mController != null)
      {
        Folder localFolder = this.mController.getFolder();
        if ((localFolder != null) && (localFolder.totalCount > k))
          k = localFolder.totalCount;
      }
      Resources localResources2 = this.mResources;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(paramInt + 1);
      arrayOfObject[1] = Integer.valueOf(k);
      return localResources2.getString(2131427504, arrayOfObject);
    }
    Resources localResources1 = this.mResources;
    if (paramInt < i);
    for (int j = 2131427505; ; j = 2131427506)
      return localResources1.getString(j);
  }

  public boolean isDetached()
  {
    return this.mDetachedMode;
  }

  public boolean isPagingDisabled()
  {
    return (this.mSingletonMode) || (this.mDetachedMode) || (getCursor() == null);
  }

  public boolean isSingletonMode()
  {
    return this.mSingletonMode;
  }

  public boolean matches(Account paramAccount, Folder paramFolder)
  {
    return (this.mAccount != null) && (this.mFolder != null) && (this.mAccount.matches(paramAccount)) && (this.mFolder.equals(paramFolder));
  }

  public void notifyDataSetChanged()
  {
    if (!this.mSafeToNotify)
    {
      LogUtils.d(LOG_TAG, "IN PagerAdapter.notifyDataSetChanged, ignoring unsafe update", new Object[0]);
      return;
    }
    int i;
    if (this.mController != null)
    {
      Conversation localConversation1 = this.mController.getCurrentConversation();
      i = getConversationPosition(localConversation1);
      if ((i != -2) || (getCursor() == null) || (localConversation1 == null))
        break label102;
      this.mDetachedMode = true;
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localConversation1.uri;
      LogUtils.i(str, "CPA: current conv is gone, reverting to detached mode. c=%s", arrayOfObject);
    }
    while (true)
    {
      super.notifyDataSetChanged();
      return;
      label102: AbstractConversationViewFragment localAbstractConversationViewFragment = (AbstractConversationViewFragment)getFragmentAt(i);
      Cursor localCursor = getCursor();
      if ((localAbstractConversationViewFragment != null) && (localCursor.moveToPosition(i)) && (localAbstractConversationViewFragment.isUserVisible()))
      {
        Conversation localConversation2 = new Conversation(localCursor);
        localConversation2.position = i;
        localAbstractConversationViewFragment.onConversationUpdated(localConversation2);
        this.mController.setCurrentConversation(localConversation2);
      }
    }
  }

  public void onPageScrollStateChanged(int paramInt)
  {
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
    if (this.mController == null);
    Cursor localCursor;
    do
    {
      return;
      localCursor = getCursor();
    }
    while ((localCursor == null) || (!localCursor.moveToPosition(paramInt)));
    Conversation localConversation = new Conversation(localCursor);
    localConversation.position = paramInt;
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = localConversation.subject;
    LogUtils.d(str, "pager adapter setting current conv: %s", arrayOfObject);
    this.mController.setCurrentConversation(localConversation);
  }

  public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader)
  {
    LogUtils.d(LOG_TAG, "IN PagerAdapter.restoreState. this=%s", new Object[] { this });
    super.restoreState(paramParcelable, paramClassLoader);
    if (paramParcelable != null)
    {
      Bundle localBundle = (Bundle)paramParcelable;
      localBundle.setClassLoader(paramClassLoader);
      this.mDetachedMode = localBundle.getBoolean(BUNDLE_DETACHED_MODE);
    }
  }

  public Parcelable saveState()
  {
    LogUtils.d(LOG_TAG, "IN PagerAdapter.saveState. this=%s", new Object[] { this });
    Bundle localBundle = (Bundle)super.saveState();
    if (localBundle == null)
      localBundle = new Bundle();
    localBundle.putBoolean(BUNDLE_DETACHED_MODE, this.mDetachedMode);
    return localBundle;
  }

  public void setActivityController(ActivityController paramActivityController)
  {
    if (this.mController != null)
    {
      this.mController.unregisterConversationListObserver(this.mListObserver);
      this.mController.unregisterFolderObserver(this.mFolderObserver);
    }
    this.mController = paramActivityController;
    if (this.mController != null)
    {
      this.mController.registerConversationListObserver(this.mListObserver);
      this.mController.registerFolderObserver(this.mFolderObserver);
      notifyDataSetChanged();
    }
  }

  public void setItemVisible(Fragment paramFragment, boolean paramBoolean)
  {
    super.setItemVisible(paramFragment, paramBoolean);
    ((AbstractConversationViewFragment)paramFragment).setExtraUserVisibleHint(paramBoolean);
  }

  public void setPager(ViewPager paramViewPager)
  {
    if (this.mPager != null)
      this.mPager.setOnPageChangeListener(null);
    this.mPager = paramViewPager;
    if (this.mPager != null)
      this.mPager.setOnPageChangeListener(this);
  }

  public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    arrayOfObject[1] = paramObject;
    LogUtils.d(str, "IN PagerAdapter.setPrimaryItem, pos=%d, frag=%s", arrayOfObject);
    super.setPrimaryItem(paramViewGroup, paramInt, paramObject);
  }

  public void setSingletonMode(boolean paramBoolean)
  {
    if (this.mSingletonMode != paramBoolean)
    {
      this.mSingletonMode = paramBoolean;
      notifyDataSetChanged();
    }
  }

  public void startUpdate(ViewGroup paramViewGroup)
  {
    this.mSafeToNotify = false;
    super.startUpdate(paramViewGroup);
  }

  private class FolderObserver extends DataSetObserver
  {
    private FolderObserver()
    {
    }

    public void onChanged()
    {
      ConversationPagerAdapter.this.notifyDataSetChanged();
    }
  }

  private class ListObserver extends DataSetObserver
  {
    private ListObserver()
    {
    }

    public void onChanged()
    {
      ConversationPagerAdapter.this.notifyDataSetChanged();
    }

    public void onInvalidated()
    {
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ConversationPagerAdapter
 * JD-Core Version:    0.6.2
 */