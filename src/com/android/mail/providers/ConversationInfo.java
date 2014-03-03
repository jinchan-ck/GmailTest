package com.android.mail.providers;

import android.text.TextUtils;
import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class ConversationInfo
{
  private static final Pattern MESSAGE_CONV_SPLITTER_REGEX = Pattern.compile("\\^\\*\\*\\^");
  private static final Pattern MESSAGE_SPLITTER_REGEX = Pattern.compile("\\^\\*\\*\\*\\^");
  private static final Pattern SPLITTER_REGEX = Pattern.compile("\\^\\*\\^");
  public int draftCount;
  public String firstSnippet;
  public String firstUnreadSnippet;
  public String lastSnippet;
  public int messageCount;
  public final ArrayList<MessageInfo> messageInfos = new ArrayList();

  public ConversationInfo()
  {
  }

  public ConversationInfo(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
  {
    set(paramInt1, paramInt2, paramString1, paramString2, paramString3);
  }

  public static void createAsString(StringBuilder paramStringBuilder, int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
  {
    paramStringBuilder.append(paramInt1);
    paramStringBuilder.append("^*^");
    paramStringBuilder.append(paramInt2);
    paramStringBuilder.append("^*^");
    paramStringBuilder.append(escapeValue(paramString1));
    paramStringBuilder.append("^*^");
    paramStringBuilder.append(escapeValue(paramString2));
    paramStringBuilder.append("^*^");
    paramStringBuilder.append(escapeValue(paramString3));
    paramStringBuilder.append("^**^");
  }

  static String escapeValue(String paramString)
  {
    if (paramString == null)
      return "";
    return paramString.replace("^", "\\^\\");
  }

  public static ConversationInfo fromString(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    String[] arrayOfString;
    do
    {
      return null;
      arrayOfString = TextUtils.split(paramString, MESSAGE_CONV_SPLITTER_REGEX);
    }
    while (arrayOfString.length < 2);
    ConversationInfo localConversationInfo = parseConversation(arrayOfString[0]);
    parseMessages(localConversationInfo, arrayOfString[1]);
    return localConversationInfo;
  }

  private static void getMessageInfoString(ConversationInfo paramConversationInfo, StringBuilder paramStringBuilder)
  {
    int i = 0;
    Iterator localIterator = paramConversationInfo.messageInfos.iterator();
    while (localIterator.hasNext())
    {
      paramStringBuilder.append(MessageInfo.toString((MessageInfo)localIterator.next()));
      if (i < -1 + paramConversationInfo.messageInfos.size())
        paramStringBuilder.append("^***^");
      i++;
    }
  }

  private static ConversationInfo parseConversation(String paramString)
  {
    String[] arrayOfString = TextUtils.split(paramString, SPLITTER_REGEX);
    return new ConversationInfo(Integer.parseInt(arrayOfString[0]), Integer.parseInt(arrayOfString[1]), unescapeValue(arrayOfString[2]), unescapeValue(arrayOfString[3]), unescapeValue(arrayOfString[4]));
  }

  private static void parseMessages(ConversationInfo paramConversationInfo, String paramString)
  {
    String[] arrayOfString = TextUtils.split(paramString, MESSAGE_SPLITTER_REGEX);
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
      paramConversationInfo.addMessage(MessageInfo.fromString(arrayOfString[j]));
  }

  public static String toString(ConversationInfo paramConversationInfo)
  {
    if (paramConversationInfo == null)
      return null;
    StringBuilder localStringBuilder = new StringBuilder();
    createAsString(localStringBuilder, paramConversationInfo.messageCount, paramConversationInfo.draftCount, paramConversationInfo.firstSnippet, paramConversationInfo.firstUnreadSnippet, paramConversationInfo.lastSnippet);
    getMessageInfoString(paramConversationInfo, localStringBuilder);
    return localStringBuilder.toString();
  }

  static String unescapeValue(String paramString)
  {
    if (paramString == null)
      return "";
    return paramString.replace("\\^\\", "^");
  }

  public void addMessage(MessageInfo paramMessageInfo)
  {
    this.messageInfos.add(paramMessageInfo);
  }

  public int hashCode()
  {
    Object[] arrayOfObject = new Object[6];
    arrayOfObject[0] = Integer.valueOf(this.messageCount);
    arrayOfObject[1] = Integer.valueOf(this.draftCount);
    arrayOfObject[2] = this.messageInfos;
    arrayOfObject[3] = this.firstSnippet;
    arrayOfObject[4] = this.lastSnippet;
    arrayOfObject[5] = this.firstUnreadSnippet;
    return Objects.hashCode(arrayOfObject);
  }

  public boolean markRead(boolean paramBoolean)
  {
    boolean bool = false;
    Iterator localIterator = this.messageInfos.iterator();
    while (localIterator.hasNext())
      bool |= ((MessageInfo)localIterator.next()).markRead(paramBoolean);
    if (paramBoolean)
    {
      this.firstSnippet = this.lastSnippet;
      return bool;
    }
    this.firstSnippet = this.firstUnreadSnippet;
    return bool;
  }

  public void set(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
  {
    this.messageInfos.clear();
    this.messageCount = paramInt1;
    this.draftCount = paramInt2;
    this.firstSnippet = paramString1;
    this.firstUnreadSnippet = paramString2;
    this.lastSnippet = paramString3;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.ConversationInfo
 * JD-Core Version:    0.6.2
 */