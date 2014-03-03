package com.android.mail.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

public abstract class DelayedTaskHandler extends Handler
{
  private final int mDelayMs;
  private long mLastTaskExecuteTime = -1L;

  public DelayedTaskHandler(Looper paramLooper, int paramInt)
  {
    super(paramLooper);
    this.mDelayMs = paramInt;
  }

  public void dispatchMessage(Message paramMessage)
  {
    onTaskExecution();
    performTask();
  }

  public void onTaskExecution()
  {
    this.mLastTaskExecuteTime = SystemClock.elapsedRealtime();
  }

  protected abstract void performTask();

  public void scheduleTask()
  {
    long l = SystemClock.elapsedRealtime();
    removeMessages(0);
    if ((this.mLastTaskExecuteTime == -1L) || (this.mLastTaskExecuteTime + this.mDelayMs < l))
    {
      sendEmptyMessage(0);
      return;
    }
    sendEmptyMessageDelayed(0, this.mDelayMs);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.DelayedTaskHandler
 * JD-Core Version:    0.6.2
 */