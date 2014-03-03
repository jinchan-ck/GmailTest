package com.google.wireless.gdata2.contacts.data;

public class Event extends TypedElement
{
  public static final byte TYPE_ANNIVERSARY = 1;
  public static final byte TYPE_OTHER = 2;
  private String startDate;

  public Event()
  {
  }

  public Event(String paramString1, byte paramByte, String paramString2)
  {
    super(paramByte, paramString2);
    this.startDate = paramString1;
  }

  public String getStartDate()
  {
    return this.startDate;
  }

  public void setStartDate(String paramString)
  {
    this.startDate = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("Event");
    super.toString(paramStringBuffer);
    paramStringBuffer.append(" date:").append(this.startDate.toString());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.Event
 * JD-Core Version:    0.6.2
 */