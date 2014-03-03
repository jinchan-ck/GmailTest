package com.google.wireless.gdata2.parser.xml;

import com.google.wireless.gdata2.data.Entry;
import com.google.wireless.gdata2.data.Feed;
import com.google.wireless.gdata2.data.StringUtils;
import com.google.wireless.gdata2.data.XmlUtils;
import com.google.wireless.gdata2.data.batch.BatchInterrupted;
import com.google.wireless.gdata2.data.batch.BatchStatus;
import com.google.wireless.gdata2.data.batch.BatchUtils;
import com.google.wireless.gdata2.parser.GDataParser;
import com.google.wireless.gdata2.parser.ParseException;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlGDataParser
  implements GDataParser
{
  public static final String NAMESPACE_ATOM_URI = "http://www.w3.org/2005/Atom";
  public static final String NAMESPACE_BATCH = "batch";
  public static final String NAMESPACE_BATCH_URI = "http://schemas.google.com/gdata/batch";
  public static final String NAMESPACE_GD = "gd";
  public static final String NAMESPACE_GD_URI = "http://schemas.google.com/g/2005";
  public static final String NAMESPACE_OPENSEARCH_1_0_URI = "http://a9.com/-/spec/opensearchrss/1.0/";
  public static final String NAMESPACE_OPENSEARCH_1_1_URI = "http://a9.com/-/spec/opensearch/1.1/";
  private String fields;
  private final InputStream is;
  private boolean isInBadState;
  private final XmlPullParser parser;

  public XmlGDataParser(InputStream paramInputStream, XmlPullParser paramXmlPullParser)
    throws ParseException
  {
    this.is = paramInputStream;
    this.parser = paramXmlPullParser;
    if (!paramXmlPullParser.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces"))
      throw new IllegalStateException("A XmlGDataParser needs to be constructed with a namespace aware XmlPullParser");
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

  private static String getAttribute(XmlPullParser paramXmlPullParser, String paramString)
  {
    return paramXmlPullParser.getAttributeValue(null, paramString);
  }

  private static int getIntAttribute(XmlPullParser paramXmlPullParser, String paramString)
  {
    return Integer.parseInt(getAttribute(paramXmlPullParser, paramString));
  }

  private void handleAuthor(Entry paramEntry)
    throws XmlPullParserException, IOException
  {
    int i = this.parser.getEventType();
    this.parser.getName();
    if ((i != 2) || (!XmlNametable.AUTHOR.equals(this.parser.getName())))
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
      String str1;
      do
      {
        while (true)
        {
          j = this.parser.next();
          break;
          String str2 = this.parser.getName();
          if (XmlNametable.NAME.equals(str2))
            paramEntry.setAuthor(XmlUtils.extractChildText(this.parser));
          else if (XmlNametable.EMAIL.equals(str2))
            paramEntry.setEmail(XmlUtils.extractChildText(this.parser));
        }
        str1 = this.parser.getName();
      }
      while (!XmlNametable.AUTHOR.equals(str1));
    }
  }

  private void handleBatchInfo(Entry paramEntry)
    throws IOException, XmlPullParserException
  {
    String str = this.parser.getName();
    if (XmlNametable.STATUS.equals(str))
    {
      BatchStatus localBatchStatus = new BatchStatus();
      BatchUtils.setBatchStatus(paramEntry, localBatchStatus);
      localBatchStatus.setStatusCode(getIntAttribute(this.parser, XmlNametable.CODE));
      localBatchStatus.setReason(getAttribute(this.parser, XmlNametable.REASON));
      localBatchStatus.setContentType(getAttribute(this.parser, XmlNametable.CONTENT_TYPE));
      skipSubTree();
      return;
    }
    if (XmlNametable.ID.equals(str))
    {
      BatchUtils.setBatchId(paramEntry, XmlUtils.extractChildText(this.parser));
      return;
    }
    if (XmlNametable.OPERATION.equals(str))
    {
      BatchUtils.setBatchOperation(paramEntry, getAttribute(this.parser, XmlNametable.TYPE));
      return;
    }
    if ("interrupted".equals(str))
    {
      BatchInterrupted localBatchInterrupted = new BatchInterrupted();
      BatchUtils.setBatchInterrupted(paramEntry, localBatchInterrupted);
      localBatchInterrupted.setReason(getAttribute(this.parser, XmlNametable.REASON));
      localBatchInterrupted.setErrorCount(getIntAttribute(this.parser, XmlNametable.ERROR));
      localBatchInterrupted.setSuccessCount(getIntAttribute(this.parser, XmlNametable.SUCCESS));
      localBatchInterrupted.setTotalCount(getIntAttribute(this.parser, XmlNametable.PARSED));
      skipSubTree();
      return;
    }
    throw new XmlPullParserException("Unexpected batch element " + str);
  }

  private final Feed parseFeed()
    throws XmlPullParserException, IOException
  {
    Feed localFeed = createFeed();
    localFeed.setETag(this.parser.getAttributeValue("http://schemas.google.com/g/2005", XmlNametable.ETAG));
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
      String str2 = this.parser.getNamespace();
      if (("http://a9.com/-/spec/opensearchrss/1.0/".equals(str2)) || ("http://a9.com/-/spec/opensearch/1.1/".equals(str2)))
      {
        if (XmlNametable.TOTAL_RESULTS.equals(str1))
          localFeed.setTotalResults(StringUtils.parseInt(XmlUtils.extractChildText(this.parser), 0));
        else if (XmlNametable.START_INDEX.equals(str1))
          localFeed.setStartIndex(StringUtils.parseInt(XmlUtils.extractChildText(this.parser), 0));
        else if (XmlNametable.ITEMS_PER_PAGE.equals(str1))
          localFeed.setItemsPerPage(StringUtils.parseInt(XmlUtils.extractChildText(this.parser), 0));
      }
      else if ("http://www.w3.org/2005/Atom".equals(str2))
      {
        if (XmlNametable.TITLE.equals(str1))
        {
          localFeed.setTitle(XmlUtils.extractChildText(this.parser));
        }
        else if (XmlNametable.ID.equals(str1))
        {
          localFeed.setId(XmlUtils.extractChildText(this.parser));
        }
        else if (XmlNametable.UPDATED.equals(str1))
        {
          localFeed.setLastUpdated(XmlUtils.extractChildText(this.parser));
        }
        else if (XmlNametable.CATEGORY.equals(str1))
        {
          String str3 = this.parser.getAttributeValue(null, XmlNametable.TERM);
          if (!StringUtils.isEmpty(str3))
            localFeed.setCategory(str3);
          String str4 = this.parser.getAttributeValue(null, XmlNametable.SCHEME);
          if (!StringUtils.isEmpty(str4))
            localFeed.setCategoryScheme(str4);
        }
        else if (XmlNametable.ENTRY.equals(str1))
        {
          return localFeed;
        }
      }
      else
        handleExtraElementInFeed(localFeed);
    }
  }

  private final Feed parsePartialFeed()
    throws XmlPullParserException, IOException
  {
    this.fields = this.parser.getAttributeValue(null, XmlNametable.FIELDS);
    int i = this.parser.next();
    if (i != 1)
    {
      switch (i)
      {
      default:
      case 2:
      }
      String str;
      do
      {
        i = this.parser.next();
        break;
        str = this.parser.getName();
      }
      while ((!"http://www.w3.org/2005/Atom".equals(this.parser.getNamespace())) || (!XmlNametable.FEED.equals(str)));
      return parseFeed();
    }
    return null;
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

  protected boolean handleDefaultEntryElements(Entry paramEntry)
    throws XmlPullParserException, IOException
  {
    return false;
  }

  protected void handleEntry(Entry paramEntry)
    throws XmlPullParserException, IOException, ParseException
  {
    if (!XmlNametable.ENTRY.equals(this.parser.getName()))
      throw new IllegalStateException("Expected <entry>: Actual element: <" + this.parser.getName() + ">");
    paramEntry.setETag(this.parser.getAttributeValue("http://schemas.google.com/g/2005", XmlNametable.ETAG));
    paramEntry.setFields(this.fields);
    this.parser.next();
    int i = this.parser.getEventType();
    if (i != 1)
    {
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
        if (XmlNametable.ENTRY.equals(str1))
          return;
        if (!handleDefaultEntryElements(paramEntry))
          if ("http://schemas.google.com/gdata/batch".equals(this.parser.getNamespace()))
          {
            handleBatchInfo(paramEntry);
          }
          else if (XmlNametable.ID.equals(str1))
          {
            paramEntry.setId(XmlUtils.extractChildText(this.parser));
          }
          else if (XmlNametable.TITLE.equals(str1))
          {
            paramEntry.setTitle(XmlUtils.extractChildText(this.parser));
          }
          else if (XmlNametable.LINK.equals(str1))
          {
            String str4 = this.parser.getAttributeValue(null, XmlNametable.REL);
            String str5 = this.parser.getAttributeValue(null, XmlNametable.TYPE);
            String str6 = this.parser.getAttributeValue(null, XmlNametable.HREF);
            if (XmlNametable.EDIT_REL.equals(str4))
              paramEntry.setEditUri(str6);
            else if ((XmlNametable.ALTERNATE_REL.equals(str4)) && (XmlNametable.TEXTHTML.equals(str5)))
              paramEntry.setHtmlUri(str6);
            else
              handleExtraLinkInEntry(str4, str5, str6, paramEntry);
          }
          else if (XmlNametable.SUMMARY.equals(str1))
          {
            paramEntry.setSummary(XmlUtils.extractChildText(this.parser));
          }
          else if (XmlNametable.CONTENT.equals(str1))
          {
            paramEntry.setContentType(this.parser.getAttributeValue(null, XmlNametable.TYPE));
            paramEntry.setContentSource(this.parser.getAttributeValue(null, XmlNametable.SRC));
            paramEntry.setContent(XmlUtils.extractChildText(this.parser));
          }
          else if (XmlNametable.AUTHOR.equals(str1))
          {
            handleAuthor(paramEntry);
          }
          else if (XmlNametable.CATEGORY.equals(str1))
          {
            String str2 = this.parser.getAttributeValue(null, XmlNametable.TERM);
            if ((str2 != null) && (str2.length() > 0))
              paramEntry.setCategory(str2);
            String str3 = this.parser.getAttributeValue(null, XmlNametable.SCHEME);
            if ((str3 != null) && (str2.length() > 0))
              paramEntry.setCategoryScheme(str3);
          }
          else if (XmlNametable.PUBLISHED.equals(str1))
          {
            paramEntry.setPublicationDate(XmlUtils.extractChildText(this.parser));
          }
          else if (XmlNametable.UPDATED.equals(str1))
          {
            paramEntry.setUpdateDate(XmlUtils.extractChildText(this.parser));
          }
          else if (XmlNametable.DELETED.equals(str1))
          {
            paramEntry.setDeleted(true);
          }
          else
          {
            handleExtraElementInEntry(paramEntry);
          }
      }
    }
    paramEntry.validate();
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

  protected void handlePartialEntry(Entry paramEntry)
    throws XmlPullParserException, IOException, ParseException
  {
    if (!XmlNametable.PARTIAL.equals(this.parser.getName()))
      throw new IllegalStateException("Expected <partial>: Actual element: <" + this.parser.getName() + ">");
    this.fields = this.parser.getAttributeValue(null, XmlNametable.FIELDS);
    this.parser.next();
    int i = this.parser.getEventType();
    if (i != 1)
    {
      switch (i)
      {
      default:
      case 2:
      }
      String str;
      do
      {
        i = this.parser.next();
        break;
        str = this.parser.getName();
      }
      while (!XmlNametable.ENTRY.equals(str));
      handleEntry(paramEntry);
    }
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
  public final Feed parseFeedEnvelope()
    throws ParseException
  {
    // Byte code:
    //   0: aload_0
    //   1: aconst_null
    //   2: putfield 319	com/google/wireless/gdata2/parser/xml/XmlGDataParser:fields	Ljava/lang/String;
    //   5: aload_0
    //   6: getfield 48	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   9: invokeinterface 98 1 0
    //   14: istore_2
    //   15: iload_2
    //   16: ifeq +27 -> 43
    //   19: new 39	com/google/wireless/gdata2/parser/ParseException
    //   22: dup
    //   23: ldc_w 439
    //   26: invokespecial 440	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;)V
    //   29: athrow
    //   30: astore_1
    //   31: new 39	com/google/wireless/gdata2/parser/ParseException
    //   34: dup
    //   35: ldc_w 442
    //   38: aload_1
    //   39: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   42: athrow
    //   43: aload_0
    //   44: getfield 48	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   47: invokeinterface 130 1 0
    //   52: istore 5
    //   54: iload 5
    //   56: istore 6
    //   58: iload 6
    //   60: iconst_1
    //   61: if_icmpeq +210 -> 271
    //   64: iload 6
    //   66: tableswitch	default:+18 -> 84, 2:+64->130
    //   85: getfield 48	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   88: invokeinterface 130 1 0
    //   93: istore 13
    //   95: iload 13
    //   97: istore 6
    //   99: goto -41 -> 58
    //   102: astore 4
    //   104: new 39	com/google/wireless/gdata2/parser/ParseException
    //   107: dup
    //   108: ldc_w 444
    //   111: aload 4
    //   113: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   116: athrow
    //   117: astore_3
    //   118: new 39	com/google/wireless/gdata2/parser/ParseException
    //   121: dup
    //   122: ldc_w 444
    //   125: aload_3
    //   126: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   129: athrow
    //   130: aload_0
    //   131: getfield 48	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   134: invokeinterface 102 1 0
    //   139: astore 7
    //   141: getstatic 430	com/google/wireless/gdata2/parser/xml/XmlNametable:PARTIAL	Ljava/lang/String;
    //   144: aload 7
    //   146: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   149: ifeq +42 -> 191
    //   152: aload_0
    //   153: invokespecial 446	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parsePartialFeed	()Lcom/google/wireless/gdata2/data/Feed;
    //   156: astore 16
    //   158: aload 16
    //   160: areturn
    //   161: astore 15
    //   163: new 39	com/google/wireless/gdata2/parser/ParseException
    //   166: dup
    //   167: ldc_w 448
    //   170: aload 15
    //   172: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   175: athrow
    //   176: astore 14
    //   178: new 39	com/google/wireless/gdata2/parser/ParseException
    //   181: dup
    //   182: ldc_w 448
    //   185: aload 14
    //   187: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   190: athrow
    //   191: getstatic 322	com/google/wireless/gdata2/parser/xml/XmlNametable:FEED	Ljava/lang/String;
    //   194: aload 7
    //   196: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   199: ifeq -115 -> 84
    //   202: aload_0
    //   203: invokespecial 324	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parseFeed	()Lcom/google/wireless/gdata2/data/Feed;
    //   206: astore 10
    //   208: aload 10
    //   210: areturn
    //   211: astore 9
    //   213: new 39	com/google/wireless/gdata2/parser/ParseException
    //   216: dup
    //   217: ldc_w 450
    //   220: aload 9
    //   222: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   225: athrow
    //   226: astore 8
    //   228: new 39	com/google/wireless/gdata2/parser/ParseException
    //   231: dup
    //   232: ldc_w 450
    //   235: aload 8
    //   237: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   240: athrow
    //   241: astore 12
    //   243: new 39	com/google/wireless/gdata2/parser/ParseException
    //   246: dup
    //   247: ldc_w 444
    //   250: aload 12
    //   252: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   255: athrow
    //   256: astore 11
    //   258: new 39	com/google/wireless/gdata2/parser/ParseException
    //   261: dup
    //   262: ldc_w 444
    //   265: aload 11
    //   267: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   270: athrow
    //   271: new 39	com/google/wireless/gdata2/parser/ParseException
    //   274: dup
    //   275: ldc_w 452
    //   278: invokespecial 440	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;)V
    //   281: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   5	15	30	org/xmlpull/v1/XmlPullParserException
    //   43	54	102	org/xmlpull/v1/XmlPullParserException
    //   43	54	117	java/io/IOException
    //   152	158	161	org/xmlpull/v1/XmlPullParserException
    //   152	158	176	java/io/IOException
    //   202	208	211	org/xmlpull/v1/XmlPullParserException
    //   202	208	226	java/io/IOException
    //   84	95	241	org/xmlpull/v1/XmlPullParserException
    //   84	95	256	java/io/IOException
  }

  // ERROR //
  public Entry parseStandaloneEntry()
    throws ParseException, IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: aconst_null
    //   2: putfield 319	com/google/wireless/gdata2/parser/xml/XmlGDataParser:fields	Ljava/lang/String;
    //   5: aload_0
    //   6: invokevirtual 455	com/google/wireless/gdata2/parser/xml/XmlGDataParser:createEntry	()Lcom/google/wireless/gdata2/data/Entry;
    //   9: astore_1
    //   10: aload_0
    //   11: getfield 48	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   14: invokeinterface 98 1 0
    //   19: istore_3
    //   20: iload_3
    //   21: ifeq +27 -> 48
    //   24: new 39	com/google/wireless/gdata2/parser/ParseException
    //   27: dup
    //   28: ldc_w 439
    //   31: invokespecial 440	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;)V
    //   34: athrow
    //   35: astore_2
    //   36: new 39	com/google/wireless/gdata2/parser/ParseException
    //   39: dup
    //   40: ldc_w 457
    //   43: aload_2
    //   44: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   47: athrow
    //   48: aload_0
    //   49: getfield 48	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   52: invokeinterface 130 1 0
    //   57: istore 6
    //   59: iload 6
    //   61: istore 7
    //   63: iload 7
    //   65: iconst_1
    //   66: if_icmpeq +192 -> 258
    //   69: iload 7
    //   71: tableswitch	default:+17 -> 88, 2:+65->136
    //   89: getfield 48	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   92: invokeinterface 130 1 0
    //   97: istore 12
    //   99: iload 12
    //   101: istore 7
    //   103: goto -40 -> 63
    //   106: astore 5
    //   108: new 39	com/google/wireless/gdata2/parser/ParseException
    //   111: dup
    //   112: ldc_w 444
    //   115: aload 5
    //   117: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   120: athrow
    //   121: astore 4
    //   123: new 39	com/google/wireless/gdata2/parser/ParseException
    //   126: dup
    //   127: ldc_w 444
    //   130: aload 4
    //   132: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   135: athrow
    //   136: aload_0
    //   137: getfield 48	com/google/wireless/gdata2/parser/xml/XmlGDataParser:parser	Lorg/xmlpull/v1/XmlPullParser;
    //   140: invokeinterface 102 1 0
    //   145: astore 8
    //   147: getstatic 430	com/google/wireless/gdata2/parser/xml/XmlNametable:PARTIAL	Ljava/lang/String;
    //   150: aload 8
    //   152: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   155: ifeq +40 -> 195
    //   158: aload_0
    //   159: aload_1
    //   160: invokevirtual 459	com/google/wireless/gdata2/parser/xml/XmlGDataParser:handlePartialEntry	(Lcom/google/wireless/gdata2/data/Entry;)V
    //   163: aload_1
    //   164: areturn
    //   165: astore 14
    //   167: new 39	com/google/wireless/gdata2/parser/ParseException
    //   170: dup
    //   171: ldc_w 461
    //   174: aload 14
    //   176: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   179: athrow
    //   180: astore 13
    //   182: new 39	com/google/wireless/gdata2/parser/ParseException
    //   185: dup
    //   186: ldc_w 461
    //   189: aload 13
    //   191: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   194: athrow
    //   195: getstatic 309	com/google/wireless/gdata2/parser/xml/XmlNametable:ENTRY	Ljava/lang/String;
    //   198: aload 8
    //   200: invokevirtual 113	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   203: ifeq -115 -> 88
    //   206: aload_0
    //   207: aload_1
    //   208: invokevirtual 434	com/google/wireless/gdata2/parser/xml/XmlGDataParser:handleEntry	(Lcom/google/wireless/gdata2/data/Entry;)V
    //   211: aload_1
    //   212: areturn
    //   213: astore 10
    //   215: new 39	com/google/wireless/gdata2/parser/ParseException
    //   218: dup
    //   219: ldc_w 463
    //   222: aload 10
    //   224: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   227: athrow
    //   228: astore 9
    //   230: new 39	com/google/wireless/gdata2/parser/ParseException
    //   233: dup
    //   234: ldc_w 463
    //   237: aload 9
    //   239: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   242: athrow
    //   243: astore 11
    //   245: new 39	com/google/wireless/gdata2/parser/ParseException
    //   248: dup
    //   249: ldc_w 444
    //   252: aload 11
    //   254: invokespecial 74	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   257: athrow
    //   258: new 39	com/google/wireless/gdata2/parser/ParseException
    //   261: dup
    //   262: ldc_w 465
    //   265: invokespecial 440	com/google/wireless/gdata2/parser/ParseException:<init>	(Ljava/lang/String;)V
    //   268: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   10	20	35	org/xmlpull/v1/XmlPullParserException
    //   48	59	106	org/xmlpull/v1/XmlPullParserException
    //   48	59	121	java/io/IOException
    //   158	163	165	org/xmlpull/v1/XmlPullParserException
    //   158	163	180	java/io/IOException
    //   206	211	213	org/xmlpull/v1/XmlPullParserException
    //   206	211	228	java/io/IOException
    //   88	99	243	org/xmlpull/v1/XmlPullParserException
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
    if ((!XmlNametable.ENTRY.equals(str)) && (!XmlNametable.PARTIAL.equals(str)))
      throw new ParseException("Expected <entry> or <partial>: Actual element: <" + str + ">");
    if (paramEntry == null)
      paramEntry = createEntry();
    try
    {
      while (XmlNametable.ENTRY.equals(str))
      {
        handleEntry(paramEntry);
        return paramEntry;
        paramEntry.clear();
      }
      handlePartialEntry(paramEntry);
      return paramEntry;
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

  protected void skipSubTree()
    throws XmlPullParserException, IOException
  {
    int i = 1;
    while (i > 0)
      switch (this.parser.next())
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
      while (!XmlNametable.ENTRY.equals(this.parser.getName()));
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.parser.xml.XmlGDataParser
 * JD-Core Version:    0.6.2
 */