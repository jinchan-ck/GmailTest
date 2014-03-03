package com.google.android.common.http;

public abstract class PartBase extends Part
{
  private String charSet;
  private String contentType;
  private String name;
  private String transferEncoding;

  public PartBase(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramString1 == null)
      throw new IllegalArgumentException("Name must not be null");
    this.name = paramString1;
    this.contentType = paramString2;
    this.charSet = paramString3;
    this.transferEncoding = paramString4;
  }

  public String getCharSet()
  {
    return this.charSet;
  }

  public String getContentType()
  {
    return this.contentType;
  }

  public String getName()
  {
    return this.name;
  }

  public String getTransferEncoding()
  {
    return this.transferEncoding;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.PartBase
 * JD-Core Version:    0.6.2
 */