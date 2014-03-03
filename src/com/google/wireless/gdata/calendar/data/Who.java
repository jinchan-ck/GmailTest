package com.google.wireless.gdata.calendar.data;

import com.google.wireless.gdata.data.StringUtils;

public class Who
{
  public static final byte RELATIONSHIP_ATTENDEE = 1;
  public static final byte RELATIONSHIP_NONE = 0;
  public static final byte RELATIONSHIP_ORGANIZER = 2;
  public static final byte RELATIONSHIP_PERFORMER = 3;
  public static final byte RELATIONSHIP_SPEAKER = 4;
  public static final byte STATUS_ACCEPTED = 1;
  public static final byte STATUS_DECLINED = 2;
  public static final byte STATUS_INVITED = 3;
  public static final byte STATUS_NONE = 0;
  public static final byte STATUS_TENTATIVE = 4;
  public static final byte TYPE_NONE = 0;
  public static final byte TYPE_OPTIONAL = 1;
  public static final byte TYPE_REQUIRED = 2;
  private String email;
  private byte relationship = 0;
  private byte status = 0;
  private byte type = 0;
  private String value;

  public String getEmail()
  {
    return this.email;
  }

  public byte getRelationship()
  {
    return this.relationship;
  }

  public byte getStatus()
  {
    return this.status;
  }

  public byte getType()
  {
    return this.type;
  }

  public String getValue()
  {
    return this.value;
  }

  public void setEmail(String paramString)
  {
    this.email = paramString;
  }

  public void setRelationship(byte paramByte)
  {
    this.relationship = paramByte;
  }

  public void setStatus(byte paramByte)
  {
    this.status = paramByte;
  }

  public void setType(byte paramByte)
  {
    this.type = paramByte;
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

  protected void toString(StringBuffer paramStringBuffer)
  {
    if (!StringUtils.isEmpty(this.email))
      paramStringBuffer.append("EMAIL: " + this.email + "\n");
    if (!StringUtils.isEmpty(this.value))
      paramStringBuffer.append("VALUE: " + this.value + "\n");
    paramStringBuffer.append("RELATIONSHIP: " + this.relationship + "\n");
    paramStringBuffer.append("TYPE: " + this.type + "\n");
    paramStringBuffer.append("STATUS: " + this.status + "\n");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.calendar.data.Who
 * JD-Core Version:    0.6.2
 */