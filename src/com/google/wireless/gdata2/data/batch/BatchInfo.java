package com.google.wireless.gdata2.data.batch;

public class BatchInfo
{
  String id;
  BatchInterrupted interrupted;
  String operation;
  BatchStatus status;

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("id: ").append(this.id);
    localStringBuffer.append(" op: ").append(this.operation);
    if (this.status != null)
      localStringBuffer.append(" sc: ").append(this.status.getStatusCode());
    if (this.interrupted != null)
      localStringBuffer.append(" interrupted: ").append(this.interrupted.getReason());
    return localStringBuffer.toString();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.data.batch.BatchInfo
 * JD-Core Version:    0.6.2
 */