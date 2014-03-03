package com.google.common.util.concurrent;

public class ValueFuture<V> extends AbstractListenableFuture<V>
{
  public static <T> ValueFuture<T> create()
  {
    return new ValueFuture();
  }

  public boolean cancel(boolean paramBoolean)
  {
    return super.cancel();
  }

  public boolean set(V paramV)
  {
    return super.set(paramV);
  }

  public boolean setException(Throwable paramThrowable)
  {
    return super.setException(paramThrowable);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ValueFuture
 * JD-Core Version:    0.6.2
 */