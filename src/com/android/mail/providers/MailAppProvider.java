package com.android.mail.providers;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.MatrixCursorWithExtra;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class MailAppProvider extends ContentProvider
  implements Loader.OnLoadCompleteListener<Cursor>
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private static final Set<Uri> PENDING_ACCOUNT_URIS = Sets.newHashSet();
  private static String sAuthority;
  private static MailAppProvider sInstance;
  private final Map<Uri, AccountCacheEntry> mAccountCache = Maps.newHashMap();
  private volatile boolean mAccountsFullyLoaded = false;
  private final Map<Uri, CursorLoader> mCursorLoaderMap = Maps.newHashMap();
  private ContentResolver mResolver;
  private SharedPreferences mSharedPrefs;

  private void addAccountImpl(Account paramAccount, Uri paramUri, int paramInt, boolean paramBoolean)
  {
    Map localMap = this.mAccountCache;
    if (paramAccount != null);
    try
    {
      LogUtils.v(LOG_TAG, "adding account %s", new Object[] { paramAccount });
      this.mAccountCache.put(paramAccount.uri, new AccountCacheEntry(paramAccount, paramUri, paramInt));
      if (paramBoolean)
        broadcastAccountChange();
      cacheAccountList();
      return;
    }
    finally
    {
    }
  }

  private void addAccountImpl(Account paramAccount, Uri paramUri, boolean paramBoolean)
  {
    addAccountImpl(paramAccount, paramUri, this.mAccountCache.size(), paramBoolean);
  }

  public static void addAccountsForUriAsync(Uri paramUri)
  {
    synchronized (PENDING_ACCOUNT_URIS)
    {
      MailAppProvider localMailAppProvider = getInstance();
      if (localMailAppProvider != null)
      {
        localMailAppProvider.startAccountsLoader(paramUri);
        return;
      }
      PENDING_ACCOUNT_URIS.add(paramUri);
    }
  }

  private static void broadcastAccountChange()
  {
    MailAppProvider localMailAppProvider = sInstance;
    if (localMailAppProvider != null)
      localMailAppProvider.mResolver.notifyChange(getAccountsUri(), null);
  }

  private void cacheAccountList()
  {
    ArrayList localArrayList = Lists.newArrayList();
    HashSet localHashSet;
    synchronized (this.mAccountCache)
    {
      localArrayList.addAll(this.mAccountCache.values());
      Collections.sort(localArrayList);
      localHashSet = Sets.newHashSet();
      Iterator localIterator = localArrayList.iterator();
      if (localIterator.hasNext())
        localHashSet.add(((AccountCacheEntry)localIterator.next()).serialize());
    }
    SharedPreferences.Editor localEditor = getPreferences().edit();
    localEditor.putStringSet("accountList", localHashSet);
    localEditor.apply();
  }

  public static Account getAccountFromAccountUri(Uri paramUri)
  {
    MailAppProvider localMailAppProvider = getInstance();
    if ((localMailAppProvider != null) && (localMailAppProvider.mAccountsFullyLoaded))
      synchronized (localMailAppProvider.mAccountCache)
      {
        AccountCacheEntry localAccountCacheEntry = (AccountCacheEntry)localMailAppProvider.mAccountCache.get(paramUri);
        if (localAccountCacheEntry != null)
        {
          Account localAccount = localAccountCacheEntry.mAccount;
          return localAccount;
        }
      }
    return null;
  }

  public static Uri getAccountsUri()
  {
    return Uri.parse("content://" + sAuthority + "/");
  }

  public static MailAppProvider getInstance()
  {
    return sInstance;
  }

  public static Intent getNoAccountIntent(Context paramContext)
  {
    return getInstance().getNoAccountsIntent(paramContext);
  }

  private SharedPreferences getPreferences()
  {
    if (this.mSharedPrefs == null)
      this.mSharedPrefs = getContext().getSharedPreferences("MailAppProvider", 0);
    return this.mSharedPrefs;
  }

  private void loadCachedAccountList()
  {
    Set localSet = getPreferences().getStringSet("accountList", null);
    if (localSet != null)
    {
      ArrayList localArrayList = Lists.newArrayList();
      Iterator localIterator1 = localSet.iterator();
      while (localIterator1.hasNext())
      {
        String str2 = (String)localIterator1.next();
        try
        {
          localArrayList.add(new AccountCacheEntry(str2));
        }
        catch (Exception localException)
        {
          LogUtils.e(LOG_TAG, localException, "Unable to create account object from serialized string '%s'", new Object[] { str2 });
        }
      }
      Collections.sort(localArrayList);
      Iterator localIterator2 = localArrayList.iterator();
      while (localIterator2.hasNext())
      {
        AccountCacheEntry localAccountCacheEntry = (AccountCacheEntry)localIterator2.next();
        if (localAccountCacheEntry.mAccount.settings != null)
        {
          Account localAccount = localAccountCacheEntry.mAccount;
          ContentProviderClient localContentProviderClient = this.mResolver.acquireContentProviderClient(localAccount.uri);
          if (localContentProviderClient != null)
          {
            localContentProviderClient.release();
            addAccountImpl(localAccount, localAccountCacheEntry.mAccountsQueryUri, false);
          }
          else
          {
            String str1 = LOG_TAG;
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = localAccount.name;
            LogUtils.e(str1, "Dropping account without provider: %s", arrayOfObject);
          }
        }
        else
        {
          LogUtils.e(LOG_TAG, "Dropping account that doesn't specify settings", new Object[0]);
        }
      }
      broadcastAccountChange();
    }
  }

  private void removeAccounts(Set<Uri> paramSet, boolean paramBoolean)
  {
    synchronized (this.mAccountCache)
    {
      Iterator localIterator = paramSet.iterator();
      if (localIterator.hasNext())
      {
        Uri localUri = (Uri)localIterator.next();
        LogUtils.d(LOG_TAG, "Removing account %s", new Object[] { localUri });
        this.mAccountCache.remove(localUri);
      }
    }
    if (paramBoolean)
      broadcastAccountChange();
    cacheAccountList();
  }

  private void startAccountsLoader(Uri paramUri)
  {
    try
    {
      CursorLoader localCursorLoader1 = new CursorLoader(getContext(), paramUri, UIProvider.ACCOUNTS_PROJECTION, null, null, null);
      localCursorLoader1.registerListener(paramUri.hashCode(), this);
      localCursorLoader1.startLoading();
      CursorLoader localCursorLoader2 = (CursorLoader)this.mCursorLoaderMap.get(paramUri);
      if (localCursorLoader2 != null)
        localCursorLoader2.stopLoading();
      this.mCursorLoaderMap.put(paramUri, localCursorLoader1);
      return;
    }
    finally
    {
    }
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }

  protected abstract String getAuthority();

  public String getLastSentFromAccount()
  {
    return getPreferences().getString("lastSendFromAccount", null);
  }

  public String getLastViewedAccount()
  {
    return getPreferences().getString("lastViewedAccount", null);
  }

  protected abstract Intent getNoAccountsIntent(Context paramContext);

  public String getType(Uri paramUri)
  {
    return null;
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    return paramUri;
  }

  public boolean onCreate()
  {
    sAuthority = getAuthority();
    this.mResolver = getContext().getContentResolver();
    Intent localIntent = new Intent("com.google.android.gm2.providers.protos.boot.intent.ACTION_PROVIDER_CREATED");
    getContext().sendBroadcast(localIntent);
    loadCachedAccountList();
    synchronized (PENDING_ACCOUNT_URIS)
    {
      sInstance = this;
      ImmutableSet localImmutableSet = ImmutableSet.copyOf(PENDING_ACCOUNT_URIS);
      PENDING_ACCOUNT_URIS.clear();
      Iterator localIterator = localImmutableSet.iterator();
      if (localIterator.hasNext())
        addAccountsForUriAsync((Uri)localIterator.next());
    }
    return true;
  }

  public void onLoadComplete(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (paramCursor == null)
    {
      LogUtils.d(LOG_TAG, "null account cursor returned", new Object[0]);
      return;
    }
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramCursor.getCount());
    LogUtils.d(str, "Cursor with %d accounts returned", arrayOfObject);
    Uri localUri1 = ((CursorLoader)paramLoader).getUri();
    int i;
    HashSet localHashSet1;
    synchronized (this.mAccountCache)
    {
      ImmutableSet localImmutableSet = ImmutableSet.copyOf(this.mAccountCache.values());
      i = -1;
      localHashSet1 = Sets.newHashSet();
      Iterator localIterator = localImmutableSet.iterator();
      while (localIterator.hasNext())
      {
        AccountCacheEntry localAccountCacheEntry2 = (AccountCacheEntry)localIterator.next();
        if (localUri1.equals(localAccountCacheEntry2.mAccountsQueryUri))
          localHashSet1.add(localAccountCacheEntry2.mAccount.uri);
        if (localAccountCacheEntry2.mPosition > i)
          i = localAccountCacheEntry2.mPosition;
      }
    }
    boolean bool1;
    HashSet localHashSet2;
    int j;
    Account localAccount;
    Uri localUri2;
    Integer localInteger;
    if (paramCursor.getExtras().getInt("accounts_loaded") != 0)
    {
      bool1 = true;
      this.mAccountsFullyLoaded = bool1;
      localHashSet2 = Sets.newHashSet();
      j = i + 1;
      if (!paramCursor.moveToNext())
        break label371;
      localAccount = new Account(paramCursor);
      localUri2 = localAccount.uri;
      localHashSet2.add(localUri2);
      boolean bool2 = this.mAccountsFullyLoaded;
      localInteger = null;
      if (bool2);
    }
    while (true)
    {
      synchronized (this.mAccountCache)
      {
        AccountCacheEntry localAccountCacheEntry1 = (AccountCacheEntry)this.mAccountCache.get(localUri2);
        localInteger = null;
        if (localAccountCacheEntry1 != null)
          localInteger = Integer.valueOf(localAccountCacheEntry1.mPosition);
        if (localInteger != null)
        {
          m = localInteger.intValue();
          addAccountImpl(localAccount, localUri1, m, false);
          break;
          bool1 = false;
        }
      }
      int k = j + 1;
      int m = j;
      j = k;
    }
    label371: localHashSet1.removeAll(localHashSet2);
    if ((localHashSet1.size() > 0) && (this.mAccountsFullyLoaded))
      removeAccounts(localHashSet1, false);
    broadcastAccountChange();
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    String[] arrayOfString = UIProviderValidator.validateAccountProjection(paramArrayOfString1);
    Bundle localBundle = new Bundle();
    int i;
    if (this.mAccountsFullyLoaded)
      i = 1;
    MatrixCursorWithExtra localMatrixCursorWithExtra;
    Account localAccount;
    MatrixCursor.RowBuilder localRowBuilder;
    int k;
    while (true)
    {
      localBundle.putInt("accounts_loaded", i);
      ArrayList localArrayList = Lists.newArrayList();
      synchronized (this.mAccountCache)
      {
        localArrayList.addAll(this.mAccountCache.values());
        Collections.sort(localArrayList);
        localMatrixCursorWithExtra = new MatrixCursorWithExtra(arrayOfString, localArrayList.size(), localBundle);
        Iterator localIterator = localArrayList.iterator();
        int j;
        do
        {
          if (!localIterator.hasNext())
            break;
          localAccount = ((AccountCacheEntry)localIterator.next()).mAccount;
          localRowBuilder = localMatrixCursorWithExtra.newRow();
          j = arrayOfString.length;
          k = 0;
        }
        while (k >= j);
        String str = arrayOfString[k];
        switch (UIProvider.getAccountColumn(str))
        {
        default:
          throw new IllegalStateException("Column not found: " + str);
          i = 0;
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 15:
        case 16:
        case 17:
        case 14:
        case 18:
        case 19:
        case 20:
        case 22:
        case 23:
        case 24:
        case 25:
        case 21:
        case 26:
        case 27:
        case 28:
        case 30:
        case 31:
        case 32:
        case 33:
        case 34:
        case 35:
        case 36:
        case 29:
        case 37:
        case 38:
        case 39:
        case 40:
        case 41:
        case 42:
        case 43:
        }
      }
    }
    localRowBuilder.add(Integer.valueOf(0));
    while (true)
    {
      k++;
      break;
      localRowBuilder.add(localAccount.name);
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.providerVersion));
      continue;
      localRowBuilder.add(localAccount.uri);
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.capabilities));
      continue;
      localRowBuilder.add(localAccount.folderListUri);
      continue;
      localRowBuilder.add(localAccount.fullFolderListUri);
      continue;
      localRowBuilder.add(localAccount.searchUri);
      continue;
      localRowBuilder.add(localAccount.accountFromAddresses);
      continue;
      localRowBuilder.add(localAccount.saveDraftUri);
      continue;
      localRowBuilder.add(localAccount.sendMessageUri);
      continue;
      localRowBuilder.add(localAccount.expungeMessageUri);
      continue;
      localRowBuilder.add(localAccount.undoUri);
      continue;
      localRowBuilder.add(localAccount.settingsIntentUri);
      continue;
      localRowBuilder.add(localAccount.helpIntentUri);
      continue;
      localRowBuilder.add(localAccount.sendFeedbackIntentUri);
      continue;
      localRowBuilder.add(localAccount.reauthenticationIntentUri);
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.syncStatus));
      continue;
      localRowBuilder.add(localAccount.composeIntentUri);
      continue;
      localRowBuilder.add(localAccount.mimeType);
      continue;
      localRowBuilder.add(localAccount.recentFolderListUri);
      continue;
      localRowBuilder.add(localAccount.defaultRecentFolderListUri);
      continue;
      localRowBuilder.add(localAccount.manualSyncUri);
      continue;
      localRowBuilder.add(localAccount.viewIntentProxyUri);
      continue;
      localRowBuilder.add(localAccount.accoutCookieQueryUri);
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.color));
      continue;
      localRowBuilder.add(localAccount.settings.signature);
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.settings.getAutoAdvanceSetting()));
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.settings.messageTextSize));
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.settings.replyBehavior));
      continue;
      if (localAccount.settings.hideCheckboxes);
      for (int i4 = 1; ; i4 = 0)
      {
        localRowBuilder.add(Integer.valueOf(i4));
        break;
      }
      if (localAccount.settings.confirmDelete);
      for (int i3 = 1; ; i3 = 0)
      {
        localRowBuilder.add(Integer.valueOf(i3));
        break;
      }
      if (localAccount.settings.confirmArchive);
      for (int i2 = 1; ; i2 = 0)
      {
        localRowBuilder.add(Integer.valueOf(i2));
        break;
      }
      if (localAccount.settings.confirmSend);
      for (int i1 = 1; ; i1 = 0)
      {
        localRowBuilder.add(Integer.valueOf(i1));
        break;
      }
      localRowBuilder.add(localAccount.settings.defaultInbox);
      continue;
      localRowBuilder.add(localAccount.settings.defaultInboxName);
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.settings.snapHeaders));
      continue;
      if (localAccount.settings.forceReplyFromDefault);
      for (int n = 1; ; n = 0)
      {
        localRowBuilder.add(Integer.valueOf(n));
        break;
      }
      localRowBuilder.add(Integer.valueOf(localAccount.settings.maxAttachmentSize));
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.settings.swipe));
      continue;
      if (localAccount.settings.priorityArrowsEnabled);
      for (int m = 1; ; m = 0)
      {
        localRowBuilder.add(Integer.valueOf(m));
        break;
      }
      localRowBuilder.add(localAccount.settings.setupIntentUri);
      continue;
      localRowBuilder.add(Integer.valueOf(localAccount.settings.conversationViewMode));
      continue;
      localRowBuilder.add(localAccount.updateSettingsUri);
    }
    localMatrixCursorWithExtra.setNotificationUri(this.mResolver, getAccountsUri());
    return localMatrixCursorWithExtra;
  }

  public void setLastSentFromAccount(String paramString)
  {
    SharedPreferences.Editor localEditor = getPreferences().edit();
    localEditor.putString("lastSendFromAccount", paramString);
    localEditor.apply();
  }

  public void setLastViewedAccount(String paramString)
  {
    SharedPreferences.Editor localEditor = getPreferences().edit();
    localEditor.putString("lastViewedAccount", paramString);
    localEditor.apply();
  }

  public void shutdown()
  {
    synchronized (PENDING_ACCOUNT_URIS)
    {
      sInstance = null;
      Iterator localIterator = this.mCursorLoaderMap.values().iterator();
      if (localIterator.hasNext())
        ((CursorLoader)localIterator.next()).stopLoading();
    }
    this.mCursorLoaderMap.clear();
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }

  private static class AccountCacheEntry
    implements Comparable<AccountCacheEntry>
  {
    private static final Pattern ACCOUNT_ENTRY_COMPONENT_SEPARATOR_PATTERN = Pattern.compile("\\^\\*\\*\\^");
    final Account mAccount;
    final Uri mAccountsQueryUri;
    final int mPosition;

    public AccountCacheEntry(Account paramAccount, Uri paramUri, int paramInt)
    {
      this.mAccount = paramAccount;
      this.mAccountsQueryUri = paramUri;
      this.mPosition = paramInt;
    }

    public AccountCacheEntry(String paramString)
      throws IllegalArgumentException
    {
      String[] arrayOfString = TextUtils.split(paramString, ACCOUNT_ENTRY_COMPONENT_SEPARATOR_PATTERN);
      if (arrayOfString.length != 3)
        throw new IllegalArgumentException("AccountCacheEntry de-serializing failed. Wrong number of members detected. " + arrayOfString.length + " detected");
      this.mAccount = Account.newinstance(arrayOfString[0]);
      if (this.mAccount == null)
        throw new IllegalArgumentException("AccountCacheEntry de-serializing failed. Account object could not be created from the serialized string: " + paramString);
      if (this.mAccount.settings == Settings.EMPTY_SETTINGS)
        throw new IllegalArgumentException("AccountCacheEntry de-serializing failed. Settings could not be created from the string: " + paramString);
      this.mPosition = Integer.parseInt(arrayOfString[1]);
      if (!TextUtils.isEmpty(arrayOfString[2]));
      for (Uri localUri = Uri.parse(arrayOfString[2]); ; localUri = null)
      {
        this.mAccountsQueryUri = localUri;
        return;
      }
    }

    public int compareTo(AccountCacheEntry paramAccountCacheEntry)
    {
      return this.mPosition - paramAccountCacheEntry.mPosition;
    }

    public String serialize()
    {
      try
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(this.mAccount.serialize()).append("^**^");
        localStringBuilder.append(this.mPosition).append("^**^");
        if (this.mAccountsQueryUri != null);
        for (String str1 = this.mAccountsQueryUri.toString(); ; str1 = "")
        {
          localStringBuilder.append(str1);
          String str2 = localStringBuilder.toString();
          return str2;
        }
      }
      finally
      {
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.MailAppProvider
 * JD-Core Version:    0.6.2
 */