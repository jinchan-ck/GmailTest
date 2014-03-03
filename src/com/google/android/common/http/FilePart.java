package com.google.android.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.util.EncodingUtils;

public class FilePart extends PartBase
{
  private static final byte[] FILE_NAME_BYTES = EncodingUtils.getAsciiBytes("; filename=");
  private PartSource source;

  public FilePart(String paramString1, PartSource paramPartSource, String paramString2, String paramString3)
  {
    super(paramString1, paramString2, paramString3, "binary");
    if (paramPartSource == null)
      throw new IllegalArgumentException("Source may not be null");
    this.source = paramPartSource;
  }

  protected long lengthOfData()
  {
    return this.source.getLength();
  }

  protected void sendData(OutputStream paramOutputStream)
    throws IOException
  {
    if (lengthOfData() == 0L)
      return;
    byte[] arrayOfByte = new byte[4096];
    InputStream localInputStream = this.source.createInputStream();
    try
    {
      while (true)
      {
        int i = localInputStream.read(arrayOfByte);
        if (i < 0)
          break;
        paramOutputStream.write(arrayOfByte, 0, i);
      }
    }
    finally
    {
      localInputStream.close();
    }
    localInputStream.close();
  }

  protected void sendDispositionHeader(OutputStream paramOutputStream)
    throws IOException
  {
    super.sendDispositionHeader(paramOutputStream);
    String str = this.source.getFileName();
    if (str != null)
    {
      paramOutputStream.write(FILE_NAME_BYTES);
      paramOutputStream.write(QUOTE_BYTES);
      paramOutputStream.write(EncodingUtils.getAsciiBytes(str));
      paramOutputStream.write(QUOTE_BYTES);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.FilePart
 * JD-Core Version:    0.6.2
 */