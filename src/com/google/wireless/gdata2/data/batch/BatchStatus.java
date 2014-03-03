package com.google.wireless.gdata2.data.batch;

public class BatchStatus
{
  private String content;
  private String contentType;
  private String reason;
  private int statusCode;

  public String getContent()
  {
    return this.content;
  }

  public String getContentType()
  {
    return this.contentType;
  }

  public String getReason()
  {
    return this.reason;
  }

  public int getStatusCode()
  {
    return this.statusCode;
  }

  public void setContent(String paramString)
  {
    this.content = paramString;
  }

  public void setContentType(String paramString)
  {
    this.contentType = paramString;
  }

  public void setReason(String paramString)
  {
    this.reason = paramString;
  }

  public void setStatusCode(int paramInt)
  {
    this.statusCode = paramInt;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.data.batch.BatchStatus
 * JD-Core Version:    0.6.2
 */