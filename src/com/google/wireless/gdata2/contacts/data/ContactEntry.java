package com.google.wireless.gdata2.contacts.data;

import com.google.wireless.gdata2.data.Entry;
import com.google.wireless.gdata2.data.ExtendedProperty;
import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.parser.ParseException;
import java.util.Enumeration;
import java.util.Vector;

public class ContactEntry extends Entry
{
  public static final String GENDER_FEMALE = "female";
  public static final String GENDER_MALE = "male";
  public static final byte TYPE_PRIORITY_HIGH = 1;
  public static final byte TYPE_PRIORITY_LOW = 3;
  public static final byte TYPE_PRIORITY_NORMAL = 2;
  public static final byte TYPE_SENSITIVITY_CONFIDENTIAL = 1;
  public static final byte TYPE_SENSITIVITY_NORMAL = 2;
  public static final byte TYPE_SENSITIVITY_PERSONAL = 3;
  public static final byte TYPE_SENSITIVITY_PRIVATE = 4;
  private String billingInformation;
  private String birthday;
  private final Vector calendarLinks = new Vector();
  private String directoryServer;
  private final Vector emailAddresses = new Vector();
  private final Vector events = new Vector();
  private final Vector extendedProperties = new Vector();
  private final Vector externalIds = new Vector();
  private String gender;
  private final Vector groups = new Vector();
  private final Vector hobbies = new Vector();
  private final Vector imAddresses = new Vector();
  private String initials;
  private final Vector jots = new Vector();
  private final Vector languages = new Vector();
  private String linkPhotoEtag;
  private String linkPhotoHref;
  private String linkPhotoType;
  private String maidenName;
  private String mileage;
  private Name name;
  private String nickname;
  private String occupation;
  private final Vector organizations = new Vector();
  private final Vector phoneNumbers = new Vector();
  private final Vector postalAddresses = new Vector();
  private byte priority = -1;
  private final Vector relations = new Vector();
  private byte sensitivity = -1;
  private String shortName;
  private String subject;
  private final Vector userDefinedFields = new Vector();
  private final Vector webSites = new Vector();

  public void addCalendarLink(CalendarLink paramCalendarLink)
  {
    this.calendarLinks.addElement(paramCalendarLink);
  }

  public void addEmailAddress(EmailAddress paramEmailAddress)
  {
    this.emailAddresses.addElement(paramEmailAddress);
  }

  public void addEvent(Event paramEvent)
  {
    this.events.addElement(paramEvent);
  }

  public void addExtendedProperty(ExtendedProperty paramExtendedProperty)
  {
    this.extendedProperties.addElement(paramExtendedProperty);
  }

  public void addExternalId(ExternalId paramExternalId)
  {
    this.externalIds.addElement(paramExternalId);
  }

  public void addGroup(GroupMembershipInfo paramGroupMembershipInfo)
  {
    this.groups.addElement(paramGroupMembershipInfo);
  }

  public void addHobby(String paramString)
  {
    this.hobbies.addElement(paramString);
  }

  public void addImAddress(ImAddress paramImAddress)
  {
    this.imAddresses.addElement(paramImAddress);
  }

  public void addJot(Jot paramJot)
  {
    this.jots.addElement(paramJot);
  }

  public void addLanguage(Language paramLanguage)
  {
    this.languages.addElement(paramLanguage);
  }

  public void addOrganization(Organization paramOrganization)
  {
    this.organizations.addElement(paramOrganization);
  }

  public void addPhoneNumber(PhoneNumber paramPhoneNumber)
  {
    this.phoneNumbers.addElement(paramPhoneNumber);
  }

  public void addPostalAddress(StructuredPostalAddress paramStructuredPostalAddress)
  {
    this.postalAddresses.addElement(paramStructuredPostalAddress);
  }

  public void addRelation(Relation paramRelation)
  {
    this.relations.addElement(paramRelation);
  }

  public void addUserDefinedField(UserDefinedField paramUserDefinedField)
  {
    this.userDefinedFields.addElement(paramUserDefinedField);
  }

  public void addWebSite(WebSite paramWebSite)
  {
    this.webSites.addElement(paramWebSite);
  }

