package com.google.android.common.gdata;

import android.text.TextUtils;
import android.util.Log;
import com.google.wireless.gdata.client.QueryParams;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class QueryParamsImpl extends QueryParams
{
  private final Map<String, String> mParams = new HashMap();

  public void clear()
  {
    setEntryId(null);
    this.mParams.clear();
  }

  public String generateQueryUrl(String paramString)
  {
    if ((TextUtils.isEmpty(getEntryId())) && (this.mParams.isEmpty()))
      return paramString;
    if (!TextUtils.isEmpty(getEntryId()))
    {
      if (!this.mParams.isEmpty())
        throw new IllegalStateException("Cannot set both an entry ID and other query paramters.");
      return paramString + '/' + getEntryId();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    Set localSet = this.mParams.keySet();
    int i = 1;
    if (paramString.contains("?"))
      i = 0;
    while (true)
    {
      Iterator localIterator = localSet.iterator();
      while (true)
        if (localIterator.hasNext())
        {
          String str1 = (String)localIterator.next();
          String str2 = (String)this.mParams.get(str1);
          if (str2 != null)
          {
            if (i != 0)
            {
              i = 0;
              localStringBuilder.append(str1);
              localStringBuilder.append('=');
            }
            try
            {
              String str4 = URLEncoder.encode(str2, "UTF-8");
              str3 = str4;
              localStringBuilder.append(str3);
              continue;
              localStringBuilder.append('?');
              break;
              localStringBuilder.append('&');
            }
            catch (UnsupportedEncodingException localUnsupportedEncodingException)
            {
              while (true)
              {
                Log.w("QueryParamsImpl", "UTF-8 not supported -- should not happen.  Using default encoding.", localUnsupportedEncodingException);
                String str3 = URLEncoder.encode(str2);
              }
            }
          }
        }
    }
    return localStringBuilder.toString();
  }

  public String getParamValue(String paramString)
  {
    if (!this.mParams.containsKey(paramString))
      return null;
    return (String)this.mParams.get(paramString);
  }

  public void setParamValue(String paramString1, String paramString2)
  {
    this.mParams.put(paramString1, paramString2);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.gdata.QueryParamsImpl
 * JD-Core Version:    0.6.2
 */