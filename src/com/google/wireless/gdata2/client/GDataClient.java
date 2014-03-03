package com.google.wireless.gdata2.client;

import com.google.wireless.gdata2.serializer.GDataSerializer;
import java.io.IOException;
import java.io.InputStream;

public abstract interface GDataClient
{
  public abstract void close();

  public abstract InputStream createEntry(String paramString1, String paramString2, String paramString3, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException;

  public abstract QueryParams createQueryParams();

  public abstract void deleteEntry(String paramString1, String paramString2, String paramString3)
    throws HttpException, IOException;

  public abstract String encodeUri(String paramString);

  public abstract InputStream getFeedAsStream(String paramString1, String paramString2, String paramString3, String paramString4)
    throws HttpException, IOException;

  public abstract InputStream getMediaEntryAsStream(String paramString1, String paramString2, String paramString3, String paramString4)
    throws HttpException, IOException;

  public abstract InputStream submitBatch(String paramString1, String paramString2, String paramString3, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException;

  public abstract InputStream updateEntry(String paramString1, String paramString2, String paramString3, String paramString4, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException;

  public abstract InputStream updateMediaEntry(String paramString1, String paramString2, String paramString3, String paramString4, InputStream paramInputStream, String paramString5)
    throws HttpException, IOException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.client.GDataClient
 * JD-Core Version:    0.6.2
 */