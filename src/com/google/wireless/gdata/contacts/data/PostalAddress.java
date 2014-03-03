package com.google.wireless.gdata.contacts.data;

public class PostalAddress extends ContactsElement
{
  public static final byte TYPE_HOME = 1;
  public static final byte TYPE_OTHER = 3;
  public static final byte TYPE_WORK = 2;
  private String value;

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
    paramStringBuffer.append("PostalAddress");
    super.toString(paramStringBuffer);
    if (this.value != null)
      paramStringBuffer.append(" value:").append(this.value);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.data.PostalAddress
 * JD-Core Version:    0.6.2
 */