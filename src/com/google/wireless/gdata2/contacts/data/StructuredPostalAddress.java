package com.google.wireless.gdata2.contacts.data;

public class StructuredPostalAddress extends ContactsElement
{
  public static final byte TYPE_HOME = 1;
  public static final byte TYPE_OTHER = 3;
  public static final byte TYPE_WORK = 2;
  private String city;
  private String country;
  private String formattedAddress;
  private String neighborhood;
  private String pobox;
  private String postcode;
  private String region;
  private String street;

  public StructuredPostalAddress()
  {
  }

  public StructuredPostalAddress(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, byte paramByte, String paramString9, boolean paramBoolean)
  {
    super(paramByte, paramString9, paramBoolean);
    this.street = paramString1;
    this.pobox = paramString2;
    this.city = paramString3;
    this.postcode = paramString4;
    this.country = paramString5;
    this.region = paramString6;
    this.neighborhood = paramString7;
    this.formattedAddress = paramString8;
  }

  public String getCity()
  {
    return this.city;
  }

  public String getCountry()
  {
    return this.country;
  }

  public String getFormattedAddress()
  {
    return this.formattedAddress;
  }

  public String getNeighborhood()
  {
    return this.neighborhood;
  }

  public String getPobox()
  {
    return this.pobox;
  }

  public String getPostcode()
  {
    return this.postcode;
  }

  public String getRegion()
  {
    return this.region;
  }

  public String getStreet()
  {
    return this.street;
  }

  public void setCity(String paramString)
  {
    this.city = paramString;
  }

  public void setCountry(String paramString)
  {
    this.country = paramString;
  }

  public void setFormattedAddress(String paramString)
  {
    this.formattedAddress = paramString;
  }

  public void setNeighborhood(String paramString)
  {
    this.neighborhood = paramString;
  }

  public void setPobox(String paramString)
  {
    this.pobox = paramString;
  }

  public void setPostcode(String paramString)
  {
    this.postcode = paramString;
  }

  public void setRegion(String paramString)
  {
    this.region = paramString;
  }

  public void setStreet(String paramString)
  {
    this.street = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("PostalAddress");
    super.toString(paramStringBuffer);
    if (this.street != null)
      paramStringBuffer.append(" street:").append(this.street);
    if (this.pobox != null)
      paramStringBuffer.append(" pobox:").append(this.pobox);
    if (this.neighborhood != null)
      paramStringBuffer.append(" neighborhood:").append(this.neighborhood);
    if (this.city != null)
      paramStringBuffer.append(" city:").append(this.city);
    if (this.region != null)
      paramStringBuffer.append(" region:").append(this.region);
    if (this.postcode != null)
      paramStringBuffer.append(" postcode:").append(this.postcode);
    if (this.country != null)
      paramStringBuffer.append(" country:").append(this.country);
    if (this.formattedAddress != null)
      paramStringBuffer.append(" formattedAddress:").append(this.formattedAddress);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.StructuredPostalAddress
 * JD-Core Version:    0.6.2
 */