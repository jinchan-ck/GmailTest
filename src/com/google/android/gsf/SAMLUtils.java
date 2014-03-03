package com.google.android.gsf;

import android.webkit.CookieManager;

public class SAMLUtils
{
  private static final String DEFAULT_HOSTED_BASE_PATH = "https://www.google.com";
  private static final String HOSTED_PREFIX = "/a/";
  private static final String TEST_GAIA_HOSTED_BASE_PATH = "http://dasher-qa.corp.google.com";

  private static String extractCookie(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return "";
    String[] arrayOfString1 = paramString1.split("; ");
    int i = arrayOfString1.length;
    for (int j = 0; j < i; j++)
    {
      String[] arrayOfString2 = arrayOfString1[j].split("=");
      if ((arrayOfString2.length == 2) && (arrayOfString2[0].equalsIgnoreCase(paramString2)))
        return arrayOfString2[1];
    }
    return "";
  }

  public static String extractHID(CookieManager paramCookieManager, String paramString)
  {
    String str = extractCookie(paramCookieManager.getCookie(makeHIDCookieExtractionPath(false, paramString)), "HID");
    if (str.length() == 0)
      str = extractCookie(paramCookieManager.getCookie(makeLSIDCookieExtractionPath(false, paramString)), "LSID");
    return str;
  }

  private static final String getHostedBaseUrl(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramBoolean);
    for (String str = "http://dasher-qa.corp.google.com"; ; str = "https://www.google.com")
      return str + "/a/";
  }

  private static String makeHIDCookieExtractionPath(boolean paramBoolean, String paramString)
  {
    return makeHostedGaiaBasePath(paramBoolean, paramString);
  }

  private static String makeHostedGaiaBasePath(boolean paramBoolean, String paramString)
  {
    return getHostedBaseUrl(paramBoolean) + paramString + "/";
  }

  private static String makeLSIDCookieExtractionPath(boolean paramBoolean, String paramString)
  {
    if (paramBoolean);
    for (String str = "http://dasher-qa.corp.google.com"; ; str = "https://www.google.com")
      return str + "/accounts/";
  }

  public static String makeWebLoginStartUrl(boolean paramBoolean, String paramString)
  {
    return makeHostedGaiaBasePath(paramBoolean, paramString) + "ServiceLogin";
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gsf.SAMLUtils
 * JD-Core Version:    0.6.2
 */