package com.android.mail.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import java.util.ArrayList;
import java.util.Collection;

public class SingleFolderSelectionDialog extends FolderSelectionDialog
{
  public SingleFolderSelectionDialog(Context paramContext, Account paramAccount, ConversationUpdater paramConversationUpdater, Collection<Conversation> paramCollection, boolean paramBoolean, Folder paramFolder)
  {
    super(paramContext, paramAccount, paramConversationUpdater, paramCollection, paramBoolean, paramFolder);
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
  }

  protected void onListItemClick(int paramInt)
  {
    Object localObject = this.mAdapter.getItem(paramInt);
    if ((localObject instanceof FolderSelectorAdapter.FolderRow))
    {
      Folder localFolder = ((FolderSelectorAdapter.FolderRow)localObject).getFolder();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(new FolderOperation(this.mCurrentFolder, Boolean.valueOf(false)));
      localArrayList.add(new FolderOperation(localFolder, Boolean.valueOf(true)));
      this.mUpdater.assignFolder(localArrayList, this.mTarget, this.mBatch, true);
      this.mDialog.dismiss();
    }
  }

  // ERROR //
  protected void updateAdapterInBackground(Context paramContext)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 87	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   4: astore 4
    //   6: aload_0
    //   7: getfield 91	com/android/mail/ui/SingleFolderSelectionDialog:mAccount	Lcom/android/mail/providers/Account;
    //   10: getfield 97	com/android/mail/providers/Account:fullFolderListUri	Landroid/net/Uri;
    //   13: invokestatic 103	com/android/mail/utils/Utils:isEmpty	(Landroid/net/Uri;)Z
    //   16: ifne +117 -> 133
    //   19: aload_0
    //   20: getfield 91	com/android/mail/ui/SingleFolderSelectionDialog:mAccount	Lcom/android/mail/providers/Account;
    //   23: getfield 97	com/android/mail/providers/Account:fullFolderListUri	Landroid/net/Uri;
    //   26: astore 5
    //   28: aload 4
    //   30: aload 5
    //   32: getstatic 109	com/android/mail/providers/UIProvider:FOLDERS_PROJECTION	[Ljava/lang/String;
    //   35: aconst_null
    //   36: aconst_null
    //   37: aconst_null
    //   38: invokevirtual 115	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   41: astore 6
    //   43: aload 6
    //   45: astore_3
    //   46: aload_1
    //   47: invokevirtual 119	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   50: ldc 120
    //   52: invokevirtual 126	android/content/res/Resources:getStringArray	(I)[Ljava/lang/String;
    //   55: astore 7
    //   57: aload_0
    //   58: getfield 16	com/android/mail/ui/SingleFolderSelectionDialog:mAdapter	Lcom/android/mail/ui/SeparatedFolderListAdapter;
    //   61: new 128	com/android/mail/ui/SystemFolderSelectorAdapter
    //   64: dup
    //   65: aload_1
    //   66: aload_3
    //   67: ldc 129
    //   69: aconst_null
    //   70: aload_0
    //   71: getfield 39	com/android/mail/ui/SingleFolderSelectionDialog:mCurrentFolder	Lcom/android/mail/providers/Folder;
    //   74: invokespecial 132	com/android/mail/ui/SystemFolderSelectorAdapter:<init>	(Landroid/content/Context;Landroid/database/Cursor;ILjava/lang/String;Lcom/android/mail/providers/Folder;)V
    //   77: invokevirtual 136	com/android/mail/ui/SeparatedFolderListAdapter:addSection	(Lcom/android/mail/ui/FolderSelectorAdapter;)V
    //   80: aload_0
    //   81: getfield 16	com/android/mail/ui/SingleFolderSelectionDialog:mAdapter	Lcom/android/mail/ui/SeparatedFolderListAdapter;
    //   84: new 138	com/android/mail/ui/HierarchicalFolderSelectorAdapter
    //   87: dup
    //   88: aload_1
    //   89: aload_3
    //   90: invokestatic 144	com/android/mail/ui/AddableFolderSelectorAdapter:filterFolders	(Landroid/database/Cursor;)Landroid/database/Cursor;
    //   93: ldc 129
    //   95: aload 7
    //   97: iconst_2
    //   98: aaload
    //   99: aload_0
    //   100: getfield 39	com/android/mail/ui/SingleFolderSelectionDialog:mCurrentFolder	Lcom/android/mail/providers/Folder;
    //   103: invokespecial 145	com/android/mail/ui/HierarchicalFolderSelectorAdapter:<init>	(Landroid/content/Context;Landroid/database/Cursor;ILjava/lang/String;Lcom/android/mail/providers/Folder;)V
    //   106: invokevirtual 136	com/android/mail/ui/SeparatedFolderListAdapter:addSection	(Lcom/android/mail/ui/FolderSelectorAdapter;)V
    //   109: aload_0
    //   110: getfield 149	com/android/mail/ui/SingleFolderSelectionDialog:mBuilder	Landroid/app/AlertDialog$Builder;
    //   113: aload_0
    //   114: getfield 16	com/android/mail/ui/SingleFolderSelectionDialog:mAdapter	Lcom/android/mail/ui/SeparatedFolderListAdapter;
    //   117: aload_0
    //   118: invokevirtual 155	android/app/AlertDialog$Builder:setAdapter	(Landroid/widget/ListAdapter;Landroid/content/DialogInterface$OnClickListener;)Landroid/app/AlertDialog$Builder;
    //   121: pop
    //   122: aload_3
    //   123: ifnull +9 -> 132
    //   126: aload_3
    //   127: invokeinterface 160 1 0
    //   132: return
    //   133: aload_0
    //   134: getfield 91	com/android/mail/ui/SingleFolderSelectionDialog:mAccount	Lcom/android/mail/providers/Account;
    //   137: getfield 163	com/android/mail/providers/Account:folderListUri	Landroid/net/Uri;
    //   140: astore 5
    //   142: goto -114 -> 28
    //   145: astore_2
    //   146: aconst_null
    //   147: astore_3
    //   148: aload_3
    //   149: ifnull +9 -> 158
    //   152: aload_3
    //   153: invokeinterface 160 1 0
    //   158: aload_2
    //   159: athrow
    //   160: astore_2
    //   161: goto -13 -> 148
    //
    // Exception table:
    //   from	to	target	type
    //   0	28	145	finally
    //   28	43	145	finally
    //   133	142	145	finally
    //   46	122	160	finally
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.SingleFolderSelectionDialog
 * JD-Core Version:    0.6.2
 */