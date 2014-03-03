package com.google.android.gm;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class LRUCache<K, V> extends LinkedHashMap<K, V>
{
  private static final long serialVersionUID = 1L;
  private final int maxCapacity;

  public LRUCache(int paramInt)
  {
    this(paramInt, paramInt);
  }

  public LRUCache(int paramInt1, int paramInt2)
  {
    super(paramInt1, 0.75F, true);
    this.maxCapacity = paramInt2;
  }

  public void addElement(K paramK, V paramV)
  {
    try
    {
      put(paramK, paramV);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public int getCapacity()
  {
    return this.maxCapacity;
  }

  public V getElement(K paramK)
  {
    try
    {
      Object localObject2 = get(paramK);
      return localObject2;
    }
    finally
    {
      localObject1 = finally;
      throw localObject1;
    }
  }

  public void putAll(Map<? extends K, ? extends V> paramMap)
  {
    try
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        put(localEntry.getKey(), localEntry.getValue());
      }
    }
    finally
    {
    }
  }

  protected boolean removeEldestEntry(Map.Entry<K, V> paramEntry)
  {
    try
    {
      int i = size();
      int j = this.maxCapacity;
      if (i > j)
      {
        bool = true;
        return bool;
      }
      boolean bool = false;
    }
    finally
    {
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LRUCache
 * JD-Core Version:    0.6.2
 */