package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FakeTimeLimiter
  implements TimeLimiter
{
  public <T> T callWithTimeout(Callable<T> paramCallable, long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean)
    throws Exception
  {
    return paramCallable.call();
  }

  public <T> T newProxy(T paramT, Class<T> paramClass, long paramLong, TimeUnit paramTimeUnit)
  {
    return paramT;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.FakeTimeLimiter
 * JD-Core Version:    0.6.2
 */