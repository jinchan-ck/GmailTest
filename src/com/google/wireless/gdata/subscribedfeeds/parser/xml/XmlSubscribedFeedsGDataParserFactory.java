package com.google.wireless.gdata.subscribedfeeds.parser.xml;

import com.google.wireless.gdata.client.GDataParserFactory;
import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.parser.GDataParser;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlParserFactory;
import com.google.wireless.gdata.serializer.GDataSerializer;
import com.google.wireless.gdata.subscribedfeeds.data.SubscribedFeedsEntry;
import com.google.wireless.gdata.subscribedfeeds.serializer.xml.XmlSubscribedFeedsEntryGDataSerializer;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlSubscribedFeedsGDataParserFactory
  implements GDataParserFactory
{
  private final XmlParserFactory xmlFactory;

  public XmlSubscribedFeedsGDataParserFactory(XmlParserFactory paramXmlParserFactory)
  {
    this.xmlFactory = paramXmlParserFactory;
  }

  public GDataParser createParser(InputStream paramInputStream)
    throws ParseException
  {
    try
    {
      XmlPullParser localXmlPullParser = this.xmlFactory.createParser();
      return new XmlSubscribedFeedsGDataParser(paramInputStream, localXmlPullParser);
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException("Could not create XmlPullParser", localXmlPullParserException);
    }
  }

  public GDataParser createParser(Class paramClass, InputStream paramInputStream)
    throws ParseException
  {
    if (paramClass != SubscribedFeedsEntry.class)
      throw new IllegalArgumentException("SubscribedFeeds supports only a single feed type");
    return createParser(paramInputStream);
  }

  public GDataSerializer createSerializer(Entry paramEntry)
  {
    if (!(paramEntry instanceof SubscribedFeedsEntry))
      throw new IllegalArgumentException("Expected SubscribedFeedsEntry!");
    SubscribedFeedsEntry localSubscribedFeedsEntry = (SubscribedFeedsEntry)paramEntry;
    return new XmlSubscribedFeedsEntryGDataSerializer(this.xmlFactory, localSubscribedFeedsEntry);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.subscribedfeeds.parser.xml.XmlSubscribedFeedsGDataParserFactory
 * JD-Core Version:    0.6.2
 */