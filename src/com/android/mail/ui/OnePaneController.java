package com.android.mail.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import com.android.mail.ConversationListContext;
import com.android.mail.browse.ConversationPagerController;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.Settings;
import com.android.mail.utils.LogUtils;

public final class OnePaneController extends AbstractActivityController
{
  private boolean mConversationListNeverShown = true;
  private boolean mConversationListVisible = false;
  private Folder mInbox;
  private int mLastConversationListTransactionId = -1;
  private int mLastConversationTransactionId = -1;
  private int mLastFolderListTransactionId = -1;
  private int mLastInboxConversationListTransactionId = -1;

  public OnePaneController(MailActivity paramMailActivity, ViewMode paramViewMode)
  {
    super(paramMailActivity, paramViewMode);
  }

  private void goUpFolderHierarchy(Folder paramFolder)
  {
    Folder localFolder = paramFolder.parent;
    if (localFolder != null)
    {
      setHierarchyFolder(localFolder);
      this.mLastFolderListTransactionId = replaceFragment(FolderListFragment.newInstance(localFolder, localFolder.childFoldersListUri, false), 4097, "tag-folder-list");
      this.mActionBarView.setBackButton();
      return;
    }
    showFolderList();
  }

  private static boolean inInbox(Account paramAccount, ConversationListContext paramConversationListContext)
  {
    if ((paramAccount == null) || (paramConversationListContext == null) || (paramConversationListContext.folder == null) || (paramAccount.settings == null));
    while ((ConversationListContext.isSearchResult(paramConversationListContext)) || (!isDefaultInbox(paramConversationListContext.folder.uri, paramAccount)))
      return false;
    return true;
  }

  private static final boolean isDefaultInbox(Uri paramUri, Account paramAccount)
  {
    if ((paramUri == null) || (paramAccount == null))
      return false;
    Settings.getDefaultInboxUri(paramAccount.settings);
    return paramUri.equals(paramAccount.settings.defaultInbox);
  }

  private boolean isTransactionIdValid(int paramInt)
  {
    return paramInt >= 0;
  }

  private int replaceFragment(Fragment paramFragment, int paramInt, String paramString)
  {
    FragmentTransaction localFragmentTransaction = this.mActivity.getFragmentManager().beginTransaction();
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.setTransition(paramInt);
    localFragmentTransaction.replace(2131689648, paramFragment, paramString);
    return localFragmentTransaction.commitAllowingStateLoss();
  }

  private void safelyPopBackStack(int paramInt, boolean paramBoolean)
  {
    PopBackStackRunnable localPopBackStackRunnable = new PopBackStackRunnable(paramInt);
    if (paramBoolean)
    {
      this.mHandler.post(localPopBackStackRunnable);
      return;
    }
    if (safeToModifyFragments())
    {
      localPopBackStackRunnable.popBackStack();
      return;
    }
    LogUtils.i(LOG_TAG, "Activity has been saved; ignoring unsafe immediate request to pop back stack", new Object[0]);
  }

  private void transitionBackToConversationListMode(boolean paramBoolean)
  {
    int i = this.mViewMode.getMode();
    enableCabMode();
    if (i == 5)
    {
      this.mViewMode.enterSearchResultsListMode();
      if (!isTransactionIdValid(this.mLastConversationListTransactionId))
        break label72;
      safelyPopBackStack(this.mLastConversationListTransactionId, paramBoolean);
    }
    while (true)
    {
      this.mConversationListVisible = true;
      onConversationVisibilityChanged(false);
      onConversationListVisibilityChanged(true);
      return;
      this.mViewMode.enterConversationListMode();
      break;
      label72: if (isTransactionIdValid(this.mLastInboxConversationListTransactionId))
      {
        safelyPopBackStack(this.mLastInboxConversationListTransactionId, paramBoolean);
        onFolderChanged(this.mInbox);
      }
      else
      {
        ConversationListContext localConversationListContext = ConversationListContext.forFolder(this.mAccount, this.mInbox);
        onFolderChanged(this.mInbox);
        showConversationList(localConversationListContext);
      }
    }
  }

  private void transitionToInbox()
  {
    this.mViewMode.enterConversationListMode();
    if (!isDefaultInbox(this.mConvListContext.folder.uri, this.mAccount));
    for (int i = 1; (this.mInbox == null) || (i != 0); i = 0)
    {
      loadAccountInbox();
      return;
    }
    ConversationListContext localConversationListContext = ConversationListContext.forFolder(this.mAccount, this.mInbox);
    onFolderChanged(this.mInbox);
    showConversationList(localConversationListContext);
  }

  public String getHelpContext()
  {
    switch (this.mViewMode.getMode())
    {
    default:
      return super.getHelpContext();
    case 3:
    }
    return this.mContext.getString(2131427339);
  }

  protected void hideOrRepositionToastBar(boolean paramBoolean)
  {
    this.mToastBar.hide(paramBoolean);
  }

  protected void hideWaitForInitialization()
  {
    transitionToInbox();
    super.hideWaitForInitialization();
  }

