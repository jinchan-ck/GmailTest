package com.google.wireless.gdata2.contacts.data;

import com.google.wireless.gdata2.data.StringUtils;

public class WebSite extends ContactsElement
{
  public static final byte TYPE_BLOG = 2;
  public static final byte TYPE_FTP = 7;
  public static final byte TYPE_HOME = 4;
  public static final byte TYPE_HOMEPAGE = 1;
  public static final byte TYPE_OTHER = 6;
  public static final byte TYPE_PROFILE = 3;
  public static final byte TYPE_WORK = 5;
  private String href;

  public WebSite()
  {
  }

  public WebSite(String paramString1, byte paramByte, String paramString2, boolean paramBoolean)
  {
    super(paramByte, paramString2, paramBoolean);
    this.href = paramString1;
  }

  public String getHRef()
  {
    return this.href;
  }

  public void setHRef(String paramString)
  {
    this.href = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("WebSite");
    super.toString(paramStringBuffer);
    if (!StringUtils.isEmpty(this.href))
      paramStringBuffer.append(" href:").append(this.href);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.WebSite
 * JD-Core Version:    0.6.2
 */