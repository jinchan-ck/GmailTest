package com.android.mail.browse;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;
import java.lang.ref.WeakReference;

public class PriorityIndicatorSpan extends ReplacementSpan
{
  private final Context mContext;
  private WeakReference<Drawable> mDrawableRef;
  private final int mPaddingH;
  private final int mPaddingV;
  private final int mResId;

  public PriorityIndicatorSpan(Context paramContext, int paramInt1, int paramInt2, int paramInt3)
  {
    this.mContext = paramContext;
    this.mResId = paramInt1;
    this.mPaddingV = paramInt2;
    this.mPaddingH = paramInt3;
  }

  private Drawable getCachedDrawable()
  {
    WeakReference localWeakReference = this.mDrawableRef;
    Drawable localDrawable = null;
    if (localWeakReference != null)
      localDrawable = (Drawable)localWeakReference.get();
    if (localDrawable == null)
    {
      localDrawable = getDrawable();
      this.mDrawableRef = new WeakReference(localDrawable);
    }
    return localDrawable;
  }

  private Drawable getDrawable()
  {
    Drawable localDrawable = this.mContext.getResources().getDrawable(this.mResId);
    localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
    return localDrawable;
  }

  public void draw(Canvas paramCanvas, CharSequence paramCharSequence, int paramInt1, int paramInt2, float paramFloat, int paramInt3, int paramInt4, int paramInt5, Paint paramPaint)
  {
    Drawable localDrawable = getCachedDrawable();
    paramCanvas.save();
    int i = (paramInt3 + paramInt5 - localDrawable.getBounds().bottom) / 2;
    paramCanvas.translate(paramFloat + this.mPaddingH, i);
    localDrawable.draw(paramCanvas);
    paramCanvas.restore();
  }

  public int getSize(Paint paramPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, Paint.FontMetricsInt paramFontMetricsInt)
  {
    Rect localRect = getCachedDrawable().getBounds();
    if (paramFontMetricsInt != null)
    {
      paramPaint.getFontMetricsInt(paramFontMetricsInt);
      paramFontMetricsInt.ascent -= this.mPaddingV;
      paramFontMetricsInt.top = paramFontMetricsInt.ascent;
      paramFontMetricsInt.bottom += this.mPaddingV;
      paramFontMetricsInt.descent += this.mPaddingV;
    }
    return localRect.right + 2 * this.mPaddingH;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.PriorityIndicatorSpan
 * JD-Core Version:    0.6.2
 */