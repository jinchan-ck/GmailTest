package com.android.mail.browse;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import com.android.mail.providers.Attachment;
import com.android.mail.utils.LogTag;
import com.google.common.base.Objects;

public class AttachmentProgressDialogFragment extends DialogFragment
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private Attachment mAttachment;
  private AttachmentCommandHandler mCommandHandler;
  private ProgressDialog mDialog;

  static AttachmentProgressDialogFragment newInstance(Attachment paramAttachment, int paramInt)
  {
    AttachmentProgressDialogFragment localAttachmentProgressDialogFragment = new AttachmentProgressDialogFragment();
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("attachment", paramAttachment);
    localAttachmentProgressDialogFragment.setArguments(localBundle);
    return localAttachmentProgressDialogFragment;
  }

  public void cancelAttachment()
  {
    ContentValues localContentValues = new ContentValues(1);
    localContentValues.put("state", Integer.valueOf(0));
    this.mCommandHandler.sendCommand(this.mAttachment.uri, localContentValues);
  }

  public boolean isIndeterminate()
  {
    return (this.mDialog != null) && (this.mDialog.isIndeterminate());
  }

  public boolean isShowingDialogForAttachment(Attachment paramAttachment)
  {
    return (getDialog() != null) && (Objects.equal(paramAttachment.uri, this.mAttachment.uri));
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mCommandHandler = new AttachmentCommandHandler(getActivity());
  }

  public void onCancel(DialogInterface paramDialogInterface)
  {
    this.mDialog = null;
    cancelAttachment();
    super.onCancel(paramDialogInterface);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mAttachment = ((Attachment)getArguments().getParcelable("attachment"));
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    this.mDialog = new ProgressDialog(getActivity());
    this.mDialog.setTitle(2131427453);
    this.mDialog.setMessage(this.mAttachment.name);
    this.mDialog.setProgressStyle(1);
    this.mDialog.setIndeterminate(true);
    this.mDialog.setMax(this.mAttachment.size);
    this.mDialog.setProgressNumberFormat(null);
    return this.mDialog;
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    this.mDialog = null;
    super.onDismiss(paramDialogInterface);
  }

  public void setIndeterminate(boolean paramBoolean)
  {
    if (this.mDialog != null)
      this.mDialog.setIndeterminate(paramBoolean);
  }

  public void setProgress(int paramInt)
  {
    if (this.mDialog != null)
      this.mDialog.setProgress(paramInt);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.AttachmentProgressDialogFragment
 * JD-Core Version:    0.6.2
 */