package com.google.android.gm;

import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SharedPreference
  implements Serializable
{
  static final long serialVersionUID = 1L;
  private String mAccount;
  private String mKey;
  private Object mValue;

  public SharedPreference(String paramString1, String paramString2, Object paramObject)
  {
    this.mKey = paramString1;
    this.mAccount = paramString2;
    this.mValue = paramObject;
  }

  public static SharedPreference fromJson(JSONObject paramJSONObject)
    throws JSONException
  {
    Object localObject = paramJSONObject.get("value");
    if ((localObject instanceof JSONArray))
    {
      HashSet localHashSet = Sets.newHashSet();
      JSONArray localJSONArray = (JSONArray)localObject;
      int i = 0;
      int j = localJSONArray.length();
      while (i < j)
      {
        localHashSet.add(localJSONArray.get(i));
        i++;
      }
      localObject = localHashSet;
    }
    if (paramJSONObject.has("account"));
    for (String str = paramJSONObject.getString("account"); ; str = null)
      return new SharedPreference(paramJSONObject.getString("key"), str, localObject);
  }

  public String getAccount()
  {
    return this.mAccount;
  }

  public String getKey()
  {
    return this.mKey;
  }

  public Object getValue()
  {
    return this.mValue;
  }

  public JSONObject toJson()
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("account", this.mAccount);
    localJSONObject.put("key", this.mKey);
    if ((this.mValue instanceof Set))
    {
      Set localSet = (Set)this.mValue;
      JSONArray localJSONArray = new JSONArray();
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext())
        localJSONArray.put(localIterator.next());
      localJSONObject.put("value", localJSONArray);
      return localJSONObject;
    }
    localJSONObject.put("value", this.mValue);
    return localJSONObject;
  }

  public String toString()
  {
    return "BackupPreference{mAccount='" + this.mAccount + '\'' + ", mKey='" + this.mKey + '\'' + ", mValue=" + this.mValue + '}';
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.SharedPreference
 * JD-Core Version:    0.6.2
 */