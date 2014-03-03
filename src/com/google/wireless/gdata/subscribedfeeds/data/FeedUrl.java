package com.google.wireless.gdata.subscribedfeeds.data;

public class FeedUrl
{
  private String authToken;
  private String feed;
  private String service;

  public FeedUrl()
  {
  }

  public FeedUrl(String paramString1, String paramString2, String paramString3)
  {
    setFeed(paramString1);
    setService(paramString2);
    setAuthToken(paramString3);
  }

  public String getAuthToken()
  {
    return this.authToken;
  }

  public String getFeed()
  {
    return this.feed;
  }

  public String getService()
  {
    return this.service;
  }

  public void setAuthToken(String paramString)
  {
    this.authToken = paramString;
  }

  public void setFeed(String paramString)
  {
    this.feed = paramString;
  }

  public void setService(String paramString)
  {
    this.service = paramString;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    toString(localStringBuffer);
    return localStringBuffer.toString();
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("FeedUrl");
    paramStringBuffer.append(" url:").append(getFeed());
    paramStringBuffer.append(" service:").append(getService());
    paramStringBuffer.append(" authToken:").append(getAuthToken());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.subscribedfeeds.data.FeedUrl
 * JD-Core Version:    0.6.2
 */