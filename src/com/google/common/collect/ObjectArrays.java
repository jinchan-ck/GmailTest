package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;

public final class ObjectArrays
{
  private static Object[] fillArray(Iterable<?> paramIterable, Object[] paramArrayOfObject)
  {
    int i = 0;
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      int j = i + 1;
      paramArrayOfObject[i] = localObject;
      i = j;
    }
    return paramArrayOfObject;
  }

  public static <T> T[] newArray(T[] paramArrayOfT, int paramInt)
  {
    return Platform.newArray(paramArrayOfT, paramInt);
  }

  static Object[] toArrayImpl(Collection<?> paramCollection)
  {
    return fillArray(paramCollection, new Object[paramCollection.size()]);
  }

  static <T> T[] toArrayImpl(Collection<?> paramCollection, T[] paramArrayOfT)
  {
    int i = paramCollection.size();
    if (paramArrayOfT.length < i)
      paramArrayOfT = newArray(paramArrayOfT, i);
    fillArray(paramCollection, paramArrayOfT);
    if (paramArrayOfT.length > i)
      paramArrayOfT[i] = null;
    return paramArrayOfT;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ObjectArrays
 * JD-Core Version:    0.6.2
 */