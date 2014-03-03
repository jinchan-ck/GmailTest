package com.android.ex.photo.util;

import android.os.Build.VERSION;
import com.android.ex.photo.PhotoViewActivity;

public class ImageUtils
{
  public static final ImageSize sUseImageSize = ImageSize.EXTRA_SMALL;

  static
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      sUseImageSize = ImageSize.NORMAL;
      return;
    }
    if (PhotoViewActivity.sMemoryClass >= 32L)
    {
      sUseImageSize = ImageSize.NORMAL;
      return;
    }
    if (PhotoViewActivity.sMemoryClass >= 24L)
    {
      sUseImageSize = ImageSize.SMALL;
      return;
    }
  }

  // ERROR //
  public static android.graphics.Bitmap createLocalBitmap(android.content.ContentResolver paramContentResolver, android.net.Uri paramUri, int paramInt)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: new 49	android/graphics/BitmapFactory$Options
    //   5: dup
    //   6: invokespecial 50	android/graphics/BitmapFactory$Options:<init>	()V
    //   9: astore 4
    //   11: aload_0
    //   12: aload_1
    //   13: invokestatic 54	com/android/ex/photo/util/ImageUtils:getImageBounds	(Landroid/content/ContentResolver;Landroid/net/Uri;)Landroid/graphics/Point;
    //   16: astore 14
    //   18: aload_0
    //   19: aload_1
    //   20: invokevirtual 60	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   23: astore_3
    //   24: aload 4
    //   26: aload 14
    //   28: getfield 65	android/graphics/Point:x	I
    //   31: iload_2
    //   32: idiv
    //   33: aload 14
    //   35: getfield 68	android/graphics/Point:y	I
    //   38: iload_2
    //   39: idiv
    //   40: invokestatic 74	java/lang/Math:max	(II)I
    //   43: putfield 77	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   46: aload_3
    //   47: aconst_null
    //   48: aload 4
    //   50: invokestatic 81	com/android/ex/photo/util/ImageUtils:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   53: astore 15
    //   55: aload 15
    //   57: astore 8
    //   59: aload_3
    //   60: ifnull +7 -> 67
    //   63: aload_3
    //   64: invokevirtual 86	java/io/InputStream:close	()V
    //   67: aload 8
    //   69: areturn
    //   70: astore 12
    //   72: aconst_null
    //   73: astore 8
    //   75: aload_3
    //   76: ifnull -9 -> 67
    //   79: aload_3
    //   80: invokevirtual 86	java/io/InputStream:close	()V
    //   83: aconst_null
    //   84: areturn
    //   85: astore 13
    //   87: aconst_null
    //   88: areturn
    //   89: astore 10
    //   91: aconst_null
    //   92: astore 8
    //   94: aload_3
    //   95: ifnull -28 -> 67
    //   98: aload_3
    //   99: invokevirtual 86	java/io/InputStream:close	()V
    //   102: aconst_null
    //   103: areturn
    //   104: astore 11
    //   106: aconst_null
    //   107: areturn
    //   108: astore 7
    //   110: aconst_null
    //   111: astore 8
    //   113: aload_3
    //   114: ifnull -47 -> 67
    //   117: aload_3
    //   118: invokevirtual 86	java/io/InputStream:close	()V
    //   121: aconst_null
    //   122: areturn
    //   123: astore 9
    //   125: aconst_null
    //   126: areturn
    //   127: astore 5
    //   129: aload_3
    //   130: ifnull +7 -> 137
    //   133: aload_3
    //   134: invokevirtual 86	java/io/InputStream:close	()V
    //   137: aload 5
    //   139: athrow
    //   140: astore 16
    //   142: aload 8
    //   144: areturn
    //   145: astore 6
    //   147: goto -10 -> 137
    //
    // Exception table:
    //   from	to	target	type
    //   2	55	70	java/io/FileNotFoundException
    //   79	83	85	java/io/IOException
    //   2	55	89	java/io/IOException
    //   98	102	104	java/io/IOException
    //   2	55	108	java/lang/IllegalArgumentException
    //   117	121	123	java/io/IOException
    //   2	55	127	finally
    //   63	67	140	java/io/IOException
    //   133	137	145	java/io/IOException
  }

  // ERROR //
  public static android.graphics.Bitmap decodeStream(java.io.InputStream paramInputStream, android.graphics.Rect paramRect, android.graphics.BitmapFactory.Options paramOptions)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: new 90	java/io/ByteArrayOutputStream
    //   5: dup
    //   6: invokespecial 91	java/io/ByteArrayOutputStream:<init>	()V
    //   9: astore 4
    //   11: sipush 4096
    //   14: newarray byte
    //   16: astore 14
    //   18: aload_0
    //   19: aload 14
    //   21: invokevirtual 95	java/io/InputStream:read	([B)I
    //   24: istore 15
    //   26: iload 15
    //   28: iflt +24 -> 52
    //   31: aload 4
    //   33: aload 14
    //   35: iconst_0
    //   36: iload 15
    //   38: invokevirtual 99	java/io/ByteArrayOutputStream:write	([BII)V
    //   41: aload_0
    //   42: aload 14
    //   44: invokevirtual 95	java/io/InputStream:read	([B)I
    //   47: istore 15
    //   49: goto -23 -> 26
    //   52: aload 4
    //   54: invokevirtual 103	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   57: astore 16
    //   59: aload 16
    //   61: invokestatic 108	com/android/ex/photo/util/Exif:getOrientation	([B)I
    //   64: istore 17
    //   66: aload 16
    //   68: iconst_0
    //   69: aload 16
    //   71: arraylength
    //   72: aload_2
    //   73: invokestatic 114	android/graphics/BitmapFactory:decodeByteArray	([BIILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   76: astore 18
    //   78: aload 18
    //   80: ifnull +65 -> 145
    //   83: iload 17
    //   85: ifeq +60 -> 145
    //   88: new 116	android/graphics/Matrix
    //   91: dup
    //   92: invokespecial 117	android/graphics/Matrix:<init>	()V
    //   95: astore 20
    //   97: aload 20
    //   99: iload 17
    //   101: i2f
    //   102: invokevirtual 121	android/graphics/Matrix:postRotate	(F)Z
    //   105: pop
    //   106: aload 18
    //   108: iconst_0
    //   109: iconst_0
    //   110: aload 18
    //   112: invokevirtual 127	android/graphics/Bitmap:getWidth	()I
    //   115: aload 18
    //   117: invokevirtual 130	android/graphics/Bitmap:getHeight	()I
    //   120: aload 20
    //   122: iconst_1
    //   123: invokestatic 134	android/graphics/Bitmap:createBitmap	(Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
    //   126: astore 22
    //   128: aload 22
    //   130: astore 9
    //   132: aload 4
    //   134: ifnull +8 -> 142
    //   137: aload 4
    //   139: invokevirtual 135	java/io/ByteArrayOutputStream:close	()V
    //   142: aload 9
    //   144: areturn
    //   145: aload 4
    //   147: ifnull +8 -> 155
    //   150: aload 4
    //   152: invokevirtual 135	java/io/ByteArrayOutputStream:close	()V
    //   155: aload 18
    //   157: areturn
    //   158: astore 5
    //   160: ldc 137
    //   162: ldc 139
    //   164: aload 5
    //   166: invokestatic 145	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   169: pop
    //   170: aconst_null
    //   171: astore 9
    //   173: aload_3
    //   174: ifnull -32 -> 142
    //   177: aload_3
    //   178: invokevirtual 135	java/io/ByteArrayOutputStream:close	()V
    //   181: aconst_null
    //   182: areturn
    //   183: astore 10
    //   185: aconst_null
    //   186: areturn
    //   187: astore 11
    //   189: ldc 137
    //   191: ldc 147
    //   193: aload 11
    //   195: invokestatic 145	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   198: pop
    //   199: aconst_null
    //   200: astore 9
    //   202: aload_3
    //   203: ifnull -61 -> 142
    //   206: aload_3
    //   207: invokevirtual 135	java/io/ByteArrayOutputStream:close	()V
    //   210: aconst_null
    //   211: areturn
    //   212: astore 13
    //   214: aconst_null
    //   215: areturn
    //   216: astore 6
    //   218: aload_3
    //   219: ifnull +7 -> 226
    //   222: aload_3
    //   223: invokevirtual 135	java/io/ByteArrayOutputStream:close	()V
    //   226: aload 6
    //   228: athrow
    //   229: astore 23
    //   231: goto -89 -> 142
    //   234: astore 19
    //   236: goto -81 -> 155
    //   239: astore 7
    //   241: goto -15 -> 226
    //   244: astore 6
    //   246: aload 4
    //   248: astore_3
    //   249: goto -31 -> 218
    //   252: astore 11
    //   254: aload 4
    //   256: astore_3
    //   257: goto -68 -> 189
    //   260: astore 5
    //   262: aload 4
    //   264: astore_3
    //   265: goto -105 -> 160
    //
    // Exception table:
    //   from	to	target	type
    //   2	11	158	java/lang/OutOfMemoryError
    //   177	181	183	java/io/IOException
    //   2	11	187	java/io/IOException
    //   206	210	212	java/io/IOException
    //   2	11	216	finally
    //   160	170	216	finally
    //   189	199	216	finally
    //   137	142	229	java/io/IOException
    //   150	155	234	java/io/IOException
    //   222	226	239	java/io/IOException
    //   11	26	244	finally
    //   31	49	244	finally
    //   52	78	244	finally
    //   88	128	244	finally
    //   11	26	252	java/io/IOException
    //   31	49	252	java/io/IOException
    //   52	78	252	java/io/IOException
    //   88	128	252	java/io/IOException
    //   11	26	260	java/lang/OutOfMemoryError
    //   31	49	260	java/lang/OutOfMemoryError
    //   52	78	260	java/lang/OutOfMemoryError
    //   88	128	260	java/lang/OutOfMemoryError
  }

  // ERROR //
  private static android.graphics.Point getImageBounds(android.content.ContentResolver paramContentResolver, android.net.Uri paramUri)
    throws java.io.IOException
  {
    // Byte code:
    //   0: new 49	android/graphics/BitmapFactory$Options
    //   3: dup
    //   4: invokespecial 50	android/graphics/BitmapFactory$Options:<init>	()V
    //   7: astore_2
    //   8: aconst_null
    //   9: astore_3
    //   10: aload_2
    //   11: iconst_1
    //   12: putfield 151	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   15: aload_0
    //   16: aload_1
    //   17: invokevirtual 60	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   20: astore_3
    //   21: aload_3
    //   22: aconst_null
    //   23: aload_2
    //   24: invokestatic 81	com/android/ex/photo/util/ImageUtils:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   27: pop
    //   28: new 62	android/graphics/Point
    //   31: dup
    //   32: aload_2
    //   33: getfield 154	android/graphics/BitmapFactory$Options:outWidth	I
    //   36: aload_2
    //   37: getfield 157	android/graphics/BitmapFactory$Options:outHeight	I
    //   40: invokespecial 160	android/graphics/Point:<init>	(II)V
    //   43: astore 7
    //   45: aload_3
    //   46: ifnull +7 -> 53
    //   49: aload_3
    //   50: invokevirtual 86	java/io/InputStream:close	()V
    //   53: aload 7
    //   55: areturn
    //   56: astore 4
    //   58: aload_3
    //   59: ifnull +7 -> 66
    //   62: aload_3
    //   63: invokevirtual 86	java/io/InputStream:close	()V
    //   66: aload 4
    //   68: athrow
    //   69: astore 8
    //   71: aload 7
    //   73: areturn
    //   74: astore 5
    //   76: goto -10 -> 66
    //
    // Exception table:
    //   from	to	target	type
    //   10	45	56	finally
    //   49	53	69	java/io/IOException
    //   62	66	74	java/io/IOException
  }

  public static boolean isImageMimeType(String paramString)
  {
    return (paramString != null) && (paramString.startsWith("image/"));
  }

  public static enum ImageSize
  {
    static
    {
      NORMAL = new ImageSize("NORMAL", 2);
      ImageSize[] arrayOfImageSize = new ImageSize[3];
      arrayOfImageSize[0] = EXTRA_SMALL;
      arrayOfImageSize[1] = SMALL;
      arrayOfImageSize[2] = NORMAL;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.util.ImageUtils
 * JD-Core Version:    0.6.2
 */