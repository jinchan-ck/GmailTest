package com.google.android.common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import com.android.common.OperationScheduler;
import com.android.common.OperationScheduler.Options;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.gsf.Gservices;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class ParentalControl
{
  private static final int APN_ALREADY_ACTIVE = 0;
  private static final int APN_REQUEST_STARTED = 1;
  private static final long DEFAULT_TIMEOUT_MILLIS = 43200000L;
  private static final String FEATURE_ENABLE_HIPRI = "enableHIPRI";
  private static final int HIPRI_ATTEMPTS = 20;
  private static final int HIPRI_ATTEMPT_MILLIS = 1000;
  private static final String KEY_ENABLED = "enabled";
  private static final String KEY_LANDING_URL = "landingUrl";
  private static final String LITMUS_URL = "http://android.clients.google.com/content/default";
  private static final String PREFS_NAME = "ParentalControl";
  private static final String TAG = "ParentalControl";
  public static final String VENDING_APP = "vending";
  public static final String YOUTUBE_APP = "youtube";

  public static Uri getLandingPage(Context paramContext, String paramString)
  {
    if (!isEnabled(paramContext, paramString))
      return null;
    String str = paramContext.getSharedPreferences("ParentalControl", 0).getString("landingUrl", "");
    if (str.length() == 0)
      return null;
    return Uri.parse(str);
  }

  public static boolean getLastCheckState(Context paramContext)
  {
    return paramContext.getSharedPreferences("ParentalControl", 0).getBoolean("enabled", false);
  }

  public static long getLastCheckTimeMillis(Context paramContext)
  {
    return new OperationScheduler(paramContext.getSharedPreferences("ParentalControl", 0)).getLastSuccessTimeMillis();
  }

  public static boolean isEnabled(Context paramContext, String paramString)
  {
    if ((Looper.myLooper() != null) && (Looper.myLooper() == Looper.getMainLooper()))
      Log.wtf("ParentalControl", "Network request on main thread");
    ContentResolver localContentResolver = paramContext.getContentResolver();
    if (!Gservices.getBoolean(localContentResolver, "parental_control_check_enabled", false))
      return false;
    if (paramString != null)
    {
      String str = Gservices.getString(localContentResolver, "parental_control_apps_list");
      if ((str != null) && (!str.contains(paramString)))
        return false;
    }
    maybeCheckState(paramContext);
    return getLastCheckState(paramContext);
  }

  private static boolean isHipriActive(ConnectivityManager paramConnectivityManager)
  {
    return paramConnectivityManager.getNetworkInfo(5).isConnected();
  }

  private static void maybeCheckState(Context paramContext)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    SharedPreferences localSharedPreferences = paramContext.getSharedPreferences("ParentalControl", 0);
    OperationScheduler localOperationScheduler = new OperationScheduler(localSharedPreferences);
    OperationScheduler.Options localOptions = new OperationScheduler.Options();
    localOptions.periodicIntervalMillis = Gservices.getLong(localContentResolver, "parental_control_timeout_in_ms", 43200000L);
    if (new File("/proc/1").lastModified() > localOperationScheduler.getLastSuccessTimeMillis())
      localOperationScheduler.setTriggerTimeMillis(0L);
    if (localOperationScheduler.getNextTimeMillis(localOptions) > System.currentTimeMillis())
      return;
    ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
    if (localConnectivityManager == null)
    {
      Log.e("ParentalControl", "Parental control unchanged: No ConnectivityManager");
      return;
    }
    GoogleHttpClient localGoogleHttpClient = new GoogleHttpClient(paramContext, "Android-PC", false);
    while (true)
    {
      Header localHeader;
      try
      {
        if (!waitForHipri(localConnectivityManager))
        {
          localOperationScheduler.onTransientError();
          return;
        }
        Uri localUri1 = Uri.parse("http://android.clients.google.com/content/default");
        InetAddress localInetAddress = InetAddress.getByName(localUri1.getHost());
        byte[] arrayOfByte = localInetAddress.getAddress();
        if (!localConnectivityManager.requestRouteToHost(5, (0xFF & arrayOfByte[3]) << 24 | (0xFF & arrayOfByte[2]) << 16 | (0xFF & arrayOfByte[1]) << 8 | 0xFF & arrayOfByte[0]))
        {
          Log.e("ParentalControl", "Parental control unchanged: Error rerouting " + localInetAddress);
          localOperationScheduler.onTransientError();
          return;
        }
        StringBuilder localStringBuilder1 = new StringBuilder().append(localUri1.getHost());
        String str1;
        if (localUri1.getPort() > 0)
        {
          str1 = ":" + localUri1.getPort();
          String str2 = str1;
          Uri localUri2 = localUri1.buildUpon().authority(str2).build();
          Log.i("ParentalControl", "Attempting litmus URL fetch: " + localUri2);
          HttpGet localHttpGet = new HttpGet(localUri2.toString());
          localHttpGet.setHeader("Connection", "close");
          localHttpResponse = localGoogleHttpClient.execute(localHttpGet);
          i = localHttpResponse.getStatusLine().getStatusCode();
          if (i != 200)
            continue;
          String str3 = Gservices.getString(localContentResolver, "parental_control_expected_response");
          if ((str3 == null) || (str3.equals(EntityUtils.toString(localHttpResponse.getEntity()).trim())))
          {
            Log.i("ParentalControl", "Parental control is OFF: Litmus fetch succeeded");
            localOperationScheduler.onSuccess();
            localSharedPreferences.edit().putBoolean("enabled", false);
          }
        }
        else
        {
          str1 = "";
          continue;
        }
        Log.i("ParentalControl", "Parental control is ON: Litmus content was modified");
        localOperationScheduler.onSuccess();
        localSharedPreferences.edit().putBoolean("enabled", true).putString("landingUrl", "").commit();
        continue;
      }
      catch (IOException localIOException)
      {
        HttpResponse localHttpResponse;
        int i;
        Log.e("ParentalControl", "Parental control unchanged: Litmus fetch failed", localIOException);
        localOperationScheduler.onTransientError();
        return;
        if (i != 302)
          continue;
        String str4 = Gservices.getString(localContentResolver, "parental_control_redirect_regex");
        localHeader = localHttpResponse.getFirstHeader("location");
        if (localHeader == null)
        {
          localObject2 = null;
          if ((str4 == null) || (localObject2 == null) || (!Pattern.matches(str4, (CharSequence)localObject2)))
            break label745;
          Log.i("ParentalControl", "Parental control is ON: Litmus redirects to " + (String)localObject2);
          localOperationScheduler.onSuccess();
          localSharedPreferences.edit().putBoolean("enabled", true).putString("landingUrl", (String)localObject2).commit();
          continue;
        }
      }
      finally
      {
        localGoogleHttpClient.close();
        localConnectivityManager.stopUsingNetworkFeature(0, "enableHIPRI");
      }
      Object localObject2 = localHeader.getValue();
      continue;
      label745: localOperationScheduler.onTransientError();
      StringBuilder localStringBuilder2 = new StringBuilder().append("Parental control unchanged: Unknown litmus redirect ");
      if (localObject2 == null)
        localObject2 = "(none)";
      Log.i("ParentalControl", (String)localObject2);
    }
  }

  private static boolean waitForHipri(ConnectivityManager paramConnectivityManager)
  {
    boolean bool = isHipriActive(paramConnectivityManager);
    int i = paramConnectivityManager.startUsingNetworkFeature(0, "enableHIPRI");
    if (bool)
      return true;
    if ((i != 0) && (i != 1))
    {
      Log.e("ParentalControl", "Parental control unchanged: Mobile network error, code " + i);
      return false;
    }
    for (int j = 0; j < 20; j++)
    {
      Log.i("ParentalControl", "Waiting 1000ms for mobile network");
      SystemClock.sleep(1000L);
      if (isHipriActive(paramConnectivityManager))
        return true;
    }
    Log.e("ParentalControl", "Parental control unchanged: Timed out waiting for mobile network");
    return false;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.ParentalControl
 * JD-Core Version:    0.6.2
 */