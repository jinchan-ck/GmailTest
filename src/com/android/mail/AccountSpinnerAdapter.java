package com.android.mail;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.mail.providers.Account;
import com.android.mail.providers.AccountObserver;
import com.android.mail.providers.Folder;
import com.android.mail.providers.FolderWatcher;
import com.android.mail.providers.RecentFolderObserver;
import com.android.mail.providers.Settings;
import com.android.mail.ui.ControllableActivity;
import com.android.mail.ui.RecentFolderController;
import com.android.mail.ui.RecentFolderList;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.util.ArrayList;
import java.util.Vector;

public class AccountSpinnerAdapter extends BaseAdapter
{
  private static final String LOG_TAG = LogTag.getLogTag();
  final AccountObserver mAccountObserver = new AccountObserver()
  {
    public void onChanged(Account paramAnonymousAccount)
    {
      if (paramAnonymousAccount == null);
      while (paramAnonymousAccount.uri.equals(AccountSpinnerAdapter.this.getCurrentAccountUri()))
        return;
      AccountSpinnerAdapter.access$202(AccountSpinnerAdapter.this, paramAnonymousAccount);
      if (AccountSpinnerAdapter.this.mRecentFoldersVisible)
      {
        int i = Account.findPosition(AccountSpinnerAdapter.this.mAllAccounts, paramAnonymousAccount.uri);
        String str = AccountSpinnerAdapter.LOG_TAG;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(i);
        LogUtils.d(str, "setCurrentAccount: mCurrentAccountPos = %d", arrayOfObject);
        if (i >= 0)
          AccountSpinnerAdapter.this.requestRecentFolders();
      }
      AccountSpinnerAdapter.this.notifyDataSetChanged();
    }
  };
  private final Vector<View> mAccountViews = new Vector();
  private Account[] mAllAccounts = new Account[0];
  private final Context mContext;
  private Account mCurrentAccount = null;
  private Folder mCurrentFolder;
  private final Vector<View> mFolderViews = new Vector();
  private final FolderWatcher mFolderWatcher;
  private View mFooterView = null;
  private View mHeaderView = null;
  private final LayoutInflater mInflater;
  private int mNumAccounts = 0;
  private RecentFolderController mRecentFolderController;
  private ArrayList<Folder> mRecentFolderList = new ArrayList();
  private RecentFolderObserver mRecentFolderObserver;
  private RecentFolderList mRecentFolders;
  private boolean mRecentFoldersVisible;
  private final boolean mShowAllFoldersItem;
  private RecentFolderObserver mSpinnerRecentFolderObserver = new RecentFolderObserver()
  {
    public void onChanged()
    {
      AccountSpinnerAdapter.this.requestRecentFolders();
    }
  };

  public AccountSpinnerAdapter(ControllableActivity paramControllableActivity, Context paramContext, boolean paramBoolean)
  {
    this.mContext = paramContext;
    this.mInflater = LayoutInflater.from(paramContext);
    this.mShowAllFoldersItem = paramBoolean;
    this.mFolderWatcher = new FolderWatcher(paramControllableActivity, this);
    this.mCurrentAccount = this.mAccountObserver.initialize(paramControllableActivity.getAccountController());
    this.mRecentFolderController = paramControllableActivity.getRecentFolderController();
  }

  private Account getAccount(int paramInt)
  {
    if (paramInt >= this.mNumAccounts)
      return null;
    return this.mAllAccounts[paramInt];
  }

  private String getCurrentAccountName()
  {
    if (isCurrentAccountInvalid())
      return "";
    return this.mCurrentAccount.name;
  }

  private Uri getCurrentAccountUri()
  {
    if (isCurrentAccountInvalid())
      return Uri.EMPTY;
    return this.mCurrentAccount.uri;
  }

  private final int getRecentOffset(int paramInt)
  {
    return -1 + (paramInt - this.mNumAccounts);
  }

  private boolean isCurrentAccountInvalid()
  {
    return this.mCurrentAccount == null;
  }

  private void requestRecentFolders()
  {
    if (this.mCurrentFolder == null);
    for (Uri localUri = null; this.mRecentFoldersVisible; localUri = this.mCurrentFolder.uri)
    {
      this.mRecentFolderList = this.mRecentFolders.getRecentFolderList(localUri);
      this.mFolderViews.setSize(this.mRecentFolderList.size());
      notifyDataSetChanged();
      return;
    }
    this.mRecentFolderList = null;
  }

