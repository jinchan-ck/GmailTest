package com.android.mail.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import com.android.mail.providers.Account;
import com.android.mail.providers.MailAppProvider;
import com.android.mail.providers.UIProvider;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AccountUtils
{
  public static Account[] getAccounts(Context paramContext)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Cursor localCursor = null;
    ArrayList localArrayList = Lists.newArrayList();
    try
    {
      localCursor = localContentResolver.query(MailAppProvider.getAccountsUri(), UIProvider.ACCOUNTS_PROJECTION, null, null, null);
      if (localCursor != null)
        while (localCursor.moveToNext())
          localArrayList.add(new Account(localCursor));
    }
    finally
    {
      if (localCursor != null)
        localCursor.close();
    }
    if (localCursor != null)
      localCursor.close();
    return (Account[])localArrayList.toArray(new Account[localArrayList.size()]);
  }

  public static Account[] getSyncingAccounts(Context paramContext)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Cursor localCursor = null;
    ArrayList localArrayList = Lists.newArrayList();
    try
    {
      localCursor = localContentResolver.query(MailAppProvider.getAccountsUri(), UIProvider.ACCOUNTS_PROJECTION, null, null, null);
      if (localCursor != null)
        while (localCursor.moveToNext())
        {
          Account localAccount = new Account(localCursor);
          if (!localAccount.isAccountSyncRequired())
            localArrayList.add(localAccount);
        }
    }
    finally
    {
      if (localCursor != null)
        localCursor.close();
    }
    if (localCursor != null)
      localCursor.close();
    return (Account[])localArrayList.toArray(new Account[localArrayList.size()]);
  }

  public static List<Account> mergeAccountLists(List<Account> paramList, Account[] paramArrayOfAccount, boolean paramBoolean)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    if (paramList != null)
    {
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
        localArrayList2.add(((Account)localIterator.next()).name);
    }
    for (int i = 0; i < paramArrayOfAccount.length; i++)
    {
      String str = paramArrayOfAccount[i].name;
      if ((paramBoolean) || ((localArrayList2 != null) && (localArrayList2.contains(str))))
        localArrayList1.add(paramArrayOfAccount[i]);
    }
    return localArrayList1;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.AccountUtils
 * JD-Core Version:    0.6.2
 */