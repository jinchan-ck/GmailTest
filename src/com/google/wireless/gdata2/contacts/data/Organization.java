package com.google.wireless.gdata2.contacts.data;

public class Organization extends ContactsElement
{
  public static final byte TYPE_OTHER = 2;
  public static final byte TYPE_WORK = 1;
  private String name;
  private String nameYomi;
  private String orgDepartment;
  private String orgJobDescription;
  private String orgSymbol;
  private String title;
  private String where;

  public Organization()
  {
  }

  public Organization(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, byte paramByte, String paramString8, boolean paramBoolean)
  {
    super(paramByte, paramString8, paramBoolean);
    this.name = paramString1;
    this.nameYomi = paramString2;
    this.title = paramString3;
    this.orgDepartment = paramString4;
    this.orgJobDescription = paramString5;
    this.orgSymbol = paramString6;
    this.where = paramString7;
  }

  public String getName()
  {
    return this.name;
  }

  public String getNameYomi()
  {
    return this.nameYomi;
  }

  public String getOrgDepartment()
  {
    return this.orgDepartment;
  }

  public String getOrgJobDescription()
  {
    return this.orgJobDescription;
  }

  public String getOrgSymbol()
  {
    return this.orgSymbol;
  }

  public String getTitle()
  {
    return this.title;
  }

  public String getWhere()
  {
    return this.where;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setNameYomi(String paramString)
  {
    this.nameYomi = paramString;
  }

  public void setOrgDepartment(String paramString)
  {
    this.orgDepartment = paramString;
  }

  public void setOrgJobDescription(String paramString)
  {
    this.orgJobDescription = paramString;
  }

  public void setOrgSymbol(String paramString)
  {
    this.orgSymbol = paramString;
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
  }

  public void setWhere(String paramString)
  {
    this.where = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("Organization");
    super.toString(paramStringBuffer);
    if (this.name != null)
      paramStringBuffer.append(" name:").append(this.name);
    if (this.title != null)
      paramStringBuffer.append(" title:").append(this.title);
    if (this.orgDepartment != null)
      paramStringBuffer.append(" orgDepartment:").append(this.orgDepartment);
    if (this.orgJobDescription != null)
      paramStringBuffer.append(" orgJobDescription:").append(this.orgJobDescription);
    if (this.orgSymbol != null)
      paramStringBuffer.append(" orgSymbol:").append(this.orgSymbol);
    if (this.nameYomi != null)
      paramStringBuffer.append(" nameYomi:").append(this.nameYomi);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.Organization
 * JD-Core Version:    0.6.2
 */