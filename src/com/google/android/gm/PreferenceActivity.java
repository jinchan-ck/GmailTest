package com.google.android.gm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.SearchRecentSuggestions;
import android.view.View;
import android.view.Window;
import com.google.common.collect.Maps;
import java.util.HashMap;

public class PreferenceActivity extends GmailSettingsBaseActivity
  implements Preference.OnPreferenceChangeListener
{
  private static final String ACTION_STRIP_REPLY_ALL = "action-strip-action-reply-all";
  private static final String ALLOW_BATCH_KEY = "allow-batch";
  private static final String AUTO_ADVANCE_KEY = "auto-advance";
  private static final String CLEAR_HISTORY_KEY = "clear-search-history";
  private static final String CONFIRM_ACTIONS_KEY = "confirm-actions";
  private static final String MESSAGE_TEXT_SIZE_KEY = "message-text";
  private static final String NOTIFICATIONS_KEY = "enable-notifications";
  private static final String RINGTONE_KEY = "ringtone";
  private static final String SIGNATURE_KEY = "signature";
  private static final String UNOBTRUSIVE_KEY = "unobtrusive";
  private static final String VIBRATE_WHEN_KEY = "vibrateWhen";
  private HashMap<CharSequence, CharSequence> mTextMessageSizeMap;
  private HashMap<CharSequence, CharSequence> mVibrateModeMap;

  private CharSequence getHumanReadableTextSizeName(String paramString)
  {
    initMessageTextSizeMap();
    return (CharSequence)this.mTextMessageSizeMap.get(paramString);
  }

  private CharSequence getHumanReadableVibrateModeName(String paramString)
  {
    initMessageVibrateModeMap();
    return (CharSequence)this.mVibrateModeMap.get(paramString);
  }

  private void initMessageTextSizeMap()
  {
    if (this.mTextMessageSizeMap != null);
    while (true)
    {
      return;
      CharSequence[] arrayOfCharSequence1 = getResources().getTextArray(2131492869);
      CharSequence[] arrayOfCharSequence2 = getResources().getTextArray(2131492870);
      this.mTextMessageSizeMap = Maps.newHashMap();
      for (int i = 0; i < arrayOfCharSequence2.length; i++)
        this.mTextMessageSizeMap.put(arrayOfCharSequence2[i], arrayOfCharSequence1[i]);
    }
  }

  private void initMessageVibrateModeMap()
  {
    if (this.mVibrateModeMap != null);
    while (true)
    {
      return;
      CharSequence[] arrayOfCharSequence1 = getResources().getTextArray(2131492871);
      CharSequence[] arrayOfCharSequence2 = getResources().getTextArray(2131492872);
      this.mVibrateModeMap = Maps.newHashMap();
      for (int i = 0; i < arrayOfCharSequence2.length; i++)
        this.mVibrateModeMap.put(arrayOfCharSequence2[i], arrayOfCharSequence1[i]);
    }
  }

  private void initializeCheckBox(String paramString, boolean paramBoolean)
  {
    CheckBoxPreference localCheckBoxPreference = (CheckBoxPreference)getPreferenceScreen().findPreference(paramString);
    if (localCheckBoxPreference != null)
      localCheckBoxPreference.setChecked(paramBoolean);
  }

  private void initializeCheckBoxAndSummary(String paramString, boolean paramBoolean, CharSequence paramCharSequence)
  {
    CheckBoxPreference localCheckBoxPreference = (CheckBoxPreference)getPreferenceScreen().findPreference(paramString);
    localCheckBoxPreference.setChecked(paramBoolean);
    localCheckBoxPreference.setSummary(paramCharSequence);
  }

  private void initializeEditText(String paramString1, String paramString2)
  {
    ((EditTextPreference)getPreferenceScreen().findPreference(paramString1)).setText(paramString2);
  }

  private void initializeList(String paramString1, String paramString2)
  {
    ((ListPreference)getPreferenceScreen().findPreference(paramString1)).setValue(paramString2);
  }

  private void initializeListAndSummary(String paramString1, String paramString2, CharSequence paramCharSequence)
  {
    ListPreference localListPreference = (ListPreference)getPreferenceScreen().findPreference(paramString1);
    localListPreference.setValue(paramString2);
    localListPreference.setSummary(paramCharSequence);
  }

  private void setNotificationSettingState(Persistence paramPersistence)
  {
    if (paramPersistence.getPriorityInboxDefault(this));
    for (int i = 2131296382; ; i = 2131296381)
    {
      String str = getString(i);
      initializeCheckBoxAndSummary("enable-notifications", paramPersistence.getEnableNotifications(this), str);
      return;
    }
  }

  private void showOrHideWipPreference(boolean paramBoolean)
  {
    if (paramBoolean)
      addPreferencesFromResource(2131034115);
    PreferenceScreen localPreferenceScreen;
    Preference localPreference;
    do
    {
      return;
      localPreferenceScreen = getPreferenceScreen();
      localPreference = localPreferenceScreen.findPreference("work-in-progress");
    }
    while (localPreference == null);
    localPreferenceScreen.removePreference(localPreference);
  }

  public void onCreate(Bundle paramBundle)
  {
    requestWindowFeature(7);
    super.onCreate(paramBundle);
    getWindow().setFeatureInt(7, 2130903054);
    findViewById(2131361845).setVisibility(0);
    getPreferenceManager().setSharedPreferencesName("Gmail");
    if (Utils.getPriorityInboxServerEnabled(Persistence.getInstance(this).getActiveAccount(this)));
    for (int i = 2131034116; ; i = 2131034114)
    {
      addPreferencesFromResource(i);
      return;
    }
  }

  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    Context localContext = paramPreference.getContext();
    Persistence localPersistence = Persistence.getInstance(localContext);
    if ("ringtone".equals(paramPreference.getKey()))
    {
      localPersistence.setRingtone(localContext, paramObject.toString());
      return true;
    }
    if ("signature".equals(paramPreference.getKey()))
    {
      String str3 = paramObject.toString();
      if ("wip".equalsIgnoreCase(str3))
      {
        if (!localPersistence.getWorkInProgress(localContext));
        for (boolean bool = true; ; bool = false)
        {
          showOrHideWipPreference(bool);
          Persistence.getInstance(this).setWorkInProgress(this, bool);
          return false;
        }
      }
      localPersistence.setSignature(localContext, str3);
      return true;
    }
    if ("confirm-actions".equals(paramPreference.getKey()))
    {
      localPersistence.setConfirmActions(localContext, paramObject.toString());
      return true;
    }
    if ("auto-advance".equals(paramPreference.getKey()))
    {
      localPersistence.setAutoAdvanceMode(localContext, paramObject.toString());
      return true;
    }
    if ("message-text".equals(paramPreference.getKey()))
    {
      String str2 = paramObject.toString();
      localPersistence.setMessageTextSize(localContext, str2);
      ((ListPreference)getPreferenceScreen().findPreference("message-text")).setSummary(getHumanReadableTextSizeName(str2));
      return true;
    }
    if ("vibrateWhen".equals(paramPreference.getKey()))
    {
      String str1 = paramObject.toString();
      localPersistence.setVibrateWhen(localContext, str1);
      ((ListPreference)getPreferenceScreen().findPreference("vibrateWhen")).setSummary(getHumanReadableVibrateModeName(str1));
      return true;
    }
    return false;
  }

  public boolean onPreferenceTreeClick(PreferenceScreen paramPreferenceScreen, Preference paramPreference)
  {
    String str = paramPreference.getKey();
    if (str == null)
      return false;
    boolean bool = true;
    Persistence localPersistence = Persistence.getInstance(this);
    if (str.equals("clear-search-history"))
      ((GmailApplication)getApplication()).getRecentSuggestions().clearHistory();
    while (true)
    {
      return bool;
      if (str.equals("allow-batch"))
      {
        localPersistence.setAllowBatch(this, ((CheckBoxPreference)paramPreferenceScreen.findPreference("allow-batch")).isChecked());
      }
      else if (str.equals("unobtrusive"))
      {
        localPersistence.setUnobtrusive(this, ((CheckBoxPreference)paramPreferenceScreen.findPreference("unobtrusive")).isChecked());
      }
      else if (str.equals("enable-notifications"))
      {
        localPersistence.setEnableNotifications(this, ((CheckBoxPreference)paramPreferenceScreen.findPreference("enable-notifications")).isChecked());
      }
      else if (str.equals("action-strip-action-reply-all"))
      {
        localPersistence.setActionStripActionReplyAll(this, ((CheckBoxPreference)paramPreferenceScreen.findPreference("action-strip-action-reply-all")).isChecked());
      }
      else if (str.equals("priority-inbox"))
      {
        localPersistence.setPriorityInboxDefault(this, ((CheckBoxPreference)paramPreferenceScreen.findPreference("priority-inbox")).isChecked());
        setNotificationSettingState(localPersistence);
      }
      else
      {
        bool = false;
      }
    }
  }

  public void onResume()
  {
    super.onResume();
    Utils.setTitleWithAccount(this, getString(2131296418), Persistence.getInstance(this).getActiveAccount(this));
    showOrHideWipPreference(Persistence.getInstance(this).getWorkInProgress(this));
    Persistence localPersistence = Persistence.getInstance(this);
    initializeList("confirm-actions", localPersistence.getConfirmActions(this));
    initializeCheckBox("allow-batch", localPersistence.getAllowBatch(this));
    initializeCheckBox("unobtrusive", localPersistence.getUnobtrusive(this));
    initializeEditText("signature", localPersistence.getSignature(this));
    initializeList("auto-advance", localPersistence.getAutoAdvanceMode(this));
    setNotificationSettingState(localPersistence);
    initializeCheckBox("priority-inbox", localPersistence.getPriorityInboxDefault(this));
    String str1 = localPersistence.getMessageTextSize(this);
    initializeListAndSummary("message-text", str1, getHumanReadableTextSizeName(str1));
    String str2 = localPersistence.getVibrateWhen(this);
    initializeListAndSummary("vibrateWhen", str2, getHumanReadableVibrateModeName(str2));
    initializeCheckBox("action-strip-action-reply-all", localPersistence.getActionStripActionReplyAll(this));
    SharedPreferences.Editor localEditor = Persistence.getPreferences(this).edit();
    localEditor.putString("ringtone", localPersistence.getRingtone(this));
    localEditor.putString("signature", localPersistence.getSignature(this));
    localEditor.putString("confirm-actions", localPersistence.getConfirmActions(this));
    localEditor.putString("auto-advance", localPersistence.getAutoAdvanceMode(this));
    localEditor.putString("message-text", localPersistence.getMessageTextSize(this));
    localEditor.putString("vibrateWhen", localPersistence.getVibrateWhen(this));
    SharedPreferencesCompat.apply(localEditor);
    PreferenceScreen localPreferenceScreen = getPreferenceScreen();
    localPreferenceScreen.findPreference("signature").setOnPreferenceChangeListener(this);
    localPreferenceScreen.findPreference("ringtone").setOnPreferenceChangeListener(this);
    localPreferenceScreen.findPreference("confirm-actions").setOnPreferenceChangeListener(this);
    localPreferenceScreen.findPreference("auto-advance").setOnPreferenceChangeListener(this);
    localPreferenceScreen.findPreference("message-text").setOnPreferenceChangeListener(this);
    localPreferenceScreen.findPreference("vibrateWhen").setOnPreferenceChangeListener(this);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.PreferenceActivity
 * JD-Core Version:    0.6.2
 */