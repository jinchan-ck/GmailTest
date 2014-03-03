package com.google.wireless.gdata.contacts.data;

import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.ExtendedProperty;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.parser.ParseException;
import java.util.Enumeration;
import java.util.Vector;

public class ContactEntry extends Entry
{
  private final Vector emailAddresses = new Vector();
  private final Vector extendedProperties = new Vector();
  private final Vector groups = new Vector();
  private final Vector imAddresses = new Vector();
  private String linkEditPhotoHref;
  private String linkEditPhotoType;
  private String linkPhotoHref;
  private String linkPhotoType;
  private final Vector organizations = new Vector();
  private final Vector phoneNumbers = new Vector();
  private final Vector postalAddresses = new Vector();
  private String yomiName;

  public void addEmailAddress(EmailAddress paramEmailAddress)
  {
    this.emailAddresses.addElement(paramEmailAddress);
  }

  public void addExtendedProperty(ExtendedProperty paramExtendedProperty)
  {
    this.extendedProperties.addElement(paramExtendedProperty);
  }

  public void addGroup(GroupMembershipInfo paramGroupMembershipInfo)
  {
    this.groups.addElement(paramGroupMembershipInfo);
  }

  public void addImAddress(ImAddress paramImAddress)
  {
    this.imAddresses.addElement(paramImAddress);
  }

  public void addOrganization(Organization paramOrganization)
  {
    this.organizations.addElement(paramOrganization);
  }

  public void addPhoneNumber(PhoneNumber paramPhoneNumber)
  {
    this.phoneNumbers.addElement(paramPhoneNumber);
  }

  public void addPostalAddress(PostalAddress paramPostalAddress)
  {
    this.postalAddresses.addElement(paramPostalAddress);
  }

  public void clear()
  {
    super.clear();
    this.linkEditPhotoHref = null;
    this.linkEditPhotoType = null;
    this.linkPhotoHref = null;
    this.linkPhotoType = null;
    this.emailAddresses.removeAllElements();
    this.imAddresses.removeAllElements();
    this.phoneNumbers.removeAllElements();
    this.postalAddresses.removeAllElements();
    this.organizations.removeAllElements();
    this.extendedProperties.removeAllElements();
    this.groups.removeAllElements();
    this.yomiName = null;
  }

  public Vector getEmailAddresses()
  {
    return this.emailAddresses;
  }

  public Vector getExtendedProperties()
  {
    return this.extendedProperties;
  }

  public Vector getGroups()
  {
    return this.groups;
  }

  public Vector getImAddresses()
  {
    return this.imAddresses;
  }

  public String getLinkEditPhotoHref()
  {
    return this.linkEditPhotoHref;
  }

  public String getLinkEditPhotoType()
  {
    return this.linkEditPhotoType;
  }

  public String getLinkPhotoHref()
  {
    return this.linkPhotoHref;
  }

  public String getLinkPhotoType()
  {
    return this.linkPhotoType;
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

  public String getYomiName()
  {
    return this.yomiName;
  }

  public void setLinkEditPhoto(String paramString1, String paramString2)
  {
    this.linkEditPhotoHref = paramString1;
    this.linkEditPhotoType = paramString2;
  }

  public void setLinkPhoto(String paramString1, String paramString2)
  {
    this.linkPhotoHref = paramString1;
    this.linkPhotoType = paramString2;
  }

  public void setYomiName(String paramString)
  {
    this.yomiName = paramString;
  }

  protected void toString(StringBuffer paramStringBuffer)
  {
    super.toString(paramStringBuffer);
    paramStringBuffer.append("\n");
    paramStringBuffer.append("ContactEntry:");
    if (!StringUtils.isEmpty(this.linkPhotoHref))
      paramStringBuffer.append(" linkPhotoHref:").append(this.linkPhotoHref).append("\n");
    if (!StringUtils.isEmpty(this.linkPhotoType))
      paramStringBuffer.append(" linkPhotoType:").append(this.linkPhotoType).append("\n");
    if (!StringUtils.isEmpty(this.linkEditPhotoHref))
      paramStringBuffer.append(" linkEditPhotoHref:").append(this.linkEditPhotoHref).append("\n");
    if (!StringUtils.isEmpty(this.linkEditPhotoType))
      paramStringBuffer.append(" linkEditPhotoType:").append(this.linkEditPhotoType).append("\n");
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
      ((PostalAddress)localEnumeration3.nextElement()).toString(paramStringBuffer);
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
    if (!StringUtils.isEmpty(this.yomiName))
      paramStringBuffer.append(" yomiName:").append(this.yomiName).append("\n");
  }

  public void validate()
    throws ParseException
  {
    super.validate();
    Enumeration localEnumeration1 = this.emailAddresses.elements();
    while (localEnumeration1.hasMoreElements())
      ((EmailAddress)localEnumeration1.nextElement()).validate();
    Enumeration localEnumeration2 = this.imAddresses.elements();
    while (localEnumeration2.hasMoreElements())
      ((ImAddress)localEnumeration2.nextElement()).validate();
    Enumeration localEnumeration3 = this.postalAddresses.elements();
    while (localEnumeration3.hasMoreElements())
      ((PostalAddress)localEnumeration3.nextElement()).validate();
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
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.data.ContactEntry
 * JD-Core Version:    0.6.2
 */