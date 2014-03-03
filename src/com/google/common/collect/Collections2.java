package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;

public final class Collections2
{
  static final Joiner STANDARD_JOINER = Joiner.on(", ");

  static <T> Collection<T> cast(Iterable<T> paramIterable)
  {
    return (Collection)paramIterable;
  }

  static boolean containsAllImpl(Collection<?> paramCollection1, Collection<?> paramCollection2)
  {
    Preconditions.checkNotNull(paramCollection1);
    Iterator localIterator = paramCollection2.iterator();
    while (localIterator.hasNext())
      if (!paramCollection1.contains(localIterator.next()))
        return false;
    return true;
  }

  static StringBuilder newStringBuilderForCollection(int paramInt)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "size must be non-negative");
      return new StringBuilder((int)Math.min(8L * paramInt, 1073741824L));
    }
  }

  static String toStringImpl(Collection<?> paramCollection)
  {
    StringBuilder localStringBuilder = newStringBuilderForCollection(paramCollection.size()).append('[');
    STANDARD_JOINER.appendTo(localStringBuilder, Iterables.transform(paramCollection, new Function()
    {
      public Object apply(Object paramAnonymousObject)
      {
        if (paramAnonymousObject == this.val$collection)
          paramAnonymousObject = "(this Collection)";
        return paramAnonymousObject;
      }
    }));
    return ']';
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Collections2
 * JD-Core Version:    0.6.2
 */