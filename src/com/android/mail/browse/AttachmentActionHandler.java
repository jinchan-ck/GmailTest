package com.android.mail.browse;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import com.android.mail.providers.Attachment;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttachmentActionHandler
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private Attachment mAttachment;
  private final AttachmentCommandHandler mCommandHandler;
  private final Context mContext;
  private boolean mDialogClosed;
  private FragmentManager mFragmentManager;
  private final Handler mHandler;
  private final AttachmentViewInterface mView;

  public AttachmentActionHandler(Context paramContext, AttachmentViewInterface paramAttachmentViewInterface)
  {
    this.mCommandHandler = new AttachmentCommandHandler(paramContext);
    this.mView = paramAttachmentViewInterface;
    this.mContext = paramContext;
    this.mDialogClosed = false;
    this.mHandler = new Handler();
  }

  private void showDownloadingDialog(int paramInt)
  {
    FragmentTransaction localFragmentTransaction = this.mFragmentManager.beginTransaction();
    Fragment localFragment = this.mFragmentManager.findFragmentByTag("attachment-progress");
    if (localFragment != null)
      localFragmentTransaction.remove(localFragment);
    localFragmentTransaction.addToBackStack(null);
    AttachmentProgressDialogFragment.newInstance(this.mAttachment, paramInt).show(localFragmentTransaction, "attachment-progress");
  }

  private void startDownloadingAttachment(Attachment paramAttachment, int paramInt)
  {
    ContentValues localContentValues = new ContentValues(2);
    localContentValues.put("state", Integer.valueOf(2));
    localContentValues.put("destination", Integer.valueOf(paramInt));
    this.mCommandHandler.sendCommand(paramAttachment.uri, localContentValues);
  }

  public void cancelAttachment()
  {
    ContentValues localContentValues = new ContentValues(1);
    localContentValues.put("state", Integer.valueOf(0));
    this.mCommandHandler.sendCommand(this.mAttachment.uri, localContentValues);
  }

  public boolean dialogJustClosed()
  {
    boolean bool = this.mDialogClosed;
    this.mDialogClosed = false;
    return bool;
  }

  public void initialize(FragmentManager paramFragmentManager)
  {
    this.mFragmentManager = paramFragmentManager;
  }

  public boolean isProgressDialogVisible()
  {
    Fragment localFragment = this.mFragmentManager.findFragmentByTag("attachment-progress");
    return (localFragment != null) && (localFragment.isVisible());
  }

  public void setAttachment(Attachment paramAttachment)
  {
    this.mAttachment = paramAttachment;
  }

  public void shareAttachment()
  {
    if (this.mAttachment.contentUri == null)
      return;
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setFlags(524289);
    localIntent.putExtra("android.intent.extra.STREAM", Utils.normalizeUri(this.mAttachment.contentUri));
    localIntent.setType(Utils.normalizeMimeType(this.mAttachment.contentType));
    try
    {
      this.mContext.startActivity(localIntent);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      LogUtils.e(LOG_TAG, "Couldn't find Activity for intent", new Object[] { localActivityNotFoundException });
    }
  }

  public void shareAttachments(ArrayList<Parcelable> paramArrayList)
  {
    Intent localIntent = new Intent("android.intent.action.SEND_MULTIPLE");
    localIntent.setFlags(524289);
    localIntent.setType("image/*");
    localIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", paramArrayList);
    try
    {
      this.mContext.startActivity(localIntent);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      LogUtils.e(LOG_TAG, "Couldn't find Activity for intent", new Object[] { localActivityNotFoundException });
    }
  }

  public void showAndDownloadAttachments()
  {
    Iterator localIterator = this.mView.getAttachments().iterator();
    while (localIterator.hasNext())
    {
      Attachment localAttachment = (Attachment)localIterator.next();
      if (!localAttachment.isPresentLocally())
        startDownloadingAttachment(localAttachment, 0);
    }
    this.mView.viewAttachment();
  }

  public void showAttachment(int paramInt)
  {
    if ((this.mAttachment.isPresentLocally()) && ((paramInt == 0) || (this.mAttachment.destination == paramInt)))
    {
      this.mView.viewAttachment();
      return;
    }
    showDownloadingDialog(paramInt);
    startDownloadingAttachment(paramInt);
  }

  public void startDownloadingAttachment(int paramInt)
  {
    startDownloadingAttachment(this.mAttachment, paramInt);
  }

  public void startRedownloadingAttachment(Attachment paramAttachment)
  {
    showDownloadingDialog(paramAttachment.destination);
    ContentValues localContentValues = new ContentValues(2);
    localContentValues.put("state", Integer.valueOf(4));
    localContentValues.put("destination", Integer.valueOf(paramAttachment.destination));
    this.mCommandHandler.sendCommand(paramAttachment.uri, localContentValues);
  }

  public void updateStatus(boolean paramBoolean)
  {
    boolean bool1 = this.mAttachment.shouldShowProgress();
    final AttachmentProgressDialogFragment localAttachmentProgressDialogFragment = (AttachmentProgressDialogFragment)this.mFragmentManager.findFragmentByTag("attachment-progress");
    boolean bool2;
    if ((localAttachmentProgressDialogFragment != null) && (localAttachmentProgressDialogFragment.isShowingDialogForAttachment(this.mAttachment)))
    {
      localAttachmentProgressDialogFragment.setProgress(this.mAttachment.downloadedSize);
      if ((!bool1) && (localAttachmentProgressDialogFragment.isIndeterminate()))
      {
        bool2 = true;
        localAttachmentProgressDialogFragment.setIndeterminate(bool2);
        if ((paramBoolean) && (!this.mAttachment.isDownloading()))
          this.mHandler.post(new Runnable()
          {
            public void run()
            {
              localAttachmentProgressDialogFragment.dismiss();
            }
          });
        if (this.mAttachment.state == 3)
          this.mView.viewAttachment();
      }
    }
    while (true)
    {
      this.mView.onUpdateStatus();
      return;
      bool2 = false;
      break;
      this.mView.updateProgress(bool1);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.AttachmentActionHandler
 * JD-Core Version:    0.6.2
 */