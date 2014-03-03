package com.google.android.gm;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

public class CheckableTextView extends TextView
  implements Checkable
{
  private static final int[] CHECKED_STATE_SET = { 16842912 };
  private boolean mChecked;

  public CheckableTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean isChecked()
  {
    return this.mChecked;
  }

  public int[] onCreateDrawableState(int paramInt)
  {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isChecked())
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET);
    return arrayOfInt;
  }

  public void setChecked(boolean paramBoolean)
  {
    if (this.mChecked != paramBoolean)
    {
      this.mChecked = paramBoolean;
      refreshDrawableState();
    }
  }

  public void toggle()
  {
    if (!this.mChecked);
    for (boolean bool = true; ; bool = false)
    {
      setChecked(bool);
      return;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.CheckableTextView
 * JD-Core Version:    0.6.2
 */