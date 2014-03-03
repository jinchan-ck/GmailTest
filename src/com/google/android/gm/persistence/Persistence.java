package com.google.android.gm.persistence;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings.System;
import android.text.TextUtils;
import com.google.android.common.base.Strings;
import com.google.android.gm.SharedPreference;
import com.google.android.gm.provider.LogUtils;
import com.google.android.gm.provider.MailEngine;
import com.google.android.gm.provider.MailEngine.Preferences;
import com.google.android.gsf.Gservices;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;

public class Persistence
{
  protected static final String ALLOW_BATCH = "allow-batch";
  private static final Set<String> BACKUP_KEYS;
  static final String CONFIRM_ACTIONS_ICS = "confirm-actions-key";
  static final String CONFIRM_ACTIONS_PRE_ICS = "confirm-actions";
  public static int LABEL_NOTIFICATION_ON = 0;
  public static int LABEL_NOTIFICATION_ONCE = 0;
  public static int LABEL_NOTIFICATION_RINGTONE = 0;
  public static int LABEL_NOTIFICATION_VIBRATE = 0;
  public static int LABEL_SYNCHRONIZATION = 0;
  static final String PRIORITY_INBOX_PRE_ICS_KEY = "priority-inbox";
  private static String SEPERATOR;
  static int UR4_LABEL_NOTIFICATION_ON;
  static int UR4_LABEL_NOTIFICATION_ONCE;
  static int UR4_LABEL_NOTIFICATION_RINGTONE;
  static int UR4_LABEL_NOTIFICATION_VIBRATE;
  private static Persistence mInstance;
  private static String sAlwaysVibrateSetting;
  private static Boolean sCanVibrate;
  private static int sLastGserviceSocialNetworkPatternsHashCode = -1;
  private static final List<Pattern> sSocialNetworkSenderPatterns;
  private static Map<String, String> sVibrateSettingConversionMap;
  private static String sVibrateWhenSilentSetting;
  final AutoAdvanceAccountToGlobalMigrator mAutoAdvanceMigrator = new AutoAdvanceAccountToGlobalMigrator();
  final ConfirmActionsAccountToGlobalMigrator mConfirmActionsMigrator = new ConfirmActionsAccountToGlobalMigrator();
  final DefaultReplyAllMigrator mDefaultReplyAllMigrator = new DefaultReplyAllMigrator();
  final MessageTextSizeAccountToGlobalMigrator mMessageTextSizeMigrator = new MessageTextSizeAccountToGlobalMigrator();

  static
  {
    LABEL_NOTIFICATION_ON = 1;
    LABEL_NOTIFICATION_RINGTONE = 2;
    LABEL_NOTIFICATION_VIBRATE = 3;
    LABEL_NOTIFICATION_ONCE = 4;
    BACKUP_KEYS = ImmutableSet.of("enable-notifications", "signature", "ringtone", "vibrateWhen", "unobtrusive", "auto-advance-key", new String[] { "swipe-key", "message-text-key", "prefetch-attachments", "conversation-mode", "action-strip-action-reply-all", "snap-headers", "priority-inbox-key", "display_images", "display_sender_images_patterns_set", "notification_labels", "hide-checkboxes", "confirm-delete", "archive", "delete", "send", "confirm-actions-key", "signature-key", "vibrate" });
    mInstance = null;
    sCanVibrate = null;
    SEPERATOR = " ";
    UR4_LABEL_NOTIFICATION_ON = 0;
    UR4_LABEL_NOTIFICATION_RINGTONE = 1;
    UR4_LABEL_NOTIFICATION_VIBRATE = 2;
    UR4_LABEL_NOTIFICATION_ONCE = 3;
    sSocialNetworkSenderPatterns = Lists.newArrayList();
    sSocialNetworkSenderPatterns.add(Pattern.compile("noreply\\-\\w+@plus\\.google\\.com"));
    sSocialNetworkSenderPatterns.add(Pattern.compile("[0-9a-fA-F]+@plus\\.google\\.com"));
  }

  private void cacheUseInfoOverloadArrowsSetting(Context paramContext, String paramString, boolean paramBoolean)
  {
    setBoolean(paramContext, paramString, "cache-use-priority-markers", Boolean.valueOf(paramBoolean));
  }

  private boolean canBackupRestore(String paramString)
  {
    if (paramString == null)
      return false;
    int i = paramString.indexOf('^');
    if (i >= 0)
      paramString = paramString.substring(0, i);
    return BACKUP_KEYS.contains(paramString);
  }

  private boolean containsKey(Context paramContext, String paramString1, String paramString2)
  {
    return getPreferences(paramContext).contains(makeKey(paramString1, paramString2));
  }

  private Set<String> convertUR4NotificationLabelInformation(Set<String> paramSet)
  {
    HashSet localHashSet = Sets.newHashSet();
    localHashSet.add(LABEL_NOTIFICATION_ON + extract(paramSet, UR4_LABEL_NOTIFICATION_ON));
    localHashSet.add(LABEL_NOTIFICATION_RINGTONE + extract(paramSet, UR4_LABEL_NOTIFICATION_RINGTONE));
    localHashSet.add(LABEL_NOTIFICATION_VIBRATE + extract(paramSet, UR4_LABEL_NOTIFICATION_VIBRATE));
    localHashSet.add(LABEL_NOTIFICATION_ONCE + extract(paramSet, UR4_LABEL_NOTIFICATION_ONCE));
    return localHashSet;
  }

