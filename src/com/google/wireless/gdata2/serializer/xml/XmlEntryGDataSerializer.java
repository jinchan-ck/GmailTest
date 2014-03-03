package com.google.wireless.gdata2.serializer.xml;

import com.google.wireless.gdata2.data.Entry;
import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.data.batch.BatchUtils;
import com.google.wireless.gdata2.parser.ParseException;
import com.google.wireless.gdata2.parser.xml.XmlNametable;
import com.google.wireless.gdata2.parser.xml.XmlParserFactory;
import com.google.wireless.gdata2.serializer.GDataSerializer;
import java.io.IOException;
import java.io.OutputStream;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class XmlEntryGDataSerializer
  implements GDataSerializer
{
  private final Entry entry;
  private final XmlParserFactory factory;
  private final boolean supportsPartial;

  public XmlEntryGDataSerializer(XmlParserFactory paramXmlParserFactory, Entry paramEntry)
  {
    this.factory = paramXmlParserFactory;
    this.entry = paramEntry;
    if (!StringUtils.isEmptyOrWhitespace(this.entry.getFields()));
    for (boolean bool = true; ; bool = false)
    {
      this.supportsPartial = bool;
      return;
    }
  }

  private void declareEntryNamespaces(XmlSerializer paramXmlSerializer)
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
    paramXmlSerializer.startTag(null, XmlNametable.AUTHOR);
    paramXmlSerializer.startTag(null, XmlNametable.NAME);
    paramXmlSerializer.text(paramString1);
    paramXmlSerializer.endTag(null, XmlNametable.NAME);
    paramXmlSerializer.startTag(null, XmlNametable.EMAIL);
    paramXmlSerializer.text(paramString2);
    paramXmlSerializer.endTag(null, XmlNametable.EMAIL);
    paramXmlSerializer.endTag(null, XmlNametable.AUTHOR);
  }

  private void serializeBatchInfo(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    if (!StringUtils.isEmpty(this.entry.getETag()))
      paramXmlSerializer.attribute("http://schemas.google.com/g/2005", XmlNametable.ETAG, this.entry.getETag());
    if (!StringUtils.isEmpty(BatchUtils.getBatchOperation(this.entry)))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/gdata/batch", XmlNametable.OPERATION);
      paramXmlSerializer.attribute(null, XmlNametable.TYPE, BatchUtils.getBatchOperation(this.entry));
      paramXmlSerializer.endTag("http://schemas.google.com/gdata/batch", XmlNametable.OPERATION);
    }
    if (!StringUtils.isEmpty(BatchUtils.getBatchId(this.entry)))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/gdata/batch", XmlNametable.ID);
      paramXmlSerializer.text(BatchUtils.getBatchId(this.entry));
      paramXmlSerializer.endTag("http://schemas.google.com/gdata/batch", XmlNametable.ID);
    }
  }

  private static void serializeCategory(XmlSerializer paramXmlSerializer, String paramString1, String paramString2)
    throws IOException
  {
    if ((StringUtils.isEmpty(paramString1)) && (StringUtils.isEmpty(paramString2)))
      return;
    paramXmlSerializer.startTag(null, XmlNametable.CATEGORY);
    if (!StringUtils.isEmpty(paramString1))
      paramXmlSerializer.attribute(null, XmlNametable.TERM, paramString1);
    if (!StringUtils.isEmpty(paramString2))
      paramXmlSerializer.attribute(null, XmlNametable.SCHEME, paramString2);
    paramXmlSerializer.endTag(null, XmlNametable.CATEGORY);
  }

  private static void serializeContent(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (paramString == null)
      return;
    paramXmlSerializer.startTag(null, XmlNametable.CONTENT);
    paramXmlSerializer.attribute(null, XmlNametable.TYPE, XmlNametable.TEXT);
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, XmlNametable.CONTENT);
  }

  private void serializeEntryContents(XmlSerializer paramXmlSerializer, int paramInt)
    throws ParseException, IOException
  {
    if (paramInt == 3)
      serializeBatchInfo(paramXmlSerializer);
    if (paramInt != 1)
      serializeId(paramXmlSerializer, this.entry.getId());
    serializeTitle(paramXmlSerializer, this.entry.getTitle());
    if (paramInt != 1)
    {
      serializeLink(paramXmlSerializer, XmlNametable.EDIT_REL, this.entry.getEditUri(), null, null);
      serializeLink(paramXmlSerializer, XmlNametable.ALTERNATE_REL, this.entry.getHtmlUri(), XmlNametable.TEXTHTML, null);
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
    paramXmlSerializer.startTag(null, XmlNametable.ID);
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, XmlNametable.ID);
  }

  protected static void serializeLink(XmlSerializer paramXmlSerializer, String paramString1, String paramString2, String paramString3, String paramString4)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString2))
      return;
    paramXmlSerializer.startTag(null, XmlNametable.LINK);
    paramXmlSerializer.attribute(null, XmlNametable.REL, paramString1);
    paramXmlSerializer.attribute(null, XmlNametable.HREF, paramString2);
    if (!StringUtils.isEmpty(paramString3))
      paramXmlSerializer.attribute(null, XmlNametable.TYPE, paramString3);
    if (!StringUtils.isEmpty(paramString4))
      paramXmlSerializer.attribute(null, XmlNametable.ETAG, paramString4);
    paramXmlSerializer.endTag(null, XmlNametable.LINK);
  }

  private static void serializePublicationDate(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag(null, XmlNametable.PUBLISHED);
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, XmlNametable.PUBLISHED);
  }

  private static void serializeSummary(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag(null, XmlNametable.SUMMARY);
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, XmlNametable.SUMMARY);
  }

  private static void serializeTitle(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag(null, XmlNametable.TITLE);
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, XmlNametable.TITLE);
  }

  private static void serializeUpdateDate(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag(null, XmlNametable.UPDATED);
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag(null, XmlNametable.UPDATED);
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

  public boolean getSupportsPartial()
  {
    return this.supportsPartial;
  }

  public void serialize(OutputStream paramOutputStream, int paramInt)
    throws IOException, ParseException
  {
    try
    {
      XmlSerializer localXmlSerializer = this.factory.createSerializer();
      localXmlSerializer.setOutput(paramOutputStream, XmlNametable.UTF8);
      if (paramInt != 3)
      {
        localXmlSerializer.startDocument(XmlNametable.UTF8, Boolean.FALSE);
        declareEntryNamespaces(localXmlSerializer);
      }
      String str = this.entry.getFields();
      if (getSupportsPartial())
      {
        localXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.PARTIAL);
        localXmlSerializer.attribute(null, XmlNametable.FIELDS, str);
      }
      localXmlSerializer.startTag("http://www.w3.org/2005/Atom", XmlNametable.ENTRY);
      serializeEntryContents(localXmlSerializer, paramInt);
      localXmlSerializer.endTag("http://www.w3.org/2005/Atom", XmlNametable.ENTRY);
      if (getSupportsPartial())
        localXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.PARTIAL);
      if (paramInt != 3)
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
 * Qualified Name:     com.google.wireless.gdata2.serializer.xml.XmlEntryGDataSerializer
 * JD-Core Version:    0.6.2
 */