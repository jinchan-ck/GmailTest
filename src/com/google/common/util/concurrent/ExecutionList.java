package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutionList
  implements Runnable
{
  private static final Logger LOG = Logger.getLogger(ExecutionList.class.getName());
  private boolean executed = false;
  private final List<RunnableExecutorPair> runnables = Lists.newArrayList();

  public void add(Runnable paramRunnable, Executor paramExecutor)
  {
    Preconditions.checkNotNull(paramRunnable, "Runnable was null.");
    Preconditions.checkNotNull(paramExecutor, "Executor was null.");
    int i = 0;
    synchronized (this.runnables)
    {
      if (!this.executed)
      {
        this.runnables.add(new RunnableExecutorPair(paramRunnable, paramExecutor));
        if (i != 0)
          paramExecutor.execute(paramRunnable);
        return;
      }
      i = 1;
    }
  }

  public void run()
  {
    synchronized (this.runnables)
    {
      this.executed = true;
      Iterator localIterator = this.runnables.iterator();
      if (localIterator.hasNext())
        ((RunnableExecutorPair)localIterator.next()).execute();
    }
  }

  private static class RunnableExecutorPair
  {
    final Executor executor;
    final Runnable runnable;

    RunnableExecutorPair(Runnable paramRunnable, Executor paramExecutor)
    {
      this.runnable = paramRunnable;
      this.executor = paramExecutor;
    }

    void execute()
    {
      try
      {
        this.executor.execute(this.runnable);
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        ExecutionList.LOG.log(Level.SEVERE, "RuntimeException while executing runnable " + this.runnable + " with executor " + this.executor, localRuntimeException);
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ExecutionList
 * JD-Core Version:    0.6.2
 */