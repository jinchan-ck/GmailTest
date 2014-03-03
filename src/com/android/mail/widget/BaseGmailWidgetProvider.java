package com.android.mail.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;
import com.google.android.gm.Utils;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.provider.UiProvider;
import com.google.common.primitives.Ints;

public class BaseGmailWidgetProvider extends BaseWidgetProvider
{
  protected void configureValidAccountWidget(Context paramContext, RemoteViews paramRemoteViews, int paramInt, Account paramAccount, Folder paramFolder, String paramString)
  {
    GmailWidgetService.configureValidAccountWidget(paramContext, paramRemoteViews, paramInt, paramAccount, paramFolder, paramFolder.name, GmailWidgetService.class);
  }

  protected int[] getCurrentWidgetIds(Context paramContext)
  {
    AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(paramContext);
    ComponentName localComponentName1 = new ComponentName(paramContext, "com.google.android.gm.widget.GmailWidgetProvider");
    ComponentName localComponentName2 = new ComponentName(paramContext, "com.google.android.gm.widget.GoogleMailWidgetProvider");
    return Ints.concat(new int[][] { localAppWidgetManager.getAppWidgetIds(localComponentName1), localAppWidgetManager.getAppWidgetIds(localComponentName2) });
  }

  protected boolean isAccountValid(Context paramContext, Account paramAccount)
  {
    return Utils.isAccountValid(paramContext, paramAccount);
  }

  protected void migrateLegacyWidgetInformation(Context paramContext, int paramInt)
  {
    SharedPreferences localSharedPreferences = Persistence.getInstance().getPreferences(paramContext);
    SharedPreferences.Editor localEditor = localSharedPreferences.edit();
    String str1 = "widget-account-" + paramInt;
    String str2 = localSharedPreferences.getString(str1, null);
    String[] arrayOfString;
    String str3;
    if (str2 != null)
    {
      arrayOfString = TextUtils.split(str2, " ");
      if (arrayOfString.length != 2)
        break label147;
      str3 = arrayOfString[0];
    }
    for (String str4 = arrayOfString[1]; ; str4 = Persistence.getAccountInbox(paramContext, str3))
    {
      Account localAccount = UiProvider.getAccountObject(paramContext, str3);
      Folder localFolder = UiProvider.getSparseFolderObject(paramContext, null, str3, str4);
      if ((localAccount != null) && (localFolder != null))
      {
        WidgetService.saveWidgetInformation(paramContext, paramInt, localAccount, localFolder);
        updateWidgetInternal(paramContext, paramInt, localAccount, localFolder);
        localEditor.remove(str1);
      }
      localEditor.apply();
      return;
      label147: str3 = str2;
    }
  }

  public void onDeleted(Context paramContext, int[] paramArrayOfInt)
  {
    super.onDeleted(paramContext, paramArrayOfInt);
    SharedPreferences.Editor localEditor = Persistence.getInstance().getPreferences(paramContext).edit();
    for (int i = 0; i < paramArrayOfInt.length; i++)
      localEditor.remove("widget-account-" + paramArrayOfInt[i]);
    localEditor.apply();
  }

  protected void updateWidgetInternal(Context paramContext, int paramInt, Account paramAccount, Folder paramFolder)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968685);
    if ((!isAccountValid(paramContext, paramAccount)) || (paramFolder == null))
      super.updateWidgetInternal(paramContext, paramInt, paramAccount, paramFolder);
    while (true)
    {
      AppWidgetManager.getInstance(paramContext).updateAppWidget(paramInt, localRemoteViews);
      return;
      configureValidAccountWidget(paramContext, localRemoteViews, paramInt, paramAccount, paramFolder, paramFolder.name);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.widget.BaseGmailWidgetProvider
 * JD-Core Version:    0.6.2
 */