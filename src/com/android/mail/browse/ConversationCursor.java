package com.android.mail.browse;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.DataSetObserver;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.UIProvider;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public final class ConversationCursor
  implements Cursor
{
  private static final Collection<Conversation> EMPTY_DELETION_LIST = Lists.newArrayList();
  private static final String LOG_TAG = LogTag.getLogTag();
  static ConversationProvider sProvider;
  private static ContentResolver sResolver;
  private static int sSequence = 0;
  private static int sUriColumnIndex;
  private HashMap<String, ContentValues> mCacheMap = new HashMap();
  private Object mCacheMapLock = new Object();
  private String[] mColumnNames;
  private final CursorObserver mCursorObserver;
  private boolean mCursorObserverRegistered = false;
  private boolean mDeferSync = false;
  private int mDeletedCount = 0;
  private boolean mInitialConversationLimit = false;
  private List<ConversationListener> mListeners = Lists.newArrayList();
  private final String mName;
  private boolean mPaused = false;
  private int mPosition = -1;
  private boolean mRefreshReady = false;
  private boolean mRefreshRequired = false;
  private RefreshTask mRefreshTask;
  private volatile UnderlyingCursorWrapper mRequeryCursor;
  UnderlyingCursorWrapper mUnderlyingCursor;
  private String[] qProjection;
  private Uri qUri;
  private List<Conversation> sMostlyDead = Lists.newArrayList();

  public ConversationCursor(Activity paramActivity, Uri paramUri, boolean paramBoolean, String paramString)
  {
    this.mInitialConversationLimit = paramBoolean;
    sResolver = paramActivity.getContentResolver();
    sUriColumnIndex = 1;
    this.qUri = paramUri;
    this.mName = paramString;
    this.qProjection = UIProvider.CONVERSATION_PROJECTION;
    this.mCursorObserver = new CursorObserver(new Handler(Looper.getMainLooper()));
  }

  private int apply(Context paramContext, Collection<ConversationOperation> paramCollection)
  {
    return sProvider.apply(paramCollection, this);
  }

  private int applyAction(Context paramContext, Collection<Conversation> paramCollection, int paramInt)
  {
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      localArrayList.add(new ConversationOperation(paramInt, (Conversation)localIterator.next()));
    return apply(paramContext, localArrayList);
  }

  private void cacheValue(String paramString1, String paramString2, Object paramObject)
  {
    if (offUiThread())
      LogUtils.e(LOG_TAG, new Error(), "cacheValue incorrectly being called from non-UI thread", new Object[0]);
    while (true)
    {
      ContentValues localContentValues;
      boolean bool;
      int i;
      int j;
      synchronized (this.mCacheMapLock)
      {
        localContentValues = (ContentValues)this.mCacheMap.get(paramString1);
        if (localContentValues == null)
        {
          localContentValues = new ContentValues();
          this.mCacheMap.put(paramString1, localContentValues);
        }
        if (paramString2 == "__deleted__")
        {
          bool = ((Boolean)paramObject).booleanValue();
          if (localContentValues.get(paramString2) == null)
            break label438;
          i = 1;
          if ((bool) && (i == 0))
          {
            this.mDeletedCount = (1 + this.mDeletedCount);
            String str4 = LOG_TAG;
            Object[] arrayOfObject3 = new Object[2];
            arrayOfObject3[0] = paramString1;
            arrayOfObject3[1] = Integer.valueOf(this.mDeletedCount);
            LogUtils.i(str4, "Deleted %s, incremented deleted count=%d", arrayOfObject3);
          }
        }
        else
        {
          if (!(paramObject instanceof Boolean))
            break label360;
          if (!((Boolean)paramObject).booleanValue())
            break label444;
          j = 1;
          localContentValues.put(paramString2, Integer.valueOf(j));
          localContentValues.put("__updatetime__", Long.valueOf(System.currentTimeMillis()));
          if (paramString2 != "__deleted__")
            LogUtils.i(LOG_TAG, "Caching value for %s: %s", new Object[] { paramString1, paramString2 });
          return;
        }
        if ((!bool) && (i != 0))
        {
          this.mDeletedCount = (-1 + this.mDeletedCount);
          localContentValues.remove(paramString2);
          String str3 = LOG_TAG;
          Object[] arrayOfObject2 = new Object[2];
          arrayOfObject2[0] = paramString1;
          arrayOfObject2[1] = Integer.valueOf(this.mDeletedCount);
          LogUtils.i(str3, "Undeleted %s, decremented deleted count=%d", arrayOfObject2);
          return;
        }
      }
      label438: label444: if (!bool)
      {
        String str1 = LOG_TAG;
        Object[] arrayOfObject1 = new Object[2];
        arrayOfObject1[0] = paramString1;
        arrayOfObject1[1] = Integer.valueOf(this.mDeletedCount);
        LogUtils.i(str1, "Undeleted %s, IGNORING, deleted count=%d", arrayOfObject1);
        return;
        label360: if ((paramObject instanceof Integer))
        {
          localContentValues.put(paramString2, (Integer)paramObject);
        }
        else if ((paramObject instanceof String))
        {
          localContentValues.put(paramString2, (String)paramObject);
        }
        else
        {
          String str2 = paramObject.getClass().getName();
          throw new IllegalArgumentException("Value class not compatible with cache: " + str2);
          i = 0;
          continue;
          j = 0;
        }
      }
    }
  }

  private void checkNotifyUI()
  {
    String str1 = LOG_TAG;
    Object[] arrayOfObject1 = new Object[2];
    boolean bool1;
    boolean bool2;
    if ((!this.mPaused) && (!this.mDeferSync))
    {
      bool1 = true;
      arrayOfObject1[0] = Boolean.valueOf(bool1);
      if ((!this.mRefreshReady) && ((!this.mRefreshRequired) || (this.mRefreshTask != null)))
        break label111;
      bool2 = true;
      label56: arrayOfObject1[1] = Boolean.valueOf(bool2);
      LogUtils.d(str1, "Received notify ui callback and sending a notification is enabled? %s and refresh ready ? %s", arrayOfObject1);
      if ((this.mPaused) || (this.mDeferSync))
        break label129;
      if ((!this.mRefreshRequired) || (this.mRefreshTask != null))
        break label117;
      notifyRefreshRequired();
    }
    label111: label117: 
    while (!this.mRefreshReady)
    {
      return;
      bool1 = false;
      break;
      bool2 = false;
      break label56;
    }
    notifyRefreshReady();
    return;
    label129: String str2 = LOG_TAG;
    Object[] arrayOfObject2 = new Object[2];
    String str3;
    if (this.mPaused)
    {
      str3 = "Paused ";
      arrayOfObject2[0] = str3;
      if (!this.mDeferSync)
        break label196;
    }
    label196: for (String str4 = "Defer"; ; str4 = "")
    {
      arrayOfObject2[1] = str4;
      LogUtils.i(str2, "[checkNotifyUI: %s%s", arrayOfObject2);
      return;
      str3 = "";
      break;
    }
  }

  private UnderlyingCursorWrapper doQuery(boolean paramBoolean)
  {
    Uri localUri = this.qUri;
    if (paramBoolean)
      localUri = localUri.buildUpon().appendQueryParameter("limit", "50").build();
    long l1 = System.currentTimeMillis();
    Cursor localCursor = sResolver.query(localUri, this.qProjection, null, null, null);
    if (localCursor == null)
      Log.w(LOG_TAG, "doQuery returning null cursor, uri: " + localUri);
    while (true)
    {
      return new UnderlyingCursorWrapper(localCursor);
      long l2 = System.currentTimeMillis() - l1;
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = localUri;
      arrayOfObject[1] = Long.valueOf(l2);
      arrayOfObject[2] = Integer.valueOf(localCursor.getCount());
      LogUtils.i(str, "ConversationCursor query: %s, %dms, %d results", arrayOfObject);
    }
  }

  private Object getCachedValue(int paramInt)
  {
    return getCachedValue(this.mUnderlyingCursor.getString(sUriColumnIndex), paramInt);
  }

  private Object getCachedValue(String paramString, int paramInt)
  {
    ContentValues localContentValues = (ContentValues)this.mCacheMap.get(paramString);
    if (localContentValues != null)
    {
      if (paramInt == -1);
      for (String str = "__deleted__"; ; str = this.mColumnNames[paramInt])
        return localContentValues.get(str);
    }
    return null;
  }

  private ArrayList<ConversationOperation> getOperationsForConversations(Collection<Conversation> paramCollection, int paramInt, ContentValues paramContentValues)
  {
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      localArrayList.add(getOperationForConversation((Conversation)localIterator.next(), paramInt, paramContentValues));
    return localArrayList;
  }

  private void notifyDataChanged()
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mName;
    LogUtils.i(str, "[Notify %s: onDataSetChanged()]", arrayOfObject);
    synchronized (this.mListeners)
    {
      Iterator localIterator = this.mListeners.iterator();
      if (localIterator.hasNext())
        ((ConversationListener)localIterator.next()).onDataSetChanged();
    }
  }

  private void notifyRefreshReady()
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = this.mName;
    arrayOfObject[1] = Integer.valueOf(this.mListeners.size());
    LogUtils.i(str, "[Notify %s: onRefreshReady(), %d listeners]", arrayOfObject);
    synchronized (this.mListeners)
    {
      Iterator localIterator = this.mListeners.iterator();
      if (localIterator.hasNext())
        ((ConversationListener)localIterator.next()).onRefreshReady();
    }
  }

  private void notifyRefreshRequired()
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mName;
    LogUtils.i(str, "[Notify %s: onRefreshRequired()]", arrayOfObject);
    if (!this.mDeferSync)
      synchronized (this.mListeners)
      {
        Iterator localIterator = this.mListeners.iterator();
        if (localIterator.hasNext())
          ((ConversationListener)localIterator.next()).onRefreshRequired();
      }
  }

  static boolean offUiThread()
  {
    return Looper.getMainLooper().getThread() != Thread.currentThread();
  }

  private void recalibratePosition()
  {
    int i = this.mPosition;
    moveToFirst();
    moveToPosition(i);
  }

  private void resetCursor(UnderlyingCursorWrapper paramUnderlyingCursorWrapper)
  {
    while (true)
    {
      String str1;
      Long localLong;
      int j;
      synchronized (this.mCacheMapLock)
      {
        Iterator localIterator = this.mCacheMap.entrySet().iterator();
        long l = System.currentTimeMillis();
        if (!localIterator.hasNext())
          break label294;
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        ContentValues localContentValues = (ContentValues)localEntry.getValue();
        str1 = (String)localEntry.getKey();
        if (localContentValues == null)
          break label266;
        localLong = localContentValues.getAsLong("__updatetime__");
        if ((localLong != null) && (l - localLong.longValue() < 10000L))
        {
          LogUtils.i(LOG_TAG, "IN resetCursor, keep recent changes to %s", new Object[] { str1 });
          i = 1;
          boolean bool1 = localContentValues.containsKey("__deleted__");
          j = 0;
          if (!bool1)
            break label358;
          boolean bool2 = paramUnderlyingCursorWrapper.contains(str1);
          j = 0;
          if (bool2)
            break label358;
          this.mDeletedCount = (-1 + this.mDeletedCount);
          j = 1;
          String str2 = LOG_TAG;
          Object[] arrayOfObject = new Object[2];
          arrayOfObject[0] = Integer.valueOf(this.mDeletedCount);
          arrayOfObject[1] = str1;
          LogUtils.i(str2, "IN resetCursor, sDeletedCount decremented to: %d by %s", arrayOfObject);
          break label358;
          localIterator.remove();
        }
      }
      int i = 0;
      if (localLong == null)
      {
        LogUtils.e(LOG_TAG, "null updateTime from mCacheMap for key: %s", new Object[] { str1 });
        i = 0;
        continue;
        label266: LogUtils.e(LOG_TAG, "null ContentValues from mCacheMap for key: %s", new Object[] { str1 });
        j = 0;
        i = 0;
        break label358;
        label294: if (this.mUnderlyingCursor != null)
          close();
        this.mUnderlyingCursor = paramUnderlyingCursorWrapper;
        this.mPosition = -1;
        this.mUnderlyingCursor.moveToPosition(this.mPosition);
        if (!this.mCursorObserverRegistered)
        {
          this.mUnderlyingCursor.registerContentObserver(this.mCursorObserver);
          this.mCursorObserverRegistered = true;
        }
        this.mRefreshRequired = false;
        return;
        label358: if (i != 0)
          if (j == 0);
      }
    }
  }

  private void setCursor(UnderlyingCursorWrapper paramUnderlyingCursorWrapper)
  {
    if (this.mUnderlyingCursor != null)
      close();
    this.mColumnNames = paramUnderlyingCursorWrapper.getColumnNames();
    this.mRefreshRequired = false;
    this.mRefreshReady = false;
    this.mRefreshTask = null;
    resetCursor(paramUnderlyingCursorWrapper);
  }

  private void underlyingChanged()
  {
    synchronized (this.mCacheMapLock)
    {
      boolean bool = this.mCursorObserverRegistered;
      if (!bool);
    }
    try
    {
      this.mUnderlyingCursor.unregisterContentObserver(this.mCursorObserver);
      label27: this.mCursorObserverRegistered = false;
      this.mRefreshRequired = true;
      if (!this.mPaused)
        notifyRefreshRequired();
      return;
      localObject2 = finally;
      throw localObject2;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      break label27;
    }
  }

  private void undoLocal()
  {
    sProvider.undo(this);
  }

  private static Uri uriFromCachingUri(Uri paramUri)
  {
    if (!paramUri.getAuthority().equals(ConversationProvider.AUTHORITY))
      return paramUri;
    List localList = paramUri.getPathSegments();
    Uri.Builder localBuilder = new Uri.Builder().scheme(paramUri.getScheme()).authority((String)localList.get(0));
    for (int i = 1; i < localList.size(); i++)
      localBuilder.appendPath((String)localList.get(i));
    return localBuilder.build();
  }

  private static String uriStringFromCachingUri(Uri paramUri)
  {
    return Uri.decode(uriFromCachingUri(paramUri).toString());
  }

  private static String uriToCachingUriString(Uri paramUri)
  {
    String str = paramUri.getAuthority();
    return paramUri.getScheme() + "://" + ConversationProvider.AUTHORITY + "/" + str + paramUri.getPath();
  }

  public void addListener(ConversationListener paramConversationListener)
  {
    synchronized (this.mListeners)
    {
      if (!this.mListeners.contains(paramConversationListener))
      {
        this.mListeners.add(paramConversationListener);
        return;
      }
      LogUtils.i(LOG_TAG, "Ignoring duplicate add of listener", new Object[0]);
    }
  }

  public int archive(Context paramContext, Collection<Conversation> paramCollection)
  {
    return applyAction(paramContext, paramCollection, 3);
  }

  boolean clearMostlyDead(String paramString)
  {
    Object localObject = getCachedValue(paramString, 15);
    if (localObject != null)
    {
      int i = ((Integer)localObject).intValue();
      if ((i & 0x1) != 0)
      {
        cacheValue(paramString, "conversationFlags", Integer.valueOf(i & 0xFFFFFFFE));
        return true;
      }
    }
    return false;
  }

  public void close()
  {
    if ((this.mUnderlyingCursor == null) || (this.mUnderlyingCursor.isClosed()) || (this.mCursorObserverRegistered));
    try
    {
      this.mUnderlyingCursor.unregisterContentObserver(this.mCursorObserver);
      label35: this.mCursorObserverRegistered = false;
      this.mUnderlyingCursor.close();
      return;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      break label35;
    }
  }

  void commitMostlyDead(Conversation paramConversation)
  {
    paramConversation.convFlags = (0xFFFFFFFE & paramConversation.convFlags);
    this.sMostlyDead.remove(paramConversation);
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramConversation.uri;
    LogUtils.i(str, "[All dead: %s]", arrayOfObject);
    if (this.sMostlyDead.isEmpty())
    {
      this.mDeferSync = false;
      checkNotifyUI();
    }
  }

  public void copyStringToBuffer(int paramInt, CharArrayBuffer paramCharArrayBuffer)
  {
    throw new UnsupportedOperationException();
  }

  public void deactivate()
  {
    throw new UnsupportedOperationException();
  }

  public int delete(Context paramContext, Collection<Conversation> paramCollection)
  {
    return applyAction(paramContext, paramCollection, 0);
  }

  public void disable()
  {
    close();
    this.mCacheMap.clear();
    this.mListeners.clear();
    this.mUnderlyingCursor = null;
  }

  public int discardDrafts(Context paramContext, Collection<Conversation> paramCollection)
  {
    return applyAction(paramContext, paramCollection, 8);
  }

  public byte[] getBlob(int paramInt)
  {
    Object localObject = getCachedValue(paramInt);
    if (localObject != null)
      return (byte[])localObject;
    return this.mUnderlyingCursor.getBlob(paramInt);
  }

  public int getColumnCount()
  {
    return this.mUnderlyingCursor.getColumnCount();
  }

  public int getColumnIndex(String paramString)
  {
    return this.mUnderlyingCursor.getColumnIndex(paramString);
  }

  public int getColumnIndexOrThrow(String paramString)
    throws IllegalArgumentException
  {
    return this.mUnderlyingCursor.getColumnIndexOrThrow(paramString);
  }

  public String getColumnName(int paramInt)
  {
    return this.mUnderlyingCursor.getColumnName(paramInt);
  }

  public String[] getColumnNames()
  {
    return this.mUnderlyingCursor.getColumnNames();
  }

  public Set<Long> getConversationIds()
  {
    if (this.mUnderlyingCursor != null)
      return this.mUnderlyingCursor.conversationIds();
    return null;
  }

  public int getCount()
  {
    if (this.mUnderlyingCursor == null)
      throw new IllegalStateException("getCount() on disabled cursor: " + this.mName + "(" + this.qUri + ")");
    return this.mUnderlyingCursor.getCount() - this.mDeletedCount;
  }

  public Set<String> getDeletedItems()
  {
    HashSet localHashSet;
    synchronized (this.mCacheMapLock)
    {
      localHashSet = Sets.newHashSet();
      Iterator localIterator = this.mCacheMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (((ContentValues)localEntry.getValue()).containsKey("__deleted__"))
          localHashSet.add(uriToCachingUriString(Uri.parse((String)localEntry.getKey())));
      }
    }
    return localHashSet;
  }

  public double getDouble(int paramInt)
  {
    Object localObject = getCachedValue(paramInt);
    if (localObject != null)
      return ((Double)localObject).doubleValue();
    return this.mUnderlyingCursor.getDouble(paramInt);
  }

  public Bundle getExtras()
  {
    if (this.mUnderlyingCursor != null)
      return this.mUnderlyingCursor.getExtras();
    return Bundle.EMPTY;
  }

  public float getFloat(int paramInt)
  {
    Object localObject = getCachedValue(paramInt);
    if (localObject != null)
      return ((Float)localObject).floatValue();
    return this.mUnderlyingCursor.getFloat(paramInt);
  }

  public int getInt(int paramInt)
  {
    Object localObject = getCachedValue(paramInt);
    if (localObject != null)
      return ((Integer)localObject).intValue();
    return this.mUnderlyingCursor.getInt(paramInt);
  }

  public long getLong(int paramInt)
  {
    Object localObject = getCachedValue(paramInt);
    if (localObject != null)
      return ((Long)localObject).longValue();
    return this.mUnderlyingCursor.getLong(paramInt);
  }

  public ConversationOperation getOperationForConversation(Conversation paramConversation, int paramInt, ContentValues paramContentValues)
  {
    return new ConversationOperation(paramInt, paramConversation, paramContentValues);
  }

  public int getPosition()
  {
    return this.mPosition;
  }

  public short getShort(int paramInt)
  {
    Object localObject = getCachedValue(paramInt);
    if (localObject != null)
      return ((Short)localObject).shortValue();
    return this.mUnderlyingCursor.getShort(paramInt);
  }

  public String getString(int paramInt)
  {
    if (paramInt == sUriColumnIndex)
      return uriToCachingUriString(Uri.parse(this.mUnderlyingCursor.getString(paramInt)));
    Object localObject = getCachedValue(paramInt);
    if (localObject != null)
      return (String)localObject;
    return this.mUnderlyingCursor.getString(paramInt);
  }

  public int getType(int paramInt)
  {
    return this.mUnderlyingCursor.getType(paramInt);
  }

  public boolean getWantsAllOnMoveCalls()
  {
    throw new UnsupportedOperationException();
  }

  public int hashCode()
  {
    return super.hashCode();
  }

  public boolean isAfterLast()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isBeforeFirst()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isClosed()
  {
    return (this.mUnderlyingCursor == null) || (this.mUnderlyingCursor.isClosed());
  }

  public boolean isFirst()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isLast()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isNull(int paramInt)
  {
    throw new UnsupportedOperationException();
  }

  public boolean isRefreshReady()
  {
    return this.mRefreshReady;
  }

  public boolean isRefreshRequired()
  {
    return this.mRefreshRequired;
  }

  // ERROR //
  public void load()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 81	com/android/mail/browse/ConversationCursor:mCacheMapLock	Ljava/lang/Object;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: getstatic 60	com/android/mail/browse/ConversationCursor:LOG_TAG	Ljava/lang/String;
    //   10: ldc_w 750
    //   13: iconst_0
    //   14: anewarray 4	java/lang/Object
    //   17: invokestatic 287	com/android/mail/utils/LogUtils:i	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   20: pop
    //   21: aload_0
    //   22: aload_0
    //   23: aload_0
    //   24: getfield 89	com/android/mail/browse/ConversationCursor:mInitialConversationLimit	Z
    //   27: invokespecial 183	com/android/mail/browse/ConversationCursor:doQuery	(Z)Lcom/android/mail/browse/ConversationCursor$UnderlyingCursorWrapper;
    //   30: invokespecial 752	com/android/mail/browse/ConversationCursor:setCursor	(Lcom/android/mail/browse/ConversationCursor$UnderlyingCursorWrapper;)V
    //   33: aload_0
    //   34: getfield 89	com/android/mail/browse/ConversationCursor:mInitialConversationLimit	Z
    //   37: ifeq +13 -> 50
    //   40: aload_0
    //   41: iconst_0
    //   42: putfield 89	com/android/mail/browse/ConversationCursor:mInitialConversationLimit	Z
    //   45: aload_0
    //   46: invokevirtual 755	com/android/mail/browse/ConversationCursor:refresh	()Z
    //   49: pop
    //   50: aload_1
    //   51: monitorexit
    //   52: return
    //   53: astore_2
    //   54: aload_0
    //   55: getfield 89	com/android/mail/browse/ConversationCursor:mInitialConversationLimit	Z
    //   58: ifeq +13 -> 71
    //   61: aload_0
    //   62: iconst_0
    //   63: putfield 89	com/android/mail/browse/ConversationCursor:mInitialConversationLimit	Z
    //   66: aload_0
    //   67: invokevirtual 755	com/android/mail/browse/ConversationCursor:refresh	()Z
    //   70: pop
    //   71: aload_2
    //   72: athrow
    //   73: astore_3
    //   74: aload_1
    //   75: monitorexit
    //   76: aload_3
    //   77: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   7	33	53	finally
    //   33	50	73	finally
    //   50	52	73	finally
    //   54	71	73	finally
    //   71	73	73	finally
    //   74	76	73	finally
  }

  public int mostlyArchive(Context paramContext, Collection<Conversation> paramCollection)
  {
    return applyAction(paramContext, paramCollection, 131);
  }

  public int mostlyDelete(Context paramContext, Collection<Conversation> paramCollection)
  {
    return applyAction(paramContext, paramCollection, 128);
  }

  public int mostlyDestructiveUpdate(Context paramContext, Collection<Conversation> paramCollection, String paramString1, String paramString2)
  {
    return mostlyDestructiveUpdate(paramContext, paramCollection, new String[] { paramString1 }, new String[] { paramString2 });
  }

  public int mostlyDestructiveUpdate(Context paramContext, Collection<Conversation> paramCollection, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    ContentValues localContentValues = new ContentValues();
    for (int i = 0; i < paramArrayOfString1.length; i++)
      localContentValues.put(paramArrayOfString1[i], paramArrayOfString2[i]);
    return apply(paramContext, getOperationsForConversations(paramCollection, 130, localContentValues));
  }

  public boolean move(int paramInt)
  {
    throw new UnsupportedOperationException("move unsupported!");
  }

  public boolean moveToFirst()
  {
    if (this.mUnderlyingCursor == null)
      throw new IllegalStateException("moveToFirst() on disabled cursor: " + this.mName + "(" + this.qUri + ")");
    this.mUnderlyingCursor.moveToPosition(-1);
    this.mPosition = -1;
    return moveToNext();
  }

  public boolean moveToLast()
  {
    throw new UnsupportedOperationException("moveToLast unsupported!");
  }

  public boolean moveToNext()
  {
    do
      if (!this.mUnderlyingCursor.moveToNext())
      {
        this.mPosition = getCount();
        String str = LOG_TAG;
        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = Integer.valueOf(this.mPosition);
        arrayOfObject[1] = Integer.valueOf(this.mUnderlyingCursor.getPosition());
        arrayOfObject[2] = Integer.valueOf(this.mDeletedCount);
        LogUtils.i(str, "*** moveToNext returns false; pos = %d, und = %d, del = %d", arrayOfObject);
        return false;
      }
    while ((getCachedValue(-1) instanceof Integer));
    this.mPosition = (1 + this.mPosition);
    return true;
  }

  public boolean moveToPosition(int paramInt)
  {
    boolean bool = true;
    if (this.mUnderlyingCursor == null)
      throw new IllegalStateException("moveToPosition() on disabled cursor: " + this.mName + "(" + this.qUri + ")");
    if (this.mUnderlyingCursor.getPosition() == -1)
    {
      String str2 = LOG_TAG;
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Integer.valueOf(this.mPosition);
      arrayOfObject2[bool] = Integer.valueOf(paramInt);
      LogUtils.i(str2, "*** Underlying cursor position is -1 asking to move from %d to %d", arrayOfObject2);
    }
    if (paramInt == 0)
      return moveToFirst();
    if (paramInt < 0)
    {
      this.mPosition = -1;
      this.mUnderlyingCursor.moveToPosition(this.mPosition);
      return false;
    }
    if (paramInt == this.mPosition)
    {
      if (paramInt < getCount());
      while (true)
      {
        return bool;
        bool = false;
      }
    }
    if (paramInt > this.mPosition)
    {
      while (paramInt > this.mPosition)
        if (!moveToNext())
          return false;
      return bool;
    }
    if ((paramInt >= 0) && (this.mPosition - paramInt > paramInt))
    {
      String str1 = LOG_TAG;
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = Integer.valueOf(this.mPosition);
      arrayOfObject1[bool] = Integer.valueOf(paramInt);
      LogUtils.i(str1, "*** Move from %d to %d, starting from first", arrayOfObject1);
      moveToFirst();
      return moveToPosition(paramInt);
    }
    while (paramInt < this.mPosition)
      if (!moveToPrevious())
        return false;
    return bool;
  }

  public boolean moveToPrevious()
  {
    do
      if (!this.mUnderlyingCursor.moveToPrevious())
      {
        this.mPosition = -1;
        return false;
      }
    while ((getCachedValue(-1) instanceof Integer));
    this.mPosition = (-1 + this.mPosition);
    return true;
  }

  public int mute(Context paramContext, Collection<Conversation> paramCollection)
  {
    return applyAction(paramContext, paramCollection, 4);
  }

  public void pause()
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mName;
    LogUtils.i(str, "[Paused: %s]", arrayOfObject);
    this.mPaused = true;
  }

  public boolean refresh()
  {
    String str1 = LOG_TAG;
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = this.mName;
    LogUtils.i(str1, "[refresh() %s]", arrayOfObject1);
    synchronized (this.mCacheMapLock)
    {
      if (this.mRefreshTask != null)
      {
        String str2 = LOG_TAG;
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = this.mName;
        arrayOfObject2[1] = Integer.valueOf(this.mRefreshTask.hashCode());
        LogUtils.i(str2, "[refresh() %s returning; already running %d]", arrayOfObject2);
        return false;
      }
      this.mRefreshTask = new RefreshTask(null);
      this.mRefreshTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
      return true;
    }
  }

  public void registerContentObserver(ContentObserver paramContentObserver)
  {
  }

  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
  }

  public void removeListener(ConversationListener paramConversationListener)
  {
    synchronized (this.mListeners)
    {
      this.mListeners.remove(paramConversationListener);
      return;
    }
  }

  public int reportNotSpam(Context paramContext, Collection<Conversation> paramCollection)
  {
    return applyAction(paramContext, paramCollection, 6);
  }

  public int reportPhishing(Context paramContext, Collection<Conversation> paramCollection)
  {
    return applyAction(paramContext, paramCollection, 7);
  }

  public int reportSpam(Context paramContext, Collection<Conversation> paramCollection)
  {
    return applyAction(paramContext, paramCollection, 5);
  }

  public boolean requery()
  {
    return true;
  }

  public Bundle respond(Bundle paramBundle)
  {
    if (this.mUnderlyingCursor != null)
      return this.mUnderlyingCursor.respond(paramBundle);
    return Bundle.EMPTY;
  }

  public void resume()
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mName;
    LogUtils.i(str, "[Resumed: %s]", arrayOfObject);
    this.mPaused = false;
    checkNotifyUI();
  }

  public void setConversationColumn(Uri paramUri, String paramString, Object paramObject)
  {
    String str = uriStringFromCachingUri(paramUri);
    synchronized (this.mCacheMapLock)
    {
      cacheValue(str, paramString, paramObject);
      notifyDataChanged();
      return;
    }
  }

  void setMostlyDead(String paramString, Conversation paramConversation)
  {
    LogUtils.i(LOG_TAG, "[Mostly dead, deferring: %s] ", new Object[] { paramString });
    cacheValue(paramString, "conversationFlags", Integer.valueOf(1));
    paramConversation.convFlags = (0x1 | paramConversation.convFlags);
    this.sMostlyDead.add(paramConversation);
    this.mDeferSync = true;
  }

  public void setNotificationUri(ContentResolver paramContentResolver, Uri paramUri)
  {
    throw new UnsupportedOperationException();
  }

  public void sync()
  {
    if (this.mRequeryCursor == null)
    {
      String str2 = LOG_TAG;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = this.mName;
      LogUtils.i(str2, "[sync() %s; no requery cursor]", arrayOfObject2);
      return;
    }
    synchronized (this.mCacheMapLock)
    {
      String str1 = LOG_TAG;
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = this.mName;
      LogUtils.i(str1, "[sync() %s]", arrayOfObject1);
      resetCursor(this.mRequeryCursor);
      this.mRequeryCursor = null;
      this.mRefreshTask = null;
      this.mRefreshReady = false;
      notifyDataChanged();
      return;
    }
  }

  public void undo(final Context paramContext, final Uri paramUri)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        Cursor localCursor = paramContext.getContentResolver().query(paramUri, UIProvider.UNDO_PROJECTION, null, null, null);
        if (localCursor != null)
          localCursor.close();
      }
    }).start();
    undoLocal();
  }

  public void unregisterContentObserver(ContentObserver paramContentObserver)
  {
  }

  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
  }

  public int updateBoolean(Context paramContext, Conversation paramConversation, String paramString, boolean paramBoolean)
  {
    return updateBoolean(paramContext, Arrays.asList(new Conversation[] { paramConversation }), paramString, paramBoolean);
  }

  public int updateBoolean(Context paramContext, Collection<Conversation> paramCollection, String paramString, boolean paramBoolean)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put(paramString, Boolean.valueOf(paramBoolean));
    return updateValues(paramContext, paramCollection, localContentValues);
  }

  public int updateBulkValues(Context paramContext, Collection<ConversationOperation> paramCollection)
  {
    return apply(paramContext, paramCollection);
  }

  public int updateInt(Context paramContext, Collection<Conversation> paramCollection, String paramString, int paramInt)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put(paramString, Integer.valueOf(paramInt));
    return updateValues(paramContext, paramCollection, localContentValues);
  }

  public int updateStrings(Context paramContext, Collection<Conversation> paramCollection, String paramString, ArrayList<String> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    ContentValues localContentValues = new ContentValues();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Conversation localConversation = (Conversation)localIterator.next();
      localContentValues.put(paramString, (String)paramArrayList.get(0));
      localArrayList.add(getOperationForConversation(localConversation, 2, localContentValues));
    }
    return apply(paramContext, localArrayList);
  }

  public int updateValues(Context paramContext, Collection<Conversation> paramCollection, ContentValues paramContentValues)
  {
    return apply(paramContext, getOperationsForConversations(paramCollection, 2, paramContentValues));
  }

  public static abstract interface ConversationListener
  {
    public abstract void onDataSetChanged();

    public abstract void onRefreshReady();

    public abstract void onRefreshRequired();
  }

  public class ConversationOperation
  {
    private final Conversation mConversation;
    private final boolean mLocalDeleteOnUpdate;
    private final boolean mMostlyDead;
    private boolean mRecalibrateRequired = true;
    private final int mType;
    private final Uri mUri;
    private final ContentValues mValues;

    public ConversationOperation(int paramConversation, Conversation arg3)
    {
      this(paramConversation, localConversation, null);
    }

    public ConversationOperation(int paramConversation, Conversation paramContentValues, ContentValues arg4)
    {
      this.mType = paramConversation;
      this.mUri = paramContentValues.uri;
      this.mConversation = paramContentValues;
      Object localObject;
      this.mValues = localObject;
      this.mLocalDeleteOnUpdate = paramContentValues.localDeleteOnUpdate;
      this.mMostlyDead = paramContentValues.isMostlyDead();
    }

    private ContentProviderOperation execute(Uri paramUri)
    {
      Uri localUri = paramUri.buildUpon().appendQueryParameter("seq", Integer.toString(ConversationCursor.sSequence)).build();
      switch (this.mType)
      {
      default:
        throw new UnsupportedOperationException("No such ConversationOperation type: " + this.mType);
      case 2:
        if (this.mLocalDeleteOnUpdate)
          ConversationCursor.sProvider.deleteLocal(this.mUri, ConversationCursor.this);
        while (!this.mMostlyDead)
        {
          return ContentProviderOperation.newUpdate(localUri).withValues(this.mValues).build();
          ConversationCursor.sProvider.updateLocal(this.mUri, this.mValues, ConversationCursor.this);
          this.mRecalibrateRequired = false;
        }
        ConversationCursor.sProvider.commitMostlyDead(this.mConversation, ConversationCursor.this);
        return null;
      case 130:
        ConversationCursor.sProvider.setMostlyDead(this.mConversation, ConversationCursor.this);
        return ContentProviderOperation.newUpdate(localUri).withValues(this.mValues).build();
      case 1:
        ConversationCursor.ConversationProvider.access$2200(ConversationCursor.sProvider, this.mUri, this.mValues);
        return ContentProviderOperation.newInsert(localUri).withValues(this.mValues).build();
      case 0:
        ConversationCursor.sProvider.deleteLocal(this.mUri, ConversationCursor.this);
        if (!this.mMostlyDead)
          return ContentProviderOperation.newDelete(localUri).build();
        ConversationCursor.sProvider.commitMostlyDead(this.mConversation, ConversationCursor.this);
        return null;
      case 128:
        ConversationCursor.sProvider.setMostlyDead(this.mConversation, ConversationCursor.this);
        return ContentProviderOperation.newDelete(localUri).build();
      case 3:
        ConversationCursor.sProvider.deleteLocal(this.mUri, ConversationCursor.this);
        if (!this.mMostlyDead)
          return ContentProviderOperation.newUpdate(localUri).withValue("operation", "archive").build();
        ConversationCursor.sProvider.commitMostlyDead(this.mConversation, ConversationCursor.this);
        return null;
      case 131:
        ConversationCursor.sProvider.setMostlyDead(this.mConversation, ConversationCursor.this);
        return ContentProviderOperation.newUpdate(localUri).withValue("operation", "archive").build();
      case 4:
        if (this.mLocalDeleteOnUpdate)
          ConversationCursor.sProvider.deleteLocal(this.mUri, ConversationCursor.this);
        return ContentProviderOperation.newUpdate(localUri).withValue("operation", "mute").build();
      case 5:
      case 6:
        ConversationCursor.sProvider.deleteLocal(this.mUri, ConversationCursor.this);
        if (this.mType == 5);
        for (String str = "report_spam"; ; str = "report_not_spam")
          return ContentProviderOperation.newUpdate(localUri).withValue("operation", str).build();
      case 7:
        ConversationCursor.sProvider.deleteLocal(this.mUri, ConversationCursor.this);
        return ContentProviderOperation.newUpdate(localUri).withValue("operation", "report_phishing").build();
      case 8:
      }
      ConversationCursor.sProvider.deleteLocal(this.mUri, ConversationCursor.this);
      return ContentProviderOperation.newUpdate(localUri).withValue("operation", "discard_drafts").build();
    }
  }

  public static abstract class ConversationProvider extends ContentProvider
  {
    public static String AUTHORITY;
    private ArrayList<Uri> mUndoDeleteUris = new ArrayList();
    private int mUndoSequence = 0;

    private void insertLocal(Uri paramUri, ContentValues paramContentValues)
    {
    }

    void addToUndoSequence(Uri paramUri)
    {
      if (ConversationCursor.sSequence != this.mUndoSequence)
      {
        this.mUndoSequence = ConversationCursor.sSequence;
        this.mUndoDeleteUris.clear();
      }
      this.mUndoDeleteUris.add(paramUri);
    }

    public int apply(Collection<ConversationCursor.ConversationOperation> paramCollection, ConversationCursor paramConversationCursor)
    {
      HashMap localHashMap = new HashMap();
      ConversationCursor.access$1408();
      int i = 0;
      Iterator localIterator1 = paramCollection.iterator();
      while (localIterator1.hasNext())
      {
        ConversationCursor.ConversationOperation localConversationOperation = (ConversationCursor.ConversationOperation)localIterator1.next();
        Uri localUri = ConversationCursor.uriFromCachingUri(localConversationOperation.mUri);
        String str2 = localUri.getAuthority();
        ArrayList localArrayList2 = (ArrayList)localHashMap.get(str2);
        if (localArrayList2 == null)
        {
          localArrayList2 = new ArrayList();
          localHashMap.put(str2, localArrayList2);
        }
        ContentProviderOperation localContentProviderOperation = localConversationOperation.execute(localUri);
        if (localContentProviderOperation != null)
          localArrayList2.add(localContentProviderOperation);
        if (localConversationOperation.mRecalibrateRequired)
          i = 1;
      }
      if (i != 0)
        paramConversationCursor.recalibratePosition();
      paramConversationCursor.notifyDataChanged();
      boolean bool = ConversationCursor.offUiThread();
      Iterator localIterator2 = localHashMap.keySet().iterator();
      while (true)
      {
        final String str1;
        final ArrayList localArrayList1;
        if (localIterator2.hasNext())
        {
          str1 = (String)localIterator2.next();
          localArrayList1 = (ArrayList)localHashMap.get(str1);
          if (!bool);
        }
        try
        {
          ConversationCursor.sResolver.applyBatch(str1, localArrayList1);
        }
        catch (RemoteException localRemoteException)
        {
          continue;
          new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                ConversationCursor.sResolver.applyBatch(str1, localArrayList1);
                return;
              }
              catch (OperationApplicationException localOperationApplicationException)
              {
              }
              catch (RemoteException localRemoteException)
              {
              }
            }
          }).start();
          continue;
          return ConversationCursor.sSequence;
        }
        catch (OperationApplicationException localOperationApplicationException)
        {
        }
      }
    }

    boolean clearMostlyDead(Uri paramUri, ConversationCursor paramConversationCursor)
    {
      return paramConversationCursor.clearMostlyDead(ConversationCursor.uriStringFromCachingUri(paramUri));
    }

    void commitMostlyDead(Conversation paramConversation, ConversationCursor paramConversationCursor)
    {
      paramConversationCursor.commitMostlyDead(paramConversation);
    }

    public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
    {
      throw new IllegalStateException("Unexpected call to ConversationProvider.delete");
    }

    void deleteLocal(Uri paramUri, ConversationCursor paramConversationCursor)
    {
      paramConversationCursor.cacheValue(ConversationCursor.access$1500(paramUri), "__deleted__", Boolean.valueOf(true));
      addToUndoSequence(paramUri);
    }

    protected abstract String getAuthority();

    public String getType(Uri paramUri)
    {
      return null;
    }

    public Uri insert(Uri paramUri, ContentValues paramContentValues)
    {
      insertLocal(paramUri, paramContentValues);
      return ProviderExecute.opInsert(paramUri, paramContentValues);
    }

    public boolean onCreate()
    {
      ConversationCursor.sProvider = this;
      AUTHORITY = getAuthority();
      return true;
    }

    public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
    {
      return ConversationCursor.sResolver.query(ConversationCursor.uriFromCachingUri(paramUri), paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
    }

    void setMostlyDead(Conversation paramConversation, ConversationCursor paramConversationCursor)
    {
      Uri localUri = paramConversation.uri;
      paramConversationCursor.setMostlyDead(ConversationCursor.uriStringFromCachingUri(localUri), paramConversation);
      addToUndoSequence(localUri);
    }

    void undeleteLocal(Uri paramUri, ConversationCursor paramConversationCursor)
    {
      paramConversationCursor.cacheValue(ConversationCursor.access$1500(paramUri), "__deleted__", Boolean.valueOf(false));
    }

    public void undo(ConversationCursor paramConversationCursor)
    {
      if (ConversationCursor.sSequence == this.mUndoSequence)
      {
        Iterator localIterator = this.mUndoDeleteUris.iterator();
        while (localIterator.hasNext())
        {
          Uri localUri = (Uri)localIterator.next();
          if (!clearMostlyDead(localUri, paramConversationCursor))
            undeleteLocal(localUri, paramConversationCursor);
        }
        this.mUndoSequence = 0;
        paramConversationCursor.recalibratePosition();
        paramConversationCursor.notifyDataChanged();
      }
    }

    public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
    {
      throw new IllegalStateException("Unexpected call to ConversationProvider.delete");
    }

    void updateLocal(Uri paramUri, ContentValues paramContentValues, ConversationCursor paramConversationCursor)
    {
      if (paramContentValues == null);
      while (true)
      {
        return;
        String str1 = ConversationCursor.uriStringFromCachingUri(paramUri);
        Iterator localIterator = paramContentValues.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str2 = (String)localIterator.next();
          paramConversationCursor.cacheValue(str1, str2, paramContentValues.get(str2));
        }
      }
    }

    static class ProviderExecute
      implements Runnable
    {
      final int mCode;
      final Uri mUri;
      final ContentValues mValues;

      ProviderExecute(int paramInt, Uri paramUri, ContentValues paramContentValues)
      {
        this.mCode = paramInt;
        this.mUri = ConversationCursor.uriFromCachingUri(paramUri);
        this.mValues = paramContentValues;
      }

      static Uri opInsert(Uri paramUri, ContentValues paramContentValues)
      {
        ProviderExecute localProviderExecute = new ProviderExecute(1, paramUri, paramContentValues);
        if (ConversationCursor.offUiThread())
          return (Uri)localProviderExecute.go();
        new Thread(localProviderExecute).start();
        return null;
      }

      public Object go()
      {
        switch (this.mCode)
        {
        default:
          return null;
        case 0:
          return Integer.valueOf(ConversationCursor.sResolver.delete(this.mUri, null, null));
        case 1:
          return ConversationCursor.sResolver.insert(this.mUri, this.mValues);
        case 2:
        }
        return Integer.valueOf(ConversationCursor.sResolver.update(this.mUri, this.mValues, null, null));
      }

      public void run()
      {
        go();
      }
    }
  }

  private class CursorObserver extends ContentObserver
  {
    public CursorObserver(Handler arg2)
    {
      super();
    }

    public void onChange(boolean paramBoolean)
    {
      ConversationCursor.this.underlyingChanged();
    }
  }

  private class RefreshTask extends AsyncTask<Void, Void, Void>
  {
    private ConversationCursor.UnderlyingCursorWrapper mCursor = null;

    private RefreshTask()
    {
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      String str = ConversationCursor.LOG_TAG;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = ConversationCursor.this.mName;
      arrayOfObject[1] = Integer.valueOf(hashCode());
      LogUtils.i(str, "[Start refresh of %s: %d]", arrayOfObject);
      this.mCursor = ConversationCursor.this.doQuery(false);
      this.mCursor.getCount();
      return null;
    }

    protected void onCancelled()
    {
      String str = ConversationCursor.LOG_TAG;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(hashCode());
      LogUtils.i(str, "[Ignoring refresh result: %d]", arrayOfObject);
      if (this.mCursor != null)
        this.mCursor.close();
    }

    protected void onPostExecute(Void paramVoid)
    {
      for (boolean bool = true; ; bool = false)
        synchronized (ConversationCursor.this.mCacheMapLock)
        {
          String str1 = ConversationCursor.LOG_TAG;
          Object[] arrayOfObject1 = new Object[1];
          if ((!ConversationCursor.this.mPaused) && (!ConversationCursor.this.mDeferSync))
          {
            arrayOfObject1[0] = Boolean.valueOf(bool);
            LogUtils.d(str1, "Received notify ui callback and sending a notification is enabled? %s", arrayOfObject1);
            if (ConversationCursor.this.isClosed())
            {
              onCancelled();
              return;
            }
            ConversationCursor.access$702(ConversationCursor.this, this.mCursor);
            ConversationCursor.access$802(ConversationCursor.this, true);
            String str2 = ConversationCursor.LOG_TAG;
            Object[] arrayOfObject2 = new Object[2];
            arrayOfObject2[0] = ConversationCursor.this.mName;
            arrayOfObject2[1] = Integer.valueOf(hashCode());
            LogUtils.i(str2, "[Query done %s: %d]", arrayOfObject2);
            if ((!ConversationCursor.this.mDeferSync) && (!ConversationCursor.this.mPaused))
              ConversationCursor.this.notifyRefreshReady();
            return;
          }
        }
    }
  }

  private class UnderlyingCursorWrapper extends CursorWrapper
  {
    private final Set<Long> mConversationIds;
    private final Set<String> mConversationUris;

    public UnderlyingCursorWrapper(Cursor arg2)
    {
      super();
      ImmutableSet.Builder localBuilder1 = new ImmutableSet.Builder();
      ImmutableSet.Builder localBuilder2 = new ImmutableSet.Builder();
      if ((localCursor != null) && (localCursor.moveToFirst()))
      {
        boolean bool = Utils.disableConversationCursorNetworkAccess(localCursor);
        do
        {
          localBuilder1.add(localCursor.getString(ConversationCursor.sUriColumnIndex));
          localBuilder2.add(Long.valueOf(localCursor.getLong(0)));
        }
        while (localCursor.moveToNext());
        if (bool)
          Utils.enableConversationCursorNetworkAccess(localCursor);
      }
      this.mConversationUris = localBuilder1.build();
      this.mConversationIds = localBuilder2.build();
    }

    public boolean contains(String paramString)
    {
      return this.mConversationUris.contains(paramString);
    }

    public Set<Long> conversationIds()
    {
      return this.mConversationIds;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ConversationCursor
 * JD-Core Version:    0.6.2
 */