package com.google.wireless.gdata.parser.xml;

import com.google.wireless.gdata.data.Entry;
import com.google.wireless.gdata.data.Feed;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.data.XmlUtils;
import com.google.wireless.gdata.parser.GDataParser;
import com.google.wireless.gdata.parser.ParseException;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlGDataParser
  implements GDataParser
{
  public static final String NAMESPACE_ATOM_URI = "http://www.w3.org/2005/Atom";
  public static final String NAMESPACE_GD = "gd";
  public static final String NAMESPACE_GD_URI = "http://schemas.google.com/g/2005";
  public static final String NAMESPACE_OPENSEARCH = "openSearch";
  public static final String NAMESPACE_OPENSEARCH_URI = "http://a9.com/-/spec/opensearchrss/1.0/";
  private final InputStream is;
  private boolean isInBadState;
  private final XmlPullParser parser;

  public XmlGDataParser(InputStream paramInputStream, XmlPullParser paramXmlPullParser)
    throws ParseException
  {
    this.is = paramInputStream;
    this.parser = paramXmlPullParser;
    this.isInBadState = false;
    if (this.is != null);
    try
    {
      this.parser.setInput(paramInputStream, null);
      return;
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new ParseException("Could not create XmlGDataParser", localXmlPullParserException);
    }
  }

  private void handleAuthor(Entry paramEntry)
    throws XmlPullParserException, IOException
  {
    int i = this.parser.getEventType();
    this.parser.getName();
    if ((i != 2) || (!"author".equals(this.parser.getName())))
      throw new IllegalStateException("Expected <author>: Actual element: <" + this.parser.getName() + ">");
    int j = this.parser.next();
    if (j != 1)
    {
      switch (j)
      {
      default:
      case 2:
      case 3:
      }
      do
        while (true)
        {
          j = this.parser.next();
          break;
          String str = this.parser.getName();
          if ("name".equals(str))
            paramEntry.setAuthor(XmlUtils.extractChildText(this.parser));
          else if ("email".equals(str))
            paramEntry.setEmail(XmlUtils.extractChildText(this.parser));
        }
      while (!"author".equals(this.parser.getName()));
    }
  }

  private final Feed parseFeed()
    throws XmlPullParserException, IOException
  {
    Feed localFeed = createFeed();
    int i = this.parser.next();
    if (i != 1)
      switch (i)
      {
      default:
      case 2:
      }
    while (true)
    {
      i = this.parser.next();
      break;
      String str1 = this.parser.getName();
      if ("totalResults".equals(str1))
      {
        localFeed.setTotalResults(StringUtils.parseInt(XmlUtils.extractChildText(this.parser), 0));
      }
      else if ("startIndex".equals(str1))
      {
        localFeed.setStartIndex(StringUtils.parseInt(XmlUtils.extractChildText(this.parser), 0));
      }
      else if ("itemsPerPage".equals(str1))
      {
        localFeed.setItemsPerPage(StringUtils.parseInt(XmlUtils.extractChildText(this.parser), 0));
      }
      else if ("title".equals(str1))
      {
        localFeed.setTitle(XmlUtils.extractChildText(this.parser));
      }
      else if ("id".equals(str1))
      {
        localFeed.setId(XmlUtils.extractChildText(this.parser));
      }
      else if ("updated".equals(str1))
      {
        localFeed.setLastUpdated(XmlUtils.extractChildText(this.parser));
      }
      else if ("category".equals(str1))
      {
        String str2 = this.parser.getAttributeValue(null, "term");
        if (!StringUtils.isEmpty(str2))
          localFeed.setCategory(str2);
        String str3 = this.parser.getAttributeValue(null, "scheme");
        if (!StringUtils.isEmpty(str3))
          localFeed.setCategoryScheme(str3);
      }
      else
      {
        if ("entry".equals(str1))
          return localFeed;
        handleExtraElementInFeed(localFeed);
      }
    }
  }

  public void close()
  {
    if (this.is != null);
    try
    {
      this.is.close();
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  protected Entry createEntry()
  {
    return new Entry();
  }

  protected Feed createFeed()
  {
    return new Feed();
  }

  protected final XmlPullParser getParser()
  {
    return this.parser;
  }

  protected void handleEntry(Entry paramEntry)
    throws XmlPullParserException, IOException, ParseException
  {
    int i = this.parser.getEventType();
    if (i != 1)
      switch (i)
      {
      default:
      case 2:
      }
    while (true)
    {
      i = this.parser.next();
      break;
      String str1 = this.parser.getName();
      if ("entry".equals(str1))
        return;
      if ("id".equals(str1))
      {
        paramEntry.setId(XmlUtils.extractChildText(this.parser));
      }
      else if ("title".equals(str1))
      {
        paramEntry.setTitle(XmlUtils.extractChildText(this.parser));
      }
      else if ("link".equals(str1))
      {
        String str4 = this.parser.getAttributeValue(null, "rel");
        String str5 = this.parser.getAttributeValue(null, "type");
        String str6 = this.parser.getAttributeValue(null, "href");
        if ("edit".equals(str4))
          paramEntry.setEditUri(str6);
        else if (("alternate".equals(str4)) && ("text/html".equals(str5)))
          paramEntry.setHtmlUri(str6);
        else
          handleExtraLinkInEntry(str4, str5, str6, paramEntry);
      }
      else if ("summary".equals(str1))
      {
        paramEntry.setSummary(XmlUtils.extractChildText(this.parser));
      }
      else if ("content".equals(str1))
      {
        paramEntry.setContent(XmlUtils.extractChildText(this.parser));
      }
      else if ("author".equals(str1))
      {
        handleAuthor(paramEntry);
      }
      else if ("category".equals(str1))
      {
        String str2 = this.parser.getAttributeValue(null, "term");
        if ((str2 != null) && (str2.length() > 0))
          paramEntry.setCategory(str2);
        String str3 = this.parser.getAttributeValue(null, "scheme");
        if ((str3 != null) && (str2.length() > 0))
          paramEntry.setCategoryScheme(str3);
      }
      else if ("published".equals(str1))
      {
        paramEntry.setPublicationDate(XmlUtils.extractChildText(this.parser));
      }
      else if ("updated".equals(str1))
      {
        paramEntry.setUpdateDate(XmlUtils.extractChildText(this.parser));
      }
      else if ("deleted".equals(str1))
      {
        paramEntry.setDeleted(true);
      }
      else
      {
        handleExtraElementInEntry(paramEntry);
      }
    }
  }

  protected void handleExtraElementInEntry(Entry paramEntry)
    throws XmlPullParserException, IOException, ParseException
  {
  }

  protected void handleExtraElementInFeed(Feed paramFeed)
    throws XmlPullParserException, IOException
  {
  }

  protected void handleExtraLinkInEntry(String paramString1, String paramString2, String paramString3, Entry paramEntry)
    throws XmlPullParserException, IOException
  {
  }

  public boolean hasMoreData()
  {
    if (this.isInBadState)
      return false;
    try
    {
      int i = this.parser.getEventType();
      return i != 1;
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
    }
    return false;
  }

  // ERROR //
  public final Feed init()
    throws ParseException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 41	com/google/wireless/gdata/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   4: invokeinterface 62 1 0
    //   9: istore_2
    //   10: iload_2
    //   11: ifeq +27 -> 38
    //   14: new 32	com/google/wireless/gdata/parser/ParseException
    //   17: dup
    //   18: ldc_w 261
    //   21: invokespecial 262	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;)V
    //   24: athrow
    //   25: astore_1
    //   26: new 32	com/google/wireless/gdata/parser/ParseException
    //   29: dup
    //   30: ldc_w 264
    //   33: aload_1
    //   34: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   37: athrow
    //   38: aload_0
    //   39: getfield 41	com/google/wireless/gdata/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   42: invokeinterface 96 1 0
    //   47: istore 5
    //   49: iload 5
    //   51: istore 6
    //   53: iload 6
    //   55: iconst_1
    //   56: if_icmpeq +157 -> 213
    //   59: iload 6
    //   61: tableswitch	default:+19 -> 80, 2:+65->126
    //   81: getfield 41	com/google/wireless/gdata/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   84: invokeinterface 96 1 0
    //   89: istore 12
    //   91: iload 12
    //   93: istore 6
    //   95: goto -42 -> 53
    //   98: astore 4
    //   100: new 32	com/google/wireless/gdata/parser/ParseException
    //   103: dup
    //   104: ldc_w 266
    //   107: aload 4
    //   109: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   112: athrow
    //   113: astore_3
    //   114: new 32	com/google/wireless/gdata/parser/ParseException
    //   117: dup
    //   118: ldc_w 266
    //   121: aload_3
    //   122: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   125: athrow
    //   126: ldc_w 268
    //   129: aload_0
    //   130: getfield 41	com/google/wireless/gdata/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   133: invokeinterface 66 1 0
    //   138: invokevirtual 74	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   141: ifeq -61 -> 80
    //   144: aload_0
    //   145: invokespecial 270	com/google/wireless/gdata/parser/xml/XmlGDataParser:parseFeed	()Lcom/google/wireless/gdata/data/Feed;
    //   148: astore 9
    //   150: aload 9
    //   152: areturn
    //   153: astore 8
    //   155: new 32	com/google/wireless/gdata/parser/ParseException
    //   158: dup
    //   159: ldc_w 272
    //   162: aload 8
    //   164: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   167: athrow
    //   168: astore 7
    //   170: new 32	com/google/wireless/gdata/parser/ParseException
    //   173: dup
    //   174: ldc_w 272
    //   177: aload 7
    //   179: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   182: athrow
    //   183: astore 11
    //   185: new 32	com/google/wireless/gdata/parser/ParseException
    //   188: dup
    //   189: ldc_w 266
    //   192: aload 11
    //   194: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   197: athrow
    //   198: astore 10
    //   200: new 32	com/google/wireless/gdata/parser/ParseException
    //   203: dup
    //   204: ldc_w 266
    //   207: aload 10
    //   209: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   212: athrow
    //   213: new 32	com/google/wireless/gdata/parser/ParseException
    //   216: dup
    //   217: ldc_w 274
    //   220: invokespecial 262	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;)V
    //   223: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   0	10	25	org/xmlpull/v1/XmlPullParserException
    //   38	49	98	org/xmlpull/v1/XmlPullParserException
    //   38	49	113	java/io/IOException
    //   144	150	153	org/xmlpull/v1/XmlPullParserException
    //   144	150	168	java/io/IOException
    //   80	91	183	org/xmlpull/v1/XmlPullParserException
    //   80	91	198	java/io/IOException
  }

  // ERROR //
  public Entry parseStandaloneEntry()
    throws ParseException, IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 277	com/google/wireless/gdata/parser/xml/XmlGDataParser:createEntry	()Lcom/google/wireless/gdata/data/Entry;
    //   4: astore_1
    //   5: aload_0
    //   6: getfield 41	com/google/wireless/gdata/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   9: invokeinterface 62 1 0
    //   14: istore_3
    //   15: iload_3
    //   16: ifeq +27 -> 43
    //   19: new 32	com/google/wireless/gdata/parser/ParseException
    //   22: dup
    //   23: ldc_w 261
    //   26: invokespecial 262	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;)V
    //   29: athrow
    //   30: astore_2
    //   31: new 32	com/google/wireless/gdata/parser/ParseException
    //   34: dup
    //   35: ldc_w 279
    //   38: aload_2
    //   39: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   42: athrow
    //   43: aload_0
    //   44: getfield 41	com/google/wireless/gdata/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   47: invokeinterface 96 1 0
    //   52: istore 6
    //   54: iload 6
    //   56: istore 7
    //   58: iload 7
    //   60: iconst_1
    //   61: if_icmpeq +150 -> 211
    //   64: iload 7
    //   66: tableswitch	default:+18 -> 84, 2:+66->132
    //   85: getfield 41	com/google/wireless/gdata/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   88: invokeinterface 96 1 0
    //   93: istore 12
    //   95: iload 12
    //   97: istore 7
    //   99: goto -41 -> 58
    //   102: astore 5
    //   104: new 32	com/google/wireless/gdata/parser/ParseException
    //   107: dup
    //   108: ldc_w 266
    //   111: aload 5
    //   113: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   116: athrow
    //   117: astore 4
    //   119: new 32	com/google/wireless/gdata/parser/ParseException
    //   122: dup
    //   123: ldc_w 266
    //   126: aload 4
    //   128: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   131: athrow
    //   132: ldc 180
    //   134: aload_0
    //   135: getfield 41	com/google/wireless/gdata/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   138: invokeinterface 66 1 0
    //   143: invokevirtual 74	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   146: ifeq -62 -> 84
    //   149: aload_0
    //   150: getfield 41	com/google/wireless/gdata/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   153: invokeinterface 96 1 0
    //   158: pop
    //   159: aload_0
    //   160: aload_1
    //   161: invokevirtual 281	com/google/wireless/gdata/parser/xml/XmlGDataParser:handleEntry	(Lcom/google/wireless/gdata/data/Entry;)V
    //   164: aload_1
    //   165: areturn
    //   166: astore 9
    //   168: new 32	com/google/wireless/gdata/parser/ParseException
    //   171: dup
    //   172: ldc_w 283
    //   175: aload 9
    //   177: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   180: athrow
    //   181: astore 8
    //   183: new 32	com/google/wireless/gdata/parser/ParseException
    //   186: dup
    //   187: ldc_w 283
    //   190: aload 8
    //   192: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   195: athrow
    //   196: astore 11
    //   198: new 32	com/google/wireless/gdata/parser/ParseException
    //   201: dup
    //   202: ldc_w 266
    //   205: aload 11
    //   207: invokespecial 54	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   210: athrow
    //   211: new 32	com/google/wireless/gdata/parser/ParseException
    //   214: dup
    //   215: ldc_w 285
    //   218: invokespecial 262	com/google/wireless/gdata/parser/ParseException:<init>	(Ljava/lang/String;)V
    //   221: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   5	15	30	org/xmlpull/v1/XmlPullParserException
    //   43	54	102	org/xmlpull/v1/XmlPullParserException
    //   43	54	117	java/io/IOException
    //   149	164	166	org/xmlpull/v1/XmlPullParserException
    //   149	164	181	java/io/IOException
    //   84	95	196	org/xmlpull/v1/XmlPullParserException
  }

  public Entry readNextEntry(Entry paramEntry)
    throws ParseException, IOException
  {
    if (!hasMoreData())
      throw new IllegalStateException("you shouldn't call this if hasMoreData() is false");
    try
    {
      int i = this.parser.getEventType();
      if (i != 2)
        throw new ParseException("Expected event START_TAG: Actual event: " + XmlPullParser.TYPES[i]);
    }
    catch (XmlPullParserException localXmlPullParserException1)
    {
      throw new ParseException("Could not parse entry.", localXmlPullParserException1);
    }
    String str = this.parser.getName();
    if (!"entry".equals(str))
      throw new ParseException("Expected <entry>: Actual element: <" + str + ">");
    if (paramEntry == null)
      paramEntry = createEntry();
    try
    {
      while (true)
      {
        this.parser.next();
        handleEntry(paramEntry);
        paramEntry.validate();
        return paramEntry;
        paramEntry.clear();
      }
    }
    catch (ParseException localParseException)
    {
      try
      {
        if (hasMoreData())
          skipToNextEntry();
        throw new ParseException("Could not parse <entry>, " + paramEntry, localParseException);
      }
      catch (XmlPullParserException localXmlPullParserException4)
      {
        while (true)
          this.isInBadState = true;
      }
    }
    catch (XmlPullParserException localXmlPullParserException2)
    {
    }
    try
    {
      if (hasMoreData())
        skipToNextEntry();
      throw new ParseException("Could not parse <entry>, " + paramEntry, localXmlPullParserException2);
    }
    catch (XmlPullParserException localXmlPullParserException3)
    {
      while (true)
        this.isInBadState = true;
    }
  }

  protected void skipToNextEntry()
    throws IOException, XmlPullParserException
  {
    if (!hasMoreData())
      throw new IllegalStateException("you shouldn't call this if hasMoreData() is false");
    int i = this.parser.getEventType();
    if (i != 1)
    {
      switch (i)
      {
      default:
      case 2:
      }
      do
      {
        i = this.parser.next();
        break;
      }
      while (!"entry".equals(this.parser.getName()));
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.parser.xml.XmlGDataParser
 * JD-Core Version:    0.6.2
 */