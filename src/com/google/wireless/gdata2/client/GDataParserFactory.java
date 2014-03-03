package com.google.wireless.gdata2.client;

import com.google.wireless.gdata2.data.Entry;
import com.google.wireless.gdata2.parser.GDataParser;
import com.google.wireless.gdata2.parser.ParseException;
import com.google.wireless.gdata2.serializer.GDataSerializer;
import java.io.InputStream;
import java.util.Enumeration;

public abstract interface GDataParserFactory
{
  public abstract GDataParser createParser(InputStream paramInputStream)
    throws ParseException;

  public abstract GDataParser createParser(Class paramClass, InputStream paramInputStream)
    throws ParseException;

  public abstract GDataSerializer createSerializer(Entry paramEntry);

  public abstract GDataSerializer createSerializer(Enumeration paramEnumeration);
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.client.GDataParserFactory
 * JD-Core Version:    0.6.2
 */