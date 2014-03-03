package com.android.mail.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.android.mail.ConversationListContext;
import com.android.mail.browse.ConversationCursor;
import com.android.mail.browse.ConversationItemView;
import com.android.mail.browse.ConversationItemViewModel;
import com.android.mail.browse.ConversationListFooterView;
import com.android.mail.browse.ToggleableItem;
import com.android.mail.providers.Account;
import com.android.mail.providers.AccountObserver;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.Settings;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class ConversationListFragment extends ListFragment
  implements AdapterView.OnItemLongClickListener, SwipeableListView.ListItemSwipedListener, ViewMode.ModeChangeListener
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private static int TIMESTAMP_UPDATE_INTERVAL = 0;
  private static boolean mTabletDevice;
  private Account mAccount;
  private final AccountObserver mAccountObserver = new AccountObserver()
  {
    public void onChanged(Account paramAnonymousAccount)
    {
      ConversationListFragment.access$002(ConversationListFragment.this, paramAnonymousAccount);
      ConversationListFragment.this.setSwipeAction();
    }
  };
  private ControllableActivity mActivity;
  private ConversationListCallbacks mCallbacks;
  private int mConversationCursorHash;
  private DataSetObserver mConversationListStatusObserver;
  private View mEmptyView;
  private ErrorListener mErrorListener;
  private Folder mFolder;
  private DataSetObserver mFolderObserver;
  private ConversationListFooterView mFooterView;
  private final Handler mHandler = new Handler();
  private AnimatedAdapter mListAdapter;
  private SwipeableListView mListView;
  private TextView mSearchResultCountTextView;
  private TextView mSearchStatusTextView;
  private View mSearchStatusView;
  private ConversationSelectionSet mSelectedSet;
  private Runnable mUpdateTimestampsRunnable = null;
  private ConversationUpdater mUpdater;
  private ConversationListContext mViewContext;

  private ConversationCursor getConversationListCursor()
  {
    if (this.mCallbacks != null)
      return this.mCallbacks.getConversationListCursor();
    return null;
  }

  private void initializeUiForFirstDisplay()
  {
    this.mSearchStatusView = this.mActivity.findViewById(2131689687);
    this.mSearchStatusTextView = ((TextView)this.mActivity.findViewById(2131689688));
    this.mSearchResultCountTextView = ((TextView)this.mActivity.findViewById(2131689689));
  }

  public static ConversationListFragment newInstance(ConversationListContext paramConversationListContext)
  {
    ConversationListFragment localConversationListFragment = new ConversationListFragment();
    Bundle localBundle = new Bundle();
    localBundle.putBundle("conversation-list", paramConversationListContext.toBundle());
    localConversationListFragment.setArguments(localBundle);
    return localConversationListFragment;
  }

  private void onCursorUpdated()
  {
    if ((this.mCallbacks == null) || (this.mListAdapter == null))
      return;
    ConversationCursor localConversationCursor = this.mCallbacks.getConversationListCursor();
    this.mListAdapter.swapCursor(localConversationCursor);
    if (localConversationCursor == null);
    for (int i = 0; ; i = localConversationCursor.hashCode())
    {
      if ((this.mConversationCursorHash == i) && (this.mConversationCursorHash != 0))
        this.mListAdapter.notifyDataSetChanged();
      this.mConversationCursorHash = i;
      Conversation localConversation = this.mCallbacks.getCurrentConversation();
      if ((localConversation == null) || (this.mListView.getCheckedItemPosition() != -1))
        break;
      setSelected(localConversation.position);
      return;
    }
  }

  private void onFolderStatusUpdated()
  {
    ConversationCursor localConversationCursor = getConversationListCursor();
    Bundle localBundle;
    int i;
    label30: int j;
    if (localConversationCursor != null)
    {
      localBundle = localConversationCursor.getExtras();
      if (!localBundle.containsKey("cursor_error"))
        break label106;
      i = localBundle.getInt("cursor_error");
      j = localBundle.getInt("cursor_status");
      if (this.mFolder == null)
        break label111;
    }
    label106: label111: for (int k = this.mFolder.totalCount; ; k = 0)
    {
      if (((i == 0) && ((j == 2) || (j == 8))) || (k > 0))
      {
        updateSearchResultHeader(k);
        if (k == 0)
          this.mListView.setEmptyView(this.mEmptyView);
      }
      return;
      localBundle = Bundle.EMPTY;
      break;
      i = 0;
      break label30;
    }
  }

  private void setSwipeAction()
  {
    int i = Settings.getSwipeSetting(this.mAccount.settings);
    if ((i == 2) || (!this.mAccount.supportsCapability(16384)) || ((this.mFolder != null) && (this.mFolder.isTrash())))
    {
      this.mListView.enableSwipe(false);
      this.mListView.setCurrentFolder(this.mFolder);
      return;
    }
    int j;
    if ((ConversationListContext.isSearchResult(this.mViewContext)) || ((this.mFolder != null) && (this.mFolder.type == 6)))
      j = 2131689753;
    while (true)
    {
      this.mListView.setSwipeAction(j);
      break;
      if (this.mFolder == null)
      {
        j = 2131689752;
      }
      else
      {
        switch (i)
        {
        default:
        case 0:
        }
        do
        {
          do
          {
            j = 2131689753;
            break;
          }
          while (!this.mAccount.supportsCapability(8));
          if (this.mFolder.supportsCapability(16))
          {
            j = 2131689751;
            break;
          }
        }
        while (!this.mFolder.supportsCapability(8));
        j = 2131689752;
      }
    }
  }

  private void showList()
  {
    this.mListView.setEmptyView(null);
    onFolderUpdated(this.mActivity.getFolderController().getFolder());
    onConversationListStatusUpdated();
  }

  private void updateSearchResultHeader(int paramInt)
  {
    if (this.mActivity == null);
    Resources localResources;
    do
    {
      return;
      localResources = getResources();
    }
    while (!ConversationListContext.isSearchResult(this.mViewContext));
    this.mSearchStatusTextView.setText(localResources.getString(2131427492));
    TextView localTextView = this.mSearchResultCountTextView;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    localTextView.setText(localResources.getString(2131427558, arrayOfObject));
  }

  public void commitDestructiveActions(boolean paramBoolean)
  {
    if (this.mListView != null)
      this.mListView.commitDestructiveActions(paramBoolean);
  }

  void configureSearchResultHeader()
  {
    if (this.mActivity == null)
      return;
    Resources localResources = getResources();
    boolean bool = ConversationListContext.isSearchResult(this.mViewContext);
    if (bool)
    {
      this.mSearchStatusTextView.setText(localResources.getString(2131427501));
      this.mSearchResultCountTextView.setText("");
    }
    View localView = this.mSearchStatusView;
    int i;
    if (bool)
    {
      i = 0;
      localView.setVisibility(i);
      if (!bool)
        break label117;
    }
    label117: for (int j = (int)localResources.getDimension(2131361856); ; j = 0)
    {
      ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mListView.getLayoutParams();
      localMarginLayoutParams.topMargin = j;
      this.mListView.setLayoutParams(localMarginLayoutParams);
      return;
      i = 8;
      break;
    }
  }

  public AnimatedAdapter getAnimatedAdapter()
  {
    return this.mListAdapter;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    Activity localActivity = getActivity();
    if (!(localActivity instanceof ControllableActivity))
      LogUtils.e(LOG_TAG, "ConversationListFragment expects only a ControllableActivity tocreate it. Cannot proceed.", new Object[0]);
    this.mActivity = ((ControllableActivity)localActivity);
    this.mAccount = this.mAccountObserver.initialize(this.mActivity.getAccountController());
    this.mCallbacks = this.mActivity.getListHandler();
    this.mErrorListener = this.mActivity.getErrorListener();
    this.mFooterView = ((ConversationListFooterView)LayoutInflater.from(this.mActivity.getActivityContext()).inflate(2130968607, null));
    this.mFooterView.setClickListener(this.mActivity);
    ConversationCursor localConversationCursor = getConversationListCursor();
    this.mListAdapter = new AnimatedAdapter(this.mActivity.getApplicationContext(), -1, localConversationCursor, this.mActivity.getSelectedSet(), this.mActivity, this.mListView);
    this.mListAdapter.addFooter(this.mFooterView);
    this.mListView.setAdapter(this.mListAdapter);
    this.mSelectedSet = this.mActivity.getSelectedSet();
    this.mListView.setSelectionSet(this.mSelectedSet);
    this.mListAdapter.hideFooter();
    this.mFolderObserver = new FolderObserver(null);
    this.mActivity.getFolderController().registerFolderObserver(this.mFolderObserver);
    this.mConversationListStatusObserver = new ConversationListStatusObserver(null);
    this.mUpdater = this.mActivity.getConversationUpdater();
    this.mUpdater.registerConversationListObserver(this.mConversationListStatusObserver);
    mTabletDevice = Utils.useTabletUI(this.mActivity.getApplicationContext());
    initializeUiForFirstDisplay();
    configureSearchResultHeader();
    onViewModeChanged(this.mActivity.getViewMode().getMode());
    this.mActivity.getViewMode().addListener(this);
    if (this.mActivity.isFinishing())
      return;
    if (localConversationCursor == null);
    for (int i = 0; ; i = localConversationCursor.hashCode())
    {
      this.mConversationCursorHash = i;
      if ((localConversationCursor != null) && (localConversationCursor.isRefreshReady()))
        localConversationCursor.sync();
      showList();
      ToastBarOperation localToastBarOperation = this.mActivity.getPendingToastOperation();
      if (localToastBarOperation == null)
        break;
      this.mActivity.setPendingToastOperation(null);
      this.mActivity.onUndoAvailable(localToastBarOperation);
      return;
    }
  }

  public void onConversationListStatusUpdated()
  {
    ConversationCursor localConversationCursor = getConversationListCursor();
    if ((localConversationCursor != null) && (this.mFooterView.updateStatus(localConversationCursor)));
    for (boolean bool = true; ; bool = false)
    {
      onFolderStatusUpdated();
      this.mListAdapter.setFooterVisibility(bool);
      onCursorUpdated();
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    TIMESTAMP_UPDATE_INTERVAL = getResources().getInteger(2131296279);
    this.mUpdateTimestampsRunnable = new Runnable()
    {
      public void run()
      {
        ConversationListFragment.this.mListView.invalidateViews();
        ConversationListFragment.this.mHandler.postDelayed(ConversationListFragment.this.mUpdateTimestampsRunnable, ConversationListFragment.TIMESTAMP_UPDATE_INTERVAL);
      }
    };
    this.mViewContext = ConversationListContext.forBundle(getArguments().getBundle("conversation-list"));
    this.mAccount = this.mViewContext.account;
    setRetainInstance(false);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130968606, null);
    this.mEmptyView = localView.findViewById(2131689560);
    this.mListView = ((SwipeableListView)localView.findViewById(16908298));
    this.mListView.setHeaderDividersEnabled(false);
    this.mListView.setChoiceMode(1);
    this.mListView.setOnItemLongClickListener(this);
    this.mListView.enableSwipe(this.mAccount.supportsCapability(16384));
    this.mListView.setSwipedListener(this);
    if ((paramBundle != null) && (paramBundle.containsKey("list-state")))
    {
      this.mListView.onRestoreInstanceState(paramBundle.getParcelable("list-state"));
      this.mListView.clearChoices();
    }
    return localView;
  }

  public void onDestroy()
  {
    super.onDestroy();
  }

  public void onDestroyView()
  {
    this.mListAdapter.destroy();
    this.mListView.setAdapter(null);
    this.mActivity.unsetViewModeListener(this);
    if (this.mFolderObserver != null)
    {
      this.mActivity.getFolderController().unregisterFolderObserver(this.mFolderObserver);
      this.mFolderObserver = null;
    }
    if (this.mConversationListStatusObserver != null)
    {
      this.mUpdater.unregisterConversationListObserver(this.mConversationListStatusObserver);
      this.mConversationListStatusObserver = null;
    }
    this.mAccountObserver.unregisterAndDestroy();
    super.onDestroyView();
  }

  public void onFolderUpdated(Folder paramFolder)
  {
    this.mFolder = paramFolder;
    setSwipeAction();
    if (this.mFolder == null)
      return;
    this.mListAdapter.setFolder(this.mFolder);
    this.mFooterView.setFolder(this.mFolder);
    if (!this.mFolder.wasSyncSuccessful())
      this.mErrorListener.onError(this.mFolder, false);
    onFolderStatusUpdated();
    ConversationItemViewModel.onFolderUpdated(this.mFolder);
  }

  public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (!(paramView instanceof ConversationItemView))
      return true;
    ((ConversationItemView)paramView).toggleCheckMarkOrBeginDrag();
    return true;
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    if (!(paramView instanceof ToggleableItem))
      return;
    if ((this.mAccount.settings.hideCheckboxes) && (!this.mSelectedSet.isEmpty()))
      ((ToggleableItem)paramView).toggleCheckMarkOrBeginDrag();
    while (true)
    {
      commitDestructiveActions(Utils.useTabletUI(this.mActivity.getActivityContext()));
      return;
      viewConversation(paramInt);
    }
  }

  public void onListItemSwiped(Collection<Conversation> paramCollection)
  {
    this.mUpdater.showNextConversation(paramCollection);
  }

  public void onPause()
  {
    super.onPause();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mListView != null)
      paramBundle.putParcelable("list-state", this.mListView.onSaveInstanceState());
  }

  public void onStart()
  {
    super.onStart();
    this.mHandler.postDelayed(this.mUpdateTimestampsRunnable, TIMESTAMP_UPDATE_INTERVAL);
  }

  public void onStop()
  {
    super.onStop();
    this.mHandler.removeCallbacks(this.mUpdateTimestampsRunnable);
  }

  public void onViewModeChanged(int paramInt)
  {
    if (mTabletDevice)
      if (paramInt == 1)
        this.mListView.setBackgroundResource(2130837633);
    while (true)
    {
      if (this.mFooterView != null)
        this.mFooterView.onViewModeChanged(paramInt);
      return;
      if ((paramInt == 2) || (paramInt == 4))
      {
        this.mListView.clearChoices();
        this.mListView.setBackgroundDrawable(null);
        continue;
        this.mListView.setBackgroundDrawable(null);
      }
    }
  }

  public void requestDelete(int paramInt, Collection<Conversation> paramCollection, Collection<ConversationItemView> paramCollection1, final DestructiveAction paramDestructiveAction)
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      ((Conversation)localIterator.next()).localDeleteOnUpdate = true;
    SwipeableListView.ListItemsRemovedListener local3 = new SwipeableListView.ListItemsRemovedListener()
    {
      public void onListItemsRemoved()
      {
        paramDestructiveAction.performAction();
      }
    };
    SwipeableListView localSwipeableListView = (SwipeableListView)getListView();
    if (localSwipeableListView.getSwipeAction() == paramInt)
    {
      if (paramCollection1 == null)
      {
        localSwipeableListView.destroyItems(paramCollection, local3);
        return;
      }
      localSwipeableListView.destroyItems(new ArrayList(paramCollection1), local3);
      return;
    }
    this.mListAdapter.delete(paramCollection, local3);
  }

  public void requestListRefresh()
  {
    this.mListAdapter.notifyDataSetChanged();
  }

  protected final void setSelected(int paramInt)
  {
    setSelected(paramInt, true);
  }

  public void setSelected(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
      this.mListView.smoothScrollToPosition(paramInt);
    this.mListView.setItemChecked(paramInt, true);
  }

  protected void viewConversation(int paramInt)
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    LogUtils.d(str, "ConversationListFragment.viewConversation(%d)", arrayOfObject);
    setSelected(paramInt);
    ConversationCursor localConversationCursor = getConversationListCursor();
    if ((localConversationCursor != null) && (localConversationCursor.moveToPosition(paramInt)))
    {
      Conversation localConversation = new Conversation(localConversationCursor);
      localConversation.position = paramInt;
      this.mCallbacks.onConversationSelected(localConversation, false);
    }
  }

  private class ConversationListStatusObserver extends DataSetObserver
  {
    private ConversationListStatusObserver()
    {
    }

    public void onChanged()
    {
      ConversationListFragment.this.onConversationListStatusUpdated();
    }
  }

  private class FolderObserver extends DataSetObserver
  {
    private FolderObserver()
    {
    }

    public void onChanged()
    {
      if (ConversationListFragment.this.mActivity == null);
      FolderController localFolderController;
      do
      {
        return;
        localFolderController = ConversationListFragment.this.mActivity.getFolderController();
      }
      while (localFolderController == null);
      ConversationListFragment.this.onFolderUpdated(localFolderController.getFolder());
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ConversationListFragment
 * JD-Core Version:    0.6.2
 */