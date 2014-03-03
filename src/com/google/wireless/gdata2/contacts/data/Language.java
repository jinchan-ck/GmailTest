package com.google.wireless.gdata2.contacts.data;

import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.parser.ParseException;

public class Language
{
  private String code;
  private String label;

  public Language()
  {
  }

  public Language(String paramString1, String paramString2)
  {
    setLabel(paramString1);
    setCode(paramString2);
  }

  public String getCode()
  {
    return this.code;
  }

  public String getLabel()
  {
    return this.label;
  }

  public void setCode(String paramString)
  {
    this.code = paramString;
  }

  public void setLabel(String paramString)
  {
    this.label = paramString;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    toString(localStringBuffer);
    return localStringBuffer.toString();
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("Language");
    if (!StringUtils.isEmpty(this.code))
      paramStringBuffer.append(" code:").append(this.code);
    if (!StringUtils.isEmpty(this.label))
      paramStringBuffer.append(" label:").append(this.label);
  }

  public void validate()
    throws ParseException
  {
    if (((StringUtils.isEmpty(this.label)) && (StringUtils.isEmpty(this.code))) || ((!StringUtils.isEmpty(this.label)) && (!StringUtils.isEmpty(this.code))))
      throw new ParseException("exactly one of label or code must be set");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.Language
 * JD-Core Version:    0.6.2
 */