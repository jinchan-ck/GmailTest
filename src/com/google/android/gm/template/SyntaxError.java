package com.google.android.gm.template;

public class SyntaxError extends RuntimeException
{
  private final int mColumn;
  private final int mLine;

  public SyntaxError(String paramString, int paramInt1, int paramInt2)
  {
    super(paramString);
    this.mLine = paramInt1;
    this.mColumn = paramInt2;
  }

  public String toString()
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Integer.valueOf(this.mLine);
    arrayOfObject[1] = Integer.valueOf(this.mColumn);
    arrayOfObject[2] = getMessage();
    return String.format("[line %d, column %d] %s", arrayOfObject);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.SyntaxError
 * JD-Core Version:    0.6.2
 */