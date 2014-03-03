package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public final class Ints
{
  public static int[] concat(int[][] paramArrayOfInt)
  {
    int i = 0;
    int j = paramArrayOfInt.length;
    for (int k = 0; k < j; k++)
      i += paramArrayOfInt[k].length;
    int[] arrayOfInt1 = new int[i];
    int m = 0;
    int n = paramArrayOfInt.length;
    for (int i1 = 0; i1 < n; i1++)
    {
      int[] arrayOfInt2 = paramArrayOfInt[i1];
      System.arraycopy(arrayOfInt2, 0, arrayOfInt1, m, arrayOfInt2.length);
      m += arrayOfInt2.length;
    }
    return arrayOfInt1;
  }

  public static int hashCode(int paramInt)
  {
    return paramInt;
  }

  private static int indexOf(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    for (int i = paramInt2; i < paramInt3; i++)
      if (paramArrayOfInt[i] == paramInt1)
        return i;
    return -1;
  }

  private static int lastIndexOf(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    for (int i = paramInt3 - 1; i >= paramInt2; i--)
      if (paramArrayOfInt[i] == paramInt1)
        return i;
    return -1;
  }

  public static int saturatedCast(long paramLong)
  {
    if (paramLong > 2147483647L)
      return 2147483647;
    if (paramLong < -2147483648L)
      return -2147483648;
    return (int)paramLong;
  }

  public static int[] toArray(Collection<Integer> paramCollection)
  {
    int[] arrayOfInt;
    if ((paramCollection instanceof IntArrayAsList))
      arrayOfInt = ((IntArrayAsList)paramCollection).toIntArray();
    while (true)
    {
      return arrayOfInt;
      Object[] arrayOfObject = paramCollection.toArray();
      int i = arrayOfObject.length;
      arrayOfInt = new int[i];
      for (int j = 0; j < i; j++)
        arrayOfInt[j] = ((Integer)Preconditions.checkNotNull(arrayOfObject[j])).intValue();
    }
  }

  private static class IntArrayAsList extends AbstractList<Integer>
    implements RandomAccess, Serializable
  {
    private static final long serialVersionUID;
    final int[] array;
    final int end;
    final int start;

    IntArrayAsList(int[] paramArrayOfInt, int paramInt1, int paramInt2)
    {
      this.array = paramArrayOfInt;
      this.start = paramInt1;
      this.end = paramInt2;
    }

    public boolean contains(Object paramObject)
    {
      return ((paramObject instanceof Integer)) && (Ints.indexOf(this.array, ((Integer)paramObject).intValue(), this.start, this.end) != -1);
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this);
      while (true)
      {
        return true;
        if (!(paramObject instanceof IntArrayAsList))
          break;
        IntArrayAsList localIntArrayAsList = (IntArrayAsList)paramObject;
        int i = size();
        if (localIntArrayAsList.size() != i)
          return false;
        for (int j = 0; j < i; j++)
          if (this.array[(j + this.start)] != localIntArrayAsList.array[(j + localIntArrayAsList.start)])
            return false;
      }
      return super.equals(paramObject);
    }

    public Integer get(int paramInt)
    {
      Preconditions.checkElementIndex(paramInt, size());
      return Integer.valueOf(this.array[(paramInt + this.start)]);
    }

    public int hashCode()
    {
      int i = 1;
      for (int j = this.start; j < this.end; j++)
        i = i * 31 + Ints.hashCode(this.array[j]);
      return i;
    }

    public int indexOf(Object paramObject)
    {
      if ((paramObject instanceof Integer))
      {
        int i = Ints.indexOf(this.array, ((Integer)paramObject).intValue(), this.start, this.end);
        if (i >= 0)
          return i - this.start;
      }
      return -1;
    }

    public boolean isEmpty()
    {
      return false;
    }

    public int lastIndexOf(Object paramObject)
    {
      if ((paramObject instanceof Integer))
      {
        int i = Ints.lastIndexOf(this.array, ((Integer)paramObject).intValue(), this.start, this.end);
        if (i >= 0)
          return i - this.start;
      }
      return -1;
    }

    public Integer set(int paramInt, Integer paramInteger)
    {
      Preconditions.checkElementIndex(paramInt, size());
      int i = this.array[(paramInt + this.start)];
      this.array[(paramInt + this.start)] = ((Integer)Preconditions.checkNotNull(paramInteger)).intValue();
      return Integer.valueOf(i);
    }

    public int size()
    {
      return this.end - this.start;
    }

    public List<Integer> subList(int paramInt1, int paramInt2)
    {
      Preconditions.checkPositionIndexes(paramInt1, paramInt2, size());
      if (paramInt1 == paramInt2)
        return Collections.emptyList();
      return new IntArrayAsList(this.array, paramInt1 + this.start, paramInt2 + this.start);
    }

    int[] toIntArray()
    {
      int i = size();
      int[] arrayOfInt = new int[i];
      System.arraycopy(this.array, this.start, arrayOfInt, 0, i);
      return arrayOfInt;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(5 * size());
      localStringBuilder.append('[').append(this.array[this.start]);
      for (int i = 1 + this.start; i < this.end; i++)
        localStringBuilder.append(", ").append(this.array[i]);
      return ']';
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.primitives.Ints
 * JD-Core Version:    0.6.2
 */