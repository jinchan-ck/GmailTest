package com.google.common.io;

import java.io.IOException;

public abstract interface OutputSupplier<T>
{
  public abstract T getOutput()
    throws IOException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.OutputSupplier
 * JD-Core Version:    0.6.2
 */