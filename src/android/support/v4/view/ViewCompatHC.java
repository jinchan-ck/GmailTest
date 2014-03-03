package android.support.v4.view;

import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.view.View;

class ViewCompatHC
{
  static long getFrameTime()
  {
    return ValueAnimator.getFrameDelay();
  }

  public static void setLayerType(View paramView, int paramInt, Paint paramPaint)
  {
    paramView.setLayerType(paramInt, paramPaint);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewCompatHC
 * JD-Core Version:    0.6.2
 */