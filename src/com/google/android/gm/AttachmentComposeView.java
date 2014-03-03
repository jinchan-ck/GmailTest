package com.google.android.gm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gm.provider.Gmail.Attachment;

class AttachmentComposeView extends LinearLayout
  implements View.OnClickListener
{
  private final Gmail.Attachment mAttachment;
  private final String mFilename;
  private final long mSize;

  public AttachmentComposeView(Context paramContext, Gmail.Attachment paramAttachment)
  {
    super(paramContext);
    this.mAttachment = paramAttachment;
    this.mFilename = paramAttachment.name;
    this.mSize = paramAttachment.size;
    Log.i("Gmail", ">>>>> Attachment uri: " + paramAttachment.originExtras);
    Log.i("Gmail", ">>>>>           type: " + paramAttachment.contentType);
    Log.i("Gmail", ">>>>>           name: " + this.mFilename);
    Log.i("Gmail", ">>>>>           size: " + this.mSize);
    LayoutInflater.from(getContext()).inflate(2130903042, this);
    populateAttachmentData(paramContext);
  }

  private void populateAttachmentData(Context paramContext)
  {
    ((TextView)findViewById(2131361801)).setText(this.mFilename);
    if (this.mSize != 0L)
    {
      ((TextView)findViewById(2131361802)).setText(Utils.convertToHumanReadableSize(paramContext, this.mSize));
      return;
    }
    ((TextView)findViewById(2131361802)).setVisibility(8);
  }

  public void addDeleteListener(View.OnClickListener paramOnClickListener)
  {
    ((Button)findViewById(2131361803)).setOnClickListener(paramOnClickListener);
  }

  public final void onClick(View paramView)
  {
    paramView.getId();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.AttachmentComposeView
 * JD-Core Version:    0.6.2
 */