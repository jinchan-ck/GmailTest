package com.google.android.common.base;

final class Platform
{
  private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal()
  {
    protected char[] initialValue()
    {
      return new char[1024];
    }
  };

  static char[] charBufferFromThreadLocal()
  {
    return (char[])DEST_TL.get();
  }

  static CharMatcher precomputeCharMatcher(CharMatcher paramCharMatcher)
  {
    return paramCharMatcher.precomputedInternal();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.Platform
 * JD-Core Version:    0.6.2
 */