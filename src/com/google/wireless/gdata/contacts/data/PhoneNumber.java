package com.google.wireless.gdata.contacts.data;

public class PhoneNumber extends ContactsElement
{
  public static final byte TYPE_HOME = 2;
  public static final byte TYPE_HOME_FAX = 5;
  public static final byte TYPE_MOBILE = 1;
  public static final byte TYPE_OTHER = 7;
  public static final byte TYPE_PAGER = 6;
  public static final byte TYPE_WORK = 3;
  public static final byte TYPE_WORK_FAX = 4;
  private String phoneNumber;

  public String getPhoneNumber()
  {
    return this.phoneNumber;
  }

  public void setPhoneNumber(String paramString)
  {
    this.phoneNumber = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("PhoneNumber");
    super.toString(paramStringBuffer);
    if (this.phoneNumber != null)
      paramStringBuffer.append(" phoneNumber:").append(this.phoneNumber);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.data.PhoneNumber
 * JD-Core Version:    0.6.2
 */