  protected boolean isConversationListVisible()
  {
    return this.mConversationListVisible;
  }

  public void onAccountChanged(Account paramAccount)
  {
    super.onAccountChanged(paramAccount);
    this.mConversationListNeverShown = true;
  }

  public boolean onBackPressed()
  {
    int i = this.mViewMode.getMode();
    if (i == 3)
    {
      Folder localFolder = getHierarchyFolder();
      FolderListFragment localFolderListFragment = getFolderListFragment();
      if ((localFolderListFragment != null) && (localFolderListFragment.showingHierarchy()) && (localFolder != null))
        goUpFolderHierarchy(localFolder);
    }
    while (true)
    {
      this.mToastBar.hide(false);
      return true;
      this.mLastFolderListTransactionId = -1;
      transitionToInbox();
      continue;
      if (i == 4)
        this.mActivity.finish();
      else if ((this.mViewMode.isListMode()) && (!inInbox(this.mAccount, this.mConvListContext)))
      {
        if (this.mLastFolderListTransactionId != -1)
        {
          this.mViewMode.enterFolderListMode();
          this.mActivity.getFragmentManager().popBackStack(this.mLastFolderListTransactionId, 0);
        }
        else
        {
          transitionToInbox();
        }
      }
      else if ((i == 1) || (i == 5))
        transitionBackToConversationListMode(false);
      else
        this.mActivity.finish();
    }
  }

  public boolean onCreate(Bundle paramBundle)
  {
    this.mActivity.setContentView(2130968660);
    return super.onCreate(paramBundle);
  }

  public void onError(Folder paramFolder, boolean paramBoolean)
  {
    switch (this.mViewMode.getMode())
    {
    case 3:
    default:
      return;
    case 2:
    case 4:
    }
    showErrorToast(paramFolder, paramBoolean);
  }

  public void onFolderSelected(Folder paramFolder)
  {
    if ((paramFolder.hasChildren) && (!paramFolder.equals(getHierarchyFolder())))
    {
      this.mViewMode.enterFolderListMode();
      setHierarchyFolder(paramFolder);
      this.mLastFolderListTransactionId = replaceFragment(FolderListFragment.newInstance(paramFolder, paramFolder.childFoldersListUri, false), 4097, "tag-folder-list");
      this.mActionBarView.setBackButton();
      return;
    }
    super.onFolderSelected(paramFolder);
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    if (paramBundle == null)
      return;
    this.mLastFolderListTransactionId = paramBundle.getInt("folder-list-transaction", -1);
    this.mLastInboxConversationListTransactionId = paramBundle.getInt("inbox_conversation-list-transaction", -1);
    this.mLastConversationListTransactionId = paramBundle.getInt("conversation-list-transaction", -1);
    this.mLastConversationTransactionId = paramBundle.getInt("conversation-transaction", -1);
    this.mConversationListVisible = paramBundle.getBoolean("conversation-list-visible");
    this.mConversationListNeverShown = paramBundle.getBoolean("conversation-list-never-shown");
    this.mInbox = ((Folder)paramBundle.getParcelable("m-inbox"));
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("folder-list-transaction", this.mLastFolderListTransactionId);
    paramBundle.putInt("inbox_conversation-list-transaction", this.mLastInboxConversationListTransactionId);
    paramBundle.putInt("conversation-list-transaction", this.mLastConversationListTransactionId);
    paramBundle.putInt("conversation-transaction", this.mLastConversationTransactionId);
    paramBundle.putBoolean("conversation-list-visible", this.mConversationListVisible);
    paramBundle.putBoolean("conversation-list-never-shown", this.mConversationListNeverShown);
    paramBundle.putParcelable("m-inbox", this.mInbox);
  }

  public void onUndoAvailable(ToastBarOperation paramToastBarOperation)
  {
    int i;
    ConversationListFragment localConversationListFragment;
    if ((paramToastBarOperation != null) && (this.mAccount.supportsCapability(16384)))
    {
      i = this.mViewMode.getMode();
      localConversationListFragment = getConversationListFragment();
    }
    switch (i)
    {
    case 3:
    default:
      return;
    case 1:
    case 5:
      this.mToastBar.setConversationMode(true);
      ActionableToastBar localActionableToastBar = this.mToastBar;
      if (localConversationListFragment != null);
      for (AnimatedAdapter localAnimatedAdapter = localConversationListFragment.getAnimatedAdapter(); ; localAnimatedAdapter = null)
      {
        localActionableToastBar.show(getUndoClickedListener(localAnimatedAdapter), 0, Html.fromHtml(paramToastBarOperation.getDescription(this.mActivity.getActivityContext(), this.mFolder)), true, 2131427488, true, paramToastBarOperation);
        return;
      }
    case 2:
    case 4:
    }
    if (localConversationListFragment != null)
    {
      this.mToastBar.setConversationMode(false);
      this.mToastBar.show(getUndoClickedListener(localConversationListFragment.getAnimatedAdapter()), 0, Html.fromHtml(paramToastBarOperation.getDescription(this.mActivity.getActivityContext(), this.mFolder)), true, 2131427488, true, paramToastBarOperation);
      return;
    }
    this.mActivity.setPendingToastOperation(paramToastBarOperation);
  }

