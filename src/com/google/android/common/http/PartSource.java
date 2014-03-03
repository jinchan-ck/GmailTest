package com.google.android.common.http;

import java.io.IOException;
import java.io.InputStream;

public abstract interface PartSource
{
  public abstract InputStream createInputStream()
    throws IOException;

  public abstract String getFileName();

  public abstract long getLength();
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.PartSource
 * JD-Core Version:    0.6.2
 */