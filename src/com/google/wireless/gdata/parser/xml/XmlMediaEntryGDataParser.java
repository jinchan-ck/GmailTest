package com.google.wireless.gdata.parser.xml;

import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.Feed;
import com.google.wireless.gdata.data.MediaEntry;
import com.google.wireless.gdata.parser.ParseException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;

public class XmlMediaEntryGDataParser extends XmlGDataParser
{
  public XmlMediaEntryGDataParser(InputStream paramInputStream, XmlPullParser paramXmlPullParser)
    throws ParseException
  {
    super(paramInputStream, paramXmlPullParser);
  }

  protected Entry createEntry()
  {
    return new MediaEntry();
  }

  protected Feed createFeed()
  {
    throw new UnsupportedOperationException("there is no such thing as a feed of media entries");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.parser.xml.XmlMediaEntryGDataParser
 * JD-Core Version:    0.6.2
 */