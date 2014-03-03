package com.google.wireless.gdata.serializer;

import com.google.wireless.gdata.parser.ParseException;
import java.io.IOException;
import java.io.OutputStream;

public abstract interface GDataSerializer
{
  public static final int FORMAT_CREATE = 1;
  public static final int FORMAT_FULL = 0;
  public static final int FORMAT_UPDATE = 2;

  public abstract String getContentType();

  public abstract void serialize(OutputStream paramOutputStream, int paramInt)
    throws IOException, ParseException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.serializer.GDataSerializer
 * JD-Core Version:    0.6.2
 */