package com.google.android.common;

public class GoogleTrafficStats
{
  public static int getDomainType(String paramString)
  {
    if (paramString.contains("google.com"))
      return 805306368;
    if ((paramString.contains("gmail.com")) || (paramString.contains("googlemail.com")))
      return 268435456;
    return 536870912;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.GoogleTrafficStats
 * JD-Core Version:    0.6.2
 */