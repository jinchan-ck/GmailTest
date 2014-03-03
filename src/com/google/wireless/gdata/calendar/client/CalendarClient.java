package com.google.wireless.gdata.calendar.client;

import com.google.wireless.gdata.calendar.data.CalendarEntry;
import com.google.wireless.gdata.client.GDataClient;
import com.google.wireless.gdata.client.GDataParserFactory;
import com.google.wireless.gdata.client.GDataServiceClient;
import com.google.wireless.gdata.client.HttpException;
import com.google.wireless.gdata.client.QueryParams;
import com.google.wireless.gdata.parser.GDataParser;
import com.google.wireless.gdata.parser.ParseException;
import java.io.IOException;
import java.io.InputStream;

public class CalendarClient extends GDataServiceClient
{
  private static final String CALENDAR_BASE_FEED_URL = "http://www.google.com/calendar/feeds/";
  public static final String PROJECTION_PRIVATE_FULL = "/private/full";
  public static final String PROJECTION_PRIVATE_SELF_ATTENDANCE = "/private/full-selfattendance";
  public static final String SERVICE = "cl";

  public CalendarClient(GDataClient paramGDataClient, GDataParserFactory paramGDataParserFactory)
  {
    super(paramGDataClient, paramGDataParserFactory);
  }

  public String getDefaultCalendarUrl(String paramString1, String paramString2, QueryParams paramQueryParams)
  {
    String str1 = "http://www.google.com/calendar/feeds/" + getGDataClient().encodeUri(paramString1);
    String str2 = str1 + paramString2;
    if (paramQueryParams == null)
      return str2;
    return paramQueryParams.generateQueryUrl(str2);
  }

  public GDataParser getParserForUserCalendars(String paramString1, String paramString2)
    throws ParseException, IOException, HttpException
  {
    InputStream localInputStream = getGDataClient().getFeedAsStream(paramString1, paramString2);
    return getGDataParserFactory().createParser(CalendarEntry.class, localInputStream);
  }

  public String getServiceName()
  {
    return "cl";
  }

  public String getUserCalendarsUrl(String paramString)
  {
    return "http://www.google.com/calendar/feeds/" + getGDataClient().encodeUri(paramString);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.calendar.client.CalendarClient
 * JD-Core Version:    0.6.2
 */