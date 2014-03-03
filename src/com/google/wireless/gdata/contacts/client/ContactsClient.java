package com.google.wireless.gdata.contacts.client;

import com.google.wireless.gdata.client.GDataClient;
import com.google.wireless.gdata.client.GDataParserFactory;
import com.google.wireless.gdata.client.GDataServiceClient;

public class ContactsClient extends GDataServiceClient
{
  public static final String SERVICE = "cp";

  public ContactsClient(GDataClient paramGDataClient, GDataParserFactory paramGDataParserFactory)
  {
    super(paramGDataClient, paramGDataParserFactory);
  }

  public String getServiceName()
  {
    return "cp";
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.client.ContactsClient
 * JD-Core Version:    0.6.2
 */