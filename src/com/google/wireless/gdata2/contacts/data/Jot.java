package com.google.wireless.gdata2.contacts.data;

import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.parser.ParseException;

public class Jot extends TypedElement
{
  public static final byte TYPE_HOME = 1;
  public static final byte TYPE_KEYWORDS = 3;
  public static final byte TYPE_OTHER = 5;
  public static final byte TYPE_USER = 4;
  public static final byte TYPE_WORK = 2;
  private String value;

  public Jot()
  {
  }

  public Jot(String paramString1, byte paramByte, String paramString2)
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
    paramStringBuffer.append("Jot");
    super.toString(paramStringBuffer);
    if (!StringUtils.isEmpty(this.value))
      paramStringBuffer.append(" value:").append(this.value);
  }

  public void validate()
    throws ParseException
  {
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.Jot
 * JD-Core Version:    0.6.2
 */