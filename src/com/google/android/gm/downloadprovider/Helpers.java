package com.google.android.gm.downloadprovider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers
{
  private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern.compile("attachment;\\s*filename\\s*=\\s*\"([^\"]*)\"");
  public static Random sRandom = new Random(SystemClock.uptimeMillis());

  private static String chooseExtensionFromFilename(String paramString1, int paramInt1, String paramString2, int paramInt2)
  {
    String str1 = null;
    if (paramString1 != null)
    {
      int i = paramString2.lastIndexOf('.');
      String str2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(paramString2.substring(i + 1));
      if (str2 != null)
      {
        boolean bool = str2.equalsIgnoreCase(paramString1);
        str1 = null;
        if (bool);
      }
      else
      {
        str1 = chooseExtensionFromMimeType(paramString1, false);
        if (str1 == null)
          break label107;
        if (Constants.LOGVV)
          Log.v("DownloadManager", "substituting extension from type");
      }
    }
    while (true)
    {
      if (str1 == null)
      {
        if (Constants.LOGVV)
          Log.v("DownloadManager", "keeping extension");
        str1 = paramString2.substring(paramInt2);
      }
      return str1;
      label107: if (Constants.LOGVV)
        Log.v("DownloadManager", "couldn't find extension for " + paramString1);
    }
  }

  private static String chooseExtensionFromMimeType(String paramString, boolean paramBoolean)
  {
    String str = null;
    if (paramString != null)
    {
      str = MimeTypeMap.getSingleton().getExtensionFromMimeType(paramString);
      if (str != null)
      {
        if (Constants.LOGVV)
          Log.v("DownloadManager", "adding extension from type");
        str = "." + str;
      }
    }
    else if (str == null)
    {
      if ((paramString == null) || (!paramString.toLowerCase().startsWith("text/")))
        break label155;
      if (!paramString.equalsIgnoreCase("text/html"))
        break label134;
      if (Constants.LOGVV)
        Log.v("DownloadManager", "adding default html extension");
      str = ".html";
    }
    label134: label155: 
    while (!paramBoolean)
    {
      do
      {
        return str;
        if (!Constants.LOGVV)
          break;
        Log.v("DownloadManager", "couldn't find extension for " + paramString);
        break;
      }
      while (!paramBoolean);
      if (Constants.LOGVV)
        Log.v("DownloadManager", "adding default text extension");
      return ".txt";
    }
    if (Constants.LOGVV)
      Log.v("DownloadManager", "adding default binary extension");
    return ".bin";
  }

  private static String chooseFilename(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
  {
    Object localObject = null;
    if (paramString2 != null)
    {
      boolean bool = paramString2.endsWith("/");
      localObject = null;
      if (!bool)
      {
        if (Constants.LOGVV)
          Log.v("DownloadManager", "getting filename from hint");
        int m = 1 + paramString2.lastIndexOf('/');
        if (m <= 0)
          break label306;
        localObject = paramString2.substring(m);
      }
    }
    if ((localObject == null) && (paramString3 != null))
    {
      localObject = parseContentDisposition(paramString3);
      if (localObject != null)
      {
        if (Constants.LOGVV)
          Log.v("DownloadManager", "getting filename from content-disposition");
        int k = 1 + ((String)localObject).lastIndexOf('/');
        if (k > 0)
          localObject = ((String)localObject).substring(k);
      }
    }
    String str2;
    int j;
    if ((localObject == null) && (paramString4 != null))
    {
      str2 = Uri.decode(paramString4);
      if ((str2 != null) && (!str2.endsWith("/")) && (str2.indexOf('?') < 0))
      {
        if (Constants.LOGVV)
          Log.v("DownloadManager", "getting filename from content-location");
        j = 1 + str2.lastIndexOf('/');
        if (j <= 0)
          break label312;
      }
    }
    label306: label312: for (localObject = str2.substring(j); ; localObject = str2)
    {
      if (localObject == null)
      {
        String str1 = Uri.decode(paramString1);
        if ((str1 != null) && (!str1.endsWith("/")) && (str1.indexOf('?') < 0))
        {
          int i = 1 + str1.lastIndexOf('/');
          if (i > 0)
          {
            if (Constants.LOGVV)
              Log.v("DownloadManager", "getting filename from uri");
            localObject = str1.substring(i);
          }
        }
      }
      if (localObject == null)
      {
        if (Constants.LOGVV)
          Log.v("DownloadManager", "using default filename");
        localObject = "downloadfile";
      }
      return ((String)localObject).replaceAll("[^a-zA-Z0-9\\.\\-_]+", "_");
      localObject = paramString2;
      break;
    }
  }

  private static String chooseUniqueFilename(int paramInt, String paramString1, String paramString2, boolean paramBoolean)
  {
    String str1 = paramString1 + paramString2;
    if ((!new File(str1).exists()) && ((!paramBoolean) || ((paramInt != 1) && (paramInt != 2) && (paramInt != 3))))
      return str1;
    String str2 = paramString1 + "-";
    int i = 1;
    int j = 1;
    while (j < 1000000000)
    {
      for (int k = 0; k < 9; k++)
      {
        String str3 = str2 + i + paramString2;
        if (!new File(str3).exists())
          return str3;
        if (Constants.LOGVV)
          Log.v("DownloadManager", "file with sequence number " + i + " exists");
        i += 1 + sRandom.nextInt(j);
      }
      j *= 10;
    }
    return null;
  }

  // ERROR //
  public static final boolean discardPurgeableFiles(Context paramContext, long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 196	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   4: getstatic 202	com/google/android/gm/downloadprovider/Downloads$Impl:CONTENT_URI	Landroid/net/Uri;
    //   7: aconst_null
    //   8: ldc 204
    //   10: aconst_null
    //   11: ldc 206
    //   13: invokevirtual 212	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   16: astore_3
    //   17: aload_3
    //   18: ifnonnull +5 -> 23
    //   21: iconst_0
    //   22: ireturn
    //   23: aload_3
    //   24: invokeinterface 217 1 0
    //   29: pop
    //   30: lconst_0
    //   31: lstore 7
    //   33: aload_3
    //   34: invokeinterface 220 1 0
    //   39: ifne +169 -> 208
    //   42: lload 7
    //   44: lload_1
    //   45: lcmp
    //   46: ifge +162 -> 208
    //   49: new 168	java/io/File
    //   52: dup
    //   53: aload_3
    //   54: aload_3
    //   55: ldc 222
    //   57: invokeinterface 226 2 0
    //   62: invokeinterface 229 2 0
    //   67: invokespecial 171	java/io/File:<init>	(Ljava/lang/String;)V
    //   70: astore 12
    //   72: getstatic 72	com/google/android/gm/downloadprovider/Constants:LOGVV	Z
    //   75: ifeq +50 -> 125
    //   78: ldc 74
    //   80: new 86	java/lang/StringBuilder
    //   83: dup
    //   84: invokespecial 87	java/lang/StringBuilder:<init>	()V
    //   87: ldc 231
    //   89: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: aload 12
    //   94: invokevirtual 234	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   97: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: ldc 236
    //   102: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: aload 12
    //   107: invokevirtual 239	java/io/File:length	()J
    //   110: invokevirtual 242	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   113: ldc 244
    //   115: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: invokevirtual 97	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   121: invokestatic 82	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   124: pop
    //   125: aload 12
    //   127: invokevirtual 239	java/io/File:length	()J
    //   130: lstore 13
    //   132: lload 7
    //   134: lload 13
    //   136: ladd
    //   137: lstore 15
    //   139: aload 12
    //   141: invokevirtual 247	java/io/File:delete	()Z
    //   144: pop
    //   145: aload_3
    //   146: aload_3
    //   147: ldc 249
    //   149: invokeinterface 226 2 0
    //   154: invokeinterface 253 2 0
    //   159: lstore 19
    //   161: aload_0
    //   162: invokevirtual 196	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   165: getstatic 202	com/google/android/gm/downloadprovider/Downloads$Impl:CONTENT_URI	Landroid/net/Uri;
    //   168: lload 19
    //   170: invokestatic 259	android/content/ContentUris:withAppendedId	(Landroid/net/Uri;J)Landroid/net/Uri;
    //   173: aconst_null
    //   174: aconst_null
    //   175: invokevirtual 262	android/content/ContentResolver:delete	(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I
    //   178: pop
    //   179: aload_3
    //   180: invokeinterface 265 1 0
    //   185: pop
    //   186: lload 15
    //   188: lstore 7
    //   190: goto -157 -> 33
    //   193: astore 4
    //   195: aload 4
    //   197: astore 5
    //   199: aload_3
    //   200: invokeinterface 268 1 0
    //   205: aload 5
    //   207: athrow
    //   208: aload_3
    //   209: invokeinterface 268 1 0
    //   214: getstatic 271	com/google/android/gm/downloadprovider/Constants:LOGV	Z
    //   217: ifeq +52 -> 269
    //   220: lload 7
    //   222: lconst_0
    //   223: lcmp
    //   224: ifle +45 -> 269
    //   227: ldc 74
    //   229: new 86	java/lang/StringBuilder
    //   232: dup
    //   233: invokespecial 87	java/lang/StringBuilder:<init>	()V
    //   236: ldc_w 273
    //   239: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: lload 7
    //   244: invokevirtual 242	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   247: ldc 236
    //   249: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: lload_1
    //   253: invokevirtual 242	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   256: ldc_w 275
    //   259: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: invokevirtual 97	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   265: invokestatic 82	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   268: pop
    //   269: lload 7
    //   271: lconst_0
    //   272: lcmp
    //   273: ifle +5 -> 278
    //   276: iconst_1
    //   277: ireturn
    //   278: iconst_0
    //   279: ireturn
    //   280: astore 9
    //   282: aload 9
    //   284: astore 5
    //   286: lload 7
    //   288: pop2
    //   289: goto -90 -> 199
    //   292: astore 17
    //   294: aload 17
    //   296: astore 5
    //   298: goto -99 -> 199
    //
    // Exception table:
    //   from	to	target	type
    //   23	30	193	finally
    //   33	42	280	finally
    //   49	125	280	finally
    //   125	132	280	finally
    //   139	186	292	finally
  }

  public static DownloadFileInfo generateSaveFile(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, int paramInt1, int paramInt2)
    throws FileNotFoundException
  {
    if ((paramInt1 == 0) || (paramInt1 == 2))
    {
      if (paramString5 == null)
      {
        Log.d("DownloadManager", "external download with no mime type not allowed");
        return new DownloadFileInfo(null, null, 406);
      }
      Intent localIntent = new Intent("android.intent.action.VIEW");
      PackageManager localPackageManager = paramContext.getPackageManager();
      localIntent.setDataAndType(Uri.fromParts("file", "", null), paramString5);
      if (localPackageManager.resolveActivity(localIntent, 65536) == null)
      {
        Log.d("DownloadManager", "no handler found for type " + paramString5);
        return new DownloadFileInfo(null, null, 406);
      }
    }
    String str1 = chooseFilename(paramString1, paramString2, paramString3, paramString4, paramInt1);
    int i = str1.indexOf('.');
    Object localObject1;
    Object localObject2;
    File localFile1;
    StatFs localStatFs1;
    int j;
    label365: File localFile2;
    if (i < 0)
    {
      localObject1 = chooseExtensionFromMimeType(paramString5, true);
      localObject2 = str1;
      if ((paramInt1 == 1) || (paramInt1 == 2) || (paramInt1 == 3))
      {
        localFile1 = new File(paramContext.getCacheDir() + "/download");
        localFile1.mkdir();
        localStatFs1 = new StatFs(localFile1.getPath());
        j = localStatFs1.getBlockSize();
      }
    }
    else
    {
      for (long l = j * (localStatFs1.getAvailableBlocks() - 4L); ; l = j * (localStatFs1.getAvailableBlocks() - 4L))
      {
        if (l >= paramInt2)
          break label365;
        if (!discardPurgeableFiles(paramContext, paramInt2 - l))
        {
          Log.d("DownloadManager", "download aborted - not enough free space in internal storage");
          return new DownloadFileInfo(null, null, 498);
          String str2 = chooseExtensionFromFilename(paramString5, paramInt1, str1, i);
          String str3 = str1.substring(0, i);
          localObject1 = str2;
          localObject2 = str3;
          break;
        }
        localStatFs1.restat(localFile1.getPath());
      }
      localFile2 = localFile1;
    }
    while (true)
    {
      boolean bool = "recovery".equalsIgnoreCase((String)localObject2 + (String)localObject1);
      String str4 = localFile2.getPath() + File.separator + (String)localObject2;
      if (Constants.LOGVV)
        Log.v("DownloadManager", "target file: " + str4 + (String)localObject1);
      String str5 = chooseUniqueFilename(paramInt1, str4, (String)localObject1, bool);
      if (str5 == null)
        break label695;
      return new DownloadFileInfo(str5, new FileOutputStream(str5), 0);
      if (!Environment.getExternalStorageState().equals("mounted"))
        break;
      String str6 = Environment.getExternalStorageDirectory().getPath();
      StatFs localStatFs2 = new StatFs(str6);
      if (localStatFs2.getBlockSize() * (localStatFs2.getAvailableBlocks() - 4L) < paramInt2)
      {
        Log.d("DownloadManager", "download aborted - not enough free space");
        return new DownloadFileInfo(null, null, 498);
      }
      localFile2 = new File(str6 + "/download");
      if ((!localFile2.isDirectory()) && (!localFile2.mkdir()))
      {
        Log.d("DownloadManager", "download aborted - can't create base directory " + localFile2.getPath());
        return new DownloadFileInfo(null, null, 492);
      }
    }
    Log.d("DownloadManager", "download aborted - no external storage");
    return new DownloadFileInfo(null, null, 499);
    label695: return new DownloadFileInfo(null, null, 492);
  }

  public static boolean isFilenameValid(Context paramContext, String paramString)
  {
    File localFile = new File(paramString).getParentFile();
    return (localFile.equals(new File(paramContext.getCacheDir() + "/download"))) || (localFile.equals(new File(Environment.getExternalStorageDirectory() + "/download")));
  }

  public static boolean isNetworkAvailable(Context paramContext)
  {
    ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
    if (localConnectivityManager == null)
      Log.w("DownloadManager", "couldn't get connectivity manager");
    while (true)
    {
      if (Constants.LOGVV)
        Log.v("DownloadManager", "network is not available");
      return false;
      NetworkInfo[] arrayOfNetworkInfo = localConnectivityManager.getAllNetworkInfo();
      if (arrayOfNetworkInfo != null)
        for (int i = 0; i < arrayOfNetworkInfo.length; i++)
          if (arrayOfNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED)
          {
            if (Constants.LOGVV)
              Log.v("DownloadManager", "network is available");
            return true;
          }
    }
  }

  public static boolean isNetworkRoaming(Context paramContext)
  {
    ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
    if (localConnectivityManager == null)
      Log.w("DownloadManager", "couldn't get connectivity manager");
    while (true)
    {
      return false;
      NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
      if ((localNetworkInfo != null) && (localNetworkInfo.getType() == 0))
      {
        if (((TelephonyManager)paramContext.getSystemService("phone")).isNetworkRoaming())
        {
          if (Constants.LOGVV)
            Log.v("DownloadManager", "network is roaming");
          return true;
        }
        if (Constants.LOGVV)
          Log.v("DownloadManager", "network is not roaming");
      }
      else if (Constants.LOGVV)
      {
        Log.v("DownloadManager", "not using mobile network");
      }
    }
  }

  private static String parseContentDisposition(String paramString)
  {
    try
    {
      Matcher localMatcher = CONTENT_DISPOSITION_PATTERN.matcher(paramString);
      if (localMatcher.find())
      {
        String str = localMatcher.group(1);
        return str;
      }
    }
    catch (IllegalStateException localIllegalStateException)
    {
    }
    return null;
  }

  private static void parseExpression(Lexer paramLexer)
  {
    while (true)
    {
      if (paramLexer.currentToken() == 1)
      {
        paramLexer.advance();
        parseExpression(paramLexer);
        if (paramLexer.currentToken() != 2)
          throw new IllegalArgumentException("syntax error, unmatched parenthese");
        paramLexer.advance();
      }
      while (paramLexer.currentToken() != 3)
      {
        return;
        parseStatement(paramLexer);
      }
      paramLexer.advance();
    }
  }

  private static void parseStatement(Lexer paramLexer)
  {
    if (paramLexer.currentToken() != 4)
      throw new IllegalArgumentException("syntax error, expected column name");
    paramLexer.advance();
    if (paramLexer.currentToken() == 5)
    {
      paramLexer.advance();
      if (paramLexer.currentToken() != 6)
        throw new IllegalArgumentException("syntax error, expected quoted string");
      paramLexer.advance();
      return;
    }
    if (paramLexer.currentToken() == 7)
    {
      paramLexer.advance();
      if (paramLexer.currentToken() != 8)
        throw new IllegalArgumentException("syntax error, expected NULL");
      paramLexer.advance();
      return;
    }
    throw new IllegalArgumentException("syntax error after column name");
  }

  public static void validateSelection(String paramString, Set<String> paramSet)
  {
    if (paramString == null);
    while (true)
    {
      return;
      try
      {
        Lexer localLexer = new Lexer(paramString, paramSet);
        parseExpression(localLexer);
        if (localLexer.currentToken() != 9)
          throw new IllegalArgumentException("syntax error");
      }
      catch (RuntimeException localRuntimeException)
      {
        if (!Constants.LOGV)
          break label84;
      }
    }
    Log.d("DownloadManager", "invalid selection [" + paramString + "] triggered " + localRuntimeException);
    while (true)
    {
      throw localRuntimeException;
      label84: Log.d("DownloadManager", "invalid selection triggered " + localRuntimeException);
    }
  }

  private static class Lexer
  {
    public static final int TOKEN_AND_OR = 3;
    public static final int TOKEN_CLOSE_PAREN = 2;
    public static final int TOKEN_COLUMN = 4;
    public static final int TOKEN_COMPARE = 5;
    public static final int TOKEN_END = 9;
    public static final int TOKEN_IS = 7;
    public static final int TOKEN_NULL = 8;
    public static final int TOKEN_OPEN_PAREN = 1;
    public static final int TOKEN_START = 0;
    public static final int TOKEN_VALUE = 6;
    private final Set<String> mAllowedColumns;
    private final char[] mChars;
    private int mCurrentToken = 0;
    private int mOffset = 0;
    private final String mSelection;

    public Lexer(String paramString, Set<String> paramSet)
    {
      this.mSelection = paramString;
      this.mAllowedColumns = paramSet;
      this.mChars = new char[this.mSelection.length()];
      this.mSelection.getChars(0, this.mChars.length, this.mChars, 0);
      advance();
    }

    private static final boolean isIdentifierChar(char paramChar)
    {
      return (paramChar == '_') || ((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= '0') && (paramChar <= '9'));
    }

    private static final boolean isIdentifierStart(char paramChar)
    {
      return (paramChar == '_') || ((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= 'a') && (paramChar <= 'z'));
    }

    public void advance()
    {
      char[] arrayOfChar = this.mChars;
      while ((this.mOffset < arrayOfChar.length) && (arrayOfChar[this.mOffset] == ' '))
        this.mOffset = (1 + this.mOffset);
      if (this.mOffset == arrayOfChar.length)
        this.mCurrentToken = 9;
      do
      {
        do
        {
          do
          {
            return;
            if (arrayOfChar[this.mOffset] == '(')
            {
              this.mOffset = (1 + this.mOffset);
              this.mCurrentToken = 1;
              return;
            }
            if (arrayOfChar[this.mOffset] == ')')
            {
              this.mOffset = (1 + this.mOffset);
              this.mCurrentToken = 2;
              return;
            }
            if (arrayOfChar[this.mOffset] == '?')
            {
              this.mOffset = (1 + this.mOffset);
              this.mCurrentToken = 6;
              return;
            }
            if (arrayOfChar[this.mOffset] != '=')
              break;
            this.mOffset = (1 + this.mOffset);
            this.mCurrentToken = 5;
          }
          while ((this.mOffset >= arrayOfChar.length) || (arrayOfChar[this.mOffset] != '='));
          this.mOffset = (1 + this.mOffset);
          return;
          if (arrayOfChar[this.mOffset] != '>')
            break;
          this.mOffset = (1 + this.mOffset);
          this.mCurrentToken = 5;
        }
        while ((this.mOffset >= arrayOfChar.length) || (arrayOfChar[this.mOffset] != '='));
        this.mOffset = (1 + this.mOffset);
        return;
        if (arrayOfChar[this.mOffset] != '<')
          break;
        this.mOffset = (1 + this.mOffset);
        this.mCurrentToken = 5;
      }
      while ((this.mOffset >= arrayOfChar.length) || ((arrayOfChar[this.mOffset] != '=') && (arrayOfChar[this.mOffset] != '>')));
      this.mOffset = (1 + this.mOffset);
      return;
      if (arrayOfChar[this.mOffset] == '!')
      {
        this.mOffset = (1 + this.mOffset);
        this.mCurrentToken = 5;
        if ((this.mOffset < arrayOfChar.length) && (arrayOfChar[this.mOffset] == '='))
        {
          this.mOffset = (1 + this.mOffset);
          return;
        }
        throw new IllegalArgumentException("Unexpected character after !");
      }
      if (isIdentifierStart(arrayOfChar[this.mOffset]))
      {
        int i = this.mOffset;
        for (this.mOffset = (1 + this.mOffset); (this.mOffset < arrayOfChar.length) && (isIdentifierChar(arrayOfChar[this.mOffset])); this.mOffset = (1 + this.mOffset));
        String str = this.mSelection.substring(i, this.mOffset);
        if (this.mOffset - i <= 4)
        {
          if (str.equals("IS"))
          {
            this.mCurrentToken = 7;
            return;
          }
          if ((str.equals("OR")) || (str.equals("AND")))
          {
            this.mCurrentToken = 3;
            return;
          }
          if (str.equals("NULL"))
          {
            this.mCurrentToken = 8;
            return;
          }
        }
        if (this.mAllowedColumns.contains(str))
        {
          this.mCurrentToken = 4;
          return;
        }
        throw new IllegalArgumentException("unrecognized column or keyword");
      }
      if (arrayOfChar[this.mOffset] == '\'')
      {
        for (this.mOffset = (1 + this.mOffset); this.mOffset < arrayOfChar.length; this.mOffset = (1 + this.mOffset))
          if (arrayOfChar[this.mOffset] == '\'')
          {
            if ((1 + this.mOffset >= arrayOfChar.length) || (arrayOfChar[(1 + this.mOffset)] != '\''))
              break;
            this.mOffset = (1 + this.mOffset);
          }
        if (this.mOffset == arrayOfChar.length)
          throw new IllegalArgumentException("unterminated string");
        this.mOffset = (1 + this.mOffset);
        this.mCurrentToken = 6;
        return;
      }
      throw new IllegalArgumentException("illegal character");
    }

    public int currentToken()
    {
      return this.mCurrentToken;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.downloadprovider.Helpers
 * JD-Core Version:    0.6.2
 */