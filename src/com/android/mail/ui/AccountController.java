package com.android.mail.ui;

import android.database.DataSetObserver;
import com.android.mail.providers.Account;

public abstract interface AccountController
{
  public abstract Account getAccount();

  public abstract void registerAccountObserver(DataSetObserver paramDataSetObserver);

  public abstract void unregisterAccountObserver(DataSetObserver paramDataSetObserver);
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.AccountController
 * JD-Core Version:    0.6.2
 */