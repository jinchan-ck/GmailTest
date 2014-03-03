package com.google.wireless.gdata.parser.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public abstract interface XmlParserFactory
{
  public abstract XmlPullParser createParser()
    throws XmlPullParserException;

  public abstract XmlSerializer createSerializer()
    throws XmlPullParserException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.parser.xml.XmlParserFactory
 * JD-Core Version:    0.6.2
 */