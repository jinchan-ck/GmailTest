package com.android.mail.browse;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.CharacterStyle;
import android.util.LruCache;
import android.util.Pair;
import android.widget.TextView;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.ConversationInfo;
import com.android.mail.providers.Folder;
import com.android.mail.providers.MessageInfo;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;

public class ConversationItemViewModel
{
  private static Folder sCachedModelsFolder;
  static LruCache<Pair<String, Long>, ConversationItemViewModel> sConversationHeaderMap = new LruCache(100);
  public boolean checkboxVisible;
  public Conversation conversation;
  Bitmap dateBackground;
  String dateText;
  boolean faded = false;
  public ConversationItemView.ConversationItemFolderDisplayer folderDisplayer;
  public boolean hasBeenForwarded;
  public boolean hasBeenRepliedTo;
  public boolean isInvite;
  private String mContentDescription;
  private int mDataHashCode;
  private int mLayoutHashCode;
  public SpannableStringBuilder messageInfoString;
  Bitmap paperclip;
  Bitmap personalLevelBitmap;
  final ArrayList<SenderFragment> senderFragments = Lists.newArrayList();
  StaticLayout sendersDisplayLayout;
  SpannableStringBuilder sendersDisplayText;
  String sendersText;
  public TextView sendersTextView;
  public int standardScaledDimen;
  public int styledMessageInfoStringOffset;
  public SpannableString[] styledSenders;
  public SpannableStringBuilder styledSendersString;
  SpannableStringBuilder subjectText;
  boolean unread;
  public int viewWidth;

  static ConversationItemViewModel forConversation(String paramString, Conversation paramConversation)
  {
    boolean bool1 = true;
    ConversationItemViewModel localConversationItemViewModel = forConversationId(paramString, paramConversation.id);
    boolean bool2;
    boolean bool3;
    label61: boolean bool4;
    if (paramConversation != null)
    {
      localConversationItemViewModel.faded = false;
      localConversationItemViewModel.checkboxVisible = bool1;
      localConversationItemViewModel.conversation = paramConversation;
      if (paramConversation.read)
        break label105;
      bool2 = bool1;
      localConversationItemViewModel.unread = bool2;
      if ((0x8 & paramConversation.convFlags) != 8)
        break label111;
      bool3 = bool1;
      localConversationItemViewModel.hasBeenForwarded = bool3;
      if ((0x4 & paramConversation.convFlags) != 4)
        break label117;
      bool4 = bool1;
      label80: localConversationItemViewModel.hasBeenRepliedTo = bool4;
      if ((0x10 & paramConversation.convFlags) != 16)
        break label123;
    }
    while (true)
    {
      localConversationItemViewModel.isInvite = bool1;
      return localConversationItemViewModel;
      label105: bool2 = false;
      break;
      label111: bool3 = false;
      break label61;
      label117: bool4 = false;
      break label80;
      label123: bool1 = false;
    }
  }

  static ConversationItemViewModel forConversationId(String paramString, long paramLong)
  {
    synchronized (sConversationHeaderMap)
    {
      ConversationItemViewModel localConversationItemViewModel = forConversationIdOrNull(paramString, paramLong);
      if (localConversationItemViewModel == null)
      {
        Pair localPair = new Pair(paramString, Long.valueOf(paramLong));
        localConversationItemViewModel = new ConversationItemViewModel();
        sConversationHeaderMap.put(localPair, localConversationItemViewModel);
      }
      return localConversationItemViewModel;
    }
  }

  static ConversationItemViewModel forConversationIdOrNull(String paramString, long paramLong)
  {
    Pair localPair = new Pair(paramString, Long.valueOf(paramLong));
    synchronized (sConversationHeaderMap)
    {
      ConversationItemViewModel localConversationItemViewModel = (ConversationItemViewModel)sConversationHeaderMap.get(localPair);
      return localConversationItemViewModel;
    }
  }

  static ConversationItemViewModel forCursor(String paramString, Cursor paramCursor)
  {
    return forConversation(paramString, new Conversation(paramCursor));
  }

  private Object getConvInfo()
  {
    if (this.conversation.conversationInfo != null)
      return this.conversation.conversationInfo;
    return this.conversation.getSnippet();
  }

