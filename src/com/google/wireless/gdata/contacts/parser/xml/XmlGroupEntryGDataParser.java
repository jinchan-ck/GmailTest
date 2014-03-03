package com.google.wireless.gdata.contacts.parser.xml;

import com.google.wireless.gdata.contacts.data.GroupEntry;
import com.google.wireless.gdata.contacts.data.GroupsFeed;
import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.Feed;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlGDataParser;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;

public class XmlGroupEntryGDataParser extends XmlGDataParser
{
  public XmlGroupEntryGDataParser(InputStream paramInputStream, XmlPullParser paramXmlPullParser)
    throws ParseException
  {
    super(paramInputStream, paramXmlPullParser);
  }

  protected Entry createEntry()
  {
    return new GroupEntry();
  }

  protected Feed createFeed()
  {
    return new GroupsFeed();
  }

  protected void handleExtraElementInEntry(Entry paramEntry)
  {
    XmlPullParser localXmlPullParser = getParser();
    if (!(paramEntry instanceof GroupEntry))
      throw new IllegalArgumentException("Expected GroupEntry!");
    GroupEntry localGroupEntry = (GroupEntry)paramEntry;
    if ("systemGroup".equals(localXmlPullParser.getName()))
    {
      String str = localXmlPullParser.getAttributeValue(null, "id");
      if (StringUtils.isEmpty(str))
        str = null;
      localGroupEntry.setSystemGroup(str);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.parser.xml.XmlGroupEntryGDataParser
 * JD-Core Version:    0.6.2
 */