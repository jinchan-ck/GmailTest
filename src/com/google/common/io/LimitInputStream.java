package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LimitInputStream extends FilterInputStream
{
  private long left;
  private long mark = -1L;

  public LimitInputStream(InputStream paramInputStream, long paramLong)
  {
    super(paramInputStream);
    Preconditions.checkNotNull(paramInputStream);
    if (paramLong >= 0L);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "limit must be non-negative");
      this.left = paramLong;
      return;
    }
  }

  public int available()
    throws IOException
  {
    return (int)Math.min(this.in.available(), this.left);
  }

  public void mark(int paramInt)
  {
    this.in.mark(paramInt);
    this.mark = this.left;
  }

  public int read()
    throws IOException
  {
    if (this.left == 0L)
      return -1;
    int i = this.in.read();
    if (i != -1)
      this.left -= 1L;
    return i;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.left == 0L)
      return -1;
    int i = (int)Math.min(paramInt2, this.left);
    int j = this.in.read(paramArrayOfByte, paramInt1, i);
    if (j != -1)
      this.left -= j;
    return j;
  }

  public void reset()
    throws IOException
  {
    if (!this.in.markSupported())
      throw new IOException("Mark not supported");
    if (this.mark == -1L)
      throw new IOException("Mark not set");
    this.in.reset();
    this.left = this.mark;
  }

  public long skip(long paramLong)
    throws IOException
  {
    long l1 = Math.min(paramLong, this.left);
    long l2 = this.in.skip(l1);
    this.left -= l2;
    return l2;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.LimitInputStream
 * JD-Core Version:    0.6.2
 */