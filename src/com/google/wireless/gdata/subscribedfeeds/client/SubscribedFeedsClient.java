package com.google.wireless.gdata.subscribedfeeds.client;

import com.google.wireless.gdata.client.GDataClient;
import com.google.wireless.gdata.client.GDataParserFactory;
import com.google.wireless.gdata.client.GDataServiceClient;

public class SubscribedFeedsClient extends GDataServiceClient
{
  public static final String SERVICE = "mail";

  public SubscribedFeedsClient(GDataClient paramGDataClient, GDataParserFactory paramGDataParserFactory)
  {
    super(paramGDataClient, paramGDataParserFactory);
  }

  public String getServiceName()
  {
    return "mail";
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.subscribedfeeds.client.SubscribedFeedsClient
 * JD-Core Version:    0.6.2
 */