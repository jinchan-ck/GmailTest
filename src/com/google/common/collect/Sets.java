package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public final class Sets
{
  static boolean equalsImpl(Set<?> paramSet, Object paramObject)
  {
    boolean bool1 = true;
    boolean bool3;
    if (paramSet == paramObject)
      bool3 = bool1;
    boolean bool2;
    do
    {
      return bool3;
      bool2 = paramObject instanceof Set;
      bool3 = false;
    }
    while (!bool2);
    Set localSet = (Set)paramObject;
    try
    {
      if (paramSet.size() == localSet.size())
      {
        boolean bool4 = paramSet.containsAll(localSet);
        if (!bool4);
      }
      while (true)
      {
        return bool1;
        bool1 = false;
      }
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

  static int hashCodeImpl(Set<?> paramSet)
  {
    int i = 0;
    Iterator localIterator = paramSet.iterator();
    if (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if (localObject != null);
      for (int j = localObject.hashCode(); ; j = 0)
      {
        i += j;
        break;
      }
    }
    return i;
  }

  public static <E> HashSet<E> newHashSet()
  {
    return new HashSet();
  }

  public static <E> HashSet<E> newHashSet(Iterable<? extends E> paramIterable)
  {
    if ((paramIterable instanceof Collection))
      return new HashSet(Collections2.cast(paramIterable));
    return newHashSet(paramIterable.iterator());
  }

  public static <E> HashSet<E> newHashSet(Iterator<? extends E> paramIterator)
  {
    HashSet localHashSet = newHashSet();
    while (paramIterator.hasNext())
      localHashSet.add(paramIterator.next());
    return localHashSet;
  }

  public static <E> HashSet<E> newHashSet(E[] paramArrayOfE)
  {
    HashSet localHashSet = newHashSetWithExpectedSize(paramArrayOfE.length);
    Collections.addAll(localHashSet, paramArrayOfE);
    return localHashSet;
  }

  public static <E> HashSet<E> newHashSetWithExpectedSize(int paramInt)
  {
    return new HashSet(Maps.capacity(paramInt));
  }

  public static <E extends Comparable> TreeSet<E> newTreeSet()
  {
    return new TreeSet();
  }

  public static <E> TreeSet<E> newTreeSet(Comparator<? super E> paramComparator)
  {
    return new TreeSet((Comparator)Preconditions.checkNotNull(paramComparator));
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Sets
 * JD-Core Version:    0.6.2
 */