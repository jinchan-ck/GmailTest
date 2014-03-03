package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E>
  implements Serializable
{
  private static final long serialVersionUID = -2250766705698539974L;
  private transient Map<E, AtomicInteger> backingMap;
  private transient AbstractMapBasedMultiset<E>.EntrySet entrySet;
  private transient long size;

  protected AbstractMapBasedMultiset(Map<E, AtomicInteger> paramMap)
  {
    this.backingMap = ((Map)Preconditions.checkNotNull(paramMap));
    this.size = super.size();
  }

  private static int getAndSet(AtomicInteger paramAtomicInteger, int paramInt)
  {
    if (paramAtomicInteger == null)
      return 0;
    return paramAtomicInteger.getAndSet(paramInt);
  }

  private void readObjectNoData()
    throws ObjectStreamException
  {
    throw new InvalidObjectException("Stream data required");
  }

  private int removeAllOccurrences(@Nullable Object paramObject, Map<E, AtomicInteger> paramMap)
  {
    AtomicInteger localAtomicInteger = (AtomicInteger)paramMap.remove(paramObject);
    if (localAtomicInteger == null)
      return 0;
    int i = localAtomicInteger.getAndSet(0);
    this.size -= i;
    return i;
  }

  public int add(@Nullable E paramE, int paramInt)
  {
    if (paramInt == 0)
      return count(paramE);
    if (paramInt > 0);
    AtomicInteger localAtomicInteger;
    for (boolean bool1 = true; ; bool1 = false)
    {
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool1, "occurrences cannot be negative: %s", arrayOfObject1);
      localAtomicInteger = (AtomicInteger)this.backingMap.get(paramE);
      if (localAtomicInteger != null)
        break;
      i = 0;
      this.backingMap.put(paramE, new AtomicInteger(paramInt));
      this.size += paramInt;
      return i;
    }
    int i = localAtomicInteger.get();
    long l = i + paramInt;
    if (l <= 2147483647L);
    for (boolean bool2 = true; ; bool2 = false)
    {
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Long.valueOf(l);
      Preconditions.checkArgument(bool2, "too many occurrences: %s", arrayOfObject2);
      localAtomicInteger.getAndAdd(paramInt);
      break;
    }
  }

  Map<E, AtomicInteger> backingMap()
  {
    return this.backingMap;
  }

  public int count(@Nullable Object paramObject)
  {
    AtomicInteger localAtomicInteger = (AtomicInteger)this.backingMap.get(paramObject);
    if (localAtomicInteger == null)
      return 0;
    return localAtomicInteger.get();
  }

  Set<E> createElementSet()
  {
    return new MapBasedElementSet(this.backingMap);
  }

  public Set<Multiset.Entry<E>> entrySet()
  {
    EntrySet localEntrySet = this.entrySet;
    if (localEntrySet == null)
    {
      localEntrySet = new EntrySet(null);
      this.entrySet = localEntrySet;
    }
    return localEntrySet;
  }

  public Iterator<E> iterator()
  {
    return new MapBasedMultisetIterator();
  }

  public int remove(@Nullable Object paramObject, int paramInt)
  {
    if (paramInt == 0)
      return count(paramObject);
    if (paramInt > 0);
    AtomicInteger localAtomicInteger;
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool, "occurrences cannot be negative: %s", arrayOfObject);
      localAtomicInteger = (AtomicInteger)this.backingMap.get(paramObject);
      if (localAtomicInteger != null)
        break;
      return 0;
    }
    int i = localAtomicInteger.get();
    int j;
    if (i > paramInt)
      j = paramInt;
    while (true)
    {
      localAtomicInteger.addAndGet(-j);
      this.size -= j;
      return i;
      j = i;
      this.backingMap.remove(paramObject);
    }
  }

  void setBackingMap(Map<E, AtomicInteger> paramMap)
  {
    this.backingMap = paramMap;
  }

  public int setCount(E paramE, int paramInt)
  {
    Multisets.checkNonnegative(paramInt, "count");
    int i;
    if (paramInt == 0)
      i = getAndSet((AtomicInteger)this.backingMap.remove(paramE), paramInt);
    while (true)
    {
      this.size += paramInt - i;
      return i;
      AtomicInteger localAtomicInteger = (AtomicInteger)this.backingMap.get(paramE);
      i = getAndSet(localAtomicInteger, paramInt);
      if (localAtomicInteger == null)
        this.backingMap.put(paramE, new AtomicInteger(paramInt));
    }
  }

  public int size()
  {
    return (int)Math.min(this.size, 2147483647L);
  }

  private class EntrySet extends AbstractSet<Multiset.Entry<E>>
  {
    private EntrySet()
    {
    }

    public void clear()
    {
      Iterator localIterator = AbstractMapBasedMultiset.this.backingMap.values().iterator();
      while (localIterator.hasNext())
        ((AtomicInteger)localIterator.next()).set(0);
      AbstractMapBasedMultiset.this.backingMap.clear();
      AbstractMapBasedMultiset.access$202(AbstractMapBasedMultiset.this, 0L);
    }

    public boolean contains(Object paramObject)
    {
      if ((paramObject instanceof Multiset.Entry))
      {
        Multiset.Entry localEntry = (Multiset.Entry)paramObject;
        int i = AbstractMapBasedMultiset.this.count(localEntry.getElement());
        return (i == localEntry.getCount()) && (i > 0);
      }
      return false;
    }

    public Iterator<Multiset.Entry<E>> iterator()
    {
      return new Iterator()
      {
        Map.Entry<E, AtomicInteger> toRemove;

        public boolean hasNext()
        {
          return this.val$backingEntries.hasNext();
        }

        public Multiset.Entry<E> next()
        {
          final Map.Entry localEntry = (Map.Entry)this.val$backingEntries.next();
          this.toRemove = localEntry;
          return new Multisets.AbstractEntry()
          {
            public int getCount()
            {
              int i = ((AtomicInteger)localEntry.getValue()).get();
              if (i == 0)
              {
                AtomicInteger localAtomicInteger = (AtomicInteger)AbstractMapBasedMultiset.this.backingMap.get(getElement());
                if (localAtomicInteger != null)
                  i = localAtomicInteger.get();
              }
              return i;
            }

            public E getElement()
            {
              return localEntry.getKey();
            }
          };
        }

        public void remove()
        {
          if (this.toRemove != null);
          for (boolean bool = true; ; bool = false)
          {
            Preconditions.checkState(bool, "no calls to next() since the last call to remove()");
            AbstractMapBasedMultiset.access$222(AbstractMapBasedMultiset.this, ((AtomicInteger)this.toRemove.getValue()).getAndSet(0));
            this.val$backingEntries.remove();
            this.toRemove = null;
            return;
          }
        }
      };
    }

    public boolean remove(Object paramObject)
    {
      if (contains(paramObject))
      {
        Multiset.Entry localEntry = (Multiset.Entry)paramObject;
        int i = ((AtomicInteger)AbstractMapBasedMultiset.this.backingMap.remove(localEntry.getElement())).getAndSet(0);
        AbstractMapBasedMultiset.access$222(AbstractMapBasedMultiset.this, i);
        return true;
      }
      return false;
    }

    public int size()
    {
      return AbstractMapBasedMultiset.this.backingMap.size();
    }
  }

  class MapBasedElementSet extends ForwardingSet<E>
  {
    private final Set<E> delegate;
    private final Map<E, AtomicInteger> map;

    MapBasedElementSet()
    {
      Object localObject;
      this.map = localObject;
      this.delegate = localObject.keySet();
    }

    public void clear()
    {
      if (this.map == AbstractMapBasedMultiset.this.backingMap)
        AbstractMapBasedMultiset.this.clear();
      while (true)
      {
        return;
        Iterator localIterator = iterator();
        while (localIterator.hasNext())
        {
          localIterator.next();
          localIterator.remove();
        }
      }
    }

    protected Set<E> delegate()
    {
      return this.delegate;
    }

    public Map<E, AtomicInteger> getMap()
    {
      return this.map;
    }

    public Iterator<E> iterator()
    {
      return new Iterator()
      {
        Map.Entry<E, AtomicInteger> toRemove;

        public boolean hasNext()
        {
          return this.val$entries.hasNext();
        }

        public E next()
        {
          this.toRemove = ((Map.Entry)this.val$entries.next());
          return this.toRemove.getKey();
        }

        public void remove()
        {
          if (this.toRemove != null);
          for (boolean bool = true; ; bool = false)
          {
            Preconditions.checkState(bool, "no calls to next() since the last call to remove()");
            AbstractMapBasedMultiset.access$222(AbstractMapBasedMultiset.this, ((AtomicInteger)this.toRemove.getValue()).getAndSet(0));
            this.val$entries.remove();
            this.toRemove = null;
            return;
          }
        }
      };
    }

    public boolean remove(Object paramObject)
    {
      return AbstractMapBasedMultiset.this.removeAllOccurrences(paramObject, this.map) != 0;
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      return Iterators.removeAll(iterator(), paramCollection);
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      return Iterators.retainAll(iterator(), paramCollection);
    }
  }

  private class MapBasedMultisetIterator
    implements Iterator<E>
  {
    boolean canRemove;
    Map.Entry<E, AtomicInteger> currentEntry;
    final Iterator<Map.Entry<E, AtomicInteger>> entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
    int occurrencesLeft;

    MapBasedMultisetIterator()
    {
    }

    public boolean hasNext()
    {
      return (this.occurrencesLeft > 0) || (this.entryIterator.hasNext());
    }

    public E next()
    {
      if (this.occurrencesLeft == 0)
      {
        this.currentEntry = ((Map.Entry)this.entryIterator.next());
        this.occurrencesLeft = ((AtomicInteger)this.currentEntry.getValue()).get();
      }
      this.occurrencesLeft -= 1;
      this.canRemove = true;
      return this.currentEntry.getKey();
    }

    public void remove()
    {
      Preconditions.checkState(this.canRemove, "no calls to next() since the last call to remove()");
      if (((AtomicInteger)this.currentEntry.getValue()).get() <= 0)
        throw new ConcurrentModificationException();
      if (((AtomicInteger)this.currentEntry.getValue()).addAndGet(-1) == 0)
        this.entryIterator.remove();
      AbstractMapBasedMultiset.access$210(AbstractMapBasedMultiset.this);
      this.canRemove = false;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractMapBasedMultiset
 * JD-Core Version:    0.6.2
 */