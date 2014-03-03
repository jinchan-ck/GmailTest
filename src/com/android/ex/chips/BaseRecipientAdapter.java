package com.android.ex.chips;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.util.Rfc822Token;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BaseRecipientAdapter extends BaseAdapter
  implements Filterable
{
  private Account mAccount;
  private final ContentResolver mContentResolver;
  private final Context mContext;
  private CharSequence mCurrentConstraint;
  private final DelayedMessageHandler mDelayedMessageHandler = new DelayedMessageHandler(null);
  private List<RecipientEntry> mEntries;
  private LinkedHashMap<Long, List<RecipientEntry>> mEntryMap;
  private Set<String> mExistingDestinations;
  private final Handler mHandler = new Handler();
  private final LayoutInflater mInflater;
  private List<RecipientEntry> mNonAggregatedEntries;
  private final LruCache<Uri, byte[]> mPhotoCacheMap;
  private final int mPreferredMaxResultCount;
  private final Queries.Query mQuery;
  private final int mQueryType;
  private int mRemainingDirectoryCount;
  private List<RecipientEntry> mTempEntries;

  public BaseRecipientAdapter(Context paramContext)
  {
    this(paramContext, 10, 0);
  }

  public BaseRecipientAdapter(Context paramContext, int paramInt1, int paramInt2)
  {
    this.mContext = paramContext;
    this.mContentResolver = paramContext.getContentResolver();
    this.mInflater = LayoutInflater.from(paramContext);
    this.mPreferredMaxResultCount = paramInt1;
    this.mPhotoCacheMap = new LruCache(20);
    this.mQueryType = paramInt2;
    if (paramInt2 == 0)
    {
      this.mQuery = Queries.EMAIL;
      return;
    }
    if (paramInt2 == 1)
    {
      this.mQuery = Queries.PHONE;
      return;
    }
    this.mQuery = Queries.EMAIL;
    Log.e("BaseRecipientAdapter", "Unsupported query type: " + paramInt2);
  }

  private void cacheCurrentEntries()
  {
    this.mTempEntries = this.mEntries;
  }

  private void clearTempEntries()
  {
    this.mTempEntries = null;
  }

  private List<RecipientEntry> constructEntryList(boolean paramBoolean, LinkedHashMap<Long, List<RecipientEntry>> paramLinkedHashMap, List<RecipientEntry> paramList, Set<String> paramSet)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    Iterator localIterator1 = paramLinkedHashMap.entrySet().iterator();
    do
    {
      if (!localIterator1.hasNext())
        break;
      List localList = (List)((Map.Entry)localIterator1.next()).getValue();
      int j = localList.size();
      for (int k = 0; k < j; k++)
      {
        RecipientEntry localRecipientEntry2 = (RecipientEntry)localList.get(k);
        localArrayList.add(localRecipientEntry2);
        tryFetchPhoto(localRecipientEntry2);
        i++;
      }
    }
    while (i <= this.mPreferredMaxResultCount);
    Iterator localIterator2;
    if (i <= this.mPreferredMaxResultCount)
      localIterator2 = paramList.iterator();
    while (true)
    {
      RecipientEntry localRecipientEntry1;
      if (localIterator2.hasNext())
      {
        localRecipientEntry1 = (RecipientEntry)localIterator2.next();
        if (i <= this.mPreferredMaxResultCount);
      }
      else
      {
        return localArrayList;
      }
      localArrayList.add(localRecipientEntry1);
      tryFetchPhoto(localRecipientEntry1);
      i++;
    }
  }

  private Cursor doQuery(CharSequence paramCharSequence, int paramInt, Long paramLong)
  {
    Uri.Builder localBuilder = this.mQuery.getContentFilterUri().buildUpon().appendPath(paramCharSequence.toString()).appendQueryParameter("limit", String.valueOf(paramInt + 5));
    if (paramLong != null)
      localBuilder.appendQueryParameter("directory", String.valueOf(paramLong));
    if (this.mAccount != null)
    {
      localBuilder.appendQueryParameter("name_for_primary_account", this.mAccount.name);
      localBuilder.appendQueryParameter("type_for_primary_account", this.mAccount.type);
    }
    System.currentTimeMillis();
    Cursor localCursor = this.mContentResolver.query(localBuilder.build(), this.mQuery.getProjection(), null, null, null);
    System.currentTimeMillis();
    return localCursor;
  }

  private void fetchPhotoAsync(final RecipientEntry paramRecipientEntry, final Uri paramUri)
  {
    new AsyncTask()
    {
      protected Void doInBackground(Void[] paramAnonymousArrayOfVoid)
      {
        Cursor localCursor = BaseRecipientAdapter.this.mContentResolver.query(paramUri, BaseRecipientAdapter.PhotoQuery.PROJECTION, null, null, null);
        if (localCursor != null);
        try
        {
          if (localCursor.moveToFirst())
          {
            final byte[] arrayOfByte = localCursor.getBlob(0);
            paramRecipientEntry.setPhotoBytes(arrayOfByte);
            BaseRecipientAdapter.this.mHandler.post(new Runnable()
            {
              public void run()
              {
                BaseRecipientAdapter.this.mPhotoCacheMap.put(BaseRecipientAdapter.1.this.val$photoThumbnailUri, arrayOfByte);
                BaseRecipientAdapter.this.notifyDataSetChanged();
              }
            });
          }
          return null;
        }
        finally
        {
          localCursor.close();
        }
      }
    }
    .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
  }

  private List<RecipientEntry> getEntries()
  {
    if (this.mTempEntries != null)
      return this.mTempEntries;
    return this.mEntries;
  }

  private void putOneEntry(TemporaryEntry paramTemporaryEntry, boolean paramBoolean, LinkedHashMap<Long, List<RecipientEntry>> paramLinkedHashMap, List<RecipientEntry> paramList, Set<String> paramSet)
  {
    if (paramSet.contains(paramTemporaryEntry.destination))
      return;
    paramSet.add(paramTemporaryEntry.destination);
    if (!paramBoolean)
    {
      paramList.add(RecipientEntry.constructTopLevelEntry(paramTemporaryEntry.displayName, paramTemporaryEntry.displayNameSource, paramTemporaryEntry.destination, paramTemporaryEntry.destinationType, paramTemporaryEntry.destinationLabel, paramTemporaryEntry.contactId, paramTemporaryEntry.dataId, paramTemporaryEntry.thumbnailUriString));
      return;
    }
    if (paramLinkedHashMap.containsKey(Long.valueOf(paramTemporaryEntry.contactId)))
    {
      ((List)paramLinkedHashMap.get(Long.valueOf(paramTemporaryEntry.contactId))).add(RecipientEntry.constructSecondLevelEntry(paramTemporaryEntry.displayName, paramTemporaryEntry.displayNameSource, paramTemporaryEntry.destination, paramTemporaryEntry.destinationType, paramTemporaryEntry.destinationLabel, paramTemporaryEntry.contactId, paramTemporaryEntry.dataId, paramTemporaryEntry.thumbnailUriString));
      return;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(RecipientEntry.constructTopLevelEntry(paramTemporaryEntry.displayName, paramTemporaryEntry.displayNameSource, paramTemporaryEntry.destination, paramTemporaryEntry.destinationType, paramTemporaryEntry.destinationLabel, paramTemporaryEntry.contactId, paramTemporaryEntry.dataId, paramTemporaryEntry.thumbnailUriString));
    paramLinkedHashMap.put(Long.valueOf(paramTemporaryEntry.contactId), localArrayList);
  }

  private List<DirectorySearchParams> setupOtherDirectories(Cursor paramCursor)
  {
    PackageManager localPackageManager = this.mContext.getPackageManager();
    ArrayList localArrayList = new ArrayList();
    Object localObject = null;
    while (paramCursor.moveToNext())
    {
      long l = paramCursor.getLong(0);
      if (l != 1L)
      {
        DirectorySearchParams localDirectorySearchParams = new DirectorySearchParams();
        String str = paramCursor.getString(4);
        int i = paramCursor.getInt(5);
        localDirectorySearchParams.directoryId = l;
        localDirectorySearchParams.displayName = paramCursor.getString(3);
        localDirectorySearchParams.accountName = paramCursor.getString(1);
        localDirectorySearchParams.accountType = paramCursor.getString(2);
        if ((str != null) && (i != 0));
        try
        {
          localDirectorySearchParams.directoryType = localPackageManager.getResourcesForApplication(str).getString(i);
          if (localDirectorySearchParams.directoryType == null)
            Log.e("BaseRecipientAdapter", "Cannot resolve directory name: " + i + "@" + str);
          if ((this.mAccount != null) && (this.mAccount.name.equals(localDirectorySearchParams.accountName)) && (this.mAccount.type.equals(localDirectorySearchParams.accountType)))
            localObject = localDirectorySearchParams;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          while (true)
            Log.e("BaseRecipientAdapter", "Cannot resolve directory name: " + i + "@" + str, localNameNotFoundException);
          localArrayList.add(localDirectorySearchParams);
        }
      }
    }
    if (localObject != null)
      localArrayList.add(1, localObject);
    return localArrayList;
  }

  private void startSearchOtherDirectories(CharSequence paramCharSequence, List<DirectorySearchParams> paramList, int paramInt)
  {
    int i = paramList.size();
    for (int j = 1; j < i; j++)
    {
      DirectorySearchParams localDirectorySearchParams = (DirectorySearchParams)paramList.get(j);
      localDirectorySearchParams.constraint = paramCharSequence;
      if (localDirectorySearchParams.filter == null)
        localDirectorySearchParams.filter = new DirectoryFilter(localDirectorySearchParams);
      localDirectorySearchParams.filter.setLimit(paramInt);
      localDirectorySearchParams.filter.filter(paramCharSequence);
    }
    this.mRemainingDirectoryCount = (i - 1);
    this.mDelayedMessageHandler.sendDelayedLoadMessage();
  }

  private void tryFetchPhoto(RecipientEntry paramRecipientEntry)
  {
    Uri localUri = paramRecipientEntry.getPhotoThumbnailUri();
    if (localUri != null)
    {
      byte[] arrayOfByte = (byte[])this.mPhotoCacheMap.get(localUri);
      if (arrayOfByte != null)
        paramRecipientEntry.setPhotoBytes(arrayOfByte);
    }
    else
    {
      return;
    }
    fetchPhotoAsync(paramRecipientEntry, localUri);
  }

  private void updateEntries(List<RecipientEntry> paramList)
  {
    this.mEntries = paramList;
    notifyDataSetChanged();
  }

  protected void fetchPhoto(RecipientEntry paramRecipientEntry, Uri paramUri)
  {
    byte[] arrayOfByte1 = (byte[])this.mPhotoCacheMap.get(paramUri);
    if (arrayOfByte1 != null)
      paramRecipientEntry.setPhotoBytes(arrayOfByte1);
    Cursor localCursor;
    do
    {
      return;
      localCursor = this.mContentResolver.query(paramUri, PhotoQuery.PROJECTION, null, null, null);
    }
    while (localCursor == null);
    try
    {
      if (localCursor.moveToFirst())
      {
        byte[] arrayOfByte2 = localCursor.getBlob(0);
        paramRecipientEntry.setPhotoBytes(arrayOfByte2);
        this.mPhotoCacheMap.put(paramUri, arrayOfByte2);
      }
      return;
    }
    finally
    {
      localCursor.close();
    }
  }

  public int getCount()
  {
    List localList = getEntries();
    if (localList != null)
      return localList.size();
    return 0;
  }

  protected int getDefaultPhotoResource()
  {
    return R.drawable.ic_contact_picture;
  }

  protected int getDestinationId()
  {
    return 16908308;
  }

  protected int getDestinationTypeId()
  {
    return 16908309;
  }

  protected int getDisplayNameId()
  {
    return 16908310;
  }

  public Filter getFilter()
  {
    return new DefaultFilter(null);
  }

  public Object getItem(int paramInt)
  {
    return getEntries().get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  protected int getItemLayout()
  {
    return R.layout.chips_recipient_dropdown_item;
  }

  public int getItemViewType(int paramInt)
  {
    return ((RecipientEntry)getEntries().get(paramInt)).getEntryType();
  }

  protected int getPhotoId()
  {
    return 16908294;
  }

  public int getQueryType()
  {
    return this.mQueryType;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    RecipientEntry localRecipientEntry = (RecipientEntry)getEntries().get(paramInt);
    Object localObject = localRecipientEntry.getDisplayName();
    String str = localRecipientEntry.getDestination();
    if ((TextUtils.isEmpty((CharSequence)localObject)) || (TextUtils.equals((CharSequence)localObject, str)))
    {
      localObject = str;
      if (localRecipientEntry.isFirstLevel())
        str = null;
    }
    View localView;
    TextView localTextView1;
    TextView localTextView2;
    ImageView localImageView;
    if (paramView != null)
    {
      localView = paramView;
      localTextView1 = (TextView)localView.findViewById(getDisplayNameId());
      localTextView2 = (TextView)localView.findViewById(getDestinationId());
      TextView localTextView3 = (TextView)localView.findViewById(getDestinationTypeId());
      localImageView = (ImageView)localView.findViewById(getPhotoId());
      localTextView1.setText((CharSequence)localObject);
      if (TextUtils.isEmpty(str))
        break label264;
      localTextView2.setText(str);
      label147: if (localTextView3 != null)
        localTextView3.setText(this.mQuery.getTypeLabel(this.mContext.getResources(), localRecipientEntry.getDestinationType(), localRecipientEntry.getDestinationLabel()).toString().toUpperCase());
      if (!localRecipientEntry.isFirstLevel())
        break label285;
      localTextView1.setVisibility(0);
      if (localImageView != null)
      {
        localImageView.setVisibility(0);
        byte[] arrayOfByte = localRecipientEntry.getPhotoBytes();
        if ((arrayOfByte == null) || (localImageView == null))
          break label273;
        localImageView.setImageBitmap(BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length));
      }
    }
    label264: label273: label285: 
    do
    {
      return localView;
      localView = this.mInflater.inflate(getItemLayout(), paramViewGroup, false);
      break;
      localTextView2.setText(null);
      break label147;
      localImageView.setImageResource(getDefaultPhotoResource());
      return localView;
      localTextView1.setVisibility(8);
    }
    while (localImageView == null);
    localImageView.setVisibility(4);
    return localView;
  }

  public int getViewTypeCount()
  {
    return 1;
  }

  public boolean isEnabled(int paramInt)
  {
    return ((RecipientEntry)getEntries().get(paramInt)).isSelectable();
  }

  public void setAccount(Account paramAccount)
  {
    this.mAccount = paramAccount;
  }

  private final class DefaultFilter extends Filter
  {
    private DefaultFilter()
    {
    }

    public CharSequence convertResultToString(Object paramObject)
    {
      RecipientEntry localRecipientEntry = (RecipientEntry)paramObject;
      String str1 = localRecipientEntry.getDisplayName();
      String str2 = localRecipientEntry.getDestination();
      if ((TextUtils.isEmpty(str1)) || (TextUtils.equals(str1, str2)))
        return str2;
      return new Rfc822Token(str1, str2, null).toString();
    }

    protected Filter.FilterResults performFiltering(CharSequence paramCharSequence)
    {
      Filter.FilterResults localFilterResults = new Filter.FilterResults();
      Object localObject1 = null;
      Cursor localCursor1 = null;
      if (TextUtils.isEmpty(paramCharSequence))
        BaseRecipientAdapter.this.clearTempEntries();
      LinkedHashMap localLinkedHashMap;
      ArrayList localArrayList;
      HashSet localHashSet;
      while (true)
      {
        return localFilterResults;
        try
        {
          Cursor localCursor2 = BaseRecipientAdapter.this.doQuery(paramCharSequence, BaseRecipientAdapter.this.mPreferredMaxResultCount, null);
          localObject1 = localCursor2;
          localCursor1 = null;
          if (localObject1 == null)
            return localFilterResults;
          localLinkedHashMap = new LinkedHashMap();
          localArrayList = new ArrayList();
          localHashSet = new HashSet();
          while (localObject1.moveToNext())
            BaseRecipientAdapter.this.putOneEntry(new BaseRecipientAdapter.TemporaryEntry(localObject1), true, localLinkedHashMap, localArrayList, localHashSet);
        }
        finally
        {
          if (localObject1 != null)
            localObject1.close();
          if (localCursor1 != null)
            localCursor1.close();
        }
      }
      List localList1 = BaseRecipientAdapter.this.constructEntryList(false, localLinkedHashMap, localArrayList, localHashSet);
      List localList2;
      if (BaseRecipientAdapter.this.mPreferredMaxResultCount - localHashSet.size() > 0)
      {
        localCursor1 = BaseRecipientAdapter.this.mContentResolver.query(BaseRecipientAdapter.DirectoryListQuery.URI, BaseRecipientAdapter.DirectoryListQuery.PROJECTION, null, null, null);
        localList2 = BaseRecipientAdapter.this.setupOtherDirectories(localCursor1);
      }
      while (true)
      {
        localFilterResults.values = new BaseRecipientAdapter.DefaultFilterResult(localList1, localLinkedHashMap, localArrayList, localHashSet, localList2);
        localFilterResults.count = 1;
        break;
        localList2 = null;
        localCursor1 = null;
      }
    }

    protected void publishResults(CharSequence paramCharSequence, Filter.FilterResults paramFilterResults)
    {
      BaseRecipientAdapter.access$702(BaseRecipientAdapter.this, paramCharSequence);
      BaseRecipientAdapter.this.clearTempEntries();
      if (paramFilterResults.values != null)
      {
        BaseRecipientAdapter.DefaultFilterResult localDefaultFilterResult = (BaseRecipientAdapter.DefaultFilterResult)paramFilterResults.values;
        BaseRecipientAdapter.access$802(BaseRecipientAdapter.this, localDefaultFilterResult.entryMap);
        BaseRecipientAdapter.access$902(BaseRecipientAdapter.this, localDefaultFilterResult.nonAggregatedEntries);
        BaseRecipientAdapter.access$1002(BaseRecipientAdapter.this, localDefaultFilterResult.existingDestinations);
        if ((localDefaultFilterResult.entries.size() == 0) && (localDefaultFilterResult.paramsList != null))
          BaseRecipientAdapter.this.cacheCurrentEntries();
        BaseRecipientAdapter.this.updateEntries(localDefaultFilterResult.entries);
        if (localDefaultFilterResult.paramsList != null)
        {
          int i = BaseRecipientAdapter.this.mPreferredMaxResultCount - localDefaultFilterResult.existingDestinations.size();
          BaseRecipientAdapter.this.startSearchOtherDirectories(paramCharSequence, localDefaultFilterResult.paramsList, i);
        }
      }
    }
  }

  private static class DefaultFilterResult
  {
    public final List<RecipientEntry> entries;
    public final LinkedHashMap<Long, List<RecipientEntry>> entryMap;
    public final Set<String> existingDestinations;
    public final List<RecipientEntry> nonAggregatedEntries;
    public final List<BaseRecipientAdapter.DirectorySearchParams> paramsList;

    public DefaultFilterResult(List<RecipientEntry> paramList1, LinkedHashMap<Long, List<RecipientEntry>> paramLinkedHashMap, List<RecipientEntry> paramList2, Set<String> paramSet, List<BaseRecipientAdapter.DirectorySearchParams> paramList)
    {
      this.entries = paramList1;
      this.entryMap = paramLinkedHashMap;
      this.nonAggregatedEntries = paramList2;
      this.existingDestinations = paramSet;
      this.paramsList = paramList;
    }
  }

  private final class DelayedMessageHandler extends Handler
  {
    private DelayedMessageHandler()
    {
    }

    public void handleMessage(Message paramMessage)
    {
      if (BaseRecipientAdapter.this.mRemainingDirectoryCount > 0)
        BaseRecipientAdapter.this.updateEntries(BaseRecipientAdapter.access$400(BaseRecipientAdapter.this, true, BaseRecipientAdapter.this.mEntryMap, BaseRecipientAdapter.this.mNonAggregatedEntries, BaseRecipientAdapter.this.mExistingDestinations));
    }

    public void removeDelayedLoadMessage()
    {
      removeMessages(1);
    }

    public void sendDelayedLoadMessage()
    {
      sendMessageDelayed(obtainMessage(1, 0, 0, null), 1000L);
    }
  }

  private final class DirectoryFilter extends Filter
  {
    private int mLimit;
    private final BaseRecipientAdapter.DirectorySearchParams mParams;

    public DirectoryFilter(BaseRecipientAdapter.DirectorySearchParams arg2)
    {
      Object localObject;
      this.mParams = localObject;
    }

    public int getLimit()
    {
      try
      {
        int i = this.mLimit;
        return i;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    protected Filter.FilterResults performFiltering(CharSequence paramCharSequence)
    {
      Filter.FilterResults localFilterResults = new Filter.FilterResults();
      localFilterResults.values = null;
      localFilterResults.count = 0;
      if (!TextUtils.isEmpty(paramCharSequence))
      {
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = null;
        try
        {
          localCursor = BaseRecipientAdapter.this.doQuery(paramCharSequence, getLimit(), Long.valueOf(this.mParams.directoryId));
          if (localCursor != null)
            while (localCursor.moveToNext())
              localArrayList.add(new BaseRecipientAdapter.TemporaryEntry(localCursor));
        }
        finally
        {
          if (localCursor != null)
            localCursor.close();
        }
        if (localCursor != null)
          localCursor.close();
        if (!localArrayList.isEmpty())
        {
          localFilterResults.values = localArrayList;
          localFilterResults.count = 1;
        }
      }
      return localFilterResults;
    }

    protected void publishResults(CharSequence paramCharSequence, Filter.FilterResults paramFilterResults)
    {
      BaseRecipientAdapter.this.mDelayedMessageHandler.removeDelayedLoadMessage();
      if (TextUtils.equals(paramCharSequence, BaseRecipientAdapter.this.mCurrentConstraint))
      {
        if (paramFilterResults.count > 0)
        {
          Iterator localIterator = ((ArrayList)paramFilterResults.values).iterator();
          if (localIterator.hasNext())
          {
            BaseRecipientAdapter.TemporaryEntry localTemporaryEntry = (BaseRecipientAdapter.TemporaryEntry)localIterator.next();
            BaseRecipientAdapter localBaseRecipientAdapter = BaseRecipientAdapter.this;
            if (this.mParams.directoryId == 0L);
            for (boolean bool = true; ; bool = false)
            {
              localBaseRecipientAdapter.putOneEntry(localTemporaryEntry, bool, BaseRecipientAdapter.this.mEntryMap, BaseRecipientAdapter.this.mNonAggregatedEntries, BaseRecipientAdapter.this.mExistingDestinations);
              break;
            }
          }
        }
        BaseRecipientAdapter.access$1510(BaseRecipientAdapter.this);
        if (BaseRecipientAdapter.this.mRemainingDirectoryCount > 0)
          BaseRecipientAdapter.this.mDelayedMessageHandler.sendDelayedLoadMessage();
        if ((paramFilterResults.count > 0) || (BaseRecipientAdapter.this.mRemainingDirectoryCount == 0))
          BaseRecipientAdapter.this.clearTempEntries();
      }
      BaseRecipientAdapter.this.updateEntries(BaseRecipientAdapter.access$400(BaseRecipientAdapter.this, false, BaseRecipientAdapter.this.mEntryMap, BaseRecipientAdapter.this.mNonAggregatedEntries, BaseRecipientAdapter.this.mExistingDestinations));
    }

    public void setLimit(int paramInt)
    {
      try
      {
        this.mLimit = paramInt;
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }

  private static class DirectoryListQuery
  {
    public static final String[] PROJECTION = { "_id", "accountName", "accountType", "displayName", "packageName", "typeResourceId" };
    public static final Uri URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "directories");
  }

  public static final class DirectorySearchParams
  {
    public String accountName;
    public String accountType;
    public CharSequence constraint;
    public long directoryId;
    public String directoryType;
    public String displayName;
    public BaseRecipientAdapter.DirectoryFilter filter;
  }

  private static class PhotoQuery
  {
    public static final String[] PROJECTION = { "data15" };
  }

  private static class TemporaryEntry
  {
    public final long contactId;
    public final long dataId;
    public final String destination;
    public final String destinationLabel;
    public final int destinationType;
    public final String displayName;
    public final int displayNameSource;
    public final String thumbnailUriString;

    public TemporaryEntry(Cursor paramCursor)
    {
      this.displayName = paramCursor.getString(0);
      this.destination = paramCursor.getString(1);
      this.destinationType = paramCursor.getInt(2);
      this.destinationLabel = paramCursor.getString(3);
      this.contactId = paramCursor.getLong(4);
      this.dataId = paramCursor.getLong(5);
      this.thumbnailUriString = paramCursor.getString(6);
      this.displayNameSource = paramCursor.getInt(7);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.chips.BaseRecipientAdapter
 * JD-Core Version:    0.6.2
 */