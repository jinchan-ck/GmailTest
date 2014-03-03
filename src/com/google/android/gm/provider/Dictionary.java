package com.google.android.gm.provider;

import java.io.ByteArrayOutputStream;
import java.util.zip.Adler32;

public class Dictionary
{
  private final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
  private final Adler32 checksum = new Adler32();

  public void append(byte[] paramArrayOfByte)
  {
    this.byteStream.write(paramArrayOfByte, 0, paramArrayOfByte.length);
    this.checksum.update(paramArrayOfByte);
  }

  public byte[] getBytes()
  {
    return this.byteStream.toByteArray();
  }

  public long getChecksum()
  {
    return this.checksum.getValue();
  }

  public int size()
  {
    return this.byteStream.size();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.Dictionary
 * JD-Core Version:    0.6.2
 */