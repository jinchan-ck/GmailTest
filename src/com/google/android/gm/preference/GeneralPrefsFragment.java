package com.google.android.gm.preference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.SearchRecentSuggestions;
import android.widget.Toast;
import com.android.mail.utils.Utils;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.provider.UiProvider;
import java.util.HashSet;
import java.util.Set;

public final class GeneralPrefsFragment extends GmailPreferenceFragment
  implements DialogInterface.OnClickListener
{
  private AlertDialog mClearDisplayImagesDialog;
  private AlertDialog mClearSearchHistoryDialog;

  private void clearDisplayImages()
  {
    this.mClearDisplayImagesDialog = new AlertDialog.Builder(getActivity()).setMessage(2131427717).setIconAttribute(16843605).setPositiveButton(2131427539, this).setNegativeButton(2131427540, this).show();
  }

  private void clearSearchHistory()
  {
    this.mClearSearchHistoryDialog = new AlertDialog.Builder(getActivity()).setMessage(2131427714).setTitle(2131427713).setIconAttribute(16843605).setPositiveButton(2131427539, this).setNegativeButton(2131427540, this).show();
  }

  private void initializeConfirmActions(Persistence paramPersistence, Context paramContext)
  {
    Set localSet = paramPersistence.getConfirmActions(paramContext);
    initializeCheckBox("confirm-actions-delete", localSet.contains("delete"));
    initializeCheckBox("confirm-actions-archive", localSet.contains("archive"));
    initializeCheckBox("confirm-actions-send", localSet.contains("send"));
  }

  private void listenForPreferenceChange(String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      Preference localPreference = findPreference(paramArrayOfString[j]);
      if (localPreference != null)
        localPreference.setOnPreferenceChangeListener(this);
    }
  }

  private void updateConfirmActions(String paramString, Boolean paramBoolean)
  {
    Activity localActivity = getActivity();
    Persistence localPersistence = Persistence.getInstance();
    Set localSet = localPersistence.getConfirmActions(localActivity);
    if (paramBoolean.booleanValue())
    {
      localSet.remove("none");
      localSet.add(paramString);
    }
    while (true)
    {
      localPersistence.setConfirmActions(localActivity, localSet);
      return;
      localSet.remove(paramString);
      if (localSet.size() == 0)
        localSet.add("none");
    }
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if (paramDialogInterface.equals(this.mClearSearchHistoryDialog))
      if (paramInt == -1)
      {
        new AsyncTask()
        {
          protected Void doInBackground(Void[] paramAnonymousArrayOfVoid)
          {
            String str = this.val$context.getString(2131427346);
            new SearchRecentSuggestions(this.val$context, str, 1).clearHistory();
            return null;
          }
        }
        .execute(new Void[0]);
        Toast.makeText(getActivity(), 2131427807, 0).show();
      }
    while ((!paramDialogInterface.equals(this.mClearDisplayImagesDialog)) || (paramInt != -1))
      return;
    Persistence localPersistence = Persistence.getInstance();
    Activity localActivity = getActivity();
    localPersistence.setString(localActivity, null, "display_images", "");
    localPersistence.setStringSet(localActivity, null, "display_sender_images_patterns_set", new HashSet());
    Toast.makeText(getActivity(), 2131427808, 0).show();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getPreferenceManager().setSharedPreferencesName(Persistence.getInstance().getSharedPreferencesName());
    addPreferencesFromResource(2131099651);
    if (Utils.useTabletUI(getActivity()))
    {
      PreferenceScreen localPreferenceScreen = getPreferenceScreen();
      localPreferenceScreen.removePreference(findPreference("snap-headers"));
      localPreferenceScreen.removePreference(findPreference("action-strip-action-reply-all"));
      localPreferenceScreen.removePreference(findPreference("hide-checkboxes"));
    }
    getPreferenceScreen().removePreference(findPreference("message-text-key"));
  }

  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    if (getActivity() == null)
      return false;
    boolean bool = true;
    Context localContext = paramPreference.getContext();
    Persistence localPersistence = Persistence.getInstance();
    String str = paramPreference.getKey();
    if ("confirm-actions-delete".equals(str))
      updateConfirmActions("delete", (Boolean)paramObject);
    while (true)
    {
      UiProvider.notifyAccountListChanged(getActivity());
      return bool;
      if ("confirm-actions-archive".equals(str))
        updateConfirmActions("archive", (Boolean)paramObject);
      else if ("confirm-actions-send".equals(str))
        updateConfirmActions("send", (Boolean)paramObject);
      else if ("swipe-key".equals(str))
        localPersistence.setSwipe(localContext, paramObject.toString());
      else if ("action-strip-action-reply-all".equals(str))
        localPersistence.setActionStripActionReplyAll(localContext, ((Boolean)paramObject).booleanValue());
      else if ("conversation-mode".equals(str))
        localPersistence.setConversationOverviewMode(localContext, ((Boolean)paramObject).booleanValue());
      else if ("auto-advance-key".equals(str))
        localPersistence.setAutoAdvanceMode(localContext, paramObject.toString());
      else if ("message-text-key".equals(str))
        localPersistence.setMessageTextSize(localContext, paramObject.toString());
      else if ("hide-checkboxes".equals(str))
        localPersistence.setBoolean(localContext, null, str, (Boolean)paramObject);
      else if ("snap-headers".equals(str))
        localPersistence.setSnapHeaderMode(localContext, paramObject.toString());
      else if ("display_images".equals(str))
        localPersistence.setBoolean(localContext, null, str, (Boolean)paramObject);
      else
        bool = false;
    }
  }

  public boolean onPreferenceTreeClick(PreferenceScreen paramPreferenceScreen, Preference paramPreference)
  {
    if (getActivity() == null);
    String str;
    do
    {
      return false;
      str = paramPreference.getKey();
    }
    while (str == null);
    if (str.equals("clear-search-history"))
    {
      clearSearchHistory();
      return true;
    }
    if (str.equals("clear-display-images-whitelist"))
    {
      clearDisplayImages();
      return true;
    }
    return false;
  }

  public void onResume()
  {
    super.onResume();
    Activity localActivity = getActivity();
    Persistence localPersistence = Persistence.getInstance();
    initializeConfirmActions(localPersistence, localActivity);
    ((FancySummaryListPreference)findPreference("auto-advance-key")).setValue(localPersistence.getAutoAdvanceMode(localActivity));
    ((FancySummaryListPreference)findPreference("swipe-key")).setValue(localPersistence.getSwipe(localActivity));
    FancySummaryListPreference localFancySummaryListPreference = (FancySummaryListPreference)findPreference("message-text-key");
    if (localFancySummaryListPreference != null)
      localFancySummaryListPreference.setValue(localPersistence.getMessageTextSize(localActivity));
    initializeCheckBox("action-strip-action-reply-all", localPersistence.getActionStripActionReplyAll(localActivity));
    listenForPreferenceChange(new String[] { "confirm-actions-delete", "confirm-actions-archive", "confirm-actions-send", "swipe-key", "action-strip-action-reply-all", "auto-advance-key", "message-text-key", "hide-checkboxes", "conversation-mode", "snap-headers", "display_images" });
  }

  public void onStop()
  {
    super.onStop();
    if ((this.mClearSearchHistoryDialog != null) && (this.mClearSearchHistoryDialog.isShowing()))
      this.mClearSearchHistoryDialog.dismiss();
    if ((this.mClearDisplayImagesDialog != null) && (this.mClearDisplayImagesDialog.isShowing()))
      this.mClearDisplayImagesDialog.dismiss();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.preference.GeneralPrefsFragment
 * JD-Core Version:    0.6.2
 */