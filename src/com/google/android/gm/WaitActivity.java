package com.google.android.gm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.LabelMap;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class WaitActivity extends GmailBaseActivity
{
  private static final String EXTRA_WRAPPED_INTENT = "wrappedIntent";
  public static final int RESULT_MAILBOX_SELECTED = 4;
  static final String WAIT_IF_NEEDED_PERF_TAG = "WA.waitIfNeeded";
  private static Account sCurrentAccount;
  private static String sNewAccount;
  private TextView mDescriptionView;
  private Gmail.LabelMap mLabelMap;
  Observer mLabelsObserver = null;

  private static boolean areLabelsOk(Gmail.LabelMap paramLabelMap)
  {
    return paramLabelMap.labelsSynced();
  }

  private static boolean isAccountOk(Context paramContext, String paramString)
  {
    return (paramString != null) && (Utils.isValidGoogleAccount(paramContext, paramString));
  }

  private void labelsMayBeLoaded()
  {
    if (this.mLabelMap == null);
    while (!this.mLabelMap.labelsSynced())
      return;
    startOriginalActivityAndFinish();
    this.mLabelMap.deleteObserver(this.mLabelsObserver);
  }

  public static void onAccountChange(String paramString)
  {
    sNewAccount = paramString;
  }

  private void onAccountsLoaded(String paramString)
  {
    Persistence.getInstance(this).setActiveAccount(this, paramString);
    waitForLabels(paramString);
  }

  private void setDescriptionText()
  {
    if (ContentResolver.getSyncAutomatically(sCurrentAccount, "gmail-ls"))
    {
      this.mDescriptionView.setText(getString(2131296441));
      return;
    }
    this.mDescriptionView.setText(getString(2131296442));
  }

  private void startOriginalActivityAndFinish()
  {
    Intent localIntent = unwrapIntent(getIntent());
    localIntent.addFlags(33554432);
    startActivity(localIntent);
    finish();
  }

  private static void startWaitActivity(Activity paramActivity, String paramString)
  {
    Intent localIntent = wrapIntentForClass(paramActivity.getIntent(), paramActivity, WaitActivity.class, paramString);
    localIntent.addFlags(33554432);
    paramActivity.startActivity(localIntent);
    paramActivity.finish();
  }

  private static Intent unwrapIntent(Intent paramIntent)
  {
    return (Intent)paramIntent.getParcelableExtra("wrappedIntent");
  }

  private void waitForAccounts(String paramString, boolean paramBoolean)
  {
    if (isAccountOk(this, paramString))
      waitForLabels(paramString);
    while (!paramBoolean)
      return;
    Bundle localBundle = new Bundle();
    localBundle.putCharSequence("optional_message", getText(2131296480));
    AccountManager.get(this).getAuthTokenByFeatures("com.google", "mail", new String[] { "hosted_or_google" }, this, localBundle, null, new AccountManagerCallback()
    {
      public void run(AccountManagerFuture<Bundle> paramAnonymousAccountManagerFuture)
      {
        try
        {
          Bundle localBundle = (Bundle)paramAnonymousAccountManagerFuture.getResult();
          WaitActivity.this.onAccountsLoaded(localBundle.getString("authAccount"));
          i = 1;
          if (i == 0)
            WaitActivity.this.finish();
          return;
        }
        catch (AuthenticatorException localAuthenticatorException)
        {
          while (true)
            i = 0;
        }
        catch (IOException localIOException)
        {
          while (true)
            i = 0;
        }
        catch (OperationCanceledException localOperationCanceledException)
        {
          while (true)
            int i = 0;
        }
      }
    }
    , null);
  }

  private void waitForLabels(String paramString)
  {
    Gmail.LabelMap localLabelMap = LongShadowUtils.getLabelMap(getContentResolver(), paramString);
    if (areLabelsOk(localLabelMap))
    {
      startOriginalActivityAndFinish();
      return;
    }
    sCurrentAccount = new Account(paramString, "com.google");
    setDescriptionText();
    this.mLabelMap = localLabelMap;
    this.mLabelsObserver = new Observer()
    {
      public void update(Observable paramAnonymousObservable, Object paramAnonymousObject)
      {
        WaitActivity.this.labelsMayBeLoaded();
      }
    };
    this.mLabelMap.addObserver(this.mLabelsObserver);
    labelsMayBeLoaded();
  }

  public static String waitIfNeededAndGetAccount(Activity paramActivity)
  {
    Gmail.startTiming("WA.waitIfNeeded");
    String str1 = sNewAccount;
    Object localObject = null;
    if (str1 != null)
    {
      String str2 = sNewAccount;
      Persistence.getInstance(paramActivity).setActiveAccount(paramActivity, str2);
      sNewAccount = null;
      localObject = str2;
    }
    Bundle localBundle;
    if (!isAccountOk(paramActivity, (String)localObject))
    {
      localBundle = paramActivity.getIntent().getExtras();
      if (localBundle == null)
        break label101;
    }
    label101: for (localObject = localBundle.getString("account"); ; localObject = null)
    {
      if (!isAccountOk(paramActivity, (String)localObject))
        localObject = Persistence.getInstance(paramActivity).getActiveAccount(paramActivity);
      if (isAccountOk(paramActivity, (String)localObject))
        break;
      startWaitActivity(paramActivity, (String)localObject);
      Gmail.stopTiming("WA.waitIfNeeded");
      return null;
    }
    if (!areLabelsOk(LongShadowUtils.getLabelMap(paramActivity.getContentResolver(), (String)localObject)))
    {
      startWaitActivity(paramActivity, (String)localObject);
      Gmail.stopTiming("WA.waitIfNeeded");
      return null;
    }
    Gmail.stopTiming("WA.waitIfNeeded");
    return localObject;
  }

  private static Intent wrapIntentForClass(Intent paramIntent, Context paramContext, Class paramClass, String paramString)
  {
    Intent localIntent = new Intent();
    localIntent.setClass(paramContext, paramClass);
    localIntent.putExtra("wrappedIntent", paramIntent);
    localIntent.putExtra("account", paramString);
    return localIntent;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    switch (paramInt1)
    {
    default:
    case 4:
    }
    do
      return;
    while (paramIntent == null);
    onAccountChange(paramIntent.getStringExtra("account"));
    finish();
    startActivity(new Intent(this, ConversationListActivity.class));
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903079);
    this.mDescriptionView = ((TextView)findViewById(2131361919));
    String str = getIntent().getStringExtra("account");
    if (paramBundle == null);
    for (boolean bool = true; ; bool = false)
    {
      waitForAccounts(str, bool);
      return;
    }
  }

  protected void onDestroy()
  {
    if ((this.mLabelMap != null) && (this.mLabelsObserver != null))
      this.mLabelMap.deleteObserver(this.mLabelsObserver);
    super.onDestroy();
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    int i = paramMenuItem.getItemId();
    if (i == 2131361934)
    {
      startActivityForResult(new Intent(this, MailboxSelectionActivity.class), 4);
      return true;
    }
    if (i == 2131361932)
    {
      if (sCurrentAccount != null)
        Utils.startSync(this, sCurrentAccount.name);
      return true;
    }
    if (i == 2131361924)
    {
      Utils.showHelp(this);
      return true;
    }
    return false;
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    paramMenu.clear();
    getMenuInflater().inflate(2131623946, paramMenu);
    return super.onPrepareOptionsMenu(paramMenu);
  }

  protected void onResume()
  {
    super.onResume();
    setDescriptionText();
    labelsMayBeLoaded();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.WaitActivity
 * JD-Core Version:    0.6.2
 */