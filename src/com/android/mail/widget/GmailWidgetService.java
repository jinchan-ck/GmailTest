package com.android.mail.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;
import com.google.android.gm.LabelSynchronizationActivity;
import com.google.android.gm.Utils;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.Settings;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;

public class GmailWidgetService extends WidgetService
{
  public static void configureValidAccountWidget(Context paramContext, RemoteViews paramRemoteViews, int paramInt, Account paramAccount, Folder paramFolder, String paramString, Class<?> paramClass)
  {
    if (isFolderSynchronized(paramContext, paramAccount, paramFolder))
    {
      paramRemoteViews.setViewVisibility(2131689727, 0);
      paramRemoteViews.setViewVisibility(2131689729, 8);
      WidgetService.configureValidAccountWidget(paramContext, paramRemoteViews, paramInt, paramAccount, paramFolder, paramString, GmailWidgetService.class);
      return;
    }
    paramRemoteViews.setViewVisibility(2131689727, 8);
    paramRemoteViews.setViewVisibility(2131689729, 0);
    Intent localIntent = new Intent(paramContext, LabelSynchronizationActivity.class);
    localIntent.putExtra("account", paramAccount);
    localIntent.putExtra("folder", paramFolder);
    localIntent.putExtra("update-widgetid-on-sync-change", paramInt);
    localIntent.putExtra("perform-actions-internally", true);
    localIntent.setData(Uri.parse(localIntent.toUri(1)));
    localIntent.setFlags(1476427776);
    paramRemoteViews.setOnClickPendingIntent(2131689729, PendingIntent.getActivity(paramContext, 0, localIntent, 134217728));
    configureValidWidgetIntents(paramContext, paramRemoteViews, paramInt, paramAccount, paramFolder, paramString, GmailWidgetService.class);
  }

  private static boolean isFolderSynchronized(Context paramContext, Account paramAccount, Folder paramFolder)
  {
    String str = paramFolder.uri.getLastPathSegment();
    Gmail.Settings localSettings = LongShadowUtils.getContentProviderMailAccess(paramContext.getContentResolver()).getSettings(paramContext, paramAccount.name);
    HashSet localHashSet = Sets.newHashSet();
    localHashSet.addAll(localSettings.getLabelsIncluded());
    localHashSet.addAll(localSettings.getLabelsPartial());
    return localHashSet.contains(str);
  }

  protected void configureValidAccountWidget(Context paramContext, RemoteViews paramRemoteViews, int paramInt, Account paramAccount, Folder paramFolder, String paramString)
  {
    configureValidAccountWidget(paramContext, paramRemoteViews, paramInt, paramAccount, paramFolder, paramString, GmailWidgetService.class);
  }

  protected boolean isAccountValid(Context paramContext, Account paramAccount)
  {
    return Utils.isAccountValid(paramContext, paramAccount);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.widget.GmailWidgetService
 * JD-Core Version:    0.6.2
 */