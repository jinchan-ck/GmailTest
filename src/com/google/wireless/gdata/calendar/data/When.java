package com.google.wireless.gdata.calendar.data;

import com.google.wireless.gdata.data.StringUtils;

public class When
{
  private final String endTime;
  private final String startTime;

  public When(String paramString1, String paramString2)
  {
    this.startTime = paramString1;
    this.endTime = paramString2;
  }

  public String getEndTime()
  {
    return this.endTime;
  }

  public String getStartTime()
  {
    return this.startTime;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    toString(localStringBuffer);
    return localStringBuffer.toString();
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    if (!StringUtils.isEmpty(this.startTime))
      paramStringBuffer.append("START TIME: " + this.startTime + "\n");
    if (!StringUtils.isEmpty(this.endTime))
      paramStringBuffer.append("END TIME: " + this.endTime + "\n");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.calendar.data.When
 * JD-Core Version:    0.6.2
 */