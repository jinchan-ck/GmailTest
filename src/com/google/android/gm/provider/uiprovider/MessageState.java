package com.google.android.gm.provider.uiprovider;

import android.content.Context;
import com.google.android.gm.provider.AttachmentStatusLoader.Result;
import com.google.android.gm.provider.Gmail.Attachment;
import com.google.android.gm.provider.Gmail.AttachmentRendition;
import com.google.android.gm.provider.LogUtils;
import com.google.android.gm.provider.MailSync.Message;
import com.google.android.gm.provider.UiProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageState
{
  private final String mAccount;
  private final Context mContext;
  private final long mConversationId;
  private final long mLocalMessageId;
  private final List<UIAttachment> mMessageAttachments = Lists.newArrayList();
  boolean mMessageAttachmentsInitialized = false;
  private final long mMessageId;

  public MessageState(Context paramContext, String paramString, long paramLong1, long paramLong2, long paramLong3)
  {
    this.mContext = paramContext;
    this.mAccount = paramString;
    this.mConversationId = paramLong1;
    this.mMessageId = paramLong2;
    this.mLocalMessageId = paramLong3;
  }

  private int getMessageAttachmentIndex(String paramString)
  {
    List localList = this.mMessageAttachments;
    for (int i = 0; ; i++)
      try
      {
        if (i < this.mMessageAttachments.size())
        {
          if (((UIAttachment)this.mMessageAttachments.get(i)).getPartId().equalsIgnoreCase(paramString))
            return i;
        }
        else
          return -1;
      }
      finally
      {
      }
  }

  public UIAttachment getMessageAttachment(String paramString)
  {
    synchronized (this.mMessageAttachments)
    {
      Iterator localIterator = this.mMessageAttachments.iterator();
      while (localIterator.hasNext())
      {
        UIAttachment localUIAttachment = (UIAttachment)localIterator.next();
        if (localUIAttachment.getPartId().equalsIgnoreCase(paramString))
          return localUIAttachment;
      }
      return null;
    }
  }

  public List<UIAttachment> getMessageAttachments()
  {
    synchronized (this.mMessageAttachments)
    {
      ImmutableList localImmutableList = ImmutableList.copyOf(this.mMessageAttachments);
      return localImmutableList;
    }
  }

  boolean gmailAttachmentDataLoaded()
  {
    if (!this.mMessageAttachmentsInitialized)
      return false;
    synchronized (this.mMessageAttachments)
    {
      Iterator localIterator = this.mMessageAttachments.iterator();
      while (localIterator.hasNext())
        if (((UIAttachment)localIterator.next()).getOriginal() == null)
          return false;
    }
    return true;
  }

  void initializeAttachmentsFromMessage(MailSync.Message paramMessage)
  {
    if (this.mMessageAttachments.size() > 0)
    {
      LogUtils.e("Gmail", "Attempting to initialize attachment when attachmentshave already been configured", new Object[0]);
      return;
    }
    synchronized (this.mMessageAttachments)
    {
      Iterator localIterator = paramMessage.attachments.iterator();
      if (localIterator.hasNext())
      {
        Gmail.Attachment localAttachment = (Gmail.Attachment)localIterator.next();
        this.mMessageAttachments.add(localAttachment);
      }
    }
    this.mMessageAttachmentsInitialized = true;
  }

  public void notifyAttachmentChange()
  {
    HashSet localHashSet = Sets.newHashSet();
    Iterator localIterator = getMessageAttachments().iterator();
    while (localIterator.hasNext())
      localHashSet.add(((UIAttachment)localIterator.next()).getPartId());
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Long.valueOf(this.mConversationId);
    arrayOfObject[1] = Long.valueOf(this.mMessageId);
    LogUtils.v("Gmail", "Notifying about attachment change conversation message %d/%d", arrayOfObject);
    UiProvider.notifyMessageAttachmentsChanged(this.mContext, this.mAccount, this.mConversationId, this.mMessageId, this.mLocalMessageId, localHashSet);
  }

  void populateGmailAttachmentData(MailSync.Message paramMessage)
  {
    if (!this.mMessageAttachmentsInitialized)
      initializeAttachmentsFromMessage(paramMessage);
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator1 = paramMessage.attachments.iterator();
    while (localIterator1.hasNext())
    {
      Gmail.Attachment localAttachment2 = (Gmail.Attachment)localIterator1.next();
      localHashMap.put(localAttachment2.getPartId(), localAttachment2);
    }
    synchronized (this.mMessageAttachments)
    {
      Iterator localIterator2 = this.mMessageAttachments.iterator();
      while (localIterator2.hasNext())
      {
        UIAttachment localUIAttachment = (UIAttachment)localIterator2.next();
        if (localUIAttachment.getOriginal() == null)
        {
          Gmail.Attachment localAttachment1 = (Gmail.Attachment)localHashMap.get(localUIAttachment.getPartId());
          if (localAttachment1 != null)
            ((AttachmentStatusLoader.Result)localUIAttachment).mOriginalAttachment = localAttachment1;
        }
      }
    }
  }

  public void updateAttachment(AttachmentStatusLoader.Result paramResult)
  {
    while (true)
    {
      synchronized (this.mMessageAttachments)
      {
        int i = getMessageAttachmentIndex(paramResult.getPartId());
        if (i != -1)
        {
          UIAttachment localUIAttachment = (UIAttachment)this.mMessageAttachments.get(i);
          Gmail.Attachment localAttachment = localUIAttachment.getOriginal();
          if (paramResult.rendition == Gmail.AttachmentRendition.BEST)
          {
            paramResult.mOriginalAttachment = localAttachment;
            if (((!paramResult.isDownloading()) && (!paramResult.isStatusPending()) && (!paramResult.isStatusPaused()) && (!paramResult.isStatusSuccess()) && (!paramResult.isStatusError()) && (paramResult.isStatusValid())) || ((paramResult.isStatusPending()) && (localUIAttachment.isDownloading())))
              paramResult.updateState(2, localUIAttachment.getDestination(), null);
            this.mMessageAttachments.remove(i);
            this.mMessageAttachments.add(i, paramResult);
            return;
          }
          LogUtils.d("Gmail", "Dropping attachment update, as this is an thumbnail attachment: %s", new Object[] { paramResult });
        }
      }
      if (LogUtils.isLoggable("Gmail", 3))
      {
        HashSet localHashSet = Sets.newHashSet();
        Iterator localIterator = this.mMessageAttachments.iterator();
        while (localIterator.hasNext())
          localHashSet.add(((UIAttachment)localIterator.next()).getPartId());
        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = Long.valueOf(this.mMessageId);
        arrayOfObject[1] = paramResult.getPartId();
        arrayOfObject[2] = localHashSet;
        LogUtils.d("Gmail", "Got unexpected attachment. messageId: %d partId: %s set: %s", arrayOfObject);
      }
      this.mMessageAttachments.add(paramResult);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.uiprovider.MessageState
 * JD-Core Version:    0.6.2
 */