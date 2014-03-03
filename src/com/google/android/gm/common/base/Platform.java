package com.google.android.gm.common.base;

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

  static boolean isInstance(Class<?> paramClass, Object paramObject)
  {
    return paramClass.isInstance(paramObject);
  }

  static CharMatcher precomputeCharMatcher(CharMatcher paramCharMatcher)
  {
    return paramCharMatcher.precomputedInternal();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.base.Platform
 * JD-Core Version:    0.6.2
 */