package com.android.mail.browse;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class SnippetTextView extends TextView
{
  private int mLastHSpec;
  private int mLastWSpec;
  private int mMaxLines;

  public SnippetTextView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SnippetTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public String getTextRemainder(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      paramString = null;
    CharSequence localCharSequence;
    TextUtils.TruncateAt localTruncateAt;
    Layout localLayout;
    do
    {
      return paramString;
      localCharSequence = getText();
      localTruncateAt = getEllipsize();
      setEllipsize(null);
      setText(paramString);
      localLayout = getLayout();
      if (localLayout == null)
      {
        measure(this.mLastWSpec, this.mLastHSpec);
        localLayout = getLayout();
      }
    }
    while (localLayout == null);
    if (localLayout.getLineCount() <= this.mMaxLines);
    for (String str = null; ; str = paramString.substring(localLayout.getLineStart(this.mMaxLines), paramString.length()))
    {
      setEllipsize(localTruncateAt);
      setText(localCharSequence);
      return str;
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    this.mLastWSpec = paramInt1;
    this.mLastHSpec = paramInt2;
  }

  public void setMaxLines(int paramInt)
  {
    super.setMaxLines(paramInt);
    this.mMaxLines = paramInt;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.SnippetTextView
 * JD-Core Version:    0.6.2
 */