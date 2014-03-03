package com.android.mail.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import com.android.mail.browse.ConversationCursor;
import com.android.mail.browse.ConversationItemView;
import com.android.mail.browse.SwipeableConversationItemView;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class SwipeableListView extends ListView
  implements AbsListView.OnScrollListener, SwipeHelper.Callback
{
  public static final String LOG_TAG = LogTag.getLogTag();
  private ConversationSelectionSet mConvSelectionSet;
  private boolean mEnableSwipe = false;
  private Folder mFolder;
  private boolean mScrolling;
  private int mSwipeAction;
  private SwipeHelper mSwipeHelper;
  private ListItemSwipedListener mSwipedListener;

  public SwipeableListView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SwipeableListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, -1);
  }

  public SwipeableListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mSwipeHelper = new SwipeHelper(paramContext, 0, this, getResources().getDisplayMetrics().density, ViewConfiguration.get(paramContext).getScaledPagingTouchSlop());
    setOnScrollListener(this);
  }

  private AnimatedAdapter getAnimatedAdapter()
  {
    return (AnimatedAdapter)getAdapter();
  }

  public boolean canChildBeDismissed(SwipeableItemView paramSwipeableItemView)
  {
    return paramSwipeableItemView.canChildBeDismissed();
  }

  public void commitDestructiveActions(boolean paramBoolean)
  {
    AnimatedAdapter localAnimatedAdapter = getAnimatedAdapter();
    if (localAnimatedAdapter != null)
      localAnimatedAdapter.commitLeaveBehindItems(paramBoolean);
  }

  public void destroyItems(ArrayList<ConversationItemView> paramArrayList, ListItemsRemovedListener paramListItemsRemovedListener)
  {
    if ((paramArrayList == null) || (paramArrayList.size() == 0))
      return;
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      ConversationItemView localConversationItemView = (ConversationItemView)localIterator.next();
      if (localConversationItemView != null)
      {
        Conversation localConversation = localConversationItemView.getConversation();
        localConversation.position = findConversation(localConversationItemView, localConversation);
        localArrayList.add(localConversation);
      }
    }
    destroyItems(localArrayList, paramListItemsRemovedListener);
  }

  public void destroyItems(Collection<Conversation> paramCollection, ListItemsRemovedListener paramListItemsRemovedListener)
  {
    if (paramCollection == null)
      return;
    AnimatedAdapter localAnimatedAdapter = getAnimatedAdapter();
    if (localAnimatedAdapter == null)
    {
      LogUtils.e(LOG_TAG, "SwipeableListView.destroyItems: Cannot destroy: adapter is null.", new Object[0]);
      return;
    }
    localAnimatedAdapter.swipeDelete(paramCollection, paramListItemsRemovedListener);
  }

  public void dismissChild(ConversationItemView paramConversationItemView)
  {
    Context localContext = getContext();
    ToastBarOperation localToastBarOperation = new ToastBarOperation(1, this.mSwipeAction, 0, false);
    Conversation localConversation = paramConversationItemView.getConversation();
    paramConversationItemView.getConversation().position = findConversation(paramConversationItemView, localConversation);
    AnimatedAdapter localAnimatedAdapter = getAnimatedAdapter();
    if (localAnimatedAdapter == null)
      return;
    localAnimatedAdapter.setupLeaveBehind(localConversation, localToastBarOperation, localConversation.position);
    ConversationCursor localConversationCursor = (ConversationCursor)localAnimatedAdapter.getCursor();
    Collection localCollection = Conversation.listOf(localConversation);
    switch (this.mSwipeAction)
    {
    default:
    case 2131689752:
    case 2131689751:
    case 2131689753:
    }
    while (true)
    {
      if (this.mSwipedListener != null)
        this.mSwipedListener.onListItemSwiped(localCollection);
      localAnimatedAdapter.notifyDataSetChanged();
      if ((this.mConvSelectionSet == null) || (this.mConvSelectionSet.isEmpty()) || (!this.mConvSelectionSet.contains(localConversation)))
        break;
      this.mConvSelectionSet.toggle(null, localConversation);
      if ((localConversation.isMostlyDead()) || (!this.mConvSelectionSet.isEmpty()))
        break;
      commitDestructiveActions(true);
      return;
      FolderOperation localFolderOperation = new FolderOperation(this.mFolder, Boolean.valueOf(false));
      HashMap localHashMap = Folder.hashMapForFolders(localConversation.getRawFolders());
      localHashMap.remove(localFolderOperation.mFolder.uri);
      localConversation.setRawFolders(Folder.getSerializedFolderString(localHashMap.values()));
      localConversationCursor.mostlyDestructiveUpdate(localContext, Conversation.listOf(localConversation), "rawFolders", localConversation.getRawFoldersString());
      continue;
      localConversationCursor.mostlyArchive(localContext, localCollection);
      continue;
      localConversationCursor.mostlyDelete(localContext, localCollection);
    }
  }

  public void enableSwipe(boolean paramBoolean)
  {
    this.mEnableSwipe = paramBoolean;
  }

  public int findConversation(ConversationItemView paramConversationItemView, Conversation paramConversation)
  {
    int i = paramConversation.position;
    long l = paramConversation.id;
    if (i == -1);
    try
    {
      int k = getPositionForView(paramConversationItemView);
      i = k;
      if (i == -1)
      {
        j = 0;
        if (j < getChildCount())
        {
          View localView = getChildAt(j);
          if ((!(localView instanceof SwipeableConversationItemView)) || (((SwipeableConversationItemView)localView).getSwipeableItemView().getConversation().id != l))
            break label105;
          i = j;
        }
      }
      return i;
    }
    catch (Exception localException)
    {
      while (true)
      {
        int j;
        i = -1;
        LogUtils.w(LOG_TAG, "Exception finding position; using alternate strategy", new Object[0]);
        continue;
        label105: j++;
      }
    }
  }

  public View getChildAtPosition(MotionEvent paramMotionEvent)
  {
    int i = getChildCount();
    int j = (int)paramMotionEvent.getY();
    int k = 0;
    if (k < i)
    {
      Object localObject = getChildAt(k);
      if (((View)localObject).getVisibility() == 8);
      while ((j < ((View)localObject).getTop()) || (j > ((View)localObject).getBottom()))
      {
        k++;
        break;
      }
      if ((localObject instanceof SwipeableConversationItemView))
        localObject = ((SwipeableConversationItemView)localObject).getSwipeableItemView();
      return localObject;
    }
    return null;
  }

  public int getSwipeAction()
  {
    return this.mSwipeAction;
  }

  public void onBeginDrag(View paramView)
  {
    requestDisallowInterceptTouchEvent(true);
    boolean bool = paramView instanceof ConversationItemView;
    SwipeableConversationItemView localSwipeableConversationItemView = null;
    if (bool)
      localSwipeableConversationItemView = (SwipeableConversationItemView)paramView.getParent();
    if (localSwipeableConversationItemView != null)
    {
      localSwipeableConversationItemView.addBackground(getContext());
      localSwipeableConversationItemView.setBackgroundVisibility(0);
    }
  }

  public void onChildDismissed(SwipeableItemView paramSwipeableItemView)
  {
    if (paramSwipeableItemView != null)
      paramSwipeableItemView.dismiss();
  }

  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    float f1 = getResources().getDisplayMetrics().density;
    this.mSwipeHelper.setDensityScale(f1);
    float f2 = ViewConfiguration.get(getContext()).getScaledPagingTouchSlop();
    this.mSwipeHelper.setPagingTouchSlop(f2);
  }

  public void onDragCancelled(SwipeableItemView paramSwipeableItemView)
  {
    boolean bool = paramSwipeableItemView instanceof ConversationItemView;
    SwipeableConversationItemView localSwipeableConversationItemView = null;
    if (bool)
      localSwipeableConversationItemView = (SwipeableConversationItemView)((View)paramSwipeableItemView).getParent();
    if (localSwipeableConversationItemView != null)
      localSwipeableConversationItemView.removeBackground();
  }

  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = Boolean.valueOf(isLayoutRequested());
    arrayOfObject1[1] = Boolean.valueOf(getRootView().isLayoutRequested());
    LogUtils.d("MailBlankFragment", "START CLF-ListView.onFocusChanged layoutRequested=%s root.layoutRequested=%s", arrayOfObject1);
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    Error localError = new Error();
    Object[] arrayOfObject2 = new Object[2];
    arrayOfObject2[0] = Boolean.valueOf(isLayoutRequested());
    arrayOfObject2[1] = Boolean.valueOf(getRootView().isLayoutRequested());
    LogUtils.d("MailBlankFragment", localError, "FINISH CLF-ListView.onFocusChanged layoutRequested=%s root.layoutRequested=%s", arrayOfObject2);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mScrolling) || (!this.mEnableSwipe))
      return super.onInterceptTouchEvent(paramMotionEvent);
    return (this.mSwipeHelper.onInterceptTouchEvent(paramMotionEvent)) || (super.onInterceptTouchEvent(paramMotionEvent));
  }

  public void onScroll()
  {
    commitDestructiveActions(true);
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
    switch (paramInt)
    {
    default:
      this.mScrolling = true;
      return;
    case 0:
    }
    this.mScrolling = false;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mEnableSwipe)
      return (this.mSwipeHelper.onTouchEvent(paramMotionEvent)) || (super.onTouchEvent(paramMotionEvent));
    return super.onTouchEvent(paramMotionEvent);
  }

  public boolean performItemClick(View paramView, int paramInt, long paramLong)
  {
    boolean bool = super.performItemClick(paramView, paramInt, paramLong);
    commitDestructiveActions(true);
    return bool;
  }

  public void setCurrentFolder(Folder paramFolder)
  {
    this.mFolder = paramFolder;
  }

  public void setSelectionSet(ConversationSelectionSet paramConversationSelectionSet)
  {
    this.mConvSelectionSet = paramConversationSelectionSet;
  }

  public void setSwipeAction(int paramInt)
  {
    this.mSwipeAction = paramInt;
  }

  public void setSwipedListener(ListItemSwipedListener paramListItemSwipedListener)
  {
    this.mSwipedListener = paramListItemSwipedListener;
  }

  public static abstract interface ListItemSwipedListener
  {
    public abstract void onListItemSwiped(Collection<Conversation> paramCollection);
  }

  public static abstract interface ListItemsRemovedListener
  {
    public abstract void onListItemsRemoved();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.SwipeableListView
 * JD-Core Version:    0.6.2
 */