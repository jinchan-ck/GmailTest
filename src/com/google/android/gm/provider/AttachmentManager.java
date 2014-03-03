package com.google.android.gm.provider;

import android.accounts.AccountManager;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AttachmentManager
{
  public static String ATTACHMENT_FROM = "attachment_from_sender";
  private static final String[] ATTACHMENT_NAME_STATUS_PROJECTION = { "filename", "status" };
  private static final String[] ATTACHMENT_PROJECTION = { "attachments._id", "messages_conversation", "messages_messageId", "messages_partId", "desiredRendition", "saveToSd", "filename", "mimeType", "fromAddress" };
  private static final Map<Long, String> sAccountsMap = Maps.newHashMap();
  private static Random sRandom = new Random(SystemClock.uptimeMillis());
  private final String mAccount;
  private final AccountManager mAccountManager;
  private final ContentResolver mContentResolver;
  private final Context mContext;
  private final SQLiteDatabase mDb;
  private final DownloadManager mDownloadManager;
  private final RestrictedMailEngine mRestrictedMailEngine;
  private final Urls mUrls;
  private long mUsedSpace;

  public AttachmentManager(Context paramContext, String paramString, SQLiteDatabase paramSQLiteDatabase, Urls paramUrls, RestrictedMailEngine paramRestrictedMailEngine)
  {
    this.mContext = paramContext;
    this.mAccount = paramString;
    this.mDb = paramSQLiteDatabase;
    this.mUrls = paramUrls;
    this.mRestrictedMailEngine = paramRestrictedMailEngine;
    this.mContentResolver = this.mContext.getContentResolver();
    this.mDownloadManager = ((DownloadManager)this.mContext.getSystemService("download"));
    this.mAccountManager = AccountManager.get(this.mContext);
    this.mUsedSpace = getUsedSpacePerAccount(paramString);
    this.mRestrictedMailEngine.postBackgroundTask(new Runnable()
    {
      public void run()
      {
        AttachmentManager.this.purgeOldAttachments();
      }
    });
    this.mRestrictedMailEngine.enqueueAttachmentDownloadTask();
  }

  public static boolean canDownloadAttachment(Context paramContext, Gmail.Attachment paramAttachment)
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if (localNetworkInfo == null);
    do
    {
      return false;
      if ((localNetworkInfo.getType() != 0) && (localNetworkInfo.isConnected()))
        return true;
    }
    while ((localNetworkInfo.getType() != 0) || (!localNetworkInfo.isConnected()));
    Long localLong = DownloadManager.getMaxBytesOverMobile(paramContext);
    if ((localLong == null) || (paramAttachment.size <= localLong.longValue()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  // ERROR //
  private void copyAttachment(long paramLong1, long paramLong2, long paramLong3, String paramString1, Gmail.AttachmentRendition paramAttachmentRendition, boolean paramBoolean, String paramString2, long paramLong4, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
  {
    // Byte code:
    //   0: iload 9
    //   2: ifeq +244 -> 246
    //   5: getstatic 194	android/os/Environment:DIRECTORY_DOWNLOADS	Ljava/lang/String;
    //   8: invokestatic 198	android/os/Environment:getExternalStoragePublicDirectory	(Ljava/lang/String;)Ljava/io/File;
    //   11: invokevirtual 204	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   14: astore 18
    //   16: aload 18
    //   18: aload 10
    //   20: invokestatic 208	com/google/android/gm/provider/AttachmentManager:getUniqueFileName	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   23: astore 19
    //   25: lload 11
    //   27: invokestatic 212	com/google/android/gm/provider/AttachmentManager:isDownloadIdValid	(J)Z
    //   30: ifeq +229 -> 259
    //   33: new 214	android/os/ParcelFileDescriptor$AutoCloseInputStream
    //   36: dup
    //   37: aload_0
    //   38: getfield 116	com/google/android/gm/provider/AttachmentManager:mDownloadManager	Landroid/app/DownloadManager;
    //   41: lload 11
    //   43: invokevirtual 218	android/app/DownloadManager:openDownloadedFile	(J)Landroid/os/ParcelFileDescriptor;
    //   46: invokespecial 221	android/os/ParcelFileDescriptor$AutoCloseInputStream:<init>	(Landroid/os/ParcelFileDescriptor;)V
    //   49: astore 26
    //   51: new 223	java/io/FileOutputStream
    //   54: dup
    //   55: aload 19
    //   57: invokespecial 226	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   60: astore 27
    //   62: invokestatic 229	android/os/SystemClock:elapsedRealtime	()J
    //   65: lstore 35
    //   67: sipush 4096
    //   70: newarray byte
    //   72: astore 37
    //   74: iconst_0
    //   75: istore 38
    //   77: aload 26
    //   79: aload 37
    //   81: invokevirtual 235	java/io/InputStream:read	([B)I
    //   84: istore 39
    //   86: iload 39
    //   88: iconst_m1
    //   89: if_icmpeq +285 -> 374
    //   92: aload 27
    //   94: aload 37
    //   96: iconst_0
    //   97: iload 39
    //   99: invokevirtual 241	java/io/OutputStream:write	([BII)V
    //   102: iload 38
    //   104: iload 39
    //   106: iadd
    //   107: istore 40
    //   109: iload 9
    //   111: ifne +15 -> 126
    //   114: aload_0
    //   115: aload_0
    //   116: getfield 130	com/google/android/gm/provider/AttachmentManager:mUsedSpace	J
    //   119: iload 39
    //   121: i2l
    //   122: ladd
    //   123: putfield 130	com/google/android/gm/provider/AttachmentManager:mUsedSpace	J
    //   126: invokestatic 229	android/os/SystemClock:elapsedRealtime	()J
    //   129: lload 35
    //   131: lsub
    //   132: ldc2_w 242
    //   135: lcmp
    //   136: ifle +231 -> 367
    //   139: new 187	java/io/IOException
    //   142: dup
    //   143: ldc 245
    //   145: invokespecial 246	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   148: athrow
    //   149: astore 28
    //   151: ldc 248
    //   153: aload 28
    //   155: ldc 250
    //   157: iconst_1
    //   158: anewarray 4	java/lang/Object
    //   161: dup
    //   162: iconst_0
    //   163: aload 19
    //   165: aastore
    //   166: invokestatic 256	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   169: pop
    //   170: new 200	java/io/File
    //   173: dup
    //   174: aload 19
    //   176: invokespecial 257	java/io/File:<init>	(Ljava/lang/String;)V
    //   179: invokevirtual 260	java/io/File:delete	()Z
    //   182: pop
    //   183: aload_0
    //   184: lload_1
    //   185: lload_3
    //   186: lload 5
    //   188: aload 7
    //   190: aload 8
    //   192: iload 9
    //   194: sipush 1000
    //   197: aconst_null
    //   198: invokespecial 264	com/google/android/gm/provider/AttachmentManager:onAttachmentDownloadFinished	(JJJLjava/lang/String;Lcom/google/android/gm/provider/Gmail$AttachmentRendition;ZILjava/lang/String;)V
    //   201: lload 11
    //   203: invokestatic 212	com/google/android/gm/provider/AttachmentManager:isDownloadIdValid	(J)Z
    //   206: ifeq +19 -> 225
    //   209: aload_0
    //   210: getfield 116	com/google/android/gm/provider/AttachmentManager:mDownloadManager	Landroid/app/DownloadManager;
    //   213: iconst_1
    //   214: newarray long
    //   216: dup
    //   217: iconst_0
    //   218: lload 11
    //   220: lastore
    //   221: invokevirtual 268	android/app/DownloadManager:remove	([J)I
    //   224: pop
    //   225: aload 26
    //   227: ifnull +8 -> 235
    //   230: aload 26
    //   232: invokevirtual 271	java/io/InputStream:close	()V
    //   235: aload 27
    //   237: ifnull +8 -> 245
    //   240: aload 27
    //   242: invokevirtual 272	java/io/OutputStream:close	()V
    //   245: return
    //   246: aload_0
    //   247: aload_0
    //   248: getfield 92	com/google/android/gm/provider/AttachmentManager:mAccount	Ljava/lang/String;
    //   251: invokespecial 276	com/google/android/gm/provider/AttachmentManager:getCacheDir	(Ljava/lang/String;)Ljava/lang/String;
    //   254: astore 18
    //   256: goto -240 -> 16
    //   259: new 278	java/io/FileInputStream
    //   262: dup
    //   263: aload 13
    //   265: invokestatic 281	com/google/android/gm/provider/AttachmentManager:getPathFromUri	(Ljava/lang/String;)Ljava/lang/String;
    //   268: invokespecial 282	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   271: astore 25
    //   273: aload 25
    //   275: astore 26
    //   277: goto -226 -> 51
    //   280: astore 22
    //   282: iconst_2
    //   283: anewarray 4	java/lang/Object
    //   286: astore 23
    //   288: aload 23
    //   290: iconst_0
    //   291: lload 11
    //   293: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   296: aastore
    //   297: aload 23
    //   299: iconst_1
    //   300: aload 13
    //   302: aastore
    //   303: ldc 248
    //   305: ldc_w 288
    //   308: aload 23
    //   310: invokestatic 291	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   313: pop
    //   314: aload_0
    //   315: lload_1
    //   316: lload_3
    //   317: lload 5
    //   319: aload 7
    //   321: aload 8
    //   323: iload 9
    //   325: sipush 404
    //   328: aconst_null
    //   329: invokespecial 264	com/google/android/gm/provider/AttachmentManager:onAttachmentDownloadFinished	(JJJLjava/lang/String;Lcom/google/android/gm/provider/Gmail$AttachmentRendition;ZILjava/lang/String;)V
    //   332: return
    //   333: astore 20
    //   335: ldc 248
    //   337: ldc_w 293
    //   340: iconst_0
    //   341: anewarray 4	java/lang/Object
    //   344: invokestatic 291	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   347: pop
    //   348: aload_0
    //   349: lload_1
    //   350: lload_3
    //   351: lload 5
    //   353: aload 7
    //   355: aload 8
    //   357: iload 9
    //   359: sipush 200
    //   362: aconst_null
    //   363: invokespecial 264	com/google/android/gm/provider/AttachmentManager:onAttachmentDownloadFinished	(JJJLjava/lang/String;Lcom/google/android/gm/provider/Gmail$AttachmentRendition;ZILjava/lang/String;)V
    //   366: return
    //   367: iload 40
    //   369: istore 38
    //   371: goto -294 -> 77
    //   374: new 295	java/lang/StringBuilder
    //   377: dup
    //   378: invokespecial 296	java/lang/StringBuilder:<init>	()V
    //   381: ldc_w 298
    //   384: invokevirtual 302	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   387: aload 19
    //   389: invokevirtual 302	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   392: invokevirtual 305	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   395: astore 41
    //   397: aload_0
    //   398: lload_1
    //   399: lload_3
    //   400: lload 5
    //   402: aload 7
    //   404: aload 8
    //   406: iload 9
    //   408: sipush 200
    //   411: aload 41
    //   413: invokespecial 264	com/google/android/gm/provider/AttachmentManager:onAttachmentDownloadFinished	(JJJLjava/lang/String;Lcom/google/android/gm/provider/Gmail$AttachmentRendition;ZILjava/lang/String;)V
    //   416: iload 9
    //   418: ifeq -217 -> 201
    //   421: aload 15
    //   423: ifnonnull +84 -> 507
    //   426: aload 16
    //   428: astore 43
    //   430: aload 43
    //   432: ifnonnull +7 -> 439
    //   435: aload 10
    //   437: astore 43
    //   439: aload 10
    //   441: invokestatic 311	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   444: ifne +86 -> 530
    //   447: aload 10
    //   449: astore 48
    //   451: aload_0
    //   452: getfield 116	com/google/android/gm/provider/AttachmentManager:mDownloadManager	Landroid/app/DownloadManager;
    //   455: aload 48
    //   457: aload 43
    //   459: iconst_1
    //   460: aload 14
    //   462: aload 19
    //   464: iload 38
    //   466: i2l
    //   467: iconst_0
    //   468: invokevirtual 315	android/app/DownloadManager:addCompletedDownload	(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;JZ)J
    //   471: pop2
    //   472: new 317	android/content/Intent
    //   475: dup
    //   476: ldc_w 319
    //   479: invokespecial 320	android/content/Intent:<init>	(Ljava/lang/String;)V
    //   482: astore 46
    //   484: aload 46
    //   486: aload 41
    //   488: invokestatic 326	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   491: invokevirtual 330	android/content/Intent:setData	(Landroid/net/Uri;)Landroid/content/Intent;
    //   494: pop
    //   495: aload_0
    //   496: getfield 90	com/google/android/gm/provider/AttachmentManager:mContext	Landroid/content/Context;
    //   499: aload 46
    //   501: invokevirtual 334	android/content/Context:sendBroadcast	(Landroid/content/Intent;)V
    //   504: goto -303 -> 201
    //   507: aload 17
    //   509: iconst_1
    //   510: anewarray 4	java/lang/Object
    //   513: dup
    //   514: iconst_0
    //   515: aload 15
    //   517: aastore
    //   518: invokestatic 338	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   521: astore 42
    //   523: aload 42
    //   525: astore 43
    //   527: goto -97 -> 430
    //   530: aload 19
    //   532: invokestatic 326	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   535: invokevirtual 341	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   538: astore 51
    //   540: aload 51
    //   542: astore 48
    //   544: goto -93 -> 451
    //   547: astore 44
    //   549: ldc 248
    //   551: aload 44
    //   553: ldc_w 343
    //   556: iconst_0
    //   557: anewarray 4	java/lang/Object
    //   560: invokestatic 256	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   563: pop
    //   564: goto -92 -> 472
    //   567: astore 31
    //   569: ldc 248
    //   571: ldc_w 293
    //   574: iconst_0
    //   575: anewarray 4	java/lang/Object
    //   578: invokestatic 291	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   581: pop
    //   582: goto -357 -> 225
    //   585: astore 33
    //   587: return
    //
    // Exception table:
    //   from	to	target	type
    //   62	74	149	java/io/IOException
    //   77	86	149	java/io/IOException
    //   92	102	149	java/io/IOException
    //   114	126	149	java/io/IOException
    //   126	149	149	java/io/IOException
    //   374	416	149	java/io/IOException
    //   439	447	149	java/io/IOException
    //   451	472	149	java/io/IOException
    //   472	504	149	java/io/IOException
    //   507	523	149	java/io/IOException
    //   530	540	149	java/io/IOException
    //   549	564	149	java/io/IOException
    //   25	51	280	java/io/FileNotFoundException
    //   51	62	280	java/io/FileNotFoundException
    //   259	273	280	java/io/FileNotFoundException
    //   25	51	333	java/lang/NullPointerException
    //   51	62	333	java/lang/NullPointerException
    //   259	273	333	java/lang/NullPointerException
    //   439	447	547	java/lang/IllegalArgumentException
    //   451	472	547	java/lang/IllegalArgumentException
    //   530	540	547	java/lang/IllegalArgumentException
    //   201	225	567	java/lang/NullPointerException
    //   230	235	585	java/io/IOException
    //   240	245	585	java/io/IOException
  }

  private void deleteAttachment(long paramLong1, long paramLong2, String paramString)
  {
    if (paramString != null)
      new File(getPathFromUri(paramString)).delete();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("status", Integer.valueOf(-1));
    localContentValues.put("downloadId", Integer.valueOf(-1));
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong2);
    localSQLiteDatabase.update("attachments", localContentValues, "_id = ?", arrayOfString);
    notifyChanged(paramLong1);
  }

  public static String getAccountFromDownloadId(long paramLong)
  {
    return (String)sAccountsMap.remove(Long.valueOf(paramLong));
  }

  private String getCacheDir(String paramString)
  {
    return this.mContext.getCacheDir().getAbsolutePath().concat("/").concat(paramString);
  }

  static String getPathFromUri(String paramString)
  {
    if (paramString.startsWith("file://"))
      paramString = paramString.substring(7);
    return paramString;
  }

  private static String getUniqueFileName(String paramString1, String paramString2)
  {
    int i = paramString2.lastIndexOf('.');
    String str1 = "";
    if (i != -1)
    {
      str1 = paramString2.substring(i);
      paramString2 = paramString2.substring(0, i);
    }
    if (paramString2.indexOf(File.separatorChar) != -1)
      paramString2 = paramString2.replace(File.separatorChar, '_');
    File localFile = new File(paramString1);
    if (!localFile.exists())
      localFile.mkdirs();
    String str2;
    while (localFile.isDirectory())
    {
      if (!paramString1.endsWith(File.separator))
        paramString1 = paramString1.concat(File.separator);
      str2 = paramString1 + paramString2 + str1;
      if (new File(str2).exists())
        break;
      return str2;
    }
    return null;
    String str3 = paramString2 + "-";
    int j = 1;
    int k = 1;
    while (true)
    {
      if (k >= 1000000000)
        break label266;
      for (int m = 0; ; m++)
      {
        if (m >= 9)
          break label256;
        str2 = paramString1 + str3 + j + str1;
        if (!new File(str2).exists())
          break;
        j += 1 + sRandom.nextInt(k);
      }
      label256: k *= 10;
    }
    label266: return null;
  }

  private long getUsedSpacePerAccount(String paramString)
  {
    long l = 0L;
    File localFile = new File(getCacheDir(paramString));
    if (localFile.listFiles() != null)
    {
      File[] arrayOfFile = localFile.listFiles();
      int i = arrayOfFile.length;
      for (int j = 0; j < i; j++)
        l += arrayOfFile[j].length();
    }
    return l;
  }

  private static boolean isDownloadIdValid(long paramLong)
  {
    return paramLong != -1L;
  }

  public static boolean isDownloadStillPresent(String paramString)
  {
    return new File(getPathFromUri(paramString)).exists();
  }

  private boolean isLowSpace()
  {
    File localFile = this.mContext.getCacheDir();
    long l1 = localFile.getTotalSpace();
    long l2 = localFile.getUsableSpace();
    try
    {
      int i = this.mAccountManager.getAccounts().length;
      long l3 = ()(0.25F * (float)l1 / i);
      if (this.mUsedSpace >= l3)
      {
        this.mUsedSpace = 0L;
        this.mUsedSpace = getUsedSpacePerAccount(this.mAccount);
      }
      boolean bool1 = (float)l2 < 0.25F * (float)l1;
      boolean bool2 = false;
      if (bool1)
      {
        boolean bool3 = this.mUsedSpace < l3;
        bool2 = false;
        if (!bool3)
          bool2 = true;
      }
      return bool2;
    }
    catch (NullPointerException localNullPointerException)
    {
      LogUtils.e("Gmail", "This maybe called from tests where we don't have Account Manager.", new Object[0]);
    }
    return false;
  }

  public static boolean isStatusError(int paramInt)
  {
    return paramInt > 200;
  }

  public static boolean isStatusPaused(int paramInt)
  {
    return paramInt == 193;
  }

  public static boolean isStatusPending(int paramInt)
  {
    return paramInt == 190;
  }

  public static boolean isStatusRunning(int paramInt)
  {
    return paramInt == 192;
  }

  public static boolean isStatusSuccess(int paramInt)
  {
    return paramInt == 200;
  }

  public static boolean isStatusValid(int paramInt)
  {
    return paramInt != -1;
  }

  private Cursor newAttachmentCursor(long paramLong, String paramString, Gmail.AttachmentRendition paramAttachmentRendition, boolean paramBoolean, String[] paramArrayOfString)
  {
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString = new String[4];
    arrayOfString[0] = Long.toString(paramLong);
    arrayOfString[1] = paramString;
    arrayOfString[2] = paramAttachmentRendition.toString();
    if (paramBoolean);
    for (long l = 1L; ; l = 0L)
    {
      arrayOfString[3] = Long.toString(l);
      return localSQLiteDatabase.query("attachments", paramArrayOfString, "messages_messageId = ? AND messages_partId = ? AND desiredRendition = ? AND saveToSd = ?", arrayOfString, null, null, null);
    }
  }

  private void notifyChanged(long paramLong)
  {
    this.mContext.getContentResolver().notifyChange(Gmail.getAttachmentsForConversationUri(this.mAccount, paramLong), null, false);
    UiProvider.notifyAttachmentChanged(this.mAccount, paramLong);
  }

  private void onAttachmentDownloadFinished(long paramLong1, long paramLong2, long paramLong3, String paramString1, Gmail.AttachmentRendition paramAttachmentRendition, boolean paramBoolean, int paramInt, String paramString2)
  {
    updateAttachmentEntry(paramLong1, paramInt, paramString2);
    if (paramAttachmentRendition == Gmail.AttachmentRendition.BEST)
    {
      int i;
      if (paramBoolean)
      {
        i = 1;
        if (!isStatusSuccess(paramInt))
          break label63;
      }
      label63: for (int j = 3; ; j = 1)
      {
        UiProvider.onAttachmentDownloadFinished(this.mAccount, paramLong2, paramLong3, paramString1, i, j, paramInt, paramString2);
        return;
        i = 0;
        break;
      }
    }
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = Long.valueOf(paramLong2);
    arrayOfObject[1] = Long.valueOf(paramLong3);
    arrayOfObject[2] = Long.valueOf(paramLong1);
    arrayOfObject[3] = paramString1;
    LogUtils.d("Gmail", "Dropping download finished, as this is an thumbnail attachment.  %d/%d/%d/%s", arrayOfObject);
  }

  private void purgeAttachmentEntries(List<Long> paramList)
  {
    int i = paramList.size();
    if (i > 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("_id IN (");
      for (int j = 0; j < i; j++)
      {
        if (j > 0)
          localStringBuilder.append(", ");
        Long localLong = (Long)paramList.get(j);
        localStringBuilder.append("'" + localLong.toString() + "'");
      }
      localStringBuilder.append(')');
      this.mDb.delete("attachments", localStringBuilder.toString(), null);
    }
  }

  private void purgeInvalidAttachments()
  {
    ArrayList localArrayList1 = Lists.newArrayList();
    ArrayList localArrayList2 = Lists.newArrayList();
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString1 = { "_id", "filename", "saveToSd" };
    String[] arrayOfString2 = new String[4];
    arrayOfString2[0] = Integer.toString(190);
    arrayOfString2[1] = Integer.toString(192);
    arrayOfString2[2] = Integer.toString(193);
    arrayOfString2[3] = Integer.toString(200);
    Cursor localCursor = localSQLiteDatabase.query("attachments", arrayOfString1, "status NOT IN (?, ?, ?, ?)", arrayOfString2, null, null, null);
    while (true)
    {
      try
      {
        if (!localCursor.moveToNext())
          break;
        long l = localCursor.getLong(0);
        String str = localCursor.getString(1);
        if (localCursor.getInt(2) != 0)
        {
          i = 1;
          localArrayList1.add(Long.valueOf(l));
          if (i != 0)
            str = null;
          localArrayList2.add(str);
          continue;
        }
      }
      finally
      {
        localCursor.close();
      }
      int i = 0;
    }
    localCursor.close();
    purgeOldAttachmentFiles(localArrayList2);
    purgeAttachmentEntries(localArrayList1);
  }

  // ERROR //
  private void purgeInvalidDownloadingAttachments()
  {
    // Byte code:
    //   0: invokestatic 548	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
    //   3: astore_1
    //   4: invokestatic 548	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
    //   7: astore_2
    //   8: aload_0
    //   9: getfield 94	com/google/android/gm/provider/AttachmentManager:mDb	Landroid/database/sqlite/SQLiteDatabase;
    //   12: astore_3
    //   13: iconst_4
    //   14: anewarray 39	java/lang/String
    //   17: dup
    //   18: iconst_0
    //   19: ldc_w 550
    //   22: aastore
    //   23: dup
    //   24: iconst_1
    //   25: ldc_w 359
    //   28: aastore
    //   29: dup
    //   30: iconst_2
    //   31: ldc 41
    //   33: aastore
    //   34: dup
    //   35: iconst_3
    //   36: ldc 57
    //   38: aastore
    //   39: astore 4
    //   41: iconst_1
    //   42: anewarray 39	java/lang/String
    //   45: astore 5
    //   47: aload 5
    //   49: iconst_0
    //   50: sipush 192
    //   53: invokestatic 552	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   56: aastore
    //   57: aload_3
    //   58: ldc_w 364
    //   61: aload 4
    //   63: ldc_w 584
    //   66: aload 5
    //   68: aconst_null
    //   69: aconst_null
    //   70: aconst_null
    //   71: invokevirtual 483	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   74: astore 6
    //   76: aload 6
    //   78: invokeinterface 559 1 0
    //   83: ifeq +204 -> 287
    //   86: aload 6
    //   88: iconst_0
    //   89: invokeinterface 563 2 0
    //   94: lstore 8
    //   96: aload 6
    //   98: iconst_1
    //   99: invokeinterface 563 2 0
    //   104: lstore 10
    //   106: new 586	android/app/DownloadManager$Query
    //   109: dup
    //   110: invokespecial 587	android/app/DownloadManager$Query:<init>	()V
    //   113: iconst_1
    //   114: newarray long
    //   116: dup
    //   117: iconst_0
    //   118: lload 10
    //   120: lastore
    //   121: invokevirtual 591	android/app/DownloadManager$Query:setFilterById	([J)Landroid/app/DownloadManager$Query;
    //   124: astore 12
    //   126: aload_0
    //   127: getfield 116	com/google/android/gm/provider/AttachmentManager:mDownloadManager	Landroid/app/DownloadManager;
    //   130: aload 12
    //   132: invokevirtual 594	android/app/DownloadManager:query	(Landroid/app/DownloadManager$Query;)Landroid/database/Cursor;
    //   135: astore 13
    //   137: aload 13
    //   139: ifnull -63 -> 76
    //   142: aload 13
    //   144: invokeinterface 559 1 0
    //   149: ifeq +61 -> 210
    //   152: aload 13
    //   154: aload 13
    //   156: ldc 43
    //   158: invokeinterface 598 2 0
    //   163: invokeinterface 569 2 0
    //   168: istore 19
    //   170: iload 19
    //   172: iconst_1
    //   173: if_icmpeq +15 -> 188
    //   176: iload 19
    //   178: iconst_2
    //   179: if_icmpeq +9 -> 188
    //   182: iload 19
    //   184: iconst_4
    //   185: if_icmpne +25 -> 210
    //   188: aload 13
    //   190: invokeinterface 576 1 0
    //   195: goto -119 -> 76
    //   198: astore 7
    //   200: aload 6
    //   202: invokeinterface 576 1 0
    //   207: aload 7
    //   209: athrow
    //   210: aload 13
    //   212: invokeinterface 576 1 0
    //   217: aload 6
    //   219: iconst_2
    //   220: invokeinterface 566 2 0
    //   225: astore 15
    //   227: aload 6
    //   229: iconst_3
    //   230: invokeinterface 569 2 0
    //   235: ifeq +46 -> 281
    //   238: iconst_1
    //   239: istore 16
    //   241: aload_1
    //   242: lload 8
    //   244: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   247: invokevirtual 575	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   250: pop
    //   251: iload 16
    //   253: ifeq +6 -> 259
    //   256: aconst_null
    //   257: astore 15
    //   259: aload_2
    //   260: aload 15
    //   262: invokevirtual 575	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   265: pop
    //   266: goto -190 -> 76
    //   269: astore 14
    //   271: aload 13
    //   273: invokeinterface 576 1 0
    //   278: aload 14
    //   280: athrow
    //   281: iconst_0
    //   282: istore 16
    //   284: goto -43 -> 241
    //   287: aload 6
    //   289: invokeinterface 576 1 0
    //   294: aload_0
    //   295: aload_2
    //   296: invokespecial 579	com/google/android/gm/provider/AttachmentManager:purgeOldAttachmentFiles	(Ljava/util/List;)V
    //   299: aload_0
    //   300: aload_1
    //   301: invokespecial 581	com/google/android/gm/provider/AttachmentManager:purgeAttachmentEntries	(Ljava/util/List;)V
    //   304: return
    //
    // Exception table:
    //   from	to	target	type
    //   76	137	198	finally
    //   188	195	198	finally
    //   210	238	198	finally
    //   241	251	198	finally
    //   259	266	198	finally
    //   271	281	198	finally
    //   142	170	269	finally
  }

  private void purgeOldAttachmentFiles(List<String> paramList)
  {
    if (paramList != null)
    {
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if (str != null)
          new File(getPathFromUri(str)).delete();
      }
    }
  }

  private static String requestDescription(long paramLong1, long paramLong2, String paramString, Gmail.AttachmentRendition paramAttachmentRendition, boolean paramBoolean)
  {
    return "conversationId = " + paramLong1 + ", messageId = " + paramLong2 + ", partId = " + paramString + ", rendition = " + paramAttachmentRendition.toString() + ", saveToSd = " + Boolean.toString(paramBoolean);
  }

  private void resetAttachment(long paramLong1, long paramLong2, String paramString)
  {
    if (paramString != null)
      new File(getPathFromUri(paramString)).delete();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("status", Integer.valueOf(190));
    localContentValues.put("downloadId", Integer.valueOf(-1));
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong2);
    localSQLiteDatabase.update("attachments", localContentValues, "_id = ?", arrayOfString);
    notifyChanged(paramLong1);
  }

  // ERROR //
  private void startAttachmentDownloadInDownloadManager(long paramLong1, long paramLong2, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 94	com/google/android/gm/provider/AttachmentManager:mDb	Landroid/database/sqlite/SQLiteDatabase;
    //   4: invokevirtual 639	android/database/sqlite/SQLiteDatabase:isDbLockedByCurrentThread	()Z
    //   7: ifeq +16 -> 23
    //   10: ldc 248
    //   12: ldc_w 641
    //   15: iconst_0
    //   16: anewarray 4	java/lang/Object
    //   19: invokestatic 644	com/google/android/gm/provider/LogUtils:wtf	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   22: pop
    //   23: bipush 8
    //   25: anewarray 39	java/lang/String
    //   28: dup
    //   29: iconst_0
    //   30: ldc 49
    //   32: aastore
    //   33: dup
    //   34: iconst_1
    //   35: ldc 51
    //   37: aastore
    //   38: dup
    //   39: iconst_2
    //   40: ldc 53
    //   42: aastore
    //   43: dup
    //   44: iconst_3
    //   45: ldc_w 646
    //   48: aastore
    //   49: dup
    //   50: iconst_4
    //   51: ldc_w 648
    //   54: aastore
    //   55: dup
    //   56: iconst_5
    //   57: ldc 57
    //   59: aastore
    //   60: dup
    //   61: bipush 6
    //   63: ldc 41
    //   65: aastore
    //   66: dup
    //   67: bipush 7
    //   69: ldc_w 650
    //   72: aastore
    //   73: astore 6
    //   75: aload_0
    //   76: getfield 94	com/google/android/gm/provider/AttachmentManager:mDb	Landroid/database/sqlite/SQLiteDatabase;
    //   79: astore 7
    //   81: iconst_1
    //   82: anewarray 39	java/lang/String
    //   85: astore 8
    //   87: aload 8
    //   89: iconst_0
    //   90: lload_1
    //   91: invokestatic 362	java/lang/Long:toString	(J)Ljava/lang/String;
    //   94: aastore
    //   95: aload 7
    //   97: ldc_w 364
    //   100: aload 6
    //   102: ldc_w 366
    //   105: aload 8
    //   107: aconst_null
    //   108: aconst_null
    //   109: aconst_null
    //   110: invokevirtual 483	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   113: astore 9
    //   115: aload 9
    //   117: invokeinterface 559 1 0
    //   122: ifne +36 -> 158
    //   125: iconst_1
    //   126: anewarray 4	java/lang/Object
    //   129: astore 58
    //   131: aload 58
    //   133: iconst_0
    //   134: lload_1
    //   135: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   138: aastore
    //   139: ldc 248
    //   141: ldc_w 652
    //   144: aload 58
    //   146: invokestatic 291	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   149: pop
    //   150: aload 9
    //   152: invokeinterface 576 1 0
    //   157: return
    //   158: aload 9
    //   160: iconst_0
    //   161: invokeinterface 563 2 0
    //   166: lstore 11
    //   168: aload 9
    //   170: iconst_1
    //   171: invokeinterface 563 2 0
    //   176: lstore 13
    //   178: aload 9
    //   180: iconst_2
    //   181: invokeinterface 566 2 0
    //   186: astore 15
    //   188: aload 9
    //   190: iconst_3
    //   191: invokeinterface 566 2 0
    //   196: astore 16
    //   198: aload 9
    //   200: iconst_4
    //   201: invokeinterface 566 2 0
    //   206: invokestatic 655	com/google/android/gm/provider/Gmail$AttachmentRendition:valueOf	(Ljava/lang/String;)Lcom/google/android/gm/provider/Gmail$AttachmentRendition;
    //   209: astore 17
    //   211: aload 9
    //   213: iconst_5
    //   214: invokeinterface 569 2 0
    //   219: ifeq +127 -> 346
    //   222: iconst_1
    //   223: istore 18
    //   225: aload 9
    //   227: bipush 6
    //   229: invokeinterface 566 2 0
    //   234: astore 19
    //   236: lload 11
    //   238: lload 13
    //   240: aload 15
    //   242: aload 17
    //   244: iload 18
    //   246: invokestatic 657	com/google/android/gm/provider/AttachmentManager:requestDescription	(JJLjava/lang/String;Lcom/google/android/gm/provider/Gmail$AttachmentRendition;Z)Ljava/lang/String;
    //   249: astore 20
    //   251: aload 17
    //   253: getstatic 660	com/google/android/gm/provider/Gmail$AttachmentRendition:SIMPLE	Lcom/google/android/gm/provider/Gmail$AttachmentRendition;
    //   256: if_acmpeq +96 -> 352
    //   259: iconst_1
    //   260: istore 21
    //   262: aload_0
    //   263: getfield 96	com/google/android/gm/provider/AttachmentManager:mUrls	Lcom/google/android/gm/provider/Urls;
    //   266: aload_0
    //   267: getfield 98	com/google/android/gm/provider/AttachmentManager:mRestrictedMailEngine	Lcom/google/android/gm/provider/AttachmentManager$RestrictedMailEngine;
    //   270: invokeinterface 663 1 0
    //   275: aload 16
    //   277: sipush 256
    //   280: iload 21
    //   282: invokevirtual 669	com/google/android/gm/provider/Urls:getFetchAttachmentUri	(ILjava/lang/String;IZ)Ljava/net/URI;
    //   285: astore 22
    //   287: aload_0
    //   288: getfield 98	com/google/android/gm/provider/AttachmentManager:mRestrictedMailEngine	Lcom/google/android/gm/provider/AttachmentManager$RestrictedMailEngine;
    //   291: invokeinterface 672 1 0
    //   296: astore 57
    //   298: aload 57
    //   300: astore 24
    //   302: aload 24
    //   304: ifnonnull +54 -> 358
    //   307: iconst_2
    //   308: anewarray 4	java/lang/Object
    //   311: astore 55
    //   313: aload 55
    //   315: iconst_0
    //   316: lload_1
    //   317: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   320: aastore
    //   321: aload 55
    //   323: iconst_1
    //   324: aload 20
    //   326: aastore
    //   327: ldc 248
    //   329: ldc_w 674
    //   332: aload 55
    //   334: invokestatic 291	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   337: pop
    //   338: aload 9
    //   340: invokeinterface 576 1 0
    //   345: return
    //   346: iconst_0
    //   347: istore 18
    //   349: goto -124 -> 225
    //   352: iconst_0
    //   353: istore 21
    //   355: goto -93 -> 262
    //   358: aload_0
    //   359: getfield 92	com/google/android/gm/provider/AttachmentManager:mAccount	Ljava/lang/String;
    //   362: aload 24
    //   364: invokestatic 677	com/google/android/gm/provider/Urls:getCookieString	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   367: astore 25
    //   369: aload 22
    //   371: invokevirtual 680	java/net/URI:toString	()Ljava/lang/String;
    //   374: astore 26
    //   376: aload_0
    //   377: getfield 106	com/google/android/gm/provider/AttachmentManager:mContentResolver	Landroid/content/ContentResolver;
    //   380: invokestatic 686	com/google/android/common/http/UrlRules:getRules	(Landroid/content/ContentResolver;)Lcom/google/android/common/http/UrlRules;
    //   383: aload 26
    //   385: invokevirtual 690	com/google/android/common/http/UrlRules:matchRule	(Ljava/lang/String;)Lcom/google/android/common/http/UrlRules$Rule;
    //   388: aload 26
    //   390: invokevirtual 695	com/google/android/common/http/UrlRules$Rule:apply	(Ljava/lang/String;)Ljava/lang/String;
    //   393: astore 27
    //   395: aload 27
    //   397: ifnonnull +101 -> 498
    //   400: ldc 248
    //   402: ldc_w 697
    //   405: iconst_1
    //   406: anewarray 4	java/lang/Object
    //   409: dup
    //   410: iconst_0
    //   411: aload 22
    //   413: aastore
    //   414: invokestatic 700	com/google/android/gm/provider/LogUtils:w	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   417: pop
    //   418: new 347	android/content/ContentValues
    //   421: dup
    //   422: invokespecial 348	android/content/ContentValues:<init>	()V
    //   425: astore 29
    //   427: aload 29
    //   429: ldc 43
    //   431: sipush 1000
    //   434: invokestatic 353	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   437: invokevirtual 357	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   440: aload_0
    //   441: getfield 94	com/google/android/gm/provider/AttachmentManager:mDb	Landroid/database/sqlite/SQLiteDatabase;
    //   444: astore 30
    //   446: iconst_2
    //   447: anewarray 39	java/lang/String
    //   450: astore 31
    //   452: aload 31
    //   454: iconst_0
    //   455: lload_1
    //   456: invokestatic 362	java/lang/Long:toString	(J)Ljava/lang/String;
    //   459: aastore
    //   460: aload 31
    //   462: iconst_1
    //   463: lload_3
    //   464: invokestatic 362	java/lang/Long:toString	(J)Ljava/lang/String;
    //   467: aastore
    //   468: aload 30
    //   470: ldc_w 364
    //   473: aload 29
    //   475: ldc_w 702
    //   478: aload 31
    //   480: invokevirtual 372	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   483: pop
    //   484: aload_0
    //   485: lload 11
    //   487: invokespecial 375	com/google/android/gm/provider/AttachmentManager:notifyChanged	(J)V
    //   490: aload 9
    //   492: invokeinterface 576 1 0
    //   497: return
    //   498: new 704	android/app/DownloadManager$Request
    //   501: dup
    //   502: aload 27
    //   504: invokestatic 326	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   507: invokespecial 707	android/app/DownloadManager$Request:<init>	(Landroid/net/Uri;)V
    //   510: ldc_w 709
    //   513: aload 25
    //   515: invokevirtual 713	android/app/DownloadManager$Request:addRequestHeader	(Ljava/lang/String;Ljava/lang/String;)Landroid/app/DownloadManager$Request;
    //   518: aload 19
    //   520: invokevirtual 717	android/app/DownloadManager$Request:setTitle	(Ljava/lang/CharSequence;)Landroid/app/DownloadManager$Request;
    //   523: iconst_0
    //   524: invokevirtual 721	android/app/DownloadManager$Request:setVisibleInDownloadsUi	(Z)Landroid/app/DownloadManager$Request;
    //   527: astore 33
    //   529: lload_3
    //   530: ldc2_w 449
    //   533: lcmp
    //   534: ifne +10 -> 544
    //   537: aload 33
    //   539: iconst_2
    //   540: invokevirtual 725	android/app/DownloadManager$Request:setNotificationVisibility	(I)Landroid/app/DownloadManager$Request;
    //   543: pop
    //   544: iload 5
    //   546: ifeq +10 -> 556
    //   549: aload 33
    //   551: iconst_2
    //   552: invokevirtual 728	android/app/DownloadManager$Request:setAllowedNetworkTypes	(I)Landroid/app/DownloadManager$Request;
    //   555: pop
    //   556: ldc2_w 449
    //   559: lstore 36
    //   561: aload_0
    //   562: getfield 116	com/google/android/gm/provider/AttachmentManager:mDownloadManager	Landroid/app/DownloadManager;
    //   565: aload 33
    //   567: invokevirtual 732	android/app/DownloadManager:enqueue	(Landroid/app/DownloadManager$Request;)J
    //   570: lstore 53
    //   572: lload 53
    //   574: lstore 36
    //   576: iconst_1
    //   577: istore 45
    //   579: iload 45
    //   581: ifeq -97 -> 484
    //   584: getstatic 85	com/google/android/gm/provider/AttachmentManager:sAccountsMap	Ljava/util/Map;
    //   587: lload 36
    //   589: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   592: aload_0
    //   593: getfield 92	com/google/android/gm/provider/AttachmentManager:mAccount	Ljava/lang/String;
    //   596: invokeinterface 735 3 0
    //   601: pop
    //   602: new 347	android/content/ContentValues
    //   605: dup
    //   606: invokespecial 348	android/content/ContentValues:<init>	()V
    //   609: astore 47
    //   611: aload 47
    //   613: ldc_w 359
    //   616: lload 36
    //   618: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   621: invokevirtual 738	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   624: aload 47
    //   626: ldc 43
    //   628: sipush 192
    //   631: invokestatic 353	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   634: invokevirtual 357	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   637: aload_0
    //   638: getfield 94	com/google/android/gm/provider/AttachmentManager:mDb	Landroid/database/sqlite/SQLiteDatabase;
    //   641: astore 48
    //   643: iconst_2
    //   644: anewarray 39	java/lang/String
    //   647: astore 49
    //   649: aload 49
    //   651: iconst_0
    //   652: lload_1
    //   653: invokestatic 362	java/lang/Long:toString	(J)Ljava/lang/String;
    //   656: aastore
    //   657: aload 49
    //   659: iconst_1
    //   660: lload_3
    //   661: invokestatic 362	java/lang/Long:toString	(J)Ljava/lang/String;
    //   664: aastore
    //   665: aload 48
    //   667: ldc_w 364
    //   670: aload 47
    //   672: ldc_w 702
    //   675: aload 49
    //   677: invokevirtual 372	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   680: pop
    //   681: iconst_3
    //   682: anewarray 4	java/lang/Object
    //   685: astore 51
    //   687: aload 51
    //   689: iconst_0
    //   690: lload_1
    //   691: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   694: aastore
    //   695: aload 51
    //   697: iconst_1
    //   698: lload 36
    //   700: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   703: aastore
    //   704: aload 51
    //   706: iconst_2
    //   707: aload 20
    //   709: aastore
    //   710: ldc 248
    //   712: ldc_w 740
    //   715: aload 51
    //   717: invokestatic 519	com/google/android/gm/provider/LogUtils:d	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   720: pop
    //   721: goto -237 -> 484
    //   724: astore 10
    //   726: aload 9
    //   728: invokeinterface 576 1 0
    //   733: aload 10
    //   735: athrow
    //   736: astore 38
    //   738: iconst_2
    //   739: anewarray 4	java/lang/Object
    //   742: astore 39
    //   744: aload 39
    //   746: iconst_0
    //   747: lload_1
    //   748: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   751: aastore
    //   752: aload 39
    //   754: iconst_1
    //   755: aload 20
    //   757: aastore
    //   758: ldc 248
    //   760: aload 38
    //   762: ldc_w 742
    //   765: aload 39
    //   767: invokestatic 256	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   770: pop
    //   771: new 347	android/content/ContentValues
    //   774: dup
    //   775: invokespecial 348	android/content/ContentValues:<init>	()V
    //   778: astore 41
    //   780: aload 41
    //   782: ldc 43
    //   784: sipush 404
    //   787: invokestatic 353	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   790: invokevirtual 357	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   793: aload_0
    //   794: getfield 94	com/google/android/gm/provider/AttachmentManager:mDb	Landroid/database/sqlite/SQLiteDatabase;
    //   797: astore 42
    //   799: iconst_2
    //   800: anewarray 39	java/lang/String
    //   803: astore 43
    //   805: aload 43
    //   807: iconst_0
    //   808: lload_1
    //   809: invokestatic 362	java/lang/Long:toString	(J)Ljava/lang/String;
    //   812: aastore
    //   813: aload 43
    //   815: iconst_1
    //   816: lload_3
    //   817: invokestatic 362	java/lang/Long:toString	(J)Ljava/lang/String;
    //   820: aastore
    //   821: aload 42
    //   823: ldc_w 364
    //   826: aload 41
    //   828: ldc_w 702
    //   831: aload 43
    //   833: invokevirtual 372	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   836: pop
    //   837: iconst_0
    //   838: istore 45
    //   840: goto -261 -> 579
    //   843: astore 23
    //   845: aconst_null
    //   846: astore 24
    //   848: goto -546 -> 302
    //
    // Exception table:
    //   from	to	target	type
    //   115	150	724	finally
    //   158	222	724	finally
    //   225	259	724	finally
    //   262	287	724	finally
    //   287	298	724	finally
    //   307	338	724	finally
    //   358	395	724	finally
    //   400	484	724	finally
    //   484	490	724	finally
    //   498	529	724	finally
    //   537	544	724	finally
    //   549	556	724	finally
    //   561	572	724	finally
    //   584	721	724	finally
    //   738	837	724	finally
    //   561	572	736	java/lang/IllegalArgumentException
    //   287	298	843	java/lang/Exception
  }

  private void updateAttachmentEntry(long paramLong, int paramInt, String paramString)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("downloadId", Integer.valueOf(-1));
    if (paramString != null)
      localContentValues.put("filename", paramString);
    localContentValues.put("status", Integer.valueOf(paramInt));
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong);
    localSQLiteDatabase.update("attachments", localContentValues, "_id = ?", arrayOfString);
  }

  public int cancelDownloadRequest(long paramLong1, long paramLong2, String paramString, Gmail.AttachmentRendition paramAttachmentRendition, boolean paramBoolean)
  {
    if (LogUtils.isLoggable("Gmail", 3))
      LogUtils.d("Gmail", "cancelDownloadRequest: %s", new Object[] { requestDescription(paramLong1, paramLong2, paramString, paramAttachmentRendition, paramBoolean) });
    Cursor localCursor = newAttachmentCursor(paramLong2, paramString, paramAttachmentRendition, paramBoolean, new String[] { "_id", "downloadId", "automatic", "status", "filename" });
    try
    {
      if (localCursor.moveToNext())
      {
        long l1 = localCursor.getLong(0);
        long l2 = localCursor.getLong(1);
        if (localCursor.getInt(2) != 0);
        while (true)
        {
          localCursor.getInt(3);
          String str = localCursor.getString(4);
          if ((this.mDownloadManager != null) && (isDownloadIdValid(l2)))
            this.mDownloadManager.remove(new long[] { l2 });
          deleteAttachment(paramLong1, l1, str);
          return 1;
        }
      }
      return 0;
    }
    finally
    {
      localCursor.close();
    }
  }

  public void enqueueAttachment(long paramLong1, long paramLong2, Gmail.Attachment paramAttachment, Gmail.AttachmentRendition paramAttachmentRendition, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    long l = recordAttachmentInDb(paramLong1, paramLong2, paramAttachment, paramAttachmentRendition, 0L, paramBoolean1, paramBoolean2, paramInt);
    if (LogUtils.isLoggable("Gmail", 3))
    {
      String str = requestDescription(paramLong1, paramLong2, paramAttachment.partId, paramAttachmentRendition, paramBoolean1);
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Long.valueOf(l);
      arrayOfObject[1] = str;
      LogUtils.d("Gmail", "enqueueAttachment attachmentId: %d: %s", arrayOfObject);
    }
    notifyChanged(paramLong1);
  }

  public void handleDownloadManagerIntent(Intent paramIntent)
  {
    long l = paramIntent.getLongExtra("extra_download_id", -1L);
    if (l == -1L)
    {
      LogUtils.e("Gmail", "Received notification from DownloadManager with invalid download id", new Object[0]);
      return;
    }
    DownloadManager.Query localQuery = new DownloadManager.Query().setFilterById(new long[] { l });
    Cursor localCursor = this.mDownloadManager.query(localQuery);
    if (localCursor == null)
    {
      LogUtils.e("Gmail", "null cursor from DownloadManager", new Object[0]);
      return;
    }
    try
    {
      int i = localCursor.getColumnIndex("status");
      int j = localCursor.getColumnIndex("reason");
      int m;
      int n;
      if (localCursor.moveToNext())
      {
        int k = localCursor.getInt(i);
        m = localCursor.getInt(j);
        n = 0;
        switch (k)
        {
        default:
        case 8:
        case 16:
        }
      }
      while (true)
      {
        ArrayList localArrayList = paramIntent.getStringArrayListExtra(ATTACHMENT_FROM);
        onDownloadCompletedByDownloadManager(l, n, (String)localArrayList.get(0), (String)localArrayList.get(1));
        return;
        n = 200;
        continue;
        n = m;
      }
    }
    finally
    {
      localCursor.close();
    }
  }

  // ERROR //
  public void maybeStartNextAttachmentDownload()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 793	com/google/android/gm/provider/AttachmentManager:isLowSpace	()Z
    //   4: ifeq +17 -> 21
    //   7: ldc 248
    //   9: ldc_w 795
    //   12: iconst_0
    //   13: anewarray 4	java/lang/Object
    //   16: invokestatic 798	com/google/android/gm/provider/LogUtils:i	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   19: pop
    //   20: return
    //   21: aload_0
    //   22: getfield 94	com/google/android/gm/provider/AttachmentManager:mDb	Landroid/database/sqlite/SQLiteDatabase;
    //   25: astore_1
    //   26: iconst_1
    //   27: anewarray 39	java/lang/String
    //   30: dup
    //   31: iconst_0
    //   32: ldc_w 359
    //   35: aastore
    //   36: astore_2
    //   37: iconst_1
    //   38: anewarray 39	java/lang/String
    //   41: astore_3
    //   42: aload_3
    //   43: iconst_0
    //   44: sipush 192
    //   47: invokestatic 552	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   50: aastore
    //   51: aload_1
    //   52: ldc_w 364
    //   55: aload_2
    //   56: ldc_w 584
    //   59: aload_3
    //   60: aconst_null
    //   61: aconst_null
    //   62: aconst_null
    //   63: invokevirtual 483	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   66: astore 4
    //   68: aload 4
    //   70: invokeinterface 801 1 0
    //   75: istore 6
    //   77: iconst_0
    //   78: istore 7
    //   80: iload 6
    //   82: ifle +254 -> 336
    //   85: invokestatic 807	com/google/common/collect/Sets:newHashSet	()Ljava/util/HashSet;
    //   88: astore 8
    //   90: aload 4
    //   92: invokeinterface 559 1 0
    //   97: ifeq +37 -> 134
    //   100: aload 8
    //   102: aload 4
    //   104: iconst_0
    //   105: invokeinterface 563 2 0
    //   110: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   113: invokeinterface 810 2 0
    //   118: pop
    //   119: goto -29 -> 90
    //   122: astore 5
    //   124: aload 4
    //   126: invokeinterface 576 1 0
    //   131: aload 5
    //   133: athrow
    //   134: new 586	android/app/DownloadManager$Query
    //   137: dup
    //   138: invokespecial 587	android/app/DownloadManager$Query:<init>	()V
    //   141: iconst_2
    //   142: invokevirtual 814	android/app/DownloadManager$Query:setFilterByStatus	(I)Landroid/app/DownloadManager$Query;
    //   145: astore 9
    //   147: aload_0
    //   148: getfield 116	com/google/android/gm/provider/AttachmentManager:mDownloadManager	Landroid/app/DownloadManager;
    //   151: aload 9
    //   153: invokevirtual 594	android/app/DownloadManager:query	(Landroid/app/DownloadManager$Query;)Landroid/database/Cursor;
    //   156: astore 10
    //   158: aload 10
    //   160: ifnonnull +24 -> 184
    //   163: ldc 248
    //   165: ldc_w 779
    //   168: iconst_0
    //   169: anewarray 4	java/lang/Object
    //   172: invokestatic 291	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   175: pop
    //   176: aload 4
    //   178: invokeinterface 576 1 0
    //   183: return
    //   184: aload 10
    //   186: ldc_w 550
    //   189: invokeinterface 598 2 0
    //   194: istore 13
    //   196: aload 10
    //   198: invokeinterface 559 1 0
    //   203: ifeq +70 -> 273
    //   206: aload 10
    //   208: iload 13
    //   210: invokeinterface 563 2 0
    //   215: lstore 26
    //   217: aload 8
    //   219: lload 26
    //   221: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   224: invokeinterface 817 2 0
    //   229: ifeq -33 -> 196
    //   232: iconst_1
    //   233: anewarray 4	java/lang/Object
    //   236: astore 28
    //   238: aload 28
    //   240: iconst_0
    //   241: lload 26
    //   243: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   246: aastore
    //   247: ldc 248
    //   249: ldc_w 819
    //   252: aload 28
    //   254: invokestatic 519	com/google/android/gm/provider/LogUtils:d	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   257: pop
    //   258: aload 10
    //   260: invokeinterface 576 1 0
    //   265: aload 4
    //   267: invokeinterface 576 1 0
    //   272: return
    //   273: aload 10
    //   275: invokeinterface 576 1 0
    //   280: new 586	android/app/DownloadManager$Query
    //   283: dup
    //   284: invokespecial 587	android/app/DownloadManager$Query:<init>	()V
    //   287: iconst_4
    //   288: invokevirtual 814	android/app/DownloadManager$Query:setFilterByStatus	(I)Landroid/app/DownloadManager$Query;
    //   291: astore 14
    //   293: aload_0
    //   294: getfield 116	com/google/android/gm/provider/AttachmentManager:mDownloadManager	Landroid/app/DownloadManager;
    //   297: aload 14
    //   299: invokevirtual 594	android/app/DownloadManager:query	(Landroid/app/DownloadManager$Query;)Landroid/database/Cursor;
    //   302: astore 15
    //   304: iconst_0
    //   305: istore 7
    //   307: aload 15
    //   309: ifnull +27 -> 336
    //   312: aload 15
    //   314: invokeinterface 801 1 0
    //   319: istore 25
    //   321: iload 25
    //   323: ifle +228 -> 551
    //   326: iconst_1
    //   327: istore 7
    //   329: aload 15
    //   331: invokeinterface 576 1 0
    //   336: aload 4
    //   338: invokeinterface 576 1 0
    //   343: aload_0
    //   344: getfield 94	com/google/android/gm/provider/AttachmentManager:mDb	Landroid/database/sqlite/SQLiteDatabase;
    //   347: ldc_w 364
    //   350: iconst_2
    //   351: anewarray 39	java/lang/String
    //   354: dup
    //   355: iconst_0
    //   356: ldc_w 550
    //   359: aastore
    //   360: dup
    //   361: iconst_1
    //   362: ldc_w 648
    //   365: aastore
    //   366: ldc_w 821
    //   369: iconst_1
    //   370: anewarray 39	java/lang/String
    //   373: dup
    //   374: iconst_0
    //   375: ldc_w 823
    //   378: aastore
    //   379: aconst_null
    //   380: aconst_null
    //   381: ldc_w 825
    //   384: invokevirtual 483	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   387: astore 16
    //   389: aload 16
    //   391: invokeinterface 559 1 0
    //   396: ifeq +126 -> 522
    //   399: aload 16
    //   401: iconst_0
    //   402: invokeinterface 563 2 0
    //   407: lstore 19
    //   409: aload 16
    //   411: iconst_1
    //   412: invokeinterface 566 2 0
    //   417: invokestatic 655	com/google/android/gm/provider/Gmail$AttachmentRendition:valueOf	(Ljava/lang/String;)Lcom/google/android/gm/provider/Gmail$AttachmentRendition;
    //   420: getstatic 660	com/google/android/gm/provider/Gmail$AttachmentRendition:SIMPLE	Lcom/google/android/gm/provider/Gmail$AttachmentRendition;
    //   423: if_acmpeq +52 -> 475
    //   426: iconst_1
    //   427: istore 21
    //   429: goto +109 -> 538
    //   432: aload_0
    //   433: lload 19
    //   435: ldc2_w 449
    //   438: iload 21
    //   440: invokespecial 827	com/google/android/gm/provider/AttachmentManager:startAttachmentDownloadInDownloadManager	(JJZ)V
    //   443: aload 16
    //   445: invokeinterface 576 1 0
    //   450: return
    //   451: astore 12
    //   453: aload 10
    //   455: invokeinterface 576 1 0
    //   460: aload 12
    //   462: athrow
    //   463: astore 24
    //   465: aload 15
    //   467: invokeinterface 576 1 0
    //   472: aload 24
    //   474: athrow
    //   475: iconst_0
    //   476: istore 21
    //   478: goto +60 -> 538
    //   481: iconst_1
    //   482: anewarray 4	java/lang/Object
    //   485: astore 22
    //   487: aload 22
    //   489: iconst_0
    //   490: lload 19
    //   492: invokestatic 286	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   495: aastore
    //   496: ldc 248
    //   498: ldc_w 829
    //   501: aload 22
    //   503: invokestatic 519	com/google/android/gm/provider/LogUtils:d	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   506: pop
    //   507: goto -64 -> 443
    //   510: astore 17
    //   512: aload 16
    //   514: invokeinterface 576 1 0
    //   519: aload 17
    //   521: athrow
    //   522: ldc 248
    //   524: ldc_w 831
    //   527: iconst_0
    //   528: anewarray 4	java/lang/Object
    //   531: invokestatic 519	com/google/android/gm/provider/LogUtils:d	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   534: pop
    //   535: goto -92 -> 443
    //   538: iload 21
    //   540: ifeq -108 -> 432
    //   543: iload 7
    //   545: ifne -64 -> 481
    //   548: goto -116 -> 432
    //   551: iconst_0
    //   552: istore 7
    //   554: goto -225 -> 329
    //
    // Exception table:
    //   from	to	target	type
    //   68	77	122	finally
    //   85	90	122	finally
    //   90	119	122	finally
    //   134	158	122	finally
    //   163	176	122	finally
    //   258	265	122	finally
    //   273	304	122	finally
    //   329	336	122	finally
    //   453	463	122	finally
    //   465	475	122	finally
    //   184	196	451	finally
    //   196	258	451	finally
    //   312	321	463	finally
    //   389	426	510	finally
    //   432	443	510	finally
    //   481	507	510	finally
    //   522	535	510	finally
  }

  void onDownloadCompletedByDownloadManager(long paramLong, int paramInt, String paramString1, String paramString2)
  {
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString1 = ATTACHMENT_PROJECTION;
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = Long.toString(paramLong);
    Cursor localCursor = localSQLiteDatabase.query("attachments, messages", arrayOfString1, "downloadId = ? AND attachments.messages_messageId = messages.messageId", arrayOfString2, null, null, "saveToSd DESC");
    while (true)
    {
      long l1;
      long l2;
      long l3;
      String str1;
      Gmail.AttachmentRendition localAttachmentRendition;
      try
      {
        if (localCursor.getCount() == 0)
        {
          Object[] arrayOfObject3 = new Object[1];
          arrayOfObject3[0] = Long.valueOf(paramLong);
          LogUtils.e("Gmail", "No attachments found with downloadId %d", arrayOfObject3);
        }
        if (!localCursor.moveToNext())
          break;
        l1 = localCursor.getLong(0);
        l2 = localCursor.getLong(1);
        l3 = localCursor.getLong(2);
        str1 = localCursor.getString(3);
        localAttachmentRendition = Gmail.AttachmentRendition.valueOf(localCursor.getString(4));
        if (localCursor.getInt(5) != 0)
        {
          bool = true;
          String str2 = requestDescription(l2, l3, str1, localAttachmentRendition, bool);
          Object[] arrayOfObject1 = new Object[3];
          arrayOfObject1[0] = Integer.valueOf(paramInt);
          arrayOfObject1[1] = Long.valueOf(paramLong);
          arrayOfObject1[2] = str2;
          LogUtils.d("Gmail", "Download finished with status %d for download %d. %s", arrayOfObject1);
          if (!localCursor.isFirst())
            break label372;
          String str3 = localCursor.getString(6);
          String str4 = localCursor.getString(7);
          String str5 = localCursor.getString(8);
          if (!isStatusSuccess(paramInt))
            break label318;
          copyAttachment(l1, l2, l3, str1, localAttachmentRendition, bool, str3, paramLong, null, str4, str5, paramString1, paramString2);
          notifyChanged(l2);
          continue;
        }
      }
      finally
      {
        localCursor.close();
      }
      boolean bool = false;
      continue;
      label318: Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Long.valueOf(paramLong);
      arrayOfObject2[1] = Integer.valueOf(paramInt);
      LogUtils.e("Gmail", "Download id %d failed with status %d", arrayOfObject2);
      onAttachmentDownloadFinished(l1, l2, l3, str1, localAttachmentRendition, bool, paramInt, null);
      continue;
      label372: updateAttachmentEntry(l1, paramInt, null);
    }
    localCursor.close();
    this.mRestrictedMailEngine.enqueueAttachmentDownloadTask();
  }

  public ParcelFileDescriptor openAttachment(long paramLong1, long paramLong2, Gmail.Attachment paramAttachment, Gmail.AttachmentRendition paramAttachmentRendition, boolean paramBoolean, String paramString)
    throws FileNotFoundException
  {
    String str1 = requestDescription(paramLong1, paramLong2, paramAttachment.partId, paramAttachmentRendition, paramBoolean);
    LogUtils.d("Gmail", "AttachmentManager.openAttachment: %s", new Object[] { str1 });
    Cursor localCursor = null;
    try
    {
      localCursor = newAttachmentCursor(paramLong2, paramAttachment.partId, paramAttachmentRendition, paramBoolean, ATTACHMENT_NAME_STATUS_PROJECTION);
      if (!localCursor.moveToNext())
        break label191;
      if (!isStatusSuccess(localCursor.getInt(1)))
        throw new FileNotFoundException("Download not complete or not successful.");
    }
    finally
    {
      if (localCursor != null)
        localCursor.close();
    }
    String str2 = getPathFromUri(localCursor.getString(0));
    long l = Binder.clearCallingIdentity();
    try
    {
      LogUtils.d("Gmail", "Opening attachment %s", new Object[] { str2 });
      ParcelFileDescriptor localParcelFileDescriptor = ParcelFileDescriptor.open(new File(str2), 268435456);
      Binder.restoreCallingIdentity(l);
      if (localCursor != null)
        localCursor.close();
      return localParcelFileDescriptor;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
    label191: LogUtils.e("Gmail", "Attachment is not requested %s", new Object[] { str1 });
    throw new FileNotFoundException("Attachment not requested.");
  }

  void purgeOldAttachments()
  {
    LogUtils.d("Gmail", "Purging old attachments.", new Object[0]);
    if (this.mDb.isDbLockedByCurrentThread())
      throw new IllegalStateException("Db should not be locked");
    purgeInvalidAttachments();
    purgeInvalidDownloadingAttachments();
  }

  public Cursor queryAndStartDownloadingAttachment(long paramLong1, long paramLong2, Gmail.Attachment paramAttachment, Gmail.AttachmentRendition paramAttachmentRendition, boolean paramBoolean, String[] paramArrayOfString)
  {
    if (LogUtils.isLoggable("Gmail", 3))
      LogUtils.d("Gmail", "queryAndStartDownloadingAttachment for %s", new Object[] { requestDescription(paramLong1, paramLong2, paramAttachment.partId, paramAttachmentRendition, paramBoolean) });
    String[] arrayOfString1 = { "_id", "downloadId", "status", "filename", "saveToSd" };
    SQLiteDatabase localSQLiteDatabase1 = this.mDb;
    String[] arrayOfString2 = new String[3];
    arrayOfString2[0] = Long.toString(paramLong2);
    arrayOfString2[1] = paramAttachment.partId;
    arrayOfString2[2] = paramAttachmentRendition.toString();
    Cursor localCursor = localSQLiteDatabase1.query("attachments", arrayOfString1, "messages_messageId = ? AND messages_partId = ? AND desiredRendition = ? ", arrayOfString2, null, null, null);
    Long[] arrayOfLong = new Long[2];
    arrayOfLong[0] = Long.valueOf(-1L);
    arrayOfLong[1] = Long.valueOf(-1L);
    long[] arrayOfLong1 = { -1L, -1L };
    int[] arrayOfInt = { -1, -1 };
    String[] arrayOfString3 = { null, null };
    try
    {
      if (localCursor.moveToNext())
      {
        int n = localCursor.getInt(4);
        arrayOfLong[n] = Long.valueOf(localCursor.getLong(0));
        arrayOfLong1[n] = localCursor.getLong(1);
        arrayOfInt[n] = localCursor.getInt(2);
        arrayOfString3[n] = localCursor.getString(3);
      }
    }
    finally
    {
      localCursor.close();
    }
    for (int i = 0; i < 2; i++)
      if ((!isStatusSuccess(arrayOfInt[i])) || (!isDownloadStillPresent(arrayOfString3[i])))
        arrayOfString3[i] = null;
    int j;
    int k;
    if (paramBoolean)
    {
      j = 1;
      k = 0;
      label351: if (k > j)
        break label424;
      if (arrayOfLong[k].longValue() == -1L)
        if (k == 0)
          break label418;
    }
    label418: for (boolean bool3 = true; ; bool3 = false)
    {
      arrayOfLong[k] = Long.valueOf(recordAttachmentInDb(paramLong1, paramLong2, paramAttachment, paramAttachmentRendition, -1L, bool3, false, 0));
      k++;
      break label351;
      j = 0;
      break;
    }
    label424: boolean bool2;
    if ((!isStatusValid(arrayOfInt[0])) && (!isStatusValid(arrayOfInt[1])))
    {
      LogUtils.d("Gmail", "AttachmentManager.queryAndStartDownloadingAttachment() starting new download", new Object[0]);
      long l3 = arrayOfLong[0].longValue();
      long l4 = arrayOfLong[1].longValue();
      if (!canDownloadAttachment(this.mContext, paramAttachment))
      {
        bool2 = true;
        startAttachmentDownloadInDownloadManager(l3, l4, bool2);
      }
    }
    while (true)
    {
      notifyChanged(paramLong1);
      return newAttachmentCursor(paramLong2, paramAttachment.partId, paramAttachmentRendition, paramBoolean, paramArrayOfString);
      bool2 = false;
      break;
      if ((arrayOfString3[0] == null) && (arrayOfString3[1] == null))
        break label612;
      LogUtils.d("Gmail", "AttachmentManager.queryAndStartDownloadingAttachment() file exists either on cache or sd card, will copy if needed", new Object[0]);
      if (arrayOfString3[j] == null)
        copyAttachment(arrayOfLong[j].longValue(), paramLong1, paramLong2, paramAttachment.partId, paramAttachmentRendition, paramBoolean, paramAttachment.name, -1L, arrayOfString3[(1 - j)], paramAttachment.contentType, null, null, null);
    }
    label612: LogUtils.d("Gmail", "AttachmentManager.queryAndStartDownloadingAttachment() refetch attachment", new Object[0]);
    for (int m = 0; m <= j; m++)
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("filename", paramAttachment.name);
      localContentValues.put("automatic", Integer.valueOf(0));
      SQLiteDatabase localSQLiteDatabase2 = this.mDb;
      String[] arrayOfString4 = new String[1];
      arrayOfString4[0] = Long.toString(arrayOfLong[m].longValue());
      localSQLiteDatabase2.update("attachments", localContentValues, "_id = ?", arrayOfString4);
    }
    long l1 = arrayOfLong[0].longValue();
    long l2 = arrayOfLong[1].longValue();
    if (!canDownloadAttachment(this.mContext, paramAttachment));
    for (boolean bool1 = true; ; bool1 = false)
    {
      startAttachmentDownloadInDownloadManager(l1, l2, bool1);
      break;
    }
  }

  public Cursor queryForConversation(long paramLong, String[] paramArrayOfString)
  {
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString = new String[2];
    arrayOfString[0] = Long.toString(paramLong);
    arrayOfString[1] = Gmail.AttachmentRendition.BEST.toString();
    return localSQLiteDatabase.query("attachments", paramArrayOfString, "messages_conversation = ? AND desiredRendition = ?", arrayOfString, null, null, null);
  }

  long recordAttachmentInDb(long paramLong1, long paramLong2, Gmail.Attachment paramAttachment, Gmail.AttachmentRendition paramAttachmentRendition, long paramLong3, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("messages_conversation", Long.valueOf(paramLong1));
    localContentValues.put("messages_messageId", Long.valueOf(paramLong2));
    localContentValues.put("messages_partId", paramAttachment.partId);
    localContentValues.put("originExtras", paramAttachment.originExtras);
    localContentValues.put("desiredRendition", paramAttachmentRendition.toString());
    localContentValues.put("downloadedRendition", paramAttachmentRendition.toString());
    localContentValues.put("downloadId", Long.valueOf(paramLong3));
    int i;
    if (paramBoolean2)
    {
      i = 1;
      localContentValues.put("automatic", Integer.valueOf(i));
      localContentValues.put("priority", Integer.valueOf(paramInt));
      if (!paramBoolean1)
        break label206;
    }
    label206: for (int j = 1; ; j = 0)
    {
      localContentValues.put("saveToSd", Integer.valueOf(j));
      localContentValues.put("filename", paramAttachment.name);
      localContentValues.put("status", Integer.valueOf(190));
      localContentValues.put("mimeType", paramAttachment.contentType);
      return this.mDb.insertWithOnConflict("attachments", null, localContentValues, 4);
      i = 0;
      break;
    }
  }

  public int resetAttachmentRequest(long paramLong1, long paramLong2, String paramString, Gmail.AttachmentRendition paramAttachmentRendition, boolean paramBoolean)
  {
    if (LogUtils.isLoggable("Gmail", 3))
      LogUtils.d("Gmail", "resetAttachmentRequest: %s", new Object[] { requestDescription(paramLong1, paramLong2, paramString, paramAttachmentRendition, paramBoolean) });
    Cursor localCursor = newAttachmentCursor(paramLong2, paramString, paramAttachmentRendition, paramBoolean, new String[] { "_id", "downloadId", "filename" });
    try
    {
      if (localCursor.moveToNext())
      {
        long l1 = localCursor.getLong(0);
        long l2 = localCursor.getLong(1);
        String str = localCursor.getString(2);
        if ((this.mDownloadManager != null) && (isDownloadIdValid(l2)))
          this.mDownloadManager.remove(new long[] { l2 });
        resetAttachment(paramLong1, l1, str);
        return 1;
      }
      return 0;
    }
    finally
    {
      localCursor.close();
    }
  }

  public void updateMessageId(long paramLong1, long paramLong2)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("messages_messageId", Long.valueOf(paramLong2));
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong1);
    this.mDb.update("attachments", localContentValues, "messages_messageId = ?", arrayOfString);
  }

  public static abstract interface RestrictedMailEngine
  {
    public abstract void enqueueAttachmentDownloadTask();

    public abstract String getAuthToken()
      throws Exception;

    public abstract int getRequestVersion();

    public abstract void postBackgroundTask(Runnable paramRunnable);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.AttachmentManager
 * JD-Core Version:    0.6.2
 */