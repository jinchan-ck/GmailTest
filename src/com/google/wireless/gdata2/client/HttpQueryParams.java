package com.google.wireless.gdata2.client;

import java.util.Hashtable;
import java.util.Vector;

public class HttpQueryParams extends QueryParams
{
  private GDataClient client;
  private Vector names;
  private Hashtable params;

  public HttpQueryParams(GDataClient paramGDataClient)
  {
    this.client = paramGDataClient;
    this.names = new Vector(4);
    this.params = new Hashtable(7);
  }

  public void clear()
  {
    this.names.removeAllElements();
    this.params.clear();
  }

  public String generateQueryUrl(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramString);
    if (paramString.indexOf('?') >= 0);
    for (char c = '&'; ; c = '?')
    {
      localStringBuffer.append(c);
      for (int i = 0; i < this.names.size(); i++)
      {
        if (i > 0)
          localStringBuffer.append('&');
        String str = (String)this.names.elementAt(i);
        localStringBuffer.append(this.client.encodeUri(str)).append('=');
        localStringBuffer.append(this.client.encodeUri(getParamValue(str)));
      }
    }
    return localStringBuffer.toString();
  }

  public String getParamValue(String paramString)
  {
    return (String)this.params.get(paramString);
  }

  public void setParamValue(String paramString1, String paramString2)
  {
    if (paramString2 != null)
    {
      if (!this.params.containsKey(paramString1))
        this.names.addElement(paramString1);
      this.params.put(paramString1, paramString2);
    }
    while (this.params.remove(paramString1) == null)
      return;
    this.names.removeElement(paramString1);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.client.HttpQueryParams
 * JD-Core Version:    0.6.2
 */