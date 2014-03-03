package com.android.mail.browse;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.android.mail.providers.Message;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;

public class MessageInviteView extends LinearLayout
  implements View.OnClickListener
{
  private InviteCommandHandler mCommandHandler = new InviteCommandHandler();
  private final Context mContext;
  private Message mMessage;

  public MessageInviteView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MessageInviteView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  public void bind(Message paramMessage)
  {
    this.mMessage = paramMessage;
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    Integer localInteger = null;
    switch (i)
    {
    default:
    case 2131689604:
    case 2131689605:
    case 2131689606:
    case 2131689607:
    }
    while (true)
    {
      if (localInteger != null)
      {
        ContentValues localContentValues = new ContentValues();
        LogUtils.w("UnifiedEmail", "SENDING INVITE COMMAND, VALUE=%s", new Object[] { localInteger });
        localContentValues.put("respond", localInteger);
        this.mCommandHandler.sendCommand(localContentValues);
      }
      return;
      boolean bool = Utils.isEmpty(this.mMessage.eventIntentUri);
      localInteger = null;
      if (!bool)
      {
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(this.mMessage.eventIntentUri);
        this.mContext.startActivity(localIntent);
        localInteger = null;
        continue;
        localInteger = Integer.valueOf(1);
        continue;
        localInteger = Integer.valueOf(2);
        continue;
        localInteger = Integer.valueOf(3);
      }
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    findViewById(2131689604).setOnClickListener(this);
    findViewById(2131689605).setOnClickListener(this);
    findViewById(2131689606).setOnClickListener(this);
    findViewById(2131689607).setOnClickListener(this);
  }

  private class InviteCommandHandler extends AsyncQueryHandler
  {
    public InviteCommandHandler()
    {
      super();
    }

    public void sendCommand(ContentValues paramContentValues)
    {
      startUpdate(0, null, MessageInviteView.this.mMessage.uri, paramContentValues, null, null);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.MessageInviteView
 * JD-Core Version:    0.6.2
 */