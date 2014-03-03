package com.google.android.gm.preference;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceActivity.Header;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import com.android.mail.providers.Folder;
import com.android.mail.ui.WhatsNewDialogFragment;
import com.android.mail.ui.WhatsNewDialogFragment.WhatsNewDialogLauncher;
import com.android.mail.ui.WhatsNewDialogFragment.WhatsNewDialogListener;
import com.google.android.gm.AccountHelper;
import com.google.android.gm.AccountHelper.AddAccountCallback;
import com.google.android.gm.ApplicationMenuHandler;
import com.google.android.gm.ApplicationMenuHandler.HelpCallback;
import com.google.android.gm.LabelsActivity;
import com.google.android.gm.Utils;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.provider.UiProvider;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GmailPreferenceActivity extends PreferenceActivity
  implements SharedPreferences.OnSharedPreferenceChangeListener, WhatsNewDialogFragment.WhatsNewDialogLauncher, WhatsNewDialogFragment.WhatsNewDialogListener, AccountHelper.AddAccountCallback, ApplicationMenuHandler.HelpCallback
{
  private static boolean sCreatedAccount = false;
  private List<String> mAccounts;
  private boolean mRestartAccountQuery = false;
  private boolean mSynced = false;

  private void addAccountHeaders(List<PreferenceActivity.Header> paramList)
  {
    this.mAccounts = Persistence.getInstance().getCachedConfiguredGoogleAccounts(this, false);
    if (!this.mSynced)
    {
      asyncInitAccounts();
      this.mSynced = true;
    }
    while (true)
    {
      return;
      for (int i = -1 + this.mAccounts.size(); i >= 0; i--)
      {
        String str = (String)this.mAccounts.get(i);
        PreferenceActivity.Header localHeader = new PreferenceActivity.Header();
        localHeader.title = str;
        localHeader.fragment = "com.google.android.gm.preference.AccountPreferenceFragment";
        Bundle localBundle = new Bundle();
        localBundle.putString("account", str);
        localHeader.fragmentArguments = localBundle;
        paramList.add(1, localHeader);
      }
    }
  }

  private void asyncInitAccounts()
  {
    AccountHelper.getSyncingAccounts(this, new AccountManagerCallback()
    {
      public void run(AccountManagerFuture<android.accounts.Account[]> paramAnonymousAccountManagerFuture)
      {
        try
        {
          GmailPreferenceActivity localGmailPreferenceActivity = GmailPreferenceActivity.this;
          GmailPreferenceActivity.access$002(GmailPreferenceActivity.this, AccountHelper.mergeAccountLists(GmailPreferenceActivity.this.mAccounts, (android.accounts.Account[])paramAnonymousAccountManagerFuture.getResult(), true));
          Persistence.getInstance().cacheConfiguredGoogleAccounts(localGmailPreferenceActivity, false, GmailPreferenceActivity.this.mAccounts);
          GmailPreferenceActivity.this.invalidateHeaders();
          return;
        }
        catch (OperationCanceledException localOperationCanceledException)
        {
        }
        catch (IOException localIOException)
        {
        }
        catch (AuthenticatorException localAuthenticatorException)
        {
        }
      }
    });
  }

  private final PreferenceActivity.Header getInitialHeader(long paramLong, List<PreferenceActivity.Header> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      PreferenceActivity.Header localHeader = (PreferenceActivity.Header)localIterator.next();
      if ((localHeader.id == paramLong) && (localHeader.fragment != null))
        return localHeader;
    }
    return null;
  }

  private void launchLabelSettings(Intent paramIntent)
  {
    Folder localFolder = (Folder)paramIntent.getParcelableExtra("extra_folder");
    String str1 = ((com.android.mail.providers.Account)paramIntent.getParcelableExtra("extra_account")).name;
    String str2 = localFolder.uri.getLastPathSegment();
    Intent localIntent = new Intent(this, LabelsActivity.class);
    localIntent.putExtra("account_key", str1);
    localIntent.putExtra("label", str2);
    startActivity(localIntent);
    finish();
  }

  private void launchManageLabels(Intent paramIntent)
  {
    String str = ((com.android.mail.providers.Account)paramIntent.getParcelableExtra("extra_account")).name;
    Intent localIntent = new Intent(this, LabelsActivity.class);
    localIntent.putExtra("account_key", str);
    startActivity(localIntent);
    finish();
  }

  private void loadHeaders(List<PreferenceActivity.Header> paramList)
  {
    loadHeadersFromResource(2131099653, paramList);
    addAccountHeaders(paramList);
  }

  private void loadInitialHeader(long paramLong)
  {
    ArrayList localArrayList = Lists.newArrayList();
    loadHeaders(localArrayList);
    PreferenceActivity.Header localHeader = getInitialHeader(paramLong, localArrayList);
    if (localHeader != null)
    {
      if (!isMultiPane())
      {
        startActivity(onBuildStartFragmentIntent(localHeader.fragment, new Bundle(), localHeader.titleRes, 0));
        finish();
      }
    }
    else
      return;
    switchToHeader(localHeader);
  }

  public String getHelpContext()
  {
    return "gm_settings";
  }

  public void onBuildHeaders(List<PreferenceActivity.Header> paramList)
  {
    loadHeaders(paramList);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    long l = -1L;
    if (localIntent.hasExtra("initial_fragment_id"))
    {
      l = localIntent.getLongExtra("initial_fragment_id", -1L);
      localIntent.removeExtra("initial_fragment_id");
    }
    while (true)
    {
      if (l != -1L)
      {
        int i = 1;
        if ((l == 2131689743L) && (localIntent.getBooleanExtra("reporting_problem", false)))
        {
          Utils.launchGoogleFeedback(this);
          i = 0;
          finish();
        }
        if (i != 0)
          loadInitialHeader(l);
      }
      ActionBar localActionBar = getActionBar();
      if (localActionBar != null)
        localActionBar.setDisplayOptions(4, 4);
      PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
      return;
      if (localIntent.hasExtra("extra_folder"))
      {
        launchLabelSettings(localIntent);
        return;
      }
      if (localIntent.hasExtra("extra_manage_folders"))
      {
        launchManageLabels(localIntent);
      }
      else
      {
        Uri localUri = localIntent.getData();
        if (localUri != null)
        {
          String str = localUri.getQueryParameter("preference_fragment_id");
          if (str != null)
            l = Long.parseLong(str);
        }
      }
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131820556, paramMenu);
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
      return ApplicationMenuHandler.handleApplicationMenu(paramMenuItem, this, this);
    case 2131689778:
      AccountHelper.showAddAccount(this, this);
      return true;
    case 16908332:
    }
    finish();
    return true;
  }

  public void onPause()
  {
    super.onPause();
    this.mRestartAccountQuery = true;
  }

  public void onResult(android.accounts.Account paramAccount)
  {
    if (paramAccount != null)
      sCreatedAccount = true;
  }

  public void onResume()
  {
    super.onResume();
    if ((sCreatedAccount) || (this.mRestartAccountQuery));
    for (int i = 1; ; i = 0)
    {
      sCreatedAccount = false;
      if (i != 0)
        asyncInitAccounts();
      return;
    }
  }

  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    UiProvider.notifyAccountListChanged(this);
  }

  public void onStop()
  {
    super.onStop();
    PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
  }

  public void onWhatsNewDialogLayoutInflated(View paramView)
  {
    CheckBox localCheckBox = (CheckBox)paramView.findViewById(2131689718);
    localCheckBox.setVisibility(8);
    localCheckBox.setChecked(Persistence.getInstance().getConversationOverviewMode(this));
  }

  public void showWhatsNewDialog()
  {
    WhatsNewDialogFragment.newInstance().show(getFragmentManager(), "WhatsNewDialogFragment");
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.preference.GmailPreferenceActivity
 * JD-Core Version:    0.6.2
 */