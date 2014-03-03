package com.google.common.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract interface CheckedFuture<V, E extends Exception> extends ListenableFuture<V>
{
  public abstract V checkedGet()
    throws Exception;

  public abstract V checkedGet(long paramLong, TimeUnit paramTimeUnit)
    throws TimeoutException, Exception;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.CheckedFuture
 * JD-Core Version:    0.6.2
 */