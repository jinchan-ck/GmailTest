package com.android.mail.compose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.mail.providers.Attachment;
import com.android.mail.utils.AttachmentUtils;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import org.json.JSONException;
import org.json.JSONObject;

class AttachmentComposeView extends LinearLayout
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private final Attachment mAttachment;

  public AttachmentComposeView(Context paramContext, Attachment paramAttachment)
  {
    super(paramContext);
    this.mAttachment = paramAttachment;
    if (LogUtils.isLoggable(LOG_TAG, 3));
    try
    {
      String str2 = paramAttachment.toJSON().toString(2);
      str1 = str2;
      LogUtils.d(LOG_TAG, "attachment view: %s", new Object[] { str1 });
      LayoutInflater.from(getContext()).inflate(2130968588, this);
      populateAttachmentData(paramContext);
      return;
    }
    catch (JSONException localJSONException)
    {
      while (true)
        String str1 = paramAttachment.toString();
    }
  }

  private void populateAttachmentData(Context paramContext)
  {
    ((TextView)findViewById(2131689508)).setText(this.mAttachment.name);
    if (this.mAttachment.size > 0)
    {
      ((TextView)findViewById(2131689509)).setText(AttachmentUtils.convertToHumanReadableSize(paramContext, this.mAttachment.size));
      return;
    }
    ((TextView)findViewById(2131689509)).setVisibility(8);
  }

  public void addDeleteListener(View.OnClickListener paramOnClickListener)
  {
    ((ImageButton)findViewById(2131689510)).setOnClickListener(paramOnClickListener);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.AttachmentComposeView
 * JD-Core Version:    0.6.2
 */