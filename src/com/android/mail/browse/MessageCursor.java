package com.android.mail.browse;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import com.android.mail.providers.Attachment;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Message;
import com.android.mail.ui.ConversationUpdater;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MessageCursor extends CursorWrapper
{
  private final Map<Long, ConversationMessage> mCache = Maps.newHashMap();
  private ConversationController mController;
  private Integer mStatus;

  public MessageCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public String getDebugDump()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = this.mController.getConversation().subject;
    arrayOfObject1[1] = Integer.valueOf(getStatus());
    localStringBuilder.append(String.format("conv subj='%s' status=%d messages:\n", arrayOfObject1));
    int i = -1;
    while (true)
    {
      i++;
      if (!moveToPosition(i))
        break;
      ConversationMessage localConversationMessage = getMessage();
      ArrayList localArrayList = Lists.newArrayList();
      Iterator localIterator = localConversationMessage.getAttachments().iterator();
      while (localIterator.hasNext())
        localArrayList.add(((Attachment)localIterator.next()).uri);
      Object[] arrayOfObject2 = new Object[11];
      arrayOfObject2[0] = Integer.valueOf(i);
      arrayOfObject2[1] = Integer.valueOf(localConversationMessage.getStateHashCode());
      arrayOfObject2[2] = localConversationMessage.uri;
      arrayOfObject2[3] = Long.valueOf(localConversationMessage.id);
      arrayOfObject2[4] = localConversationMessage.serverId;
      arrayOfObject2[5] = localConversationMessage.getFrom();
      arrayOfObject2[6] = Integer.valueOf(localConversationMessage.draftType);
      arrayOfObject2[7] = Boolean.valueOf(localConversationMessage.isSending);
      arrayOfObject2[8] = Boolean.valueOf(localConversationMessage.read);
      arrayOfObject2[9] = Boolean.valueOf(localConversationMessage.starred);
      arrayOfObject2[10] = localArrayList;
      localStringBuilder.append(String.format("[Message #%d hash=%s uri=%s id=%s serverId=%s from='%s' draftType=%d isSending=%s read=%s starred=%s attUris=%s]\n", arrayOfObject2));
    }
    return localStringBuilder.toString();
  }

  public ConversationMessage getMessage()
  {
    long l = getWrappedCursor().getLong(0);
    ConversationMessage localConversationMessage = (ConversationMessage)this.mCache.get(Long.valueOf(l));
    if (localConversationMessage == null)
    {
      localConversationMessage = new ConversationMessage(this, null);
      this.mCache.put(Long.valueOf(l), localConversationMessage);
    }
    localConversationMessage.setController(this.mController);
    return localConversationMessage;
  }

  public int getStateHashCode()
  {
    return getStateHashCode(0);
  }

  public int getStateHashCode(int paramInt)
  {
    int i = 17;
    int j = -1;
    int k = getCount() - paramInt;
    while (true)
    {
      j++;
      if ((!moveToPosition(j)) || (j >= k))
        break;
      i = i * 31 + getMessage().getStateHashCode();
    }
    return i;
  }

  public int getStatus()
  {
    if (this.mStatus != null)
      return this.mStatus.intValue();
    this.mStatus = Integer.valueOf(2);
    Bundle localBundle = getExtras();
    if ((localBundle != null) && (localBundle.containsKey("cursor_status")))
      this.mStatus = Integer.valueOf(localBundle.getInt("cursor_status"));
    return this.mStatus.intValue();
  }

  public boolean isConversationRead()
  {
    int i = -1;
    do
    {
      i++;
      if (!moveToPosition(i))
        break;
    }
    while (getMessage().read);
    return false;
    return true;
  }

  public boolean isConversationStarred()
  {
    int i = -1;
    do
    {
      i++;
      if (!moveToPosition(i))
        break;
    }
    while (!getMessage().starred);
    return true;
    return false;
  }

  public boolean isLoaded()
  {
    return (getStatus() >= 2) || (getCount() > 0);
  }

  public void markMessagesRead()
  {
    int i = -1;
    while (true)
    {
      i++;
      if (!moveToPosition(i))
        break;
      getMessage().read = true;
    }
  }

  public void setController(ConversationController paramConversationController)
  {
    this.mController = paramConversationController;
  }

  public static abstract interface ConversationController
  {
    public abstract Conversation getConversation();

    public abstract ConversationUpdater getListController();

    public abstract MessageCursor getMessageCursor();
  }

  public static final class ConversationMessage extends Message
  {
    private transient MessageCursor.ConversationController mController;

    private ConversationMessage(MessageCursor paramMessageCursor)
    {
      super();
    }

    private int getAttachmentsStateHashCode()
    {
      int i = 0;
      Iterator localIterator = getAttachments().iterator();
      if (localIterator.hasNext())
      {
        Uri localUri = ((Attachment)localIterator.next()).getIdentifierUri();
        if (localUri != null);
        for (int j = localUri.hashCode(); ; j = 0)
        {
          i += j;
          break;
        }
      }
      return i;
    }

    public Conversation getConversation()
    {
      return this.mController.getConversation();
    }

    public int getStateHashCode()
    {
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = this.uri;
      arrayOfObject[1] = Boolean.valueOf(this.read);
      arrayOfObject[2] = Boolean.valueOf(this.starred);
      arrayOfObject[3] = Integer.valueOf(getAttachmentsStateHashCode());
      return Objects.hashCode(arrayOfObject);
    }

    public boolean isConversationStarred()
    {
      MessageCursor localMessageCursor = this.mController.getMessageCursor();
      return (localMessageCursor != null) && (localMessageCursor.isConversationStarred());
    }

    public void setController(MessageCursor.ConversationController paramConversationController)
    {
      this.mController = paramConversationController;
    }

    public void star(boolean paramBoolean)
    {
      ConversationUpdater localConversationUpdater = this.mController.getListController();
      if (localConversationUpdater != null)
        localConversationUpdater.starMessage(this, paramBoolean);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.MessageCursor
 * JD-Core Version:    0.6.2
 */