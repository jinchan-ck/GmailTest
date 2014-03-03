package com.google.wireless.gdata2.contacts.client;

import com.google.wireless.gdata2.client.GDataClient;
import com.google.wireless.gdata2.client.GDataParserFactory;
import com.google.wireless.gdata2.client.GDataServiceClient;

public class ContactsClient extends GDataServiceClient
{
  public static final String SERVICE = "cp";

  public ContactsClient(GDataClient paramGDataClient, GDataParserFactory paramGDataParserFactory)
  {
    super(paramGDataClient, paramGDataParserFactory);
  }

  public String getProtocolVersion()
  {
    return "3.0";
  }

  public String getServiceName()
  {
    return "cp";
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.client.ContactsClient
 * JD-Core Version:    0.6.2
 */