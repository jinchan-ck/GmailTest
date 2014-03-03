package com.google.common.util.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractCheckedFuture<V, E extends Exception>
  implements CheckedFuture<V, E>
{
  protected final ListenableFuture<V> delegate;

  protected AbstractCheckedFuture(ListenableFuture<V> paramListenableFuture)
  {
    this.delegate = paramListenableFuture;
  }

  public void addListener(Runnable paramRunnable, Executor paramExecutor)
  {
    this.delegate.addListener(paramRunnable, paramExecutor);
  }

  public boolean cancel(boolean paramBoolean)
  {
    return this.delegate.cancel(paramBoolean);
  }

  public V checkedGet()
    throws Exception
  {
    try
    {
      Object localObject = get();
      return localObject;
    }
    catch (InterruptedException localInterruptedException)
    {
      cancel(true);
      throw mapException(localInterruptedException);
    }
    catch (CancellationException localCancellationException)
    {
      throw mapException(localCancellationException);
    }
    catch (ExecutionException localExecutionException)
    {
      throw mapException(localExecutionException);
    }
  }

  public V checkedGet(long paramLong, TimeUnit paramTimeUnit)
    throws TimeoutException, Exception
  {
    try
    {
      Object localObject = get(paramLong, paramTimeUnit);
      return localObject;
    }
    catch (InterruptedException localInterruptedException)
    {
      cancel(true);
      throw mapException(localInterruptedException);
    }
    catch (CancellationException localCancellationException)
    {
      throw mapException(localCancellationException);
    }
    catch (ExecutionException localExecutionException)
    {
      throw mapException(localExecutionException);
    }
  }

  public V get()
    throws InterruptedException, ExecutionException
  {
    return this.delegate.get();
  }

  public V get(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    return this.delegate.get(paramLong, paramTimeUnit);
  }

  public boolean isCancelled()
  {
    return this.delegate.isCancelled();
  }

  public boolean isDone()
  {
    return this.delegate.isDone();
  }

  protected abstract E mapException(Exception paramException);
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.AbstractCheckedFuture
 * JD-Core Version:    0.6.2
 */