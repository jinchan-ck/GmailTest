package com.google.common.collect;

import java.util.Map;
import java.util.Set;

public abstract interface BiMap<K, V> extends Map<K, V>
{
  public abstract BiMap<V, K> inverse();

  public abstract V put(K paramK, V paramV);

  public abstract Set<V> values();
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.BiMap
 * JD-Core Version:    0.6.2
 */