package com.google.wireless.gdata.contacts.parser.xml;

import com.google.wireless.gdata.contacts.data.ContactEntry;
import com.google.wireless.gdata.contacts.data.ContactsElement;
import com.google.wireless.gdata.contacts.data.ContactsFeed;
import com.google.wireless.gdata.contacts.data.EmailAddress;
import com.google.wireless.gdata.contacts.data.GroupMembershipInfo;
import com.google.wireless.gdata.contacts.data.ImAddress;
import com.google.wireless.gdata.contacts.data.Organization;
import com.google.wireless.gdata.contacts.data.PhoneNumber;
import com.google.wireless.gdata.contacts.data.PostalAddress;
import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.ExtendedProperty;
import com.google.wireless.gdata.data.Feed;
import com.google.wireless.gdata.data.XmlUtils;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlGDataParser;
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
  public static final String IM_PROTOCOL_QQ = "http://schemas.google.com/g/2005#QQ";
  public static final String IM_PROTOCOL_SKYPE = "http://schemas.google.com/g/2005#SKYPE";
  private static final Hashtable IM_PROTOCOL_STRING_TO_TYPE_MAP = localHashtable6;
  public static final Hashtable IM_PROTOCOL_TYPE_TO_STRING_MAP = swapMap(localHashtable6);
  public static final String IM_PROTOCOL_YAHOO = "http://schemas.google.com/g/2005#YAHOO";
  public static final String LINK_REL_EDIT_PHOTO = "http://schemas.google.com/contacts/2008/rel#edit-photo";
  public static final String LINK_REL_PHOTO = "http://schemas.google.com/contacts/2008/rel#photo";
  public static final String NAMESPACE_CONTACTS = "gContact";
  public static final String NAMESPACE_CONTACTS_URI = "http://schemas.google.com/contact/2008";
  private static final Hashtable REL_TO_TYPE_EMAIL;
  private static final Hashtable REL_TO_TYPE_IM;
  private static final Hashtable REL_TO_TYPE_ORGANIZATION;
  private static final Hashtable REL_TO_TYPE_PHONE;
  private static final Hashtable REL_TO_TYPE_POSTAL;
  public static final String TYPESTRING_HOME = "http://schemas.google.com/g/2005#home";
  public static final String TYPESTRING_HOME_FAX = "http://schemas.google.com/g/2005#home_fax";
  public static final String TYPESTRING_MOBILE = "http://schemas.google.com/g/2005#mobile";
  public static final String TYPESTRING_OTHER = "http://schemas.google.com/g/2005#other";
  public static final String TYPESTRING_PAGER = "http://schemas.google.com/g/2005#pager";
  public static final String TYPESTRING_WORK = "http://schemas.google.com/g/2005#work";
  public static final String TYPESTRING_WORK_FAX = "http://schemas.google.com/g/2005#work_fax";
  public static final Hashtable TYPE_TO_REL_EMAIL;
  public static final Hashtable TYPE_TO_REL_IM;
  public static final Hashtable TYPE_TO_REL_ORGANIZATION;
  public static final Hashtable TYPE_TO_REL_PHONE;
  public static final Hashtable TYPE_TO_REL_POSTAL;

  static
  {
    Hashtable localHashtable1 = new Hashtable();
    localHashtable1.put("http://schemas.google.com/g/2005#home", new Byte((byte)1));
    localHashtable1.put("http://schemas.google.com/g/2005#work", new Byte((byte)2));
    localHashtable1.put("http://schemas.google.com/g/2005#other", new Byte((byte)3));
    localHashtable1.put("http://schemas.google.com/g/2005#primary", Byte.valueOf((byte)4));
    REL_TO_TYPE_EMAIL = localHashtable1;
    TYPE_TO_REL_EMAIL = swapMap(localHashtable1);
    Hashtable localHashtable2 = new Hashtable();
    localHashtable2.put("http://schemas.google.com/g/2005#home", new Byte((byte)2));
    localHashtable2.put("http://schemas.google.com/g/2005#mobile", new Byte((byte)1));
    localHashtable2.put("http://schemas.google.com/g/2005#pager", new Byte((byte)6));
    localHashtable2.put("http://schemas.google.com/g/2005#work", new Byte((byte)3));
    localHashtable2.put("http://schemas.google.com/g/2005#home_fax", new Byte((byte)5));
    localHashtable2.put("http://schemas.google.com/g/2005#work_fax", new Byte((byte)4));
    localHashtable2.put("http://schemas.google.com/g/2005#other", new Byte((byte)7));
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
  }

  public XmlContactsGDataParser(InputStream paramInputStream, XmlPullParser paramXmlPullParser)
    throws ParseException
  {
    super(paramInputStream, paramXmlPullParser);
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
      if ("orgName".equals(str))
        paramOrganization.setName(XmlUtils.extractChildText(paramXmlPullParser));
      else if ("orgTitle".equals(str))
        paramOrganization.setTitle(XmlUtils.extractChildText(paramXmlPullParser));
    }
  }

  private static void parseContactsElement(ContactsElement paramContactsElement, XmlPullParser paramXmlPullParser, Hashtable paramHashtable)
    throws XmlPullParserException
  {
    String str1 = paramXmlPullParser.getAttributeValue(null, "rel");
    String str2 = paramXmlPullParser.getAttributeValue(null, "label");
    if (((str2 == null) && (str1 == null)) || ((str2 != null) && (str1 != null)))
      str1 = "http://schemas.google.com/g/2005#other";
    if (str1 != null)
    {
      Object localObject = paramHashtable.get(str1.toLowerCase());
      if (localObject == null)
        throw new XmlPullParserException("unknown rel, " + str1);
      paramContactsElement.setType(((Byte)localObject).byteValue());
    }
    paramContactsElement.setLabel(str2);
    paramContactsElement.setIsPrimary("true".equals(paramXmlPullParser.getAttributeValue(null, "primary")));
  }

  private void parseExtendedProperty(ExtendedProperty paramExtendedProperty)
    throws IOException, XmlPullParserException
  {
    XmlPullParser localXmlPullParser = getParser();
    paramExtendedProperty.setName(localXmlPullParser.getAttributeValue(null, "name"));
    paramExtendedProperty.setValue(localXmlPullParser.getAttributeValue(null, "value"));
    paramExtendedProperty.setXmlBlob(XmlUtils.extractFirstChildTextIgnoreRest(localXmlPullParser));
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
    if ("email".equals(str1))
    {
      EmailAddress localEmailAddress = new EmailAddress();
      parseContactsElement(localEmailAddress, localXmlPullParser, REL_TO_TYPE_EMAIL);
      if (localEmailAddress.getType() == 4)
      {
        localEmailAddress.setType((byte)3);
        localEmailAddress.setIsPrimary(true);
        localEmailAddress.setLabel(null);
      }
      localEmailAddress.setAddress(localXmlPullParser.getAttributeValue(null, "address"));
      localContactEntry.addEmailAddress(localEmailAddress);
    }
    do
    {
      return;
      if ("deleted".equals(str1))
      {
        localContactEntry.setDeleted(true);
        return;
      }
      if ("im".equals(str1))
      {
        ImAddress localImAddress = new ImAddress();
        parseContactsElement(localImAddress, localXmlPullParser, REL_TO_TYPE_IM);
        localImAddress.setAddress(localXmlPullParser.getAttributeValue(null, "address"));
        localImAddress.setLabel(localXmlPullParser.getAttributeValue(null, "label"));
        String str2 = localXmlPullParser.getAttributeValue(null, "protocol");
        if (str2 == null)
        {
          localImAddress.setProtocolPredefined((byte)10);
          localImAddress.setProtocolCustom(null);
        }
        while (true)
        {
          localContactEntry.addImAddress(localImAddress);
          return;
          Byte localByte = (Byte)IM_PROTOCOL_STRING_TO_TYPE_MAP.get(str2);
          if (localByte == null)
          {
            localImAddress.setProtocolPredefined((byte)1);
            localImAddress.setProtocolCustom(str2);
          }
          else
          {
            localImAddress.setProtocolPredefined(localByte.byteValue());
            localImAddress.setProtocolCustom(null);
          }
        }
      }
      if ("postalAddress".equals(str1))
      {
        PostalAddress localPostalAddress = new PostalAddress();
        parseContactsElement(localPostalAddress, localXmlPullParser, REL_TO_TYPE_POSTAL);
        localPostalAddress.setValue(XmlUtils.extractChildText(localXmlPullParser));
        localContactEntry.addPostalAddress(localPostalAddress);
        return;
      }
      if ("phoneNumber".equals(str1))
      {
        PhoneNumber localPhoneNumber = new PhoneNumber();
        parseContactsElement(localPhoneNumber, localXmlPullParser, REL_TO_TYPE_PHONE);
        localPhoneNumber.setPhoneNumber(XmlUtils.extractChildText(localXmlPullParser));
        localContactEntry.addPhoneNumber(localPhoneNumber);
        return;
      }
      if ("organization".equals(str1))
      {
        Organization localOrganization = new Organization();
        parseContactsElement(localOrganization, localXmlPullParser, REL_TO_TYPE_ORGANIZATION);
        handleOrganizationSubElement(localOrganization, localXmlPullParser);
        localContactEntry.addOrganization(localOrganization);
        return;
      }
      if ("extendedProperty".equals(str1))
      {
        ExtendedProperty localExtendedProperty = new ExtendedProperty();
        parseExtendedProperty(localExtendedProperty);
        localContactEntry.addExtendedProperty(localExtendedProperty);
        return;
      }
      if ("groupMembershipInfo".equals(str1))
      {
        GroupMembershipInfo localGroupMembershipInfo = new GroupMembershipInfo();
        localGroupMembershipInfo.setGroup(localXmlPullParser.getAttributeValue(null, "href"));
        localGroupMembershipInfo.setDeleted("true".equals(localXmlPullParser.getAttributeValue(null, "deleted")));
        localContactEntry.addGroup(localGroupMembershipInfo);
        return;
      }
    }
    while (!"yomiName".equals(str1));
    localContactEntry.setYomiName(XmlUtils.extractChildText(localXmlPullParser));
  }

  protected void handleExtraLinkInEntry(String paramString1, String paramString2, String paramString3, Entry paramEntry)
    throws XmlPullParserException, IOException
  {
    if ("http://schemas.google.com/contacts/2008/rel#photo".equals(paramString1))
      ((ContactEntry)paramEntry).setLinkPhoto(paramString3, paramString2);
    while (!"http://schemas.google.com/contacts/2008/rel#edit-photo".equals(paramString1))
      return;
    ((ContactEntry)paramEntry).setLinkEditPhoto(paramString3, paramString2);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.parser.xml.XmlContactsGDataParser
 * JD-Core Version:    0.6.2
 */