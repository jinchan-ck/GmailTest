package com.android.mail.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import com.android.mail.providers.Account;
import com.android.mail.providers.AccountObserver;
import com.android.mail.providers.Folder;
import com.android.mail.providers.Settings;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.LruCache;
import com.android.mail.utils.Utils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class RecentFolderList
{
  private static final Comparator<Folder> ALPHABET_IGNORECASE;
  private Account mAccount = null;
  private final AccountObserver mAccountObserver = new AccountObserver()
  {
    public void onChanged(Account paramAnonymousAccount)
    {
      RecentFolderList.this.setCurrentAccount(paramAnonymousAccount);
    }
  };
  private final Context mContext;
  private final LruCache<String, RecentFolderListEntry> mFolderCache = new LruCache(7);

  static
  {
    if (!RecentFolderList.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      ALPHABET_IGNORECASE = new Comparator()
      {
        public int compare(Folder paramAnonymousFolder1, Folder paramAnonymousFolder2)
        {
          return paramAnonymousFolder1.name.compareToIgnoreCase(paramAnonymousFolder2.name);
        }
      };
      return;
    }
  }

  public RecentFolderList(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private void setCurrentAccount(Account paramAccount)
  {
    if ((this.mAccount == null) || (!this.mAccount.matches(paramAccount)));
    for (int i = 1; ; i = 0)
    {
      this.mAccount = paramAccount;
      if (i != 0)
        this.mFolderCache.clear();
      return;
    }
  }

  public void destroy()
  {
    this.mAccountObserver.unregisterAndDestroy();
  }

  public ArrayList<Folder> getRecentFolderList(Uri paramUri)
  {
    ArrayList localArrayList1 = new ArrayList();
    if (paramUri != null)
      localArrayList1.add(paramUri);
    if (this.mAccount == null);
    for (Uri localUri = Uri.EMPTY; ; localUri = Settings.getDefaultInboxUri(this.mAccount.settings))
    {
      if (!localUri.equals(Uri.EMPTY))
        localArrayList1.add(localUri);
      ArrayList localArrayList2 = Lists.newArrayList();
      localArrayList2.addAll(this.mFolderCache.values());
      Collections.sort(localArrayList2);
      ArrayList localArrayList3 = Lists.newArrayList();
      Iterator localIterator = localArrayList2.iterator();
      do
      {
        if (!localIterator.hasNext())
          break;
        RecentFolderListEntry localRecentFolderListEntry = (RecentFolderListEntry)localIterator.next();
        if (!localArrayList1.contains(localRecentFolderListEntry.mFolder.uri))
          localArrayList3.add(localRecentFolderListEntry.mFolder);
      }
      while (localArrayList3.size() != 5);
      Collections.sort(localArrayList3, ALPHABET_IGNORECASE);
      return localArrayList3;
    }
  }

  public void initialize(ControllableActivity paramControllableActivity)
  {
    setCurrentAccount(this.mAccountObserver.initialize(paramControllableActivity.getAccountController()));
  }

  public void loadFromUiProvider(Cursor paramCursor)
  {
    if ((this.mAccount == null) || (paramCursor == null))
    {
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = this.mAccount;
      arrayOfObject1[1] = paramCursor;
      LogUtils.e("RecentFolderList", "RecentFolderList.loadFromUiProvider: bad input. mAccount=%s,cursor=%s", arrayOfObject1);
      return;
    }
    Object[] arrayOfObject2 = new Object[1];
    arrayOfObject2[0] = Integer.valueOf(paramCursor.getCount());
    LogUtils.d("RecentFolderList", "Number of recents = %d", arrayOfObject2);
    if (!paramCursor.moveToLast())
    {
      LogUtils.e("RecentFolderList", "Not able to move to last in recent labels cursor", new Object[0]);
      return;
    }
    do
    {
      Folder localFolder = new Folder(paramCursor);
      RecentFolderListEntry localRecentFolderListEntry = new RecentFolderListEntry(localFolder);
      this.mFolderCache.putElement(localFolder.uri.toString(), localRecentFolderListEntry);
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = this.mAccount.name;
      arrayOfObject3[1] = localFolder.name;
      LogUtils.v("RecentFolderList", "Account %s, Recent: %s", arrayOfObject3);
    }
    while (paramCursor.moveToPrevious());
  }

  public void touchFolder(Folder paramFolder, Account paramAccount)
  {
    if ((this.mAccount == null) || (!this.mAccount.equals(paramAccount)))
    {
      if (paramAccount != null)
        setCurrentAccount(paramAccount);
    }
    else
    {
      if (($assertionsDisabled) || (paramFolder != null))
        break label58;
      throw new AssertionError();
    }
    LogUtils.w("RecentFolderList", "No account set for setting recent folders?", new Object[0]);
    return;
    label58: RecentFolderListEntry localRecentFolderListEntry = new RecentFolderListEntry(paramFolder);
    this.mFolderCache.putElement(paramFolder.uri.toString(), localRecentFolderListEntry);
    new StoreRecent(this.mAccount, paramFolder).execute(new Void[0]);
  }

  private static class RecentFolderListEntry
    implements Comparable<RecentFolderListEntry>
  {
    private static final AtomicInteger SEQUENCE_GENERATOR = new AtomicInteger();
    private final Folder mFolder;
    private final int mSequence;

    RecentFolderListEntry(Folder paramFolder)
    {
      this.mFolder = paramFolder;
      this.mSequence = SEQUENCE_GENERATOR.getAndIncrement();
    }

    public int compareTo(RecentFolderListEntry paramRecentFolderListEntry)
    {
      return paramRecentFolderListEntry.mSequence - this.mSequence;
    }
  }

  private class StoreRecent extends AsyncTask<Void, Void, Void>
  {
    private final Account mAccount;
    private final Folder mFolder;

    static
    {
      if (!RecentFolderList.class.desiredAssertionStatus());
      for (boolean bool = true; ; bool = false)
      {
        $assertionsDisabled = bool;
        return;
      }
    }

    public StoreRecent(Account paramFolder, Folder arg3)
    {
      Object localObject;
      assert ((paramFolder != null) && (localObject != null));
      this.mAccount = paramFolder;
      this.mFolder = localObject;
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      Uri localUri = this.mAccount.recentFolderListUri;
      if (!Utils.isEmpty(localUri))
      {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put(this.mFolder.uri.toString(), Integer.valueOf(0));
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mFolder.name;
        LogUtils.i("RecentFolderList", "Save: %s", arrayOfObject);
        RecentFolderList.this.mContext.getContentResolver().update(localUri, localContentValues, null, null);
      }
      return null;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.RecentFolderList
 * JD-Core Version:    0.6.2
 */