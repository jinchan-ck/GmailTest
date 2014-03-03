package com.google.wireless.gdata2.contacts.data;

import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.parser.ParseException;

public class UserDefinedField
{
  private String key;
  private String value;

  public UserDefinedField()
  {
  }

  public UserDefinedField(String paramString1, String paramString2)
  {
    this.key = paramString1;
    this.value = paramString2;
  }

  public String getKey()
  {
    return this.key;
  }

  public String getValue()
  {
    return this.value;
  }

  public void setKey(String paramString)
  {
    this.key = paramString;
  }

  public void setValue(String paramString)
  {
    this.value = paramString;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    toString(localStringBuffer);
    return localStringBuffer.toString();
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("UserDefinedField");
    if (!StringUtils.isEmpty(this.key))
      paramStringBuffer.append(" key:").append(this.key);
    if (!StringUtils.isEmpty(this.value))
      paramStringBuffer.append(" value:").append(this.value);
  }

  public void validate()
    throws ParseException
  {
    if ((StringUtils.isEmpty(this.key)) && (StringUtils.isEmpty(this.value)))
      throw new ParseException("key and value can't both be empty");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.UserDefinedField
 * JD-Core Version:    0.6.2
 */