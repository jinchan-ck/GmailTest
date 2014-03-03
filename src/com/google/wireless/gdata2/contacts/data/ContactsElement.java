package com.google.wireless.gdata2.contacts.data;

public abstract class ContactsElement extends TypedElement
{
  private boolean isPrimary;

  public ContactsElement()
  {
  }

  public ContactsElement(byte paramByte, String paramString, boolean paramBoolean)
  {
    super(paramByte, paramString);
    this.isPrimary = paramBoolean;
  }

  public boolean isPrimary()
  {
    return this.isPrimary;
  }

  public void setIsPrimary(boolean paramBoolean)
  {
    this.isPrimary = paramBoolean;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    super.toString(paramStringBuffer);
    paramStringBuffer.append(" isPrimary:").append(this.isPrimary);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.ContactsElement
 * JD-Core Version:    0.6.2
 */