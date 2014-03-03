package com.google.android.gm;

import android.os.Handler;
import com.google.android.gm.provider.LogUtils;
import java.util.concurrent.atomic.AtomicInteger;

public class UiHandler
{
  private AtomicInteger mCount = new AtomicInteger(0);
  private boolean mEnabled = true;
  private final Handler mHandler = new Handler();

  public void post(final Runnable paramRunnable)
  {
    if (this.mEnabled)
    {
      this.mCount.incrementAndGet();
      this.mHandler.post(new Runnable()
      {
        public void run()
        {
          UiHandler.this.mCount.decrementAndGet();
          paramRunnable.run();
        }
      });
      return;
    }
    LogUtils.d("Gmail", "UiHandler is disabled in post(). Dropping Runnable.", new Object[0]);
  }

  public void setEnabled(boolean paramBoolean)
  {
    this.mEnabled = paramBoolean;
    if (!this.mEnabled)
    {
      int i = this.mCount.getAndSet(0);
      if (i > 0)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(i);
        LogUtils.e("Gmail", "Disable UiHandler. Dropping %d Runnables.", arrayOfObject);
      }
      this.mHandler.removeCallbacksAndMessages(null);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.UiHandler
 * JD-Core Version:    0.6.2
 */