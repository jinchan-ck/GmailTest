package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class Lists
{
  static int computeArrayListCapacity(int paramInt)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return Ints.saturatedCast(5L + paramInt + paramInt / 10);
    }
  }

  static boolean equalsImpl(List<?> paramList, Object paramObject)
  {
    if (paramObject == Preconditions.checkNotNull(paramList));
    List localList;
    do
    {
      return true;
      if (!(paramObject instanceof List))
        return false;
      localList = (List)paramObject;
    }
    while ((paramList.size() == localList.size()) && (Iterators.elementsEqual(paramList.iterator(), localList.iterator())));
    return false;
  }

  static int hashCodeImpl(List<?> paramList)
  {
    int i = 1;
    Iterator localIterator = paramList.iterator();
    if (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      int j = i * 31;
      if (localObject == null);
      for (int k = 0; ; k = localObject.hashCode())
      {
        i = j + k;
        break;
      }
    }
    return i;
  }

  public static <E> ArrayList<E> newArrayList()
  {
    return new ArrayList();
  }

  public static <E> ArrayList<E> newArrayList(Iterable<? extends E> paramIterable)
  {
    Preconditions.checkNotNull(paramIterable);
    if ((paramIterable instanceof Collection))
      return new ArrayList(Collections2.cast(paramIterable));
    return newArrayList(paramIterable.iterator());
  }

  public static <E> ArrayList<E> newArrayList(Iterator<? extends E> paramIterator)
  {
    Preconditions.checkNotNull(paramIterator);
    ArrayList localArrayList = newArrayList();
    while (paramIterator.hasNext())
      localArrayList.add(paramIterator.next());
    return localArrayList;
  }

  public static <E> ArrayList<E> newArrayList(E[] paramArrayOfE)
  {
    Preconditions.checkNotNull(paramArrayOfE);
    ArrayList localArrayList = new ArrayList(computeArrayListCapacity(paramArrayOfE.length));
    Collections.addAll(localArrayList, paramArrayOfE);
    return localArrayList;
  }

  public static <E> LinkedList<E> newLinkedList()
  {
    return new LinkedList();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Lists
 * JD-Core Version:    0.6.2
 */