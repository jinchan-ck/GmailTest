package com.google.common.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends FilterInputStream
{
  private long count;
  private long mark = -1L;

  public CountingInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }

  public long getCount()
  {
    return this.count;
  }

  public void mark(int paramInt)
  {
    this.in.mark(paramInt);
    this.mark = this.count;
  }

  public int read()
    throws IOException
  {
    int i = this.in.read();
    if (i != -1)
      this.count = (1L + this.count);
    return i;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
    if (i != -1)
      this.count += i;
    return i;
  }

  public void reset()
    throws IOException
  {
    if (!this.in.markSupported())
      throw new IOException("Mark not supported");
    if (this.mark == -1L)
      throw new IOException("Mark not set");
    this.in.reset();
    this.count = this.mark;
  }

  public long skip(long paramLong)
    throws IOException
  {
    long l = this.in.skip(paramLong);
    this.count = (l + this.count);
    return l;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.CountingInputStream
 * JD-Core Version:    0.6.2
 */