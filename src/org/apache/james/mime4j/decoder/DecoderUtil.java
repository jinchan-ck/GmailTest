package org.apache.james.mime4j.decoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.james.mime4j.Log;
import org.apache.james.mime4j.LogFactory;
import org.apache.james.mime4j.util.CharsetUtil;

public class DecoderUtil
{
  private static Log log = LogFactory.getLog(DecoderUtil.class);

  public static String decodeB(String paramString1, String paramString2)
    throws UnsupportedEncodingException
  {
    return new String(decodeBase64(paramString1), paramString2);
  }

  public static byte[] decodeBase64(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      Base64InputStream localBase64InputStream = new Base64InputStream(new ByteArrayInputStream(paramString.getBytes("US-ASCII")));
      while (true)
      {
        int i = localBase64InputStream.read();
        if (i == -1)
          break;
        localByteArrayOutputStream.write(i);
      }
    }
    catch (IOException localIOException)
    {
      log.error(localIOException);
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public static byte[] decodeBaseQuotedPrintable(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      QuotedPrintableInputStream localQuotedPrintableInputStream = new QuotedPrintableInputStream(new ByteArrayInputStream(paramString.getBytes("US-ASCII")));
      while (true)
      {
        int i = localQuotedPrintableInputStream.read();
        if (i == -1)
          break;
        localByteArrayOutputStream.write(i);
      }
    }
    catch (IOException localIOException)
    {
      log.error(localIOException);
    }
    return localByteArrayOutputStream.toByteArray();
  }

  private static String decodeEncodedWord(String paramString, int paramInt1, int paramInt2)
  {
    int i = paramString.indexOf('?', paramInt1 + 2);
    if (i == paramInt2 - 2);
    do
      while (true)
      {
        return null;
        int j = paramString.indexOf('?', i + 1);
        if (j != paramInt2 - 2)
        {
          String str1 = paramString.substring(paramInt1 + 2, i);
          String str2 = paramString.substring(i + 1, j);
          String str3 = paramString.substring(j + 1, paramInt2 - 2);
          String str4 = CharsetUtil.toJavaCharset(str1);
          if (str4 == null)
          {
            if (log.isWarnEnabled())
            {
              log.warn("MIME charset '" + str1 + "' in encoded word '" + paramString.substring(paramInt1, paramInt2) + "' doesn't have a " + "corresponding Java charset");
              return null;
            }
          }
          else if (!CharsetUtil.isDecodingSupported(str4))
          {
            if (log.isWarnEnabled())
            {
              log.warn("Current JDK doesn't support decoding of charset '" + str4 + "' (MIME charset '" + str1 + "' in encoded word '" + paramString.substring(paramInt1, paramInt2) + "')");
              return null;
            }
          }
          else if (str3.length() == 0)
          {
            if (log.isWarnEnabled())
            {
              log.warn("Missing encoded text in encoded word: '" + paramString.substring(paramInt1, paramInt2) + "'");
              return null;
            }
          }
          else
            try
            {
              if (str2.equalsIgnoreCase("Q"))
                return decodeQ(str3, str4);
              if (str2.equalsIgnoreCase("B"))
                return decodeB(str3, str4);
              if (log.isWarnEnabled())
              {
                log.warn("Warning: Unknown encoding in encoded word '" + paramString.substring(paramInt1, paramInt2) + "'");
                return null;
              }
            }
            catch (UnsupportedEncodingException localUnsupportedEncodingException)
            {
              if (log.isWarnEnabled())
              {
                log.warn("Unsupported encoding in encoded word '" + paramString.substring(paramInt1, paramInt2) + "'", localUnsupportedEncodingException);
                return null;
              }
            }
            catch (RuntimeException localRuntimeException)
            {
            }
        }
      }
    while (!log.isWarnEnabled());
    log.warn("Could not decode encoded word '" + paramString.substring(paramInt1, paramInt2) + "'", localRuntimeException);
    return null;
  }

  public static String decodeEncodedWords(String paramString)
  {
    if (paramString.indexOf("=?") == -1)
      return paramString;
    int i = 0;
    int j = 0;
    StringBuilder localStringBuilder = new StringBuilder();
    int k = paramString.indexOf("=?", i);
    int m = k + 2;
    if (k != -1)
    {
      int i2 = paramString.indexOf('?', 1 + paramString.indexOf(63, m + 2));
      if (i2 != -1)
        m = i2 + 1;
    }
    if (k == -1);
    for (int n = -1; ; n = paramString.indexOf("?=", m))
    {
      if (n != -1)
        break label124;
      if (i == 0)
        break;
      localStringBuilder.append(paramString.substring(i));
      return localStringBuilder.toString();
    }
    label124: int i1 = n + 2;
    String str1 = paramString.substring(i, k);
    String str2 = decodeEncodedWord(paramString, k, i1);
    if (str2 == null)
    {
      localStringBuilder.append(str1);
      localStringBuilder.append(paramString.substring(k, i1));
      label174: i = i1;
      if (str2 == null)
        break label216;
    }
    label216: for (j = 1; ; j = 0)
    {
      break;
      if ((j == 0) || (!CharsetUtil.isWhitespace(str1)))
        localStringBuilder.append(str1);
      localStringBuilder.append(str2);
      break label174;
    }
  }

  public static String decodeQ(String paramString1, String paramString2)
    throws UnsupportedEncodingException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    if (i < paramString1.length())
    {
      char c = paramString1.charAt(i);
      if (c == '_')
        localStringBuffer.append("=20");
      while (true)
      {
        i++;
        break;
        localStringBuffer.append(c);
      }
    }
    return new String(decodeBaseQuotedPrintable(localStringBuffer.toString()), paramString2);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     org.apache.james.mime4j.decoder.DecoderUtil
 * JD-Core Version:    0.6.2
 */