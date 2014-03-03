package com.google.common.primitives;

import java.util.Comparator;

public final class UnsignedBytes
{
  public static int compare(byte paramByte1, byte paramByte2)
  {
    return toInt(paramByte1) - toInt(paramByte2);
  }

  static Comparator<byte[]> lexicographicalComparatorJavaImpl()
  {
    return UnsignedBytes.LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
  }

  public static int toInt(byte paramByte)
  {
    return paramByte & 0xFF;
  }

  static class LexicographicalComparatorHolder
  {
    static final Comparator<byte[]> BEST_COMPARATOR = UnsignedBytes.lexicographicalComparatorJavaImpl();
    static final String UNSAFE_COMPARATOR_NAME = LexicographicalComparatorHolder.class.getName() + "$UnsafeComparator";

    static enum PureJavaComparator
      implements Comparator<byte[]>
    {
      static
      {
        PureJavaComparator[] arrayOfPureJavaComparator = new PureJavaComparator[1];
        arrayOfPureJavaComparator[0] = INSTANCE;
      }

      public int compare(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
      {
        int i = Math.min(paramArrayOfByte1.length, paramArrayOfByte2.length);
        for (int j = 0; j < i; j++)
        {
          int k = UnsignedBytes.compare(paramArrayOfByte1[j], paramArrayOfByte2[j]);
          if (k != 0)
            return k;
        }
        return paramArrayOfByte1.length - paramArrayOfByte2.length;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.primitives.UnsignedBytes
 * JD-Core Version:    0.6.2
 */