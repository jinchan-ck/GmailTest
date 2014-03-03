package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingMultiset<E> extends ForwardingCollection<E>
  implements Multiset<E>
{
  public int add(E paramE, int paramInt)
  {
    return delegate().add(paramE, paramInt);
  }

  public int count(Object paramObject)
  {
    return delegate().count(paramObject);
  }

  protected abstract Multiset<E> delegate();

  public Set<E> elementSet()
  {
    return delegate().elementSet();
  }

  public Set<Multiset.Entry<E>> entrySet()
  {
    return delegate().entrySet();
  }

  public boolean equals(@Nullable Object paramObject)
  {
    return (paramObject == this) || (delegate().equals(paramObject));
  }

  public int hashCode()
  {
    return delegate().hashCode();
  }

  public int remove(Object paramObject, int paramInt)
  {
    return delegate().remove(paramObject, paramInt);
  }

  public int setCount(E paramE, int paramInt)
  {
    return delegate().setCount(paramE, paramInt);
  }

  public boolean setCount(E paramE, int paramInt1, int paramInt2)
  {
    return delegate().setCount(paramE, paramInt1, paramInt2);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingMultiset
 * JD-Core Version:    0.6.2
 */