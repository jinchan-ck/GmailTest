package com.google.wireless.gdata2.contacts.serializer.xml;

import com.google.wireless.gdata2.contacts.data.GroupEntry;
import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.parser.ParseException;
import com.google.wireless.gdata2.parser.xml.XmlParserFactory;
import com.google.wireless.gdata2.serializer.xml.XmlEntryGDataSerializer;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public class XmlGroupEntryGDataSerializer extends XmlEntryGDataSerializer
{
  public XmlGroupEntryGDataSerializer(XmlParserFactory paramXmlParserFactory, GroupEntry paramGroupEntry)
  {
    super(paramXmlParserFactory, paramGroupEntry);
  }

  private void serializeSystemGroup(GroupEntry paramGroupEntry, XmlSerializer paramXmlSerializer)
    throws IOException
  {
    String str = paramGroupEntry.getSystemGroup();
    if (!StringUtils.isEmpty(str))
    {
      paramXmlSerializer.startTag(null, "systemGroup");
      paramXmlSerializer.attribute(null, "id", str);
      paramXmlSerializer.endTag(null, "systemGroup");
    }
  }

  protected void declareExtraEntryNamespaces(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    super.declareExtraEntryNamespaces(paramXmlSerializer);
    paramXmlSerializer.setPrefix("gContact", "http://schemas.google.com/contact/2008");
  }

  protected GroupEntry getGroupEntry()
  {
    return (GroupEntry)getEntry();
  }

  protected void serializeExtraEntryContents(XmlSerializer paramXmlSerializer, int paramInt)
    throws ParseException, IOException
  {
    GroupEntry localGroupEntry = getGroupEntry();
    localGroupEntry.validate();
    serializeSystemGroup(localGroupEntry, paramXmlSerializer);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.serializer.xml.XmlGroupEntryGDataSerializer
 * JD-Core Version:    0.6.2
 */