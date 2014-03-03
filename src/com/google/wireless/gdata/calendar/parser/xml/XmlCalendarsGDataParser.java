package com.google.wireless.gdata.calendar.parser.xml;

import com.google.wireless.gdata.calendar.data.CalendarEntry;
import com.google.wireless.gdata.calendar.data.CalendarsFeed;
import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.Feed;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlGDataParser;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlCalendarsGDataParser extends XmlGDataParser
{
  public XmlCalendarsGDataParser(InputStream paramInputStream, XmlPullParser paramXmlPullParser)
    throws ParseException
  {
    super(paramInputStream, paramXmlPullParser);
  }

  protected Entry createEntry()
  {
    return new CalendarEntry();
  }

  protected Feed createFeed()
  {
    return new CalendarsFeed();
  }

  protected void handleExtraElementInEntry(Entry paramEntry)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = getParser();
    if (!(paramEntry instanceof CalendarEntry))
      throw new IllegalArgumentException("Expected CalendarEntry!");
    CalendarEntry localCalendarEntry = (CalendarEntry)paramEntry;
    String str1 = localXmlPullParser.getName();
    String str4;
    byte b;
    if ("accesslevel".equals(str1))
    {
      str4 = localXmlPullParser.getAttributeValue(null, "value");
      b = 1;
      if ("none".equals(str4))
      {
        b = 0;
        localCalendarEntry.setAccessLevel(b);
      }
    }
    do
    {
      return;
      if ("read".equals(str4))
      {
        b = 1;
        break;
      }
      if ("freebusy".equals(str4))
      {
        b = 2;
        break;
      }
      if ("contributor".equals(str4))
      {
        b = 3;
        break;
      }
      if ("editor".equals(str4))
      {
        b = 3;
        break;
      }
      if ("owner".equals(str4))
      {
        b = 4;
        break;
      }
      if (!"root".equals(str4))
        break;
      b = 5;
      break;
      if ("color".equals(str1))
      {
        localCalendarEntry.setColor(localXmlPullParser.getAttributeValue(null, "value"));
        return;
      }
      if ("hidden".equals(str1))
      {
        String str3 = localXmlPullParser.getAttributeValue(null, "value");
        boolean bool4;
        if ("false".equals(str3))
          bool4 = false;
        while (true)
        {
          localCalendarEntry.setHidden(bool4);
          if (!bool4)
            break;
          localCalendarEntry.setSelected(false);
          return;
          boolean bool3 = "true".equals(str3);
          bool4 = false;
          if (bool3)
            bool4 = true;
        }
      }
      if ("selected".equals(str1))
      {
        String str2 = localXmlPullParser.getAttributeValue(null, "value");
        boolean bool2;
        if ("false".equals(str2))
          bool2 = false;
        while (true)
        {
          localCalendarEntry.setSelected(bool2);
          return;
          boolean bool1 = "true".equals(str2);
          bool2 = false;
          if (bool1)
            bool2 = true;
        }
      }
      if ("timezone".equals(str1))
      {
        localCalendarEntry.setTimezone(localXmlPullParser.getAttributeValue(null, "value"));
        return;
      }
    }
    while (!"overridename".equals(str1));
    localCalendarEntry.setOverrideName(localXmlPullParser.getAttributeValue(null, "value"));
  }

  protected void handleExtraLinkInEntry(String paramString1, String paramString2, String paramString3, Entry paramEntry)
    throws XmlPullParserException, IOException
  {
    if (("alternate".equals(paramString1)) && ("application/atom+xml".equals(paramString2)))
      ((CalendarEntry)paramEntry).setAlternateLink(paramString3);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.calendar.parser.xml.XmlCalendarsGDataParser
 * JD-Core Version:    0.6.2
 */