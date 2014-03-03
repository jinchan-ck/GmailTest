package com.google.android.gm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageButton;

class NumberPickerButton extends ImageButton
{
  private NumberPicker mNumberPicker;

  public NumberPickerButton(Context paramContext)
  {
    super(paramContext);
  }

  public NumberPickerButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public NumberPickerButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private void cancelLongpress()
  {
    if (2131361895 == getId())
      this.mNumberPicker.cancelIncrement();
    while (2131361897 != getId())
      return;
    this.mNumberPicker.cancelDecrement();
  }

  private void cancelLongpressIfRequired(MotionEvent paramMotionEvent)
  {
    if ((paramMotionEvent.getAction() == 3) || (paramMotionEvent.getAction() == 1))
      cancelLongpress();
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 23) || (paramInt == 66))
      cancelLongpress();
    return super.onKeyUp(paramInt, paramKeyEvent);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    cancelLongpressIfRequired(paramMotionEvent);
    return super.onTouchEvent(paramMotionEvent);
  }

  public boolean onTrackballEvent(MotionEvent paramMotionEvent)
  {
    cancelLongpressIfRequired(paramMotionEvent);
    return super.onTrackballEvent(paramMotionEvent);
  }

  public void setNumberPicker(NumberPicker paramNumberPicker)
  {
    this.mNumberPicker = paramNumberPicker;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.NumberPickerButton
 * JD-Core Version:    0.6.2
 */