package com.google.common.io;

import java.io.IOException;

public abstract interface ByteProcessor<T>
{
  public abstract T getResult();

  public abstract boolean processBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.ByteProcessor
 * JD-Core Version:    0.6.2
 */