  public void clear()
  {
    super.clear();
    this.linkPhotoHref = null;
    this.linkPhotoType = null;
    this.linkPhotoEtag = null;
    this.directoryServer = null;
    this.gender = null;
    this.initials = null;
    this.maidenName = null;
    this.mileage = null;
    this.nickname = null;
    this.occupation = null;
    this.priority = -1;
    this.sensitivity = -1;
    this.shortName = null;
    this.subject = null;
    this.birthday = null;
    this.billingInformation = null;
    this.name = null;
    this.emailAddresses.removeAllElements();
    this.imAddresses.removeAllElements();
    this.phoneNumbers.removeAllElements();
    this.postalAddresses.removeAllElements();
    this.organizations.removeAllElements();
    this.extendedProperties.removeAllElements();
    this.groups.removeAllElements();
    this.calendarLinks.removeAllElements();
    this.events.removeAllElements();
    this.externalIds.removeAllElements();
    this.hobbies.removeAllElements();
    this.jots.removeAllElements();
    this.languages.removeAllElements();
    this.relations.removeAllElements();
    this.userDefinedFields.removeAllElements();
    this.webSites.removeAllElements();
  }

  public String getBillingInformation()
  {
    return this.billingInformation;
  }

  public String getBirthday()
  {
    return this.birthday;
  }

  public Vector getCalendarLinks()
  {
    return this.calendarLinks;
  }

  public String getDirectoryServer()
  {
    return this.directoryServer;
  }

  public Vector getEmailAddresses()
  {
    return this.emailAddresses;
  }

  public Vector getEvents()
  {
    return this.events;
  }

  public Vector getExtendedProperties()
  {
    return this.extendedProperties;
  }

  public Vector getExternalIds()
  {
    return this.externalIds;
  }

  public String getGender()
  {
    return this.gender;
  }

  public Vector getGroups()
  {
    return this.groups;
  }

  public Vector getHobbies()
  {
    return this.hobbies;
  }

  public Vector getImAddresses()
  {
    return this.imAddresses;
  }

  public String getInitials()
  {
    return this.initials;
  }

  public Vector getJots()
  {
    return this.jots;
  }

  public Vector getLanguages()
  {
    return this.languages;
  }

  public String getLinkPhotoETag()
  {
    return this.linkPhotoEtag;
  }

  public String getLinkPhotoHref()
  {
    return this.linkPhotoHref;
  }

  public String getLinkPhotoType()
  {
    return this.linkPhotoType;
  }

  public String getMaidenName()
  {
    return this.maidenName;
  }

  public String getMileage()
  {
    return this.mileage;
  }

  public Name getName()
  {
    return this.name;
  }

  public String getNickname()
  {
    return this.nickname;
  }

  public String getOccupation()
  {
    return this.occupation;
  }

  public Vector getOrganizations()
  {
    return this.organizations;
  }

  public Vector getPhoneNumbers()
  {
    return this.phoneNumbers;
  }

  public Vector getPostalAddresses()
  {
    return this.postalAddresses;
  }

  public byte getPriority()
  {
    return this.priority;
  }

  public Vector getRelations()
  {
    return this.relations;
  }

  public byte getSensitivity()
  {
    return this.sensitivity;
  }

  public String getShortName()
  {
    return this.shortName;
  }

  public String getSubject()
  {
    return this.subject;
  }

  public Vector getUserDefinedFields()
  {
    return this.userDefinedFields;
  }

  public Vector getWebSites()
  {
    return this.webSites;
  }

  public void setBillingInformation(String paramString)
  {
    this.billingInformation = paramString;
  }

  public void setBirthday(String paramString)
  {
    this.birthday = paramString;
  }

  public void setDirectoryServer(String paramString)
  {
    this.directoryServer = paramString;
  }

  public void setGender(String paramString)
  {
    this.gender = paramString;
  }

  public void setInitials(String paramString)
  {
    this.initials = paramString;
  }

  public void setLinkPhoto(String paramString1, String paramString2, String paramString3)
  {
    this.linkPhotoHref = paramString1;
    this.linkPhotoType = paramString2;
    this.linkPhotoEtag = paramString3;
  }

