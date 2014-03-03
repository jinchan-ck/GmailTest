package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingList<E> extends ForwardingCollection<E>
  implements List<E>
{
  public void add(int paramInt, E paramE)
  {
    delegate().add(paramInt, paramE);
  }

  public boolean addAll(int paramInt, Collection<? extends E> paramCollection)
  {
    return delegate().addAll(paramInt, paramCollection);
  }

  protected abstract List<E> delegate();

  public boolean equals(@Nullable Object paramObject)
  {
    return (paramObject == this) || (delegate().equals(paramObject));
  }

  public E get(int paramInt)
  {
    return delegate().get(paramInt);
  }

  public int hashCode()
  {
    return delegate().hashCode();
  }

  public int indexOf(Object paramObject)
  {
    return delegate().indexOf(paramObject);
  }

  public int lastIndexOf(Object paramObject)
  {
    return delegate().lastIndexOf(paramObject);
  }

  public ListIterator<E> listIterator()
  {
    return delegate().listIterator();
  }

  public ListIterator<E> listIterator(int paramInt)
  {
    return delegate().listIterator(paramInt);
  }

  public E remove(int paramInt)
  {
    return delegate().remove(paramInt);
  }

  public E set(int paramInt, E paramE)
  {
    return delegate().set(paramInt, paramE);
  }

  @GwtIncompatible("List.subList")
  public List<E> subList(int paramInt1, int paramInt2)
  {
    return Platform.subList(delegate(), paramInt1, paramInt2);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingList
 * JD-Core Version:    0.6.2
 */