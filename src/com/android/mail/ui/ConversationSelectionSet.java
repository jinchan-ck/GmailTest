package com.android.mail.ui;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import com.android.mail.browse.ConversationCursor;
import com.android.mail.browse.ConversationItemView;
import com.android.mail.browse.ConversationItemViewModel;
import com.android.mail.providers.Conversation;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConversationSelectionSet
  implements Parcelable
{
  public static final Parcelable.ClassLoaderCreator<ConversationSelectionSet> CREATOR = new Parcelable.ClassLoaderCreator()
  {
    public ConversationSelectionSet createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ConversationSelectionSet(paramAnonymousParcel, null, null);
    }

    public ConversationSelectionSet createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
    {
      return new ConversationSelectionSet(paramAnonymousParcel, paramAnonymousClassLoader, null);
    }

    public ConversationSelectionSet[] newArray(int paramAnonymousInt)
    {
      return new ConversationSelectionSet[paramAnonymousInt];
    }
  };
  private final BiMap<String, Long> mConversationUriToIdMap = HashBiMap.create();
  private final HashMap<Long, Conversation> mInternalMap = new HashMap();
  private final HashMap<Long, ConversationItemView> mInternalViewMap = new HashMap();
  private final Object mLock = new Object();
  final ArrayList<ConversationSetObserver> mObservers = new ArrayList();

  public ConversationSelectionSet()
  {
  }

  private ConversationSelectionSet(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    Parcelable[] arrayOfParcelable = paramParcel.readParcelableArray(paramClassLoader);
    int i = arrayOfParcelable.length;
    for (int j = 0; j < i; j++)
    {
      Conversation localConversation = (Conversation)arrayOfParcelable[j];
      put(Long.valueOf(localConversation.id), localConversation);
    }
  }

  private void dispatchOnBecomeUnempty(ArrayList<ConversationSetObserver> paramArrayList)
  {
    synchronized (this.mLock)
    {
      Iterator localIterator = paramArrayList.iterator();
      if (localIterator.hasNext())
        ((ConversationSetObserver)localIterator.next()).onSetPopulated(this);
    }
  }

  private void dispatchOnChange(ArrayList<ConversationSetObserver> paramArrayList)
  {
    synchronized (this.mLock)
    {
      Iterator localIterator = paramArrayList.iterator();
      if (localIterator.hasNext())
        ((ConversationSetObserver)localIterator.next()).onSetChanged(this);
    }
  }

  private void dispatchOnEmpty(ArrayList<ConversationSetObserver> paramArrayList)
  {
    synchronized (this.mLock)
    {
      Iterator localIterator = paramArrayList.iterator();
      if (localIterator.hasNext())
        ((ConversationSetObserver)localIterator.next()).onSetEmpty();
    }
  }

  private void put(Long paramLong, ConversationItemView paramConversationItemView)
  {
    synchronized (this.mLock)
    {
      boolean bool = this.mInternalMap.isEmpty();
      this.mInternalViewMap.put(paramLong, paramConversationItemView);
      this.mInternalMap.put(paramLong, paramConversationItemView.mHeader.conversation);
      this.mConversationUriToIdMap.put(paramConversationItemView.mHeader.conversation.uri.toString(), paramLong);
      ArrayList localArrayList = Lists.newArrayList(this.mObservers);
      dispatchOnChange(localArrayList);
      if (bool)
        dispatchOnBecomeUnempty(localArrayList);
      return;
    }
  }

  private void put(Long paramLong, Conversation paramConversation)
  {
    synchronized (this.mLock)
    {
      boolean bool = this.mInternalMap.isEmpty();
      this.mInternalMap.put(paramLong, paramConversation);
      this.mInternalViewMap.put(paramLong, null);
      this.mConversationUriToIdMap.put(paramConversation.uri.toString(), paramLong);
      ArrayList localArrayList = Lists.newArrayList(this.mObservers);
      dispatchOnChange(localArrayList);
      if (bool)
        dispatchOnBecomeUnempty(localArrayList);
      return;
    }
  }

  private void remove(Long paramLong)
  {
    synchronized (this.mLock)
    {
      removeAll(Collections.singleton(paramLong));
      return;
    }
  }

  private void removeAll(Collection<Long> paramCollection)
  {
    int i;
    while (true)
    {
      synchronized (this.mLock)
      {
        if (!this.mInternalMap.isEmpty())
        {
          i = 1;
          BiMap localBiMap = this.mConversationUriToIdMap.inverse();
          Iterator localIterator = paramCollection.iterator();
          if (!localIterator.hasNext())
            break;
          Long localLong = (Long)localIterator.next();
          this.mInternalViewMap.remove(localLong);
          this.mInternalMap.remove(localLong);
          localBiMap.remove(localLong);
        }
      }
      i = 0;
    }
    ArrayList localArrayList = Lists.newArrayList(this.mObservers);
    dispatchOnChange(localArrayList);
    if ((this.mInternalMap.isEmpty()) && (i != 0))
      dispatchOnEmpty(localArrayList);
  }

  public void addObserver(ConversationSetObserver paramConversationSetObserver)
  {
    synchronized (this.mLock)
    {
      this.mObservers.add(paramConversationSetObserver);
      return;
    }
  }

  public void clear()
  {
    while (true)
    {
      synchronized (this.mLock)
      {
        if (!this.mInternalMap.isEmpty())
        {
          i = 1;
          this.mInternalViewMap.clear();
          this.mInternalMap.clear();
          this.mConversationUriToIdMap.clear();
          if ((this.mInternalMap.isEmpty()) && (i != 0))
          {
            ArrayList localArrayList = Lists.newArrayList(this.mObservers);
            dispatchOnChange(localArrayList);
            dispatchOnEmpty(localArrayList);
          }
          return;
        }
      }
      int i = 0;
    }
  }

  public boolean contains(Conversation paramConversation)
  {
    synchronized (this.mLock)
    {
      boolean bool = containsKey(Long.valueOf(paramConversation.id));
      return bool;
    }
  }

  public boolean containsKey(Long paramLong)
  {
    synchronized (this.mLock)
    {
      boolean bool = this.mInternalMap.containsKey(paramLong);
      return bool;
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean isEmpty()
  {
    synchronized (this.mLock)
    {
      boolean bool = this.mInternalMap.isEmpty();
      return bool;
    }
  }

  public Set<Long> keySet()
  {
    synchronized (this.mLock)
    {
      Set localSet = this.mInternalMap.keySet();
      return localSet;
    }
  }

  public void putAll(ConversationSelectionSet paramConversationSelectionSet)
  {
    if (paramConversationSelectionSet == null);
    boolean bool;
    ArrayList localArrayList;
    do
    {
      return;
      bool = this.mInternalMap.isEmpty();
      this.mInternalMap.putAll(paramConversationSelectionSet.mInternalMap);
      Iterator localIterator = paramConversationSelectionSet.mInternalMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        Long localLong = (Long)localIterator.next();
        this.mInternalViewMap.put(localLong, null);
      }
      localArrayList = Lists.newArrayList(this.mObservers);
      dispatchOnChange(localArrayList);
    }
    while (!bool);
    dispatchOnBecomeUnempty(localArrayList);
  }

  public void removeObserver(ConversationSetObserver paramConversationSetObserver)
  {
    synchronized (this.mLock)
    {
      this.mObservers.remove(paramConversationSetObserver);
      return;
    }
  }

  public int size()
  {
    synchronized (this.mLock)
    {
      int i = this.mInternalMap.size();
      return i;
    }
  }

  public String toString()
  {
    synchronized (this.mLock)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = super.toString();
      arrayOfObject[1] = this.mInternalMap;
      String str = String.format("%s:%s", arrayOfObject);
      return str;
    }
  }

  public void toggle(ConversationItemView paramConversationItemView, Conversation paramConversation)
  {
    long l = paramConversation.id;
    if (containsKey(Long.valueOf(l)))
    {
      remove(Long.valueOf(l));
      return;
    }
    put(Long.valueOf(l), paramConversationItemView);
  }

  public void validateAgainstCursor(ConversationCursor paramConversationCursor)
  {
    synchronized (this.mLock)
    {
      if (isEmpty())
        return;
      if (paramConversationCursor == null)
      {
        clear();
        return;
      }
    }
    Set localSet1 = paramConversationCursor.getDeletedItems();
    HashSet localHashSet1 = Sets.newHashSet();
    Iterator localIterator = localSet1.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Long localLong = (Long)this.mConversationUriToIdMap.get(str);
      if (localLong != null)
        localHashSet1.add(localLong);
    }
    HashSet localHashSet2 = new HashSet(keySet());
    localHashSet2.removeAll(localHashSet1);
    Set localSet2 = paramConversationCursor.getConversationIds();
    if ((!localHashSet2.isEmpty()) && (localSet2 != null))
      localHashSet2.removeAll(localSet2);
    localHashSet1.addAll(localHashSet2);
    removeAll(localHashSet1);
  }

  public Collection<Conversation> values()
  {
    synchronized (this.mLock)
    {
      Collection localCollection = this.mInternalMap.values();
      return localCollection;
    }
  }

  public Collection<ConversationItemView> views()
  {
    return this.mInternalViewMap.values();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelableArray((Conversation[])values().toArray(new Conversation[size()]), paramInt);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ConversationSelectionSet
 * JD-Core Version:    0.6.2
 */