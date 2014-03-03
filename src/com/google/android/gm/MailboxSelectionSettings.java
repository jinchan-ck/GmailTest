package com.google.android.gm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.Window;
import android.widget.TextView;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.LabelMap;

public class MailboxSelectionSettings extends PreferenceActivity
{
  private Gmail mGmail;

  private void onAccountsReceived(Account[] paramArrayOfAccount)
  {
    ((TextView)findViewById(2131361867)).setText(Utils.formatPlural(this, 2131427348, paramArrayOfAccount.length));
    int i = 0;
    while (true)
    {
      int j = paramArrayOfAccount.length;
      if (i < j)
      {
        addPreferencesFromResource(2131034113);
        PreferenceCategory localPreferenceCategory = (PreferenceCategory)getPreferenceScreen().getPreference(i);
        String str = paramArrayOfAccount[i].name;
        localPreferenceCategory.setTitle(str);
        Gmail.LabelMap localLabelMap = this.mGmail.getLabelMap(str);
        Preference localPreference = new Preference(this);
        CharSequence localCharSequence = LongShadowUtils.getHumanLabelName(this, localLabelMap, "^i");
        try
        {
          int k = localLabelMap.getNumUnreadConversations(localLabelMap.getLabelIdInbox());
          localPreference.setTitle(localCharSequence + " (" + k + ")");
          Intent localIntent = new Intent(this, ConversationListActivity.class);
          localIntent.putExtra("account", str);
          localPreference.setIntent(localIntent);
          localPreferenceCategory.addPreference(localPreference);
          i++;
        }
        catch (IllegalStateException localIllegalStateException)
        {
          while (true)
          {
            Preference.OnPreferenceClickListener local1 = new Preference.OnPreferenceClickListener()
            {
              public boolean onPreferenceClick(Preference paramAnonymousPreference)
              {
                Utils.handleAccountNotSynchronized(MailboxSelectionSettings.this);
                return true;
              }
            };
            localPreference.setOnPreferenceClickListener(local1);
            localPreference.setTitle(localCharSequence + " (" + getString(2131296487) + ")");
            localPreferenceCategory.addPreference(localPreference);
          }
        }
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    requestWindowFeature(7);
    super.onCreate(paramBundle);
    getWindow().setFeatureInt(7, 2130903066);
    onAccountsReceived(AccountManager.get(this).getAccountsByType("com.google"));
    this.mGmail = LongShadowUtils.getContentProviderMailAccess(getContentResolver());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.MailboxSelectionSettings
 * JD-Core Version:    0.6.2
 */