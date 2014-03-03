package com.google.wireless.gdata2.data.batch;

public class BatchInterrupted
{
  private int error;
  private String reason;
  private int success;
  private int total;

  public int getErrorCount()
  {
    return this.error;
  }

  public String getReason()
  {
    return this.reason;
  }

  public int getSuccessCount()
  {
    return this.success;
  }

  public int getTotalCount()
  {
    return this.total;
  }

  public void setErrorCount(int paramInt)
  {
    this.error = paramInt;
  }

  public void setReason(String paramString)
  {
    this.reason = paramString;
  }

  public void setSuccessCount(int paramInt)
  {
    this.success = paramInt;
  }

  public void setTotalCount(int paramInt)
  {
    this.total = paramInt;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.data.batch.BatchInterrupted
 * JD-Core Version:    0.6.2
 */