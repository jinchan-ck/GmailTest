package com.google.wireless.gdata.client;

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
    char c;
    int i;
    label30: String str1;
    String str2;
    if (paramString.indexOf('?') >= 0)
    {
      c = '&';
      localStringBuffer.append(c);
      i = 0;
      if (i >= this.names.size())
        break label133;
      if (i > 0)
        localStringBuffer.append('&');
      str1 = (String)this.names.elementAt(i);
      str2 = getParamValue(str1);
      if (str2 != null)
        break label93;
    }
    while (true)
    {
      i++;
      break label30;
      c = '?';
      break;
      label93: localStringBuffer.append(this.client.encodeUri(str1)).append('=');
      localStringBuffer.append(this.client.encodeUri(str2));
    }
    label133: return localStringBuffer.toString();
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
 * Qualified Name:     com.google.wireless.gdata.client.HttpQueryParams
 * JD-Core Version:    0.6.2
 */