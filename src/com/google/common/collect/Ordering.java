package com.google.common.collect;

import com.google.common.base.Function;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Ordering<T>
  implements Comparator<T>
{
  public static <C extends Comparable> Ordering<C> natural()
  {
    return NaturalOrdering.INSTANCE;
  }

  static class ArbitraryOrdering extends Ordering<Object>
  {
    private Map<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeComputingMap(new Function()
    {
      final AtomicInteger counter = new AtomicInteger(0);

      public Integer apply(Object paramAnonymousObject)
      {
        return Integer.valueOf(this.counter.getAndIncrement());
      }
    });

    public int compare(Object paramObject1, Object paramObject2)
    {
      if (paramObject1 == paramObject2)
        return 0;
      int i = identityHashCode(paramObject1);
      int j = identityHashCode(paramObject2);
      if (i != j)
      {
        if (i < j)
          return -1;
        return 1;
      }
      int k = ((Integer)this.uids.get(paramObject1)).compareTo((Integer)this.uids.get(paramObject2));
      if (k == 0)
        throw new AssertionError();
      return k;
    }

    int identityHashCode(Object paramObject)
    {
      return System.identityHashCode(paramObject);
    }

    public String toString()
    {
      return "Ordering.arbitrary()";
    }
  }

  static class IncomparableValueException extends ClassCastException
  {
    private static final long serialVersionUID;
    final Object value;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Ordering
 * JD-Core Version:    0.6.2
 */