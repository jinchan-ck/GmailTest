package com.google.android.common;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class SwipeySwitcher extends ViewGroup
{
  private static final float MOVE_THRESHOLD = 0.5F;
  private static final int VIEW_CACHE_SIZE = 3;
  static final int[] sOrderLeft = { 1, 2, 0 };
  static final int[] sOrderRight = { 1, 0, 2 };
  private Adapter mAdapter;
  ScrollRunnable mAnimateScrollRunnable = new ScrollRunnable();
  private boolean mAnimatingScroll;
  private boolean mCenterOnLayout = true;
  private boolean mClearAnimatingScroll;
  private Context mContext;
  private int mDownX;
  private boolean mDragging;
  private SimpleViewCache mFakes = new SimpleViewCache(new IMakeView()
  {
    public View makeView()
    {
      return new View(SwipeySwitcher.this.mContext);
    }
  });
  private GestureDetector mGestureDetector;
  private Handler mHandler = new Handler();
  float mLastTouchX;
  private int mNextSelection;
  private ObserverCallbacks mObserver;
  private SimpleViewCache mPlaceholders = new SimpleViewCache(new IMakeView()
  {
    public View makeView()
    {
      return new View(SwipeySwitcher.this.mContext);
    }
  });
  Runnable mRefreshRunnable = new Runnable()
  {
    public void run()
    {
      if (SwipeySwitcher.this.mAdapter == null);
      do
      {
        return;
        int i = 0;
        if (i < SwipeySwitcher.this.mViews.length)
        {
          SwipeySwitcher.IViewHolder localIViewHolder1 = SwipeySwitcher.this.mViews[i];
          if ((!SwipeySwitcher.this.mPlaceholders.memberOf(localIViewHolder1)) || (localIViewHolder1.getIntent() == null));
          while (true)
          {
            i++;
            break;
            int j = i + SwipeySwitcher.this.mSelection - 1;
            SwipeySwitcher.IViewHolder localIViewHolder2 = SwipeySwitcher.this.mAdapter.getView(SwipeySwitcher.this.wrap(j), SwipeySwitcher.this);
            if (localIViewHolder2 != null)
            {
              if (SwipeySwitcher.this.getChildCount() > i)
              {
                SwipeySwitcher.this.mViews[i].stop();
                SwipeySwitcher.this.removeViewAt(i);
              }
              SwipeySwitcher.this.installView(localIViewHolder2, i);
            }
          }
        }
      }
      while (SwipeySwitcher.this.mViews[1] == null);
      SwipeySwitcher.this.mViews[1].focus();
    }
  };
  final HashMap<String, IViewHolder> mReuseCache = new HashMap();
  private Scroller mScroller;
  private int mSelection;
  private int mTouchSlop;
  private IViewHolder[] mViews = new IViewHolder[3];

  public SwipeySwitcher(Context paramContext)
  {
    super(paramContext);
    initSwipeySwitcher(paramContext);
  }

  public SwipeySwitcher(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initSwipeySwitcher(paramContext);
  }

  private void clearScrollingCaches()
  {
    int i = this.mViews.length;
    for (int j = 0; j < i; j++)
    {
      IViewHolder localIViewHolder = this.mViews[j];
      if (localIViewHolder != null)
        localIViewHolder.clearScrollingCache();
    }
  }

  private void createScrollingCaches()
  {
    int i = this.mViews.length;
    for (int j = 0; j < i; j++)
    {
      IViewHolder localIViewHolder = this.mViews[j];
      if (localIViewHolder != null)
        localIViewHolder.createScrollingCache();
    }
  }

  private void ensureLeft()
  {
    if ((this.mAdapter != null) && (this.mAdapter.getCount() == 2) && (this.mFakes.memberOf(this.mViews[0])))
      swapLeftRight();
  }

  private void ensureRight()
  {
    if ((this.mAdapter != null) && (this.mAdapter.getCount() == 2) && (this.mFakes.memberOf(this.mViews[2])))
      swapLeftRight();
  }

  private void initSwipeySwitcher(Context paramContext)
  {
    this.mGestureDetector = makeGestureDetector();
    this.mScroller = new Scroller(paramContext);
    this.mContext = paramContext;
    this.mTouchSlop = ViewConfiguration.get(paramContext).getScaledPagingTouchSlop();
  }

  private void installView(IViewHolder paramIViewHolder, int paramInt)
  {
    int i = getWidth();
    int j = getHeight();
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
    View localView = paramIViewHolder.get();
    addViewInLayout(localView, paramInt, localLayoutParams, true);
    localView.layout(i * paramInt, 0, i + i * paramInt, j);
    this.mViews[paramInt] = paramIViewHolder;
  }

  private GestureDetector makeGestureDetector()
  {
    GestureDetector.OnGestureListener local3 = new GestureDetector.OnGestureListener()
    {
      private boolean canGoLeft()
      {
        if (SwipeySwitcher.this.mAdapter != null);
        for (int i = SwipeySwitcher.this.mAdapter.getCount(); i > 1; i = 0)
          return true;
        return false;
      }

      private boolean canGoRight()
      {
        if (SwipeySwitcher.this.mAdapter != null);
        for (int i = SwipeySwitcher.this.mAdapter.getCount(); i > 1; i = 0)
          return true;
        return false;
      }

      public boolean onDown(MotionEvent paramAnonymousMotionEvent)
      {
        SwipeySwitcher.access$202(SwipeySwitcher.this, (int)paramAnonymousMotionEvent.getX());
        return true;
      }

      public boolean onFling(MotionEvent paramAnonymousMotionEvent1, MotionEvent paramAnonymousMotionEvent2, float paramAnonymousFloat1, float paramAnonymousFloat2)
      {
        if (!SwipeySwitcher.this.mDragging)
          return false;
        if (paramAnonymousFloat1 == 0.0F)
          return false;
        if ((paramAnonymousFloat1 > 0.0F) && (!canGoLeft()))
          return false;
        if ((paramAnonymousFloat1 < 0.0F) && (!canGoRight()))
          return false;
        boolean bool1;
        if (paramAnonymousFloat1 < 0.0F)
        {
          bool1 = true;
          if (SwipeySwitcher.this.getScrollX() - SwipeySwitcher.this.getWidth() <= 0)
            break label102;
        }
        label102: for (boolean bool2 = true; ; bool2 = false)
        {
          if (bool1 == bool2)
            break label108;
          SwipeySwitcher.this.recenter();
          return true;
          bool1 = false;
          break;
        }
        label108: if (bool1)
          SwipeySwitcher.this.ensureRight();
        while (true)
        {
          SwipeySwitcher.this.recenterAndSelect(bool1);
          break;
          SwipeySwitcher.this.ensureLeft();
        }
      }

      public void onLongPress(MotionEvent paramAnonymousMotionEvent)
      {
      }

      public boolean onScroll(MotionEvent paramAnonymousMotionEvent1, MotionEvent paramAnonymousMotionEvent2, float paramAnonymousFloat1, float paramAnonymousFloat2)
      {
        if (!SwipeySwitcher.this.mDragging)
          return false;
        int i = SwipeySwitcher.this.getScrollX() + (int)paramAnonymousFloat1;
        int j;
        int k;
        if ((i < SwipeySwitcher.this.getWidth()) && (!canGoLeft()))
        {
          i = SwipeySwitcher.this.getWidth();
          SwipeySwitcher.this.scrollTo(i, 0);
          if ((SwipeySwitcher.this.mAdapter != null) && (SwipeySwitcher.this.mAdapter.getCount() == 2))
          {
            j = SwipeySwitcher.this.getWidth();
            if ((i >= j) || (!(SwipeySwitcher.this.mViews[0] instanceof SwipeySwitcher.ViewHolder)))
              break label167;
            k = 1;
          }
        }
        while (true)
        {
          if (k != 0)
            SwipeySwitcher.this.swapLeftRight();
          return true;
          if ((i <= SwipeySwitcher.this.getWidth()) || (canGoRight()))
            break;
          i = SwipeySwitcher.this.getWidth();
          break;
          label167: k = 0;
          if (i > j)
          {
            boolean bool = SwipeySwitcher.this.mViews[2] instanceof SwipeySwitcher.ViewHolder;
            k = 0;
            if (bool)
              k = 1;
          }
        }
      }

      public void onShowPress(MotionEvent paramAnonymousMotionEvent)
      {
      }

      public boolean onSingleTapUp(MotionEvent paramAnonymousMotionEvent)
      {
        return false;
      }
    };
    return new GestureDetector(this.mContext, local3);
  }

  private float moveFactor()
  {
    int i = getWidth();
    return (getScrollX() - i) / i;
  }

  private int nextSelection(boolean paramBoolean)
  {
    if (paramBoolean)
      return 1 + this.mSelection;
    return this.mSelection - 1;
  }

  private void recenter()
  {
    this.mScroller.startScroll(getScrollX(), 0, getWidth() - getScrollX(), 0);
    this.mHandler.post(this.mAnimateScrollRunnable);
  }

  private void recenterAndSelect(boolean paramBoolean)
  {
    recenterAndSelect(paramBoolean, null, 400);
  }

  private void recenterAndSelect(boolean paramBoolean, Runnable paramRunnable, int paramInt)
  {
    this.mNextSelection = nextSelection(paramBoolean);
    this.mAnimateScrollRunnable.mOnDoneRunnable = paramRunnable;
    if (!paramBoolean)
    {
      this.mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, paramInt);
      this.mHandler.post(this.mAnimateScrollRunnable);
      return;
    }
    this.mScroller.startScroll(getScrollX(), 0, 2 * getWidth() - getScrollX(), 0, paramInt);
    this.mHandler.post(this.mAnimateScrollRunnable);
  }

  private void swapLeftRight()
  {
    IViewHolder localIViewHolder = this.mViews[0];
    this.mViews[0] = this.mViews[2];
    this.mViews[2] = localIViewHolder;
    requestLayout();
  }

  private int wrap(int paramInt)
  {
    if (this.mAdapter == null)
      return paramInt;
    int i = this.mAdapter.getCount();
    if (i == 0)
      return paramInt;
    if (paramInt < 0)
      paramInt = i - -paramInt % i;
    return paramInt % i;
  }

  public void clear()
  {
    for (int i = 0; i < this.mViews.length; i++)
    {
      IViewHolder localIViewHolder = this.mViews[i];
      if (localIViewHolder != null)
      {
        localIViewHolder.stop();
        this.mViews[i] = null;
      }
    }
  }

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    View localView = getSelectedView();
    if (localView != null)
      return localView.dispatchKeyEvent(paramKeyEvent);
    return super.dispatchKeyEvent(paramKeyEvent);
  }

  public void foreachView(ViewRunnable paramViewRunnable)
  {
    for (int i = 0; ; i++)
      if ((i >= 3) || ((this.mViews[i] != null) && (!this.mViews[i].run(paramViewRunnable))))
        return;
  }

  public Adapter getAdapter()
  {
    return this.mAdapter;
  }

  public int getCount()
  {
    return getAdapter().getCount();
  }

  public int getSelectedItemPosition()
  {
    return wrap(this.mNextSelection);
  }

  public View getSelectedView()
  {
    if (this.mViews[1] != null)
      return this.mViews[1].get();
    return null;
  }

  public View getSelectedViewAdjacent(boolean paramBoolean)
  {
    IViewHolder[] arrayOfIViewHolder = this.mViews;
    if (paramBoolean);
    for (int i = 2; ; i = 0)
    {
      IViewHolder localIViewHolder = arrayOfIViewHolder[i];
      if (localIViewHolder == null)
        break;
      return localIViewHolder.get();
    }
    return null;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mAnimatingScroll)
      return true;
    switch (paramMotionEvent.getAction())
    {
    case 1:
    default:
    case 0:
    case 2:
    }
    while (true)
    {
      return this.mDragging;
      this.mGestureDetector.onTouchEvent(paramMotionEvent);
      continue;
      int i = (int)paramMotionEvent.getX();
      int j = i - this.mDownX;
      this.mGestureDetector.onTouchEvent(paramMotionEvent);
      if (Math.abs(j) > this.mTouchSlop)
      {
        this.mDownX = i;
        this.mDragging = true;
        this.mCenterOnLayout = false;
        createScrollingCaches();
      }
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getWidth();
    int j;
    if (this.mAdapter != null)
    {
      j = this.mAdapter.getCount();
      removeAllViews();
      if (this.mAdapter != null)
        break label42;
    }
    label42: label213: 
    do
    {
      return;
      j = 0;
      break;
      this.mSelection = this.mNextSelection;
      HashMap localHashMap = this.mReuseCache;
      localHashMap.clear();
      int k = 0;
      if (k < 3)
      {
        IViewHolder localIViewHolder3 = this.mViews[k];
        this.mViews[k] = null;
        if (localIViewHolder3 == null);
        while (true)
        {
          k++;
          break;
          Intent localIntent2 = localIViewHolder3.getIntent();
          if (localIntent2 != null)
            localHashMap.put(localIntent2.getDataString(), localIViewHolder3);
        }
      }
      int[] arrayOfInt;
      int m;
      int i6;
      Intent localIntent1;
      if (getScrollX() > getWidth())
      {
        arrayOfInt = sOrderLeft;
        m = 0;
        int n = arrayOfInt.length;
        if (m >= n)
          break label275;
        i6 = arrayOfInt[m];
        int i7 = i6 + this.mSelection - 1;
        localIntent1 = this.mAdapter.getIntent(wrap(i7));
        if (localIntent1 != null)
          break label213;
      }
      while (true)
      {
        m++;
        break label147;
        arrayOfInt = sOrderRight;
        break;
        String str = localIntent1.getDataString();
        IViewHolder localIViewHolder2 = (IViewHolder)localHashMap.get(str);
        if (localIViewHolder2 != null)
        {
          localHashMap.remove(str);
          this.mViews[i6] = localIViewHolder2;
          if (i6 != 1)
            this.mViews[i6].unfocus();
        }
      }
      if (localHashMap.size() > 0)
      {
        Iterator localIterator = localHashMap.keySet().iterator();
        while (localIterator.hasNext())
          ((IViewHolder)localHashMap.get((String)localIterator.next())).stop();
      }
      int i1 = -1;
      if (i1 <= 1)
      {
        int i2 = i1 + 1;
        IViewHolder localIViewHolder1 = this.mViews[i2];
        int i4;
        if (localIViewHolder1 == null)
          switch (j)
          {
          default:
            i4 = 0;
            if (i4 != 0)
              localIViewHolder1 = this.mFakes.get(null);
            break;
          case 1:
          case 2:
          }
        while (true)
        {
          this.mViews[(i1 + 1)] = localIViewHolder1;
          installView(localIViewHolder1, i2);
          i1++;
          break;
          if (i1 != 0);
          for (i4 = 1; ; i4 = 0)
            break;
          if (i1 == -1)
          {
            if ((this.mViews[2] != null) && (!this.mFakes.memberOf(this.mViews[2])));
            for (i4 = 1; ; i4 = 0)
              break;
          }
          int i3 = i1;
          i4 = 0;
          if (i3 != 1)
            break label387;
          if ((this.mViews[0] != null) && (!this.mFakes.memberOf(this.mViews[0])));
          for (i4 = 1; ; i4 = 0)
            break;
          int i5 = wrap(i1 + this.mSelection);
          if ((i1 == 0) && (j > 0))
            localIViewHolder1 = this.mAdapter.getView(i5, this);
          if (localIViewHolder1 == null)
            localIViewHolder1 = this.mPlaceholders.get(this.mAdapter.getIntent(i5));
        }
      }
      if (this.mCenterOnLayout)
        scrollTo(i, 0);
      if (this.mViews[1] != null)
        this.mViews[1].focus();
      this.mHandler.post(this.mRefreshRunnable);
    }
    while (!this.mClearAnimatingScroll);
    label147: this.mAnimatingScroll = false;
    label275: this.mClearAnimatingScroll = false;
    label387:
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    int k = getChildCount();
    for (int m = 0; m < k; m++)
      getChildAt(m).measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(j, 1073741824));
    setMeasuredDimension(i, j);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mAnimatingScroll)
      return true;
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    case 2:
    case 1:
    case 3:
    }
    do
    {
      do
      {
        do
          while (true)
          {
            return true;
            this.mGestureDetector.onTouchEvent(paramMotionEvent);
            this.mDownX = ((int)paramMotionEvent.getX());
            this.mDragging = true;
            this.mCenterOnLayout = false;
            createScrollingCaches();
          }
        while (!this.mDragging);
        this.mGestureDetector.onTouchEvent(paramMotionEvent);
      }
      while (this.mObserver == null);
      float f2 = moveFactor();
      if ((f2 > 0.5F) || (f2 < -0.5F));
      for (boolean bool2 = true; ; bool2 = false)
      {
        this.mObserver.onRevealChange(getScrollX() / getWidth() - 1.0F, bool2);
        break;
      }
    }
    while (!this.mDragging);
    this.mCenterOnLayout = true;
    boolean bool1 = this.mGestureDetector.onTouchEvent(paramMotionEvent);
    clearScrollingCaches();
    float f1;
    if (!bool1)
    {
      f1 = moveFactor();
      if (f1 <= 0.5F)
        break label233;
      recenterAndSelect(true);
    }
    while (true)
    {
      if (this.mObserver != null)
        this.mObserver.onStopReveal();
      this.mDragging = false;
      break;
      label233: if (f1 < -0.5F)
        recenterAndSelect(false);
      else
        recenter();
    }
  }

  public void selectNext(boolean paramBoolean, Runnable paramRunnable, int paramInt)
  {
    recenterAndSelect(paramBoolean, paramRunnable, paramInt);
  }

  public void setAdapter(Adapter paramAdapter)
  {
    this.mAdapter = paramAdapter;
    if (paramAdapter == null)
      for (int i = 0; i < 3; i++)
        if (this.mViews[i] != null)
        {
          this.mViews[i].stop();
          this.mViews[i] = null;
        }
    this.mNextSelection = 0;
    this.mSelection = 0;
    requestLayout();
    this.mHandler.removeCallbacks(this.mAnimateScrollRunnable);
  }

  public void setOnTouchCallback(ObserverCallbacks paramObserverCallbacks)
  {
    this.mObserver = paramObserverCallbacks;
  }

  public void setSelection(int paramInt)
  {
    if (this.mAdapter == null);
    do
    {
      return;
      this.mNextSelection = paramInt;
      this.mCenterOnLayout = true;
      requestLayout();
    }
    while (this.mObserver == null);
    this.mObserver.onSelection(this.mAdapter.getIntent(wrap(paramInt)));
  }

  public static abstract interface Adapter
  {
    public abstract int getCount();

    public abstract Intent getIntent(int paramInt);

    public abstract SwipeySwitcher.IViewHolder getView(int paramInt, ViewGroup paramViewGroup);
  }

  static abstract interface IMakeView
  {
    public abstract View makeView();
  }

  public static abstract interface IViewHolder
  {
    public abstract void clearScrollingCache();

    public abstract void createScrollingCache();

    public abstract void focus();

    public abstract View get();

    public abstract Intent getIntent();

    public abstract boolean run(SwipeySwitcher.ViewRunnable paramViewRunnable);

    public abstract void stop();

    public abstract void unfocus();
  }

  public static abstract interface ObserverCallbacks
  {
    public abstract void onRevealChange(float paramFloat, boolean paramBoolean);

    public abstract void onSelection(Intent paramIntent);

    public abstract void onStartReveal();

    public abstract void onStopReveal();
  }

  class ScrollRunnable
    implements Runnable
  {
    Runnable mOnDoneRunnable;

    ScrollRunnable()
    {
    }

    public void run()
    {
      if (SwipeySwitcher.this.mScroller.computeScrollOffset())
      {
        SwipeySwitcher.access$1602(SwipeySwitcher.this, true);
        SwipeySwitcher.this.scrollTo(SwipeySwitcher.this.mScroller.getCurrX(), 0);
        SwipeySwitcher.this.postInvalidate();
        SwipeySwitcher.this.mHandler.post(this);
      }
      do
      {
        return;
        SwipeySwitcher.this.scrollTo(SwipeySwitcher.this.mScroller.getCurrX(), 0);
        SwipeySwitcher.this.setSelection(SwipeySwitcher.this.mNextSelection);
        SwipeySwitcher.access$1902(SwipeySwitcher.this, true);
      }
      while (this.mOnDoneRunnable == null);
      this.mOnDoneRunnable.run();
    }
  }

  class SimpleViewCache
  {
    private ArrayList<SwipeySwitcher.IViewHolder> mAvailable = new ArrayList();
    private ArrayList<SwipeySwitcher.IViewHolder> mInUse = new ArrayList();
    private SwipeySwitcher.IMakeView mMaker;

    SimpleViewCache(SwipeySwitcher.IMakeView arg2)
    {
      Object localObject;
      this.mMaker = localObject;
    }

    SwipeySwitcher.IViewHolder get(Intent paramIntent)
    {
      if (this.mAvailable.size() == 0)
        this.mAvailable.add(new SwipeySwitcher.ViewHolder(this.mMaker.makeView(), this.mAvailable, this.mInUse));
      SwipeySwitcher.IViewHolder localIViewHolder = (SwipeySwitcher.IViewHolder)this.mAvailable.remove(this.mAvailable.size() - 1);
      this.mInUse.add(localIViewHolder);
      SwipeySwitcher.ViewHolder.access$002((SwipeySwitcher.ViewHolder)localIViewHolder, paramIntent);
      return localIViewHolder;
    }

    boolean memberOf(SwipeySwitcher.IViewHolder paramIViewHolder)
    {
      return (this.mInUse.contains(paramIViewHolder)) || (this.mAvailable.contains(paramIViewHolder));
    }
  }

  public static class ViewHolder
    implements SwipeySwitcher.IViewHolder
  {
    ArrayList<SwipeySwitcher.IViewHolder> mAvailable;
    ArrayList<SwipeySwitcher.IViewHolder> mInUse;
    private Intent mIntent;
    private View mView;

    public ViewHolder(View paramView)
    {
      this.mView = paramView;
    }

    public ViewHolder(View paramView, ArrayList<SwipeySwitcher.IViewHolder> paramArrayList1, ArrayList<SwipeySwitcher.IViewHolder> paramArrayList2)
    {
      this.mView = paramView;
      this.mAvailable = paramArrayList1;
      this.mInUse = paramArrayList2;
    }

    public void clearScrollingCache()
    {
    }

    public void createScrollingCache()
    {
    }

    public void focus()
    {
    }

    public View get()
    {
      return this.mView;
    }

    public Intent getIntent()
    {
      return this.mIntent;
    }

    public String getTitle()
    {
      return "";
    }

    public boolean run(SwipeySwitcher.ViewRunnable paramViewRunnable)
    {
      return true;
    }

    public void stop()
    {
      if (this.mInUse != null)
        this.mInUse.remove(this);
      if (this.mAvailable != null)
        this.mAvailable.add(this);
    }

    public void unfocus()
    {
    }
  }

  public static abstract interface ViewRunnable
  {
    public abstract boolean run(View paramView);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.SwipeySwitcher
 * JD-Core Version:    0.6.2
 */