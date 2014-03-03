package com.google.wireless.gdata2.client;

import com.google.wireless.gdata2.GDataException;

public class ResourceUnavailableException extends GDataException
{
  private long retryAfter;

  public ResourceUnavailableException(long paramLong)
  {
    this.retryAfter = paramLong;
  }

  public ResourceUnavailableException(String paramString, long paramLong)
  {
    super(paramString);
    this.retryAfter = paramLong;
  }

  public ResourceUnavailableException(String paramString, Throwable paramThrowable, long paramLong)
  {
    super(paramString, paramThrowable);
    this.retryAfter = paramLong;
  }

  public long getRetryAfter()
  {
    return this.retryAfter;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.client.ResourceUnavailableException
 * JD-Core Version:    0.6.2
 */