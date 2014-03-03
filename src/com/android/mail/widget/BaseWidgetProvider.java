package com.android.mail.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.android.mail.preferences.MailPrefs;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;
import com.android.mail.providers.Settings;
import com.android.mail.providers.UIProvider;
import com.android.mail.ui.MailboxSelectionActivity;
import com.android.mail.utils.AccountUtils;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseWidgetProvider extends AppWidgetProvider
{
  private static final String LOG_TAG = LogTag.getLogTag();

  private final void migrateAllLegacyWidgetInformation(Context paramContext)
  {
    migrateLegacyWidgets(paramContext, getCurrentWidgetIds(paramContext));
  }

  private final void migrateLegacyWidgets(Context paramContext, int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++)
    {
      int k = paramArrayOfInt[j];
      if (!MailPrefs.get(paramContext).isWidgetConfigured(k))
        migrateLegacyWidgetInformation(paramContext, k);
    }
  }

  public static void updateWidget(Context paramContext, int paramInt, Account paramAccount, Folder paramFolder)
  {
    if ((paramAccount == null) || (paramFolder == null))
    {
      LogUtils.e(LOG_TAG, "Missing account or folder.  account: %s folder %s", new Object[] { paramAccount, paramFolder });
      return;
    }
    Intent localIntent = new Intent("com.android.mail.ACTION_UPDATE_WIDGET");
    localIntent.setType(paramAccount.mimeType);
    localIntent.putExtra("widgetId", paramInt);
    localIntent.putExtra("account", paramAccount.serialize());
    localIntent.putExtra("folder", Folder.toString(paramFolder));
    paramContext.sendBroadcast(localIntent);
  }

  protected void configureValidAccountWidget(Context paramContext, RemoteViews paramRemoteViews, int paramInt, Account paramAccount, Folder paramFolder, String paramString)
  {
    WidgetService.configureValidAccountWidget(paramContext, paramRemoteViews, paramInt, paramAccount, paramFolder, paramString, WidgetService.class);
  }

  protected Account getAccountObject(Context paramContext, String paramString)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Cursor localCursor = null;
    try
    {
      localCursor = localContentResolver.query(Uri.parse(paramString), UIProvider.ACCOUNTS_PROJECTION, null, null, null);
      Account localAccount = null;
      if (localCursor != null)
      {
        boolean bool = localCursor.moveToFirst();
        localAccount = null;
        if (bool)
          localAccount = new Account(localCursor);
      }
      return localAccount;
    }
    finally
    {
      if (localCursor != null)
        localCursor.close();
    }
  }

  protected int[] getCurrentWidgetIds(Context paramContext)
  {
    return AppWidgetManager.getInstance(paramContext).getAppWidgetIds(new ComponentName(paramContext, "com.android.mail.widget.WidgetProvider"));
  }

  protected boolean isAccountValid(Context paramContext, Account paramAccount)
  {
    if (paramAccount != null)
      for (Account localAccount : AccountUtils.getSyncingAccounts(paramContext))
        if ((paramAccount != null) && (localAccount != null) && (paramAccount.uri.equals(localAccount.uri)))
          return true;
    return false;
  }

  protected abstract void migrateLegacyWidgetInformation(Context paramContext, int paramInt);

  public void onDeleted(Context paramContext, int[] paramArrayOfInt)
  {
    super.onDeleted(paramContext, paramArrayOfInt);
    MailPrefs.get(paramContext).clearWidgets(paramArrayOfInt);
  }

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    migrateAllLegacyWidgetInformation(paramContext);
    super.onReceive(paramContext, paramIntent);
    LogUtils.d(LOG_TAG, "BaseWidgetProvider.onReceive: %s", new Object[] { paramIntent });
    String str1 = paramIntent.getAction();
    if ("com.android.mail.ACTION_UPDATE_WIDGET".equals(str1))
    {
      int m = paramIntent.getIntExtra("widgetId", -1);
      Account localAccount = Account.newinstance(paramIntent.getStringExtra("account"));
      Folder localFolder = Folder.fromString(paramIntent.getStringExtra("folder"));
      if ((m != -1) && (localAccount != null) && (localFolder != null))
        updateWidgetInternal(paramContext, m, localAccount, localFolder);
    }
    HashSet localHashSet;
    label283: 
    do
    {
      Uri localUri1;
      Uri localUri2;
      boolean bool1;
      do
      {
        do
          return;
        while (!"com.android.mail.ACTION_NOTIFY_DATASET_CHANGED".equals(str1));
        Bundle localBundle = paramIntent.getExtras();
        localUri1 = (Uri)localBundle.getParcelable("accountUri");
        localUri2 = (Uri)localBundle.getParcelable("folderUri");
        bool1 = localBundle.getBoolean("update-all-widgets", false);
      }
      while ((localUri1 == null) && (localUri2 == null) && (!bool1));
      localHashSet = Sets.newHashSet();
      int[] arrayOfInt1 = getCurrentWidgetIds(paramContext);
      int i = arrayOfInt1.length;
      int j = 0;
      if (j < i)
      {
        int k = arrayOfInt1[j];
        String str2 = MailPrefs.get(paramContext).getWidgetConfiguration(k);
        String[] arrayOfString;
        if (str2 != null)
        {
          arrayOfString = TextUtils.split(str2, " ");
          bool2 = bool1;
          if (!bool2)
            if ((localUri1 == null) || (!TextUtils.equals(localUri1.toString(), arrayOfString[0])))
              break label283;
        }
        for (boolean bool2 = true; ; bool2 = true)
          do
          {
            if (bool2)
              localHashSet.add(Integer.valueOf(k));
            j++;
            break;
          }
          while ((localUri2 == null) || (!TextUtils.equals(localUri2.toString(), arrayOfString[1])));
      }
    }
    while (localHashSet.size() <= 0);
    int[] arrayOfInt2 = Ints.toArray(localHashSet);
    AppWidgetManager.getInstance(paramContext).notifyAppWidgetViewDataChanged(arrayOfInt2, 2131689727);
  }

  public void onUpdate(Context paramContext, AppWidgetManager paramAppWidgetManager, int[] paramArrayOfInt)
  {
    migrateLegacyWidgets(paramContext, paramArrayOfInt);
    super.onUpdate(paramContext, paramAppWidgetManager, paramArrayOfInt);
    ContentResolver localContentResolver = paramContext.getContentResolver();
    int i = 0;
    while (i < paramArrayOfInt.length)
    {
      String str = MailPrefs.get(paramContext).getWidgetConfiguration(paramArrayOfInt[i]);
      boolean bool1 = TextUtils.isEmpty(str);
      Uri localUri = null;
      Object localObject1 = null;
      label91: Account localAccount;
      Folder localFolder;
      Cursor localCursor;
      if (!bool1)
      {
        String[] arrayOfString = TextUtils.split(str, " ");
        if (arrayOfString.length == 2)
        {
          localObject1 = arrayOfString[0];
          localUri = Uri.parse(arrayOfString[1]);
        }
      }
      else
      {
        boolean bool2 = TextUtils.isEmpty((CharSequence)localObject1);
        localAccount = null;
        if (!bool2)
          localAccount = getAccountObject(paramContext, (String)localObject1);
        if ((Utils.isEmpty(localUri)) && (localAccount != null))
          localUri = localAccount.settings.defaultInbox;
        boolean bool3 = Utils.isEmpty(localUri);
        localFolder = null;
        if (!bool3)
          localCursor = null;
      }
      try
      {
        localCursor = localContentResolver.query(localUri, UIProvider.FOLDERS_PROJECTION, null, null, null);
        localFolder = null;
        if (localCursor != null)
        {
          boolean bool4 = localCursor.moveToFirst();
          localFolder = null;
          if (bool4)
            localFolder = new Folder(localCursor);
        }
        if (localCursor != null)
          localCursor.close();
        updateWidgetInternal(paramContext, paramArrayOfInt[i], localAccount, localFolder);
        i++;
        continue;
        localObject1 = str;
        localUri = Uri.EMPTY;
        break label91;
      }
      finally
      {
        if (localCursor != null)
          localCursor.close();
      }
    }
  }

  protected void updateWidgetInternal(Context paramContext, int paramInt, Account paramAccount, Folder paramFolder)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968685);
    if ((!isAccountValid(paramContext, paramAccount)) || (paramFolder == null))
    {
      localRemoteViews.setViewVisibility(2131689722, 8);
      localRemoteViews.setViewVisibility(2131689723, 8);
      localRemoteViews.setViewVisibility(2131689724, 8);
      localRemoteViews.setViewVisibility(2131689725, 8);
      localRemoteViews.setViewVisibility(2131689727, 8);
      localRemoteViews.setViewVisibility(2131689729, 8);
      localRemoteViews.setViewVisibility(2131689728, 0);
      localRemoteViews.setTextViewText(2131689726, paramContext.getString(2131427486));
      Intent localIntent = new Intent(paramContext, MailboxSelectionActivity.class);
      localIntent.putExtra("appWidgetId", paramInt);
      localIntent.setData(Uri.parse(localIntent.toUri(1)));
      localIntent.setFlags(1073741824);
      localRemoteViews.setOnClickPendingIntent(2131689728, PendingIntent.getActivity(paramContext, 0, localIntent, 134217728));
    }
    while (true)
    {
      AppWidgetManager.getInstance(paramContext).updateAppWidget(paramInt, localRemoteViews);
      return;
      configureValidAccountWidget(paramContext, localRemoteViews, paramInt, paramAccount, paramFolder, " ");
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.widget.BaseWidgetProvider
 * JD-Core Version:    0.6.2
 */