package com.google.android.common.gdata2;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import com.google.android.common.http.GoogleHttpClient;
import com.google.wireless.gdata2.client.GDataClient;
import com.google.wireless.gdata2.client.HttpException;
import com.google.wireless.gdata2.client.QueryParams;
import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.serializer.GDataSerializer;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;

public class AndroidGDataClient
  implements GDataClient
{
  private static final boolean DEBUG = false;
  private static String DEFAULT_GDATA_VERSION = "2.0";
  private static final String DEFAULT_USER_AGENT_APP_VERSION = "Android-GData/1.2";
  private static final boolean LOCAL_LOGV = false;
  private static final int MAX_REDIRECTS = 10;
  private static final String TAG = "GDataClient";
  private static final String X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";
  private String mGDataVersion;
  private final GoogleHttpClient mHttpClient;
  private ContentResolver mResolver;

  public AndroidGDataClient(Context paramContext)
  {
    this(paramContext, "Android-GData/1.2");
  }

  public AndroidGDataClient(Context paramContext, String paramString)
  {
    this(paramContext, paramString, DEFAULT_GDATA_VERSION);
  }

  public AndroidGDataClient(Context paramContext, String paramString1, String paramString2)
  {
    this.mHttpClient = new GoogleHttpClient(paramContext, paramString1, true);
    this.mHttpClient.enableCurlLogging("GDataClient", 2);
    this.mResolver = paramContext.getContentResolver();
    this.mGDataVersion = paramString2;
  }

  // ERROR //
  private InputStream createAndExecuteMethod(HttpRequestCreator paramHttpRequestCreator, String paramString1, String paramString2, String paramString3, String paramString4)
    throws HttpException, IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 6
    //   3: sipush 500
    //   6: istore 7
    //   8: bipush 10
    //   10: istore 8
    //   12: new 80	java/net/URI
    //   15: dup
    //   16: aload_2
    //   17: invokespecial 83	java/net/URI:<init>	(Ljava/lang/String;)V
    //   20: astore 9
    //   22: lconst_0
    //   23: lstore 10
    //   25: iload 8
    //   27: ifle +595 -> 622
    //   30: aload_1
    //   31: aload 9
    //   33: invokeinterface 89 2 0
    //   38: astore 24
    //   40: aload 24
    //   42: invokestatic 95	android/net/http/AndroidHttpClient:modifyRequestToAcceptGzipResponse	(Lorg/apache/http/HttpRequest;)V
    //   45: aload_3
    //   46: invokestatic 101	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   49: ifne +31 -> 80
    //   52: aload 24
    //   54: ldc 103
    //   56: new 105	java/lang/StringBuilder
    //   59: dup
    //   60: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   63: ldc 108
    //   65: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: aload_3
    //   69: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   72: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   75: invokeinterface 122 3 0
    //   80: aload_0
    //   81: getfield 66	com/google/android/common/gdata2/AndroidGDataClient:mGDataVersion	Ljava/lang/String;
    //   84: invokestatic 101	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   87: ifne +16 -> 103
    //   90: aload 24
    //   92: ldc 124
    //   94: aload_0
    //   95: getfield 66	com/google/android/common/gdata2/AndroidGDataClient:mGDataVersion	Ljava/lang/String;
    //   98: invokeinterface 122 3 0
    //   103: aload 4
    //   105: invokestatic 101	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   108: ifne +58 -> 166
    //   111: aload 24
    //   113: invokeinterface 127 1 0
    //   118: astore 56
    //   120: aload 24
    //   122: ldc 24
    //   124: invokeinterface 131 2 0
    //   129: astore 57
    //   131: aload 57
    //   133: ifnull +12 -> 145
    //   136: aload 57
    //   138: invokeinterface 136 1 0
    //   143: astore 56
    //   145: ldc 138
    //   147: aload 56
    //   149: invokevirtual 144	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   152: ifeq +178 -> 330
    //   155: aload 24
    //   157: ldc 146
    //   159: aload 4
    //   161: invokeinterface 122 3 0
    //   166: ldc 21
    //   168: iconst_3
    //   169: invokestatic 152	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   172: ifeq +37 -> 209
    //   175: ldc 21
    //   177: new 105	java/lang/StringBuilder
    //   180: dup
    //   181: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   184: ldc 154
    //   186: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: aload 24
    //   191: invokeinterface 158 1 0
    //   196: invokevirtual 159	java/lang/Object:toString	()Ljava/lang/String;
    //   199: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   202: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   205: invokestatic 163	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   208: pop
    //   209: aload_0
    //   210: getfield 52	com/google/android/common/gdata2/AndroidGDataClient:mHttpClient	Lcom/google/android/common/http/GoogleHttpClient;
    //   213: aload 24
    //   215: invokevirtual 167	com/google/android/common/http/GoogleHttpClient:execute	(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
    //   218: astore 27
    //   220: aload 27
    //   222: astore 6
    //   224: aload 6
    //   226: invokeinterface 173 1 0
    //   231: astore 28
    //   233: aload 28
    //   235: ifnonnull +170 -> 405
    //   238: ldc 21
    //   240: ldc 175
    //   242: invokestatic 178	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   245: pop
    //   246: new 180	java/lang/NullPointerException
    //   249: dup
    //   250: ldc 182
    //   252: invokespecial 183	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   255: athrow
    //   256: astore 58
    //   258: ldc 21
    //   260: new 105	java/lang/StringBuilder
    //   263: dup
    //   264: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   267: ldc 185
    //   269: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   272: aload_2
    //   273: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   276: ldc 187
    //   278: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   284: aload 58
    //   286: invokestatic 190	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   289: pop
    //   290: new 72	java/io/IOException
    //   293: dup
    //   294: new 105	java/lang/StringBuilder
    //   297: dup
    //   298: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   301: ldc 185
    //   303: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   306: aload_2
    //   307: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   310: ldc 192
    //   312: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   315: aload 58
    //   317: invokevirtual 195	java/net/URISyntaxException:getMessage	()Ljava/lang/String;
    //   320: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   326: invokespecial 196	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   329: athrow
    //   330: ldc 198
    //   332: aload 56
    //   334: invokevirtual 144	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   337: ifne +13 -> 350
    //   340: ldc 200
    //   342: aload 56
    //   344: invokevirtual 144	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   347: ifeq -181 -> 166
    //   350: aload 4
    //   352: ldc 202
    //   354: invokevirtual 206	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   357: ifne -191 -> 166
    //   360: aload 24
    //   362: ldc 208
    //   364: aload 4
    //   366: invokeinterface 122 3 0
    //   371: goto -205 -> 166
    //   374: astore 25
    //   376: ldc 21
    //   378: new 105	java/lang/StringBuilder
    //   381: dup
    //   382: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   385: ldc 210
    //   387: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   390: aload 25
    //   392: invokevirtual 213	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   395: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   398: invokestatic 178	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   401: pop
    //   402: aload 25
    //   404: athrow
    //   405: ldc 21
    //   407: iconst_3
    //   408: invokestatic 152	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   411: ifeq +97 -> 508
    //   414: ldc 21
    //   416: aload 6
    //   418: invokeinterface 173 1 0
    //   423: invokevirtual 159	java/lang/Object:toString	()Ljava/lang/String;
    //   426: invokestatic 163	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   429: pop
    //   430: aload 6
    //   432: invokeinterface 217 1 0
    //   437: astore 49
    //   439: aload 49
    //   441: arraylength
    //   442: istore 50
    //   444: iconst_0
    //   445: istore 51
    //   447: iload 51
    //   449: iload 50
    //   451: if_icmpge +57 -> 508
    //   454: aload 49
    //   456: iload 51
    //   458: aaload
    //   459: astore 52
    //   461: ldc 21
    //   463: new 105	java/lang/StringBuilder
    //   466: dup
    //   467: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   470: aload 52
    //   472: invokeinterface 220 1 0
    //   477: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   480: ldc 222
    //   482: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   485: aload 52
    //   487: invokeinterface 136 1 0
    //   492: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   495: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   498: invokestatic 163	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   501: pop
    //   502: iinc 51 1
    //   505: goto -58 -> 447
    //   508: aload 28
    //   510: invokeinterface 228 1 0
    //   515: istore 7
    //   517: aload 6
    //   519: invokeinterface 232 1 0
    //   524: astore 29
    //   526: iload 7
    //   528: sipush 200
    //   531: if_icmplt +43 -> 574
    //   534: iload 7
    //   536: sipush 300
    //   539: if_icmpge +35 -> 574
    //   542: aload 29
    //   544: ifnull +30 -> 574
    //   547: aload 29
    //   549: invokestatic 236	android/net/http/AndroidHttpClient:getUngzippedContent	(Lorg/apache/http/HttpEntity;)Ljava/io/InputStream;
    //   552: astore 47
    //   554: ldc 21
    //   556: iconst_3
    //   557: invokestatic 152	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   560: ifeq +11 -> 571
    //   563: aload_0
    //   564: aload 47
    //   566: invokespecial 240	com/google/android/common/gdata2/AndroidGDataClient:logInputStreamContents	(Ljava/io/InputStream;)Ljava/io/InputStream;
    //   569: astore 47
    //   571: aload 47
    //   573: areturn
    //   574: iload 7
    //   576: sipush 302
    //   579: if_icmpne +331 -> 910
    //   582: aload 29
    //   584: invokeinterface 245 1 0
    //   589: aload 6
    //   591: ldc 247
    //   593: invokeinterface 248 2 0
    //   598: astore 41
    //   600: aload 41
    //   602: ifnonnull +154 -> 756
    //   605: ldc 21
    //   607: iconst_3
    //   608: invokestatic 152	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   611: ifeq +11 -> 622
    //   614: ldc 21
    //   616: ldc 250
    //   618: invokestatic 163	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   621: pop
    //   622: ldc 21
    //   624: iconst_2
    //   625: invokestatic 152	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   628: ifeq +35 -> 663
    //   631: ldc 21
    //   633: new 105	java/lang/StringBuilder
    //   636: dup
    //   637: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   640: ldc 252
    //   642: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   645: iload 7
    //   647: invokevirtual 255	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   650: ldc_w 257
    //   653: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   656: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   659: invokestatic 260	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   662: pop
    //   663: aload 6
    //   665: invokeinterface 232 1 0
    //   670: astore 12
    //   672: aconst_null
    //   673: astore 13
    //   675: aload 6
    //   677: ifnull +395 -> 1072
    //   680: aconst_null
    //   681: astore 13
    //   683: aload 12
    //   685: ifnull +387 -> 1072
    //   688: aload 12
    //   690: invokestatic 236	android/net/http/AndroidHttpClient:getUngzippedContent	(Lorg/apache/http/HttpEntity;)Ljava/io/InputStream;
    //   693: astore 17
    //   695: new 262	java/io/ByteArrayOutputStream
    //   698: dup
    //   699: invokespecial 263	java/io/ByteArrayOutputStream:<init>	()V
    //   702: astore 18
    //   704: sipush 8192
    //   707: newarray byte
    //   709: astore 19
    //   711: aload 17
    //   713: aload 19
    //   715: invokevirtual 269	java/io/InputStream:read	([B)I
    //   718: istore 20
    //   720: iload 20
    //   722: iconst_m1
    //   723: if_icmpeq +314 -> 1037
    //   726: aload 18
    //   728: aload 19
    //   730: iconst_0
    //   731: iload 20
    //   733: invokevirtual 273	java/io/ByteArrayOutputStream:write	([BII)V
    //   736: goto -25 -> 711
    //   739: astore 16
    //   741: aload 12
    //   743: ifnull +10 -> 753
    //   746: aload 12
    //   748: invokeinterface 245 1 0
    //   753: aload 16
    //   755: athrow
    //   756: ldc 21
    //   758: iconst_3
    //   759: invokestatic 152	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   762: ifeq +35 -> 797
    //   765: ldc 21
    //   767: new 105	java/lang/StringBuilder
    //   770: dup
    //   771: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   774: ldc_w 275
    //   777: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   780: aload 41
    //   782: invokeinterface 136 1 0
    //   787: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   790: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   793: invokestatic 163	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   796: pop
    //   797: aload 41
    //   799: invokeinterface 136 1 0
    //   804: astore 44
    //   806: new 80	java/net/URI
    //   809: dup
    //   810: aload 44
    //   812: invokespecial 83	java/net/URI:<init>	(Ljava/lang/String;)V
    //   815: astore 9
    //   817: iinc 8 255
    //   820: goto -795 -> 25
    //   823: astore 42
    //   825: ldc 21
    //   827: iconst_3
    //   828: invokestatic 152	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   831: ifeq -209 -> 622
    //   834: ldc 21
    //   836: new 105	java/lang/StringBuilder
    //   839: dup
    //   840: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   843: ldc 185
    //   845: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   848: aload 41
    //   850: invokeinterface 136 1 0
    //   855: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   858: ldc 187
    //   860: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   863: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   866: aload 42
    //   868: invokestatic 277	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   871: pop
    //   872: new 72	java/io/IOException
    //   875: dup
    //   876: new 105	java/lang/StringBuilder
    //   879: dup
    //   880: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   883: ldc 185
    //   885: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   888: aload 41
    //   890: invokeinterface 136 1 0
    //   895: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   898: ldc 187
    //   900: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   903: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   906: invokespecial 196	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   909: athrow
    //   910: iload 7
    //   912: sipush 503
    //   915: if_icmpne -293 -> 622
    //   918: aload 6
    //   920: ldc_w 279
    //   923: invokeinterface 248 2 0
    //   928: astore 30
    //   930: aload 30
    //   932: ifnull -310 -> 622
    //   935: aload 30
    //   937: invokeinterface 136 1 0
    //   942: astore 31
    //   944: invokestatic 285	java/lang/System:currentTimeMillis	()J
    //   947: ldc2_w 286
    //   950: ldiv
    //   951: lstore 37
    //   953: aload 31
    //   955: invokestatic 293	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   958: lstore 39
    //   960: lload 37
    //   962: lload 39
    //   964: ladd
    //   965: lstore 10
    //   967: goto -345 -> 622
    //   970: astore 32
    //   972: new 295	android/text/format/Time
    //   975: dup
    //   976: invokespecial 296	android/text/format/Time:<init>	()V
    //   979: astore 33
    //   981: aload 33
    //   983: aload 31
    //   985: invokevirtual 299	android/text/format/Time:parse3339	(Ljava/lang/String;)Z
    //   988: pop
    //   989: aload 33
    //   991: iconst_0
    //   992: invokevirtual 303	android/text/format/Time:toMillis	(Z)J
    //   995: ldc2_w 286
    //   998: ldiv
    //   999: lstore 10
    //   1001: goto -379 -> 622
    //   1004: astore 34
    //   1006: ldc 21
    //   1008: new 105	java/lang/StringBuilder
    //   1011: dup
    //   1012: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   1015: ldc 185
    //   1017: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1020: aload 31
    //   1022: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1025: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1028: aload 34
    //   1030: invokestatic 277	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1033: pop
    //   1034: goto -412 -> 622
    //   1037: new 140	java/lang/String
    //   1040: dup
    //   1041: aload 18
    //   1043: invokevirtual 307	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   1046: invokespecial 310	java/lang/String:<init>	([B)V
    //   1049: astore 21
    //   1051: ldc 21
    //   1053: iconst_2
    //   1054: invokestatic 152	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   1057: ifeq +11 -> 1068
    //   1060: ldc 21
    //   1062: aload 21
    //   1064: invokestatic 260	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   1067: pop
    //   1068: aload 21
    //   1070: astore 13
    //   1072: aload 12
    //   1074: ifnull +10 -> 1084
    //   1077: aload 12
    //   1079: invokeinterface 245 1 0
    //   1084: new 105	java/lang/StringBuilder
    //   1087: dup
    //   1088: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   1091: ldc 252
    //   1093: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1096: iload 7
    //   1098: invokevirtual 255	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1101: ldc_w 312
    //   1104: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1107: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1110: astore 14
    //   1112: aload 13
    //   1114: ifnull +30 -> 1144
    //   1117: new 105	java/lang/StringBuilder
    //   1120: dup
    //   1121: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   1124: aload 14
    //   1126: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1129: ldc 222
    //   1131: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1134: aload 13
    //   1136: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1139: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1142: astore 14
    //   1144: new 70	com/google/wireless/gdata2/client/HttpException
    //   1147: dup
    //   1148: aload 14
    //   1150: iload 7
    //   1152: aconst_null
    //   1153: invokespecial 315	com/google/wireless/gdata2/client/HttpException:<init>	(Ljava/lang/String;ILjava/io/InputStream;)V
    //   1156: astore 15
    //   1158: iload 7
    //   1160: sipush 503
    //   1163: if_icmpne +10 -> 1173
    //   1166: aload 15
    //   1168: lload 10
    //   1170: invokevirtual 319	com/google/wireless/gdata2/client/HttpException:setRetryAfter	(J)V
    //   1173: aload 15
    //   1175: athrow
    //   1176: astore 16
    //   1178: goto -437 -> 741
    //
    // Exception table:
    //   from	to	target	type
    //   12	22	256	java/net/URISyntaxException
    //   209	220	374	java/io/IOException
    //   688	711	739	finally
    //   711	720	739	finally
    //   726	736	739	finally
    //   1037	1051	739	finally
    //   797	817	823	java/net/URISyntaxException
    //   944	960	970	java/lang/NumberFormatException
    //   972	1001	1004	android/util/TimeFormatException
    //   1051	1068	1176	finally
  }

  // ERROR //
  private HttpEntity createEntityForEntry(GDataSerializer paramGDataSerializer, int paramInt)
    throws IOException
  {
    // Byte code:
    //   0: new 262	java/io/ByteArrayOutputStream
    //   3: dup
    //   4: invokespecial 263	java/io/ByteArrayOutputStream:<init>	()V
    //   7: astore_3
    //   8: aload_1
    //   9: aload_3
    //   10: iload_2
    //   11: invokeinterface 331 3 0
    //   16: aload_3
    //   17: invokevirtual 307	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   20: astore 8
    //   22: aload 8
    //   24: ifnull +49 -> 73
    //   27: ldc 21
    //   29: iconst_3
    //   30: invokestatic 152	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   33: ifeq +40 -> 73
    //   36: ldc 21
    //   38: new 105	java/lang/StringBuilder
    //   41: dup
    //   42: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   45: ldc_w 333
    //   48: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: new 140	java/lang/String
    //   54: dup
    //   55: aload 8
    //   57: ldc_w 335
    //   60: invokespecial 338	java/lang/String:<init>	([BLjava/lang/String;)V
    //   63: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   69: invokestatic 163	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   72: pop
    //   73: aload 8
    //   75: aload_0
    //   76: getfield 64	com/google/android/common/gdata2/AndroidGDataClient:mResolver	Landroid/content/ContentResolver;
    //   79: invokestatic 342	android/net/http/AndroidHttpClient:getCompressedEntity	([BLandroid/content/ContentResolver;)Lorg/apache/http/entity/AbstractHttpEntity;
    //   82: astore 9
    //   84: aload 9
    //   86: aload_1
    //   87: invokeinterface 345 1 0
    //   92: invokevirtual 350	org/apache/http/entity/AbstractHttpEntity:setContentType	(Ljava/lang/String;)V
    //   95: aload 9
    //   97: areturn
    //   98: astore 6
    //   100: ldc 21
    //   102: ldc_w 352
    //   105: aload 6
    //   107: invokestatic 355	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   110: pop
    //   111: aload 6
    //   113: athrow
    //   114: astore 4
    //   116: ldc 21
    //   118: ldc_w 352
    //   121: aload 4
    //   123: invokestatic 355	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   126: pop
    //   127: new 72	java/io/IOException
    //   130: dup
    //   131: new 105	java/lang/StringBuilder
    //   134: dup
    //   135: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   138: ldc_w 357
    //   141: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: aload 4
    //   146: invokevirtual 358	com/google/wireless/gdata2/parser/ParseException:getMessage	()Ljava/lang/String;
    //   149: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   155: invokespecial 196	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   158: athrow
    //   159: astore 10
    //   161: new 360	java/lang/IllegalStateException
    //   164: dup
    //   165: ldc_w 362
    //   168: aload 10
    //   170: invokespecial 365	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   173: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   8	16	98	java/io/IOException
    //   8	16	114	com/google/wireless/gdata2/parser/ParseException
    //   36	73	159	java/io/UnsupportedEncodingException
  }

  private InputStream logInputStreamContents(InputStream paramInputStream)
    throws IOException
  {
    if (paramInputStream == null)
      return paramInputStream;
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, 16384);
    localBufferedInputStream.mark(16384);
    int i = 16384;
    int j = 0;
    byte[] arrayOfByte = new byte[i];
    while (true)
    {
      int k;
      if (i > 0)
      {
        k = localBufferedInputStream.read(arrayOfByte, j, i);
        if (k > 0);
      }
      else
      {
        Log.d("GDataClient", new String(arrayOfByte, 0, j, "UTF-8"));
        localBufferedInputStream.reset();
        return localBufferedInputStream;
      }
      i -= k;
      j += k;
    }
  }

  public void close()
  {
    this.mHttpClient.close();
  }

  public InputStream createEntry(String paramString1, String paramString2, String paramString3, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new PostRequestCreator(null, createEntityForEntry(paramGDataSerializer, 1)), paramString1, paramString2, null, paramString3);
    if (localInputStream != null)
      return localInputStream;
    throw new IOException("Unable to create entry.");
  }

  public QueryParams createQueryParams()
  {
    return new QueryParamsImpl();
  }

  public void deleteEntry(String paramString1, String paramString2, String paramString3)
    throws HttpException, IOException
  {
    if (StringUtils.isEmpty(paramString1))
      throw new IllegalArgumentException("you must specify an non-empty edit url");
    InputStream localInputStream = createAndExecuteMethod(new PostRequestCreator("DELETE", null), paramString1, paramString2, paramString3, null);
    if (localInputStream == null)
      throw new IOException("Unable to delete entry.");
    try
    {
      localInputStream.close();
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  public String encodeUri(String paramString)
  {
    try
    {
      String str = URLEncoder.encode(paramString, "UTF-8");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      Log.e("JakartaGDataClient", "UTF-8 not supported -- should not happen.  Using default encoding.", localUnsupportedEncodingException);
    }
    return URLEncoder.encode(paramString);
  }

  public InputStream getFeedAsStream(String paramString1, String paramString2, String paramString3, String paramString4)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new GetRequestCreator(), paramString1, paramString2, paramString3, paramString4);
    if (localInputStream != null)
      return localInputStream;
    throw new IOException("Unable to access feed.");
  }

  public InputStream getMediaEntryAsStream(String paramString1, String paramString2, String paramString3, String paramString4)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new GetRequestCreator(), paramString1, paramString2, paramString3, paramString4);
    if (localInputStream != null)
      return localInputStream;
    throw new IOException("Unable to access media entry.");
  }

  public InputStream submitBatch(String paramString1, String paramString2, String paramString3, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new PostRequestCreator("POST", createEntityForEntry(paramGDataSerializer, 3)), paramString1, paramString2, null, paramString3);
    if (localInputStream != null)
      return localInputStream;
    throw new IOException("Unable to process batch request.");
  }

  public InputStream updateEntry(String paramString1, String paramString2, String paramString3, String paramString4, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException
  {
    HttpEntity localHttpEntity = createEntityForEntry(paramGDataSerializer, 2);
    if (paramGDataSerializer.getSupportsPartial());
    for (String str = "PATCH"; ; str = "PUT")
    {
      InputStream localInputStream = createAndExecuteMethod(new PostRequestCreator(str, localHttpEntity), paramString1, paramString2, paramString3, paramString4);
      if (localInputStream == null)
        break;
      return localInputStream;
    }
    throw new IOException("Unable to update entry.");
  }

  public InputStream updateMediaEntry(String paramString1, String paramString2, String paramString3, String paramString4, InputStream paramInputStream, String paramString5)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new MediaPutRequestCreator(paramInputStream, paramString5), paramString1, paramString2, paramString3, paramString4);
    if (localInputStream != null)
      return localInputStream;
    throw new IOException("Unable to write media entry.");
  }

  private static class GetRequestCreator
    implements AndroidGDataClient.HttpRequestCreator
  {
    public HttpUriRequest createRequest(URI paramURI)
    {
      return new HttpGet(paramURI);
    }
  }

  private static abstract interface HttpRequestCreator
  {
    public abstract HttpUriRequest createRequest(URI paramURI);
  }

  private static class MediaPutRequestCreator
    implements AndroidGDataClient.HttpRequestCreator
  {
    private final String mContentType;
    private final InputStream mMediaInputStream;

    public MediaPutRequestCreator(InputStream paramInputStream, String paramString)
    {
      this.mMediaInputStream = paramInputStream;
      this.mContentType = paramString;
    }

    public HttpUriRequest createRequest(URI paramURI)
    {
      HttpPost localHttpPost = new HttpPost(paramURI);
      localHttpPost.addHeader("X-HTTP-Method-Override", "PUT");
      InputStreamEntity localInputStreamEntity = new InputStreamEntity(this.mMediaInputStream, -1L);
      localInputStreamEntity.setContentType(this.mContentType);
      localHttpPost.setEntity(localInputStreamEntity);
      return localHttpPost;
    }
  }

  private static class PostRequestCreator
    implements AndroidGDataClient.HttpRequestCreator
  {
    private final HttpEntity mEntity;
    private final String mMethodOverride;

    public PostRequestCreator(String paramString, HttpEntity paramHttpEntity)
    {
      this.mMethodOverride = paramString;
      this.mEntity = paramHttpEntity;
    }

    public HttpUriRequest createRequest(URI paramURI)
    {
      HttpPost localHttpPost = new HttpPost(paramURI);
      if (this.mMethodOverride != null)
        localHttpPost.addHeader("X-HTTP-Method-Override", this.mMethodOverride);
      localHttpPost.setEntity(this.mEntity);
      return localHttpPost;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.gdata2.AndroidGDataClient
 * JD-Core Version:    0.6.2
 */