  private static int getHashCode(Context paramContext, String paramString1, Object paramObject, String paramString2, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
  {
    if (paramString1 == null)
      return -1;
    if (TextUtils.isEmpty(paramString2))
      paramString2 = "";
    Object[] arrayOfObject = new Object[7];
    arrayOfObject[0] = paramObject;
    arrayOfObject[1] = paramString1;
    arrayOfObject[2] = paramString2;
    arrayOfObject[3] = Boolean.valueOf(paramBoolean1);
    arrayOfObject[4] = Boolean.valueOf(paramBoolean2);
    arrayOfObject[5] = Integer.valueOf(paramInt1);
    arrayOfObject[6] = Integer.valueOf(paramInt2);
    return Objects.hashCode(arrayOfObject);
  }

  private int getLayoutHashCode()
  {
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = Integer.valueOf(this.mDataHashCode);
    arrayOfObject[1] = Integer.valueOf(this.viewWidth);
    arrayOfObject[2] = Integer.valueOf(this.standardScaledDimen);
    arrayOfObject[3] = Boolean.valueOf(this.checkboxVisible);
    return Objects.hashCode(arrayOfObject);
  }

  public static void onAccessibilityUpdated()
  {
    sConversationHeaderMap.evictAll();
  }

  public static void onFolderUpdated(Folder paramFolder)
  {
    Uri localUri1;
    if (sCachedModelsFolder != null)
    {
      localUri1 = sCachedModelsFolder.uri;
      if (paramFolder == null)
        break label48;
    }
    label48: for (Uri localUri2 = paramFolder.uri; ; localUri2 = Uri.EMPTY)
    {
      if (!localUri1.equals(localUri2))
      {
        sCachedModelsFolder = paramFolder;
        sConversationHeaderMap.evictAll();
      }
      return;
      localUri1 = Uri.EMPTY;
      break;
    }
  }

  void addSenderFragment(int paramInt1, int paramInt2, CharacterStyle paramCharacterStyle, boolean paramBoolean)
  {
    SenderFragment localSenderFragment = new SenderFragment(paramInt1, paramInt2, this.sendersText, paramCharacterStyle, paramBoolean);
    this.senderFragments.add(localSenderFragment);
  }

  public CharSequence getContentDescription(Context paramContext)
  {
    Object localObject1;
    String str2;
    int j;
    label106: String str1;
    if (this.mContentDescription == null)
    {
      localObject1 = "";
      if (this.conversation.conversationInfo != null)
      {
        str2 = "";
        if (this.conversation.conversationInfo.messageInfos == null)
          break label207;
        j = -1 + this.conversation.conversationInfo.messageInfos.size();
        if (j != -1)
          str2 = ((MessageInfo)this.conversation.conversationInfo.messageInfos.get(j)).sender;
        if (!this.conversation.read)
          break label219;
        if (!TextUtils.isEmpty(str2))
          break label213;
        localObject1 = SendersView.getMe(paramContext);
        if (TextUtils.isEmpty((CharSequence)localObject1))
          localObject1 = str2;
      }
      boolean bool1 = DateUtils.isToday(this.conversation.dateMs);
      str1 = DateUtils.getRelativeTimeSpanString(paramContext, this.conversation.dateMs).toString();
      if (!bool1)
        break label308;
    }
    label308: for (int i = 2131427478; ; i = 2131427477)
    {
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = localObject1;
      arrayOfObject[1] = this.conversation.subject;
      arrayOfObject[2] = this.conversation.getSnippet();
      arrayOfObject[3] = str1;
      this.mContentDescription = paramContext.getString(i, arrayOfObject);
      return this.mContentDescription;
      label207: j = -1;
      break;
      label213: localObject1 = str2;
      break label106;
      label219: Iterator localIterator = this.conversation.conversationInfo.messageInfos.iterator();
      MessageInfo localMessageInfo;
      do
      {
        boolean bool2 = localIterator.hasNext();
        localObject2 = null;
        if (!bool2)
          break;
        localMessageInfo = (MessageInfo)localIterator.next();
      }
      while (localMessageInfo.read);
      Object localObject2 = localMessageInfo;
      if (localObject2 == null)
        break label106;
      if (TextUtils.isEmpty(localObject2.sender));
      for (localObject1 = SendersView.getMe(paramContext); ; localObject1 = localObject2.sender)
        break;
    }
  }

  boolean isDataValid(Context paramContext)
  {
    return this.mDataHashCode == getHashCode(paramContext, this.dateText, getConvInfo(), this.conversation.getRawFoldersString(), this.conversation.starred, this.conversation.read, this.conversation.priority, this.conversation.sendingState);
  }

  boolean isLayoutValid(Context paramContext)
  {
    return (isDataValid(paramContext)) && (this.mLayoutHashCode == getLayoutHashCode());
  }

  public void resetContentDescription()
  {
    this.mContentDescription = null;
  }

  void validate(Context paramContext)
  {
    this.mDataHashCode = getHashCode(paramContext, this.dateText, getConvInfo(), this.conversation.getRawFoldersString(), this.conversation.starred, this.conversation.read, this.conversation.priority, this.conversation.sendingState);
    this.mLayoutHashCode = getLayoutHashCode();
  }

  static class SenderFragment
  {
    String ellipsizedText;
    int end;
    boolean isFixed;
    boolean shouldDisplay;
    int start;
    CharacterStyle style;
    int width;

    SenderFragment(int paramInt1, int paramInt2, CharSequence paramCharSequence, CharacterStyle paramCharacterStyle, boolean paramBoolean)
    {
      this.start = paramInt1;
      this.end = paramInt2;
      this.style = paramCharacterStyle;
      this.isFixed = paramBoolean;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ConversationItemViewModel
 * JD-Core Version:    0.6.2
 */