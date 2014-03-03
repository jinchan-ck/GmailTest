package com.google.common.io.protocol;

import java.util.Vector;

public class ProtoBufType
{
  private final Vector data = new Vector();
  private final String typeName;
  private final StringBuffer types = new StringBuffer();

  public ProtoBufType()
  {
    this.typeName = null;
  }

  public ProtoBufType(String paramString)
  {
    this.typeName = paramString;
  }

  public static boolean stringEquals(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    if (paramCharSequence1 == paramCharSequence2);
    while (true)
    {
      return true;
      if ((paramCharSequence1 == null) || (paramCharSequence2 == null))
        break;
      int i = paramCharSequence1.length();
      if (i != paramCharSequence2.length())
        break;
      if (((paramCharSequence1 instanceof String)) && ((paramCharSequence2 instanceof String)))
        return paramCharSequence1.equals(paramCharSequence2);
      for (int j = 0; j < i; j++)
        if (paramCharSequence1.charAt(j) != paramCharSequence2.charAt(j))
          return false;
    }
    return false;
  }

  public ProtoBufType addElement(int paramInt1, int paramInt2, Object paramObject)
  {
    while (this.types.length() <= paramInt2)
    {
      this.types.append('\020');
      this.data.addElement(null);
    }
    this.types.setCharAt(paramInt2, (char)paramInt1);
    this.data.setElementAt(paramObject, paramInt2);
    return this;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == null);
    do
    {
      return false;
      if (this == paramObject)
        return true;
    }
    while (getClass() != paramObject.getClass());
    ProtoBufType localProtoBufType = (ProtoBufType)paramObject;
    return stringEquals(this.types, localProtoBufType.types);
  }

  public Object getData(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.data.size()))
      return null;
    return this.data.elementAt(paramInt);
  }

  public int getType(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.types.length()))
      return 16;
    return 0xFF & this.types.charAt(paramInt);
  }

  public int hashCode()
  {
    if (this.types != null)
      return this.types.hashCode();
    return super.hashCode();
  }

  public String toString()
  {
    return this.typeName;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.protocol.ProtoBufType
 * JD-Core Version:    0.6.2
 */