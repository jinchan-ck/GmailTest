package com.google.android.gm;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import com.google.android.gm.provider.Label;
import com.google.android.gm.provider.LabelList;
import com.google.android.gm.provider.LabelManager;
import com.google.android.gm.provider.LogUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecentLabelsCache
{
  private static RecentLabelsCache sInstance;
  private String mAccount;
  private final Context mContext;
  private String[] mDefaultRecentLabels;
  private final Comparator<Label> mDisplayComparator;
  private final Map<String, Long> mMap;
  private final Set<RecentLabelList> mUpdateLists = Sets.newHashSet();

  private RecentLabelsCache(Context paramContext)
  {
    this.mContext = paramContext;
    this.mMap = Maps.newHashMap();
    this.mDisplayComparator = new Comparator()
    {
      public int compare(Label paramAnonymousLabel1, Label paramAnonymousLabel2)
      {
        return paramAnonymousLabel1.getName().compareToIgnoreCase(paramAnonymousLabel2.getName());
      }
    };
  }

  public static RecentLabelsCache getInstance(Context paramContext)
  {
    try
    {
      if (sInstance == null)
        sInstance = new RecentLabelsCache(paramContext.getApplicationContext());
      RecentLabelsCache localRecentLabelsCache = sInstance;
      return localRecentLabelsCache;
    }
    finally
    {
    }
  }

  private List<String> getSortedRecentCanonicalNames(LabelList paramLabelList)
  {
    ArrayList localArrayList1 = Lists.newArrayList();
    for (int i = 0; i < paramLabelList.size(); i++)
      localArrayList1.add(paramLabelList.get(i));
    Collections.sort(localArrayList1, new Comparator()
    {
      public int compare(Label paramAnonymousLabel1, Label paramAnonymousLabel2)
      {
        long l1 = paramAnonymousLabel1.getLastTouched();
        long l2 = paramAnonymousLabel2.getLastTouched();
        if (l1 < l2)
          return -1;
        if (l1 == l2)
          return 0;
        return 1;
      }
    });
    ArrayList localArrayList2 = Lists.newArrayList();
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
      localArrayList2.add(((Label)localIterator.next()).getCanonicalName());
    return localArrayList2;
  }

  void clear()
  {
    this.mMap.clear();
  }

  public Comparator<Label> getRecentLabelDisplayComparator()
  {
    return this.mDisplayComparator;
  }

  public RecentLabelList getRecentLabelNames(String paramString, int paramInt, UiHandler paramUiHandler)
  {
    return getRecentLabelNames(paramString, paramInt, paramUiHandler, true);
  }

  RecentLabelList getRecentLabelNames(String paramString, int paramInt, UiHandler paramUiHandler, boolean paramBoolean)
  {
    List localList = getSortedRecentCanonicalNames(getRecentLabels(paramString, paramInt));
    if ((paramBoolean) && (localList.isEmpty()))
    {
      if (this.mDefaultRecentLabels == null)
        this.mDefaultRecentLabels = this.mContext.getResources().getStringArray(2131558405);
      final String[] arrayOfString = this.mDefaultRecentLabels;
      if (arrayOfString.length > 0)
      {
        int i = arrayOfString.length;
        for (int j = 0; j < i; j++)
          localList.add(arrayOfString[j]);
        paramUiHandler.post(new Runnable()
        {
          public void run()
          {
            for (String str : arrayOfString)
              RecentLabelsCache.this.touch(str);
          }
        });
      }
    }
    return new RecentLabelList(localList, paramInt, null);
  }

  public RecentLabelList getRecentLabelNames(String paramString, UiHandler paramUiHandler)
  {
    return getRecentLabelNames(paramString, 5, paramUiHandler);
  }

  LabelList getRecentLabels(String paramString, int paramInt)
  {
    if ((this.mAccount != null) && (!this.mAccount.equals(paramString)))
    {
      save();
      this.mUpdateLists.clear();
    }
    this.mAccount = paramString;
    return LabelManager.getRecentLabelList(this.mContext, paramString, System.currentTimeMillis(), paramInt);
  }

  public void save()
  {
    if (!this.mMap.isEmpty())
    {
      HashMap localHashMap = Maps.newHashMap(this.mMap);
      new SaveTask(this.mContext, this.mAccount, localHashMap).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
      this.mMap.clear();
    }
  }

  void saveSync()
  {
    LabelManager.updateRecentLabels(this.mContext, this.mAccount, this.mMap);
  }

  public void touch(String paramString)
  {
    touch(paramString, System.currentTimeMillis());
  }

  void touch(String paramString, long paramLong)
  {
    this.mMap.put(paramString, Long.valueOf(paramLong));
    Iterator localIterator = this.mUpdateLists.iterator();
    while (localIterator.hasNext())
    {
      RecentLabelList localRecentLabelList = (RecentLabelList)localIterator.next();
      localRecentLabelList.addLabel(paramString);
      localRecentLabelList.notifyChanged();
    }
  }

  public class RecentLabelList extends DataSetObservable
    implements Iterable<String>
  {
    private final LRUCache<String, Void> mRecentLabelsLRU;

    private RecentLabelList(int arg2)
    {
      int i;
      this.mRecentLabelsLRU = new LRUCache(i);
      Object localObject;
      Iterator localIterator = localObject.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        this.mRecentLabelsLRU.addElement(str, null);
      }
    }

    private void addLabel(String paramString)
    {
      this.mRecentLabelsLRU.addElement(paramString, null);
    }

    public Iterator<String> iterator()
    {
      return this.mRecentLabelsLRU.keySet().iterator();
    }

    public void registerObserver(DataSetObserver paramDataSetObserver)
    {
      super.registerObserver(paramDataSetObserver);
      RecentLabelsCache.this.mUpdateLists.add(this);
      if (RecentLabelsCache.this.mUpdateLists.size() > 3)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(RecentLabelsCache.this.mUpdateLists.size());
        LogUtils.w("Gmail", "global RLC update set size=%d", arrayOfObject);
      }
    }

    public int size()
    {
      return this.mRecentLabelsLRU.size();
    }

    public void unregisterAll()
    {
      super.unregisterAll();
      RecentLabelsCache.this.mUpdateLists.remove(this);
    }

    public void unregisterObserver(DataSetObserver paramDataSetObserver)
    {
      super.unregisterObserver(paramDataSetObserver);
      if (this.mObservers.isEmpty())
        RecentLabelsCache.this.mUpdateLists.remove(this);
    }
  }

  private static class SaveTask extends AsyncTask<Void, Void, Void>
  {
    private final String mAccount;
    private final Context mContext;
    private final Map<String, Long> mTouchMap;

    SaveTask(Context paramContext, String paramString, Map<String, Long> paramMap)
    {
      this.mContext = paramContext;
      this.mAccount = paramString;
      this.mTouchMap = paramMap;
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      LabelManager.updateRecentLabels(this.mContext, this.mAccount, this.mTouchMap);
      return null;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.RecentLabelsCache
 * JD-Core Version:    0.6.2
 */