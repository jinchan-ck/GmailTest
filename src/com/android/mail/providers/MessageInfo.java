package com.android.mail.providers;

import android.text.TextUtils;
import com.google.common.base.Objects;
import java.util.regex.Pattern;

public class MessageInfo
{
  private static final Pattern MSG_DIVIDER_REGEX = Pattern.compile("\\^\\*\\*\\*\\*\\^");
  public int priority;
  public boolean read;
  public String sender;
  public boolean starred;

  public MessageInfo()
  {
  }

  public MessageInfo(boolean paramBoolean1, boolean paramBoolean2, String paramString, int paramInt)
  {
    set(paramBoolean1, paramBoolean2, paramString, paramInt);
  }

  public static void createAsString(StringBuilder paramStringBuilder, boolean paramBoolean1, boolean paramBoolean2, String paramString, int paramInt)
  {
    int i = 1;
    int j;
    if (paramBoolean1)
    {
      j = i;
      paramStringBuilder.append(j);
      paramStringBuilder.append("^****^");
      if (!paramBoolean2)
        break label73;
    }
    while (true)
    {
      paramStringBuilder.append(i);
      paramStringBuilder.append("^****^");
      paramStringBuilder.append(ConversationInfo.escapeValue(paramString));
      paramStringBuilder.append("^****^");
      paramStringBuilder.append(paramInt);
      return;
      j = 0;
      break;
      label73: i = 0;
    }
  }

  public static MessageInfo fromString(String paramString)
  {
    boolean bool1 = true;
    String[] arrayOfString = TextUtils.split(paramString, MSG_DIVIDER_REGEX);
    int i = Integer.parseInt(arrayOfString[0]);
    int j = Integer.parseInt(arrayOfString[bool1]);
    String str = ConversationInfo.unescapeValue(arrayOfString[2]);
    int k = Integer.parseInt(arrayOfString[3]);
    boolean bool2;
    if (i != 0)
    {
      bool2 = bool1;
      if (j == 0)
        break label74;
    }
    while (true)
    {
      return new MessageInfo(bool2, bool1, str, k);
      bool2 = false;
      break;
      label74: bool1 = false;
    }
  }

  public static String toString(MessageInfo paramMessageInfo)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    createAsString(localStringBuilder, paramMessageInfo.read, paramMessageInfo.starred, paramMessageInfo.sender, paramMessageInfo.priority);
    return localStringBuilder.toString();
  }

  public int hashCode()
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Boolean.valueOf(this.read);
    arrayOfObject[1] = Boolean.valueOf(this.starred);
    arrayOfObject[2] = this.sender;
    return Objects.hashCode(arrayOfObject);
  }

  public boolean markRead(boolean paramBoolean)
  {
    if (this.read != paramBoolean)
    {
      this.read = paramBoolean;
      return true;
    }
    return false;
  }

  public void set(boolean paramBoolean1, boolean paramBoolean2, String paramString, int paramInt)
  {
    this.read = paramBoolean1;
    this.starred = paramBoolean2;
    this.sender = paramString;
    this.priority = paramInt;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.MessageInfo
 * JD-Core Version:    0.6.2
 */