package com.google.wireless.gdata2.calendar.data;

import com.google.wireless.gdata2.data.Entry;

public class CalendarEntry extends Entry
{
  public static final byte ACCESS_EDITOR = 3;
  public static final byte ACCESS_FREEBUSY = 2;
  public static final byte ACCESS_NONE = 0;
  public static final byte ACCESS_OWNER = 4;
  public static final byte ACCESS_READ = 1;
  public static final byte ACCESS_ROOT = 5;
  private byte accessLevel = 1;
  private String alternateLink = null;
  private String color = null;
  private boolean hidden = false;
  private String overrideName = null;
  private boolean selected = true;
  private String timezone = null;

  public void clear()
  {
    super.clear();
    this.accessLevel = 1;
    this.alternateLink = null;
    this.color = null;
    this.hidden = false;
    this.selected = true;
    this.timezone = null;
    this.overrideName = null;
  }

  public byte getAccessLevel()
  {
    return this.accessLevel;
  }

  public String getAlternateLink()
  {
    return this.alternateLink;
  }

  public String getColor()
  {
    return this.color;
  }

  public String getOverrideName()
  {
    return this.overrideName;
  }

  public String getTimezone()
  {
    return this.timezone;
  }

  public boolean isHidden()
  {
    return this.hidden;
  }

  public boolean isSelected()
  {
    return this.selected;
  }

  public void setAccessLevel(byte paramByte)
  {
    this.accessLevel = paramByte;
  }

  public void setAlternateLink(String paramString)
  {
    this.alternateLink = paramString;
  }

  public void setColor(String paramString)
  {
    this.color = paramString;
  }

  public void setHidden(boolean paramBoolean)
  {
    this.hidden = paramBoolean;
  }

  public void setOverrideName(String paramString)
  {
    this.overrideName = paramString;
  }

  public void setSelected(boolean paramBoolean)
  {
    this.selected = paramBoolean;
  }

  public void setTimezone(String paramString)
  {
    this.timezone = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("ACCESS LEVEL: ");
    paramStringBuffer.append(this.accessLevel);
    paramStringBuffer.append('\n');
    appendIfNotNull(paramStringBuffer, "ALTERNATE LINK", this.alternateLink);
    appendIfNotNull(paramStringBuffer, "COLOR", this.color);
    paramStringBuffer.append("HIDDEN: ");
    paramStringBuffer.append(this.hidden);
    paramStringBuffer.append('\n');
    paramStringBuffer.append("SELECTED: ");
    paramStringBuffer.append(this.selected);
    paramStringBuffer.append('\n');
    appendIfNotNull(paramStringBuffer, "TIMEZONE", this.timezone);
    appendIfNotNull(paramStringBuffer, "OVERRIDE NAME", this.overrideName);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.calendar.data.CalendarEntry
 * JD-Core Version:    0.6.2
 */