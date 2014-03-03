package com.google.android.gm.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class ZipUtils
{
  public static byte[] deflate(byte[] paramArrayOfByte)
  {
    return deflate(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public static byte[] deflate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DeflaterOutputStream localDeflaterOutputStream = new DeflaterOutputStream(localByteArrayOutputStream, new Deflater());
    try
    {
      localDeflaterOutputStream.write(paramArrayOfByte, paramInt1, paramInt2);
      localDeflaterOutputStream.close();
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException("ByteArrayOutputStream threw ", localIOException);
    }
  }

  public static byte[] inflate(Inflater paramInflater)
    throws DataFormatException
  {
    return inflateToStream(paramInflater).toByteArray();
  }

  private static ByteArrayOutputStream inflateToStream(Inflater paramInflater)
    throws DataFormatException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte = new byte[1024];
    int i;
    do
    {
      i = paramInflater.inflate(arrayOfByte);
      if (i != 0)
        localByteArrayOutputStream.write(arrayOfByte, 0, i);
    }
    while (i != 0);
    return localByteArrayOutputStream;
  }

  public static InputStream inflateToStream(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    Inflater local1 = new Inflater()
    {
      public boolean needsDictionary()
      {
        if (super.needsDictionary())
          setDictionary(this.val$dict);
        return false;
      }
    };
    return new InflaterInputStream(new ByteArrayInputStream(paramArrayOfByte1), local1)
    {
      public void close()
        throws IOException
      {
        super.close();
        this.inf.end();
      }
    };
  }

  public static String inflateToUTF8(Inflater paramInflater)
    throws DataFormatException, UnsupportedEncodingException
  {
    return inflateToStream(paramInflater).toString("UTF-8");
  }

  public static String inflateToUTF8(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws DataFormatException, UnsupportedEncodingException
  {
    Inflater localInflater = new Inflater();
    try
    {
      localInflater.setInput(paramArrayOfByte, paramInt1, paramInt2);
      String str = inflateToUTF8(localInflater);
      return str;
    }
    finally
    {
      localInflater.end();
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.ZipUtils
 * JD-Core Version:    0.6.2
 */