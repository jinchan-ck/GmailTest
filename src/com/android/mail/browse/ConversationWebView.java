package com.android.mail.browse;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.webkit.WebView;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConversationWebView extends WebView
  implements ScrollNotifier
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private Bitmap mBitmap;
  private int mCachedContentHeight;
  private Canvas mCanvas;
  private final float mDensity;
  private boolean mHandlingTouch;
  private final Runnable mNotifyPageRenderedInHardwareLayer = new Runnable()
  {
    public void run()
    {
      ConversationWebView.access$002(ConversationWebView.this, false);
      ConversationWebView.this.destroyBitmap();
      ConversationWebView.this.invalidate();
    }
  };
  private final Set<ScrollNotifier.ScrollListener> mScrollListeners = new CopyOnWriteArraySet();
  private ContentSizeChangeListener mSizeChangeListener;
  private boolean mUseSoftwareLayer;
  private final int mViewportWidth;
  private boolean mVisible;
  private final int mWebviewInitialDelay;

  public ConversationWebView(Context paramContext)
  {
    this(paramContext, null);
  }

  public ConversationWebView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = getResources();
    this.mViewportWidth = localResources.getInteger(2131296294);
    this.mWebviewInitialDelay = localResources.getInteger(2131296301);
    this.mDensity = localResources.getDisplayMetrics().density;
  }

  private void destroyBitmap()
  {
    if (this.mBitmap != null)
    {
      this.mBitmap = null;
      this.mCanvas = null;
    }
  }

  public void addScrollListener(ScrollNotifier.ScrollListener paramScrollListener)
  {
    this.mScrollListeners.add(paramScrollListener);
  }

  public int computeHorizontalScrollExtent()
  {
    return super.computeHorizontalScrollExtent();
  }

  public int computeHorizontalScrollOffset()
  {
    return super.computeHorizontalScrollOffset();
  }

  public int computeHorizontalScrollRange()
  {
    return super.computeHorizontalScrollRange();
  }

  public int computeVerticalScrollExtent()
  {
    return super.computeVerticalScrollExtent();
  }

  public int computeVerticalScrollOffset()
  {
    return super.computeVerticalScrollOffset();
  }

  public int computeVerticalScrollRange()
  {
    return super.computeVerticalScrollRange();
  }

  public void destroy()
  {
    destroyBitmap();
    removeCallbacks(this.mNotifyPageRenderedInHardwareLayer);
    super.destroy();
  }

  public float getInitialScale()
  {
    return this.mDensity;
  }

  public int getViewportWidth()
  {
    return this.mViewportWidth;
  }

  public void invalidate()
  {
    super.invalidate();
    if (this.mSizeChangeListener != null)
    {
      int i = getContentHeight();
      if (i != this.mCachedContentHeight)
      {
        this.mCachedContentHeight = i;
        this.mSizeChangeListener.onHeightChange(i);
      }
    }
  }

  public boolean isHandlingTouch()
  {
    return this.mHandlingTouch;
  }

  public void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if ((!this.mUseSoftwareLayer) || (!this.mVisible) || (getWidth() <= 0) || (getHeight() <= 0) || (this.mBitmap == null));
    try
    {
      this.mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
      this.mCanvas = new Canvas(this.mBitmap);
      if (this.mBitmap != null)
      {
        int i = getScrollX();
        int j = getScrollY();
        this.mCanvas.save();
        this.mCanvas.translate(-i, -j);
        super.onDraw(this.mCanvas);
        this.mCanvas.restore();
        paramCanvas.drawBitmap(this.mBitmap, i, j, null);
      }
      return;
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      while (true)
      {
        this.mBitmap = null;
        this.mCanvas = null;
        this.mUseSoftwareLayer = false;
      }
    }
  }

  public void onRenderComplete()
  {
    if (this.mUseSoftwareLayer)
      postDelayed(this.mNotifyPageRenderedInHardwareLayer, this.mWebviewInitialDelay);
  }

  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    Iterator localIterator = this.mScrollListeners.iterator();
    while (localIterator.hasNext())
      ((ScrollNotifier.ScrollListener)localIterator.next()).onNotifierScroll(paramInt1, paramInt2);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getActionMasked())
    {
    case 2:
    case 4:
    default:
    case 0:
    case 5:
    case 1:
    case 3:
    }
    while (true)
    {
      return super.onTouchEvent(paramMotionEvent);
      this.mHandlingTouch = true;
      continue;
      LogUtils.d(LOG_TAG, "WebView disabling intercepts: POINTER_DOWN", new Object[0]);
      requestDisallowInterceptTouchEvent(true);
      continue;
      this.mHandlingTouch = false;
    }
  }

  public void onUserVisibilityChanged(boolean paramBoolean)
  {
    this.mVisible = paramBoolean;
  }

  public int screenPxToWebPx(int paramInt)
  {
    return (int)(paramInt / getInitialScale());
  }

  public float screenPxToWebPxError(int paramInt)
  {
    return paramInt / getInitialScale() - screenPxToWebPx(paramInt);
  }

  public void setContentSizeChangeListener(ContentSizeChangeListener paramContentSizeChangeListener)
  {
    this.mSizeChangeListener = paramContentSizeChangeListener;
  }

  public void setUseSoftwareLayer(boolean paramBoolean)
  {
    this.mUseSoftwareLayer = paramBoolean;
  }

  public static abstract interface ContentSizeChangeListener
  {
    public abstract void onHeightChange(int paramInt);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ConversationWebView
 * JD-Core Version:    0.6.2
 */