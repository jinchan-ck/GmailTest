package com.google.common.collect;

import java.util.Set;

public abstract class ForwardingSet<E> extends ForwardingCollection<E>
  implements Set<E>
{
  protected abstract Set<E> delegate();

  public boolean equals(Object paramObject)
  {
    return (paramObject == this) || (delegate().equals(paramObject));
  }

  public int hashCode()
  {
    return delegate().hashCode();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingSet
 * JD-Core Version:    0.6.2
 */