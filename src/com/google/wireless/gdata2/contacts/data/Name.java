package com.google.wireless.gdata2.contacts.data;

public class Name
{
  private String additionalName;
  private String additionalNameYomi;
  private String familyName;
  private String familyNameYomi;
  private String fullName;
  private String fullNameYomi;
  private String givenName;
  private String givenNameYomi;
  private String namePrefix;
  private String nameSuffix;

  public String getAdditionalName()
  {
    return this.additionalName;
  }

  public String getAdditionalNameYomi()
  {
    return this.additionalNameYomi;
  }

  public String getFamilyName()
  {
    return this.familyName;
  }

  public String getFamilyNameYomi()
  {
    return this.familyNameYomi;
  }

  public String getFullName()
  {
    return this.fullName;
  }

  public String getFullNameYomi()
  {
    return this.fullNameYomi;
  }

  public String getGivenName()
  {
    return this.givenName;
  }

  public String getGivenNameYomi()
  {
    return this.givenNameYomi;
  }

  public String getNamePrefix()
  {
    return this.namePrefix;
  }

  public String getNameSuffix()
  {
    return this.nameSuffix;
  }

  public void setAdditionalName(String paramString)
  {
    this.additionalName = paramString;
  }

  public void setAdditionalNameYomi(String paramString)
  {
    this.additionalNameYomi = paramString;
  }

  public void setFamilyName(String paramString)
  {
    this.familyName = paramString;
  }

  public void setFamilyNameYomi(String paramString)
  {
    this.familyNameYomi = paramString;
  }

  public void setFullName(String paramString)
  {
    this.fullName = paramString;
  }

  public void setFullNameYomi(String paramString)
  {
    this.fullNameYomi = paramString;
  }

  public void setGivenName(String paramString)
  {
    this.givenName = paramString;
  }

  public void setGivenNameYomi(String paramString)
  {
    this.givenNameYomi = paramString;
  }

  public void setNamePrefix(String paramString)
  {
    this.namePrefix = paramString;
  }

  public void setNameSuffix(String paramString)
  {
    this.nameSuffix = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("Name");
    if (this.fullName != null)
      paramStringBuffer.append(" fullName:").append(this.fullName);
    if (this.nameSuffix != null)
      paramStringBuffer.append(" nameSuffix:").append(this.nameSuffix);
    if (this.namePrefix != null)
      paramStringBuffer.append(" namePrefix:").append(this.namePrefix);
    if (this.familyName != null)
      paramStringBuffer.append(" familyName:").append(this.familyName);
    if (this.additionalName != null)
      paramStringBuffer.append(" additionalName:").append(this.additionalName);
    if (this.givenName != null)
      paramStringBuffer.append(" givenName:").append(this.givenName);
    if (this.givenNameYomi != null)
      paramStringBuffer.append(" givenNameYomi:").append(this.givenNameYomi);
    if (this.familyNameYomi != null)
      paramStringBuffer.append(" familyNameYomi:").append(this.familyNameYomi);
    if (this.additionalNameYomi != null)
      paramStringBuffer.append(" additionalNameYomi:").append(this.additionalNameYomi);
    if (this.fullNameYomi != null)
      paramStringBuffer.append(" fullNameYomi:").append(this.fullNameYomi);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.Name
 * JD-Core Version:    0.6.2
 */