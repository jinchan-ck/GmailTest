package com.android.mail.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Looper;
import android.support.v4.app.TaskStackBuilder;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.CharacterStyle;
import android.text.style.TextAppearanceSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import com.android.mail.browse.SendersView;
import com.android.mail.preferences.MailPrefs;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.ConversationInfo;
import com.android.mail.providers.Folder;
import com.android.mail.providers.UIProvider;
import com.android.mail.utils.AccountUtils;
import com.android.mail.utils.DelayedTaskHandler;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import com.google.android.common.html.parser.HtmlParser;
import com.google.android.common.html.parser.HtmlTreeBuilder;

public class WidgetService extends RemoteViewsService
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private static Object sWidgetLock = new Object();

  public static void configureValidAccountWidget(Context paramContext, RemoteViews paramRemoteViews, int paramInt, Account paramAccount, Folder paramFolder, String paramString, Class<?> paramClass)
  {
    paramRemoteViews.setViewVisibility(2131689722, 0);
    if ((TextUtils.isEmpty(paramString)) || (TextUtils.isEmpty(paramAccount.name)))
    {
      String str = LOG_TAG;
      Error localError = new Error();
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramAccount.name;
      arrayOfObject[1] = paramString;
      LogUtils.e(str, localError, "Empty folder or account name.  account: %s, folder: %s", arrayOfObject);
    }
    if (!TextUtils.isEmpty(paramString))
      paramRemoteViews.setTextViewText(2131689722, paramString);
    paramRemoteViews.setViewVisibility(2131689723, 0);
    if (!TextUtils.isEmpty(paramAccount.name))
      paramRemoteViews.setTextViewText(2131689723, paramAccount.name);
    paramRemoteViews.setViewVisibility(2131689724, 0);
    paramRemoteViews.setViewVisibility(2131689725, 0);
    paramRemoteViews.setViewVisibility(2131689727, 0);
    paramRemoteViews.setViewVisibility(2131689729, 8);
    paramRemoteViews.setEmptyView(2131689727, 2131689726);
    configureValidWidgetIntents(paramContext, paramRemoteViews, paramInt, paramAccount, paramFolder, paramString, paramClass);
  }

  public static void configureValidWidgetIntents(Context paramContext, RemoteViews paramRemoteViews, int paramInt, Account paramAccount, Folder paramFolder, String paramString, Class<?> paramClass)
  {
    paramRemoteViews.setViewVisibility(2131689728, 8);
    Intent localIntent1 = new Intent(paramContext, paramClass);
    localIntent1.putExtra("appWidgetId", paramInt);
    localIntent1.putExtra("account", paramAccount.serialize());
    localIntent1.putExtra("folder", Folder.toString(paramFolder));
    localIntent1.setData(Uri.parse(localIntent1.toUri(1)));
    paramRemoteViews.setRemoteAdapter(2131689727, localIntent1);
    Intent localIntent2 = Utils.createViewFolderIntent(paramFolder, paramAccount);
    paramRemoteViews.setOnClickPendingIntent(2131689720, PendingIntent.getActivity(paramContext, 0, localIntent2, 134217728));
    Intent localIntent3 = new Intent();
    localIntent3.setAction("android.intent.action.SEND");
    localIntent3.putExtra("account", paramAccount.serialize());
    localIntent3.setData(paramAccount.composeIntentUri);
    localIntent3.putExtra("fromemail", true);
    if (paramAccount.composeIntentUri != null)
      localIntent3.putExtra("composeUri", paramAccount.composeIntentUri);
    paramRemoteViews.setOnClickPendingIntent(2131689725, TaskStackBuilder.create(paramContext).addNextIntent(localIntent2).addNextIntent(localIntent3).getPendingIntent(0, 134217728));
    Intent localIntent4 = new Intent();
    localIntent4.setAction("android.intent.action.VIEW");
    paramRemoteViews.setPendingIntentTemplate(2131689727, PendingIntent.getActivity(paramContext, 0, localIntent4, 134217728));
  }

  public static void saveWidgetInformation(Context paramContext, int paramInt, Account paramAccount, Folder paramFolder)
  {
    MailPrefs.get(paramContext).configureWidget(paramInt, paramAccount, paramFolder);
  }

  protected void configureValidAccountWidget(Context paramContext, RemoteViews paramRemoteViews, int paramInt, Account paramAccount, Folder paramFolder, String paramString)
  {
    configureValidAccountWidget(paramContext, paramRemoteViews, paramInt, paramAccount, paramFolder, paramString, WidgetService.class);
  }

  protected boolean isAccountValid(Context paramContext, Account paramAccount)
  {
    if (paramAccount != null)
      for (Account localAccount : AccountUtils.getSyncingAccounts(paramContext))
        if ((paramAccount != null) && (localAccount != null) && (paramAccount.uri.equals(localAccount.uri)))
          return true;
    return false;
  }

  public boolean isWidgetConfigured(Context paramContext, int paramInt, Account paramAccount, Folder paramFolder)
  {
    return (isAccountValid(paramContext, paramAccount)) && (MailPrefs.get(paramContext).isWidgetConfigured(paramInt));
  }

  public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent paramIntent)
  {
    return new MailFactory(getApplicationContext(), paramIntent, this);
  }

  protected static class MailFactory
    implements RemoteViewsService.RemoteViewsFactory, Loader.OnLoadCompleteListener<Cursor>
  {
    private final Account mAccount;
    private final int mAppWidgetId;
    private final Context mContext;
    private Cursor mConversationCursor;
    private CursorLoader mConversationCursorLoader;
    private String mElidedPaddingToken;
    private Folder mFolder;
    private int mFolderCount;
    private boolean mFolderInformationShown = false;
    private CursorLoader mFolderLoader;
    private FolderUpdateHandler mFolderUpdateHandler;
    private TextAppearanceSpan mReadStyle;
    private String mSendersSplitToken;
    private WidgetService mService;
    private boolean mShouldShowViewMore;
    private TextAppearanceSpan mUnreadStyle;
    private final WidgetConversationViewBuilder mWidgetConversationViewBuilder;

    public MailFactory(Context paramContext, Intent paramIntent, WidgetService paramWidgetService)
    {
      this.mContext = paramContext;
      this.mAppWidgetId = paramIntent.getIntExtra("appWidgetId", 0);
      this.mAccount = Account.newinstance(paramIntent.getStringExtra("account"));
      this.mFolder = Folder.fromString(paramIntent.getStringExtra("folder"));
      this.mWidgetConversationViewBuilder = new WidgetConversationViewBuilder(paramContext, this.mAccount);
      this.mService = paramWidgetService;
    }

    private SpannableString copyStyles(CharacterStyle[] paramArrayOfCharacterStyle, CharSequence paramCharSequence)
    {
      SpannableString localSpannableString = new SpannableString(paramCharSequence);
      if ((paramArrayOfCharacterStyle != null) && (paramArrayOfCharacterStyle.length > 0))
        localSpannableString.setSpan(paramArrayOfCharacterStyle[0], 0, localSpannableString.length(), 0);
      return localSpannableString;
    }

    private SpannableStringBuilder ellipsizeStyledSenders(ConversationInfo paramConversationInfo, int paramInt, SpannableString[] paramArrayOfSpannableString)
    {
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
      Object localObject1 = null;
      int i = paramArrayOfSpannableString.length;
      int j = 0;
      while (j < i)
      {
        Object localObject2 = paramArrayOfSpannableString[j];
        if (localObject2 == null)
        {
          LogUtils.e(WidgetService.LOG_TAG, "null sender while iterating over styledSenders", new Object[0]);
          localObject2 = localObject1;
          j++;
          localObject1 = localObject2;
        }
        else
        {
          CharacterStyle[] arrayOfCharacterStyle = (CharacterStyle[])((SpannableString)localObject2).getSpans(0, ((SpannableString)localObject2).length(), CharacterStyle.class);
          Object localObject3;
          if (SendersView.sElidedString.equals(((SpannableString)localObject2).toString()))
            localObject3 = copyStyles(arrayOfCharacterStyle, this.mElidedPaddingToken + localObject2 + this.mElidedPaddingToken);
          while (true)
          {
            localSpannableStringBuilder.append((CharSequence)localObject3);
            break;
            if ((localSpannableStringBuilder.length() > 0) && ((localObject1 == null) || (!SendersView.sElidedString.equals(localObject1.toString()))))
              localObject3 = copyStyles(arrayOfCharacterStyle, this.mSendersSplitToken + localObject2);
            else
              localObject3 = localObject2;
          }
        }
      }
      return localSpannableStringBuilder;
    }

    private static String filterTag(String paramString)
    {
      String str1 = paramString;
      if ((paramString.length() > 0) && (paramString.charAt(0) == '['))
      {
        int i = paramString.indexOf(']');
        if (i > 0)
        {
          String str2 = paramString.substring(1, i);
          str1 = "[" + Utils.ellipsize(str2, 7) + "]" + paramString.substring(i + 1);
        }
      }
      return str1;
    }

    private int getConversationCount()
    {
      while (true)
      {
        synchronized (WidgetService.sWidgetLock)
        {
          if (this.mConversationCursor != null)
          {
            i = this.mConversationCursor.getCount();
            int j = Math.min(i, 25);
            return j;
          }
        }
        int i = 0;
      }
    }

    private CharacterStyle getReadStyle()
    {
      if (this.mReadStyle == null)
        this.mReadStyle = new TextAppearanceSpan(this.mContext, 2131492941);
      return CharacterStyle.wrap(this.mReadStyle);
    }

    private CharacterStyle getUnreadStyle()
    {
      if (this.mUnreadStyle == null)
        this.mUnreadStyle = new TextAppearanceSpan(this.mContext, 2131492942);
      return CharacterStyle.wrap(this.mUnreadStyle);
    }

    private RemoteViews getViewMoreConversationsView()
    {
      RemoteViews localRemoteViews = new RemoteViews(this.mContext.getPackageName(), 2130968687);
      localRemoteViews.setTextViewText(2131689742, this.mContext.getText(2131427549));
      localRemoteViews.setOnClickFillInIntent(2131689741, Utils.createViewFolderIntent(this.mFolder, this.mAccount));
      return localRemoteViews;
    }

    private boolean isDataValid(Cursor paramCursor)
    {
      return (paramCursor != null) && (!paramCursor.isClosed()) && (paramCursor.moveToFirst());
    }

    public int getCount()
    {
      int i = 1;
      while (true)
      {
        synchronized (WidgetService.sWidgetLock)
        {
          int j = getConversationCount();
          if (this.mConversationCursor == null)
            break label86;
          k = this.mConversationCursor.getCount();
          if (j >= k)
          {
            if (j >= this.mFolderCount)
              break label92;
            break label80;
            this.mShouldShowViewMore = bool;
            if (!this.mShouldShowViewMore)
              break label98;
            int m = i + j;
            return m;
          }
        }
        label80: boolean bool = i;
        continue;
        label86: int k = 0;
        continue;
        label92: bool = false;
        continue;
        label98: i = 0;
      }
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public RemoteViews getLoadingView()
    {
      RemoteViews localRemoteViews = new RemoteViews(this.mContext.getPackageName(), 2130968687);
      localRemoteViews.setTextViewText(2131689742, this.mContext.getText(2131427550));
      return localRemoteViews;
    }

    public RemoteViews getViewAt(int paramInt)
    {
      synchronized (WidgetService.sWidgetLock)
      {
        if ((this.mConversationCursor == null) || (this.mConversationCursor.isClosed()) || ((this.mShouldShowViewMore) && (paramInt >= getConversationCount())))
        {
          RemoteViews localRemoteViews1 = getViewMoreConversationsView();
          return localRemoteViews1;
        }
        if (!this.mConversationCursor.moveToPosition(paramInt))
        {
          String str = WidgetService.LOG_TAG;
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = Integer.valueOf(paramInt);
          LogUtils.e(str, "Failed to move to position %d in the cursor.", arrayOfObject);
          RemoteViews localRemoteViews3 = getViewMoreConversationsView();
          return localRemoteViews3;
        }
      }
      Conversation localConversation = new Conversation(this.mConversationCursor);
      SpannableStringBuilder localSpannableStringBuilder1 = new SpannableStringBuilder();
      SpannableStringBuilder localSpannableStringBuilder2 = new SpannableStringBuilder();
      if (localConversation.conversationInfo != null)
      {
        localSpannableStringBuilder1 = ellipsizeStyledSenders(localConversation.conversationInfo, 25, SendersView.format(this.mContext, localConversation.conversationInfo, "", 25, new HtmlParser(), new HtmlTreeBuilder()));
        CharSequence localCharSequence = DateUtils.getRelativeTimeSpanString(this.mContext, localConversation.dateMs);
        RemoteViews localRemoteViews2 = this.mWidgetConversationViewBuilder.getStyledView(localSpannableStringBuilder2, localCharSequence, localConversation, this.mFolder, localSpannableStringBuilder1, filterTag(localConversation.subject));
        localRemoteViews2.setOnClickFillInIntent(2131689730, Utils.createViewConversationIntent(localConversation, this.mFolder, this.mAccount));
        return localRemoteViews2;
      }
      localSpannableStringBuilder1.append(localConversation.senders);
      if (localConversation.read);
      CharacterStyle localCharacterStyle;
      for (Object localObject3 = getReadStyle(); ; localObject3 = localCharacterStyle)
      {
        localSpannableStringBuilder1.setSpan(localObject3, 0, localSpannableStringBuilder1.length(), 0);
        break;
        localCharacterStyle = getUnreadStyle();
      }
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public boolean hasStableIds()
    {
      return false;
    }

    public void onCreate()
    {
      WidgetService.saveWidgetInformation(this.mContext, this.mAppWidgetId, this.mAccount, this.mFolder);
      if (!this.mService.isWidgetConfigured(this.mContext, this.mAppWidgetId, this.mAccount, this.mFolder))
        BaseWidgetProvider.updateWidget(this.mContext, this.mAppWidgetId, this.mAccount, this.mFolder);
      this.mFolderInformationShown = false;
      Uri localUri = this.mFolder.conversationListUri.buildUpon().appendQueryParameter("limit", Integer.toString(25)).appendQueryParameter("use_network", Boolean.FALSE.toString()).appendQueryParameter("all_notifications", Boolean.TRUE.toString()).build();
      Resources localResources = this.mContext.getResources();
      this.mConversationCursorLoader = new CursorLoader(this.mContext, localUri, UIProvider.CONVERSATION_PROJECTION, null, null, null);
      this.mConversationCursorLoader.registerListener(1, this);
      this.mConversationCursorLoader.setUpdateThrottle(localResources.getInteger(2131296287));
      this.mConversationCursorLoader.startLoading();
      this.mSendersSplitToken = localResources.getString(2131427587);
      this.mElidedPaddingToken = localResources.getString(2131427590);
      this.mFolderLoader = new CursorLoader(this.mContext, this.mFolder.uri, UIProvider.FOLDERS_PROJECTION, null, null, null);
      this.mFolderLoader.registerListener(0, this);
      this.mFolderUpdateHandler = new FolderUpdateHandler(localResources.getInteger(2131296288));
      this.mFolderUpdateHandler.scheduleTask();
    }

    public void onDataSetChanged()
    {
      this.mFolderUpdateHandler.scheduleTask();
    }

    public void onDestroy()
    {
      synchronized (WidgetService.sWidgetLock)
      {
        if (this.mConversationCursorLoader != null)
        {
          this.mConversationCursorLoader.reset();
          this.mConversationCursorLoader.unregisterListener(this);
          this.mConversationCursorLoader = null;
        }
        this.mConversationCursor = null;
        if (this.mFolderLoader != null)
        {
          this.mFolderLoader.reset();
          this.mFolderLoader.unregisterListener(this);
          this.mFolderLoader = null;
        }
        return;
      }
    }

    public void onLoadComplete(Loader<Cursor> paramLoader, Cursor paramCursor)
    {
      AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(this.mContext);
      RemoteViews localRemoteViews = new RemoteViews(this.mContext.getPackageName(), 2130968685);
      if (paramLoader == this.mFolderLoader)
        if (isDataValid(paramCursor));
      while (true)
      {
        return;
        int i = paramCursor.getInt(8);
        String str = paramCursor.getString(2);
        this.mFolderCount = paramCursor.getInt(9);
        if ((!this.mFolderInformationShown) && (!TextUtils.isEmpty(str)) && (!TextUtils.isEmpty(this.mAccount.name)))
        {
          this.mService.configureValidAccountWidget(this.mContext, localRemoteViews, this.mAppWidgetId, this.mAccount, this.mFolder, str);
          localAppWidgetManager.updateAppWidget(this.mAppWidgetId, localRemoteViews);
          this.mFolderInformationShown = true;
        }
        if (!TextUtils.isEmpty(str))
        {
          localRemoteViews.setViewVisibility(2131689722, 0);
          localRemoteViews.setTextViewText(2131689722, str);
        }
        while (true)
        {
          if (!TextUtils.isEmpty(this.mAccount.name))
            localRemoteViews.setTextViewText(2131689723, this.mAccount.name);
          localRemoteViews.setViewVisibility(2131689724, 0);
          localRemoteViews.setTextViewText(2131689724, Utils.getUnreadCountString(this.mContext, i));
          localAppWidgetManager.partiallyUpdateAppWidget(this.mAppWidgetId, localRemoteViews);
          return;
          LogUtils.e(WidgetService.LOG_TAG, "Empty folder name", new Object[0]);
        }
        if (paramLoader != this.mConversationCursorLoader)
          continue;
        synchronized (WidgetService.sWidgetLock)
        {
          if (!isDataValid(paramCursor))
          {
            this.mConversationCursor = null;
            localAppWidgetManager.notifyAppWidgetViewDataChanged(this.mAppWidgetId, 2131689727);
            if ((this.mConversationCursor != null) && (this.mConversationCursor.getCount() != 0))
              continue;
            localRemoteViews.setTextViewText(2131689726, this.mContext.getString(2131427487));
            localAppWidgetManager.partiallyUpdateAppWidget(this.mAppWidgetId, localRemoteViews);
            return;
          }
          this.mConversationCursor = paramCursor;
        }
      }
    }

    private class FolderUpdateHandler extends DelayedTaskHandler
    {
      public FolderUpdateHandler(int arg2)
      {
        super(i);
      }

      protected void performTask()
      {
        if (WidgetService.MailFactory.this.mFolderLoader != null)
          WidgetService.MailFactory.this.mFolderLoader.startLoading();
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.widget.WidgetService
 * JD-Core Version:    0.6.2
 */