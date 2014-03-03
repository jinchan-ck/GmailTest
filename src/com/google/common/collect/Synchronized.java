package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final class Synchronized
{
  private static <E> Collection<E> collection(Collection<E> paramCollection, Object paramObject)
  {
    return new SynchronizedCollection(paramCollection, paramObject, null);
  }

  static <K, V> Map<K, V> map(Map<K, V> paramMap, Object paramObject)
  {
    return new SynchronizedMap(paramMap, paramObject);
  }

  static <E> Set<E> set(Set<E> paramSet, Object paramObject)
  {
    return new SynchronizedSet(paramSet, paramObject);
  }

  static class SynchronizedBiMap<K, V> extends Synchronized.SynchronizedMap<K, V>
    implements BiMap<K, V>, Serializable
  {
    private static final long serialVersionUID;
    private transient BiMap<V, K> inverse;
    private transient Set<V> valueSet;

    private SynchronizedBiMap(BiMap<K, V> paramBiMap, Object paramObject, BiMap<V, K> paramBiMap1)
    {
      super(paramObject);
      this.inverse = paramBiMap1;
    }

    BiMap<K, V> delegate()
    {
      return (BiMap)super.delegate();
    }

    public BiMap<V, K> inverse()
    {
      synchronized (this.mutex)
      {
        if (this.inverse == null)
          this.inverse = new SynchronizedBiMap(delegate().inverse(), this.mutex, this);
        BiMap localBiMap = this.inverse;
        return localBiMap;
      }
    }

    public Set<V> values()
    {
      synchronized (this.mutex)
      {
        if (this.valueSet == null)
          this.valueSet = Synchronized.set(delegate().values(), this.mutex);
        Set localSet = this.valueSet;
        return localSet;
      }
    }
  }

  static class SynchronizedCollection<E> extends Synchronized.SynchronizedObject
    implements Collection<E>
  {
    private static final long serialVersionUID;

    private SynchronizedCollection(Collection<E> paramCollection, Object paramObject)
    {
      super(paramObject);
    }

    public boolean add(E paramE)
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().add(paramE);
        return bool;
      }
    }

    public boolean addAll(Collection<? extends E> paramCollection)
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().addAll(paramCollection);
        return bool;
      }
    }

    public void clear()
    {
      synchronized (this.mutex)
      {
        delegate().clear();
        return;
      }
    }

    public boolean contains(Object paramObject)
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().contains(paramObject);
        return bool;
      }
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().containsAll(paramCollection);
        return bool;
      }
    }

    Collection<E> delegate()
    {
      return (Collection)super.delegate();
    }

    public boolean isEmpty()
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().isEmpty();
        return bool;
      }
    }

    public Iterator<E> iterator()
    {
      return delegate().iterator();
    }

    public boolean remove(Object paramObject)
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().remove(paramObject);
        return bool;
      }
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().removeAll(paramCollection);
        return bool;
      }
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().retainAll(paramCollection);
        return bool;
      }
    }

    public int size()
    {
      synchronized (this.mutex)
      {
        int i = delegate().size();
        return i;
      }
    }

    public Object[] toArray()
    {
      synchronized (this.mutex)
      {
        Object[] arrayOfObject = delegate().toArray();
        return arrayOfObject;
      }
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      synchronized (this.mutex)
      {
        Object[] arrayOfObject = delegate().toArray(paramArrayOfT);
        return arrayOfObject;
      }
    }
  }

  private static class SynchronizedMap<K, V> extends Synchronized.SynchronizedObject
    implements Map<K, V>
  {
    private static final long serialVersionUID;
    transient Set<Map.Entry<K, V>> entrySet;
    transient Set<K> keySet;
    transient Collection<V> values;

    SynchronizedMap(Map<K, V> paramMap, Object paramObject)
    {
      super(paramObject);
    }

    public void clear()
    {
      synchronized (this.mutex)
      {
        delegate().clear();
        return;
      }
    }

    public boolean containsKey(Object paramObject)
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().containsKey(paramObject);
        return bool;
      }
    }

    public boolean containsValue(Object paramObject)
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().containsValue(paramObject);
        return bool;
      }
    }

    Map<K, V> delegate()
    {
      return (Map)super.delegate();
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
      synchronized (this.mutex)
      {
        if (this.entrySet == null)
          this.entrySet = Synchronized.set(delegate().entrySet(), this.mutex);
        Set localSet = this.entrySet;
        return localSet;
      }
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this)
        return true;
      synchronized (this.mutex)
      {
        boolean bool = delegate().equals(paramObject);
        return bool;
      }
    }

    public V get(Object paramObject)
    {
      synchronized (this.mutex)
      {
        Object localObject3 = delegate().get(paramObject);
        return localObject3;
      }
    }

    public int hashCode()
    {
      synchronized (this.mutex)
      {
        int i = delegate().hashCode();
        return i;
      }
    }

    public boolean isEmpty()
    {
      synchronized (this.mutex)
      {
        boolean bool = delegate().isEmpty();
        return bool;
      }
    }

    public Set<K> keySet()
    {
      synchronized (this.mutex)
      {
        if (this.keySet == null)
          this.keySet = Synchronized.set(delegate().keySet(), this.mutex);
        Set localSet = this.keySet;
        return localSet;
      }
    }

    public V put(K paramK, V paramV)
    {
      synchronized (this.mutex)
      {
        Object localObject3 = delegate().put(paramK, paramV);
        return localObject3;
      }
    }

    public void putAll(Map<? extends K, ? extends V> paramMap)
    {
      synchronized (this.mutex)
      {
        delegate().putAll(paramMap);
        return;
      }
    }

    public V remove(Object paramObject)
    {
      synchronized (this.mutex)
      {
        Object localObject3 = delegate().remove(paramObject);
        return localObject3;
      }
    }

    public int size()
    {
      synchronized (this.mutex)
      {
        int i = delegate().size();
        return i;
      }
    }

    public Collection<V> values()
    {
      synchronized (this.mutex)
      {
        if (this.values == null)
          this.values = Synchronized.collection(delegate().values(), this.mutex);
        Collection localCollection = this.values;
        return localCollection;
      }
    }
  }

  static class SynchronizedObject
    implements Serializable
  {
    private static final long serialVersionUID;
    final Object delegate;
    final Object mutex;

    SynchronizedObject(Object paramObject1, Object paramObject2)
    {
      this.delegate = Preconditions.checkNotNull(paramObject1);
      if (paramObject2 == null)
        paramObject2 = this;
      this.mutex = paramObject2;
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      synchronized (this.mutex)
      {
        paramObjectOutputStream.defaultWriteObject();
        return;
      }
    }

    Object delegate()
    {
      return this.delegate;
    }

    public String toString()
    {
      synchronized (this.mutex)
      {
        String str = this.delegate.toString();
        return str;
      }
    }
  }

  static class SynchronizedSet<E> extends Synchronized.SynchronizedCollection<E>
    implements Set<E>
  {
    private static final long serialVersionUID;

    SynchronizedSet(Set<E> paramSet, Object paramObject)
    {
      super(paramObject, null);
    }

    Set<E> delegate()
    {
      return (Set)super.delegate();
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this)
        return true;
      synchronized (this.mutex)
      {
        boolean bool = delegate().equals(paramObject);
        return bool;
      }
    }

    public int hashCode()
    {
      synchronized (this.mutex)
      {
        int i = delegate().hashCode();
        return i;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Synchronized
 * JD-Core Version:    0.6.2
 */