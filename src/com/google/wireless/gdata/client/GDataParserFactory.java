package com.google.wireless.gdata.client;

import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.parser.GDataParser;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.serializer.GDataSerializer;
import java.io.InputStream;

public abstract interface GDataParserFactory
{
  public abstract GDataParser createParser(InputStream paramInputStream)
    throws ParseException;

  public abstract GDataParser createParser(Class paramClass, InputStream paramInputStream)
    throws ParseException;

  public abstract GDataSerializer createSerializer(Entry paramEntry);
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.client.GDataParserFactory
 * JD-Core Version:    0.6.2
 */