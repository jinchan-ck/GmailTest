package com.google.wireless.gdata.data;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class XmlUtils
{
  public static String extractChildText(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    if (paramXmlPullParser.next() != 4)
      return null;
    return paramXmlPullParser.getText();
  }

  public static String extractFirstChildTextIgnoreRest(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    int j = paramXmlPullParser.next();
    String str = null;
    if (j != 1)
    {
      int k = paramXmlPullParser.getDepth();
      if (j == 4)
        if (str == null)
          str = paramXmlPullParser.getText();
      while ((j != 3) || (k != i))
      {
        j = paramXmlPullParser.next();
        break;
      }
      return str;
    }
    throw new XmlPullParserException("End of document reached; never saw expected end tag at depth " + i);
  }

  public static String nextDirectChildTag(XmlPullParser paramXmlPullParser, int paramInt)
    throws XmlPullParserException, IOException
  {
    int i = paramInt + 1;
    for (int j = paramXmlPullParser.next(); j != 1; j = paramXmlPullParser.next())
    {
      int k = paramXmlPullParser.getDepth();
      if ((j == 2) && (k == i))
        return paramXmlPullParser.getName();
      if ((j == 3) && (k == paramInt))
        return null;
    }
    throw new XmlPullParserException("End of document reached; never saw expected end tag at depth " + paramInt);
  }

  public void skipSubTree(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = 1;
    while (i > 0)
      switch (paramXmlPullParser.next())
      {
      default:
        break;
      case 2:
        i++;
        break;
      case 3:
        i--;
      }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.data.XmlUtils
 * JD-Core Version:    0.6.2
 */