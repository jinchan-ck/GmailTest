package com.google.wireless.gdata2.calendar.data;

import com.google.wireless.gdata2.data.Entry;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class EventEntry extends Entry
{
  public static final byte STATUS_CANCELED = 2;
  public static final byte STATUS_CONFIRMED = 1;
  public static final byte STATUS_TENTATIVE = 0;
  public static final byte TRANSPARENCY_OPAQUE = 0;
  public static final byte TRANSPARENCY_TRANSPARENT = 1;
  public static final byte VISIBILITY_CONFIDENTIAL = 1;
  public static final byte VISIBILITY_DEFAULT = 0;
  public static final byte VISIBILITY_PRIVATE = 2;
  public static final byte VISIBILITY_PUBLIC = 3;
  private Vector attendees = new Vector();
  private String calendarUrl = null;
  private String commentsUri = null;
  private Hashtable extendedProperties = null;
  private boolean guestsCanInviteOthers = true;
  private boolean guestsCanModify = false;
  private boolean guestsCanSeeGuests = true;
  private String organizer = null;
  private String originalEventId = null;
  private String originalEventStartTime = null;
  private boolean quickAdd = false;
  private String recurrence = null;
  private Vector reminders = null;
  private boolean sendEventNotifications = false;
  private byte status = 0;
  private byte transparency = 0;
  private byte visibility = 0;
  private Vector whens = new Vector();
  private String where = null;

  public void addAttendee(Who paramWho)
  {
    this.attendees.addElement(paramWho);
  }

  public void addExtendedProperty(String paramString1, String paramString2)
  {
    if (this.extendedProperties == null)
      this.extendedProperties = new Hashtable();
    this.extendedProperties.put(paramString1, paramString2);
  }

  public void addReminder(Reminder paramReminder)
  {
    if (this.reminders == null)
      this.reminders = new Vector();
    this.reminders.addElement(paramReminder);
  }

  public void addWhen(When paramWhen)
  {
    this.whens.addElement(paramWhen);
  }

  public void clear()
  {
    super.clear();
    this.status = 0;
    this.recurrence = null;
    this.visibility = 0;
    this.transparency = 0;
    this.sendEventNotifications = false;
    this.guestsCanModify = false;
    this.guestsCanInviteOthers = true;
    this.guestsCanSeeGuests = true;
    this.organizer = null;
    this.attendees.removeAllElements();
    this.whens.removeAllElements();
    this.reminders = null;
    this.originalEventId = null;
    this.originalEventStartTime = null;
    this.where = null;
    this.commentsUri = null;
    this.extendedProperties = null;
    this.quickAdd = false;
    this.calendarUrl = null;
  }

  public void clearAttendees()
  {
    this.attendees.removeAllElements();
  }

  public void clearExtendedProperties()
  {
    this.extendedProperties = null;
    this.quickAdd = false;
  }

  public void clearReminders()
  {
    this.reminders = null;
  }

  public void clearWhens()
  {
    this.whens.removeAllElements();
  }

  public Vector getAttendees()
  {
    return this.attendees;
  }

  public String getCalendarUrl()
  {
    return this.calendarUrl;
  }

  public String getCommentsUri()
  {
    return this.commentsUri;
  }

  public Hashtable getExtendedProperties()
  {
    return this.extendedProperties;
  }

  public String getExtendedProperty(String paramString)
  {
    if (this.extendedProperties == null)
      return null;
    boolean bool = this.extendedProperties.containsKey(paramString);
    String str = null;
    if (bool)
      str = (String)this.extendedProperties.get(paramString);
    return str;
  }

  public When getFirstWhen()
  {
    if (this.whens.isEmpty())
      return null;
    return (When)this.whens.elementAt(0);
  }

  public boolean getGuestsCanInviteOthers()
  {
    return this.guestsCanInviteOthers;
  }

  public boolean getGuestsCanModify()
  {
    return this.guestsCanModify;
  }

  public boolean getGuestsCanSeeGuests()
  {
    return this.guestsCanSeeGuests;
  }

  public String getOrganizer()
  {
    return this.organizer;
  }

  public String getOriginalEventId()
  {
    return this.originalEventId;
  }

  public String getOriginalEventStartTime()
  {
    return this.originalEventStartTime;
  }

  public String getRecurrence()
  {
    return this.recurrence;
  }

  public Vector getReminders()
  {
    return this.reminders;
  }

  public boolean getSendEventNotifications()
  {
    return this.sendEventNotifications;
  }

  public byte getStatus()
  {
    return this.status;
  }

  public byte getTransparency()
  {
    return this.transparency;
  }

  public byte getVisibility()
  {
    return this.visibility;
  }

  public Vector getWhens()
  {
    return this.whens;
  }

  public String getWhere()
  {
    return this.where;
  }

  public boolean isQuickAdd()
  {
    return this.quickAdd;
  }

  public void setCalendarUrl(String paramString)
  {
    this.calendarUrl = paramString;
  }

  public void setCommentsUri(String paramString)
  {
    this.commentsUri = paramString;
  }

  public void setGuestsCanInviteOthers(boolean paramBoolean)
  {
    this.guestsCanInviteOthers = paramBoolean;
  }

  public void setGuestsCanModify(boolean paramBoolean)
  {
    this.guestsCanModify = paramBoolean;
  }

  public void setGuestsCanSeeGuests(boolean paramBoolean)
  {
    this.guestsCanSeeGuests = paramBoolean;
  }

  public void setOrganizer(String paramString)
  {
    this.organizer = paramString;
  }

  public void setOriginalEventId(String paramString)
  {
    this.originalEventId = paramString;
  }

  public void setOriginalEventStartTime(String paramString)
  {
    this.originalEventStartTime = paramString;
  }

  public void setQuickAdd(boolean paramBoolean)
  {
    this.quickAdd = paramBoolean;
  }

  public void setRecurrence(String paramString)
  {
    this.recurrence = paramString;
  }

  public void setSendEventNotifications(boolean paramBoolean)
  {
    this.sendEventNotifications = paramBoolean;
  }

  public void setStatus(byte paramByte)
  {
    this.status = paramByte;
  }

  public void setTransparency(byte paramByte)
  {
    this.transparency = paramByte;
  }

  public void setVisibility(byte paramByte)
  {
    this.visibility = paramByte;
  }

  public void setWhere(String paramString)
  {
    this.where = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    super.toString(paramStringBuffer);
    paramStringBuffer.append("STATUS: " + this.status + "\n");
    appendIfNotNull(paramStringBuffer, "RECURRENCE", this.recurrence);
    paramStringBuffer.append("VISIBILITY: " + this.visibility + "\n");
    paramStringBuffer.append("TRANSPARENCY: " + this.transparency + "\n");
    appendIfNotNull(paramStringBuffer, "ORIGINAL_EVENT_ID", this.originalEventId);
    appendIfNotNull(paramStringBuffer, "ORIGINAL_START_TIME", this.originalEventStartTime);
    StringBuilder localStringBuilder1 = new StringBuilder().append("QUICK_ADD: ");
    String str1;
    String str2;
    label204: String str3;
    label249: String str4;
    label294: StringBuilder localStringBuilder5;
    if (this.quickAdd)
    {
      str1 = "true";
      paramStringBuffer.append(str1 + "\n");
      StringBuilder localStringBuilder2 = new StringBuilder().append("SEND_EVENT_NOTIFICATIONS: ");
      if (!this.sendEventNotifications)
        break label413;
      str2 = "true";
      paramStringBuffer.append(str2 + "\n");
      StringBuilder localStringBuilder3 = new StringBuilder().append("GUESTS_CAN_MODIFY: ");
      if (!this.guestsCanModify)
        break label420;
      str3 = "true";
      paramStringBuffer.append(str3 + "\n");
      StringBuilder localStringBuilder4 = new StringBuilder().append("GUESTS_CAN_INVITE_OTHERS: ");
      if (!this.guestsCanInviteOthers)
        break label427;
      str4 = "true";
      paramStringBuffer.append(str4 + "\n");
      localStringBuilder5 = new StringBuilder().append("GUESTS_CAN_SEE_GUESTS: ");
      if (!this.guestsCanSeeGuests)
        break label434;
    }
    label413: label420: label427: label434: for (String str5 = "true"; ; str5 = "false")
    {
      paramStringBuffer.append(str5 + "\n");
      appendIfNotNull(paramStringBuffer, "ORGANIZER", this.organizer);
      Enumeration localEnumeration1 = this.attendees.elements();
      while (localEnumeration1.hasMoreElements())
        ((Who)localEnumeration1.nextElement()).toString(paramStringBuffer);
      str1 = "false";
      break;
      str2 = "false";
      break label204;
      str3 = "false";
      break label249;
      str4 = "false";
      break label294;
    }
    Enumeration localEnumeration2 = this.whens.elements();
    while (localEnumeration2.hasMoreElements())
      ((When)localEnumeration2.nextElement()).toString(paramStringBuffer);
    if (this.reminders != null)
    {
      Enumeration localEnumeration4 = this.reminders.elements();
      while (localEnumeration4.hasMoreElements())
        ((Reminder)localEnumeration4.nextElement()).toString(paramStringBuffer);
    }
    appendIfNotNull(paramStringBuffer, "WHERE", this.where);
    appendIfNotNull(paramStringBuffer, "COMMENTS", this.commentsUri);
    if (this.extendedProperties != null)
    {
      Enumeration localEnumeration3 = this.extendedProperties.keys();
      while (localEnumeration3.hasMoreElements())
      {
        String str6 = (String)localEnumeration3.nextElement();
        String str7 = (String)this.extendedProperties.get(str6);
        paramStringBuffer.append(str6);
        paramStringBuffer.append(':');
        paramStringBuffer.append(str7);
        paramStringBuffer.append('\n');
      }
    }
    appendIfNotNull(paramStringBuffer, "CALENDAR_URL", this.calendarUrl);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.calendar.data.EventEntry
 * JD-Core Version:    0.6.2
 */