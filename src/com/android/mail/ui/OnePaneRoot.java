package com.android.mail.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;

public class OnePaneRoot extends FrameLayout
{
  private static final String LOG_TAG = LogTag.getLogTag();

  public OnePaneRoot(Context paramContext)
  {
    this(paramContext, null);
  }

  public OnePaneRoot(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    LogUtils.d("MailBlankFragment", "OnePaneRoot(%s).onLayout() START", new Object[] { this });
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    LogUtils.d("MailBlankFragment", "OnePaneRoot(%s).onLayout() FINISH", new Object[] { this });
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    LogUtils.d("MailBlankFragment", "OnePaneRoot(%s).onMeasure() called", new Object[] { this });
    super.onMeasure(paramInt1, paramInt2);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.OnePaneRoot
 * JD-Core Version:    0.6.2
 */