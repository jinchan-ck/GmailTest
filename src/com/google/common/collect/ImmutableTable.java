package com.google.common.collect;

import java.util.Map;

public abstract class ImmutableTable<R, C, V>
  implements Table<R, C, V>
{
  public abstract ImmutableSet<Table.Cell<R, C, V>> cellSet();

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if ((paramObject instanceof Table))
    {
      Table localTable = (Table)paramObject;
      return cellSet().equals(localTable.cellSet());
    }
    return false;
  }

  public int hashCode()
  {
    return cellSet().hashCode();
  }

  public abstract ImmutableMap<R, Map<C, V>> rowMap();

  public String toString()
  {
    return rowMap().toString();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableTable
 * JD-Core Version:    0.6.2
 */