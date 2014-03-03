package com.google.common.collect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E>
  implements Serializable
{
  private static final long serialVersionUID;
  private final transient ConcurrentMap<E, Integer> countMap;
  private transient ConcurrentHashMultiset<E>.EntrySet entrySet;

  @VisibleForTesting
  ConcurrentHashMultiset(ConcurrentMap<E, Integer> paramConcurrentMap)
  {
    Preconditions.checkArgument(paramConcurrentMap.isEmpty());
    this.countMap = paramConcurrentMap;
  }

  public static <E> ConcurrentHashMultiset<E> create()
  {
    return new ConcurrentHashMultiset(new ConcurrentHashMap());
  }

  public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> paramIterable)
  {
    ConcurrentHashMultiset localConcurrentHashMultiset = create();
    Iterables.addAll(localConcurrentHashMultiset, paramIterable);
    return localConcurrentHashMultiset;
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, new ConcurrentHashMap());
    Serialization.populateMultiset(this, paramObjectInputStream);
  }

  private int removeAllOccurrences(@Nullable Object paramObject)
  {
    try
    {
      int i = unbox((Integer)this.countMap.remove(paramObject));
      return i;
    }
    catch (NullPointerException localNullPointerException)
    {
      return 0;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    return 0;
  }

  private List<E> snapshot()
  {
    ArrayList localArrayList = Lists.newArrayListWithExpectedSize(size());
    Iterator localIterator = entrySet().iterator();
    while (localIterator.hasNext())
    {
      Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
      Object localObject = localEntry.getElement();
      for (int i = localEntry.getCount(); i > 0; i--)
        localArrayList.add(localObject);
    }
    return localArrayList;
  }

  private static int unbox(Integer paramInteger)
  {
    if (paramInteger == null)
      return 0;
    return paramInteger.intValue();
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    Serialization.writeMultiset(HashMultiset.create(this), paramObjectOutputStream);
  }

  public int add(E paramE, int paramInt)
  {
    if (paramInt == 0)
      return count(paramE);
    if (paramInt > 0);
    int i;
    for (boolean bool1 = true; ; bool1 = false)
    {
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool1, "Invalid occurrences: %s", arrayOfObject1);
      do
      {
        i = count(paramE);
        if (i != 0)
          break;
      }
      while (this.countMap.putIfAbsent(paramE, Integer.valueOf(paramInt)) != null);
      return 0;
    }
    if (paramInt <= 2147483647 - i);
    for (boolean bool2 = true; ; bool2 = false)
    {
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Integer.valueOf(paramInt);
      arrayOfObject2[1] = Integer.valueOf(i);
      Preconditions.checkArgument(bool2, "Overflow adding %s occurrences to a count of %s", arrayOfObject2);
      int j = i + paramInt;
      if (!this.countMap.replace(paramE, Integer.valueOf(i), Integer.valueOf(j)))
        break;
      return i;
    }
  }

  public int count(@Nullable Object paramObject)
  {
    try
    {
      int i = unbox((Integer)this.countMap.get(paramObject));
      return i;
    }
    catch (NullPointerException localNullPointerException)
    {
      return 0;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    return 0;
  }

  Set<E> createElementSet()
  {
    return new ForwardingSet()
    {
      protected Set<E> delegate()
      {
        return this.val$delegate;
      }

      public boolean remove(Object paramAnonymousObject)
      {
        try
        {
          boolean bool = this.val$delegate.remove(paramAnonymousObject);
          return bool;
        }
        catch (NullPointerException localNullPointerException)
        {
          return false;
        }
        catch (ClassCastException localClassCastException)
        {
        }
        return false;
      }
    };
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

  public int remove(@Nullable Object paramObject, int paramInt)
  {
    if (paramInt == 0)
      return count(paramObject);
    boolean bool;
    if (paramInt > 0)
    {
      bool = true;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool, "Invalid occurrences: %s", arrayOfObject);
    }
    int i;
    label84: 
    do
    {
      do
      {
        i = count(paramObject);
        if (i == 0)
        {
          return 0;
          bool = false;
          break;
        }
        if (paramInt < i)
          break label84;
      }
      while (!this.countMap.remove(paramObject, Integer.valueOf(i)));
      return i;
    }
    while (!this.countMap.replace(paramObject, Integer.valueOf(i), Integer.valueOf(i - paramInt)));
    return i;
  }

  public boolean removeExactly(@Nullable Object paramObject, int paramInt)
  {
    if (paramInt == 0)
      return true;
    boolean bool;
    if (paramInt > 0)
    {
      bool = true;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool, "Invalid occurrences: %s", arrayOfObject);
    }
    int i;
    label79: 
    do
    {
      do
      {
        i = count(paramObject);
        if (paramInt > i)
        {
          return false;
          bool = false;
          break;
        }
        if (paramInt != i)
          break label79;
      }
      while (!this.countMap.remove(paramObject, Integer.valueOf(paramInt)));
      return true;
    }
    while (!this.countMap.replace(paramObject, Integer.valueOf(i), Integer.valueOf(i - paramInt)));
    return true;
  }

  public int setCount(E paramE, int paramInt)
  {
    Multisets.checkNonnegative(paramInt, "count");
    if (paramInt == 0)
      return removeAllOccurrences(paramE);
    return unbox((Integer)this.countMap.put(paramE, Integer.valueOf(paramInt)));
  }

  public boolean setCount(E paramE, int paramInt1, int paramInt2)
  {
    Multisets.checkNonnegative(paramInt1, "oldCount");
    Multisets.checkNonnegative(paramInt2, "newCount");
    if (paramInt2 == 0)
    {
      if (paramInt1 == 0)
        return !this.countMap.containsKey(paramE);
      return this.countMap.remove(paramE, Integer.valueOf(paramInt1));
    }
    if (paramInt1 == 0)
      return this.countMap.putIfAbsent(paramE, Integer.valueOf(paramInt2)) == null;
    return this.countMap.replace(paramE, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
  }

  public int size()
  {
    long l = 0L;
    Iterator localIterator = this.countMap.values().iterator();
    while (localIterator.hasNext())
      l += ((Integer)localIterator.next()).intValue();
    return (int)Math.min(l, 2147483647L);
  }

  public Object[] toArray()
  {
    return snapshot().toArray();
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    return snapshot().toArray(paramArrayOfT);
  }

  private class EntrySet extends AbstractSet<Multiset.Entry<E>>
  {
    private EntrySet()
    {
    }

    private List<Multiset.Entry<E>> snapshot()
    {
      ArrayList localArrayList = Lists.newArrayListWithExpectedSize(size());
      Iterator localIterator = iterator();
      while (localIterator.hasNext())
        localArrayList.add((Multiset.Entry)localIterator.next());
      return localArrayList;
    }

    public void clear()
    {
      ConcurrentHashMultiset.this.countMap.clear();
    }

    public boolean contains(Object paramObject)
    {
      if ((paramObject instanceof Multiset.Entry))
      {
        Multiset.Entry localEntry = (Multiset.Entry)paramObject;
        Object localObject = localEntry.getElement();
        int i = localEntry.getCount();
        return (i > 0) && (ConcurrentHashMultiset.this.count(localObject) == i);
      }
      return false;
    }

    public int hashCode()
    {
      return ConcurrentHashMultiset.this.countMap.hashCode();
    }

    public boolean isEmpty()
    {
      return ConcurrentHashMultiset.this.countMap.isEmpty();
    }

    public Iterator<Multiset.Entry<E>> iterator()
    {
      return new Iterator()
      {
        public boolean hasNext()
        {
          return this.val$backingIterator.hasNext();
        }

        public Multiset.Entry<E> next()
        {
          Map.Entry localEntry = (Map.Entry)this.val$backingIterator.next();
          return Multisets.immutableEntry(localEntry.getKey(), ((Integer)localEntry.getValue()).intValue());
        }

        public void remove()
        {
          this.val$backingIterator.remove();
        }
      };
    }

    public boolean remove(Object paramObject)
    {
      if ((paramObject instanceof Multiset.Entry))
      {
        Multiset.Entry localEntry = (Multiset.Entry)paramObject;
        Object localObject = localEntry.getElement();
        int i = localEntry.getCount();
        return ConcurrentHashMultiset.this.countMap.remove(localObject, Integer.valueOf(i));
      }
      return false;
    }

    public int size()
    {
      return ConcurrentHashMultiset.this.countMap.size();
    }

    public Object[] toArray()
    {
      return snapshot().toArray();
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      return snapshot().toArray(paramArrayOfT);
    }
  }

  private static class FieldSettersHolder
  {
    static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ConcurrentHashMultiset
 * JD-Core Version:    0.6.2
 */