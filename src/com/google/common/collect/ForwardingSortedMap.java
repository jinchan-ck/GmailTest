package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.SortedMap;

@GwtCompatible
public abstract class ForwardingSortedMap<K, V> extends ForwardingMap<K, V>
  implements SortedMap<K, V>
{
  public Comparator<? super K> comparator()
  {
    return delegate().comparator();
  }

  protected abstract SortedMap<K, V> delegate();

  public K firstKey()
  {
    return delegate().firstKey();
  }

  public SortedMap<K, V> headMap(K paramK)
  {
    return delegate().headMap(paramK);
  }

  public K lastKey()
  {
    return delegate().lastKey();
  }

  public SortedMap<K, V> subMap(K paramK1, K paramK2)
  {
    return delegate().subMap(paramK1, paramK2);
  }

  public SortedMap<K, V> tailMap(K paramK)
  {
    return delegate().tailMap(paramK);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingSortedMap
 * JD-Core Version:    0.6.2
 */