package com.android.mail.ui;

import android.os.Handler;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import java.util.concurrent.atomic.AtomicInteger;

public class UiHandler
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private AtomicInteger mCount = new AtomicInteger(0);
  private boolean mEnabled = true;
  private final Handler mHandler = new Handler();

  public void setEnabled(boolean paramBoolean)
  {
    this.mEnabled = paramBoolean;
    if (!this.mEnabled)
    {
      int i = this.mCount.getAndSet(0);
      if (i > 0)
      {
        String str = LOG_TAG;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(i);
        LogUtils.e(str, "Disable UiHandler. Dropping %d Runnables.", arrayOfObject);
      }
      this.mHandler.removeCallbacksAndMessages(null);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.UiHandler
 * JD-Core Version:    0.6.2
 */