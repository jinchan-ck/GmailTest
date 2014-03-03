package com.android.mail.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.text.style.CharacterStyle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.android.mail.browse.ConversationCursor;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.Settings;
import com.google.android.common.html.parser.HtmlDocument;
import com.google.android.common.html.parser.HtmlParser;
import com.google.android.common.html.parser.HtmlTree;
import com.google.android.common.html.parser.HtmlTreeBuilder;
import com.google.common.collect.Maps;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;

public class Utils
{
  private static final String LOG_TAG;
  public static final Character SENDER_LIST_SEPARATOR;
  private static int sDefaultFolderBackgroundColor;
  private static int sMaxUnreadCount;
  private static final Map<Integer, Integer> sPriorityToLength;
  public static String[] sSenderFragments;
  public static final TextUtils.SimpleStringSplitter sSenderListSplitter;
  private static CharacterStyle sUnreadStyleSpan;
  private static String sUnreadText;
  private static int sUseFolderListFragmentTransition;
  private static int sVersionCode;

  static
  {
    if (!Utils.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      sPriorityToLength = Maps.newHashMap();
      SENDER_LIST_SEPARATOR = Character.valueOf('\n');
      sSenderListSplitter = new TextUtils.SimpleStringSplitter(SENDER_LIST_SEPARATOR.charValue());
      sSenderFragments = new String[8];
      sVersionCode = -1;
      LOG_TAG = LogTag.getLogTag();
      sUnreadStyleSpan = null;
      sMaxUnreadCount = -1;
      sDefaultFolderBackgroundColor = -1;
      sUseFolderListFragmentTransition = -1;
      return;
    }
  }

  private static Uri addParamsToUrl(Context paramContext, String paramString)
  {
    Uri.Builder localBuilder = Uri.parse(replaceLocale(paramString)).buildUpon();
    int i = getVersionCode(paramContext);
    if (i != -1)
      localBuilder = localBuilder.appendQueryParameter("version", String.valueOf(i));
    return localBuilder.build();
  }

