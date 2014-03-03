package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.Iterator;

public final class Iterables
{
  public static <T> T getOnlyElement(Iterable<T> paramIterable)
  {
    return Iterators.getOnlyElement(paramIterable.iterator());
  }

  public static String toString(Iterable<?> paramIterable)
  {
    return Iterators.toString(paramIterable.iterator());
  }

  public static <F, T> Iterable<T> transform(Iterable<F> paramIterable, final Function<? super F, ? extends T> paramFunction)
  {
    Preconditions.checkNotNull(paramIterable);
    Preconditions.checkNotNull(paramFunction);
    return new IterableWithToString()
    {
      public Iterator<T> iterator()
      {
        return Iterators.transform(this.val$fromIterable.iterator(), paramFunction);
      }
    };
  }

  static abstract class IterableWithToString<E>
    implements Iterable<E>
  {
    public String toString()
    {
      return Iterables.toString(this);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Iterables
 * JD-Core Version:    0.6.2
 */