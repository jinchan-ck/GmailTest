package com.google.android.gm;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gm.provider.Gmail.ConversationCursor;
import com.google.android.gm.provider.Gmail.CursorError;
import com.google.android.gm.provider.Gmail.CursorStatus;

public class ConversationListFooterView extends LinearLayout
  implements View.OnClickListener
{
  private TextView mErrorText;
  private View mLoading;
  private View mNetworkError;
  private View mRetry;

  public ConversationListFooterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private String getTextForError(Gmail.CursorError paramCursorError)
  {
    switch (1.$SwitchMap$com$google$android$gm$provider$Gmail$CursorError[paramCursorError.ordinal()])
    {
    default:
      return getResources().getString(2131296505);
    case 1:
      throw new IllegalStateException();
    case 2:
      return getResources().getString(2131296503);
    case 3:
      return getResources().getString(2131296284);
    case 4:
    }
    return getResources().getString(2131296504);
  }

  public void bind(Gmail.CursorStatus paramCursorStatus, Gmail.CursorError paramCursorError, Gmail.ConversationCursor paramConversationCursor)
  {
    this.mRetry.setTag(paramConversationCursor);
    switch (1.$SwitchMap$com$google$android$gm$provider$Gmail$CursorStatus[paramCursorStatus.ordinal()])
    {
    default:
      return;
    case 1:
      throw new IllegalStateException();
    case 2:
      this.mLoading.setVisibility(0);
      this.mNetworkError.setVisibility(8);
      return;
    case 3:
    }
    this.mNetworkError.setVisibility(0);
    this.mErrorText.setText(getTextForError(paramCursorError));
    this.mLoading.setVisibility(8);
    View localView = this.mRetry;
    if (paramCursorError == Gmail.CursorError.IO_ERROR);
    for (int i = 0; ; i = 8)
    {
      localView.setVisibility(i);
      return;
    }
  }

  public void onClick(View paramView)
  {
    ((Gmail.ConversationCursor)paramView.getTag()).retry();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mLoading = findViewById(2131361840);
    this.mNetworkError = findViewById(2131361837);
    this.mRetry = findViewById(2131361839);
    this.mRetry.setOnClickListener(this);
    this.mErrorText = ((TextView)findViewById(2131361838));
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ConversationListFooterView
 * JD-Core Version:    0.6.2
 */