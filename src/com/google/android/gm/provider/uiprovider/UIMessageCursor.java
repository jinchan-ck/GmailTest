package com.google.android.gm.provider.uiprovider;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.mail.providers.Attachment;
import com.google.android.gm.EmailAddress;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.Attachment;
import com.google.android.gm.provider.Gmail.CursorStatus;
import com.google.android.gm.provider.Gmail.MessageModification;
import com.google.android.gm.provider.LogUtils;
import com.google.android.gm.provider.SpamReasonType;
import com.google.android.gm.provider.UiProvider;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;

public class UIMessageCursor extends UICursorWapper
{
  private static final Map<String, Integer> GMAIL_STATUS_UI_STATUS_MAP = new ImmutableMap.Builder().put(Gmail.CursorStatus.LOADED.toString(), Integer.valueOf(2)).put(Gmail.CursorStatus.LOADING.toString(), Integer.valueOf(1)).put(Gmail.CursorStatus.SEARCHING.toString(), Integer.valueOf(1)).put(Gmail.CursorStatus.ERROR.toString(), Integer.valueOf(4)).put(Gmail.CursorStatus.COMPLETE.toString(), Integer.valueOf(8)).build();
  private final String mAccount;
  private final List<Gmail.Attachment> mAttachments = Lists.newArrayList();
  private final int mBccIndex;
  private final int mBodyEmbedsExternalResourcesIndex;
  private final int mBodyIndex;
  private final int mCcIndex;
  private final Context mContext;
  private long mConversationId;
  private final int mConversationIdIndex;
  private final int mCustomFromIndex;
  private final int mDateReceivedMsIndex;
  private final Bundle mExtras;
  private final int mForwardIndex;
  private String mFromAddress;
  private final int mFromIndex;
  private final String mGmailQuote;
  private final int mIdIndex;
  private final int mIsDraftIndex;
  private final int mIsInOutboxIndex;
  private final int mIsStarredIndex;
  private final int mIsUnreadIndex;
  private final int mJoinedAttachmentInfosIndex;
  private long mLocalMessageId;
  private String mMessageBody;
  private final Persistence mPersistence;
  private final int mQuoteStartPosIndex;
  private final int mRefMessageIdIndex;
  private final int mReplyToIndex;
  private boolean mRowDataIsCached = false;
  private long mServerMessageId;
  private final int mServerMessageIdIndex;
  private final int mSnippetIndex;
  private int mSpamReason;
  private final int mSubjectIndex;
  private final int mToIndex;

  public UIMessageCursor(Context paramContext, Cursor paramCursor, String paramString1, Persistence paramPersistence, String paramString2, String[] paramArrayOfString)
  {
    super(paramCursor, paramArrayOfString);
    this.mAccount = paramString1;
    this.mPersistence = paramPersistence;
    this.mContext = paramContext;
    this.mGmailQuote = paramString2;
    this.mIdIndex = paramCursor.getColumnIndexOrThrow("_id");
    this.mServerMessageIdIndex = paramCursor.getColumnIndexOrThrow("messageId");
    this.mConversationIdIndex = paramCursor.getColumnIndexOrThrow("conversation");
    this.mSubjectIndex = paramCursor.getColumnIndexOrThrow("subject");
    this.mSnippetIndex = paramCursor.getColumnIndexOrThrow("snippet");
    this.mFromIndex = paramCursor.getColumnIndexOrThrow("fromAddress");
    this.mCustomFromIndex = paramCursor.getColumnIndexOrThrow("customFromAddress");
    this.mToIndex = paramCursor.getColumnIndexOrThrow("toAddresses");
    this.mCcIndex = paramCursor.getColumnIndexOrThrow("ccAddresses");
    this.mBccIndex = paramCursor.getColumnIndexOrThrow("bccAddresses");
    this.mReplyToIndex = paramCursor.getColumnIndexOrThrow("replyToAddresses");
    this.mDateReceivedMsIndex = paramCursor.getColumnIndexOrThrow("dateReceivedMs");
    this.mBodyIndex = paramCursor.getColumnIndexOrThrow("body");
    this.mBodyEmbedsExternalResourcesIndex = paramCursor.getColumnIndexOrThrow("bodyEmbedsExternalResources");
    this.mRefMessageIdIndex = paramCursor.getColumnIndexOrThrow("refMessageId");
    this.mIsDraftIndex = paramCursor.getColumnIndexOrThrow("isDraft");
    this.mForwardIndex = paramCursor.getColumnIndexOrThrow("forward");
    this.mJoinedAttachmentInfosIndex = paramCursor.getColumnIndexOrThrow("joinedAttachmentInfos");
    this.mIsUnreadIndex = paramCursor.getColumnIndexOrThrow("isUnread");
    this.mIsStarredIndex = paramCursor.getColumnIndexOrThrow("isStarred");
    this.mIsInOutboxIndex = paramCursor.getColumnIndexOrThrow("isInOutbox");
    this.mQuoteStartPosIndex = paramCursor.getColumnIndexOrThrow("quoteStartPos");
    this.mExtras = getExtrasInternal();
  }

