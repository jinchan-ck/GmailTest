package com.google.wireless.gdata.subscribedfeeds.serializer.xml;

import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.parser.xml.XmlParserFactory;
import com.google.wireless.gdata.serializer.xml.XmlEntryGDataSerializer;
import com.google.wireless.gdata.subscribedfeeds.data.FeedUrl;
import com.google.wireless.gdata.subscribedfeeds.data.SubscribedFeedsEntry;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public class XmlSubscribedFeedsEntryGDataSerializer extends XmlEntryGDataSerializer
{
  public static final String NAMESPACE_GSYNC = "gsync";
  public static final String NAMESPACE_GSYNC_URI = "http://schemas.google.com/gsync/data";

  public XmlSubscribedFeedsEntryGDataSerializer(XmlParserFactory paramXmlParserFactory, SubscribedFeedsEntry paramSubscribedFeedsEntry)
  {
    super(paramXmlParserFactory, paramSubscribedFeedsEntry);
  }

  private static void serializeClientToken(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      paramString = "";
    paramXmlSerializer.startTag("http://schemas.google.com/gsync/data", "clientToken");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag("http://schemas.google.com/gsync/data", "clientToken");
  }

  private static void serializeFeedUrl(XmlSerializer paramXmlSerializer, FeedUrl paramFeedUrl)
    throws IOException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/gsync/data", "feedurl");
    paramXmlSerializer.attribute(null, "value", paramFeedUrl.getFeed());
    paramXmlSerializer.attribute(null, "service", paramFeedUrl.getService());
    paramXmlSerializer.attribute(null, "authtoken", paramFeedUrl.getAuthToken());
    paramXmlSerializer.endTag("http://schemas.google.com/gsync/data", "feedurl");
  }

  private static void serializeRoutingInfo(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      paramString = "";
    paramXmlSerializer.startTag("http://schemas.google.com/gsync/data", "routingInfo");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag("http://schemas.google.com/gsync/data", "routingInfo");
  }

  protected void declareExtraEntryNamespaces(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.setPrefix("gsync", "http://schemas.google.com/gsync/data");
  }

  protected SubscribedFeedsEntry getSubscribedFeedsEntry()
  {
    return (SubscribedFeedsEntry)getEntry();
  }

  protected void serializeExtraEntryContents(XmlSerializer paramXmlSerializer, int paramInt)
    throws IOException
  {
    SubscribedFeedsEntry localSubscribedFeedsEntry = getSubscribedFeedsEntry();
    serializeFeedUrl(paramXmlSerializer, localSubscribedFeedsEntry.getSubscribedFeed());
    serializeClientToken(paramXmlSerializer, localSubscribedFeedsEntry.getClientToken());
    serializeRoutingInfo(paramXmlSerializer, localSubscribedFeedsEntry.getRoutingInfo());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.subscribedfeeds.serializer.xml.XmlSubscribedFeedsEntryGDataSerializer
 * JD-Core Version:    0.6.2
 */