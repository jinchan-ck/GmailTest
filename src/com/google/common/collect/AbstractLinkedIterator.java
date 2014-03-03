package com.google.common.collect;

import java.util.NoSuchElementException;

public abstract class AbstractLinkedIterator<T> extends UnmodifiableIterator<T>
{
  private T nextOrNull;

  protected AbstractLinkedIterator(T paramT)
  {
    this.nextOrNull = paramT;
  }

  protected abstract T computeNext(T paramT);

  public final boolean hasNext()
  {
    return this.nextOrNull != null;
  }

  public final T next()
  {
    if (!hasNext())
      throw new NoSuchElementException();
    try
    {
      Object localObject2 = this.nextOrNull;
      return localObject2;
    }
    finally
    {
      this.nextOrNull = computeNext(this.nextOrNull);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractLinkedIterator
 * JD-Core Version:    0.6.2
 */