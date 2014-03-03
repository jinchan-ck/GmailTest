package com.google.android.common.base;

public abstract class CharEscaper extends Escaper
{
  private static char[] growBuffer(char[] paramArrayOfChar, int paramInt1, int paramInt2)
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
    for (int j = 0; ; j++)
      if (j < i)
      {
        if (escape(paramString.charAt(j)) != null)
          paramString = escapeSlow(paramString, j);
      }
      else
        return paramString;
  }

  protected abstract char[] escape(char paramChar);

  protected String escapeSlow(String paramString, int paramInt)
  {
    int i = paramString.length();
    char[] arrayOfChar1 = Platform.charBufferFromThreadLocal();
    int j = arrayOfChar1.length;
    int k = 0;
    int m = 0;
    if (paramInt < i)
    {
      char[] arrayOfChar2 = escape(paramString.charAt(paramInt));
      if (arrayOfChar2 == null);
      while (true)
      {
        paramInt++;
        break;
        int i2 = arrayOfChar2.length;
        int i3 = paramInt - m;
        int i4 = i2 + (k + i3);
        if (j < i4)
        {
          j = 32 + (i4 + (i - paramInt));
          arrayOfChar1 = growBuffer(arrayOfChar1, k, j);
        }
        if (i3 > 0)
        {
          paramString.getChars(m, paramInt, arrayOfChar1, k);
          k += i3;
        }
        if (i2 > 0)
        {
          System.arraycopy(arrayOfChar2, 0, arrayOfChar1, k, i2);
          k += i2;
        }
        m = paramInt + 1;
      }
    }
    int n = i - m;
    if (n > 0)
    {
      int i1 = k + n;
      if (j < i1)
        arrayOfChar1 = growBuffer(arrayOfChar1, k, i1);
      paramString.getChars(m, i, arrayOfChar1, k);
      k = i1;
    }
    return new String(arrayOfChar1, 0, k);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.CharEscaper
 * JD-Core Version:    0.6.2
 */