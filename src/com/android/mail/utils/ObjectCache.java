package com.android.mail.utils;

import com.google.common.collect.Lists;
import java.util.Deque;

public class ObjectCache<T>
{
  private final Callback<T> mCallback;
  private final Deque<T> mDataStore = Lists.newLinkedList();
  private final int mMaxSize;

  public ObjectCache(Callback<T> paramCallback, int paramInt)
  {
    this.mCallback = paramCallback;
    this.mMaxSize = paramInt;
  }

  public T get()
  {
    synchronized (this.mDataStore)
    {
      Object localObject2 = this.mDataStore.poll();
      if (localObject2 == null)
        localObject2 = this.mCallback.newInstance();
      return localObject2;
    }
  }

  public void release(T paramT)
  {
    synchronized (this.mDataStore)
    {
      if (this.mDataStore.size() < this.mMaxSize)
      {
        this.mCallback.onObjectReleased(paramT);
        this.mDataStore.add(paramT);
      }
      return;
    }
  }

  public static abstract interface Callback<T>
  {
    public abstract T newInstance();

    public abstract void onObjectReleased(T paramT);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.ObjectCache
 * JD-Core Version:    0.6.2
 */