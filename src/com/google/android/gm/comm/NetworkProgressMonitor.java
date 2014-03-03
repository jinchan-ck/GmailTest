package com.google.android.gm.comm;

import android.app.Activity;
import android.view.Window;
import com.google.android.gm.IProgressMonitor;

public class NetworkProgressMonitor
  implements IProgressMonitor
{
  public static final String TEXT_VIEW_ID = "gmail:loading";
  private Activity mActivity;
  private Runnable mDoneRunnable;

  public NetworkProgressMonitor(Activity paramActivity, Runnable paramRunnable)
  {
    this.mActivity = paramActivity;
    this.mDoneRunnable = paramRunnable;
  }

  public void beginTask(CharSequence paramCharSequence, int paramInt)
  {
    this.mActivity.getWindow().setFeatureInt(5, -1);
  }

  public void done()
  {
    if (this.mDoneRunnable != null)
      this.mDoneRunnable.run();
    this.mActivity.getWindow().setFeatureInt(5, -2);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.comm.NetworkProgressMonitor
 * JD-Core Version:    0.6.2
 */