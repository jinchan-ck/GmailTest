package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;

public abstract class ForwardingCollection<E> extends ForwardingObject
  implements Collection<E>
{
  public boolean add(E paramE)
  {
    return delegate().add(paramE);
  }

  public boolean addAll(Collection<? extends E> paramCollection)
  {
    return delegate().addAll(paramCollection);
  }

  public void clear()
  {
    delegate().clear();
  }

  public boolean contains(Object paramObject)
  {
    return delegate().contains(paramObject);
  }

  public boolean containsAll(Collection<?> paramCollection)
  {
    return delegate().containsAll(paramCollection);
  }

  protected abstract Collection<E> delegate();

  public boolean isEmpty()
  {
    return delegate().isEmpty();
  }

  public Iterator<E> iterator()
  {
    return delegate().iterator();
  }

  public boolean remove(Object paramObject)
  {
    return delegate().remove(paramObject);
  }

  public boolean removeAll(Collection<?> paramCollection)
  {
    return delegate().removeAll(paramCollection);
  }

  public boolean retainAll(Collection<?> paramCollection)
  {
    return delegate().retainAll(paramCollection);
  }

  public int size()
  {
    return delegate().size();
  }

  protected boolean standardContainsAll(Collection<?> paramCollection)
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      if (!contains(localIterator.next()))
        return false;
    return true;
  }

  protected boolean standardRemoveAll(Collection<?> paramCollection)
  {
    return Iterators.removeAll(iterator(), paramCollection);
  }

  protected boolean standardRetainAll(Collection<?> paramCollection)
  {
    return Iterators.retainAll(iterator(), paramCollection);
  }

  protected Object[] standardToArray()
  {
    return toArray(new Object[size()]);
  }

  protected <T> T[] standardToArray(T[] paramArrayOfT)
  {
    return ObjectArrays.toArrayImpl(this, paramArrayOfT);
  }

  protected String standardToString()
  {
    return Collections2.toStringImpl(this);
  }

  public Object[] toArray()
  {
    return delegate().toArray();
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    return delegate().toArray(paramArrayOfT);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingCollection
 * JD-Core Version:    0.6.2
 */