package com.google.wireless.gdata.subscribedfeeds.parser.xml;

import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.Feed;
import com.google.wireless.gdata.data.XmlUtils;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlGDataParser;
import com.google.wireless.gdata.subscribedfeeds.data.FeedUrl;
import com.google.wireless.gdata.subscribedfeeds.data.SubscribedFeedsEntry;
import com.google.wireless.gdata.subscribedfeeds.data.SubscribedFeedsFeed;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlSubscribedFeedsGDataParser extends XmlGDataParser
{
  public XmlSubscribedFeedsGDataParser(InputStream paramInputStream, XmlPullParser paramXmlPullParser)
    throws ParseException
  {
    super(paramInputStream, paramXmlPullParser);
  }

  protected Entry createEntry()
  {
    return new SubscribedFeedsEntry();
  }

  protected Feed createFeed()
  {
    return new SubscribedFeedsFeed();
  }

  protected void handleExtraElementInEntry(Entry paramEntry)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = getParser();
    if (!(paramEntry instanceof SubscribedFeedsEntry))
      throw new IllegalArgumentException("Expected SubscribedFeedsEntry!");
    SubscribedFeedsEntry localSubscribedFeedsEntry = (SubscribedFeedsEntry)paramEntry;
    String str = localXmlPullParser.getName();
    if ("feedurl".equals(str))
    {
      FeedUrl localFeedUrl = new FeedUrl();
      localFeedUrl.setFeed(localXmlPullParser.getAttributeValue(null, "value"));
      localFeedUrl.setService(localXmlPullParser.getAttributeValue(null, "service"));
      localFeedUrl.setAuthToken(localXmlPullParser.getAttributeValue(null, "authtoken"));
      localSubscribedFeedsEntry.setSubscribedFeed(localFeedUrl);
    }
    if ("routingInfo".equals(str))
      localSubscribedFeedsEntry.setRoutingInfo(XmlUtils.extractChildText(localXmlPullParser));
    if ("clientToken".equals(str))
      localSubscribedFeedsEntry.setClientToken(XmlUtils.extractChildText(localXmlPullParser));
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.subscribedfeeds.parser.xml.XmlSubscribedFeedsGDataParser
 * JD-Core Version:    0.6.2
 */