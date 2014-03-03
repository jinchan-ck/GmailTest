package com.android.mail.browse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.TextAppearanceSpan;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.ConversationInfo;
import com.android.mail.providers.MessageInfo;
import com.android.mail.utils.ObjectCache;
import com.android.mail.utils.ObjectCache.Callback;
import com.android.mail.utils.Utils;
import com.google.android.common.html.parser.HtmlParser;
import com.google.android.common.html.parser.HtmlTreeBuilder;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class SendersView
{
  private static final Integer DOES_NOT_EXIST = Integer.valueOf(-5);
  private static final ObjectCache<Map<Integer, Integer>> PRIORITY_LENGTH_MAP_CACHE = new ObjectCache(new ObjectCache.Callback()
  {
    public Map<Integer, Integer> newInstance()
    {
      return Maps.newHashMap();
    }

    public void onObjectReleased(Map<Integer, Integer> paramAnonymousMap)
    {
      paramAnonymousMap.clear();
    }
  }
  , 2);
  public static String SENDERS_VERSION_SEPARATOR = "^**^";
  public static Pattern SENDERS_VERSION_SEPARATOR_PATTERN = Pattern.compile("\\^\\*\\*\\^");
  private static BroadcastReceiver sConfigurationChangedReceiver;
  private static String sDraftCountFormatString;
  private static CharSequence sDraftPluralString;
  private static CharSequence sDraftSingularString;
  private static CharacterStyle sDraftsStyleSpan;
  public static CharSequence sElidedString;
  private static String sMeString;
  private static Locale sMeStringLocale;
  private static String sMessageCountSpacerString;
  private static CharacterStyle sMessageInfoStyleSpan;
  private static CharacterStyle sReadStyleSpan;
  private static String sSendersSplitToken;
  private static CharSequence sSendingString;
  private static CharacterStyle sSendingStyleSpan;
  private static CharacterStyle sUnreadStyleSpan;

  public static SpannableStringBuilder createMessageInfo(Context paramContext, Conversation paramConversation)
  {
    ConversationInfo localConversationInfo = paramConversation.conversationInfo;
    int i = paramConversation.sendingState;
    SpannableStringBuilder localSpannableStringBuilder1 = new SpannableStringBuilder();
    Iterator localIterator = localConversationInfo.messageInfos.iterator();
    do
    {
      boolean bool = localIterator.hasNext();
      j = 0;
      if (!bool)
        break;
    }
    while (TextUtils.isEmpty(((MessageInfo)localIterator.next()).sender));
    int j = 1;
    getSenderResources(paramContext);
    int k;
    int m;
    int n;
    SpannableStringBuilder localSpannableStringBuilder3;
    if (localConversationInfo != null)
    {
      k = localConversationInfo.messageCount;
      m = localConversationInfo.draftCount;
      if (i != 2)
        break label328;
      n = 1;
      if (k > 1)
        localSpannableStringBuilder1.append(k + "");
      localSpannableStringBuilder1.setSpan(CharacterStyle.wrap(sMessageInfoStyleSpan), 0, localSpannableStringBuilder1.length(), 0);
      if (m > 0)
      {
        if ((j != 0) || (k > 1))
          localSpannableStringBuilder1.append(sSendersSplitToken);
        localSpannableStringBuilder3 = new SpannableStringBuilder();
        if (m != 1)
          break label334;
        localSpannableStringBuilder3.append(sDraftSingularString);
      }
    }
    while (true)
    {
      localSpannableStringBuilder3.setSpan(CharacterStyle.wrap(sDraftsStyleSpan), 0, localSpannableStringBuilder3.length(), 33);
      localSpannableStringBuilder1.append(localSpannableStringBuilder3);
      if (n != 0)
      {
        if ((k > 1) || (m > 0))
          localSpannableStringBuilder1.append(sSendersSplitToken);
        SpannableStringBuilder localSpannableStringBuilder2 = new SpannableStringBuilder();
        localSpannableStringBuilder2.append(sSendingString);
        localSpannableStringBuilder2.setSpan(sSendingStyleSpan, 0, localSpannableStringBuilder2.length(), 0);
        localSpannableStringBuilder1.append(localSpannableStringBuilder2);
      }
      if ((k > 1) || ((m > 0) && (j != 0)) || (n != 0))
        localSpannableStringBuilder1 = new SpannableStringBuilder(sMessageCountSpacerString).append(localSpannableStringBuilder1);
      return localSpannableStringBuilder1;
      label328: n = 0;
      break;
      label334: StringBuilder localStringBuilder = new StringBuilder().append(sDraftPluralString);
      String str = sDraftCountFormatString;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(m);
      localSpannableStringBuilder3.append(String.format(str, arrayOfObject));
    }
  }

  public static SpannableString[] format(Context paramContext, ConversationInfo paramConversationInfo, String paramString, int paramInt, HtmlParser paramHtmlParser, HtmlTreeBuilder paramHtmlTreeBuilder)
  {
    getSenderResources(paramContext);
    ArrayList localArrayList = handlePriority(paramContext, paramInt, paramString.toString(), paramConversationInfo, paramHtmlParser, paramHtmlTreeBuilder);
    return (SpannableString[])localArrayList.toArray(new SpannableString[localArrayList.size()]);
  }

  private static void formatDefault(ConversationItemViewModel paramConversationItemViewModel, String paramString, Context paramContext)
  {
    getSenderResources(paramContext);
    paramConversationItemViewModel.senderFragments.clear();
    String[] arrayOfString1 = TextUtils.split(paramString, ",");
    String[] arrayOfString2 = new String[arrayOfString1.length];
    for (int i = 0; i < arrayOfString1.length; i++)
    {
      Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(arrayOfString1[i]);
      if ((arrayOfRfc822Token != null) && (arrayOfRfc822Token.length > 0))
      {
        String str = arrayOfRfc822Token[0].getName();
        if (TextUtils.isEmpty(str))
          str = arrayOfRfc822Token[0].getAddress();
        arrayOfString2[i] = str;
      }
    }
    generateSenderFragments(paramConversationItemViewModel, arrayOfString2);
  }

  public static void formatSenders(ConversationItemViewModel paramConversationItemViewModel, Context paramContext)
  {
    formatDefault(paramConversationItemViewModel, paramConversationItemViewModel.conversation.senders, paramContext);
  }

  private static void generateSenderFragments(ConversationItemViewModel paramConversationItemViewModel, String[] paramArrayOfString)
  {
    paramConversationItemViewModel.sendersText = TextUtils.join(", ", paramArrayOfString);
    paramConversationItemViewModel.addSenderFragment(0, paramConversationItemViewModel.sendersText.length(), getReadStyleSpan(), true);
  }

  static String getMe(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    Locale localLocale = localResources.getConfiguration().locale;
    if ((sMeString == null) || (!localLocale.equals(sMeStringLocale)))
    {
      sMeString = localResources.getString(2131427483);
      sMeStringLocale = localLocale;
    }
    return sMeString;
  }

  private static CharacterStyle getReadStyleSpan()
  {
    return CharacterStyle.wrap(sReadStyleSpan);
  }

  private static void getSenderResources(Context paramContext)
  {
    if (sConfigurationChangedReceiver == null)
    {
      sConfigurationChangedReceiver = new BroadcastReceiver()
      {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
          SendersView.access$002(null);
          SendersView.getSenderResources(paramAnonymousContext);
        }
      };
      paramContext.registerReceiver(sConfigurationChangedReceiver, new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"));
    }
    if (sDraftSingularString == null)
    {
      Resources localResources = paramContext.getResources();
      sSendersSplitToken = localResources.getString(2131427587);
      sElidedString = localResources.getString(2131427589);
      sDraftSingularString = localResources.getQuantityText(2131755010, 1);
      sDraftPluralString = localResources.getQuantityText(2131755010, 2);
      sDraftCountFormatString = localResources.getString(2131427588);
      sMessageInfoStyleSpan = new TextAppearanceSpan(paramContext, 2131492938);
      sDraftsStyleSpan = new TextAppearanceSpan(paramContext, 2131492939);
      sUnreadStyleSpan = new TextAppearanceSpan(paramContext, 2131492942);
      sSendingStyleSpan = new TextAppearanceSpan(paramContext, 2131492940);
      sReadStyleSpan = new TextAppearanceSpan(paramContext, 2131492941);
      sMessageCountSpacerString = localResources.getString(2131427591);
      sSendingString = localResources.getString(2131427481);
    }
  }

  public static Typeface getTypeface(boolean paramBoolean)
  {
    if (paramBoolean)
      return Typeface.DEFAULT_BOLD;
    return Typeface.DEFAULT;
  }

  private static CharacterStyle getUnreadStyleSpan()
  {
    return CharacterStyle.wrap(sUnreadStyleSpan);
  }

  public static ArrayList<SpannableString> handlePriority(Context paramContext, int paramInt, String paramString, ConversationInfo paramConversationInfo, HtmlParser paramHtmlParser, HtmlTreeBuilder paramHtmlTreeBuilder)
  {
    int i = -1;
    int j = paramString.length();
    int k = 0;
    int m = 0;
    if (j > paramInt)
      m = j - paramInt;
    Map localMap = (Map)PRIORITY_LENGTH_MAP_CACHE.get();
    while (true)
    {
      int n;
      int i1;
      int i3;
      ArrayList localArrayList;
      int i4;
      try
      {
        localMap.clear();
        Iterator localIterator = paramConversationInfo.messageInfos.iterator();
        boolean bool = localIterator.hasNext();
        n = 0;
        if (bool)
        {
          MessageInfo localMessageInfo1 = (MessageInfo)localIterator.next();
          if (TextUtils.isEmpty(localMessageInfo1.sender))
            break label608;
          i1 = localMessageInfo1.sender.length();
          localMap.put(Integer.valueOf(localMessageInfo1.priority), Integer.valueOf(i1));
          k = Math.max(k, localMessageInfo1.priority);
          continue;
        }
        if (i < k)
        {
          if (!localMap.containsKey(Integer.valueOf(i + 1)))
            break label621;
          int i2 = ((Integer)localMap.get(Integer.valueOf(i + 1))).intValue();
          i3 = j + i2;
          if (j > 0)
            i3 += 2;
          if ((i3 <= paramInt) || (n < 2))
            break label614;
        }
        PRIORITY_LENGTH_MAP_CACHE.release(localMap);
        localArrayList = new ArrayList();
        i4 = 0;
        HashMap localHashMap = Maps.newHashMap();
        int i5 = 0;
        if (i5 >= paramConversationInfo.messageInfos.size())
          break label605;
        MessageInfo localMessageInfo2 = (MessageInfo)paramConversationInfo.messageInfos.get(i5);
        if (!TextUtils.isEmpty(localMessageInfo2.sender))
        {
          str1 = localMessageInfo2.sender;
          if (str1.length() != 0)
            break label530;
          str2 = getMe(paramContext);
          if (m != 0)
            str2 = str2.substring(0, Math.max(str2.length() - m, 0));
          int i6 = localMessageInfo2.priority;
          if (localMessageInfo2.read)
            break label544;
          localCharacterStyle = getUnreadStyleSpan();
          if (i6 > i)
            break label560;
          SpannableString localSpannableString1 = new SpannableString(str2);
          if (!localHashMap.containsKey(localMessageInfo2.sender))
            break label552;
          localInteger = (Integer)localHashMap.get(localMessageInfo2.sender);
          int i7 = localInteger.intValue();
          if ((i7 == DOES_NOT_EXIST.intValue()) || (!localMessageInfo2.read))
          {
            if ((i7 != DOES_NOT_EXIST.intValue()) && (i5 > 0) && (i7 == i5 - 1) && (i7 < localArrayList.size()))
              localArrayList.set(i7, null);
            localHashMap.put(localMessageInfo2.sender, Integer.valueOf(i5));
            localSpannableString1.setSpan(localCharacterStyle, 0, localSpannableString1.length(), 0);
            localArrayList.add(localSpannableString1);
          }
          i5++;
          continue;
        }
      }
      finally
      {
        PRIORITY_LENGTH_MAP_CACHE.release(localMap);
      }
      String str1 = "";
      continue;
      label530: String str2 = Utils.convertHtmlToPlainText(str1, paramHtmlParser, paramHtmlTreeBuilder);
      continue;
      label544: CharacterStyle localCharacterStyle = getReadStyleSpan();
      continue;
      label552: Integer localInteger = DOES_NOT_EXIST;
      continue;
      label560: if (i4 == 0)
      {
        SpannableString localSpannableString2 = new SpannableString(sElidedString);
        localSpannableString2.setSpan(localCharacterStyle, 0, localSpannableString2.length(), 0);
        i4 = 1;
        localArrayList.add(localSpannableString2);
        continue;
        label605: return localArrayList;
        label608: i1 = 0;
        continue;
        label614: j = i3;
        n++;
        label621: i++;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.SendersView
 * JD-Core Version:    0.6.2
 */