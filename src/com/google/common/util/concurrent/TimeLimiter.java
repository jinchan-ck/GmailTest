package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public abstract interface TimeLimiter
{
  public abstract <T> T callWithTimeout(Callable<T> paramCallable, long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean)
    throws Exception;

  public abstract <T> T newProxy(T paramT, Class<T> paramClass, long paramLong, TimeUnit paramTimeUnit);
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.TimeLimiter
 * JD-Core Version:    0.6.2
 */