  public static Object cleanUpString(String paramString, boolean paramBoolean)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (paramBoolean)
        paramString = paramString.replace("\"\"", "");
      return TextUtils.htmlEncode(paramString);
    }
    return "";
  }

  public static String convertHtmlToPlainText(String paramString)
  {
    return getHtmlTree(paramString, new HtmlParser(), new HtmlTreeBuilder()).getPlainText();
  }

  public static String convertHtmlToPlainText(String paramString, HtmlParser paramHtmlParser, HtmlTreeBuilder paramHtmlTreeBuilder)
  {
    return getHtmlTree(paramString, paramHtmlParser, paramHtmlTreeBuilder).getPlainText();
  }

  public static Intent createViewConversationIntent(Conversation paramConversation, Folder paramFolder, Account paramAccount)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setFlags(268484608);
    localIntent.setDataAndType(paramConversation.uri, paramAccount.mimeType);
    localIntent.putExtra("account", paramAccount.serialize());
    localIntent.putExtra("folder", Folder.toString(paramFolder));
    localIntent.putExtra("conversationUri", paramConversation);
    return localIntent;
  }

  public static Intent createViewFolderIntent(Folder paramFolder, Account paramAccount)
  {
    if ((paramFolder == null) || (paramAccount == null))
    {
      LogUtils.wtf(LOG_TAG, "Utils.createViewFolderIntent(%s,%s): Bad input", new Object[] { paramFolder, paramAccount });
      return null;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setFlags(268484608);
    localIntent.setDataAndType(paramFolder.uri, paramAccount.mimeType);
    localIntent.putExtra("account", paramAccount.serialize());
    localIntent.putExtra("folder", Folder.toString(paramFolder));
    return localIntent;
  }

  public static Intent createViewInboxIntent(Account paramAccount)
  {
    if (paramAccount == null)
    {
      LogUtils.wtf(LOG_TAG, "Utils.createViewInboxIntent(%s): Bad input", new Object[] { paramAccount });
      return null;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setFlags(268484608);
    localIntent.setDataAndType(paramAccount.settings.defaultInbox, paramAccount.mimeType);
    localIntent.putExtra("account", paramAccount.serialize());
    return localIntent;
  }

  public static boolean disableConversationCursorNetworkAccess(Cursor paramCursor)
  {
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("allowNetwork", false);
    return executeConversationCursorCommand(paramCursor, localBundle, "allowNetwork");
  }

  public static String ellipsize(String paramString, int paramInt)
  {
    int i = paramString.length();
    if (i < paramInt)
      return paramString;
    int j = Math.min(paramInt, i);
    int k = paramString.lastIndexOf(".");
    String str = "…";
    if ((k >= 0) && (i - k <= 5))
      str = str + paramString.substring(k + 1);
    int m = j - str.length();
    if (m < 0)
      m = 0;
    return paramString.substring(0, m) + str;
  }

  public static boolean enableConversationCursorNetworkAccess(Cursor paramCursor)
  {
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("allowNetwork", true);
    return executeConversationCursorCommand(paramCursor, localBundle, "allowNetwork");
  }

  public static void enableHardwareLayer(View paramView)
  {
    if ((paramView != null) && (paramView.isHardwareAccelerated()))
    {
      paramView.setLayerType(2, null);
      paramView.buildLayer();
    }
  }

  public static String ensureQuotedString(String paramString)
  {
    if (paramString == null)
      paramString = null;
    while (paramString.matches("^\".*\"$"))
      return paramString;
    return "\"" + paramString + "\"";
  }

  private static boolean executeConversationCursorCommand(Cursor paramCursor, Bundle paramBundle, String paramString)
  {
    return "ok".equals(paramCursor.respond(paramBundle).getString(paramString, "failed"));
  }

  public static String formatPlural(Context paramContext, int paramInt1, int paramInt2)
  {
    String str = paramContext.getResources().getQuantityText(paramInt1, paramInt2).toString();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt2);
    return String.format(str, arrayOfObject);
  }

  public static long getConversationId(ConversationCursor paramConversationCursor)
  {
    return paramConversationCursor.getLong(0);
  }

  public static int getDefaultFolderBackgroundColor(Context paramContext)
  {
    if (sDefaultFolderBackgroundColor == -1)
      sDefaultFolderBackgroundColor = paramContext.getResources().getColor(2131230739);
    return sDefaultFolderBackgroundColor;
  }

  public static String getFileExtension(String paramString)
  {
    if (!TextUtils.isEmpty(paramString));
    for (int i = paramString.lastIndexOf('.'); ; i = -1)
    {
      String str = null;
      if (i >= 0)
      {
        int j = paramString.length() - i;
        str = null;
        if (j <= 5)
          str = paramString.substring(i);
      }
      return str;
    }
  }

  public static int getFolderUnreadDisplayCount(Folder paramFolder)
  {
    if (paramFolder != null)
    {
      switch (paramFolder.type)
      {
      case 4:
      default:
        return paramFolder.unreadCount;
      case 2:
      case 3:
      case 5:
      }
      return paramFolder.totalCount;
    }
    return 0;
  }

  public static HtmlTree getHtmlTree(String paramString)
  {
    return getHtmlTree(paramString, new HtmlParser(), new HtmlTreeBuilder());
  }

  public static HtmlTree getHtmlTree(String paramString, HtmlParser paramHtmlParser, HtmlTreeBuilder paramHtmlTreeBuilder)
  {
    paramHtmlParser.parse(paramString).accept(paramHtmlTreeBuilder);
    return paramHtmlTreeBuilder.getTree();
  }

  public static CharSequence getSyncStatusText(Context paramContext, int paramInt)
  {
    String[] arrayOfString = paramContext.getResources().getStringArray(2131558407);
    int i = paramInt & 0xF;
    if (i >= arrayOfString.length)
      return "";
    return arrayOfString[i];
  }

  public static int getTransparentColor(int paramInt)
  {
    return 0xFFFFFF & paramInt;
  }

  public static String getUnreadCountString(Context paramContext, int paramInt)
  {
    Resources localResources = paramContext.getResources();
    if (sMaxUnreadCount == -1)
      sMaxUnreadCount = localResources.getInteger(2131296284);
    if (paramInt > sMaxUnreadCount)
    {
      if (sUnreadText == null)
        sUnreadText = localResources.getString(2131427548);
      String str = sUnreadText;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(sMaxUnreadCount);
      return String.format(str, arrayOfObject);
    }
    if (paramInt <= 0)
      return "";
    return String.valueOf(paramInt);
  }

  public static Uri getValidUri(String paramString)
  {
    if ((TextUtils.isEmpty(paramString)) || (paramString == JSONObject.NULL))
      return Uri.EMPTY;
    return Uri.parse(paramString);
  }

  public static int getVersionCode(Context paramContext)
  {
    if (sVersionCode == -1);
    try
    {
      sVersionCode = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionCode;
      return sVersionCode;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
      {
        String str = LOG_TAG;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = paramContext.getApplicationInfo().packageName;
        LogUtils.e(str, "Error finding package %s", arrayOfObject);
      }
    }
  }

  public static boolean isEmpty(Uri paramUri)
  {
    return (paramUri == null) || (paramUri.equals(Uri.EMPTY));
  }

  public static boolean isRunningJellybeanOrLater()
  {
    return Build.VERSION.SDK_INT >= 16;
  }

  public static int measureViewHeight(View paramView, ViewGroup paramViewGroup)
  {
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams;
    if ((localLayoutParams instanceof ViewGroup.MarginLayoutParams))
      localMarginLayoutParams = (ViewGroup.MarginLayoutParams)localLayoutParams;
    for (int i = localMarginLayoutParams.leftMargin + localMarginLayoutParams.rightMargin; ; i = 0)
    {
      paramView.measure(ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(paramViewGroup.getWidth(), 1073741824), i + (paramViewGroup.getPaddingLeft() + paramViewGroup.getPaddingRight()), -1), View.MeasureSpec.makeMeasureSpec(0, 0));
      return paramView.getMeasuredHeight();
    }
  }

  public static String normalizeMimeType(String paramString)
  {
    String str;
    if (paramString == null)
      str = null;
    int i;
    do
    {
      return str;
      str = paramString.trim().toLowerCase(Locale.US);
      i = str.indexOf(';');
    }
    while (i == -1);
    return str.substring(0, i);
  }

  public static Uri normalizeUri(Uri paramUri)
  {
    String str1 = paramUri.getScheme();
    if (str1 == null);
    String str2;
    do
    {
      return paramUri;
      str2 = str1.toLowerCase(Locale.US);
    }
    while (str1.equals(str2));
    return paramUri.buildUpon().scheme(str2).build();
  }

  private static void openUrl(Context paramContext, Uri paramUri, Bundle paramBundle)
  {
    if ((paramUri == null) || (TextUtils.isEmpty(paramUri.toString())))
    {
      LogUtils.wtf(LOG_TAG, "invalid url in Utils.openUrl(): %s", new Object[] { paramUri });
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW", paramUri);
    if (paramBundle != null)
      localIntent.putExtras(paramBundle);
    localIntent.putExtra("com.android.browser.application_id", paramContext.getPackageName());
    localIntent.addFlags(524288);
    paramContext.startActivity(localIntent);
  }

  private static String replaceLocale(String paramString)
  {
    if (paramString.contains("%locale%"))
    {
      Locale localLocale = Locale.getDefault();
      paramString = paramString.replace("%locale%", localLocale.getLanguage() + "_" + localLocale.getCountry().toLowerCase());
    }
    return paramString;
  }

  public static void restrictWebView(WebView paramWebView)
  {
    WebSettings localWebSettings = paramWebView.getSettings();
    localWebSettings.setSavePassword(false);
    localWebSettings.setSaveFormData(false);
    localWebSettings.setJavaScriptEnabled(true);
    localWebSettings.setSupportZoom(false);
  }

  public static void sendFeedback(Context paramContext, Account paramAccount, boolean paramBoolean)
  {
    if ((paramAccount != null) && (paramAccount.sendFeedbackIntentUri != null))
    {
      Bundle localBundle = new Bundle(1);
      localBundle.putBoolean("reporting_problem", paramBoolean);
      openUrl(paramContext, paramAccount.sendFeedbackIntentUri, localBundle);
    }
  }

  public static void setConversationCursorVisibility(Cursor paramCursor, boolean paramBoolean1, boolean paramBoolean2)
  {
    new MarkConversationCursorVisibleTask(paramCursor, paramBoolean1, paramBoolean2).execute(new Void[0]);
  }

  public static Intent setIntentDataAndTypeAndNormalize(Intent paramIntent, Uri paramUri, String paramString)
  {
    return paramIntent.setDataAndType(normalizeUri(paramUri), normalizeMimeType(paramString));
  }

  public static Intent setIntentTypeAndNormalize(Intent paramIntent, String paramString)
  {
    return paramIntent.setType(normalizeMimeType(paramString));
  }

  public static void setMenuItemVisibility(Menu paramMenu, int paramInt, boolean paramBoolean)
  {
    MenuItem localMenuItem = paramMenu.findItem(paramInt);
    if (localMenuItem == null)
      return;
    localMenuItem.setVisible(paramBoolean);
  }

  public static boolean shouldShowDisabledArchiveIcon(Context paramContext)
  {
    return paramContext.getResources().getBoolean(2131623938);
  }

  public static void showFolderSettings(Context paramContext, Account paramAccount, Folder paramFolder)
  {
    if ((paramAccount == null) || (paramFolder == null))
    {
      LogUtils.e(LOG_TAG, "Invalid attempt to show folder settings. account: %s folder: %s", new Object[] { paramAccount, paramFolder });
      return;
    }
    Intent localIntent = new Intent("android.intent.action.EDIT", paramAccount.settingsIntentUri);
    localIntent.putExtra("extra_account", paramAccount);
    localIntent.putExtra("extra_folder", paramFolder);
    localIntent.addFlags(524288);
    paramContext.startActivity(localIntent);
  }

  public static void showHelp(Context paramContext, Account paramAccount, String paramString)
  {
    if ((paramAccount != null) && (paramAccount.helpIntentUri != null));
    for (String str = paramAccount.helpIntentUri.toString(); TextUtils.isEmpty(str); str = null)
    {
      LogUtils.e(LOG_TAG, "unable to show help for account: %s", new Object[] { paramAccount });
      return;
    }
    Uri.Builder localBuilder = addParamsToUrl(paramContext, str).buildUpon();
    if (!TextUtils.isEmpty(paramString))
      localBuilder = localBuilder.appendQueryParameter("p", paramString);
    openUrl(paramContext, localBuilder.build(), null);
  }

  public static void showManageFolder(Context paramContext, Account paramAccount)
  {
    if (paramAccount == null)
    {
      LogUtils.e(LOG_TAG, "Invalid attempt to the manage folders screen with null account", new Object[0]);
      return;
    }
    Intent localIntent = new Intent("android.intent.action.EDIT", paramAccount.settingsIntentUri);
    localIntent.putExtra("extra_account", paramAccount);
    localIntent.putExtra("extra_manage_folders", true);
    localIntent.addFlags(524288);
    paramContext.startActivity(localIntent);
  }

  public static void showSettings(Context paramContext, Account paramAccount)
  {
    if (paramAccount == null)
    {
      LogUtils.e(LOG_TAG, "Invalid attempt to show setting screen with null account", new Object[0]);
      return;
    }
    Intent localIntent = new Intent("android.intent.action.EDIT", paramAccount.settingsIntentUri);
    localIntent.addFlags(524288);
    paramContext.startActivity(localIntent);
  }

  public static boolean showTwoPaneSearchResults(Context paramContext)
  {
    return paramContext.getResources().getBoolean(2131623937);
  }

  public static boolean useFolderListFragmentTransition(Context paramContext)
  {
    if (sUseFolderListFragmentTransition == -1)
      sUseFolderListFragmentTransition = paramContext.getResources().getInteger(2131296303);
    return sUseFolderListFragmentTransition != 0;
  }

  public static boolean useTabletUI(Context paramContext)
  {
    return paramContext.getResources().getInteger(2131296274) != 0;
  }

  private static class MarkConversationCursorVisibleTask extends AsyncTask<Void, Void, Void>
  {
    private final Cursor mCursor;
    private final boolean mIsFirstSeen;
    private final boolean mVisible;

    public MarkConversationCursorVisibleTask(Cursor paramCursor, boolean paramBoolean1, boolean paramBoolean2)
    {
      this.mCursor = paramCursor;
      this.mVisible = paramBoolean1;
      this.mIsFirstSeen = paramBoolean2;
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      if (this.mCursor == null)
        return null;
      Bundle localBundle = new Bundle();
      if (this.mIsFirstSeen)
        localBundle.putBoolean("enteredFolder", true);
      localBundle.putBoolean("setVisibility", this.mVisible);
      Utils.executeConversationCursorCommand(this.mCursor, localBundle, "setVisibility");
      return null;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.Utils
 * JD-Core Version:    0.6.2
 */