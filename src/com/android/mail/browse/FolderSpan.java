package com.android.mail.browse;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.text.style.ReplacementSpan;

public class FolderSpan extends ReplacementSpan
{
  private final FolderSpanDimensions mDims;
  private final Spanned mSpanned;
  private TextPaint mWorkPaint = new TextPaint();

  public FolderSpan(Spanned paramSpanned, FolderSpanDimensions paramFolderSpanDimensions)
  {
    this.mSpanned = paramSpanned;
    this.mDims = paramFolderSpanDimensions;
  }

  private int measureWidth(Paint paramPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = this.mDims.getPadding() + this.mDims.getPaddingExtraWidth();
    int j = this.mDims.getMaxWidth();
    int k = (int)paramPaint.measureText(paramCharSequence, paramInt1, paramInt2) + i * 2;
    if (paramBoolean)
      k += this.mDims.getPaddingBefore();
    if (k > j)
      k = j;
    return k;
  }

  public void draw(Canvas paramCanvas, CharSequence paramCharSequence, int paramInt1, int paramInt2, float paramFloat, int paramInt3, int paramInt4, int paramInt5, Paint paramPaint)
  {
    int i = this.mDims.getPadding() + this.mDims.getPaddingExtraWidth();
    int j = this.mDims.getPaddingBefore();
    int k = this.mDims.getMaxWidth();
    this.mWorkPaint.set(paramPaint);
    CharacterStyle[] arrayOfCharacterStyle = (CharacterStyle[])this.mSpanned.getSpans(paramInt1, paramInt2, CharacterStyle.class);
    int m = arrayOfCharacterStyle.length;
    for (int n = 0; n < m; n++)
      arrayOfCharacterStyle[n].updateDrawState(this.mWorkPaint);
    int i1 = measureWidth(this.mWorkPaint, paramCharSequence, paramInt1, paramInt2, false);
    if (this.mWorkPaint.bgColor != 0)
    {
      int i4 = this.mWorkPaint.getColor();
      Paint.Style localStyle = this.mWorkPaint.getStyle();
      this.mWorkPaint.setColor(this.mWorkPaint.bgColor);
      this.mWorkPaint.setStyle(Paint.Style.FILL);
      paramCanvas.drawRect(paramFloat + j, paramInt3, paramFloat + i1 + j, paramInt5, this.mWorkPaint);
      this.mWorkPaint.setColor(i4);
      this.mWorkPaint.setStyle(localStyle);
    }
    CharSequence localCharSequence;
    int i3;
    int i2;
    if (i1 == k)
    {
      localCharSequence = TextUtils.ellipsize(paramCharSequence.subSequence(paramInt1, paramInt2).toString(), this.mWorkPaint, i1 - i * 2, TextUtils.TruncateAt.MIDDLE);
      i3 = 0;
      i2 = localCharSequence.length();
    }
    while (true)
    {
      paramCanvas.drawText(localCharSequence, i3, i2, paramFloat + i + j, paramInt4, this.mWorkPaint);
      return;
      i2 = paramInt2;
      i3 = paramInt1;
      localCharSequence = paramCharSequence;
    }
  }

  public int getSize(Paint paramPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, Paint.FontMetricsInt paramFontMetricsInt)
  {
    if (paramFontMetricsInt != null)
    {
      int i = this.mDims.getPadding();
      paramPaint.getFontMetricsInt(paramFontMetricsInt);
      paramFontMetricsInt.ascent -= i;
      paramFontMetricsInt.top = paramFontMetricsInt.ascent;
      paramFontMetricsInt.bottom = (i + paramFontMetricsInt.bottom);
      paramFontMetricsInt.descent = (i + paramFontMetricsInt.descent);
    }
    return measureWidth(paramPaint, paramCharSequence, paramInt1, paramInt2, true);
  }

  public static abstract interface FolderSpanDimensions
  {
    public abstract int getMaxWidth();

    public abstract int getPadding();

    public abstract int getPaddingBefore();

    public abstract int getPaddingExtraWidth();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.FolderSpan
 * JD-Core Version:    0.6.2
 */