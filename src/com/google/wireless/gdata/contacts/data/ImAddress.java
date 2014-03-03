package com.google.wireless.gdata.contacts.data;

public class ImAddress extends ContactsElement
{
  public static final byte PROTOCOL_AIM = 2;
  public static final byte PROTOCOL_CUSTOM = 1;
  public static final byte PROTOCOL_GOOGLE_TALK = 7;
  public static final byte PROTOCOL_ICQ = 8;
  public static final byte PROTOCOL_JABBER = 9;
  public static final byte PROTOCOL_MSN = 3;
  public static final byte PROTOCOL_NONE = 10;
  public static final byte PROTOCOL_QQ = 6;
  public static final byte PROTOCOL_SKYPE = 5;
  public static final byte PROTOCOL_YAHOO = 4;
  public static final byte TYPE_HOME = 1;
  public static final byte TYPE_OTHER = 3;
  public static final byte TYPE_WORK = 2;
  private String address;
  private String protocolCustom;
  private byte protocolPredefined;

  public String getAddress()
  {
    return this.address;
  }

  public String getProtocolCustom()
  {
    return this.protocolCustom;
  }

  public byte getProtocolPredefined()
  {
    return this.protocolPredefined;
  }

  public void setAddress(String paramString)
  {
    this.address = paramString;
  }

  public void setProtocolCustom(String paramString)
  {
    this.protocolCustom = paramString;
  }

  public void setProtocolPredefined(byte paramByte)
  {
    this.protocolPredefined = paramByte;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("ImAddress");
    super.toString(paramStringBuffer);
    paramStringBuffer.append(" protocolPredefined:").append(this.protocolPredefined);
    if (this.protocolCustom != null)
      paramStringBuffer.append(" protocolCustom:").append(this.protocolCustom);
    if (this.address != null)
      paramStringBuffer.append(" address:").append(this.address);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.data.ImAddress
 * JD-Core Version:    0.6.2
 */