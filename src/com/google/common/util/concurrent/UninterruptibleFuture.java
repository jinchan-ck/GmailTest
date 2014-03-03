package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract interface UninterruptibleFuture<V> extends Future<V>
{
  public abstract V get()
    throws ExecutionException;

  public abstract V get(long paramLong, TimeUnit paramTimeUnit)
    throws ExecutionException, TimeoutException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.UninterruptibleFuture
 * JD-Core Version:    0.6.2
 */