package com.android.mail.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.FrameLayout.LayoutParams;
import com.android.mail.ConversationListContext;
import com.android.mail.browse.ConversationPagerController;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;

public final class TwoPaneController extends AbstractActivityController
{
  private Conversation mConversationToShow;
  private TwoPaneLayout mLayout;

  public TwoPaneController(MailActivity paramMailActivity, ViewMode paramViewMode)
  {
    super(paramMailActivity, paramViewMode);
  }

  private void createFolderListFragment(Folder paramFolder, Uri paramUri)
  {
    setHierarchyFolder(paramFolder);
    FolderListFragment localFolderListFragment = FolderListFragment.newInstance(paramFolder, paramUri, true);
    FragmentTransaction localFragmentTransaction = this.mActivity.getFragmentManager().beginTransaction();
    if (Utils.useFolderListFragmentTransition(this.mActivity.getActivityContext()))
      localFragmentTransaction.setTransition(4097);
    localFragmentTransaction.replace(2131689648, localFolderListFragment, "tag-folder-list");
    localFragmentTransaction.commitAllowingStateLoss();
    if (this.mViewMode.getMode() != 0)
      resetActionBarIcon();
  }

  private final void enableOrDisableCab()
  {
    if (this.mLayout.isConversationListCollapsed())
    {
      disableCabMode();
      return;
    }
    enableCabMode();
  }

  private int getUndoBarWidth(int paramInt, ToastBarOperation paramToastBarOperation)
  {
    int i = -1;
    int j = -1;
    switch (paramInt)
    {
    case 3:
    default:
    case 4:
    case 2:
    case 1:
    case 5:
    }
    while (true)
    {
      if (j != i)
        i = (int)this.mContext.getResources().getDimension(j);
      return i;
      j = 2131361806;
      continue;
      j = 2131361803;
      continue;
      if ((paramToastBarOperation.isBatchUndo()) && (!this.mLayout.isConversationListCollapsed()))
        j = 2131361805;
      else
        j = 2131361804;
    }
  }

  private void goUpFolderHierarchy(Folder paramFolder)
  {
    Folder localFolder = paramFolder.parent;
    if (localFolder.parent != null)
    {
      createFolderListFragment(localFolder.parent, localFolder.parent.childFoldersListUri);
      this.mActionBarView.setBackButton();
      return;
    }
    onFolderSelected(localFolder);
  }

