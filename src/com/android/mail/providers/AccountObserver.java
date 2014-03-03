package com.android.mail.providers;

import android.database.DataSetObserver;
import com.android.mail.ui.AccountController;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;

public abstract class AccountObserver extends DataSetObserver
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private AccountController mController;

  public Account initialize(AccountController paramAccountController)
  {
    if (paramAccountController == null)
      LogUtils.wtf(LOG_TAG, "AccountObserver initialized with null controller!", new Object[0]);
    this.mController = paramAccountController;
    this.mController.registerAccountObserver(this);
    return this.mController.getAccount();
  }

  public final void onChanged()
  {
    if (this.mController == null)
      return;
    onChanged(this.mController.getAccount());
  }

  public abstract void onChanged(Account paramAccount);

  public void unregisterAndDestroy()
  {
    if (this.mController == null)
      return;
    this.mController.unregisterAccountObserver(this);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.AccountObserver
 * JD-Core Version:    0.6.2
 */