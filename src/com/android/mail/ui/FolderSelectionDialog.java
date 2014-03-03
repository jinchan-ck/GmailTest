package com.android.mail.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import java.util.Collection;

public abstract class FolderSelectionDialog
  implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener
{
  protected static final String LOG_TAG = LogTag.getLogTag();
  private static boolean sDialogShown;
  protected Account mAccount;
  protected SeparatedFolderListAdapter mAdapter;
  protected boolean mBatch;
  protected AlertDialog.Builder mBuilder;
  protected Folder mCurrentFolder;
  protected AlertDialog mDialog;
  protected QueryRunner mRunner;
  protected Collection<Conversation> mTarget;
  protected ConversationUpdater mUpdater;

  protected FolderSelectionDialog(Context paramContext, Account paramAccount, ConversationUpdater paramConversationUpdater, Collection<Conversation> paramCollection, boolean paramBoolean, Folder paramFolder)
  {
    this.mUpdater = paramConversationUpdater;
    this.mTarget = paramCollection;
    this.mBatch = paramBoolean;
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
    localBuilder.setTitle(2131427555);
    localBuilder.setPositiveButton(2131427539, this);
    localBuilder.setNegativeButton(2131427540, this);
    this.mAccount = paramAccount;
    this.mBuilder = localBuilder;
    this.mCurrentFolder = paramFolder;
    this.mAdapter = new SeparatedFolderListAdapter(paramContext);
    this.mRunner = new QueryRunner(paramContext, null);
  }

  public static FolderSelectionDialog getInstance(Context paramContext, Account paramAccount, ConversationUpdater paramConversationUpdater, Collection<Conversation> paramCollection, boolean paramBoolean, Folder paramFolder)
  {
    if (sDialogShown)
      return null;
    if (paramAccount.supportsCapability(8192))
      return new MultiFoldersSelectionDialog(paramContext, paramAccount, paramConversationUpdater, paramCollection, paramBoolean, paramFolder);
    return new SingleFolderSelectionDialog(paramContext, paramAccount, paramConversationUpdater, paramCollection, paramBoolean, paramFolder);
  }

  public static void setDialogDismissed()
  {
    LogUtils.d(LOG_TAG, "Folder Selection dialog dismissed", new Object[0]);
    sDialogShown = false;
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    setDialogDismissed();
  }

  protected abstract void onListItemClick(int paramInt);

  public void show()
  {
    sDialogShown = true;
    this.mRunner.execute(new Void[0]);
  }

  protected void showInternal()
  {
    this.mDialog.show();
    this.mDialog.setOnDismissListener(this);
    this.mDialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        FolderSelectionDialog.this.onListItemClick(paramAnonymousInt);
      }
    });
  }

  protected abstract void updateAdapterInBackground(Context paramContext);

  private class QueryRunner extends AsyncTask<Void, Void, Void>
  {
    private final Context mContext;

    private QueryRunner(Context arg2)
    {
      Object localObject;
      this.mContext = localObject;
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      FolderSelectionDialog.this.updateAdapterInBackground(this.mContext);
      return null;
    }

    protected void onPostExecute(Void paramVoid)
    {
      FolderSelectionDialog.this.mDialog = FolderSelectionDialog.this.mBuilder.create();
      FolderSelectionDialog.this.showInternal();
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.FolderSelectionDialog
 * JD-Core Version:    0.6.2
 */