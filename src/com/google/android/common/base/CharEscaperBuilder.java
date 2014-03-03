package com.google.android.common.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CharEscaperBuilder
{
  private final Map<Character, String> map = new HashMap();
  private int max = -1;

  public CharEscaperBuilder addEscape(char paramChar, String paramString)
  {
    this.map.put(Character.valueOf(paramChar), paramString);
    if (paramChar > this.max)
      this.max = paramChar;
    return this;
  }

  public CharEscaperBuilder addEscapes(char[] paramArrayOfChar, String paramString)
  {
    int i = paramArrayOfChar.length;
    for (int j = 0; j < i; j++)
      addEscape(paramArrayOfChar[j], paramString);
    return this;
  }

  public char[][] toArray()
  {
    char[][] arrayOfChar = new char[1 + this.max][];
    Iterator localIterator = this.map.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      arrayOfChar[((Character)localEntry.getKey()).charValue()] = ((String)localEntry.getValue()).toCharArray();
    }
    return arrayOfChar;
  }

  public CharEscaper toEscaper()
  {
    return new CharArrayDecorator(toArray());
  }

  private static class CharArrayDecorator extends CharEscaper
  {
    private final int replaceLength;
    private final char[][] replacements;

    CharArrayDecorator(char[][] paramArrayOfChar)
    {
      this.replacements = paramArrayOfChar;
      this.replaceLength = paramArrayOfChar.length;
    }

    public String escape(String paramString)
    {
      int i = paramString.length();
      for (int j = 0; ; j++)
        if (j < i)
        {
          int k = paramString.charAt(j);
          if ((k < this.replacements.length) && (this.replacements[k] != null))
            paramString = escapeSlow(paramString, j);
        }
        else
        {
          return paramString;
        }
    }

    protected char[] escape(char paramChar)
    {
      if (paramChar < this.replaceLength)
        return this.replacements[paramChar];
      return null;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.CharEscaperBuilder
 * JD-Core Version:    0.6.2
 */