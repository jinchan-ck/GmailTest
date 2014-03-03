package com.google.wireless.gdata.contacts.data;

public class EmailAddress extends ContactsElement
{
  public static final byte TYPE_HOME = 1;
  public static final byte TYPE_OTHER = 3;
  public static final byte TYPE_WORK = 2;
  private String address;

  public String getAddress()
  {
    return this.address;
  }

  public void setAddress(String paramString)
  {
    this.address = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("EmailAddress");
    super.toString(paramStringBuffer);
    if (this.address != null)
      paramStringBuffer.append(" address:").append(this.address);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.data.EmailAddress
 * JD-Core Version:    0.6.2
 */