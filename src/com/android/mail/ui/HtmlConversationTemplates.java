package com.android.mail.ui;

import android.content.Context;
import com.android.mail.providers.Message;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlConversationTemplates
{
  public static final int MESSAGE_PREFIX_LENGTH = "m".length();
  private static final String TAG = LogTag.getLogTag();
  private static final Pattern sAbsoluteImgUrlPattern = Pattern.compile("(<\\s*img\\s+(?:[^>]*\\s+)?)src(\\s*=[\\s'\"]*http)", 10);
  private static String sConversationLower;
  private static String sConversationUpper;
  private static boolean sLoadedTemplates;
  private static String sMessage;
  private static String sSuperCollapsed;
  private StringBuilder mBuilder;
  private Context mContext;
  private Formatter mFormatter;
  private boolean mInProgress = false;

  public HtmlConversationTemplates(Context paramContext)
  {
    this.mContext = paramContext;
    if (!sLoadedTemplates)
    {
      sLoadedTemplates = true;
      sSuperCollapsed = readTemplate(2131165188);
      sMessage = readTemplate(2131165187);
      sConversationUpper = readTemplate(2131165186);
      sConversationLower = readTemplate(2131165185);
    }
  }

  private void append(String paramString, Object[] paramArrayOfObject)
  {
    this.mFormatter.format(paramString, paramArrayOfObject);
  }

  // ERROR //
  private String readTemplate(int paramInt)
    throws android.content.res.Resources.NotFoundException
  {
    // Byte code:
    //   0: new 95	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   7: astore_2
    //   8: new 98	java/io/InputStreamReader
    //   11: dup
    //   12: aload_0
    //   13: getfield 61	com/android/mail/ui/HtmlConversationTemplates:mContext	Landroid/content/Context;
    //   16: invokevirtual 104	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   19: iload_1
    //   20: invokevirtual 110	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   23: ldc 112
    //   25: invokespecial 115	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   28: astore_3
    //   29: sipush 4096
    //   32: newarray char
    //   34: astore 7
    //   36: aload_3
    //   37: aload 7
    //   39: invokevirtual 119	java/io/InputStreamReader:read	([C)I
    //   42: istore 8
    //   44: iload 8
    //   46: ifle +79 -> 125
    //   49: aload_2
    //   50: aload 7
    //   52: iconst_0
    //   53: iload 8
    //   55: invokevirtual 122	java/lang/StringBuilder:append	([CII)Ljava/lang/StringBuilder;
    //   58: pop
    //   59: goto -23 -> 36
    //   62: astore 4
    //   64: aload_3
    //   65: astore 5
    //   67: aload 5
    //   69: ifnull +8 -> 77
    //   72: aload 5
    //   74: invokevirtual 125	java/io/InputStreamReader:close	()V
    //   77: aload 4
    //   79: athrow
    //   80: astore 6
    //   82: new 91	android/content/res/Resources$NotFoundException
    //   85: dup
    //   86: new 95	java/lang/StringBuilder
    //   89: dup
    //   90: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   93: ldc 127
    //   95: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: iload_1
    //   99: invokestatic 135	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   102: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: ldc 137
    //   107: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   110: aload 6
    //   112: invokevirtual 140	java/io/IOException:getMessage	()Ljava/lang/String;
    //   115: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: invokevirtual 143	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   121: invokespecial 146	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   124: athrow
    //   125: aload_2
    //   126: invokevirtual 143	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   129: astore 10
    //   131: aload_3
    //   132: ifnull +7 -> 139
    //   135: aload_3
    //   136: invokevirtual 125	java/io/InputStreamReader:close	()V
    //   139: aload 10
    //   141: areturn
    //   142: astore 6
    //   144: goto -62 -> 82
    //   147: astore 4
    //   149: aconst_null
    //   150: astore 5
    //   152: goto -85 -> 67
    //
    // Exception table:
    //   from	to	target	type
    //   29	36	62	finally
    //   36	44	62	finally
    //   49	59	62	finally
    //   125	131	62	finally
    //   72	77	80	java/io/IOException
    //   77	80	80	java/io/IOException
    //   135	139	142	java/io/IOException
    //   8	29	147	finally
  }

  static String replaceAbsoluteImgUrls(String paramString)
  {
    return sAbsoluteImgUrlPattern.matcher(paramString).replaceAll("$1src='data:' blocked-src$2");
  }

  public void appendMessageHtml(Message paramMessage, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
  {
    String str1;
    String str2;
    if (paramBoolean1)
    {
      str1 = "block";
      if (!paramBoolean1)
        break label135;
      str2 = "expanded";
      label16: if (!paramBoolean2)
        break label142;
    }
    label135: label142: for (String str3 = "mail-show-images"; ; str3 = "")
    {
      String str4 = paramMessage.getBodyAsHtml();
      if ((!paramBoolean2) && (paramMessage.embedsExternalResources))
        str4 = replaceAbsoluteImgUrls(str4);
      String str5 = sMessage;
      Object[] arrayOfObject = new Object[8];
      arrayOfObject[0] = getMessageDomId(paramMessage);
      arrayOfObject[1] = str2;
      arrayOfObject[2] = Integer.valueOf(paramInt1);
      arrayOfObject[3] = str3;
      arrayOfObject[4] = str1;
      arrayOfObject[5] = str4;
      arrayOfObject[6] = str1;
      arrayOfObject[7] = Integer.valueOf(paramInt2);
      append(str5, arrayOfObject);
      return;
      str1 = "none";
      break;
      str2 = "";
      break label16;
    }
  }

  public void appendSuperCollapsedHtml(int paramInt1, int paramInt2)
  {
    if (!this.mInProgress)
      throw new IllegalStateException("must call startConversation first");
    String str = sSuperCollapsed;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(paramInt1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    append(str, arrayOfObject);
  }

  public String emit()
  {
    String str = this.mFormatter.toString();
    this.mFormatter = null;
    this.mBuilder = null;
    return str;
  }

  public String endConversation(String paramString1, String paramString2, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!this.mInProgress)
      throw new IllegalStateException("must call startConversation first");
    if (paramBoolean1);
    for (String str1 = "initial-load"; ; str1 = "")
    {
      String str2 = sConversationLower;
      Object[] arrayOfObject1 = new Object[9];
      arrayOfObject1[0] = str1;
      arrayOfObject1[1] = this.mContext.getString(2131427531);
      arrayOfObject1[2] = this.mContext.getString(2131427530);
      arrayOfObject1[3] = paramString1;
      arrayOfObject1[4] = paramString2;
      arrayOfObject1[5] = Integer.valueOf(paramInt1);
      arrayOfObject1[6] = Integer.valueOf(paramInt2);
      arrayOfObject1[7] = Boolean.valueOf(paramBoolean1);
      arrayOfObject1[8] = Boolean.valueOf(paramBoolean2);
      append(str2, arrayOfObject1);
      this.mInProgress = false;
      String str3 = TAG;
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Integer.valueOf(this.mBuilder.length() << 1);
      arrayOfObject2[1] = Integer.valueOf(this.mBuilder.capacity() << 1);
      LogUtils.d(str3, "rendered conversation of %d bytes, buffer capacity=%d", arrayOfObject2);
      return emit();
    }
  }

  public String getMessageDomId(Message paramMessage)
  {
    return "m" + paramMessage.id;
  }

  public void reset()
  {
    this.mBuilder = new StringBuilder(65536);
    this.mFormatter = new Formatter(this.mBuilder, null);
  }

  public void startConversation(int paramInt1, int paramInt2)
  {
    if (this.mInProgress)
      throw new IllegalStateException("must call startConversation first");
    reset();
    String str = sConversationUpper;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(paramInt1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    append(str, arrayOfObject);
    this.mInProgress = true;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.HtmlConversationTemplates
 * JD-Core Version:    0.6.2
 */