package com.android.mail.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.android.mail.providers.Account;

public class WaitFragment extends Fragment
  implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
  private Account mAccount;
  private ViewGroup mContainer;
  private boolean mDefault;
  private LayoutInflater mInflater;

  private View getContent()
  {
    this.mContainer.removeAllViews();
    if ((this.mAccount != null) && ((0x10 & this.mAccount.syncStatus) == 16))
    {
      View localView = this.mInflater.inflate(2130968681, this.mContainer, false);
      localView.findViewById(2131689716).setOnClickListener(this);
      localView.findViewById(2131689717).setOnClickListener(this);
      return localView;
    }
    if (this.mDefault)
      return this.mInflater.inflate(2130968680, this.mContainer, false);
    return this.mInflater.inflate(2130968682, this.mContainer, false);
  }

  public static WaitFragment newInstance(Account paramAccount)
  {
    return newInstance(paramAccount, false);
  }

  public static WaitFragment newInstance(Account paramAccount, boolean paramBoolean)
  {
    WaitFragment localWaitFragment = new WaitFragment();
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("account", paramAccount);
    localBundle.putBoolean("isDefault", paramBoolean);
    localWaitFragment.setArguments(localBundle);
    return localWaitFragment;
  }

  Account getAccount()
  {
    return this.mAccount;
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
    case 2131689717:
    case 2131689716:
    }
    do
    {
      return;
      Intent localIntent = new Intent("android.settings.SYNC_SETTINGS");
      localIntent.addFlags(268435456);
      startActivity(localIntent);
      return;
    }
    while ((this.mAccount == null) || (this.mAccount.manualSyncUri == null));
    getLoaderManager().initLoader(0, null, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Bundle localBundle = getArguments();
    this.mAccount = ((Account)localBundle.getParcelable("account"));
    this.mDefault = localBundle.getBoolean("isDefault", false);
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    return new CursorLoader(getActivity(), this.mAccount.manualSyncUri, null, null, null, null);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mInflater = paramLayoutInflater;
    this.mContainer = paramViewGroup;
    return getContent();
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
  }

  public void updateAccount(Account paramAccount)
  {
    this.mAccount = paramAccount;
    this.mContainer.addView(getContent());
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.WaitFragment
 * JD-Core Version:    0.6.2
 */