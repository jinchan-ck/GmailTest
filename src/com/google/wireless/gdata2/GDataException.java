package com.google.wireless.gdata2;

public class GDataException extends Exception
{
  private final Throwable cause;

  public GDataException()
  {
    this.cause = null;
  }

  public GDataException(String paramString)
  {
    super(paramString);
    this.cause = null;
  }

  public GDataException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.cause = paramThrowable;
  }

  public GDataException(Throwable paramThrowable)
  {
    this("", paramThrowable);
  }

  public Throwable getCause()
  {
    return this.cause;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder().append(super.toString());
    if (this.cause != null);
    for (String str = " " + this.cause.toString(); ; str = "")
      return str;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.GDataException
 * JD-Core Version:    0.6.2
 */