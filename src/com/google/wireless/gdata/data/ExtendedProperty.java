package com.google.wireless.gdata.data;

import com.google.wireless.gdata.parser.ParseException;

public class ExtendedProperty
{
  private String name;
  private String value;
  private String xmlBlob;

  public String getName()
  {
    return this.name;
  }

  public String getValue()
  {
    return this.value;
  }

  public String getXmlBlob()
  {
    return this.xmlBlob;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setValue(String paramString)
  {
    this.value = paramString;
  }

  public void setXmlBlob(String paramString)
  {
    this.xmlBlob = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("ExtendedProperty");
    if (this.name != null)
      paramStringBuffer.append(" name:").append(this.name);
    if (this.value != null)
      paramStringBuffer.append(" value:").append(this.value);
    if (this.xmlBlob != null)
      paramStringBuffer.append(" xmlBlob:").append(this.xmlBlob);
  }

  public void validate()
    throws ParseException
  {
    if (this.name == null)
      throw new ParseException("name must not be null");
    if (((this.value == null) && (this.xmlBlob == null)) || ((this.value != null) && (this.xmlBlob != null)))
      throw new ParseException("exactly one of value and xmlBlob must be present");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.data.ExtendedProperty
 * JD-Core Version:    0.6.2
 */