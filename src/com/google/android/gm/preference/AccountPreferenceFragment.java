package com.google.android.gm.preference;

import android.accounts.Account;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import com.google.android.gm.LabelsActivity;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.Settings;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;

public final class AccountPreferenceFragment extends GmailPreferenceFragment
  implements IntegerPickerPreference.Callbacks
{
  private String mAccount;
  private Gmail mGmail;
  private Persistence mPersistence;
  private Gmail.Settings mSettings;

  private void migrateNotificationSettings(boolean paramBoolean)
  {
    String str1;
    if (paramBoolean)
    {
      str1 = "^i";
      if (!paramBoolean)
        break label76;
    }
    Persistence localPersistence;
    Activity localActivity;
    Set localSet;
    label76: for (String str2 = "^iim"; ; str2 = "^i")
    {
      localPersistence = Persistence.getInstance();
      localActivity = getActivity();
      localSet = localPersistence.getNotificationLabelInformation(localActivity, this.mAccount, str1, null);
      localPersistence.clearNotificationLabel(localActivity, this.mAccount, str1);
      if (localSet != null)
        break label82;
      localPersistence.clearNotificationLabel(localActivity, this.mAccount, str2);
      return;
      str1 = "^iim";
      break;
    }
    label82: localPersistence.addNotificationLabel(localActivity, this.mAccount, str2, localSet);
  }

  private void setNotificationSettingState(Activity paramActivity, Persistence paramPersistence)
  {
    String str1 = getString(2131427700);
    initializeCheckBoxAndSummary("enable-notifications", paramPersistence.getEnableNotifications(paramActivity, this.mAccount), str1);
    Preference localPreference = findPreference("inbox-settings");
    if (localPreference != null)
    {
      String str2 = Persistence.getAccountInbox(paramActivity, this.mAccount);
      localPreference.setSummary(com.google.android.gm.Utils.getLabelNotificationSummary(paramActivity, this.mAccount, str2));
    }
  }

  private void setPreferenceChangeListener(String paramString)
  {
    Preference localPreference = findPreference(paramString);
    if (localPreference != null)
      localPreference.setOnPreferenceChangeListener(this);
  }

  private void updatePreferenceList()
  {
    ((IntegerPickerPreference)findPreference("number-picker")).bind(this, (int)this.mSettings.getConversationAgeDays());
    Vibrator localVibrator = (Vibrator)getActivity().getSystemService("vibrator");
    if ((localVibrator != null) && (localVibrator.hasVibrator()));
    for (int i = 1; ; i = 0)
    {
      if (i == 0)
      {
        Preference localPreference = findPreference("inbox-settings");
        if (localPreference != null)
          localPreference.setTitle(2131427746);
      }
      return;
    }
  }

  private boolean useMultiPaneUI()
  {
    Activity localActivity = getActivity();
    if ((localActivity instanceof PreferenceActivity))
      return ((PreferenceActivity)localActivity).onIsMultiPane();
    return com.android.mail.utils.Utils.useTabletUI(localActivity);
  }

  private void validateSyncSetForInboxSetting(Preference paramPreference)
  {
    Context localContext = paramPreference.getContext();
    String str = Persistence.getAccountInbox(localContext, this.mAccount);
    Gmail localGmail = LongShadowUtils.getContentProviderMailAccess(localContext.getContentResolver());
    Gmail.Settings localSettings = localGmail.getSettings(localContext, this.mAccount);
    HashSet localHashSet1 = Sets.newHashSet();
    localHashSet1.addAll(localSettings.getLabelsPartial());
    HashSet localHashSet2 = Sets.newHashSet();
    localHashSet2.addAll(localSettings.getLabelsIncluded());
    if ((!localHashSet1.contains(str)) && (!localHashSet2.contains(str)))
    {
      localHashSet1.add(str);
      localSettings.setLabelsPartial(localHashSet1);
      localGmail.setSettings(this.mAccount, localSettings);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mAccount = getArguments().getString("account");
    this.mPersistence = Persistence.getInstance();
    if (!useMultiPaneUI())
    {
      ActionBar localActionBar = getActivity().getActionBar();
      if (localActionBar != null)
        localActionBar.setTitle(this.mAccount);
    }
    this.mGmail = LongShadowUtils.getContentProviderMailAccess(getActivity().getContentResolver());
    this.mSettings = this.mGmail.getSettings(getActivity(), this.mAccount);
    addPreferencesFromResource(2131099648);
    updatePreferenceList();
  }

  public void onDestroy()
  {
    super.onDestroy();
  }

  public void onNumberChanged(int paramInt)
  {
    this.mSettings.setConversationAgeDays(paramInt);
    updatePreferenceList();
  }

  public void onPause()
  {
    super.onPause();
    this.mGmail.setSettings(this.mAccount, this.mSettings);
  }

  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    Context localContext = paramPreference.getContext();
    if ("signature".equals(paramPreference.getKey()))
    {
      String str = paramObject.toString();
      this.mPersistence.setSignature(localContext, this.mAccount, str);
      return true;
    }
    return false;
  }

  public boolean onPreferenceTreeClick(PreferenceScreen paramPreferenceScreen, Preference paramPreference)
  {
    String str1 = paramPreference.getKey();
    if (str1 == null)
      return false;
    if ("enable-notifications".equals(str1))
    {
      boolean bool2 = ((CheckBoxPreference)findPreference("enable-notifications")).isChecked();
      this.mPersistence.setEnableNotifications(getActivity(), this.mAccount, bool2);
      if (!bool2)
        PreferenceUtils.validateNotificationsForAccount(getActivity(), this.mAccount);
    }
    while (true)
    {
      return true;
      if (str1.equals("action-strip-action-reply-all"))
      {
        CheckBoxPreference localCheckBoxPreference2 = (CheckBoxPreference)paramPreference;
        this.mPersistence.setActionStripActionReplyAll(getActivity(), localCheckBoxPreference2.isChecked());
      }
      else if (str1.equals("priority-inbox-key"))
      {
        boolean bool1 = ((CheckBoxPreference)paramPreferenceScreen.findPreference("priority-inbox-key")).isChecked();
        this.mPersistence.setPriorityInboxDefault(getActivity(), this.mAccount, bool1);
        setNotificationSettingState(getActivity(), this.mPersistence);
        migrateNotificationSettings(bool1);
        validateSyncSetForInboxSetting(paramPreference);
        PreferenceUtils.validateNotificationsForAccount(getActivity(), this.mAccount);
      }
      else if (str1.equals("prefetch-attachments"))
      {
        CheckBoxPreference localCheckBoxPreference1 = (CheckBoxPreference)paramPreference;
        this.mPersistence.setPrefetchAttachments(getActivity(), this.mAccount, localCheckBoxPreference1.isChecked());
      }
      else if (str1.equals("manage-labels"))
      {
        Intent localIntent1 = new Intent(getActivity(), LabelsActivity.class);
        localIntent1.putExtra("account_key", this.mAccount);
        startActivity(localIntent1);
      }
      else if (str1.equals("inbox-settings"))
      {
        String str2 = Persistence.getAccountInbox(getActivity(), this.mAccount);
        Intent localIntent3 = new Intent(getActivity(), LabelsActivity.class);
        localIntent3.putExtra("account_key", this.mAccount);
        localIntent3.putExtra("label", str2);
        startActivity(localIntent3);
      }
      else
      {
        if (!str1.equals("sync_status"))
          break;
        Intent localIntent2 = new Intent("android.settings.SYNC_SETTINGS");
        localIntent2.putExtra("authorities", new String[] { "gmail-ls" });
        localIntent2.setFlags(524288);
        startActivity(localIntent2);
      }
    }
  }

  public void onResume()
  {
    super.onResume();
    Activity localActivity = getActivity();
    boolean bool1 = ContentResolver.getMasterSyncAutomatically();
    boolean bool2 = ((ConnectivityManager)getActivity().getSystemService("connectivity")).getBackgroundDataSetting();
    boolean bool3 = ContentResolver.getSyncAutomatically(new Account(this.mAccount, "com.google"), "gmail-ls");
    if ((bool1) && (bool2) && (bool3));
    for (int i = 2131427749; ; i = 2131427750)
    {
      Resources localResources = localActivity.getResources();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localActivity.getResources().getString(i);
      initializePreferenceScreen("sync_status", localResources.getString(2131427747, arrayOfObject));
      initializeCheckBox("prefetch-attachments", this.mPersistence.getPrefetchAttachments(localActivity, this.mAccount));
      initializeEditText("signature", this.mPersistence.getSignature(localActivity, this.mAccount));
      setNotificationSettingState(localActivity, this.mPersistence);
      initializeCheckBox("priority-inbox-key", this.mPersistence.getPriorityInboxDefault(localActivity, this.mAccount));
      setPreferenceChangeListener("signature");
      return;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.preference.AccountPreferenceFragment
 * JD-Core Version:    0.6.2
 */