package com.google.android.gm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.Attachment;
import com.google.android.gm.provider.Gmail.AttachmentRendition;
import com.google.android.gm.provider.Gmail.MessageCursor;
import java.util.ArrayList;
import java.util.List;

public class VoicemailView extends LinearLayout
{
  private View.OnClickListener mClickListener = new ClickListener(null);
  private MediaController mMediaController;
  private ImageButton mPlay;
  private Uri mUri;

  public VoicemailView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private MediaController getOrMakeMediaController()
  {
    if (this.mMediaController == null)
      this.mMediaController = MediaController.newMediaController(getContext(), this.mPlay, 17301540, 17301539, null, this.mUri);
    return this.mMediaController;
  }

  private void updateSpeakerButton()
  {
  }

  public void bind(String paramString, Gmail.MessageCursor paramMessageCursor)
  {
    long l = paramMessageCursor.getMessageId();
    ArrayList localArrayList = paramMessageCursor.getAttachmentInfos();
    if (localArrayList.size() > 0)
    {
      Gmail.Attachment localAttachment = (Gmail.Attachment)localArrayList.get(0);
      this.mMediaController = null;
      this.mUri = Gmail.getAttachmentUri(paramString, l, localAttachment, Gmail.AttachmentRendition.BEST, false);
      updateSpeakerButton();
      return;
    }
    throw new IllegalArgumentException("Message should have an attachment");
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mPlay = ((ImageButton)findViewById(2131361916));
    this.mPlay.setOnClickListener(this.mClickListener);
    this.mPlay.requestFocus();
  }

  private class ClickListener
    implements View.OnClickListener
  {
    private ClickListener()
    {
    }

    public void onClick(View paramView)
    {
      switch (paramView.getId())
      {
      default:
        return;
      case 2131361916:
      }
      MediaController localMediaController = VoicemailView.this.getOrMakeMediaController();
      if (localMediaController != null)
      {
        localMediaController.playOrPause();
        return;
      }
      Intent localIntent = new Intent(VoicemailView.this.getContext(), ViewAttachmentActivity.class);
      localIntent.setData(VoicemailView.this.mUri);
      localIntent.putExtra(ViewAttachmentActivity.EXTRA_ON_DOWNLOADED_ACTION, ViewAttachmentActivity.ON_DOWNLOADED_ACTION_FINISH);
      VoicemailView.this.getContext().startActivity(localIntent);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.VoicemailView
 * JD-Core Version:    0.6.2
 */