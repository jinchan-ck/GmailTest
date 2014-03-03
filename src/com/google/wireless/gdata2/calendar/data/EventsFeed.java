package com.google.wireless.gdata2.calendar.data;

import com.google.wireless.gdata2.data.Feed;

public class EventsFeed extends Feed
{
  private String timezone = null;

  public String getTimezone()
  {
    return this.timezone;
  }

  public void setTimezone(String paramString)
  {
    this.timezone = paramString;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.calendar.data.EventsFeed
 * JD-Core Version:    0.6.2
 */