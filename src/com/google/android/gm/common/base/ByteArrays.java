package com.google.android.gm.common.base;

public final class ByteArrays
{
  private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

  public static String toHexString(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder(2 * paramArrayOfByte.length);
    int i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++)
    {
      int k = paramArrayOfByte[j];
      localStringBuilder.append(HEX_DIGITS[(0xF & k >> 4)]).append(HEX_DIGITS[(k & 0xF)]);
    }
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.base.ByteArrays
 * JD-Core Version:    0.6.2
 */