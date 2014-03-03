package com.google.android.gm;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import com.google.android.gm.common.html.parser.HtmlDocument;
import com.google.android.gm.common.html.parser.HtmlParser;
import com.google.android.gm.common.html.parser.HtmlTree;
import com.google.android.gm.common.html.parser.HtmlTreeBuilder;

class QuotedTextView extends LinearLayout
  implements View.OnClickListener
{
  private boolean mIncludeText = true;
  private CharSequence mQuotedText;
  private WebView mQuotedTextWebView;
  private Button mRespondInlineButton;
  private RespondInlineListener mRespondInlineListener;
  private CheckBox mShowHideCheckBox;
  private ShowHideQuotedTextListener mShowHideListener;

  public QuotedTextView(Context paramContext)
  {
    this(paramContext, null);
  }

  public QuotedTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }

  private void init(Context paramContext)
  {
    LayoutInflater.from(paramContext).inflate(2130903072, this);
    this.mQuotedTextWebView = ((WebView)findViewById(2131361908));
    Utils.restrictWebView(this.mQuotedTextWebView);
    this.mShowHideCheckBox = ((CheckBox)findViewById(2131361905));
    this.mShowHideCheckBox.setChecked(true);
    this.mShowHideCheckBox.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        boolean bool = QuotedTextView.this.mShowHideCheckBox.isChecked();
        QuotedTextView.this.updateQuotedTextVisibility(bool);
        QuotedTextView.this.mShowHideListener.onShowHideQuotedText(bool);
      }
    });
    this.mRespondInlineButton = ((Button)findViewById(2131361907));
    if (this.mRespondInlineButton != null)
      this.mRespondInlineButton.setEnabled(false);
  }

  private void populateData()
  {
    String str = "<head><style type=\"text/css\">* { color: #6d2a6d; }</style></head>" + this.mQuotedText.toString();
    this.mQuotedTextWebView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
  }

  private void updateCheckedState(boolean paramBoolean)
  {
    this.mShowHideCheckBox.setChecked(paramBoolean);
    updateQuotedTextVisibility(paramBoolean);
  }

  public void allowQuotedText(boolean paramBoolean)
  {
    View localView1 = findViewById(2131361906);
    View localView2 = findViewById(2131361905);
    View localView3 = findViewById(2131361904);
    int k;
    int j;
    if (localView3 != null)
    {
      if (!paramBoolean)
      {
        k = 0;
        localView3.setVisibility(k);
      }
    }
    else
    {
      if (localView1 != null)
      {
        if (!paramBoolean)
          break label83;
        j = 0;
        label52: localView1.setVisibility(j);
      }
      if (localView2 != null)
        if (!paramBoolean)
          break label90;
    }
    label83: label90: for (int i = 0; ; i = 8)
    {
      localView2.setVisibility(i);
      return;
      k = 8;
      break;
      j = 8;
      break label52;
    }
  }

  public void allowRespondInline(boolean paramBoolean)
  {
    Button localButton;
    if (this.mRespondInlineButton != null)
    {
      localButton = this.mRespondInlineButton;
      if (!paramBoolean)
        break label24;
    }
    label24: for (int i = 0; ; i = 8)
    {
      localButton.setVisibility(i);
      return;
    }
  }

  public CharSequence getQuotedText()
  {
    return this.mQuotedText;
  }

  public CharSequence getQuotedTextIfIncluded()
  {
    if (this.mIncludeText)
      return this.mQuotedText;
    return null;
  }

  public boolean isTextIncluded()
  {
    return this.mIncludeText;
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
    case 2131361907:
    }
    View localView;
    do
    {
      return;
      HtmlDocument localHtmlDocument = new HtmlParser().parse((String)getQuotedText());
      HtmlTreeBuilder localHtmlTreeBuilder = new HtmlTreeBuilder();
      localHtmlDocument.accept(localHtmlTreeBuilder);
      this.mRespondInlineListener.onRespondInline("\n" + localHtmlTreeBuilder.getTree().getPlainText());
      updateCheckedState(false);
      this.mRespondInlineButton.setVisibility(8);
      localView = findViewById(2131361903);
    }
    while (localView == null);
    localView.setVisibility(8);
  }

  public void setQuotedText(CharSequence paramCharSequence)
  {
    this.mQuotedText = paramCharSequence;
    populateData();
    if (this.mRespondInlineButton != null)
    {
      if (!TextUtils.isEmpty(paramCharSequence))
      {
        this.mRespondInlineButton.setVisibility(0);
        this.mRespondInlineButton.setEnabled(true);
        this.mRespondInlineButton.setOnClickListener(this);
      }
    }
    else
      return;
    this.mRespondInlineButton.setVisibility(8);
    this.mRespondInlineButton.setEnabled(false);
  }

  public void setRespondInlineListener(RespondInlineListener paramRespondInlineListener)
  {
    this.mRespondInlineListener = paramRespondInlineListener;
  }

  public void setShowHideListener(ShowHideQuotedTextListener paramShowHideQuotedTextListener)
  {
    this.mShowHideListener = paramShowHideQuotedTextListener;
  }

  public void updateQuotedTextVisibility(boolean paramBoolean)
  {
    WebView localWebView = this.mQuotedTextWebView;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localWebView.setVisibility(i);
      this.mIncludeText = paramBoolean;
      return;
    }
  }

  public static abstract interface RespondInlineListener
  {
    public abstract void onRespondInline(String paramString);
  }

  public static abstract interface ShowHideQuotedTextListener
  {
    public abstract void onShowHideQuotedText(boolean paramBoolean);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.QuotedTextView
 * JD-Core Version:    0.6.2
 */