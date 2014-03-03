package com.google.android.gsf;

import android.content.Intent;

public class GoogleLoginServiceConstants
{
  public static final Intent SERVICE_INTENT = new Intent("com.google.android.gsf.action.GET_GLS");

  public static String featureForService(String paramString)
  {
    return "service_" + paramString;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gsf.GoogleLoginServiceConstants
 * JD-Core Version:    0.6.2
 */