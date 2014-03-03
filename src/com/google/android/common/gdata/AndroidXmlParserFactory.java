package com.google.android.common.gdata;

import android.util.Xml;
import com.google.wireless.gdata.parser.xml.XmlParserFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class AndroidXmlParserFactory
  implements XmlParserFactory
{
  public XmlPullParser createParser()
    throws XmlPullParserException
  {
    return Xml.newPullParser();
  }

  public XmlSerializer createSerializer()
    throws XmlPullParserException
  {
    return Xml.newSerializer();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.gdata.AndroidXmlParserFactory
 * JD-Core Version:    0.6.2
 */