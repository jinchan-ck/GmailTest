package com.android.mail.browse;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.mail.providers.Account;
import com.android.mail.providers.AccountObserver;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.MailAppProvider;
import com.android.mail.providers.Settings;
import com.android.mail.providers.UIProvider.ConversationColumns;
import com.android.mail.ui.ControllableActivity;
import com.android.mail.ui.ConversationListCallbacks;
import com.android.mail.ui.ConversationSelectionSet;
import com.android.mail.ui.ConversationSetObserver;
import com.android.mail.ui.ConversationUpdater;
import com.android.mail.ui.DestructiveAction;
import com.android.mail.ui.FolderSelectionDialog;
import com.android.mail.ui.SwipeableListView;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.util.Collection;
import java.util.Iterator;

public class SelectedConversationsActionMenu
  implements ActionMode.Callback, ConversationSetObserver
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private Account mAccount;
  private AccountObserver mAccountObserver;
  private ActionMode mActionMode;
  private boolean mActivated = false;
  private final ControllableActivity mActivity;
  private final Context mContext;
  private final Folder mFolder;
  private final ConversationListCallbacks mListController;
  private Menu mMenu;
  protected final ConversationSelectionSet mSelectionSet;
  private final ConversationUpdater mUpdater;

  public SelectedConversationsActionMenu(ControllableActivity paramControllableActivity, ConversationSelectionSet paramConversationSelectionSet, Folder paramFolder, SwipeableListView paramSwipeableListView)
  {
    this.mActivity = paramControllableActivity;
    this.mListController = paramControllableActivity.getListHandler();
    this.mSelectionSet = paramConversationSelectionSet;
    this.mAccountObserver = new AccountObserver()
    {
      public void onChanged(Account paramAnonymousAccount)
      {
        SelectedConversationsActionMenu.access$002(SelectedConversationsActionMenu.this, paramAnonymousAccount);
      }
    };
    this.mAccount = this.mAccountObserver.initialize(paramControllableActivity.getAccountController());
    this.mFolder = paramFolder;
    this.mContext = this.mActivity.getActivityContext();
    this.mUpdater = paramControllableActivity.getConversationUpdater();
    FolderSelectionDialog.setDialogDismissed();
  }

  private void clearSelection()
  {
    this.mSelectionSet.clear();
  }

  private void destroy()
  {
    deactivate();
    this.mSelectionSet.removeObserver(this);
    clearSelection();
    this.mUpdater.refreshConversationList();
    if (this.mAccountObserver != null)
    {
      this.mAccountObserver.unregisterAndDestroy();
      this.mAccountObserver = null;
    }
  }

  private void destroy(int paramInt, Collection<Conversation> paramCollection, Collection<ConversationItemView> paramCollection1, DestructiveAction paramDestructiveAction)
  {
    this.mUpdater.delete(paramInt, paramCollection, paramCollection1, paramDestructiveAction);
  }

  private void markConversationsImportant(boolean paramBoolean)
  {
    Collection localCollection = this.mSelectionSet.values();
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      this.mUpdater.updateConversation(localCollection, UIProvider.ConversationColumns.PRIORITY, i);
      Iterator localIterator = localCollection.iterator();
      while (localIterator.hasNext())
        ((Conversation)localIterator.next()).priority = i;
    }
    updateSelection();
  }

  private void markConversationsRead(boolean paramBoolean)
  {
    Collection localCollection = this.mSelectionSet.values();
    this.mUpdater.markConversationsRead(localCollection, paramBoolean, false);
    updateSelection();
  }

  private void performDestructiveAction(final int paramInt)
  {
    final DestructiveAction localDestructiveAction = this.mUpdater.getDeferredBatchAction(paramInt);
    Settings localSettings = this.mAccount.settings;
    final Collection localCollection1 = this.mSelectionSet.values();
    final Collection localCollection2 = this.mSelectionSet.views();
    boolean bool;
    int i;
    if ((localSettings != null) && ((paramInt == 2131689753) || (paramInt == 2131689754)))
    {
      bool = localSettings.confirmDelete;
      if (!bool)
        break label181;
      switch (paramInt)
      {
      default:
        i = 2131755012;
      case 2131689753:
      case 2131689754:
      }
    }
    while (true)
    {
      String str = Utils.formatPlural(this.mContext, i, localCollection1.size());
      new AlertDialog.Builder(this.mContext).setMessage(str).setPositiveButton(2131427539, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          SelectedConversationsActionMenu.this.destroy(paramInt, localCollection1, localCollection2, localDestructiveAction);
        }
      }).setNegativeButton(2131427540, null).create().show();
      return;
      bool = localSettings.confirmArchive;
      break;
      i = 2131755011;
      continue;
      i = 2131755013;
    }
    label181: destroy(paramInt, localCollection1, localCollection2, localDestructiveAction);
  }

  private void starConversations(boolean paramBoolean)
  {
    Collection localCollection = this.mSelectionSet.values();
    this.mUpdater.updateConversation(localCollection, UIProvider.ConversationColumns.STARRED, paramBoolean);
    Iterator localIterator = localCollection.iterator();
    while (localIterator.hasNext())
      ((Conversation)localIterator.next()).starred = paramBoolean;
    updateSelection();
  }

  private void updateCount()
  {
    if (this.mActionMode != null)
    {
      ActionMode localActionMode = this.mActionMode;
      Context localContext = this.mContext;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(this.mSelectionSet.size());
      localActionMode.setTitle(localContext.getString(2131427476, arrayOfObject));
    }
  }

  private void updateSelection()
  {
    this.mUpdater.refreshConversationList();
    if (this.mActionMode != null)
      onPrepareActionMode(this.mActionMode, this.mActionMode.getMenu());
  }

  public void activate()
  {
    if (this.mSelectionSet.isEmpty());
    do
    {
      return;
      this.mActivated = true;
    }
    while (this.mActionMode != null);
    this.mActivity.startActionMode(this);
  }

  public void deactivate()
  {
    if (this.mActionMode != null)
    {
      this.mActivated = false;
      this.mActionMode.finish();
    }
  }

  public boolean isActivated()
  {
    return this.mActivated;
  }

  public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem)
  {
    boolean bool1 = true;
    this.mListController.commitDestructiveActions(true);
    switch (paramMenuItem.getItemId())
    {
    default:
      bool1 = false;
    case 2131689753:
    case 2131689754:
    case 2131689751:
    case 2131689752:
    case 2131689759:
    case 2131689760:
    case 2131689761:
    case 2131689762:
    case 2131689767:
    case 2131689522:
    case 2131689558:
    case 2131689768:
    case 2131689755:
      FolderSelectionDialog localFolderSelectionDialog;
      do
      {
        Account localAccount;
        int i;
        do
        {
          return bool1;
          performDestructiveAction(2131689753);
          return bool1;
          performDestructiveAction(2131689754);
          return bool1;
          performDestructiveAction(2131689751);
          return bool1;
          destroy(2131689752, this.mSelectionSet.values(), this.mSelectionSet.views(), this.mUpdater.getDeferredRemoveFolder(this.mSelectionSet.values(), this.mFolder, true, true, true));
          return bool1;
          this.mUpdater.delete(2131689759, this.mSelectionSet.values(), this.mUpdater.getBatchAction(2131689759));
          return bool1;
          this.mUpdater.delete(2131689760, this.mSelectionSet.values(), this.mUpdater.getBatchAction(2131689760));
          return bool1;
          this.mUpdater.delete(2131689761, this.mSelectionSet.values(), this.mUpdater.getBatchAction(2131689761));
          return bool1;
          this.mUpdater.delete(2131689762, this.mSelectionSet.values(), this.mUpdater.getBatchAction(2131689762));
          return bool1;
          markConversationsRead(true);
          return bool1;
          markConversationsRead(false);
          return bool1;
          starConversations(true);
          return bool1;
          if (this.mFolder.type == 7)
          {
            LogUtils.d(LOG_TAG, "We are in a starred folder, removing the star", new Object[0]);
            performDestructiveAction(2131689768);
            return bool1;
          }
          LogUtils.d(LOG_TAG, "Not in a starred folder.", new Object[0]);
          starConversations(false);
          return bool1;
          localAccount = this.mAccount;
          boolean bool2 = this.mFolder.supportsCapability(4096);
          i = 0;
          if (bool2)
          {
            Uri localUri = null;
            Iterator localIterator = this.mSelectionSet.values().iterator();
            Conversation localConversation;
            do
              while (true)
              {
                boolean bool3 = localIterator.hasNext();
                i = 0;
                if (!bool3)
                  break label549;
                localConversation = (Conversation)localIterator.next();
                if (localUri != null)
                  break;
                localUri = localConversation.accountUri;
              }
            while (localUri.equals(localConversation.accountUri));
            Toast.makeText(this.mContext, 2131427571, 1).show();
            i = 1;
            if (i == 0)
              localAccount = MailAppProvider.getAccountFromAccountUri(localUri);
          }
        }
        while (i != 0);
        localFolderSelectionDialog = FolderSelectionDialog.getInstance(this.mContext, localAccount, this.mUpdater, this.mSelectionSet.values(), true, this.mFolder);
      }
      while (localFolderSelectionDialog == null);
      localFolderSelectionDialog.show();
      return bool1;
    case 2131689757:
      label549: markConversationsImportant(true);
      return bool1;
    case 2131689758:
    }
    if (this.mFolder.supportsCapability(1024))
    {
      performDestructiveAction(2131689758);
      return bool1;
    }
    markConversationsImportant(false);
    return bool1;
  }

  public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu)
  {
    this.mSelectionSet.addObserver(this);
    this.mActivity.getMenuInflater().inflate(2131820548, paramMenu);
    this.mActionMode = paramActionMode;
    this.mMenu = paramMenu;
    updateCount();
    return true;
  }

  public void onDestroyActionMode(ActionMode paramActionMode)
  {
    this.mActionMode = null;
    if (this.mActivated)
    {
      destroy();
      this.mActivity.getListHandler().commitDestructiveActions(true);
    }
    this.mMenu = null;
  }

  public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu)
  {
    Collection localCollection = this.mSelectionSet.values();
    boolean bool1 = false;
    boolean bool2 = false;
    int i = 0;
    int j = 0;
    int k = 0;
    Iterator localIterator = localCollection.iterator();
    do
    {
      if (!localIterator.hasNext())
        break;
      Conversation localConversation = (Conversation)localIterator.next();
      if (!localConversation.starred)
        bool1 = true;
      if (localConversation.read)
        bool2 = true;
      if (!localConversation.isImportant())
        i = 1;
      if (localConversation.spam)
        j = 1;
      if (!localConversation.phishing)
        k = 1;
    }
    while ((!bool1) || (!bool2) || (i == 0) || (j == 0) || (k == 0));
    paramMenu.findItem(2131689558).setVisible(bool1);
    MenuItem localMenuItem1 = paramMenu.findItem(2131689768);
    boolean bool3;
    boolean bool4;
    label198: boolean bool5;
    label278: MenuItem localMenuItem4;
    boolean bool7;
    label388: label396: boolean bool8;
    label486: boolean bool9;
    label539: boolean bool10;
    label592: boolean bool15;
    label651: boolean bool11;
    label693: boolean bool12;
    label735: boolean bool13;
    if (!bool1)
    {
      bool3 = true;
      localMenuItem1.setVisible(bool3);
      MenuItem localMenuItem2 = paramMenu.findItem(2131689767);
      if (bool2)
        break label854;
      bool4 = true;
      localMenuItem2.setVisible(bool4);
      paramMenu.findItem(2131689522).setVisible(bool2);
      MenuItem localMenuItem3 = paramMenu.findItem(2131689752);
      if ((this.mFolder == null) || (this.mFolder.type != 0) || (!this.mFolder.supportsCapability(8)) || (this.mFolder.isProviderFolder()))
        break label860;
      bool5 = true;
      localMenuItem3.setVisible(bool5);
      if ((this.mFolder != null) && (bool5))
      {
        Context localContext = this.mActivity.getActivityContext();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mFolder.name;
        localMenuItem3.setTitle(localContext.getString(2131427392, arrayOfObject));
      }
      localMenuItem4 = paramMenu.findItem(2131689751);
      boolean bool6 = this.mAccount.supportsCapability(8);
      if ((!bool6) || (!this.mFolder.supportsCapability(16)))
        break label866;
      bool7 = true;
      if (localMenuItem4 != null)
        break label872;
      bool7 = false;
      if ((!bool7) && (bool6) && (!bool5) && (Utils.shouldShowDisabledArchiveIcon(this.mActivity.getActivityContext())))
      {
        localMenuItem4.setEnabled(false);
        localMenuItem4.setVisible(true);
      }
      MenuItem localMenuItem5 = paramMenu.findItem(2131689760);
      if ((j != 0) || (!this.mAccount.supportsCapability(2)) || (!this.mFolder.supportsCapability(64)))
        break label885;
      bool8 = true;
      localMenuItem5.setVisible(bool8);
      MenuItem localMenuItem6 = paramMenu.findItem(2131689761);
      if ((j == 0) || (!this.mAccount.supportsCapability(2)) || (!this.mFolder.supportsCapability(128)))
        break label891;
      bool9 = true;
      localMenuItem6.setVisible(bool9);
      MenuItem localMenuItem7 = paramMenu.findItem(2131689762);
      if ((k == 0) || (!this.mAccount.supportsCapability(4)) || (!this.mFolder.supportsCapability(8192)))
        break label897;
      bool10 = true;
      localMenuItem7.setVisible(bool10);
      MenuItem localMenuItem8 = paramMenu.findItem(2131689759);
      if (localMenuItem8 != null)
      {
        if ((!this.mAccount.supportsCapability(16)) || (this.mFolder == null) || (this.mFolder.type != 1))
          break label903;
        bool15 = true;
        localMenuItem8.setVisible(bool15);
      }
      MenuItem localMenuItem9 = paramMenu.findItem(2131689757);
      if ((i == 0) || (!this.mAccount.supportsCapability(131072)))
        break label909;
      bool11 = true;
      localMenuItem9.setVisible(bool11);
      MenuItem localMenuItem10 = paramMenu.findItem(2131689758);
      if ((i != 0) || (!this.mAccount.supportsCapability(131072)))
        break label915;
      bool12 = true;
      localMenuItem10.setVisible(bool12);
      if ((this.mFolder == null) || (!this.mFolder.supportsCapability(32)))
        break label921;
      bool13 = true;
      label767: paramMenu.findItem(2131689753).setVisible(bool13);
      if ((bool13) || (this.mFolder == null) || (!this.mFolder.isDraft()) || (!this.mAccount.supportsCapability(1048576)))
        break label927;
    }
    label897: label903: label909: label915: label921: label927: for (boolean bool14 = true; ; bool14 = false)
    {
      MenuItem localMenuItem11 = paramMenu.findItem(2131689754);
      if (localMenuItem11 != null)
        localMenuItem11.setVisible(bool14);
      return true;
      bool3 = false;
      break;
      label854: bool4 = false;
      break label198;
      label860: bool5 = false;
      break label278;
      label866: bool7 = false;
      break label388;
      label872: localMenuItem4.setVisible(bool7);
      break label396;
      label885: bool8 = false;
      break label486;
      label891: bool9 = false;
      break label539;
      bool10 = false;
      break label592;
      bool15 = false;
      break label651;
      bool11 = false;
      break label693;
      bool12 = false;
      break label735;
      bool13 = false;
      break label767;
    }
  }

  public void onSetChanged(ConversationSelectionSet paramConversationSelectionSet)
  {
    if (paramConversationSelectionSet.isEmpty())
      return;
    updateCount();
  }

  public void onSetEmpty()
  {
    LogUtils.d(LOG_TAG, "onSetEmpty called.", new Object[0]);
    destroy();
  }

  public void onSetPopulated(ConversationSelectionSet paramConversationSelectionSet)
  {
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.SelectedConversationsActionMenu
 * JD-Core Version:    0.6.2
 */