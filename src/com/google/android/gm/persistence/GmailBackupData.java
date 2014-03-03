package com.google.android.gm.persistence;

import com.google.android.gm.SharedPreference;
import com.google.android.gm.provider.Gmail.Settings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GmailBackupData
{
  private final List<SharedPreference> sharedPreferences;
  private final Map<String, Gmail.Settings> syncSettings;

  public GmailBackupData(Map<String, Gmail.Settings> paramMap, List<SharedPreference> paramList)
  {
    this.syncSettings = paramMap;
    this.sharedPreferences = paramList;
  }

  public static GmailBackupData fromJson(JSONObject paramJSONObject)
    throws JSONException
  {
    HashMap localHashMap = Maps.newHashMap();
    JSONObject localJSONObject = paramJSONObject.getJSONObject("sync_settings");
    Iterator localIterator = localJSONObject.keys();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localHashMap.put(str, Gmail.Settings.fromJson(localJSONObject.getJSONObject(str)));
    }
    ArrayList localArrayList = Lists.newArrayList();
    JSONArray localJSONArray = paramJSONObject.getJSONArray("shared_preferences");
    int i = 0;
    int j = localJSONArray.length();
    while (i < j)
    {
      localArrayList.add(SharedPreference.fromJson(localJSONArray.getJSONObject(i)));
      i++;
    }
    return new GmailBackupData(localHashMap, localArrayList);
  }

  public List<SharedPreference> getSharedPreferences()
  {
    return this.sharedPreferences;
  }

  public Map<String, Gmail.Settings> getSyncSettings()
  {
    return this.syncSettings;
  }

  public JSONObject toJson()
    throws JSONException
  {
    JSONObject localJSONObject1 = new JSONObject();
    JSONObject localJSONObject2 = new JSONObject();
    localJSONObject1.put("sync_settings", localJSONObject2);
    Iterator localIterator1 = this.syncSettings.entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      localJSONObject2.put((String)localEntry.getKey(), ((Gmail.Settings)localEntry.getValue()).toJson());
    }
    JSONArray localJSONArray = new JSONArray();
    localJSONObject1.put("shared_preferences", localJSONArray);
    Iterator localIterator2 = this.sharedPreferences.iterator();
    while (localIterator2.hasNext())
      localJSONArray.put(((SharedPreference)localIterator2.next()).toJson());
    return localJSONObject1;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.persistence.GmailBackupData
 * JD-Core Version:    0.6.2
 */