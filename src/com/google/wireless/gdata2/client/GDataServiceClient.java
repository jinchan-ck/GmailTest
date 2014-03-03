package com.google.wireless.gdata2.client;

import com.google.wireless.gdata2.ConflictDetectedException;
import com.google.wireless.gdata2.data.Entry;
import com.google.wireless.gdata2.data.MediaEntry;
import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.parser.GDataParser;
import com.google.wireless.gdata2.parser.ParseException;
import com.google.wireless.gdata2.serializer.GDataSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public abstract class GDataServiceClient
{
  protected static String DEFAULT_GDATA_VERSION = "2.0";
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

  protected void convertHttpExceptionForEntryReads(String paramString, HttpException paramHttpException)
    throws AuthenticationException, HttpException, ResourceNotFoundException, ResourceNotModifiedException, ForbiddenException
  {
    switch (paramHttpException.getStatusCode())
    {
    default:
      throw new HttpException(paramString + ": " + paramHttpException.getMessage(), paramHttpException.getStatusCode(), paramHttpException.getResponseStream());
    case 403:
      throw new ForbiddenException(paramString, paramHttpException);
    case 401:
      throw new AuthenticationException(paramString, paramHttpException);
    case 404:
      throw new ResourceNotFoundException(paramString, paramHttpException);
    case 304:
    }
    throw new ResourceNotModifiedException(paramString, paramHttpException);
  }

  protected void convertHttpExceptionForFeedReads(String paramString, HttpException paramHttpException)
    throws AuthenticationException, ResourceGoneException, ResourceNotModifiedException, HttpException, ForbiddenException
  {
    switch (paramHttpException.getStatusCode())
    {
    default:
      throw new HttpException(paramString + ": " + paramHttpException.getMessage(), paramHttpException.getStatusCode(), paramHttpException.getResponseStream());
    case 403:
      throw new ForbiddenException(paramString, paramHttpException);
    case 401:
      throw new AuthenticationException(paramString, paramHttpException);
    case 410:
      throw new ResourceGoneException(paramString, paramHttpException);
    case 304:
    }
    throw new ResourceNotModifiedException(paramString, paramHttpException);
  }

  protected void convertHttpExceptionForWrites(Class paramClass, String paramString, HttpException paramHttpException)
    throws ConflictDetectedException, AuthenticationException, PreconditionFailedException, ParseException, HttpException, IOException, ForbiddenException, ResourceNotFoundException, BadRequestException
  {
    switch (paramHttpException.getStatusCode())
    {
    case 402:
    case 405:
    case 406:
    case 407:
    case 408:
    case 410:
    case 411:
    default:
      throw new HttpException(paramString + ": " + paramHttpException.getMessage(), paramHttpException.getStatusCode(), paramHttpException.getResponseStream());
    case 409:
      Entry localEntry = null;
      if (paramClass != null)
      {
        InputStream localInputStream = paramHttpException.getResponseStream();
        localEntry = null;
        if (localInputStream != null)
          localEntry = parseEntry(paramClass, paramHttpException.getResponseStream());
      }
      throw new ConflictDetectedException(localEntry);
    case 400:
      throw new BadRequestException(paramString, paramHttpException);
    case 403:
      throw new ForbiddenException(paramString, paramHttpException);
    case 401:
      throw new AuthenticationException(paramString, paramHttpException);
    case 412:
      throw new PreconditionFailedException(paramString, paramHttpException);
    case 404:
    }
    throw new ResourceNotFoundException(paramString, paramHttpException);
  }

  protected void convertHttpExceptionsForBatches(String paramString, HttpException paramHttpException)
    throws AuthenticationException, ParseException, HttpException, ForbiddenException, BadRequestException
  {
    switch (paramHttpException.getStatusCode())
    {
    case 402:
    default:
      throw new HttpException(paramString + ": " + paramHttpException.getMessage(), paramHttpException.getStatusCode(), paramHttpException.getResponseStream());
    case 403:
      throw new ForbiddenException(paramString, paramHttpException);
    case 401:
      throw new AuthenticationException(paramString, paramHttpException);
    case 400:
    }
    throw new BadRequestException(paramString, paramHttpException);
  }

  public Entry createEntry(String paramString1, String paramString2, Entry paramEntry)
    throws ConflictDetectedException, AuthenticationException, PreconditionFailedException, HttpException, ParseException, IOException, ForbiddenException, BadRequestException
  {
    GDataSerializer localGDataSerializer = this.gDataParserFactory.createSerializer(paramEntry);
    try
    {
      InputStream localInputStream = this.gDataClient.createEntry(paramString1, paramString2, getProtocolVersion(), localGDataSerializer);
      Entry localEntry = parseEntry(paramEntry.getClass(), localInputStream);
      return localEntry;
    }
    catch (HttpException localHttpException)
    {
      try
      {
        convertHttpExceptionForWrites(paramEntry.getClass(), "Could not create entry " + paramString1, localHttpException);
        return null;
      }
      catch (ResourceNotFoundException localResourceNotFoundException)
      {
      }
      throw localHttpException;
    }
  }

  public QueryParams createQueryParams()
  {
    return this.gDataClient.createQueryParams();
  }

  public void deleteEntry(String paramString1, String paramString2, String paramString3)
    throws AuthenticationException, ConflictDetectedException, PreconditionFailedException, HttpException, ParseException, IOException, ForbiddenException, ResourceNotFoundException, BadRequestException
  {
    try
    {
      this.gDataClient.deleteEntry(paramString1, paramString2, paramString3);
      return;
    }
    catch (HttpException localHttpException)
    {
      while (localHttpException.getStatusCode() == 404);
      convertHttpExceptionForWrites(null, "Unable to delete " + paramString1, localHttpException);
    }
  }

  public Entry getEntry(Class paramClass, String paramString1, String paramString2, String paramString3)
    throws AuthenticationException, ResourceNotFoundException, ResourceNotModifiedException, HttpException, ParseException, IOException, ForbiddenException
  {
    try
    {
      Entry localEntry = parseEntry(paramClass, getGDataClient().getFeedAsStream(paramString1, paramString2, paramString3, getProtocolVersion()));
      return localEntry;
    }
    catch (HttpException localHttpException)
    {
      convertHttpExceptionForEntryReads("Could not fetch entry " + paramString1, localHttpException);
    }
    return null;
  }

  protected GDataClient getGDataClient()
  {
    return this.gDataClient;
  }

  protected GDataParserFactory getGDataParserFactory()
  {
    return this.gDataParserFactory;
  }

  public InputStream getMediaEntryAsStream(String paramString1, String paramString2, String paramString3)
    throws AuthenticationException, ResourceGoneException, ResourceNotModifiedException, ResourceNotFoundException, HttpException, IOException, ForbiddenException
  {
    try
    {
      InputStream localInputStream = this.gDataClient.getMediaEntryAsStream(paramString1, paramString2, paramString3, getProtocolVersion());
      return localInputStream;
    }
    catch (HttpException localHttpException)
    {
      convertHttpExceptionForEntryReads("Could not fetch media entry " + paramString1, localHttpException);
    }
    return null;
  }

  public GDataParser getParserForFeed(Class paramClass, String paramString1, String paramString2, String paramString3)
    throws AuthenticationException, ResourceGoneException, ResourceNotModifiedException, HttpException, ParseException, IOException, ForbiddenException
  {
    try
    {
      InputStream localInputStream = this.gDataClient.getFeedAsStream(paramString1, paramString2, paramString3, getProtocolVersion());
      GDataParser localGDataParser = this.gDataParserFactory.createParser(paramClass, localInputStream);
      return localGDataParser;
    }
    catch (HttpException localHttpException)
    {
      convertHttpExceptionForFeedReads("Could not fetch feed " + paramString1, localHttpException);
    }
    return null;
  }

  public abstract String getProtocolVersion();

  public abstract String getServiceName();

  public GDataParser submitBatch(Class paramClass, String paramString1, String paramString2, Enumeration paramEnumeration)
    throws AuthenticationException, HttpException, ParseException, IOException, ForbiddenException, BadRequestException
  {
    GDataSerializer localGDataSerializer = this.gDataParserFactory.createSerializer(paramEnumeration);
    try
    {
      InputStream localInputStream = this.gDataClient.submitBatch(paramString1, paramString2, getProtocolVersion(), localGDataSerializer);
      GDataParser localGDataParser = this.gDataParserFactory.createParser(paramClass, localInputStream);
      return localGDataParser;
    }
    catch (HttpException localHttpException)
    {
      convertHttpExceptionsForBatches("Could not submit batch " + paramString1, localHttpException);
    }
    return null;
  }

  public Entry updateEntry(Entry paramEntry, String paramString)
    throws AuthenticationException, ConflictDetectedException, PreconditionFailedException, HttpException, ParseException, IOException, ForbiddenException, ResourceNotFoundException, BadRequestException
  {
    String str = paramEntry.getEditUri();
    if (StringUtils.isEmpty(str))
      throw new ParseException("No edit URI -- cannot update.");
    GDataSerializer localGDataSerializer = this.gDataParserFactory.createSerializer(paramEntry);
    try
    {
      InputStream localInputStream = this.gDataClient.updateEntry(str, paramString, paramEntry.getETag(), getProtocolVersion(), localGDataSerializer);
      Entry localEntry = parseEntry(paramEntry.getClass(), localInputStream);
      return localEntry;
    }
    catch (HttpException localHttpException)
    {
      convertHttpExceptionForWrites(paramEntry.getClass(), "Could not update entry " + str, localHttpException);
    }
    return null;
  }

  public MediaEntry updateMediaEntry(String paramString1, InputStream paramInputStream, String paramString2, String paramString3, String paramString4)
    throws AuthenticationException, ConflictDetectedException, PreconditionFailedException, HttpException, ParseException, IOException, ForbiddenException, ResourceNotFoundException, BadRequestException
  {
    if (StringUtils.isEmpty(paramString1))
      throw new IllegalArgumentException("No edit URI -- cannot update.");
    try
    {
      MediaEntry localMediaEntry = (MediaEntry)parseEntry(MediaEntry.class, this.gDataClient.updateMediaEntry(paramString1, paramString3, paramString4, getProtocolVersion(), paramInputStream, paramString2));
      return localMediaEntry;
    }
    catch (HttpException localHttpException)
    {
      convertHttpExceptionForWrites(MediaEntry.class, "Could not update entry " + paramString1, localHttpException);
    }
    return null;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.client.GDataServiceClient
 * JD-Core Version:    0.6.2
 */