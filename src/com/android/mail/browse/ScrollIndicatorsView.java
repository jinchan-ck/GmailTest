package com.android.mail.browse;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ScrollIndicatorsView extends View
  implements ScrollNotifier.ScrollListener
{
  private ScrollNotifier mSource;

  public ScrollIndicatorsView(Context paramContext)
  {
    super(paramContext);
  }

  public ScrollIndicatorsView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected int computeHorizontalScrollExtent()
  {
    return this.mSource.computeHorizontalScrollExtent();
  }

  protected int computeHorizontalScrollOffset()
  {
    return this.mSource.computeHorizontalScrollOffset();
  }

  protected int computeHorizontalScrollRange()
  {
    return this.mSource.computeHorizontalScrollRange();
  }

  protected int computeVerticalScrollExtent()
  {
    return this.mSource.computeVerticalScrollExtent();
  }

  protected int computeVerticalScrollOffset()
  {
    return this.mSource.computeVerticalScrollOffset();
  }

  protected int computeVerticalScrollRange()
  {
    return this.mSource.computeVerticalScrollRange();
  }

  public void onNotifierScroll(int paramInt1, int paramInt2)
  {
    awakenScrollBars();
  }

  public void setSourceView(ScrollNotifier paramScrollNotifier)
  {
    this.mSource = paramScrollNotifier;
    this.mSource.addScrollListener(this);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ScrollIndicatorsView
 * JD-Core Version:    0.6.2
 */