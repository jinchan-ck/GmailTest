package com.google.android.gm.template;

import java.util.WeakHashMap;

public class Token
{
  private static final WeakHashMap<String, Token> sNumberPool = new WeakHashMap();
  private static final WeakHashMap<String, Token> sStringPool = new WeakHashMap();
  private static final WeakHashMap<String, Token> sSymbolPool = new WeakHashMap();
  private static final WeakHashMap<String, Token> sWordPool = new WeakHashMap();
  private int mHashCode = 0;
  final Type mType;
  final String mValue;

  private Token(Type paramType, String paramString)
  {
    this.mType = paramType;
    this.mValue = paramString;
  }

  public static final Token getToken(Type paramType, String paramString)
  {
    switch (1.$SwitchMap$com$google$android$gm$template$Token$Type[paramType.ordinal()])
    {
    default:
      throw new RuntimeException("Invalid token type: " + paramType);
    case 1:
    case 2:
    case 3:
    case 4:
    }
    synchronized (sNumberPool)
    {
      while (true)
      {
        Token localToken = (Token)???.get(paramString);
        if (localToken == null)
        {
          localToken = new Token(paramType, paramString);
          ???.put(paramString, localToken);
        }
        return localToken;
        ??? = sStringPool;
        continue;
        ??? = sSymbolPool;
      }
      ??? = sWordPool;
    }
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject)
      return true;
    if ((paramObject != null) && ((paramObject instanceof Token)))
    {
      Token localToken = (Token)paramObject;
      return (this.mType.equals(localToken.mType)) && (this.mValue.equals(localToken.mValue));
    }
    return false;
  }

  public int hashCode()
  {
    if (this.mHashCode == 0)
    {
      (17 * 37);
      this.mHashCode = (37 * (629 + this.mType.hashCode()) + this.mValue.hashCode());
    }
    return this.mHashCode;
  }

  public String toString()
  {
    return this.mType + "(" + this.mValue + ")";
  }

  public static enum Type
  {
    static
    {
      Type[] arrayOfType = new Type[4];
      arrayOfType[0] = NUMBER;
      arrayOfType[1] = STRING;
      arrayOfType[2] = SYMBOL;
      arrayOfType[3] = WORD;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Token
 * JD-Core Version:    0.6.2
 */