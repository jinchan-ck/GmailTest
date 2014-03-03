package com.google.common.collect;

final class Hashing
{
  static int smear(int paramInt)
  {
    int i = paramInt ^ (paramInt >>> 20 ^ paramInt >>> 12);
    return i ^ i >>> 7 ^ i >>> 4;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Hashing
 * JD-Core Version:    0.6.2
 */