package com.google.wireless.gdata2.parser;

import com.google.wireless.gdata2.data.Entry;
import com.google.wireless.gdata2.data.Feed;
import java.io.IOException;

public abstract interface GDataParser
{
  public abstract void close();

  public abstract boolean hasMoreData();

  public abstract Feed parseFeedEnvelope()
    throws ParseException;

  public abstract Entry parseStandaloneEntry()
    throws ParseException, IOException;

  public abstract Entry readNextEntry(Entry paramEntry)
    throws ParseException, IOException;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.parser.GDataParser
 * JD-Core Version:    0.6.2
 */