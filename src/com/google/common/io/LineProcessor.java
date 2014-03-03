package com.google.common.io;

import java.io.IOException;

public abstract interface LineProcessor<T>
{
  public abstract T getResult();

  public abstract boolean processLine(String paramString)
    throws IOException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.LineProcessor
 * JD-Core Version:    0.6.2
 */