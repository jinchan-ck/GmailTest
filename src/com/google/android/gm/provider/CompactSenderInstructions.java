package com.google.android.gm.provider;

import android.text.Html;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import com.android.mail.utils.ObjectCache;
import com.android.mail.utils.ObjectCache.Callback;
import com.google.common.collect.Lists;
import com.google.common.io.protocol.ProtoBuf;
import com.google.wireless.gdata2.parser.xml.SimplePullParser;
import com.google.wireless.gdata2.parser.xml.SimplePullParser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CompactSenderInstructions
{
  private static final ObjectCache<List<String>> SENDER_LIST_CACHE = new ObjectCache(new ObjectCache.Callback()
  {
    public List<String> newInstance()
    {
      return new ArrayList(8);
    }

    public void onObjectReleased(List<String> paramAnonymousList)
    {
      paramAnonymousList.clear();
    }
  }
  , 2);
  private static final TextUtils.SimpleStringSplitter SENDER_LIST_SPLITTER = new TextUtils.SimpleStringSplitter(Gmail.SENDER_LIST_SEPARATOR.charValue());
  private boolean mHasErrors = false;
  private boolean mHasSending = false;
  private SenderInstructions mSenderInstructions = new SenderInstructions();

  private static void appendElided(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append("e");
    paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
  }

  private static void appendNumDrafts(StringBuilder paramStringBuilder, int paramInt)
  {
    if (paramInt != 0)
    {
      paramStringBuilder.append("d");
      paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
      paramStringBuilder.append(paramInt);
      paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
    }
  }

  private static void appendNumMessages(StringBuilder paramStringBuilder, int paramInt)
  {
    if (paramInt > 1)
    {
      paramStringBuilder.append("n");
      paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
      paramStringBuilder.append(paramInt);
      paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
    }
  }

  private static void appendSendFailed(StringBuilder paramStringBuilder, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      paramStringBuilder.append("f");
      paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
    }
  }

  private static void appendSender(StringBuilder paramStringBuilder, boolean paramBoolean1, int paramInt, String paramString, boolean paramBoolean2)
  {
    if (paramBoolean1);
    for (int i = 1; ; i = 0)
    {
      paramStringBuilder.append(i);
      paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
      paramStringBuilder.append(paramInt);
      paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
      if (paramBoolean2)
        paramString = shortNameFromLongName(paramString);
      paramStringBuilder.append(paramString);
      paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
      return;
    }
  }

  private static void appendSending(StringBuilder paramStringBuilder, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      paramStringBuilder.append("s");
      paramStringBuilder.append(Gmail.SENDER_LIST_SEPARATOR);
    }
  }

  private static String constructString(Collection<SenderInstructions.Sender> paramCollection, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 1;
    StringBuilder localStringBuilder = new StringBuilder();
    appendSending(localStringBuilder, paramBoolean1);
    appendSendFailed(localStringBuilder, paramBoolean2);
    appendNumMessages(localStringBuilder, paramInt1);
    appendNumDrafts(localStringBuilder, paramInt2);
    int j;
    Iterator localIterator;
    if (paramInt3 > i)
    {
      j = 0;
      localIterator = paramCollection.iterator();
    }
    while (true)
    {
      if (!localIterator.hasNext())
        break label165;
      SenderInstructions.Sender localSender = (SenderInstructions.Sender)localIterator.next();
      switch (2.$SwitchMap$com$google$android$gm$provider$SenderInstructions$Visibility[localSender.visibility.ordinal()])
      {
      default:
        break;
      case 1:
        appendSender(localStringBuilder, localSender.unread, localSender.priority, localSender.name, i);
        j = 0;
        continue;
        i = 0;
        break;
      case 2:
        if (j == 0)
        {
          appendElided(localStringBuilder);
          j = 1;
        }
        break;
      }
    }
    label165: return localStringBuilder.toString();
  }

  public static String instructionsStringFromProto(ProtoBuf paramProtoBuf)
  {
    int i = paramProtoBuf.getInt(1);
    int j = paramProtoBuf.getInt(2);
    ArrayList localArrayList1 = Lists.newArrayList();
    int k = 0;
    ArrayList localArrayList2 = Lists.newArrayList();
    ProtoBufHelpers.getAllProtoBufs(paramProtoBuf, 3, localArrayList2);
    Iterator localIterator = localArrayList2.iterator();
    while (localIterator.hasNext())
    {
      ProtoBuf localProtoBuf = (ProtoBuf)localIterator.next();
      if (localProtoBuf.getBool(1))
      {
        SenderInstructions.Sender localSender1 = new SenderInstructions.Sender();
        localSender1.visibility = SenderInstructions.Visibility.HIDDEN;
        localArrayList1.add(localSender1);
      }
      else
      {
        SenderInstructions.Sender localSender2 = new SenderInstructions.Sender();
        localSender2.unread = localProtoBuf.getBool(2);
        localSender2.priority = localProtoBuf.getInt(3);
        localSender2.name = localProtoBuf.getString(4);
        localSender2.visibility = SenderInstructions.Visibility.VISIBLE;
        k++;
        localArrayList1.add(localSender2);
      }
    }
    return constructString(localArrayList1, i, j, k, false, false);
  }

  public static String instructionsStringFromXml(SimplePullParser paramSimplePullParser)
    throws IOException, SimplePullParser.ParseException
  {
    int i = paramSimplePullParser.getIntAttribute(null, "numMessages");
    int j = paramSimplePullParser.getIntAttribute(null, "numDrafts");
    ArrayList localArrayList = Lists.newArrayList();
    int k = 0;
    int m = paramSimplePullParser.getDepth();
    while (true)
    {
      String str = paramSimplePullParser.nextTag(m);
      if (str == null)
        break;
      if ("sender".equals(str))
      {
        SenderInstructions.Sender localSender1 = new SenderInstructions.Sender();
        if (paramSimplePullParser.getIntAttribute(null, "unread") != 0);
        for (boolean bool = true; ; bool = false)
        {
          localSender1.unread = bool;
          localSender1.priority = paramSimplePullParser.getIntAttribute(null, "priority");
          localSender1.name = paramSimplePullParser.getStringAttribute(null, "name");
          localSender1.visibility = SenderInstructions.Visibility.VISIBLE;
          k++;
          localArrayList.add(localSender1);
          break;
        }
      }
      if ("elided".equals(str))
      {
        SenderInstructions.Sender localSender2 = new SenderInstructions.Sender();
        localSender2.visibility = SenderInstructions.Visibility.HIDDEN;
        localArrayList.add(localSender2);
      }
    }
    return constructString(localArrayList, i, j, k, false, false);
  }

  public static void parseCompactSenderInstructions(String paramString, SenderInstructions paramSenderInstructions)
  {
    List localList = (List)SENDER_LIST_CACHE.get();
    try
    {
      localList.clear();
      synchronized (SENDER_LIST_SPLITTER)
      {
        SENDER_LIST_SPLITTER.setString(paramString);
        if (SENDER_LIST_SPLITTER.hasNext())
          localList.add(SENDER_LIST_SPLITTER.next());
      }
    }
    finally
    {
      SENDER_LIST_CACHE.release(localList);
    }
    int j;
    label384: label396: for (int i = 0; ; i = j)
    {
      String str1;
      boolean bool1;
      if (i < localList.size())
      {
        j = i + 1;
        str1 = (String)localList.get(i);
        if (("".equals(str1)) || ("e".equals(str1)))
          continue;
        if ("n".equals(str1))
        {
          j++;
          continue;
        }
        if ("d".equals(str1))
        {
          int k = j + 1;
          paramSenderInstructions.setNumDrafts(Integer.parseInt((String)localList.get(j)));
          j = k;
          continue;
        }
        if ("l".equals(str1))
        {
          (j + 1);
          String str2 = Html.fromHtml((String)localList.get(j)).toString();
          if (str2.length() == 0)
          {
            bool1 = true;
            paramSenderInstructions.addMessage(str2, false, false, bool1, -1);
          }
        }
      }
      while (true)
      {
        SENDER_LIST_CACHE.release(localList);
        return;
        bool1 = false;
        break;
        if (("s".equals(str1)) || ("f".equals(str1)))
          break label396;
        if (j + 2 <= localList.size())
        {
          boolean bool2;
          String str4;
          int n;
          if (Integer.parseInt(str1) != 0)
          {
            bool2 = true;
            int m = j + 1;
            String str3 = (String)localList.get(j);
            j = m + 1;
            str4 = (String)localList.get(m);
            n = Integer.parseInt(str3);
            if (str4.length() != 0)
              break label384;
          }
          for (boolean bool3 = true; ; bool3 = false)
          {
            paramSenderInstructions.addMessage(str4, false, bool2, bool3, n);
            break label396;
            bool2 = false;
            break;
          }
        }
      }
    }
  }

  private static String shortNameFromLongName(String paramString)
  {
    String str1 = paramString.trim();
    if ((str1.startsWith("\"")) && (str1.endsWith("\"")) && (str1.length() >= 2))
      str1 = str1.substring(1, -1 + str1.length()).trim();
    String str2 = str1;
    int i = str2.indexOf(',');
    int m;
    if ((i != -1) && (i != -1 + str2.length()))
    {
      String[] arrayOfString2 = TextUtils.split(str2.substring(0, i), "\\s+");
      int k = 0;
      m = 0;
      if (m < arrayOfString2.length)
      {
        if (arrayOfString2[m].endsWith("."))
          break label208;
        k++;
        if (k < 2)
          break label208;
      }
      if (k == 1)
        str2 = str2.substring(i + 1).trim();
    }
    if (str2.toLowerCase().startsWith("the "))
      str2 = str2.substring("the ".length()).trim();
    String[] arrayOfString1 = TextUtils.split(str2, "\\s+");
    for (int j = 0; ; j++)
    {
      if (j >= arrayOfString1.length)
        break label220;
      String str3 = arrayOfString1[j];
      if (!str3.endsWith("."))
      {
        return str3;
        label208: m++;
        break;
      }
    }
    label220: return str1;
  }

  public void addMessage(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
  {
    boolean bool = true;
    SenderInstructions localSenderInstructions;
    String str;
    if (paramBoolean4)
    {
      this.mHasSending = bool;
      localSenderInstructions = this.mSenderInstructions;
      if (paramString != null)
        break label69;
      str = null;
      label27: if ((!paramBoolean3) && (!paramBoolean4) && (!paramBoolean5))
        break label78;
    }
    while (true)
    {
      localSenderInstructions.addMessage(str, paramBoolean1, paramBoolean2, bool, -1);
      return;
      if (!paramBoolean5)
        break;
      this.mHasErrors = bool;
      break;
      label69: str = Gmail.getNameFromAddressString(paramString);
      break label27;
      label78: bool = false;
    }
  }

  public String toInstructionString(int paramInt)
  {
    this.mSenderInstructions.calculateVisibility(paramInt);
    Collection localCollection = this.mSenderInstructions.getSenders();
    return constructString(localCollection, localCollection.size(), this.mSenderInstructions.getNumDrafts(), this.mSenderInstructions.getNumVisible(), this.mHasSending, this.mHasErrors);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.CompactSenderInstructions
 * JD-Core Version:    0.6.2
 */