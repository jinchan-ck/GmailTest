package com.google.android.gm.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LabelList
{
  private static Map<LabelChangeObserver, Set<LabelList>> sAutoUpdateLists = Maps.newHashMap();
  private static Map<Uri, LabelChangeObserver> sLabelChangeObservers;
  private static Object sLabelListObserverLock = new Object();
  private final String mAccount;
  private final Context mContext;
  private final DataSetObservable mDataSetObservable = new DataSetObservable();
  private final boolean mIncludeHiddenLabels;
  private Uri mLabelUri;
  private final ArrayList<Label> mList = new ArrayList();

  static
  {
    sLabelChangeObservers = Maps.newHashMap();
  }

  LabelList(Context paramContext, String paramString, Uri paramUri, boolean paramBoolean)
  {
    this.mContext = paramContext;
    this.mAccount = paramString;
    this.mIncludeHiddenLabels = paramBoolean;
    if (paramContext == null)
      LogUtils.e("Gmail", "Attempt to construct LabelList without context. Uri: %s", new Object[] { paramUri });
    Cursor localCursor;
    do
    {
      return;
      localCursor = paramContext.getContentResolver().query(paramUri, Gmail.LABEL_PROJECTION, null, null, null);
    }
    while (localCursor == null);
    try
    {
      LabelManager.LabelFactory localLabelFactory = new LabelManager.LabelFactory(paramContext, paramString, localCursor);
      while (localCursor.moveToNext())
      {
        Label localLabel = localLabelFactory.newLabel(localCursor);
        this.mList.add(localLabel);
      }
    }
    finally
    {
      localCursor.close();
    }
    localCursor.close();
  }

  private void onLabelDataSetChanged()
  {
    this.mDataSetObservable.notifyChanged();
  }

  public Label get(int paramInt)
  {
    return (Label)this.mList.get(paramInt);
  }

  public Label get(String paramString)
  {
    if (paramString != null)
    {
      int i = size();
      for (int j = 0; j < i; j++)
      {
        Label localLabel = get(j);
        if (paramString.equals(localLabel.getCanonicalName()))
          return localLabel;
      }
    }
    return null;
  }

  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mDataSetObservable.registerObserver(paramDataSetObserver);
  }

  public void registerForLabelChanges()
  {
    this.mLabelUri = Gmail.getLabelUri(this.mAccount);
    synchronized (sLabelListObserverLock)
    {
      LabelChangeObserver localLabelChangeObserver = (LabelChangeObserver)sLabelChangeObservers.get(this.mLabelUri);
      if (localLabelChangeObserver == null)
      {
        localLabelChangeObserver = new LabelChangeObserver(this.mContext, this.mAccount, this.mIncludeHiddenLabels);
        sLabelChangeObservers.put(this.mLabelUri, localLabelChangeObserver);
      }
      Object localObject3 = (Set)sAutoUpdateLists.get(localLabelChangeObserver);
      if (localObject3 == null)
      {
        localObject3 = Sets.newHashSet();
        sAutoUpdateLists.put(localLabelChangeObserver, localObject3);
      }
      ((Set)localObject3).add(this);
      this.mContext.getContentResolver().registerContentObserver(this.mLabelUri, true, localLabelChangeObserver);
      return;
    }
  }

  public int size()
  {
    return this.mList.size();
  }

  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mDataSetObservable.unregisterObserver(paramDataSetObserver);
  }

  public void unregisterForLabelChanges()
  {
    synchronized (sLabelListObserverLock)
    {
      LabelChangeObserver localLabelChangeObserver = (LabelChangeObserver)sLabelChangeObservers.get(this.mLabelUri);
      if (localLabelChangeObserver == null)
        return;
      Set localSet = (Set)sAutoUpdateLists.get(localLabelChangeObserver);
      int i = 0;
      if (localSet != null)
      {
        localSet.remove(this);
        int j = localSet.size();
        i = 0;
        if (j == 0)
        {
          sAutoUpdateLists.remove(localLabelChangeObserver);
          i = 1;
        }
      }
      if (i != 0)
      {
        this.mContext.getContentResolver().unregisterContentObserver(localLabelChangeObserver);
        return;
      }
    }
  }

  private static class LabelChangeObserver extends ContentObserver
  {
    private final String mAccount;
    private final Context mContext;
    private final boolean mIncludeHiddenLabels;
    private UpdateListTask mListRefreshTask = null;
    private boolean mUpdateRequested = false;

    public LabelChangeObserver(Context paramContext, String paramString, boolean paramBoolean)
    {
      super();
      this.mContext = paramContext.getApplicationContext();
      this.mAccount = paramString;
      this.mIncludeHiddenLabels = paramBoolean;
    }

    private void startUpdateTask()
    {
      this.mListRefreshTask = new UpdateListTask();
      this.mListRefreshTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
      this.mUpdateRequested = false;
    }

    private void updateAllRegisteredLists(LabelList paramLabelList)
    {
      synchronized (LabelList.sLabelListObserverLock)
      {
        Set localSet = (Set)LabelList.sAutoUpdateLists.get(this);
        if (localSet == null)
          return;
        Iterator localIterator = Sets.newHashSet(localSet).iterator();
        if (localIterator.hasNext())
        {
          LabelList localLabelList = (LabelList)localIterator.next();
          localLabelList.mList.clear();
          localLabelList.mList.addAll(paramLabelList.mList);
          localLabelList.onLabelDataSetChanged();
        }
      }
    }

    public boolean deliverSelfNotifications()
    {
      return true;
    }

    public void onChange(boolean paramBoolean)
    {
      if (this.mListRefreshTask == null)
      {
        startUpdateTask();
        return;
      }
      this.mUpdateRequested = true;
    }

    class UpdateListTask extends AsyncTask<Void, Void, LabelList>
    {
      UpdateListTask()
      {
      }

      protected LabelList doInBackground(Void[] paramArrayOfVoid)
      {
        return LabelManager.getLabelList(LabelList.LabelChangeObserver.this.mContext, LabelList.LabelChangeObserver.this.mAccount, null, LabelList.LabelChangeObserver.this.mIncludeHiddenLabels);
      }

      protected void onPostExecute(LabelList paramLabelList)
      {
        LabelList.LabelChangeObserver.this.updateAllRegisteredLists(paramLabelList);
        LabelList.LabelChangeObserver.access$802(LabelList.LabelChangeObserver.this, null);
        if (LabelList.LabelChangeObserver.this.mUpdateRequested)
          LabelList.LabelChangeObserver.this.startUpdateTask();
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.LabelList
 * JD-Core Version:    0.6.2
 */