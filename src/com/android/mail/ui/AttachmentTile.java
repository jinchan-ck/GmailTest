package com.android.mail.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.ex.photo.util.ImageUtils;
import com.android.mail.providers.Attachment;
import com.android.mail.utils.AttachmentUtils;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;

public class AttachmentTile extends RelativeLayout
  implements AttachmentBitmapHolder
{
  private static final String LOG_TAG = LogTag.getLogTag();
  protected Attachment mAttachment;
  private AttachmentPreviewCache mAttachmentPreviewCache;
  private String mAttachmentSizeText;
  private ImageView mDefaultIcon;
  private boolean mDefaultThumbnailSet = true;
  private String mDisplayType;
  private ImageView mIcon;
  private TextView mSubtitle;
  private ThumbnailLoadTask mThumbnailTask;
  private TextView mTitle;

  public AttachmentTile(Context paramContext)
  {
    this(paramContext, null);
  }

  public AttachmentTile(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public static boolean isTiledAttachment(Attachment paramAttachment)
  {
    return ImageUtils.isImageMimeType(paramAttachment.contentType);
  }

  private void updateSubtitleText()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.mAttachmentSizeText);
    localStringBuilder.append(' ');
    localStringBuilder.append(this.mDisplayType);
    this.mSubtitle.setText(localStringBuilder.toString());
  }

  public boolean bitmapSetToDefault()
  {
    return this.mDefaultThumbnailSet;
  }

  public ContentResolver getResolver()
  {
    return getContext().getContentResolver();
  }

  public int getThumbnailHeight()
  {
    return this.mIcon.getHeight();
  }

  public int getThumbnailWidth()
  {
    return this.mIcon.getWidth();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTitle = ((TextView)findViewById(2131689542));
    this.mSubtitle = ((TextView)findViewById(2131689543));
    this.mIcon = ((ImageView)findViewById(2131689511));
    this.mDefaultIcon = ((ImageView)findViewById(2131689512));
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    ThumbnailLoadTask.setupThumbnailPreview(this.mThumbnailTask, this, this.mAttachment, null);
  }

  public void render(Attachment paramAttachment, Uri paramUri, int paramInt, AttachmentPreviewCache paramAttachmentPreviewCache, boolean paramBoolean)
  {
    if (paramAttachment == null)
    {
      setVisibility(4);
      return;
    }
    Attachment localAttachment = this.mAttachment;
    this.mAttachment = paramAttachment;
    this.mAttachmentPreviewCache = paramAttachmentPreviewCache;
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[6];
    arrayOfObject[0] = paramAttachment.name;
    arrayOfObject[1] = Integer.valueOf(paramAttachment.state);
    arrayOfObject[2] = Integer.valueOf(paramAttachment.destination);
    arrayOfObject[3] = Integer.valueOf(paramAttachment.downloadedSize);
    arrayOfObject[4] = paramAttachment.contentUri;
    arrayOfObject[5] = paramAttachment.contentType;
    LogUtils.d(str, "got attachment list row: name=%s state/dest=%d/%d dled=%d contentUri=%s MIME=%s", arrayOfObject);
    if ((localAttachment == null) || (!TextUtils.equals(paramAttachment.name, localAttachment.name)))
      this.mTitle.setText(paramAttachment.name);
    if ((localAttachment == null) || (paramAttachment.size != localAttachment.size))
    {
      this.mAttachmentSizeText = AttachmentUtils.convertToHumanReadableSize(getContext(), paramAttachment.size);
      this.mDisplayType = AttachmentUtils.getDisplayType(getContext(), paramAttachment);
      updateSubtitleText();
    }
    ThumbnailLoadTask.setupThumbnailPreview(this.mThumbnailTask, this, paramAttachment, localAttachment);
  }

  public void setThumbnail(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
      return;
    this.mDefaultIcon.setVisibility(8);
    int i = getResources().getInteger(2131296302);
    int j = paramBitmap.getWidth();
    int k = paramBitmap.getHeight();
    int m = j * getResources().getDisplayMetrics().densityDpi / 160;
    int n = k * getResources().getDisplayMetrics().densityDpi / 160;
    float f = Math.min(j / k, k / j);
    boolean bool1;
    boolean bool2;
    if ((j >= i) || (m >= this.mIcon.getWidth()) || (k >= i) || (n >= this.mIcon.getHeight()))
    {
      bool1 = true;
      if ((f >= 0.5F) || ((m >= 0.5F * this.mIcon.getHeight()) && (n >= 0.5F * this.mIcon.getWidth())))
        break label285;
      bool2 = true;
      label174: String str = LOG_TAG;
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = Integer.valueOf(m);
      arrayOfObject[1] = Integer.valueOf(n);
      arrayOfObject[2] = Boolean.valueOf(bool1);
      arrayOfObject[3] = Boolean.valueOf(bool2);
      LogUtils.d(str, "scaledWidth: %d, scaledHeight: %d, large: %b, skinny: %b", arrayOfObject);
      if (!bool1)
        break label304;
      if (!bool2)
        break label291;
      this.mIcon.setScaleType(ImageView.ScaleType.CENTER);
    }
    while (true)
    {
      this.mIcon.setImageBitmap(paramBitmap);
      this.mAttachmentPreviewCache.set(this.mAttachment, paramBitmap);
      this.mDefaultThumbnailSet = false;
      return;
      bool1 = false;
      break;
      label285: bool2 = false;
      break label174;
      label291: this.mIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
      continue;
      label304: this.mIcon.setScaleType(ImageView.ScaleType.CENTER);
    }
  }

  public void setThumbnailToDefault()
  {
    Bitmap localBitmap = this.mAttachmentPreviewCache.get(this.mAttachment);
    if (localBitmap != null)
    {
      setThumbnail(localBitmap);
      return;
    }
    this.mDefaultIcon.setVisibility(0);
    this.mDefaultThumbnailSet = true;
  }

  public static final class AttachmentPreview
    implements Parcelable
  {
    public static final Parcelable.Creator<AttachmentPreview> CREATOR = new Parcelable.Creator()
    {
      public AttachmentTile.AttachmentPreview createFromParcel(Parcel paramAnonymousParcel)
      {
        return new AttachmentTile.AttachmentPreview(paramAnonymousParcel, null);
      }

      public AttachmentTile.AttachmentPreview[] newArray(int paramAnonymousInt)
      {
        return new AttachmentTile.AttachmentPreview[paramAnonymousInt];
      }
    };
    public String attachmentIdentifier;
    public Bitmap preview;

    private AttachmentPreview(Parcel paramParcel)
    {
      this.attachmentIdentifier = paramParcel.readString();
      this.preview = ((Bitmap)paramParcel.readParcelable(null));
    }

    public AttachmentPreview(Attachment paramAttachment, Bitmap paramBitmap)
    {
      this.attachmentIdentifier = AttachmentUtils.getIdentifier(paramAttachment);
      this.preview = paramBitmap;
    }

    public int describeContents()
    {
      return 0;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(this.attachmentIdentifier);
      paramParcel.writeParcelable(this.preview, 0);
    }
  }

  public static abstract interface AttachmentPreviewCache
  {
    public abstract Bitmap get(Attachment paramAttachment);

    public abstract void set(Attachment paramAttachment, Bitmap paramBitmap);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.AttachmentTile
 * JD-Core Version:    0.6.2
 */