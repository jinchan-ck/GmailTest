package com.google.android.gm;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gm.downloadprovider.Downloads;
import com.google.android.gm.provider.Gmail;

public class ViewAttachmentActivity extends GmailBaseActivity
{
  private static String[] ATTACHMENT_PROJECTION = { "_id", "status", "filename", "saveToSd" };
  private static final int ERROR_DIALOG_ID = 2;
  public static String EXTRA_ON_DOWNLOADED_ACTION = "onDownloadAction";
  public static String ON_DOWNLOADED_ACTION_FINISH = "return";
  private static final int WAIT_DIALOG_ID = 1;
  private boolean mCancelDownloadWhenDismissingWaitDialog = true;
  private ContentObserver mContentObserver = new ContentObserver(new Handler())
  {
    public void onChange(boolean paramAnonymousBoolean)
    {
      if (ViewAttachmentActivity.this.mCursor != null)
        ViewAttachmentActivity.this.requeryAndInspectCursor(ViewAttachmentActivity.this.mCursor);
    }
  };
  private int mCurrentQueryToken = 0;
  private Cursor mCursor;
  private boolean mFinishWhenDismissingWaitDialog = true;
  private AttachmentQueryHandler mQueryHandler;

  private void cancelDownloadInBackground()
  {
    new AsyncTask()
    {
      protected Void doInBackground(Void[] paramAnonymousArrayOfVoid)
      {
        Uri localUri = Gmail.getAttachmentDownloadUri(ViewAttachmentActivity.this.getIntent().getData());
        ViewAttachmentActivity.this.getContentResolver().delete(localUri, null, null);
        return null;
      }
    }
    .execute(new Void[0]);
  }

  private void closeCursor()
  {
    if (this.mCursor != null)
    {
      this.mCursor.unregisterContentObserver(this.mContentObserver);
      this.mCursor.close();
      this.mCursor = null;
    }
  }

  private void initializeCursor()
  {
    Uri localUri = Gmail.getAttachmentDownloadUri(getIntent().getData());
    AttachmentQueryHandler localAttachmentQueryHandler = this.mQueryHandler;
    int i = 1 + this.mCurrentQueryToken;
    this.mCurrentQueryToken = i;
    localAttachmentQueryHandler.startQuery(i, null, localUri, ATTACHMENT_PROJECTION, null, null, null);
  }

  private void inspectCursor()
  {
    if (this.mCursor == null);
    int i;
    label158: 
    do
    {
      return;
      if (!this.mCursor.moveToPosition(0))
      {
        Log.w("Gmail", "queryAndDownloadAttachment returned an empty cursor");
        initializeCursor();
        return;
      }
      i = this.mCursor.getInt(this.mCursor.getColumnIndexOrThrow("status"));
      if (Downloads.isStatusCompleted(i))
        this.mCancelDownloadWhenDismissingWaitDialog = false;
      if (Downloads.isStatusSuccess(i))
      {
        String str = this.mCursor.getString(this.mCursor.getColumnIndexOrThrow("filename"));
        int j;
        Uri localUri1;
        if (this.mCursor.getInt(this.mCursor.getColumnIndexOrThrow("saveToSd")) != 0)
        {
          j = 1;
          localUri1 = getIntent().getData();
          if ((!TextUtils.isEmpty(str)) && (j != 0))
            break label158;
        }
        for (Uri localUri2 = localUri1; ; localUri2 = Uri.parse(str))
        {
          openAttachmentAndFinish(localUri2, localUri1);
          return;
          j = 0;
          break;
        }
      }
    }
    while (!Downloads.isStatusError(i));
    this.mFinishWhenDismissingWaitDialog = false;
    dismissDialog(1);
    showDialog(2);
  }

