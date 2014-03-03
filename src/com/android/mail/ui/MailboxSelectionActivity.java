package com.android.mail.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.android.mail.providers.Account;
import com.android.mail.providers.MailAppProvider;
import com.android.mail.providers.UIProvider;
import com.android.mail.utils.LogTag;
import java.util.ArrayList;

public class MailboxSelectionActivity extends ListActivity
  implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
  private static final String[] COLUMN_NAMES = { "name" };
  protected static final String LOG_TAG = LogTag.getLogTag();
  private final int[] VIEW_IDS = { 2131689661 };
  private SimpleCursorAdapter mAdapter;
  private int mAppWidgetId = 0;
  private boolean mConfigureWidget = false;
  private View mContent;
  private boolean mCreateShortcut = false;
  private Handler mHandler = new Handler();
  private boolean mResumed = false;
  private View mWait;
  boolean mWaitingForAddAccountResult = false;

  private void completeSetupWithAccounts(final Cursor paramCursor)
  {
    this.mHandler.post(new Runnable()
    {
      public void run()
      {
        MailboxSelectionActivity.this.updateAccountList(paramCursor);
      }
    });
  }

  private WaitFragment getWaitFragment()
  {
    return (WaitFragment)getFragmentManager().findFragmentByTag("wait-fragment");
  }

  private int replaceFragment(Fragment paramFragment, int paramInt, String paramString)
  {
    FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.setTransition(paramInt);
    localFragmentTransaction.replace(2131689530, paramFragment, paramString);
    return localFragmentTransaction.commitAllowingStateLoss();
  }

  private void restoreState(Bundle paramBundle)
  {
    if (paramBundle.containsKey("createShortcut"))
      this.mCreateShortcut = paramBundle.getBoolean("createShortcut");
    if (paramBundle.containsKey("createWidget"))
      this.mConfigureWidget = paramBundle.getBoolean("createWidget");
    if (paramBundle.containsKey("widgetId"))
      this.mAppWidgetId = paramBundle.getInt("widgetId");
    if (paramBundle.containsKey("waitingForAddAccountResult"))
      this.mWaitingForAddAccountResult = paramBundle.getBoolean("waitingForAddAccountResult");
  }

  private void selectAccount(Account paramAccount)
  {
    if ((this.mCreateShortcut) || (this.mConfigureWidget))
    {
      Intent localIntent = new Intent(this, getFolderSelectionActivity());
      localIntent.setFlags(1107296256);
      if (this.mCreateShortcut);
      for (String str = "android.intent.action.CREATE_SHORTCUT"; ; str = "android.appwidget.action.APPWIDGET_CONFIGURE")
      {
        localIntent.setAction(str);
        if (this.mConfigureWidget)
          localIntent.putExtra("appWidgetId", this.mAppWidgetId);
        localIntent.putExtra("account-shortcut", paramAccount);
        startActivity(localIntent);
        finish();
        return;
      }
    }
    finish();
  }

  private void setupWithAccounts()
  {
    new AsyncTask()
    {
      protected Void doInBackground(Void[] paramAnonymousArrayOfVoid)
      {
        Cursor localCursor = null;
        try
        {
          localCursor = this.val$resolver.query(MailAppProvider.getAccountsUri(), UIProvider.ACCOUNTS_PROJECTION, null, null, null);
          MailboxSelectionActivity.this.completeSetupWithAccounts(localCursor);
          return null;
        }
        finally
        {
          if (localCursor != null)
            localCursor.close();
        }
      }
    }
    .execute(new Void[0]);
  }

  private void showWaitFragment(Account paramAccount)
  {
    WaitFragment localWaitFragment = getWaitFragment();
    if (localWaitFragment != null)
      localWaitFragment.updateAccount(paramAccount);
    while (true)
    {
      this.mContent.setVisibility(8);
      return;
      this.mWait.setVisibility(0);
      replaceFragment(WaitFragment.newInstance(paramAccount, true), 4097, "wait-fragment");
    }
  }

  private void updateAccountList(Cursor paramCursor)
  {
    int i = 1;
    if ((this.mConfigureWidget) || (this.mCreateShortcut))
    {
      if ((paramCursor != null) && (paramCursor.getCount() != 0))
        break label108;
      Intent localIntent = MailAppProvider.getNoAccountIntent(this);
      if (localIntent != null)
        startActivityForResult(localIntent, 2);
      i = 0;
      this.mWaitingForAddAccountResult = true;
    }
    while (true)
    {
      if (i != 0)
      {
        this.mContent.setVisibility(0);
        if (this.mResumed)
          setVisible(true);
        this.mAdapter = new SimpleCursorAdapter(this, 2130968654, paramCursor, COLUMN_NAMES, this.VIEW_IDS, 0)
        {
          public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
          {
            View localView = super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup);
            ((TextView)localView.findViewById(2131689661)).setText(new Account((Cursor)getItem(paramAnonymousInt)).name);
            return localView;
          }
        };
        setListAdapter(this.mAdapter);
      }
      return;
      label108: if ((this.mConfigureWidget) && (paramCursor.getCount() == 1))
      {
        this.mWait.setVisibility(8);
        paramCursor.moveToFirst();
        selectAccount(new Account(paramCursor));
        i = 0;
      }
    }
  }

  protected Class<?> getFolderSelectionActivity()
  {
    return FolderSelectionActivity.class;
  }

  protected final void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 2)
    {
      if (paramInt2 != -1)
        finish();
    }
    else
      return;
    getLoaderManager().initLoader(0, null, this);
    showWaitFragment(null);
  }

  public void onBackPressed()
  {
    this.mWaitingForAddAccountResult = false;
    if (getWaitFragment() != null)
    {
      finish();
      return;
    }
    super.onBackPressed();
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
      return;
    case 2131689665:
    }
    setResult(0);
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968655);
    this.mContent = findViewById(2131689476);
    this.mWait = findViewById(2131689530);
    if (paramBundle != null)
      restoreState(paramBundle);
    while (true)
    {
      if ((this.mCreateShortcut) || (this.mConfigureWidget))
      {
        setTitle(getResources().getString(2131427551));
        ActionBar localActionBar = getActionBar();
        if (localActionBar != null)
          localActionBar.setIcon(2130903041);
      }
      ((Button)findViewById(2131689665)).setOnClickListener(this);
      setVisible(false);
      setResult(0);
      return;
      if ("android.intent.action.CREATE_SHORTCUT".equals(getIntent().getAction()))
        this.mCreateShortcut = true;
      this.mAppWidgetId = getIntent().getIntExtra("appWidgetId", 0);
      if (this.mAppWidgetId != 0)
        this.mConfigureWidget = true;
    }
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    switch (paramInt)
    {
    default:
      return null;
    case 0:
    }
    return new CursorLoader(this, MailAppProvider.getAccountsUri(), UIProvider.ACCOUNTS_PROJECTION, null, null, null);
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    selectAccount(new Account((Cursor)this.mAdapter.getItem(paramInt)));
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    ArrayList localArrayList1;
    if ((paramCursor != null) && (paramCursor.moveToFirst()))
    {
      localArrayList1 = new ArrayList();
      ArrayList localArrayList2 = new ArrayList();
      do
      {
        Account localAccount1 = new Account(paramCursor);
        if (localAccount1.isAccountReady())
          localArrayList2.add(localAccount1);
        localArrayList1.add(localAccount1);
      }
      while (paramCursor.moveToNext());
      if (localArrayList2.size() > 0)
      {
        this.mWait.setVisibility(8);
        getLoaderManager().destroyLoader(0);
        this.mContent.setVisibility(0);
        updateAccountList(paramCursor);
      }
    }
    else
    {
      return;
    }
    if (localArrayList1.size() > 0);
    for (Account localAccount2 = (Account)localArrayList1.get(0); ; localAccount2 = null)
    {
      showWaitFragment(localAccount2);
      return;
    }
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
  }

  public void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
  }

  public void onPause()
  {
    super.onPause();
    this.mResumed = false;
  }

  public void onResume()
  {
    super.onResume();
    this.mResumed = true;
    if (!this.mWaitingForAddAccountResult)
      setupWithAccounts();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("createShortcut", this.mCreateShortcut);
    paramBundle.putBoolean("createWidget", this.mConfigureWidget);
    if (this.mAppWidgetId != 0)
      paramBundle.putInt("widgetId", this.mAppWidgetId);
    paramBundle.putBoolean("waitingForAddAccountResult", this.mWaitingForAddAccountResult);
  }

  public void onStart()
  {
    super.onStart();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.MailboxSelectionActivity
 * JD-Core Version:    0.6.2
 */