package com.google.common.collect;

import com.google.common.base.Function;
import java.util.Map;

abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V>
{
  private static final Function<Table.Cell<Object, Object, Object>, Object> GET_VALUE_FUNCTION = new Function()
  {
    public Object apply(Table.Cell<Object, Object, Object> paramAnonymousCell)
    {
      return paramAnonymousCell.getValue();
    }
  };
  private final ImmutableSet<Table.Cell<R, C, V>> cellSet;

  public final ImmutableSet<Table.Cell<R, C, V>> cellSet()
  {
    return this.cellSet;
  }

  static final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V>
  {
    private final ImmutableBiMap<C, Integer> columnKeyToIndex;
    private final ImmutableBiMap<R, Integer> rowKeyToIndex;
    private volatile transient ImmutableMap<R, Map<C, V>> rowMap;
    private final V[][] values;

    private ImmutableMap<R, Map<C, V>> makeRowMap()
    {
      ImmutableMap.Builder localBuilder1 = ImmutableMap.builder();
      for (int i = 0; i < this.values.length; i++)
      {
        Object[] arrayOfObject = this.values[i];
        ImmutableMap.Builder localBuilder2 = ImmutableMap.builder();
        for (int j = 0; j < arrayOfObject.length; j++)
        {
          Object localObject = arrayOfObject[j];
          if (localObject != null)
            localBuilder2.put(this.columnKeyToIndex.inverse().get(Integer.valueOf(j)), localObject);
        }
        localBuilder1.put(this.rowKeyToIndex.inverse().get(Integer.valueOf(i)), localBuilder2.build());
      }
      return localBuilder1.build();
    }

    public ImmutableMap<R, Map<C, V>> rowMap()
    {
      ImmutableMap localImmutableMap = this.rowMap;
      if (localImmutableMap == null)
      {
        localImmutableMap = makeRowMap();
        this.rowMap = localImmutableMap;
      }
      return localImmutableMap;
    }
  }

  static final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V>
  {
    private final ImmutableMap<R, Map<C, V>> rowMap;

    public ImmutableMap<R, Map<C, V>> rowMap()
    {
      return this.rowMap;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.RegularImmutableTable
 * JD-Core Version:    0.6.2
 */