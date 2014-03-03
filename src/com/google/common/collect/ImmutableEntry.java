package com.google.common.collect;

import java.io.Serializable;

class ImmutableEntry<K, V> extends AbstractMapEntry<K, V>
  implements Serializable
{
  private static final long serialVersionUID;
  private final K key;
  private final V value;

  ImmutableEntry(K paramK, V paramV)
  {
    this.key = paramK;
    this.value = paramV;
  }

  public K getKey()
  {
    return this.key;
  }

  public V getValue()
  {
    return this.value;
  }

  public final V setValue(V paramV)
  {
    throw new UnsupportedOperationException();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableEntry
 * JD-Core Version:    0.6.2
 */