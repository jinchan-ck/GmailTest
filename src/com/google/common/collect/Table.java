package com.google.common.collect;

import java.util.Set;

public abstract interface Table<R, C, V>
{
  public abstract Set<Cell<R, C, V>> cellSet();

  public static abstract interface Cell<R, C, V>
  {
    public abstract V getValue();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Table
 * JD-Core Version:    0.6.2
 */