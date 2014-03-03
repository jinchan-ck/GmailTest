package com.google.wireless.gdata.calendar.parser.xml;

import com.google.wireless.gdata.calendar.data.CalendarEntry;
import com.google.wireless.gdata.calendar.data.EventEntry;
import com.google.wireless.gdata.calendar.serializer.xml.XmlEventEntryGDataSerializer;
import com.google.wireless.gdata.client.GDataParserFactory;
import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.parser.GDataParser;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlParserFactory;
import com.google.wireless.gdata.serializer.GDataSerializer;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlCalendarGDataParserFactory
  implements GDataParserFactory
{
  private final XmlParserFactory xmlFactory;

  public XmlCalendarGDataParserFactory(XmlParserFactory paramXmlParserFactory)
  {
    this.xmlFactory = paramXmlParserFactory;
  }

  public GDataParser createCalendarsFeedParser(InputStream paramInputStream)
    throws ParseException
  {
    try
    {
      XmlPullParser localXmlPullParser = this.xmlFactory.createParser();
      return new XmlCalendarsGDataParser(paramInputStream, localXmlPullParser);
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException("Could not create XmlPullParser", localXmlPullParserException);
    }
  }

  public GDataParser createParser(InputStream paramInputStream)
    throws ParseException
  {
    try
    {
      XmlPullParser localXmlPullParser = this.xmlFactory.createParser();
      return new XmlEventsGDataParser(paramInputStream, localXmlPullParser);
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException("Could not create XmlPullParser", localXmlPullParserException);
    }
  }

  public GDataParser createParser(Class paramClass, InputStream paramInputStream)
    throws ParseException
  {
    if (paramClass == CalendarEntry.class)
      return createCalendarsFeedParser(paramInputStream);
    if (paramClass == EventEntry.class)
      return createParser(paramInputStream);
    throw new IllegalArgumentException("Unknown entry class '" + paramClass.getName() + "' specified.");
  }

  public GDataSerializer createSerializer(Entry paramEntry)
  {
    if (!(paramEntry instanceof EventEntry))
      throw new IllegalArgumentException("Expected EventEntry!");
    EventEntry localEventEntry = (EventEntry)paramEntry;
    return new XmlEventEntryGDataSerializer(this.xmlFactory, localEventEntry);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.calendar.parser.xml.XmlCalendarGDataParserFactory
 * JD-Core Version:    0.6.2
 */