  private void cacheRowValues()
  {
    if (!this.mRowDataIsCached)
    {
      loadAttachmentInfos();
      this.mServerMessageId = super.getLong(this.mServerMessageIdIndex);
      this.mLocalMessageId = super.getLong(this.mIdIndex);
      this.mConversationId = super.getLong(this.mConversationIdIndex);
      this.mFromAddress = getFromAddress();
      this.mSpamReason = getSpamReason();
      this.mRowDataIsCached = true;
    }
  }

  private String[] getAddresses(String paramString, int paramInt)
  {
    return TextUtils.split(getStringInColumn(paramInt), Gmail.EMAIL_SEPARATOR_PATTERN);
  }

  private int getDraftType()
  {
    boolean bool = getIsDraft();
    int i = 0;
    if (bool)
    {
      if (getForward())
        i = 4;
    }
    else
      return i;
    if (getRefMessageId() != 0L)
    {
      if (getToAddresses().length + getCcAddresses().length + getBccAddresses().length > 1)
        return 3;
      return 2;
    }
    return 1;
  }

  private static EmailAddress getEmailAddress(String paramString)
  {
    return EmailAddress.getEmailAddress(paramString);
  }

  private Bundle getExtrasInternal()
  {
    Bundle localBundle1 = super.getExtras();
    Bundle localBundle2 = new Bundle();
    int i = 2;
    if (localBundle1.containsKey("status"))
    {
      String str = localBundle1.getString("status");
      if (GMAIL_STATUS_UI_STATUS_MAP.containsKey(str))
        i = ((Integer)GMAIL_STATUS_UI_STATUS_MAP.get(str)).intValue();
    }
    if ((i == 2) && (getCount() == 0))
    {
      LogUtils.e("Gmail", "Unexpected loaded state for empty cursor", new Object[0]);
      i = 1;
    }
    localBundle2.putInt("cursor_status", i);
    return localBundle2;
  }

  private boolean getForward()
  {
    return super.getLong(this.mForwardIndex) != 0L;
  }

  private static String getGmailAttachmentsAsJson(Collection<Gmail.Attachment> paramCollection, String paramString, long paramLong1, long paramLong2, long paramLong3)
  {
    Object localObject = null;
    if (paramCollection != null)
    {
      boolean bool = paramCollection.isEmpty();
      localObject = null;
      if (!bool)
      {
        JSONArray localJSONArray;
        try
        {
          localJSONArray = new JSONArray();
          Iterator localIterator = paramCollection.iterator();
          while (localIterator.hasNext())
          {
            Gmail.Attachment localAttachment = (Gmail.Attachment)localIterator.next();
            Uri localUri = UiProvider.getMessageAttachmentUri(paramString, paramLong1, paramLong2, paramLong3, localAttachment.partId, localAttachment.contentType, localAttachment.toJoinedString());
            localJSONArray.put(Attachment.toJSON(localAttachment.name, localAttachment.size, localUri, null, localAttachment.contentType, null));
          }
        }
        catch (JSONException localJSONException)
        {
          throw new IllegalArgumentException(localJSONException);
        }
        String str = localJSONArray.toString();
        localObject = str;
      }
    }
    return localObject;
  }

