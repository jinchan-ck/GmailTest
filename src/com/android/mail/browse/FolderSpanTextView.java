package com.android.mail.browse;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.TextView;

public class FolderSpanTextView extends TextView
  implements FolderSpan.FolderSpanDimensions
{
  private final int mFolderPadding;
  private final int mFolderPaddingBefore;
  private final int mFolderPaddingExtraWidth;
  private int mMaxSpanWidth;

  public FolderSpanTextView(Context paramContext)
  {
    this(paramContext, null);
  }

  public FolderSpanTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = getResources();
    this.mFolderPadding = localResources.getDimensionPixelOffset(2131361841);
    this.mFolderPaddingExtraWidth = localResources.getDimensionPixelOffset(2131361842);
    this.mFolderPaddingBefore = localResources.getDimensionPixelOffset(2131361843);
  }

  public int getMaxWidth()
  {
    return this.mMaxSpanWidth;
  }

  public int getPadding()
  {
    return this.mFolderPadding;
  }

  public int getPaddingBefore()
  {
    return this.mFolderPaddingBefore;
  }

  public int getPaddingExtraWidth()
  {
    return this.mFolderPaddingExtraWidth;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    this.mMaxSpanWidth = (View.MeasureSpec.getSize(paramInt1) - getTotalPaddingLeft() - getTotalPaddingRight());
    super.onMeasure(paramInt1, paramInt2);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.FolderSpanTextView
 * JD-Core Version:    0.6.2
 */