package com.android.mail.providers;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import com.android.mail.utils.Utils;
import com.google.common.base.Objects;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message
  implements Parcelable
{
  public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator()
  {
    public Message createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Message(paramAnonymousParcel, null);
    }

    public Message[] newArray(int paramAnonymousInt)
    {
      return new Message[paramAnonymousInt];
    }
  };
  private static Pattern INLINE_IMAGE_PATTERN = Pattern.compile("<img\\s+[^>]*src=", 10);
  public Uri accountUri;
  public boolean alwaysShowImages;
  public boolean appendRefMessageContent;
  public Uri attachmentListUri;
  public String attachmentsJson;
  public String bodyHtml;
  public String bodyText;
  public Uri conversationUri;
  public long dateReceivedMs;
  public int draftType;
  public boolean embedsExternalResources;
  public Uri eventIntentUri;
  public boolean hasAttachments;
  public long id;
  public boolean isSending;
  private transient List<Attachment> mAttachments = null;
  private String mBcc;
  private transient String[] mBccAddresses = null;
  private String mCc;
  private transient String[] mCcAddresses = null;
  private String mFrom;
  private transient String[] mFromAddresses = null;
  private String mReplyTo;
  private transient String[] mReplyToAddresses = null;
  private String mTo;
  private transient String[] mToAddresses = null;
  public long messageFlags;
  public int quotedTextOffset;
  public boolean read;
  public String refMessageId;

  @Deprecated
  public String saveUri;

  @Deprecated
  public String sendUri;
  public String serverId;
  public String snippet;
  public int spamLinkType;
  public int spamWarningLevel;
  public String spamWarningString;
  public boolean starred;
  public String subject;
  public Uri uri;
  public String viaDomain;

  public Message()
  {
  }

  public Message(Cursor paramCursor)
  {
    Uri localUri1;
    Uri localUri2;
    label113: int j;
    label251: int k;
    label295: int m;
    label315: Uri localUri3;
    label353: int n;
    label409: int i1;
    label429: int i2;
    if (paramCursor != null)
    {
      this.id = paramCursor.getLong(0);
      this.serverId = paramCursor.getString(i);
      String str1 = paramCursor.getString(2);
      if (TextUtils.isEmpty(str1))
        break label597;
      localUri1 = Uri.parse(str1);
      this.uri = localUri1;
      String str2 = paramCursor.getString(3);
      if (TextUtils.isEmpty(str2))
        break label603;
      localUri2 = Uri.parse(str2);
      this.conversationUri = localUri2;
      this.subject = paramCursor.getString(4);
      this.snippet = paramCursor.getString(5);
      this.mFrom = paramCursor.getString(6);
      this.mTo = paramCursor.getString(7);
      this.mCc = paramCursor.getString(8);
      this.mBcc = paramCursor.getString(9);
      this.mReplyTo = paramCursor.getString(10);
      this.dateReceivedMs = paramCursor.getLong(11);
      this.bodyHtml = paramCursor.getString(12);
      this.bodyText = paramCursor.getString(13);
      if (paramCursor.getInt(14) == 0)
        break label609;
      j = i;
      this.embedsExternalResources = j;
      this.refMessageId = paramCursor.getString(15);
      this.draftType = paramCursor.getInt(16);
      if (paramCursor.getInt(17) == 0)
        break label615;
      k = i;
      this.appendRefMessageContent = k;
      if (paramCursor.getInt(18) == 0)
        break label621;
      m = i;
      this.hasAttachments = m;
      String str3 = paramCursor.getString(19);
      if ((!this.hasAttachments) || (TextUtils.isEmpty(str3)))
        break label627;
      localUri3 = Uri.parse(str3);
      this.attachmentListUri = localUri3;
      this.messageFlags = paramCursor.getLong(20);
      this.saveUri = paramCursor.getString(22);
      this.sendUri = paramCursor.getString(23);
      if (paramCursor.getInt(24) == 0)
        break label633;
      n = i;
      this.alwaysShowImages = n;
      if (paramCursor.getInt(25) == 0)
        break label639;
      i1 = i;
      this.read = i1;
      if (paramCursor.getInt(26) == 0)
        break label645;
      i2 = i;
      label449: this.starred = i2;
      this.quotedTextOffset = paramCursor.getInt(27);
      this.attachmentsJson = paramCursor.getString(28);
      String str4 = paramCursor.getString(30);
      boolean bool = TextUtils.isEmpty(str4);
      Uri localUri4 = null;
      if (!bool)
        localUri4 = Uri.parse(str4);
      this.accountUri = localUri4;
      this.eventIntentUri = Utils.getValidUri(paramCursor.getString(31));
      this.spamWarningString = paramCursor.getString(32);
      this.spamWarningLevel = paramCursor.getInt(33);
      this.spamLinkType = paramCursor.getInt(34);
      this.viaDomain = paramCursor.getString(35);
      if (paramCursor.getInt(36) == 0)
        break label651;
    }
    while (true)
    {
      this.isSending = i;
      return;
      label597: localUri1 = null;
      break;
      label603: localUri2 = null;
      break label113;
      label609: j = 0;
      break label251;
      label615: k = 0;
      break label295;
      label621: m = 0;
      break label315;
      label627: localUri3 = null;
      break label353;
      label633: n = 0;
      break label409;
      label639: i1 = 0;
      break label429;
      label645: i2 = 0;
      break label449;
      label651: i = 0;
    }
  }

  private Message(Parcel paramParcel)
  {
    this.id = paramParcel.readLong();
    this.serverId = paramParcel.readString();
    this.uri = ((Uri)paramParcel.readParcelable(null));
    this.conversationUri = ((Uri)paramParcel.readParcelable(null));
    this.subject = paramParcel.readString();
    this.snippet = paramParcel.readString();
    this.mFrom = paramParcel.readString();
    this.mTo = paramParcel.readString();
    this.mCc = paramParcel.readString();
    this.mBcc = paramParcel.readString();
    this.mReplyTo = paramParcel.readString();
    this.dateReceivedMs = paramParcel.readLong();
    this.bodyHtml = paramParcel.readString();
    this.bodyText = paramParcel.readString();
    boolean bool2;
    boolean bool3;
    label196: boolean bool4;
    label212: boolean bool5;
    if (paramParcel.readInt() != 0)
    {
      bool2 = bool1;
      this.embedsExternalResources = bool2;
      this.refMessageId = paramParcel.readString();
      this.draftType = paramParcel.readInt();
      if (paramParcel.readInt() == 0)
        break label360;
      bool3 = bool1;
      this.appendRefMessageContent = bool3;
      if (paramParcel.readInt() == 0)
        break label366;
      bool4 = bool1;
      this.hasAttachments = bool4;
      this.attachmentListUri = ((Uri)paramParcel.readParcelable(null));
      this.messageFlags = paramParcel.readLong();
      this.saveUri = paramParcel.readString();
      this.sendUri = paramParcel.readString();
      if (paramParcel.readInt() == 0)
        break label372;
      bool5 = bool1;
      label264: this.alwaysShowImages = bool5;
      this.quotedTextOffset = paramParcel.readInt();
      this.attachmentsJson = paramParcel.readString();
      this.accountUri = ((Uri)paramParcel.readParcelable(null));
      this.eventIntentUri = ((Uri)paramParcel.readParcelable(null));
      this.spamWarningString = paramParcel.readString();
      this.spamWarningLevel = paramParcel.readInt();
      this.spamLinkType = paramParcel.readInt();
      this.viaDomain = paramParcel.readString();
      if (paramParcel.readInt() == 0)
        break label378;
    }
    while (true)
    {
      this.isSending = bool1;
      return;
      bool2 = false;
      break;
      label360: bool3 = false;
      break label196;
      label366: bool4 = false;
      break label212;
      label372: bool5 = false;
      break label264;
      label378: bool1 = false;
    }
  }

  private boolean embedsExternalResources()
  {
    return (this.embedsExternalResources) || ((!TextUtils.isEmpty(this.bodyHtml)) && (INLINE_IMAGE_PATTERN.matcher(this.bodyHtml).find()));
  }

  public static String[] tokenizeAddresses(String paramString)
  {
    String[] arrayOfString;
    if (TextUtils.isEmpty(paramString))
      arrayOfString = new String[0];
    while (true)
    {
      return arrayOfString;
      Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(paramString);
      arrayOfString = new String[arrayOfRfc822Token.length];
      for (int i = 0; i < arrayOfRfc822Token.length; i++)
        arrayOfString[i] = arrayOfRfc822Token[i].toString();
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(Object paramObject)
  {
    return (this == paramObject) || ((paramObject != null) && ((paramObject instanceof Message)) && (Objects.equal(this.uri, ((Message)paramObject).uri)));
  }

  public List<Attachment> getAttachments()
  {
    if (this.mAttachments == null)
      if (this.attachmentsJson == null)
        break label30;
    label30: for (this.mAttachments = Attachment.fromJSONArray(this.attachmentsJson); ; this.mAttachments = Collections.emptyList())
      return this.mAttachments;
  }

  public String getBcc()
  {
    return this.mBcc;
  }

  public String[] getBccAddresses()
  {
    try
    {
      if (this.mBccAddresses == null)
        this.mBccAddresses = tokenizeAddresses(this.mBcc);
      String[] arrayOfString = this.mBccAddresses;
      return arrayOfString;
    }
    finally
    {
    }
  }

  public String getBodyAsHtml()
  {
    String str = "";
    if (!TextUtils.isEmpty(this.bodyHtml))
      str = this.bodyHtml;
    while (TextUtils.isEmpty(this.bodyText))
      return str;
    return Html.toHtml(new SpannedString(this.bodyText));
  }

  public String getCc()
  {
    return this.mCc;
  }

  public String[] getCcAddresses()
  {
    try
    {
      if (this.mCcAddresses == null)
        this.mCcAddresses = tokenizeAddresses(this.mCc);
      String[] arrayOfString = this.mCcAddresses;
      return arrayOfString;
    }
    finally
    {
    }
  }

  public String getFrom()
  {
    return this.mFrom;
  }

  public String[] getFromAddresses()
  {
    try
    {
      if (this.mFromAddresses == null)
        this.mFromAddresses = tokenizeAddresses(this.mFrom);
      String[] arrayOfString = this.mFromAddresses;
      return arrayOfString;
    }
    finally
    {
    }
  }

  public String getReplyTo()
  {
    return this.mReplyTo;
  }

  public String[] getReplyToAddresses()
  {
    try
    {
      if (this.mReplyToAddresses == null)
        this.mReplyToAddresses = tokenizeAddresses(this.mReplyTo);
      String[] arrayOfString = this.mReplyToAddresses;
      return arrayOfString;
    }
    finally
    {
    }
  }

  public String getTo()
  {
    return this.mTo;
  }

  public String[] getToAddresses()
  {
    try
    {
      if (this.mToAddresses == null)
        this.mToAddresses = tokenizeAddresses(this.mTo);
      String[] arrayOfString = this.mToAddresses;
      return arrayOfString;
    }
    finally
    {
    }
  }

  public int hashCode()
  {
    if (this.uri == null)
      return 0;
    return this.uri.hashCode();
  }

  public boolean isFlaggedCalendarInvite()
  {
    return (0x10 & this.messageFlags) == 16L;
  }

  public void markAlwaysShowImages(AsyncQueryHandler paramAsyncQueryHandler, int paramInt, Object paramObject)
  {
    this.alwaysShowImages = true;
    ContentValues localContentValues = new ContentValues(1);
    localContentValues.put("alwaysShowImages", Integer.valueOf(1));
    paramAsyncQueryHandler.startUpdate(paramInt, paramObject, this.uri, localContentValues, null, null);
  }

  public void setBcc(String paramString)
  {
    try
    {
      this.mBcc = paramString;
      this.mBccAddresses = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void setCc(String paramString)
  {
    try
    {
      this.mCc = paramString;
      this.mCcAddresses = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void setFrom(String paramString)
  {
    try
    {
      this.mFrom = paramString;
      this.mFromAddresses = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void setReplyTo(String paramString)
  {
    try
    {
      this.mReplyTo = paramString;
      this.mReplyToAddresses = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void setTo(String paramString)
  {
    try
    {
      this.mTo = paramString;
      this.mToAddresses = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public boolean shouldShowImagePrompt()
  {
    return (!this.alwaysShowImages) && (embedsExternalResources());
  }

  public String toString()
  {
    return "[message id=" + this.id + "]";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    paramParcel.writeLong(this.id);
    paramParcel.writeString(this.serverId);
    paramParcel.writeParcelable(this.uri, 0);
    paramParcel.writeParcelable(this.conversationUri, 0);
    paramParcel.writeString(this.subject);
    paramParcel.writeString(this.snippet);
    paramParcel.writeString(this.mFrom);
    paramParcel.writeString(this.mTo);
    paramParcel.writeString(this.mCc);
    paramParcel.writeString(this.mBcc);
    paramParcel.writeString(this.mReplyTo);
    paramParcel.writeLong(this.dateReceivedMs);
    paramParcel.writeString(this.bodyHtml);
    paramParcel.writeString(this.bodyText);
    int j;
    int k;
    label158: int m;
    label174: int n;
    if (this.embedsExternalResources)
    {
      j = i;
      paramParcel.writeInt(j);
      paramParcel.writeString(this.refMessageId);
      paramParcel.writeInt(this.draftType);
      if (!this.appendRefMessageContent)
        break label314;
      k = i;
      paramParcel.writeInt(k);
      if (!this.hasAttachments)
        break label320;
      m = i;
      paramParcel.writeInt(m);
      paramParcel.writeParcelable(this.attachmentListUri, 0);
      paramParcel.writeLong(this.messageFlags);
      paramParcel.writeString(this.saveUri);
      paramParcel.writeString(this.sendUri);
      if (!this.alwaysShowImages)
        break label326;
      n = i;
      label223: paramParcel.writeInt(n);
      paramParcel.writeInt(this.quotedTextOffset);
      paramParcel.writeString(this.attachmentsJson);
      paramParcel.writeParcelable(this.accountUri, 0);
      paramParcel.writeParcelable(this.eventIntentUri, 0);
      paramParcel.writeString(this.spamWarningString);
      paramParcel.writeInt(this.spamWarningLevel);
      paramParcel.writeInt(this.spamLinkType);
      paramParcel.writeString(this.viaDomain);
      if (!this.isSending)
        break label332;
    }
    while (true)
    {
      paramParcel.writeInt(i);
      return;
      j = 0;
      break;
      label314: k = 0;
      break label158;
      label320: m = 0;
      break label174;
      label326: n = 0;
      break label223;
      label332: i = 0;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.Message
 * JD-Core Version:    0.6.2
 */