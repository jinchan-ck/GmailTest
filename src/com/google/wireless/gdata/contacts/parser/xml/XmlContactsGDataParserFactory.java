package com.google.wireless.gdata.contacts.parser.xml;

import com.google.wireless.gdata.client.GDataParserFactory;
import com.google.wireless.gdata.contacts.data.ContactEntry;
import com.google.wireless.gdata.contacts.data.GroupEntry;
import com.google.wireless.gdata.contacts.serializer.xml.XmlContactEntryGDataSerializer;
import com.google.wireless.gdata.contacts.serializer.xml.XmlGroupEntryGDataSerializer;
import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.MediaEntry;
import com.google.wireless.gdata.parser.GDataParser;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlMediaEntryGDataParser;
import com.google.wireless.gdata.parser.xml.XmlParserFactory;
import com.google.wireless.gdata.serializer.GDataSerializer;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlContactsGDataParserFactory
  implements GDataParserFactory
{
  private final XmlParserFactory xmlFactory;

  public XmlContactsGDataParserFactory(XmlParserFactory paramXmlParserFactory)
  {
    this.xmlFactory = paramXmlParserFactory;
  }

  public GDataParser createGroupEntryFeedParser(InputStream paramInputStream)
    throws ParseException
  {
    try
    {
      XmlPullParser localXmlPullParser = this.xmlFactory.createParser();
      return new XmlGroupEntryGDataParser(paramInputStream, localXmlPullParser);
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException("Could not create XmlPullParser", localXmlPullParserException);
    }
  }

  public GDataParser createMediaEntryFeedParser(InputStream paramInputStream)
    throws ParseException
  {
    try
    {
      XmlPullParser localXmlPullParser = this.xmlFactory.createParser();
      return new XmlMediaEntryGDataParser(paramInputStream, localXmlPullParser);
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
      return new XmlContactsGDataParser(paramInputStream, localXmlPullParser);
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException("Could not create XmlPullParser", localXmlPullParserException);
    }
  }

  public GDataParser createParser(Class paramClass, InputStream paramInputStream)
    throws ParseException
  {
    if (paramClass == ContactEntry.class)
      return createParser(paramInputStream);
    if (paramClass == GroupEntry.class)
      return createGroupEntryFeedParser(paramInputStream);
    if (paramClass == MediaEntry.class)
      return createMediaEntryFeedParser(paramInputStream);
    throw new IllegalArgumentException("unexpected feed type, " + paramClass.getName());
  }

  public GDataSerializer createSerializer(Entry paramEntry)
  {
    if ((paramEntry instanceof ContactEntry))
    {
      ContactEntry localContactEntry = (ContactEntry)paramEntry;
      return new XmlContactEntryGDataSerializer(this.xmlFactory, localContactEntry);
    }
    if ((paramEntry instanceof GroupEntry))
    {
      GroupEntry localGroupEntry = (GroupEntry)paramEntry;
      return new XmlGroupEntryGDataSerializer(this.xmlFactory, localGroupEntry);
    }
    throw new IllegalArgumentException("unexpected entry type, " + paramEntry.getClass().toString());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.parser.xml.XmlContactsGDataParserFactory
 * JD-Core Version:    0.6.2
 */