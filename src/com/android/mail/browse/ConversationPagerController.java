package com.android.mail.browse;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.support.v4.view.ViewPager;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.ui.ActivityController;
import com.android.mail.ui.RestrictedActivity;
import com.android.mail.ui.SubjectDisplayChanger;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;

public class ConversationPagerController
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private ActivityController mActivityController;
  private FragmentManager mFragmentManager;
  private boolean mInitialConversationLoading;
  private final DataSetObservable mLoadedObservable = new DataSetObservable();
  private ViewPager mPager;
  private ConversationPagerAdapter mPagerAdapter;
  private boolean mShown;
  private SubjectDisplayChanger mSubjectDisplayChanger;

  public ConversationPagerController(RestrictedActivity paramRestrictedActivity, ActivityController paramActivityController)
  {
    this.mFragmentManager = paramRestrictedActivity.getFragmentManager();
    this.mPager = ((ViewPager)paramRestrictedActivity.findViewById(2131689632));
    this.mActivityController = paramActivityController;
    this.mSubjectDisplayChanger = paramActivityController.getSubjectDisplayChanger();
    setupPageMargin(paramRestrictedActivity.getActivityContext());
  }

  private void cleanup()
  {
    if (this.mPagerAdapter != null)
    {
      this.mPagerAdapter.setActivityController(null);
      this.mPagerAdapter.setPager(null);
      this.mPagerAdapter = null;
    }
  }

  private void setupPageMargin(Context paramContext)
  {
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(new int[] { 16843284 });
    Drawable localDrawable = localTypedArray.getDrawable(0);
    localTypedArray.recycle();
    int i = paramContext.getResources().getDimensionPixelOffset(2131361834);
    InsetDrawable localInsetDrawable = new InsetDrawable(localDrawable, i, 0, i, 0);
    this.mPager.setPageMargin(localInsetDrawable.getIntrinsicWidth() + i * 2);
    this.mPager.setPageMarginDrawable(localInsetDrawable);
  }

  public void hide(boolean paramBoolean)
  {
    if (!this.mShown)
    {
      LogUtils.d(LOG_TAG, "IN CPC.hide, but already hidden", new Object[0]);
      return;
    }
    this.mShown = false;
    if (paramBoolean)
      this.mPager.setVisibility(8);
    this.mSubjectDisplayChanger.clearSubject();
    LogUtils.d(LOG_TAG, "IN CPC.hide, clearing adapter and unregistering list observer", new Object[0]);
    this.mPager.setAdapter(null);
    cleanup();
  }

  public boolean isInitialConversationLoading()
  {
    return this.mInitialConversationLoading;
  }

  public void onConversationSeen(Conversation paramConversation)
  {
    if (this.mPagerAdapter == null);
    do
    {
      return;
      if (this.mPagerAdapter.isSingletonMode())
      {
        LogUtils.i(LOG_TAG, "IN pager adapter, finished loading primary conversation, switching to cursor mode to load other conversations", new Object[0]);
        this.mPagerAdapter.setSingletonMode(false);
      }
    }
    while (!this.mInitialConversationLoading);
    this.mInitialConversationLoading = false;
    this.mLoadedObservable.notifyChanged();
  }

  public void onDestroy()
  {
    cleanup();
  }

  public void registerConversationLoadedObserver(DataSetObserver paramDataSetObserver)
  {
    this.mLoadedObservable.registerObserver(paramDataSetObserver);
  }

  public void show(Account paramAccount, Folder paramFolder, Conversation paramConversation, boolean paramBoolean)
  {
    this.mInitialConversationLoading = true;
    if (this.mShown)
    {
      LogUtils.d(LOG_TAG, "IN CPC.show, but already shown", new Object[0]);
      if ((this.mPagerAdapter != null) && (this.mPagerAdapter.matches(paramAccount, paramFolder)) && (!this.mPagerAdapter.isDetached()))
      {
        int j = this.mPagerAdapter.getConversationPosition(paramConversation);
        if (j >= 0)
        {
          this.mPager.setCurrentItem(j);
          return;
        }
      }
      cleanup();
    }
    if (paramBoolean)
      this.mPager.setVisibility(0);
    this.mPagerAdapter = new ConversationPagerAdapter(this.mPager.getResources(), this.mFragmentManager, paramAccount, paramFolder, paramConversation);
    this.mPagerAdapter.setSingletonMode(false);
    this.mPagerAdapter.setActivityController(this.mActivityController);
    this.mPagerAdapter.setPager(this.mPager);
    String str1 = LOG_TAG;
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = this.mPagerAdapter;
    LogUtils.d(str1, "IN CPC.show, adapter=%s", arrayOfObject1);
    String str2 = LOG_TAG;
    Object[] arrayOfObject2 = new Object[2];
    arrayOfObject2[0] = Integer.valueOf(this.mPagerAdapter.getCount());
    arrayOfObject2[1] = paramConversation.subject;
    LogUtils.d(str2, "init pager adapter, count=%d initial=%s", arrayOfObject2);
    this.mPager.setAdapter(this.mPagerAdapter);
    int i = this.mPagerAdapter.getConversationPosition(paramConversation);
    if (i >= 0)
    {
      String str3 = LOG_TAG;
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Integer.valueOf(i);
      LogUtils.d(str3, "*** pager fragment init pos=%d", arrayOfObject3);
      this.mPager.setCurrentItem(i);
    }
    this.mShown = true;
  }

  public void stopListening()
  {
    if (this.mPagerAdapter != null)
      this.mPagerAdapter.setActivityController(null);
  }

  public void unregisterConversationLoadedObserver(DataSetObserver paramDataSetObserver)
  {
    this.mLoadedObservable.unregisterObserver(paramDataSetObserver);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ConversationPagerController
 * JD-Core Version:    0.6.2
 */