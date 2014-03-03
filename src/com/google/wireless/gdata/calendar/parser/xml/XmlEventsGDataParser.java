package com.google.wireless.gdata.calendar.parser.xml;

import com.google.wireless.gdata.calendar.data.EventEntry;
import com.google.wireless.gdata.calendar.data.EventsFeed;
import com.google.wireless.gdata.calendar.data.Reminder;
import com.google.wireless.gdata.calendar.data.When;
import com.google.wireless.gdata.calendar.data.Who;
import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.Feed;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.data.XmlUtils;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.parser.xml.XmlGDataParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlEventsGDataParser extends XmlGDataParser
{
  private boolean hasSeenReminder = false;

  public XmlEventsGDataParser(InputStream paramInputStream, XmlPullParser paramXmlPullParser)
    throws ParseException
  {
    super(paramInputStream, paramXmlPullParser);
  }

  private void handleOriginalEvent(EventEntry paramEventEntry)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = getParser();
    int i = localXmlPullParser.getEventType();
    String str = localXmlPullParser.getName();
    if ((i != 2) || (!"originalEvent".equals(localXmlPullParser.getName())))
      throw new IllegalStateException("Expected <originalEvent>: Actual element: <" + str + ">");
    paramEventEntry.setOriginalEventId(localXmlPullParser.getAttributeValue(null, "href"));
    int j = localXmlPullParser.next();
    if (j != 1)
    {
      switch (j)
      {
      default:
      case 2:
      case 3:
      }
      do
        while (true)
        {
          j = localXmlPullParser.next();
          break;
          if ("when".equals(localXmlPullParser.getName()))
            paramEventEntry.setOriginalEventStartTime(localXmlPullParser.getAttributeValue(null, "startTime"));
        }
      while (!"originalEvent".equals(localXmlPullParser.getName()));
    }
  }

  private void handleReminder(EventEntry paramEventEntry)
  {
    XmlPullParser localXmlPullParser = getParser();
    Reminder localReminder = new Reminder();
    paramEventEntry.addReminder(localReminder);
    String str1 = localXmlPullParser.getAttributeValue(null, "method");
    String str2 = localXmlPullParser.getAttributeValue(null, "minutes");
    String str3 = localXmlPullParser.getAttributeValue(null, "hours");
    String str4 = localXmlPullParser.getAttributeValue(null, "days");
    int i;
    if (!StringUtils.isEmpty(str1))
    {
      if ("alert".equals(str1))
        localReminder.setMethod((byte)3);
    }
    else
    {
      i = -1;
      if (StringUtils.isEmpty(str2))
        break label156;
      i = StringUtils.parseInt(str2, i);
    }
    while (true)
    {
      if (i < 0)
        i = -1;
      localReminder.setMinutes(i);
      return;
      if ("email".equals(str1))
      {
        localReminder.setMethod((byte)1);
        break;
      }
      if (!"sms".equals(str1))
        break;
      localReminder.setMethod((byte)2);
      break;
      label156: if (!StringUtils.isEmpty(str3))
        i = 60 * StringUtils.parseInt(str3, i);
      else if (!StringUtils.isEmpty(str4))
        i = 1440 * StringUtils.parseInt(str4, i);
    }
  }

  private void handleWhen(EventEntry paramEventEntry)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = getParser();
    int i = localXmlPullParser.getEventType();
    String str = localXmlPullParser.getName();
    if ((i != 2) || (!"when".equals(localXmlPullParser.getName())))
      throw new IllegalStateException("Expected <when>: Actual element: <" + str + ">");
    paramEventEntry.addWhen(new When(localXmlPullParser.getAttributeValue(null, "startTime"), localXmlPullParser.getAttributeValue(null, "endTime")));
    int j;
    int k;
    label130: int m;
    if (paramEventEntry.getWhens().size() == 1)
    {
      j = 1;
      if ((j == 0) || (this.hasSeenReminder))
        break label185;
      k = 1;
      m = localXmlPullParser.next();
      label138: if (m == 1)
        return;
      switch (m)
      {
      default:
      case 2:
      case 3:
      }
    }
    label185: 
    do
      while (true)
      {
        m = localXmlPullParser.next();
        break label138;
        j = 0;
        break;
        k = 0;
        break label130;
        if (("reminder".equals(localXmlPullParser.getName())) && (k != 0))
          handleReminder(paramEventEntry);
      }
    while (!"when".equals(localXmlPullParser.getName()));
  }

  private void handleWho(EventEntry paramEventEntry)
    throws XmlPullParserException, IOException, ParseException
  {
    XmlPullParser localXmlPullParser = getParser();
    int i = localXmlPullParser.getEventType();
    String str1 = localXmlPullParser.getName();
    if ((i != 2) || (!"who".equals(localXmlPullParser.getName())))
      throw new IllegalStateException("Expected <who>: Actual element: <" + str1 + ">");
    String str2 = localXmlPullParser.getAttributeValue(null, "email");
    String str3 = localXmlPullParser.getAttributeValue(null, "rel");
    String str4 = localXmlPullParser.getAttributeValue(null, "valueString");
    Who localWho = new Who();
    localWho.setEmail(str2);
    localWho.setValue(str4);
    byte b1;
    if ("http://schemas.google.com/g/2005#event.attendee".equals(str3))
    {
      b1 = 1;
      localWho.setRelationship(b1);
      paramEventEntry.addAttendee(localWho);
      label154: if (i == 1)
        return;
      switch (i)
      {
      default:
      case 2:
      case 3:
      }
    }
    label398: label495: 
    do
    {
      String str5;
      do
      {
        i = localXmlPullParser.next();
        break label154;
        if ("http://schemas.google.com/g/2005#event.organizer".equals(str3))
        {
          b1 = 2;
          break;
        }
        if ("http://schemas.google.com/g/2005#event.performer".equals(str3))
        {
          b1 = 3;
          break;
        }
        if ("http://schemas.google.com/g/2005#event.speaker".equals(str3))
        {
          b1 = 4;
          break;
        }
        if (StringUtils.isEmpty(str3))
        {
          b1 = 1;
          break;
        }
        throw new ParseException("Unexpected rel: " + str3);
        str5 = localXmlPullParser.getName();
        if ("attendeeStatus".equals(str5))
        {
          String str7 = localXmlPullParser.getAttributeValue(null, "value");
          byte b3;
          if ("http://schemas.google.com/g/2005#event.accepted".equals(str7))
            b3 = 1;
          while (true)
          {
            localWho.setStatus(b3);
            break;
            if ("http://schemas.google.com/g/2005#event.declined".equals(str7))
            {
              b3 = 2;
            }
            else if ("http://schemas.google.com/g/2005#event.invited".equals(str7))
            {
              b3 = 3;
            }
            else if ("http://schemas.google.com/g/2005#event.tentative".equals(str7))
            {
              b3 = 4;
            }
            else
            {
              if (!StringUtils.isEmpty(str7))
                break label398;
              b3 = 4;
            }
          }
          throw new ParseException("Unexpected status: " + str7);
        }
      }
      while (!"attendeeType".equals(str5));
      String str6 = XmlUtils.extractChildText(localXmlPullParser);
      byte b2;
      if ("http://schemas.google.com/g/2005#event.optional".equals(str6))
        b2 = 1;
      while (true)
      {
        localWho.setType(b2);
        break;
        if ("http://schemas.google.com/g/2005#event.required".equals(str6))
        {
          b2 = 2;
        }
        else
        {
          if (!StringUtils.isEmpty(str6))
            break label495;
          b2 = 2;
        }
      }
      throw new ParseException("Unexpected type: " + str6);
    }
    while (!"who".equals(localXmlPullParser.getName()));
  }

  protected Entry createEntry()
  {
    return new EventEntry();
  }

  protected Feed createFeed()
  {
    return new EventsFeed();
  }

  protected void handleEntry(Entry paramEntry)
    throws XmlPullParserException, IOException, ParseException
  {
    this.hasSeenReminder = false;
    super.handleEntry(paramEntry);
  }

  protected void handleExtraElementInEntry(Entry paramEntry)
    throws XmlPullParserException, IOException, ParseException
  {
    XmlPullParser localXmlPullParser = getParser();
    if (!(paramEntry instanceof EventEntry))
      throw new IllegalArgumentException("Expected EventEntry!");
    EventEntry localEventEntry = (EventEntry)paramEntry;
    String str1 = localXmlPullParser.getName();
    String str6;
    byte b3;
    if ("eventStatus".equals(str1))
    {
      str6 = localXmlPullParser.getAttributeValue(null, "value");
      if ("http://schemas.google.com/g/2005#event.canceled".equals(str6))
      {
        b3 = 2;
        localEventEntry.setStatus(b3);
      }
    }
    label563: 
    do
    {
      String str2;
      String str3;
      do
      {
        return;
        if ("http://schemas.google.com/g/2005#event.confirmed".equals(str6))
        {
          b3 = 1;
          break;
        }
        boolean bool3 = "http://schemas.google.com/g/2005#event.tentative".equals(str6);
        b3 = 0;
        if (!bool3)
          break;
        b3 = 0;
        break;
        if ("recurrence".equals(str1))
        {
          localEventEntry.setRecurrence(XmlUtils.extractChildText(localXmlPullParser));
          return;
        }
        if ("transparency".equals(str1))
        {
          String str5 = localXmlPullParser.getAttributeValue(null, "value");
          byte b2;
          if ("http://schemas.google.com/g/2005#event.opaque".equals(str5))
            b2 = 0;
          while (true)
          {
            localEventEntry.setTransparency(b2);
            return;
            boolean bool2 = "http://schemas.google.com/g/2005#event.transparent".equals(str5);
            b2 = 0;
            if (bool2)
              b2 = 1;
          }
        }
        if ("visibility".equals(str1))
        {
          String str4 = localXmlPullParser.getAttributeValue(null, "value");
          byte b1;
          if ("http://schemas.google.com/g/2005#event.confidential".equals(str4))
            b1 = 1;
          while (true)
          {
            localEventEntry.setVisibility(b1);
            return;
            if ("http://schemas.google.com/g/2005#event.default".equals(str4))
            {
              b1 = 0;
            }
            else if ("http://schemas.google.com/g/2005#event.private".equals(str4))
            {
              b1 = 2;
            }
            else
            {
              boolean bool1 = "http://schemas.google.com/g/2005#event.public".equals(str4);
              b1 = 0;
              if (bool1)
                b1 = 3;
            }
          }
        }
        if ("who".equals(str1))
        {
          handleWho(localEventEntry);
          return;
        }
        if ("sendEventNotifications".equals(str1))
        {
          localEventEntry.setSendEventNotifications("true".equals(localXmlPullParser.getAttributeValue(null, "value")));
          return;
        }
        if ("guestsCanModify".equals(str1))
        {
          localEventEntry.setGuestsCanModify("true".equals(localXmlPullParser.getAttributeValue(null, "value")));
          return;
        }
        if ("guestsCanInviteOthers".equals(str1))
        {
          localEventEntry.setGuestsCanInviteOthers("true".equals(localXmlPullParser.getAttributeValue(null, "value")));
          return;
        }
        if ("guestsCanSeeGuests".equals(str1))
        {
          localEventEntry.setGuestsCanSeeGuests("true".equals(localXmlPullParser.getAttributeValue(null, "value")));
          return;
        }
        if ("when".equals(str1))
        {
          handleWhen(localEventEntry);
          return;
        }
        if ("reminder".equals(str1))
        {
          if (!this.hasSeenReminder)
          {
            localEventEntry.clearReminders();
            this.hasSeenReminder = true;
          }
          handleReminder(localEventEntry);
          return;
        }
        if ("originalEvent".equals(str1))
        {
          handleOriginalEvent(localEventEntry);
          return;
        }
        if (!"where".equals(str1))
          break label563;
        str2 = localXmlPullParser.getAttributeValue(null, "valueString");
        str3 = localXmlPullParser.getAttributeValue(null, "rel");
      }
      while ((!StringUtils.isEmpty(str3)) && (!"http://schemas.google.com/g/2005#event".equals(str3)));
      localEventEntry.setWhere(str2);
      return;
      if ("feedLink".equals(str1))
      {
        localEventEntry.setCommentsUri(localXmlPullParser.getAttributeValue(null, "href"));
        return;
      }
    }
    while (!"extendedProperty".equals(str1));
    localEventEntry.addExtendedProperty(localXmlPullParser.getAttributeValue(null, "name"), localXmlPullParser.getAttributeValue(null, "value"));
  }

  protected void handleExtraElementInFeed(Feed paramFeed)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = getParser();
    if (!(paramFeed instanceof EventsFeed))
      throw new IllegalArgumentException("Expected EventsFeed!");
    EventsFeed localEventsFeed = (EventsFeed)paramFeed;
    if ("timezone".equals(localXmlPullParser.getName()))
    {
      String str = localXmlPullParser.getAttributeValue(null, "value");
      if (!StringUtils.isEmpty(str))
        localEventsFeed.setTimezone(str);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.calendar.parser.xml.XmlEventsGDataParser
 * JD-Core Version:    0.6.2
 */