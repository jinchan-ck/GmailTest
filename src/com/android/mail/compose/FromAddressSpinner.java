package com.android.mail.compose;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import com.android.mail.providers.Account;
import com.android.mail.providers.ReplyFromAccount;
import com.android.mail.utils.AccountUtils;
import com.android.mail.utils.LogTag;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class FromAddressSpinner extends Spinner
  implements AdapterView.OnItemSelectedListener
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private ReplyFromAccount mAccount;
  private OnAccountChangedListener mAccountChangedListener;
  private List<Account> mAccounts;
  private final List<ReplyFromAccount> mReplyFromAccounts = Lists.newArrayList();

  public FromAddressSpinner(Context paramContext)
  {
    this(paramContext, null);
  }

  public FromAddressSpinner(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void selectCurrentAccount()
  {
    if (this.mAccount == null);
    while (true)
    {
      return;
      int i = 0;
      Iterator localIterator = this.mReplyFromAccounts.iterator();
      while (localIterator.hasNext())
      {
        ReplyFromAccount localReplyFromAccount = (ReplyFromAccount)localIterator.next();
        if ((TextUtils.equals(this.mAccount.name, localReplyFromAccount.name)) && (TextUtils.equals(this.mAccount.address, localReplyFromAccount.address)))
        {
          setSelection(i, true);
          return;
        }
        i++;
      }
    }
  }

  public void asyncInitFromSpinner(int paramInt, Account paramAccount, Account[] paramArrayOfAccount)
  {
    if (paramInt == -1);
    for (this.mAccounts = AccountUtils.mergeAccountLists(this.mAccounts, paramArrayOfAccount, true); ; this.mAccounts = ImmutableList.of(paramAccount))
    {
      initFromSpinner();
      return;
    }
  }

  public ReplyFromAccount getCurrentAccount()
  {
    return this.mAccount;
  }

  public ReplyFromAccount getMatchingReplyFromAccount(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      Iterator localIterator = this.mReplyFromAccounts.iterator();
      while (localIterator.hasNext())
      {
        ReplyFromAccount localReplyFromAccount = (ReplyFromAccount)localIterator.next();
        if (paramString.equals(localReplyFromAccount.name))
          return localReplyFromAccount;
      }
    }
    return null;
  }

  public List<ReplyFromAccount> getReplyFromAccounts()
  {
    return this.mReplyFromAccounts;
  }

  protected void initFromSpinner()
  {
    if ((this.mAccounts == null) || (this.mAccounts.size() == 0))
      return;
    FromAddressSpinnerAdapter localFromAddressSpinnerAdapter = new FromAddressSpinnerAdapter(getContext());
    this.mReplyFromAccounts.clear();
    Iterator localIterator = this.mAccounts.iterator();
    while (localIterator.hasNext())
    {
      Account localAccount = (Account)localIterator.next();
      this.mReplyFromAccounts.addAll(localAccount.getReplyFroms());
    }
    localFromAddressSpinnerAdapter.addAccounts(this.mReplyFromAccounts);
    setAdapter(localFromAddressSpinnerAdapter);
    selectCurrentAccount();
    setOnItemSelectedListener(this);
  }

  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    ReplyFromAccount localReplyFromAccount = (ReplyFromAccount)getItemAtPosition(paramInt);
    if (!localReplyFromAccount.name.equals(this.mAccount.name))
    {
      this.mAccount = localReplyFromAccount;
      this.mAccountChangedListener.onAccountChanged();
    }
  }

  public void onNothingSelected(AdapterView<?> paramAdapterView)
  {
  }

  public void setCurrentAccount(ReplyFromAccount paramReplyFromAccount)
  {
    this.mAccount = paramReplyFromAccount;
    selectCurrentAccount();
  }

  public void setOnAccountChangedListener(OnAccountChangedListener paramOnAccountChangedListener)
  {
    this.mAccountChangedListener = paramOnAccountChangedListener;
  }

  public static abstract interface OnAccountChangedListener
  {
    public abstract void onAccountChanged();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.FromAddressSpinner
 * JD-Core Version:    0.6.2
 */