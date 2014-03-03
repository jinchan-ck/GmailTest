package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo;

class AccessibilityNodeInfoCompatIcs
{
  public static void addAction(Object paramObject, int paramInt)
  {
    ((AccessibilityNodeInfo)paramObject).addAction(paramInt);
  }

  public static void setClassName(Object paramObject, CharSequence paramCharSequence)
  {
    ((AccessibilityNodeInfo)paramObject).setClassName(paramCharSequence);
  }

  public static void setScrollable(Object paramObject, boolean paramBoolean)
  {
    ((AccessibilityNodeInfo)paramObject).setScrollable(paramBoolean);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs
 * JD-Core Version:    0.6.2
 */