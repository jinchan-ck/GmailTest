package com.google.wireless.gdata2.parser.xml;

import com.google.wireless.gdata2.data.Entry;
import com.google.wireless.gdata2.data.Feed;
import com.google.wireless.gdata2.data.MediaEntry;
import com.google.wireless.gdata2.parser.ParseException;
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
    throw new IllegalStateException("there is no such thing as a feed of media entries");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.parser.xml.XmlMediaEntryGDataParser
 * JD-Core Version:    0.6.2
 */