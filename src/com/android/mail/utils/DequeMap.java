package com.android.mail.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Deque;
import java.util.Map;

public class DequeMap<K, V>
{
  private final Map<K, Deque<V>> mMap = Maps.newHashMap();

  public void add(K paramK, V paramV)
  {
    Object localObject = (Deque)this.mMap.get(paramK);
    if (localObject == null)
    {
      localObject = Lists.newLinkedList();
      this.mMap.put(paramK, localObject);
    }
    ((Deque)localObject).add(paramV);
  }

  public V peek(K paramK)
  {
    Deque localDeque = (Deque)this.mMap.get(paramK);
    if (localDeque == null)
      return null;
    return localDeque.peek();
  }

  public V poll(K paramK)
  {
    Deque localDeque = (Deque)this.mMap.get(paramK);
    if (localDeque == null)
      return null;
    return localDeque.poll();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.DequeMap
 * JD-Core Version:    0.6.2
 */