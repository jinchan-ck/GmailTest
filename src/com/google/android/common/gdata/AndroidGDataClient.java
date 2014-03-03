package com.google.android.common.gdata;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import com.google.android.common.http.GoogleHttpClient;
import com.google.wireless.gdata.client.GDataClient;
import com.google.wireless.gdata.client.HttpException;
import com.google.wireless.gdata.client.QueryParams;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.serializer.GDataSerializer;
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
  private static final String DEFAULT_USER_AGENT_APP_VERSION = "Android-GData/1.1";
  private static final boolean LOCAL_LOGV = false;
  private static final int MAX_REDIRECTS = 10;
  private static final String TAG = "GDataClient";
  private static final String X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";
  private final GoogleHttpClient mHttpClient;
  private ContentResolver mResolver;

  public AndroidGDataClient(Context paramContext)
  {
    this(paramContext, "Android-GData/1.1");
  }

  public AndroidGDataClient(Context paramContext, String paramString)
  {
    this.mHttpClient = new GoogleHttpClient(paramContext, paramString, true);
    this.mHttpClient.enableCurlLogging("GDataClient", 2);
    this.mResolver = paramContext.getContentResolver();
  }

  // ERROR //
  private InputStream createAndExecuteMethod(HttpRequestCreator paramHttpRequestCreator, String paramString1, String paramString2)
    throws HttpException, IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: sipush 500
    //   6: istore 5
    //   8: bipush 10
    //   10: istore 6
    //   12: new 64	java/net/URI
    //   15: dup
    //   16: aload_2
    //   17: invokespecial 67	java/net/URI:<init>	(Ljava/lang/String;)V
    //   20: astore 7
    //   22: iload 6
    //   24: ifle +465 -> 489
    //   27: aload_1
    //   28: aload 7
    //   30: invokeinterface 73 2 0
    //   35: astore 20
    //   37: aload 20
    //   39: invokestatic 79	android/net/http/AndroidHttpClient:modifyRequestToAcceptGzipResponse	(Lorg/apache/http/HttpRequest;)V
    //   42: aload_3
    //   43: invokestatic 85	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   46: ifne +31 -> 77
    //   49: aload 20
    //   51: ldc 87
    //   53: new 89	java/lang/StringBuilder
    //   56: dup
    //   57: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   60: ldc 92
    //   62: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   65: aload_3
    //   66: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   72: invokeinterface 106 3 0
    //   77: ldc 20
    //   79: iconst_3
    //   80: invokestatic 112	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   83: ifeq +37 -> 120
    //   86: ldc 20
    //   88: new 89	java/lang/StringBuilder
    //   91: dup
    //   92: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   95: ldc 114
    //   97: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: aload 20
    //   102: invokeinterface 118 1 0
    //   107: invokevirtual 119	java/lang/Object:toString	()Ljava/lang/String;
    //   110: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   113: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   116: invokestatic 123	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   119: pop
    //   120: aload_0
    //   121: getfield 42	com/google/android/common/gdata/AndroidGDataClient:mHttpClient	Lcom/google/android/common/http/GoogleHttpClient;
    //   124: aload 20
    //   126: invokevirtual 127	com/google/android/common/http/GoogleHttpClient:execute	(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
    //   129: astore 23
    //   131: aload 23
    //   133: astore 4
    //   135: aload 4
    //   137: invokeinterface 133 1 0
    //   142: astore 24
    //   144: aload 24
    //   146: ifnonnull +126 -> 272
    //   149: ldc 20
    //   151: ldc 135
    //   153: invokestatic 138	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   156: pop
    //   157: new 140	java/lang/NullPointerException
    //   160: dup
    //   161: ldc 142
    //   163: invokespecial 143	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   166: athrow
    //   167: astore 41
    //   169: ldc 20
    //   171: new 89	java/lang/StringBuilder
    //   174: dup
    //   175: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   178: ldc 145
    //   180: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: aload_2
    //   184: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: ldc 147
    //   189: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   192: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   195: aload 41
    //   197: invokestatic 150	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   200: pop
    //   201: new 60	java/io/IOException
    //   204: dup
    //   205: new 89	java/lang/StringBuilder
    //   208: dup
    //   209: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   212: ldc 145
    //   214: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: aload_2
    //   218: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: ldc 152
    //   223: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   226: aload 41
    //   228: invokevirtual 155	java/net/URISyntaxException:getMessage	()Ljava/lang/String;
    //   231: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   237: invokespecial 156	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   240: athrow
    //   241: astore 21
    //   243: ldc 20
    //   245: new 89	java/lang/StringBuilder
    //   248: dup
    //   249: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   252: ldc 158
    //   254: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: aload 21
    //   259: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   262: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   265: invokestatic 138	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   268: pop
    //   269: aload 21
    //   271: athrow
    //   272: ldc 20
    //   274: iconst_3
    //   275: invokestatic 112	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   278: ifeq +97 -> 375
    //   281: ldc 20
    //   283: aload 4
    //   285: invokeinterface 133 1 0
    //   290: invokevirtual 119	java/lang/Object:toString	()Ljava/lang/String;
    //   293: invokestatic 123	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   296: pop
    //   297: aload 4
    //   299: invokeinterface 165 1 0
    //   304: astore 34
    //   306: aload 34
    //   308: arraylength
    //   309: istore 35
    //   311: iconst_0
    //   312: istore 36
    //   314: iload 36
    //   316: iload 35
    //   318: if_icmpge +57 -> 375
    //   321: aload 34
    //   323: iload 36
    //   325: aaload
    //   326: astore 37
    //   328: ldc 20
    //   330: new 89	java/lang/StringBuilder
    //   333: dup
    //   334: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   337: aload 37
    //   339: invokeinterface 170 1 0
    //   344: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   347: ldc 172
    //   349: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   352: aload 37
    //   354: invokeinterface 175 1 0
    //   359: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   362: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   365: invokestatic 123	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   368: pop
    //   369: iinc 36 1
    //   372: goto -58 -> 314
    //   375: aload 24
    //   377: invokeinterface 181 1 0
    //   382: istore 5
    //   384: aload 4
    //   386: invokeinterface 185 1 0
    //   391: astore 25
    //   393: iload 5
    //   395: sipush 200
    //   398: if_icmplt +43 -> 441
    //   401: iload 5
    //   403: sipush 300
    //   406: if_icmpge +35 -> 441
    //   409: aload 25
    //   411: ifnull +30 -> 441
    //   414: aload 25
    //   416: invokestatic 189	android/net/http/AndroidHttpClient:getUngzippedContent	(Lorg/apache/http/HttpEntity;)Ljava/io/InputStream;
    //   419: astore 32
    //   421: ldc 20
    //   423: iconst_3
    //   424: invokestatic 112	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   427: ifeq +11 -> 438
    //   430: aload_0
    //   431: aload 32
    //   433: invokespecial 193	com/google/android/common/gdata/AndroidGDataClient:logInputStreamContents	(Ljava/io/InputStream;)Ljava/io/InputStream;
    //   436: astore 32
    //   438: aload 32
    //   440: areturn
    //   441: iload 5
    //   443: sipush 302
    //   446: if_icmpne +43 -> 489
    //   449: aload 25
    //   451: invokeinterface 198 1 0
    //   456: aload 4
    //   458: ldc 200
    //   460: invokeinterface 204 2 0
    //   465: astore 26
    //   467: aload 26
    //   469: ifnonnull +153 -> 622
    //   472: ldc 20
    //   474: iconst_3
    //   475: invokestatic 112	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   478: ifeq +11 -> 489
    //   481: ldc 20
    //   483: ldc 206
    //   485: invokestatic 123	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   488: pop
    //   489: ldc 20
    //   491: iconst_2
    //   492: invokestatic 112	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   495: ifeq +34 -> 529
    //   498: ldc 20
    //   500: new 89	java/lang/StringBuilder
    //   503: dup
    //   504: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   507: ldc 208
    //   509: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   512: iload 5
    //   514: invokevirtual 211	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   517: ldc 213
    //   519: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   522: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   525: invokestatic 216	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   528: pop
    //   529: aload 4
    //   531: invokeinterface 185 1 0
    //   536: astore 8
    //   538: aconst_null
    //   539: astore 9
    //   541: aload 4
    //   543: ifnull +267 -> 810
    //   546: aconst_null
    //   547: astore 9
    //   549: aload 8
    //   551: ifnull +259 -> 810
    //   554: aload 8
    //   556: invokestatic 189	android/net/http/AndroidHttpClient:getUngzippedContent	(Lorg/apache/http/HttpEntity;)Ljava/io/InputStream;
    //   559: astore 13
    //   561: new 218	java/io/ByteArrayOutputStream
    //   564: dup
    //   565: invokespecial 219	java/io/ByteArrayOutputStream:<init>	()V
    //   568: astore 14
    //   570: sipush 8192
    //   573: newarray byte
    //   575: astore 15
    //   577: aload 13
    //   579: aload 15
    //   581: invokevirtual 225	java/io/InputStream:read	([B)I
    //   584: istore 16
    //   586: iload 16
    //   588: iconst_m1
    //   589: if_icmpeq +186 -> 775
    //   592: aload 14
    //   594: aload 15
    //   596: iconst_0
    //   597: iload 16
    //   599: invokevirtual 229	java/io/ByteArrayOutputStream:write	([BII)V
    //   602: goto -25 -> 577
    //   605: astore 12
    //   607: aload 8
    //   609: ifnull +10 -> 619
    //   612: aload 8
    //   614: invokeinterface 198 1 0
    //   619: aload 12
    //   621: athrow
    //   622: ldc 20
    //   624: iconst_3
    //   625: invokestatic 112	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   628: ifeq +34 -> 662
    //   631: ldc 20
    //   633: new 89	java/lang/StringBuilder
    //   636: dup
    //   637: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   640: ldc 231
    //   642: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   645: aload 26
    //   647: invokeinterface 175 1 0
    //   652: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   655: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   658: invokestatic 123	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   661: pop
    //   662: aload 26
    //   664: invokeinterface 175 1 0
    //   669: astore 29
    //   671: new 64	java/net/URI
    //   674: dup
    //   675: aload 29
    //   677: invokespecial 67	java/net/URI:<init>	(Ljava/lang/String;)V
    //   680: astore 7
    //   682: iinc 6 255
    //   685: goto -663 -> 22
    //   688: astore 27
    //   690: ldc 20
    //   692: iconst_3
    //   693: invokestatic 112	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   696: ifeq -207 -> 489
    //   699: ldc 20
    //   701: new 89	java/lang/StringBuilder
    //   704: dup
    //   705: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   708: ldc 145
    //   710: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   713: aload 26
    //   715: invokeinterface 175 1 0
    //   720: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   723: ldc 147
    //   725: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   728: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   731: aload 27
    //   733: invokestatic 233	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   736: pop
    //   737: new 60	java/io/IOException
    //   740: dup
    //   741: new 89	java/lang/StringBuilder
    //   744: dup
    //   745: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   748: ldc 145
    //   750: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   753: aload 26
    //   755: invokeinterface 175 1 0
    //   760: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   763: ldc 147
    //   765: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   768: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   771: invokespecial 156	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   774: athrow
    //   775: new 235	java/lang/String
    //   778: dup
    //   779: aload 14
    //   781: invokevirtual 239	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   784: invokespecial 242	java/lang/String:<init>	([B)V
    //   787: astore 17
    //   789: ldc 20
    //   791: iconst_2
    //   792: invokestatic 112	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   795: ifeq +11 -> 806
    //   798: ldc 20
    //   800: aload 17
    //   802: invokestatic 216	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   805: pop
    //   806: aload 17
    //   808: astore 9
    //   810: aload 8
    //   812: ifnull +10 -> 822
    //   815: aload 8
    //   817: invokeinterface 198 1 0
    //   822: new 89	java/lang/StringBuilder
    //   825: dup
    //   826: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   829: ldc 208
    //   831: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   834: iload 5
    //   836: invokevirtual 211	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   839: ldc 244
    //   841: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   844: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   847: astore 10
    //   849: aload 9
    //   851: ifnull +30 -> 881
    //   854: new 89	java/lang/StringBuilder
    //   857: dup
    //   858: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   861: aload 10
    //   863: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   866: ldc 172
    //   868: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   871: aload 9
    //   873: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   876: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   879: astore 10
    //   881: new 58	com/google/wireless/gdata/client/HttpException
    //   884: dup
    //   885: aload 10
    //   887: iload 5
    //   889: aconst_null
    //   890: invokespecial 247	com/google/wireless/gdata/client/HttpException:<init>	(Ljava/lang/String;ILjava/io/InputStream;)V
    //   893: astore 11
    //   895: aload 11
    //   897: athrow
    //   898: astore 12
    //   900: goto -293 -> 607
    //
    // Exception table:
    //   from	to	target	type
    //   12	22	167	java/net/URISyntaxException
    //   120	131	241	java/io/IOException
    //   554	577	605	finally
    //   577	586	605	finally
    //   592	602	605	finally
    //   775	789	605	finally
    //   662	682	688	java/net/URISyntaxException
    //   789	806	898	finally
  }

  // ERROR //
  private HttpEntity createEntityForEntry(GDataSerializer paramGDataSerializer, int paramInt)
    throws IOException
  {
    // Byte code:
    //   0: new 218	java/io/ByteArrayOutputStream
    //   3: dup
    //   4: invokespecial 219	java/io/ByteArrayOutputStream:<init>	()V
    //   7: astore_3
    //   8: aload_1
    //   9: aload_3
    //   10: iload_2
    //   11: invokeinterface 259 3 0
    //   16: aload_3
    //   17: invokevirtual 239	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   20: astore 8
    //   22: aload 8
    //   24: ifnull +49 -> 73
    //   27: ldc 20
    //   29: iconst_3
    //   30: invokestatic 112	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   33: ifeq +40 -> 73
    //   36: ldc 20
    //   38: new 89	java/lang/StringBuilder
    //   41: dup
    //   42: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   45: ldc_w 261
    //   48: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: new 235	java/lang/String
    //   54: dup
    //   55: aload 8
    //   57: ldc_w 263
    //   60: invokespecial 266	java/lang/String:<init>	([BLjava/lang/String;)V
    //   63: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   69: invokestatic 123	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   72: pop
    //   73: aload 8
    //   75: aload_0
    //   76: getfield 54	com/google/android/common/gdata/AndroidGDataClient:mResolver	Landroid/content/ContentResolver;
    //   79: invokestatic 270	android/net/http/AndroidHttpClient:getCompressedEntity	([BLandroid/content/ContentResolver;)Lorg/apache/http/entity/AbstractHttpEntity;
    //   82: astore 9
    //   84: aload 9
    //   86: aload_1
    //   87: invokeinterface 273 1 0
    //   92: invokevirtual 278	org/apache/http/entity/AbstractHttpEntity:setContentType	(Ljava/lang/String;)V
    //   95: aload 9
    //   97: areturn
    //   98: astore 6
    //   100: ldc 20
    //   102: ldc_w 280
    //   105: aload 6
    //   107: invokestatic 283	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   110: pop
    //   111: aload 6
    //   113: athrow
    //   114: astore 4
    //   116: ldc 20
    //   118: ldc_w 280
    //   121: aload 4
    //   123: invokestatic 283	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   126: pop
    //   127: new 60	java/io/IOException
    //   130: dup
    //   131: new 89	java/lang/StringBuilder
    //   134: dup
    //   135: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   138: ldc_w 285
    //   141: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: aload 4
    //   146: invokevirtual 286	com/google/wireless/gdata/parser/ParseException:getMessage	()Ljava/lang/String;
    //   149: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: invokevirtual 100	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   155: invokespecial 156	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   158: athrow
    //   159: astore 10
    //   161: new 288	java/lang/IllegalStateException
    //   164: dup
    //   165: ldc_w 290
    //   168: aload 10
    //   170: invokespecial 293	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   173: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   8	16	98	java/io/IOException
    //   8	16	114	com/google/wireless/gdata/parser/ParseException
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

  public InputStream createEntry(String paramString1, String paramString2, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new PostRequestCreator(null, createEntityForEntry(paramGDataSerializer, 1)), paramString1, paramString2);
    if (localInputStream != null)
      return localInputStream;
    throw new IOException("Unable to create entry.");
  }

  public QueryParams createQueryParams()
  {
    return new QueryParamsImpl();
  }

  public void deleteEntry(String paramString1, String paramString2)
    throws HttpException, IOException
  {
    if (StringUtils.isEmpty(paramString1))
      throw new IllegalArgumentException("you must specify an non-empty edit url");
    InputStream localInputStream = createAndExecuteMethod(new PostRequestCreator("DELETE", null), paramString1, paramString2);
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

  public InputStream getFeedAsStream(String paramString1, String paramString2)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new GetRequestCreator(), paramString1, paramString2);
    if (localInputStream != null)
      return localInputStream;
    throw new IOException("Unable to access feed.");
  }

  public InputStream getMediaEntryAsStream(String paramString1, String paramString2)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new GetRequestCreator(), paramString1, paramString2);
    if (localInputStream != null)
      return localInputStream;
    throw new IOException("Unable to access media entry.");
  }

  public InputStream updateEntry(String paramString1, String paramString2, GDataSerializer paramGDataSerializer)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new PostRequestCreator("PUT", createEntityForEntry(paramGDataSerializer, 2)), paramString1, paramString2);
    if (localInputStream != null)
      return localInputStream;
    throw new IOException("Unable to update entry.");
  }

  public InputStream updateMediaEntry(String paramString1, String paramString2, InputStream paramInputStream, String paramString3)
    throws HttpException, IOException
  {
    InputStream localInputStream = createAndExecuteMethod(new MediaPutRequestCreator(paramInputStream, paramString3), paramString1, paramString2);
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
 * Qualified Name:     com.google.android.common.gdata.AndroidGDataClient
 * JD-Core Version:    0.6.2
 */