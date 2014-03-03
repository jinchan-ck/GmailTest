package com.android.mail.ui;

import com.android.mail.browse.ConversationCursor;
import com.android.mail.providers.Conversation;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.util.Collection;

public class ConversationPositionTracker
{
  protected static final String LOG_TAG = LogTag.getLogTag();
  private final Callbacks mCallbacks;
  private Conversation mConversation;
  private boolean mCursorDirty = false;

  public ConversationPositionTracker(Callbacks paramCallbacks)
  {
    this.mCallbacks = paramCallbacks;
  }

  private int calculatePosition()
  {
    int i = -1;
    ConversationCursor localConversationCursor = this.mCallbacks.getConversationListCursor();
    if (!this.mCursorDirty)
      i = this.mConversation.position;
    while (true)
    {
      return i;
      if ((localConversationCursor == null) || (this.mConversation == null))
        continue;
      this.mCursorDirty = false;
      int j = 0;
      boolean bool;
      if (localConversationCursor == null)
      {
        if ((!isDataLoaded(localConversationCursor)) || (j == 0))
          continue;
        bool = Utils.disableConversationCursorNetworkAccess(localConversationCursor);
        i = 0;
      }
      try
      {
        while (true)
        {
          if (!localConversationCursor.moveToPosition(i))
            break label135;
          if (Utils.getConversationId(localConversationCursor) == this.mConversation.id)
          {
            this.mConversation.position = i;
            localConversationCursor.moveToPosition(i + 1);
            return i;
            j = localConversationCursor.getCount();
            break;
          }
          i++;
        }
        label135: if ((this.mConversation.position >= j) || (i >= j))
          i = -1 + localConversationCursor.getCount();
        if (isDataLoaded(localConversationCursor))
        {
          String str = LOG_TAG;
          Object[] arrayOfObject = new Object[2];
          arrayOfObject[0] = this.mConversation.toString();
          arrayOfObject[1] = Integer.valueOf(i);
          LogUtils.d(str, "ConversationPositionTracker: Could not find conversation %s in the cursor. Moving to position %d ", arrayOfObject);
          localConversationCursor.moveToPosition(i);
          this.mConversation = new Conversation(localConversationCursor);
        }
        return i;
      }
      finally
      {
        if (bool)
          Utils.enableConversationCursorNetworkAccess(localConversationCursor);
      }
    }
  }

  private Conversation conversationAtPosition(int paramInt)
  {
    ConversationCursor localConversationCursor = this.mCallbacks.getConversationListCursor();
    localConversationCursor.moveToPosition(paramInt);
    Conversation localConversation = new Conversation(localConversationCursor);
    localConversation.position = paramInt;
    return localConversation;
  }

  private int getCount()
  {
    ConversationCursor localConversationCursor = this.mCallbacks.getConversationListCursor();
    if (isDataLoaded(localConversationCursor))
      return localConversationCursor.getCount();
    return 0;
  }

  private Conversation getNewer(Collection<Conversation> paramCollection)
  {
    int i = calculatePosition();
    Conversation localConversation;
    if ((!isDataLoaded()) || (i < 0))
    {
      localConversation = null;
      return localConversation;
    }
    for (int j = i - 1; ; j--)
    {
      if (j < 0)
        break label51;
      localConversation = conversationAtPosition(j);
      if (!Conversation.contains(paramCollection, localConversation))
        break;
    }
    label51: return null;
  }

  private Conversation getOlder(Collection<Conversation> paramCollection)
  {
    int i = calculatePosition();
    Conversation localConversation;
    if ((!isDataLoaded()) || (i < 0))
    {
      localConversation = null;
      return localConversation;
    }
    for (int j = i + 1; ; j++)
    {
      if (j >= getCount())
        break label55;
      localConversation = conversationAtPosition(j);
      if (!Conversation.contains(paramCollection, localConversation))
        break;
    }
    label55: return null;
  }

  private boolean isDataLoaded()
  {
    return isDataLoaded(this.mCallbacks.getConversationListCursor());
  }

  private static boolean isDataLoaded(ConversationCursor paramConversationCursor)
  {
    return (paramConversationCursor != null) && (!paramConversationCursor.isClosed());
  }

  public Conversation getNextConversation(int paramInt, Collection<Conversation> paramCollection)
  {
    boolean bool1;
    boolean bool2;
    label15: Conversation localConversation;
    if (paramInt == 2)
    {
      bool1 = true;
      if (paramInt != 1)
        break label78;
      bool2 = true;
      if (!bool1)
        break label84;
      localConversation = getNewer(paramCollection);
    }
    while (true)
    {
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = Boolean.valueOf(bool1);
      arrayOfObject[1] = Boolean.valueOf(bool2);
      arrayOfObject[2] = localConversation;
      LogUtils.d(str, "ConversationPositionTracker.getNextConversation: getNewer = %b, getOlder = %b, Next conversation is %s", arrayOfObject);
      return localConversation;
      bool1 = false;
      break;
      label78: bool2 = false;
      break label15;
      label84: if (bool2)
        localConversation = getOlder(paramCollection);
      else
        localConversation = null;
    }
  }

  public void initialize(Conversation paramConversation)
  {
    this.mConversation = paramConversation;
    this.mCursorDirty = true;
    calculatePosition();
  }

  public void onCursorUpdated()
  {
    this.mCursorDirty = true;
  }

  public static abstract interface Callbacks
  {
    public abstract ConversationCursor getConversationListCursor();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ConversationPositionTracker
 * JD-Core Version:    0.6.2
 */