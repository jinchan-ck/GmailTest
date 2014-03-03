package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultimap<K, V>
  implements Multimap<K, V>, Serializable
{
  private static final long serialVersionUID = 2447537837011683357L;
  private transient Map<K, Collection<V>> asMap;
  private transient Collection<Map.Entry<K, V>> entries;
  private transient Set<K> keySet;
  private transient Map<K, Collection<V>> map;
  private transient Multiset<K> multiset;
  private transient int totalSize;
  private transient Collection<V> valuesCollection;

  protected AbstractMultimap(Map<K, Collection<V>> paramMap)
  {
    Preconditions.checkArgument(paramMap.isEmpty());
    this.map = paramMap;
  }

  private Map<K, Collection<V>> createAsMap()
  {
    if ((this.map instanceof SortedMap))
      return new SortedAsMap((SortedMap)this.map);
    return new AsMap(this.map);
  }

  private Collection<Map.Entry<K, V>> createEntries()
  {
    if ((this instanceof SetMultimap))
      return new EntrySet(null);
    return new Entries(null);
  }

  private Set<K> createKeySet()
  {
    if ((this.map instanceof SortedMap))
      return new SortedKeySet((SortedMap)this.map);
    return new KeySet(this.map);
  }

  private Collection<V> getOrCreateCollection(@Nullable K paramK)
  {
    Collection localCollection = (Collection)this.map.get(paramK);
    if (localCollection == null)
    {
      localCollection = createCollection(paramK);
      this.map.put(paramK, localCollection);
    }
    return localCollection;
  }

  private Iterator<V> iteratorOrListIterator(Collection<V> paramCollection)
  {
    if ((paramCollection instanceof List))
      return ((List)paramCollection).listIterator();
    return paramCollection.iterator();
  }

  private int removeValuesForKey(Object paramObject)
  {
    try
    {
      Collection localCollection = (Collection)this.map.remove(paramObject);
      int i = 0;
      if (localCollection != null)
      {
        i = localCollection.size();
        localCollection.clear();
        this.totalSize -= i;
      }
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

  private Collection<V> unmodifiableCollectionSubclass(Collection<V> paramCollection)
  {
    if ((paramCollection instanceof SortedSet))
      return Collections.unmodifiableSortedSet((SortedSet)paramCollection);
    if ((paramCollection instanceof Set))
      return Collections.unmodifiableSet((Set)paramCollection);
    if ((paramCollection instanceof List))
      return Collections.unmodifiableList((List)paramCollection);
    return Collections.unmodifiableCollection(paramCollection);
  }

  private Collection<V> wrapCollection(@Nullable K paramK, Collection<V> paramCollection)
  {
    if ((paramCollection instanceof SortedSet))
      return new WrappedSortedSet(paramK, (SortedSet)paramCollection, null);
    if ((paramCollection instanceof Set))
      return new WrappedSet(paramK, (Set)paramCollection);
    if ((paramCollection instanceof List))
      return wrapList(paramK, (List)paramCollection, null);
    return new WrappedCollection(paramK, paramCollection, null);
  }

  private List<V> wrapList(K paramK, List<V> paramList, @Nullable AbstractMultimap<K, V>.WrappedCollection paramAbstractMultimap)
  {
    if ((paramList instanceof RandomAccess))
      return new RandomAccessWrappedList(paramK, paramList, paramAbstractMultimap);
    return new WrappedList(paramK, paramList, paramAbstractMultimap);
  }

  public Map<K, Collection<V>> asMap()
  {
    Map localMap1 = this.asMap;
    if (localMap1 == null)
    {
      Map localMap2 = createAsMap();
      this.asMap = localMap2;
      return localMap2;
    }
    return localMap1;
  }

  Map<K, Collection<V>> backingMap()
  {
    return this.map;
  }

  public void clear()
  {
    Iterator localIterator = this.map.values().iterator();
    while (localIterator.hasNext())
      ((Collection)localIterator.next()).clear();
    this.map.clear();
    this.totalSize = 0;
  }

  public boolean containsEntry(@Nullable Object paramObject1, @Nullable Object paramObject2)
  {
    Collection localCollection = (Collection)this.map.get(paramObject1);
    return (localCollection != null) && (localCollection.contains(paramObject2));
  }

  public boolean containsKey(@Nullable Object paramObject)
  {
    return this.map.containsKey(paramObject);
  }

  public boolean containsValue(@Nullable Object paramObject)
  {
    Iterator localIterator = this.map.values().iterator();
    while (localIterator.hasNext())
      if (((Collection)localIterator.next()).contains(paramObject))
        return true;
    return false;
  }

  abstract Collection<V> createCollection();

  Collection<V> createCollection(@Nullable K paramK)
  {
    return createCollection();
  }

  Iterator<Map.Entry<K, V>> createEntryIterator()
  {
    return new EntryIterator();
  }

  public Collection<Map.Entry<K, V>> entries()
  {
    Collection localCollection1 = this.entries;
    if (this.entries == null)
    {
      Collection localCollection2 = createEntries();
      this.entries = localCollection2;
      return localCollection2;
    }
    return localCollection1;
  }

  public boolean equals(@Nullable Object paramObject)
  {
    if (paramObject == this)
      return true;
    if ((paramObject instanceof Multimap))
    {
      Multimap localMultimap = (Multimap)paramObject;
      return this.map.equals(localMultimap.asMap());
    }
    return false;
  }

  public Collection<V> get(@Nullable K paramK)
  {
    Collection localCollection = (Collection)this.map.get(paramK);
    if (localCollection == null)
      localCollection = createCollection(paramK);
    return wrapCollection(paramK, localCollection);
  }

  public int hashCode()
  {
    return this.map.hashCode();
  }

  public boolean isEmpty()
  {
    return this.totalSize == 0;
  }

  public Set<K> keySet()
  {
    Set localSet1 = this.keySet;
    if (localSet1 == null)
    {
      Set localSet2 = createKeySet();
      this.keySet = localSet2;
      return localSet2;
    }
    return localSet1;
  }

  public Multiset<K> keys()
  {
    Multiset localMultiset = this.multiset;
    if (localMultiset == null)
    {
      MultisetView localMultisetView = new MultisetView(null);
      this.multiset = localMultisetView;
      return localMultisetView;
    }
    return localMultiset;
  }

  public boolean put(@Nullable K paramK, @Nullable V paramV)
  {
    if (getOrCreateCollection(paramK).add(paramV))
    {
      this.totalSize = (1 + this.totalSize);
      return true;
    }
    return false;
  }

  public boolean putAll(Multimap<? extends K, ? extends V> paramMultimap)
  {
    boolean bool = false;
    Iterator localIterator = paramMultimap.entries().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      bool |= put(localEntry.getKey(), localEntry.getValue());
    }
    return bool;
  }

  public boolean putAll(@Nullable K paramK, Iterable<? extends V> paramIterable)
  {
    if (!paramIterable.iterator().hasNext())
      return false;
    Collection localCollection = getOrCreateCollection(paramK);
    int i = localCollection.size();
    boolean bool = false;
    if ((paramIterable instanceof Collection))
      bool = localCollection.addAll((Collection)paramIterable);
    while (true)
    {
      this.totalSize += localCollection.size() - i;
      return bool;
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext())
        bool |= localCollection.add(localIterator.next());
    }
  }

  public boolean remove(@Nullable Object paramObject1, @Nullable Object paramObject2)
  {
    Collection localCollection = (Collection)this.map.get(paramObject1);
    if (localCollection == null)
      return false;
    boolean bool = localCollection.remove(paramObject2);
    if (bool)
    {
      this.totalSize -= 1;
      if (localCollection.isEmpty())
        this.map.remove(paramObject1);
    }
    return bool;
  }

  public Collection<V> removeAll(@Nullable Object paramObject)
  {
    Collection localCollection1 = (Collection)this.map.remove(paramObject);
    Collection localCollection2 = createCollection();
    if (localCollection1 != null)
    {
      localCollection2.addAll(localCollection1);
      this.totalSize -= localCollection1.size();
      localCollection1.clear();
    }
    return unmodifiableCollectionSubclass(localCollection2);
  }

  public Collection<V> replaceValues(@Nullable K paramK, Iterable<? extends V> paramIterable)
  {
    Iterator localIterator = paramIterable.iterator();
    if (!localIterator.hasNext())
      return removeAll(paramK);
    Collection localCollection1 = getOrCreateCollection(paramK);
    Collection localCollection2 = createCollection();
    localCollection2.addAll(localCollection1);
    this.totalSize -= localCollection1.size();
    localCollection1.clear();
    while (localIterator.hasNext())
      if (localCollection1.add(localIterator.next()))
        this.totalSize = (1 + this.totalSize);
    return unmodifiableCollectionSubclass(localCollection2);
  }

  final void setMap(Map<K, Collection<V>> paramMap)
  {
    this.map = paramMap;
    this.totalSize = 0;
    Iterator localIterator = paramMap.values().iterator();
    if (localIterator.hasNext())
    {
      Collection localCollection = (Collection)localIterator.next();
      if (!localCollection.isEmpty());
      for (boolean bool = true; ; bool = false)
      {
        Preconditions.checkArgument(bool);
        this.totalSize += localCollection.size();
        break;
      }
    }
  }

  public int size()
  {
    return this.totalSize;
  }

  public String toString()
  {
    return this.map.toString();
  }

  public Collection<V> values()
  {
    Collection localCollection = this.valuesCollection;
    if (localCollection == null)
    {
      Values localValues = new Values(null);
      this.valuesCollection = localValues;
      return localValues;
    }
    return localCollection;
  }

  private class AsMap extends AbstractMap<K, Collection<V>>
  {
    transient Set<Map.Entry<K, Collection<V>>> entrySet;
    final transient Map<K, Collection<V>> submap;

    AsMap()
    {
      Object localObject;
      this.submap = localObject;
    }

    public boolean containsKey(Object paramObject)
    {
      return Maps.safeContainsKey(this.submap, paramObject);
    }

    public Set<Map.Entry<K, Collection<V>>> entrySet()
    {
      Set localSet = this.entrySet;
      if (this.entrySet == null)
      {
        AsMapEntries localAsMapEntries = new AsMapEntries();
        this.entrySet = localAsMapEntries;
        return localAsMapEntries;
      }
      return localSet;
    }

    public boolean equals(@Nullable Object paramObject)
    {
      return (this == paramObject) || (this.submap.equals(paramObject));
    }

    public Collection<V> get(Object paramObject)
    {
      Collection localCollection = (Collection)Maps.safeGet(this.submap, paramObject);
      if (localCollection == null)
        return null;
      return AbstractMultimap.this.wrapCollection(paramObject, localCollection);
    }

    public int hashCode()
    {
      return this.submap.hashCode();
    }

    public Set<K> keySet()
    {
      return AbstractMultimap.this.keySet();
    }

    public Collection<V> remove(Object paramObject)
    {
      Collection localCollection1 = (Collection)this.submap.remove(paramObject);
      if (localCollection1 == null)
        return null;
      Collection localCollection2 = AbstractMultimap.this.createCollection();
      localCollection2.addAll(localCollection1);
      AbstractMultimap.access$220(AbstractMultimap.this, localCollection1.size());
      localCollection1.clear();
      return localCollection2;
    }

    public String toString()
    {
      return this.submap.toString();
    }

    class AsMapEntries extends AbstractSet<Map.Entry<K, Collection<V>>>
    {
      AsMapEntries()
      {
      }

      public boolean contains(Object paramObject)
      {
        return Collections2.safeContains(AbstractMultimap.AsMap.this.submap.entrySet(), paramObject);
      }

      public Iterator<Map.Entry<K, Collection<V>>> iterator()
      {
        return new AbstractMultimap.AsMap.AsMapIterator(AbstractMultimap.AsMap.this);
      }

      public boolean remove(Object paramObject)
      {
        if (!contains(paramObject))
          return false;
        Map.Entry localEntry = (Map.Entry)paramObject;
        AbstractMultimap.this.removeValuesForKey(localEntry.getKey());
        return true;
      }

      public int size()
      {
        return AbstractMultimap.AsMap.this.submap.size();
      }
    }

    class AsMapIterator
      implements Iterator<Map.Entry<K, Collection<V>>>
    {
      Collection<V> collection;
      final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = AbstractMultimap.AsMap.this.submap.entrySet().iterator();

      AsMapIterator()
      {
      }

      public boolean hasNext()
      {
        return this.delegateIterator.hasNext();
      }

      public Map.Entry<K, Collection<V>> next()
      {
        Map.Entry localEntry = (Map.Entry)this.delegateIterator.next();
        Object localObject = localEntry.getKey();
        this.collection = ((Collection)localEntry.getValue());
        return Maps.immutableEntry(localObject, AbstractMultimap.this.wrapCollection(localObject, this.collection));
      }

      public void remove()
      {
        this.delegateIterator.remove();
        AbstractMultimap.access$220(AbstractMultimap.this, this.collection.size());
        this.collection.clear();
      }
    }
  }

  private class Entries extends AbstractCollection<Map.Entry<K, V>>
  {
    private Entries()
    {
    }

    public void clear()
    {
      AbstractMultimap.this.clear();
    }

    public boolean contains(Object paramObject)
    {
      if (!(paramObject instanceof Map.Entry))
        return false;
      Map.Entry localEntry = (Map.Entry)paramObject;
      return AbstractMultimap.this.containsEntry(localEntry.getKey(), localEntry.getValue());
    }

    public Iterator<Map.Entry<K, V>> iterator()
    {
      return AbstractMultimap.this.createEntryIterator();
    }

    public boolean remove(Object paramObject)
    {
      if (!(paramObject instanceof Map.Entry))
        return false;
      Map.Entry localEntry = (Map.Entry)paramObject;
      return AbstractMultimap.this.remove(localEntry.getKey(), localEntry.getValue());
    }

    public int size()
    {
      return AbstractMultimap.this.totalSize;
    }
  }

  private class EntryIterator
    implements Iterator<Map.Entry<K, V>>
  {
    Collection<V> collection;
    K key;
    final Iterator<Map.Entry<K, Collection<V>>> keyIterator = AbstractMultimap.this.map.entrySet().iterator();
    Iterator<V> valueIterator;

    EntryIterator()
    {
      if (this.keyIterator.hasNext())
      {
        findValueIteratorAndKey();
        return;
      }
      this.valueIterator = Iterators.emptyModifiableIterator();
    }

    void findValueIteratorAndKey()
    {
      Map.Entry localEntry = (Map.Entry)this.keyIterator.next();
      this.key = localEntry.getKey();
      this.collection = ((Collection)localEntry.getValue());
      this.valueIterator = this.collection.iterator();
    }

    public boolean hasNext()
    {
      return (this.keyIterator.hasNext()) || (this.valueIterator.hasNext());
    }

    public Map.Entry<K, V> next()
    {
      if (!this.valueIterator.hasNext())
        findValueIteratorAndKey();
      return Maps.immutableEntry(this.key, this.valueIterator.next());
    }

    public void remove()
    {
      this.valueIterator.remove();
      if (this.collection.isEmpty())
        this.keyIterator.remove();
      AbstractMultimap.access$210(AbstractMultimap.this);
    }
  }

  private class EntrySet extends AbstractMultimap<K, V>.Entries
    implements Set<Map.Entry<K, V>>
  {
    private EntrySet()
    {
      super(null);
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

  private class KeySet extends AbstractSet<K>
  {
    final Map<K, Collection<V>> subMap;

    KeySet()
    {
      Object localObject;
      this.subMap = localObject;
    }

    public boolean contains(Object paramObject)
    {
      return this.subMap.containsKey(paramObject);
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      return this.subMap.keySet().containsAll(paramCollection);
    }

    public boolean equals(@Nullable Object paramObject)
    {
      return (this == paramObject) || (this.subMap.keySet().equals(paramObject));
    }

    public int hashCode()
    {
      return this.subMap.keySet().hashCode();
    }

    public Iterator<K> iterator()
    {
      return new Iterator()
      {
        Map.Entry<K, Collection<V>> entry;
        final Iterator<Map.Entry<K, Collection<V>>> entryIterator = AbstractMultimap.KeySet.this.subMap.entrySet().iterator();

        public boolean hasNext()
        {
          return this.entryIterator.hasNext();
        }

        public K next()
        {
          this.entry = ((Map.Entry)this.entryIterator.next());
          return this.entry.getKey();
        }

        public void remove()
        {
          if (this.entry != null);
          for (boolean bool = true; ; bool = false)
          {
            Preconditions.checkState(bool);
            Collection localCollection = (Collection)this.entry.getValue();
            this.entryIterator.remove();
            AbstractMultimap.access$220(AbstractMultimap.this, localCollection.size());
            localCollection.clear();
            return;
          }
        }
      };
    }

    public boolean remove(Object paramObject)
    {
      Collection localCollection = (Collection)this.subMap.remove(paramObject);
      int i = 0;
      if (localCollection != null)
      {
        i = localCollection.size();
        localCollection.clear();
        AbstractMultimap.access$220(AbstractMultimap.this, i);
      }
      return i > 0;
    }

    public int size()
    {
      return this.subMap.size();
    }
  }

  private class MultisetEntry extends Multisets.AbstractEntry<K>
  {
    final Map.Entry<K, Collection<V>> entry;

    public MultisetEntry()
    {
      Object localObject;
      this.entry = localObject;
    }

    public int getCount()
    {
      return ((Collection)this.entry.getValue()).size();
    }

    public K getElement()
    {
      return this.entry.getKey();
    }
  }

  private class MultisetEntryIterator
    implements Iterator<Multiset.Entry<K>>
  {
    final Iterator<Map.Entry<K, Collection<V>>> asMapIterator = AbstractMultimap.this.asMap().entrySet().iterator();

    private MultisetEntryIterator()
    {
    }

    public boolean hasNext()
    {
      return this.asMapIterator.hasNext();
    }

    public Multiset.Entry<K> next()
    {
      return new AbstractMultimap.MultisetEntry(AbstractMultimap.this, (Map.Entry)this.asMapIterator.next());
    }

    public void remove()
    {
      this.asMapIterator.remove();
    }
  }

  private class MultisetKeyIterator
    implements Iterator<K>
  {
    final Iterator<Map.Entry<K, V>> entryIterator = AbstractMultimap.this.entries().iterator();

    private MultisetKeyIterator()
    {
    }

    public boolean hasNext()
    {
      return this.entryIterator.hasNext();
    }

    public K next()
    {
      return ((Map.Entry)this.entryIterator.next()).getKey();
    }

    public void remove()
    {
      this.entryIterator.remove();
    }
  }

  private class MultisetView extends AbstractMultiset<K>
  {
    transient Set<Multiset.Entry<K>> entrySet;

    private MultisetView()
    {
    }

    public void clear()
    {
      AbstractMultimap.this.clear();
    }

    public int count(Object paramObject)
    {
      try
      {
        Collection localCollection = (Collection)AbstractMultimap.this.map.get(paramObject);
        if (localCollection == null)
          return 0;
        int i = localCollection.size();
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

    public Set<K> elementSet()
    {
      return AbstractMultimap.this.keySet();
    }

    public Set<Multiset.Entry<K>> entrySet()
    {
      Set localSet = this.entrySet;
      if (localSet == null)
      {
        EntrySet localEntrySet = new EntrySet(null);
        this.entrySet = localEntrySet;
        return localEntrySet;
      }
      return localSet;
    }

    public Iterator<K> iterator()
    {
      return new AbstractMultimap.MultisetKeyIterator(AbstractMultimap.this, null);
    }

    public int remove(Object paramObject, int paramInt)
    {
      if (paramInt == 0)
        return count(paramObject);
      boolean bool;
      if (paramInt > 0)
        bool = true;
      Collection localCollection;
      while (true)
      {
        Preconditions.checkArgument(bool);
        try
        {
          localCollection = (Collection)AbstractMultimap.this.map.get(paramObject);
          if (localCollection == null)
          {
            return 0;
            bool = false;
          }
        }
        catch (NullPointerException localNullPointerException)
        {
          return 0;
        }
        catch (ClassCastException localClassCastException)
        {
          return 0;
        }
      }
      int i = localCollection.size();
      if (paramInt >= i)
        return AbstractMultimap.this.removeValuesForKey(paramObject);
      Iterator localIterator = localCollection.iterator();
      for (int j = 0; j < paramInt; j++)
      {
        localIterator.next();
        localIterator.remove();
      }
      AbstractMultimap.access$220(AbstractMultimap.this, paramInt);
      return i;
    }

    public int size()
    {
      return AbstractMultimap.this.totalSize;
    }

    private class EntrySet extends AbstractSet<Multiset.Entry<K>>
    {
      private EntrySet()
      {
      }

      public void clear()
      {
        AbstractMultimap.this.clear();
      }

      public boolean contains(Object paramObject)
      {
        if (!(paramObject instanceof Multiset.Entry))
          return false;
        Multiset.Entry localEntry = (Multiset.Entry)paramObject;
        Collection localCollection = (Collection)AbstractMultimap.this.map.get(localEntry.getElement());
        return (localCollection != null) && (localCollection.size() == localEntry.getCount());
      }

      public Iterator<Multiset.Entry<K>> iterator()
      {
        return new AbstractMultimap.MultisetEntryIterator(AbstractMultimap.this, null);
      }

      public boolean remove(Object paramObject)
      {
        return (contains(paramObject)) && (AbstractMultimap.this.removeValuesForKey(((Multiset.Entry)paramObject).getElement()) > 0);
      }

      public int size()
      {
        return AbstractMultimap.this.map.size();
      }
    }
  }

  private class RandomAccessWrappedList extends AbstractMultimap.WrappedList
    implements RandomAccess
  {
    RandomAccessWrappedList(List<V> paramAbstractMultimap, @Nullable AbstractMultimap<K, V>.WrappedCollection arg3)
    {
      super(paramAbstractMultimap, localList, localWrappedCollection);
    }
  }

  private class SortedAsMap extends AbstractMultimap<K, V>.AsMap
    implements SortedMap<K, Collection<V>>
  {
    SortedSet<K> sortedKeySet;

    SortedAsMap()
    {
      super(localMap);
    }

    public Comparator<? super K> comparator()
    {
      return sortedMap().comparator();
    }

    public K firstKey()
    {
      return sortedMap().firstKey();
    }

    public SortedMap<K, Collection<V>> headMap(K paramK)
    {
      return new SortedAsMap(AbstractMultimap.this, sortedMap().headMap(paramK));
    }

    public SortedSet<K> keySet()
    {
      SortedSet localSortedSet = this.sortedKeySet;
      if (localSortedSet == null)
      {
        AbstractMultimap.SortedKeySet localSortedKeySet = new AbstractMultimap.SortedKeySet(AbstractMultimap.this, sortedMap());
        this.sortedKeySet = localSortedKeySet;
        return localSortedKeySet;
      }
      return localSortedSet;
    }

    public K lastKey()
    {
      return sortedMap().lastKey();
    }

    SortedMap<K, Collection<V>> sortedMap()
    {
      return (SortedMap)this.submap;
    }

    public SortedMap<K, Collection<V>> subMap(K paramK1, K paramK2)
    {
      return new SortedAsMap(AbstractMultimap.this, sortedMap().subMap(paramK1, paramK2));
    }

    public SortedMap<K, Collection<V>> tailMap(K paramK)
    {
      return new SortedAsMap(AbstractMultimap.this, sortedMap().tailMap(paramK));
    }
  }

  private class SortedKeySet extends AbstractMultimap<K, V>.KeySet
    implements SortedSet<K>
  {
    SortedKeySet()
    {
      super(localMap);
    }

    public Comparator<? super K> comparator()
    {
      return sortedMap().comparator();
    }

    public K first()
    {
      return sortedMap().firstKey();
    }

    public SortedSet<K> headSet(K paramK)
    {
      return new SortedKeySet(AbstractMultimap.this, sortedMap().headMap(paramK));
    }

    public K last()
    {
      return sortedMap().lastKey();
    }

    SortedMap<K, Collection<V>> sortedMap()
    {
      return (SortedMap)this.subMap;
    }

    public SortedSet<K> subSet(K paramK1, K paramK2)
    {
      return new SortedKeySet(AbstractMultimap.this, sortedMap().subMap(paramK1, paramK2));
    }

    public SortedSet<K> tailSet(K paramK)
    {
      return new SortedKeySet(AbstractMultimap.this, sortedMap().tailMap(paramK));
    }
  }

  private class ValueIterator
    implements Iterator<V>
  {
    final Iterator<Map.Entry<K, V>> entryIterator = AbstractMultimap.this.createEntryIterator();

    private ValueIterator()
    {
    }

    public boolean hasNext()
    {
      return this.entryIterator.hasNext();
    }

    public V next()
    {
      return ((Map.Entry)this.entryIterator.next()).getValue();
    }

    public void remove()
    {
      this.entryIterator.remove();
    }
  }

  private class Values extends AbstractCollection<V>
  {
    private Values()
    {
    }

    public void clear()
    {
      AbstractMultimap.this.clear();
    }

    public boolean contains(Object paramObject)
    {
      return AbstractMultimap.this.containsValue(paramObject);
    }

    public Iterator<V> iterator()
    {
      return new AbstractMultimap.ValueIterator(AbstractMultimap.this, null);
    }

    public int size()
    {
      return AbstractMultimap.this.totalSize;
    }
  }

  private class WrappedCollection extends AbstractCollection<V>
  {
    final AbstractMultimap<K, V>.WrappedCollection ancestor;
    final Collection<V> ancestorDelegate;
    Collection<V> delegate;
    final K key;

    WrappedCollection(Collection<V> paramAbstractMultimap, @Nullable AbstractMultimap<K, V>.WrappedCollection arg3)
    {
      this.key = paramAbstractMultimap;
      Object localObject1;
      this.delegate = localObject1;
      Object localObject2;
      this.ancestor = localObject2;
      if (localObject2 == null);
      for (Collection localCollection = null; ; localCollection = localObject2.getDelegate())
      {
        this.ancestorDelegate = localCollection;
        return;
      }
    }

    public boolean add(V paramV)
    {
      refreshIfEmpty();
      boolean bool1 = this.delegate.isEmpty();
      boolean bool2 = this.delegate.add(paramV);
      if (bool2)
      {
        AbstractMultimap.access$208(AbstractMultimap.this);
        if (bool1)
          addToMap();
      }
      return bool2;
    }

    public boolean addAll(Collection<? extends V> paramCollection)
    {
      if (paramCollection.isEmpty())
        return false;
      int i = size();
      boolean bool = this.delegate.addAll(paramCollection);
      if (bool)
      {
        int j = this.delegate.size();
        AbstractMultimap.access$212(AbstractMultimap.this, j - i);
        if (i == 0)
          addToMap();
      }
      return bool;
    }

    void addToMap()
    {
      if (this.ancestor != null)
      {
        this.ancestor.addToMap();
        return;
      }
      AbstractMultimap.this.map.put(this.key, this.delegate);
    }

    public void clear()
    {
      int i = size();
      if (i == 0)
        return;
      this.delegate.clear();
      AbstractMultimap.access$220(AbstractMultimap.this, i);
      removeIfEmpty();
    }

    public boolean contains(Object paramObject)
    {
      refreshIfEmpty();
      return this.delegate.contains(paramObject);
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      refreshIfEmpty();
      return this.delegate.containsAll(paramCollection);
    }

    public boolean equals(@Nullable Object paramObject)
    {
      if (paramObject == this)
        return true;
      refreshIfEmpty();
      return this.delegate.equals(paramObject);
    }

    AbstractMultimap<K, V>.WrappedCollection getAncestor()
    {
      return this.ancestor;
    }

    Collection<V> getDelegate()
    {
      return this.delegate;
    }

    K getKey()
    {
      return this.key;
    }

    public int hashCode()
    {
      refreshIfEmpty();
      return this.delegate.hashCode();
    }

    public Iterator<V> iterator()
    {
      refreshIfEmpty();
      return new WrappedIterator();
    }

    void refreshIfEmpty()
    {
      if (this.ancestor != null)
      {
        this.ancestor.refreshIfEmpty();
        if (this.ancestor.getDelegate() != this.ancestorDelegate)
          throw new ConcurrentModificationException();
      }
      else if (this.delegate.isEmpty())
      {
        Collection localCollection = (Collection)AbstractMultimap.this.map.get(this.key);
        if (localCollection != null)
          this.delegate = localCollection;
      }
    }

    public boolean remove(Object paramObject)
    {
      refreshIfEmpty();
      boolean bool = this.delegate.remove(paramObject);
      if (bool)
      {
        AbstractMultimap.access$210(AbstractMultimap.this);
        removeIfEmpty();
      }
      return bool;
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      if (paramCollection.isEmpty())
        return false;
      int i = size();
      boolean bool = this.delegate.removeAll(paramCollection);
      if (bool)
      {
        int j = this.delegate.size();
        AbstractMultimap.access$212(AbstractMultimap.this, j - i);
        removeIfEmpty();
      }
      return bool;
    }

    void removeIfEmpty()
    {
      if (this.ancestor != null)
        this.ancestor.removeIfEmpty();
      while (!this.delegate.isEmpty())
        return;
      AbstractMultimap.this.map.remove(this.key);
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      Preconditions.checkNotNull(paramCollection);
      int i = size();
      boolean bool = this.delegate.retainAll(paramCollection);
      if (bool)
      {
        int j = this.delegate.size();
        AbstractMultimap.access$212(AbstractMultimap.this, j - i);
        removeIfEmpty();
      }
      return bool;
    }

    public int size()
    {
      refreshIfEmpty();
      return this.delegate.size();
    }

    public String toString()
    {
      refreshIfEmpty();
      return this.delegate.toString();
    }

    class WrappedIterator
      implements Iterator<V>
    {
      final Iterator<V> delegateIterator;
      final Collection<V> originalDelegate = AbstractMultimap.WrappedCollection.this.delegate;

      WrappedIterator()
      {
        this.delegateIterator = AbstractMultimap.this.iteratorOrListIterator(AbstractMultimap.WrappedCollection.this.delegate);
      }

      WrappedIterator()
      {
        Object localObject;
        this.delegateIterator = localObject;
      }

      Iterator<V> getDelegateIterator()
      {
        validateIterator();
        return this.delegateIterator;
      }

      public boolean hasNext()
      {
        validateIterator();
        return this.delegateIterator.hasNext();
      }

      public V next()
      {
        validateIterator();
        return this.delegateIterator.next();
      }

      public void remove()
      {
        this.delegateIterator.remove();
        AbstractMultimap.access$210(AbstractMultimap.this);
        AbstractMultimap.WrappedCollection.this.removeIfEmpty();
      }

      void validateIterator()
      {
        AbstractMultimap.WrappedCollection.this.refreshIfEmpty();
        if (AbstractMultimap.WrappedCollection.this.delegate != this.originalDelegate)
          throw new ConcurrentModificationException();
      }
    }
  }

  private class WrappedList extends AbstractMultimap<K, V>.WrappedCollection
    implements List<V>
  {
    WrappedList(List<V> paramAbstractMultimap, @Nullable AbstractMultimap<K, V>.WrappedCollection arg3)
    {
      super(paramAbstractMultimap, localCollection, localWrappedCollection);
    }

    public void add(int paramInt, V paramV)
    {
      refreshIfEmpty();
      boolean bool = getDelegate().isEmpty();
      getListDelegate().add(paramInt, paramV);
      AbstractMultimap.access$208(AbstractMultimap.this);
      if (bool)
        addToMap();
    }

    public boolean addAll(int paramInt, Collection<? extends V> paramCollection)
    {
      if (paramCollection.isEmpty())
        return false;
      int i = size();
      boolean bool = getListDelegate().addAll(paramInt, paramCollection);
      if (bool)
      {
        int j = getDelegate().size();
        AbstractMultimap.access$212(AbstractMultimap.this, j - i);
        if (i == 0)
          addToMap();
      }
      return bool;
    }

    public V get(int paramInt)
    {
      refreshIfEmpty();
      return getListDelegate().get(paramInt);
    }

    List<V> getListDelegate()
    {
      return (List)getDelegate();
    }

    public int indexOf(Object paramObject)
    {
      refreshIfEmpty();
      return getListDelegate().indexOf(paramObject);
    }

    public int lastIndexOf(Object paramObject)
    {
      refreshIfEmpty();
      return getListDelegate().lastIndexOf(paramObject);
    }

    public ListIterator<V> listIterator()
    {
      refreshIfEmpty();
      return new WrappedListIterator();
    }

    public ListIterator<V> listIterator(int paramInt)
    {
      refreshIfEmpty();
      return new WrappedListIterator(paramInt);
    }

    public V remove(int paramInt)
    {
      refreshIfEmpty();
      Object localObject = getListDelegate().remove(paramInt);
      AbstractMultimap.access$210(AbstractMultimap.this);
      removeIfEmpty();
      return localObject;
    }

    public V set(int paramInt, V paramV)
    {
      refreshIfEmpty();
      return getListDelegate().set(paramInt, paramV);
    }

    @GwtIncompatible("List.subList")
    public List<V> subList(int paramInt1, int paramInt2)
    {
      refreshIfEmpty();
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      Object localObject1 = getKey();
      List localList = Platform.subList(getListDelegate(), paramInt1, paramInt2);
      if (getAncestor() == null);
      for (Object localObject2 = this; ; localObject2 = getAncestor())
        return localAbstractMultimap.wrapList(localObject1, localList, (AbstractMultimap.WrappedCollection)localObject2);
    }

    private class WrappedListIterator extends AbstractMultimap<K, V>.WrappedCollection.WrappedIterator
      implements ListIterator<V>
    {
      WrappedListIterator()
      {
        super();
      }

      public WrappedListIterator(int arg2)
      {
        super(AbstractMultimap.WrappedList.this.getListDelegate().listIterator(i));
      }

      private ListIterator<V> getDelegateListIterator()
      {
        return (ListIterator)getDelegateIterator();
      }

      public void add(V paramV)
      {
        boolean bool = AbstractMultimap.WrappedList.this.isEmpty();
        getDelegateListIterator().add(paramV);
        AbstractMultimap.access$208(AbstractMultimap.this);
        if (bool)
          AbstractMultimap.WrappedList.this.addToMap();
      }

      public boolean hasPrevious()
      {
        return getDelegateListIterator().hasPrevious();
      }

      public int nextIndex()
      {
        return getDelegateListIterator().nextIndex();
      }

      public V previous()
      {
        return getDelegateListIterator().previous();
      }

      public int previousIndex()
      {
        return getDelegateListIterator().previousIndex();
      }

      public void set(V paramV)
      {
        getDelegateListIterator().set(paramV);
      }
    }
  }

  private class WrappedSet extends AbstractMultimap<K, V>.WrappedCollection
    implements Set<V>
  {
    WrappedSet(Set<V> arg2)
    {
      super(localObject, localCollection, null);
    }
  }

  private class WrappedSortedSet extends AbstractMultimap<K, V>.WrappedCollection
    implements SortedSet<V>
  {
    WrappedSortedSet(SortedSet<V> paramAbstractMultimap, @Nullable AbstractMultimap<K, V>.WrappedCollection arg3)
    {
      super(paramAbstractMultimap, localCollection, localWrappedCollection);
    }

    public Comparator<? super V> comparator()
    {
      return getSortedSetDelegate().comparator();
    }

    public V first()
    {
      refreshIfEmpty();
      return getSortedSetDelegate().first();
    }

    SortedSet<V> getSortedSetDelegate()
    {
      return (SortedSet)getDelegate();
    }

    public SortedSet<V> headSet(V paramV)
    {
      refreshIfEmpty();
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      Object localObject1 = getKey();
      SortedSet localSortedSet = getSortedSetDelegate().headSet(paramV);
      if (getAncestor() == null);
      for (Object localObject2 = this; ; localObject2 = getAncestor())
        return new WrappedSortedSet(localAbstractMultimap, localObject1, localSortedSet, (AbstractMultimap.WrappedCollection)localObject2);
    }

    public V last()
    {
      refreshIfEmpty();
      return getSortedSetDelegate().last();
    }

    public SortedSet<V> subSet(V paramV1, V paramV2)
    {
      refreshIfEmpty();
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      Object localObject1 = getKey();
      SortedSet localSortedSet = getSortedSetDelegate().subSet(paramV1, paramV2);
      if (getAncestor() == null);
      for (Object localObject2 = this; ; localObject2 = getAncestor())
        return new WrappedSortedSet(localAbstractMultimap, localObject1, localSortedSet, (AbstractMultimap.WrappedCollection)localObject2);
    }

    public SortedSet<V> tailSet(V paramV)
    {
      refreshIfEmpty();
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      Object localObject1 = getKey();
      SortedSet localSortedSet = getSortedSetDelegate().tailSet(paramV);
      if (getAncestor() == null);
      for (Object localObject2 = this; ; localObject2 = getAncestor())
        return new WrappedSortedSet(localAbstractMultimap, localObject1, localSortedSet, (AbstractMultimap.WrappedCollection)localObject2);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractMultimap
 * JD-Core Version:    0.6.2
 */