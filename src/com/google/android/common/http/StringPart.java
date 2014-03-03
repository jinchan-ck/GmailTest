package com.google.android.common.http;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.util.EncodingUtils;

public class StringPart extends PartBase
{
  public static final String DEFAULT_CHARSET = "US-ASCII";
  public static final String DEFAULT_CONTENT_TYPE = "text/plain";
  public static final String DEFAULT_TRANSFER_ENCODING = "8bit";
  private byte[] content;
  private String value;

  public StringPart(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, null);
  }

  public StringPart(String paramString1, String paramString2, String paramString3)
  {
  }

  private byte[] getContent()
  {
    if (this.content == null)
      this.content = EncodingUtils.getBytes(this.value, getCharSet());
    return this.content;
  }

  protected long lengthOfData()
  {
    return getContent().length;
  }

  protected void sendData(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(getContent());
  }

  public void setCharSet(String paramString)
  {
    super.setCharSet(paramString);
    this.content = null;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.StringPart
 * JD-Core Version:    0.6.2
 */