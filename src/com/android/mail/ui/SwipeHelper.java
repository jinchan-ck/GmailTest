package com.android.mail.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.android.mail.utils.Utils;

public class SwipeHelper
{
  public static float ALPHA_FADE_START = 0.0F;
  private static int DEFAULT_ESCAPE_ANIMATION_DURATION;
  private static int DISMISS_ANIMATION_DURATION;
  private static int MAX_DISMISS_VELOCITY;
  private static int MAX_ESCAPE_ANIMATION_DURATION;
  private static float MIN_LOCK;
  private static float MIN_SWIPE;
  private static float MIN_VERT;
  private static int SNAP_ANIM_LEN;
  private static int SWIPE_ESCAPE_VELOCITY;
  private static LinearInterpolator sLinearInterpolator = new LinearInterpolator();
  private Callback mCallback;
  private boolean mCanCurrViewBeDimissed;
  private View mCurrAnimView;
  private SwipeableItemView mCurrView;
  private float mDensityScale;
  private boolean mDragging;
  private float mInitialTouchPosX;
  private float mInitialTouchPosY;
  private float mLastY;
  private float mMinAlpha = 0.5F;
  private float mPagingTouchSlop;
  private int mSwipeDirection;
  private VelocityTracker mVelocityTracker;

  static
  {
    SWIPE_ESCAPE_VELOCITY = -1;
  }

  public SwipeHelper(Context paramContext, int paramInt, Callback paramCallback, float paramFloat1, float paramFloat2)
  {
    this.mCallback = paramCallback;
    this.mSwipeDirection = paramInt;
    this.mVelocityTracker = VelocityTracker.obtain();
    this.mDensityScale = paramFloat1;
    this.mPagingTouchSlop = paramFloat2;
    if (SWIPE_ESCAPE_VELOCITY == -1)
    {
      Resources localResources = paramContext.getResources();
      SWIPE_ESCAPE_VELOCITY = localResources.getInteger(2131296268);
      DEFAULT_ESCAPE_ANIMATION_DURATION = localResources.getInteger(2131296269);
      MAX_ESCAPE_ANIMATION_DURATION = localResources.getInteger(2131296270);
      MAX_DISMISS_VELOCITY = localResources.getInteger(2131296271);
      SNAP_ANIM_LEN = localResources.getInteger(2131296272);
      DISMISS_ANIMATION_DURATION = localResources.getInteger(2131296273);
      MIN_SWIPE = localResources.getDimension(2131361800);
      MIN_VERT = localResources.getDimension(2131361801);
      MIN_LOCK = localResources.getDimension(2131361802);
    }
  }

  private ObjectAnimator createDismissAnimation(View paramView, float paramFloat, int paramInt)
  {
    ObjectAnimator localObjectAnimator = createTranslationAnimation(paramView, paramFloat);
    localObjectAnimator.setInterpolator(sLinearInterpolator);
    localObjectAnimator.setDuration(paramInt);
    return localObjectAnimator;
  }

  private ObjectAnimator createTranslationAnimation(View paramView, float paramFloat)
  {
    if (this.mSwipeDirection == 0);
    for (String str = "translationX"; ; str = "translationY")
      return ObjectAnimator.ofFloat(paramView, str, new float[] { paramFloat });
  }

  private int determineDuration(View paramView, float paramFloat1, float paramFloat2)
  {
    int i = MAX_ESCAPE_ANIMATION_DURATION;
    if (paramFloat2 != 0.0F)
      return Math.min(i, (int)(1000.0F * Math.abs(paramFloat1 - paramView.getTranslationX()) / Math.abs(paramFloat2)));
    return DEFAULT_ESCAPE_ANIMATION_DURATION;
  }

  private float determinePos(View paramView, float paramFloat)
  {
    if ((paramFloat < 0.0F) || ((paramFloat == 0.0F) && (paramView.getTranslationX() < 0.0F)) || ((paramFloat == 0.0F) && (paramView.getTranslationX() == 0.0F) && (this.mSwipeDirection == 1)))
      return -getSize(paramView);
    return getSize(paramView);
  }

