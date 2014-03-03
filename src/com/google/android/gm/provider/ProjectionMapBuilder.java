package com.google.android.gm.provider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ProjectionMapBuilder
{
  private Map<String, String> mMap = new HashMap();

  public ProjectionMapBuilder add(String paramString)
  {
    this.mMap.put(paramString, paramString);
    return this;
  }

  public ProjectionMapBuilder add(String paramString1, String paramString2)
  {
    this.mMap.put(paramString1, paramString2 + " AS " + paramString1);
    return this;
  }

  public ProjectionMapBuilder addIdentities(String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
      add(paramArrayOfString[j]);
    return this;
  }

  public ProjectionMapBuilder addTransformations(String[][] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    int j = 0;
    if (j < i)
    {
      String[] arrayOfString = paramArrayOfString[j];
      switch (arrayOfString.length)
      {
      default:
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Arrays.toString(arrayOfString);
        LogUtils.i("Gmail", "unrecognized projection map entry: %s", arrayOfObject);
      case 2:
      case 1:
      }
      while (true)
      {
        j++;
        break;
        add(arrayOfString[0], arrayOfString[1]);
        continue;
        add(arrayOfString[0]);
      }
    }
    return this;
  }

  public Map<String, String> getMap()
  {
    return this.mMap;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.ProjectionMapBuilder
 * JD-Core Version:    0.6.2
 */