  private static void setText(View paramView, int paramInt, String paramString)
  {
    TextView localTextView = (TextView)paramView.findViewById(paramInt);
    if (localTextView == null)
      return;
    localTextView.setText(paramString);
  }

  private final void setUnreadCount(View paramView, int paramInt1, int paramInt2)
  {
    TextView localTextView = (TextView)paramView.findViewById(paramInt1);
    if (localTextView == null)
      return;
    if (paramInt2 <= 0)
    {
      localTextView.setVisibility(8);
      return;
    }
    localTextView.setVisibility(0);
    localTextView.setText(Utils.getUnreadCountString(this.mContext, paramInt2));
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public void destroy()
  {
    this.mAccountObserver.unregisterAndDestroy();
    if (this.mRecentFolderObserver != null)
    {
      this.mRecentFolderObserver.unregisterAndDestroy();
      this.mRecentFolderObserver = null;
    }
  }

  public void disableRecentFolders()
  {
    if (this.mRecentFoldersVisible)
    {
      if (this.mRecentFolderObserver != null)
      {
        this.mRecentFolderObserver.unregisterAndDestroy();
        this.mRecentFolderObserver = null;
      }
      this.mRecentFolders = null;
      notifyDataSetChanged();
      this.mRecentFoldersVisible = false;
    }
  }

  public void enableRecentFolders()
  {
    if (!this.mRecentFoldersVisible)
    {
      this.mRecentFolderObserver = this.mSpinnerRecentFolderObserver;
      this.mRecentFolders = this.mRecentFolderObserver.initialize(this.mRecentFolderController);
      this.mRecentFoldersVisible = true;
      if ((this.mRecentFolderList == null) || (this.mRecentFolderList.size() <= 0))
        requestRecentFolders();
    }
    else
    {
      return;
    }
    notifyDataSetChanged();
  }

  final View getCachedView(int paramInt1, ViewGroup paramViewGroup, int paramInt2)
  {
    View localView3;
    switch (paramInt2)
    {
    case -1:
    default:
      localView3 = null;
    case 0:
      do
      {
        return localView3;
        localView3 = (View)this.mAccountViews.get(paramInt1);
      }
      while (localView3 != null);
      View localView4 = this.mInflater.inflate(2130968577, paramViewGroup, false);
      this.mAccountViews.set(paramInt1, localView4);
      return localView4;
    case -2:
      if (this.mHeaderView == null)
        this.mHeaderView = this.mInflater.inflate(2130968580, paramViewGroup, false);
      return this.mHeaderView;
    case 1:
      int i = getRecentOffset(paramInt1);
      View localView1 = (View)this.mFolderViews.get(i);
      if (localView1 != null)
        return localView1;
      View localView2 = this.mInflater.inflate(2130968578, paramViewGroup, false);
      this.mFolderViews.set(i, localView2);
      return localView2;
    case 2:
    }
    if (this.mFooterView == null)
      this.mFooterView = this.mInflater.inflate(2130968579, paramViewGroup, false);
    return this.mFooterView;
  }

  public int getCount()
  {
    int i;
    int k;
    int m;
    if (this.mRecentFolderList == null)
    {
      i = 0;
      if ((!this.mRecentFoldersVisible) || (i <= 0))
        break label64;
      k = i + 1;
      boolean bool = this.mShowAllFoldersItem;
      m = 0;
      if (bool)
        m = 1;
    }
    label64: for (int j = k + m; ; j = 0)
    {
      return j + this.mNumAccounts;
      i = this.mRecentFolderList.size();
      break;
    }
  }

  public Object getItem(int paramInt)
  {
    switch (getItemViewType(paramInt))
    {
    case -1:
    case 1:
    default:
      return this.mRecentFolderList.get(getRecentOffset(paramInt));
    case 0:
      return getAccount(paramInt);
    case -2:
      return "account spinner header";
    case 2:
    }
    return "show all folders";
  }

  public long getItemId(int paramInt)
  {
    int i = getItemViewType(paramInt);
    switch (i)
    {
    case -1:
    case 1:
    default:
      return ((Folder)this.mRecentFolderList.get(getRecentOffset(paramInt))).uri.hashCode();
    case -2:
    case 2:
      return i;
    case 0:
    }
    return getAccount(paramInt).uri.hashCode();
  }

  public int getItemViewType(int paramInt)
  {
    if (paramInt < this.mNumAccounts)
      return 0;
    if (paramInt == this.mNumAccounts)
      return -2;
    if ((this.mShowAllFoldersItem) && (getRecentOffset(paramInt) >= this.mRecentFolderList.size()))
      return 2;
    return 1;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = getItemViewType(paramInt);
    View localView1 = getCachedView(paramInt, paramViewGroup, i);
    switch (i)
    {
    case -1:
    default:
      String str2 = LOG_TAG;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Integer.valueOf(i);
      LogUtils.e(str2, "AccountSpinnerAdapter.getView(): Unknown type: %d", arrayOfObject2);
    case 2:
      return localView1;
    case 0:
      Account localAccount = getAccount(paramInt);
      if (localAccount == null)
      {
        String str1 = LOG_TAG;
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Integer.valueOf(paramInt);
        LogUtils.e(str1, "AccountSpinnerAdapter(%d): Null account at position.", arrayOfObject1);
        return null;
      }
      int j = localAccount.color;
      View localView2 = localView1.findViewById(2131689489);
      Folder localFolder2;
      if (j != 0)
      {
        localView2.setVisibility(0);
        localView2.setBackgroundColor(j);
        localFolder2 = this.mFolderWatcher.get(localAccount.settings.defaultInbox);
        if (localFolder2 == null)
          break label247;
      }
      for (int k = localFolder2.unreadCount; ; k = 0)
      {
        setText(localView1, 2131689487, localAccount.settings.defaultInboxName);
        setText(localView1, 2131689488, localAccount.name);
        setUnreadCount(localView1, 2131689490, k);
        return localView1;
        localView2.setVisibility(8);
        break;
      }
    case -2:
      label247: setText(localView1, 2131689491, getCurrentAccountName());
      return localView1;
    case 1:
    }
    Folder localFolder1 = (Folder)this.mRecentFolderList.get(getRecentOffset(paramInt));
    Folder.setFolderBlockColor(localFolder1, localView1.findViewById(2131689489));
    setText(localView1, 2131689487, localFolder1.name);
    setUnreadCount(localView1, 2131689490, localFolder1.unreadCount);
    return localView1;
  }

  public int getViewTypeCount()
  {
    return 4;
  }

  public final boolean hasRecentFolders()
  {
    return (this.mRecentFolderList != null) && (this.mRecentFolderList.size() > 0);
  }

  public boolean hasStableIds()
  {
    return true;
  }

  public boolean isEmpty()
  {
    return false;
  }

  public boolean isEnabled(int paramInt)
  {
    return getItemViewType(paramInt) != -2;
  }

  public void onFolderUpdated(Folder paramFolder)
  {
    this.mCurrentFolder = paramFolder;
    notifyDataSetChanged();
  }

  public void setAccountArray(Account[] paramArrayOfAccount)
  {
    Uri localUri1 = getCurrentAccountUri();
    this.mAllAccounts = paramArrayOfAccount;
    this.mNumAccounts = paramArrayOfAccount.length;
    this.mAccountViews.setSize(this.mNumAccounts);
    if (!isCurrentAccountInvalid())
    {
      int j = Account.findPosition(paramArrayOfAccount, localUri1);
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(j);
      LogUtils.d(str, "setAccountArray: mCurrentAccountPos = %d", arrayOfObject);
    }
    for (int i = 0; i < this.mNumAccounts; i++)
    {
      Uri localUri2 = this.mAllAccounts[i].settings.defaultInbox;
      this.mFolderWatcher.startWatching(localUri2);
    }
    notifyDataSetChanged();
  }

  public boolean setCurrentFolder(Folder paramFolder)
  {
    if ((paramFolder != null) && (paramFolder != this.mCurrentFolder))
    {
      this.mCurrentFolder = paramFolder;
      requestRecentFolders();
      return true;
    }
    return false;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.AccountSpinnerAdapter
 * JD-Core Version:    0.6.2
 */