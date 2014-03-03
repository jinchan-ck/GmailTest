package com.google.common.util.concurrent;

import java.util.concurrent.Executor;

public abstract class AbstractListenableFuture<V> extends AbstractFuture<V>
  implements ListenableFuture<V>
{
  private final ExecutionList executionList = new ExecutionList();

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
 * Qualified Name:     com.google.common.util.concurrent.AbstractListenableFuture
 * JD-Core Version:    0.6.2
 */