package com.google.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

final class MultiInputStream extends InputStream
{
  private InputStream in;
  private Iterator<? extends InputSupplier<? extends InputStream>> it;

  public MultiInputStream(Iterator<? extends InputSupplier<? extends InputStream>> paramIterator)
    throws IOException
  {
    this.it = paramIterator;
    advance();
  }

  private void advance()
    throws IOException
  {
    close();
    if (this.it.hasNext())
      this.in = ((InputStream)((InputSupplier)this.it.next()).getInput());
  }

  public int available()
    throws IOException
  {
    if (this.in == null)
      return 0;
    return this.in.available();
  }

  public void close()
    throws IOException
  {
    if (this.in != null);
    try
    {
      this.in.close();
      return;
    }
    finally
    {
      this.in = null;
    }
  }

  public boolean markSupported()
  {
    return false;
  }

  public int read()
    throws IOException
  {
    if (this.in == null)
      return -1;
    int i = this.in.read();
    if (i == -1)
    {
      advance();
      return read();
    }
    return i;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.in == null)
      return -1;
    int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
    if (i == -1)
    {
      advance();
      return read(paramArrayOfByte, paramInt1, paramInt2);
    }
    return i;
  }

  public long skip(long paramLong)
    throws IOException
  {
    if ((this.in == null) || (paramLong <= 0L))
      return 0L;
    long l = this.in.skip(paramLong);
    if (l != 0L)
      return l;
    if (read() == -1)
      return 0L;
    return 1L + this.in.skip(paramLong - 1L);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.MultiInputStream
 * JD-Core Version:    0.6.2
 */