package com.google.wireless.gdata2.contacts.data;

public class EmailAddress extends ContactsElement
{
  public static final byte TYPE_HOME = 1;
  public static final byte TYPE_OTHER = 3;
  public static final byte TYPE_WORK = 2;
  private String address;
  private String displayName;

  public EmailAddress()
  {
  }

  public EmailAddress(String paramString1, String paramString2, byte paramByte, String paramString3, boolean paramBoolean)
  {
    super(paramByte, paramString3, paramBoolean);
    this.address = paramString1;
    this.displayName = paramString2;
  }

  public String getAddress()
  {
    return this.address;
  }

  public String getDisplayName()
  {
    return this.displayName;
  }

  public void setAddress(String paramString)
  {
    this.address = paramString;
  }

  public void setDisplayName(String paramString)
  {
    this.displayName = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("EmailAddress");
    super.toString(paramStringBuffer);
    if (this.address != null)
      paramStringBuffer.append(" address:").append(this.address);
    if (this.displayName != null)
      paramStringBuffer.append(" displayName:").append(this.displayName);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.EmailAddress
 * JD-Core Version:    0.6.2
 */