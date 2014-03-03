package com.google.android.gm.provider;

import android.content.ContentQueryMap;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.text.TextUtils.StringSplitter;
import android.text.style.CharacterStyle;
import android.util.LruCache;
import android.util.Patterns;
import com.google.android.gm.persistence.GmailBackupAgent;
import com.google.android.gm.provider.uiprovider.UIAttachment;
import com.google.android.gsf.Gservices;
import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableBiMap.Builder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Gmail
{
  private static final Set<String> ASSIGNABLE_BUILTIN_LABELS;
  public static final Pattern ATTACHMENT_INFO_SEPARATOR_PATTERN;
  public static final Pattern COMMA_SEPARATOR_PATTERN;
  static String[] CONVERSATION_PROJECTION;
  static String[] CONVERSATION_PROJECTION_LIMITED;
  private static final Set<String> DISPLAYABLE_SYSTEM_LABELS;
  public static final Pattern EMAIL_SEPARATOR_PATTERN;
  static String[] LABEL_PROJECTION;
  public static final BiMap<Long, String> LOCAL_PRIORITY_LABELS;
  static String[] MESSAGE_PROJECTION;
  private static final Pattern NAME_ADDRESS_PATTERN;
  public static final Set<String> PRIORITY_MARKERS;
  public static final Character SENDER_LIST_SEPARATOR;
  public static String[] SETTINGS_PROJECTION;
  public static final Pattern SPACE_SEPARATOR_PATTERN;
  static final String[] STATUS_PROJECTION;
  private static final Set<String> SYSTEM_LABELS;
  private static final Pattern UNNAMED_ADDRESS_PATTERN;
  private static final Set<String> USER_SETTABLE_BUILTIN_LABELS;
  static Map<String, Map<String, Uri>> sAccountUriMap;
  private static final Map<Integer, Integer> sPriorityToLength;
  public static String[] sSenderFragments;
  public static final TextUtils.SimpleStringSplitter sSenderListSplitter;
  private final ContentResolver mContentResolver;

  static
  {
    if (!Gmail.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      EMAIL_SEPARATOR_PATTERN = Pattern.compile("\n");
      sAccountUriMap = Maps.newHashMap();
      SPACE_SEPARATOR_PATTERN = Pattern.compile(" ");
      COMMA_SEPARATOR_PATTERN = Pattern.compile(",");
      ATTACHMENT_INFO_SEPARATOR_PATTERN = Pattern.compile("\n");
      SENDER_LIST_SEPARATOR = Character.valueOf('\n');
      NAME_ADDRESS_PATTERN = Pattern.compile("\"(.*)\"");
      UNNAMED_ADDRESS_PATTERN = Pattern.compile("([^<]+)@");
      sPriorityToLength = Maps.newHashMap();
      sSenderListSplitter = new TextUtils.SimpleStringSplitter(SENDER_LIST_SEPARATOR.charValue());
      sSenderFragments = new String[8];
      SYSTEM_LABELS = ImmutableSet.of("^all", "^b", "^r", "^g", "^i", "^f", new String[] { "^s", "^t", "^k", "^u", "^io_im", "^iim", "^^cached", "^^out", "^^important", "^^unimportant", "^imi", "^imn", "^io_ns", "^im" });
      DISPLAYABLE_SYSTEM_LABELS = ImmutableSet.of("^g", "^i", "^s", "^k");
      LOCAL_PRIORITY_LABELS = ImmutableBiMap.builder().put(Long.valueOf(-500L), "^imi").put(Long.valueOf(-501L), "^imn").put(Long.valueOf(-499L), "^im").put(Long.valueOf(-502L), "^io_ns").build();
      PRIORITY_MARKERS = ImmutableSet.of("^imi", "^imn", "^im", "^io_ns");
      USER_SETTABLE_BUILTIN_LABELS = ImmutableSet.of("^i", "^u", "^k", "^s", "^t", "^g", new String[] { "^^important", "^^unimportant", "^imi", "^imn", "^io_ns", "^o" });
      ASSIGNABLE_BUILTIN_LABELS = ImmutableSet.of("^im", "^io_im", "^iim", "^p");
      CONVERSATION_PROJECTION = new String[] { "_id", "subject", "snippet", "fromAddress", "date", "personalLevel", "labelIds", "numMessages", "maxMessageId", "hasAttachments", "hasMessagesWithErrors", "forceAllUnread", "synced", "conversationLabels" };
      CONVERSATION_PROJECTION_LIMITED = new String[] { "_id", "subject", "snippet", "fromAddress", "personalLevel", "labelIds", "numMessages", "maxMessageId", "hasAttachments", "hasMessagesWithErrors", "forceAllUnread" };
      MESSAGE_PROJECTION = new String[] { "_id", "messageId", "conversation", "subject", "snippet", "fromAddress", "customFromAddress", "toAddresses", "ccAddresses", "bccAddresses", "replyToAddresses", "dateSentMs", "dateReceivedMs", "listInfo", "personalLevel", "body", "bodyEmbedsExternalResources", "labelIds", "joinedAttachmentInfos", "refMessageId", "error", "forward", "includeQuotedText", "quoteStartPos", "labelCount", "messageLabels", "isStarred", "isDraft", "isInOutbox", "isUnread" };
      LABEL_PROJECTION = new String[] { "_id", "canonicalName", "name", "numConversations", "numUnreadConversations", "color", "systemLabel", "hidden", "labelCountDisplayBehavior", "labelSyncPolicy", "lastTouched", "sortOrder" };
      SETTINGS_PROJECTION = new String[] { "labelsIncluded", "labelsPartial", "conversationAgeDays", "maxAttachmentSize" };
      STATUS_PROJECTION = new String[] { "status", "account" };
      return;
    }
  }

  public Gmail(ContentResolver paramContentResolver)
  {
    this.mContentResolver = paramContentResolver;
  }

  public static void addOrRemoveLabelOnMessage(ContentResolver paramContentResolver, String paramString1, long paramLong1, long paramLong2, String paramString2, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      Uri localUri = getMessageLabelsUri(paramString1, paramLong2);
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("canonicalName", paramString2);
      paramContentResolver.insert(localUri, localContentValues);
      return;
    }
    paramContentResolver.delete(getMessageLabelUri(paramString1, paramLong2, paramString2), null, null);
  }

  private static void addStyledFragment(SpannableStringBuilder paramSpannableStringBuilder, String paramString, CharacterStyle paramCharacterStyle, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      int m = paramSpannableStringBuilder.length();
      paramSpannableStringBuilder.append(paramString);
      paramSpannableStringBuilder.setSpan(CharacterStyle.wrap(paramCharacterStyle), m, paramSpannableStringBuilder.length(), 33);
      return;
    }
    int i = 0;
    do
    {
      int j = paramString.substring(i).indexOf(' ');
      if (j == -1)
      {
        addStyledFragment(paramSpannableStringBuilder, paramString.substring(i), paramCharacterStyle, true);
        return;
      }
      int k = j + i;
      if (i < k)
      {
        addStyledFragment(paramSpannableStringBuilder, paramString.substring(i, k), paramCharacterStyle, true);
        paramSpannableStringBuilder.append(' ');
      }
      i = k + 1;
    }
    while (i < paramString.length());
  }

  public static boolean deviceHasLargeDataPartition(Context paramContext)
  {
    return paramContext.getFilesDir().getTotalSpace() >= Gservices.getLong(paramContext.getContentResolver(), "gmail_large_data_partition_min_threshold_bytes", 1000000000L);
  }

  private static Map<String, Uri> getAccountUriCache(String paramString)
  {
    try
    {
      Object localObject2 = (Map)sAccountUriMap.get(paramString);
      if (localObject2 == null)
      {
        localObject2 = new ConcurrentHashMap();
        sAccountUriMap.put(paramString, localObject2);
      }
      return localObject2;
    }
    finally
    {
    }
  }

  public static Uri getAttachmentUri(String paramString, long paramLong, UIAttachment paramUIAttachment, AttachmentRendition paramAttachmentRendition, boolean paramBoolean)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    if ((paramUIAttachment.supportsOrigin()) && (paramUIAttachment.getOrigin() == AttachmentOrigin.LOCAL_FILE) && (paramAttachmentRendition == AttachmentRendition.BEST))
      return Uri.parse(paramUIAttachment.getOriginExtras());
    return getMessagesUri(paramString).buildUpon().appendPath(Long.toString(paramLong)).appendPath("attachments").appendPath(paramUIAttachment.getPartId()).appendPath(paramAttachmentRendition.toString()).appendPath(Boolean.toString(paramBoolean)).build();
  }

  static Uri getAttachmentsForConversationUri(String paramString, long paramLong)
  {
    return getConversationsUri(paramString).buildUpon().appendEncodedPath(Long.toString(paramLong)).appendEncodedPath("attachments").build();
  }

  public static Uri getBaseUri(String paramString)
  {
    Map localMap = getAccountUriCache(paramString);
    Uri localUri = (Uri)localMap.get("baseUri");
    if (localUri == null)
    {
      localUri = Uri.parse("content://gmail-ls/" + paramString);
      localMap.put("baseUri", localUri);
    }
    return localUri;
  }

  private static Uri getCachedUri(String paramString1, String paramString2)
  {
    Map localMap = getAccountUriCache(paramString1);
    Uri localUri = (Uri)localMap.get(paramString2);
    if (localUri == null)
    {
      localUri = Uri.parse("content://gmail-ls/" + paramString1 + paramString2);
      localMap.put(paramString2, localUri);
    }
    return localUri;
  }

  public static String getCanonicalLabelForNotification(String paramString)
  {
    return "^^unseen-" + paramString;
  }

  public static String getCanonicalLabelForTagLabel(String paramString)
  {
    if ((!TextUtils.isEmpty(paramString)) && (paramString.startsWith("^^unseen-")))
      paramString = paramString.substring("^^unseen-".length());
    return paramString;
  }

  static Uri getConversationLabelUri(String paramString1, long paramLong, String paramString2)
  {
    return getConversationUri(paramString1).buildUpon().appendEncodedPath(Long.toString(paramLong)).appendEncodedPath("labels").appendEncodedPath(urlEncodeLabel(paramString2)).build();
  }

  private static Uri getConversationUri(String paramString)
  {
    return getCachedUri(paramString, "/conversation/");
  }

  public static Uri getConversationsUri(String paramString)
  {
    return getCachedUri(paramString, "/conversations/");
  }

  public static long getDefaultConversationAgeDays(Context paramContext)
  {
    String str;
    if (deviceHasLargeDataPartition(paramContext))
      str = "gmail_default_label_sync_days_large_data_partition";
    for (long l = 30L; ; l = 4L)
    {
      return Gservices.getLong(paramContext.getContentResolver(), str, l);
      str = "gmail_default_label_sync_days";
    }
  }

  public static String getEmailFromAddressString(String paramString)
  {
    String str = paramString;
    Matcher localMatcher = Patterns.EMAIL_ADDRESS.matcher(paramString);
    if (localMatcher.find())
      str = paramString.substring(localMatcher.start(), localMatcher.end());
    return str;
  }

  public static Set<Long> getLabelIdsFromLabelIdsString(TextUtils.StringSplitter paramStringSplitter)
  {
    HashSet localHashSet = Sets.newHashSet();
    Iterator localIterator = paramStringSplitter.iterator();
    while (localIterator.hasNext())
      localHashSet.add(Long.valueOf((String)localIterator.next()));
    return localHashSet;
  }

  public static Set<Long> getLabelIdsFromLabelMap(Map<String, Label> paramMap)
  {
    HashSet localHashSet = Sets.newHashSet();
    Iterator localIterator = paramMap.values().iterator();
    while (localIterator.hasNext())
      localHashSet.add(Long.valueOf(((Label)localIterator.next()).getId()));
    return localHashSet;
  }

  public static String getLabelIdsStringFromLabelIds(Set<Long> paramSet)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(',');
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      localStringBuilder.append((Long)localIterator.next());
      localStringBuilder.append(',');
    }
    return localStringBuilder.toString();
  }

  public static Uri getLabelUri(String paramString)
  {
    return getCachedUri(paramString, "/label/");
  }

  static Uri getLabelsUri(String paramString)
  {
    return getCachedUri(paramString, "/labels/");
  }

  public static Uri getMessageByIdUri(String paramString, long paramLong)
  {
    return getMessagesUri(paramString).buildUpon().appendEncodedPath(Long.toString(paramLong)).build();
  }

  static Uri getMessageLabelUri(String paramString1, long paramLong, String paramString2)
  {
    return getMessagesUri(paramString1).buildUpon().appendEncodedPath(Long.toString(paramLong)).appendEncodedPath("labels").appendEncodedPath(urlEncodeLabel(paramString2)).build();
  }

  static Uri getMessageLabelsUri(String paramString, long paramLong)
  {
    return getMessagesUri(paramString).buildUpon().appendEncodedPath(Long.toString(paramLong)).appendEncodedPath("labels").build();
  }

  private static Uri getMessagesForConversationUri(String paramString, long paramLong)
  {
    return getConversationsUri(paramString).buildUpon().appendEncodedPath(Long.toString(paramLong)).appendEncodedPath("messages").build();
  }

  static Uri getMessagesUri(String paramString)
  {
    return getCachedUri(paramString, "/messages/");
  }

  public static String getNameFromAddressString(String paramString)
  {
    Matcher localMatcher1 = NAME_ADDRESS_PATTERN.matcher(paramString);
    if (localMatcher1.find())
    {
      String str = localMatcher1.group(1);
      if (str.length() > 0)
        return str;
      paramString = paramString.substring(localMatcher1.end(), paramString.length());
    }
    Matcher localMatcher2 = UNNAMED_ADDRESS_PATTERN.matcher(paramString);
    if (localMatcher2.find())
      return localMatcher2.group(1);
    return paramString;
  }

  private Cursor getRawCursorForConversationId(String paramString, long paramLong)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    Uri localUri = getMessagesForConversationUri(paramString, paramLong);
    return this.mContentResolver.query(localUri, MESSAGE_PROJECTION, null, null, null);
  }

  private static String[] getSelectionArguments(BecomeActiveNetworkCursor paramBecomeActiveNetworkCursor)
  {
    if (BecomeActiveNetworkCursor.NO == paramBecomeActiveNetworkCursor)
      return new String[] { "SELECTION_ARGUMENT_DO_NOT_BECOME_ACTIVE_NETWORK_CURSOR" };
    return null;
  }

  public static void getSenderSnippet(String paramString, SpannableStringBuilder paramSpannableStringBuilder1, SpannableStringBuilder paramSpannableStringBuilder2, int paramInt, CharacterStyle paramCharacterStyle1, CharacterStyle paramCharacterStyle2, CharacterStyle paramCharacterStyle3, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, CharSequence paramCharSequence4, CharSequence paramCharSequence5, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
  {
    try
    {
      if ((!$assertionsDisabled) && (paramBoolean1) && (paramBoolean2))
        throw new AssertionError();
    }
    finally
    {
    }
    int i;
    String[] arrayOfString;
    int m;
    label100: int n;
    label173: int i1;
    int i17;
    int i3;
    int i19;
    int i2;
    label365: boolean bool2;
    label405: label497: String str1;
    SpannableStringBuilder localSpannableStringBuilder;
    int i5;
    int i6;
    label760: int i7;
    int i8;
    int i9;
    label773: int i10;
    label833: int i12;
    if ((paramBoolean1) || (paramBoolean2))
    {
      i = 1;
      Map localMap = sPriorityToLength;
      localMap.clear();
      int j = -2147483648;
      Object localObject2 = "";
      Object localObject3 = "";
      Object localObject4 = "";
      sSenderListSplitter.setString(paramString);
      arrayOfString = sSenderFragments;
      int k = arrayOfString.length;
      m = 0;
      while (true)
        if (sSenderListSplitter.hasNext())
        {
          n = m + 1;
          arrayOfString[m] = sSenderListSplitter.next();
          if (n == k)
          {
            sSenderFragments = new String[k * 2];
            System.arraycopy(arrayOfString, 0, sSenderFragments, 0, k);
            k *= 2;
            arrayOfString = sSenderFragments;
            m = n;
            continue;
            if (i1 < m)
            {
              i17 = i1 + 1;
              String str6 = arrayOfString[i1];
              if (("".equals(str6)) || ("e".equals(str6)))
                break label1262;
              if ("n".equals(str6))
              {
                int i18 = i17 + 1;
                i3 = Integer.valueOf(arrayOfString[i17]).intValue();
                i17 = i18;
                break label1262;
              }
              if ("d".equals(str6))
              {
                i19 = i17 + 1;
                String str7 = arrayOfString[i17];
                i2 = Integer.parseInt(str7);
                if (i2 == 1)
                {
                  localObject2 = paramCharSequence2;
                  break label1269;
                }
                localObject2 = paramCharSequence3 + " (" + str7 + ")";
                break label1269;
              }
              if ("l".equals(str6))
              {
                (i17 + 1);
                paramSpannableStringBuilder1.append(Html.fromHtml(arrayOfString[i17]));
                return;
              }
              if ("s".equals(str6))
              {
                localObject3 = paramCharSequence4;
                break label1262;
              }
              if (!"f".equals(str6))
                break label1276;
              localObject4 = paramCharSequence5;
              break label1262;
              while (true)
              {
                int i20 = i17 + 1;
                String str8 = arrayOfString[i17];
                i17 = i20 + 1;
                Object localObject6 = arrayOfString[i20];
                if (((CharSequence)localObject6).length() == 0)
                  localObject6 = paramCharSequence1;
                int i21 = Integer.parseInt(str8);
                if ((paramBoolean5) && (!bool2))
                  break;
                localMap.put(Integer.valueOf(i21), Integer.valueOf(((CharSequence)localObject6).length()));
                j = Math.max(j, i21);
                break;
                if (Integer.parseInt(str6) == 0)
                  break label1288;
                bool2 = true;
              }
            }
            if ((i3 == 0) || (!paramBoolean4))
              break label1332;
            str1 = "  " + Integer.toString(i3 + i2);
            int i4 = ((CharSequence)localObject2).length();
            localSpannableStringBuilder = null;
            if (i4 != 0)
            {
              localSpannableStringBuilder = null;
              if (paramBoolean3)
              {
                localSpannableStringBuilder = null;
                if (0 == 0)
                  localSpannableStringBuilder = new SpannableStringBuilder();
                localSpannableStringBuilder.append((CharSequence)localObject2);
                if (paramCharacterStyle3 != null)
                  localSpannableStringBuilder.setSpan(CharacterStyle.wrap(paramCharacterStyle3), 0, localSpannableStringBuilder.length(), 33);
              }
            }
            if (((CharSequence)localObject3).length() != 0)
            {
              if (localSpannableStringBuilder == null)
                localSpannableStringBuilder = new SpannableStringBuilder();
              if (localSpannableStringBuilder.length() != 0)
                localSpannableStringBuilder.append(", ");
              localSpannableStringBuilder.append((CharSequence)localObject3);
            }
            if (((CharSequence)localObject4).length() != 0)
            {
              if (localSpannableStringBuilder == null)
                localSpannableStringBuilder = new SpannableStringBuilder();
              if (localSpannableStringBuilder.length() != 0)
                localSpannableStringBuilder.append(", ");
              localSpannableStringBuilder.append((CharSequence)localObject4);
            }
            i5 = 0;
            if (localSpannableStringBuilder != null)
              i5 = localSpannableStringBuilder.length();
            i6 = paramInt - i5;
            if (str1.length() != 0)
              break label1294;
            if (j == -2147483648)
              break label1340;
            break label1294;
            i7 = -1;
            i8 = str1.length();
            i9 = 0;
            if (i7 < j)
            {
              if (!localMap.containsKey(Integer.valueOf(i7 + 1)))
                break label1350;
              i10 = i8 + ((Integer)localMap.get(Integer.valueOf(i7 + 1))).intValue();
              if (i8 <= 0)
                break label1297;
              i10 += 2;
              break label1297;
            }
            int i11 = i8;
            i12 = 0;
            if (i11 <= i6)
              break label1313;
            i12 = (i8 - i6) / i9;
            break label1313;
          }
        }
    }
    label860: int i13;
    int i14;
    Object localObject5;
    CharacterStyle localCharacterStyle;
    boolean bool1;
    label1040: String str5;
    while (true)
    {
      if (i13 < m)
      {
        i14 = i13 + 1;
        String str2 = arrayOfString[i13];
        if ("".equals(str2))
          break label1325;
        if ("e".equals(str2))
        {
          if (localObject5 == null)
            break label1356;
          addStyledFragment(paramSpannableStringBuilder1, (String)localObject5, localCharacterStyle, false);
          paramSpannableStringBuilder1.append(" ");
          addStyledFragment(paramSpannableStringBuilder1, "..", localCharacterStyle, true);
          paramSpannableStringBuilder1.append(" ");
          break label1356;
        }
        if ("n".equals(str2))
        {
          i14++;
          break label1325;
        }
        if ("d".equals(str2))
        {
          i14++;
          break label1325;
        }
        if (("s".equals(str2)) || ("f".equals(str2)))
          break label1325;
        int i15 = i14 + 1;
        String str3 = arrayOfString[i14];
        i14 = i15 + 1;
        String str4 = arrayOfString[i15];
        if (i != 0)
        {
          bool1 = paramBoolean1;
          break label1362;
          if (str4.length() != 0)
            break label1147;
        }
        label1147: for (str5 = paramCharSequence1.toString(); ; str5 = Html.fromHtml(str4).toString())
        {
          if (i12 != 0)
          {
            int i16 = Math.max(str5.length() - i12, 0);
            str5 = str5.substring(0, i16);
          }
          if (Integer.parseInt(str3) > i7)
            break;
          if ((localObject5 == null) || (((String)localObject5).equals(str5)))
            break label1375;
          addStyledFragment(paramSpannableStringBuilder1, ((String)localObject5).concat(","), localCharacterStyle, false);
          paramSpannableStringBuilder1.append(" ");
          break label1375;
          if (Integer.parseInt(str2) == 0)
            break label1391;
          bool1 = true;
          break label1362;
        }
        if (localObject5 == null)
          break label1404;
        addStyledFragment(paramSpannableStringBuilder1, (String)localObject5, localCharacterStyle, false);
        paramSpannableStringBuilder1.append(" ");
        addStyledFragment(paramSpannableStringBuilder1, "..", localCharacterStyle, true);
        paramSpannableStringBuilder1.append(" ");
        break label1404;
      }
      if (localObject5 != null)
        addStyledFragment(paramSpannableStringBuilder1, (String)localObject5, localCharacterStyle, false);
      paramSpannableStringBuilder1.append(str1);
      if (i5 == 0)
        break label365;
      paramSpannableStringBuilder2.append(localSpannableStringBuilder);
      break label365;
      m = n;
      break label100;
      i = 0;
      break;
      i1 = 0;
      i2 = 0;
      i3 = 0;
      break label173;
      while (true)
      {
        label1262: i1 = i17;
        break;
        label1269: i17 = i19;
      }
      label1276: if (i == 0)
        break label497;
      bool2 = paramBoolean1;
      break label405;
      label1288: bool2 = false;
      break label405;
      label1294: break label760;
      label1297: if ((i10 <= i6) || (i9 < 2))
        break label1343;
      break label833;
      label1313: i13 = 0;
      localObject5 = null;
      localCharacterStyle = null;
    }
    while (true)
    {
      label1325: i13 = i14;
      break label860;
      label1332: str1 = "";
      break;
      label1340: break label760;
      label1343: i8 = i10;
      i9++;
      label1350: i7++;
      break label773;
      label1356: localObject5 = null;
      continue;
      label1362: if (paramBoolean5)
      {
        if (!bool1)
          continue;
        break label1040;
        label1375: localObject5 = str5;
        if (!bool1)
          break label1397;
      }
      label1391: label1397: for (localCharacterStyle = paramCharacterStyle1; ; localCharacterStyle = paramCharacterStyle2)
      {
        break label1325;
        bool1 = false;
        break label1362;
        break;
      }
      label1404: localObject5 = null;
    }
  }

  static Settings getSettings(Context paramContext, Cursor paramCursor)
  {
    Settings localSettings = new Settings();
    paramCursor.moveToNext();
    Settings.access$202(localSettings, Sets.newHashSet(TextUtils.split(paramCursor.getString(0), SPACE_SEPARATOR_PATTERN)));
    Settings.access$302(localSettings, Sets.newHashSet(TextUtils.split(paramCursor.getString(1), SPACE_SEPARATOR_PATTERN)));
    String str1 = paramCursor.getString(2);
    long l1;
    String str2;
    if (str1 != null)
    {
      l1 = Long.parseLong(str1);
      Settings.access$402(localSettings, l1);
      str2 = paramCursor.getString(3);
      if (str2 == null)
        break label130;
    }
    label130: for (long l2 = Long.parseLong(str2); ; l2 = Long.valueOf(0L).longValue())
    {
      Settings.access$502(localSettings, l2);
      return localSettings;
      l1 = Long.valueOf(getDefaultConversationAgeDays(paramContext)).longValue();
      break;
    }
  }

  static Uri getSettingsUri(String paramString)
  {
    return getCachedUri(paramString, "/settings/");
  }

  public static Uri getStatusUri(String paramString)
  {
    return getCachedUri(paramString, "/status/");
  }

  public static boolean isDisplayableSystemLabel(String paramString)
  {
    return DISPLAYABLE_SYSTEM_LABELS.contains(paramString);
  }

  public static boolean isImportant(Map<String, Label> paramMap)
  {
    return paramMap.containsKey("^io_im");
  }

  public static boolean isLabelProviderSettable(String paramString)
  {
    return (ASSIGNABLE_BUILTIN_LABELS.contains(paramString)) || (isLabelUserSettable(paramString));
  }

  public static boolean isLabelUserDefined(String paramString)
  {
    return (paramString != null) && (!paramString.startsWith("^"));
  }

  public static boolean isLabelUserSettable(String paramString)
  {
    return (USER_SETTABLE_BUILTIN_LABELS.contains(paramString)) || (isLabelUserDefined(paramString));
  }

  public static boolean isRunningICSOrLater()
  {
    return Build.VERSION.SDK_INT >= 14;
  }

  public static boolean isSystemLabel(String paramString)
  {
    return SYSTEM_LABELS.contains(paramString);
  }

  public static TextUtils.StringSplitter newConversationLabelIdsSplitter()
  {
    return new CommaStringSplitter();
  }

  public static TextUtils.StringSplitter newMessageLabelIdsSplitter()
  {
    return new TextUtils.SimpleStringSplitter(' ');
  }

  private ContentValues settingsToValues(Settings paramSettings)
  {
    ContentValues localContentValues = new ContentValues();
    if (paramSettings.hasLabelsIncludedChanged())
      localContentValues.put("labelsIncluded", TextUtils.join(String.valueOf(' '), paramSettings.mLabelsIncluded));
    if (paramSettings.hasLabelsPartialChanged())
      localContentValues.put("labelsPartial", TextUtils.join(String.valueOf(' '), paramSettings.mLabelsPartial));
    if (paramSettings.hasConversationAgeDaysChanged())
      localContentValues.put("conversationAgeDays", Long.valueOf(paramSettings.mConversationAgeDays));
    if (paramSettings.hasMaxAttachmentSizeMbChanged())
      localContentValues.put("maxAttachmentSize", Long.valueOf(paramSettings.mMaxAttachmentSizeMb));
    return localContentValues;
  }

  private static String toNonnullString(String paramString)
  {
    if (paramString == null)
      paramString = "";
    return paramString;
  }

  private static String urlEncodeLabel(String paramString)
  {
    try
    {
      String str = URLEncoder.encode(paramString, "utf-8");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException(localUnsupportedEncodingException);
    }
  }

  public void addOrRemoveLabelOnConversationBulk(String paramString, ContentValues[] paramArrayOfContentValues)
  {
    addOrRemoveLabelOnConversationBulk(paramString, paramArrayOfContentValues, false);
  }

  public void addOrRemoveLabelOnConversationBulk(String paramString, ContentValues[] paramArrayOfContentValues, boolean paramBoolean)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    Uri localUri = getCachedUri(paramString, "/conversations/labels");
    if (paramBoolean)
      localUri = localUri.buildUpon().appendQueryParameter("suppressUINotifications", Boolean.TRUE.toString()).build();
    this.mContentResolver.bulkInsert(localUri, paramArrayOfContentValues);
  }

  void addOrRemoveLabelOnMessageBulk(String paramString, ContentValues[] paramArrayOfContentValues, boolean paramBoolean)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    Uri localUri = getMessagesUri(paramString).buildUpon().appendEncodedPath("labels").build();
    if (paramBoolean)
      localUri = localUri.buildUpon().appendQueryParameter("suppressUINotifications", Boolean.TRUE.toString()).build();
    this.mContentResolver.bulkInsert(localUri, paramArrayOfContentValues);
  }

  public Settings getBackupSettings(Context paramContext, String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    Cursor localCursor = MailEngine.getOrMakeMailEngine(paramContext, paramString).getPublicSettingsCursor(SETTINGS_PROJECTION);
    try
    {
      Settings localSettings = getSettings(paramContext, localCursor);
      return localSettings;
    }
    finally
    {
      if (localCursor != null)
        localCursor.close();
    }
  }

  public ConversationCursor getConversationCursorForCursor(String paramString, Cursor paramCursor)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    return new ConversationCursor(this, paramString, paramCursor, null);
  }

  public ConversationCursor getDetachedConversationCursorForQuery(String paramString1, String paramString2, BecomeActiveNetworkCursor paramBecomeActiveNetworkCursor)
  {
    String[] arrayOfString = getSelectionArguments(paramBecomeActiveNetworkCursor);
    return new DetachedConversationCursor(this, paramString1, this.mContentResolver.query(getConversationsUri(paramString1), CONVERSATION_PROJECTION, paramString2, arrayOfString, null));
  }

  public MessageCursor getDetachedMessageCursorForConversationId(String paramString, long paramLong)
  {
    Cursor localCursor = getRawCursorForConversationId(paramString, paramLong);
    return new DetachedMessageCursor(this.mContentResolver, paramString, localCursor);
  }

  @Deprecated
  public LabelMap getLabelMap(String paramString)
  {
    return MailProvider.getMailProvider().getOrMakeMailEngine(paramString).getLabelMap();
  }

  public MessageCursor getMessageCursorForConversationId(String paramString, long paramLong)
  {
    Cursor localCursor = getRawCursorForConversationId(paramString, paramLong);
    return new MessageCursor(this.mContentResolver, paramString, localCursor, null);
  }

  public MessageCursor getMessageCursorForMessageId(String paramString, long paramLong)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    Uri localUri = getMessageByIdUri(paramString, paramLong);
    Cursor localCursor = this.mContentResolver.query(localUri, MESSAGE_PROJECTION, null, null, null);
    return new MessageCursor(this.mContentResolver, paramString, localCursor, null);
  }

  public Settings getSettings(Context paramContext, String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    Cursor localCursor = this.mContentResolver.query(getSettingsUri(paramString), SETTINGS_PROJECTION, null, null, null);
    try
    {
      Settings localSettings = getSettings(paramContext, localCursor);
      return localSettings;
    }
    finally
    {
      if (localCursor != null)
        localCursor.close();
    }
  }

  public void restoreSettings(Context paramContext, String paramString, Settings paramSettings)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    Settings localSettings = new Settings();
    localSettings.setLabelsIncluded(paramSettings.getLabelsIncluded());
    localSettings.setLabelsPartial(paramSettings.getLabelsPartial());
    localSettings.setConversationAgeDays(paramSettings.getConversationAgeDays());
    localSettings.setMaxAttachmentSizeMb(paramSettings.getMaxAttachmentSizeMb());
    MailEngine localMailEngine = MailEngine.getOrMakeMailEngine(paramContext, paramString);
    ContentValues localContentValues = settingsToValues(localSettings);
    if (localContentValues.size() > 0)
      localMailEngine.setPublicSettings(localContentValues);
  }

  public void setSettings(String paramString, Settings paramSettings)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    ContentValues localContentValues = settingsToValues(paramSettings);
    if (localContentValues.size() > 0)
    {
      this.mContentResolver.update(getSettingsUri(paramString), localContentValues, null, null);
      GmailBackupAgent.dataChanged("Sync settings ");
    }
  }

  public static final class Attachment
    implements UIAttachment, Serializable
  {
    private static final long serialVersionUID = 1L;
    public String cachedContent;
    public String contentType;
    public String name;
    public Gmail.AttachmentOrigin origin;
    public String originExtras;
    public String partId;
    public String simpleContentType;
    public int size;
    private Integer transientDestination;
    private String transientSavedFileUri;
    private Integer transientStatus;

    // ERROR //
    public static Attachment parseJoinedString(String paramString)
    {
      // Byte code:
      //   0: aload_0
      //   1: ldc 39
      //   3: invokestatic 45	android/text/TextUtils:split	(Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;
      //   6: astore_1
      //   7: aload_1
      //   8: arraylength
      //   9: bipush 7
      //   11: if_icmpge +5 -> 16
      //   14: aconst_null
      //   15: areturn
      //   16: new 2	com/google/android/gm/provider/Gmail$Attachment
      //   19: dup
      //   20: invokespecial 46	com/google/android/gm/provider/Gmail$Attachment:<init>	()V
      //   23: astore_2
      //   24: iconst_0
      //   25: iconst_1
      //   26: iadd
      //   27: istore_3
      //   28: aload_2
      //   29: aload_1
      //   30: iconst_0
      //   31: aaload
      //   32: putfield 48	com/google/android/gm/provider/Gmail$Attachment:partId	Ljava/lang/String;
      //   35: iload_3
      //   36: iconst_1
      //   37: iadd
      //   38: istore 4
      //   40: aload_2
      //   41: aload_1
      //   42: iload_3
      //   43: aaload
      //   44: putfield 50	com/google/android/gm/provider/Gmail$Attachment:name	Ljava/lang/String;
      //   47: aload_2
      //   48: getfield 50	com/google/android/gm/provider/Gmail$Attachment:name	Ljava/lang/String;
      //   51: astore 5
      //   53: iload 4
      //   55: iconst_1
      //   56: iadd
      //   57: istore 6
      //   59: aload_2
      //   60: aload 5
      //   62: aload_1
      //   63: iload 4
      //   65: aaload
      //   66: invokestatic 56	com/google/android/gm/provider/MimeType:inferMimeType	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   69: putfield 58	com/google/android/gm/provider/Gmail$Attachment:contentType	Ljava/lang/String;
      //   72: iload 6
      //   74: iconst_1
      //   75: iadd
      //   76: istore 7
      //   78: aload_2
      //   79: aload_1
      //   80: iload 6
      //   82: aaload
      //   83: invokestatic 64	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   86: putfield 66	com/google/android/gm/provider/Gmail$Attachment:size	I
      //   89: aload_2
      //   90: getfield 50	com/google/android/gm/provider/Gmail$Attachment:name	Ljava/lang/String;
      //   93: astore 9
      //   95: iload 7
      //   97: iconst_1
      //   98: iadd
      //   99: istore 10
      //   101: aload_2
      //   102: aload 9
      //   104: aload_1
      //   105: iload 7
      //   107: aaload
      //   108: invokestatic 56	com/google/android/gm/provider/MimeType:inferMimeType	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   111: putfield 68	com/google/android/gm/provider/Gmail$Attachment:simpleContentType	Ljava/lang/String;
      //   114: aload_2
      //   115: aload_1
      //   116: iload 10
      //   118: aaload
      //   119: invokestatic 74	com/google/android/gm/provider/Gmail$AttachmentOrigin:valueOf	(Ljava/lang/String;)Lcom/google/android/gm/provider/Gmail$AttachmentOrigin;
      //   122: putfield 76	com/google/android/gm/provider/Gmail$Attachment:origin	Lcom/google/android/gm/provider/Gmail$AttachmentOrigin;
      //   125: iload 10
      //   127: iconst_1
      //   128: iadd
      //   129: istore 12
      //   131: iload 12
      //   133: iconst_1
      //   134: iadd
      //   135: istore 13
      //   137: aload_2
      //   138: aload_1
      //   139: iload 12
      //   141: aaload
      //   142: putfield 78	com/google/android/gm/provider/Gmail$Attachment:originExtras	Ljava/lang/String;
      //   145: aload_1
      //   146: arraylength
      //   147: bipush 7
      //   149: if_icmple +54 -> 203
      //   152: iload 13
      //   154: iconst_1
      //   155: iadd
      //   156: pop
      //   157: aload_2
      //   158: aload_1
      //   159: iload 13
      //   161: aaload
      //   162: putfield 80	com/google/android/gm/provider/Gmail$Attachment:cachedContent	Ljava/lang/String;
      //   165: aload_2
      //   166: areturn
      //   167: astore 8
      //   169: aconst_null
      //   170: areturn
      //   171: astore 11
      //   173: aload_1
      //   174: iload 10
      //   176: aaload
      //   177: invokestatic 64	java/lang/Integer:parseInt	(Ljava/lang/String;)I
      //   180: ifne +13 -> 193
      //   183: aload_2
      //   184: getstatic 83	com/google/android/gm/provider/Gmail$AttachmentOrigin:SERVER_ATTACHMENT	Lcom/google/android/gm/provider/Gmail$AttachmentOrigin;
      //   187: putfield 76	com/google/android/gm/provider/Gmail$Attachment:origin	Lcom/google/android/gm/provider/Gmail$AttachmentOrigin;
      //   190: goto -65 -> 125
      //   193: aload_2
      //   194: getstatic 86	com/google/android/gm/provider/Gmail$AttachmentOrigin:LOCAL_FILE	Lcom/google/android/gm/provider/Gmail$AttachmentOrigin;
      //   197: putfield 76	com/google/android/gm/provider/Gmail$Attachment:origin	Lcom/google/android/gm/provider/Gmail$AttachmentOrigin;
      //   200: goto -75 -> 125
      //   203: aload_2
      //   204: areturn
      //
      // Exception table:
      //   from	to	target	type
      //   78	89	167	java/lang/NumberFormatException
      //   114	125	171	java/lang/IllegalArgumentException
    }

    public boolean downloadCompletedSuccessfully()
    {
      return (getStatus() == 3) || ((!TextUtils.isEmpty(this.transientSavedFileUri)) && (this.transientStatus != null) && (this.transientStatus.intValue() == 3));
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this);
      Attachment localAttachment;
      do
      {
        return true;
        if ((paramObject == null) || (paramObject.getClass() != getClass()))
          return false;
        localAttachment = (Attachment)paramObject;
      }
      while ((this.partId != null) && (localAttachment.partId != null) && (this.partId.equals(localAttachment.partId)) && (this.name.equals(localAttachment.name)) && (this.contentType.equals(localAttachment.contentType)) && (this.size == localAttachment.size) && (this.origin == localAttachment.origin) && (this.originExtras.equals(localAttachment.originExtras)));
      return false;
    }

    public String getContentType()
    {
      return MimeType.inferMimeType(this.name, this.contentType);
    }

    public int getDestination()
    {
      if (this.transientDestination != null)
        return this.transientDestination.intValue();
      if (TextUtils.isEmpty(this.partId))
        return 1;
      return 0;
    }

    public int getDownloadedSize()
    {
      return 0;
    }

    public Uri getExternalFilePath()
    {
      if (this.transientSavedFileUri != null)
        return Uri.parse(this.transientSavedFileUri);
      return null;
    }

    public String getJoinedAttachmentInfo()
    {
      return toJoinedString();
    }

    public String getName()
    {
      return this.name;
    }

    public Gmail.AttachmentOrigin getOrigin()
    {
      return this.origin;
    }

    public String getOriginExtras()
    {
      return this.originExtras;
    }

    public Attachment getOriginal()
    {
      return this;
    }

    public String getPartId()
    {
      return this.partId;
    }

    public int getSize()
    {
      return this.size;
    }

    public int getStatus()
    {
      if (this.transientStatus != null)
        return this.transientStatus.intValue();
      if (TextUtils.isEmpty(this.partId))
        return 3;
      return 0;
    }

    public int hashCode()
    {
      Object[] arrayOfObject = new Object[6];
      arrayOfObject[0] = this.partId;
      arrayOfObject[1] = this.name;
      arrayOfObject[2] = this.contentType;
      arrayOfObject[3] = Integer.valueOf(this.size);
      arrayOfObject[4] = this.origin;
      arrayOfObject[5] = this.originExtras;
      return Objects.hashCode(arrayOfObject);
    }

    public boolean isDownloading()
    {
      return getStatus() == 2;
    }

    public boolean supportsOrigin()
    {
      return true;
    }

    public String toJoinedString()
    {
      Serializable[] arrayOfSerializable = new Serializable[8];
      String str1;
      String str2;
      if (this.partId == null)
      {
        str1 = "";
        arrayOfSerializable[0] = str1;
        if (this.name != null)
          break label111;
        str2 = "";
        label30: arrayOfSerializable[1] = str2;
        arrayOfSerializable[2] = this.contentType;
        arrayOfSerializable[3] = Integer.valueOf(this.size);
        arrayOfSerializable[4] = this.simpleContentType;
        arrayOfSerializable[5] = this.origin.toString();
        arrayOfSerializable[6] = this.originExtras;
        if (this.cachedContent != null)
          break label126;
      }
      label111: label126: for (String str3 = ""; ; str3 = this.cachedContent)
      {
        arrayOfSerializable[7] = str3;
        return TextUtils.join("|", Lists.newArrayList(arrayOfSerializable));
        str1 = this.partId;
        break;
        str2 = this.name.replaceAll("[|\n]", "");
        break label30;
      }
    }

    public String toString()
    {
      return "Attachment{contentType='" + this.contentType + '\'' + ", partId='" + this.partId + '\'' + ", name='" + this.name + '\'' + ", size=" + this.size + ", simpleContentType='" + this.simpleContentType + '\'' + ", origin=" + this.origin + ", originExtras='" + this.originExtras + '\'' + ", cachedContent='" + this.cachedContent + '\'' + '}';
    }

    public void updateState(int paramInt1, int paramInt2, String paramString)
    {
      this.transientStatus = Integer.valueOf(paramInt1);
      this.transientDestination = Integer.valueOf(paramInt2);
      this.transientSavedFileUri = paramString;
    }
  }

  public static enum AttachmentOrigin
  {
    static
    {
      LOCAL_FILE = new AttachmentOrigin("LOCAL_FILE", 1);
      AttachmentOrigin[] arrayOfAttachmentOrigin = new AttachmentOrigin[2];
      arrayOfAttachmentOrigin[0] = SERVER_ATTACHMENT;
      arrayOfAttachmentOrigin[1] = LOCAL_FILE;
    }

    public static String serverExtras(long paramLong1, long paramLong2, String paramString)
    {
      return paramLong1 + "_" + paramLong2 + "_" + paramString;
    }

    public static String[] splitServerExtras(String paramString)
    {
      return TextUtils.split(paramString, "_");
    }
  }

  public static enum AttachmentRendition
  {
    static
    {
      AttachmentRendition[] arrayOfAttachmentRendition = new AttachmentRendition[2];
      arrayOfAttachmentRendition[0] = BEST;
      arrayOfAttachmentRendition[1] = SIMPLE;
    }
  }

  public static enum BecomeActiveNetworkCursor
  {
    static
    {
      NO = new BecomeActiveNetworkCursor("NO", 1);
      BecomeActiveNetworkCursor[] arrayOfBecomeActiveNetworkCursor = new BecomeActiveNetworkCursor[2];
      arrayOfBecomeActiveNetworkCursor[0] = YES;
      arrayOfBecomeActiveNetworkCursor[1] = NO;
    }
  }

  private static class CommaStringSplitter extends TextUtils.SimpleStringSplitter
  {
    public CommaStringSplitter()
    {
      super();
    }

    public void setString(String paramString)
    {
      super.setString(paramString.substring(1));
    }
  }

  public static class ConversationCursor extends Gmail.MailCursor
  {
    private final int mConversationIdIndex = this.mCursor.getColumnIndexOrThrow("_id");
    private final int mDateIndex = this.mCursor.getColumnIndex("date");
    private final int mForceAllUnreadIndex = this.mCursor.getColumnIndexOrThrow("forceAllUnread");
    private final int mFromIndex = this.mCursor.getColumnIndexOrThrow("fromAddress");
    private final int mHasAttachmentsIndex = this.mCursor.getColumnIndexOrThrow("hasAttachments");
    private final int mHasMessagesWithErrorsIndex = this.mCursor.getColumnIndexOrThrow("hasMessagesWithErrors");
    private final int mLabelIdsIndex = this.mCursor.getColumnIndex("labelIds");
    private final TextUtils.StringSplitter mLabelIdsSplitter = Gmail.newConversationLabelIdsSplitter();
    private final int mLabelsIndex = this.mCursor.getColumnIndex("conversationLabels");
    private final int mMaxMessageIdIndex = this.mCursor.getColumnIndexOrThrow("maxMessageId");
    private final int mNumMessagesIndex = this.mCursor.getColumnIndexOrThrow("numMessages");
    private final LruCache<Long, Map<String, Label>> mParsedLabels = new LruCache(50);
    private final int mPersonalLevelIndex = this.mCursor.getColumnIndexOrThrow("personalLevel");
    private final int mSnippetIndex = this.mCursor.getColumnIndexOrThrow("snippet");
    private final int mSubjectIndex = this.mCursor.getColumnIndexOrThrow("subject");
    private final int mSyncedIndex = this.mCursor.getColumnIndex("synced");

    static
    {
      if (!Gmail.class.desiredAssertionStatus());
      for (boolean bool = true; ; bool = false)
      {
        $assertionsDisabled = bool;
        return;
      }
    }

    private ConversationCursor(Handler paramHandler, Gmail paramGmail, String paramString, Cursor paramCursor)
    {
      super(paramString, paramCursor);
    }

    private ConversationCursor(Gmail paramGmail, String paramString, Cursor paramCursor)
    {
      this(new Handler(), paramGmail, paramString, paramCursor);
    }

    public long getConversationId()
    {
      return this.mCursor.getLong(this.mConversationIdIndex);
    }

    public long getDateMs()
    {
      if (this.mDateIndex > -1)
        return this.mCursor.getLong(this.mDateIndex);
      return 0L;
    }

    public boolean getForceAllUnread()
    {
      return (!this.mCursor.isNull(this.mForceAllUnreadIndex)) && (this.mCursor.getInt(this.mForceAllUnreadIndex) != 0);
    }

    public String getFromSnippetInstructions()
    {
      return getStringInColumn(this.mFromIndex);
    }

    public boolean getHasDraftMessage()
    {
      String str = getRawLabels();
      if (str != null)
        return str.contains("^*^^r^*^");
      return false;
    }

    public boolean getHasUnreadMessage()
    {
      String str = getRawLabels();
      if (str != null)
        return str.contains("^*^^u^*^");
      return false;
    }

    public Set<Long> getLabelIds()
    {
      if (this.mLabelIdsIndex != -1)
      {
        String str = this.mCursor.getString(this.mLabelIdsIndex);
        TextUtils.StringSplitter localStringSplitter = this.mLabelIdsSplitter;
        if (str == null)
          str = "";
        localStringSplitter.setString(str);
        return Gmail.getLabelIdsFromLabelIdsString(this.mLabelIdsSplitter);
      }
      return Gmail.getLabelIdsFromLabelMap(getLabels());
    }

    public Map<String, Label> getLabels()
    {
      long l = getConversationId();
      synchronized (this.mParsedLabels)
      {
        Map localMap = (Map)this.mParsedLabels.get(Long.valueOf(getConversationId()));
        if (localMap == null)
        {
          localMap = LabelManager.parseLabelQueryResult(this.mAccount, getRawLabels());
          this.mParsedLabels.put(Long.valueOf(l), localMap);
        }
        return localMap;
      }
    }

    MailEngine.ConversationCursorLogic getLogic()
    {
      return (MailEngine.ConversationCursorLogic)((MailEngine.NetworkCursor)this.mCursor).mLogic;
    }

    public long getMaxServerMessageId()
    {
      return this.mCursor.getLong(this.mMaxMessageIdIndex);
    }

    public int getNumMessages()
    {
      return this.mCursor.getInt(this.mNumMessagesIndex);
    }

    public Gmail.PersonalLevel getPersonalLevel()
    {
      return Gmail.PersonalLevel.fromInt(this.mCursor.getInt(this.mPersonalLevelIndex));
    }

    public String getRawLabels()
    {
      if (this.mLabelsIndex > -1)
        return this.mCursor.getString(this.mLabelsIndex);
      return "";
    }

    public String getSnippet()
    {
      return getStringInColumn(this.mSnippetIndex);
    }

    public String getSubject()
    {
      return getStringInColumn(this.mSubjectIndex);
    }

    public boolean hasAttachments()
    {
      return this.mCursor.getInt(this.mHasAttachmentsIndex) != 0;
    }

    public boolean isSynced()
    {
      int i = this.mSyncedIndex;
      boolean bool = false;
      if (i > -1)
      {
        int j = this.mCursor.getInt(this.mSyncedIndex);
        bool = false;
        if (j != 0)
          bool = true;
      }
      return bool;
    }

    protected void onCursorPositionChanged()
    {
      super.onCursorPositionChanged();
    }
  }

  public static enum CursorError
  {
    static
    {
      IO_ERROR = new CursorError("IO_ERROR", 1);
      AUTH_ERROR = new CursorError("AUTH_ERROR", 2);
      PARSE_ERROR = new CursorError("PARSE_ERROR", 3);
      DB_ERROR = new CursorError("DB_ERROR", 4);
      UNKNOWN_ERROR = new CursorError("UNKNOWN_ERROR", 5);
      CursorError[] arrayOfCursorError = new CursorError[6];
      arrayOfCursorError[0] = NO_ERROR;
      arrayOfCursorError[1] = IO_ERROR;
      arrayOfCursorError[2] = AUTH_ERROR;
      arrayOfCursorError[3] = PARSE_ERROR;
      arrayOfCursorError[4] = DB_ERROR;
      arrayOfCursorError[5] = UNKNOWN_ERROR;
    }
  }

  public static enum CursorStatus
  {
    static
    {
      COMPLETE = new CursorStatus("COMPLETE", 2);
      ERROR = new CursorStatus("ERROR", 3);
      SEARCHING = new CursorStatus("SEARCHING", 4);
      CursorStatus[] arrayOfCursorStatus = new CursorStatus[5];
      arrayOfCursorStatus[0] = LOADED;
      arrayOfCursorStatus[1] = LOADING;
      arrayOfCursorStatus[2] = COMPLETE;
      arrayOfCursorStatus[3] = ERROR;
      arrayOfCursorStatus[4] = SEARCHING;
    }
  }

  public static final class DetachedConversationCursor extends Gmail.ConversationCursor
  {
    DetachedConversationCursor(Gmail paramGmail, String paramString, Cursor paramCursor)
    {
      super(paramGmail, paramString, paramCursor, null);
    }

    public void onChange(boolean paramBoolean)
    {
    }
  }

  public static final class DetachedMessageCursor extends Gmail.MessageCursor
  {
    public DetachedMessageCursor(ContentResolver paramContentResolver, String paramString, Cursor paramCursor)
    {
      super(paramContentResolver, paramString, paramCursor);
    }

    public void onChange(boolean paramBoolean)
    {
    }
  }

  public static final class LabelMap extends Observable
  {
    private static final ContentValues EMPTY_CONTENT_VALUES = new ContentValues();
    private Map<String, Long> mCanonicalNameToId;
    private boolean mContentQueryMapPopulated = false;
    private long mLabelIdCached;
    private long mLabelIdChat;
    private long mLabelIdDraft;
    private long mLabelIdIgnored;
    private long mLabelIdImportantImap;
    private long mLabelIdInbox;
    private long mLabelIdMarkImportant;
    private long mLabelIdMarkUnimportant;
    private long mLabelIdOutbox;
    private long mLabelIdSent;
    private long mLabelIdSpam;
    private long mLabelIdStarred;
    private long mLabelIdTrash;
    private long mLabelIdUnread;
    private Boolean mLabelsSynced = null;
    private ContentQueryMap mQueryMap;

    public LabelMap(ContentResolver paramContentResolver, Cursor paramCursor, boolean paramBoolean)
    {
      init(paramCursor);
    }

    public LabelMap(ContentResolver paramContentResolver, String paramString)
    {
      if (TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("account is empty");
      init(paramContentResolver.query(Uri.withAppendedPath(Gmail.getLabelsUri(paramString), "1"), Gmail.LABEL_PROJECTION, null, null, null));
    }

    private void checkLabelsSynced()
    {
      if (!labelsSynced())
        throw new IllegalStateException("LabelMap not initalized");
    }

    private ContentValues getLabelIdValues(long paramLong)
    {
      if (this.mContentQueryMapPopulated);
      for (ContentValues localContentValues = this.mQueryMap.getValues(Long.toString(paramLong)); localContentValues != null; localContentValues = null)
        return localContentValues;
      return EMPTY_CONTENT_VALUES;
    }

    private void init(Cursor paramCursor)
    {
      this.mQueryMap = new LabelsContentQueryMap(paramCursor, "_id");
      this.mCanonicalNameToId = Maps.newHashMap();
      this.mQueryMap.addObserver(new Observer()
      {
        public void update(Observable paramAnonymousObservable, Object paramAnonymousObject)
        {
          Gmail.LabelMap.this.updateDataStructures();
          Gmail.LabelMap.this.setChanged();
          Gmail.LabelMap.this.notifyObservers();
        }
      });
    }

    private void updateDataStructures()
    {
      while (true)
      {
        long l;
        String str2;
        boolean bool;
        try
        {
          this.mContentQueryMapPopulated = true;
          this.mCanonicalNameToId.clear();
          Iterator localIterator = this.mQueryMap.getRows().entrySet().iterator();
          if (!localIterator.hasNext())
            break;
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          String str1 = (String)localEntry.getKey();
          if (str1 == null)
            continue;
          l = Long.valueOf(str1).longValue();
          str2 = ((ContentValues)localEntry.getValue()).getAsString("canonicalName");
          this.mCanonicalNameToId.put(str2, Long.valueOf(l));
          if ("^f".equals(str2))
          {
            this.mLabelIdSent = l;
            if ((this.mLabelIdSent == 0L) || (this.mLabelIdInbox == 0L) || (this.mLabelIdDraft == 0L) || (this.mLabelIdUnread == 0L) || (this.mLabelIdTrash == 0L) || (this.mLabelIdSpam == 0L) || (this.mLabelIdStarred == 0L) || (this.mLabelIdChat == 0L) || (this.mLabelIdIgnored == 0L))
              break label479;
            bool = true;
            this.mLabelsSynced = Boolean.valueOf(bool);
            continue;
          }
        }
        finally
        {
        }
        if ("^i".equals(str2))
        {
          this.mLabelIdInbox = l;
        }
        else if ("^r".equals(str2))
        {
          this.mLabelIdDraft = l;
        }
        else if ("^u".equals(str2))
        {
          this.mLabelIdUnread = l;
        }
        else if ("^k".equals(str2))
        {
          this.mLabelIdTrash = l;
        }
        else if ("^s".equals(str2))
        {
          this.mLabelIdSpam = l;
        }
        else if ("^t".equals(str2))
        {
          this.mLabelIdStarred = l;
        }
        else if ("^b".equals(str2))
        {
          this.mLabelIdChat = l;
        }
        else if ("^g".equals(str2))
        {
          this.mLabelIdIgnored = l;
        }
        else if ("^^cached".equals(str2))
        {
          this.mLabelIdCached = l;
        }
        else if ("^^out".equals(str2))
        {
          this.mLabelIdOutbox = l;
        }
        else if ("^io_im".equals(str2))
        {
          this.mLabelIdImportantImap = l;
        }
        else if ("^^important".equals(str2))
        {
          this.mLabelIdMarkImportant = l;
        }
        else if ("^^unimportant".equals(str2))
        {
          this.mLabelIdMarkUnimportant = l;
          continue;
          label479: bool = false;
        }
      }
    }

    public void close()
    {
      this.mQueryMap.close();
    }

    public String getCanonicalName(long paramLong)
    {
      return getLabelIdValues(paramLong).getAsString("canonicalName");
    }

    @Deprecated
    public long getLabelId(String paramString)
      throws IllegalArgumentException
    {
      try
      {
        if (labelPresent(paramString))
        {
          long l = ((Long)this.mCanonicalNameToId.get(paramString)).longValue();
          return l;
        }
        throw new IllegalArgumentException("Unknown canonical name: " + paramString);
      }
      finally
      {
      }
    }

    public long getLabelIdCached()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdCached;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdDraft()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdDraft;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdIgnored()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdIgnored;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdImportantImap()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdImportantImap;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdInbox()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdInbox;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdOutbox()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdOutbox;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdSent()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdSent;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdSpam()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdSpam;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdStarred()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdStarred;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdTrash()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdTrash;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getLabelIdUnread()
    {
      try
      {
        checkLabelsSynced();
        long l = this.mLabelIdUnread;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public String getName(long paramLong)
    {
      return getLabelIdValues(paramLong).getAsString("name");
    }

    public int getNumConversations(long paramLong)
    {
      return getLabelIdValues(paramLong).getAsInteger("numConversations").intValue();
    }

    public int getNumUnreadConversations(long paramLong)
    {
      Integer localInteger = getLabelIdValues(paramLong).getAsInteger("numUnreadConversations");
      if ((localInteger == null) || (localInteger.intValue() < 0))
        return 0;
      return localInteger.intValue();
    }

    ContentQueryMap getQueryMap()
    {
      return this.mQueryMap;
    }

    public boolean labelPresent(String paramString)
    {
      try
      {
        boolean bool = this.mCanonicalNameToId.containsKey(paramString);
        return bool;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public boolean labelsSynced()
    {
      try
      {
        if (this.mLabelsSynced != null)
        {
          boolean bool2 = this.mLabelsSynced.booleanValue();
          if (bool2)
          {
            bool1 = true;
            return bool1;
          }
        }
        boolean bool1 = false;
      }
      finally
      {
      }
    }

    boolean labelsSynchronizationStateInitialized()
    {
      try
      {
        Boolean localBoolean = this.mLabelsSynced;
        if (localBoolean != null)
        {
          bool = true;
          return bool;
        }
        boolean bool = false;
      }
      finally
      {
      }
    }

    public void requery()
    {
      this.mQueryMap.requery();
    }

    static class LabelsContentQueryMap extends ContentQueryMap
    {
      public LabelsContentQueryMap(Cursor paramCursor, String paramString)
      {
        super(paramString, true, null);
        super.setKeepUpdated(false);
      }

      public void setKeepUpdated(boolean paramBoolean)
      {
      }
    }
  }

  public static class MailCursor extends ContentObserver
  {
    protected String mAccount;
    protected Cursor mCursor;
    private final Set<Gmail.MailCursorObserver> mObservers = new HashSet();
    private ContentValues mUpdateValues;

    protected MailCursor(Handler paramHandler, String paramString, Cursor paramCursor)
    {
      super();
      this.mCursor = paramCursor;
      this.mAccount = paramString;
      if (this.mCursor != null)
        this.mCursor.registerContentObserver(this);
    }

    protected void checkCursor()
    {
      if (this.mCursor == null)
        throw new IllegalStateException("cannot read from an insertion cursor");
    }

    protected void checkThread()
    {
    }

    public void close()
    {
      release();
      if ((this.mCursor != null) && (!this.mCursor.isClosed()))
        this.mCursor.close();
    }

    public final int count()
    {
      if (this.mCursor != null)
        return this.mCursor.getCount();
      return 0;
    }

    public final void deactivate()
    {
      if (this.mCursor != null)
        this.mCursor.deactivate();
    }

    public final boolean deliverSelfNotifications()
    {
      return false;
    }

    public String getAccount()
    {
      return this.mAccount;
    }

    protected String getStringInColumn(int paramInt)
    {
      checkCursor();
      return Gmail.toNonnullString(this.mCursor.getString(paramInt));
    }

    protected ContentValues getUpdateValues()
    {
      if (this.mUpdateValues == null)
        this.mUpdateValues = new ContentValues();
      return this.mUpdateValues;
    }

    public final boolean isLast()
    {
      return (this.mCursor != null) && (this.mCursor.isLast());
    }

    public final boolean moveTo(int paramInt)
    {
      checkCursor();
      checkThread();
      boolean bool = this.mCursor.moveToPosition(paramInt);
      if (bool)
        onCursorPositionChanged();
      return bool;
    }

    public final boolean next()
    {
      checkCursor();
      checkThread();
      boolean bool = this.mCursor.moveToNext();
      if (bool)
        onCursorPositionChanged();
      return bool;
    }

    public void onChange(boolean paramBoolean)
    {
      Iterator localIterator = this.mObservers.iterator();
      while (localIterator.hasNext())
        ((Gmail.MailCursorObserver)localIterator.next()).onCursorChanged(this);
    }

    protected void onCursorPositionChanged()
    {
      this.mUpdateValues = null;
    }

    public final int position()
    {
      if (this.mCursor != null)
        return this.mCursor.getPosition();
      return -1;
    }

    public final void release()
    {
      if (this.mCursor != null)
      {
        this.mCursor.unregisterContentObserver(this);
        this.mCursor.deactivate();
      }
    }

    public void requery()
    {
      this.mCursor.requery();
    }
  }

  public static abstract interface MailCursorObserver
  {
    public abstract void onCursorChanged(Gmail.MailCursor paramMailCursor);
  }

  public static class MessageCursor extends Gmail.MailCursor
  {
    private int mBccIndex;
    private int mBodyEmbedsExternalResourcesIndex;
    private int mBodyIndex;
    private int mCcIndex;
    private final ContentResolver mContentResolver;
    private int mConversationIdIndex;
    private boolean mCursorCloned = false;
    private int mCustomFromIndex;
    private int mDateReceivedMsIndex;
    private int mDateSentMsIndex;
    private int mErrorIndex;
    private int mForwardIndex;
    private int mFromIndex;
    private int mIdIndex;
    long mInReplyToLocalMessageId;
    private int mIncludeQuotedTextIndex;
    private int mIsDraftIndex;
    private int mIsInOutboxIndex;
    private int mIsStarredIndex;
    private int mIsUnreadIndex;
    private int mJoinedAttachmentInfosIndex;
    private int mLabelCountIndex;
    private int mLabelIdsIndex;
    private final TextUtils.StringSplitter mLabelIdsSplitter = Gmail.newMessageLabelIdsSplitter();
    private int mLabelsIndex;
    private int mListInfoIndex;
    private int mPersonalLevelIndex;
    boolean mPreserveAttachments;
    private int mQuoteStartPosIndex;
    private int mRefMessageIdIndex;
    private int mReplyToIndex;
    private int mServerMessageIdIndex;
    private int mSnippetIndex;
    private int mSubjectIndex;
    private int mToIndex;

    static
    {
      if (!Gmail.class.desiredAssertionStatus());
      for (boolean bool = true; ; bool = false)
      {
        $assertionsDisabled = bool;
        return;
      }
    }

    private MessageCursor(ContentResolver paramContentResolver, String paramString, Cursor paramCursor)
    {
      this(new Handler(), paramContentResolver, paramString, paramCursor);
    }

    public MessageCursor(Handler paramHandler, ContentResolver paramContentResolver, String paramString, Cursor paramCursor)
    {
      super(paramString, paramCursor);
      if (paramCursor == null)
        throw new IllegalArgumentException("null cursor passed to MessageCursor()");
      this.mContentResolver = paramContentResolver;
      this.mIdIndex = this.mCursor.getColumnIndexOrThrow("_id");
      this.mServerMessageIdIndex = this.mCursor.getColumnIndexOrThrow("messageId");
      this.mConversationIdIndex = this.mCursor.getColumnIndexOrThrow("conversation");
      this.mSubjectIndex = this.mCursor.getColumnIndexOrThrow("subject");
      this.mSnippetIndex = this.mCursor.getColumnIndexOrThrow("snippet");
      this.mFromIndex = this.mCursor.getColumnIndexOrThrow("fromAddress");
      this.mCustomFromIndex = this.mCursor.getColumnIndexOrThrow("customFromAddress");
      this.mToIndex = this.mCursor.getColumnIndexOrThrow("toAddresses");
      this.mCcIndex = this.mCursor.getColumnIndexOrThrow("ccAddresses");
      this.mBccIndex = this.mCursor.getColumnIndexOrThrow("bccAddresses");
      this.mReplyToIndex = this.mCursor.getColumnIndexOrThrow("replyToAddresses");
      this.mDateSentMsIndex = this.mCursor.getColumnIndexOrThrow("dateSentMs");
      this.mDateReceivedMsIndex = this.mCursor.getColumnIndexOrThrow("dateReceivedMs");
      this.mListInfoIndex = this.mCursor.getColumnIndexOrThrow("listInfo");
      this.mPersonalLevelIndex = this.mCursor.getColumnIndexOrThrow("personalLevel");
      this.mBodyIndex = this.mCursor.getColumnIndexOrThrow("body");
      this.mBodyEmbedsExternalResourcesIndex = this.mCursor.getColumnIndexOrThrow("bodyEmbedsExternalResources");
      this.mLabelIdsIndex = this.mCursor.getColumnIndexOrThrow("labelIds");
      this.mJoinedAttachmentInfosIndex = this.mCursor.getColumnIndexOrThrow("joinedAttachmentInfos");
      this.mErrorIndex = this.mCursor.getColumnIndexOrThrow("error");
      this.mRefMessageIdIndex = this.mCursor.getColumnIndexOrThrow("refMessageId");
      this.mForwardIndex = this.mCursor.getColumnIndexOrThrow("forward");
      this.mIncludeQuotedTextIndex = this.mCursor.getColumnIndexOrThrow("includeQuotedText");
      this.mQuoteStartPosIndex = this.mCursor.getColumnIndexOrThrow("quoteStartPos");
      this.mLabelCountIndex = this.mCursor.getColumnIndexOrThrow("labelCount");
      this.mLabelsIndex = this.mCursor.getColumnIndexOrThrow("messageLabels");
      this.mIsStarredIndex = this.mCursor.getColumnIndexOrThrow("isStarred");
      this.mIsDraftIndex = this.mCursor.getColumnIndexOrThrow("isDraft");
      this.mIsInOutboxIndex = this.mCursor.getColumnIndexOrThrow("isInOutbox");
      this.mIsUnreadIndex = this.mCursor.getColumnIndexOrThrow("isUnread");
      this.mInReplyToLocalMessageId = 0L;
      this.mPreserveAttachments = false;
    }

    private String[] getAddresses(String paramString, int paramInt)
    {
      if (getUpdateValues().containsKey(paramString));
      for (String str = (String)getUpdateValues().get(paramString); ; str = getStringInColumn(paramInt))
        return TextUtils.split(str, Gmail.EMAIL_SEPARATOR_PATTERN);
    }

    public void addOrRemoveLabel(String paramString, boolean paramBoolean)
    {
      Gmail.addOrRemoveLabelOnMessage(this.mContentResolver, this.mAccount, getConversationId(), getLocalId(), paramString, paramBoolean);
    }

    public Object clone()
    {
      if (this.mCursor == null)
        return new MessageCursor(new Handler(), this.mContentResolver, this.mAccount, null);
      MatrixCursor localMatrixCursor = new MatrixCursor(this.mCursor.getColumnNames(), this.mCursor.getCount());
      this.mCursor.moveToPosition(-1);
      int i = this.mCursor.getColumnCount();
      while (this.mCursor.moveToNext())
      {
        Object[] arrayOfObject = new Object[i];
        arrayOfObject[this.mIdIndex] = Long.valueOf(this.mCursor.getLong(this.mIdIndex));
        arrayOfObject[this.mServerMessageIdIndex] = Long.valueOf(this.mCursor.getLong(this.mServerMessageIdIndex));
        arrayOfObject[this.mConversationIdIndex] = Long.valueOf(this.mCursor.getLong(this.mConversationIdIndex));
        arrayOfObject[this.mSubjectIndex] = this.mCursor.getString(this.mSubjectIndex);
        arrayOfObject[this.mSnippetIndex] = this.mCursor.getString(this.mSnippetIndex);
        arrayOfObject[this.mFromIndex] = this.mCursor.getString(this.mFromIndex);
        arrayOfObject[this.mCustomFromIndex] = this.mCursor.getString(this.mCustomFromIndex);
        arrayOfObject[this.mToIndex] = this.mCursor.getString(this.mToIndex);
        arrayOfObject[this.mCcIndex] = this.mCursor.getString(this.mCcIndex);
        arrayOfObject[this.mBccIndex] = this.mCursor.getString(this.mBccIndex);
        arrayOfObject[this.mReplyToIndex] = this.mCursor.getString(this.mReplyToIndex);
        arrayOfObject[this.mDateSentMsIndex] = Long.valueOf(this.mCursor.getLong(this.mDateSentMsIndex));
        arrayOfObject[this.mDateReceivedMsIndex] = Long.valueOf(this.mCursor.getLong(this.mDateSentMsIndex));
        arrayOfObject[this.mListInfoIndex] = this.mCursor.getString(this.mListInfoIndex);
        arrayOfObject[this.mPersonalLevelIndex] = Integer.valueOf(this.mCursor.getInt(this.mPersonalLevelIndex));
        arrayOfObject[this.mBodyIndex] = this.mCursor.getString(this.mBodyIndex);
        arrayOfObject[this.mBodyEmbedsExternalResourcesIndex] = Integer.valueOf(this.mCursor.getInt(this.mBodyEmbedsExternalResourcesIndex));
        arrayOfObject[this.mLabelIdsIndex] = this.mCursor.getString(this.mLabelIdsIndex);
        arrayOfObject[this.mJoinedAttachmentInfosIndex] = this.mCursor.getString(this.mJoinedAttachmentInfosIndex);
        arrayOfObject[this.mErrorIndex] = this.mCursor.getString(this.mErrorIndex);
        arrayOfObject[this.mRefMessageIdIndex] = Long.valueOf(this.mCursor.getLong(this.mRefMessageIdIndex));
        arrayOfObject[this.mForwardIndex] = Long.valueOf(this.mCursor.getLong(this.mForwardIndex));
        arrayOfObject[this.mLabelCountIndex] = Integer.valueOf(this.mCursor.getInt(this.mLabelCountIndex));
        arrayOfObject[this.mLabelsIndex] = this.mCursor.getString(this.mLabelsIndex);
        arrayOfObject[this.mIsStarredIndex] = Long.valueOf(this.mCursor.getLong(this.mIsStarredIndex));
        arrayOfObject[this.mIsDraftIndex] = Long.valueOf(this.mCursor.getLong(this.mIsDraftIndex));
        arrayOfObject[this.mIsInOutboxIndex] = Long.valueOf(this.mCursor.getLong(this.mIsInOutboxIndex));
        arrayOfObject[this.mIsUnreadIndex] = Long.valueOf(this.mCursor.getLong(this.mIsUnreadIndex));
        arrayOfObject[this.mIncludeQuotedTextIndex] = Long.valueOf(this.mCursor.getLong(this.mIncludeQuotedTextIndex));
        arrayOfObject[this.mQuoteStartPosIndex] = Integer.valueOf(this.mCursor.getInt(this.mQuoteStartPosIndex));
        localMatrixCursor.addRow(arrayOfObject);
      }
      MessageCursor localMessageCursor = new MessageCursor(new Handler(), this.mContentResolver, this.mAccount, localMatrixCursor);
      localMessageCursor.mCursorCloned = true;
      return localMessageCursor;
    }

    public ArrayList<Gmail.Attachment> getAttachmentInfos()
    {
      ArrayList localArrayList = Lists.newArrayList();
      localArrayList.addAll(Gmail.MessageModification.parseJoinedAttachmentString(this.mCursor.getString(this.mJoinedAttachmentInfosIndex)));
      return localArrayList;
    }

    public String[] getBccAddresses()
    {
      return getAddresses("bccAddresses", this.mBccIndex);
    }

    public String getBody()
    {
      return getStringInColumn(this.mBodyIndex);
    }

    public String[] getCcAddresses()
    {
      return getAddresses("ccAddresses", this.mCcIndex);
    }

    public long getConversationId()
    {
      checkCursor();
      return this.mCursor.getLong(this.mConversationIdIndex);
    }

    public long getDateSentMs()
    {
      checkCursor();
      return this.mCursor.getLong(this.mDateSentMsIndex);
    }

    public String getErrorText()
    {
      return this.mCursor.getString(this.mErrorIndex);
    }

    public String getFromAddress()
    {
      String str1 = getStringInColumn(this.mCustomFromIndex);
      String str2 = getStringInColumn(this.mFromIndex);
      if ((!TextUtils.isEmpty(str1)) && (TextUtils.isEmpty(str2)))
        return str1;
      return getStringInColumn(this.mFromIndex);
    }

    public boolean getIsUnread()
    {
      return this.mCursor.getLong(this.mIsUnreadIndex) != 0L;
    }

    public Set<Long> getLabelIds()
    {
      String str = this.mCursor.getString(this.mLabelIdsIndex);
      TextUtils.StringSplitter localStringSplitter = this.mLabelIdsSplitter;
      if (str == null)
        str = "";
      localStringSplitter.setString(str);
      return Gmail.getLabelIdsFromLabelIdsString(this.mLabelIdsSplitter);
    }

    public String getListInfo()
    {
      return getStringInColumn(this.mListInfoIndex);
    }

    public long getLocalId()
    {
      checkCursor();
      return this.mCursor.getLong(this.mIdIndex);
    }

    public Gmail.PersonalLevel getPersonalLevel()
    {
      checkCursor();
      return Gmail.PersonalLevel.fromInt(this.mCursor.getInt(this.mPersonalLevelIndex));
    }

    public String[] getReplyToAddress()
    {
      return TextUtils.split(getStringInColumn(this.mReplyToIndex), Gmail.EMAIL_SEPARATOR_PATTERN);
    }

    public String getSubject()
    {
      return getStringInColumn(this.mSubjectIndex);
    }

    public String[] getToAddresses()
    {
      return getAddresses("toAddresses", this.mToIndex);
    }

    protected void onCursorPositionChanged()
    {
      super.onCursorPositionChanged();
    }
  }

  public static class MessageModification
  {
    public static void expungeMessage(ContentResolver paramContentResolver, String paramString, long paramLong)
    {
      if (TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("account is empty");
      paramContentResolver.delete(Gmail.getMessageByIdUri(paramString, paramLong), null, null);
    }

    public static void expungeMessages(ContentResolver paramContentResolver, String paramString, List<Long> paramList)
    {
      if (TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("account is empty");
      Uri localUri = Gmail.getMessagesUri(paramString);
      HashSet localHashSet = Sets.newHashSet();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
        localHashSet.add(((Long)localIterator.next()).toString());
      paramContentResolver.delete(localUri, null, (String[])localHashSet.toArray(new String[localHashSet.size()]));
    }

    public static String joinedAttachmentsString(List<Gmail.Attachment> paramList)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        Gmail.Attachment localAttachment = (Gmail.Attachment)localIterator.next();
        if (localStringBuilder.length() != 0)
          localStringBuilder.append("\n");
        localStringBuilder.append(localAttachment.toJoinedString());
      }
      return localStringBuilder.toString();
    }

    public static List<Gmail.Attachment> parseJoinedAttachmentString(String paramString)
    {
      ArrayList localArrayList = Lists.newArrayList();
      if (paramString != null)
      {
        String[] arrayOfString = TextUtils.split(paramString, Gmail.ATTACHMENT_INFO_SEPARATOR_PATTERN);
        int i = arrayOfString.length;
        int j = 0;
        if (j < i)
        {
          String str = arrayOfString[j];
          Gmail.Attachment localAttachment = Gmail.Attachment.parseJoinedString(str);
          if (localAttachment != null)
            localArrayList.add(localAttachment);
          while (true)
          {
            j++;
            break;
            LogUtils.d("Gmail", "Failed to parse attachment: %s", new Object[] { str });
          }
        }
      }
      return localArrayList;
    }

    public static void putBccAddresses(ContentValues paramContentValues, String[] paramArrayOfString)
    {
      paramContentValues.put("bccAddresses", TextUtils.join(",", paramArrayOfString));
    }

    public static void putBody(ContentValues paramContentValues, String paramString)
    {
      paramContentValues.put("body", paramString);
    }

    public static void putCcAddresses(ContentValues paramContentValues, String[] paramArrayOfString)
    {
      paramContentValues.put("ccAddresses", TextUtils.join(",", paramArrayOfString));
    }

    public static void putSubject(ContentValues paramContentValues, String paramString)
    {
      paramContentValues.put("subject", paramString);
    }

    public static void putToAddresses(ContentValues paramContentValues, String[] paramArrayOfString)
    {
      paramContentValues.put("toAddresses", TextUtils.join(",", paramArrayOfString));
    }

    public static void sendOrSaveExistingMessage(ContentResolver paramContentResolver, String paramString, long paramLong, ContentValues paramContentValues, boolean paramBoolean)
    {
      paramContentValues.put("save", Boolean.valueOf(paramBoolean));
      paramContentValues.put("refMessageId", Integer.valueOf(0));
      paramContentResolver.update(Gmail.getMessageByIdUri(paramString, paramLong), paramContentValues, null, null);
    }

    public static long sendOrSaveNewMessage(ContentResolver paramContentResolver, String paramString, ContentValues paramContentValues, long paramLong, boolean paramBoolean)
    {
      paramContentValues.put("save", Boolean.valueOf(paramBoolean));
      paramContentValues.put("refMessageId", Long.valueOf(paramLong));
      return ContentUris.parseId(paramContentResolver.insert(Gmail.getMessagesUri(paramString), paramContentValues));
    }
  }

  public static enum PersonalLevel
  {
    private int mLevel;

    static
    {
      ONLY_TO_ME = new PersonalLevel("ONLY_TO_ME", 2, 2);
      PersonalLevel[] arrayOfPersonalLevel = new PersonalLevel[3];
      arrayOfPersonalLevel[0] = NOT_TO_ME;
      arrayOfPersonalLevel[1] = TO_ME_AND_OTHERS;
      arrayOfPersonalLevel[2] = ONLY_TO_ME;
    }

    private PersonalLevel(int paramInt)
    {
      this.mLevel = paramInt;
    }

    public static PersonalLevel fromInt(int paramInt)
    {
      switch (paramInt)
      {
      default:
        throw new IllegalArgumentException(paramInt + " is not a personal level");
      case 0:
        return NOT_TO_ME;
      case 1:
        return TO_ME_AND_OTHERS;
      case 2:
      }
      return ONLY_TO_ME;
    }

    public int toInt()
    {
      return this.mLevel;
    }
  }

  public static class Settings
    implements Serializable
  {
    static final long serialVersionUID = 1L;
    private long mConversationAgeDays;
    private transient BitSet mDirtyBits = new BitSet();
    private Set<String> mLabelsIncluded;
    private Set<String> mLabelsPartial;
    private long mMaxAttachmentSizeMb;
    private long mSwipe;

    public static Settings fromJson(JSONObject paramJSONObject)
      throws JSONException
    {
      Settings localSettings = new Settings();
      localSettings.mConversationAgeDays = paramJSONObject.getLong("conversation_age_days");
      localSettings.mMaxAttachmentSizeMb = paramJSONObject.getLong("max_attachment_size_mb");
      localSettings.mLabelsIncluded = Sets.newHashSet();
      getStringsIntoCollection(paramJSONObject, "labels_included", localSettings.mLabelsIncluded);
      localSettings.mLabelsPartial = Sets.newHashSet();
      getStringsIntoCollection(paramJSONObject, "labels_partial", localSettings.mLabelsPartial);
      return localSettings;
    }

    private BitSet getDirtyBits()
    {
      if (this.mDirtyBits == null)
        this.mDirtyBits = new BitSet();
      return this.mDirtyBits;
    }

    private static void getStringsIntoCollection(JSONObject paramJSONObject, String paramString, Collection<String> paramCollection)
      throws JSONException
    {
      if (paramJSONObject.has(paramString))
      {
        JSONArray localJSONArray = paramJSONObject.getJSONArray(paramString);
        int i = 0;
        int j = localJSONArray.length();
        while (i < j)
        {
          paramCollection.add(localJSONArray.getString(i));
          i++;
        }
      }
    }

    private boolean hasConversationAgeDaysChanged()
    {
      try
      {
        boolean bool = getDirtyBits().get(1);
        return bool;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    private boolean hasLabelsIncludedChanged()
    {
      try
      {
        boolean bool = getDirtyBits().get(3);
        return bool;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    private boolean hasLabelsPartialChanged()
    {
      try
      {
        boolean bool = getDirtyBits().get(4);
        return bool;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    private boolean hasMaxAttachmentSizeMbChanged()
    {
      try
      {
        boolean bool = getDirtyBits().get(2);
        return bool;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      this.mConversationAgeDays = paramObjectInputStream.readLong();
      this.mMaxAttachmentSizeMb = paramObjectInputStream.readLong();
      this.mLabelsIncluded = Sets.newHashSet((String[])paramObjectInputStream.readObject());
      this.mLabelsPartial = Sets.newHashSet((String[])paramObjectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.writeLong(this.mConversationAgeDays);
      paramObjectOutputStream.writeLong(this.mMaxAttachmentSizeMb);
      paramObjectOutputStream.writeObject((String[])this.mLabelsIncluded.toArray(new String[this.mLabelsIncluded.size()]));
      paramObjectOutputStream.writeObject((String[])this.mLabelsPartial.toArray(new String[this.mLabelsPartial.size()]));
    }

    public long getConversationAgeDays()
    {
      try
      {
        long l = this.mConversationAgeDays;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public List<String> getLabelsIncluded()
    {
      try
      {
        ImmutableList localImmutableList = ImmutableList.copyOf(this.mLabelsIncluded.toArray(new String[this.mLabelsIncluded.size()]));
        return localImmutableList;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public List<String> getLabelsPartial()
    {
      try
      {
        ImmutableList localImmutableList = ImmutableList.copyOf(this.mLabelsPartial.toArray(new String[this.mLabelsPartial.size()]));
        return localImmutableList;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public long getMaxAttachmentSizeMb()
    {
      try
      {
        long l = this.mMaxAttachmentSizeMb;
        return l;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public void setConversationAgeDays(long paramLong)
    {
      try
      {
        if (paramLong != this.mConversationAgeDays)
        {
          getDirtyBits().set(1);
          this.mConversationAgeDays = paramLong;
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public void setLabelsIncluded(Collection<String> paramCollection)
    {
      try
      {
        HashSet localHashSet = Sets.newHashSet(paramCollection);
        if (!localHashSet.equals(this.mLabelsIncluded))
        {
          getDirtyBits().set(3);
          this.mLabelsIncluded = localHashSet;
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public void setLabelsPartial(Collection<String> paramCollection)
    {
      try
      {
        HashSet localHashSet = Sets.newHashSet(paramCollection);
        if (!localHashSet.equals(this.mLabelsPartial))
        {
          getDirtyBits().set(4);
          this.mLabelsPartial = localHashSet;
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public void setMaxAttachmentSizeMb(long paramLong)
    {
      try
      {
        if (paramLong != this.mMaxAttachmentSizeMb)
        {
          getDirtyBits().set(2);
          this.mMaxAttachmentSizeMb = paramLong;
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public JSONObject toJson()
      throws JSONException
    {
      JSONObject localJSONObject = new JSONObject();
      localJSONObject.put("conversation_age_days", this.mConversationAgeDays);
      localJSONObject.put("max_attachment_size_mb", this.mMaxAttachmentSizeMb);
      localJSONObject.put("labels_included", new JSONArray(this.mLabelsIncluded));
      localJSONObject.put("labels_partial", new JSONArray(this.mLabelsPartial));
      return localJSONObject;
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Settings{mConversationAgeDays=").append(this.mConversationAgeDays).append(", mMaxAttachmentSizeMb=").append(this.mMaxAttachmentSizeMb).append(", mLabelsIncluded=");
      Object localObject1;
      StringBuilder localStringBuilder2;
      Object localObject2;
      if (this.mLabelsIncluded == null)
      {
        localObject1 = null;
        localStringBuilder2 = localStringBuilder1.append(localObject1).append(", mLabelsPartial=");
        Set localSet = this.mLabelsPartial;
        localObject2 = null;
        if (localSet != null)
          break label120;
      }
      while (true)
      {
        return localObject2 + ", mDirtyBits=" + getDirtyBits() + '}';
        Set[] arrayOfSet1 = new Set[1];
        arrayOfSet1[0] = this.mLabelsIncluded;
        localObject1 = Arrays.asList(arrayOfSet1);
        break;
        label120: Set[] arrayOfSet2 = new Set[1];
        arrayOfSet2[0] = this.mLabelsPartial;
        localObject2 = Arrays.asList(arrayOfSet2);
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.Gmail
 * JD-Core Version:    0.6.2
 */