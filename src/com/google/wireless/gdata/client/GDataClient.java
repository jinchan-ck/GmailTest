package com.google.wireless.gdata.client;

import com.google.wireless.gdata.serializer.GDataSerializer;
import java.io.IOException;
import java.io.InputStream;

public abstract interface GDataClient
{
  public abstract void close();

  public abstract InputStream createEntry(String paramString1, String paramString2, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException;

  public abstract QueryParams createQueryParams();

  public abstract void deleteEntry(String paramString1, String paramString2)
    throws HttpException, IOException;

  public abstract String encodeUri(String paramString);

  public abstract InputStream getFeedAsStream(String paramString1, String paramString2)
    throws HttpException, IOException;

  public abstract InputStream getMediaEntryAsStream(String paramString1, String paramString2)
    throws HttpException, IOException;

  public abstract InputStream updateEntry(String paramString1, String paramString2, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException;

  public abstract InputStream updateMediaEntry(String paramString1, String paramString2, InputStream paramInputStream, String paramString3)
    throws HttpException, IOException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.client.GDataClient
 * JD-Core Version:    0.6.2
 */