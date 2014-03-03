package com.google.wireless.gdata.contacts.data;

import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.StringUtils;

public class GroupEntry extends Entry
{
  private String systemGroup = null;

  public void clear()
  {
    super.clear();
    this.systemGroup = null;
  }

  public String getSystemGroup()
  {
    return this.systemGroup;
  }

  public void setSystemGroup(String paramString)
  {
    this.systemGroup = paramString;
  }

  protected void toString(StringBuffer paramStringBuffer)
  {
    super.toString(paramStringBuffer);
    paramStringBuffer.append("\n");
    paramStringBuffer.append("GroupEntry:");
    if (!StringUtils.isEmpty(this.systemGroup))
      paramStringBuffer.append(" systemGroup:").append(this.systemGroup).append("\n");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.contacts.data.GroupEntry
 * JD-Core Version:    0.6.2
 */