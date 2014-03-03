package com.google.wireless.gdata.serializer.xml;

import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlParserFactory;
import com.google.wireless.gdata.serializer.GDataSerializer;
import java.io.IOException;
import java.io.OutputStream;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class XmlEntryGDataSerializer
  implements GDataSerializer
{
  private final Entry entry;
  private final XmlParserFactory factory;

  public XmlEntryGDataSerializer(XmlParserFactory paramXmlParserFactory, Entry paramEntry)
  {
    this.factory = paramXmlParserFactory;
    this.entry = paramEntry;
  }

  private final void declareEntryNamespaces(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.setPrefix("", "http://www.w3.org/2005/Atom");
    paramXmlSerializer.setPrefix("gd", "http://schemas.google.com/g/2005");
    declareExtraEntryNamespaces(paramXmlSerializer);
  }

  private static void serializeAuthor(XmlSerializer paramXmlSerializer, String paramString1, String paramString2)
    throws IOException
  {
    if ((StringUtils.isEmpty(paramString1)) || (StringUtils.isEmpty(paramString2)))
      return;
    paramXmlSerializer.startTag(null, "author");
    paramXmlSerializer.startTag(null, "name");
    paramXmlSerializer.text(paramString1);
    paramXmlSerializer.endTag(null, "name");
    paramXmlSerializer.startTag(null, "email");
    paramXmlSerializer.text(paramString2);
    paramXmlSerializer.endTag(null, "email");
    paramXmlSerializer.endTag(null, "author");
  }

  private static void serializeCategory(XmlSerializer paramXmlSerializer, String paramString1, String paramString2)
    throws IOException
  {
    if ((StringUtils.isEmpty(paramString1)) && (StringUtils.isEmpty(paramString2)))
      return;
    paramXmlSerializer.startTag(null, "category");
    if (!StringUtils.isEmpty(paramString1))
      paramXmlSerializer.attribute(null, "term", paramString1);
    if (!StringUtils.isEmpty(paramString2))
      paramXmlSerializer.attribute(null, "scheme", paramString2);
    paramXmlSerializer.endTag(null, "category");
  }

  private static void serializeContent(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (paramString == null)
      return;
    paramXmlSerializer.startTag(null, "content");
    paramXmlSerializer.attribute(null, "type", "text");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, "content");
  }

  private final void serializeEntryContents(XmlSerializer paramXmlSerializer, int paramInt)
    throws ParseException, IOException
  {
    if (paramInt != 1)
      serializeId(paramXmlSerializer, this.entry.getId());
    serializeTitle(paramXmlSerializer, this.entry.getTitle());
    if (paramInt != 1)
    {
      serializeLink(paramXmlSerializer, "edit", this.entry.getEditUri(), null);
      serializeLink(paramXmlSerializer, "alternate", this.entry.getHtmlUri(), "text/html");
    }
    serializeSummary(paramXmlSerializer, this.entry.getSummary());
    serializeContent(paramXmlSerializer, this.entry.getContent());
    serializeAuthor(paramXmlSerializer, this.entry.getAuthor(), this.entry.getEmail());
    serializeCategory(paramXmlSerializer, this.entry.getCategory(), this.entry.getCategoryScheme());
    if (paramInt == 0)
      serializePublicationDate(paramXmlSerializer, this.entry.getPublicationDate());
    if (paramInt != 1)
      serializeUpdateDate(paramXmlSerializer, this.entry.getUpdateDate());
    serializeExtraEntryContents(paramXmlSerializer, paramInt);
  }

  private static void serializeId(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag(null, "id");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, "id");
  }

  public static void serializeLink(XmlSerializer paramXmlSerializer, String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString2))
      return;
    paramXmlSerializer.startTag(null, "link");
    paramXmlSerializer.attribute(null, "rel", paramString1);
    paramXmlSerializer.attribute(null, "href", paramString2);
    if (!StringUtils.isEmpty(paramString3))
      paramXmlSerializer.attribute(null, "type", paramString3);
    paramXmlSerializer.endTag(null, "link");
  }

  private static void serializePublicationDate(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag(null, "published");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, "published");
  }

  private static void serializeSummary(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag(null, "summary");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, "summary");
  }

  private static void serializeTitle(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag(null, "title");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, "title");
  }

  private static void serializeUpdateDate(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag(null, "updated");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, "updated");
  }

  protected void declareExtraEntryNamespaces(XmlSerializer paramXmlSerializer)
    throws IOException
  {
  }

  public String getContentType()
  {
    return "application/atom+xml";
  }

  protected Entry getEntry()
  {
    return this.entry;
  }

  public void serialize(OutputStream paramOutputStream, int paramInt)
    throws IOException, ParseException
  {
    try
    {
      XmlSerializer localXmlSerializer = this.factory.createSerializer();
      localXmlSerializer.setOutput(paramOutputStream, "UTF-8");
      localXmlSerializer.startDocument("UTF-8", new Boolean(false));
      declareEntryNamespaces(localXmlSerializer);
      localXmlSerializer.startTag("http://www.w3.org/2005/Atom", "entry");
      serializeEntryContents(localXmlSerializer, paramInt);
      localXmlSerializer.endTag("http://www.w3.org/2005/Atom", "entry");
      localXmlSerializer.endDocument();
      localXmlSerializer.flush();
      return;
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException("Unable to create XmlSerializer.", localXmlPullParserException);
    }
  }

  protected void serializeExtraEntryContents(XmlSerializer paramXmlSerializer, int paramInt)
    throws ParseException, IOException
  {
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.serializer.xml.XmlEntryGDataSerializer
 * JD-Core Version:    0.6.2
 */