package com.google.android.gm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailboxSelectionActivity extends GmailBaseListActivity
{
  private static final String ACCOUNT = "name";
  private static final String ACCOUNT_STATUS = "account-status";
  private static final String[] COLUMN_NAMES = { "is-active", "name", "account-status" };
  private static final String CREATE_SHORTCUT_KEY = "createShortcut";
  public static final String EXTRA_FROM_NOTIFICATION = "from-notification";
  public static final String EXTRA_SIMPLE = "simple";
  public static final String EXTRA_TITLE = "title";
  private static final String IS_ACTIVE = "is-active";
  private static final String LABEL_CANONICAL_NAME = "label-canonical-name";
  private static final String LABEL_NAME = "label-name";
  private static final int RESULT_LABEL_NAMED = 2;
  private static final int RESULT_LABEL_SELECTION = 1;
  private static final String SHORTCUT_ACCOUNT_KEY = "shortcutAccount";
  private static final String UNSEEN = "unseen";
  private int[] VIEW_IDS = { 2131361862, 2131361863 };
  private final BroadcastReceiver mAccountsChangedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      MailboxSelectionActivity.this.setupWithAccounts();
    }
  };
  private boolean mCreateShortcut = false;
  private Gmail mGmail;
  private String mShortcutAccount = null;
  private String mTitle;

  private void addLabelInfoToMap(String paramString1, String paramString2, Map<String, Object> paramMap)
  {
    CharSequence localCharSequence = LongShadowUtils.getHumanLabelName(this, this.mGmail.getLabelMap(paramString1), paramString2);
    paramMap.put("account-status", localCharSequence);
    paramMap.put("label-name", localCharSequence);
    paramMap.put("label-canonical-name", paramString2);
  }

  private void completeSetupWithAccounts(Account[] paramArrayOfAccount)
  {
    Utils.cacheGoogleAccountList(this, false, paramArrayOfAccount);
    ArrayList localArrayList = Lists.newArrayList();
    Persistence localPersistence = Persistence.getInstance(this);
    String str1 = localPersistence.getActiveAccount(this);
    int i = 0;
    while (true)
      if (i < paramArrayOfAccount.length)
      {
        HashMap localHashMap = Maps.newHashMap();
        String str2 = paramArrayOfAccount[i].name;
        localHashMap.put("name", str2);
        try
        {
          if (localPersistence.getPriorityInboxDefault(this, str2));
          for (String str3 = "^iim"; ; str3 = "^i")
          {
            int j = Utils.getUnreadConversations(this, str2, str3);
            addLabelInfoToMap(str2, str3, localHashMap);
            localHashMap.put("unseen", Integer.valueOf(j));
            if (str2.equals(str1))
              localHashMap.put("is-active", "true");
            localArrayList.add(localHashMap);
            i++;
            break;
          }
        }
        catch (IllegalStateException localIllegalStateException)
        {
          while (true)
            localHashMap.put("account-status", getResources().getString(2131296487));
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          while (true)
          {
            Log.i("Gmail", "Couldn't find a system label: " + localIllegalArgumentException);
            localHashMap.put("account-status", getResources().getString(2131296487));
          }
        }
      }
    updateAccountList(localArrayList);
  }

  private void initFooter()
  {
    View localView = findViewById(2131361865);
    if (showFullScreen())
    {
      localView.setVisibility(0);
      ((Button)findViewById(2131361866)).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Intent localIntent = new Intent("android.settings.ADD_ACCOUNT_SETTINGS");
          localIntent.putExtra("authorities", new String[] { "gmail-ls" });
          MailboxSelectionActivity.this.startActivity(localIntent);
        }
      });
      return;
    }
    localView.setVisibility(8);
  }

  private void restoreState(Bundle paramBundle)
  {
    if (paramBundle.containsKey("shortcutAccount"))
      this.mShortcutAccount = paramBundle.getString("shortcutAccount");
    if (paramBundle.containsKey("createShortcut"))
      this.mCreateShortcut = paramBundle.getBoolean("createShortcut");
  }

  private void setupWithAccounts()
  {
    AccountManager.get(this).getAccountsByTypeAndFeatures("com.google", new String[] { "service_mail" }, new AccountManagerCallback()
    {
      public void run(AccountManagerFuture<Account[]> paramAnonymousAccountManagerFuture)
      {
        Account[] arrayOfAccount = new Account[0];
        try
        {
          arrayOfAccount = (Account[])paramAnonymousAccountManagerFuture.getResult();
          MailboxSelectionActivity.this.completeSetupWithAccounts(arrayOfAccount);
          return;
        }
        catch (OperationCanceledException localOperationCanceledException)
        {
          while (true)
            Log.w("Gmail", "Unexpected exception trying to get accounts.", localOperationCanceledException);
        }
        catch (IOException localIOException)
        {
          while (true)
            Log.w("Gmail", "Unexpected exception trying to get accounts.", localIOException);
        }
        catch (AuthenticatorException localAuthenticatorException)
        {
          while (true)
            Log.w("Gmail", "Unexpected exception trying to get accounts.", localAuthenticatorException);
        }
      }
    }
    , null);
  }

  private void setupWithCachedAccounts()
  {
    List localList = Persistence.getInstance(this).getCachedConfiguredGoogleAccounts(this, false);
    int i = localList.size();
    if (i == 0)
      return;
    ArrayList localArrayList = Lists.newArrayList();
    Persistence localPersistence = Persistence.getInstance(this);
    String str1 = localPersistence.getActiveAccount(this);
    int j = 0;
    if (j < i)
    {
      String str2 = (String)localList.get(j);
      HashMap localHashMap = Maps.newHashMap();
      localHashMap.put("name", str2);
      if (localPersistence.getPriorityInboxDefault(this, str2));
      for (String str3 = "^iim"; ; str3 = "^i")
      {
        addLabelInfoToMap(str2, str3, localHashMap);
        localHashMap.put("unseen", Integer.valueOf(0));
        if (str2.equals(str1))
          localHashMap.put("is-active", "true");
        localArrayList.add(localHashMap);
        j++;
        break;
      }
    }
    updateAccountList(localArrayList);
  }

  private boolean showFullScreen()
  {
    return (!this.mCreateShortcut) && (getIntent().getStringExtra("simple") == null);
  }

  private void updateAccountList(final List<Map<String, Object>> paramList)
  {
    if (this.mCreateShortcut);
    for (this.mTitle = getResources().getString(2131296485); ; this.mTitle = getIntent().getStringExtra("title"))
    {
      if (this.mTitle == null)
        this.mTitle = Utils.formatPlural(this, 2131427348, paramList.size());
      ((TextView)findViewById(2131361867)).setText(this.mTitle);
      setListAdapter(new SimpleAdapter(this, paramList, 2130903064, COLUMN_NAMES, this.VIEW_IDS)
      {
        public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
        {
          View localView = super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup);
          TextView localTextView1 = (TextView)localView.findViewById(2131361862);
          Map localMap = (Map)paramList.get(paramAnonymousInt);
          Integer localInteger = (Integer)localMap.get("unseen");
          String str1 = (String)localMap.get("name");
          String str2 = (String)localMap.get("label-name");
          localTextView1.setText(str1);
          localView.setTag(str1);
          TextView localTextView2 = (TextView)localView.findViewById(2131361863);
          TextView localTextView3 = (TextView)localView.findViewById(2131361864);
          if (localInteger != null)
          {
            localTextView2.setText(str2);
            localTextView2.setVisibility(0);
            if (localInteger.intValue() > 0)
            {
              localTextView3.setVisibility(0);
              localTextView3.setText(localInteger.toString());
              localTextView3.setTypeface(Typeface.DEFAULT_BOLD);
              return localView;
            }
            localTextView3.setVisibility(8);
            return localView;
          }
          localTextView2.setVisibility(0);
          localTextView2.setText(MailboxSelectionActivity.this.getResources().getString(2131296487));
          return localView;
        }
      });
      return;
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 1) && (paramIntent != null))
    {
      localIntent2 = new Intent();
      str2 = paramIntent.getStringExtra("label");
      localIntent2.putExtra("android.intent.extra.shortcut.INTENT", Utils.createLabelIntent(this, this.mShortcutAccount, str2, true));
      localIntent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", paramIntent.getParcelableExtra("android.intent.extra.shortcut.ICON_RESOURCE"));
      localIntent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(this, 2130837574));
      localCharSequence = LongShadowUtils.getHumanLabelName(this, LongShadowUtils.getLabelMap(getContentResolver(), this.mShortcutAccount), str2);
      localIntent2.putExtra("android.intent.extra.shortcut.NAME", localCharSequence);
      localIntent3 = new Intent(this, ShortcutNameActivity.class);
      localIntent3.setFlags(1073741824);
      localIntent3.putExtra("extra_label_click_intent", localIntent2);
      localIntent3.putExtra("extra_shortcut_name", localCharSequence);
      startActivityForResult(localIntent3, 2);
    }
    while (paramInt1 != 2)
    {
      Intent localIntent2;
      String str2;
      CharSequence localCharSequence;
      Intent localIntent3;
      return;
    }
    if (paramIntent != null)
    {
      Intent localIntent1 = (Intent)paramIntent.getParcelableExtra("extra_label_click_intent");
      String str1 = paramIntent.getStringExtra("extra_shortcut_name");
      if (TextUtils.getTrimmedLength(str1) > 0)
        localIntent1.putExtra("android.intent.extra.shortcut.NAME", str1);
      setResult(-1, localIntent1);
    }
    while (true)
    {
      finish();
      return;
      setResult(paramInt2);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(7);
    setContentView(2130903065);
    getWindow().setFeatureInt(7, 2130903066);
    if (paramBundle != null)
      restoreState(paramBundle);
    while (true)
    {
      this.mGmail = LongShadowUtils.getContentProviderMailAccess(getContentResolver());
      return;
      if ("android.intent.action.CREATE_SHORTCUT".equals(getIntent().getAction()))
        this.mCreateShortcut = true;
    }
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    String str;
    if (paramView != null)
    {
      str = (String)paramView.getTag();
      if (this.mCreateShortcut)
      {
        Intent localIntent = new Intent(this, LabelsActivity.class);
        this.mShortcutAccount = str;
        localIntent.setFlags(1073741824);
        localIntent.putExtra("account-shortcut", this.mShortcutAccount);
        startActivityForResult(localIntent, 1);
      }
    }
    else
    {
      return;
    }
    Utils.changeAccount(this, str, false);
  }

  public void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
      return false;
    case 2131361961:
      Utils.performAccountPreferences(this);
      return true;
    case 2131361924:
      Utils.showHelp(this);
      return true;
    case 2131361938:
    }
    Utils.showAbout(this);
    return true;
  }

  public void onPause()
  {
    super.onPause();
    unregisterReceiver(this.mAccountsChangedReceiver);
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    paramMenu.clear();
    if (showFullScreen())
      getMenuInflater().inflate(2131623944, paramMenu);
    return super.onPrepareOptionsMenu(paramMenu);
  }

  public void onResume()
  {
    super.onResume();
    initFooter();
    setupWithAccounts();
    registerReceiver(this.mAccountsChangedReceiver, new IntentFilter("android.accounts.LOGIN_ACCOUNTS_CHANGED"));
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mShortcutAccount != null)
      paramBundle.putString("shortcutAccount", this.mShortcutAccount);
    paramBundle.putBoolean("createShortcut", this.mCreateShortcut);
  }

  public void onStart()
  {
    super.onStart();
    InternalActivityStack.finishAllOtherActivities(this);
    setupWithCachedAccounts();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.MailboxSelectionActivity
 * JD-Core Version:    0.6.2
 */