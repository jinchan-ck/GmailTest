package com.android.mail.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.ConversationInfo;
import com.android.mail.providers.Message;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class ConversationViewState
  implements Parcelable
{
  public static final Parcelable.ClassLoaderCreator<ConversationViewState> CREATOR = new Parcelable.ClassLoaderCreator()
  {
    public ConversationViewState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ConversationViewState(paramAnonymousParcel, null, null);
    }

    public ConversationViewState createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
    {
      return new ConversationViewState(paramAnonymousParcel, paramAnonymousClassLoader, null);
    }

    public ConversationViewState[] newArray(int paramAnonymousInt)
    {
      return new ConversationViewState[paramAnonymousInt];
    }
  };
  private String mConversationInfo;
  private final Map<Uri, MessageViewState> mMessageViewStates = Maps.newHashMap();

  public ConversationViewState()
  {
  }

  private ConversationViewState(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    Bundle localBundle = paramParcel.readBundle(paramClassLoader);
    Iterator localIterator = localBundle.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      MessageViewState localMessageViewState = (MessageViewState)localBundle.getParcelable(str);
      this.mMessageViewStates.put(Uri.parse(str), localMessageViewState);
    }
    this.mConversationInfo = paramParcel.readString();
  }

  public ConversationViewState(ConversationViewState paramConversationViewState)
  {
    this.mConversationInfo = paramConversationViewState.mConversationInfo;
  }

  public boolean contains(Message paramMessage)
  {
    return this.mMessageViewStates.containsKey(paramMessage.uri);
  }

  public int describeContents()
  {
    return 0;
  }

  public String getConversationInfo()
  {
    return this.mConversationInfo;
  }

  public Integer getExpansionState(Message paramMessage)
  {
    MessageViewState localMessageViewState = (MessageViewState)this.mMessageViewStates.get(paramMessage.uri);
    if (localMessageViewState == null)
      return null;
    return localMessageViewState.expansionState;
  }

  public boolean getShouldShowImages(Message paramMessage)
  {
    MessageViewState localMessageViewState = (MessageViewState)this.mMessageViewStates.get(paramMessage.uri);
    return (localMessageViewState != null) && (localMessageViewState.showImages);
  }

  public Set<Uri> getUnreadMessageUris()
  {
    HashSet localHashSet = Sets.newHashSet();
    Iterator localIterator = this.mMessageViewStates.keySet().iterator();
    while (localIterator.hasNext())
    {
      Uri localUri = (Uri)localIterator.next();
      MessageViewState localMessageViewState = (MessageViewState)this.mMessageViewStates.get(localUri);
      if ((localMessageViewState != null) && (!localMessageViewState.read))
        localHashSet.add(localUri);
    }
    return localHashSet;
  }

  public boolean isUnread(Message paramMessage)
  {
    MessageViewState localMessageViewState = (MessageViewState)this.mMessageViewStates.get(paramMessage.uri);
    return (localMessageViewState != null) && (!localMessageViewState.read);
  }

  public void setExpansionState(Message paramMessage, int paramInt)
  {
    MessageViewState localMessageViewState = (MessageViewState)this.mMessageViewStates.get(paramMessage.uri);
    if (localMessageViewState == null)
      localMessageViewState = new MessageViewState();
    localMessageViewState.expansionState = Integer.valueOf(paramInt);
    this.mMessageViewStates.put(paramMessage.uri, localMessageViewState);
  }

  public void setInfoForConversation(Conversation paramConversation)
  {
    this.mConversationInfo = ConversationInfo.toString(paramConversation.conversationInfo);
  }

  public void setReadState(Message paramMessage, boolean paramBoolean)
  {
    MessageViewState localMessageViewState = (MessageViewState)this.mMessageViewStates.get(paramMessage.uri);
    if (localMessageViewState == null)
      localMessageViewState = new MessageViewState();
    localMessageViewState.read = paramBoolean;
    this.mMessageViewStates.put(paramMessage.uri, localMessageViewState);
  }

  public void setShouldShowImages(Message paramMessage, boolean paramBoolean)
  {
    MessageViewState localMessageViewState = (MessageViewState)this.mMessageViewStates.get(paramMessage.uri);
    if (localMessageViewState == null)
      localMessageViewState = new MessageViewState();
    localMessageViewState.showImages = paramBoolean;
    this.mMessageViewStates.put(paramMessage.uri, localMessageViewState);
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Bundle localBundle = new Bundle();
    Iterator localIterator = this.mMessageViewStates.keySet().iterator();
    while (localIterator.hasNext())
    {
      Uri localUri = (Uri)localIterator.next();
      MessageViewState localMessageViewState = (MessageViewState)this.mMessageViewStates.get(localUri);
      localBundle.putParcelable(localUri.toString(), localMessageViewState);
    }
    paramParcel.writeBundle(localBundle);
    paramParcel.writeString(this.mConversationInfo);
  }

  public static final class ExpansionState
  {
    public static int COLLAPSED = 2;
    public static int EXPANDED = 1;
    public static int SUPER_COLLAPSED = 3;

    public static boolean isExpanded(int paramInt)
    {
      return paramInt == EXPANDED;
    }

    public static boolean isSuperCollapsed(int paramInt)
    {
      return paramInt == SUPER_COLLAPSED;
    }
  }

  static class MessageViewState
    implements Parcelable
  {
    public static final Parcelable.Creator<MessageViewState> CREATOR = new Parcelable.Creator()
    {
      public ConversationViewState.MessageViewState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ConversationViewState.MessageViewState(paramAnonymousParcel, null);
      }

      public ConversationViewState.MessageViewState[] newArray(int paramAnonymousInt)
      {
        return new ConversationViewState.MessageViewState[paramAnonymousInt];
      }
    };
    public Integer expansionState;
    public boolean read;
    public boolean showImages;

    public MessageViewState()
    {
    }

    private MessageViewState(Parcel paramParcel)
    {
      boolean bool2;
      int i;
      Integer localInteger;
      if (paramParcel.readInt() != 0)
      {
        bool2 = bool1;
        this.read = bool2;
        i = paramParcel.readInt();
        if (i != -1)
          break label59;
        localInteger = null;
        label35: this.expansionState = localInteger;
        if (paramParcel.readInt() == 0)
          break label69;
      }
      while (true)
      {
        this.showImages = bool1;
        return;
        bool2 = false;
        break;
        label59: localInteger = Integer.valueOf(i);
        break label35;
        label69: bool1 = false;
      }
    }

    public int describeContents()
    {
      return 0;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      int i = 1;
      int j;
      int k;
      if (this.read)
      {
        j = i;
        paramParcel.writeInt(j);
        if (this.expansionState != null)
          break label53;
        k = -1;
        label28: paramParcel.writeInt(k);
        if (!this.showImages)
          break label65;
      }
      while (true)
      {
        paramParcel.writeInt(i);
        return;
        j = 0;
        break;
        label53: k = this.expansionState.intValue();
        break label28;
        label65: i = 0;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ConversationViewState
 * JD-Core Version:    0.6.2
 */