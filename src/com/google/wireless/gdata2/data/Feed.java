package com.google.wireless.gdata2.data;

public class Feed
{
  private String category;
  private String categoryScheme;
  private String eTagValue;
  private String id;
  private int itemsPerPage;
  private String lastUpdated;
  private int startIndex;
  private String title;
  private int totalResults;

  public String getCategory()
  {
    return this.category;
  }

  public String getCategoryScheme()
  {
    return this.categoryScheme;
  }

  public String getETag()
  {
    return this.eTagValue;
  }

  public String getId()
  {
    return this.id;
  }

  public int getItemsPerPage()
  {
    return this.itemsPerPage;
  }

  public String getLastUpdated()
  {
    return this.lastUpdated;
  }

  public int getStartIndex()
  {
    return this.startIndex;
  }

  public String getTitle()
  {
    return this.title;
  }

  public int getTotalResults()
  {
    return this.totalResults;
  }

  public void setCategory(String paramString)
  {
    this.category = paramString;
  }

  public void setCategoryScheme(String paramString)
  {
    this.categoryScheme = paramString;
  }

  public void setETag(String paramString)
  {
    this.eTagValue = paramString;
  }

  public void setId(String paramString)
  {
    this.id = paramString;
  }

  public void setItemsPerPage(int paramInt)
  {
    this.itemsPerPage = paramInt;
  }

  public void setLastUpdated(String paramString)
  {
    this.lastUpdated = paramString;
  }

  public void setStartIndex(int paramInt)
  {
    this.startIndex = paramInt;
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
  }

  public void setTotalResults(int paramInt)
  {
    this.totalResults = paramInt;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.data.Feed
 * JD-Core Version:    0.6.2
 */