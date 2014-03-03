package com.google.common.collect;

import java.util.Map.Entry;

public abstract class ForwardingMapEntry<K, V> extends ForwardingObject
  implements Map.Entry<K, V>
{
  protected abstract Map.Entry<K, V> delegate();

  public boolean equals(Object paramObject)
  {
    return delegate().equals(paramObject);
  }

  public K getKey()
  {
    return delegate().getKey();
  }

  public V getValue()
  {
    return delegate().getValue();
  }

  public int hashCode()
  {
    return delegate().hashCode();
  }

  public V setValue(V paramV)
  {
    return delegate().setValue(paramV);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingMapEntry
 * JD-Core Version:    0.6.2
 */