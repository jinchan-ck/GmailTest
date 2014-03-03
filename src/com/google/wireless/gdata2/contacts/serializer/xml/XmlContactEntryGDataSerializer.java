package com.google.wireless.gdata2.contacts.serializer.xml;

import com.google.wireless.gdata2.contacts.data.CalendarLink;
import com.google.wireless.gdata2.contacts.data.ContactEntry;
import com.google.wireless.gdata2.contacts.data.ContactsElement;
import com.google.wireless.gdata2.contacts.data.EmailAddress;
import com.google.wireless.gdata2.contacts.data.Event;
import com.google.wireless.gdata2.contacts.data.ExternalId;
import com.google.wireless.gdata2.contacts.data.GroupMembershipInfo;
import com.google.wireless.gdata2.contacts.data.ImAddress;
import com.google.wireless.gdata2.contacts.data.Jot;
import com.google.wireless.gdata2.contacts.data.Language;
import com.google.wireless.gdata2.contacts.data.Name;
import com.google.wireless.gdata2.contacts.data.Organization;
import com.google.wireless.gdata2.contacts.data.PhoneNumber;
import com.google.wireless.gdata2.contacts.data.Relation;
import com.google.wireless.gdata2.contacts.data.StructuredPostalAddress;
import com.google.wireless.gdata2.contacts.data.TypedElement;
import com.google.wireless.gdata2.contacts.data.UserDefinedField;
import com.google.wireless.gdata2.contacts.data.WebSite;
import com.google.wireless.gdata2.contacts.parser.xml.XmlContactsGDataParser;
import com.google.wireless.gdata2.contacts.parser.xml.XmlNametable;
import com.google.wireless.gdata2.data.ExtendedProperty;
import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.parser.ParseException;
import com.google.wireless.gdata2.parser.xml.XmlParserFactory;
import com.google.wireless.gdata2.serializer.xml.XmlEntryGDataSerializer;
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

  private static void serialize(XmlSerializer paramXmlSerializer, CalendarLink paramCalendarLink)
    throws IOException, ParseException
  {
    String str = paramCalendarLink.getHRef();
    if (shouldSerialize(paramCalendarLink, str))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_CALENDARLINK);
      serializeContactsElement(paramXmlSerializer, paramCalendarLink, XmlContactsGDataParser.TYPE_TO_REL_CALENDARLINK);
      if (!StringUtils.isEmpty(str))
        paramXmlSerializer.attribute(null, XmlNametable.HREF, str);
      paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_CALENDARLINK);
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, EmailAddress paramEmailAddress)
    throws IOException, ParseException
  {
    if (StringUtils.isEmptyOrWhitespace(paramEmailAddress.getAddress()))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.GD_EMAIL);
    serializeContactsElement(paramXmlSerializer, paramEmailAddress, XmlContactsGDataParser.TYPE_TO_REL_EMAIL);
    paramXmlSerializer.attribute(null, XmlNametable.GD_ADDRESS, paramEmailAddress.getAddress());
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.GD_EMAIL);
  }

  private static void serialize(XmlSerializer paramXmlSerializer, Event paramEvent)
    throws IOException, ParseException
  {
    String str = paramEvent.getStartDate();
    if (shouldSerialize(paramEvent, str))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_EVENT);
      serializeTypedElement(paramXmlSerializer, paramEvent, XmlContactsGDataParser.TYPE_TO_REL_EVENT);
      if (!StringUtils.isEmpty(str))
      {
        paramXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.GD_WHEN);
        paramXmlSerializer.attribute(null, XmlNametable.STARTTIME, str);
        paramXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.GD_WHEN);
      }
      paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_EVENT);
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, ExternalId paramExternalId)
    throws IOException, ParseException
  {
    String str = paramExternalId.getValue();
    if (shouldSerialize(paramExternalId, str))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_EXTERNALID);
      serializeTypedElement(paramXmlSerializer, paramExternalId, XmlContactsGDataParser.TYPE_TO_REL_EXTERNALID);
      if (!StringUtils.isEmpty(str))
        paramXmlSerializer.attribute(null, XmlNametable.VALUE, str);
      paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_EXTERNALID);
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, GroupMembershipInfo paramGroupMembershipInfo)
    throws IOException, ParseException
  {
    String str1 = paramGroupMembershipInfo.getGroup();
    boolean bool = paramGroupMembershipInfo.isDeleted();
    if (StringUtils.isEmptyOrWhitespace(str1))
      throw new ParseException("the group must not be empty");
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_GMI);
    paramXmlSerializer.attribute(null, XmlNametable.HREF, str1);
    String str2 = XmlNametable.GD_DELETED;
    if (bool);
    for (String str3 = "true"; ; str3 = "false")
    {
      paramXmlSerializer.attribute(null, str2, str3);
      paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_GMI);
      return;
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, ImAddress paramImAddress)
    throws IOException, ParseException
  {
    if (StringUtils.isEmptyOrWhitespace(paramImAddress.getAddress()))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.GD_IM);
    serializeContactsElement(paramXmlSerializer, paramImAddress, XmlContactsGDataParser.TYPE_TO_REL_IM);
    paramXmlSerializer.attribute(null, XmlNametable.GD_ADDRESS, paramImAddress.getAddress());
    switch (paramImAddress.getProtocolPredefined())
    {
    default:
      String str2 = (String)XmlContactsGDataParser.IM_PROTOCOL_TYPE_TO_STRING_MAP.get(new Byte(paramImAddress.getProtocolPredefined()));
      paramXmlSerializer.attribute(null, XmlNametable.GD_PROTOCOL, str2);
    case 11:
    case 1:
    }
    while (true)
    {
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.GD_IM);
      return;
      String str1 = paramImAddress.getProtocolCustom();
      if (str1 == null)
        throw new IllegalArgumentException("the protocol is custom, but the custom string is null");
      paramXmlSerializer.attribute(null, XmlNametable.GD_PROTOCOL, str1);
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, Jot paramJot)
    throws IOException
  {
    String str = paramJot.getLabel();
    if (!StringUtils.isEmptyOrWhitespace(str))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_JOT);
      serializeRelation(paramXmlSerializer, paramJot.getType(), XmlContactsGDataParser.TYPE_TO_REL_JOT);
      paramXmlSerializer.text(str);
      paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_JOT);
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, Language paramLanguage)
    throws IOException, ParseException
  {
    paramLanguage.validate();
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_LANGUAGE);
    String str = paramLanguage.getCode();
    if (!StringUtils.isEmptyOrWhitespace(str))
      paramXmlSerializer.attribute(null, XmlNametable.CODE, str);
    while (true)
    {
      paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_LANGUAGE);
      return;
      paramXmlSerializer.attribute(null, XmlNametable.LABEL, paramLanguage.getLabel());
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, Organization paramOrganization)
    throws IOException, ParseException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.GD_ORGANIZATION);
    serializeContactsElement(paramXmlSerializer, paramOrganization, XmlContactsGDataParser.TYPE_TO_REL_ORGANIZATION);
    serializeGDSubelement(paramXmlSerializer, paramOrganization.getName(), XmlNametable.GD_ORG_NAME);
    serializeGDSubelement(paramXmlSerializer, paramOrganization.getTitle(), XmlNametable.GD_ORG_TITLE);
    serializeGDSubelement(paramXmlSerializer, paramOrganization.getOrgDepartment(), XmlNametable.GD_ORG_DEPARTMENT);
    serializeGDSubelement(paramXmlSerializer, paramOrganization.getOrgJobDescription(), XmlNametable.GD_ORG_JOBDESC);
    serializeGDSubelement(paramXmlSerializer, paramOrganization.getOrgSymbol(), XmlNametable.GD_ORG_SYMBOL);
    String str = paramOrganization.getWhere();
    if (!StringUtils.isEmpty(str))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.GD_WHERE);
      paramXmlSerializer.attribute(null, XmlNametable.VALUESTRING, str);
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.GD_WHERE);
    }
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.GD_ORGANIZATION);
  }

  private static void serialize(XmlSerializer paramXmlSerializer, PhoneNumber paramPhoneNumber)
    throws IOException, ParseException
  {
    if (StringUtils.isEmptyOrWhitespace(paramPhoneNumber.getPhoneNumber()))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.GD_PHONENUMBER);
    serializeContactsElement(paramXmlSerializer, paramPhoneNumber, XmlContactsGDataParser.TYPE_TO_REL_PHONE);
    paramXmlSerializer.text(paramPhoneNumber.getPhoneNumber());
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.GD_PHONENUMBER);
  }

  private static void serialize(XmlSerializer paramXmlSerializer, Relation paramRelation)
    throws IOException, ParseException
  {
    String str = paramRelation.getText();
    if (shouldSerialize(paramRelation, str))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_RELATION);
      serializeTypedElement(paramXmlSerializer, paramRelation, XmlContactsGDataParser.TYPE_TO_REL_RELATION);
      paramXmlSerializer.text(str);
      paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_RELATION);
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, StructuredPostalAddress paramStructuredPostalAddress)
    throws IOException, ParseException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.GD_SPA);
    serializeContactsElement(paramXmlSerializer, paramStructuredPostalAddress, XmlContactsGDataParser.TYPE_TO_REL_POSTAL);
    serializeGDSubelement(paramXmlSerializer, paramStructuredPostalAddress.getStreet(), XmlNametable.GD_SPA_STREET);
    serializeGDSubelement(paramXmlSerializer, paramStructuredPostalAddress.getPobox(), XmlNametable.GD_SPA_POBOX);
    serializeGDSubelement(paramXmlSerializer, paramStructuredPostalAddress.getNeighborhood(), XmlNametable.GD_SPA_NEIGHBORHOOD);
    serializeGDSubelement(paramXmlSerializer, paramStructuredPostalAddress.getCity(), XmlNametable.GD_SPA_CITY);
    serializeGDSubelement(paramXmlSerializer, paramStructuredPostalAddress.getRegion(), XmlNametable.GD_SPA_REGION);
    serializeGDSubelement(paramXmlSerializer, paramStructuredPostalAddress.getPostcode(), XmlNametable.GD_SPA_POSTCODE);
    serializeGDSubelement(paramXmlSerializer, paramStructuredPostalAddress.getCountry(), XmlNametable.GD_SPA_COUNTRY);
    serializeGDSubelement(paramXmlSerializer, paramStructuredPostalAddress.getFormattedAddress(), XmlNametable.GD_SPA_FORMATTEDADDRESS);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.GD_SPA);
  }

  private static void serialize(XmlSerializer paramXmlSerializer, UserDefinedField paramUserDefinedField)
    throws IOException, ParseException
  {
    paramUserDefinedField.validate();
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_UDF);
    if (!StringUtils.isEmpty(paramUserDefinedField.getKey()))
      paramXmlSerializer.attribute(null, XmlNametable.KEY, paramUserDefinedField.getKey());
    if (!StringUtils.isEmpty(paramUserDefinedField.getValue()))
      paramXmlSerializer.attribute(null, XmlNametable.VALUE, paramUserDefinedField.getValue());
    paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_UDF);
  }

  private static void serialize(XmlSerializer paramXmlSerializer, WebSite paramWebSite)
    throws IOException, ParseException
  {
    String str = paramWebSite.getHRef();
    if (shouldSerialize(paramWebSite, str))
    {
      paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_WEBSITE);
      serializeContactsElement(paramXmlSerializer, paramWebSite, XmlContactsGDataParser.TYPE_TO_REL_WEBSITE);
      if (!StringUtils.isEmpty(str))
        paramXmlSerializer.attribute(null, XmlNametable.HREF, str);
      paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_WEBSITE);
    }
  }

  private static void serialize(XmlSerializer paramXmlSerializer, ExtendedProperty paramExtendedProperty)
    throws IOException
  {
    String str1 = paramExtendedProperty.getName();
    String str2 = paramExtendedProperty.getValue();
    String str3 = paramExtendedProperty.getXmlBlob();
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.GD_EXTENDEDPROPERTY);
    if (!StringUtils.isEmpty(str1))
      paramXmlSerializer.attribute(null, XmlNametable.GD_NAME, str1);
    if (!StringUtils.isEmpty(str2))
      paramXmlSerializer.attribute(null, XmlNametable.VALUE, str2);
    if (!StringUtils.isEmpty(str3))
      serializeBlob(paramXmlSerializer, str3);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.GD_EXTENDEDPROPERTY);
  }

  private static void serializeBirthday(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmptyOrWhitespace(paramString))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_BIRTHDAY);
    paramXmlSerializer.attribute(null, XmlNametable.GD_WHEN, paramString);
    paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_BIRTHDAY);
  }

  private static void serializeBlob(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    paramXmlSerializer.text(paramString);
  }

  private static void serializeContactsElement(XmlSerializer paramXmlSerializer, ContactsElement paramContactsElement, Hashtable paramHashtable)
    throws IOException, ParseException
  {
    serializeTypedElement(paramXmlSerializer, paramContactsElement, paramHashtable);
    if (paramContactsElement.isPrimary())
      paramXmlSerializer.attribute(null, XmlNametable.PRIMARY, "true");
  }

  private static void serializeElement(XmlSerializer paramXmlSerializer, byte paramByte, String paramString, Hashtable paramHashtable)
    throws IOException
  {
    if (paramByte == -1)
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", paramString);
    serializeRelation(paramXmlSerializer, paramByte, paramHashtable);
    paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", paramString);
  }

  private static void serializeElement(XmlSerializer paramXmlSerializer, String paramString1, String paramString2)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString1))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", paramString2);
    paramXmlSerializer.text(paramString1);
    paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", paramString2);
  }

  private static void serializeGDSubelement(XmlSerializer paramXmlSerializer, String paramString1, String paramString2)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString1))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", paramString2);
    paramXmlSerializer.text(paramString1);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", paramString2);
  }

  private static void serializeGenderElement(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_GENDER);
    paramXmlSerializer.attribute(null, XmlNametable.VALUE, paramString);
    paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_GENDER);
  }

  private static void serializeHobby(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmptyOrWhitespace(paramString))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/contact/2008", XmlNametable.GC_HOBBY);
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag("http://schemas.google.com/contact/2008", XmlNametable.GC_HOBBY);
  }

  private static void serializeName(XmlSerializer paramXmlSerializer, Name paramName)
    throws IOException
  {
    if (paramName == null)
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", XmlNametable.GD_NAME);
    serializeNameSubelement(paramXmlSerializer, paramName.getGivenName(), paramName.getGivenNameYomi(), XmlNametable.GD_NAME_GIVENNAME);
    serializeNameSubelement(paramXmlSerializer, paramName.getAdditionalName(), paramName.getAdditionalNameYomi(), XmlNametable.GD_NAME_ADDITIONALNAME);
    serializeNameSubelement(paramXmlSerializer, paramName.getFamilyName(), paramName.getFamilyNameYomi(), XmlNametable.GD_NAME_FAMILYNAME);
    serializeNameSubelement(paramXmlSerializer, paramName.getNamePrefix(), null, XmlNametable.GD_NAME_PREFIX);
    serializeNameSubelement(paramXmlSerializer, paramName.getNameSuffix(), null, XmlNametable.GD_NAME_SUFFIX);
    serializeNameSubelement(paramXmlSerializer, paramName.getFullName(), null, XmlNametable.GD_NAME_FULLNAME);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", XmlNametable.GD_NAME);
  }

  private static void serializeNameSubelement(XmlSerializer paramXmlSerializer, String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString1))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", paramString3);
    if (!StringUtils.isEmpty(paramString2))
      paramXmlSerializer.attribute(null, XmlNametable.GD_NAME_YOMI, paramString2);
    paramXmlSerializer.text(paramString1);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", paramString3);
  }

  private static void serializeRelation(XmlSerializer paramXmlSerializer, byte paramByte, Hashtable paramHashtable)
    throws IOException
  {
    paramXmlSerializer.attribute(null, XmlNametable.REL, (String)paramHashtable.get(new Byte(paramByte)));
  }

  private static void serializeTypedElement(XmlSerializer paramXmlSerializer, TypedElement paramTypedElement, Hashtable paramHashtable)
    throws IOException, ParseException
  {
    String str = paramTypedElement.getLabel();
    byte b = paramTypedElement.getType();
    if (b != -1);
    for (int i = 1; ; i = 0)
    {
      paramTypedElement.validate();
      if (str != null)
        paramXmlSerializer.attribute(null, XmlNametable.LABEL, str);
      if (i != 0)
        serializeRelation(paramXmlSerializer, b, paramHashtable);
      return;
    }
  }

  private static boolean shouldSerialize(TypedElement paramTypedElement, String paramString)
  {
    if (paramTypedElement.getType() != -1)
      return true;
    if (!StringUtils.isEmptyOrWhitespace(paramTypedElement.getLabel()))
      return true;
    return !StringUtils.isEmptyOrWhitespace(paramString);
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
    serializeLink(paramXmlSerializer, "http://schemas.google.com/contacts/2008/rel#photo", localContactEntry.getLinkPhotoHref(), localContactEntry.getLinkPhotoType(), localContactEntry.getLinkPhotoETag());
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
      serialize(paramXmlSerializer, (StructuredPostalAddress)localEnumeration4.nextElement());
    Enumeration localEnumeration5 = localContactEntry.getOrganizations().elements();
    while (localEnumeration5.hasMoreElements())
      serialize(paramXmlSerializer, (Organization)localEnumeration5.nextElement());
    Enumeration localEnumeration6 = localContactEntry.getExtendedProperties().elements();
    while (localEnumeration6.hasMoreElements())
      serialize(paramXmlSerializer, (ExtendedProperty)localEnumeration6.nextElement());
    Enumeration localEnumeration7 = localContactEntry.getGroups().elements();
    while (localEnumeration7.hasMoreElements())
      serialize(paramXmlSerializer, (GroupMembershipInfo)localEnumeration7.nextElement());
    Enumeration localEnumeration8 = localContactEntry.getCalendarLinks().elements();
    while (localEnumeration8.hasMoreElements())
      serialize(paramXmlSerializer, (CalendarLink)localEnumeration8.nextElement());
    Enumeration localEnumeration9 = localContactEntry.getEvents().elements();
    while (localEnumeration9.hasMoreElements())
      serialize(paramXmlSerializer, (Event)localEnumeration9.nextElement());
    Enumeration localEnumeration10 = localContactEntry.getWebSites().elements();
    while (localEnumeration10.hasMoreElements())
      serialize(paramXmlSerializer, (WebSite)localEnumeration10.nextElement());
    Enumeration localEnumeration11 = localContactEntry.getExternalIds().elements();
    while (localEnumeration11.hasMoreElements())
      serialize(paramXmlSerializer, (ExternalId)localEnumeration11.nextElement());
    Enumeration localEnumeration12 = localContactEntry.getHobbies().elements();
    while (localEnumeration12.hasMoreElements())
      serializeHobby(paramXmlSerializer, (String)localEnumeration12.nextElement());
    Enumeration localEnumeration13 = localContactEntry.getJots().elements();
    while (localEnumeration13.hasMoreElements())
      serialize(paramXmlSerializer, (Jot)localEnumeration13.nextElement());
    Enumeration localEnumeration14 = localContactEntry.getLanguages().elements();
    while (localEnumeration14.hasMoreElements())
      serialize(paramXmlSerializer, (Language)localEnumeration14.nextElement());
    Enumeration localEnumeration15 = localContactEntry.getRelations().elements();
    while (localEnumeration15.hasMoreElements())
      serialize(paramXmlSerializer, (Relation)localEnumeration15.nextElement());
    Enumeration localEnumeration16 = localContactEntry.getUserDefinedFields().elements();
    while (localEnumeration16.hasMoreElements())
      serialize(paramXmlSerializer, (UserDefinedField)localEnumeration16.nextElement());
    serializeBirthday(paramXmlSerializer, localContactEntry.getBirthday());
    serializeElement(paramXmlSerializer, localContactEntry.getDirectoryServer(), XmlNametable.GC_DIRECTORYSERVER);
    serializeGenderElement(paramXmlSerializer, localContactEntry.getGender());
    serializeElement(paramXmlSerializer, localContactEntry.getInitials(), XmlNametable.GC_INITIALS);
    serializeElement(paramXmlSerializer, localContactEntry.getMaidenName(), XmlNametable.GC_MAIDENNAME);
    serializeElement(paramXmlSerializer, localContactEntry.getMileage(), XmlNametable.GC_MILEAGE);
    serializeElement(paramXmlSerializer, localContactEntry.getNickname(), XmlNametable.GC_NICKNAME);
    serializeElement(paramXmlSerializer, localContactEntry.getOccupation(), XmlNametable.GC_OCCUPATION);
    serializeElement(paramXmlSerializer, localContactEntry.getShortName(), XmlNametable.GC_SHORTNAME);
    serializeElement(paramXmlSerializer, localContactEntry.getSubject(), XmlNametable.GC_SUBJECT);
    serializeElement(paramXmlSerializer, localContactEntry.getBillingInformation(), XmlNametable.GC_BILLINGINFO);
    serializeElement(paramXmlSerializer, localContactEntry.getPriority(), XmlNametable.GC_PRIORITY, XmlContactsGDataParser.TYPE_TO_REL_PRIORITY);
    serializeElement(paramXmlSerializer, localContactEntry.getSensitivity(), XmlNametable.GC_SENSITIVITY, XmlContactsGDataParser.TYPE_TO_REL_SENSITIVITY);
    serializeName(paramXmlSerializer, localContactEntry.getName());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.serializer.xml.XmlContactEntryGDataSerializer
 * JD-Core Version:    0.6.2
 */