  private boolean getIsDraft()
  {
    return super.getLong(this.mIsDraftIndex) != 0L;
  }

  private boolean getIsStarred()
  {
    return super.getLong(this.mIsStarredIndex) != 0L;
  }

  private boolean getIsUnread()
  {
    return super.getLong(this.mIsUnreadIndex) != 0L;
  }

  private String getMessageBody()
  {
    if (this.mMessageBody == null)
      this.mMessageBody = super.getString(this.mBodyIndex);
    return this.mMessageBody;
  }

  private int getQuotedTextOffset()
  {
    int i = getQuoteStartPos();
    if (i <= 0)
    {
      String str = getMessageBody();
      if (TextUtils.isEmpty(str))
        return -1;
      return str.indexOf(this.mGmailQuote);
    }
    return i;
  }

  private long getRefMessageId()
  {
    return super.getLong(this.mRefMessageIdIndex);
  }

  private int getSpamReason()
  {
    return -1;
  }

  private String getViaDomain()
  {
    return null;
  }

  private void loadAttachmentInfos()
  {
    String str = super.getString(this.mJoinedAttachmentInfosIndex);
    this.mAttachments.addAll(Gmail.MessageModification.parseJoinedAttachmentString(str));
  }

  public String[] getBccAddresses()
  {
    return getAddresses("bccAddresses", this.mBccIndex);
  }

  public String[] getCcAddresses()
  {
    return getAddresses("ccAddresses", this.mCcIndex);
  }

  public Bundle getExtras()
  {
    return this.mExtras;
  }

  public String getFromAddress()
  {
    String str1 = getStringInColumn(this.mFromIndex);
    if (!TextUtils.isEmpty(str1))
      return str1;
    String str2 = getStringInColumn(this.mCustomFromIndex);
    if (!TextUtils.isEmpty(str2));
    while (true)
    {
      return str2;
      str2 = str1;
    }
  }

  public int getInt(int paramInt)
  {
    int i = 1;
    cacheRowValues();
    int j;
    if (this.mAttachments.size() > 0)
      j = i;
    switch (paramInt)
    {
    case 15:
    case 19:
    case 20:
    case 21:
    case 22:
    case 23:
    case 28:
    case 29:
    case 30:
    case 31:
    case 32:
    case 35:
    default:
      Object[] arrayOfObject = new Object[i];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      LogUtils.e("Gmail", "UIMessageCursor.getInt(%d): Unexpected column", arrayOfObject);
      i = super.getInt(paramInt);
    case 14:
    case 16:
    case 18:
    case 25:
    case 26:
    case 36:
    case 24:
    case 17:
    case 27:
    case 33:
      do
      {
        do
        {
          String str;
          do
          {
            do
            {
              do
              {
                do
                {
                  return i;
                  j = 0;
                  break;
                  return super.getInt(this.mBodyEmbedsExternalResourcesIndex);
                  return getDraftType();
                }
                while (j != 0);
                return 0;
                boolean bool = getIsUnread();
                int k = 0;
                if (bool);
                while (true)
                {
                  return k;
                  k = i;
                }
              }
              while (getIsStarred());
              return 0;
            }
            while (getIsInOutbox());
            return 0;
            str = getEmailAddress(this.mFromAddress).getAddress();
          }
          while (this.mPersistence.getDisplayImagesFromSender(this.mContext, str));
          return 0;
        }
        while (getQuotedTextOffset() >= 0);
        return 0;
        return getQuotedTextOffset();
        if (this.mSpamReason == -1)
          return 0;
      }
      while (!SpamReasonType.HIGH_WARNING_LEVEL_SPAM_TYPES.contains(Integer.valueOf(this.mSpamReason)));
      return 2;
    case 34:
    }
    Integer localInteger = (Integer)SpamReasonType.WARNING_LINK_TYPE_MAP.get(Integer.valueOf(this.mSpamReason));
    if (localInteger == null)
      return 0;
    return localInteger.intValue();
  }

