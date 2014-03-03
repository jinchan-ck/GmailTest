package com.android.mail.ui;

import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.TextView;
import com.android.mail.AccountSpinnerAdapter;
import com.android.mail.browse.SnippetTextView;
import com.android.mail.providers.Account;
import com.android.mail.providers.AccountObserver;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;

public class MailActionBarView extends LinearLayout
  implements MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, SubjectDisplayChanger, ViewMode.ModeChangeListener
{
  public static final String LOG_TAG = LogTag.getLogTag();
  private Account mAccount;
  private final AccountObserver mAccountObserver = new AccountObserver()
  {
    public void onChanged(Account paramAnonymousAccount)
    {
      MailActionBarView.access$002(MailActionBarView.this, paramAnonymousAccount);
      if (MailActionBarView.this.mFolderAccountName != null)
        MailActionBarView.this.mFolderAccountName.setText(MailActionBarView.this.mAccount.name);
      MailActionBarView.this.mSpinner.setAccount(MailActionBarView.this.mAccount);
    }
  };
  protected ActionBar mActionBar;
  protected ControllableActivity mActivity;
  protected ActivityController mController;
  private Conversation mCurrentConversation;
  private Folder mFolder;
  private TextView mFolderAccountName;
  private DataSetObserver mFolderObserver;
  private MenuItem mFolderSettingsItem;
  private View mFolderView;
  private final Handler mHandler = new Handler();
  private boolean mHasManyAccounts;
  private MenuItem mHelpItem;
  private final Runnable mInvalidateMenu = new Runnable()
  {
    public void run()
    {
      MailActionBarView.this.mActivity.invalidateOptionsMenu();
    }
  };
  private final boolean mIsOnTablet;
  private int mMode = 0;
  private View mRefreshActionView;
  private boolean mRefreshInProgress;
  private MenuItem mRefreshItem;
  private MenuItem mSearch;
  private SearchView mSearchWidget;
  private MenuItem mSendFeedbackItem;
  private final boolean mShowConversationSubject = getResources().getBoolean(2131623939);
  private MailSpinner mSpinner;
  private AccountSpinnerAdapter mSpinnerAdapter;
  private SnippetTextView mSubjectView;

  public MailActionBarView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MailActionBarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public MailActionBarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mIsOnTablet = Utils.useTabletUI(paramContext);
  }

  private final void enableDisableSpinnner()
  {
    if (!this.mIsOnTablet)
      return;
    int i;
    if ((ViewMode.isConversationMode(this.mMode)) && (this.mSpinnerAdapter.hasRecentFolders()))
    {
      i = 1;
      if ((!this.mHasManyAccounts) && (i == 0))
        break label57;
    }
    label57: for (boolean bool = true; ; bool = false)
    {
      this.mSpinner.changeEnabledState(bool);
      return;
      i = 0;
      break;
    }
  }

  private void onRefreshStarted()
  {
    setRefreshInProgress(true);
  }

  private void onRefreshStopped()
  {
    setRefreshInProgress(false);
  }

  private boolean setRefreshInProgress(boolean paramBoolean)
  {
    if (paramBoolean != this.mRefreshInProgress)
    {
      this.mRefreshInProgress = paramBoolean;
      if ((this.mSearch == null) || (!this.mSearch.isActionViewExpanded()))
        this.mHandler.post(this.mInvalidateMenu);
      return true;
    }
    return false;
  }

  private void setStandardMode()
  {
    this.mSpinner.setVisibility(8);
    this.mFolderView.setVisibility(0);
    this.mFolderAccountName.setVisibility(0);
  }

  private void showNavList()
  {
    this.mSpinner.setVisibility(0);
    this.mFolderView.setVisibility(8);
    this.mFolderAccountName.setVisibility(8);
  }

  public void attach()
  {
  }

  public void clearSubject()
  {
    if (!this.mShowConversationSubject)
      return;
    this.mSubjectView.setText(null);
  }

  public void collapseSearch()
  {
    if (this.mSearch != null)
      this.mSearch.collapseActionView();
  }

  protected int getMode()
  {
    return this.mMode;
  }

  public int getOptionsMenuId()
  {
    return new int[] { 2131820546, 2131820545, 2131820546, 2131820550, 2131820547, 2131820549, 2131820557 }[this.mMode];
  }

  public String getQuery()
  {
    if (this.mSearchWidget != null)
      return this.mSearchWidget.getQuery().toString();
    return "";
  }

  protected MenuItem getSearch()
  {
    return this.mSearch;
  }

  public String getUnshownSubject(String paramString)
  {
    if (!this.mShowConversationSubject)
      return paramString;
    return this.mSubjectView.getTextRemainder(paramString);
  }

  public void initialize(ControllableActivity paramControllableActivity, ActivityController paramActivityController, ViewMode paramViewMode, ActionBar paramActionBar, RecentFolderList paramRecentFolderList)
  {
    this.mActionBar = paramActionBar;
    this.mController = paramActivityController;
    this.mActivity = paramControllableActivity;
    this.mFolderObserver = new FolderObserver(null);
    this.mController.registerFolderObserver(this.mFolderObserver);
    if (!Utils.useTabletUI(getContext()));
    for (boolean bool = true; ; bool = false)
    {
      this.mSpinnerAdapter = new AccountSpinnerAdapter(paramControllableActivity, getContext(), bool);
      this.mSpinner = ((MailSpinner)findViewById(2131689506));
      this.mSpinner.setAdapter(this.mSpinnerAdapter);
      this.mSpinner.setController(this.mController);
      this.mAccount = this.mAccountObserver.initialize(paramControllableActivity.getAccountController());
      return;
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    if (this.mMode == 0)
      return false;
    this.mSearch = paramMenu.findItem(2131689763);
    if (this.mSearch != null)
    {
      this.mSearchWidget = ((SearchView)this.mSearch.getActionView());
      this.mSearch.setOnActionExpandListener(this);
      SearchManager localSearchManager = (SearchManager)this.mActivity.getActivityContext().getSystemService("search");
      if ((localSearchManager != null) && (this.mSearchWidget != null))
      {
        SearchableInfo localSearchableInfo = localSearchManager.getSearchableInfo(this.mActivity.getComponentName());
        this.mSearchWidget.setSearchableInfo(localSearchableInfo);
        this.mSearchWidget.setOnQueryTextListener(this);
        this.mSearchWidget.setOnSuggestionListener(this);
        this.mSearchWidget.setIconifiedByDefault(true);
      }
    }
    this.mHelpItem = paramMenu.findItem(2131689750);
    this.mSendFeedbackItem = paramMenu.findItem(2131689749);
    this.mRefreshItem = paramMenu.findItem(2131689764);
    this.mFolderSettingsItem = paramMenu.findItem(2131689765);
    return true;
  }

  public void onDestroy()
  {
    if (this.mFolderObserver != null)
    {
      this.mController.unregisterFolderObserver(this.mFolderObserver);
      this.mFolderObserver = null;
    }
    this.mSpinnerAdapter.destroy();
    this.mAccountObserver.unregisterAndDestroy();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mSubjectView = ((SnippetTextView)findViewById(2131689505));
    this.mFolderView = findViewById(2131689502);
    this.mFolderAccountName = ((TextView)this.mFolderView.findViewById(2131689504));
  }

  public void onFolderUpdated(Folder paramFolder)
  {
    this.mSpinner.onFolderUpdated(paramFolder);
    if (paramFolder.isSyncInProgress())
    {
      onRefreshStarted();
      return;
    }
    onRefreshStopped();
  }

  public boolean onMenuItemActionCollapse(MenuItem paramMenuItem)
  {
    setVisibility(0);
    return true;
  }

  public boolean onMenuItemActionExpand(MenuItem paramMenuItem)
  {
    return true;
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    boolean bool1 = true;
    LogUtils.d(LOG_TAG, "ActionBarView.onPrepareOptionsMenu().", new Object[0]);
    label63: boolean bool3;
    label99: boolean bool2;
    label145: MenuItem localMenuItem1;
    if (this.mRefreshInProgress)
      if (this.mRefreshItem != null)
      {
        if (this.mRefreshActionView == null)
        {
          this.mRefreshItem.setActionView(2130968582);
          this.mRefreshActionView = this.mRefreshItem.getActionView();
        }
      }
      else
      {
        if (this.mHelpItem != null)
        {
          MenuItem localMenuItem3 = this.mHelpItem;
          if ((this.mAccount == null) || (!this.mAccount.supportsCapability(32768)))
            break label276;
          bool3 = bool1;
          localMenuItem3.setVisible(bool3);
        }
        if (this.mSendFeedbackItem != null)
        {
          MenuItem localMenuItem2 = this.mSendFeedbackItem;
          if ((this.mAccount == null) || (!this.mAccount.supportsCapability(65536)))
            break label282;
          bool2 = bool1;
          localMenuItem2.setVisible(bool2);
        }
        if (this.mFolderSettingsItem != null)
        {
          localMenuItem1 = this.mFolderSettingsItem;
          if ((this.mFolder == null) || (!this.mFolder.supportsCapability(512)))
            break label288;
        }
      }
    while (true)
    {
      localMenuItem1.setVisible(bool1);
      switch (this.mMode)
      {
      case 3:
      case 4:
      default:
        return false;
        this.mRefreshItem.setActionView(this.mRefreshActionView);
        break label63;
        if (this.mRefreshItem == null)
          break label63;
        this.mRefreshItem.setActionView(null);
        break label63;
        label276: bool3 = false;
        break label99;
        label282: bool2 = false;
        break label145;
        label288: bool1 = false;
      case 1:
      case 5:
      case 2:
      }
    }
    setConversationModeOptions(paramMenu);
    return false;
    Utils.setMenuItemVisibility(paramMenu, 2131689763, this.mAccount.supportsCapability(64));
    return false;
  }

  public boolean onQueryTextChange(String paramString)
  {
    return false;
  }

  public boolean onQueryTextSubmit(String paramString)
  {
    if (this.mSearch != null)
    {
      this.mSearch.collapseActionView();
      this.mSearchWidget.setQuery("", false);
    }
    this.mActivity.onSearchRequested(paramString.trim());
    return true;
  }

  public boolean onSuggestionClick(int paramInt)
  {
    Cursor localCursor = this.mSearchWidget.getSuggestionsAdapter().getCursor();
    if ((localCursor != null) && (localCursor.moveToPosition(paramInt)));
    for (int i = 1; i == 0; i = 0)
    {
      LogUtils.d(LOG_TAG, "onSuggestionClick: Couldn't get a search query", new Object[0]);
      return true;
    }
    collapseSearch();
    String str1 = this.mSearchWidget.getQuery().toString();
    String str2 = localCursor.getString(localCursor.getColumnIndex("suggest_intent_query"));
    if ((!TextUtils.isEmpty(str1)) && (str2.indexOf(str1) != 0))
    {
      int j = str1.lastIndexOf(" ");
      if (j > -1)
        str1 = str1.substring(0, j);
      if ((j > -1) && (!TextUtils.isEmpty(str2)) && (str2.contains(str1)) && (str1.length() < str2.length()))
      {
        int k = str2.indexOf(str1);
        str2 = str2.substring(0, k) + str2.substring(k + str1.length());
      }
    }
    this.mController.onSearchRequested(str2.trim());
    return true;
  }

  public boolean onSuggestionSelect(int paramInt)
  {
    return onSuggestionClick(paramInt);
  }

  public void onViewModeChanged(int paramInt)
  {
    this.mMode = paramInt;
    enableDisableSpinnner();
    this.mActivity.invalidateOptionsMenu();
    int j;
    label92: View localView;
    if ((!this.mIsOnTablet) || (this.mMode == 1))
    {
      this.mSpinnerAdapter.enableRecentFolders();
      int i = this.mMode;
      j = 0;
      switch (i)
      {
      case 4:
      case 5:
      default:
        localView = this.mFolderView;
        if (j == 0)
          break;
      case 0:
      case 2:
      case 1:
      case 3:
      case 6:
      }
    }
    for (int k = 0; ; k = 8)
    {
      localView.setVisibility(k);
      return;
      this.mSpinnerAdapter.disableRecentFolders();
      break;
      MenuItem localMenuItem = this.mSearch;
      j = 0;
      if (localMenuItem == null)
        break label92;
      this.mSearch.collapseActionView();
      j = 0;
      break label92;
      showNavList();
      j = 0;
      break label92;
      this.mActionBar.setDisplayHomeAsUpEnabled(true);
      if (!this.mShowConversationSubject)
      {
        showNavList();
        j = 0;
        break label92;
      }
      setStandardMode();
      j = 0;
      break label92;
      this.mActionBar.setDisplayHomeAsUpEnabled(true);
      setStandardMode();
      j = 1;
      break label92;
      showNavList();
      j = 0;
      break label92;
    }
  }

  public void removeBackButton()
  {
    if (this.mActionBar == null)
      return;
    this.mActionBar.setDisplayOptions(2, 6);
    this.mActivity.getActionBar().setHomeButtonEnabled(false);
  }

  public void setAccounts(Account[] paramArrayOfAccount)
  {
    int i = 1;
    this.mSpinnerAdapter.setAccountArray(paramArrayOfAccount);
    if (paramArrayOfAccount.length > i);
    while (true)
    {
      this.mHasManyAccounts = i;
      enableDisableSpinnner();
      return;
      i = 0;
    }
  }

  public void setBackButton()
  {
    if (this.mActionBar == null)
      return;
    this.mActionBar.setDisplayOptions(6, 6);
    this.mActivity.getActionBar().setHomeButtonEnabled(true);
  }

  public void setConversationModeOptions(Menu paramMenu)
  {
    boolean bool1 = true;
    if (this.mCurrentConversation == null)
      return;
    boolean bool2;
    boolean bool3;
    label42: boolean bool4;
    label71: boolean bool5;
    label102: boolean bool6;
    label149: boolean bool7;
    label202: boolean bool8;
    label248: boolean bool9;
    label362: boolean bool10;
    label415: boolean bool11;
    if (!this.mCurrentConversation.isImportant())
    {
      bool2 = bool1;
      if ((!bool2) || (!this.mAccount.supportsCapability(131072)))
        break label533;
      bool3 = bool1;
      Utils.setMenuItemVisibility(paramMenu, 2131689757, bool3);
      if ((bool2) || (!this.mAccount.supportsCapability(131072)))
        break label539;
      bool4 = bool1;
      Utils.setMenuItemVisibility(paramMenu, 2131689758, bool4);
      if ((this.mFolder == null) || (!this.mFolder.supportsCapability(32)))
        break label545;
      bool5 = bool1;
      Utils.setMenuItemVisibility(paramMenu, 2131689753, bool5);
      if ((bool5) || (this.mFolder == null) || (!this.mFolder.isDraft()) || (!this.mAccount.supportsCapability(1048576)))
        break label551;
      bool6 = bool1;
      Utils.setMenuItemVisibility(paramMenu, 2131689754, bool6);
      if ((!this.mAccount.supportsCapability(8)) || (this.mFolder == null) || (!this.mFolder.supportsCapability(16)) || (this.mFolder.isTrash()))
        break label557;
      bool7 = bool1;
      Utils.setMenuItemVisibility(paramMenu, 2131689751, bool7);
      if ((bool7) || (this.mFolder == null) || (!this.mFolder.supportsCapability(8)) || (this.mFolder.isProviderFolder()))
        break label563;
      bool8 = bool1;
      Utils.setMenuItemVisibility(paramMenu, 2131689752, bool8);
      MenuItem localMenuItem = paramMenu.findItem(2131689752);
      if (localMenuItem != null)
      {
        Context localContext = this.mActivity.getApplicationContext();
        Object[] arrayOfObject = new Object[bool1];
        arrayOfObject[0] = this.mFolder.name;
        localMenuItem.setTitle(localContext.getString(2131427392, arrayOfObject));
      }
      if ((!this.mAccount.supportsCapability(2)) || (this.mFolder == null) || (!this.mFolder.supportsCapability(64)) || (this.mCurrentConversation.spam))
        break label569;
      bool9 = bool1;
      Utils.setMenuItemVisibility(paramMenu, 2131689760, bool9);
      if ((!this.mAccount.supportsCapability(2)) || (this.mFolder == null) || (!this.mFolder.supportsCapability(128)) || (!this.mCurrentConversation.spam))
        break label575;
      bool10 = bool1;
      Utils.setMenuItemVisibility(paramMenu, 2131689761, bool10);
      if ((!this.mAccount.supportsCapability(4)) || (this.mFolder == null) || (!this.mFolder.supportsCapability(8192)) || (this.mCurrentConversation.phishing))
        break label581;
      bool11 = bool1;
      label468: Utils.setMenuItemVisibility(paramMenu, 2131689762, bool11);
      if ((!this.mAccount.supportsCapability(16)) || (this.mFolder == null) || (!this.mFolder.supportsCapability(256)) || (this.mCurrentConversation.muted))
        break label587;
    }
    while (true)
    {
      Utils.setMenuItemVisibility(paramMenu, 2131689759, bool1);
      return;
      bool2 = false;
      break;
      label533: bool3 = false;
      break label42;
      label539: bool4 = false;
      break label71;
      label545: bool5 = false;
      break label102;
      label551: bool6 = false;
      break label149;
      label557: bool7 = false;
      break label202;
      label563: bool8 = false;
      break label248;
      label569: bool9 = false;
      break label362;
      label575: bool10 = false;
      break label415;
      label581: bool11 = false;
      break label468;
      label587: bool1 = false;
    }
  }

  public void setCurrentConversation(Conversation paramConversation)
  {
    this.mCurrentConversation = paramConversation;
  }

  protected void setEmptyMode()
  {
    this.mSpinner.setVisibility(8);
    this.mFolderView.setVisibility(8);
    this.mFolderAccountName.setVisibility(8);
  }

  public void setFolder(Folder paramFolder)
  {
    setRefreshInProgress(false);
    this.mFolder = paramFolder;
    this.mSpinner.setFolder(paramFolder);
    this.mActivity.invalidateOptionsMenu();
  }

  public void setSubject(String paramString)
  {
    if (!this.mShowConversationSubject)
      return;
    this.mSubjectView.setText(paramString);
  }

  protected boolean showConversationSubject()
  {
    return ((this.mMode == 5) || (this.mMode == 1)) && (this.mShowConversationSubject);
  }

  private class FolderObserver extends DataSetObserver
  {
    private FolderObserver()
    {
    }

    public void onChanged()
    {
      MailActionBarView.this.onFolderUpdated(MailActionBarView.this.mController.getFolder());
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.MailActionBarView
 * JD-Core Version:    0.6.2
 */