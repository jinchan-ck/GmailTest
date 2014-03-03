package com.google.android.common.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EncodingUtils;

public class MultipartEntity extends AbstractHttpEntity
{
  private static byte[] MULTIPART_CHARS = EncodingUtils.getAsciiBytes("-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
  private boolean contentConsumed = false;
  private byte[] multipartBoundary;
  private HttpParams params;
  protected Part[] parts;

  public MultipartEntity(Part[] paramArrayOfPart)
  {
    setContentType("multipart/form-data");
    if (paramArrayOfPart == null)
      throw new IllegalArgumentException("parts cannot be null");
    this.parts = paramArrayOfPart;
    this.params = null;
  }

  private static byte[] generateMultipartBoundary()
  {
    Random localRandom = new Random();
    byte[] arrayOfByte = new byte[30 + localRandom.nextInt(11)];
    for (int i = 0; i < arrayOfByte.length; i++)
      arrayOfByte[i] = MULTIPART_CHARS[localRandom.nextInt(MULTIPART_CHARS.length)];
    return arrayOfByte;
  }

  public InputStream getContent()
    throws IOException, IllegalStateException
  {
    if ((!isRepeatable()) && (this.contentConsumed))
      throw new IllegalStateException("Content has been consumed");
    this.contentConsumed = true;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    Part.sendParts(localByteArrayOutputStream, this.parts, this.multipartBoundary);
    return new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
  }

  public long getContentLength()
  {
    try
    {
      long l = Part.getLengthOfParts(this.parts, getMultipartBoundary());
      return l;
    }
    catch (Exception localException)
    {
    }
    return 0L;
  }

  public Header getContentType()
  {
    StringBuffer localStringBuffer = new StringBuffer("multipart/form-data");
    localStringBuffer.append("; boundary=");
    localStringBuffer.append(EncodingUtils.getAsciiString(getMultipartBoundary()));
    return new BasicHeader("Content-Type", localStringBuffer.toString());
  }

  protected byte[] getMultipartBoundary()
  {
    String str;
    if (this.multipartBoundary == null)
    {
      HttpParams localHttpParams = this.params;
      str = null;
      if (localHttpParams != null)
        str = (String)this.params.getParameter("http.method.multipart.boundary");
      if (str == null)
        break label50;
    }
    label50: for (this.multipartBoundary = EncodingUtils.getAsciiBytes(str); ; this.multipartBoundary = generateMultipartBoundary())
      return this.multipartBoundary;
  }

  public boolean isRepeatable()
  {
    for (int i = 0; i < this.parts.length; i++)
      if (!this.parts[i].isRepeatable())
        return false;
    return true;
  }

  public boolean isStreaming()
  {
    return false;
  }

  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Part.sendParts(paramOutputStream, this.parts, getMultipartBoundary());
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.MultipartEntity
 * JD-Core Version:    0.6.2
 */