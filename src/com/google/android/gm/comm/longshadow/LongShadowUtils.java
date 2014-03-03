package com.google.android.gm.comm.longshadow;

import android.content.ContentResolver;
import android.content.Context;
import android.text.TextUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Label;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LongShadowUtils
{
  private static Gmail sGmail;

  public static Gmail getContentProviderMailAccess(ContentResolver paramContentResolver)
  {
    try
    {
      if (sGmail == null)
        sGmail = new Gmail(paramContentResolver);
      Gmail localGmail = sGmail;
      return localGmail;
    }
    finally
    {
    }
  }

  public static CharSequence getDisplayableLabel(Context paramContext, Label paramLabel)
  {
    String str = paramLabel.getCanonicalName();
    if ((paramLabel.getHidden()) || ((!isUserLabel(str)) && (!Gmail.isDisplayableSystemLabel(str))))
      return null;
    return paramLabel.getName();
  }

  public static Map<String, Label> getDisplayableLabels(Context paramContext, Map<String, Label> paramMap)
  {
    HashMap localHashMap = Maps.newHashMap();
    ArrayList localArrayList = Lists.newArrayList();
    localArrayList.addAll(paramMap.values());
    int i = localArrayList.size();
    for (int j = 0; j < i; j++)
    {
      Label localLabel = (Label)localArrayList.get(j);
      if (getDisplayableLabel(paramContext, localLabel) != null)
        localHashMap.put(localLabel.getCanonicalName(), localLabel);
    }
    return localHashMap;
  }

  public static boolean isUserLabel(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    while (paramString.charAt(0) == '^')
      return false;
    return true;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.comm.longshadow.LongShadowUtils
 * JD-Core Version:    0.6.2
 */