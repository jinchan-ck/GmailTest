package com.google.android.common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.http.SslError;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gsf.Gservices;
import java.util.Locale;

public class GoogleWebContentHelper
{
  private Context mContext;
  private final float mDipScale;
  private FrameLayout mLayout;
  private String mLoadingMessage = null;
  private String mPrettyUrl;
  private View mProgressBar;
  private boolean mReceivedResponse;
  private String mSecureUrl;
  private TextView mTextView;
  private String mUnsuccessfulMessage;
  private WebView mWebView;

  public GoogleWebContentHelper(Context paramContext)
  {
    this.mContext = paramContext;
    this.mDipScale = this.mContext.getResources().getDisplayMetrics().density;
  }

  private void ensureViews()
  {
    try
    {
      if (this.mLayout == null)
        initializeViews();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private static String fillUrl(String paramString, Context paramContext)
  {
    if (TextUtils.isEmpty(paramString))
      return "";
    Locale localLocale = Locale.getDefault();
    return String.format(paramString, new Object[] { localLocale.getLanguage() + "_" + localLocale.getCountry().toLowerCase() });
  }

  private void handleWebViewCompletion(boolean paramBoolean)
  {
    while (true)
    {
      try
      {
        boolean bool = this.mReceivedResponse;
        if (bool)
          return;
        this.mReceivedResponse = true;
        ((ViewGroup)this.mProgressBar.getParent()).removeView(this.mProgressBar);
        if (paramBoolean)
        {
          localObject2 = this.mTextView;
          ((ViewGroup)((View)localObject2).getParent()).removeView((View)localObject2);
          if (!paramBoolean)
            break label92;
          localObject3 = this.mWebView;
          ((View)localObject3).setVisibility(0);
          continue;
        }
      }
      finally
      {
      }
      Object localObject2 = this.mWebView;
      continue;
      label92: TextView localTextView = this.mTextView;
      Object localObject3 = localTextView;
    }
  }

  private void initializeViews()
  {
    this.mLayout = new FrameLayout(this.mContext);
    this.mLayout.setForegroundGravity(17);
    int i = (int)(0.5F + 8.0F * this.mDipScale);
    int j = (int)(0.5F + 10.0F * this.mDipScale);
    int k = (int)(0.5F + 12.0F * this.mDipScale);
    LinearLayout localLinearLayout;
    if (this.mLoadingMessage != null)
    {
      FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(-2, -2);
      localLayoutParams1.gravity = 17;
      localLinearLayout = new LinearLayout(this.mContext);
      localLinearLayout.setBaselineAligned(false);
      localLinearLayout.setPadding(i, j, i, j);
      this.mLayout.addView(localLinearLayout, localLayoutParams1);
      LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams2.rightMargin = k;
      ProgressBar localProgressBar1 = new ProgressBar(this.mContext);
      localProgressBar1.setIndeterminate(true);
      localLinearLayout.addView(localProgressBar1, localLayoutParams2);
      LinearLayout.LayoutParams localLayoutParams3 = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams3.gravity = 16;
      TextView localTextView = new TextView(this.mContext);
      localTextView.setText(this.mLoadingMessage);
      localLinearLayout.addView(localTextView, localLayoutParams3);
    }
    ProgressBar localProgressBar2;
    for (this.mProgressBar = localLinearLayout; ; this.mProgressBar = localProgressBar2)
    {
      FrameLayout.LayoutParams localLayoutParams4 = new FrameLayout.LayoutParams(-1, -1);
      this.mWebView = new WebView(this.mContext);
      this.mWebView.setVisibility(4);
      this.mWebView.getSettings().setCacheMode(2);
      this.mWebView.setWebViewClient(new MyWebViewClient(null));
      this.mLayout.addView(this.mWebView, localLayoutParams4);
      FrameLayout.LayoutParams localLayoutParams5 = new FrameLayout.LayoutParams(-2, -2);
      localLayoutParams5.gravity = 17;
      this.mTextView = new TextView(this.mContext);
      this.mTextView.setVisibility(8);
      this.mTextView.setPadding(j, j, j, j);
      this.mTextView.setText(this.mUnsuccessfulMessage);
      this.mLayout.addView(this.mTextView, localLayoutParams5);
      return;
      FrameLayout.LayoutParams localLayoutParams6 = new FrameLayout.LayoutParams(-2, -2);
      localLayoutParams6.gravity = 17;
      localProgressBar2 = new ProgressBar(this.mContext);
      localProgressBar2.setIndeterminate(true);
      this.mLayout.addView(localProgressBar2, localLayoutParams6);
    }
  }

  public ViewGroup getLayout()
  {
    ensureViews();
    return this.mLayout;
  }

  public boolean handleKey(KeyEvent paramKeyEvent)
  {
    if ((paramKeyEvent.getKeyCode() == 4) && (paramKeyEvent.getAction() == 0) && (this.mWebView.canGoBack()))
    {
      this.mWebView.goBack();
      return true;
    }
    return false;
  }

  public GoogleWebContentHelper loadDataWithFailUrl(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    ensureViews();
    this.mWebView.loadDataWithBaseURL(paramString1, paramString2, paramString3, paramString4, paramString5);
    return this;
  }

  public GoogleWebContentHelper loadUrl()
  {
    ensureViews();
    this.mWebView.loadUrl(this.mSecureUrl);
    return this;
  }

  public GoogleWebContentHelper setLoadingMessage(String paramString)
  {
    this.mLoadingMessage = paramString;
    return this;
  }

  public GoogleWebContentHelper setUnsuccessfulMessage(String paramString)
  {
    Locale localLocale = this.mContext.getResources().getConfiguration().locale;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mPrettyUrl;
    this.mUnsuccessfulMessage = String.format(localLocale, paramString, arrayOfObject);
    return this;
  }

  public GoogleWebContentHelper setUrls(String paramString1, String paramString2)
  {
    this.mSecureUrl = fillUrl(paramString1, this.mContext);
    this.mPrettyUrl = fillUrl(paramString2, this.mContext);
    return this;
  }

  public GoogleWebContentHelper setUrlsFromGservices(String paramString1, String paramString2)
  {
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    this.mSecureUrl = fillUrl(Gservices.getString(localContentResolver, paramString1), this.mContext);
    this.mPrettyUrl = fillUrl(Gservices.getString(localContentResolver, paramString2), this.mContext);
    return this;
  }

  private class MyWebViewClient extends WebViewClient
  {
    private MyWebViewClient()
    {
    }

    public void onPageFinished(WebView paramWebView, String paramString)
    {
      GoogleWebContentHelper.this.handleWebViewCompletion(true);
    }

    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
      GoogleWebContentHelper.this.handleWebViewCompletion(false);
    }

    public void onReceivedHttpAuthRequest(WebView paramWebView, HttpAuthHandler paramHttpAuthHandler, String paramString1, String paramString2)
    {
      GoogleWebContentHelper.this.handleWebViewCompletion(false);
    }

    public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError)
    {
      GoogleWebContentHelper.this.handleWebViewCompletion(false);
    }

    public void onTooManyRedirects(WebView paramWebView, Message paramMessage1, Message paramMessage2)
    {
      GoogleWebContentHelper.this.handleWebViewCompletion(false);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.GoogleWebContentHelper
 * JD-Core Version:    0.6.2
 */