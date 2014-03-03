package com.android.mail.ui;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;
import com.android.mail.ConversationListContext;
import com.android.mail.browse.ConversationCursor;
import com.android.mail.browse.ConversationItemView;
import com.android.mail.browse.ConversationItemViewModel;
import com.android.mail.browse.ConversationPagerController;
import com.android.mail.browse.MessageCursor.ConversationMessage;
import com.android.mail.browse.SelectedConversationsActionMenu;
import com.android.mail.browse.SyncErrorDialogFragment;
import com.android.mail.compose.ComposeActivity;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.ConversationInfo;
import com.android.mail.providers.Folder;
import com.android.mail.providers.MailAppProvider;
import com.android.mail.providers.Settings;
import com.android.mail.providers.UIProvider;
import com.android.mail.providers.UIProvider.AutoAdvance;
import com.android.mail.providers.UIProvider.ConversationColumns;
import com.android.mail.providers.UIProvider.MessageColumns;
import com.android.mail.utils.ContentProviderTask;
import com.android.mail.utils.ContentProviderTask.Result;
import com.android.mail.utils.ContentProviderTask.UpdateTask;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

public abstract class AbstractActivityController
  implements ActivityController
{
  protected static final String LOG_TAG = LogTag.getLogTag();
  protected boolean isLoaderInitialized = false;
  protected Account mAccount;
  private final DataSetObservable mAccountObservers = new DataSetObservable()
  {
    public void registerObserver(DataSetObserver paramAnonymousDataSetObserver)
    {
      int i = this.mObservers.size();
      super.registerObserver(paramAnonymousDataSetObserver);
      String str = AbstractActivityController.LOG_TAG;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramAnonymousDataSetObserver;
      arrayOfObject[1] = Integer.valueOf(i);
      arrayOfObject[2] = Integer.valueOf(this.mObservers.size());
      LogUtils.d(str, "IN AAC.register(Account)Observer: %s before=%d after=%d", arrayOfObject);
    }

    public void unregisterObserver(DataSetObserver paramAnonymousDataSetObserver)
    {
      int i = this.mObservers.size();
      super.unregisterObserver(paramAnonymousDataSetObserver);
      String str = AbstractActivityController.LOG_TAG;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramAnonymousDataSetObserver;
      arrayOfObject[1] = Integer.valueOf(i);
      arrayOfObject[2] = Integer.valueOf(this.mObservers.size());
      LogUtils.d(str, "IN AAC.unregister(Account)Observer: %s before=%d after=%d", arrayOfObject);
    }
  };
  protected MailActionBarView mActionBarView;
  protected final ControllableActivity mActivity;
  private AsyncRefreshTask mAsyncRefreshTask;
  SelectedConversationsActionMenu mCabActionMenu;
  protected final Context mContext;
  protected ConversationListContext mConvListContext;
  protected ConversationCursor mConversationListCursor;
  private final DataSetObservable mConversationListObservable = new DataSetObservable()
  {
    public void registerObserver(DataSetObserver paramAnonymousDataSetObserver)
    {
      int i = this.mObservers.size();
      super.registerObserver(paramAnonymousDataSetObserver);
      String str = AbstractActivityController.LOG_TAG;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramAnonymousDataSetObserver;
      arrayOfObject[1] = Integer.valueOf(i);
      arrayOfObject[2] = Integer.valueOf(this.mObservers.size());
      LogUtils.d(str, "IN AAC.register(List)Observer: %s before=%d after=%d", arrayOfObject);
    }

    public void unregisterObserver(DataSetObserver paramAnonymousDataSetObserver)
    {
      int i = this.mObservers.size();
      super.unregisterObserver(paramAnonymousDataSetObserver);
      String str = AbstractActivityController.LOG_TAG;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramAnonymousDataSetObserver;
      arrayOfObject[1] = Integer.valueOf(i);
      arrayOfObject[2] = Integer.valueOf(this.mObservers.size());
      LogUtils.d(str, "IN AAC.unregister(List)Observer: %s before=%d after=%d", arrayOfObject);
    }
  };
  private RefreshTimerTask mConversationListRefreshTask;
  private final Set<Uri> mCurrentAccountUris = Sets.newHashSet();
  protected Conversation mCurrentConversation;
  private boolean mDestroyed;
  private AsyncTask<String, Void, Void> mEnableShareIntents;
  protected Folder mFolder;
  private boolean mFolderChanged = false;
  private final int mFolderItemUpdateDelayMs;
  private Folder mFolderListFolder;
  private final DataSetObservable mFolderObservable = new DataSetObservable();
  protected AsyncRefreshTask mFolderSyncTask;
  private final FragmentManager mFragmentManager;
  protected Handler mHandler = new Handler();
  private boolean mHaveSearchResults = false;
  private boolean mIsDragHappening;
  private final ConversationListLoaderCallbacks mListCursorCallbacks = new ConversationListLoaderCallbacks(null);
  private SuppressNotificationReceiver mNewEmailReceiver = null;
  protected ConversationPagerController mPagerController;
  private DestructiveAction mPendingDestruction;
  protected final RecentFolderList mRecentFolderList;
  private final DataSetObservable mRecentFolderObservers = new DataSetObservable()
  {
    public void registerObserver(DataSetObserver paramAnonymousDataSetObserver)
    {
      int i = this.mObservers.size();
      super.registerObserver(paramAnonymousDataSetObserver);
      String str = AbstractActivityController.LOG_TAG;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramAnonymousDataSetObserver;
      arrayOfObject[1] = Integer.valueOf(i);
      arrayOfObject[2] = Integer.valueOf(this.mObservers.size());
      LogUtils.d(str, "IN AAC.register(RecentFolder)Observer: %s before=%d after=%d", arrayOfObject);
    }

    public void unregisterObserver(DataSetObserver paramAnonymousDataSetObserver)
    {
      int i = this.mObservers.size();
      super.unregisterObserver(paramAnonymousDataSetObserver);
      String str = AbstractActivityController.LOG_TAG;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramAnonymousDataSetObserver;
      arrayOfObject[1] = Integer.valueOf(i);
      arrayOfObject[2] = Integer.valueOf(this.mObservers.size());
      LogUtils.d(str, "IN AAC.unregister(RecentFolder)Observer: %s before=%d after=%d", arrayOfObject);
    }
  };
  private boolean mRecentsDataUpdated;
  protected ContentResolver mResolver;
  private boolean mSafeToModifyFragments = true;
  private final ConversationSelectionSet mSelectedSet = new ConversationSelectionSet();
  private int mShowUndoBarDelay;
  protected ActionableToastBar mToastBar;
  protected final ConversationPositionTracker mTracker;
  protected final ViewMode mViewMode;
  private WaitFragment mWaitFragment;

  public AbstractActivityController(MailActivity paramMailActivity, ViewMode paramViewMode)
  {
    this.mActivity = paramMailActivity;
    this.mFragmentManager = this.mActivity.getFragmentManager();
    this.mViewMode = paramViewMode;
    this.mContext = paramMailActivity.getApplicationContext();
    this.mRecentFolderList = new RecentFolderList(this.mContext);
    this.mTracker = new ConversationPositionTracker(this);
    this.mSelectedSet.addObserver(this);
    this.mFolderItemUpdateDelayMs = this.mContext.getResources().getInteger(2131296289);
    this.mShowUndoBarDelay = this.mContext.getResources().getInteger(2131296290);
  }

  private boolean accountsUpdated(Cursor paramCursor)
  {
    if ((this.mAccount == null) || (!paramCursor.moveToFirst()));
    int i;
    do
    {
      do
        return true;
      while (this.mCurrentAccountUris.size() != paramCursor.getCount());
      i = 0;
      do
      {
        Uri localUri = Uri.parse(paramCursor.getString(3));
        if ((i == 0) && (this.mAccount.uri.equals(localUri)))
          i = 1;
        if (!this.mCurrentAccountUris.contains(localUri))
          break;
      }
      while (paramCursor.moveToNext());
    }
    while (i == 0);
    return false;
  }

  private void attachActionBar()
  {
    ActionBar localActionBar = this.mActivity.getActionBar();
    if ((localActionBar != null) && (this.mActionBarView != null))
    {
      localActionBar.setCustomView(this.mActionBarView, new ActionBar.LayoutParams(-1, -1));
      localActionBar.setDisplayOptions(18, 26);
      this.mActionBarView.attach();
    }
    this.mViewMode.addListener(this.mActionBarView);
  }

  private void cancelRefreshTask()
  {
    if (this.mConversationListRefreshTask != null)
    {
      this.mConversationListRefreshTask.cancel();
      this.mConversationListRefreshTask = null;
    }
  }

  private final void changeFolder(Folder paramFolder, String paramString)
  {
    if (!Objects.equal(this.mFolder, paramFolder))
      commitDestructiveActions(false);
    if (((paramFolder != null) && (!paramFolder.equals(this.mFolder))) || (this.mViewMode.getMode() != 2))
    {
      setListContext(paramFolder, paramString);
      showConversationList(this.mConvListContext);
    }
    resetActionBarIcon();
  }

  private final void destroyPending(DestructiveAction paramDestructiveAction)
  {
    if (this.mPendingDestruction != null)
      this.mPendingDestruction.performAction();
    this.mPendingDestruction = paramDestructiveAction;
  }

  private void disableNotifications()
  {
    this.mNewEmailReceiver.activate(this.mContext, this);
  }

  private void disableNotificationsOnAccountChange(Account paramAccount)
  {
    if ((this.mNewEmailReceiver.activated()) && (!this.mNewEmailReceiver.notificationsDisabledForAccount(paramAccount)))
    {
      this.mNewEmailReceiver.deactivate();
      this.mNewEmailReceiver.activate(this.mContext, this);
    }
  }

  private void displayAutoAdvanceDialogAndPerformAction(final Runnable paramRunnable)
  {
    String[] arrayOfString1 = this.mContext.getResources().getStringArray(2131558409);
    final String[] arrayOfString2 = this.mContext.getResources().getStringArray(2131558411);
    String str = this.mContext.getString(2131427600);
    int i = 0;
    if (i < arrayOfString2.length)
      if (!str.equals(arrayOfString2[i]));
    while (true)
    {
      DialogInterface.OnClickListener local7 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          String str = arrayOfString2[paramAnonymousInt];
          int i = UIProvider.AutoAdvance.getAutoAdvanceInt(str);
          AbstractActivityController.this.mAccount.settings.setAutoAdvanceSetting(i);
          ContentValues localContentValues = new ContentValues(1);
          localContentValues.put("auto_advance", str);
          AbstractActivityController.this.mContext.getContentResolver().update(AbstractActivityController.this.mAccount.updateSettingsUri, localContentValues, null, null);
          paramAnonymousDialogInterface.dismiss();
          if (paramRunnable != null)
            paramRunnable.run();
        }
      };
      new AlertDialog.Builder(this.mActivity.getActivityContext()).setTitle(2131427599).setSingleChoiceItems(arrayOfString1, i, local7).setPositiveButton(null, null).create().show();
      return;
      i++;
      break;
      i = 0;
    }
  }

  private void enableNotifications()
  {
    this.mNewEmailReceiver.deactivate();
  }

  private void fetchSearchFolder(Intent paramIntent)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("query", paramIntent.getStringExtra("query"));
    this.mActivity.getLoaderManager().restartLoader(6, localBundle, this);
  }

  private ActionableToastBar.ActionClickedListener getInternalErrorClickedListener()
  {
    return new ActionableToastBar.ActionClickedListener()
    {
      public void onActionClicked()
      {
        Utils.sendFeedback(AbstractActivityController.this.mActivity.getActivityContext(), AbstractActivityController.this.mAccount, true);
      }
    };
  }

  private ActionableToastBar.ActionClickedListener getRetryClickedListener(final Folder paramFolder)
  {
    return new ActionableToastBar.ActionClickedListener()
    {
      public void onActionClicked()
      {
        Uri localUri = paramFolder.refreshUri;
        if (localUri != null)
          AbstractActivityController.this.startAsyncRefreshTask(localUri);
      }
    };
  }

  private ActionableToastBar.ActionClickedListener getSignInClickedListener()
  {
    return new ActionableToastBar.ActionClickedListener()
    {
      public void onActionClicked()
      {
        AbstractActivityController.this.promptUserForAuthentication(AbstractActivityController.this.mAccount);
      }
    };
  }

  private ActionableToastBar.ActionClickedListener getStorageErrorClickedListener()
  {
    return new ActionableToastBar.ActionClickedListener()
    {
      public void onActionClicked()
      {
        AbstractActivityController.this.showStorageErrorDialog();
      }
    };
  }

  private void handleIntent(Intent paramIntent)
  {
    boolean bool1;
    if ("android.intent.action.VIEW".equals(paramIntent.getAction()))
    {
      if (paramIntent.hasExtra("account"))
        setAccount(Account.newinstance(paramIntent.getStringExtra("account")));
      if (this.mAccount == null)
        return;
      bool1 = paramIntent.hasExtra("conversationUri");
      if ((bool1) && (this.mViewMode.getMode() == 0))
      {
        this.mViewMode.enterConversationMode();
        boolean bool2 = paramIntent.hasExtra("folder");
        Folder localFolder = null;
        if (bool2)
          localFolder = Folder.fromString(paramIntent.getStringExtra("folder"));
        if (localFolder == null)
          break label373;
        onFolderChanged(localFolder);
      }
    }
    label371: label373: for (int i = 1; ; i = 0)
    {
      if (bool1)
      {
        String str3 = LOG_TAG;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = paramIntent.getParcelableExtra("conversationUri");
        LogUtils.d(str3, "SHOW THE CONVERSATION at %s", arrayOfObject);
        Conversation localConversation = (Conversation)paramIntent.getParcelableExtra("conversationUri");
        if ((localConversation != null) && (localConversation.position < 0))
          localConversation.position = 0;
        showConversation(localConversation);
        i = 1;
      }
      if (i == 0)
        loadAccountInbox();
      while (true)
      {
        if (this.mAccount == null)
          break label371;
        restartOptionalLoader(7);
        return;
        this.mViewMode.enterConversationListMode();
        break;
        if ("android.intent.action.SEARCH".equals(paramIntent.getAction()))
          if (paramIntent.hasExtra("account"))
          {
            this.mHaveSearchResults = false;
            String str1 = paramIntent.getStringExtra("query");
            String str2 = this.mContext.getString(2131427346);
            new SearchRecentSuggestions(this.mContext, str2, 1).saveRecentQuery(str1, null);
            setAccount((Account)paramIntent.getParcelableExtra("account"));
            fetchSearchFolder(paramIntent);
            if (shouldEnterSearchConvMode())
              this.mViewMode.enterSearchResultsConversationMode();
            else
              this.mViewMode.enterSearchResultsListMode();
          }
          else
          {
            LogUtils.e(LOG_TAG, "Missing account extra from search intent.  Finishing", new Object[0]);
            this.mActivity.finish();
          }
      }
      break;
    }
  }

  private boolean inWaitMode()
  {
    this.mActivity.getFragmentManager();
    WaitFragment localWaitFragment = getWaitFragment();
    boolean bool1 = false;
    if (localWaitFragment != null)
    {
      Account localAccount = localWaitFragment.getAccount();
      bool1 = false;
      if (localAccount != null)
      {
        boolean bool2 = localAccount.uri.equals(this.mAccount.uri);
        bool1 = false;
        if (bool2)
        {
          int i = this.mViewMode.getMode();
          bool1 = false;
          if (i == 6)
            bool1 = true;
        }
      }
    }
    return bool1;
  }

  private void initializeActionBar()
  {
    ActionBar localActionBar = this.mActivity.getActionBar();
    if (localActionBar == null)
      return;
    LayoutInflater localLayoutInflater = LayoutInflater.from(localActionBar.getThemedContext());
    int i;
    if ((this.mActivity.getIntent() != null) && ("android.intent.action.SEARCH".equals(this.mActivity.getIntent().getAction())))
    {
      i = 1;
      if (i == 0)
        break label108;
    }
    label108: for (int j = 2130968667; ; j = 2130968586)
    {
      this.mActionBarView = ((MailActionBarView)localLayoutInflater.inflate(j, null));
      this.mActionBarView.initialize(this.mActivity, this, this.mViewMode, localActionBar, this.mRecentFolderList);
      return;
      i = 0;
      break;
    }
  }

  private boolean isDragging()
  {
    return this.mIsDragHappening;
  }

  private static final boolean isValidFragment(Fragment paramFragment)
  {
    return (paramFragment != null) && (paramFragment.getActivity() != null) && (paramFragment.getView() != null);
  }

  private void loadRecentFolders(Cursor paramCursor)
  {
    this.mRecentFolderList.loadFromUiProvider(paramCursor);
    if (isAnimating())
    {
      this.mRecentsDataUpdated = true;
      return;
    }
    this.mRecentFolderObservers.notifyChanged();
  }

  private void markConversationsRead(final Collection<Conversation> paramCollection, final boolean paramBoolean1, final boolean paramBoolean2, final boolean paramBoolean3)
  {
    if ((paramBoolean3) && (!paramBoolean1) && (!showNextConversation(paramCollection, new Runnable()
    {
      public void run()
      {
        AbstractActivityController.this.markConversationsRead(paramCollection, paramBoolean1, paramBoolean2, paramBoolean3);
      }
    })))
      return;
    ArrayList localArrayList = new ArrayList(paramCollection.size());
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Conversation localConversation = (Conversation)localIterator.next();
      ContentValues localContentValues = new ContentValues();
      localContentValues.put(UIProvider.ConversationColumns.READ, Boolean.valueOf(paramBoolean1));
      localContentValues.put("suppress_undo", Boolean.valueOf(true));
      if (paramBoolean2)
        localContentValues.put("viewed", Boolean.valueOf(true));
      ConversationInfo localConversationInfo = localConversation.conversationInfo;
      if ((localConversationInfo != null) && (localConversationInfo.markRead(paramBoolean1)))
        localContentValues.put("conversationInfo", ConversationInfo.toString(localConversationInfo));
      localArrayList.add(this.mConversationListCursor.getOperationForConversation(localConversation, 2, localContentValues));
      localConversation.read = paramBoolean1;
      if (paramBoolean2)
        localConversation.markViewed();
    }
    this.mConversationListCursor.updateBulkValues(this.mContext, localArrayList);
  }

  private final void perhapsShowFirstSearchResult()
  {
    if (this.mCurrentConversation == null)
      if ((!"android.intent.action.SEARCH".equals(this.mActivity.getIntent().getAction())) || (this.mConversationListCursor.getCount() <= 0))
        break label53;
    label53: for (boolean bool = true; ; bool = false)
    {
      this.mHaveSearchResults = bool;
      if (shouldShowFirstConversation())
        break;
      return;
    }
    this.mConversationListCursor.moveToPosition(0);
    Conversation localConversation = new Conversation(this.mConversationListCursor);
    localConversation.position = 0;
    onConversationSelected(localConversation, true);
  }

  private void promptUserForAuthentication(Account paramAccount)
  {
    if ((paramAccount != null) && (!Utils.isEmpty(paramAccount.reauthenticationIntentUri)))
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", paramAccount.reauthenticationIntentUri);
      this.mActivity.startActivityForResult(localIntent, 2);
    }
  }

  private final void registerDestructiveAction(DestructiveAction paramDestructiveAction)
  {
    destroyPending(paramDestructiveAction);
  }

  private void requestFolderRefresh()
  {
    if (this.mFolder != null)
    {
      if (this.mAsyncRefreshTask != null)
        this.mAsyncRefreshTask.cancel(true);
      this.mAsyncRefreshTask = new AsyncRefreshTask(this.mContext, this.mFolder.refreshUri);
      this.mAsyncRefreshTask.execute(new Void[0]);
    }
  }

  private void requestUpdate(Collection<Conversation> paramCollection, DestructiveAction paramDestructiveAction)
  {
    paramDestructiveAction.performAction();
    refreshConversationList();
  }

  private void restartOptionalLoader(int paramInt)
  {
    LoaderManager localLoaderManager = this.mActivity.getLoaderManager();
    localLoaderManager.destroyLoader(paramInt);
    localLoaderManager.restartLoader(paramInt, Bundle.EMPTY, this);
  }

  private final void restoreSelectedConversations(Bundle paramBundle)
  {
    if (paramBundle == null)
    {
      this.mSelectedSet.clear();
      return;
    }
    ConversationSelectionSet localConversationSelectionSet = (ConversationSelectionSet)paramBundle.getParcelable("saved-selected-set");
    if ((localConversationSelectionSet == null) || (localConversationSelectionSet.isEmpty()))
    {
      this.mSelectedSet.clear();
      return;
    }
    this.mSelectedSet.putAll(localConversationSelectionSet);
  }

  private void setAccount(Account paramAccount)
  {
    if (paramAccount == null)
    {
      LogUtils.w(LOG_TAG, new Error(), "AAC ignoring null (presumably invalid) account restoration", new Object[0]);
      return;
    }
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramAccount.uri;
    LogUtils.d(str, "AbstractActivityController.setAccount(): account = %s", arrayOfObject);
    this.mAccount = paramAccount;
    restartOptionalLoader(3);
    this.mActivity.invalidateOptionsMenu();
    disableNotificationsOnAccountChange(this.mAccount);
    restartOptionalLoader(7);
    MailAppProvider.getInstance().setLastViewedAccount(this.mAccount.uri.toString());
    if (paramAccount.settings == null)
    {
      LogUtils.w(LOG_TAG, new Error(), "AAC ignoring account with null settings.", new Object[0]);
      return;
    }
    this.mAccountObservers.notifyChanged();
    perhapsEnterWaitMode();
  }

  private final void setHasFolderChanged(Folder paramFolder)
  {
    if (paramFolder == null);
    while ((this.mFolder != null) && (paramFolder.uri.equals(this.mFolder.uri)))
      return;
    this.mFolderChanged = true;
  }

  private void setListContext(Folder paramFolder, String paramString)
  {
    updateFolder(paramFolder);
    if (paramString != null);
    for (this.mConvListContext = ConversationListContext.forSearchQuery(this.mAccount, this.mFolder, paramString); ; this.mConvListContext = ConversationListContext.forFolder(this.mAccount, this.mFolder))
    {
      cancelRefreshTask();
      return;
    }
  }

  private final void showConversation(Conversation paramConversation)
  {
    showConversation(paramConversation, false);
  }

  private boolean showNextConversation(Collection<Conversation> paramCollection, Runnable paramRunnable)
  {
    int i;
    if ((this.mViewMode.getMode() == 1) && (Conversation.contains(paramCollection, this.mCurrentConversation)))
      i = 1;
    while (i != 0)
    {
      int j = this.mAccount.settings.getAutoAdvanceSetting();
      if ((j == 0) && (Utils.useTabletUI(this.mContext)))
      {
        displayAutoAdvanceDialogAndPerformAction(paramRunnable);
        return false;
        i = 0;
      }
      else
      {
        if (j == 0)
          j = Settings.getAutoAdvanceSetting(null);
        Conversation localConversation = this.mTracker.getNextConversation(j, paramCollection);
        LogUtils.d(LOG_TAG, "showNextConversation: showing %s next.", new Object[] { localConversation });
        showConversation(localConversation);
        return true;
      }
    }
    return true;
  }

  private void showStorageErrorDialog()
  {
    Object localObject = (DialogFragment)this.mFragmentManager.findFragmentByTag("SyncErrorDialogFragment");
    if (localObject == null)
      localObject = SyncErrorDialogFragment.newInstance();
    ((DialogFragment)localObject).show(this.mFragmentManager, "SyncErrorDialogFragment");
  }

  private void startAsyncRefreshTask(Uri paramUri)
  {
    if (this.mFolderSyncTask != null)
      this.mFolderSyncTask.cancel(true);
    this.mFolderSyncTask = new AsyncRefreshTask(this.mActivity.getActivityContext(), paramUri);
    this.mFolderSyncTask.execute(new Void[0]);
  }

  private boolean updateAccounts(Cursor paramCursor)
  {
    if ((paramCursor == null) || (!paramCursor.moveToFirst()))
      return false;
    Account[] arrayOfAccount = Account.getAllAccounts(paramCursor);
    Object localObject1 = null;
    this.mCurrentAccountUris.clear();
    int i = arrayOfAccount.length;
    int j = 0;
    Object localObject2;
    if (j < i)
    {
      localObject2 = arrayOfAccount[j];
      LogUtils.d(LOG_TAG, "updateAccounts(%s)", new Object[] { localObject2 });
      this.mCurrentAccountUris.add(((Account)localObject2).uri);
      if ((this.mAccount == null) || (!((Account)localObject2).uri.equals(this.mAccount.uri)))
        break label253;
    }
    while (true)
    {
      j++;
      localObject1 = localObject2;
      break;
      Account localAccount = arrayOfAccount[0];
      int k;
      if (localObject1 != null)
      {
        if (((Account)localObject1).equals(this.mAccount))
          break label244;
        k = 1;
      }
      while (true)
      {
        if (k != 0)
          onAccountChanged((Account)localObject1);
        this.mActionBarView.setAccounts(arrayOfAccount);
        if (arrayOfAccount.length > 0)
        {
          return true;
          if (this.mAccount == null)
          {
            String str = MailAppProvider.getInstance().getLastViewedAccount();
            if (str != null)
            {
              int m = arrayOfAccount.length;
              for (int n = 0; ; n++)
              {
                if (n >= m)
                  break label235;
                localObject1 = arrayOfAccount[n];
                if (str.equals(((Account)localObject1).uri.toString()))
                {
                  k = 1;
                  break;
                }
              }
            }
          }
        }
        else
        {
          return false;
        }
        label235: localObject1 = localAccount;
        k = 1;
        continue;
        label244: localObject1 = localAccount;
        k = 0;
      }
      label253: localObject2 = localObject1;
    }
  }

  private final void updateConversationListFragment()
  {
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    if (localConversationListFragment != null)
    {
      refreshConversationList();
      if (localConversationListFragment.isVisible())
        informCursorVisiblity(true);
    }
  }

  private void updateFolder(Folder paramFolder)
  {
    if ((paramFolder == null) || (!paramFolder.isInitialized()))
    {
      LogUtils.e(LOG_TAG, new Error(), "AAC.setFolder(%s): Bad input", new Object[] { paramFolder });
      return;
    }
    if (paramFolder.equals(this.mFolder))
    {
      LogUtils.d(LOG_TAG, "AAC.setFolder(%s): Input matches mFolder", new Object[] { paramFolder });
      return;
    }
    int i;
    LoaderManager localLoaderManager;
    if (this.mFolder == null)
    {
      i = 1;
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramFolder.name;
      LogUtils.d(str, "AbstractActivityController.setFolder(%s)", arrayOfObject);
      localLoaderManager = this.mActivity.getLoaderManager();
      setHasFolderChanged(paramFolder);
      this.mFolder = paramFolder;
      this.mActionBarView.setFolder(this.mFolder);
      if (localLoaderManager.getLoader(2) != null)
        break label187;
      localLoaderManager.initLoader(2, null, this);
    }
    while (true)
    {
      if ((i == 0) && (localLoaderManager.getLoader(4) != null))
        break label199;
      localLoaderManager.initLoader(4, null, this.mListCursorCallbacks);
      return;
      i = 0;
      break;
      label187: localLoaderManager.restartLoader(2, null, this);
    }
    label199: localLoaderManager.destroyLoader(4);
    localLoaderManager.initLoader(4, null, this.mListCursorCallbacks);
  }

  private void updateRecentFolderList()
  {
    if (this.mFolder != null)
      this.mRecentFolderList.touchFolder(this.mFolder, this.mAccount);
  }

  private void updateWaitMode()
  {
    WaitFragment localWaitFragment = (WaitFragment)this.mActivity.getFragmentManager().findFragmentByTag("wait-fragment");
    if (localWaitFragment != null)
      localWaitFragment.updateAccount(this.mAccount);
  }

  public final void assignFolder(Collection<FolderOperation> paramCollection, Collection<Conversation> paramCollection1, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((this.mFolder.supportsCapability(8)) && (FolderOperation.isDestructive(paramCollection, this.mFolder)));
    for (boolean bool = true; ; bool = false)
    {
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Boolean.valueOf(bool);
      LogUtils.d(str, "onFolderChangesCommit: isDestructive = %b", arrayOfObject);
      if (!bool)
        break;
      Iterator localIterator = paramCollection1.iterator();
      while (localIterator.hasNext())
        ((Conversation)localIterator.next()).localDeleteOnUpdate = true;
    }
    if (bool)
    {
      delete(0, paramCollection1, getDeferredFolderChange(paramCollection1, paramCollection, bool, paramBoolean1, paramBoolean2));
      return;
    }
    requestUpdate(paramCollection1, getFolderChange(paramCollection1, paramCollection, bool, paramBoolean1, paramBoolean2));
  }

  public void commitDestructiveActions(boolean paramBoolean)
  {
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    if (localConversationListFragment != null)
      localConversationListFragment.commitDestructiveActions(paramBoolean);
  }

  protected void confirmAndDelete(final Collection<Conversation> paramCollection, boolean paramBoolean, int paramInt, final DestructiveAction paramDestructiveAction)
  {
    if (paramBoolean)
    {
      DialogInterface.OnClickListener local9 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (paramAnonymousInt == -1)
            AbstractActivityController.this.delete(0, paramCollection, paramDestructiveAction);
        }
      };
      String str = Utils.formatPlural(this.mContext, paramInt, paramCollection.size());
      new AlertDialog.Builder(this.mActivity.getActivityContext()).setMessage(str).setPositiveButton(2131427539, local9).setNegativeButton(2131427540, null).create().show();
      return;
    }
    delete(0, paramCollection, paramDestructiveAction);
  }

  public void delete(int paramInt, Collection<Conversation> paramCollection, DestructiveAction paramDestructiveAction)
  {
    delete(paramInt, paramCollection, null, paramDestructiveAction);
  }

  public void delete(final int paramInt, final Collection<Conversation> paramCollection, final Collection<ConversationItemView> paramCollection1, final DestructiveAction paramDestructiveAction)
  {
    if (!showNextConversation(paramCollection, new Runnable()
    {
      public void run()
      {
        AbstractActivityController.this.delete(paramInt, paramCollection, paramCollection1, paramDestructiveAction);
      }
    }))
      return;
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    if (localConversationListFragment != null)
    {
      LogUtils.d(LOG_TAG, "AAC.requestDelete: ListFragment is handling delete.", new Object[0]);
      localConversationListFragment.requestDelete(paramInt, paramCollection, paramCollection1, paramDestructiveAction);
      return;
    }
    paramDestructiveAction.performAction();
  }

  protected void disableCabMode()
  {
    commitDestructiveActions(true);
    if (this.mCabActionMenu != null)
      this.mCabActionMenu.deactivate();
  }

  public void disablePagerUpdates()
  {
    this.mPagerController.stopListening();
  }

  protected void enableCabMode()
  {
    if (this.mCabActionMenu != null)
      this.mCabActionMenu.activate();
  }

  protected final void exitCabMode()
  {
    this.mSelectedSet.clear();
  }

  public void exitSearchMode()
  {
    if (this.mViewMode.getMode() == 4)
      this.mActivity.finish();
  }

  public Account getAccount()
  {
    return this.mAccount;
  }

  public final DestructiveAction getBatchAction(int paramInt)
  {
    ConversationAction localConversationAction = new ConversationAction(paramInt, this.mSelectedSet.values(), true);
    registerDestructiveAction(localConversationAction);
    return localConversationAction;
  }

  public final ConversationCursor getConversationListCursor()
  {
    return this.mConversationListCursor;
  }

  protected ConversationListFragment getConversationListFragment()
  {
    Fragment localFragment = this.mFragmentManager.findFragmentByTag("tag-conversation-list");
    if (isValidFragment(localFragment))
      return (ConversationListFragment)localFragment;
    return null;
  }

  public Account getCurrentAccount()
  {
    return this.mAccount;
  }

  public Conversation getCurrentConversation()
  {
    return this.mCurrentConversation;
  }

  public ConversationListContext getCurrentListContext()
  {
    return this.mConvListContext;
  }

  public DestructiveAction getDeferredAction(int paramInt, Collection<Conversation> paramCollection, boolean paramBoolean)
  {
    return new ConversationAction(paramInt, paramCollection, paramBoolean);
  }

  public final DestructiveAction getDeferredBatchAction(int paramInt)
  {
    return getDeferredAction(paramInt, this.mSelectedSet.values(), true);
  }

  public final DestructiveAction getDeferredFolderChange(Collection<Conversation> paramCollection, Collection<FolderOperation> paramCollection1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    return new FolderDestruction(paramCollection, paramCollection1, paramBoolean1, paramBoolean2, paramBoolean3, 2131689755, null);
  }

  public final DestructiveAction getDeferredRemoveFolder(Collection<Conversation> paramCollection, Folder paramFolder, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new FolderOperation(paramFolder, Boolean.valueOf(false)));
    return new FolderDestruction(paramCollection, localArrayList, paramBoolean1, paramBoolean2, paramBoolean3, 2131689752, null);
  }

  public Folder getFolder()
  {
    return this.mFolder;
  }

  public final DestructiveAction getFolderChange(Collection<Conversation> paramCollection, Collection<FolderOperation> paramCollection1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    DestructiveAction localDestructiveAction = getDeferredFolderChange(paramCollection, paramCollection1, paramBoolean1, paramBoolean2, paramBoolean3);
    registerDestructiveAction(localDestructiveAction);
    return localDestructiveAction;
  }

  protected FolderListFragment getFolderListFragment()
  {
    Fragment localFragment = this.mFragmentManager.findFragmentByTag("tag-folder-list");
    if (isValidFragment(localFragment))
      return (FolderListFragment)localFragment;
    return null;
  }

  public String getHelpContext()
  {
    switch (this.mViewMode.getMode())
    {
    default:
    case 6:
    }
    for (int i = 2131427341; ; i = 2131427340)
      return this.mContext.getString(i);
  }

  public Folder getHierarchyFolder()
  {
    return this.mFolderListFolder;
  }

  public RecentFolderList getRecentFolders()
  {
    return this.mRecentFolderList;
  }

  public ConversationSelectionSet getSelectedSet()
  {
    return this.mSelectedSet;
  }

  public SubjectDisplayChanger getSubjectDisplayChanger()
  {
    return this.mActionBarView;
  }

  protected final ActionableToastBar.ActionClickedListener getUndoClickedListener(final AnimatedAdapter paramAnimatedAdapter)
  {
    return new ActionableToastBar.ActionClickedListener()
    {
      public void onActionClicked()
      {
        if (AbstractActivityController.this.mAccount.undoUri != null)
        {
          if (AbstractActivityController.this.mConversationListCursor != null)
            AbstractActivityController.this.mConversationListCursor.undo(AbstractActivityController.this.mActivity.getActivityContext(), AbstractActivityController.this.mAccount.undoUri);
          if (paramAnimatedAdapter != null)
            paramAnimatedAdapter.setUndo(true);
        }
      }
    };
  }

  protected final WaitFragment getWaitFragment()
  {
    WaitFragment localWaitFragment = (WaitFragment)this.mActivity.getFragmentManager().findFragmentByTag("wait-fragment");
    if (localWaitFragment != null)
      this.mWaitFragment = localWaitFragment;
    return this.mWaitFragment;
  }

  public void handleDrop(DragEvent paramDragEvent, Folder paramFolder)
  {
    if (!supportsDrag(paramDragEvent, paramFolder))
      return;
    Collection localCollection = this.mSelectedSet.values();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new FolderOperation(paramFolder, Boolean.valueOf(true)));
    if ((!this.mFolder.isViewAll()) && (this.mFolder.supportsCapability(8)));
    DestructiveAction localDestructiveAction;
    for (boolean bool = true; ; bool = false)
    {
      if (bool)
        localArrayList.add(new FolderOperation(this.mFolder, Boolean.valueOf(false)));
      localDestructiveAction = getFolderChange(localCollection, localArrayList, bool, true, true);
      if (!bool)
        break;
      delete(0, localCollection, localDestructiveAction);
      return;
    }
    localDestructiveAction.performAction();
  }

  protected abstract void hideOrRepositionToastBar(boolean paramBoolean);

  protected void hideWaitForInitialization()
  {
    this.mWaitFragment = null;
  }

  protected void informCursorVisiblity(boolean paramBoolean)
  {
    try
    {
      if (this.mConversationListCursor != null)
      {
        Utils.setConversationCursorVisibility(this.mConversationListCursor, paramBoolean, this.mFolderChanged);
        this.mFolderChanged = false;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public boolean isAnimating()
  {
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    boolean bool = false;
    if (localConversationListFragment != null)
    {
      AnimatedAdapter localAnimatedAdapter = localConversationListFragment.getAnimatedAdapter();
      bool = false;
      if (localAnimatedAdapter != null)
        bool = localAnimatedAdapter.isAnimating();
    }
    return bool;
  }

  protected abstract boolean isConversationListVisible();

  public boolean isDestroyed()
  {
    return this.mDestroyed;
  }

  public boolean isInitialConversationLoading()
  {
    return this.mPagerController.isInitialConversationLoading();
  }

  public void loadAccountInbox()
  {
    restartOptionalLoader(5);
  }

  public void markConversationMessagesUnread(Conversation paramConversation, Set<Uri> paramSet, String paramString)
  {
    int i = 1;
    showConversation(null);
    paramConversation.read = false;
    if (this.mConversationListCursor == null)
    {
      LogUtils.e(LOG_TAG, "null ConversationCursor in markConversationMessagesUnread", new Object[0]);
      return;
    }
    int j;
    if (paramSet == null)
    {
      j = 0;
      int k = paramConversation.getNumMessages();
      if ((k <= i) || (j <= 0) || (j >= k))
        break label95;
    }
    while (true)
    {
      if (i != 0)
        break label101;
      markConversationsRead(Collections.singletonList(paramConversation), false, false, false);
      return;
      j = paramSet.size();
      break;
      label95: i = 0;
    }
    label101: this.mConversationListCursor.setConversationColumn(paramConversation.uri, UIProvider.ConversationColumns.READ, Integer.valueOf(0));
    if (paramString != null)
      this.mConversationListCursor.setConversationColumn(paramConversation.uri, "conversationInfo", paramString);
    ArrayList localArrayList = Lists.newArrayList();
    String str = null;
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      Uri localUri = (Uri)localIterator.next();
      if (str == null)
        str = localUri.getAuthority();
      localArrayList.add(ContentProviderOperation.newUpdate(localUri).withValue(UIProvider.MessageColumns.READ, Integer.valueOf(0)).build());
    }
    new ContentProviderTask()
    {
      protected void onPostExecute(ContentProviderTask.Result paramAnonymousResult)
      {
      }
    }
    .run(this.mResolver, str, localArrayList);
  }

  public void markConversationsRead(Collection<Conversation> paramCollection, boolean paramBoolean1, boolean paramBoolean2)
  {
    markConversationsRead(paramCollection, paramBoolean1, paramBoolean2, true);
  }

  public void onAccessibilityStateChanged()
  {
    ConversationItemViewModel.onAccessibilityUpdated();
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    if (localConversationListFragment != null)
    {
      AnimatedAdapter localAnimatedAdapter = localConversationListFragment.getAnimatedAdapter();
      if (localAnimatedAdapter != null)
        localAnimatedAdapter.notifyDataSetInvalidated();
    }
  }

  public void onAccountChanged(Account paramAccount)
  {
    int i = 1;
    int j;
    if (this.mAccount == null)
    {
      j = i;
      if ((j == 0) && (paramAccount.uri.equals(this.mAccount.uri)))
        break label53;
      label32: if ((i != 0) || (paramAccount.settingsDiffer(this.mAccount)))
        break label58;
    }
    label53: label58: 
    do
    {
      return;
      j = 0;
      break;
      i = 0;
      break label32;
      if (paramAccount == null)
      {
        LogUtils.e(LOG_TAG, "AAC.onAccountChanged(null) called.", new Object[0]);
        return;
      }
      final String str = paramAccount.name;
      this.mHandler.post(new Runnable()
      {
        public void run()
        {
          MailActivity.setForegroundNdef(MailActivity.getMailtoNdef(str));
        }
      });
      if (i != 0)
        commitDestructiveActions(false);
      setAccount(paramAccount);
      cancelRefreshTask();
      if (i != 0)
        loadAccountInbox();
    }
    while ((this.mAccount == null) || (Uri.EMPTY.equals(this.mAccount.settings.setupIntentUri)));
    Intent localIntent = new Intent("android.intent.action.EDIT");
    localIntent.setData(this.mAccount.settings.setupIntentUri);
    this.mActivity.startActivity(localIntent);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    switch (paramInt1)
    {
    default:
    case 1:
    case 2:
    }
    Uri localUri;
    do
    {
      do
      {
        return;
        if (paramInt2 == -1)
        {
          this.mActivity.getLoaderManager().initLoader(0, null, this);
          return;
        }
        this.mActivity.finish();
        return;
      }
      while (paramInt2 != -1);
      Folder localFolder = this.mFolder;
      localUri = null;
      if (localFolder != null)
        localUri = this.mFolder.refreshUri;
    }
    while (localUri == null);
    startAsyncRefreshTask(localUri);
  }

  public void onAnimationEnd(AnimatedAdapter paramAnimatedAdapter)
  {
    if (this.mConversationListCursor == null)
      LogUtils.e(LOG_TAG, "null ConversationCursor in onAnimationEnd", new Object[0]);
    FolderListFragment localFolderListFragment;
    do
    {
      return;
      if (this.mConversationListCursor.isRefreshReady())
      {
        LogUtils.d(LOG_TAG, "Stopped animating: try sync", new Object[0]);
        onRefreshReady();
      }
      if (this.mConversationListCursor.isRefreshRequired())
      {
        LogUtils.d(LOG_TAG, "Stopped animating: refresh", new Object[0]);
        this.mConversationListCursor.refresh();
      }
      if (this.mRecentsDataUpdated)
      {
        this.mRecentsDataUpdated = false;
        this.mRecentFolderObservers.notifyChanged();
      }
      localFolderListFragment = getFolderListFragment();
    }
    while (localFolderListFragment == null);
    localFolderListFragment.onAnimationEnd();
  }

  public void onConversationListVisibilityChanged(boolean paramBoolean)
  {
    informCursorVisiblity(paramBoolean);
  }

  public void onConversationSeen(Conversation paramConversation)
  {
    this.mPagerController.onConversationSeen(paramConversation);
  }

  public final void onConversationSelected(Conversation paramConversation, boolean paramBoolean)
  {
    commitDestructiveActions(Utils.useTabletUI(this.mContext));
    showConversation(paramConversation, paramBoolean);
  }

  public void onConversationVisibilityChanged(boolean paramBoolean)
  {
  }

  public boolean onCreate(Bundle paramBundle)
  {
    initializeActionBar();
    this.mActivity.setDefaultKeyMode(2);
    this.mResolver = this.mActivity.getContentResolver();
    this.mNewEmailReceiver = new SuppressNotificationReceiver();
    this.mRecentFolderList.initialize(this.mActivity);
    this.mViewMode.addListener(this);
    this.mPagerController = new ConversationPagerController(this.mActivity, this);
    this.mToastBar = ((ActionableToastBar)this.mActivity.findViewById(2131689667));
    attachActionBar();
    FolderSelectionDialog.setDialogDismissed();
    Intent localIntent = this.mActivity.getIntent();
    if (paramBundle != null)
    {
      if (paramBundle.containsKey("saved-account"))
        setAccount((Account)paramBundle.getParcelable("saved-account"));
      if (paramBundle.containsKey("saved-folder"))
        setListContext((Folder)paramBundle.getParcelable("saved-folder"), paramBundle.getString("saved-query", null));
      this.mViewMode.handleRestore(paramBundle);
    }
    while (true)
    {
      this.mActivity.getLoaderManager().initLoader(0, null, this);
      return true;
      if (localIntent != null)
        handleIntent(localIntent);
    }
  }

  public Dialog onCreateDialog(int paramInt, Bundle paramBundle)
  {
    return null;
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    switch (paramInt)
    {
    case 1:
    case 4:
    default:
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      LogUtils.wtf(str, "Loader returned unexpected id: %d", arrayOfObject);
    case 0:
    case 2:
    case 3:
    case 5:
      Uri localUri;
      do
      {
        do
        {
          return null;
          return new CursorLoader(this.mContext, MailAppProvider.getAccountsUri(), UIProvider.ACCOUNTS_PROJECTION, null, null, null);
          CursorLoader localCursorLoader = new CursorLoader(this.mContext, this.mFolder.uri, UIProvider.FOLDERS_PROJECTION, null, null, null);
          localCursorLoader.setUpdateThrottle(this.mFolderItemUpdateDelayMs);
          return localCursorLoader;
        }
        while ((this.mAccount == null) || (this.mAccount.recentFolderListUri == null));
        return new CursorLoader(this.mContext, this.mAccount.recentFolderListUri, UIProvider.FOLDERS_PROJECTION, null, null, null);
        localUri = Settings.getDefaultInboxUri(this.mAccount.settings);
        if (localUri.equals(Uri.EMPTY))
          localUri = this.mAccount.folderListUri;
        LogUtils.d(LOG_TAG, "Loading the default inbox: %s", new Object[] { localUri });
      }
      while (localUri == null);
      return new CursorLoader(this.mContext, localUri, UIProvider.FOLDERS_PROJECTION, null, null, null);
    case 6:
      return Folder.forSearchResults(this.mAccount, paramBundle.getString("query"), this.mActivity.getActivityContext());
    case 7:
    }
    return new CursorLoader(this.mContext, this.mAccount.uri, UIProvider.ACCOUNTS_PROJECTION, null, null, null);
  }

  public final boolean onCreateOptionsMenu(Menu paramMenu)
  {
    this.mActivity.getMenuInflater().inflate(this.mActionBarView.getOptionsMenuId(), paramMenu);
    this.mActionBarView.onCreateOptionsMenu(paramMenu);
    return true;
  }

  public final void onDataSetChanged()
  {
    updateConversationListFragment();
    this.mConversationListObservable.notifyChanged();
    this.mSelectedSet.validateAgainstCursor(this.mConversationListCursor);
  }

  public void onDestroy()
  {
    this.mPagerController.onDestroy();
    this.mActionBarView.onDestroy();
    this.mRecentFolderList.destroy();
    this.mDestroyed = true;
  }

  public void onFolderChanged(Folder paramFolder)
  {
    changeFolder(paramFolder, null);
  }

  public void onFolderSelected(Folder paramFolder)
  {
    onFolderChanged(paramFolder);
  }

  public void onFooterViewErrorActionClick(Folder paramFolder, int paramInt)
  {
    switch (paramInt)
    {
    case 3:
    default:
    case 1:
      Uri localUri1;
      do
      {
        return;
        localUri1 = null;
        if (paramFolder != null)
        {
          Uri localUri2 = paramFolder.refreshUri;
          localUri1 = null;
          if (localUri2 != null)
            localUri1 = paramFolder.refreshUri;
        }
      }
      while (localUri1 == null);
      startAsyncRefreshTask(localUri1);
      return;
    case 2:
      promptUserForAuthentication(this.mAccount);
      return;
    case 4:
      showStorageErrorDialog();
      return;
    case 5:
    }
    Utils.sendFeedback(this.mActivity.getActivityContext(), this.mAccount, true);
  }

  public void onFooterViewLoadMoreClick(Folder paramFolder)
  {
    if ((paramFolder != null) && (paramFolder.loadMoreUri != null))
      startAsyncRefreshTask(paramFolder.loadMoreUri);
  }

  public final boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    int i = 1;
    if (paramCursor == null)
    {
      String str9 = LOG_TAG;
      Object[] arrayOfObject7 = new Object[i];
      arrayOfObject7[0] = Integer.valueOf(paramLoader.getId());
      LogUtils.e(str9, "Received null cursor from loader id: %d", arrayOfObject7);
    }
    switch (paramLoader.getId())
    {
    case 1:
    case 4:
    default:
    case 0:
    case 7:
      do
      {
        boolean bool;
        do
        {
          Intent localIntent;
          do
          {
            int k;
            do
            {
              do
                return;
              while (paramCursor == null);
              if (paramCursor.getCount() != 0)
                break;
              int j = paramCursor.getExtras().getInt("accounts_loaded");
              k = 0;
              if (j != 0)
                k = i;
            }
            while (k == 0);
            localIntent = MailAppProvider.getNoAccountIntent(this.mContext);
          }
          while (localIntent == null);
          this.mActivity.startActivityForResult(localIntent, i);
          return;
          bool = accountsUpdated(paramCursor);
        }
        while ((this.isLoaderInitialized) && (!bool));
        this.isLoaderInitialized = updateAccounts(paramCursor);
        return;
      }
      while ((paramCursor == null) || (!paramCursor.moveToFirst()));
      Account localAccount = new Account(paramCursor);
      if (localAccount.uri.equals(this.mAccount.uri))
      {
        Settings localSettings = this.mAccount.settings;
        this.mAccount = localAccount;
        String str8 = LOG_TAG;
        Object[] arrayOfObject6 = new Object[i];
        arrayOfObject6[0] = this.mAccount.uri;
        LogUtils.d(str8, "AbstractActivityController.onLoadFinished(): mAccount = %s", arrayOfObject6);
        if (!Objects.equal(this.mAccount.settings, localSettings))
          this.mAccountObservers.notifyChanged();
        perhapsEnterWaitMode();
        return;
      }
      String str7 = LOG_TAG;
      Object[] arrayOfObject5 = new Object[2];
      arrayOfObject5[0] = localAccount.uri;
      arrayOfObject5[i] = this.mAccount.uri;
      LogUtils.e(str7, "Got update for account: %s with current account: %s", arrayOfObject5);
      restartOptionalLoader(7);
      return;
    case 2:
      if ((paramCursor != null) && (paramCursor.moveToFirst()))
      {
        Folder localFolder2 = new Folder(paramCursor);
        String str6 = LOG_TAG;
        Object[] arrayOfObject4 = new Object[i];
        arrayOfObject4[0] = Integer.valueOf(localFolder2.syncStatus);
        LogUtils.d(str6, "FOLDER STATUS = %d", arrayOfObject4);
        setHasFolderChanged(localFolder2);
        this.mFolder = localFolder2;
        this.mFolderObservable.notifyChanged();
        return;
      }
      String str4 = LOG_TAG;
      Object[] arrayOfObject3 = new Object[i];
      if (this.mFolder != null);
      for (String str5 = this.mAccount.name; ; str5 = "")
      {
        arrayOfObject3[0] = str5;
        LogUtils.d(str4, "Unable to get the folder %s", arrayOfObject3);
        return;
      }
    case 3:
      if ((paramCursor != null) && (paramCursor.getCount() <= i) && (!Utils.useTabletUI(this.mContext)))
      {
        Uri localUri = this.mAccount.defaultRecentFolderListUri;
        String str3 = LOG_TAG;
        Object[] arrayOfObject2 = new Object[i];
        arrayOfObject2[0] = localUri;
        LogUtils.v(str3, "Default recents at %s", arrayOfObject2);
        AsyncTask local1PopulateDefault = new AsyncTask()
        {
          protected Void doInBackground(Uri[] paramAnonymousArrayOfUri)
          {
            AbstractActivityController.this.mContext.getContentResolver().update(paramAnonymousArrayOfUri[0], null, null, null);
            return null;
          }
        };
        Uri[] arrayOfUri = new Uri[i];
        arrayOfUri[0] = localUri;
        local1PopulateDefault.execute(arrayOfUri);
        return;
      }
      LogUtils.v(LOG_TAG, "Reading recent folders from the cursor.", new Object[0]);
      loadRecentFolders(paramCursor);
      return;
    case 5:
      if ((paramCursor != null) && (!paramCursor.isClosed()) && (paramCursor.moveToFirst()))
      {
        onFolderChanged(new Folder(paramCursor));
        this.mActivity.getLoaderManager().destroyLoader(5);
        return;
      }
      String str1 = LOG_TAG;
      Object[] arrayOfObject1 = new Object[i];
      if (this.mAccount != null);
      for (String str2 = this.mAccount.name; ; str2 = "")
      {
        arrayOfObject1[0] = str2;
        LogUtils.d(str1, "Unable to get the account inbox for account %s", arrayOfObject1);
        return;
      }
    case 6:
    }
    if ((paramCursor != null) && (paramCursor.getCount() > 0))
    {
      paramCursor.moveToFirst();
      Folder localFolder1 = new Folder(paramCursor);
      updateFolder(localFolder1);
      this.mConvListContext = ConversationListContext.forSearchQuery(this.mAccount, this.mFolder, this.mActivity.getIntent().getStringExtra("query"));
      showConversationList(this.mConvListContext);
      this.mActivity.invalidateOptionsMenu();
      if (localFolder1.totalCount > 0);
      while (true)
      {
        this.mHaveSearchResults = i;
        this.mActivity.getLoaderManager().destroyLoader(6);
        return;
        i = 0;
      }
    }
    LogUtils.e(LOG_TAG, "Null or empty cursor returned by LOADER_SEARCH loader", new Object[0]);
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
  }

  public final boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    int i = paramMenuItem.getItemId();
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(i);
    LogUtils.d(str, "AbstractController.onOptionsItemSelected(%d) called.", arrayOfObject);
    boolean bool1 = true;
    Collection localCollection = Conversation.listOf(this.mCurrentConversation);
    Settings localSettings;
    if (this.mAccount == null)
    {
      localSettings = null;
      commitDestructiveActions(true);
      switch (i)
      {
      default:
        bool1 = false;
      case 2131689751:
      case 2131689752:
      case 2131689753:
      case 2131689754:
      case 2131689757:
      case 2131689758:
      case 2131689759:
      case 2131689760:
      case 2131689761:
      case 2131689762:
      case 16908332:
      case 2131689525:
      case 2131689766:
      case 2131689764:
      case 2131689748:
      case 2131689765:
      case 2131689750:
      case 2131689749:
      case 2131689475:
      case 2131689755:
      }
    }
    FolderSelectionDialog localFolderSelectionDialog;
    do
    {
      return bool1;
      localSettings = this.mAccount.settings;
      break;
      if ((localSettings != null) && (localSettings.confirmArchive));
      for (boolean bool4 = true; ; bool4 = false)
      {
        confirmAndDelete(localCollection, bool4, 2131755012, getDeferredAction(2131689751, localCollection, false));
        return bool1;
      }
      delete(2131689752, localCollection, getDeferredRemoveFolder(localCollection, this.mFolder, true, false, true));
      return bool1;
      if ((localSettings != null) && (localSettings.confirmDelete));
      for (boolean bool3 = true; ; bool3 = false)
      {
        confirmAndDelete(localCollection, bool3, 2131755011, getDeferredAction(2131689753, localCollection, false));
        return bool1;
      }
      if ((localSettings != null) && (localSettings.confirmDelete));
      for (boolean bool2 = true; ; bool2 = false)
      {
        confirmAndDelete(localCollection, bool2, 2131755013, getDeferredAction(2131689754, localCollection, false));
        return bool1;
      }
      updateConversation(Conversation.listOf(this.mCurrentConversation), UIProvider.ConversationColumns.PRIORITY, 1);
      return bool1;
      if ((this.mFolder != null) && (this.mFolder.isImportantOnly()))
      {
        delete(2131689758, localCollection, getDeferredAction(2131689758, localCollection, false));
        return bool1;
      }
      updateConversation(Conversation.listOf(this.mCurrentConversation), UIProvider.ConversationColumns.PRIORITY, 0);
      return bool1;
      delete(2131689759, localCollection, getDeferredAction(2131689759, localCollection, false));
      return bool1;
      delete(2131689760, localCollection, getDeferredAction(2131689760, localCollection, false));
      return bool1;
      delete(2131689761, localCollection, getDeferredAction(2131689761, localCollection, false));
      return bool1;
      delete(2131689762, localCollection, getDeferredAction(2131689762, localCollection, false));
      return bool1;
      onUpPressed();
      return bool1;
      ComposeActivity.compose(this.mActivity.getActivityContext(), this.mAccount);
      return bool1;
      showFolderList();
      return bool1;
      requestFolderRefresh();
      return bool1;
      Utils.showSettings(this.mActivity.getActivityContext(), this.mAccount);
      return bool1;
      Utils.showFolderSettings(this.mActivity.getActivityContext(), this.mAccount, this.mFolder);
      return bool1;
      Utils.showHelp(this.mActivity.getActivityContext(), this.mAccount, getHelpContext());
      return bool1;
      Utils.sendFeedback(this.mActivity.getActivityContext(), this.mAccount, false);
      return bool1;
      Utils.showManageFolder(this.mActivity.getActivityContext(), this.mAccount);
      return bool1;
      localFolderSelectionDialog = FolderSelectionDialog.getInstance(this.mActivity.getActivityContext(), this.mAccount, this, Conversation.listOf(this.mCurrentConversation), false, this.mFolder);
    }
    while (localFolderSelectionDialog == null);
    localFolderSelectionDialog.show();
    return bool1;
  }

  public void onPause()
  {
    this.isLoaderInitialized = false;
    enableNotifications();
  }

  public void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle)
  {
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return this.mActionBarView.onPrepareOptionsMenu(paramMenu);
  }

  public final void onRefreshReady()
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    if (this.mFolder != null);
    for (Object localObject = Integer.valueOf(this.mFolder.id); ; localObject = "-1")
    {
      arrayOfObject[0] = localObject;
      LogUtils.d(str, "Received refresh ready callback for folder %s", arrayOfObject);
      if (!isAnimating())
        this.mConversationListCursor.sync();
      this.mTracker.onCursorUpdated();
      perhapsShowFirstSearchResult();
      return;
    }
  }

  public final void onRefreshRequired()
  {
    if ((isAnimating()) || (isDragging()))
      LogUtils.d(LOG_TAG, "onRefreshRequired: delay until animating done", new Object[0]);
    while (!this.mConversationListCursor.isRefreshRequired())
      return;
    this.mConversationListCursor.refresh();
  }

  public void onRestart()
  {
    DialogFragment localDialogFragment = (DialogFragment)this.mFragmentManager.findFragmentByTag("SyncErrorDialogFragment");
    if (localDialogFragment != null)
      localDialogFragment.dismiss();
    if (this.mToastBar != null)
      this.mToastBar.hide(false);
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    LogUtils.d(LOG_TAG, "IN AAC.onRestoreInstanceState", new Object[0]);
    if (paramBundle.containsKey("saved-conversation"))
    {
      Conversation localConversation = (Conversation)paramBundle.getParcelable("saved-conversation");
      if ((localConversation != null) && (localConversation.position < 0))
        localConversation.position = 0;
      showConversation(localConversation);
    }
    ToastBarOperation localToastBarOperation;
    if (paramBundle.containsKey("saved-toast-bar-op"))
    {
      localToastBarOperation = (ToastBarOperation)paramBundle.getParcelable("saved-toast-bar-op");
      if (localToastBarOperation != null)
      {
        if (localToastBarOperation.getType() != 0)
          break label152;
        onUndoAvailable(localToastBarOperation);
      }
    }
    while (true)
    {
      String str = paramBundle.getString("saved-hierarchical-folder", null);
      if (!TextUtils.isEmpty(str))
        this.mFolderListFolder = Folder.fromString(str);
      ConversationListFragment localConversationListFragment = getConversationListFragment();
      if (localConversationListFragment != null)
        localConversationListFragment.getAnimatedAdapter().onRestoreInstanceState(paramBundle);
      restoreSelectedConversations(paramBundle);
      return;
      label152: if (localToastBarOperation.getType() == 1)
        onError(this.mFolder, true);
    }
  }

  public void onResume()
  {
    disableNotifications();
    this.mSafeToModifyFragments = true;
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    this.mViewMode.handleSaveInstanceState(paramBundle);
    if (this.mAccount != null)
    {
      LogUtils.d(LOG_TAG, "Saving the account now", new Object[0]);
      paramBundle.putParcelable("saved-account", this.mAccount);
    }
    if (this.mFolder != null)
      paramBundle.putParcelable("saved-folder", this.mFolder);
    if (ConversationListContext.isSearchResult(this.mConvListContext))
      paramBundle.putString("saved-query", this.mConvListContext.searchQuery);
    if ((this.mCurrentConversation != null) && (this.mViewMode.isConversationMode()))
      paramBundle.putParcelable("saved-conversation", this.mCurrentConversation);
    if (!this.mSelectedSet.isEmpty())
      paramBundle.putParcelable("saved-selected-set", this.mSelectedSet);
    if (this.mToastBar.getVisibility() == 0)
      paramBundle.putParcelable("saved-toast-bar-op", this.mToastBar.getOperation());
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    if (localConversationListFragment != null)
      localConversationListFragment.getAnimatedAdapter().onSaveInstanceState(paramBundle);
    this.mSafeToModifyFragments = false;
    if (this.mFolderListFolder != null);
    for (String str = Folder.toString(this.mFolderListFolder); ; str = null)
    {
      paramBundle.putString("saved-hierarchical-folder", str);
      return;
    }
  }

  public void onSearchRequested(String paramString)
  {
    Intent localIntent = new Intent();
    localIntent.setAction("android.intent.action.SEARCH");
    localIntent.putExtra("query", paramString);
    localIntent.putExtra("account", this.mAccount);
    localIntent.setComponent(this.mActivity.getComponentName());
    this.mActionBarView.collapseSearch();
    this.mActivity.startActivity(localIntent);
  }

  public void onSetChanged(ConversationSelectionSet paramConversationSelectionSet)
  {
  }

  public void onSetEmpty()
  {
  }

  public void onSetPopulated(ConversationSelectionSet paramConversationSelectionSet)
  {
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    if (localConversationListFragment == null)
      return;
    this.mCabActionMenu = new SelectedConversationsActionMenu(this.mActivity, paramConversationSelectionSet, this.mFolder, (SwipeableListView)localConversationListFragment.getListView());
    enableCabMode();
  }

  public void onStart()
  {
    this.mSafeToModifyFragments = true;
  }

  public void onStop()
  {
    if (this.mEnableShareIntents != null)
      this.mEnableShareIntents.cancel(true);
  }

  public void onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((paramMotionEvent.getAction() == 0) && (this.mToastBar != null) && (!this.mToastBar.isEventInToastBar(paramMotionEvent)))
      hideOrRepositionToastBar(true);
  }

  public void onViewModeChanged(int paramInt)
  {
    if (!ViewMode.isConversationMode(paramInt))
      setCurrentConversation(null);
    if (paramInt != 0)
      resetActionBarIcon();
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    if ((paramBoolean) && (localConversationListFragment != null) && (localConversationListFragment.isVisible()))
      informCursorVisiblity(true);
  }

  final void perhapsEnterWaitMode()
  {
    if (this.mAccount.isAccountInitializationRequired())
      showWaitForInitialization();
    boolean bool;
    do
    {
      return;
      bool = inWaitMode();
      if (this.mAccount.isAccountSyncRequired())
      {
        if (bool)
        {
          updateWaitMode();
          return;
        }
        showWaitForInitialization();
        return;
      }
    }
    while (!bool);
    hideWaitForInitialization();
  }

  public final void refreshConversationList()
  {
    ConversationListFragment localConversationListFragment = getConversationListFragment();
    if (localConversationListFragment == null)
      return;
    localConversationListFragment.requestListRefresh();
  }

  public void registerAccountObserver(DataSetObserver paramDataSetObserver)
  {
    this.mAccountObservers.registerObserver(paramDataSetObserver);
  }

  public void registerConversationListObserver(DataSetObserver paramDataSetObserver)
  {
    this.mConversationListObservable.registerObserver(paramDataSetObserver);
  }

  public void registerConversationLoadedObserver(DataSetObserver paramDataSetObserver)
  {
    this.mPagerController.registerConversationLoadedObserver(paramDataSetObserver);
  }

  public void registerFolderObserver(DataSetObserver paramDataSetObserver)
  {
    this.mFolderObservable.registerObserver(paramDataSetObserver);
  }

  public void registerRecentFolderObserver(DataSetObserver paramDataSetObserver)
  {
    this.mRecentFolderObservers.registerObserver(paramDataSetObserver);
  }

  protected abstract void resetActionBarIcon();

  protected boolean safeToModifyFragments()
  {
    return this.mSafeToModifyFragments;
  }

  public void setCurrentConversation(Conversation paramConversation)
  {
    this.mTracker.initialize(paramConversation);
    this.mCurrentConversation = paramConversation;
    if (this.mCurrentConversation != null)
    {
      this.mActionBarView.setCurrentConversation(this.mCurrentConversation);
      getSubjectDisplayChanger().setSubject(this.mCurrentConversation.subject);
      this.mActivity.invalidateOptionsMenu();
    }
  }

  public void setHierarchyFolder(Folder paramFolder)
  {
    this.mFolderListFolder = paramFolder;
  }

  protected final boolean shouldEnterSearchConvMode()
  {
    return (this.mHaveSearchResults) && (Utils.showTwoPaneSearchResults(this.mActivity.getActivityContext()));
  }

  protected void showConversation(Conversation paramConversation, boolean paramBoolean)
  {
    setCurrentConversation(paramConversation);
    updateRecentFolderList();
  }

  public void showConversationList(ConversationListContext paramConversationListContext)
  {
  }

  protected final void showErrorToast(Folder paramFolder, boolean paramBoolean)
  {
    this.mToastBar.setConversationMode(false);
    int i = paramFolder.lastSyncResult;
    int m;
    int n;
    label90: ActionableToastBar.ActionClickedListener localActionClickedListener;
    int j;
    switch (i & 0xF)
    {
    case 3:
    default:
    case 1:
      do
      {
        return;
        int k = i >> 4;
        if ((k & 0x1) == 0)
          break;
        m = 1;
        if ((m != 0) || ((paramFolder.syncWindow <= 0) && ((k & 0x4) == 0)))
          break label154;
        n = 1;
      }
      while (n != 0);
      localActionClickedListener = getRetryClickedListener(paramFolder);
      j = 2131427329;
    case 2:
    case 4:
    case 5:
    }
    while (true)
    {
      this.mToastBar.show(localActionClickedListener, 2130837548, Utils.getSyncStatusText(this.mActivity.getActivityContext(), i), false, j, paramBoolean, new ToastBarOperation(1, 0, 1, false));
      return;
      m = 0;
      break;
      label154: n = 0;
      break label90;
      localActionClickedListener = getSignInClickedListener();
      j = 2131427576;
      continue;
      localActionClickedListener = getStorageErrorClickedListener();
      j = 2131427577;
      continue;
      localActionClickedListener = getInternalErrorClickedListener();
      j = 2131427578;
    }
  }

  public void showNextConversation(Collection<Conversation> paramCollection)
  {
    showNextConversation(paramCollection, null);
  }

  public void showWaitForInitialization()
  {
    this.mViewMode.enterWaitingForInitializationMode();
    this.mWaitFragment = WaitFragment.newInstance(this.mAccount);
  }

  public void starMessage(MessageCursor.ConversationMessage paramConversationMessage, boolean paramBoolean)
  {
    int i = 1;
    if (paramConversationMessage.starred == paramBoolean)
      return;
    paramConversationMessage.starred = paramBoolean;
    ContentValues localContentValues;
    String str;
    if ((paramBoolean) || (paramConversationMessage.isConversationStarred()))
    {
      int j = i;
      Conversation localConversation = paramConversationMessage.getConversation();
      if (j != localConversation.starred)
      {
        localConversation.starred = j;
        this.mConversationListCursor.setConversationColumn(localConversation.uri, UIProvider.ConversationColumns.STARRED, Boolean.valueOf(j));
      }
      localContentValues = new ContentValues(i);
      str = UIProvider.MessageColumns.STARRED;
      if (!paramBoolean)
        break label133;
    }
    while (true)
    {
      localContentValues.put(str, Integer.valueOf(i));
      new ContentProviderTask.UpdateTask()
      {
        protected void onPostExecute(ContentProviderTask.Result paramAnonymousResult)
        {
        }
      }
      .run(this.mResolver, paramConversationMessage.uri, localContentValues, null, null);
      return;
      int k = 0;
      break;
      label133: i = 0;
    }
  }

  public void startDragMode()
  {
    this.mIsDragHappening = true;
  }

  public void startSearch()
  {
    if (this.mAccount == null)
    {
      LogUtils.d(LOG_TAG, "AbstractActivityController.startSearch(): null account", new Object[0]);
      return;
    }
    if ((this.mAccount.supportsCapability(2048) | this.mAccount.supportsCapability(32)))
    {
      onSearchRequested(this.mActionBarView.getQuery());
      return;
    }
    Toast.makeText(this.mActivity.getActivityContext(), this.mActivity.getActivityContext().getString(2131427493), 0).show();
  }

  public void stopDragMode()
  {
    this.mIsDragHappening = false;
    if (this.mConversationListCursor.isRefreshReady())
    {
      LogUtils.d(LOG_TAG, "Stopped animating: try sync", new Object[0]);
      onRefreshReady();
    }
    if (this.mConversationListCursor.isRefreshRequired())
    {
      LogUtils.d(LOG_TAG, "Stopped animating: refresh", new Object[0]);
      this.mConversationListCursor.refresh();
    }
  }

  public boolean supportsDrag(DragEvent paramDragEvent, Folder paramFolder)
  {
    return (paramFolder != null) && (paramDragEvent != null) && (paramDragEvent.getClipDescription() != null) && (paramFolder.supportsCapability(8)) && (paramFolder.supportsCapability(4)) && (!this.mFolder.uri.equals(paramFolder.uri));
  }

  public void unregisterAccountObserver(DataSetObserver paramDataSetObserver)
  {
    this.mAccountObservers.unregisterObserver(paramDataSetObserver);
  }

  public void unregisterConversationListObserver(DataSetObserver paramDataSetObserver)
  {
    this.mConversationListObservable.unregisterObserver(paramDataSetObserver);
  }

  public void unregisterConversationLoadedObserver(DataSetObserver paramDataSetObserver)
  {
    this.mPagerController.unregisterConversationLoadedObserver(paramDataSetObserver);
  }

  public void unregisterFolderObserver(DataSetObserver paramDataSetObserver)
  {
    this.mFolderObservable.unregisterObserver(paramDataSetObserver);
  }

  public void unregisterRecentFolderObserver(DataSetObserver paramDataSetObserver)
  {
    this.mRecentFolderObservers.unregisterObserver(paramDataSetObserver);
  }

  public void updateConversation(Collection<Conversation> paramCollection, String paramString, int paramInt)
  {
    this.mConversationListCursor.updateInt(this.mContext, paramCollection, paramString, paramInt);
    refreshConversationList();
  }

  public void updateConversation(Collection<Conversation> paramCollection, String paramString, boolean paramBoolean)
  {
    this.mConversationListCursor.updateBoolean(this.mContext, paramCollection, paramString, paramBoolean);
    refreshConversationList();
  }

  public class ConversationAction
    implements DestructiveAction
  {
    private final int mAction;
    private boolean mCompleted;
    private final boolean mIsSelectedSet;
    private final Collection<Conversation> mTarget;

    public ConversationAction(Collection<Conversation> paramBoolean, boolean arg3)
    {
      this.mAction = paramBoolean;
      Collection localCollection;
      this.mTarget = ImmutableList.copyOf(localCollection);
      boolean bool;
      this.mIsSelectedSet = bool;
    }

    private boolean isPerformed()
    {
      boolean bool1 = true;
      try
      {
        boolean bool2 = this.mCompleted;
        if (bool2);
        while (true)
        {
          return bool1;
          this.mCompleted = true;
          bool1 = false;
        }
      }
      finally
      {
      }
    }

    public void performAction()
    {
      if (isPerformed())
        return;
      boolean bool = AbstractActivityController.this.mAccount.supportsCapability(16384);
      if (LogUtils.isLoggable(AbstractActivityController.LOG_TAG, 3))
      {
        String str2 = AbstractActivityController.LOG_TAG;
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = Conversation.toString(this.mTarget);
        arrayOfObject2[1] = AbstractActivityController.this.mCurrentConversation;
        LogUtils.d(str2, "ConversationAction.performAction():\nmTarget=%s\nCurrent=%s", arrayOfObject2);
      }
      if (AbstractActivityController.this.mConversationListCursor == null)
      {
        String str1 = AbstractActivityController.LOG_TAG;
        Object[] arrayOfObject1 = new Object[2];
        arrayOfObject1[0] = Conversation.toString(this.mTarget);
        arrayOfObject1[1] = AbstractActivityController.this.mCurrentConversation;
        LogUtils.e(str1, "null ConversationCursor in ConversationAction.performAction():\nmTarget=%s\nCurrent=%s", arrayOfObject1);
        return;
      }
      switch (this.mAction)
      {
      case 2131689752:
      case 2131689755:
      case 2131689756:
      case 2131689757:
      case 2131689763:
      case 2131689764:
      case 2131689765:
      case 2131689766:
      case 2131689767:
      default:
      case 2131689751:
      case 2131689753:
      case 2131689759:
      case 2131689760:
      case 2131689761:
      case 2131689762:
      case 2131689768:
      case 2131689758:
      case 2131689754:
      }
      while (true)
      {
        if (bool)
          AbstractActivityController.this.mHandler.postDelayed(new Runnable()
          {
            public void run()
            {
              AbstractActivityController.this.onUndoAvailable(new ToastBarOperation(AbstractActivityController.ConversationAction.this.mTarget.size(), AbstractActivityController.ConversationAction.this.mAction, 0, AbstractActivityController.ConversationAction.this.mIsSelectedSet));
            }
          }
          , AbstractActivityController.this.mShowUndoBarDelay);
        AbstractActivityController.this.refreshConversationList();
        if (!this.mIsSelectedSet)
          break;
        AbstractActivityController.this.mSelectedSet.clear();
        return;
        LogUtils.d(AbstractActivityController.LOG_TAG, "Archiving", new Object[0]);
        AbstractActivityController.this.mConversationListCursor.archive(AbstractActivityController.this.mContext, this.mTarget);
        continue;
        LogUtils.d(AbstractActivityController.LOG_TAG, "Deleting", new Object[0]);
        AbstractActivityController.this.mConversationListCursor.delete(AbstractActivityController.this.mContext, this.mTarget);
        if (AbstractActivityController.this.mFolder.supportsCapability(2048))
        {
          bool = false;
          continue;
          LogUtils.d(AbstractActivityController.LOG_TAG, "Muting", new Object[0]);
          if (AbstractActivityController.this.mFolder.supportsCapability(256))
          {
            Iterator localIterator3 = this.mTarget.iterator();
            while (localIterator3.hasNext())
              ((Conversation)localIterator3.next()).localDeleteOnUpdate = true;
          }
          AbstractActivityController.this.mConversationListCursor.mute(AbstractActivityController.this.mContext, this.mTarget);
          continue;
          LogUtils.d(AbstractActivityController.LOG_TAG, "Reporting spam", new Object[0]);
          AbstractActivityController.this.mConversationListCursor.reportSpam(AbstractActivityController.this.mContext, this.mTarget);
          continue;
          LogUtils.d(AbstractActivityController.LOG_TAG, "Marking not spam", new Object[0]);
          AbstractActivityController.this.mConversationListCursor.reportNotSpam(AbstractActivityController.this.mContext, this.mTarget);
          continue;
          LogUtils.d(AbstractActivityController.LOG_TAG, "Reporting phishing", new Object[0]);
          AbstractActivityController.this.mConversationListCursor.reportPhishing(AbstractActivityController.this.mContext, this.mTarget);
          continue;
          LogUtils.d(AbstractActivityController.LOG_TAG, "Removing star", new Object[0]);
          AbstractActivityController.this.mConversationListCursor.updateBoolean(AbstractActivityController.this.mContext, this.mTarget, UIProvider.ConversationColumns.STARRED, false);
          continue;
          LogUtils.d(AbstractActivityController.LOG_TAG, "Marking not-important", new Object[0]);
          if ((AbstractActivityController.this.mFolder != null) && (AbstractActivityController.this.mFolder.isImportantOnly()))
          {
            Iterator localIterator2 = this.mTarget.iterator();
            while (localIterator2.hasNext())
              ((Conversation)localIterator2.next()).localDeleteOnUpdate = true;
          }
          AbstractActivityController.this.mConversationListCursor.updateInt(AbstractActivityController.this.mContext, this.mTarget, UIProvider.ConversationColumns.PRIORITY, 0);
          continue;
          LogUtils.d(AbstractActivityController.LOG_TAG, "Discarding draft messages", new Object[0]);
          if ((AbstractActivityController.this.mFolder != null) && (AbstractActivityController.this.mFolder.isDraft()))
          {
            Iterator localIterator1 = this.mTarget.iterator();
            while (localIterator1.hasNext())
              ((Conversation)localIterator1.next()).localDeleteOnUpdate = true;
          }
          AbstractActivityController.this.mConversationListCursor.discardDrafts(AbstractActivityController.this.mContext, this.mTarget);
          bool = false;
        }
      }
    }
  }

  private class ConversationListLoaderCallbacks
    implements LoaderManager.LoaderCallbacks<ConversationCursor>
  {
    private ConversationListLoaderCallbacks()
    {
    }

    public Loader<ConversationCursor> onCreateLoader(int paramInt, Bundle paramBundle)
    {
      return new ConversationCursorLoader((Activity)AbstractActivityController.this.mActivity, AbstractActivityController.this.mAccount, AbstractActivityController.this.mFolder.conversationListUri, AbstractActivityController.this.mFolder.name);
    }

    public void onLoadFinished(Loader<ConversationCursor> paramLoader, ConversationCursor paramConversationCursor)
    {
      LogUtils.d(AbstractActivityController.LOG_TAG, "IN AAC.ConversationCursor.onLoadFinished, data=%s loader=%s", new Object[] { paramConversationCursor, paramLoader });
      AbstractActivityController.this.destroyPending(null);
      AbstractActivityController.this.mConversationListCursor = paramConversationCursor;
      AbstractActivityController.this.mConversationListCursor.addListener(AbstractActivityController.this);
      AbstractActivityController.this.mTracker.onCursorUpdated();
      AbstractActivityController.this.mConversationListObservable.notifyChanged();
      ConversationListFragment localConversationListFragment = AbstractActivityController.this.getConversationListFragment();
      if ((localConversationListFragment != null) && (localConversationListFragment.isVisible()))
        AbstractActivityController.this.informCursorVisiblity(true);
      AbstractActivityController.this.perhapsShowFirstSearchResult();
    }

    public void onLoaderReset(Loader<ConversationCursor> paramLoader)
    {
      String str = AbstractActivityController.LOG_TAG;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = AbstractActivityController.this.mConversationListCursor;
      arrayOfObject[1] = paramLoader;
      LogUtils.d(str, "IN AAC.ConversationCursor.onLoaderReset, data=%s loader=%s", arrayOfObject);
      if (AbstractActivityController.this.mConversationListCursor != null)
      {
        AbstractActivityController.this.mConversationListCursor.removeListener(AbstractActivityController.this);
        AbstractActivityController.this.mConversationListCursor = null;
        AbstractActivityController.this.mTracker.onCursorUpdated();
        AbstractActivityController.this.mConversationListObservable.notifyChanged();
      }
    }
  }

  private class FolderDestruction
    implements DestructiveAction
  {
    private int mAction;
    private boolean mCompleted;
    private final ArrayList<FolderOperation> mFolderOps = new ArrayList();
    private final boolean mIsDestructive;
    private boolean mIsSelectedSet;
    private boolean mShowUndo;
    private final Collection<Conversation> mTarget;

    private FolderDestruction(Collection<FolderOperation> paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramInt, int arg6)
    {
      this.mTarget = ImmutableList.copyOf(paramBoolean1);
      this.mFolderOps.addAll(paramBoolean2);
      this.mIsDestructive = paramBoolean3;
      this.mIsSelectedSet = paramInt;
      boolean bool;
      this.mShowUndo = bool;
      int i;
      this.mAction = i;
    }

    private boolean isPerformed()
    {
      boolean bool1 = true;
      try
      {
        boolean bool2 = this.mCompleted;
        if (bool2);
        while (true)
        {
          return bool1;
          this.mCompleted = true;
          bool1 = false;
        }
      }
      finally
      {
      }
    }

    public void performAction()
    {
      if (isPerformed());
      do
      {
        return;
        if ((this.mIsDestructive) && (this.mShowUndo))
        {
          ToastBarOperation localToastBarOperation = new ToastBarOperation(this.mTarget.size(), this.mAction, 0, this.mIsSelectedSet);
          AbstractActivityController.this.onUndoAvailable(localToastBarOperation);
        }
        ArrayList localArrayList = new ArrayList(this.mTarget.size());
        Iterator localIterator1 = this.mTarget.iterator();
        while (localIterator1.hasNext())
        {
          Conversation localConversation = (Conversation)localIterator1.next();
          HashMap localHashMap = Folder.hashMapForFolders(localConversation.getRawFolders());
          if (this.mIsDestructive)
            localConversation.localDeleteOnUpdate = true;
          Iterator localIterator2 = this.mFolderOps.iterator();
          while (localIterator2.hasNext())
          {
            FolderOperation localFolderOperation = (FolderOperation)localIterator2.next();
            if (localFolderOperation.mAdd)
              localHashMap.put(localFolderOperation.mFolder.uri, localFolderOperation.mFolder);
            else
              localHashMap.remove(localFolderOperation.mFolder.uri);
          }
          localArrayList.add(Folder.getSerializedFolderString(localHashMap.values()));
        }
        if (AbstractActivityController.this.mConversationListCursor != null)
          AbstractActivityController.this.mConversationListCursor.updateStrings(AbstractActivityController.this.mContext, this.mTarget, "rawFolders", localArrayList);
        AbstractActivityController.this.refreshConversationList();
      }
      while (!this.mIsSelectedSet);
      AbstractActivityController.this.mSelectedSet.clear();
    }
  }

  static class RefreshTimerTask extends TimerTask
  {
    final AbstractActivityController mController;
    final Handler mHandler;

    public void run()
    {
      this.mHandler.post(new Runnable()
      {
        public void run()
        {
          LogUtils.d(AbstractActivityController.LOG_TAG, "Delay done... calling onRefreshRequired", new Object[0]);
          AbstractActivityController.RefreshTimerTask.this.mController.onRefreshRequired();
        }
      });
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.AbstractActivityController
 * JD-Core Version:    0.6.2
 */