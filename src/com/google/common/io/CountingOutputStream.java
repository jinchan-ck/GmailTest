package com.google.common.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends FilterOutputStream
{
  private long count;

  public CountingOutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }

  public long getCount()
  {
    return this.count;
  }

  public void write(int paramInt)
    throws IOException
  {
    this.out.write(paramInt);
    this.count = (1L + this.count);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.out.write(paramArrayOfByte, paramInt1, paramInt2);
    this.count += paramInt2;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.CountingOutputStream
 * JD-Core Version:    0.6.2
 */