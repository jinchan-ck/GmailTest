package android.support.v13.app;

import android.app.Fragment;
import android.os.Build.VERSION;

public class FragmentCompat
{
  static final FragmentCompatImpl IMPL = new BaseFragmentCompatImpl();

  static
  {
    if (Build.VERSION.SDK_INT >= 15)
    {
      IMPL = new ICSMR1FragmentCompatImpl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new ICSFragmentCompatImpl();
      return;
    }
  }

  public static void setMenuVisibility(Fragment paramFragment, boolean paramBoolean)
  {
    IMPL.setMenuVisibility(paramFragment, paramBoolean);
  }

  public static void setUserVisibleHint(Fragment paramFragment, boolean paramBoolean)
  {
    IMPL.setUserVisibleHint(paramFragment, paramBoolean);
  }

  static class BaseFragmentCompatImpl
    implements FragmentCompat.FragmentCompatImpl
  {
    public void setMenuVisibility(Fragment paramFragment, boolean paramBoolean)
    {
    }

    public void setUserVisibleHint(Fragment paramFragment, boolean paramBoolean)
    {
    }
  }

  static abstract interface FragmentCompatImpl
  {
    public abstract void setMenuVisibility(Fragment paramFragment, boolean paramBoolean);

    public abstract void setUserVisibleHint(Fragment paramFragment, boolean paramBoolean);
  }

  static class ICSFragmentCompatImpl extends FragmentCompat.BaseFragmentCompatImpl
  {
    public void setMenuVisibility(Fragment paramFragment, boolean paramBoolean)
    {
      FragmentCompatICS.setMenuVisibility(paramFragment, paramBoolean);
    }
  }

  static class ICSMR1FragmentCompatImpl extends FragmentCompat.ICSFragmentCompatImpl
  {
    public void setUserVisibleHint(Fragment paramFragment, boolean paramBoolean)
    {
      FragmentCompatICSMR1.setUserVisibleHint(paramFragment, paramBoolean);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     android.support.v13.app.FragmentCompat
 * JD-Core Version:    0.6.2
 */