package com.google.wireless.gdata2.client;

public abstract class QueryParams
{
  public static final String ALT_JSON = "json";
  public static final String ALT_PARAM = "alt";
  public static final String ALT_RSS = "rss";
  public static final String AUTHOR_PARAM = "author";
  public static final String FIELDS_PARAM = "fields";
  public static final String MAX_RESULTS_PARAM = "max-results";
  public static final String ORDERBY_LASTMODIFIED = "lastmodified";
  public static final String ORDERBY_PARAM = "orderby";
  public static final String PUBLISHED_MAX_PARAM = "published-max";
  public static final String PUBLISHED_MIN_PARAM = "published-min";
  public static final String QUERY_PARAM = "q";
  public static final String REQUIREALLDELETED_PARAM = "requirealldeleted";
  public static final String SHOWDELETED_PARAM = "showdeleted";
  public static final String SORTORDER_ASCENDING = "ascending";
  public static final String SORTORDER_DESCENDING = "descending";
  public static final String SORTORDER_PARAM = "sortorder";
  public static final String START_INDEX_PARAM = "start-index";
  public static final String START_MAX_PARAM = "start-max";
  public static final String START_MIN_PARAM = "start-min";
  public static final String UPDATED_MAX_PARAM = "updated-max";
  public static final String UPDATED_MIN_PARAM = "updated-min";
  private String entryId;

  public abstract void clear();

  public abstract String generateQueryUrl(String paramString);

  public String getAlt()
  {
    return getParamValue("alt");
  }

  public String getAuthor()
  {
    return getParamValue("author");
  }

  public String getEntryId()
  {
    return this.entryId;
  }

  public String getFields()
  {
    return getParamValue("fields");
  }

  public int getMaxResults()
  {
    return Integer.parseInt(getParamValue("max-results"));
  }

  public abstract String getParamValue(String paramString);

  public String getPublishedMax()
  {
    return getParamValue("published-max");
  }

  public String getPublishedMin()
  {
    return getParamValue("published-min");
  }

  public String getQuery()
  {
    return getParamValue("q");
  }

  public int getStartIndex()
  {
    return Integer.parseInt(getParamValue("start-index"));
  }

  public String getUpdatedMax()
  {
    return getParamValue("updated-max");
  }

  public String getUpdatedMin()
  {
    return getParamValue("updated-min");
  }

  public void setAlt(String paramString)
  {
    setParamValue("alt", paramString);
  }

  public void setAuthor(String paramString)
  {
    setParamValue("author", paramString);
  }

  public void setEntryId(String paramString)
  {
    this.entryId = paramString;
  }

  public void setFields(String paramString)
  {
    setParamValue("fields", paramString);
  }

  public void setMaxResults(int paramInt)
  {
    setParamValue("max-results", String.valueOf(paramInt));
  }

  public abstract void setParamValue(String paramString1, String paramString2);

  public void setPublishedMax(String paramString)
  {
    setParamValue("published-max", paramString);
  }

  public void setPublishedMin(String paramString)
  {
    setParamValue("published-min", paramString);
  }

  public void setQuery(String paramString)
  {
    setParamValue("q", paramString);
  }

  public void setStartIndex(int paramInt)
  {
    setParamValue("start-index", String.valueOf(paramInt));
  }

  public void setUpdatedMax(String paramString)
  {
    setParamValue("updated-max", paramString);
  }

  public void setUpdatedMin(String paramString)
  {
    setParamValue("updated-min", paramString);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.client.QueryParams
 * JD-Core Version:    0.6.2
 */