package com.google.wireless.gdata.contacts.data;

import com.google.wireless.gdata.parser.ParseException;

public class Organization extends ContactsElement
{
  public static final byte TYPE_OTHER = 2;
  public static final byte TYPE_WORK = 1;
  private String name;
  private String title;

  public String getName()
  {
    return this.name;
  }

  public String getTitle()
  {
    return this.title;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("Organization");
    super.toString(paramStringBuffer);
    if (this.name != null)
      paramStringBuffer.append(" name:").append(this.name);
    if (this.title != null)
      paramStringBuffer.append(" title:").append(this.title);
  }

  public void validate()
    throws ParseException
  {
    super.validate();
    if ((this.name == null) && (this.title == null))
      throw new ParseException("at least one of name or title must be present");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.data.Organization
 * JD-Core Version:    0.6.2
 */