package com.android.mail.ui;

import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.UIProvider;
import com.android.mail.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MultiFoldersSelectionDialog extends FolderSelectionDialog
{
  private final HashMap<Uri, FolderOperation> mOperations;
  private final boolean mSingle;

  public MultiFoldersSelectionDialog(Context paramContext, Account paramAccount, ConversationUpdater paramConversationUpdater, Collection<Conversation> paramCollection, boolean paramBoolean, Folder paramFolder)
  {
    super(paramContext, paramAccount, paramConversationUpdater, paramCollection, paramBoolean, paramFolder);
    if (!paramAccount.supportsCapability(8192));
    for (boolean bool = true; ; bool = false)
    {
      this.mSingle = bool;
      this.mOperations = new HashMap();
      return;
    }
  }

  private final void update(FolderSelectorAdapter.FolderRow paramFolderRow)
  {
    boolean bool;
    if (!paramFolderRow.isPresent())
      bool = true;
    while (this.mSingle)
      if (!bool)
      {
        return;
        bool = false;
      }
      else
      {
        int i = 0;
        int j = this.mAdapter.getCount();
        while (i < j)
        {
          Object localObject = this.mAdapter.getItem(i);
          if ((localObject instanceof FolderSelectorAdapter.FolderRow))
          {
            ((FolderSelectorAdapter.FolderRow)localObject).setIsPresent(false);
            Folder localFolder2 = ((FolderSelectorAdapter.FolderRow)localObject).getFolder();
            this.mOperations.put(localFolder2.uri, new FolderOperation(localFolder2, Boolean.valueOf(false)));
          }
          i++;
        }
      }
    paramFolderRow.setIsPresent(bool);
    this.mAdapter.notifyDataSetChanged();
    Folder localFolder1 = paramFolderRow.getFolder();
    this.mOperations.put(localFolder1.uri, new FolderOperation(localFolder1, Boolean.valueOf(bool)));
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    switch (paramInt)
    {
    case -2:
    default:
    case -1:
    }
    do
      return;
    while (this.mUpdater == null);
    this.mUpdater.assignFolder(this.mOperations.values(), this.mTarget, this.mBatch, true);
  }

  protected void onListItemClick(int paramInt)
  {
    Object localObject = this.mAdapter.getItem(paramInt);
    if ((localObject instanceof FolderSelectorAdapter.FolderRow))
      update((FolderSelectorAdapter.FolderRow)localObject);
  }

  protected void updateAdapterInBackground(Context paramContext)
  {
    Cursor localCursor = null;
    HashSet localHashSet;
    while (true)
    {
      try
      {
        ContentResolver localContentResolver = paramContext.getContentResolver();
        boolean bool = Utils.isEmpty(this.mAccount.fullFolderListUri);
        localCursor = null;
        if (!bool)
        {
          localUri = this.mAccount.fullFolderListUri;
          localCursor = localContentResolver.query(localUri, UIProvider.FOLDERS_PROJECTION, null, null, null);
          localHashSet = new HashSet();
          Iterator localIterator = this.mTarget.iterator();
          if (!localIterator.hasNext())
            break;
          Conversation localConversation = (Conversation)localIterator.next();
          ArrayList localArrayList = localConversation.getRawFolders();
          if ((localConversation == null) || (localArrayList == null) || (localArrayList.size() <= 0))
            break label159;
          localHashSet.addAll(Arrays.asList(Folder.getUriArray(localArrayList)));
          continue;
        }
      }
      finally
      {
        if (localCursor != null)
          localCursor.close();
      }
      Uri localUri = this.mAccount.folderListUri;
      continue;
      label159: localHashSet.add(this.mCurrentFolder.uri.toString());
    }
    this.mAdapter.addSection(new AddableFolderSelectorAdapter(paramContext, AddableFolderSelectorAdapter.filterFolders(localCursor), localHashSet, 2130968657, null));
    this.mBuilder.setAdapter(this.mAdapter, this);
    if (localCursor != null)
      localCursor.close();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.MultiFoldersSelectionDialog
 * JD-Core Version:    0.6.2
 */