package com.google.android.gsf;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class UseLocationForServices
{
  public static final String ACTION_SET_USE_LOCATION_FOR_SERVICES = "com.google.android.gsf.action.SET_USE_LOCATION_FOR_SERVICES";
  private static final String[] GOOGLE_GEOLOCATION_ORIGINS = { "http://www.google.com", "http://www.google.co.uk" };
  private static final String TAG = "UseLocationForServices";
  public static final int USE_LOCATION_FOR_SERVICES_NOT_SET = 2;
  public static final int USE_LOCATION_FOR_SERVICES_OFF = 0;
  public static final int USE_LOCATION_FOR_SERVICES_ON = 1;

  private static String addGoogleOrigins(String paramString)
  {
    HashSet localHashSet = parseAllowGeolocationOrigins(paramString);
    String[] arrayOfString = GOOGLE_GEOLOCATION_ORIGINS;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
      localHashSet.add(arrayOfString[j]);
    return formatAllowGeolocationOrigins(localHashSet);
  }

  private static String formatAllowGeolocationOrigins(Collection<String> paramCollection)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (localStringBuilder.length() > 0)
        localStringBuilder.append(' ');
      localStringBuilder.append(str);
    }
    return localStringBuilder.toString();
  }

  public static int getUseLocationForServices(Context paramContext)
  {
    return GoogleSettingsContract.Partner.getInt(paramContext.getContentResolver(), "use_location_for_services", 2);
  }

  private static HashSet<String> parseAllowGeolocationOrigins(String paramString)
  {
    HashSet localHashSet = new HashSet();
    if (!TextUtils.isEmpty(paramString))
      for (String str : paramString.split("\\s+"))
        if (!TextUtils.isEmpty(str))
          localHashSet.add(str);
    return localHashSet;
  }

  private static String removeGoogleOrigins(String paramString)
  {
    HashSet localHashSet = parseAllowGeolocationOrigins(paramString);
    String[] arrayOfString = GOOGLE_GEOLOCATION_ORIGINS;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
      localHashSet.remove(arrayOfString[j]);
    return formatAllowGeolocationOrigins(localHashSet);
  }

  private static void setGoogleBrowserGeolocation(Context paramContext, boolean paramBoolean)
  {
    try
    {
      ContentResolver localContentResolver = paramContext.getContentResolver();
      String str1 = Settings.Secure.getString(localContentResolver, "allowed_geolocation_origins");
      if (paramBoolean);
      String str2;
      for (Object localObject = addGoogleOrigins(str1); ; localObject = str2)
      {
        Settings.Secure.putString(localContentResolver, "allowed_geolocation_origins", (String)localObject);
        return;
        str2 = removeGoogleOrigins(str1);
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.e("UseLocationForServices", "Failed to set browser geolocation permissions: " + localRuntimeException);
    }
  }

  public static boolean setUseLocationForServices(Context paramContext, boolean paramBoolean)
  {
    setGoogleBrowserGeolocation(paramContext, paramBoolean);
    ContentResolver localContentResolver = paramContext.getContentResolver();
    if (paramBoolean);
    for (int i = 1; ; i = 0)
      return GoogleSettingsContract.Partner.putInt(localContentResolver, "use_location_for_services", i);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gsf.UseLocationForServices
 * JD-Core Version:    0.6.2
 */