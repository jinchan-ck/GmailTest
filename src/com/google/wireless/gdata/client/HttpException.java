package com.google.wireless.gdata.client;

import java.io.InputStream;

public class HttpException extends Exception
{
  public static final int SC_BAD_REQUEST = 400;
  public static final int SC_CONFLICT = 409;
  public static final int SC_FORBIDDEN = 403;
  public static final int SC_GONE = 410;
  public static final int SC_INTERNAL_SERVER_ERROR = 500;
  public static final int SC_NOT_FOUND = 404;
  public static final int SC_UNAUTHORIZED = 401;
  private final InputStream responseStream;
  private final int statusCode;

  public HttpException(String paramString, int paramInt, InputStream paramInputStream)
  {
    super(paramString);
    this.statusCode = paramInt;
    this.responseStream = paramInputStream;
  }

  public InputStream getResponseStream()
  {
    return this.responseStream;
  }

  public int getStatusCode()
  {
    return this.statusCode;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.client.HttpException
 * JD-Core Version:    0.6.2
 */