package com.google.common.util.concurrent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Executors
{
  public static void addDelayedShutdownHook(ExecutorService paramExecutorService, final long paramLong, TimeUnit paramTimeUnit)
  {
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          this.val$service.shutdown();
          this.val$service.awaitTermination(paramLong, this.val$timeUnit);
          return;
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    }));
  }

  public static ThreadFactory daemonThreadFactory()
  {
    return daemonThreadFactory(java.util.concurrent.Executors.defaultThreadFactory());
  }

  public static ThreadFactory daemonThreadFactory(ThreadFactory paramThreadFactory)
  {
    return new DaemonThreadFactory(paramThreadFactory);
  }

  public static ExecutorService getExitingExecutorService(ThreadPoolExecutor paramThreadPoolExecutor)
  {
    return getExitingExecutorService(paramThreadPoolExecutor, 120L, TimeUnit.SECONDS);
  }

  public static ExecutorService getExitingExecutorService(ThreadPoolExecutor paramThreadPoolExecutor, long paramLong, TimeUnit paramTimeUnit)
  {
    paramThreadPoolExecutor.setThreadFactory(daemonThreadFactory(paramThreadPoolExecutor.getThreadFactory()));
    ExecutorService localExecutorService = java.util.concurrent.Executors.unconfigurableExecutorService(paramThreadPoolExecutor);
    addDelayedShutdownHook(localExecutorService, paramLong, paramTimeUnit);
    return localExecutorService;
  }

  public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor paramScheduledThreadPoolExecutor)
  {
    return getExitingScheduledExecutorService(paramScheduledThreadPoolExecutor, 120L, TimeUnit.SECONDS);
  }

  public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor paramScheduledThreadPoolExecutor, long paramLong, TimeUnit paramTimeUnit)
  {
    paramScheduledThreadPoolExecutor.setThreadFactory(daemonThreadFactory(paramScheduledThreadPoolExecutor.getThreadFactory()));
    ScheduledExecutorService localScheduledExecutorService = java.util.concurrent.Executors.unconfigurableScheduledExecutorService(paramScheduledThreadPoolExecutor);
    addDelayedShutdownHook(localScheduledExecutorService, paramLong, paramTimeUnit);
    return localScheduledExecutorService;
  }

  public static ExecutorService sameThreadExecutor()
  {
    return new SameThreadExecutorService(null);
  }

  private static class SameThreadExecutorService extends AbstractExecutorService
  {
    private final Lock lock = new ReentrantLock();
    private int runningTasks = 0;
    private boolean shutdown = false;
    private final Condition termination = this.lock.newCondition();

    private void endTask()
    {
      this.lock.lock();
      try
      {
        this.runningTasks -= 1;
        if (isTerminated())
          this.termination.signalAll();
        return;
      }
      finally
      {
        this.lock.unlock();
      }
    }

    private void startTask()
    {
      this.lock.lock();
      try
      {
        if (isShutdown())
          throw new RejectedExecutionException("Executor already shutdown");
      }
      finally
      {
        this.lock.unlock();
      }
      this.runningTasks = (1 + this.runningTasks);
      this.lock.unlock();
    }

    public boolean awaitTermination(long paramLong, TimeUnit paramTimeUnit)
      throws InterruptedException
    {
      long l1 = paramTimeUnit.toNanos(paramLong);
      this.lock.lock();
      try
      {
        while (true)
        {
          boolean bool = isTerminated();
          if (bool)
            return true;
          if (l1 <= 0L)
            return false;
          long l2 = this.termination.awaitNanos(l1);
          l1 = l2;
        }
      }
      finally
      {
        this.lock.unlock();
      }
    }

    public void execute(Runnable paramRunnable)
    {
      startTask();
      try
      {
        paramRunnable.run();
        return;
      }
      finally
      {
        endTask();
      }
    }

    public boolean isShutdown()
    {
      this.lock.lock();
      try
      {
        boolean bool = this.shutdown;
        return bool;
      }
      finally
      {
        this.lock.unlock();
      }
    }

    public boolean isTerminated()
    {
      this.lock.lock();
      try
      {
        if (this.shutdown)
        {
          int i = this.runningTasks;
          if (i == 0)
          {
            bool = true;
            return bool;
          }
        }
        boolean bool = false;
      }
      finally
      {
        this.lock.unlock();
      }
    }

    public void shutdown()
    {
      this.lock.lock();
      try
      {
        this.shutdown = true;
        return;
      }
      finally
      {
        this.lock.unlock();
      }
    }

    public List<Runnable> shutdownNow()
    {
      shutdown();
      return Collections.emptyList();
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.Executors
 * JD-Core Version:    0.6.2
 */