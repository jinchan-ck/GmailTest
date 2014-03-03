package com.google.android.common.base;

public abstract class UnicodeEscaper extends Escaper
{
  protected static final int codePointAt(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    if (paramInt1 < paramInt2)
    {
      int i = paramInt1 + 1;
      int j = paramCharSequence.charAt(paramInt1);
      if ((j < 55296) || (j > 57343))
        return j;
      if (j <= 56319)
      {
        if (i == paramInt2)
          return -j;
        char c = paramCharSequence.charAt(i);
        if (Character.isLowSurrogate(c))
          return Character.toCodePoint(j, c);
        throw new IllegalArgumentException("Expected low surrogate but got char '" + c + "' with value " + c + " at index " + i);
      }
      throw new IllegalArgumentException("Unexpected low surrogate character '" + j + "' with value " + j + " at index " + (i - 1));
    }
    throw new IndexOutOfBoundsException("Index exceeds specified range");
  }

  private static final char[] growBuffer(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2];
    if (paramInt1 > 0)
      System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramInt1);
    return arrayOfChar;
  }

  public String escape(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    int i = paramString.length();
    int j = nextEscapeIndex(paramString, 0, i);
    if (j == i)
      return paramString;
    return escapeSlow(paramString, j);
  }

  protected abstract char[] escape(int paramInt);

  protected final String escapeSlow(String paramString, int paramInt)
  {
    int i = paramString.length();
    char[] arrayOfChar1 = Platform.charBufferFromThreadLocal();
    int j = 0;
    int k = 0;
    if (paramInt < i)
    {
      int i1 = codePointAt(paramString, paramInt, i);
      if (i1 < 0)
        throw new IllegalArgumentException("Trailing high surrogate at end of input");
      char[] arrayOfChar2 = escape(i1);
      if (Character.isSupplementaryCodePoint(i1));
      for (int i2 = 2; ; i2 = 1)
      {
        int i3 = paramInt + i2;
        if (arrayOfChar2 != null)
        {
          int i4 = paramInt - k;
          int i5 = j + i4 + arrayOfChar2.length;
          if (arrayOfChar1.length < i5)
            arrayOfChar1 = growBuffer(arrayOfChar1, j, 32 + (i5 + (i - paramInt)));
          if (i4 > 0)
          {
            paramString.getChars(k, paramInt, arrayOfChar1, j);
            j += i4;
          }
          if (arrayOfChar2.length > 0)
          {
            System.arraycopy(arrayOfChar2, 0, arrayOfChar1, j, arrayOfChar2.length);
            j += arrayOfChar2.length;
          }
          k = i3;
        }
        paramInt = nextEscapeIndex(paramString, i3, i);
        break;
      }
    }
    int m = i - k;
    if (m > 0)
    {
      int n = j + m;
      if (arrayOfChar1.length < n)
        arrayOfChar1 = growBuffer(arrayOfChar1, j, n);
      paramString.getChars(k, i, arrayOfChar1, j);
      j = n;
    }
    return new String(arrayOfChar1, 0, j);
  }

  protected int nextEscapeIndex(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j;
    if (i < paramInt2)
    {
      j = codePointAt(paramCharSequence, i, paramInt2);
      if ((j >= 0) && (escape(j) == null));
    }
    else
    {
      return i;
    }
    if (Character.isSupplementaryCodePoint(j));
    for (int k = 2; ; k = 1)
    {
      i += k;
      break;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.UnicodeEscaper
 * JD-Core Version:    0.6.2
 */