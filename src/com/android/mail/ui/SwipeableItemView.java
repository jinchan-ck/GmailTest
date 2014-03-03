package com.android.mail.ui;

import android.view.View;

public abstract interface SwipeableItemView
{
  public abstract boolean canChildBeDismissed();

  public abstract void dismiss();

  public abstract float getMinAllowScrollDistance();

  public abstract View getSwipeableView();
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.SwipeableItemView
 * JD-Core Version:    0.6.2
 */