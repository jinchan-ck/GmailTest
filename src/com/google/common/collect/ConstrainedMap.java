package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
class ConstrainedMap<K, V> extends ForwardingMap<K, V>
{
  final MapConstraint<? super K, ? super V> constraint;
  final Map<K, V> delegate;
  private volatile Set<Map.Entry<K, V>> entrySet;

  ConstrainedMap(Map<K, V> paramMap, MapConstraint<? super K, ? super V> paramMapConstraint)
  {
    this.delegate = ((Map)Preconditions.checkNotNull(paramMap));
    this.constraint = ((MapConstraint)Preconditions.checkNotNull(paramMapConstraint));
  }

  private static <K, V> Map.Entry<K, V> constrainedEntry(Map.Entry<K, V> paramEntry, final MapConstraint<? super K, ? super V> paramMapConstraint)
  {
    Preconditions.checkNotNull(paramEntry);
    Preconditions.checkNotNull(paramMapConstraint);
    return new ForwardingMapEntry()
    {
      protected Map.Entry<K, V> delegate()
      {
        return this.val$entry;
      }

      public V setValue(V paramAnonymousV)
      {
        paramMapConstraint.checkKeyValue(getKey(), paramAnonymousV);
        return this.val$entry.setValue(paramAnonymousV);
      }
    };
  }

  private static <K, V> Set<Map.Entry<K, V>> constrainedEntrySet(Set<Map.Entry<K, V>> paramSet, MapConstraint<? super K, ? super V> paramMapConstraint)
  {
    return new ConstrainedEntrySet(paramSet, paramMapConstraint);
  }

  protected Map<K, V> delegate()
  {
    return this.delegate;
  }

  public Set<Map.Entry<K, V>> entrySet()
  {
    if (this.entrySet == null)
      this.entrySet = constrainedEntrySet(this.delegate.entrySet(), this.constraint);
    return this.entrySet;
  }

  public V put(K paramK, V paramV)
  {
    this.constraint.checkKeyValue(paramK, paramV);
    return this.delegate.put(paramK, paramV);
  }

  public void putAll(Map<? extends K, ? extends V> paramMap)
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      put(localEntry.getKey(), localEntry.getValue());
    }
  }

  private static class ConstrainedEntries<K, V> extends ForwardingCollection<Map.Entry<K, V>>
  {
    final MapConstraint<? super K, ? super V> constraint;
    final Collection<Map.Entry<K, V>> entries;

    ConstrainedEntries(Collection<Map.Entry<K, V>> paramCollection, MapConstraint<? super K, ? super V> paramMapConstraint)
    {
      this.entries = paramCollection;
      this.constraint = paramMapConstraint;
    }

    public boolean contains(Object paramObject)
    {
      return Maps.containsEntryImpl(delegate(), paramObject);
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      return Collections2.containsAll(this, paramCollection);
    }

    protected Collection<Map.Entry<K, V>> delegate()
    {
      return this.entries;
    }

    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new ForwardingIterator()
      {
        protected Iterator<Map.Entry<K, V>> delegate()
        {
          return this.val$iterator;
        }

        public Map.Entry<K, V> next()
        {
          return ConstrainedMap.constrainedEntry((Map.Entry)this.val$iterator.next(), ConstrainedMap.ConstrainedEntries.this.constraint);
        }
      };
    }

    public boolean remove(Object paramObject)
    {
      return Maps.removeEntryImpl(delegate(), paramObject);
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      return Iterators.removeAll(iterator(), paramCollection);
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      return Iterators.retainAll(iterator(), paramCollection);
    }

    public Object[] toArray()
    {
      return ObjectArrays.toArrayImpl(this);
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      return ObjectArrays.toArrayImpl(this, paramArrayOfT);
    }
  }

  static class ConstrainedEntrySet<K, V> extends ConstrainedMap.ConstrainedEntries<K, V>
    implements Set<Map.Entry<K, V>>
  {
    ConstrainedEntrySet(Set<Map.Entry<K, V>> paramSet, MapConstraint<? super K, ? super V> paramMapConstraint)
    {
      super(paramMapConstraint);
    }

    public boolean equals(@Nullable Object paramObject)
    {
      return Collections2.setEquals(this, paramObject);
    }

    public int hashCode()
    {
      return Sets.hashCodeImpl(this);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ConstrainedMap
 * JD-Core Version:    0.6.2
 */