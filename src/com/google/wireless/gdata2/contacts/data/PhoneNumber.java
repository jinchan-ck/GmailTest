package com.google.wireless.gdata2.contacts.data;

public class PhoneNumber extends ContactsElement
{
  public static final byte TYPE_ASSISTANT = 7;
  public static final byte TYPE_CALLBACK = 8;
  public static final byte TYPE_CAR = 9;
  public static final byte TYPE_COMPANY_MAIN = 10;
  public static final byte TYPE_HOME = 2;
  public static final byte TYPE_HOME_FAX = 5;
  public static final byte TYPE_ISDN = 11;
  public static final byte TYPE_MAIN = 12;
  public static final byte TYPE_MOBILE = 1;
  public static final byte TYPE_OTHER = 19;
  public static final byte TYPE_OTHER_FAX = 13;
  public static final byte TYPE_PAGER = 6;
  public static final byte TYPE_RADIO = 14;
  public static final byte TYPE_TELEX = 15;
  public static final byte TYPE_TTY_TDD = 16;
  public static final byte TYPE_WORK = 3;
  public static final byte TYPE_WORK_FAX = 4;
  public static final byte TYPE_WORK_MOBILE = 17;
  public static final byte TYPE_WORK_PAGER = 18;
  private String phoneNumber;

  public PhoneNumber()
  {
  }

  public PhoneNumber(String paramString1, byte paramByte, String paramString2, boolean paramBoolean)
  {
    super(paramByte, paramString2, paramBoolean);
    this.phoneNumber = paramString1;
  }

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
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.PhoneNumber
 * JD-Core Version:    0.6.2
 */