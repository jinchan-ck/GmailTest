package com.google.common.util.concurrent;

import com.google.common.collect.ForwardingObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class ForwardingFuture<V> extends ForwardingObject
  implements Future<V>
{
  public boolean cancel(boolean paramBoolean)
  {
    return delegate().cancel(paramBoolean);
  }

  protected abstract Future<V> delegate();

  public V get()
    throws InterruptedException, ExecutionException
  {
    return delegate().get();
  }

  public V get(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    return delegate().get(paramLong, paramTimeUnit);
  }

  public boolean isCancelled()
  {
    return delegate().isCancelled();
  }

  public boolean isDone()
  {
    return delegate().isDone();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ForwardingFuture
 * JD-Core Version:    0.6.2
 */