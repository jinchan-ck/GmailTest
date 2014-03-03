package com.google.common.collect;

import java.util.Comparator;

abstract interface SortedIterable<T> extends Iterable<T>
{
  public abstract Comparator<? super T> comparator();
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.SortedIterable
 * JD-Core Version:    0.6.2
 */