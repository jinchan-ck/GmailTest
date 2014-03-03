package com.android.mail.compose;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import com.android.mail.providers.Message;
import com.android.mail.utils.Utils;
import java.text.DateFormat;
import java.util.Date;

class QuotedTextView extends LinearLayout
  implements View.OnClickListener
{
  private static final int HEADER_SEPARATOR_LENGTH = "<br type='attribution'>".length();
  private static String sQuoteBegin;
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
    this(paramContext, paramAttributeSet, -1);
  }

  public QuotedTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet);
    LayoutInflater.from(paramContext).inflate(2130968664, this);
    this.mQuotedTextWebView = ((WebView)findViewById(2131689686));
    Utils.restrictWebView(this.mQuotedTextWebView);
    this.mQuotedTextWebView.getSettings().setBlockNetworkLoads(true);
    this.mShowHideCheckBox = ((CheckBox)findViewById(2131689682));
    this.mShowHideCheckBox.setChecked(true);
    this.mShowHideCheckBox.setOnClickListener(this);
    sQuoteBegin = paramContext.getResources().getString(2131427593);
    findViewById(2131689683).setOnClickListener(this);
    this.mRespondInlineButton = ((Button)findViewById(2131689684));
    if (this.mRespondInlineButton != null)
      this.mRespondInlineButton.setEnabled(false);
  }

  public static boolean containsQuotedText(String paramString)
  {
    return paramString.indexOf(sQuoteBegin) >= 0;
  }

  public static int findQuotedTextIndex(CharSequence paramCharSequence)
  {
    if (TextUtils.isEmpty(paramCharSequence))
      return -1;
    return paramCharSequence.toString().indexOf(sQuoteBegin);
  }

  private String getHtmlText(Message paramMessage)
  {
    if (paramMessage.bodyHtml != null)
      return paramMessage.bodyHtml;
    if (paramMessage.bodyText != null)
      return Html.toHtml(new SpannedString(paramMessage.bodyText));
    return "";
  }

  public static int getQuotedTextOffset(String paramString)
  {
    return paramString.indexOf("<br type='attribution'>") + HEADER_SEPARATOR_LENGTH;
  }

  private void populateData()
  {
    String str1 = getContext().getResources().getString(2131427334);
    String str2 = getContext().getResources().getString(2131427335);
    String str3 = "<head><style type=\"text/css\">* body { background-color: " + str1 + "; color: " + str2 + "; }</style></head>" + this.mQuotedText.toString();
    this.mQuotedTextWebView.loadDataWithBaseURL(null, str3, "text/html", "utf-8", null);
  }

  private void respondInline()
  {
    String str = Utils.convertHtmlToPlainText(getQuotedText().toString());
    if (this.mRespondInlineListener != null)
      this.mRespondInlineListener.onRespondInline("\n" + str);
    updateCheckedState(false);
    this.mRespondInlineButton.setVisibility(8);
    View localView = findViewById(2131689678);
    if (localView != null)
      localView.setVisibility(8);
  }

  private void setQuotedText(CharSequence paramCharSequence)
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

  private void updateQuotedTextVisibility(boolean paramBoolean)
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

  public void allowQuotedText(boolean paramBoolean)
  {
    View localView = findViewById(2131689681);
    if (localView != null)
      if (!paramBoolean)
        break label23;
    label23: for (int i = 0; ; i = 4)
    {
      localView.setVisibility(i);
      return;
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
      return;
    case 2131689684:
      respondInline();
      return;
    case 2131689682:
      updateCheckedState(this.mShowHideCheckBox.isChecked());
      return;
    case 2131689683:
    }
    if (!this.mShowHideCheckBox.isChecked());
    for (boolean bool = true; ; bool = false)
    {
      updateCheckedState(bool);
      return;
    }
  }

  public void setQuotedText(int paramInt, Message paramMessage, boolean paramBoolean)
  {
    setVisibility(0);
    String str1 = getHtmlText(paramMessage);
    StringBuffer localStringBuffer = new StringBuffer();
    DateFormat localDateFormat = DateFormat.getDateTimeInstance(2, 3);
    Date localDate = new Date(paramMessage.dateReceivedMs);
    Resources localResources = getContext().getResources();
    if ((paramInt == 0) || (paramInt == 1))
    {
      localStringBuffer.append(sQuoteBegin);
      String str2 = localResources.getString(2131427369);
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = localDateFormat.format(localDate);
      arrayOfObject1[1] = Utils.cleanUpString(paramMessage.getFrom(), true);
      localStringBuffer.append(String.format(str2, arrayOfObject1));
      localStringBuffer.append("<br type='attribution'>");
      localStringBuffer.append("<blockquote class=\"quote\" style=\"margin:0 0 0 .8ex;border-left:1px #ccc solid;padding-left:1ex\">");
      localStringBuffer.append(str1);
      localStringBuffer.append("</blockquote>");
      localStringBuffer.append("</div>");
    }
    while (true)
    {
      setQuotedText(localStringBuffer);
      allowQuotedText(paramBoolean);
      allowRespondInline(true);
      return;
      if (paramInt == 2)
      {
        localStringBuffer.append(sQuoteBegin);
        String str3 = localResources.getString(2131427370);
        Object[] arrayOfObject2 = new Object[4];
        arrayOfObject2[0] = Utils.cleanUpString(paramMessage.getFrom(), true);
        arrayOfObject2[1] = localDateFormat.format(localDate);
        arrayOfObject2[2] = Utils.cleanUpString(paramMessage.subject, false);
        arrayOfObject2[3] = Utils.cleanUpString(paramMessage.getTo(), true);
        localStringBuffer.append(String.format(str3, arrayOfObject2));
        String str4 = paramMessage.getCc();
        String str5 = localResources.getString(2131427371);
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = Utils.cleanUpString(str4, true);
        localStringBuffer.append(String.format(str5, arrayOfObject3));
        localStringBuffer.append("<br type='attribution'>");
        localStringBuffer.append("<blockquote class=\"quote\" style=\"margin:0 0 0 .8ex;border-left:1px #ccc solid;padding-left:1ex\">");
        localStringBuffer.append(str1);
        localStringBuffer.append("</blockquote>");
        localStringBuffer.append("</div>");
      }
    }
  }

  public void setQuotedTextFromDraft(CharSequence paramCharSequence, boolean paramBoolean)
  {
    setVisibility(0);
    setQuotedText(paramCharSequence);
    boolean bool = false;
    if (!paramBoolean)
      bool = true;
    allowQuotedText(bool);
    allowRespondInline(true);
  }

  public void setRespondInlineListener(RespondInlineListener paramRespondInlineListener)
  {
    this.mRespondInlineListener = paramRespondInlineListener;
  }

  public void setUpperDividerVisible(boolean paramBoolean)
  {
    View localView = findViewById(2131689679);
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localView.setVisibility(i);
      return;
    }
  }

  public void updateCheckedState(boolean paramBoolean)
  {
    this.mShowHideCheckBox.setChecked(paramBoolean);
    updateQuotedTextVisibility(paramBoolean);
    if (this.mShowHideListener != null)
      this.mShowHideListener.onShowHideQuotedText(paramBoolean);
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

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.QuotedTextView
 * JD-Core Version:    0.6.2
 */