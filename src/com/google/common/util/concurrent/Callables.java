package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import javax.annotation.Nullable;

public final class Callables
{
  public static <T> Callable<T> returning(@Nullable T paramT)
  {
    return new Callable()
    {
      public T call()
      {
        return this.val$value;
      }
    };
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.Callables
 * JD-Core Version:    0.6.2
 */