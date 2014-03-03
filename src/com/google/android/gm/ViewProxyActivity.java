package com.google.android.gm;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.android.mail.providers.Account;
import com.android.mail.utils.LogTag;
import com.google.android.gm.provider.LogUtils;
import com.google.android.gsf.Gservices;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewProxyActivity extends Activity
{
  public void addUriAccountIntentExtras(Uri paramUri, Account paramAccount, Intent paramIntent)
  {
    String str1 = paramUri.getHost();
    Pattern localPattern = Pattern.compile(Gservices.getString(getContentResolver(), "gmail_account_extras_uri_host_pattern", ".*\\.google(\\.co(m?))?(\\.\\w{2})?"));
    if ((str1 != null) && (localPattern.matcher(str1).matches()));
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      String str2 = Long.toString(new Random().nextLong());
      localMessageDigest.update((str2 + paramAccount.name + "com.google").getBytes());
      paramIntent.putExtra("salt", str2);
      paramIntent.putExtra("digest", localMessageDigest.digest());
      return;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      LogUtils.e(LogTag.getLogTag(), localNoSuchAlgorithmException, "Unable to load MD5 digest instance", new Object[0]);
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Uri localUri = (Uri)getIntent().getParcelableExtra("original_uri");
    Account localAccount = (Account)getIntent().getParcelableExtra("account");
    Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
    localIntent.setFlags(524288);
    localIntent.putExtra("com.android.browser.application_id", getPackageName());
    addUriAccountIntentExtras(localUri, localAccount, localIntent);
    try
    {
      startActivity(localIntent);
      label77: finish();
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      break label77;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ViewProxyActivity
 * JD-Core Version:    0.6.2
 */