  private void dismissChild(SwipeableItemView paramSwipeableItemView, float paramFloat)
  {
    final View localView = this.mCurrView.getSwipeableView();
    final boolean bool = this.mCallback.canChildBeDismissed(paramSwipeableItemView);
    float f = determinePos(localView, paramFloat);
    int i = determineDuration(localView, f, paramFloat);
    Utils.enableHardwareLayer(localView);
    ObjectAnimator localObjectAnimator = createDismissAnimation(localView, f, i);
    localObjectAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        SwipeHelper.this.mCallback.onChildDismissed(SwipeHelper.this.mCurrView);
        localView.setLayerType(0, null);
      }
    });
    localObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        if (bool)
          localView.setAlpha(SwipeHelper.this.getAlphaForOffset(localView));
        SwipeHelper.invalidateGlobalRegion(localView);
      }
    });
    localObjectAnimator.start();
  }

  private float getAlphaForOffset(View paramView)
  {
    float f1 = getSize(paramView);
    float f2 = 0.7F * f1;
    float f3 = 1.0F;
    float f4 = paramView.getTranslationX();
    if (f4 >= f1 * ALPHA_FADE_START)
      f3 = 1.0F - (f4 - f1 * ALPHA_FADE_START) / f2;
    while (true)
    {
      return Math.max(this.mMinAlpha, f3);
      if (f4 < f1 * (1.0F - ALPHA_FADE_START))
        f3 = 1.0F + (f4 + f1 * ALPHA_FADE_START) / f2;
    }
  }

  private float getPerpendicularVelocity(VelocityTracker paramVelocityTracker)
  {
    if (this.mSwipeDirection == 0)
      return paramVelocityTracker.getYVelocity();
    return paramVelocityTracker.getXVelocity();
  }

  private float getSize(View paramView)
  {
    if (this.mSwipeDirection == 0)
      return paramView.getMeasuredWidth();
    return paramView.getMeasuredHeight();
  }

  private float getVelocity(VelocityTracker paramVelocityTracker)
  {
    if (this.mSwipeDirection == 0)
      return paramVelocityTracker.getXVelocity();
    return paramVelocityTracker.getYVelocity();
  }

  public static void invalidateGlobalRegion(View paramView)
  {
    invalidateGlobalRegion(paramView, new RectF(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom()));
  }

  public static void invalidateGlobalRegion(View paramView, RectF paramRectF)
  {
    while ((paramView.getParent() != null) && ((paramView.getParent() instanceof View)))
    {
      paramView = (View)paramView.getParent();
      paramView.getMatrix().mapRect(paramRectF);
      paramView.invalidate((int)Math.floor(paramRectF.left), (int)Math.floor(paramRectF.top), (int)Math.ceil(paramRectF.right), (int)Math.ceil(paramRectF.bottom));
    }
  }

  private void setTranslation(View paramView, float paramFloat)
  {
    if (this.mSwipeDirection == 0)
    {
      paramView.setTranslationX(paramFloat);
      return;
    }
    paramView.setTranslationY(paramFloat);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    case 2:
    case 1:
    case 3:
    }
    while (true)
    {
      return this.mDragging;
      this.mLastY = paramMotionEvent.getY();
      this.mDragging = false;
      View localView = this.mCallback.getChildAtPosition(paramMotionEvent);
      if ((localView instanceof SwipeableItemView))
        this.mCurrView = ((SwipeableItemView)localView);
      this.mVelocityTracker.clear();
      if (this.mCurrView != null)
      {
        this.mCurrAnimView = this.mCurrView.getSwipeableView();
        this.mCanCurrViewBeDimissed = this.mCallback.canChildBeDismissed(this.mCurrView);
        this.mVelocityTracker.addMovement(paramMotionEvent);
        this.mInitialTouchPosX = paramMotionEvent.getX();
        this.mInitialTouchPosY = paramMotionEvent.getY();
        continue;
        if (this.mCurrView != null)
        {
          if ((this.mLastY >= 0.0F) && (!this.mDragging))
          {
            float f1 = paramMotionEvent.getY();
            float f2 = paramMotionEvent.getX();
            float f3 = Math.abs(f1 - this.mInitialTouchPosY);
            float f4 = Math.abs(f2 - this.mInitialTouchPosX);
            if ((f3 > this.mCurrView.getMinAllowScrollDistance()) && (f3 > 1.2F * f4))
            {
              this.mLastY = paramMotionEvent.getY();
              this.mCallback.onScroll();
              return false;
            }
          }
          this.mVelocityTracker.addMovement(paramMotionEvent);
          if (Math.abs(paramMotionEvent.getX() - this.mInitialTouchPosX) > this.mPagingTouchSlop)
          {
            this.mCallback.onBeginDrag(this.mCurrView.getSwipeableView());
            this.mDragging = true;
            this.mInitialTouchPosX = (paramMotionEvent.getX() - this.mCurrAnimView.getTranslationX());
            this.mInitialTouchPosY = paramMotionEvent.getY();
          }
        }
        this.mLastY = paramMotionEvent.getY();
        continue;
        this.mDragging = false;
        this.mCurrView = null;
        this.mCurrAnimView = null;
        this.mLastY = -1.0F;
      }
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.mDragging)
      return false;
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (paramMotionEvent.getAction())
    {
    default:
    case 2:
    case 4:
    case 1:
    case 3:
    }
    while (true)
    {
      return true;
      if (this.mCurrView != null)
      {
        float f7 = paramMotionEvent.getX() - this.mInitialTouchPosX;
        float f8 = Math.abs(paramMotionEvent.getY() - this.mInitialTouchPosY);
        if ((!this.mDragging) && (f8 > MIN_VERT) && (Math.abs(f7) < MIN_LOCK) && (f8 > 1.2F * Math.abs(f7)))
        {
          this.mCallback.onScroll();
          return false;
        }
        float f9 = MIN_SWIPE;
        if (Math.abs(f7) < f9)
          return true;
        float f10;
        float f11;
        if (!this.mCallback.canChildBeDismissed(this.mCurrView))
        {
          f10 = getSize(this.mCurrAnimView);
          f11 = 0.15F * f10;
          if (Math.abs(f7) < f10)
            break label269;
          if (f7 <= 0.0F)
            break label261;
          f7 = f11;
        }
        while (true)
        {
          setTranslation(this.mCurrAnimView, f7);
          if (this.mCanCurrViewBeDimissed)
            this.mCurrAnimView.setAlpha(getAlphaForOffset(this.mCurrAnimView));
          invalidateGlobalRegion(this.mCurrView.getSwipeableView());
          break;
          label261: f7 = -f11;
          continue;
          label269: f7 = f11 * (float)Math.sin(1.570796326794897D * (f7 / f10));
        }
        if (this.mCurrView != null)
        {
          float f1 = MAX_DISMISS_VELOCITY * this.mDensityScale;
          this.mVelocityTracker.computeCurrentVelocity(1000, f1);
          float f2 = SWIPE_ESCAPE_VELOCITY * this.mDensityScale;
          float f3 = getVelocity(this.mVelocityTracker);
          float f4 = getPerpendicularVelocity(this.mVelocityTracker);
          float f5 = Math.abs(this.mCurrAnimView.getTranslationX());
          float f6 = getSize(this.mCurrAnimView);
          int i;
          label388: int m;
          label422: int n;
          label437: int j;
          label461: int k;
          label490: SwipeableItemView localSwipeableItemView;
          if (f5 > 0.4D * f6)
          {
            i = 1;
            if ((Math.abs(f3) <= f2) || (Math.abs(f3) <= Math.abs(f4)))
              break label535;
            if (f3 <= 0.0F)
              break label523;
            m = 1;
            if (this.mCurrAnimView.getTranslationX() <= 0.0F)
              break label529;
            n = 1;
            if ((m != n) || (f5 <= 0.05D * f6))
              break label535;
            j = 1;
            if ((!this.mCallback.canChildBeDismissed(this.mCurrView)) || ((j == 0) && (i == 0)))
              break label541;
            k = 1;
            if (k == 0)
              break label553;
            localSwipeableItemView = this.mCurrView;
            if (j == 0)
              break label547;
          }
          while (true)
          {
            dismissChild(localSwipeableItemView, f3);
            break;
            i = 0;
            break label388;
            label523: m = 0;
            break label422;
            label529: n = 0;
            break label437;
            label535: j = 0;
            break label461;
            label541: k = 0;
            break label490;
            label547: f3 = 0.0F;
          }
          label553: snapChild(this.mCurrView, f3);
        }
      }
    }
  }

  public void setDensityScale(float paramFloat)
  {
    this.mDensityScale = paramFloat;
  }

  public void setPagingTouchSlop(float paramFloat)
  {
    this.mPagingTouchSlop = paramFloat;
  }

  public void snapChild(SwipeableItemView paramSwipeableItemView, float paramFloat)
  {
    final View localView = paramSwipeableItemView.getSwipeableView();
    final boolean bool = this.mCallback.canChildBeDismissed(paramSwipeableItemView);
    ObjectAnimator localObjectAnimator = createTranslationAnimation(localView, 0.0F);
    localObjectAnimator.setDuration(SNAP_ANIM_LEN);
    localObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        if (bool)
          localView.setAlpha(SwipeHelper.this.getAlphaForOffset(localView));
        SwipeHelper.invalidateGlobalRegion(localView);
      }
    });
    localObjectAnimator.addListener(new Animator.AnimatorListener()
    {
      public void onAnimationCancel(Animator paramAnonymousAnimator)
      {
      }

      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        localView.setAlpha(1.0F);
        SwipeHelper.this.mCallback.onDragCancelled(SwipeHelper.this.mCurrView);
      }

      public void onAnimationRepeat(Animator paramAnonymousAnimator)
      {
      }

      public void onAnimationStart(Animator paramAnonymousAnimator)
      {
      }
    });
    localObjectAnimator.start();
  }

  public static abstract interface Callback
  {
    public abstract boolean canChildBeDismissed(SwipeableItemView paramSwipeableItemView);

    public abstract View getChildAtPosition(MotionEvent paramMotionEvent);

    public abstract void onBeginDrag(View paramView);

    public abstract void onChildDismissed(SwipeableItemView paramSwipeableItemView);

    public abstract void onDragCancelled(SwipeableItemView paramSwipeableItemView);

    public abstract void onScroll();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.SwipeHelper
 * JD-Core Version:    0.6.2
 */