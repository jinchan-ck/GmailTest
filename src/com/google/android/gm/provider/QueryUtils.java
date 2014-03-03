package com.google.android.gm.provider;

import android.content.Context;
import android.content.res.Resources;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class QueryUtils
{
  public static String[] argListWithLabelNames(Context paramContext, String[] paramArrayOfString)
  {
    Resources localResources = paramContext.getResources();
    String[] arrayOfString = new String[13];
    arrayOfString[0] = localResources.getString(2131427774);
    arrayOfString[1] = localResources.getString(2131427775);
    arrayOfString[2] = localResources.getString(2131427776);
    arrayOfString[3] = localResources.getString(2131427777);
    arrayOfString[4] = localResources.getString(2131427778);
    arrayOfString[5] = localResources.getString(2131427779);
    arrayOfString[6] = localResources.getString(2131427780);
    arrayOfString[7] = localResources.getString(2131427781);
    arrayOfString[8] = localResources.getString(2131427782);
    arrayOfString[9] = localResources.getString(2131427783);
    arrayOfString[10] = localResources.getString(2131427784);
    arrayOfString[11] = localResources.getString(2131427785);
    arrayOfString[12] = localResources.getString(2131427786);
    ArrayList localArrayList = Lists.newArrayList(arrayOfString);
    localArrayList.addAll(Arrays.asList(paramArrayOfString));
    return (String[])localArrayList.toArray(new String[localArrayList.size()]);
  }

  public static String[] getQueryBindArgs(Context paramContext, String[] paramArrayOfString1, String paramString, String[] paramArrayOfString2)
  {
    int i;
    if ((paramArrayOfString1 == null) || (paramArrayOfString1.length == 0))
    {
      i = 1;
      if (i != 0)
        paramArrayOfString2 = argListWithLabelNames(paramContext, paramArrayOfString2);
      return paramArrayOfString2;
    }
    for (int j = 0; ; j++)
    {
      int k = paramArrayOfString1.length;
      i = 0;
      if (j >= k)
        break;
      if (paramString.equals(paramArrayOfString1[j]))
      {
        i = 1;
        break;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.QueryUtils
 * JD-Core Version:    0.6.2
 */