  public boolean getIsInOutbox()
  {
    return super.getLong(this.mIsInOutboxIndex) != 0L;
  }

  public long getLong(int paramInt)
  {
    switch (paramInt)
    {
    default:
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      LogUtils.e("Gmail", "UIMessageCursor.getLong(%d): Unexpected column", arrayOfObject);
      return super.getLong(paramInt);
    case 0:
      return super.getLong(this.mIdIndex);
    case 11:
      return super.getLong(this.mDateReceivedMsIndex);
    case 20:
    }
    return 0L;
  }

  public int getQuoteStartPos()
  {
    return super.getInt(this.mQuoteStartPosIndex);
  }

  public String getReplyToAddress()
  {
    String[] arrayOfString = TextUtils.split(getStringInColumn(this.mReplyToIndex), Gmail.EMAIL_SEPARATOR_PATTERN);
    if ((arrayOfString == null) || (arrayOfString.length == 0))
      return null;
    return arrayOfString[0];
  }

  public String getString(int paramInt)
  {
    int i = 1;
    cacheRowValues();
    String str = null;
    switch (paramInt)
    {
    case 11:
    case 14:
    case 16:
    case 17:
    case 18:
    case 20:
    case 24:
    case 25:
    case 26:
    case 27:
    case 33:
    case 34:
    default:
      Object[] arrayOfObject = new Object[i];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      LogUtils.e("Gmail", "UIMessageCursor.getString(%d): Unexpected column", arrayOfObject);
      str = super.getString(paramInt);
    case 13:
    case 30:
    case 31:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 29:
    case 7:
    case 8:
    case 9:
    case 10:
    case 12:
    case 15:
    case 19:
    case 21:
    case 22:
    case 23:
    case 28:
    case 32:
      Integer localInteger;
      int j;
      do
      {
        do
        {
          return str;
          return Long.toString(this.mServerMessageId);
          return UiProvider.getMessageByIdUri(this.mAccount, this.mLocalMessageId).toString();
          return UiProvider.getConversationUri(this.mAccount, this.mConversationId).toString();
          return super.getString(this.mSubjectIndex);
          return super.getString(this.mSnippetIndex);
          return this.mFromAddress;
          return super.getString(this.mCustomFromIndex);
          return TextUtils.join(", ", getToAddresses());
          return TextUtils.join(", ", getCcAddresses());
          return TextUtils.join(", ", getBccAddresses());
          return getReplyToAddress();
          return getMessageBody();
          return Long.toString(getRefMessageId());
          if (this.mAttachments.size() > 0);
          while (true)
          {
            str = null;
            if (i == 0)
              break;
            return UiProvider.getMessageAttachmentsUri(this.mAccount, this.mConversationId, this.mServerMessageId, this.mLocalMessageId).toString();
            i = 0;
          }
          return Gmail.MessageModification.joinedAttachmentsString(this.mAttachments);
          return UiProvider.getSaveMessageUri(this.mAccount).toString();
          return UiProvider.getSendMessageUri(this.mAccount).toString();
          return getGmailAttachmentsAsJson(this.mAttachments, this.mAccount, this.mConversationId, this.mServerMessageId, this.mLocalMessageId);
          localInteger = (Integer)SpamReasonType.SPAM_REASON_TO_STRING_MAP.get(Integer.valueOf(this.mSpamReason));
          str = null;
        }
        while (localInteger == null);
        j = localInteger.intValue();
        str = null;
      }
      while (j == 0);
      return this.mContext.getResources().getString(localInteger.intValue());
    case 35:
    }
    return getViaDomain();
  }

  public String[] getToAddresses()
  {
    return getAddresses("toAddresses", this.mToIndex);
  }

  protected void resetCursorRowState()
  {
    super.resetCursorRowState();
    this.mAttachments.clear();
    this.mMessageBody = null;
    this.mRowDataIsCached = false;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.uiprovider.UIMessageCursor
 * JD-Core Version:    0.6.2
 */