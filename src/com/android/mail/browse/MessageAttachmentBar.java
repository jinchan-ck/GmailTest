package com.android.mail.browse;

import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.mail.providers.Attachment;
import com.android.mail.utils.AttachmentUtils;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.MimeType;
import com.android.mail.utils.Utils;
import java.util.List;

public class MessageAttachmentBar extends FrameLayout
  implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, AttachmentViewInterface
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private final AttachmentActionHandler mActionHandler = new AttachmentActionHandler(paramContext, this);
  private Attachment mAttachment;
  private String mAttachmentSizeText;
  private ImageButton mCancelButton;
  private String mDisplayType;
  private ImageView mOverflowButton;
  private PopupMenu mPopup;
  private ProgressBar mProgress;
  private boolean mSaveClicked;
  private TextView mSubTitle;
  private TextView mTitle;
  private final Runnable mUpdateRunnable = new Runnable()
  {
    public void run()
    {
      MessageAttachmentBar.this.updateActionsInternal();
    }
  };

  public MessageAttachmentBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public MessageAttachmentBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public static MessageAttachmentBar inflate(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return (MessageAttachmentBar)paramLayoutInflater.inflate(2130968609, paramViewGroup, false);
  }

  private boolean onClick(int paramInt, View paramView)
  {
    switch (paramInt)
    {
    default:
      if (MimeType.isInstallable(this.mAttachment.contentType))
        this.mActionHandler.showAttachment(1);
      break;
    case 2131689770:
    case 2131689771:
    case 2131689772:
    case 2131689573:
    case 2131689572:
      boolean bool2;
      boolean bool3;
      boolean bool4;
      do
      {
        do
        {
          do
          {
            return true;
            previewAttachment();
            return true;
          }
          while (!this.mAttachment.canSave());
          this.mActionHandler.startDownloadingAttachment(1);
          this.mSaveClicked = true;
          return true;
        }
        while (!this.mAttachment.isPresentLocally());
        this.mActionHandler.startRedownloadingAttachment(this.mAttachment);
        return true;
        this.mActionHandler.cancelAttachment();
        this.mSaveClicked = false;
        return true;
        boolean bool1 = this.mAttachment.canSave();
        bool2 = false;
        if (bool1)
        {
          boolean bool5 = this.mAttachment.isDownloading();
          bool2 = false;
          if (!bool5)
            bool2 = true;
        }
        bool3 = this.mAttachment.canPreview();
        bool4 = this.mAttachment.isPresentLocally();
      }
      while ((!bool2) && (!bool3) && (!bool4));
      if (this.mPopup == null)
      {
        this.mPopup = new PopupMenu(getContext(), paramView);
        this.mPopup.getMenuInflater().inflate(2131820552, this.mPopup.getMenu());
        this.mPopup.setOnMenuItemClickListener(this);
      }
      Menu localMenu = this.mPopup.getMenu();
      localMenu.findItem(2131689770).setVisible(bool3);
      localMenu.findItem(2131689771).setVisible(bool2);
      localMenu.findItem(2131689772).setVisible(bool4);
      this.mPopup.show();
      return true;
    }
    if (MimeType.isViewable(getContext(), this.mAttachment.contentUri, this.mAttachment.contentType))
    {
      this.mActionHandler.showAttachment(0);
      return true;
    }
    if (this.mAttachment.canPreview())
    {
      previewAttachment();
      return true;
    }
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
    if (MimeType.isBlocked(this.mAttachment.contentType));
    for (int i = 2131427451; ; i = 2131427452)
    {
      localBuilder.setTitle(2131427450).setMessage(i).show();
      return true;
    }
  }

  private void previewAttachment()
  {
    if (this.mAttachment.canPreview())
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", this.mAttachment.previewIntentUri);
      getContext().startActivity(localIntent);
    }
  }

  private void setButtonVisible(View paramView, boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      paramView.setVisibility(i);
      return;
    }
  }

  private void updateActions()
  {
    removeCallbacks(this.mUpdateRunnable);
    post(this.mUpdateRunnable);
  }

  private void updateActionsInternal()
  {
    if ((this.mActionHandler.isProgressDialogVisible()) || (this.mActionHandler.dialogJustClosed()))
      return;
    boolean bool1 = this.mAttachment.isDownloading();
    int i;
    boolean bool2;
    boolean bool3;
    boolean bool4;
    ImageButton localImageButton;
    if ((this.mAttachment.canSave()) && (MimeType.isViewable(getContext(), this.mAttachment.contentUri, this.mAttachment.contentType)))
    {
      i = 1;
      bool2 = this.mAttachment.canPreview();
      bool3 = MimeType.isInstallable(this.mAttachment.contentType);
      bool4 = this.mAttachment.isPresentLocally();
      localImageButton = this.mCancelButton;
      if ((!bool1) || (!this.mSaveClicked))
        break label141;
    }
    label141: for (boolean bool5 = true; ; bool5 = false)
    {
      setButtonVisible(localImageButton, bool5);
      if (!bool1)
        break label147;
      setButtonVisible(this.mOverflowButton, false);
      return;
      i = 0;
      break;
    }
    label147: if ((i != 0) && (this.mSaveClicked))
    {
      setButtonVisible(this.mOverflowButton, false);
      return;
    }
    if ((bool3) && (!bool4))
    {
      setButtonVisible(this.mOverflowButton, false);
      return;
    }
    ImageView localImageView = this.mOverflowButton;
    boolean bool6;
    if ((i == 0) && (!bool2))
    {
      bool6 = false;
      if (!bool4);
    }
    else
    {
      bool6 = true;
    }
    setButtonVisible(localImageView, bool6);
  }

  private void updateSubtitleText(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramString != null)
      localStringBuilder.append(paramString);
    localStringBuilder.append(this.mAttachmentSizeText);
    localStringBuilder.append(' ');
    localStringBuilder.append(this.mDisplayType);
    this.mSubTitle.setText(localStringBuilder.toString());
  }

  public List<Attachment> getAttachments()
  {
    return null;
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
    this.mTitle = ((TextView)findViewById(2131689569));
    this.mSubTitle = ((TextView)findViewById(2131689570));
    this.mProgress = ((ProgressBar)findViewById(2131689571));
    this.mOverflowButton = ((ImageView)findViewById(2131689572));
    this.mCancelButton = ((ImageButton)findViewById(2131689573));
    setOnClickListener(this);
    this.mOverflowButton.setOnClickListener(this);
    this.mCancelButton.setOnClickListener(this);
  }

  public boolean onMenuItemClick(MenuItem paramMenuItem)
  {
    this.mPopup.dismiss();
    return onClick(paramMenuItem.getItemId(), null);
  }

  public void onUpdateStatus()
  {
    if (this.mAttachment.state == 1)
    {
      this.mSubTitle.setText(getResources().getString(2131427456));
      return;
    }
    if (this.mAttachment.isSavedToExternal());
    for (String str = getResources().getString(2131427455); ; str = null)
    {
      updateSubtitleText(str);
      return;
    }
  }

  public void render(Attachment paramAttachment, boolean paramBoolean)
  {
    Attachment localAttachment = this.mAttachment;
    this.mAttachment = paramAttachment;
    this.mActionHandler.setAttachment(this.mAttachment);
    if (!paramAttachment.isDownloading());
    for (boolean bool = false; ; bool = this.mSaveClicked)
    {
      this.mSaveClicked = bool;
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
        updateSubtitleText(null);
      }
      updateActions();
      this.mActionHandler.updateStatus(paramBoolean);
      return;
    }
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
        this.mSubTitle.setVisibility(4);
        return;
      }
    }
    this.mProgress.setVisibility(4);
    this.mSubTitle.setVisibility(0);
  }

  public void viewAttachment()
  {
    if (this.mAttachment.contentUri == null)
    {
      LogUtils.e(LOG_TAG, "viewAttachment with null content uri", new Object[0]);
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
      LogUtils.e(LOG_TAG, localActivityNotFoundException, "Couldn't find Activity for intent", new Object[0]);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.MessageAttachmentBar
 * JD-Core Version:    0.6.2
 */