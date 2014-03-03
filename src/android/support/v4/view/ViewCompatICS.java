package android.support.v4.view;

import android.view.View;
import android.view.View.AccessibilityDelegate;

class ViewCompatICS
{
  public static boolean canScrollHorizontally(View paramView, int paramInt)
  {
    return paramView.canScrollHorizontally(paramInt);
  }

  public static void setAccessibilityDelegate(View paramView, Object paramObject)
  {
    paramView.setAccessibilityDelegate((View.AccessibilityDelegate)paramObject);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewCompatICS
 * JD-Core Version:    0.6.2
 */