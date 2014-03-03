package com.android.mail.browse;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.mail.providers.Address;
import com.android.mail.providers.Message;

public class SpamWarningView extends RelativeLayout
  implements View.OnClickListener
{
  private final int mHighWarningColor = getResources().getColor(2131230760);
  private final int mLowWarningColor = getResources().getColor(2131230753);
  private ImageView mSpamWarningIcon;
  private TextView mSpamWarningLink;
  private TextView mSpamWarningText;

  public SpamWarningView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SpamWarningView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    case 2131689612:
    }
  }

  public void onFinishInflate()
  {
    setOnClickListener(this);
    this.mSpamWarningIcon = ((ImageView)findViewById(2131689613));
    this.mSpamWarningText = ((TextView)findViewById(2131689614));
    this.mSpamWarningLink = ((TextView)findViewById(2131689615));
    this.mSpamWarningLink.setOnClickListener(this);
  }

  public void showSpamWarning(Message paramMessage, Address paramAddress)
  {
    setVisibility(0);
    String str1 = paramAddress.getAddress();
    String str2 = str1.substring(1 + str1.indexOf('@'));
    this.mSpamWarningText.setText(Html.fromHtml(String.format(paramMessage.spamWarningString, new Object[] { str1, str2 })));
    if (paramMessage.spamWarningLevel == 2)
    {
      this.mSpamWarningText.setTextColor(this.mHighWarningColor);
      this.mSpamWarningIcon.setImageResource(2130837547);
    }
    while (true)
      switch (paramMessage.spamLinkType)
      {
      default:
        return;
        this.mSpamWarningText.setTextColor(this.mLowWarningColor);
        this.mSpamWarningIcon.setImageResource(2130837546);
      case 0:
      case 1:
      case 2:
      }
    this.mSpamWarningLink.setVisibility(8);
    return;
    this.mSpamWarningLink.setVisibility(0);
    this.mSpamWarningLink.setText(2131427574);
    return;
    this.mSpamWarningLink.setVisibility(0);
    this.mSpamWarningLink.setText(2131427396);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.SpamWarningView
 * JD-Core Version:    0.6.2
 */