  public void setMaidenName(String paramString)
  {
    this.maidenName = paramString;
  }

  public void setMileage(String paramString)
  {
    this.mileage = paramString;
  }

  public void setName(Name paramName)
  {
    this.name = paramName;
  }

  public void setNickname(String paramString)
  {
    this.nickname = paramString;
  }

  public void setOccupation(String paramString)
  {
    this.occupation = paramString;
  }

  public void setPriority(byte paramByte)
  {
    this.priority = paramByte;
  }

  public void setSensitivity(byte paramByte)
  {
    this.sensitivity = paramByte;
  }

  public void setShortName(String paramString)
  {
    this.shortName = paramString;
  }

  public void setSubject(String paramString)
  {
    this.subject = paramString;
  }

  protected void toString(StringBuffer paramStringBuffer)
  {
    super.toString(paramStringBuffer);
    paramStringBuffer.append("\n");
    paramStringBuffer.append("ContactEntry:");
    if (!StringUtils.isEmpty(this.linkPhotoHref))
      paramStringBuffer.append(" linkPhotoHref:").append(this.linkPhotoHref);
    if (!StringUtils.isEmpty(this.linkPhotoType))
      paramStringBuffer.append(" linkPhotoType:").append(this.linkPhotoType);
    if (!StringUtils.isEmpty(this.linkPhotoEtag))
      paramStringBuffer.append(" linkPhotoEtag:").append(this.linkPhotoEtag);
    if (!StringUtils.isEmpty(this.directoryServer))
      paramStringBuffer.append(" directoryServer:").append(this.directoryServer);
    if (!StringUtils.isEmpty(this.gender))
      paramStringBuffer.append(" gender:").append(this.gender);
    if (!StringUtils.isEmpty(this.initials))
      paramStringBuffer.append(" initials:").append(this.initials);
    if (!StringUtils.isEmpty(this.maidenName))
      paramStringBuffer.append(" maidenName:").append(this.maidenName);
    if (!StringUtils.isEmpty(this.mileage))
      paramStringBuffer.append(" mileage:").append(this.mileage);
    if (!StringUtils.isEmpty(this.nickname))
      paramStringBuffer.append(" nickname:").append(this.nickname);
    if (!StringUtils.isEmpty(this.occupation))
      paramStringBuffer.append(" occupaton:").append(this.occupation);
    paramStringBuffer.append(" priority:").append(this.priority);
    paramStringBuffer.append(" sensitivity:").append(this.sensitivity);
    if (!StringUtils.isEmpty(this.shortName))
      paramStringBuffer.append(" shortName:").append(this.shortName);
    if (!StringUtils.isEmpty(this.subject))
      paramStringBuffer.append(" subject:").append(this.subject);
    if (!StringUtils.isEmpty(this.birthday))
      paramStringBuffer.append(" birthday:").append(this.birthday);
    if (!StringUtils.isEmpty(this.billingInformation))
      paramStringBuffer.append(" billingInformation:").append(this.billingInformation);
    paramStringBuffer.append("\n");
    if (this.name != null)
    {
      this.name.toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration1 = this.emailAddresses.elements();
    while (localEnumeration1.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((EmailAddress)localEnumeration1.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration2 = this.imAddresses.elements();
    while (localEnumeration2.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((ImAddress)localEnumeration2.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration3 = this.postalAddresses.elements();
    while (localEnumeration3.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((StructuredPostalAddress)localEnumeration3.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration4 = this.phoneNumbers.elements();
    while (localEnumeration4.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((PhoneNumber)localEnumeration4.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration5 = this.organizations.elements();
    while (localEnumeration5.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((Organization)localEnumeration5.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration6 = this.extendedProperties.elements();
    while (localEnumeration6.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((ExtendedProperty)localEnumeration6.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration7 = this.groups.elements();
    while (localEnumeration7.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((GroupMembershipInfo)localEnumeration7.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration8 = this.calendarLinks.elements();
    while (localEnumeration8.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((CalendarLink)localEnumeration8.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration9 = this.events.elements();
    while (localEnumeration9.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((Event)localEnumeration9.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration10 = this.externalIds.elements();
    while (localEnumeration10.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((ExternalId)localEnumeration10.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration11 = this.hobbies.elements();
    while (localEnumeration11.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      paramStringBuffer.append((String)localEnumeration11.nextElement());
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration12 = this.jots.elements();
    while (localEnumeration12.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      paramStringBuffer.append((Jot)localEnumeration12.nextElement());
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration13 = this.languages.elements();
    while (localEnumeration13.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((Language)localEnumeration13.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration14 = this.relations.elements();
    while (localEnumeration14.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((Relation)localEnumeration14.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration15 = this.userDefinedFields.elements();
    while (localEnumeration15.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((UserDefinedField)localEnumeration15.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
    Enumeration localEnumeration16 = this.webSites.elements();
    while (localEnumeration16.hasMoreElements())
    {
      paramStringBuffer.append("  ");
      ((WebSite)localEnumeration16.nextElement()).toString(paramStringBuffer);
      paramStringBuffer.append("\n");
    }
  }

  public void validate()
    throws ParseException
  {
    super.validate();
    if ((this.gender != null) && (!"female".equals(this.gender)) && (!"male".equals(this.gender)))
    {
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = this.gender;
      arrayOfObject[1] = "female";
      arrayOfObject[2] = "male";
      throw new ParseException(String.format("invalid gender \"%s\", must be one of \"%s\" or \"%s\"", arrayOfObject));
    }
    Enumeration localEnumeration1 = this.emailAddresses.elements();
    while (localEnumeration1.hasMoreElements())
      ((EmailAddress)localEnumeration1.nextElement()).validate();
    Enumeration localEnumeration2 = this.imAddresses.elements();
    while (localEnumeration2.hasMoreElements())
      ((ImAddress)localEnumeration2.nextElement()).validate();
    Enumeration localEnumeration3 = this.postalAddresses.elements();
    while (localEnumeration3.hasMoreElements())
      ((StructuredPostalAddress)localEnumeration3.nextElement()).validate();
    Enumeration localEnumeration4 = this.phoneNumbers.elements();
    while (localEnumeration4.hasMoreElements())
      ((PhoneNumber)localEnumeration4.nextElement()).validate();
    Enumeration localEnumeration5 = this.organizations.elements();
    while (localEnumeration5.hasMoreElements())
      ((Organization)localEnumeration5.nextElement()).validate();
    Enumeration localEnumeration6 = this.extendedProperties.elements();
    while (localEnumeration6.hasMoreElements())
      ((ExtendedProperty)localEnumeration6.nextElement()).validate();
    Enumeration localEnumeration7 = this.groups.elements();
    while (localEnumeration7.hasMoreElements())
      ((GroupMembershipInfo)localEnumeration7.nextElement()).validate();
    Enumeration localEnumeration8 = this.calendarLinks.elements();
    while (localEnumeration8.hasMoreElements())
      ((CalendarLink)localEnumeration8.nextElement()).validate();
    Enumeration localEnumeration9 = this.events.elements();
    while (localEnumeration9.hasMoreElements())
      ((Event)localEnumeration9.nextElement()).validate();
    Enumeration localEnumeration10 = this.externalIds.elements();
    while (localEnumeration10.hasMoreElements())
      ((ExternalId)localEnumeration10.nextElement()).validate();
    Enumeration localEnumeration11 = this.languages.elements();
    while (localEnumeration11.hasMoreElements())
      ((Language)localEnumeration11.nextElement()).validate();
    Enumeration localEnumeration12 = this.relations.elements();
    while (localEnumeration12.hasMoreElements())
      ((Relation)localEnumeration12.nextElement()).validate();
    Enumeration localEnumeration13 = this.userDefinedFields.elements();
    while (localEnumeration13.hasMoreElements())
      ((UserDefinedField)localEnumeration13.nextElement()).validate();
    Enumeration localEnumeration14 = this.webSites.elements();
    while (localEnumeration14.hasMoreElements())
      ((WebSite)localEnumeration14.nextElement()).validate();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.ContactEntry
 * JD-Core Version:    0.6.2
 */