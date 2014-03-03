package com.google.wireless.gdata.subscribedfeeds.data;

import com.google.wireless.gdata.data.Entry;

public class SubscribedFeedsEntry extends Entry
{
  private String clientToken;
  private FeedUrl feedUrl;
  private String routingInfo;

  public void clear()
  {
    super.clear();
  }

  public String getClientToken()
  {
    return this.clientToken;
  }

  public String getRoutingInfo()
  {
    return this.routingInfo;
  }

  public FeedUrl getSubscribedFeed()
  {
    return this.feedUrl;
  }

  public void setClientToken(String paramString)
  {
    this.clientToken = paramString;
  }

  public void setRoutingInfo(String paramString)
  {
    this.routingInfo = paramString;
  }

  public void setSubscribedFeed(FeedUrl paramFeedUrl)
  {
    this.feedUrl = paramFeedUrl;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    super.toString(paramStringBuffer);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.subscribedfeeds.data.SubscribedFeedsEntry
 * JD-Core Version:    0.6.2
 */