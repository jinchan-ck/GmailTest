package com.google.wireless.gdata.parser.xml;

import android.util.Log;
import android.util.Xml;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SimplePullParser
{
  public static final String TEXT_TAG = "![CDATA[";
  private String mCurrentStartTag;
  private String mLogTag = null;
  private final XmlPullParser mParser;
  private Closeable source;

  public SimplePullParser(InputStream paramInputStream, String paramString)
    throws SimplePullParser.ParseException, IOException
  {
    try
    {
      XmlPullParser localXmlPullParser = Xml.newPullParser();
      localXmlPullParser.setInput(paramInputStream, paramString);
      moveToStartDocument(localXmlPullParser);
      this.mParser = localXmlPullParser;
      this.mCurrentStartTag = null;
      this.source = paramInputStream;
      return;
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException(localXmlPullParserException);
    }
  }

  public SimplePullParser(Reader paramReader)
    throws IOException, SimplePullParser.ParseException
  {
    try
    {
      XmlPullParser localXmlPullParser = Xml.newPullParser();
      localXmlPullParser.setInput(paramReader);
      moveToStartDocument(localXmlPullParser);
      this.mParser = localXmlPullParser;
      this.mCurrentStartTag = null;
      this.source = paramReader;
      return;
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException(localXmlPullParserException);
    }
  }

  public SimplePullParser(String paramString)
    throws IOException, SimplePullParser.ParseException
  {
    this(new StringReader(paramString));
  }

  public SimplePullParser(XmlPullParser paramXmlPullParser)
  {
    this.mParser = paramXmlPullParser;
    this.mCurrentStartTag = null;
    this.source = null;
  }

  private static void moveToStartDocument(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    if (paramXmlPullParser.getEventType() != 0)
      throw new XmlPullParserException("Not at start of response");
  }

  public void close()
  {
    if (this.source != null);
    try
    {
      this.source.close();
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  public String getAttributeName(int paramInt)
  {
    return this.mParser.getAttributeName(paramInt);
  }

  public String getAttributeNamespace(int paramInt)
  {
    return this.mParser.getAttributeNamespace(paramInt);
  }

  public int getDepth()
  {
    return this.mParser.getDepth();
  }

  public int getIntAttribute(String paramString1, String paramString2)
    throws SimplePullParser.ParseException
  {
    String str = getStringAttribute(paramString1, paramString2);
    try
    {
      int i = Integer.parseInt(str);
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw new ParseException("Cannot parse '" + str + "' as an integer");
  }

  public int getIntAttribute(String paramString1, String paramString2, int paramInt)
    throws SimplePullParser.ParseException
  {
    String str = this.mParser.getAttributeValue(paramString1, paramString2);
    if (str == null)
      return paramInt;
    try
    {
      int i = Integer.parseInt(str);
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw new ParseException("Cannot parse '" + str + "' as an integer");
  }

  public long getLongAttribute(String paramString1, String paramString2)
    throws SimplePullParser.ParseException
  {
    String str = getStringAttribute(paramString1, paramString2);
    try
    {
      long l = Long.parseLong(str);
      return l;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw new ParseException("Cannot parse '" + str + "' as a long");
  }

  public long getLongAttribute(String paramString1, String paramString2, long paramLong)
    throws SimplePullParser.ParseException
  {
    String str = this.mParser.getAttributeValue(paramString1, paramString2);
    if (str == null)
      return paramLong;
    try
    {
      long l = Long.parseLong(str);
      return l;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw new ParseException("Cannot parse '" + str + "' as a long");
  }

  public String getStringAttribute(String paramString1, String paramString2)
    throws SimplePullParser.ParseException
  {
    String str = this.mParser.getAttributeValue(paramString1, paramString2);
    if (str == null)
      throw new ParseException("missing '" + paramString2 + "' attribute on '" + this.mCurrentStartTag + "' element");
    return str;
  }

  public String getStringAttribute(String paramString1, String paramString2, String paramString3)
  {
    String str = this.mParser.getAttributeValue(paramString1, paramString2);
    if (str == null)
      return paramString3;
    return str;
  }

  public String nextTag(int paramInt)
    throws IOException, SimplePullParser.ParseException
  {
    return nextTagOrText(paramInt, null);
  }

  public String nextTagOrText(int paramInt, StringBuilder paramStringBuilder)
    throws IOException, SimplePullParser.ParseException
  {
    int i;
    int j;
    label241: label251: 
    do
    {
      StringBuilder localStringBuilder2;
      try
      {
        i = this.mParser.next();
        j = this.mParser.getDepth();
        this.mCurrentStartTag = null;
        if ((i != 2) || (j != paramInt + 1))
          break label251;
        this.mCurrentStartTag = this.mParser.getName();
        if ((this.mLogTag == null) || (!Log.isLoggable(this.mLogTag, 3)))
          break label241;
        localStringBuilder2 = new StringBuilder();
        for (int m = 0; m < j; m++)
          localStringBuilder2.append("  ");
      }
      catch (XmlPullParserException localXmlPullParserException)
      {
        throw new ParseException(localXmlPullParserException);
      }
      localStringBuilder2.append("<").append(this.mParser.getName());
      int n = this.mParser.getAttributeCount();
      for (int i1 = 0; i1 < n; i1++)
      {
        localStringBuilder2.append(" ");
        localStringBuilder2.append(this.mParser.getAttributeName(i1));
        localStringBuilder2.append("=\"");
        localStringBuilder2.append(this.mParser.getAttributeValue(i1));
        localStringBuilder2.append("\"");
      }
      localStringBuilder2.append(">");
      Log.d(this.mLogTag, localStringBuilder2.toString());
      return this.mParser.getName();
      if ((i == 3) && (j == paramInt))
      {
        if ((this.mLogTag != null) && (Log.isLoggable(this.mLogTag, 3)))
        {
          StringBuilder localStringBuilder1 = new StringBuilder();
          for (int k = 0; k < j; k++)
            localStringBuilder1.append("  ");
          localStringBuilder1.append("</>");
          Log.d(this.mLogTag, localStringBuilder1.toString());
        }
        return null;
      }
      if ((i == 1) && (paramInt == 0))
      {
        if (this.source != null)
        {
          this.source.close();
          this.source = null;
        }
        return null;
      }
    }
    while ((i != 4) || (j != paramInt) || (paramStringBuilder == null));
    paramStringBuilder.append(this.mParser.getText());
    return "![CDATA[";
  }

  public int numAttributes()
  {
    return this.mParser.getAttributeCount();
  }

  public void readRemainingText(int paramInt, StringBuilder paramStringBuilder)
    throws IOException, SimplePullParser.ParseException
  {
    while (nextTagOrText(paramInt, paramStringBuilder) != null);
  }

  public void setLogTag(String paramString)
  {
    this.mLogTag = paramString;
  }

  public static final class ParseException extends Exception
  {
    public ParseException(String paramString)
    {
      super();
    }

    public ParseException(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }

    public ParseException(Throwable paramThrowable)
    {
      super();
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.parser.xml.SimplePullParser
 * JD-Core Version:    0.6.2
 */