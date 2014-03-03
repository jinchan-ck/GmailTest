package com.google.wireless.gdata.contacts.serializer.xml;

import com.google.wireless.gdata.contacts.data.ContactEntry;
import com.google.wireless.gdata.contacts.data.ContactsElement;
import com.google.wireless.gdata.contacts.data.EmailAddress;
import com.google.wireless.gdata.contacts.data.GroupMembershipInfo;
import com.google.wireless.gdata.contacts.data.ImAddress;
import com.google.wireless.gdata.contacts.data.Organization;
import com.google.wireless.gdata.contacts.data.PhoneNumber;
import com.google.wireless.gdata.contacts.data.PostalAddress;
import com.google.wireless.gdata.contacts.parser.xml.XmlContactsGDataParser;
import com.google.wireless.gdata.data.ExtendedProperty;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlParserFactory;
import com.google.wireless.gdata.serializer.xml.XmlEntryGDataSerializer;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.xmlpull.v1.XmlSerializer;

public class XmlContactEntryGDataSerializer extends XmlEntryGDataSerializer
{
  public XmlContactEntryGDataSerializer(XmlParserFactory paramXmlParserFactory, ContactEntry paramContactEntry)
  {
    super(paramXmlParserFactory, paramContactEntry);
  }

  private static void serialize(XmlSerializer paramXmlSerializer, EmailAddress paramEmailAddress)
    throws IOException, ParseException
  {
    if (StringUtils.isEmptyOrWhitespace(paramEmailAddress.getAddress()))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "email");
    serializeContactsElement(paramXmlSerializer, paramEmailAddress, XmlContactsGDataParser.TYPE_TO_REL_EMAIL);
    paramXmlSerializer.attribute(null, "address", paramEmailAddress.getAddress());
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "email");
  }

  private static void serialize(XmlSerializer paramXmlSerializer, GroupMembershipInfo paramGroupMembershipInfo)
    throws IOException, ParseException
  {
    String str1 = paramGroupMembershipInfo.getGroup();
    boolean bool = paramGroupMembershipInfo.isDeleted();
    if (StringUtils.isEmptyOrWhitespace(str1))
      throw new ParseException("the group must not be empty");
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", "groupMembershipInfo");
    paramXmlSerializer.attribute(null, "href", str1);
    if (bool);
    for (String str2 = "true"; ; str2 = "false")
    {
      paramXmlSerializer.attribute(null, "deleted", str2);
      paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", "groupMembershipInfo");
      return;
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, ImAddress paramImAddress)
    throws IOException, ParseException
  {
    if (StringUtils.isEmptyOrWhitespace(paramImAddress.getAddress()))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "im");
    serializeContactsElement(paramXmlSerializer, paramImAddress, XmlContactsGDataParser.TYPE_TO_REL_IM);
    paramXmlSerializer.attribute(null, "address", paramImAddress.getAddress());
    switch (paramImAddress.getProtocolPredefined())
    {
    default:
      paramXmlSerializer.attribute(null, "protocol", (String)XmlContactsGDataParser.IM_PROTOCOL_TYPE_TO_STRING_MAP.get(new Byte(paramImAddress.getProtocolPredefined())));
    case 10:
    case 1:
    }
    while (true)
    {
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "im");
      return;
      String str = paramImAddress.getProtocolCustom();
      if (str == null)
        throw new IllegalArgumentException("the protocol is custom, but the custom string is null");
      paramXmlSerializer.attribute(null, "protocol", str);
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, Organization paramOrganization)
    throws IOException, ParseException
  {
    String str1 = paramOrganization.getName();
    String str2 = paramOrganization.getTitle();
    if ((StringUtils.isEmptyOrWhitespace(str1)) && (StringUtils.isEmptyOrWhitespace(str2)))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "organization");
    serializeContactsElement(paramXmlSerializer, paramOrganization, XmlContactsGDataParser.TYPE_TO_REL_ORGANIZATION);
    if (!StringUtils.isEmpty(str1))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "orgName");
      paramXmlSerializer.text(str1);
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "orgName");
    }
    if (!StringUtils.isEmpty(str2))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "orgTitle");
      paramXmlSerializer.text(str2);
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "orgTitle");
    }
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "organization");
  }

  private static void serialize(XmlSerializer paramXmlSerializer, PhoneNumber paramPhoneNumber)
    throws IOException, ParseException
  {
    if (StringUtils.isEmptyOrWhitespace(paramPhoneNumber.getPhoneNumber()))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "phoneNumber");
    serializeContactsElement(paramXmlSerializer, paramPhoneNumber, XmlContactsGDataParser.TYPE_TO_REL_PHONE);
    paramXmlSerializer.text(paramPhoneNumber.getPhoneNumber());
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "phoneNumber");
  }

  private static void serialize(XmlSerializer paramXmlSerializer, PostalAddress paramPostalAddress)
    throws IOException, ParseException
  {
    if (StringUtils.isEmptyOrWhitespace(paramPostalAddress.getValue()))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "postalAddress");
    serializeContactsElement(paramXmlSerializer, paramPostalAddress, XmlContactsGDataParser.TYPE_TO_REL_POSTAL);
    String str = paramPostalAddress.getValue();
    if (str != null)
      paramXmlSerializer.text(str);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "postalAddress");
  }

  private static void serialize(XmlSerializer paramXmlSerializer, ExtendedProperty paramExtendedProperty)
    throws IOException, ParseException
  {
    String str1 = paramExtendedProperty.getName();
    String str2 = paramExtendedProperty.getValue();
    String str3 = paramExtendedProperty.getXmlBlob();
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "extendedProperty");
    if (!StringUtils.isEmpty(str1))
      paramXmlSerializer.attribute(null, "name", str1);
    if (!StringUtils.isEmpty(str2))
      paramXmlSerializer.attribute(null, "value", str2);
    if (!StringUtils.isEmpty(str3))
      serializeBlob(paramXmlSerializer, str3);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "extendedProperty");
  }

  private static void serializeBlob(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException, ParseException
  {
    paramXmlSerializer.text(paramString);
  }

  private static void serializeContactsElement(XmlSerializer paramXmlSerializer, ContactsElement paramContactsElement, Hashtable paramHashtable)
    throws IOException, ParseException
  {
    String str = paramContactsElement.getLabel();
    if (paramContactsElement.getType() != -1);
    for (int i = 1; ((str == null) && (i == 0)) || ((str != null) && (i != 0)); i = 0)
      throw new ParseException("exactly one of label or rel must be set");
    if (str != null)
      paramXmlSerializer.attribute(null, "label", str);
    if (i != 0)
      paramXmlSerializer.attribute(null, "rel", (String)paramHashtable.get(new Byte(paramContactsElement.getType())));
    if (paramContactsElement.isPrimary())
      paramXmlSerializer.attribute(null, "primary", "true");
  }

  private static void serializeYomiName(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", "yomiName");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", "yomiName");
  }

  protected void declareExtraEntryNamespaces(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    super.declareExtraEntryNamespaces(paramXmlSerializer);
    paramXmlSerializer.setPrefix("gContact", "http://schemas.google.com/contact/2008");
  }

  protected ContactEntry getContactEntry()
  {
    return (ContactEntry)getEntry();
  }

  protected void serializeExtraEntryContents(XmlSerializer paramXmlSerializer, int paramInt)
    throws ParseException, IOException
  {
    ContactEntry localContactEntry = getContactEntry();
    localContactEntry.validate();
    serializeLink(paramXmlSerializer, "http://schemas.google.com/contacts/2008/rel#edit-photo", localContactEntry.getLinkEditPhotoHref(), localContactEntry.getLinkEditPhotoType());
    serializeLink(paramXmlSerializer, "http://schemas.google.com/contacts/2008/rel#photo", localContactEntry.getLinkPhotoHref(), localContactEntry.getLinkPhotoType());
    Enumeration localEnumeration1 = localContactEntry.getEmailAddresses().elements();
    while (localEnumeration1.hasMoreElements())
      serialize(paramXmlSerializer, (EmailAddress)localEnumeration1.nextElement());
    Enumeration localEnumeration2 = localContactEntry.getImAddresses().elements();
    while (localEnumeration2.hasMoreElements())
      serialize(paramXmlSerializer, (ImAddress)localEnumeration2.nextElement());
    Enumeration localEnumeration3 = localContactEntry.getPhoneNumbers().elements();
    while (localEnumeration3.hasMoreElements())
      serialize(paramXmlSerializer, (PhoneNumber)localEnumeration3.nextElement());
    Enumeration localEnumeration4 = localContactEntry.getPostalAddresses().elements();
    while (localEnumeration4.hasMoreElements())
      serialize(paramXmlSerializer, (PostalAddress)localEnumeration4.nextElement());
    Enumeration localEnumeration5 = localContactEntry.getOrganizations().elements();
    while (localEnumeration5.hasMoreElements())
      serialize(paramXmlSerializer, (Organization)localEnumeration5.nextElement());
    Enumeration localEnumeration6 = localContactEntry.getExtendedProperties().elements();
    while (localEnumeration6.hasMoreElements())
      serialize(paramXmlSerializer, (ExtendedProperty)localEnumeration6.nextElement());
    Enumeration localEnumeration7 = localContactEntry.getGroups().elements();
    while (localEnumeration7.hasMoreElements())
      serialize(paramXmlSerializer, (GroupMembershipInfo)localEnumeration7.nextElement());
    serializeYomiName(paramXmlSerializer, localContactEntry.getYomiName());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.serializer.xml.XmlContactEntryGDataSerializer
 * JD-Core Version:    0.6.2
 */