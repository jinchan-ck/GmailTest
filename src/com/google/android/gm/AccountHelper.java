package com.google.android.gm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.LogUtils;
import com.google.android.gm.provider.MailEngine;
import com.google.android.gsf.GoogleLoginServiceConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountHelper
{
  private final Context mContext;

  public AccountHelper(Context paramContext)
  {
    this.mContext = paramContext;
  }

  public AccountHelper(RestrictedActivity paramRestrictedActivity)
  {
    this.mContext = paramRestrictedActivity.getContext();
  }

  public static void getSyncingAccounts(Context paramContext, AccountManagerCallback<Account[]> paramAccountManagerCallback)
  {
    AccountManager localAccountManager = AccountManager.get(paramContext);
    String[] arrayOfString = new String[1];
    arrayOfString[0] = GoogleLoginServiceConstants.featureForService("mail");
    localAccountManager.getAccountsByTypeAndFeatures("com.google", arrayOfString, paramAccountManagerCallback, null);
  }

  public static List<String> mergeAccountLists(List<String> paramList, Account[] paramArrayOfAccount, boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if (i < paramArrayOfAccount.length)
    {
      String str = paramArrayOfAccount[i].name;
      MailEngine localMailEngine = MailEngine.getMailEngine(str);
      if (((localMailEngine != null) && (localMailEngine.labelsSynced())) || (paramBoolean))
        localArrayList.add(str);
      while (true)
      {
        i++;
        break;
        if ((paramBoolean) || ((paramList != null) && (paramList.contains(str))))
          localArrayList.add(str);
      }
    }
    return localArrayList;
  }

  public static void promptForCredentials(Activity paramActivity, String paramString, CredentialsCallback paramCredentialsCallback)
  {
    Account localAccount = new Account(paramString, "com.google");
    Bundle localBundle = new Bundle();
    AccountManager.get(paramActivity).getAuthToken(localAccount, "mail", localBundle, paramActivity, new AccountManagerCallback()
    {
      public void run(AccountManagerFuture<Bundle> paramAnonymousAccountManagerFuture)
      {
        try
        {
          ((Bundle)paramAnonymousAccountManagerFuture.getResult());
          bool = true;
          this.val$callback.onCredentialsResult(bool);
          return;
        }
        catch (OperationCanceledException localOperationCanceledException)
        {
          while (true)
          {
            LogUtils.v("Gmail", "promptForCredentials(): User Canceled", new Object[0]);
            bool = false;
          }
        }
        catch (IOException localIOException)
        {
          while (true)
          {
            LogUtils.v("Gmail", "promptForCredentials(): IO Error", new Object[0]);
            bool = false;
          }
        }
        catch (AuthenticatorException localAuthenticatorException)
        {
          while (true)
          {
            LogUtils.v("Gmail", "promptForCredentials(): Not Authenticated", new Object[0]);
            boolean bool = false;
          }
        }
      }
    }
    , null);
  }

  public static void showAddAccount(Activity paramActivity, AddAccountCallback paramAddAccountCallback)
  {
    final PendingIntent localPendingIntent = PendingIntent.getActivity(paramActivity, -1, new Intent("android.intent.action.VIEW"), 0);
    boolean bool = Gmail.isRunningICSOrLater();
    Bundle localBundle = null;
    if (bool)
    {
      localBundle = new Bundle();
      localBundle.putBoolean("allowSkip", false);
      localBundle.putString("introMessage", paramActivity.getResources().getString(2131427641));
      localBundle.putParcelable("pendingIntent", localPendingIntent);
    }
    AccountManager.get(paramActivity).addAccount("com.google", "mail", null, localBundle, paramActivity, new AccountManagerCallback()
    {
      // ERROR //
      public void run(AccountManagerFuture<Bundle> paramAnonymousAccountManagerFuture)
      {
        // Byte code:
        //   0: aload_1
        //   1: invokeinterface 39 1 0
        //   6: ifeq +28 -> 34
        //   9: aload_0
        //   10: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   13: ifnull +13 -> 26
        //   16: aload_0
        //   17: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   20: aconst_null
        //   21: invokeinterface 45 2 0
        //   26: aload_0
        //   27: getfield 22	com/google/android/gm/AccountHelper$2:val$pendingIntent	Landroid/app/PendingIntent;
        //   30: invokevirtual 50	android/app/PendingIntent:cancel	()V
        //   33: return
        //   34: aconst_null
        //   35: astore_2
        //   36: aload_1
        //   37: invokeinterface 54 1 0
        //   42: checkcast 56	android/os/Bundle
        //   45: astore 7
        //   47: aload 7
        //   49: ldc 58
        //   51: invokevirtual 62	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
        //   54: astore 8
        //   56: aload 7
        //   58: ldc 64
        //   60: invokevirtual 62	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
        //   63: astore 9
        //   65: aload 8
        //   67: invokestatic 70	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
        //   70: istore 10
        //   72: aconst_null
        //   73: astore_2
        //   74: iload 10
        //   76: ifne +51 -> 127
        //   79: aload 9
        //   81: invokestatic 70	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
        //   84: istore 11
        //   86: aconst_null
        //   87: astore_2
        //   88: iload 11
        //   90: ifne +37 -> 127
        //   93: new 72	android/accounts/Account
        //   96: dup
        //   97: aload 8
        //   99: aload 9
        //   101: invokespecial 75	android/accounts/Account:<init>	(Ljava/lang/String;Ljava/lang/String;)V
        //   104: astore 12
        //   106: aload 12
        //   108: ldc 77
        //   110: invokestatic 83	android/content/ContentResolver:getIsSyncable	(Landroid/accounts/Account;Ljava/lang/String;)I
        //   113: ifge +11 -> 124
        //   116: aload 12
        //   118: ldc 77
        //   120: iconst_1
        //   121: invokestatic 87	android/content/ContentResolver:setSyncAutomatically	(Landroid/accounts/Account;Ljava/lang/String;Z)V
        //   124: aload 12
        //   126: astore_2
        //   127: aload_0
        //   128: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   131: ifnull +13 -> 144
        //   134: aload_0
        //   135: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   138: aload_2
        //   139: invokeinterface 45 2 0
        //   144: aload_0
        //   145: getfield 22	com/google/android/gm/AccountHelper$2:val$pendingIntent	Landroid/app/PendingIntent;
        //   148: invokevirtual 50	android/app/PendingIntent:cancel	()V
        //   151: return
        //   152: astore 6
        //   154: aload_0
        //   155: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   158: ifnull +13 -> 171
        //   161: aload_0
        //   162: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   165: aload_2
        //   166: invokeinterface 45 2 0
        //   171: aload_0
        //   172: getfield 22	com/google/android/gm/AccountHelper$2:val$pendingIntent	Landroid/app/PendingIntent;
        //   175: invokevirtual 50	android/app/PendingIntent:cancel	()V
        //   178: return
        //   179: astore 5
        //   181: aload_0
        //   182: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   185: ifnull +13 -> 198
        //   188: aload_0
        //   189: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   192: aload_2
        //   193: invokeinterface 45 2 0
        //   198: aload_0
        //   199: getfield 22	com/google/android/gm/AccountHelper$2:val$pendingIntent	Landroid/app/PendingIntent;
        //   202: invokevirtual 50	android/app/PendingIntent:cancel	()V
        //   205: return
        //   206: astore 4
        //   208: aload_0
        //   209: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   212: ifnull +13 -> 225
        //   215: aload_0
        //   216: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   219: aload_2
        //   220: invokeinterface 45 2 0
        //   225: aload_0
        //   226: getfield 22	com/google/android/gm/AccountHelper$2:val$pendingIntent	Landroid/app/PendingIntent;
        //   229: invokevirtual 50	android/app/PendingIntent:cancel	()V
        //   232: return
        //   233: astore_3
        //   234: aload_0
        //   235: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   238: ifnull +13 -> 251
        //   241: aload_0
        //   242: getfield 20	com/google/android/gm/AccountHelper$2:val$callback	Lcom/google/android/gm/AccountHelper$AddAccountCallback;
        //   245: aload_2
        //   246: invokeinterface 45 2 0
        //   251: aload_0
        //   252: getfield 22	com/google/android/gm/AccountHelper$2:val$pendingIntent	Landroid/app/PendingIntent;
        //   255: invokevirtual 50	android/app/PendingIntent:cancel	()V
        //   258: aload_3
        //   259: athrow
        //   260: astore_3
        //   261: aload 12
        //   263: astore_2
        //   264: goto -30 -> 234
        //   267: astore 15
        //   269: aload 12
        //   271: astore_2
        //   272: goto -64 -> 208
        //   275: astore 14
        //   277: aload 12
        //   279: astore_2
        //   280: goto -99 -> 181
        //   283: astore 13
        //   285: aload 12
        //   287: astore_2
        //   288: goto -134 -> 154
        //
        // Exception table:
        //   from	to	target	type
        //   36	72	152	android/accounts/OperationCanceledException
        //   79	86	152	android/accounts/OperationCanceledException
        //   93	106	152	android/accounts/OperationCanceledException
        //   36	72	179	java/io/IOException
        //   79	86	179	java/io/IOException
        //   93	106	179	java/io/IOException
        //   36	72	206	android/accounts/AuthenticatorException
        //   79	86	206	android/accounts/AuthenticatorException
        //   93	106	206	android/accounts/AuthenticatorException
        //   36	72	233	finally
        //   79	86	233	finally
        //   93	106	233	finally
        //   106	124	260	finally
        //   106	124	267	android/accounts/AuthenticatorException
        //   106	124	275	java/io/IOException
        //   106	124	283	android/accounts/OperationCanceledException
      }
    }
    , null);
  }

  public void asyncGetAccountsInfo(final AccountResultsCallback paramAccountResultsCallback)
  {
    AccountManager localAccountManager = AccountManager.get(this.mContext);
    String[] arrayOfString = new String[1];
    arrayOfString[0] = GoogleLoginServiceConstants.featureForService("mail");
    localAccountManager.getAccountsByTypeAndFeatures("com.google", arrayOfString, new AccountManagerCallback()
    {
      public void run(AccountManagerFuture<Account[]> paramAnonymousAccountManagerFuture)
      {
        try
        {
          arrayOfAccount = (Account[])paramAnonymousAccountManagerFuture.getResult();
          paramAccountResultsCallback.exec(arrayOfAccount);
          return;
        }
        catch (OperationCanceledException localOperationCanceledException)
        {
          while (true)
          {
            LogUtils.w("Gmail", localOperationCanceledException, "Unexpected exception trying to get accounts.", new Object[0]);
            arrayOfAccount = null;
          }
        }
        catch (IOException localIOException)
        {
          while (true)
          {
            LogUtils.w("Gmail", localIOException, "Unexpected exception trying to get accounts.", new Object[0]);
            arrayOfAccount = null;
          }
        }
        catch (AuthenticatorException localAuthenticatorException)
        {
          while (true)
          {
            LogUtils.w("Gmail", localAuthenticatorException, "Unexpected exception trying to get accounts.", new Object[0]);
            Account[] arrayOfAccount = null;
          }
        }
      }
    }
    , null);
  }

  public String validateAccountName(String paramString)
  {
    if ((paramString != null) && (Utils.isValidGoogleAccount(this.mContext, paramString)))
      return paramString;
    return null;
  }

  public static abstract interface AccountResultsCallback
  {
    public abstract void exec(Account[] paramArrayOfAccount);
  }

  public static abstract interface AddAccountCallback
  {
    public abstract void onResult(Account paramAccount);
  }

  public static abstract interface CredentialsCallback
  {
    public abstract void onCredentialsResult(boolean paramBoolean);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.AccountHelper
 * JD-Core Version:    0.6.2
 */