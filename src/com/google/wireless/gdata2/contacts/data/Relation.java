package com.google.wireless.gdata2.contacts.data;

import com.google.wireless.gdata2.data.StringUtils;

public class Relation extends TypedElement
{
  public static final byte TYPE_ASSISTANT = 1;
  public static final byte TYPE_BROTHER = 2;
  public static final byte TYPE_CHILD = 3;
  public static final byte TYPE_DOMESTICPARTNER = 4;
  public static final byte TYPE_FATHER = 5;
  public static final byte TYPE_FRIEND = 6;
  public static final byte TYPE_MANAGER = 7;
  public static final byte TYPE_MOTHER = 8;
  public static final byte TYPE_PARENT = 9;
  public static final byte TYPE_PARTNER = 10;
  public static final byte TYPE_REFERREDBY = 11;
  public static final byte TYPE_RELATIVE = 12;
  public static final byte TYPE_SISTER = 13;
  public static final byte TYPE_SPOUSE = 14;
  private String text;

  public Relation()
  {
  }

  public Relation(String paramString1, byte paramByte, String paramString2)
  {
    super(paramByte, paramString2);
    this.text = paramString1;
  }

  public String getText()
  {
    return this.text;
  }

  public void setText(String paramString)
  {
    this.text = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("Relation");
    super.toString(paramStringBuffer);
    if (!StringUtils.isEmpty(this.text))
      paramStringBuffer.append(" text:").append(this.text);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.Relation
 * JD-Core Version:    0.6.2
 */