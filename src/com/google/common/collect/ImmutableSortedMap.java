package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.SortedMap;

public class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V>
  implements SortedMap<K, V>
{
  private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(ImmutableList.of(), NATURAL_ORDER);
  private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
  private static final long serialVersionUID;
  private final transient Comparator<? super K> comparator;
  final transient ImmutableList<Map.Entry<K, V>> entries;
  private transient ImmutableSet<Map.Entry<K, V>> entrySet;
  private transient ImmutableSortedSet<K> keySet;
  private transient ImmutableCollection<V> values;

  ImmutableSortedMap(ImmutableList<Map.Entry<K, V>> paramImmutableList, Comparator<? super K> paramComparator)
  {
    this.entries = paramImmutableList;
    this.comparator = paramComparator;
  }

  private ImmutableSet<Map.Entry<K, V>> createEntrySet()
  {
    if (isEmpty())
      return ImmutableSet.of();
    return new EntrySet(this);
  }

  private ImmutableSortedSet<K> createKeySet()
  {
    if (isEmpty())
      return ImmutableSortedSet.emptySet(this.comparator);
    return new RegularImmutableSortedSet(new TransformedImmutableList(this.entries)
    {
      K transform(Map.Entry<K, V> paramAnonymousEntry)
      {
        return paramAnonymousEntry.getKey();
      }
    }
    , this.comparator);
  }

  private ImmutableSortedMap<K, V> createSubmap(int paramInt1, int paramInt2)
  {
    if (paramInt1 < paramInt2)
      return new ImmutableSortedMap(this.entries.subList(paramInt1, paramInt2), this.comparator);
    return emptyMap(this.comparator);
  }

  private static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> paramComparator)
  {
    if (NATURAL_ORDER.equals(paramComparator))
      return NATURAL_EMPTY_MAP;
    return new ImmutableSortedMap(ImmutableList.of(), paramComparator);
  }

  private int index(Object paramObject, SortedLists.KeyPresentBehavior paramKeyPresentBehavior, SortedLists.KeyAbsentBehavior paramKeyAbsentBehavior)
  {
    return SortedLists.binarySearch(keyList(), Preconditions.checkNotNull(paramObject), unsafeComparator(), paramKeyPresentBehavior, paramKeyAbsentBehavior);
  }

  private ImmutableList<K> keyList()
  {
    return new TransformedImmutableList(this.entries)
    {
      K transform(Map.Entry<K, V> paramAnonymousEntry)
      {
        return paramAnonymousEntry.getKey();
      }
    };
  }

  private static <K, V> void sortEntries(List<Map.Entry<K, V>> paramList, Comparator<? super K> paramComparator)
  {
    Collections.sort(paramList, new Comparator()
    {
      public int compare(Map.Entry<K, V> paramAnonymousEntry1, Map.Entry<K, V> paramAnonymousEntry2)
      {
        return this.val$comparator.compare(paramAnonymousEntry1.getKey(), paramAnonymousEntry2.getKey());
      }
    });
  }

  private static <K, V> void validateEntries(List<Map.Entry<K, V>> paramList, Comparator<? super K> paramComparator)
  {
    for (int i = 1; i < paramList.size(); i++)
      if (paramComparator.compare(((Map.Entry)paramList.get(i - 1)).getKey(), ((Map.Entry)paramList.get(i)).getKey()) == 0)
        throw new IllegalArgumentException("Duplicate keys in mappings " + paramList.get(i - 1) + " and " + paramList.get(i));
  }

  public Comparator<? super K> comparator()
  {
    return this.comparator;
  }

  public boolean containsValue(Object paramObject)
  {
    if (paramObject == null)
      return false;
    return Iterators.contains(valueIterator(), paramObject);
  }

  public ImmutableSet<Map.Entry<K, V>> entrySet()
  {
    ImmutableSet localImmutableSet = this.entrySet;
    if (localImmutableSet == null)
    {
      localImmutableSet = createEntrySet();
      this.entrySet = localImmutableSet;
    }
    return localImmutableSet;
  }

  public K firstKey()
  {
    if (isEmpty())
      throw new NoSuchElementException();
    return ((Map.Entry)this.entries.get(0)).getKey();
  }

  public V get(Object paramObject)
  {
    if (paramObject == null);
    while (true)
    {
      return null;
      try
      {
        int i = index(paramObject, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
        if (i >= 0)
          return ((Map.Entry)this.entries.get(i)).getValue();
      }
      catch (ClassCastException localClassCastException)
      {
      }
    }
    return null;
  }

  public ImmutableSortedMap<K, V> headMap(K paramK)
  {
    return headMap(paramK, false);
  }

  ImmutableSortedMap<K, V> headMap(K paramK, boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 1 + index(paramK, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER); ; i = index(paramK, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER))
      return createSubmap(0, i);
  }

  boolean isPartialView()
  {
    return this.entries.isPartialView();
  }

  public ImmutableSortedSet<K> keySet()
  {
    ImmutableSortedSet localImmutableSortedSet = this.keySet;
    if (localImmutableSortedSet == null)
    {
      localImmutableSortedSet = createKeySet();
      this.keySet = localImmutableSortedSet;
    }
    return localImmutableSortedSet;
  }

  public K lastKey()
  {
    if (isEmpty())
      throw new NoSuchElementException();
    return ((Map.Entry)this.entries.get(-1 + size())).getKey();
  }

  public int size()
  {
    return this.entries.size();
  }

  public ImmutableSortedMap<K, V> subMap(K paramK1, K paramK2)
  {
    return subMap(paramK1, true, paramK2, false);
  }

  ImmutableSortedMap<K, V> subMap(K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2)
  {
    Preconditions.checkNotNull(paramK1);
    Preconditions.checkNotNull(paramK2);
    if (this.comparator.compare(paramK1, paramK2) <= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return tailMap(paramK1, paramBoolean1).headMap(paramK2, paramBoolean2);
    }
  }

  public ImmutableSortedMap<K, V> tailMap(K paramK)
  {
    return tailMap(paramK, true);
  }

  ImmutableSortedMap<K, V> tailMap(K paramK, boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = index(paramK, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER); ; i = 1 + index(paramK, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER))
      return createSubmap(i, size());
  }

  Comparator<Object> unsafeComparator()
  {
    return this.comparator;
  }

  UnmodifiableIterator<V> valueIterator()
  {
    return new UnmodifiableIterator()
    {
      public boolean hasNext()
      {
        return this.val$entryIterator.hasNext();
      }

      public V next()
      {
        return ((Map.Entry)this.val$entryIterator.next()).getValue();
      }
    };
  }

  public ImmutableCollection<V> values()
  {
    Object localObject = this.values;
    if (localObject == null)
    {
      localObject = new Values(this);
      this.values = ((ImmutableCollection)localObject);
    }
    return localObject;
  }

  Object writeReplace()
  {
    return new SerializedForm(this);
  }

  public static class Builder<K, V> extends ImmutableMap.Builder<K, V>
  {
    private final Comparator<? super K> comparator;

    public Builder(Comparator<? super K> paramComparator)
    {
      this.comparator = ((Comparator)Preconditions.checkNotNull(paramComparator));
    }

    public ImmutableSortedMap<K, V> build()
    {
      ImmutableSortedMap.sortEntries(this.entries, this.comparator);
      ImmutableSortedMap.validateEntries(this.entries, this.comparator);
      return new ImmutableSortedMap(ImmutableList.copyOf(this.entries), this.comparator);
    }

    public Builder<K, V> put(K paramK, V paramV)
    {
      this.entries.add(ImmutableMap.entryOf(paramK, paramV));
      return this;
    }
  }

  private static class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>>
  {
    final transient ImmutableSortedMap<K, V> map;

    EntrySet(ImmutableSortedMap<K, V> paramImmutableSortedMap)
    {
      this.map = paramImmutableSortedMap;
    }

    public boolean contains(Object paramObject)
    {
      boolean bool1 = paramObject instanceof Map.Entry;
      boolean bool2 = false;
      if (bool1)
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        Object localObject = this.map.get(localEntry.getKey());
        bool2 = false;
        if (localObject != null)
        {
          boolean bool3 = localObject.equals(localEntry.getValue());
          bool2 = false;
          if (bool3)
            bool2 = true;
        }
      }
      return bool2;
    }

    boolean isPartialView()
    {
      return this.map.isPartialView();
    }

    public UnmodifiableIterator<Map.Entry<K, V>> iterator()
    {
      return this.map.entries.iterator();
    }

    public int size()
    {
      return this.map.size();
    }

    Object writeReplace()
    {
      return new ImmutableSortedMap.EntrySetSerializedForm(this.map);
    }
  }

  private static class EntrySetSerializedForm<K, V>
    implements Serializable
  {
    private static final long serialVersionUID;
    final ImmutableSortedMap<K, V> map;

    EntrySetSerializedForm(ImmutableSortedMap<K, V> paramImmutableSortedMap)
    {
      this.map = paramImmutableSortedMap;
    }

    Object readResolve()
    {
      return this.map.entrySet();
    }
  }

  private static class SerializedForm extends ImmutableMap.SerializedForm
  {
    private static final long serialVersionUID;
    private final Comparator<Object> comparator;

    SerializedForm(ImmutableSortedMap<?, ?> paramImmutableSortedMap)
    {
      super();
      this.comparator = paramImmutableSortedMap.comparator();
    }

    Object readResolve()
    {
      return createMap(new ImmutableSortedMap.Builder(this.comparator));
    }
  }

  private static class Values<V> extends ImmutableCollection<V>
  {
    private final ImmutableSortedMap<?, V> map;

    Values(ImmutableSortedMap<?, V> paramImmutableSortedMap)
    {
      this.map = paramImmutableSortedMap;
    }

    public boolean contains(Object paramObject)
    {
      return this.map.containsValue(paramObject);
    }

    boolean isPartialView()
    {
      return true;
    }

    public UnmodifiableIterator<V> iterator()
    {
      return this.map.valueIterator();
    }

    public int size()
    {
      return this.map.size();
    }

    Object writeReplace()
    {
      return new ImmutableSortedMap.ValuesSerializedForm(this.map);
    }
  }

  private static class ValuesSerializedForm<V>
    implements Serializable
  {
    private static final long serialVersionUID;
    final ImmutableSortedMap<?, V> map;

    ValuesSerializedForm(ImmutableSortedMap<?, V> paramImmutableSortedMap)
    {
      this.map = paramImmutableSortedMap;
    }

    Object readResolve()
    {
      return this.map.values();
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableSortedMap
 * JD-Core Version:    0.6.2
 */