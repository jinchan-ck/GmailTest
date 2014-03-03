package android.support.v4.view.accessibility;

import android.os.Build.VERSION;

public class AccessibilityNodeInfoCompat
{
  private static final AccessibilityNodeInfoImpl IMPL = new AccessibilityNodeInfoStubImpl();
  private final Object mInfo;

  static
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      IMPL = new AccessibilityNodeInfoJellybeanImpl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new AccessibilityNodeInfoIcsImpl();
      return;
    }
  }

  public AccessibilityNodeInfoCompat(Object paramObject)
  {
    this.mInfo = paramObject;
  }

  public void addAction(int paramInt)
  {
    IMPL.addAction(this.mInfo, paramInt);
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat;
    do
    {
      do
      {
        return true;
        if (paramObject == null)
          return false;
        if (getClass() != paramObject.getClass())
          return false;
        localAccessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat)paramObject;
        if (this.mInfo != null)
          break;
      }
      while (localAccessibilityNodeInfoCompat.mInfo == null);
      return false;
    }
    while (this.mInfo.equals(localAccessibilityNodeInfoCompat.mInfo));
    return false;
  }

  public Object getInfo()
  {
    return this.mInfo;
  }

  public int hashCode()
  {
    if (this.mInfo == null)
      return 0;
    return this.mInfo.hashCode();
  }

  public void setClassName(CharSequence paramCharSequence)
  {
    IMPL.setClassName(this.mInfo, paramCharSequence);
  }

  public void setScrollable(boolean paramBoolean)
  {
    IMPL.setScrollable(this.mInfo, paramBoolean);
  }

  static class AccessibilityNodeInfoIcsImpl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoStubImpl
  {
    public void addAction(Object paramObject, int paramInt)
    {
      AccessibilityNodeInfoCompatIcs.addAction(paramObject, paramInt);
    }

    public void setClassName(Object paramObject, CharSequence paramCharSequence)
    {
      AccessibilityNodeInfoCompatIcs.setClassName(paramObject, paramCharSequence);
    }

    public void setScrollable(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatIcs.setScrollable(paramObject, paramBoolean);
    }
  }

  static abstract interface AccessibilityNodeInfoImpl
  {
    public abstract void addAction(Object paramObject, int paramInt);

    public abstract void setClassName(Object paramObject, CharSequence paramCharSequence);

    public abstract void setScrollable(Object paramObject, boolean paramBoolean);
  }

  static class AccessibilityNodeInfoJellybeanImpl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoIcsImpl
  {
  }

  static class AccessibilityNodeInfoStubImpl
    implements AccessibilityNodeInfoCompat.AccessibilityNodeInfoImpl
  {
    public void addAction(Object paramObject, int paramInt)
    {
    }

    public void setClassName(Object paramObject, CharSequence paramCharSequence)
    {
    }

    public void setScrollable(Object paramObject, boolean paramBoolean)
    {
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityNodeInfoCompat
 * JD-Core Version:    0.6.2
 */