package com.android.mail.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.mail.providers.Folder;
import com.android.mail.providers.RecentFolderObserver;
import com.android.mail.providers.UIProvider;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class FolderListFragment extends ListFragment
  implements LoaderManager.LoaderCallbacks<Cursor>
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private ControllableActivity mActivity;
  private ConversationListCallbacks mConversationListCallback;
  private Folder mCurrentFolderForUnreadCheck;
  private FolderListFragmentCursorAdapter mCursorAdapter;
  private View mEmptyView;
  private ArrayList<Integer> mExcludedFolderTypes;
  private Uri mFolderListUri;
  private FolderObserver mFolderObserver = null;
  private Cursor mFutureData;
  private boolean mIsSectioned;
  private ListView mListView;
  private FolderListSelectionListener mListener;
  private Folder mParentFolder;
  private int mSelectedFolderType = 0;
  private Uri mSelectedFolderUri = Uri.EMPTY;

  public static FolderListFragment newInstance(Folder paramFolder, Uri paramUri, boolean paramBoolean)
  {
    return newInstance(paramFolder, paramUri, paramBoolean, null);
  }

  public static FolderListFragment newInstance(Folder paramFolder, Uri paramUri, boolean paramBoolean, ArrayList<Integer> paramArrayList)
  {
    FolderListFragment localFolderListFragment = new FolderListFragment();
    Bundle localBundle = new Bundle();
    if (paramFolder != null)
      localBundle.putParcelable("arg-parent-folder", paramFolder);
    localBundle.putString("arg-folder-list-uri", paramUri.toString());
    localBundle.putBoolean("arg-is-sectioned", paramBoolean);
    if (paramArrayList != null)
      localBundle.putIntegerArrayList("arg-excluded-folder-types", paramArrayList);
    localFolderListFragment.setArguments(localBundle);
    return localFolderListFragment;
  }

  private void setSelectedFolder(Folder paramFolder)
  {
    if (paramFolder == null)
    {
      this.mSelectedFolderUri = Uri.EMPTY;
      LogUtils.e(LOG_TAG, "FolderListFragment.setSelectedFolder(null) called!", new Object[0]);
    }
    do
    {
      return;
      this.mCurrentFolderForUnreadCheck = paramFolder;
      this.mSelectedFolderUri = paramFolder.uri;
      setSelectedFolderType(paramFolder);
    }
    while (this.mCursorAdapter == null);
    this.mCursorAdapter.notifyDataSetChanged();
  }

  private void setSelectedFolderType(Folder paramFolder)
  {
    if (this.mSelectedFolderType != 0)
      return;
    if (paramFolder.isProviderFolder());
    for (int i = 1; ; i = 3)
    {
      this.mSelectedFolderType = i;
      return;
    }
  }

  private void updateCursorAdapter(Cursor paramCursor)
  {
    this.mCursorAdapter.setCursor(paramCursor);
    if ((paramCursor == null) || (paramCursor.getCount() == 0))
    {
      this.mEmptyView.setVisibility(0);
      this.mListView.setEmptyView(this.mEmptyView);
    }
  }

  private void viewFolder(int paramInt)
  {
    Object localObject = getListAdapter().getItem(paramInt);
    Folder localFolder1;
    if ((localObject instanceof FolderListFragment.FolderListAdapter.Item))
    {
      FolderListFragment.FolderListAdapter.Item localItem = (FolderListFragment.FolderListAdapter.Item)localObject;
      localFolder1 = this.mCursorAdapter.getFullFolder(localItem);
      this.mSelectedFolderType = localItem.mFolderType;
      if (localFolder1 == null)
        break label119;
      if (!localFolder1.equals(this.mParentFolder))
        break label110;
    }
    label110: for (Folder localFolder2 = null; ; localFolder2 = this.mParentFolder)
    {
      localFolder1.parent = localFolder2;
      this.mListener.onFolderSelected(localFolder1);
      return;
      if ((localObject instanceof Folder))
      {
        localFolder1 = (Folder)localObject;
        break;
      }
      localFolder1 = new Folder((Cursor)localObject);
      break;
    }
    label119: String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    LogUtils.d(str, "FolderListFragment unable to get a full fledged folder to hand to the listener for position %d", arrayOfObject);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    Activity localActivity = getActivity();
    if (!(localActivity instanceof ControllableActivity))
      LogUtils.wtf(LOG_TAG, "FolderListFragment expects only a ControllableActivity tocreate it. Cannot proceed.", new Object[0]);
    this.mActivity = ((ControllableActivity)localActivity);
    this.mConversationListCallback = this.mActivity.getListHandler();
    FolderController localFolderController = this.mActivity.getFolderController();
    this.mFolderObserver = new FolderObserver(null);
    if (localFolderController != null)
    {
      localFolderController.registerFolderObserver(this.mFolderObserver);
      this.mCurrentFolderForUnreadCheck = localFolderController.getFolder();
    }
    this.mListener = this.mActivity.getFolderListSelectionListener();
    if (this.mActivity.isFinishing())
      return;
    Folder localFolder;
    if (this.mParentFolder != null)
    {
      this.mCursorAdapter = new HierarchicalFolderListAdapter(null, this.mParentFolder);
      localFolder = this.mActivity.getHierarchyFolder();
    }
    do
    {
      if ((localFolder != null) && (!localFolder.uri.equals(this.mSelectedFolderUri)))
        setSelectedFolder(localFolder);
      setListAdapter(this.mCursorAdapter);
      getLoaderManager().initLoader(0, Bundle.EMPTY, this);
      return;
      this.mCursorAdapter = new FolderListAdapter(2130968631, this.mIsSectioned);
      localFolder = null;
    }
    while (localFolderController == null);
    while (true)
      localFolder = localFolderController.getFolder();
  }

  public void onAnimationEnd()
  {
    if (this.mFutureData != null)
    {
      updateCursorAdapter(this.mFutureData);
      this.mFutureData = null;
    }
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    this.mListView.setEmptyView(null);
    this.mEmptyView.setVisibility(8);
    return new CursorLoader(this.mActivity.getActivityContext(), this.mFolderListUri, UIProvider.FOLDERS_PROJECTION, null, null, null);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    Bundle localBundle = getArguments();
    this.mFolderListUri = Uri.parse(localBundle.getString("arg-folder-list-uri"));
    this.mParentFolder = ((Folder)localBundle.getParcelable("arg-parent-folder"));
    this.mIsSectioned = localBundle.getBoolean("arg-is-sectioned");
    this.mExcludedFolderTypes = localBundle.getIntegerArrayList("arg-excluded-folder-types");
    View localView = paramLayoutInflater.inflate(2130968632, null);
    this.mListView = ((ListView)localView.findViewById(16908298));
    this.mListView.setHeaderDividersEnabled(false);
    this.mListView.setChoiceMode(1);
    this.mListView.setEmptyView(null);
    if ((paramBundle != null) && (paramBundle.containsKey("flf-list-state")))
      this.mListView.onRestoreInstanceState(paramBundle.getParcelable("flf-list-state"));
    this.mEmptyView = localView.findViewById(2131689560);
    if ((paramBundle != null) && (paramBundle.containsKey("flf-selected-folder")))
    {
      this.mSelectedFolderUri = Uri.parse(paramBundle.getString("flf-selected-folder"));
      this.mSelectedFolderType = paramBundle.getInt("flf-selected-type");
    }
    while (this.mParentFolder == null)
      return localView;
    this.mSelectedFolderUri = this.mParentFolder.uri;
    return localView;
  }

  public void onDestroyView()
  {
    if (this.mCursorAdapter != null)
      this.mCursorAdapter.destroy();
    setListAdapter(null);
    if (this.mFolderObserver != null)
    {
      FolderController localFolderController = this.mActivity.getFolderController();
      if (localFolderController != null)
      {
        localFolderController.unregisterFolderObserver(this.mFolderObserver);
        this.mFolderObserver = null;
      }
    }
    super.onDestroyView();
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    viewFolder(paramInt);
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if ((this.mConversationListCallback == null) || (!this.mConversationListCallback.isAnimating()))
    {
      updateCursorAdapter(paramCursor);
      return;
    }
    this.mFutureData = paramCursor;
    this.mCursorAdapter.setCursor(null);
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    this.mCursorAdapter.setCursor(null);
  }

  public void onPause()
  {
    super.onPause();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mListView != null)
      paramBundle.putParcelable("flf-list-state", this.mListView.onSaveInstanceState());
    if (this.mSelectedFolderUri != null)
      paramBundle.putString("flf-selected-folder", this.mSelectedFolderUri.toString());
    paramBundle.putInt("flf-selected-type", this.mSelectedFolderType);
  }

  public void onStart()
  {
    super.onStart();
  }

  public void onStop()
  {
    super.onStop();
  }

  public boolean showingHierarchy()
  {
    return this.mParentFolder != null;
  }

  private class FolderListAdapter extends BaseAdapter
    implements FolderListFragment.FolderListFragmentCursorAdapter
  {
    private Cursor mCursor = null;
    private final LayoutInflater mInflater = LayoutInflater.from(FolderListFragment.this.mActivity.getActivityContext());
    private final boolean mIsSectioned;
    private final List<Item> mItemList = new ArrayList();
    private final RecentFolderObserver mRecentFolderObserver = new RecentFolderObserver()
    {
      public void onChanged()
      {
        FolderListFragment.FolderListAdapter.this.recalculateList();
      }
    };
    private final RecentFolderList mRecentFolders;

    public FolderListAdapter(int paramBoolean, boolean arg3)
    {
      boolean bool;
      this.mIsSectioned = bool;
      RecentFolderController localRecentFolderController = FolderListFragment.this.mActivity.getRecentFolderController();
      if ((localRecentFolderController != null) && (this.mIsSectioned))
      {
        this.mRecentFolders = this.mRecentFolderObserver.initialize(localRecentFolderController);
        return;
      }
      this.mRecentFolders = null;
    }

    private final List<Folder> getRecentFolders(RecentFolderList paramRecentFolderList)
    {
      ArrayList localArrayList = new ArrayList();
      if (paramRecentFolderList == null);
      while (true)
      {
        return localArrayList;
        Iterator localIterator = paramRecentFolderList.getRecentFolderList(null).iterator();
        while (localIterator.hasNext())
        {
          Folder localFolder = (Folder)localIterator.next();
          if (!localFolder.isProviderFolder())
            localArrayList.add(localFolder);
        }
      }
    }

    private void recalculateList()
    {
      if ((this.mCursor == null) || (this.mCursor.isClosed()) || (this.mCursor.getCount() <= 0) || (!this.mCursor.moveToFirst()))
        return;
      this.mItemList.clear();
      if (!this.mIsSectioned)
      {
        do
        {
          Folder localFolder3 = Folder.getDeficientDisplayOnlyFolder(this.mCursor);
          if ((FolderListFragment.this.mExcludedFolderTypes == null) || (!FolderListFragment.this.mExcludedFolderTypes.contains(Integer.valueOf(localFolder3.type))))
            this.mItemList.add(new Item(localFolder3, 3, this.mCursor.getPosition(), null));
        }
        while (this.mCursor.moveToNext());
        notifyDataSetChanged();
        return;
      }
      ArrayList localArrayList = new ArrayList();
      List localList;
      while (true)
      {
        Folder localFolder1 = Folder.getDeficientDisplayOnlyFolder(this.mCursor);
        if ((FolderListFragment.this.mExcludedFolderTypes == null) || (!FolderListFragment.this.mExcludedFolderTypes.contains(Integer.valueOf(localFolder1.type))))
        {
          if (!localFolder1.isProviderFolder())
            break label321;
          this.mItemList.add(new Item(localFolder1, 1, this.mCursor.getPosition(), null));
        }
        while (!this.mCursor.moveToNext())
        {
          localList = getRecentFolders(this.mRecentFolders);
          if (FolderListFragment.this.mExcludedFolderTypes == null)
            break label351;
          Iterator localIterator3 = localList.iterator();
          while (localIterator3.hasNext())
            if (FolderListFragment.this.mExcludedFolderTypes.contains(Integer.valueOf(((Folder)localIterator3.next()).type)))
              localIterator3.remove();
          label321: localArrayList.add(new Item(localFolder1, 3, this.mCursor.getPosition(), null));
        }
      }
      label351: if (localList.size() > 0)
      {
        this.mItemList.add(new Item(2131427595, null));
        Iterator localIterator2 = localList.iterator();
        while (localIterator2.hasNext())
        {
          Folder localFolder2 = (Folder)localIterator2.next();
          this.mItemList.add(new Item(localFolder2, 2, -1, null));
        }
      }
      if (localArrayList.size() > 0)
      {
        this.mItemList.add(new Item(2131427594, null));
        Iterator localIterator1 = localArrayList.iterator();
        while (localIterator1.hasNext())
        {
          Item localItem = (Item)localIterator1.next();
          this.mItemList.add(localItem);
        }
      }
      notifyDataSetChanged();
    }

    public boolean areAllItemsEnabled()
    {
      return false;
    }

    public final void destroy()
    {
      this.mRecentFolderObserver.unregisterAndDestroy();
    }

    public int getCount()
    {
      return this.mItemList.size();
    }

    public Folder getFullFolder(Item paramItem)
    {
      Folder localFolder;
      if (paramItem.mFolderType == 2)
        localFolder = paramItem.mFolder;
      boolean bool2;
      do
      {
        boolean bool1;
        do
        {
          Cursor localCursor;
          do
          {
            int i;
            do
            {
              return localFolder;
              i = paramItem.mPosition;
              if (FolderListFragment.this.mFutureData != null)
              {
                this.mCursor = FolderListFragment.this.mFutureData;
                FolderListFragment.access$1302(FolderListFragment.this, null);
              }
              localFolder = null;
            }
            while (i <= -1);
            localCursor = this.mCursor;
            localFolder = null;
          }
          while (localCursor == null);
          bool1 = this.mCursor.isClosed();
          localFolder = null;
        }
        while (bool1);
        bool2 = this.mCursor.moveToPosition(paramItem.mPosition);
        localFolder = null;
      }
      while (!bool2);
      return new Folder(this.mCursor);
    }

    public Object getItem(int paramInt)
    {
      return this.mItemList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return getItem(paramInt).hashCode();
    }

    public int getItemViewType(int paramInt)
    {
      return ((Item)getItem(paramInt)).mType;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      return ((Item)getItem(paramInt)).getView(paramInt, paramView, paramViewGroup);
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public boolean isEnabled(int paramInt)
    {
      return ((Item)getItem(paramInt)).mType != 1;
    }

    public void setCursor(Cursor paramCursor)
    {
      this.mCursor = paramCursor;
      recalculateList();
    }

    private class Item
    {
      public final Folder mFolder;
      public final int mFolderType;
      public int mPosition;
      public final int mResource;
      public final int mType;

      private Item(int arg2)
      {
        this.mFolder = null;
        int i;
        this.mResource = i;
        this.mType = 1;
        this.mFolderType = 0;
      }

      private Item(Folder paramInt1, int paramInt2, int arg4)
      {
        this.mFolder = paramInt1;
        this.mResource = -1;
        this.mType = 0;
        this.mFolderType = paramInt2;
        int i;
        this.mPosition = i;
      }

      private final View getFolderView(int paramInt, View paramView, ViewGroup paramViewGroup)
      {
        if (paramView != null);
        for (FolderItemView localFolderItemView = (FolderItemView)paramView; ; localFolderItemView = (FolderItemView)FolderListFragment.FolderListAdapter.this.mInflater.inflate(2130968631, null, false))
        {
          localFolderItemView.bind(this.mFolder, FolderListFragment.this.mActivity);
          if (FolderListFragment.this.mListView != null)
          {
            int i = this.mFolderType;
            int j = FolderListFragment.this.mSelectedFolderType;
            boolean bool1 = false;
            if (i == j)
            {
              boolean bool2 = this.mFolder.uri.equals(FolderListFragment.this.mSelectedFolderUri);
              bool1 = false;
              if (bool2)
                bool1 = true;
            }
            FolderListFragment.this.mListView.setItemChecked(paramInt, bool1);
            if ((bool1) && (FolderListFragment.this.mCurrentFolderForUnreadCheck != null) && (this.mFolder.unreadCount != FolderListFragment.this.mCurrentFolderForUnreadCheck.unreadCount))
              localFolderItemView.overrideUnreadCount(FolderListFragment.this.mCurrentFolderForUnreadCheck.unreadCount);
          }
          Folder.setFolderBlockColor(this.mFolder, localFolderItemView.findViewById(2131689646));
          Folder.setIcon(this.mFolder, (ImageView)localFolderItemView.findViewById(2131689521));
          return localFolderItemView;
        }
      }

      private final View getHeaderView(int paramInt, View paramView, ViewGroup paramViewGroup)
      {
        if (paramView != null);
        for (TextView localTextView = (TextView)paramView; ; localTextView = (TextView)FolderListFragment.FolderListAdapter.this.mInflater.inflate(2130968633, paramViewGroup, false))
        {
          localTextView.setText(this.mResource);
          return localTextView;
        }
      }

      private final View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
      {
        if (this.mType == 0)
          return getFolderView(paramInt, paramView, paramViewGroup);
        return getHeaderView(paramInt, paramView, paramViewGroup);
      }
    }
  }

  private static abstract interface FolderListFragmentCursorAdapter extends ListAdapter
  {
    public abstract void destroy();

    public abstract Folder getFullFolder(FolderListFragment.FolderListAdapter.Item paramItem);

    public abstract void notifyDataSetChanged();

    public abstract void setCursor(Cursor paramCursor);
  }

  public static abstract interface FolderListSelectionListener
  {
    public abstract void onFolderSelected(Folder paramFolder);
  }

  private final class FolderObserver extends DataSetObserver
  {
    private FolderObserver()
    {
    }

    public void onChanged()
    {
      if (FolderListFragment.this.mActivity == null);
      FolderController localFolderController;
      do
      {
        return;
        localFolderController = FolderListFragment.this.mActivity.getFolderController();
      }
      while (localFolderController == null);
      FolderListFragment.this.setSelectedFolder(localFolderController.getFolder());
    }
  }

  private class HierarchicalFolderListAdapter extends ArrayAdapter<Folder>
    implements FolderListFragment.FolderListFragmentCursorAdapter
  {
    private Cursor mCursor;
    private final FolderItemView.DropHandler mDropHandler = FolderListFragment.this.mActivity;
    private final Folder mParent;
    private final Uri mParentUri;

    public HierarchicalFolderListAdapter(Cursor paramFolder, Folder arg3)
    {
      super(2130968631);
      Object localObject;
      this.mParent = localObject;
      this.mParentUri = localObject.uri;
      setCursor(paramFolder);
    }

    public void destroy()
    {
    }

    public Folder getFullFolder(FolderListFragment.FolderListAdapter.Item paramItem)
    {
      int i = paramItem.mPosition;
      if ((this.mCursor == null) || (this.mCursor.isClosed()))
      {
        this.mCursor = FolderListFragment.this.mFutureData;
        FolderListFragment.access$1302(FolderListFragment.this, null);
      }
      Folder localFolder = null;
      if (i > -1)
      {
        Cursor localCursor = this.mCursor;
        localFolder = null;
        if (localCursor != null)
        {
          boolean bool1 = this.mCursor.isClosed();
          localFolder = null;
          if (!bool1)
          {
            boolean bool2 = this.mCursor.moveToPosition(paramItem.mPosition);
            localFolder = null;
            if (bool2)
              localFolder = new Folder(this.mCursor);
          }
        }
      }
      return localFolder;
    }

    public int getItemViewType(int paramInt)
    {
      if (((Folder)getItem(paramInt)).uri.equals(this.mParentUri))
        return 0;
      return 1;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      boolean bool1 = true;
      Folder localFolder = (Folder)getItem(paramInt);
      boolean bool2 = localFolder.uri.equals(this.mParentUri);
      FolderItemView localFolderItemView;
      if (paramView != null)
      {
        localFolderItemView = (FolderItemView)paramView;
        localFolderItemView.bind(localFolder, this.mDropHandler);
        if (localFolder.uri.equals(FolderListFragment.this.mSelectedFolderUri))
        {
          FolderListFragment.this.getListView().setItemChecked(paramInt, bool1);
          if ((FolderListFragment.this.mCurrentFolderForUnreadCheck == null) || (localFolder.unreadCount == FolderListFragment.this.mCurrentFolderForUnreadCheck.unreadCount))
            break label187;
        }
      }
      while (true)
      {
        if (bool1)
          localFolderItemView.overrideUnreadCount(FolderListFragment.this.mCurrentFolderForUnreadCheck.unreadCount);
        Folder.setFolderBlockColor(localFolder, localFolderItemView.findViewById(2131689521));
        return localFolderItemView;
        if (bool2);
        for (int i = 2130968631; ; i = 2130968592)
        {
          localFolderItemView = (FolderItemView)LayoutInflater.from(FolderListFragment.this.mActivity.getActivityContext()).inflate(i, null);
          break;
        }
        label187: bool1 = false;
      }
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public void setCursor(Cursor paramCursor)
    {
      this.mCursor = paramCursor;
      clear();
      if (this.mParent != null)
        add(this.mParent);
      if ((paramCursor != null) && (paramCursor.getCount() > 0))
      {
        paramCursor.moveToFirst();
        do
        {
          Folder localFolder = new Folder(paramCursor);
          localFolder.parent = this.mParent;
          add(localFolder);
        }
        while (paramCursor.moveToNext());
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.FolderListFragment
 * JD-Core Version:    0.6.2
 */