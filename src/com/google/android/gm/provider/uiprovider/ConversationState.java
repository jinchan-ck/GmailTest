package com.google.android.gm.provider.uiprovider;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.database.Cursor;
import android.os.Handler;
import com.google.android.gm.provider.AttachmentStatusLoader;
import com.google.android.gm.provider.AttachmentStatusLoader.Result;
import com.google.android.gm.provider.LogUtils;
import com.google.android.gm.provider.MailEngine;
import com.google.android.gm.provider.MailSync.Message;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ConversationState
  implements Loader.OnLoadCompleteListener<List<AttachmentStatusLoader.Result>>
{
  private final String mAccount;
  private AttachmentStatusLoader mAttachmentStatusLoader;
  private final Context mContext;
  private final long mConversationId;
  private final DownloadManager mDownloadManager;
  private final MailEngine mEngine;
  private final Handler mLoaderHandler;
  private final Object mLoaderLock = new Object();
  private final WeakHashMap<Cursor, Object> mMessageAttachmentCursors = new WeakHashMap();
  private final Map<Long, MessageState> mMessageStateMap = Maps.newHashMap();

  public ConversationState(Context paramContext, String paramString, long paramLong, Handler paramHandler, MailEngine paramMailEngine)
  {
    this.mContext = paramContext;
    this.mDownloadManager = ((DownloadManager)this.mContext.getSystemService("download"));
    this.mAccount = paramString;
    this.mConversationId = paramLong;
    this.mLoaderHandler = paramHandler;
    this.mEngine = paramMailEngine;
  }

  private MessageState createMessageStateFromMessage(MailSync.Message paramMessage)
  {
    long l = paramMessage.messageId;
    synchronized (this.mMessageStateMap)
    {
      if (!this.mMessageStateMap.containsKey(Long.valueOf(l)))
      {
        localMessageState = new MessageState(this.mContext, this.mAccount, this.mConversationId, l, paramMessage.localMessageId);
        this.mMessageStateMap.put(Long.valueOf(l), localMessageState);
        localMessageState.initializeAttachmentsFromMessage(paramMessage);
        return localMessageState;
      }
      MessageState localMessageState = (MessageState)this.mMessageStateMap.get(Long.valueOf(l));
    }
  }

  public void addAttachmentCursor(Cursor paramCursor)
  {
    synchronized (this.mMessageAttachmentCursors)
    {
      this.mMessageAttachmentCursors.put(paramCursor, null);
      return;
    }
  }

  public void ensureAttachmentStatusLoaderStarted()
  {
    this.mLoaderHandler.post(new Runnable()
    {
      public void run()
      {
        synchronized (ConversationState.this.mLoaderLock)
        {
          if (ConversationState.this.mAttachmentStatusLoader == null)
          {
            ConversationState.access$102(ConversationState.this, new AttachmentStatusLoader(ConversationState.this.mContext, ConversationState.this.mAccount, ConversationState.this.mConversationId, ConversationState.this.mDownloadManager));
            ConversationState.this.mAttachmentStatusLoader.registerListener(0, ConversationState.this);
          }
          if (!ConversationState.this.mAttachmentStatusLoader.isStarted())
          {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Long.valueOf(ConversationState.this.mConversationId);
            LogUtils.d("Gmail", "starting attachment loader for conversation %d", arrayOfObject);
            ConversationState.this.mAttachmentStatusLoader.startLoading();
          }
          return;
        }
      }
    });
  }

  public MessageState getMessageState(long paramLong)
  {
    synchronized (this.mMessageStateMap)
    {
      MessageState localMessageState = (MessageState)this.mMessageStateMap.get(Long.valueOf(paramLong));
      return localMessageState;
    }
  }

  public MessageState getOrCreateMessageState(long paramLong)
  {
    MessageState localMessageState = getMessageState(paramLong);
    if ((localMessageState == null) || (!localMessageState.gmailAttachmentDataLoaded()))
    {
      MailSync.Message localMessage = this.mEngine.getMessage(paramLong, false);
      if (localMessage == null)
      {
        LogUtils.e("Gmail", "Message not found", new Object[0]);
        return null;
      }
      if (localMessageState == null)
        localMessageState = createMessageStateFromMessage(localMessage);
    }
    return localMessageState;
  }

  public MessageState getOrCreateMessageState(UIAttachment.UriParser paramUriParser)
  {
    MessageState localMessageState = getMessageState(paramUriParser.mMessageId);
    MailSync.Message localMessage = this.mEngine.getLocalMessage(paramUriParser.mLocalMessageId, false);
    if (localMessage == null)
    {
      LogUtils.e("Gmail", "Message not found", new Object[0]);
      return null;
    }
    if (localMessageState == null)
    {
      localMessageState = createMessageStateFromMessage(localMessage);
      ensureAttachmentStatusLoaderStarted();
    }
    while (true)
    {
      return localMessageState;
      if (!localMessageState.gmailAttachmentDataLoaded())
        localMessageState.populateGmailAttachmentData(localMessage);
    }
  }

  public void handleCursorClose(Cursor paramCursor)
  {
    synchronized (this.mMessageAttachmentCursors)
    {
      this.mMessageAttachmentCursors.remove(paramCursor);
      int i = this.mMessageAttachmentCursors.size();
      int j = 0;
      if (i == 0)
        j = 1;
      if (j == 0);
    }
    synchronized (this.mLoaderLock)
    {
      if (this.mAttachmentStatusLoader != null)
      {
        LogUtils.d("Gmail", "attachment cursor closed, and stopping loader", new Object[0]);
        this.mAttachmentStatusLoader.abandon();
        this.mAttachmentStatusLoader.stopLoading();
        this.mAttachmentStatusLoader.reset();
        this.mAttachmentStatusLoader = null;
      }
      return;
      localObject1 = finally;
      throw localObject1;
    }
  }

  void notifyAttachmentChanges()
  {
    synchronized (this.mMessageStateMap)
    {
      Iterator localIterator = this.mMessageStateMap.values().iterator();
      if (localIterator.hasNext())
        ((MessageState)localIterator.next()).notifyAttachmentChange();
    }
  }

  public void onLoadComplete(Loader<List<AttachmentStatusLoader.Result>> paramLoader, List<AttachmentStatusLoader.Result> paramList)
  {
    while (true)
    {
      AttachmentStatusLoader.Result localResult;
      synchronized (this.mMessageStateMap)
      {
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Long.valueOf(this.mConversationId);
        LogUtils.d("Gmail", "onLoadComplete called on loader for conversation %d", arrayOfObject1);
        Iterator localIterator = paramList.iterator();
        if (!localIterator.hasNext())
          break;
        localResult = (AttachmentStatusLoader.Result)localIterator.next();
        MessageState localMessageState = getOrCreateMessageState(localResult.messageId);
        if (localMessageState != null)
          localMessageState.updateAttachment(localResult);
      }
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Long.valueOf(localResult.messageId);
      LogUtils.e("Gmail", "failed to find Message state for message id: %d", arrayOfObject2);
    }
    notifyAttachmentChanges();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.uiprovider.ConversationState
 * JD-Core Version:    0.6.2
 */