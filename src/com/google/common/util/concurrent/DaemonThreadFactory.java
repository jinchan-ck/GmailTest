package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory
  implements ThreadFactory
{
  private final ThreadFactory factory;

  public DaemonThreadFactory(ThreadFactory paramThreadFactory)
  {
    Preconditions.checkNotNull(paramThreadFactory);
    this.factory = paramThreadFactory;
  }

  public Thread newThread(Runnable paramRunnable)
  {
    Thread localThread = this.factory.newThread(paramRunnable);
    localThread.setDaemon(true);
    return localThread;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.DaemonThreadFactory
 * JD-Core Version:    0.6.2
 */