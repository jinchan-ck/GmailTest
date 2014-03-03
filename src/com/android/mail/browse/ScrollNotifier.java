package com.android.mail.browse;

public abstract interface ScrollNotifier
{
  public abstract void addScrollListener(ScrollListener paramScrollListener);

  public abstract int computeHorizontalScrollExtent();

  public abstract int computeHorizontalScrollOffset();

  public abstract int computeHorizontalScrollRange();

  public abstract int computeVerticalScrollExtent();

  public abstract int computeVerticalScrollOffset();

  public abstract int computeVerticalScrollRange();

  public static abstract interface ScrollListener
  {
    public abstract void onNotifierScroll(int paramInt1, int paramInt2);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ScrollNotifier
 * JD-Core Version:    0.6.2
 */