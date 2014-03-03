package com.google.wireless.gdata2.contacts.parser.xml;

import com.google.wireless.gdata2.contacts.data.CalendarLink;
import com.google.wireless.gdata2.contacts.data.ContactEntry;
import com.google.wireless.gdata2.contacts.data.ContactsElement;
import com.google.wireless.gdata2.contacts.data.ContactsFeed;
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
import com.google.wireless.gdata2.data.Entry;
import com.google.wireless.gdata2.data.ExtendedProperty;
import com.google.wireless.gdata2.data.Feed;
import com.google.wireless.gdata2.data.XmlUtils;
import com.google.wireless.gdata2.parser.ParseException;
import com.google.wireless.gdata2.parser.xml.XmlGDataParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlContactsGDataParser extends XmlGDataParser
{
  private static final String GD_NAMESPACE = "http://schemas.google.com/g/2005#";
  public static final String IM_PROTOCOL_AIM = "http://schemas.google.com/g/2005#AIM";
  public static final String IM_PROTOCOL_GOOGLE_TALK = "http://schemas.google.com/g/2005#GOOGLE_TALK";
  public static final String IM_PROTOCOL_ICQ = "http://schemas.google.com/g/2005#ICQ";
  public static final String IM_PROTOCOL_JABBER = "http://schemas.google.com/g/2005#JABBER";
  public static final String IM_PROTOCOL_MSN = "http://schemas.google.com/g/2005#MSN";
  public static final String IM_PROTOCOL_NETMEETING = "http://schemas.google.com/g/2005#netmeeting";
  public static final String IM_PROTOCOL_QQ = "http://schemas.google.com/g/2005#QQ";
  public static final String IM_PROTOCOL_SKYPE = "http://schemas.google.com/g/2005#SKYPE";
  private static final Hashtable IM_PROTOCOL_STRING_TO_TYPE_MAP;
  public static final Hashtable IM_PROTOCOL_TYPE_TO_STRING_MAP;
  public static final String IM_PROTOCOL_YAHOO = "http://schemas.google.com/g/2005#YAHOO";
  public static final String LINK_REL_PHOTO = "http://schemas.google.com/contacts/2008/rel#photo";
  public static final String NAMESPACE_CONTACTS = "gContact";
  public static final String NAMESPACE_CONTACTS_URI = "http://schemas.google.com/contact/2008";
  private static final Hashtable REL_TO_TYPE_CALENDARLINK;
  private static final Hashtable REL_TO_TYPE_EMAIL;
  private static final Hashtable REL_TO_TYPE_EVENT;
  private static final Hashtable REL_TO_TYPE_EXTERNALID;
  private static final Hashtable REL_TO_TYPE_IM;
  private static final Hashtable REL_TO_TYPE_JOT;
  private static final Hashtable REL_TO_TYPE_ORGANIZATION;
  private static final Hashtable REL_TO_TYPE_PHONE;
  private static final Hashtable REL_TO_TYPE_POSTAL;
  private static final Hashtable REL_TO_TYPE_PRIORITY;
  private static final Hashtable REL_TO_TYPE_RELATION;
  private static final Hashtable REL_TO_TYPE_SENSITIVITY;
  private static final Hashtable REL_TO_TYPE_WEBSITE = localHashtable14;
  public static final String TYPESTRING_ASSISTANT = "http://schemas.google.com/g/2005#assistant";
  public static final String TYPESTRING_CALENDARLINK_FREEBUSY = "free-busy";
  public static final String TYPESTRING_CALENDARLINK_HOME = "home";
  public static final String TYPESTRING_CALENDARLINK_WORK = "work";
  public static final String TYPESTRING_CALLBACK = "http://schemas.google.com/g/2005#callback";
  public static final String TYPESTRING_CAR = "http://schemas.google.com/g/2005#car";
  public static final String TYPESTRING_COMPANY_MAIN = "http://schemas.google.com/g/2005#company_main";
  public static final String TYPESTRING_EVENT_ANNIVERARY = "anniversary";
  public static final String TYPESTRING_EVENT_OTHER = "other";
  public static final String TYPESTRING_EXTERNALID_ACCOUNT = "account";
  public static final String TYPESTRING_EXTERNALID_CUSTOMER = "customer";
  public static final String TYPESTRING_EXTERNALID_NETWORK = "network";
  public static final String TYPESTRING_EXTERNALID_ORGANIZATION = "organization";
  public static final String TYPESTRING_HOME = "http://schemas.google.com/g/2005#home";
  public static final String TYPESTRING_HOME_FAX = "http://schemas.google.com/g/2005#home_fax";
  public static final String TYPESTRING_ISDN = "http://schemas.google.com/g/2005#isdn";
  public static final String TYPESTRING_JOT_HOME = "home";
  public static final String TYPESTRING_JOT_KEYWORDS = "keywords";
  public static final String TYPESTRING_JOT_OTHER = "other";
  public static final String TYPESTRING_JOT_USER = "user";
  public static final String TYPESTRING_JOT_WORK = "work";
  public static final String TYPESTRING_MAIN = "http://schemas.google.com/g/2005#main";
  public static final String TYPESTRING_MOBILE = "http://schemas.google.com/g/2005#mobile";
  public static final String TYPESTRING_OTHER = "http://schemas.google.com/g/2005#other";
  public static final String TYPESTRING_OTHER_FAX = "http://schemas.google.com/g/2005#other_fax";
  public static final String TYPESTRING_PAGER = "http://schemas.google.com/g/2005#pager";
  public static final String TYPESTRING_PRIORITY_HIGH = "high";
  public static final String TYPESTRING_PRIORITY_LOW = "low";
  public static final String TYPESTRING_PRIORITY_NORMAL = "normal";
  public static final String TYPESTRING_RADIO = "http://schemas.google.com/g/2005#radio";
  public static final String TYPESTRING_RELATION_ASSISTANT = "assistant";
  public static final String TYPESTRING_RELATION_BROTHER = "brother";
  public static final String TYPESTRING_RELATION_CHILD = "child";
  public static final String TYPESTRING_RELATION_DOMESTICPARTNER = "domestic-partner";
  public static final String TYPESTRING_RELATION_FATHER = "father";
  public static final String TYPESTRING_RELATION_FRIEND = "friend";
  public static final String TYPESTRING_RELATION_MANAGER = "manager";
  public static final String TYPESTRING_RELATION_MOTHER = "mother";
  public static final String TYPESTRING_RELATION_PARENT = "parent";
  public static final String TYPESTRING_RELATION_PARTNER = "partner";
  public static final String TYPESTRING_RELATION_REFERREDBY = "referred-by";
  public static final String TYPESTRING_RELATION_RELATIVE = "relative";
  public static final String TYPESTRING_RELATION_SISTER = "sister";
  public static final String TYPESTRING_RELATION_SPOUSE = "spouse";
  public static final String TYPESTRING_SENSITIVITY_CONFIDENTIAL = "confidential";
  public static final String TYPESTRING_SENSITIVITY_NORMAL = "normal";
  public static final String TYPESTRING_SENSITIVITY_PERSONAL = "personal";
  public static final String TYPESTRING_SENSITIVITY_PRIVATE = "private";
  public static final String TYPESTRING_TELEX = "http://schemas.google.com/g/2005#telex";
  public static final String TYPESTRING_TTY_TDD = "http://schemas.google.com/g/2005#tty_tdd";
  public static final String TYPESTRING_WEBSITE_BLOG = "blog";
  public static final String TYPESTRING_WEBSITE_FTP = "ftp";
  public static final String TYPESTRING_WEBSITE_HOME = "home";
  public static final String TYPESTRING_WEBSITE_HOMEPAGE = "home-page";
  public static final String TYPESTRING_WEBSITE_OTHER = "other";
  public static final String TYPESTRING_WEBSITE_PROFILE = "profile";
  public static final String TYPESTRING_WEBSITE_WORK = "work";
  public static final String TYPESTRING_WORK = "http://schemas.google.com/g/2005#work";
  public static final String TYPESTRING_WORK_FAX = "http://schemas.google.com/g/2005#work_fax";
  public static final String TYPESTRING_WORK_MOBILE = "http://schemas.google.com/g/2005#work_mobile";
  public static final String TYPESTRING_WORK_PAGER = "http://schemas.google.com/g/2005#work_pager";
  public static final Hashtable TYPE_TO_REL_CALENDARLINK;
  public static final Hashtable TYPE_TO_REL_EMAIL;
  public static final Hashtable TYPE_TO_REL_EVENT;
  public static final Hashtable TYPE_TO_REL_EXTERNALID;
  public static final Hashtable TYPE_TO_REL_IM;
  public static final Hashtable TYPE_TO_REL_JOT;
  public static final Hashtable TYPE_TO_REL_ORGANIZATION;
  public static final Hashtable TYPE_TO_REL_PHONE;
  public static final Hashtable TYPE_TO_REL_POSTAL;
  public static final Hashtable TYPE_TO_REL_PRIORITY;
  public static final Hashtable TYPE_TO_REL_RELATION;
  public static final Hashtable TYPE_TO_REL_SENSITIVITY;
  public static final Hashtable TYPE_TO_REL_WEBSITE = swapMap(localHashtable14);

  static
  {
    Hashtable localHashtable1 = new Hashtable();
    localHashtable1.put("http://schemas.google.com/g/2005#home", new Byte((byte)1));
    localHashtable1.put("http://schemas.google.com/g/2005#work", new Byte((byte)2));
    localHashtable1.put("http://schemas.google.com/g/2005#other", new Byte((byte)3));
    REL_TO_TYPE_EMAIL = localHashtable1;
    TYPE_TO_REL_EMAIL = swapMap(localHashtable1);
    Hashtable localHashtable2 = new Hashtable();
    localHashtable2.put("http://schemas.google.com/g/2005#home", new Byte((byte)2));
    localHashtable2.put("http://schemas.google.com/g/2005#mobile", new Byte((byte)1));
    localHashtable2.put("http://schemas.google.com/g/2005#pager", new Byte((byte)6));
    localHashtable2.put("http://schemas.google.com/g/2005#work", new Byte((byte)3));
    localHashtable2.put("http://schemas.google.com/g/2005#home_fax", new Byte((byte)5));
    localHashtable2.put("http://schemas.google.com/g/2005#work_fax", new Byte((byte)4));
    localHashtable2.put("http://schemas.google.com/g/2005#assistant", new Byte((byte)7));
    localHashtable2.put("http://schemas.google.com/g/2005#callback", new Byte((byte)8));
    localHashtable2.put("http://schemas.google.com/g/2005#car", new Byte((byte)9));
    localHashtable2.put("http://schemas.google.com/g/2005#company_main", new Byte((byte)10));
    localHashtable2.put("http://schemas.google.com/g/2005#isdn", new Byte((byte)11));
    localHashtable2.put("http://schemas.google.com/g/2005#main", new Byte((byte)12));
    localHashtable2.put("http://schemas.google.com/g/2005#other_fax", new Byte((byte)13));
    localHashtable2.put("http://schemas.google.com/g/2005#radio", new Byte((byte)14));
    localHashtable2.put("http://schemas.google.com/g/2005#telex", new Byte((byte)15));
    localHashtable2.put("http://schemas.google.com/g/2005#tty_tdd", new Byte((byte)16));
    localHashtable2.put("http://schemas.google.com/g/2005#work_mobile", new Byte((byte)17));
    localHashtable2.put("http://schemas.google.com/g/2005#work_pager", new Byte((byte)18));
    localHashtable2.put("http://schemas.google.com/g/2005#other", new Byte((byte)19));
    REL_TO_TYPE_PHONE = localHashtable2;
    TYPE_TO_REL_PHONE = swapMap(localHashtable2);
    Hashtable localHashtable3 = new Hashtable();
    localHashtable3.put("http://schemas.google.com/g/2005#home", new Byte((byte)1));
    localHashtable3.put("http://schemas.google.com/g/2005#work", new Byte((byte)2));
    localHashtable3.put("http://schemas.google.com/g/2005#other", new Byte((byte)3));
    REL_TO_TYPE_POSTAL = localHashtable3;
    TYPE_TO_REL_POSTAL = swapMap(localHashtable3);
    Hashtable localHashtable4 = new Hashtable();
    localHashtable4.put("http://schemas.google.com/g/2005#home", new Byte((byte)1));
    localHashtable4.put("http://schemas.google.com/g/2005#work", new Byte((byte)2));
    localHashtable4.put("http://schemas.google.com/g/2005#other", new Byte((byte)3));
    REL_TO_TYPE_IM = localHashtable4;
    TYPE_TO_REL_IM = swapMap(localHashtable4);
    Hashtable localHashtable5 = new Hashtable();
    localHashtable5.put("http://schemas.google.com/g/2005#work", new Byte((byte)1));
    localHashtable5.put("http://schemas.google.com/g/2005#other", new Byte((byte)2));
    REL_TO_TYPE_ORGANIZATION = localHashtable5;
    TYPE_TO_REL_ORGANIZATION = swapMap(localHashtable5);
    Hashtable localHashtable6 = new Hashtable();
    localHashtable6.put("http://schemas.google.com/g/2005#AIM", new Byte((byte)2));
    localHashtable6.put("http://schemas.google.com/g/2005#MSN", new Byte((byte)3));
    localHashtable6.put("http://schemas.google.com/g/2005#YAHOO", new Byte((byte)4));
    localHashtable6.put("http://schemas.google.com/g/2005#SKYPE", new Byte((byte)5));
    localHashtable6.put("http://schemas.google.com/g/2005#QQ", new Byte((byte)6));
    localHashtable6.put("http://schemas.google.com/g/2005#GOOGLE_TALK", new Byte((byte)7));
    localHashtable6.put("http://schemas.google.com/g/2005#ICQ", new Byte((byte)8));
    localHashtable6.put("http://schemas.google.com/g/2005#JABBER", new Byte((byte)9));
    localHashtable6.put("http://schemas.google.com/g/2005#netmeeting", new Byte((byte)10));
    IM_PROTOCOL_STRING_TO_TYPE_MAP = localHashtable6;
    IM_PROTOCOL_TYPE_TO_STRING_MAP = swapMap(localHashtable6);
    Hashtable localHashtable7 = new Hashtable();
    localHashtable7.put("home", new Byte((byte)1));
    localHashtable7.put("work", new Byte((byte)2));
    localHashtable7.put("free-busy", new Byte((byte)3));
    REL_TO_TYPE_CALENDARLINK = localHashtable7;
    TYPE_TO_REL_CALENDARLINK = swapMap(localHashtable7);
    Hashtable localHashtable8 = new Hashtable();
    localHashtable8.put("anniversary", new Byte((byte)1));
    localHashtable8.put("other", new Byte((byte)2));
    REL_TO_TYPE_EVENT = localHashtable8;
    TYPE_TO_REL_EVENT = swapMap(localHashtable8);
    Hashtable localHashtable9 = new Hashtable();
    localHashtable9.put("account", new Byte((byte)1));
    localHashtable9.put("customer", new Byte((byte)2));
    localHashtable9.put("network", new Byte((byte)3));
    localHashtable9.put("organization", new Byte((byte)4));
    REL_TO_TYPE_EXTERNALID = localHashtable9;
    TYPE_TO_REL_EXTERNALID = swapMap(localHashtable9);
    Hashtable localHashtable10 = new Hashtable();
    localHashtable10.put("home", new Byte((byte)1));
    localHashtable10.put("keywords", new Byte((byte)3));
    localHashtable10.put("other", new Byte((byte)5));
    localHashtable10.put("user", new Byte((byte)4));
    localHashtable10.put("work", new Byte((byte)2));
    REL_TO_TYPE_JOT = localHashtable10;
    TYPE_TO_REL_JOT = swapMap(localHashtable10);
    Hashtable localHashtable11 = new Hashtable();
    localHashtable11.put("high", new Byte((byte)1));
    localHashtable11.put("normal", new Byte((byte)2));
    localHashtable11.put("low", new Byte((byte)3));
    REL_TO_TYPE_PRIORITY = localHashtable11;
    TYPE_TO_REL_PRIORITY = swapMap(localHashtable11);
    Hashtable localHashtable12 = new Hashtable();
    localHashtable12.put("assistant", new Byte((byte)1));
    localHashtable12.put("brother", new Byte((byte)2));
    localHashtable12.put("child", new Byte((byte)3));
    localHashtable12.put("domestic-partner", new Byte((byte)4));
    localHashtable12.put("father", new Byte((byte)5));
    localHashtable12.put("friend", new Byte((byte)6));
    localHashtable12.put("manager", new Byte((byte)7));
    localHashtable12.put("mother", new Byte((byte)8));
    localHashtable12.put("parent", new Byte((byte)9));
    localHashtable12.put("partner", new Byte((byte)10));
    localHashtable12.put("referred-by", new Byte((byte)11));
    localHashtable12.put("relative", new Byte((byte)12));
    localHashtable12.put("sister", new Byte((byte)13));
    localHashtable12.put("spouse", new Byte((byte)14));
    REL_TO_TYPE_RELATION = localHashtable12;
    TYPE_TO_REL_RELATION = swapMap(localHashtable12);
    Hashtable localHashtable13 = new Hashtable();
    localHashtable13.put("confidential", new Byte((byte)1));
    localHashtable13.put("normal", new Byte((byte)2));
    localHashtable13.put("personal", new Byte((byte)3));
    localHashtable13.put("private", new Byte((byte)4));
    REL_TO_TYPE_SENSITIVITY = localHashtable13;
    TYPE_TO_REL_SENSITIVITY = swapMap(localHashtable13);
    Hashtable localHashtable14 = new Hashtable();
    localHashtable14.put("blog", new Byte((byte)2));
    localHashtable14.put("home-page", new Byte((byte)1));
    localHashtable14.put("profile", new Byte((byte)3));
    localHashtable14.put("home", new Byte((byte)4));
    localHashtable14.put("work", new Byte((byte)5));
    localHashtable14.put("other", new Byte((byte)6));
    localHashtable14.put("ftp", new Byte((byte)7));
  }

  public XmlContactsGDataParser(InputStream paramInputStream, XmlPullParser paramXmlPullParser)
    throws ParseException
  {
    super(paramInputStream, paramXmlPullParser);
  }

  private static void handleEventSubElement(Event paramEvent, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    while (true)
    {
      String str = XmlUtils.nextDirectChildTag(paramXmlPullParser, i);
      if (str == null)
        return;
      if (XmlNametable.GD_WHEN.equals(str))
        paramEvent.setStartDate(paramXmlPullParser.getAttributeValue(null, XmlNametable.STARTTIME));
    }
  }

  private static void handleNameSubElement(Name paramName, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    while (true)
    {
      String str = XmlUtils.nextDirectChildTag(paramXmlPullParser, i);
      if (str == null)
        return;
      if (XmlNametable.GD_NAME_GIVENNAME.equals(str))
      {
        paramName.setGivenNameYomi(paramXmlPullParser.getAttributeValue(null, XmlNametable.GD_NAME_YOMI));
        paramName.setGivenName(XmlUtils.extractChildText(paramXmlPullParser));
      }
      else if (XmlNametable.GD_NAME_ADDITIONALNAME.equals(str))
      {
        paramName.setAdditionalNameYomi(paramXmlPullParser.getAttributeValue(null, XmlNametable.GD_NAME_YOMI));
        paramName.setAdditionalName(XmlUtils.extractChildText(paramXmlPullParser));
      }
      else if (XmlNametable.GD_NAME_FAMILYNAME.equals(str))
      {
        paramName.setFamilyNameYomi(paramXmlPullParser.getAttributeValue(null, XmlNametable.GD_NAME_YOMI));
        paramName.setFamilyName(XmlUtils.extractChildText(paramXmlPullParser));
      }
      else if (XmlNametable.GD_NAME_PREFIX.equals(str))
      {
        paramName.setNamePrefix(XmlUtils.extractChildText(paramXmlPullParser));
      }
      else if (XmlNametable.GD_NAME_SUFFIX.equals(str))
      {
        paramName.setNameSuffix(XmlUtils.extractChildText(paramXmlPullParser));
      }
      else if (XmlNametable.GD_NAME_FULLNAME.equals(str))
      {
        paramName.setFullNameYomi(paramXmlPullParser.getAttributeValue(null, XmlNametable.GD_NAME_YOMI));
        paramName.setFullName(XmlUtils.extractChildText(paramXmlPullParser));
      }
    }
  }

  private static void handleOrganizationSubElement(Organization paramOrganization, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    while (true)
    {
      String str = XmlUtils.nextDirectChildTag(paramXmlPullParser, i);
      if (str == null)
        return;
      if (XmlNametable.GD_ORG_NAME.equals(str))
        paramOrganization.setName(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_ORG_TITLE.equals(str))
        paramOrganization.setTitle(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_ORG_DEPARTMENT.equals(str))
        paramOrganization.setOrgDepartment(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_ORG_JOBDESC.equals(str))
        paramOrganization.setOrgJobDescription(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_ORG_SYMBOL.equals(str))
        paramOrganization.setOrgSymbol(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_WHERE.equals(str))
        paramOrganization.setWhere(paramXmlPullParser.getAttributeValue(null, XmlNametable.VALUESTRING));
    }
  }

  private static void handleStructuredPostalAddressSubElement(StructuredPostalAddress paramStructuredPostalAddress, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    while (true)
    {
      String str = XmlUtils.nextDirectChildTag(paramXmlPullParser, i);
      if (str == null)
        return;
      if (XmlNametable.GD_SPA_STREET.equals(str))
        paramStructuredPostalAddress.setStreet(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_SPA_POBOX.equals(str))
        paramStructuredPostalAddress.setPobox(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_SPA_NEIGHBORHOOD.equals(str))
        paramStructuredPostalAddress.setNeighborhood(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_SPA_CITY.equals(str))
        paramStructuredPostalAddress.setCity(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_SPA_REGION.equals(str))
        paramStructuredPostalAddress.setRegion(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_SPA_POSTCODE.equals(str))
        paramStructuredPostalAddress.setPostcode(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_SPA_COUNTRY.equals(str))
        paramStructuredPostalAddress.setCountry(XmlUtils.extractChildText(paramXmlPullParser));
      else if (XmlNametable.GD_SPA_FORMATTEDADDRESS.equals(str))
        paramStructuredPostalAddress.setFormattedAddress(XmlUtils.extractChildText(paramXmlPullParser));
    }
  }

  private static void parseContactsElement(ContactsElement paramContactsElement, XmlPullParser paramXmlPullParser, Hashtable paramHashtable)
    throws XmlPullParserException
  {
    parseTypedElement(paramContactsElement, paramXmlPullParser, paramHashtable);
    paramContactsElement.setIsPrimary("true".equals(paramXmlPullParser.getAttributeValue(null, XmlNametable.PRIMARY)));
  }

  private void parseExtendedProperty(ExtendedProperty paramExtendedProperty)
    throws IOException, XmlPullParserException
  {
    XmlPullParser localXmlPullParser = getParser();
    paramExtendedProperty.setName(localXmlPullParser.getAttributeValue(null, XmlNametable.GD_NAME));
    paramExtendedProperty.setValue(localXmlPullParser.getAttributeValue(null, XmlNametable.VALUE));
    paramExtendedProperty.setXmlBlob(XmlUtils.extractFirstChildTextIgnoreRest(localXmlPullParser));
  }

  private static void parseTypedElement(TypedElement paramTypedElement, XmlPullParser paramXmlPullParser, Hashtable paramHashtable)
    throws XmlPullParserException
  {
    String str1 = paramXmlPullParser.getAttributeValue(null, XmlNametable.REL);
    String str2 = paramXmlPullParser.getAttributeValue(null, XmlNametable.LABEL);
    if (((str2 == null) && (str1 == null)) || ((str2 != null) && (str1 != null)))
      str1 = "http://schemas.google.com/g/2005#other";
    if (str1 != null)
      paramTypedElement.setType(relToType(str1, paramHashtable));
    paramTypedElement.setLabel(str2);
  }

  private static byte relToType(String paramString, Hashtable paramHashtable)
    throws XmlPullParserException
  {
    if (paramString != null)
    {
      Object localObject = paramHashtable.get(paramString.toLowerCase());
      if (localObject == null)
        throw new XmlPullParserException("unknown rel, " + paramString);
      return ((Byte)localObject).byteValue();
    }
    return -1;
  }

  private static Hashtable swapMap(Hashtable paramHashtable)
  {
    Hashtable localHashtable = new Hashtable();
    Enumeration localEnumeration = paramHashtable.keys();
    while (localEnumeration.hasMoreElements())
    {
      Object localObject1 = localEnumeration.nextElement();
      Object localObject2 = paramHashtable.get(localObject1);
      if (localHashtable.containsKey(localObject2))
        throw new IllegalArgumentException("value " + localObject2 + " was already encountered");
      localHashtable.put(localObject2, localObject1);
    }
    return localHashtable;
  }

  protected Entry createEntry()
  {
    return new ContactEntry();
  }

  protected Feed createFeed()
  {
    return new ContactsFeed();
  }

  protected void handleExtraElementInEntry(Entry paramEntry)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = getParser();
    if (!(paramEntry instanceof ContactEntry))
      throw new IllegalArgumentException("Expected ContactEntry!");
    ContactEntry localContactEntry = (ContactEntry)paramEntry;
    String str1 = localXmlPullParser.getName();
    String str2 = localXmlPullParser.getNamespace();
    if ("http://schemas.google.com/g/2005".equals(str2))
      if (XmlNametable.GD_EMAIL.equals(str1))
      {
        EmailAddress localEmailAddress = new EmailAddress();
        parseContactsElement(localEmailAddress, localXmlPullParser, REL_TO_TYPE_EMAIL);
        localEmailAddress.setDisplayName(localXmlPullParser.getAttributeValue(null, XmlNametable.GD_EMAIL_DISPLAYNAME));
        localEmailAddress.setAddress(localXmlPullParser.getAttributeValue(null, XmlNametable.GD_ADDRESS));
        localContactEntry.addEmailAddress(localEmailAddress);
      }
    do
    {
      do
      {
        do
        {
          return;
          if (XmlNametable.GD_DELETED.equals(str1))
          {
            localContactEntry.setDeleted(true);
            return;
          }
          if (XmlNametable.GD_IM.equals(str1))
          {
            ImAddress localImAddress = new ImAddress();
            parseContactsElement(localImAddress, localXmlPullParser, REL_TO_TYPE_IM);
            localImAddress.setAddress(localXmlPullParser.getAttributeValue(null, XmlNametable.GD_ADDRESS));
            localImAddress.setLabel(localXmlPullParser.getAttributeValue(null, XmlNametable.LABEL));
            String str3 = localXmlPullParser.getAttributeValue(null, XmlNametable.GD_PROTOCOL);
            if (str3 == null)
            {
              localImAddress.setProtocolPredefined((byte)11);
              localImAddress.setProtocolCustom(null);
            }
            while (true)
            {
              localContactEntry.addImAddress(localImAddress);
              return;
              Byte localByte = (Byte)IM_PROTOCOL_STRING_TO_TYPE_MAP.get(str3);
              if (localByte == null)
              {
                localImAddress.setProtocolPredefined((byte)1);
                localImAddress.setProtocolCustom(str3);
              }
              else
              {
                localImAddress.setProtocolPredefined(localByte.byteValue());
                localImAddress.setProtocolCustom(null);
              }
            }
          }
          if (XmlNametable.GD_SPA.equals(str1))
          {
            StructuredPostalAddress localStructuredPostalAddress = new StructuredPostalAddress();
            parseContactsElement(localStructuredPostalAddress, localXmlPullParser, REL_TO_TYPE_POSTAL);
            handleStructuredPostalAddressSubElement(localStructuredPostalAddress, localXmlPullParser);
            localContactEntry.addPostalAddress(localStructuredPostalAddress);
            return;
          }
          if (XmlNametable.GD_PHONENUMBER.equals(str1))
          {
            PhoneNumber localPhoneNumber = new PhoneNumber();
            parseContactsElement(localPhoneNumber, localXmlPullParser, REL_TO_TYPE_PHONE);
            localPhoneNumber.setPhoneNumber(XmlUtils.extractChildText(localXmlPullParser));
            localContactEntry.addPhoneNumber(localPhoneNumber);
            return;
          }
          if (XmlNametable.GD_ORGANIZATION.equals(str1))
          {
            Organization localOrganization = new Organization();
            parseContactsElement(localOrganization, localXmlPullParser, REL_TO_TYPE_ORGANIZATION);
            handleOrganizationSubElement(localOrganization, localXmlPullParser);
            localContactEntry.addOrganization(localOrganization);
            return;
          }
          if (XmlNametable.GD_EXTENDEDPROPERTY.equals(str1))
          {
            ExtendedProperty localExtendedProperty = new ExtendedProperty();
            parseExtendedProperty(localExtendedProperty);
            localContactEntry.addExtendedProperty(localExtendedProperty);
            return;
          }
        }
        while (!XmlNametable.GD_NAME.equals(str1));
        Name localName = new Name();
        handleNameSubElement(localName, localXmlPullParser);
        localContactEntry.setName(localName);
        return;
      }
      while (!"http://schemas.google.com/contact/2008".equals(str2));
      if (XmlNametable.GC_GMI.equals(str1))
      {
        GroupMembershipInfo localGroupMembershipInfo = new GroupMembershipInfo();
        localGroupMembershipInfo.setGroup(localXmlPullParser.getAttributeValue(null, XmlNametable.HREF));
        localGroupMembershipInfo.setDeleted("true".equals(localXmlPullParser.getAttributeValue(null, XmlNametable.GD_DELETED)));
        localContactEntry.addGroup(localGroupMembershipInfo);
        return;
      }
      if (XmlNametable.GC_BIRTHDAY.equals(str1))
      {
        localContactEntry.setBirthday(localXmlPullParser.getAttributeValue(null, XmlNametable.GD_WHEN));
        return;
      }
      if (XmlNametable.GC_BILLINGINFO.equals(str1))
      {
        localContactEntry.setBillingInformation(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if (XmlNametable.GC_CALENDARLINK.equals(str1))
      {
        CalendarLink localCalendarLink = new CalendarLink();
        parseContactsElement(localCalendarLink, localXmlPullParser, REL_TO_TYPE_CALENDARLINK);
        localCalendarLink.setHRef(localXmlPullParser.getAttributeValue(null, XmlNametable.HREF));
        localContactEntry.addCalendarLink(localCalendarLink);
        return;
      }
      if (XmlNametable.GC_DIRECTORYSERVER.equals(str1))
      {
        localContactEntry.setDirectoryServer(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if ("event".equals(str1))
      {
        Event localEvent = new Event();
        parseTypedElement(localEvent, localXmlPullParser, REL_TO_TYPE_EVENT);
        handleEventSubElement(localEvent, localXmlPullParser);
        localContactEntry.addEvent(localEvent);
        return;
      }
      if (XmlNametable.GC_EXTERNALID.equals(str1))
      {
        ExternalId localExternalId = new ExternalId();
        parseTypedElement(localExternalId, localXmlPullParser, REL_TO_TYPE_EXTERNALID);
        localExternalId.setValue(localXmlPullParser.getAttributeValue(null, XmlNametable.VALUE));
        localContactEntry.addExternalId(localExternalId);
        return;
      }
      if (XmlNametable.GC_GENDER.equals(str1))
      {
        localContactEntry.setGender(localXmlPullParser.getAttributeValue(null, XmlNametable.VALUE));
        return;
      }
      if (XmlNametable.GC_HOBBY.equals(str1))
      {
        localContactEntry.addHobby(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if (XmlNametable.GC_INITIALS.equals(str1))
      {
        localContactEntry.setInitials(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if (XmlNametable.GC_JOT.equals(str1))
      {
        Jot localJot = new Jot();
        parseTypedElement(localJot, localXmlPullParser, REL_TO_TYPE_JOT);
        localJot.setLabel(XmlUtils.extractChildText(localXmlPullParser));
        localContactEntry.addJot(localJot);
        return;
      }
      if (XmlNametable.GC_LANGUAGE.equals(str1))
      {
        Language localLanguage = new Language();
        localLanguage.setCode(localXmlPullParser.getAttributeValue(null, XmlNametable.CODE));
        localLanguage.setLabel(localXmlPullParser.getAttributeValue(null, XmlNametable.LABEL));
        localContactEntry.addLanguage(localLanguage);
        return;
      }
      if (XmlNametable.GC_MAIDENNAME.equals(str1))
      {
        localContactEntry.setMaidenName(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if (XmlNametable.GC_MILEAGE.equals(str1))
      {
        localContactEntry.setMileage(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if (XmlNametable.GC_NICKNAME.equals(str1))
      {
        localContactEntry.setNickname(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if (XmlNametable.GC_OCCUPATION.equals(str1))
      {
        localContactEntry.setOccupation(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if (XmlNametable.GC_PRIORITY.equals(str1))
      {
        localContactEntry.setPriority(relToType(localXmlPullParser.getAttributeValue(null, XmlNametable.REL), REL_TO_TYPE_PRIORITY));
        return;
      }
      if (XmlNametable.GC_RELATION.equals(str1))
      {
        Relation localRelation = new Relation();
        parseTypedElement(localRelation, localXmlPullParser, REL_TO_TYPE_RELATION);
        localRelation.setText(XmlUtils.extractChildText(localXmlPullParser));
        localContactEntry.addRelation(localRelation);
        return;
      }
      if (XmlNametable.GC_SENSITIVITY.equals(str1))
      {
        localContactEntry.setSensitivity(relToType(localXmlPullParser.getAttributeValue(null, XmlNametable.REL), REL_TO_TYPE_SENSITIVITY));
        return;
      }
      if (XmlNametable.GC_SHORTNAME.equals(str1))
      {
        localContactEntry.setShortName(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if (XmlNametable.GC_SUBJECT.equals(str1))
      {
        localContactEntry.setSubject(XmlUtils.extractChildText(localXmlPullParser));
        return;
      }
      if (XmlNametable.GC_UDF.equals(str1))
      {
        UserDefinedField localUserDefinedField = new UserDefinedField();
        localUserDefinedField.setKey(localXmlPullParser.getAttributeValue(null, XmlNametable.KEY));
        localUserDefinedField.setValue(localXmlPullParser.getAttributeValue(null, XmlNametable.VALUE));
        localContactEntry.addUserDefinedField(localUserDefinedField);
        return;
      }
    }
    while (!XmlNametable.GC_WEBSITE.equals(str1));
    WebSite localWebSite = new WebSite();
    parseContactsElement(localWebSite, localXmlPullParser, REL_TO_TYPE_WEBSITE);
    localWebSite.setHRef(localXmlPullParser.getAttributeValue(null, XmlNametable.HREF));
    localContactEntry.addWebSite(localWebSite);
  }

  protected void handleExtraLinkInEntry(String paramString1, String paramString2, String paramString3, Entry paramEntry)
    throws XmlPullParserException, IOException
  {
    if ("http://schemas.google.com/contacts/2008/rel#photo".equals(paramString1))
      ((ContactEntry)paramEntry).setLinkPhoto(paramString3, paramString2, getParser().getAttributeValue("http://schemas.google.com/g/2005", XmlNametable.ETAG));
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.parser.xml.XmlContactsGDataParser
 * JD-Core Version:    0.6.2
 */