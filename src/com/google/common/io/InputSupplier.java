package com.google.common.io;

import java.io.IOException;

public abstract interface InputSupplier<T>
{
  public abstract T getInput()
    throws IOException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.InputSupplier
 * JD-Core Version:    0.6.2
 */