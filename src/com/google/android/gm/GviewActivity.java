package com.google.android.gm;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gm.provider.Urls;
import com.google.android.gsf.Gservices;

public class GviewActivity extends GmailBaseActivity
{
  private GviewAsyncTask mGviewAsyncTask;
  private boolean mProgressDialogCreated;
  private WebView mWebView;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(2130968639);
    this.mWebView = ((WebView)findViewById(2131689650));
    WebSettings localWebSettings = this.mWebView.getSettings();
    localWebSettings.setSavePassword(false);
    localWebSettings.setSaveFormData(false);
    localWebSettings.setJavaScriptEnabled(true);
    localWebSettings.setSupportZoom(true);
    localWebSettings.setBlockNetworkImage(false);
    this.mWebView.setScrollBarStyle(0);
    this.mWebView.setWebViewClient(new WebViewClient()
    {
      public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
      {
        return false;
      }
    });
    Uri localUri = getIntent().getData();
    String str1 = localUri.getQueryParameter("account");
    String str2 = localUri.getQueryParameter("serverMessageId");
    String str3 = localUri.getQueryParameter("attId");
    String str4 = localUri.getQueryParameter("mimeType");
    String str5 = String.format(Gservices.getString(getContentResolver(), "gmail_gview_url", "https://docs.google.com/viewer?embedded=true&a=v&pid=gmail&user=%s&thid=%s&attid=%s&mt=%s"), new Object[] { str1, str2, str3, str4 });
    showDialog(1);
    this.mGviewAsyncTask = new GviewAsyncTask(this, str1, this.mWebView, str5);
    this.mGviewAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
  }

  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle)
  {
    if (paramInt == 1)
    {
      ProgressDialog localProgressDialog = new ProgressDialog(this);
      localProgressDialog.setTitle(2131427453);
      localProgressDialog.setMessage(getResources().getString(2131427454));
      localProgressDialog.setIndeterminate(true);
      localProgressDialog.setCancelable(true);
      localProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
      {
        public void onDismiss(DialogInterface paramAnonymousDialogInterface)
        {
          if (GviewActivity.this.mGviewAsyncTask.isLoading())
          {
            GviewActivity.this.mGviewAsyncTask.cancel(true);
            GviewActivity.this.finish();
          }
        }
      });
      this.mProgressDialogCreated = true;
      return localProgressDialog;
    }
    if (paramInt == 2)
      return new AlertDialog.Builder(this).setPositiveButton(2131427539, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (GviewActivity.this.mProgressDialogCreated)
            GviewActivity.this.dismissDialog(1);
          GviewActivity.this.finish();
        }
      }).setMessage(getResources().getString(2131427770)).create();
    throw new AssertionError("Invalid dialog ID in GviewActivity: " + paramInt);
  }

  protected void onDestroy()
  {
    this.mWebView.destroy();
    super.onDestroy();
  }

  private class GviewAsyncTask extends AsyncTask<Void, Void, String>
  {
    private final String mAccount;
    private final Activity mActivity;
    private boolean mLoading;
    private final String mUrl;
    private final WebView mWebView;

    GviewAsyncTask(Activity paramString1, String paramWebView, WebView paramString2, String arg5)
    {
      this.mActivity = paramString1;
      this.mAccount = paramWebView;
      this.mWebView = paramString2;
      Object localObject;
      this.mUrl = localObject;
      this.mLoading = true;
    }

    // ERROR //
    protected String doInBackground(Void[] paramArrayOfVoid)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_2
      //   2: aconst_null
      //   3: astore_3
      //   4: new 48	android/accounts/Account
      //   7: dup
      //   8: aload_0
      //   9: getfield 27	com/google/android/gm/GviewActivity$GviewAsyncTask:mAccount	Ljava/lang/String;
      //   12: ldc 50
      //   14: invokespecial 53	android/accounts/Account:<init>	(Ljava/lang/String;Ljava/lang/String;)V
      //   17: astore 4
      //   19: aload_0
      //   20: getfield 25	com/google/android/gm/GviewActivity$GviewAsyncTask:mActivity	Landroid/app/Activity;
      //   23: invokestatic 59	android/accounts/AccountManager:get	(Landroid/content/Context;)Landroid/accounts/AccountManager;
      //   26: aload 4
      //   28: ldc 61
      //   30: iconst_0
      //   31: invokevirtual 65	android/accounts/AccountManager:blockingGetAuthToken	(Landroid/accounts/Account;Ljava/lang/String;Z)Ljava/lang/String;
      //   34: astore 23
      //   36: aload_0
      //   37: getfield 25	com/google/android/gm/GviewActivity$GviewAsyncTask:mActivity	Landroid/app/Activity;
      //   40: invokestatic 59	android/accounts/AccountManager:get	(Landroid/content/Context;)Landroid/accounts/AccountManager;
      //   43: aload 4
      //   45: ldc 67
      //   47: iconst_0
      //   48: invokevirtual 65	android/accounts/AccountManager:blockingGetAuthToken	(Landroid/accounts/Account;Ljava/lang/String;Z)Ljava/lang/String;
      //   51: astore 24
      //   53: new 69	com/google/android/common/http/GoogleHttpClient
      //   56: dup
      //   57: aload_0
      //   58: getfield 25	com/google/android/gm/GviewActivity$GviewAsyncTask:mActivity	Landroid/app/Activity;
      //   61: ldc 71
      //   63: iconst_1
      //   64: invokespecial 74	com/google/android/common/http/GoogleHttpClient:<init>	(Landroid/content/Context;Ljava/lang/String;Z)V
      //   67: new 76	org/apache/http/client/methods/HttpPost
      //   70: dup
      //   71: ldc 78
      //   73: iconst_4
      //   74: anewarray 80	java/lang/String
      //   77: dup
      //   78: iconst_0
      //   79: ldc 61
      //   81: aastore
      //   82: dup
      //   83: iconst_1
      //   84: aload 23
      //   86: aastore
      //   87: dup
      //   88: iconst_2
      //   89: ldc 67
      //   91: aastore
      //   92: dup
      //   93: iconst_3
      //   94: aload 24
      //   96: aastore
      //   97: invokestatic 86	com/google/android/gm/provider/Urls:buildUri	(Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;
      //   100: invokespecial 89	org/apache/http/client/methods/HttpPost:<init>	(Ljava/lang/String;)V
      //   103: invokeinterface 95 2 0
      //   108: invokeinterface 101 1 0
      //   113: astore_2
      //   114: new 103	java/io/ByteArrayOutputStream
      //   117: dup
      //   118: aload_2
      //   119: invokeinterface 109 1 0
      //   124: l2i
      //   125: invokespecial 112	java/io/ByteArrayOutputStream:<init>	(I)V
      //   128: astore 25
      //   130: aload_2
      //   131: aload 25
      //   133: invokeinterface 116 2 0
      //   138: aload 25
      //   140: ldc 118
      //   142: invokevirtual 122	java/io/ByteArrayOutputStream:toString	(Ljava/lang/String;)Ljava/lang/String;
      //   145: invokevirtual 126	java/lang/String:trim	()Ljava/lang/String;
      //   148: astore 26
      //   150: aload_2
      //   151: ifnull +9 -> 160
      //   154: aload_2
      //   155: invokeinterface 129 1 0
      //   160: aload 25
      //   162: ifnull +8 -> 170
      //   165: aload 25
      //   167: invokevirtual 132	java/io/ByteArrayOutputStream:close	()V
      //   170: aload 26
      //   172: areturn
      //   173: astore 18
      //   175: iconst_1
      //   176: anewarray 134	java/lang/Object
      //   179: astore 19
      //   181: aload 19
      //   183: iconst_0
      //   184: aload 18
      //   186: invokevirtual 136	android/accounts/AuthenticatorException:toString	()Ljava/lang/String;
      //   189: aastore
      //   190: ldc 138
      //   192: ldc 140
      //   194: aload 19
      //   196: invokestatic 146	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
      //   199: pop
      //   200: aload_2
      //   201: ifnull +9 -> 210
      //   204: aload_2
      //   205: invokeinterface 129 1 0
      //   210: aload_3
      //   211: ifnull +7 -> 218
      //   214: aload_3
      //   215: invokevirtual 132	java/io/ByteArrayOutputStream:close	()V
      //   218: aconst_null
      //   219: areturn
      //   220: astore 13
      //   222: iconst_1
      //   223: anewarray 134	java/lang/Object
      //   226: astore 14
      //   228: aload 14
      //   230: iconst_0
      //   231: aload 13
      //   233: invokevirtual 147	java/io/IOException:toString	()Ljava/lang/String;
      //   236: aastore
      //   237: ldc 138
      //   239: ldc 140
      //   241: aload 14
      //   243: invokestatic 146	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
      //   246: pop
      //   247: aload_2
      //   248: ifnull +9 -> 257
      //   251: aload_2
      //   252: invokeinterface 129 1 0
      //   257: aload_3
      //   258: ifnull -40 -> 218
      //   261: aload_3
      //   262: invokevirtual 132	java/io/ByteArrayOutputStream:close	()V
      //   265: goto -47 -> 218
      //   268: astore 16
      //   270: goto -52 -> 218
      //   273: astore 8
      //   275: iconst_1
      //   276: anewarray 134	java/lang/Object
      //   279: astore 9
      //   281: aload 9
      //   283: iconst_0
      //   284: aload 8
      //   286: invokevirtual 148	android/accounts/OperationCanceledException:toString	()Ljava/lang/String;
      //   289: aastore
      //   290: ldc 138
      //   292: ldc 140
      //   294: aload 9
      //   296: invokestatic 146	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
      //   299: pop
      //   300: aload_2
      //   301: ifnull +9 -> 310
      //   304: aload_2
      //   305: invokeinterface 129 1 0
      //   310: aload_3
      //   311: ifnull -93 -> 218
      //   314: aload_3
      //   315: invokevirtual 132	java/io/ByteArrayOutputStream:close	()V
      //   318: goto -100 -> 218
      //   321: astore 11
      //   323: goto -105 -> 218
      //   326: astore 5
      //   328: aload_2
      //   329: ifnull +9 -> 338
      //   332: aload_2
      //   333: invokeinterface 129 1 0
      //   338: aload_3
      //   339: ifnull +7 -> 346
      //   342: aload_3
      //   343: invokevirtual 132	java/io/ByteArrayOutputStream:close	()V
      //   346: aload 5
      //   348: athrow
      //   349: astore 28
      //   351: goto -191 -> 160
      //   354: astore 27
      //   356: goto -186 -> 170
      //   359: astore 22
      //   361: goto -151 -> 210
      //   364: astore 21
      //   366: goto -148 -> 218
      //   369: astore 17
      //   371: goto -114 -> 257
      //   374: astore 12
      //   376: goto -66 -> 310
      //   379: astore 7
      //   381: goto -43 -> 338
      //   384: astore 6
      //   386: goto -40 -> 346
      //   389: astore 5
      //   391: aload 25
      //   393: astore_3
      //   394: goto -66 -> 328
      //   397: astore 8
      //   399: aload 25
      //   401: astore_3
      //   402: goto -127 -> 275
      //   405: astore 13
      //   407: aload 25
      //   409: astore_3
      //   410: goto -188 -> 222
      //   413: astore 18
      //   415: aload 25
      //   417: astore_3
      //   418: goto -243 -> 175
      //
      // Exception table:
      //   from	to	target	type
      //   4	130	173	android/accounts/AuthenticatorException
      //   4	130	220	java/io/IOException
      //   261	265	268	java/io/IOException
      //   4	130	273	android/accounts/OperationCanceledException
      //   314	318	321	java/io/IOException
      //   4	130	326	finally
      //   175	200	326	finally
      //   222	247	326	finally
      //   275	300	326	finally
      //   154	160	349	java/io/IOException
      //   165	170	354	java/io/IOException
      //   204	210	359	java/io/IOException
      //   214	218	364	java/io/IOException
      //   251	257	369	java/io/IOException
      //   304	310	374	java/io/IOException
      //   332	338	379	java/io/IOException
      //   342	346	384	java/io/IOException
      //   130	150	389	finally
      //   130	150	397	android/accounts/OperationCanceledException
      //   130	150	405	java/io/IOException
      //   130	150	413	android/accounts/AuthenticatorException
    }

    public boolean isLoading()
    {
      return this.mLoading;
    }

    protected void onPostExecute(String paramString)
    {
      if (this.mActivity.isFinishing())
        return;
      this.mLoading = false;
      if (GviewActivity.this.mProgressDialogCreated)
        this.mActivity.dismissDialog(1);
      if (paramString != null)
      {
        WebView localWebView = this.mWebView;
        String[] arrayOfString = new String[4];
        arrayOfString[0] = "auth";
        arrayOfString[1] = paramString;
        arrayOfString[2] = "continue";
        arrayOfString[3] = this.mUrl;
        localWebView.loadUrl(Urls.buildUri("https://www.google.com/accounts/TokenAuth?service=writely&source=gmail", arrayOfString));
        return;
      }
      this.mActivity.showDialog(2);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.GviewActivity
 * JD-Core Version:    0.6.2
 */