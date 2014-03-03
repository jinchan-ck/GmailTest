package com.google.wireless.gdata.client;

import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.MediaEntry;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.parser.GDataParser;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.serializer.GDataSerializer;
import java.io.IOException;
import java.io.InputStream;

public abstract class GDataServiceClient
{
  private final GDataClient gDataClient;
  private final GDataParserFactory gDataParserFactory;

  public GDataServiceClient(GDataClient paramGDataClient, GDataParserFactory paramGDataParserFactory)
  {
    this.gDataClient = paramGDataClient;
    this.gDataParserFactory = paramGDataParserFactory;
  }

  private Entry parseEntry(Class paramClass, InputStream paramInputStream)
    throws ParseException, IOException
  {
    GDataParser localGDataParser = null;
    try
    {
      localGDataParser = this.gDataParserFactory.createParser(paramClass, paramInputStream);
      Entry localEntry = localGDataParser.parseStandaloneEntry();
      return localEntry;
    }
    finally
    {
      if (localGDataParser != null)
        localGDataParser.close();
    }
  }

  public Entry createEntry(String paramString1, String paramString2, Entry paramEntry)
    throws ParseException, IOException, HttpException
  {
    GDataSerializer localGDataSerializer = this.gDataParserFactory.createSerializer(paramEntry);
    InputStream localInputStream = this.gDataClient.createEntry(paramString1, paramString2, localGDataSerializer);
    return parseEntry(paramEntry.getClass(), localInputStream);
  }

  public QueryParams createQueryParams()
  {
    return this.gDataClient.createQueryParams();
  }

  public void deleteEntry(String paramString1, String paramString2)
    throws IOException, HttpException
  {
    this.gDataClient.deleteEntry(paramString1, paramString2);
  }

  public Entry getEntry(Class paramClass, String paramString1, String paramString2)
    throws ParseException, IOException, HttpException
  {
    return parseEntry(paramClass, getGDataClient().getFeedAsStream(paramString1, paramString2));
  }

  protected GDataClient getGDataClient()
  {
    return this.gDataClient;
  }

  protected GDataParserFactory getGDataParserFactory()
  {
    return this.gDataParserFactory;
  }

  public InputStream getMediaEntryAsStream(String paramString1, String paramString2)
    throws IOException, HttpException
  {
    return this.gDataClient.getMediaEntryAsStream(paramString1, paramString2);
  }

  public GDataParser getParserForFeed(Class paramClass, String paramString1, String paramString2)
    throws ParseException, IOException, HttpException
  {
    InputStream localInputStream = this.gDataClient.getFeedAsStream(paramString1, paramString2);
    return this.gDataParserFactory.createParser(paramClass, localInputStream);
  }

  public abstract String getServiceName();

  public Entry updateEntry(Entry paramEntry, String paramString)
    throws ParseException, IOException, HttpException
  {
    String str = paramEntry.getEditUri();
    if (StringUtils.isEmpty(str))
      throw new ParseException("No edit URI -- cannot update.");
    GDataSerializer localGDataSerializer = this.gDataParserFactory.createSerializer(paramEntry);
    InputStream localInputStream = this.gDataClient.updateEntry(str, paramString, localGDataSerializer);
    return parseEntry(paramEntry.getClass(), localInputStream);
  }

  public MediaEntry updateMediaEntry(String paramString1, InputStream paramInputStream, String paramString2, String paramString3)
    throws IOException, HttpException, ParseException
  {
    if (StringUtils.isEmpty(paramString1))
      throw new IllegalArgumentException("No edit URI -- cannot update.");
    return (MediaEntry)parseEntry(MediaEntry.class, this.gDataClient.updateMediaEntry(paramString1, paramString3, paramInputStream, paramString2));
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.client.GDataServiceClient
 * JD-Core Version:    0.6.2
 */