  public boolean onUpPressed()
  {
    int i = this.mViewMode.getMode();
    if (i == 4)
      this.mActivity.finish();
    while (((inInbox(this.mAccount, this.mConvListContext)) || (!this.mViewMode.isListMode())) && (i != 1) && (i != 3) && (i != 5))
      return true;
    this.mActivity.onBackPressed();
    return true;
  }

  public void onViewModeChanged(int paramInt)
  {
    super.onViewModeChanged(paramInt);
    if (ViewMode.isListMode(paramInt))
      this.mPagerController.hide(true);
    if (!ViewMode.isConversationMode(paramInt))
      setCurrentConversation(null);
  }

  public void resetActionBarIcon()
  {
    if ((this.mViewMode.getMode() == 2) && (inInbox(this.mAccount, this.mConvListContext)))
    {
      this.mActionBarView.removeBackButton();
      return;
    }
    this.mActionBarView.setBackButton();
  }

  public boolean shouldShowFirstConversation()
  {
    return false;
  }

  protected void showConversation(Conversation paramConversation, boolean paramBoolean)
  {
    super.showConversation(paramConversation, paramBoolean);
    if (paramConversation == null)
    {
      transitionBackToConversationListMode(paramBoolean);
      return;
    }
    disableCabMode();
    if (ConversationListContext.isSearchResult(this.mConvListContext))
      this.mViewMode.enterSearchResultsConversationMode();
    while (true)
    {
      FragmentManager localFragmentManager = this.mActivity.getFragmentManager();
      FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
      localFragmentTransaction.addToBackStack(null);
      Fragment localFragment = localFragmentManager.findFragmentById(2131689648);
      if ((localFragment != null) && (localFragment.isAdded()))
      {
        localFragmentTransaction.setTransition(4097);
        localFragmentTransaction.remove(localFragment);
        localFragmentTransaction.commitAllowingStateLoss();
      }
      this.mPagerController.show(this.mAccount, this.mFolder, paramConversation, true);
      onConversationVisibilityChanged(true);
      this.mConversationListVisible = false;
      onConversationListVisibilityChanged(false);
      return;
      this.mViewMode.enterConversationMode();
    }
  }

  public void showConversationList(ConversationListContext paramConversationListContext)
  {
    super.showConversationList(paramConversationListContext);
    enableCabMode();
    int i;
    label35: ConversationListFragment localConversationListFragment;
    if (ConversationListContext.isSearchResult(paramConversationListContext))
    {
      this.mViewMode.enterSearchResultsListMode();
      if (!this.mConversationListNeverShown)
        break label101;
      i = 4099;
      localConversationListFragment = ConversationListFragment.newInstance(paramConversationListContext);
      if (inInbox(this.mAccount, this.mConvListContext))
        break label108;
    }
    for (this.mLastConversationListTransactionId = replaceFragment(localConversationListFragment, i, "tag-conversation-list"); ; this.mLastConversationListTransactionId = -1)
    {
      this.mConversationListVisible = true;
      onConversationVisibilityChanged(false);
      onConversationListVisibilityChanged(true);
      this.mConversationListNeverShown = false;
      return;
      this.mViewMode.enterConversationListMode();
      break;
      label101: i = 4097;
      break label35;
      label108: this.mInbox = paramConversationListContext.folder;
      this.mLastInboxConversationListTransactionId = replaceFragment(localConversationListFragment, i, "tag-conversation-list");
      this.mLastFolderListTransactionId = -1;
    }
  }

  public void showFolderList()
  {
    if (this.mAccount == null)
    {
      LogUtils.e(LOG_TAG, "Null account in showFolderList", new Object[0]);
      return;
    }
    setHierarchyFolder(null);
    this.mViewMode.enterFolderListMode();
    enableCabMode();
    this.mLastFolderListTransactionId = replaceFragment(FolderListFragment.newInstance(null, this.mAccount.folderListUri, false), 4097, "tag-folder-list");
    this.mConversationListVisible = false;
    onConversationVisibilityChanged(false);
    onConversationListVisibilityChanged(false);
  }

  public void showWaitForInitialization()
  {
    super.showWaitForInitialization();
    replaceFragment(getWaitFragment(), 4097, "wait-fragment");
  }

  private final class PopBackStackRunnable
    implements Runnable
  {
    private final int mTransactionId;

    public PopBackStackRunnable(int arg2)
    {
      int i;
      this.mTransactionId = i;
    }

    public void popBackStack()
    {
      OnePaneController.this.mActivity.getFragmentManager().popBackStack(this.mTransactionId, 0);
    }

    public void run()
    {
      if (OnePaneController.this.safeToModifyFragments())
      {
        popBackStack();
        return;
      }
      LogUtils.i(AbstractActivityController.LOG_TAG, "Activity has been saved; ignoring unsafe deferred request to pop back stack", new Object[0]);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.OnePaneController
 * JD-Core Version:    0.6.2
 */