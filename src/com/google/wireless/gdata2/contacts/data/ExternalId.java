package com.google.wireless.gdata2.contacts.data;

import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.parser.ParseException;

public class ExternalId extends TypedElement
{
  public static final byte TYPE_ACCOUNT = 1;
  public static final byte TYPE_CUSTOMER = 2;
  public static final byte TYPE_NETWORK = 3;
  public static final byte TYPE_ORGANIZATION = 4;
  private String value;

  public ExternalId()
  {
  }

  public ExternalId(String paramString1, byte paramByte, String paramString2)
  {
    super(paramByte, paramString2);
    setValue(paramString1);
  }

  public String getValue()
  {
    return this.value;
  }

  public void setValue(String paramString)
  {
    this.value = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("ExternalId");
    super.toString(paramStringBuffer);
    if (!StringUtils.isEmpty(this.value))
      paramStringBuffer.append(" value:").append(this.value);
  }

  public void validate()
    throws ParseException
  {
    if (this.value == null)
      throw new ParseException("the value must be set");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.ExternalId
 * JD-Core Version:    0.6.2
 */