  private void openAttachmentAndFinish(Uri paramUri1, Uri paramUri2)
  {
    String str1 = getIntent().getStringExtra(EXTRA_ON_DOWNLOADED_ACTION);
    if (str1 == null)
    {
      str2 = getContentResolver().getType(paramUri2);
      localIntent = new Intent("android.intent.action.VIEW");
      localIntent.setFlags(524289);
      localIntent.setDataAndType(paramUri1, str2);
      startActivity(localIntent);
    }
    while (ON_DOWNLOADED_ACTION_FINISH.equals(str1))
    {
      String str2;
      Intent localIntent;
      finish();
      return;
    }
    throw new IllegalArgumentException("EXTRA_ON_DOWNLOADED_ACTION set to unknown action: " + str1);
  }

  private void requeryAndInspectCursor(final Cursor paramCursor)
  {
    new AsyncTask()
    {
      protected Void doInBackground(Void[] paramAnonymousArrayOfVoid)
      {
        paramCursor.requery();
        return null;
      }

      protected void onPostExecute(Void paramAnonymousVoid)
      {
        ViewAttachmentActivity.this.inspectCursor();
      }
    }
    .execute(new Void[0]);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    showDialog(1);
    this.mQueryHandler = new AttachmentQueryHandler();
  }

  protected Dialog onCreateDialog(int paramInt)
  {
    if (paramInt == 1)
    {
      ProgressDialog localProgressDialog = new ProgressDialog(this);
      localProgressDialog.setTitle(2131296424);
      localProgressDialog.setMessage(getResources().getString(2131296425));
      localProgressDialog.setIndeterminate(true);
      localProgressDialog.setCancelable(true);
      localProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
      {
        public void onDismiss(DialogInterface paramAnonymousDialogInterface)
        {
          if (ViewAttachmentActivity.this.mCancelDownloadWhenDismissingWaitDialog)
            ViewAttachmentActivity.this.cancelDownloadInBackground();
          if (ViewAttachmentActivity.this.mFinishWhenDismissingWaitDialog)
            ViewAttachmentActivity.this.finish();
        }
      });
      return localProgressDialog;
    }
    if (paramInt == 2)
      return new AlertDialog.Builder(this).setPositiveButton(2131296282, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          ViewAttachmentActivity.this.dismissDialog(2);
          ViewAttachmentActivity.this.finish();
        }
      }).setMessage("").create();
    throw new AssertionError("Unknown dialog ID:" + paramInt);
  }

  protected void onPrepareDialog(int paramInt, Dialog paramDialog)
  {
    int i;
    if (paramInt == 2)
      switch (this.mCursor.getInt(this.mCursor.getColumnIndexOrThrow("status")))
      {
      default:
        i = 2131296427;
      case 406:
      case 499:
      case 498:
      }
    while (true)
    {
      setTitle(2131296426);
      ((AlertDialog)paramDialog).setMessage(getResources().getString(i));
      return;
      i = 2131296428;
      continue;
      i = 2131296429;
      continue;
      i = 2131296430;
    }
  }

  protected void onStart()
  {
    super.onStart();
    Uri localUri = getIntent().getData();
    if (!"gmail-ls".equals(localUri.getAuthority()))
    {
      openAttachmentAndFinish(localUri, localUri);
      return;
    }
    initializeCursor();
  }

  protected void onStop()
  {
    super.onStop();
    dismissDialog(1);
    closeCursor();
  }

  private class AttachmentQueryHandler extends AsyncQueryHandler
  {
    public AttachmentQueryHandler()
    {
      super();
    }

    protected void onQueryComplete(int paramInt, Object paramObject, Cursor paramCursor)
    {
      ViewAttachmentActivity.this.closeCursor();
      ViewAttachmentActivity.access$102(ViewAttachmentActivity.this, paramCursor);
      if (ViewAttachmentActivity.this.mCursor != null)
      {
        ViewAttachmentActivity.this.mCursor.registerContentObserver(ViewAttachmentActivity.this.mContentObserver);
        ViewAttachmentActivity.this.requeryAndInspectCursor(ViewAttachmentActivity.this.mCursor);
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ViewAttachmentActivity
 * JD-Core Version:    0.6.2
 */