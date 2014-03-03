package org.apache.james.mime4j.decoder;

import java.io.IOException;
import java.io.InputStream;

public class Base64InputStream extends InputStream
{
  private static byte[] TRANSLATION = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
  private boolean done = false;
  private final byte[] inputBuffer = new byte[4];
  private int outCount = 0;
  private int outIndex = 0;
  private final int[] outputBuffer = new int[3];
  private final InputStream s;

  public Base64InputStream(InputStream paramInputStream)
  {
    this.s = paramInputStream;
  }

  private void decodeAndEnqueue(int paramInt)
  {
    int i = 0x0 | this.inputBuffer[0] << 18 | this.inputBuffer[1] << 12 | this.inputBuffer[2] << 6 | this.inputBuffer[3];
    if (paramInt == 4)
    {
      this.outputBuffer[0] = (0xFF & i >> 16);
      this.outputBuffer[1] = (0xFF & i >> 8);
      this.outputBuffer[2] = (i & 0xFF);
      this.outCount = 3;
      return;
    }
    if (paramInt == 3)
    {
      this.outputBuffer[0] = (0xFF & i >> 16);
      this.outputBuffer[1] = (0xFF & i >> 8);
      this.outCount = 2;
      return;
    }
    this.outputBuffer[0] = (0xFF & i >> 16);
    this.outCount = 1;
  }

  private void fillBuffer()
    throws IOException
  {
    this.outCount = 0;
    this.outIndex = 0;
    int i = 0;
    while (true)
    {
      int j;
      if (!this.done)
        j = this.s.read();
      int m;
      switch (j)
      {
      default:
        int k = TRANSLATION[j];
        if (k >= 0)
        {
          byte[] arrayOfByte = this.inputBuffer;
          m = i + 1;
          arrayOfByte[i] = k;
          if (m == 4)
            decodeAndEnqueue(m);
        }
        break;
      case -1:
        return;
      case 61:
        this.done = true;
        decodeAndEnqueue(i);
        return;
        i = m;
      }
    }
  }

  public void close()
    throws IOException
  {
    this.s.close();
  }

  public int read()
    throws IOException
  {
    if (this.outIndex == this.outCount)
    {
      fillBuffer();
      if (this.outIndex == this.outCount)
        return -1;
    }
    int[] arrayOfInt = this.outputBuffer;
    int i = this.outIndex;
    this.outIndex = (i + 1);
    return arrayOfInt[i];
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     org.apache.james.mime4j.decoder.Base64InputStream
 * JD-Core Version:    0.6.2
 */