  private void initializeConversationListFragment(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (!"android.intent.action.SEARCH".equals(this.mActivity.getIntent().getAction()))
        break label55;
      if (!shouldEnterSearchConvMode())
        break label44;
      this.mViewMode.enterSearchResultsConversationMode();
    }
    while (true)
    {
      renderConversationList();
      return;
      label44: this.mViewMode.enterSearchResultsListMode();
      continue;
      label55: this.mViewMode.enterConversationListMode();
    }
  }

  private void renderConversationList()
  {
    if (this.mActivity == null)
      return;
    FragmentTransaction localFragmentTransaction = this.mActivity.getFragmentManager().beginTransaction();
    localFragmentTransaction.setTransition(4099);
    localFragmentTransaction.replace(2131689711, ConversationListFragment.newInstance(this.mConvListContext), "tag-conversation-list");
    localFragmentTransaction.commitAllowingStateLoss();
  }

  private void renderFolderList()
  {
    if (this.mActivity == null)
      return;
    createFolderListFragment(null, this.mAccount.folderListUri);
  }

  public void exitSearchMode()
  {
    int i = this.mViewMode.getMode();
    if ((i == 4) || ((i == 5) && (Utils.showTwoPaneSearchResults(this.mActivity.getApplicationContext()))))
      this.mActivity.finish();
  }

  protected void hideOrRepositionToastBar(final boolean paramBoolean)
  {
    final int i = this.mViewMode.getMode();
    this.mLayout.postDelayed(new Runnable()
    {
      public void run()
      {
        if ((i == TwoPaneController.this.mViewMode.getMode()) || (!TwoPaneController.this.mToastBar.isAnimating()))
        {
          TwoPaneController.this.mToastBar.hide(paramBoolean);
          return;
        }
        TwoPaneController.this.repositionToastBar(TwoPaneController.this.mToastBar.getOperation());
      }
    }
    , this.mContext.getResources().getInteger(2131296291));
  }

  protected void hideWaitForInitialization()
  {
    WaitFragment localWaitFragment = getWaitFragment();
    if (localWaitFragment == null)
      return;
    FragmentTransaction localFragmentTransaction = this.mActivity.getFragmentManager().beginTransaction();
    localFragmentTransaction.remove(localWaitFragment);
    localFragmentTransaction.commitAllowingStateLoss();
    super.hideWaitForInitialization();
  }

  protected boolean isConversationListVisible()
  {
    return !this.mLayout.isConversationListCollapsed();
  }

  public void onAccountChanged(Account paramAccount)
  {
    super.onAccountChanged(paramAccount);
    renderFolderList();
  }

  public boolean onBackPressed()
  {
    this.mToastBar.hide(false);
    popView(false);
    return true;
  }

  public void onConversationListVisibilityChanged(boolean paramBoolean)
  {
    super.onConversationListVisibilityChanged(paramBoolean);
    enableOrDisableCab();
  }

  public void onConversationVisibilityChanged(boolean paramBoolean)
  {
    super.onConversationVisibilityChanged(paramBoolean);
    if (!paramBoolean)
      this.mPagerController.hide(false);
    while (this.mConversationToShow == null)
      return;
    this.mPagerController.show(this.mAccount, this.mFolder, this.mConversationToShow, false);
    this.mConversationToShow = null;
  }

  public boolean onCreate(Bundle paramBundle)
  {
    this.mActivity.setContentView(2130968678);
    this.mLayout = ((TwoPaneLayout)this.mActivity.findViewById(2131689710));
    if (this.mLayout == null)
      LogUtils.wtf(LOG_TAG, "mLayout is null!", new Object[0]);
    this.mLayout.setController(this, "android.intent.action.SEARCH".equals(this.mActivity.getIntent().getAction()));
    this.mViewMode.addListener(this.mLayout);
    return super.onCreate(paramBundle);
  }

  public void onError(Folder paramFolder, boolean paramBoolean)
  {
    int i = this.mViewMode.getMode();
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.mToastBar.getLayoutParams();
    switch (i)
    {
    case 3:
    default:
    case 2:
    case 4:
    case 1:
    case 5:
    }
    while (true)
    {
      showErrorToast(paramFolder, paramBoolean);
      return;
      localLayoutParams.width = (this.mLayout.computeConversationListWidth() - localLayoutParams.leftMargin - localLayoutParams.rightMargin);
      localLayoutParams.gravity = 85;
      this.mToastBar.setLayoutParams(localLayoutParams);
      continue;
      localLayoutParams.gravity = 83;
      localLayoutParams.width = (this.mLayout.computeConversationListWidth() - localLayoutParams.leftMargin - localLayoutParams.rightMargin);
      this.mToastBar.setLayoutParams(localLayoutParams);
    }
  }

  public void onFolderChanged(Folder paramFolder)
  {
    super.onFolderChanged(paramFolder);
    exitCabMode();
    if ((getFolderListFragment() == null) && (this.mViewMode.getMode() == 2))
      renderFolderList();
  }

  public void onFolderSelected(Folder paramFolder)
  {
    if ((paramFolder.hasChildren) && (!paramFolder.equals(getHierarchyFolder())))
    {
      createFolderListFragment(paramFolder, paramFolder.childFoldersListUri);
      this.mActionBarView.setBackButton();
      super.onFolderSelected(paramFolder);
      return;
    }
    setHierarchyFolder(paramFolder);
    super.onFolderSelected(paramFolder);
  }

  public void onUndoAvailable(ToastBarOperation paramToastBarOperation)
  {
    int i = this.mViewMode.getMode();
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    repositionToastBar(paramToastBarOperation);
    switch (i)
    {
    case 3:
    default:
    case 2:
    case 4:
    case 1:
    case 5:
    }
    do
    {
      do
        return;
      while (localConversationListFragment == null);
      this.mToastBar.show(getUndoClickedListener(localConversationListFragment.getAnimatedAdapter()), 0, Html.fromHtml(paramToastBarOperation.getDescription(this.mActivity.getActivityContext(), this.mFolder)), true, 2131427488, true, paramToastBarOperation);
      return;
    }
    while (localConversationListFragment == null);
    this.mToastBar.show(getUndoClickedListener(localConversationListFragment.getAnimatedAdapter()), 0, Html.fromHtml(paramToastBarOperation.getDescription(this.mActivity.getActivityContext(), this.mFolder)), true, 2131427488, true, paramToastBarOperation);
  }

  public boolean onUpPressed()
  {
    int i = this.mViewMode.getMode();
    if (i == 1)
      this.mActivity.onBackPressed();
    do
    {
      return true;
      if (i == 5)
      {
        if ((this.mLayout.isConversationListCollapsed()) || ((ConversationListContext.isSearchResult(this.mConvListContext)) && (!Utils.showTwoPaneSearchResults(this.mActivity.getApplicationContext()))))
        {
          onBackPressed();
          return true;
        }
        this.mActivity.finish();
        return true;
      }
      if (i == 4)
      {
        this.mActivity.finish();
        return true;
      }
    }
    while (i != 2);
    popView(true);
    return true;
  }

  public void onViewModeChanged(int paramInt)
  {
    super.onViewModeChanged(paramInt);
    if (paramInt != 6)
      hideWaitForInitialization();
    if ((paramInt == 1) || (paramInt == 2))
      enableOrDisableCab();
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    if ((paramBoolean) && (!this.mLayout.isConversationListCollapsed()))
      informCursorVisiblity(true);
  }

  protected void popView(boolean paramBoolean)
  {
    int i = this.mViewMode.getMode();
    if (i == 4)
      this.mActivity.finish();
    do
    {
      return;
      if (i == 1)
      {
        this.mViewMode.enterConversationListMode();
        return;
      }
      if (i == 5)
      {
        this.mViewMode.enterSearchResultsListMode();
        return;
      }
      FolderListFragment localFolderListFragment = getFolderListFragment();
      if ((i == 2) && (localFolderListFragment != null) && (localFolderListFragment.showingHierarchy()))
      {
        Folder localFolder = getHierarchyFolder();
        if (localFolder.parent != null)
        {
          goUpFolderHierarchy(localFolder);
          return;
        }
        createFolderListFragment(null, this.mAccount.folderListUri);
        loadAccountInbox();
        return;
      }
    }
    while (paramBoolean);
    this.mActivity.finish();
  }

  public void repositionToastBar(ToastBarOperation paramToastBarOperation)
  {
    int i = this.mViewMode.getMode();
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.mToastBar.getLayoutParams();
    int j = getUndoBarWidth(i, paramToastBarOperation);
    switch (i)
    {
    case 3:
    default:
      return;
    case 2:
    case 4:
      localLayoutParams.width = (j - localLayoutParams.leftMargin - localLayoutParams.rightMargin);
      localLayoutParams.gravity = 85;
      this.mToastBar.setLayoutParams(localLayoutParams);
      this.mToastBar.setConversationMode(false);
      return;
    case 1:
    case 5:
    }
    if ((paramToastBarOperation.isBatchUndo()) && (!this.mLayout.isConversationListCollapsed()))
    {
      localLayoutParams.gravity = 83;
      localLayoutParams.width = (j - localLayoutParams.leftMargin - localLayoutParams.rightMargin);
      this.mToastBar.setLayoutParams(localLayoutParams);
      this.mToastBar.setConversationMode(false);
      return;
    }
    localLayoutParams.gravity = 85;
    localLayoutParams.width = (j - localLayoutParams.leftMargin - localLayoutParams.rightMargin);
    this.mToastBar.setLayoutParams(localLayoutParams);
    this.mToastBar.setConversationMode(true);
  }

  public void resetActionBarIcon()
  {
    if (this.mViewMode.isListMode())
    {
      this.mActionBarView.removeBackButton();
      return;
    }
    this.mActionBarView.setBackButton();
  }

  public void setCurrentConversation(Conversation paramConversation)
  {
    long l1 = -1L;
    long l2;
    if (this.mCurrentConversation != null)
    {
      l2 = this.mCurrentConversation.id;
      if (paramConversation != null)
        l1 = paramConversation.id;
      if (l2 == l1)
        break label77;
    }
    label77: for (boolean bool = true; ; bool = false)
    {
      super.setCurrentConversation(paramConversation);
      ConversationListFragment localConversationListFragment = getConversationListFragment();
      if ((localConversationListFragment != null) && (paramConversation != null))
        localConversationListFragment.setSelected(paramConversation.position, bool);
      return;
      l2 = l1;
      break;
    }
  }

  public boolean shouldShowFirstConversation()
  {
    return ("android.intent.action.SEARCH".equals(this.mActivity.getIntent().getAction())) && (shouldEnterSearchConvMode());
  }

  protected void showConversation(Conversation paramConversation, boolean paramBoolean)
  {
    super.showConversation(paramConversation, paramBoolean);
    if (this.mActivity == null);
    while (true)
    {
      return;
      if (paramConversation == null)
      {
        onBackPressed();
        return;
      }
      enableOrDisableCab();
      this.mConversationToShow = paramConversation;
      int i = this.mViewMode.getMode();
      if ((i == 4) || (i == 5));
      for (boolean bool = this.mViewMode.enterSearchResultsConversationMode(); !bool; bool = this.mViewMode.enterConversationMode())
      {
        onConversationVisibilityChanged(true);
        return;
      }
    }
  }

  public void showConversationList(ConversationListContext paramConversationListContext)
  {
    super.showConversationList(paramConversationListContext);
    initializeConversationListFragment(true);
  }

  public void showFolderList()
  {
    onUpPressed();
  }

  public void showWaitForInitialization()
  {
    super.showWaitForInitialization();
    FragmentTransaction localFragmentTransaction = this.mActivity.getFragmentManager().beginTransaction();
    localFragmentTransaction.setTransition(4097);
    localFragmentTransaction.replace(2131689530, getWaitFragment(), "wait-fragment");
    localFragmentTransaction.commitAllowingStateLoss();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.TwoPaneController
 * JD-Core Version:    0.6.2
 */