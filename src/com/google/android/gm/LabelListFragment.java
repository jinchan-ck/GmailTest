package com.google.android.gm;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Paint;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.provider.Label;
import com.google.android.gm.provider.LabelList;
import com.google.android.gm.provider.LabelLoader;
import com.google.android.gm.utils.LabelColorUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LabelListFragment extends ListFragment
  implements LoaderManager.LoaderCallbacks<LabelList>, LabelSettingsObserver
{
  private String mAccount;
  private LabelsAdapter mAdapter;
  private LabelListCallbacks mCallbacks;
  private Context mContext;
  private LabelItemView.DropHandler mDropHandler;
  private LayoutInflater mInflater;
  private String mLabel;
  private ListView mListView;
  private int mMode;
  private int mOptions;
  private Persistence mPersistence;
  private final RecentLabelsLoaderCallbacks mRecentsCallbacks = new RecentLabelsLoaderCallbacks(null);
  private Resources mResources;
  private Parcelable mSavedListState;
  private LabelSettingsObservable mSettingsObservable;
  private UiHandler mUiHandler;

  private String getLabelDescription(String paramString)
  {
    boolean bool = this.mPersistence.shouldNotifyForLabel(this.mContext, this.mAccount, paramString);
    String str;
    if (this.mSettingsObservable.getIncludedLabels().contains(paramString))
      str = this.mResources.getString(2131427553);
    while (bool)
    {
      Resources localResources = this.mResources;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = str;
      arrayOfObject[1] = Utils.getLabelNotificationSummary(this.mContext, this.mAccount, paramString);
      return localResources.getString(2131427815, arrayOfObject);
      if (this.mSettingsObservable.getPartialLabels().contains(paramString))
      {
        str = Utils.formatPlural(this.mContext, 2131755032, this.mSettingsObservable.getNumberOfSyncDays());
      }
      else
      {
        str = this.mResources.getString(2131427814);
        bool = false;
      }
    }
    return str;
  }

  public static LabelListFragment newInstance(String paramString1, String paramString2, int paramInt)
  {
    return newInstance(paramString1, paramString2, paramInt, 0);
  }

  public static LabelListFragment newInstance(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    LabelListFragment localLabelListFragment = new LabelListFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("account", paramString1);
    localBundle.putString("label", paramString2);
    localBundle.putInt("mode", paramInt1);
    localBundle.putInt("options", paramInt2);
    localLabelListFragment.setArguments(localBundle);
    return localLabelListFragment;
  }

  boolean hideRecentLabels()
  {
    return (0x1 & this.mOptions) != 0;
  }

  boolean isManageLabelMode()
  {
    return this.mMode == 1;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    Activity localActivity = getActivity();
    this.mContext = localActivity;
    this.mInflater = LayoutInflater.from(this.mContext);
    this.mResources = this.mContext.getResources();
    this.mPersistence = Persistence.getInstance();
    this.mCallbacks = ((LabelListCallbacks)localActivity);
    if ((localActivity instanceof LabelItemView.DropHandler))
      this.mDropHandler = ((LabelItemView.DropHandler)localActivity);
    if ((localActivity instanceof LabelSettingsObservable))
    {
      this.mSettingsObservable = ((LabelSettingsObservable)localActivity);
      this.mSettingsObservable.registerObserver(this);
    }
    this.mUiHandler = ((RestrictedActivity)localActivity).getUiHandler();
    this.mAdapter = new LabelsAdapter(null);
    setListAdapter(this.mAdapter);
    getLoaderManager().initLoader(0, Bundle.EMPTY, this);
    if (!hideRecentLabels())
      getLoaderManager().initLoader(1, Bundle.EMPTY, this.mRecentsCallbacks);
  }

  public void onChanged()
  {
    this.mAdapter.notifyDataSetChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Bundle localBundle = getArguments();
    this.mAccount = localBundle.getString("account");
    this.mLabel = localBundle.getString("label");
    if ((paramBundle != null) && (paramBundle.containsKey("label")))
      this.mLabel = paramBundle.getString("label");
    this.mMode = localBundle.getInt("mode");
    this.mOptions = localBundle.getInt("options");
    if (paramBundle != null)
      this.mSavedListState = paramBundle.getParcelable("list-state");
  }

  public Loader<LabelList> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    return new LabelLoader(getActivity(), this.mAccount, true);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130968643, null);
    this.mListView = ((ListView)localView.findViewById(16908298));
    this.mListView.setEmptyView(null);
    this.mListView.setChoiceMode(1);
    return localView;
  }

  public void onDestroyView()
  {
    if ((getActivity() instanceof LabelSettingsObservable))
      this.mSettingsObservable.unregisterObserver(this);
    this.mAdapter.stopListening();
    this.mAdapter = null;
    super.onDestroyView();
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    LabelListItem localLabelListItem = (LabelListItem)paramListView.getItemAtPosition(paramInt);
    if (localLabelListItem.isHeader());
    String str;
    do
    {
      return;
      str = localLabelListItem.mLabel.getCanonicalName();
      this.mCallbacks.onLabelSelected(str);
    }
    while (this.mLabel == null);
    this.mLabel = str;
  }

  public void onLoadFinished(Loader<LabelList> paramLoader, LabelList paramLabelList)
  {
    this.mAdapter.setData(paramLabelList);
    this.mListView.setEmptyView(getView().findViewById(2131689560));
    if (this.mSavedListState != null)
    {
      this.mListView.onRestoreInstanceState(this.mSavedListState);
      this.mSavedListState = null;
    }
    if (this.mLabel != null)
      setSelectedLabel(this.mLabel);
  }

  public void onLoaderReset(Loader<LabelList> paramLoader)
  {
  }

  public void onPause()
  {
    super.onPause();
    if (this.mLabel == null)
      this.mListView.clearChoices();
  }

  public void onResume()
  {
    super.onResume();
    this.mCallbacks.onLabelListResumed(this);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mLabel != null)
      paramBundle.putString("label", this.mLabel);
    if (this.mListView != null)
      paramBundle.putParcelable("list-state", this.mListView.onSaveInstanceState());
  }

  public void setSelectedLabel(String paramString)
  {
    int i = -1;
    ListAdapter localListAdapter = getListAdapter();
    int j = 0;
    if (j < localListAdapter.getCount())
    {
      LabelListItem localLabelListItem = (LabelListItem)localListAdapter.getItem(j);
      if (localLabelListItem.isHeader());
      Label localLabel;
      do
      {
        j++;
        break;
        localLabel = localLabelListItem.mLabel;
      }
      while ((localLabel == null) || (!localLabel.getCanonicalName().equals(paramString)));
      i = j;
    }
    if (i != -1)
    {
      getListView().setItemChecked(i, true);
      getListView().smoothScrollToPosition(i);
    }
  }

  public static abstract interface LabelListCallbacks
  {
    public abstract void onLabelListResumed(LabelListFragment paramLabelListFragment);

    public abstract void onLabelSelected(String paramString);
  }

  private static class LabelListItem
  {
    final String mHeaderText;
    final Label mLabel;

    public LabelListItem(Label paramLabel)
    {
      this.mLabel = paramLabel;
      this.mHeaderText = null;
    }

    public LabelListItem(String paramString)
    {
      this.mHeaderText = paramString;
      this.mLabel = null;
    }

    public int getItemType()
    {
      if (this.mHeaderText != null)
        return 0;
      return 1;
    }

    public boolean isHeader()
    {
      return getItemType() == 0;
    }
  }

  private class LabelsAdapter extends BaseAdapter
  {
    private LabelList mData;
    private final DataSetObserver mDataSetObserver = new DataSetObserver()
    {
      public void onChanged()
      {
        if (LabelListFragment.LabelsAdapter.this.mData != null)
        {
          LabelListFragment.LabelsAdapter.this.extractLabelList();
          LabelListFragment.LabelsAdapter.this.notifyDataSetChanged();
        }
      }
    };
    private final List<LabelListFragment.LabelListItem> mItems = Lists.newArrayList();
    private RecentLabelsCache.RecentLabelList mRecentLabels;

    private LabelsAdapter()
    {
    }

    private void addSection(int paramInt, List<Label> paramList)
    {
      if (!paramList.isEmpty())
        this.mItems.add(new LabelListFragment.LabelListItem(LabelListFragment.this.mContext.getString(paramInt)));
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        Label localLabel = (Label)localIterator.next();
        this.mItems.add(new LabelListFragment.LabelListItem(localLabel));
      }
    }

    private void addSplitLabelLists(List<Label> paramList)
    {
      ArrayList localArrayList1 = Lists.newArrayList();
      Iterator localIterator1 = paramList.iterator();
      while (localIterator1.hasNext())
      {
        Label localLabel2 = (Label)localIterator1.next();
        if (localLabel2.isSystemLabel())
          this.mItems.add(new LabelListFragment.LabelListItem(localLabel2));
        else
          localArrayList1.add(localLabel2);
      }
      ArrayList localArrayList2 = Lists.newArrayList();
      Iterator localIterator2 = this.mRecentLabels.iterator();
      while (localIterator2.hasNext())
      {
        String str = (String)localIterator2.next();
        Label localLabel1 = this.mData.get(str);
        if ((localLabel1 != null) && (!shouldHideLabel(localLabel1)))
          localArrayList2.add(localLabel1);
      }
      Collections.sort(localArrayList2, RecentLabelsCache.getInstance(LabelListFragment.this.mContext).getRecentLabelDisplayComparator());
      addSection(2131427809, localArrayList2);
      addSection(2131427810, localArrayList1);
    }

    private void extractLabelList()
    {
      this.mItems.clear();
      ArrayList localArrayList = Lists.newArrayList();
      for (int i = 0; i < this.mData.size(); i++)
      {
        Label localLabel2 = this.mData.get(i);
        if (!shouldHideLabel(localLabel2))
          localArrayList.add(localLabel2);
      }
      if (hasRecentLabels())
        addSplitLabelLists(localArrayList);
      while (true)
      {
        return;
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          Label localLabel1 = (Label)localIterator.next();
          this.mItems.add(new LabelListFragment.LabelListItem(localLabel1));
        }
      }
    }

    private View getLabelItemView(int paramInt, View paramView, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater, Label paramLabel)
    {
      LabelListFragment.ViewHolder localViewHolder;
      if (paramView == null)
      {
        paramView = paramLayoutInflater.inflate(2130968642, null);
        localViewHolder = new LabelListFragment.ViewHolder(null);
        localViewHolder.labelCountView = ((TextView)paramView.findViewById(2131689522));
        localViewHolder.name = ((TextView)paramView.findViewById(2131689523));
        localViewHolder.description = ((TextView)paramView.findViewById(2131689524));
        localViewHolder.labelBox = ((ImageView)paramView.findViewById(2131689653));
        paramView.setTag(localViewHolder);
      }
      int i;
      while (true)
      {
        ((LabelItemView)paramView).bind(LabelListFragment.this.mAccount, paramLabel, LabelListFragment.this.mDropHandler);
        localViewHolder.name.setText(paramLabel.getName());
        if (LabelListFragment.this.isManageLabelMode())
        {
          localViewHolder.description.setVisibility(0);
          localViewHolder.description.setText(LabelListFragment.this.getLabelDescription(paramLabel.getCanonicalName()));
          localViewHolder.labelCountView.setVisibility(8);
        }
        String str = Utils.getUnreadCountString(LabelListFragment.this.mContext, Utils.getLabelDisplayCount(paramLabel));
        localViewHolder.labelCountView.setText(str);
        i = paramLabel.getBackgroundColor();
        if (i != LabelColorUtils.getDefaultLabelBackgroundColor())
          break;
        localViewHolder.labelBox.setBackgroundDrawable(null);
        return paramView;
        localViewHolder = (LabelListFragment.ViewHolder)paramView.getTag();
      }
      PaintDrawable localPaintDrawable = new PaintDrawable();
      localPaintDrawable.getPaint().setColor(i);
      localViewHolder.labelBox.setBackgroundDrawable(localPaintDrawable);
      return paramView;
    }

    private boolean hasRecentLabels()
    {
      return (this.mRecentLabels != null) && (this.mRecentLabels.size() > 0);
    }

    private boolean shouldHideLabel(Label paramLabel)
    {
      return (LabelListFragment.this.isManageLabelMode()) && ((paramLabel.getForceSyncNone()) || (paramLabel.getForceSyncAll()));
    }

    public boolean areAllItemsEnabled()
    {
      return false;
    }

    public int getCount()
    {
      return this.mItems.size();
    }

    public Object getItem(int paramInt)
    {
      return this.mItems.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      return ((LabelListFragment.LabelListItem)this.mItems.get(paramInt)).getItemType();
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      LabelListFragment.LabelListItem localLabelListItem = (LabelListFragment.LabelListItem)getItem(paramInt);
      if (!localLabelListItem.isHeader())
      {
        LayoutInflater localLayoutInflater = LabelListFragment.this.mInflater;
        Label localLabel = localLabelListItem.mLabel;
        return getLabelItemView(paramInt, paramView, paramViewGroup, localLayoutInflater, localLabel);
      }
      LabelListFragment.ViewHolder localViewHolder;
      if (paramView == null)
      {
        paramView = LabelListFragment.this.mInflater.inflate(2130968644, paramViewGroup, false);
        localViewHolder = new LabelListFragment.ViewHolder(null);
        localViewHolder.name = ((TextView)paramView.findViewById(2131689523));
        paramView.setTag(localViewHolder);
      }
      while (true)
      {
        localViewHolder.name.setText(localLabelListItem.mHeaderText);
        return paramView;
        localViewHolder = (LabelListFragment.ViewHolder)paramView.getTag();
      }
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public boolean isEnabled(int paramInt)
    {
      if (!hasRecentLabels())
        return true;
      if (!((LabelListFragment.LabelListItem)this.mItems.get(paramInt)).isHeader());
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public void setData(LabelList paramLabelList)
    {
      if (this.mData != null)
        this.mData.unregisterDataSetObserver(this.mDataSetObserver);
      this.mData = paramLabelList;
      this.mData.registerDataSetObserver(this.mDataSetObserver);
      extractLabelList();
      notifyDataSetChanged();
    }

    public void setRecentLabels(RecentLabelsCache.RecentLabelList paramRecentLabelList)
    {
      if (this.mRecentLabels != null)
        this.mRecentLabels.unregisterObserver(this.mDataSetObserver);
      this.mRecentLabels = paramRecentLabelList;
      this.mRecentLabels.registerObserver(this.mDataSetObserver);
      if (this.mData != null)
      {
        extractLabelList();
        notifyDataSetChanged();
      }
    }

    public void stopListening()
    {
      if (this.mData != null)
        this.mData.unregisterDataSetObserver(this.mDataSetObserver);
      if (this.mRecentLabels != null)
        this.mRecentLabels.unregisterObserver(this.mDataSetObserver);
    }
  }

  private class RecentLabelsLoaderCallbacks
    implements LoaderManager.LoaderCallbacks<RecentLabelsCache.RecentLabelList>
  {
    private RecentLabelsLoaderCallbacks()
    {
    }

    public Loader<RecentLabelsCache.RecentLabelList> onCreateLoader(int paramInt, Bundle paramBundle)
    {
      return new RecentLabelLoader(LabelListFragment.this.mContext, LabelListFragment.this.mAccount, LabelListFragment.this.mUiHandler);
    }

    public void onLoadFinished(Loader<RecentLabelsCache.RecentLabelList> paramLoader, RecentLabelsCache.RecentLabelList paramRecentLabelList)
    {
      LabelListFragment.this.mAdapter.setRecentLabels(paramRecentLabelList);
    }

    public void onLoaderReset(Loader<RecentLabelsCache.RecentLabelList> paramLoader)
    {
    }
  }

  private static class ViewHolder
  {
    TextView description;
    ImageView labelBox;
    TextView labelCountView;
    TextView name;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelListFragment
 * JD-Core Version:    0.6.2
 */