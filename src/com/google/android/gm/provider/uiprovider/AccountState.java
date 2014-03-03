package com.google.android.gm.provider.uiprovider;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.util.LruCache;
import com.google.android.gm.provider.MailEngine;
import com.google.common.collect.Maps;
import java.lang.ref.WeakReference;
import java.util.Map;

public class AccountState
{
  private final String mAccount;
  private final LruCache<String, WeakReference<UIConversationCursor>> mConversationCursorMap = new LruCache(3);
  private final Map<Long, ConversationState> mConversationStateMap = Maps.newHashMap();
  private final MailEngine mEngine;
  private final Handler mLoaderHandler;
  private final Map<String, Integer> mSearchInfo = Maps.newHashMap();

  public AccountState(String paramString, Handler paramHandler, MailEngine paramMailEngine)
  {
    this.mAccount = paramString;
    this.mLoaderHandler = paramHandler;
    this.mEngine = paramMailEngine;
  }

  private static String generateConversationCursorKey(Uri paramUri)
  {
    return paramUri.buildUpon().clearQuery().build().toString();
  }

  private void updateSearchInfoMapForQuery(String paramString)
  {
    if (!this.mSearchInfo.containsKey(paramString))
    {
      this.mSearchInfo.clear();
      this.mSearchInfo.put(paramString, Integer.valueOf(0));
    }
  }

  public void cacheConversationCursor(Uri paramUri, UIConversationCursor paramUIConversationCursor)
  {
    String str = generateConversationCursorKey(paramUri);
    this.mConversationCursorMap.put(str, new WeakReference(paramUIConversationCursor));
  }

  public UIConversationCursor getConversationCursor(Uri paramUri)
  {
    String str = generateConversationCursorKey(paramUri);
    WeakReference localWeakReference = (WeakReference)this.mConversationCursorMap.get(str);
    if (localWeakReference != null)
      return (UIConversationCursor)localWeakReference.get();
    return null;
  }

  public ConversationState getConversationState(long paramLong)
  {
    synchronized (this.mConversationStateMap)
    {
      ConversationState localConversationState = (ConversationState)this.mConversationStateMap.get(Long.valueOf(paramLong));
      return localConversationState;
    }
  }

  public int getNumSearchResults(String paramString)
  {
    updateSearchInfoMapForQuery(paramString);
    Integer localInteger = (Integer)this.mSearchInfo.get(paramString);
    if (localInteger != null)
      return localInteger.intValue();
    return 0;
  }

  public ConversationState getOrCreateConversationState(Context paramContext, long paramLong)
  {
    synchronized (this.mConversationStateMap)
    {
      if (this.mConversationStateMap.containsKey(Long.valueOf(paramLong)))
      {
        localConversationState = (ConversationState)this.mConversationStateMap.get(Long.valueOf(paramLong));
        return localConversationState;
      }
      ConversationState localConversationState = new ConversationState(paramContext, this.mAccount, paramLong, this.mLoaderHandler, this.mEngine);
      this.mConversationStateMap.put(Long.valueOf(paramLong), localConversationState);
    }
  }

  public void notifyAttachmentChange(long paramLong)
  {
    synchronized (this.mConversationStateMap)
    {
      ConversationState localConversationState = (ConversationState)this.mConversationStateMap.get(Long.valueOf(paramLong));
      if (localConversationState != null)
        localConversationState.notifyAttachmentChanges();
      return;
    }
  }

  public void setNumSearchResults(String paramString, int paramInt)
  {
    updateSearchInfoMapForQuery(paramString);
    this.mSearchInfo.put(paramString, Integer.valueOf(paramInt));
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.uiprovider.AccountState
 * JD-Core Version:    0.6.2
 */