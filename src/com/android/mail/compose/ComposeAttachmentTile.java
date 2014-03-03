package com.android.mail.compose;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.android.mail.ui.AttachmentTile;

public class ComposeAttachmentTile extends AttachmentTile
{
  private ImageButton mDeleteButton;

  public ComposeAttachmentTile(Context paramContext)
  {
    this(paramContext, null);
  }

  public ComposeAttachmentTile(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public static ComposeAttachmentTile inflate(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return (ComposeAttachmentTile)paramLayoutInflater.inflate(2130968596, paramViewGroup, false);
  }

  public void addDeleteListener(View.OnClickListener paramOnClickListener)
  {
    this.mDeleteButton.setOnClickListener(paramOnClickListener);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mDeleteButton = ((ImageButton)findViewById(2131689541));
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.ComposeAttachmentTile
 * JD-Core Version:    0.6.2
 */