package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@GwtCompatible
public final class TreeMultiset<E> extends AbstractMapBasedMultiset<E>
{
  private static final long serialVersionUID;

  private TreeMultiset()
  {
    super(new TreeMap());
  }

  private TreeMultiset(Comparator<? super E> paramComparator)
  {
    super(new TreeMap(paramComparator));
  }

  public static <E extends Comparable> TreeMultiset<E> create()
  {
    return new TreeMultiset();
  }

  public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> paramIterable)
  {
    TreeMultiset localTreeMultiset = create();
    Iterables.addAll(localTreeMultiset, paramIterable);
    return localTreeMultiset;
  }

  public static <E> TreeMultiset<E> create(Comparator<? super E> paramComparator)
  {
    return new TreeMultiset(paramComparator);
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    setBackingMap(new TreeMap((Comparator)paramObjectInputStream.readObject()));
    Serialization.populateMultiset(this, paramObjectInputStream);
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeObject(elementSet().comparator());
    Serialization.writeMultiset(this, paramObjectOutputStream);
  }

  public int count(@Nullable Object paramObject)
  {
    try
    {
      int i = super.count(paramObject);
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
    return new SortedMapBasedElementSet((SortedMap)backingMap());
  }

  public SortedSet<E> elementSet()
  {
    return (SortedSet)super.elementSet();
  }

  private class SortedMapBasedElementSet extends AbstractMapBasedMultiset<E>.MapBasedElementSet
    implements SortedSet<E>
  {
    SortedMapBasedElementSet()
    {
      super(localMap);
    }

    public Comparator<? super E> comparator()
    {
      return sortedMap().comparator();
    }

    public E first()
    {
      return sortedMap().firstKey();
    }

    public SortedSet<E> headSet(E paramE)
    {
      return new SortedMapBasedElementSet(TreeMultiset.this, sortedMap().headMap(paramE));
    }

    public E last()
    {
      return sortedMap().lastKey();
    }

    public boolean remove(Object paramObject)
    {
      try
      {
        boolean bool = super.remove(paramObject);
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

    SortedMap<E, AtomicInteger> sortedMap()
    {
      return (SortedMap)getMap();
    }

    public SortedSet<E> subSet(E paramE1, E paramE2)
    {
      return new SortedMapBasedElementSet(TreeMultiset.this, sortedMap().subMap(paramE1, paramE2));
    }

    public SortedSet<E> tailSet(E paramE)
    {
      return new SortedMapBasedElementSet(TreeMultiset.this, sortedMap().tailMap(paramE));
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.TreeMultiset
 * JD-Core Version:    0.6.2
 */