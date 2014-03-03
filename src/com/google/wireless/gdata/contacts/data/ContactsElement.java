package com.google.wireless.gdata.contacts.data;

import com.google.wireless.gdata.parser.ParseException;

public abstract class ContactsElement
{
  public static final byte TYPE_NONE = -1;
  private boolean isPrimary;
  private String label;
  private byte type = -1;

  public String getLabel()
  {
    return this.label;
  }

  public byte getType()
  {
    return this.type;
  }

  public boolean isPrimary()
  {
    return this.isPrimary;
  }

  public void setIsPrimary(boolean paramBoolean)
  {
    this.isPrimary = paramBoolean;
  }

  public void setLabel(String paramString)
  {
    this.label = paramString;
  }

  public void setType(byte paramByte)
  {
    this.type = paramByte;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    toString(localStringBuffer);
    return localStringBuffer.toString();
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append(" type:").append(this.type);
    paramStringBuffer.append(" isPrimary:").append(this.isPrimary);
    if (this.label != null)
      paramStringBuffer.append(" label:").append(this.label);
  }

  public void validate()
    throws ParseException
  {
    if (((this.label == null) && (this.type == -1)) || ((this.label != null) && (this.type != -1)))
      throw new ParseException("exactly one of label or type must be set");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.data.ContactsElement
 * JD-Core Version:    0.6.2
 */