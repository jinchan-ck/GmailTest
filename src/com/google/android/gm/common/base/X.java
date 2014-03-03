package com.google.android.gm.common.base;

public final class X
{
  public static void assertTrue(boolean paramBoolean)
  {
    if (!paramBoolean)
      throw new RuntimeException("Assertion failed");
  }

  public static void assertTrue(boolean paramBoolean, String paramString)
  {
    if (!paramBoolean)
      throw new RuntimeException("Assertion failed: " + paramString);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.base.X
 * JD-Core Version:    0.6.2
 */