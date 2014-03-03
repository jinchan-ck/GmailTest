package com.android.mail.ui;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import com.android.mail.providers.Attachment;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import java.io.IOException;

public class ThumbnailLoadTask extends AsyncTask<Uri, Void, Bitmap>
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private final int mHeight;
  private final AttachmentBitmapHolder mHolder;
  private final int mWidth;

  public ThumbnailLoadTask(AttachmentBitmapHolder paramAttachmentBitmapHolder, int paramInt1, int paramInt2)
  {
    this.mHolder = paramAttachmentBitmapHolder;
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
  }

  // ERROR //
  private int getOrientation(Uri paramUri)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +5 -> 6
    //   4: iconst_0
    //   5: ireturn
    //   6: aconst_null
    //   7: astore_2
    //   8: aconst_null
    //   9: astore_3
    //   10: aload_0
    //   11: getfield 28	com/android/mail/ui/ThumbnailLoadTask:mHolder	Lcom/android/mail/ui/AttachmentBitmapHolder;
    //   14: invokeinterface 44 1 0
    //   19: aload_1
    //   20: invokevirtual 50	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   23: astore_3
    //   24: new 52	java/io/ByteArrayOutputStream
    //   27: dup
    //   28: invokespecial 53	java/io/ByteArrayOutputStream:<init>	()V
    //   31: astore 15
    //   33: sipush 4096
    //   36: newarray byte
    //   38: astore 17
    //   40: aload_3
    //   41: aload 17
    //   43: invokevirtual 59	java/io/InputStream:read	([B)I
    //   46: istore 18
    //   48: iload 18
    //   50: iflt +24 -> 74
    //   53: aload 15
    //   55: aload 17
    //   57: iconst_0
    //   58: iload 18
    //   60: invokevirtual 63	java/io/ByteArrayOutputStream:write	([BII)V
    //   63: aload_3
    //   64: aload 17
    //   66: invokevirtual 59	java/io/InputStream:read	([B)I
    //   69: istore 18
    //   71: goto -23 -> 48
    //   74: aload_3
    //   75: invokevirtual 66	java/io/InputStream:close	()V
    //   78: aconst_null
    //   79: astore_3
    //   80: aload_0
    //   81: invokevirtual 70	com/android/mail/ui/ThumbnailLoadTask:isCancelled	()Z
    //   84: istore 19
    //   86: iload 19
    //   88: ifeq +62 -> 150
    //   91: iconst_0
    //   92: ifeq +7 -> 99
    //   95: aconst_null
    //   96: invokevirtual 66	java/io/InputStream:close	()V
    //   99: aload 15
    //   101: ifnull -97 -> 4
    //   104: aload 15
    //   106: invokevirtual 71	java/io/ByteArrayOutputStream:close	()V
    //   109: iconst_0
    //   110: ireturn
    //   111: astore 25
    //   113: getstatic 22	com/android/mail/ui/ThumbnailLoadTask:LOG_TAG	Ljava/lang/String;
    //   116: aload 25
    //   118: ldc 73
    //   120: iconst_0
    //   121: anewarray 75	java/lang/Object
    //   124: invokestatic 81	com/android/mail/utils/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   127: pop
    //   128: iconst_0
    //   129: ireturn
    //   130: astore 27
    //   132: getstatic 22	com/android/mail/ui/ThumbnailLoadTask:LOG_TAG	Ljava/lang/String;
    //   135: aload 27
    //   137: ldc 83
    //   139: iconst_0
    //   140: anewarray 75	java/lang/Object
    //   143: invokestatic 81	com/android/mail/utils/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   146: pop
    //   147: goto -48 -> 99
    //   150: aload 15
    //   152: invokevirtual 87	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   155: invokestatic 91	com/android/ex/photo/util/Exif:getOrientation	([B)I
    //   158: istore 20
    //   160: iconst_0
    //   161: ifeq +7 -> 168
    //   164: aconst_null
    //   165: invokevirtual 66	java/io/InputStream:close	()V
    //   168: aload 15
    //   170: ifnull +8 -> 178
    //   173: aload 15
    //   175: invokevirtual 71	java/io/ByteArrayOutputStream:close	()V
    //   178: iload 20
    //   180: ireturn
    //   181: astore 23
    //   183: getstatic 22	com/android/mail/ui/ThumbnailLoadTask:LOG_TAG	Ljava/lang/String;
    //   186: aload 23
    //   188: ldc 83
    //   190: iconst_0
    //   191: anewarray 75	java/lang/Object
    //   194: invokestatic 81	com/android/mail/utils/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   197: pop
    //   198: goto -30 -> 168
    //   201: astore 21
    //   203: getstatic 22	com/android/mail/ui/ThumbnailLoadTask:LOG_TAG	Ljava/lang/String;
    //   206: aload 21
    //   208: ldc 73
    //   210: iconst_0
    //   211: anewarray 75	java/lang/Object
    //   214: invokestatic 81	com/android/mail/utils/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   217: pop
    //   218: goto -40 -> 178
    //   221: astore 9
    //   223: getstatic 22	com/android/mail/ui/ThumbnailLoadTask:LOG_TAG	Ljava/lang/String;
    //   226: ldc 93
    //   228: iconst_1
    //   229: anewarray 75	java/lang/Object
    //   232: dup
    //   233: iconst_0
    //   234: aload_1
    //   235: aastore
    //   236: invokestatic 96	com/android/mail/utils/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   239: pop
    //   240: aload_3
    //   241: ifnull +7 -> 248
    //   244: aload_3
    //   245: invokevirtual 66	java/io/InputStream:close	()V
    //   248: aload_2
    //   249: ifnull -245 -> 4
    //   252: aload_2
    //   253: invokevirtual 71	java/io/ByteArrayOutputStream:close	()V
    //   256: iconst_0
    //   257: ireturn
    //   258: astore 11
    //   260: getstatic 22	com/android/mail/ui/ThumbnailLoadTask:LOG_TAG	Ljava/lang/String;
    //   263: aload 11
    //   265: ldc 73
    //   267: iconst_0
    //   268: anewarray 75	java/lang/Object
    //   271: invokestatic 81	com/android/mail/utils/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   274: pop
    //   275: iconst_0
    //   276: ireturn
    //   277: astore 13
    //   279: getstatic 22	com/android/mail/ui/ThumbnailLoadTask:LOG_TAG	Ljava/lang/String;
    //   282: aload 13
    //   284: ldc 83
    //   286: iconst_0
    //   287: anewarray 75	java/lang/Object
    //   290: invokestatic 81	com/android/mail/utils/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   293: pop
    //   294: goto -46 -> 248
    //   297: astore 4
    //   299: aload_3
    //   300: ifnull +7 -> 307
    //   303: aload_3
    //   304: invokevirtual 66	java/io/InputStream:close	()V
    //   307: aload_2
    //   308: ifnull +7 -> 315
    //   311: aload_2
    //   312: invokevirtual 71	java/io/ByteArrayOutputStream:close	()V
    //   315: aload 4
    //   317: athrow
    //   318: astore 7
    //   320: getstatic 22	com/android/mail/ui/ThumbnailLoadTask:LOG_TAG	Ljava/lang/String;
    //   323: aload 7
    //   325: ldc 83
    //   327: iconst_0
    //   328: anewarray 75	java/lang/Object
    //   331: invokestatic 81	com/android/mail/utils/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   334: pop
    //   335: goto -28 -> 307
    //   338: astore 5
    //   340: getstatic 22	com/android/mail/ui/ThumbnailLoadTask:LOG_TAG	Ljava/lang/String;
    //   343: aload 5
    //   345: ldc 73
    //   347: iconst_0
    //   348: anewarray 75	java/lang/Object
    //   351: invokestatic 81	com/android/mail/utils/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   354: pop
    //   355: goto -40 -> 315
    //   358: astore 4
    //   360: aload 15
    //   362: astore_2
    //   363: goto -64 -> 299
    //   366: astore 16
    //   368: aload 15
    //   370: astore_2
    //   371: goto -148 -> 223
    //
    // Exception table:
    //   from	to	target	type
    //   104	109	111	java/io/IOException
    //   95	99	130	java/io/IOException
    //   164	168	181	java/io/IOException
    //   173	178	201	java/io/IOException
    //   10	33	221	java/lang/Throwable
    //   252	256	258	java/io/IOException
    //   244	248	277	java/io/IOException
    //   10	33	297	finally
    //   223	240	297	finally
    //   303	307	318	java/io/IOException
    //   311	315	338	java/io/IOException
    //   33	48	358	finally
    //   53	71	358	finally
    //   74	78	358	finally
    //   80	86	358	finally
    //   150	160	358	finally
    //   33	48	366	java/lang/Throwable
    //   53	71	366	java/lang/Throwable
    //   74	78	366	java/lang/Throwable
    //   80	86	366	java/lang/Throwable
    //   150	160	366	java/lang/Throwable
  }

  private Bitmap loadBitmap(Uri paramUri)
  {
    Object localObject2;
    if (paramUri == null)
    {
      LogUtils.e(LOG_TAG, "Attempting to load bitmap for null uri", new Object[0]);
      localObject2 = null;
    }
    AssetFileDescriptor localAssetFileDescriptor;
    while (true)
    {
      return localObject2;
      int i = getOrientation(paramUri);
      localAssetFileDescriptor = null;
      try
      {
        localAssetFileDescriptor = this.mHolder.getResolver().openAssetFileDescriptor(paramUri, "r");
        boolean bool = isCancelled();
        if ((bool) || (localAssetFileDescriptor == null))
        {
          localObject2 = null;
          if (localAssetFileDescriptor != null)
            try
            {
              localAssetFileDescriptor.close();
              return null;
            }
            catch (IOException localIOException3)
            {
              LogUtils.e(LOG_TAG, localIOException3, "", new Object[0]);
              return null;
            }
        }
        else
        {
          BitmapFactory.Options localOptions = new BitmapFactory.Options();
          localOptions.inJustDecodeBounds = true;
          localOptions.inDensity = 120;
          BitmapFactory.decodeFileDescriptor(localAssetFileDescriptor.getFileDescriptor(), null, localOptions);
          if ((!isCancelled()) && (localOptions.outWidth != -1))
          {
            int j = localOptions.outHeight;
            if (j != -1);
          }
          else
          {
            localObject2 = null;
            if (localAssetFileDescriptor == null)
              continue;
            try
            {
              localAssetFileDescriptor.close();
              return null;
            }
            catch (IOException localIOException4)
            {
              LogUtils.e(LOG_TAG, localIOException4, "", new Object[0]);
              return null;
            }
          }
          localOptions.inJustDecodeBounds = false;
          localOptions.inSampleSize = Math.min(Math.max(localOptions.outWidth / this.mWidth, 1), Math.max(localOptions.outHeight / this.mHeight, 1));
          String str = LOG_TAG;
          Object[] arrayOfObject = new Object[5];
          arrayOfObject[0] = Integer.valueOf(localOptions.outWidth);
          arrayOfObject[1] = Integer.valueOf(localOptions.outHeight);
          arrayOfObject[2] = Integer.valueOf(this.mWidth);
          arrayOfObject[3] = Integer.valueOf(this.mHeight);
          arrayOfObject[4] = Integer.valueOf(localOptions.inSampleSize);
          LogUtils.d(str, "in background, src w/h=%d/%d dst w/h=%d/%d, divider=%d", arrayOfObject);
          localObject2 = BitmapFactory.decodeFileDescriptor(localAssetFileDescriptor.getFileDescriptor(), null, localOptions);
          if ((localObject2 != null) && (i != 0))
          {
            Matrix localMatrix = new Matrix();
            localMatrix.postRotate(i);
            Bitmap localBitmap = Bitmap.createBitmap((Bitmap)localObject2, 0, 0, ((Bitmap)localObject2).getWidth(), ((Bitmap)localObject2).getHeight(), localMatrix, true);
            localObject2 = localBitmap;
            if (localAssetFileDescriptor != null)
              try
              {
                localAssetFileDescriptor.close();
                return localObject2;
              }
              catch (IOException localIOException6)
              {
                LogUtils.e(LOG_TAG, localIOException6, "", new Object[0]);
                return localObject2;
              }
          }
          else if (localAssetFileDescriptor != null)
          {
            try
            {
              localAssetFileDescriptor.close();
              return localObject2;
            }
            catch (IOException localIOException5)
            {
              LogUtils.e(LOG_TAG, localIOException5, "", new Object[0]);
              return localObject2;
            }
          }
        }
      }
      catch (Throwable localThrowable)
      {
        LogUtils.e(LOG_TAG, "Unable to decode thumbnail %s", new Object[] { paramUri });
        if (localAssetFileDescriptor != null);
        try
        {
          localAssetFileDescriptor.close();
          return null;
        }
        catch (IOException localIOException2)
        {
          while (true)
            LogUtils.e(LOG_TAG, localIOException2, "", new Object[0]);
        }
      }
      finally
      {
        if (localAssetFileDescriptor == null);
      }
    }
    try
    {
      localAssetFileDescriptor.close();
      throw localObject1;
    }
    catch (IOException localIOException1)
    {
      while (true)
        LogUtils.e(LOG_TAG, localIOException1, "", new Object[0]);
    }
  }

  public static void setupThumbnailPreview(ThumbnailLoadTask paramThumbnailLoadTask, AttachmentBitmapHolder paramAttachmentBitmapHolder, Attachment paramAttachment1, Attachment paramAttachment2)
  {
    int i = paramAttachmentBitmapHolder.getThumbnailWidth();
    int j = paramAttachmentBitmapHolder.getThumbnailHeight();
    if ((paramAttachment1 == null) || (i == 0) || (j == 0) || (!paramAttachment1.isImage()))
      paramAttachmentBitmapHolder.setThumbnailToDefault();
    Uri localUri1;
    Uri localUri2;
    label154: label163: 
    do
    {
      return;
      localUri1 = paramAttachment1.thumbnailUri;
      localUri2 = paramAttachment1.contentUri;
      Uri localUri3;
      Object localObject;
      if (paramAttachment2 == null)
      {
        localUri3 = null;
        localObject = null;
        if (paramAttachment2 != null)
          break label154;
      }
      while (true)
      {
        if (((localUri1 == null) && (localUri2 == null)) || ((!paramAttachmentBitmapHolder.bitmapSetToDefault()) && (localObject != null) && (localUri3.equals(localObject))))
          break label163;
        if (paramThumbnailLoadTask != null)
          paramThumbnailLoadTask.cancel(true);
        new ThumbnailLoadTask(paramAttachmentBitmapHolder, i, j).execute(new Uri[] { localUri1, localUri2 });
        return;
        localUri3 = paramAttachment2.uri;
        break;
        localObject = paramAttachment2.uri;
      }
    }
    while ((localUri1 != null) || (localUri2 != null));
    paramAttachmentBitmapHolder.setThumbnailToDefault();
  }

  protected Bitmap doInBackground(Uri[] paramArrayOfUri)
  {
    Bitmap localBitmap = loadBitmap(paramArrayOfUri[0]);
    if (localBitmap == null)
      localBitmap = loadBitmap(paramArrayOfUri[1]);
    return localBitmap;
  }

  protected void onPostExecute(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
    {
      LogUtils.d(LOG_TAG, "back in UI thread, decode failed", new Object[0]);
      this.mHolder.setThumbnailToDefault();
      return;
    }
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(paramBitmap.getWidth());
    arrayOfObject[1] = Integer.valueOf(paramBitmap.getHeight());
    LogUtils.d(str, "back in UI thread, decode success, w/h=%d/%d", arrayOfObject);
    this.mHolder.setThumbnail(paramBitmap);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ThumbnailLoadTask
 * JD-Core Version:    0.6.2
 */