package com.google.wireless.gdata.calendar.serializer.xml;

import com.google.wireless.gdata.calendar.data.EventEntry;
import com.google.wireless.gdata.calendar.data.Reminder;
import com.google.wireless.gdata.calendar.data.When;
import com.google.wireless.gdata.calendar.data.Who;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlParserFactory;
import com.google.wireless.gdata.serializer.xml.XmlEntryGDataSerializer;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.xmlpull.v1.XmlSerializer;

public class XmlEventEntryGDataSerializer extends XmlEntryGDataSerializer
{
  public static final String NAMESPACE_GCAL = "gCal";
  public static final String NAMESPACE_GCAL_URI = "http://schemas.google.com/gCal/2005";

  public XmlEventEntryGDataSerializer(XmlParserFactory paramXmlParserFactory, EventEntry paramEventEntry)
  {
    super(paramXmlParserFactory, paramEventEntry);
  }

  private static void serializeCommentsUri(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (paramString == null)
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "feedLink");
    paramXmlSerializer.attribute(null, "href", paramString);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "feedLink");
  }

  private static void serializeEventStatus(XmlSerializer paramXmlSerializer, byte paramByte)
    throws IOException
  {
    String str;
    switch (paramByte)
    {
    default:
      str = "http://schemas.google.com/g/2005#event.tentative";
    case 0:
    case 2:
    case 1:
    }
    while (true)
    {
      paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "eventStatus");
      paramXmlSerializer.attribute(null, "value", str);
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "eventStatus");
      return;
      str = "http://schemas.google.com/g/2005#event.tentative";
      continue;
      str = "http://schemas.google.com/g/2005#event.canceled";
      continue;
      str = "http://schemas.google.com/g/2005#event.confirmed";
    }
  }

  private static void serializeExtendedProperty(XmlSerializer paramXmlSerializer, String paramString1, String paramString2)
    throws IOException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "extendedProperty");
    paramXmlSerializer.attribute(null, "name", paramString1);
    paramXmlSerializer.attribute(null, "value", paramString2);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "extendedProperty");
  }

  private static void serializeGuestsCanInviteOthers(XmlSerializer paramXmlSerializer, boolean paramBoolean)
    throws IOException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/gCal/2005", "guestsCanInviteOthers");
    if (paramBoolean);
    for (String str = "true"; ; str = "false")
    {
      paramXmlSerializer.attribute(null, "value", str);
      paramXmlSerializer.endTag("http://schemas.google.com/gCal/2005", "guestsCanInviteOthers");
      return;
    }
  }

  private static void serializeGuestsCanModify(XmlSerializer paramXmlSerializer, boolean paramBoolean)
    throws IOException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/gCal/2005", "guestsCanModify");
    if (paramBoolean);
    for (String str = "true"; ; str = "false")
    {
      paramXmlSerializer.attribute(null, "value", str);
      paramXmlSerializer.endTag("http://schemas.google.com/gCal/2005", "guestsCanModify");
      return;
    }
  }

  private static void serializeGuestsCanSeeGuests(XmlSerializer paramXmlSerializer, boolean paramBoolean)
    throws IOException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/gCal/2005", "guestsCanSeeGuests");
    if (paramBoolean);
    for (String str = "true"; ; str = "false")
    {
      paramXmlSerializer.attribute(null, "value", str);
      paramXmlSerializer.endTag("http://schemas.google.com/gCal/2005", "guestsCanSeeGuests");
      return;
    }
  }

  private static void serializeOriginalEvent(XmlSerializer paramXmlSerializer, String paramString1, String paramString2)
    throws IOException
  {
    if ((StringUtils.isEmpty(paramString1)) || (StringUtils.isEmpty(paramString2)))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "originalEvent");
    int i = paramString1.lastIndexOf("/");
    if (i != -1)
    {
      String str = paramString1.substring(i + 1);
      if (!StringUtils.isEmpty(str))
        paramXmlSerializer.attribute(null, "id", str);
    }
    paramXmlSerializer.attribute(null, "href", paramString1);
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "when");
    paramXmlSerializer.attribute(null, "startTime", paramString2);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "when");
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "originalEvent");
  }

  private static void serializeQuickAdd(XmlSerializer paramXmlSerializer, boolean paramBoolean)
    throws IOException
  {
    if (paramBoolean)
    {
      paramXmlSerializer.startTag("gCal", "quickadd");
      paramXmlSerializer.attribute(null, "value", "true");
      paramXmlSerializer.endTag("gCal", "quickadd");
    }
  }

  private static void serializeRecurrence(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (StringUtils.isEmpty(paramString))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "recurrence");
    paramXmlSerializer.text(paramString);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "recurrence");
  }

  private static void serializeReminder(XmlSerializer paramXmlSerializer, Reminder paramReminder)
    throws IOException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "reminder");
    int i = paramReminder.getMethod();
    String str = null;
    switch (i)
    {
    default:
    case 3:
    case 1:
    case 2:
    }
    while (true)
    {
      if (str != null)
        paramXmlSerializer.attribute(null, "method", str);
      int j = paramReminder.getMinutes();
      if (j != -1)
        paramXmlSerializer.attribute(null, "minutes", Integer.toString(j));
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "reminder");
      return;
      str = "alert";
      continue;
      str = "email";
      continue;
      str = "sms";
    }
  }

  private static void serializeSendEventNotifications(XmlSerializer paramXmlSerializer, boolean paramBoolean)
    throws IOException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/gCal/2005", "sendEventNotifications");
    if (paramBoolean);
    for (String str = "true"; ; str = "false")
    {
      paramXmlSerializer.attribute(null, "value", str);
      paramXmlSerializer.endTag("http://schemas.google.com/gCal/2005", "sendEventNotifications");
      return;
    }
  }

  private static void serializeTransparency(XmlSerializer paramXmlSerializer, byte paramByte)
    throws IOException
  {
    String str;
    switch (paramByte)
    {
    default:
      str = "http://schemas.google.com/g/2005#event.transparent";
    case 0:
    case 1:
    }
    while (true)
    {
      paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "transparency");
      paramXmlSerializer.attribute(null, "value", str);
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "transparency");
      return;
      str = "http://schemas.google.com/g/2005#event.opaque";
      continue;
      str = "http://schemas.google.com/g/2005#event.transparent";
    }
  }

  private static void serializeVisibility(XmlSerializer paramXmlSerializer, byte paramByte)
    throws IOException
  {
    String str;
    switch (paramByte)
    {
    default:
      str = "http://schemas.google.com/g/2005#event.default";
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "visibility");
      paramXmlSerializer.attribute(null, "value", str);
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "visibility");
      return;
      str = "http://schemas.google.com/g/2005#event.default";
      continue;
      str = "http://schemas.google.com/g/2005#event.confidential";
      continue;
      str = "http://schemas.google.com/g/2005#event.private";
      continue;
      str = "http://schemas.google.com/g/2005#event.public";
    }
  }

  private static void serializeWhen(XmlSerializer paramXmlSerializer, EventEntry paramEventEntry, When paramWhen)
    throws IOException
  {
    String str1 = paramWhen.getStartTime();
    String str2 = paramWhen.getEndTime();
    if (StringUtils.isEmpty(paramWhen.getStartTime()))
      return;
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "when");
    paramXmlSerializer.attribute(null, "startTime", str1);
    if (!StringUtils.isEmpty(str2))
      paramXmlSerializer.attribute(null, "endTime", str2);
    if (paramEventEntry.getReminders() != null)
    {
      Enumeration localEnumeration = paramEventEntry.getReminders().elements();
      while (localEnumeration.hasMoreElements())
        serializeReminder(paramXmlSerializer, (Reminder)localEnumeration.nextElement());
    }
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "when");
  }

  private static void serializeWhere(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    if (paramString == null)
      paramString = "";
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "where");
    paramXmlSerializer.attribute(null, "valueString", paramString);
    paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "where");
  }

  private static void serializeWho(XmlSerializer paramXmlSerializer, EventEntry paramEventEntry, Who paramWho)
    throws IOException, ParseException
  {
    paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "who");
    String str1 = paramWho.getEmail();
    if (!StringUtils.isEmpty(str1))
      paramXmlSerializer.attribute(null, "email", str1);
    String str2 = paramWho.getValue();
    if (!StringUtils.isEmpty(str2))
      paramXmlSerializer.attribute(null, "valueString", str2);
    int i = paramWho.getRelationship();
    String str3 = null;
    switch (i)
    {
    default:
      throw new ParseException("Unexpected rel: " + paramWho.getRelationship());
    case 1:
      str3 = "http://schemas.google.com/g/2005#event.attendee";
    case 0:
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      if (!StringUtils.isEmpty(str3))
        paramXmlSerializer.attribute(null, "rel", str3);
      int j = paramWho.getStatus();
      str4 = null;
      switch (j)
      {
      default:
        throw new ParseException("Unexpected status: " + paramWho.getStatus());
        str3 = "http://schemas.google.com/g/2005#event.organizer";
        continue;
        str3 = "http://schemas.google.com/g/2005#event.performer";
        continue;
        str3 = "http://schemas.google.com/g/2005#event.speaker";
      case 1:
      case 0:
      case 2:
      case 3:
      case 4:
      }
    }
    String str4 = "http://schemas.google.com/g/2005#event.accepted";
    while (true)
    {
      if (!StringUtils.isEmpty(str4))
      {
        paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "attendeeStatus");
        paramXmlSerializer.attribute(null, "value", str4);
        paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "attendeeStatus");
      }
      int k = paramWho.getType();
      str5 = null;
      switch (k)
      {
      default:
        throw new ParseException("Unexpected type: " + paramWho.getType());
        str4 = "http://schemas.google.com/g/2005#event.declined";
        continue;
        str4 = "http://schemas.google.com/g/2005#event.invited";
        continue;
        str4 = "http://schemas.google.com/g/2005#event.tentative";
      case 2:
      case 0:
      case 1:
      }
    }
    for (String str5 = "http://schemas.google.com/g/2005#event.required"; ; str5 = "http://schemas.google.com/g/2005#event.optional")
    {
      if (!StringUtils.isEmpty(str5))
      {
        paramXmlSerializer.startTag("http://schemas.google.com/g/2005", "attendeeType");
        paramXmlSerializer.attribute(null, "value", str5);
        paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "attendeeType");
      }
      paramXmlSerializer.endTag("http://schemas.google.com/g/2005", "who");
      return;
    }
  }

  protected void declareExtraEntryNamespaces(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.setPrefix("gCal", "http://schemas.google.com/gCal/2005");
  }

  protected EventEntry getEventEntry()
  {
    return (EventEntry)getEntry();
  }

  protected void serializeExtraEntryContents(XmlSerializer paramXmlSerializer, int paramInt)
    throws IOException, ParseException
  {
    EventEntry localEventEntry = getEventEntry();
    serializeEventStatus(paramXmlSerializer, localEventEntry.getStatus());
    serializeTransparency(paramXmlSerializer, localEventEntry.getTransparency());
    serializeVisibility(paramXmlSerializer, localEventEntry.getVisibility());
    if (localEventEntry.getSendEventNotifications())
    {
      paramXmlSerializer.startTag("http://schemas.google.com/gCal/2005", "sendEventNotifications");
      paramXmlSerializer.attribute(null, "value", "true");
      paramXmlSerializer.endTag("http://schemas.google.com/gCal/2005", "sendEventNotifications");
    }
    Enumeration localEnumeration1 = localEventEntry.getAttendees().elements();
    while (localEnumeration1.hasMoreElements())
      serializeWho(paramXmlSerializer, localEventEntry, (Who)localEnumeration1.nextElement());
    serializeRecurrence(paramXmlSerializer, localEventEntry.getRecurrence());
    if (localEventEntry.getRecurrence() != null)
    {
      if (localEventEntry.getReminders() != null)
      {
        Enumeration localEnumeration4 = localEventEntry.getReminders().elements();
        while (localEnumeration4.hasMoreElements())
          serializeReminder(paramXmlSerializer, (Reminder)localEnumeration4.nextElement());
      }
    }
    else
    {
      Enumeration localEnumeration2 = localEventEntry.getWhens().elements();
      while (localEnumeration2.hasMoreElements())
        serializeWhen(paramXmlSerializer, localEventEntry, (When)localEnumeration2.nextElement());
    }
    serializeOriginalEvent(paramXmlSerializer, localEventEntry.getOriginalEventId(), localEventEntry.getOriginalEventStartTime());
    serializeWhere(paramXmlSerializer, localEventEntry.getWhere());
    serializeCommentsUri(paramXmlSerializer, localEventEntry.getCommentsUri());
    Hashtable localHashtable = localEventEntry.getExtendedProperties();
    if (localHashtable != null)
    {
      Enumeration localEnumeration3 = localHashtable.keys();
      while (localEnumeration3.hasMoreElements())
      {
        String str = (String)localEnumeration3.nextElement();
        serializeExtendedProperty(paramXmlSerializer, str, (String)localHashtable.get(str));
      }
    }
    serializeQuickAdd(paramXmlSerializer, localEventEntry.isQuickAdd());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.calendar.serializer.xml.XmlEventEntryGDataSerializer
 * JD-Core Version:    0.6.2
 */