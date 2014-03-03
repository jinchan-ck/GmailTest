package com.google.wireless.gdata2.serializer.xml;

import com.google.wireless.gdata2.client.GDataParserFactory;
import com.google.wireless.gdata2.data.Entry;
import com.google.wireless.gdata2.parser.ParseException;
import com.google.wireless.gdata2.parser.xml.XmlNametable;
import com.google.wireless.gdata2.parser.xml.XmlParserFactory;
import com.google.wireless.gdata2.serializer.GDataSerializer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class XmlBatchGDataSerializer
  implements GDataSerializer
{
  private final Enumeration batch;
  private final GDataParserFactory gdataFactory;
  private final XmlParserFactory xmlFactory;

  public XmlBatchGDataSerializer(GDataParserFactory paramGDataParserFactory, XmlParserFactory paramXmlParserFactory, Enumeration paramEnumeration)
  {
    this.gdataFactory = paramGDataParserFactory;
    this.xmlFactory = paramXmlParserFactory;
    this.batch = paramEnumeration;
  }

  private static void declareNamespaces(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.setPrefix("", "http://www.w3.org/2005/Atom");
    paramXmlSerializer.setPrefix("gd", "http://schemas.google.com/g/2005");
    paramXmlSerializer.setPrefix("batch", "http://schemas.google.com/gdata/batch");
  }

  public String getContentType()
  {
    return "application/atom+xml";
  }

  public boolean getSupportsPartial()
  {
    return false;
  }

  public void serialize(OutputStream paramOutputStream, int paramInt)
    throws IOException, ParseException
  {
    XmlSerializer localXmlSerializer;
    int i;
    try
    {
      localXmlSerializer = this.xmlFactory.createSerializer();
      localXmlSerializer.setOutput(paramOutputStream, XmlNametable.UTF8);
      localXmlSerializer.startDocument(XmlNametable.UTF8, Boolean.FALSE);
      declareNamespaces(localXmlSerializer);
      i = 1;
      while (this.batch.hasMoreElements())
      {
        Entry localEntry = (Entry)this.batch.nextElement();
        XmlEntryGDataSerializer localXmlEntryGDataSerializer = (XmlEntryGDataSerializer)this.gdataFactory.createSerializer(localEntry);
        if (i != 0)
        {
          i = 0;
          localXmlSerializer.startTag("http://www.w3.org/2005/Atom", XmlNametable.FEED);
          localXmlEntryGDataSerializer.declareExtraEntryNamespaces(localXmlSerializer);
        }
        localXmlEntryGDataSerializer.serialize(paramOutputStream, 3);
      }
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException("Unable to create XmlSerializer.", localXmlPullParserException);
    }
    if (i != 0)
      localXmlSerializer.startTag("http://www.w3.org/2005/Atom", XmlNametable.FEED);
    localXmlSerializer.endTag("http://www.w3.org/2005/Atom", XmlNametable.FEED);
    localXmlSerializer.endDocument();
    localXmlSerializer.flush();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.serializer.xml.XmlBatchGDataSerializer
 * JD-Core Version:    0.6.2
 */