package com.google.wireless.gdata2.contacts.data;

import com.google.wireless.gdata2.data.StringUtils;

public class CalendarLink extends ContactsElement
{
  public static final byte TYPE_FREE_BUSY = 3;
  public static final byte TYPE_HOME = 1;
  public static final byte TYPE_WORK = 2;
  private String href;

  public CalendarLink()
  {
  }

  public CalendarLink(String paramString1, byte paramByte, String paramString2, boolean paramBoolean)
  {
    super(paramByte, paramString2, paramBoolean);
    setHRef(paramString1);
  }

  public String getHRef()
  {
    return this.href;
  }

  public void setHRef(String paramString)
  {
    this.href = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("CalendarLink");
    super.toString(paramStringBuffer);
    if (!StringUtils.isEmpty(this.href))
      paramStringBuffer.append(" href:").append(this.href);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.CalendarLink
 * JD-Core Version:    0.6.2
 */