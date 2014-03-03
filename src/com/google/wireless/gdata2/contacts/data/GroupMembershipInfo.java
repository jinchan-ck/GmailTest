package com.google.wireless.gdata2.contacts.data;

import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.parser.ParseException;

public class GroupMembershipInfo
{
  private boolean deleted;
  private String group;

  public GroupMembershipInfo()
  {
  }

  public GroupMembershipInfo(String paramString, boolean paramBoolean)
  {
    setGroup(paramString);
    setDeleted(paramBoolean);
  }

  public String getGroup()
  {
    return this.group;
  }

  public boolean isDeleted()
  {
    return this.deleted;
  }

  public void setDeleted(boolean paramBoolean)
  {
    this.deleted = paramBoolean;
  }

  public void setGroup(String paramString)
  {
    this.group = paramString;
  }

  public void toString(StringBuffer paramStringBuffer)
  {
    paramStringBuffer.append("GroupMembershipInfo");
    if (this.group != null)
      paramStringBuffer.append(" group:").append(this.group);
    paramStringBuffer.append(" deleted:").append(this.deleted);
  }

  public void validate()
    throws ParseException
  {
    if (StringUtils.isEmpty(this.group))
      throw new ParseException("the group must be present");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.contacts.data.GroupMembershipInfo
 * JD-Core Version:    0.6.2
 */