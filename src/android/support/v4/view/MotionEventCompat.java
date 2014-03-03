package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.MotionEvent;

public class MotionEventCompat
{
  static final MotionEventVersionImpl IMPL = new BaseMotionEventVersionImpl();

  static
  {
    if (Build.VERSION.SDK_INT >= 5)
    {
      IMPL = new EclairMotionEventVersionImpl();
      return;
    }
  }

  public static int findPointerIndex(MotionEvent paramMotionEvent, int paramInt)
  {
    return IMPL.findPointerIndex(paramMotionEvent, paramInt);
  }

  public static int getActionIndex(MotionEvent paramMotionEvent)
  {
    return (0xFF00 & paramMotionEvent.getAction()) >> 8;
  }

  public static int getPointerCount(MotionEvent paramMotionEvent)
  {
    return IMPL.getPointerCount(paramMotionEvent);
  }

  public static int getPointerId(MotionEvent paramMotionEvent, int paramInt)
  {
    return IMPL.getPointerId(paramMotionEvent, paramInt);
  }

  public static float getX(MotionEvent paramMotionEvent, int paramInt)
  {
    return IMPL.getX(paramMotionEvent, paramInt);
  }

  public static float getY(MotionEvent paramMotionEvent, int paramInt)
  {
    return IMPL.getY(paramMotionEvent, paramInt);
  }

  static class BaseMotionEventVersionImpl
    implements MotionEventCompat.MotionEventVersionImpl
  {
    public int findPointerIndex(MotionEvent paramMotionEvent, int paramInt)
    {
      if (paramInt == 0)
        return 0;
      return -1;
    }

    public int getPointerCount(MotionEvent paramMotionEvent)
    {
      return 1;
    }

    public int getPointerId(MotionEvent paramMotionEvent, int paramInt)
    {
      if (paramInt == 0)
        return 0;
      throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
    }

    public float getX(MotionEvent paramMotionEvent, int paramInt)
    {
      if (paramInt == 0)
        return paramMotionEvent.getX();
      throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
    }

    public float getY(MotionEvent paramMotionEvent, int paramInt)
    {
      if (paramInt == 0)
        return paramMotionEvent.getY();
      throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
    }
  }

  static class EclairMotionEventVersionImpl
    implements MotionEventCompat.MotionEventVersionImpl
  {
    public int findPointerIndex(MotionEvent paramMotionEvent, int paramInt)
    {
      return MotionEventCompatEclair.findPointerIndex(paramMotionEvent, paramInt);
    }

    public int getPointerCount(MotionEvent paramMotionEvent)
    {
      return MotionEventCompatEclair.getPointerCount(paramMotionEvent);
    }

    public int getPointerId(MotionEvent paramMotionEvent, int paramInt)
    {
      return MotionEventCompatEclair.getPointerId(paramMotionEvent, paramInt);
    }

    public float getX(MotionEvent paramMotionEvent, int paramInt)
    {
      return MotionEventCompatEclair.getX(paramMotionEvent, paramInt);
    }

    public float getY(MotionEvent paramMotionEvent, int paramInt)
    {
      return MotionEventCompatEclair.getY(paramMotionEvent, paramInt);
    }
  }

  static abstract interface MotionEventVersionImpl
  {
    public abstract int findPointerIndex(MotionEvent paramMotionEvent, int paramInt);

    public abstract int getPointerCount(MotionEvent paramMotionEvent);

    public abstract int getPointerId(MotionEvent paramMotionEvent, int paramInt);

    public abstract float getX(MotionEvent paramMotionEvent, int paramInt);

    public abstract float getY(MotionEvent paramMotionEvent, int paramInt);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.MotionEventCompat
 * JD-Core Version:    0.6.2
 */