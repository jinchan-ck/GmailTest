package com.android.mail.browse;

import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.android.ex.photo.Intents;
import com.android.ex.photo.Intents.PhotoViewIntentBuilder;
import com.android.ex.photo.util.ImageUtils;
import com.android.mail.photo.MailPhotoViewActivity;
import com.android.mail.providers.Attachment;
import com.android.mail.providers.UIProvider;
import com.android.mail.ui.AttachmentTile;
import com.android.mail.ui.AttachmentTile.AttachmentPreviewCache;
import com.android.mail.ui.AttachmentTileGrid;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.util.List;

public class MessageAttachmentTile extends AttachmentTile
  implements View.OnClickListener, AttachmentViewInterface
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private final AttachmentActionHandler mActionHandler = new AttachmentActionHandler(paramContext, this);
  private Uri mAttachmentsListUri;
  private int mPhotoIndex;
  private ProgressBar mProgress;
  private View mTextContainer;

  public MessageAttachmentTile(Context paramContext)
  {
    this(paramContext, null);
  }

  public MessageAttachmentTile(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public static MessageAttachmentTile inflate(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return (MessageAttachmentTile)paramLayoutInflater.inflate(2130968610, paramViewGroup, false);
  }

  private boolean onClick(int paramInt, View paramView)
  {
    this.mActionHandler.showAndDownloadAttachments();
    return true;
  }

  public List<Attachment> getAttachments()
  {
    return ((AttachmentTileGrid)getParent()).getAttachments();
  }

  public void initialize(FragmentManager paramFragmentManager)
  {
    this.mActionHandler.initialize(paramFragmentManager);
  }

  public void onClick(View paramView)
  {
    onClick(paramView.getId(), paramView);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTextContainer = findViewById(2131689575);
    this.mProgress = ((ProgressBar)findViewById(2131689571));
    setOnClickListener(this);
  }

  public void onUpdateStatus()
  {
  }

  public void render(Attachment paramAttachment, Uri paramUri, int paramInt, AttachmentTile.AttachmentPreviewCache paramAttachmentPreviewCache, boolean paramBoolean)
  {
    super.render(paramAttachment, paramUri, paramInt, paramAttachmentPreviewCache, paramBoolean);
    this.mAttachmentsListUri = paramUri;
    this.mPhotoIndex = paramInt;
    this.mActionHandler.setAttachment(this.mAttachment);
    this.mActionHandler.updateStatus(paramBoolean);
  }

  public void setThumbnail(Bitmap paramBitmap)
  {
    super.setThumbnail(paramBitmap);
    this.mTextContainer.setVisibility(8);
  }

  public void setThumbnailToDefault()
  {
    super.setThumbnailToDefault();
    this.mTextContainer.setVisibility(0);
  }

  public void updateProgress(boolean paramBoolean)
  {
    if (this.mAttachment.isDownloading())
    {
      this.mProgress.setMax(this.mAttachment.size);
      this.mProgress.setProgress(this.mAttachment.downloadedSize);
      ProgressBar localProgressBar = this.mProgress;
      if (!paramBoolean);
      for (boolean bool = true; ; bool = false)
      {
        localProgressBar.setIndeterminate(bool);
        this.mProgress.setVisibility(0);
        return;
      }
    }
    this.mProgress.setVisibility(8);
  }

  public void viewAttachment()
  {
    if (ImageUtils.isImageMimeType(Utils.normalizeMimeType(this.mAttachment.contentType)))
    {
      Intents.PhotoViewIntentBuilder localPhotoViewIntentBuilder = Intents.newPhotoViewIntentBuilder(getContext(), MailPhotoViewActivity.class);
      localPhotoViewIntentBuilder.setPhotosUri(this.mAttachmentsListUri.toString()).setProjection(UIProvider.ATTACHMENT_PROJECTION).setPhotoIndex(Integer.valueOf(this.mPhotoIndex));
      getContext().startActivity(localPhotoViewIntentBuilder.build());
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setFlags(524289);
    Utils.setIntentDataAndTypeAndNormalize(localIntent, this.mAttachment.contentUri, this.mAttachment.contentType);
    try
    {
      getContext().startActivity(localIntent);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      LogUtils.e(LOG_TAG, "Coun't find Activity for intent", new Object[] { localActivityNotFoundException });
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.MessageAttachmentTile
 * JD-Core Version:    0.6.2
 */