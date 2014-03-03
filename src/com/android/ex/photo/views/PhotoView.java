package com.android.ex.photo.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import com.android.ex.photo.R.color;
import com.android.ex.photo.R.dimen;

public class PhotoView extends View
  implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener
{
  private static Paint sCropDimPaint;
  private static Paint sCropPaint;
  private static int sCropSize;
  private static boolean sInitialized;
  private static Bitmap sVideoImage;
  private static Bitmap sVideoNotReadyImage;
  private boolean mAllowCrop;
  private Rect mCropRect = new Rect();
  private int mCropSize;
  private boolean mDoubleTapDebounce;
  private boolean mDoubleTapToZoomEnabled = true;
  private Matrix mDrawMatrix;
  private BitmapDrawable mDrawable;
  private View.OnClickListener mExternalClickListener;
  private int mFixedHeight = -1;
  private boolean mFullScreen;
  private GestureDetectorCompat mGestureDetector;
  private boolean mHaveLayout;
  private boolean mIsDoubleTouch;
  private Matrix mMatrix = new Matrix();
  private float mMaxScale;
  private float mMinScale;
  private Matrix mOriginalMatrix = new Matrix();
  private RotateRunnable mRotateRunnable;
  private float mRotation;
  private ScaleGestureDetector mScaleGetureDetector;
  private ScaleRunnable mScaleRunnable;
  private SnapRunnable mSnapRunnable;
  private RectF mTempDst = new RectF();
  private RectF mTempSrc = new RectF();
  private boolean mTransformsEnabled;
  private RectF mTranslateRect = new RectF();
  private TranslateRunnable mTranslateRunnable;
  private float[] mValues = new float[9];
  private byte[] mVideoBlob;
  private boolean mVideoReady;

  public PhotoView(Context paramContext)
  {
    super(paramContext);
    initialize();
  }

