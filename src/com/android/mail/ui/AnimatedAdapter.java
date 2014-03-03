package com.android.mail.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import com.android.mail.browse.ConversationCursor;
import com.android.mail.browse.ConversationItemView;
import com.android.mail.browse.ConversationItemViewCoordinates;
import com.android.mail.browse.SwipeableConversationItemView;
import com.android.mail.providers.Account;
import com.android.mail.providers.AccountObserver;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.Settings;
import com.android.mail.providers.UIProvider;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class AnimatedAdapter extends SimpleCursorAdapter
  implements Animator.AnimatorListener
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private Account mAccount;
  private final AccountObserver mAccountListener = new AccountObserver()
  {
    public void onChanged(Account paramAnonymousAccount)
    {
      AnimatedAdapter.this.setAccount(paramAnonymousAccount);
      AnimatedAdapter.this.notifyDataSetChanged();
    }
  };
  private ControllableActivity mActivity;
  private final HashMap<Long, SwipeableConversationItemView> mAnimatingViews = new HashMap();
  private final ConversationSelectionSet mBatchConversations;
  private final Context mContext;
  private final HashSet<Long> mDeletingItems = new HashSet();
  private final HashMap<Long, LeaveBehindItem> mFadeLeaveBehindItems = new HashMap();
  private Folder mFolder;
  private View mFooter;
  private final ArrayList<Long> mLastDeletingItems = new ArrayList();
  private LeaveBehindItem mLeaveBehindItem;
  private final SwipeableListView mListView;
  private SwipeableListView.ListItemsRemovedListener mPendingDestruction;
  private boolean mPriorityMarkersEnabled;
  private final SwipeableListView.ListItemsRemovedListener mRefreshAction = new SwipeableListView.ListItemsRemovedListener()
  {
    public void onListItemsRemoved()
    {
      AnimatedAdapter.this.notifyDataSetChanged();
    }
  };
  private boolean mShowFooter;
  private final HashSet<Long> mSwipeDeletingItems = new HashSet();
  private boolean mSwipeEnabled;
  private final HashSet<Long> mSwipeUndoingItems = new HashSet();
  private final HashSet<Long> mUndoingItems = new HashSet();

  public AnimatedAdapter(Context paramContext, int paramInt, ConversationCursor paramConversationCursor, ConversationSelectionSet paramConversationSelectionSet, ControllableActivity paramControllableActivity, SwipeableListView paramSwipeableListView)
  {
    super(paramContext, paramInt, paramConversationCursor, UIProvider.CONVERSATION_PROJECTION, null, 0);
    this.mContext = paramContext;
    this.mBatchConversations = paramConversationSelectionSet;
    setAccount(this.mAccountListener.initialize(paramControllableActivity.getAccountController()));
    this.mActivity = paramControllableActivity;
    this.mShowFooter = false;
    this.mListView = paramSwipeableListView;
  }

  private void delete(Collection<Conversation> paramCollection, SwipeableListView.ListItemsRemovedListener paramListItemsRemovedListener, HashSet<Long> paramHashSet)
  {
    this.mLastDeletingItems.clear();
    this.mUndoingItems.clear();
    int i = this.mListView.getFirstVisiblePosition();
    int j = this.mListView.getLastVisiblePosition();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Conversation localConversation = (Conversation)localIterator.next();
      if ((localConversation.position >= i) && (localConversation.position <= j))
      {
        this.mLastDeletingItems.add(Long.valueOf(localConversation.id));
        paramHashSet.add(Long.valueOf(localConversation.id));
      }
    }
    if (paramHashSet.isEmpty())
      paramListItemsRemovedListener.onListItemsRemoved();
    while (true)
    {
      notifyDataSetChanged();
      return;
      performAndSetNextAction(paramListItemsRemovedListener);
    }
  }

  private View getDeletingView(int paramInt, Conversation paramConversation, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    paramConversation.position = paramInt;
    SwipeableConversationItemView localSwipeableConversationItemView = (SwipeableConversationItemView)this.mAnimatingViews.get(Long.valueOf(paramConversation.id));
    if (localSwipeableConversationItemView == null)
    {
      localSwipeableConversationItemView = newConversationItemView(paramInt, paramViewGroup, paramConversation);
      localSwipeableConversationItemView.startDeleteAnimation(this, paramBoolean);
    }
    return localSwipeableConversationItemView;
  }

  private LeaveBehindItem getFadeLeaveBehindItem(int paramInt, Conversation paramConversation)
  {
    return (LeaveBehindItem)this.mFadeLeaveBehindItems.get(Long.valueOf(paramConversation.id));
  }

  private LeaveBehindItem getLeaveBehindItem(Conversation paramConversation)
  {
    return this.mLeaveBehindItem;
  }

  private View getUndoingView(int paramInt, Conversation paramConversation, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    paramConversation.position = paramInt;
    SwipeableConversationItemView localSwipeableConversationItemView = (SwipeableConversationItemView)this.mAnimatingViews.get(Long.valueOf(paramConversation.id));
    if (localSwipeableConversationItemView == null)
    {
      localSwipeableConversationItemView = newConversationItemView(paramInt, paramViewGroup, paramConversation);
      localSwipeableConversationItemView.startUndoAnimation(this.mActivity.getViewMode(), this, paramBoolean);
    }
    return localSwipeableConversationItemView;
  }

  private boolean hasFadeLeaveBehinds()
  {
    return !this.mFadeLeaveBehindItems.isEmpty();
  }

  private boolean hasLeaveBehinds()
  {
    return this.mLeaveBehindItem != null;
  }

  private boolean isPositionDeleting(long paramLong)
  {
    return this.mDeletingItems.contains(Long.valueOf(paramLong));
  }

  private boolean isPositionFadeLeaveBehind(Conversation paramConversation)
  {
    return (hasFadeLeaveBehinds()) && (this.mFadeLeaveBehindItems.containsKey(Long.valueOf(paramConversation.id))) && (paramConversation.isMostlyDead());
  }

  private boolean isPositionLeaveBehind(Conversation paramConversation)
  {
    return (hasLeaveBehinds()) && (this.mLeaveBehindItem.getConversationId() == paramConversation.id) && (paramConversation.isMostlyDead());
  }

  private boolean isPositionSwipeDeleting(long paramLong)
  {
    return this.mSwipeDeletingItems.contains(Long.valueOf(paramLong));
  }

  private boolean isPositionUndoing(long paramLong)
  {
    return this.mUndoingItems.contains(Long.valueOf(paramLong));
  }

  private boolean isPositionUndoingSwipe(long paramLong)
  {
    return this.mSwipeUndoingItems.contains(Long.valueOf(paramLong));
  }

  private SwipeableConversationItemView newConversationItemView(int paramInt, ViewGroup paramViewGroup, Conversation paramConversation)
  {
    SwipeableConversationItemView localSwipeableConversationItemView = (SwipeableConversationItemView)super.getView(paramInt, null, paramViewGroup);
    localSwipeableConversationItemView.reset();
    ControllableActivity localControllableActivity = this.mActivity;
    ConversationSelectionSet localConversationSelectionSet = this.mBatchConversations;
    Folder localFolder = this.mFolder;
    if (this.mAccount != null);
    for (boolean bool = this.mAccount.settings.hideCheckboxes; ; bool = false)
    {
      localSwipeableConversationItemView.bind(paramConversation, localControllableActivity, localConversationSelectionSet, localFolder, bool, this.mSwipeEnabled, this.mPriorityMarkersEnabled, this);
      this.mAnimatingViews.put(Long.valueOf(paramConversation.id), localSwipeableConversationItemView);
      return localSwipeableConversationItemView;
    }
  }

  private final void performAndSetNextAction(SwipeableListView.ListItemsRemovedListener paramListItemsRemovedListener)
  {
    if (this.mPendingDestruction != null)
      this.mPendingDestruction.onListItemsRemoved();
    this.mPendingDestruction = paramListItemsRemovedListener;
  }

  private final void setAccount(Account paramAccount)
  {
    this.mAccount = paramAccount;
    this.mPriorityMarkersEnabled = this.mAccount.settings.priorityArrowsEnabled;
    this.mSwipeEnabled = this.mAccount.supportsCapability(16384);
  }

  private void updateAnimatingConversationItems(Object paramObject, HashSet<Long> paramHashSet)
  {
    if ((!paramHashSet.isEmpty()) && ((paramObject instanceof ConversationItemView)))
    {
      long l = ((ConversationItemView)paramObject).getConversation().id;
      paramHashSet.remove(Long.valueOf(l));
      this.mAnimatingViews.remove(Long.valueOf(l));
      if (paramHashSet.isEmpty())
      {
        performAndSetNextAction(null);
        notifyDataSetChanged();
      }
    }
  }

  public void addFooter(View paramView)
  {
    this.mFooter = paramView;
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    if (!(paramView instanceof SwipeableConversationItemView))
      return;
    SwipeableConversationItemView localSwipeableConversationItemView = (SwipeableConversationItemView)paramView;
    ControllableActivity localControllableActivity = this.mActivity;
    ConversationSelectionSet localConversationSelectionSet = this.mBatchConversations;
    Folder localFolder = this.mFolder;
    if (this.mAccount != null);
    for (boolean bool = this.mAccount.settings.hideCheckboxes; ; bool = false)
    {
      localSwipeableConversationItemView.bind(paramCursor, localControllableActivity, localConversationSelectionSet, localFolder, bool, this.mSwipeEnabled, this.mPriorityMarkersEnabled, this);
      return;
    }
  }

  public void clearLeaveBehind(long paramLong)
  {
    if ((hasLeaveBehinds()) && (this.mLeaveBehindItem.getConversationId() == paramLong))
    {
      this.mLeaveBehindItem = null;
      return;
    }
    if (hasFadeLeaveBehinds())
    {
      this.mFadeLeaveBehindItems.remove(Long.valueOf(paramLong));
      return;
    }
    LogUtils.d(LOG_TAG, "Trying to clear a non-existant leave behind", new Object[0]);
  }

  public void commitLeaveBehindItems(boolean paramBoolean)
  {
    boolean bool = hasLeaveBehinds();
    int i = 0;
    if (bool)
    {
      if (!paramBoolean)
        break label75;
      this.mLeaveBehindItem.dismiss();
    }
    while (true)
    {
      i = 1;
      if ((!hasFadeLeaveBehinds()) || (paramBoolean))
        break label99;
      Iterator localIterator = this.mFadeLeaveBehindItems.values().iterator();
      while (localIterator.hasNext())
        ((LeaveBehindItem)localIterator.next()).commit();
      label75: this.mLeaveBehindItem.commit();
      this.mLeaveBehindItem = null;
    }
    this.mFadeLeaveBehindItems.clear();
    i = 1;
    label99: if (!this.mLastDeletingItems.isEmpty())
    {
      this.mLastDeletingItems.clear();
      i = 1;
    }
    if (i != 0)
      notifyDataSetChanged();
  }

  public View createConversationItemView(SwipeableConversationItemView paramSwipeableConversationItemView, Context paramContext, Conversation paramConversation)
  {
    if (paramSwipeableConversationItemView == null)
      paramSwipeableConversationItemView = new SwipeableConversationItemView(paramContext, this.mAccount.name);
    ControllableActivity localControllableActivity = this.mActivity;
    ConversationSelectionSet localConversationSelectionSet = this.mBatchConversations;
    Folder localFolder = this.mFolder;
    if (this.mAccount != null);
    for (boolean bool1 = this.mAccount.settings.hideCheckboxes; ; bool1 = false)
    {
      boolean bool2 = this.mSwipeEnabled;
      boolean bool3 = this.mPriorityMarkersEnabled;
      paramSwipeableConversationItemView.bind(paramConversation, localControllableActivity, localConversationSelectionSet, localFolder, bool1, bool2, bool3, this);
      return paramSwipeableConversationItemView;
    }
  }

  public void delete(Collection<Conversation> paramCollection, SwipeableListView.ListItemsRemovedListener paramListItemsRemovedListener)
  {
    delete(paramCollection, paramListItemsRemovedListener, this.mDeletingItems);
  }

  public final void destroy()
  {
    swapCursor(null);
    this.mAccountListener.unregisterAndDestroy();
  }

  public void fadeOutLeaveBehindItems()
  {
    int i = this.mListView.getFirstVisiblePosition();
    int j = this.mListView.getLastVisiblePosition();
    Conversation localConversation;
    if (hasLeaveBehinds())
    {
      localConversation = this.mLeaveBehindItem.getData();
      if ((localConversation.position < i) || (localConversation.position > j))
        break label96;
      this.mFadeLeaveBehindItems.put(Long.valueOf(localConversation.id), this.mLeaveBehindItem);
    }
    while (true)
    {
      clearLeaveBehind(localConversation.id);
      if (!this.mLastDeletingItems.isEmpty())
        this.mLastDeletingItems.clear();
      notifyDataSetChanged();
      return;
      label96: this.mLeaveBehindItem.commit();
    }
  }

  public ConversationCursor getConversationCursor()
  {
    return (ConversationCursor)getCursor();
  }

  public int getCount()
  {
    int i = super.getCount();
    if (this.mShowFooter)
      i++;
    return i;
  }

  public Object getItem(int paramInt)
  {
    if ((this.mShowFooter) && (paramInt == super.getCount()))
      return this.mFooter;
    return super.getItem(paramInt);
  }

  public long getItemId(int paramInt)
  {
    if ((this.mShowFooter) && (paramInt == super.getCount()))
      return -1L;
    return super.getItemId(paramInt);
  }

  public int getItemViewType(int paramInt)
  {
    if ((this.mShowFooter) && (paramInt == super.getCount()))
      return 1;
    if ((hasLeaveBehinds()) || (isAnimating()))
      return -1;
    return 0;
  }

  public SwipeableListView getListView()
  {
    return this.mListView;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if ((this.mShowFooter) && (paramInt == super.getCount()))
      return this.mFooter;
    ConversationCursor localConversationCursor = (ConversationCursor)getItem(paramInt);
    Conversation localConversation = new Conversation(localConversationCursor);
    if (isPositionUndoing(localConversation.id))
      return getUndoingView(paramInt, localConversation, paramViewGroup, false);
    if (isPositionUndoingSwipe(localConversation.id))
      return getUndoingView(paramInt, localConversation, paramViewGroup, true);
    if (isPositionDeleting(localConversation.id))
      return getDeletingView(paramInt, localConversation, paramViewGroup, false);
    if (isPositionSwipeDeleting(localConversation.id))
      return getDeletingView(paramInt, localConversation, paramViewGroup, true);
    if ((hasFadeLeaveBehinds()) && (isPositionFadeLeaveBehind(localConversation)))
    {
      LeaveBehindItem localLeaveBehindItem2 = getFadeLeaveBehindItem(paramInt, localConversation);
      localLeaveBehindItem2.startAnimation(this.mActivity.getViewMode(), this);
      return localLeaveBehindItem2;
    }
    if ((hasLeaveBehinds()) && (isPositionLeaveBehind(localConversation)))
    {
      LeaveBehindItem localLeaveBehindItem1 = getLeaveBehindItem(localConversation);
      if (hasFadeLeaveBehinds())
        localLeaveBehindItem1.showTextImmediately();
      while (true)
      {
        return localLeaveBehindItem1;
        localLeaveBehindItem1.startFadeInAnimation();
      }
    }
    if ((paramView != null) && (!(paramView instanceof SwipeableConversationItemView)))
    {
      LogUtils.w(LOG_TAG, "Incorrect convert view received; nulling it out", new Object[0]);
      paramView = newView(this.mContext, localConversationCursor, paramViewGroup);
    }
    while (true)
    {
      return createConversationItemView((SwipeableConversationItemView)paramView, this.mContext, localConversation);
      if (paramView != null)
        ((SwipeableConversationItemView)paramView).reset();
    }
  }

  public int getViewTypeCount()
  {
    return 5;
  }

  public boolean hasStableIds()
  {
    return true;
  }

  public void hideFooter()
  {
    setFooterVisibility(false);
  }

  public boolean isAnimating()
  {
    return (!this.mUndoingItems.isEmpty()) || (!this.mSwipeUndoingItems.isEmpty()) || (hasFadeLeaveBehinds()) || (!this.mDeletingItems.isEmpty()) || (!this.mSwipeDeletingItems.isEmpty());
  }

  public boolean isEnabled(int paramInt)
  {
    return (!isPositionDeleting(paramInt)) && (!isPositionUndoing(paramInt));
  }

  public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    return new SwipeableConversationItemView(paramContext, this.mAccount.name);
  }

  public void onAnimationCancel(Animator paramAnimator)
  {
    onAnimationEnd(paramAnimator);
  }

  public void onAnimationEnd(Animator paramAnimator)
  {
    if ((paramAnimator instanceof AnimatorSet));
    for (Object localObject = ((ObjectAnimator)((AnimatorSet)paramAnimator).getChildAnimations().get(0)).getTarget(); ; localObject = ((ObjectAnimator)paramAnimator).getTarget())
    {
      updateAnimatingConversationItems(localObject, this.mSwipeDeletingItems);
      updateAnimatingConversationItems(localObject, this.mDeletingItems);
      updateAnimatingConversationItems(localObject, this.mSwipeUndoingItems);
      updateAnimatingConversationItems(localObject, this.mUndoingItems);
      if ((hasFadeLeaveBehinds()) && ((localObject instanceof LeaveBehindItem)))
      {
        LeaveBehindItem localLeaveBehindItem = (LeaveBehindItem)localObject;
        clearLeaveBehind(localLeaveBehindItem.getConversationId());
        localLeaveBehindItem.commit();
        notifyDataSetChanged();
      }
      if (!isAnimating())
        this.mActivity.onAnimationEnd(this);
      return;
    }
  }

  public void onAnimationRepeat(Animator paramAnimator)
  {
  }

  public void onAnimationStart(Animator paramAnimator)
  {
    if (!this.mUndoingItems.isEmpty())
    {
      this.mDeletingItems.clear();
      this.mLastDeletingItems.clear();
      this.mSwipeDeletingItems.clear();
      return;
    }
    this.mUndoingItems.clear();
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    if (paramBundle.containsKey("last_deleting_items"))
    {
      long[] arrayOfLong = paramBundle.getLongArray("last_deleting_items");
      for (int i = 0; i < arrayOfLong.length; i++)
        this.mLastDeletingItems.add(Long.valueOf(arrayOfLong[i]));
    }
    if (paramBundle.containsKey("leave_behind_item"))
    {
      LeaveBehindData localLeaveBehindData = (LeaveBehindData)paramBundle.getParcelable("leave_behind_item");
      this.mLeaveBehindItem = setupLeaveBehind(localLeaveBehindData.data, localLeaveBehindData.op, localLeaveBehindData.data.position);
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    long[] arrayOfLong = new long[this.mLastDeletingItems.size()];
    for (int i = 0; i < arrayOfLong.length; i++)
      arrayOfLong[i] = ((Long)this.mLastDeletingItems.get(i)).longValue();
    paramBundle.putLongArray("last_deleting_items", arrayOfLong);
    if (hasLeaveBehinds())
      paramBundle.putParcelable("leave_behind_item", this.mLeaveBehindItem.getLeaveBehindData());
  }

  public void setFolder(Folder paramFolder)
  {
    this.mFolder = paramFolder;
  }

  public void setFooterVisibility(boolean paramBoolean)
  {
    if (this.mShowFooter != paramBoolean)
    {
      this.mShowFooter = paramBoolean;
      notifyDataSetChanged();
    }
  }

  public void setSwipeUndo(boolean paramBoolean)
  {
    if ((paramBoolean) && (!this.mLastDeletingItems.isEmpty()))
    {
      this.mSwipeUndoingItems.addAll(this.mLastDeletingItems);
      this.mLastDeletingItems.clear();
      notifyDataSetChanged();
      performAndSetNextAction(this.mRefreshAction);
    }
  }

  public void setUndo(boolean paramBoolean)
  {
    if ((paramBoolean) && (!this.mLastDeletingItems.isEmpty()))
    {
      this.mUndoingItems.addAll(this.mLastDeletingItems);
      this.mLastDeletingItems.clear();
      notifyDataSetChanged();
      performAndSetNextAction(this.mRefreshAction);
    }
  }

  public LeaveBehindItem setupLeaveBehind(Conversation paramConversation, ToastBarOperation paramToastBarOperation, int paramInt)
  {
    fadeOutLeaveBehindItems();
    boolean bool = ConversationItemViewCoordinates.isWideMode(ConversationItemViewCoordinates.getMode(this.mContext, this.mActivity.getViewMode()));
    LayoutInflater localLayoutInflater = LayoutInflater.from(this.mContext);
    if (bool);
    for (int i = 2130968675; ; i = 2130968673)
    {
      LeaveBehindItem localLeaveBehindItem = (LeaveBehindItem)localLayoutInflater.inflate(i, null);
      localLeaveBehindItem.bindOperations(paramInt, this.mAccount, this, paramToastBarOperation, paramConversation, this.mFolder);
      this.mLeaveBehindItem = localLeaveBehindItem;
      this.mLastDeletingItems.add(Long.valueOf(paramConversation.id));
      return localLeaveBehindItem;
    }
  }

  public void swipeDelete(Collection<Conversation> paramCollection, SwipeableListView.ListItemsRemovedListener paramListItemsRemovedListener)
  {
    delete(paramCollection, paramListItemsRemovedListener, this.mSwipeDeletingItems);
  }

  public static abstract interface Listener
  {
    public abstract void onAnimationEnd(AnimatedAdapter paramAnimatedAdapter);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.AnimatedAdapter
 * JD-Core Version:    0.6.2
 */