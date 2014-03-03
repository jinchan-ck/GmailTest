package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.SortedSet;

@GwtCompatible
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E>
  implements SortedSet<E>
{
  public Comparator<? super E> comparator()
  {
    return delegate().comparator();
  }

  protected abstract SortedSet<E> delegate();

  public E first()
  {
    return delegate().first();
  }

  public SortedSet<E> headSet(E paramE)
  {
    return delegate().headSet(paramE);
  }

  public E last()
  {
    return delegate().last();
  }

  public SortedSet<E> subSet(E paramE1, E paramE2)
  {
    return delegate().subSet(paramE1, paramE2);
  }

  public SortedSet<E> tailSet(E paramE)
  {
    return delegate().tailSet(paramE);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingSortedSet
 * JD-Core Version:    0.6.2
 */