  public PhotoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initialize();
  }

  public PhotoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initialize();
  }

  private void configureBounds(boolean paramBoolean)
  {
    if ((this.mDrawable == null) || (!this.mHaveLayout))
      return;
    int i = this.mDrawable.getIntrinsicWidth();
    int j = this.mDrawable.getIntrinsicHeight();
    int k = getWidth();
    int m = getHeight();
    if (((i < 0) || (k == i)) && ((j < 0) || (m == j)));
    for (int n = 1; ; n = 0)
    {
      this.mDrawable.setBounds(0, 0, i, j);
      if ((paramBoolean) || ((this.mMinScale == 0.0F) && (this.mDrawable != null) && (this.mHaveLayout)))
      {
        generateMatrix();
        generateScale();
      }
      if ((n == 0) && (!this.mMatrix.isIdentity()))
        break;
      this.mDrawMatrix = null;
      return;
    }
    this.mDrawMatrix = this.mMatrix;
  }

  private void generateMatrix()
  {
    int i = this.mDrawable.getIntrinsicWidth();
    int j = this.mDrawable.getIntrinsicHeight();
    int k;
    int m;
    label39: int n;
    if (this.mAllowCrop)
    {
      k = sCropSize;
      if (!this.mAllowCrop)
        break label100;
      m = sCropSize;
      if (((i >= 0) && (k != i)) || ((j >= 0) && (m != j)))
        break label109;
      n = 1;
      label61: if ((n == 0) || (this.mAllowCrop))
        break label115;
      this.mMatrix.reset();
    }
    while (true)
    {
      this.mOriginalMatrix.set(this.mMatrix);
      return;
      k = getWidth();
      break;
      label100: m = getHeight();
      break label39;
      label109: n = 0;
      break label61;
      label115: this.mTempSrc.set(0.0F, 0.0F, i, j);
      if (this.mAllowCrop)
        this.mTempDst.set(this.mCropRect);
      while (true)
      {
        if ((i >= k) || (j >= m) || (this.mAllowCrop))
          break label208;
        this.mMatrix.setTranslate(k / 2 - i / 2, m / 2 - j / 2);
        break;
        this.mTempDst.set(0.0F, 0.0F, k, m);
      }
      label208: this.mMatrix.setRectToRect(this.mTempSrc, this.mTempDst, Matrix.ScaleToFit.CENTER);
    }
  }

  private void generateScale()
  {
    int i = this.mDrawable.getIntrinsicWidth();
    int j = this.mDrawable.getIntrinsicHeight();
    int k;
    int m;
    if (this.mAllowCrop)
    {
      k = getCropSize();
      if (!this.mAllowCrop)
        break label89;
      m = getCropSize();
      label41: if ((i >= k) || (j >= m) || (this.mAllowCrop))
        break label98;
    }
    label89: label98: for (this.mMinScale = 1.0F; ; this.mMinScale = getScale())
    {
      this.mMaxScale = Math.max(8.0F * this.mMinScale, 8.0F);
      return;
      k = getWidth();
      break;
      m = getHeight();
      break label41;
    }
  }

  private int getCropSize()
  {
    if (this.mCropSize > 0)
      return this.mCropSize;
    return sCropSize;
  }

  private float getScale()
  {
    this.mMatrix.getValues(this.mValues);
    return this.mValues[0];
  }

  private void initialize()
  {
    Context localContext = getContext();
    if (!sInitialized)
    {
      sInitialized = true;
      Resources localResources = localContext.getApplicationContext().getResources();
      sCropSize = localResources.getDimensionPixelSize(R.dimen.photo_crop_width);
      sCropDimPaint = new Paint();
      sCropDimPaint.setAntiAlias(true);
      sCropDimPaint.setColor(localResources.getColor(R.color.photo_crop_dim_color));
      sCropDimPaint.setStyle(Paint.Style.FILL);
      sCropPaint = new Paint();
      sCropPaint.setAntiAlias(true);
      sCropPaint.setColor(localResources.getColor(R.color.photo_crop_highlight_color));
      sCropPaint.setStyle(Paint.Style.STROKE);
      sCropPaint.setStrokeWidth(localResources.getDimension(R.dimen.photo_crop_stroke_width));
    }
    this.mGestureDetector = new GestureDetectorCompat(localContext, this, null);
    this.mScaleGetureDetector = new ScaleGestureDetector(localContext, this);
    this.mScaleRunnable = new ScaleRunnable(this);
    this.mTranslateRunnable = new TranslateRunnable(this);
    this.mSnapRunnable = new SnapRunnable(this);
    this.mRotateRunnable = new RotateRunnable(this);
  }

  private void rotate(float paramFloat, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mRotateRunnable.start(paramFloat);
      return;
    }
    this.mRotation = (paramFloat + this.mRotation);
    this.mMatrix.postRotate(paramFloat, getWidth() / 2, getHeight() / 2);
    invalidate();
  }

  private void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    this.mMatrix.postRotate(-this.mRotation, getWidth() / 2, getHeight() / 2);
    float f = Math.min(Math.max(paramFloat1, this.mMinScale), this.mMaxScale) / getScale();
    this.mMatrix.postScale(f, f, paramFloat2, paramFloat3);
    snap();
    this.mMatrix.postRotate(this.mRotation, getWidth() / 2, getHeight() / 2);
    invalidate();
  }

  private void snap()
  {
    this.mTranslateRect.set(this.mTempSrc);
    this.mMatrix.mapRect(this.mTranslateRect);
    float f1;
    float f2;
    label55: float f3;
    float f4;
    float f5;
    label100: float f6;
    label117: float f7;
    label134: float f8;
    float f9;
    float f10;
    if (this.mAllowCrop)
    {
      f1 = this.mCropRect.left;
      if (!this.mAllowCrop)
        break label226;
      f2 = this.mCropRect.right;
      f3 = this.mTranslateRect.left;
      f4 = this.mTranslateRect.right;
      if (f4 - f3 >= f2 - f1)
        break label235;
      f5 = f1 + (f2 - f1 - (f4 + f3)) / 2.0F;
      if (!this.mAllowCrop)
        break label273;
      f6 = this.mCropRect.top;
      if (!this.mAllowCrop)
        break label279;
      f7 = this.mCropRect.bottom;
      f8 = this.mTranslateRect.top;
      f9 = this.mTranslateRect.bottom;
      if (f9 - f8 >= f7 - f6)
        break label289;
      f10 = f6 + (f7 - f6 - (f9 + f8)) / 2.0F;
    }
    while (true)
    {
      if ((Math.abs(f5) <= 20.0F) && (Math.abs(f10) <= 20.0F))
        break label331;
      this.mSnapRunnable.start(f5, f10);
      return;
      f1 = 0.0F;
      break;
      label226: f2 = getWidth();
      break label55;
      label235: if (f3 > f1)
      {
        f5 = f1 - f3;
        break label100;
      }
      if (f4 < f2)
      {
        f5 = f2 - f4;
        break label100;
      }
      f5 = 0.0F;
      break label100;
      label273: f6 = 0.0F;
      break label117;
      label279: f7 = getHeight();
      break label134;
      label289: if (f8 > f6)
        f10 = f6 - f8;
      else if (f9 < f7)
        f10 = f7 - f9;
      else
        f10 = 0.0F;
    }
    label331: this.mMatrix.postTranslate(f5, f10);
    invalidate();
  }

  private boolean translate(float paramFloat1, float paramFloat2)
  {
    this.mTranslateRect.set(this.mTempSrc);
    this.mMatrix.mapRect(this.mTranslateRect);
    float f1;
    float f2;
    label57: float f3;
    float f4;
    float f5;
    label111: float f6;
    label128: float f7;
    label145: float f8;
    float f9;
    float f10;
    if (this.mAllowCrop)
    {
      f1 = this.mCropRect.left;
      if (!this.mAllowCrop)
        break label237;
      f2 = this.mCropRect.right;
      f3 = this.mTranslateRect.left;
      f4 = this.mTranslateRect.right;
      if (!this.mAllowCrop)
        break label247;
      f5 = Math.max(f1 - this.mTranslateRect.right, Math.min(f2 - this.mTranslateRect.left, paramFloat1));
      if (!this.mAllowCrop)
        break label304;
      f6 = this.mCropRect.top;
      if (!this.mAllowCrop)
        break label310;
      f7 = this.mCropRect.bottom;
      f8 = this.mTranslateRect.top;
      f9 = this.mTranslateRect.bottom;
      if (!this.mAllowCrop)
        break label320;
      f10 = Math.max(f6 - this.mTranslateRect.bottom, Math.min(f7 - this.mTranslateRect.top, paramFloat2));
    }
    while (true)
    {
      this.mMatrix.postTranslate(f5, f10);
      invalidate();
      if ((f5 != paramFloat1) || (f10 != paramFloat2))
        break label377;
      return true;
      f1 = 0.0F;
      break;
      label237: f2 = getWidth();
      break label57;
      label247: if (f4 - f3 < f2 - f1)
      {
        f5 = f1 + (f2 - f1 - (f4 + f3)) / 2.0F;
        break label111;
      }
      f5 = Math.max(f2 - f4, Math.min(f1 - f3, paramFloat1));
      break label111;
      label304: f6 = 0.0F;
      break label128;
      label310: f7 = getHeight();
      break label145;
      label320: if (f9 - f8 < f7 - f6)
        f10 = f6 + (f7 - f6 - (f9 + f8)) / 2.0F;
      else
        f10 = Math.max(f7 - f9, Math.min(f6 - f8, paramFloat2));
    }
    label377: return false;
  }

  public void bindPhoto(Bitmap paramBitmap)
  {
    BitmapDrawable localBitmapDrawable = this.mDrawable;
    boolean bool = false;
    if (localBitmapDrawable != null)
    {
      if (paramBitmap == this.mDrawable.getBitmap())
        return;
      if ((paramBitmap == null) || ((this.mDrawable.getIntrinsicWidth() == paramBitmap.getWidth()) && (this.mDrawable.getIntrinsicHeight() == paramBitmap.getHeight())))
        break label104;
    }
    label104: for (bool = true; ; bool = false)
    {
      this.mMinScale = 0.0F;
      this.mDrawable = null;
      if ((this.mDrawable == null) && (paramBitmap != null))
        this.mDrawable = new BitmapDrawable(getResources(), paramBitmap);
      configureBounds(bool);
      invalidate();
      return;
    }
  }

  public void clear()
  {
    this.mGestureDetector = null;
    this.mScaleGetureDetector = null;
    this.mDrawable = null;
    this.mScaleRunnable.stop();
    this.mScaleRunnable = null;
    this.mTranslateRunnable.stop();
    this.mTranslateRunnable = null;
    this.mSnapRunnable.stop();
    this.mSnapRunnable = null;
    this.mRotateRunnable.stop();
    this.mRotateRunnable = null;
    setOnClickListener(null);
    this.mExternalClickListener = null;
  }

  public void enableImageTransforms(boolean paramBoolean)
  {
    this.mTransformsEnabled = paramBoolean;
    if (!this.mTransformsEnabled)
      resetTransformations();
  }

  public boolean interceptMoveLeft(float paramFloat1, float paramFloat2)
  {
    if (!this.mTransformsEnabled);
    float f1;
    float f2;
    float f3;
    do
    {
      return false;
      if (this.mTranslateRunnable.mRunning)
        return true;
      this.mMatrix.getValues(this.mValues);
      this.mTranslateRect.set(this.mTempSrc);
      this.mMatrix.mapRect(this.mTranslateRect);
      f1 = getWidth();
      f2 = this.mValues[2];
      f3 = this.mTranslateRect.right - this.mTranslateRect.left;
    }
    while ((!this.mTransformsEnabled) || (f3 <= f1) || (f2 == 0.0F));
    return f1 >= f3 + f2;
  }

  public boolean interceptMoveRight(float paramFloat1, float paramFloat2)
  {
    if (!this.mTransformsEnabled);
    float f1;
    float f2;
    float f3;
    do
    {
      do
      {
        return false;
        if (this.mTranslateRunnable.mRunning)
          return true;
        this.mMatrix.getValues(this.mValues);
        this.mTranslateRect.set(this.mTempSrc);
        this.mMatrix.mapRect(this.mTranslateRect);
        f1 = getWidth();
        f2 = this.mValues[2];
        f3 = this.mTranslateRect.right - this.mTranslateRect.left;
      }
      while ((!this.mTransformsEnabled) || (f3 <= f1));
      if (f2 == 0.0F)
        return true;
    }
    while (f1 >= f3 + f2);
    return true;
  }

  public boolean isPhotoBound()
  {
    return this.mDrawable != null;
  }

  public boolean onDoubleTap(MotionEvent paramMotionEvent)
  {
    if ((this.mDoubleTapToZoomEnabled) && (this.mTransformsEnabled))
    {
      if (!this.mDoubleTapDebounce)
      {
        float f1 = getScale();
        float f2 = f1 * 1.5F;
        float f3 = Math.max(this.mMinScale, f2);
        float f4 = Math.min(this.mMaxScale, f3);
        this.mScaleRunnable.start(f1, f4, paramMotionEvent.getX(), paramMotionEvent.getY());
      }
      this.mDoubleTapDebounce = false;
    }
    return true;
  }

  public boolean onDoubleTapEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }

  public boolean onDown(MotionEvent paramMotionEvent)
  {
    if (this.mTransformsEnabled)
    {
      this.mTranslateRunnable.stop();
      this.mSnapRunnable.stop();
    }
    return true;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mDrawable != null)
    {
      int i = paramCanvas.getSaveCount();
      paramCanvas.save();
      if (this.mDrawMatrix != null)
        paramCanvas.concat(this.mDrawMatrix);
      this.mDrawable.draw(paramCanvas);
      paramCanvas.restoreToCount(i);
      if (this.mVideoBlob != null)
        if (!this.mVideoReady)
          break label230;
    }
    label230: for (Bitmap localBitmap = sVideoImage; ; localBitmap = sVideoNotReadyImage)
    {
      int k = (getWidth() - localBitmap.getWidth()) / 2;
      int m = (getHeight() - localBitmap.getHeight()) / 2;
      paramCanvas.drawBitmap(localBitmap, k, m, null);
      this.mTranslateRect.set(this.mDrawable.getBounds());
      if (this.mDrawMatrix != null)
        this.mDrawMatrix.mapRect(this.mTranslateRect);
      if (this.mAllowCrop)
      {
        int j = paramCanvas.getSaveCount();
        paramCanvas.drawRect(0.0F, 0.0F, getWidth(), getHeight(), sCropDimPaint);
        paramCanvas.save();
        paramCanvas.clipRect(this.mCropRect);
        if (this.mDrawMatrix != null)
          paramCanvas.concat(this.mDrawMatrix);
        this.mDrawable.draw(paramCanvas);
        paramCanvas.restoreToCount(j);
        paramCanvas.drawRect(this.mCropRect, sCropPaint);
      }
      return;
    }
  }

  public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    if (this.mTransformsEnabled)
      this.mTranslateRunnable.start(paramFloat1, paramFloat2);
    return true;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mHaveLayout = true;
    int i = getWidth();
    int j = getHeight();
    if (this.mAllowCrop)
    {
      this.mCropSize = Math.min(sCropSize, Math.min(i, j));
      int k = (i - this.mCropSize) / 2;
      int m = (j - this.mCropSize) / 2;
      int n = k + this.mCropSize;
      int i1 = m + this.mCropSize;
      this.mCropRect.set(k, m, n, i1);
    }
    configureBounds(paramBoolean);
  }

  public void onLongPress(MotionEvent paramMotionEvent)
  {
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (this.mFixedHeight != -1)
    {
      super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(this.mFixedHeight, -2147483648));
      setMeasuredDimension(getMeasuredWidth(), this.mFixedHeight);
      return;
    }
    super.onMeasure(paramInt1, paramInt2);
  }

  public boolean onScale(ScaleGestureDetector paramScaleGestureDetector)
  {
    if (this.mTransformsEnabled)
    {
      this.mIsDoubleTouch = false;
      scale(getScale() * paramScaleGestureDetector.getScaleFactor(), paramScaleGestureDetector.getFocusX(), paramScaleGestureDetector.getFocusY());
    }
    return true;
  }

  public boolean onScaleBegin(ScaleGestureDetector paramScaleGestureDetector)
  {
    if (this.mTransformsEnabled)
    {
      this.mScaleRunnable.stop();
      this.mIsDoubleTouch = true;
    }
    return true;
  }

  public void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector)
  {
    if ((this.mTransformsEnabled) && (this.mIsDoubleTouch))
    {
      this.mDoubleTapDebounce = true;
      resetTransformations();
    }
  }

  public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    if (this.mTransformsEnabled)
      translate(-paramFloat1, -paramFloat2);
    return true;
  }

  public void onShowPress(MotionEvent paramMotionEvent)
  {
  }

  public boolean onSingleTapConfirmed(MotionEvent paramMotionEvent)
  {
    if ((this.mExternalClickListener != null) && (!this.mIsDoubleTouch))
      this.mExternalClickListener.onClick(this);
    this.mIsDoubleTouch = false;
    return true;
  }

  public boolean onSingleTapUp(MotionEvent paramMotionEvent)
  {
    return false;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mScaleGetureDetector == null) || (this.mGestureDetector == null));
    do
    {
      return true;
      this.mScaleGetureDetector.onTouchEvent(paramMotionEvent);
      this.mGestureDetector.onTouchEvent(paramMotionEvent);
      switch (paramMotionEvent.getAction())
      {
      case 2:
      default:
        return true;
      case 1:
      case 3:
      }
    }
    while (this.mTranslateRunnable.mRunning);
    snap();
    return true;
  }

  public void resetTransformations()
  {
    this.mMatrix.set(this.mOriginalMatrix);
    invalidate();
  }

  public void setFullScreen(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1 != this.mFullScreen)
    {
      this.mFullScreen = paramBoolean1;
      requestLayout();
      invalidate();
    }
  }

  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mExternalClickListener = paramOnClickListener;
  }

  private static class RotateRunnable
    implements Runnable
  {
    private float mAppliedRotation;
    private final PhotoView mHeader;
    private long mLastRuntime;
    private boolean mRunning;
    private boolean mStop;
    private float mTargetRotation;
    private float mVelocity;

    public RotateRunnable(PhotoView paramPhotoView)
    {
      this.mHeader = paramPhotoView;
    }

    public void run()
    {
      if (this.mStop)
        return;
      long l1;
      if (this.mAppliedRotation != this.mTargetRotation)
      {
        l1 = System.currentTimeMillis();
        if (this.mLastRuntime == -1L)
          break label177;
      }
      label177: for (long l2 = l1 - this.mLastRuntime; ; l2 = 0L)
      {
        float f = this.mVelocity * (float)l2;
        if (((this.mAppliedRotation < this.mTargetRotation) && (f + this.mAppliedRotation > this.mTargetRotation)) || ((this.mAppliedRotation > this.mTargetRotation) && (f + this.mAppliedRotation < this.mTargetRotation)))
          f = this.mTargetRotation - this.mAppliedRotation;
        this.mHeader.rotate(f, false);
        this.mAppliedRotation = (f + this.mAppliedRotation);
        if (this.mAppliedRotation == this.mTargetRotation)
          stop();
        this.mLastRuntime = l1;
        if (this.mStop)
          break;
        this.mHeader.post(this);
        return;
      }
    }

    public void start(float paramFloat)
    {
      if (this.mRunning)
        return;
      this.mTargetRotation = paramFloat;
      this.mVelocity = (this.mTargetRotation / 500.0F);
      this.mAppliedRotation = 0.0F;
      this.mLastRuntime = -1L;
      this.mStop = false;
      this.mRunning = true;
      this.mHeader.post(this);
    }

    public void stop()
    {
      this.mRunning = false;
      this.mStop = true;
    }
  }

  private static class ScaleRunnable
    implements Runnable
  {
    private float mCenterX;
    private float mCenterY;
    private final PhotoView mHeader;
    private boolean mRunning;
    private float mStartScale;
    private long mStartTime;
    private boolean mStop;
    private float mTargetScale;
    private float mVelocity;
    private boolean mZoomingIn;

    public ScaleRunnable(PhotoView paramPhotoView)
    {
      this.mHeader = paramPhotoView;
    }

    public void run()
    {
      if (this.mStop)
        return;
      long l = System.currentTimeMillis() - this.mStartTime;
      float f = this.mStartScale + this.mVelocity * (float)l;
      this.mHeader.scale(f, this.mCenterX, this.mCenterY);
      boolean bool1;
      if (f != this.mTargetScale)
      {
        bool1 = this.mZoomingIn;
        if (f <= this.mTargetScale)
          break label120;
      }
      label120: for (boolean bool2 = true; ; bool2 = false)
      {
        if (bool1 == bool2)
        {
          this.mHeader.scale(this.mTargetScale, this.mCenterX, this.mCenterY);
          stop();
        }
        if (this.mStop)
          break;
        this.mHeader.post(this);
        return;
      }
    }

    public boolean start(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
    {
      if (this.mRunning)
        return false;
      this.mCenterX = paramFloat3;
      this.mCenterY = paramFloat4;
      this.mTargetScale = paramFloat2;
      this.mStartTime = System.currentTimeMillis();
      this.mStartScale = paramFloat1;
      if (this.mTargetScale > this.mStartScale);
      for (boolean bool = true; ; bool = false)
      {
        this.mZoomingIn = bool;
        this.mVelocity = ((this.mTargetScale - this.mStartScale) / 300.0F);
        this.mRunning = true;
        this.mStop = false;
        this.mHeader.post(this);
        return true;
      }
    }

    public void stop()
    {
      this.mRunning = false;
      this.mStop = true;
    }
  }

  private static class SnapRunnable
    implements Runnable
  {
    private final PhotoView mHeader;
    private boolean mRunning;
    private long mStartRunTime = -1L;
    private boolean mStop;
    private float mTranslateX;
    private float mTranslateY;

    public SnapRunnable(PhotoView paramPhotoView)
    {
      this.mHeader = paramPhotoView;
    }

    public void run()
    {
      if (this.mStop)
        return;
      long l = System.currentTimeMillis();
      float f1;
      label31: float f2;
      if (this.mStartRunTime != -1L)
      {
        f1 = (float)(l - this.mStartRunTime);
        if (this.mStartRunTime == -1L)
          this.mStartRunTime = l;
        if (f1 < 100.0F)
          break label144;
        f2 = this.mTranslateX;
      }
      for (float f3 = this.mTranslateY; ; f3 = this.mTranslateY)
        label144: 
        do
        {
          this.mHeader.translate(f2, f3);
          this.mTranslateX -= f2;
          this.mTranslateY -= f3;
          if ((this.mTranslateX == 0.0F) && (this.mTranslateY == 0.0F))
            stop();
          if (this.mStop)
            break;
          this.mHeader.post(this);
          return;
          f1 = 0.0F;
          break label31;
          f2 = 10.0F * (this.mTranslateX / (100.0F - f1));
          f3 = 10.0F * (this.mTranslateY / (100.0F - f1));
          if ((Math.abs(f2) > Math.abs(this.mTranslateX)) || (f2 == (0.0F / 0.0F)))
            f2 = this.mTranslateX;
        }
        while ((Math.abs(f3) <= Math.abs(this.mTranslateY)) && (f3 != (0.0F / 0.0F)));
    }

    public boolean start(float paramFloat1, float paramFloat2)
    {
      if (this.mRunning)
        return false;
      this.mStartRunTime = -1L;
      this.mTranslateX = paramFloat1;
      this.mTranslateY = paramFloat2;
      this.mStop = false;
      this.mRunning = true;
      this.mHeader.postDelayed(this, 250L);
      return true;
    }

    public void stop()
    {
      this.mRunning = false;
      this.mStop = true;
    }
  }

  private static class TranslateRunnable
    implements Runnable
  {
    private final PhotoView mHeader;
    private long mLastRunTime = -1L;
    private boolean mRunning;
    private boolean mStop;
    private float mVelocityX;
    private float mVelocityY;

    public TranslateRunnable(PhotoView paramPhotoView)
    {
      this.mHeader = paramPhotoView;
    }

    public void run()
    {
      if (this.mStop)
        return;
      long l = System.currentTimeMillis();
      float f1;
      label34: boolean bool;
      float f2;
      if (this.mLastRunTime != -1L)
      {
        f1 = (float)(l - this.mLastRunTime) / 1000.0F;
        bool = this.mHeader.translate(f1 * this.mVelocityX, f1 * this.mVelocityY);
        this.mLastRunTime = l;
        f2 = 1000.0F * f1;
        if (this.mVelocityX <= 0.0F)
          break label190;
        this.mVelocityX -= f2;
        if (this.mVelocityX < 0.0F)
          this.mVelocityX = 0.0F;
        label100: if (this.mVelocityY <= 0.0F)
          break label218;
        this.mVelocityY -= f2;
        if (this.mVelocityY < 0.0F)
          this.mVelocityY = 0.0F;
      }
      while (true)
      {
        if (((this.mVelocityX == 0.0F) && (this.mVelocityY == 0.0F)) || (!bool))
        {
          stop();
          this.mHeader.snap();
        }
        if (this.mStop)
          break;
        this.mHeader.post(this);
        return;
        f1 = 0.0F;
        break label34;
        label190: this.mVelocityX = (f2 + this.mVelocityX);
        if (this.mVelocityX <= 0.0F)
          break label100;
        this.mVelocityX = 0.0F;
        break label100;
        label218: this.mVelocityY = (f2 + this.mVelocityY);
        if (this.mVelocityY > 0.0F)
          this.mVelocityY = 0.0F;
      }
    }

    public boolean start(float paramFloat1, float paramFloat2)
    {
      if (this.mRunning)
        return false;
      this.mLastRunTime = -1L;
      this.mVelocityX = paramFloat1;
      this.mVelocityY = paramFloat2;
      this.mStop = false;
      this.mRunning = true;
      this.mHeader.post(this);
      return true;
    }

    public void stop()
    {
      this.mRunning = false;
      this.mStop = true;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.views.PhotoView
 * JD-Core Version:    0.6.2
 */