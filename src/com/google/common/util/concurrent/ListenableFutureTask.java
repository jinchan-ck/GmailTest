package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class ListenableFutureTask<V> extends FutureTask<V>
  implements ListenableFuture<V>
{
  private final ExecutionList executionList = new ExecutionList();

  public ListenableFutureTask(Runnable paramRunnable, V paramV)
  {
    super(paramRunnable, paramV);
  }

  public ListenableFutureTask(Callable<V> paramCallable)
  {
    super(paramCallable);
  }

  public void addListener(Runnable paramRunnable, Executor paramExecutor)
  {
    this.executionList.add(paramRunnable, paramExecutor);
  }

  protected void done()
  {
    this.executionList.run();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ListenableFutureTask
 * JD-Core Version:    0.6.2
 */