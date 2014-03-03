package com.google.android.gm.provider;

import android.content.Context;
import android.database.DataSetObservable;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gm.utils.LabelColorUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Label
{
  private static final Pattern LABEL_COMPONENT_SEPARATOR_PATTERN = Pattern.compile("\\^\\*\\^");
  private static int sLabelRequeryDelayMs;
  private static HashMap<String, CharSequence> sSystemLabelCache;
  private static Object sSystemLabelCacheLock = new Object();
  private final String mAccount;
  private String mCanonicalName;
  private String mColor;
  private boolean mCountsInitialized;
  private final DataSetObservable mDataSetObservable = new DataSetObservable();
  private final Map<String, CharSequence> mFactorySystemLabelMap;
  private final long mId;
  private final boolean mIsHidden;
  private boolean mIsSystemLabel;
  private int mLabelCountBehavior = 0;
  private int mLabelSyncPolicy = 0;
  private long mLastTouched;
  private String mName;
  private int mNumConversations;
  private int mNumUnreadConversations;
  private String mSerializedInfo;

  static
  {
    sLabelRequeryDelayMs = -1;
  }

  Label(Context paramContext, String paramString1, long paramLong1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, int paramInt4, long paramLong2, Map<String, CharSequence> paramMap)
  {
    this.mAccount = paramString1;
    this.mId = paramLong1;
    this.mIsHidden = paramBoolean;
    this.mFactorySystemLabelMap = paramMap;
    loadInternal(paramContext, paramString2, paramString3, paramString4, paramInt1, paramInt2, paramInt3, paramInt4, paramLong2);
  }

  Label(Context paramContext, String paramString1, long paramLong, String paramString2, String paramString3, String paramString4, boolean paramBoolean, Map<String, CharSequence> paramMap)
  {
    this.mAccount = paramString1;
    this.mId = paramLong;
    this.mCanonicalName = paramString2;
    this.mIsSystemLabel = Gmail.isSystemLabel(paramString2);
    this.mFactorySystemLabelMap = paramMap;
    if ((this.mIsSystemLabel) && (paramString3.equals(this.mCanonicalName)));
    for (this.mName = getHumanSystemLabelName(paramContext); ; this.mName = paramString3)
    {
      this.mColor = paramString4;
      this.mCountsInitialized = false;
      this.mIsHidden = paramBoolean;
      return;
    }
  }

  public static int getBackgroundColor(String paramString1, String paramString2, String paramString3)
  {
    boolean bool = Gmail.isSystemLabel(paramString2);
    if (paramString2.equals("^g"));
    for (int[] arrayOfInt = LabelColorUtils.getMutedColorInts(); bool; arrayOfInt = LabelColorUtils.getLabelColorInts(getColor(bool, paramString3), paramString1))
      return 16777215;
    return arrayOfInt[0];
  }

  static String getColor(boolean paramBoolean, String paramString)
  {
    if (paramBoolean)
      paramString = "2147483647";
    return paramString;
  }

  private String getHumanSystemLabelName(Context paramContext)
  {
    CharSequence localCharSequence;
    if (this.mFactorySystemLabelMap != null)
      localCharSequence = (CharSequence)this.mFactorySystemLabelMap.get(this.mCanonicalName);
    while (localCharSequence == null)
    {
      return this.mCanonicalName;
      localCharSequence = null;
      if (paramContext != null)
        synchronized (sSystemLabelCacheLock)
        {
          initLabelCache(paramContext);
          localCharSequence = (CharSequence)sSystemLabelCache.get(this.mCanonicalName);
        }
    }
    return localCharSequence.toString();
  }

  static Uri getLabelUri(String paramString, Long paramLong)
  {
    return Uri.withAppendedPath(Gmail.getLabelUri(paramString), paramLong.toString());
  }

  static Map<String, CharSequence> getSystemLabelNameMap(Context paramContext)
  {
    HashMap localHashMap = null;
    if (paramContext != null)
    {
      localHashMap = Maps.newHashMap();
      loadSystemLabelNameMap(paramContext, localHashMap);
    }
    return localHashMap;
  }

  public static int getTextColor(String paramString1, String paramString2, String paramString3)
  {
    boolean bool = Gmail.isSystemLabel(paramString2);
    if (paramString2.equals("^g"));
    for (int[] arrayOfInt = LabelColorUtils.getMutedColorInts(); ; arrayOfInt = LabelColorUtils.getLabelColorInts(getColor(bool, paramString3), paramString1))
      return arrayOfInt[1];
  }

  private void initLabelCache(Context paramContext)
  {
    if (sSystemLabelCache == null)
      sSystemLabelCache = new HashMap();
    loadSystemLabelNameMap(paramContext, sSystemLabelCache);
  }

  private static void loadSystemLabelNameMap(Context paramContext, Map<String, CharSequence> paramMap)
  {
    paramMap.put("^f", paramContext.getString(2131427774));
    paramMap.put("^^out", paramContext.getString(2131427775));
    paramMap.put("^i", paramContext.getString(2131427776));
    paramMap.put("^r", paramContext.getString(2131427777));
    paramMap.put("^b", paramContext.getString(2131427778));
    paramMap.put("^all", paramContext.getString(2131427779));
    paramMap.put("^u", paramContext.getString(2131427780));
    paramMap.put("^k", paramContext.getString(2131427781));
    paramMap.put("^s", paramContext.getString(2131427782));
    paramMap.put("^t", paramContext.getString(2131427783));
    paramMap.put("^g", paramContext.getString(2131427784));
    paramMap.put("^io_im", paramContext.getString(2131427785));
    paramMap.put("^iim", paramContext.getString(2131427786));
  }

  public static Label parseJoinedString(String paramString)
  {
    return parseJoinedString(paramString, null);
  }

  static Label parseJoinedString(String paramString1, String paramString2, Map<String, CharSequence> paramMap, Map<Long, Label> paramMap1)
  {
    int i = paramString2.indexOf("^*^");
    long l;
    if (i != -1)
    {
      try
      {
        l = Long.valueOf(paramString2.substring(0, i)).longValue();
        if ((paramMap1 == null) || (!paramMap1.containsKey(Long.valueOf(l))))
          break label106;
        Label localLabel2 = (Label)paramMap1.get(Long.valueOf(l));
        return localLabel2;
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        LogUtils.w("Gmail", "Problem parsing labelId: original string: %s", new Object[] { paramString2 });
        return null;
      }
    }
    else
    {
      LogUtils.w("Gmail", "Problem parsing labelId: original string: %s", new Object[] { paramString2 });
      return null;
    }
    label106: String[] arrayOfString = TextUtils.split(paramString2, LABEL_COMPONENT_SEPARATOR_PATTERN);
    if (arrayOfString.length < 5)
      return null;
    int j = 0 + 1;
    int k = j + 1;
    String str1 = arrayOfString[j];
    int m = k + 1;
    String str2 = arrayOfString[k];
    int n = m + 1;
    String str3 = arrayOfString[m];
    (n + 1);
    try
    {
      int i1 = Integer.valueOf(arrayOfString[n]).intValue();
      if (i1 > 0);
      for (boolean bool = true; ; bool = false)
      {
        Label localLabel1 = new Label(null, paramString1, l, str1, str2, str3, bool, paramMap);
        if (paramMap1 != null)
          paramMap1.put(Long.valueOf(l), localLabel1);
        return localLabel1;
      }
    }
    catch (NumberFormatException localNumberFormatException2)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = localNumberFormatException2.getMessage();
      arrayOfObject[1] = paramString2;
      LogUtils.w("Gmail", "Problem parsing isHidden: %s original string: %s", arrayOfObject);
    }
    return null;
  }

  // ERROR //
  static Label parseJoinedString(String paramString, Map<String, CharSequence> paramMap)
  {
    // Byte code:
    //   0: aload_0
    //   1: getstatic 54	com/google/android/gm/provider/Label:LABEL_COMPONENT_SEPARATOR_PATTERN	Ljava/util/regex/Pattern;
    //   4: invokestatic 268	android/text/TextUtils:split	(Ljava/lang/String;Ljava/util/regex/Pattern;)[Ljava/lang/String;
    //   7: astore_2
    //   8: aload_2
    //   9: arraylength
    //   10: bipush 6
    //   12: if_icmpge +5 -> 17
    //   15: aconst_null
    //   16: areturn
    //   17: iconst_0
    //   18: iconst_1
    //   19: iadd
    //   20: istore_3
    //   21: aload_2
    //   22: iconst_0
    //   23: aaload
    //   24: astore 4
    //   26: iload_3
    //   27: iconst_1
    //   28: iadd
    //   29: istore 5
    //   31: aload_2
    //   32: iload_3
    //   33: aaload
    //   34: invokestatic 242	java/lang/Long:valueOf	(Ljava/lang/String;)Ljava/lang/Long;
    //   37: invokevirtual 246	java/lang/Long:longValue	()J
    //   40: lstore 9
    //   42: iload 5
    //   44: iconst_1
    //   45: iadd
    //   46: istore 11
    //   48: aload_2
    //   49: iload 5
    //   51: aaload
    //   52: astore 12
    //   54: iload 11
    //   56: iconst_1
    //   57: iadd
    //   58: istore 13
    //   60: aload_2
    //   61: iload 11
    //   63: aaload
    //   64: astore 14
    //   66: aload 14
    //   68: ldc_w 290
    //   71: invokestatic 296	java/net/URLDecoder:decode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   74: astore 29
    //   76: aload 29
    //   78: astore 19
    //   80: aload 4
    //   82: ldc_w 290
    //   85: invokestatic 296	java/net/URLDecoder:decode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   88: astore 20
    //   90: iload 13
    //   92: iconst_1
    //   93: iadd
    //   94: istore 21
    //   96: aload_2
    //   97: iload 13
    //   99: aaload
    //   100: astore 22
    //   102: iload 21
    //   104: iconst_1
    //   105: iadd
    //   106: pop
    //   107: aload_2
    //   108: iload 21
    //   110: aaload
    //   111: invokestatic 273	java/lang/Integer:valueOf	(Ljava/lang/String;)Ljava/lang/Integer;
    //   114: invokevirtual 277	java/lang/Integer:intValue	()I
    //   117: istore 27
    //   119: iload 27
    //   121: ifle +106 -> 227
    //   124: iconst_1
    //   125: istore 28
    //   127: new 2	com/google/android/gm/provider/Label
    //   130: dup
    //   131: aconst_null
    //   132: aload 20
    //   134: lload 9
    //   136: aload 12
    //   138: aload 19
    //   140: aload 22
    //   142: iload 28
    //   144: aload_1
    //   145: invokespecial 279	com/google/android/gm/provider/Label:<init>	(Landroid/content/Context;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/util/Map;)V
    //   148: areturn
    //   149: astore 6
    //   151: iconst_2
    //   152: anewarray 4	java/lang/Object
    //   155: astore 7
    //   157: aload 7
    //   159: iconst_0
    //   160: aload 6
    //   162: invokevirtual 282	java/lang/NumberFormatException:getMessage	()Ljava/lang/String;
    //   165: aastore
    //   166: aload 7
    //   168: iconst_1
    //   169: aload_0
    //   170: aastore
    //   171: ldc 254
    //   173: ldc_w 298
    //   176: aload 7
    //   178: invokestatic 262	com/google/android/gm/provider/LogUtils:w	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   181: pop
    //   182: aconst_null
    //   183: areturn
    //   184: astore 17
    //   186: ldc 254
    //   188: aload 17
    //   190: ldc_w 300
    //   193: iconst_0
    //   194: anewarray 4	java/lang/Object
    //   197: invokestatic 304	com/google/android/gm/provider/LogUtils:wtf	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   200: pop
    //   201: aload 14
    //   203: astore 19
    //   205: goto -125 -> 80
    //   208: astore 15
    //   210: ldc 254
    //   212: aload 15
    //   214: ldc_w 306
    //   217: iconst_0
    //   218: anewarray 4	java/lang/Object
    //   221: invokestatic 304	com/google/android/gm/provider/LogUtils:wtf	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   224: pop
    //   225: aconst_null
    //   226: areturn
    //   227: iconst_0
    //   228: istore 28
    //   230: goto -103 -> 127
    //   233: astore 24
    //   235: iconst_2
    //   236: anewarray 4	java/lang/Object
    //   239: astore 25
    //   241: aload 25
    //   243: iconst_0
    //   244: aload 24
    //   246: invokevirtual 282	java/lang/NumberFormatException:getMessage	()Ljava/lang/String;
    //   249: aastore
    //   250: aload 25
    //   252: iconst_1
    //   253: aload_0
    //   254: aastore
    //   255: ldc 254
    //   257: ldc_w 284
    //   260: aload 25
    //   262: invokestatic 262	com/google/android/gm/provider/LogUtils:w	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   265: pop
    //   266: aconst_null
    //   267: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   31	42	149	java/lang/NumberFormatException
    //   66	76	184	java/lang/IllegalArgumentException
    //   66	76	208	java/io/UnsupportedEncodingException
    //   80	90	208	java/io/UnsupportedEncodingException
    //   186	201	208	java/io/UnsupportedEncodingException
    //   107	119	233	java/lang/NumberFormatException
  }

  public int getBackgroundColor()
  {
    try
    {
      int i = getBackgroundColor(this.mAccount, this.mCanonicalName, this.mColor);
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public String getCanonicalName()
  {
    try
    {
      String str = this.mCanonicalName;
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public String getColor()
  {
    try
    {
      String str = getColor(isSystemLabel(), this.mColor);
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public boolean getDisplayNoConversationCount()
  {
    try
    {
      int i = this.mLabelCountBehavior;
      if (i == 2)
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

  public boolean getDisplayTotalConversationCount()
  {
    int i = 1;
    try
    {
      int j = this.mLabelCountBehavior;
      if (j == i)
        return i;
      i = 0;
    }
    finally
    {
    }
  }

  public boolean getForceSyncAll()
  {
    int i = 1;
    try
    {
      int j = this.mLabelSyncPolicy;
      if (j == i)
        return i;
      i = 0;
    }
    finally
    {
    }
  }

  public boolean getForceSyncAllOrPartial()
  {
    try
    {
      int i = this.mLabelSyncPolicy;
      if (i == 3)
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

  public boolean getForceSyncNone()
  {
    try
    {
      int i = this.mLabelSyncPolicy;
      if (i == 2)
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

  public boolean getHidden()
  {
    try
    {
      boolean bool = this.mIsHidden;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public long getId()
  {
    try
    {
      long l = this.mId;
      return l;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public long getLastTouched()
  {
    try
    {
      long l = this.mLastTouched;
      return l;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public String getName()
  {
    try
    {
      String str = this.mName;
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public int getNumConversations()
  {
    try
    {
      if (!this.mCountsInitialized)
        throw new IllegalArgumentException("conversation counts were not initialized");
    }
    finally
    {
    }
    int i = this.mNumConversations;
    return i;
  }

  public int getNumUnreadConversations()
  {
    try
    {
      if (!this.mCountsInitialized)
        throw new IllegalArgumentException("unread conversation counts were not initialized");
    }
    finally
    {
    }
    int i = this.mNumUnreadConversations;
    return i;
  }

  public int getTextColor()
  {
    try
    {
      int i = getTextColor(this.mAccount, this.mCanonicalName, this.mColor);
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public boolean isSystemLabel()
  {
    try
    {
      boolean bool = this.mIsSystemLabel;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  void loadInternal(Context paramContext, String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong)
  {
    if (!paramString1.equals(this.mCanonicalName))
    {
      this.mCanonicalName = paramString1;
      this.mIsSystemLabel = Gmail.isSystemLabel(this.mCanonicalName);
      this.mSerializedInfo = null;
    }
    if (!Objects.equal(paramString3, this.mColor))
    {
      this.mColor = paramString3;
      this.mSerializedInfo = null;
    }
    if ((this.mIsSystemLabel) && ((paramString2 == null) || (this.mCanonicalName.equals(paramString2))))
      paramString2 = getHumanSystemLabelName(paramContext);
    if (!Objects.equal(paramString2, this.mName))
    {
      this.mName = paramString2;
      this.mSerializedInfo = null;
    }
    this.mNumConversations = paramInt1;
    this.mNumUnreadConversations = paramInt2;
    this.mCountsInitialized = true;
    this.mLabelCountBehavior = paramInt3;
    this.mLabelSyncPolicy = paramInt4;
    this.mLastTouched = paramLong;
  }

  public String serialize()
  {
    while (true)
    {
      try
      {
        String str3;
        if (this.mSerializedInfo != null)
        {
          str3 = this.mSerializedInfo;
          return str3;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        String str1 = "";
        Object localObject2 = "";
        try
        {
          str1 = URLEncoder.encode(this.mAccount, "UTF-8");
          String str4 = URLEncoder.encode(this.mName, "UTF-8");
          localObject2 = str4;
          localStringBuilder.append(str1).append("^*^");
          localStringBuilder.append(this.mId).append("^*^");
          localStringBuilder.append(this.mCanonicalName).append("^*^");
          localStringBuilder.append((String)localObject2).append("^*^");
          localStringBuilder.append(this.mColor).append("^*^");
          if (this.mIsHidden)
          {
            str2 = "1";
            localStringBuilder.append(str2);
            this.mSerializedInfo = localStringBuilder.toString();
            str3 = this.mSerializedInfo;
          }
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          LogUtils.wtf("Gmail", localUnsupportedEncodingException, "unsupported encoding", new Object[0]);
          continue;
        }
      }
      finally
      {
      }
      String str2 = "0";
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.Label
 * JD-Core Version:    0.6.2
 */