  public static String extract(Set<String> paramSet, int paramInt)
  {
    String str1 = Integer.toString(paramInt);
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      String str2 = (String)localIterator.next();
      if (str2.startsWith(str1))
        return str2.substring(1);
    }
    return null;
  }

  private static Set<String> extractSet(String paramString1, String paramString2, boolean paramBoolean)
  {
    if ((paramString1 == null) || (paramString1.equals("")))
    {
      localObject = null;
      return localObject;
    }
    String[] arrayOfString = TextUtils.split(paramString1, paramString2);
    Object localObject = new HashSet();
    int i = arrayOfString.length;
    int j = 0;
    label41: String str1;
    if (j < i)
    {
      str1 = arrayOfString[j];
      if (!paramBoolean)
        break label81;
    }
    label81: for (String str2 = Uri.decode(str1); ; str2 = str1)
    {
      ((Set)localObject).add(str2);
      j++;
      break label41;
      break;
    }
  }

  public static String extractVibrateSetting(Context paramContext, Set<String> paramSet)
  {
    getInstance().initializeVibrationState(paramContext);
    String str1 = extract(paramSet, LABEL_NOTIFICATION_VIBRATE);
    if (str1 != null);
    for (String str2 = (String)sVibrateSettingConversionMap.get(str1); str2 != null; str2 = null)
      return str2;
    return str1;
  }

  public static String getAccountInbox(Context paramContext, String paramString)
  {
    if (getInstance().getPriorityInboxDefault(paramContext, paramString))
      return "^iim";
    return "^i";
  }

  private boolean getBoolean(Context paramContext, String paramString1, String paramString2, boolean paramBoolean)
  {
    return getPreferences(paramContext).getBoolean(makeKey(paramString1, paramString2), paramBoolean);
  }

  private boolean getCachedInfoOverloadArrowsSetting(Context paramContext, String paramString)
  {
    return getBoolean(paramContext, paramString, "cache-use-priority-markers", false);
  }

  private Boolean getInfoOverloadArrowsEnabled(String paramString)
  {
    MailEngine localMailEngine = MailEngine.getMailEngine(paramString);
    if (localMailEngine == null)
      return null;
    return Boolean.valueOf(localMailEngine.getServerArrowsEnabled());
  }

  public static Persistence getInstance()
  {
    if (mInstance == null)
      mInstance = new Persistence();
    return mInstance;
  }

  public static MailEnginePreferences getMailEnginePreferences(Context paramContext)
  {
    return new MailEnginePreferences(paramContext);
  }

  private static String getRingtoneTitle(Context paramContext, String paramString)
  {
    if (paramString.length() == 0);
    while (RingtoneManager.isDefault(Uri.parse(paramString)))
      return paramString;
    RingtoneManager localRingtoneManager = new RingtoneManager(paramContext);
    localRingtoneManager.setType(2);
    Cursor localCursor = localRingtoneManager.getCursor();
    try
    {
      while (localCursor.moveToNext())
        if (ContentUris.withAppendedId(Uri.parse(localCursor.getString(2)), localCursor.getLong(0)).toString().equals(paramString))
        {
          String str = localCursor.getString(1);
          boolean bool = Strings.isNullOrEmpty(str);
          if (!bool)
            return str;
        }
      return null;
    }
    finally
    {
      localCursor.close();
    }
  }

  private static String getRingtoneUri(Context paramContext, String paramString)
  {
    if ((paramString.length() == 0) || (RingtoneManager.isDefault(Uri.parse(paramString))))
      return paramString;
    RingtoneManager localRingtoneManager = new RingtoneManager(paramContext);
    localRingtoneManager.setType(2);
    Cursor localCursor = localRingtoneManager.getCursor();
    try
    {
      while (localCursor.moveToNext())
        if (paramString.equals(localCursor.getString(1)))
        {
          String str = ContentUris.withAppendedId(Uri.parse(localCursor.getString(2)), localCursor.getLong(0)).toString();
          return str;
        }
      return null;
    }
    finally
    {
      localCursor.close();
    }
  }

  private Set<String> getSenderWhitelist(Context paramContext)
  {
    HashSet localHashSet = new HashSet();
    String str = getString(paramContext, null, "display_images", "");
    if ((str != null) && (str.length() > 0))
      try
      {
        JSONArray localJSONArray = new JSONArray(str);
        int i = 0;
        int j = localJSONArray.length();
        while (i < j)
        {
          localHashSet.add((String)localJSONArray.get(i));
          i++;
        }
      }
      catch (JSONException localJSONException)
      {
        setString(paramContext, null, "display_images", "");
      }
    return localHashSet;
  }

  private String getStoredAutoAdvanceMode(Context paramContext)
  {
    String str = getString(paramContext, null, "auto-advance-key", null);
    if (str == null)
      str = (String)this.mAutoAdvanceMigrator.migrate(paramContext);
    if (str == null)
      str = getString(paramContext, null, "auto-advance", null);
    return str;
  }

  private String getString(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    if (paramString1 != null)
      return getPreferences(paramContext).getString(makeKey(paramString1, paramString2), paramString3);
    return getPreferences(paramContext).getString(paramString2, paramString3);
  }

  private String getString(Context paramContext, String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramBoolean)
      return getString(paramContext, getActiveAccount(paramContext), paramString1, paramString2);
    return getPreferences(paramContext).getString(paramString1, paramString2);
  }

  private boolean getUnobtrusive(Context paramContext, String paramString)
  {
    return getBoolean(paramContext, paramString, "unobtrusive", true);
  }

  private void initializeVibrationState(Context paramContext)
  {
    try
    {
      if ((sCanVibrate == null) || (sVibrateSettingConversionMap == null))
      {
        Vibrator localVibrator = (Vibrator)paramContext.getSystemService("vibrator");
        if ((localVibrator == null) || (!localVibrator.hasVibrator()))
          break label204;
      }
      label204: for (boolean bool = true; ; bool = false)
      {
        sCanVibrate = Boolean.valueOf(bool);
        Resources localResources = paramContext.getResources();
        if (bool)
        {
          sAlwaysVibrateSetting = localResources.getString(2131427737);
          sVibrateWhenSilentSetting = localResources.getString(2131427739);
        }
        String str1 = localResources.getString(2131427737);
        String str2 = localResources.getString(2131427738);
        String str3 = localResources.getString(2131427739);
        ImmutableMap.Builder localBuilder = ImmutableMap.builder();
        localBuilder.put(str1, str1);
        localBuilder.put(str2, str2);
        localBuilder.put(str3, str3);
        localBuilder.put(localResources.getString(2131427754), str1);
        localBuilder.put(localResources.getString(2131427756), str2);
        localBuilder.put(localResources.getString(2131427755), str3);
        sVibrateSettingConversionMap = localBuilder.build();
        return;
      }
    }
    finally
    {
    }
  }

  public static final boolean isNotification(int paramInt)
  {
    return paramInt >= LABEL_NOTIFICATION_ON;
  }

  public static final boolean isNotificationSubChoice(int paramInt)
  {
    return paramInt > LABEL_NOTIFICATION_ON;
  }

  private boolean isPresent(Context paramContext, String paramString1, String paramString2)
  {
    if (paramString1 != null)
      paramString2 = makeKey(paramString1, paramString2);
    return getPreferences(paramContext).contains(paramString2);
  }

  public static boolean isRingtone(String paramString)
  {
    return "ringtone".equals(paramString);
  }

  private void loadSocialNetworkPatterns(Context paramContext)
  {
    String str1 = Gservices.getString(paramContext.getContentResolver(), "gmail_social_network_sender_patterns", null);
    if (str1 != null)
    {
      int i = str1.hashCode();
      if (i != sLastGserviceSocialNetworkPatternsHashCode)
      {
        sLastGserviceSocialNetworkPatternsHashCode = i;
        String[] arrayOfString = TextUtils.split(str1, ";");
        sSocialNetworkSenderPatterns.clear();
        int j = arrayOfString.length;
        for (int k = 0; k < j; k++)
        {
          String str2 = arrayOfString[k];
          sSocialNetworkSenderPatterns.add(Pattern.compile(str2));
        }
      }
    }
  }

  private String makeKey(String paramString1, String paramString2)
  {
    if (paramString1 != null)
      paramString2 = paramString1 + "-" + paramString2;
    return paramString2;
  }

  private String migrateVibrateValue(Context paramContext, String paramString)
  {
    if (getBoolean(paramContext, paramString, "vibrate", false))
      return paramContext.getResources().getString(2131427737);
    return paramContext.getResources().getString(2131427739);
  }

  private void notifyBackupDataChanged(String paramString)
  {
    if (canBackupRestore(paramString))
      GmailBackupAgent.dataChanged("Shared preferences");
  }

  private void remove(Context paramContext, String paramString)
  {
    SharedPreferences.Editor localEditor = getPreferences(paramContext).edit();
    localEditor.remove(paramString);
    localEditor.apply();
    notifyBackupDataChanged(paramString);
  }

  private void setHideCheckboxes(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, null, "hide-checkboxes", Boolean.valueOf(paramBoolean));
  }

  public void addNotificationLabel(Context paramContext, String paramString1, String paramString2, Set<String> paramSet)
  {
    setStringSet(paramContext, paramString1, "notification_labels" + paramString2, paramSet);
  }

  public void cacheActiveNotificationSet(Context paramContext, Set<String> paramSet)
  {
    setStringSet(paramContext, "cache-active-notification-set", null, paramSet);
  }

  public void cacheConfiguredGoogleAccounts(Context paramContext, boolean paramBoolean, Iterable<String> paramIterable)
  {
    if (paramBoolean);
    for (String str = "cache-google-accounts"; ; str = "cache-google-accounts-synced")
    {
      setString(paramContext, null, str, TextUtils.join(" ", paramIterable));
      return;
    }
  }

  public void clearNotificationLabel(Context paramContext, String paramString1, String paramString2)
  {
    remove(paramContext, paramString1, "notification_labels" + paramString2);
  }

  public boolean fullTextSearchEnabled(Context paramContext)
  {
    return true;
  }

  public boolean getActionStripActionReplyAll(Context paramContext)
  {
    if (isPresent(paramContext, null, "action-strip-action-reply-all"))
      return getBoolean(paramContext, null, "action-strip-action-reply-all", false);
    Boolean localBoolean = (Boolean)this.mDefaultReplyAllMigrator.migrate(paramContext);
    if (localBoolean == null)
      localBoolean = Boolean.valueOf(paramContext.getResources().getBoolean(2131623941));
    return localBoolean.booleanValue();
  }

  public String getActiveAccount(Context paramContext)
  {
    return getString(paramContext, null, "active-account", null);
  }

  public Set<String> getActiveNotificationSet(Context paramContext)
  {
    try
    {
      Set localSet = getStringSet(paramContext, "cache-active-notification-set", null, null);
      return localSet;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    return null;
  }

  public String getAutoAdvanceMode(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    String str = getStoredAutoAdvanceMode(paramContext);
    if (str == null)
      str = localResources.getString(2131427600);
    return str;
  }

  public boolean getAutoAdvanceModeList(Context paramContext)
  {
    return "list".equals(getAutoAdvanceMode(paramContext));
  }

  public boolean getAutoAdvanceModeNewer(Context paramContext)
  {
    return "newer".equals(getAutoAdvanceMode(paramContext));
  }

  public boolean getAutoAdvanceModeOlder(Context paramContext)
  {
    return "older".equals(getAutoAdvanceMode(paramContext));
  }

  public List<SharedPreference> getBackupPreferences(Context paramContext)
  {
    ArrayList localArrayList = Lists.newArrayList();
    SharedPreferences localSharedPreferences = getPreferences(paramContext);
    Iterator localIterator1 = localSharedPreferences.getAll().entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      String str1 = (String)localEntry.getKey();
      String str3;
      String str2;
      label109: Object localObject;
      if (str1.indexOf('@') >= 0)
      {
        int i = str1.indexOf('-', str1.lastIndexOf(46));
        str3 = str1.substring(0, i);
        str2 = str1.substring(i + 1);
        if (canBackupRestore(str2))
        {
          localObject = localEntry.getValue();
          if (!"ringtone".equals(str2))
            break label197;
          String str7 = getRingtoneTitle(paramContext, (String)localEntry.getValue());
          if (str7 == null)
            continue;
          localObject = str7;
        }
      }
      else
      {
        while (true)
        {
          SharedPreference localSharedPreference = new SharedPreference(str2, str3, localObject);
          localArrayList.add(localSharedPreference);
          break;
          str2 = str1;
          str3 = null;
          break label109;
          break;
          label197: if (str2.startsWith("notification_labels"))
          {
            Set localSet = localSharedPreferences.getStringSet(str1, null);
            if (localSet == null)
              break;
            HashSet localHashSet = Sets.newHashSet();
            String str4 = Integer.toString(LABEL_NOTIFICATION_RINGTONE);
            Iterator localIterator2 = localSet.iterator();
            while (localIterator2.hasNext())
            {
              String str5 = (String)localIterator2.next();
              if (str5.startsWith(str4))
              {
                String str6 = getRingtoneTitle(paramContext, str5.substring(1));
                if (str6 == null)
                  str6 = RingtoneManager.getDefaultUri(2).toString();
                localHashSet.add(LABEL_NOTIFICATION_RINGTONE + str6);
              }
              else
              {
                localHashSet.add(str5);
              }
            }
            localObject = localHashSet;
          }
        }
      }
    }
    return localArrayList;
  }

  public List<String> getCachedConfiguredGoogleAccounts(Context paramContext, boolean paramBoolean)
  {
    if (paramBoolean);
    for (String str = "cache-google-accounts"; ; str = "cache-google-accounts-synced")
      return ImmutableList.copyOf(TextUtils.split(getString(paramContext, null, str, ""), " "));
  }

  public Set<String> getConfirmActions(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    String str = getString(paramContext, null, "confirm-actions-key", null);
    if (str == null)
      str = (String)this.mConfirmActionsMigrator.migrate(paramContext);
    if (str == null)
      str = getString(paramContext, null, "confirm-actions", null);
    if (str == null)
      str = localResources.getString(2131427725);
    return Sets.newHashSet(TextUtils.split(str, ","));
  }

  public boolean getConfirmArchive(Context paramContext)
  {
    return getConfirmActions(paramContext).contains("archive");
  }

  public boolean getConfirmDelete(Context paramContext)
  {
    return getConfirmActions(paramContext).contains("delete");
  }

  public boolean getConfirmSend(Context paramContext)
  {
    return getConfirmActions(paramContext).contains("send");
  }

  public boolean getConversationOverviewMode(Context paramContext)
  {
    return getBoolean(paramContext, null, "conversation-mode", false);
  }

  public boolean getDisplayImagesFromSender(Context paramContext, String paramString)
  {
    boolean bool = getSenderWhitelist(paramContext).contains(paramString);
    Set localSet1;
    if (!bool)
      localSet1 = Collections.emptySet();
    try
    {
      Set localSet3 = getStringSet(paramContext, null, "display_sender_images_patterns_set", localSet1);
      localSet2 = localSet3;
      Iterator localIterator = localSet2.iterator();
      do
      {
        if (!localIterator.hasNext())
          break;
        bool = Pattern.compile((String)localIterator.next()).matcher(paramString).matches();
      }
      while (!bool);
      return bool;
    }
    catch (ClassCastException localClassCastException)
    {
      while (true)
      {
        LogUtils.e("Gmail", localClassCastException, "Error retrieving previously saved senders pattern", new Object[0]);
        remove(paramContext, null, "display_sender_images_patterns_set");
        Set localSet2 = localSet1;
      }
    }
  }

  public boolean getEnableNotifications(Context paramContext, String paramString)
  {
    return getBoolean(paramContext, paramString, "enable-notifications", true);
  }

  public boolean getHasUserSetAutoAdvanceSetting(Context paramContext)
  {
    return getStoredAutoAdvanceMode(paramContext) != null;
  }

  public boolean getHideCheckboxes(Context paramContext)
  {
    boolean bool = true;
    if (!getPreferences(paramContext).contains("hide-checkboxes"))
    {
      if (!getPreferences(paramContext).contains("allow-batch"))
        break label69;
      if (getPreferences(paramContext).getBoolean("allow-batch", bool))
        break label64;
      setHideCheckboxes(paramContext, bool);
    }
    while (true)
    {
      return getBoolean(paramContext, null, "hide-checkboxes", false);
      label64: bool = false;
      break;
      label69: setHideCheckboxes(paramContext, false);
    }
  }

  public String getMessageTextSize(Context paramContext)
  {
    String str = getString(paramContext, null, "message-text-key", null);
    if (str == null)
      str = (String)this.mMessageTextSizeMigrator.migrate(paramContext);
    if (str == null)
      str = getString(paramContext, null, "message-text", null);
    if (str == null)
      str = paramContext.getResources().getString(2131427730);
    return str;
  }

  public Set<String> getNotificationLabelInformation(Context paramContext, String paramString1, String paramString2)
  {
    boolean bool1 = getPriorityInboxDefault(paramContext, paramString1);
    HashSet localHashSet;
    if ((bool1) || (!paramString2.equals("^i")))
    {
      localHashSet = null;
      if (bool1)
      {
        boolean bool2 = paramString2.equals("^iim");
        localHashSet = null;
        if (!bool2);
      }
    }
    else
    {
      localHashSet = Sets.newHashSet();
      localHashSet.add(LABEL_NOTIFICATION_ON + Boolean.toString(true));
      localHashSet.add(LABEL_NOTIFICATION_RINGTONE + getRingtone(paramContext, paramString1));
      localHashSet.add(LABEL_NOTIFICATION_VIBRATE + getVibrateWhen(paramContext, paramString1));
      localHashSet.add(LABEL_NOTIFICATION_ONCE + Boolean.toString(getUnobtrusive(paramContext, paramString1)));
    }
    return getNotificationLabelInformation(paramContext, paramString1, paramString2, localHashSet);
  }

  public Set<String> getNotificationLabelInformation(Context paramContext, String paramString1, String paramString2, Set<String> paramSet)
  {
    String str = "notification_labels" + paramString2;
    try
    {
      Set localSet2 = getStringSet(paramContext, paramString1, str, paramSet);
      paramSet = localSet2;
      return paramSet;
    }
    catch (ClassCastException localClassCastException)
    {
      Set localSet1;
      do
        localSet1 = extractSet(getString(paramContext, paramString1, str, null), SEPERATOR, false);
      while (localSet1 == null);
      return convertUR4NotificationLabelInformation(localSet1);
    }
  }

  public Uri getNotificationRingtoneUriForLabel(Context paramContext, String paramString1, String paramString2)
  {
    Set localSet = getNotificationLabelInformation(paramContext, paramString1, paramString2);
    Uri localUri = null;
    if (localSet != null)
    {
      String str = extract(localSet, LABEL_NOTIFICATION_RINGTONE);
      boolean bool = TextUtils.isEmpty(str);
      localUri = null;
      if (!bool)
        localUri = Uri.parse(str);
    }
    return localUri;
  }

  public SharedPreferences getPreferences(Context paramContext)
  {
    return paramContext.getSharedPreferences(getSharedPreferencesName(), 0);
  }

  public boolean getPrefetchAttachments(Context paramContext, String paramString)
  {
    return getBoolean(paramContext, paramString, "prefetch-attachments", true);
  }

  public boolean getPriorityInboxArrowsEnabled(Context paramContext, String paramString)
  {
    Boolean localBoolean = getInfoOverloadArrowsEnabled(paramString);
    boolean bool1 = getCachedInfoOverloadArrowsSetting(paramContext, paramString);
    if (localBoolean != null);
    for (boolean bool2 = localBoolean.booleanValue(); ; bool2 = bool1)
    {
      if (bool1 != bool2)
        cacheUseInfoOverloadArrowsSetting(paramContext, paramString, bool2);
      return bool2;
    }
  }

  public boolean getPriorityInboxDefault(Context paramContext, String paramString)
  {
    if (containsKey(paramContext, paramString, "priority-inbox-key"));
    for (String str = "priority-inbox-key"; ; str = "priority-inbox")
      return getBoolean(paramContext, paramString, str, false);
  }

  public String getRingtone(Context paramContext, String paramString)
  {
    String str = getString(paramContext, paramString, "ringtone", null);
    if (str == null)
      str = getString(paramContext, null, "ringtone", null);
    if (str == null)
      str = Settings.System.DEFAULT_NOTIFICATION_URI.toString();
    return str;
  }

  public String getSharedPreferencesName()
  {
    return "Gmail";
  }

  public String getSignature(Context paramContext, String paramString)
  {
    String str = getString(paramContext, paramString, "signature", null);
    if (str == null)
      str = getString(paramContext, null, "signature-key", null);
    if (str == null)
      str = "";
    return str;
  }

  public String getSnapHeaderMode(Context paramContext)
  {
    return getString(paramContext, null, "snap-headers", paramContext.getResources().getString(2131427604));
  }

  public Set<String> getStringSet(Context paramContext, String paramString1, String paramString2, Set<String> paramSet)
  {
    return getPreferences(paramContext).getStringSet(makeKey(paramString1, paramString2), paramSet);
  }

  public String getSwipe(Context paramContext)
  {
    return getString(paramContext, "swipe-key", "archive", false);
  }

  public int getSwipeIntegerPreference(Context paramContext)
  {
    String str = getString(paramContext, null, "swipe-key", null);
    if ("archive".equals(str));
    do
    {
      return 0;
      if ("delete".equals(str))
        return 1;
    }
    while (!"disabled".equals(str));
    return 2;
  }

  public String getVibrateWhen(Context paramContext, String paramString)
  {
    return getString(paramContext, paramString, "vibrateWhen", migrateVibrateValue(paramContext, paramString));
  }

  public boolean isConversationOverviewModeSet(Context paramContext)
  {
    return isPresent(paramContext, null, "conversation-mode");
  }

  public void remove(Context paramContext, String paramString1, String paramString2)
  {
    remove(paramContext, makeKey(paramString1, paramString2));
  }

  public void removeNotificationLabel(Context paramContext, String paramString1, String paramString2)
  {
    Set localSet = getNotificationLabelInformation(paramContext, paramString1, paramString2);
    if ((localSet != null) && (localSet.contains(LABEL_NOTIFICATION_ON + Boolean.toString(true))))
    {
      localSet.remove(LABEL_NOTIFICATION_ON + Boolean.toString(true));
      localSet.add(LABEL_NOTIFICATION_ON + Boolean.toString(false));
      addNotificationLabel(paramContext, paramString1, paramString2, localSet);
    }
  }

  public void restoreSharedPreferences(Context paramContext, List<SharedPreference> paramList, String paramString)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SharedPreference localSharedPreference = (SharedPreference)localIterator.next();
      Object localObject = localSharedPreference.getValue();
      String str1 = localSharedPreference.getKey();
      if (canBackupRestore(str1))
      {
        String str2 = localSharedPreference.getAccount();
        if ((localObject instanceof Boolean))
        {
          setBoolean(paramContext, str2, str1, (Boolean)localObject);
          LogUtils.v("Gmail", "Restore: %s", new Object[] { localSharedPreference });
        }
        else if ((localObject instanceof String))
        {
          String str5 = (String)localObject;
          if (isRingtone(str1))
          {
            str5 = getRingtoneUri(paramContext, str5);
            if (str5 == null)
              LogUtils.w("Gmail", "Can't restore ringtone (not found)", new Object[0]);
          }
          else
          {
            setString(paramContext, str2, str1, str5);
            LogUtils.v(paramString, "Restore: %s", new Object[] { localSharedPreference });
          }
        }
        else if ((localObject instanceof Set))
        {
          Set localSet = (Set)localObject;
          if (str1.startsWith("notification_labels"))
          {
            String str3 = extract(localSet, LABEL_NOTIFICATION_RINGTONE);
            String str4 = getRingtoneUri(paramContext, str3);
            if (str4 == null)
              str4 = RingtoneManager.getDefaultUri(2).toString();
            localSet.remove(LABEL_NOTIFICATION_RINGTONE + str3);
            localSet.add(LABEL_NOTIFICATION_RINGTONE + str4);
          }
          setStringSet(paramContext, str2, str1, localSet);
        }
        else
        {
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = localObject.getClass();
          LogUtils.e(paramString, "Unknown preference data type: %s", arrayOfObject);
        }
      }
    }
    getPreferences(paramContext).edit().commit();
  }

  public void setActionStripActionReplyAll(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, null, "action-strip-action-reply-all", Boolean.valueOf(paramBoolean));
  }

  public void setActiveAccount(Context paramContext, String paramString)
  {
    setString(paramContext, null, "active-account", paramString);
  }

  public void setAutoAdvanceMode(Context paramContext, String paramString)
  {
    setString(paramContext, null, "auto-advance-key", paramString);
  }

  public void setBoolean(Context paramContext, String paramString1, String paramString2, Boolean paramBoolean)
  {
    SharedPreferences.Editor localEditor = getPreferences(paramContext).edit();
    localEditor.putBoolean(makeKey(paramString1, paramString2), paramBoolean.booleanValue());
    localEditor.apply();
    notifyBackupDataChanged(paramString2);
  }

  public void setConfirmActions(Context paramContext, Set<String> paramSet)
  {
    setString(paramContext, null, "confirm-actions-key", TextUtils.join(",", paramSet));
  }

  public void setConversationOverviewMode(Context paramContext, boolean paramBoolean)
  {
    setBoolean(paramContext, null, "conversation-mode", Boolean.valueOf(paramBoolean));
  }

  public void setDisplayImagesFromSender(Context paramContext, String paramString)
  {
    loadSocialNetworkPatterns(paramContext);
    Iterator localIterator = sSocialNetworkSenderPatterns.iterator();
    while (true)
      if (localIterator.hasNext())
      {
        Pattern localPattern = (Pattern)localIterator.next();
        if (localPattern.matcher(paramString).matches())
        {
          Set localSet2 = getStringSet(paramContext, null, "display_sender_images_patterns_set", new HashSet());
          String str = localPattern.pattern();
          if (!localSet2.contains(str))
          {
            localSet2.add(str);
            setStringSet(paramContext, null, "display_sender_images_patterns_set", localSet2);
          }
        }
      }
    Set localSet1;
    do
    {
      return;
      localSet1 = getSenderWhitelist(paramContext);
    }
    while (localSet1.contains(paramString));
    localSet1.add(paramString);
    setString(paramContext, null, "display_images", new JSONArray(localSet1).toString());
  }

  public void setEnableNotifications(Context paramContext, String paramString, boolean paramBoolean)
  {
    setBoolean(paramContext, paramString, "enable-notifications", Boolean.valueOf(paramBoolean));
  }

  public void setMessageTextSize(Context paramContext, String paramString)
  {
    setString(paramContext, null, "message-text-key", paramString);
  }

  public void setPrefetchAttachments(Context paramContext, String paramString, boolean paramBoolean)
  {
    setBoolean(paramContext, paramString, "prefetch-attachments", Boolean.valueOf(paramBoolean));
  }

  public void setPriorityInboxDefault(Context paramContext, String paramString, boolean paramBoolean)
  {
    setBoolean(paramContext, paramString, "priority-inbox-key", Boolean.valueOf(paramBoolean));
  }

  public void setSignature(Context paramContext, String paramString1, String paramString2)
  {
    setString(paramContext, paramString1, "signature", paramString2);
  }

  public void setSnapHeaderMode(Context paramContext, String paramString)
  {
    setString(paramContext, null, "snap-headers", paramString);
  }

  public void setString(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    SharedPreferences.Editor localEditor = getPreferences(paramContext).edit();
    localEditor.putString(makeKey(paramString1, paramString2), paramString3);
    localEditor.apply();
    notifyBackupDataChanged(paramString2);
  }

  public void setStringSet(Context paramContext, String paramString1, String paramString2, Set<String> paramSet)
  {
    SharedPreferences.Editor localEditor = getPreferences(paramContext).edit();
    localEditor.putStringSet(makeKey(paramString1, paramString2), paramSet);
    localEditor.apply();
    notifyBackupDataChanged(paramString2);
  }

  public void setSwipe(Context paramContext, String paramString)
  {
    setString(paramContext, null, "swipe-key", paramString);
  }

  public void setUpgradeSyncWindow(Context paramContext, boolean paramBoolean)
  {
    if ((paramBoolean) && (isPresent(paramContext, null, "show-sync-window-upgrade")))
      return;
    setBoolean(paramContext, null, "show-sync-window-upgrade", Boolean.valueOf(paramBoolean));
  }

  public boolean shouldNotifyForLabel(Context paramContext, String paramString1, String paramString2)
  {
    Set localSet = getNotificationLabelInformation(paramContext, paramString1, paramString2);
    return (localSet != null) && (Boolean.parseBoolean(extract(localSet, LABEL_NOTIFICATION_ON)));
  }

  public boolean shouldNotifyOnceForLabel(Context paramContext, String paramString1, String paramString2)
  {
    Set localSet = getNotificationLabelInformation(paramContext, paramString1, paramString2);
    return (localSet != null) && (Boolean.parseBoolean(extract(localSet, LABEL_NOTIFICATION_ONCE)));
  }

  public boolean shouldVibrateForLabel(Context paramContext, String paramString1, String paramString2)
  {
    initializeVibrationState(paramContext);
    Set localSet = getNotificationLabelInformation(paramContext, paramString1, paramString2);
    boolean bool1 = sCanVibrate.booleanValue();
    boolean bool2 = false;
    if (bool1)
    {
      String str = extractVibrateSetting(paramContext, localSet);
      if (!sAlwaysVibrateSetting.equals(str))
      {
        boolean bool3 = sVibrateWhenSilentSetting.equals(str);
        bool2 = false;
        if (!bool3);
      }
      else
      {
        bool2 = true;
      }
    }
    return bool2;
  }

  private abstract class AccountToGlobalPreferenceMigrator<T>
  {
    private AccountToGlobalPreferenceMigrator()
    {
    }

    public T migrate(Context paramContext)
    {
      List localList = Persistence.this.getCachedConfiguredGoogleAccounts(paramContext, false);
      if (localList != null)
      {
        if (localList.size() == 1)
          return migrateFromSingleAccount(paramContext, (String)localList.get(0));
        if (localList.size() > 1)
          return migrateFromMultipleAccounts(paramContext, localList);
      }
      String str = Persistence.this.getActiveAccount(paramContext);
      if (str != null)
        return migrateFromSingleAccount(paramContext, str);
      return null;
    }

    abstract T migrateFromMultipleAccounts(Context paramContext, List<String> paramList);

    abstract T migrateFromSingleAccount(Context paramContext, String paramString);
  }

  class AutoAdvanceAccountToGlobalMigrator extends Persistence.AccountToGlobalPreferenceMigrator<String>
  {
    AutoAdvanceAccountToGlobalMigrator()
    {
      super(null);
    }

    private int autoAdvanceSettingPriority(String paramString)
    {
      if ("newer".equals(paramString))
        return 3;
      if ("older".equals(paramString))
        return 2;
      if ("list".equals(paramString))
        return 1;
      return -1;
    }

    public String migrateFromMultipleAccounts(Context paramContext, List<String> paramList)
    {
      Object localObject = null;
      int i = -1;
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        String str1 = (String)localIterator.next();
        String str2 = Persistence.this.getString(paramContext, str1, "auto-advance", null);
        if (str2 != null)
        {
          int j = autoAdvanceSettingPriority(str2);
          if (j > i)
          {
            i = j;
            localObject = str2;
          }
        }
      }
      return localObject;
    }

    public String migrateFromSingleAccount(Context paramContext, String paramString)
    {
      return Persistence.this.getString(paramContext, paramString, "auto-advance", null);
    }
  }

  class ConfirmActionsAccountToGlobalMigrator extends Persistence.AccountToGlobalPreferenceMigrator<String>
  {
    ConfirmActionsAccountToGlobalMigrator()
    {
      super(null);
    }

    public String migrateFromMultipleAccounts(Context paramContext, List<String> paramList)
    {
      ArrayList localArrayList = Lists.newArrayList();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        String str2 = (String)localIterator.next();
        String str3 = Persistence.this.getString(paramContext, str2, "confirm-actions", null);
        if (str3 != null)
          localArrayList.add(str3);
      }
      int i = localArrayList.size();
      String str1 = null;
      if (i > 0)
        str1 = TextUtils.join(",", localArrayList);
      return str1;
    }

    public String migrateFromSingleAccount(Context paramContext, String paramString)
    {
      return Persistence.this.getString(paramContext, paramString, "confirm-actions", null);
    }
  }

  class DefaultReplyAllMigrator extends Persistence.AccountToGlobalPreferenceMigrator<Boolean>
  {
    DefaultReplyAllMigrator()
    {
      super(null);
    }

    Boolean migrateFromMultipleAccounts(Context paramContext, List<String> paramList)
    {
      boolean bool = true;
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        bool &= Persistence.this.getBoolean(paramContext, str, "action-strip-action-reply-all", false);
      }
      return Boolean.valueOf(bool);
    }

    Boolean migrateFromSingleAccount(Context paramContext, String paramString)
    {
      return Boolean.valueOf(Persistence.this.getBoolean(paramContext, paramString, "action-strip-action-reply-all", false));
    }
  }

  static class MailEnginePreferences
    implements MailEngine.Preferences
  {
    private final Context mContext;

    MailEnginePreferences(Context paramContext)
    {
      this.mContext = paramContext;
    }

    public boolean getFullTextSearchEnabled(String paramString)
    {
      return Persistence.getInstance().fullTextSearchEnabled(this.mContext);
    }

    public boolean getPrefetchAttachments(String paramString)
    {
      return Persistence.getInstance().getPrefetchAttachments(this.mContext, paramString);
    }
  }

  public class MessageTextSizeAccountToGlobalMigrator extends Persistence.AccountToGlobalPreferenceMigrator<String>
  {
    public MessageTextSizeAccountToGlobalMigrator()
    {
      super(null);
    }

    public String migrateFromMultipleAccounts(Context paramContext, List<String> paramList)
    {
      Object localObject = null;
      float f1 = 0.0F;
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        String str1 = (String)localIterator.next();
        String str2 = Persistence.this.getString(paramContext, str1, "message-text", null);
        if (str2 != null)
          try
          {
            float f2 = Float.parseFloat(str2);
            if (f2 > f1)
            {
              f1 = f2;
              localObject = str2;
            }
          }
          catch (NumberFormatException localNumberFormatException)
          {
          }
      }
      return localObject;
    }

    public String migrateFromSingleAccount(Context paramContext, String paramString)
    {
      return Persistence.this.getString(paramContext, paramString, "message-text", null);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.persistence.Persistence
 * JD-Core Version:    0.6.2
 */