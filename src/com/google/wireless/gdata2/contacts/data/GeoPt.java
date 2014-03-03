package com.google.wireless.gdata2.contacts.data;

public class GeoPt
{
  private Float elevation;
  private String label;
  private Float latitude;
  private Float longitude;
  private String time;

  public Float getElevation()
  {
    return this.elevation;
  }

  public String getLabel()
  {
    return this.label;
  }

  public Float getLatitute()
  {
    return this.latitude;
  }

  public Float getLongitute()
  {
    return this.longitude;
  }

  public String getTime()
  {
    return this.time;
  }

  public void setElevation(Float paramFloat)
  {
    this.elevation = paramFloat;
  }

  public void setLabel(String paramString)
  {
    this.label = paramString;
  }

  public void setLatitude(Float paramFloat)
  {
    this.latitude = paramFloat;
  }

  public void setLongitude(Float paramFloat)
  {
    this.longitude = paramFloat;
  }

  public void setTime(String paramString)
  {
    this.time = paramString;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    toString(localStringBuffer);
    return localStringBuffer.toString();
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("GeoPt");
    if (this.latitude != null)
      paramStringBuffer.append(" latitude:").append(this.latitude);
    if (this.longitude != null)
      paramStringBuffer.append(" longitude:").append(this.longitude);
    if (this.elevation != null)
      paramStringBuffer.append(" elevation:").append(this.elevation);
    if (this.time != null)
      paramStringBuffer.append(" time:").append(this.time);
    if (this.label != null)
      paramStringBuffer.append(" label:").append(this.label);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.GeoPt
 * JD-Core Version:    0.6.2
 */