package com.google.wireless.gdata2.calendar.data;

public class Reminder
{
  public static final byte METHOD_ALERT = 3;
  public static final byte METHOD_DEFAULT = 0;
  public static final byte METHOD_EMAIL = 1;
  public static final byte METHOD_SMS = 2;
  public static final int MINUTES_DEFAULT = -1;
  private byte method = 0;
  private int minutes = -1;

  public byte getMethod()
  {
    return this.method;
  }

  public int getMinutes()
  {
    return this.minutes;
  }

  public void setMethod(byte paramByte)
  {
    this.method = paramByte;
  }

  public void setMinutes(int paramInt)
  {
    this.minutes = paramInt;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    toString(localStringBuffer);
    return localStringBuffer.toString();
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("REMINDER MINUTES: " + this.minutes);
    paramStringBuffer.append("\n");
    paramStringBuffer.append("REMINDER METHOD: " + this.method);
    paramStringBuffer.append("\n");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.calendar.data.Reminder
 * JD-Core Version:    0.6.2
 */