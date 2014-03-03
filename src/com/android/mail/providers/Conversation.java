package com.android.mail.providers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Conversation
  implements Parcelable
{
  public static final Parcelable.Creator<Conversation> CREATOR = new Parcelable.Creator()
  {
    public Conversation createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Conversation(paramAnonymousParcel, null);
    }

    public Conversation[] newArray(int paramAnonymousInt)
    {
      return new Conversation[paramAnonymousInt];
    }
  };
  public static final Collection<Conversation> EMPTY = Collections.emptyList();
  public static final Uri MOVE_CONVERSATIONS_URI = Uri.parse("content://moveconversations");
  private static String sSubjectAndSnippet;
  public Uri accountUri;
  private ArrayList<Folder> cachedDisplayableFolders;
  private ArrayList<Folder> cachedRawFolders;
  public int color;
  public int convFlags;
  public Uri conversationBaseUri;
  public ConversationInfo conversationInfo;
  public long dateMs;
  public boolean hasAttachments;
  public long id;
  public boolean isRemote;
  public transient boolean localDeleteOnUpdate;
  public Uri messageListUri;
  public boolean muted;
  private int numDrafts;
  private int numMessages;
  public int personalLevel;
  public boolean phishing;
  public transient int position;
  public int priority;
  private String rawFolders;
  public boolean read;

  @Deprecated
  public String senders;
  public int sendingState;

  @Deprecated
  public String snippet;
  public boolean spam;
  public boolean starred;
  public String subject;
  public Uri uri;
  private transient boolean viewed;

  public Conversation()
  {
  }

  public Conversation(Cursor paramCursor)
  {
    int j;
    Uri localUri1;
    label113: int k;
    label157: int m;
    label177: int n;
    label233: int i1;
    label253: int i2;
    label273: Uri localUri2;
    if (paramCursor != null)
    {
      this.id = paramCursor.getLong(0);
      this.uri = Uri.parse(paramCursor.getString(i));
      this.dateMs = paramCursor.getLong(6);
      this.subject = paramCursor.getString(3);
      if (this.subject == null)
        this.subject = "";
      if (paramCursor.getInt(7) == 0)
        break label458;
      j = i;
      this.hasAttachments = j;
      String str1 = paramCursor.getString(2);
      if (TextUtils.isEmpty(str1))
        break label463;
      localUri1 = Uri.parse(str1);
      this.messageListUri = localUri1;
      this.sendingState = paramCursor.getInt(10);
      this.priority = paramCursor.getInt(11);
      if (paramCursor.getInt(12) == 0)
        break label469;
      k = i;
      this.read = k;
      if (paramCursor.getInt(13) == 0)
        break label475;
      m = i;
      this.starred = m;
      this.rawFolders = paramCursor.getString(14);
      this.convFlags = paramCursor.getInt(15);
      this.personalLevel = paramCursor.getInt(16);
      if (paramCursor.getInt(17) == 0)
        break label481;
      n = i;
      this.spam = n;
      if (paramCursor.getInt(18) == 0)
        break label487;
      i1 = i;
      this.phishing = i1;
      if (paramCursor.getInt(19) == 0)
        break label493;
      i2 = i;
      this.muted = i2;
      this.color = paramCursor.getInt(20);
      String str2 = paramCursor.getString(21);
      if (TextUtils.isEmpty(str2))
        break label499;
      localUri2 = Uri.parse(str2);
      label316: this.accountUri = localUri2;
      this.position = -1;
      this.localDeleteOnUpdate = false;
      this.conversationInfo = ConversationInfo.fromString(paramCursor.getString(5));
      String str3 = paramCursor.getString(23);
      boolean bool = TextUtils.isEmpty(str3);
      Uri localUri3 = null;
      if (!bool)
        localUri3 = Uri.parse(str3);
      this.conversationBaseUri = localUri3;
      if (this.conversationInfo == null)
      {
        this.snippet = paramCursor.getString(4);
        this.senders = emptyIfNull(paramCursor.getString(22));
        this.numMessages = paramCursor.getInt(8);
        this.numDrafts = paramCursor.getInt(9);
      }
      if (paramCursor.getInt(24) == 0)
        break label505;
    }
    while (true)
    {
      this.isRemote = i;
      return;
      label458: j = 0;
      break;
      label463: localUri1 = null;
      break label113;
      label469: k = 0;
      break label157;
      label475: m = 0;
      break label177;
      label481: n = 0;
      break label233;
      label487: i1 = 0;
      break label253;
      label493: i2 = 0;
      break label273;
      label499: localUri2 = null;
      break label316;
      label505: i = 0;
    }
  }

  private Conversation(Parcel paramParcel)
  {
    this.id = paramParcel.readLong();
    this.uri = ((Uri)paramParcel.readParcelable(null));
    this.subject = paramParcel.readString();
    this.dateMs = paramParcel.readLong();
    this.snippet = paramParcel.readString();
    boolean bool2;
    boolean bool3;
    label129: boolean bool4;
    label145: boolean bool5;
    label185: boolean bool6;
    label201: boolean bool7;
    if (paramParcel.readInt() != 0)
    {
      bool2 = bool1;
      this.hasAttachments = bool2;
      this.messageListUri = ((Uri)paramParcel.readParcelable(null));
      this.senders = emptyIfNull(paramParcel.readString());
      this.numMessages = paramParcel.readInt();
      this.numDrafts = paramParcel.readInt();
      this.sendingState = paramParcel.readInt();
      this.priority = paramParcel.readInt();
      if (paramParcel.readInt() == 0)
        break label294;
      bool3 = bool1;
      this.read = bool3;
      if (paramParcel.readInt() == 0)
        break label300;
      bool4 = bool1;
      this.starred = bool4;
      this.rawFolders = paramParcel.readString();
      this.convFlags = paramParcel.readInt();
      this.personalLevel = paramParcel.readInt();
      if (paramParcel.readInt() == 0)
        break label306;
      bool5 = bool1;
      this.spam = bool5;
      if (paramParcel.readInt() == 0)
        break label312;
      bool6 = bool1;
      this.phishing = bool6;
      if (paramParcel.readInt() == 0)
        break label318;
      bool7 = bool1;
      label217: this.muted = bool7;
      this.color = paramParcel.readInt();
      this.accountUri = ((Uri)paramParcel.readParcelable(null));
      this.position = -1;
      this.localDeleteOnUpdate = false;
      this.conversationInfo = ConversationInfo.fromString(paramParcel.readString());
      this.conversationBaseUri = ((Uri)paramParcel.readParcelable(null));
      if (paramParcel.readInt() == 0)
        break label324;
    }
    while (true)
    {
      this.isRemote = bool1;
      return;
      bool2 = false;
      break;
      label294: bool3 = false;
      break label129;
      label300: bool4 = false;
      break label145;
      label306: bool5 = false;
      break label185;
      label312: bool6 = false;
      break label201;
      label318: bool7 = false;
      break label217;
      label324: bool1 = false;
    }
  }

  private void clearCachedFolders()
  {
    this.cachedRawFolders = null;
    this.cachedDisplayableFolders = null;
  }

  public static final boolean contains(Collection<Conversation> paramCollection, Conversation paramConversation)
  {
    boolean bool = true;
    if ((paramCollection == null) || (paramCollection.size() <= 0))
      bool = false;
    while (paramConversation == null)
      return bool;
    long l = paramConversation.id;
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      if (l == ((Conversation)localIterator.next()).id)
        return bool;
    return false;
  }

  public static Conversation create(long paramLong1, Uri paramUri1, String paramString1, long paramLong2, String paramString2, boolean paramBoolean1, Uri paramUri2, String paramString3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean2, boolean paramBoolean3, String paramString4, int paramInt5, int paramInt6, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, Uri paramUri3, ConversationInfo paramConversationInfo, Uri paramUri4, boolean paramBoolean7)
  {
    Conversation localConversation = new Conversation();
    localConversation.id = paramLong1;
    localConversation.uri = paramUri1;
    localConversation.subject = paramString1;
    localConversation.dateMs = paramLong2;
    localConversation.snippet = paramString2;
    localConversation.hasAttachments = paramBoolean1;
    localConversation.messageListUri = paramUri2;
    localConversation.senders = emptyIfNull(paramString3);
    localConversation.numMessages = paramInt1;
    localConversation.numDrafts = paramInt2;
    localConversation.sendingState = paramInt3;
    localConversation.priority = paramInt4;
    localConversation.read = paramBoolean2;
    localConversation.starred = paramBoolean3;
    localConversation.rawFolders = paramString4;
    localConversation.convFlags = paramInt5;
    localConversation.personalLevel = paramInt6;
    localConversation.spam = paramBoolean4;
    localConversation.phishing = paramBoolean5;
    localConversation.muted = paramBoolean6;
    localConversation.color = 0;
    localConversation.accountUri = paramUri3;
    localConversation.conversationInfo = paramConversationInfo;
    localConversation.conversationBaseUri = paramUri4;
    localConversation.isRemote = paramBoolean7;
    return localConversation;
  }

  private static String emptyIfNull(String paramString)
  {
    if (paramString != null)
      return paramString;
    return "";
  }

  public static SpannableStringBuilder getSubjectAndSnippetForDisplay(Context paramContext, String paramString1, String paramString2)
  {
    if (sSubjectAndSnippet == null)
      sSubjectAndSnippet = paramContext.getString(2131427480);
    if (!TextUtils.isEmpty(paramString2))
      paramString1 = String.format(sSubjectAndSnippet, new Object[] { paramString1, paramString2 });
    return new SpannableStringBuilder(paramString1);
  }

  public static Collection<Conversation> listOf(Conversation paramConversation)
  {
    if (paramConversation == null)
      return EMPTY;
    return ImmutableList.of(paramConversation);
  }

  public static String toString(Collection<Conversation> paramCollection)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramCollection.size() + " conversations:");
    int i = 0;
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Conversation localConversation = (Conversation)localIterator.next();
      i++;
      localStringBuilder.append("      " + i + ": " + localConversation.toString() + "\n");
    }
    return localStringBuilder.toString();
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof Conversation))
      return ((Conversation)paramObject).uri.equals(this.uri);
    return false;
  }

  public int getNumMessages()
  {
    if (this.conversationInfo != null)
      return this.conversationInfo.messageCount;
    return this.numMessages;
  }

  public ArrayList<Folder> getRawFolders()
  {
    if (this.cachedRawFolders == null)
    {
      if (!TextUtils.isEmpty(this.rawFolders))
        this.cachedRawFolders = Folder.getFoldersArray(this.rawFolders);
    }
    else
      return this.cachedRawFolders;
    return new ArrayList();
  }

  public ArrayList<Folder> getRawFoldersForDisplay(Folder paramFolder)
  {
    ArrayList localArrayList = getRawFolders();
    if (this.cachedDisplayableFolders == null)
    {
      this.cachedDisplayableFolders = new ArrayList();
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        Folder localFolder = (Folder)localIterator.next();
        if ((paramFolder == null) || (!paramFolder.equals(localFolder)))
          this.cachedDisplayableFolders.add(localFolder);
      }
    }
    return this.cachedDisplayableFolders;
  }

  public String getRawFoldersString()
  {
    return this.rawFolders;
  }

  public String getSnippet()
  {
    if ((this.conversationInfo != null) && (!TextUtils.isEmpty(this.conversationInfo.firstSnippet)))
      return this.conversationInfo.firstSnippet;
    return this.snippet;
  }

  public int hashCode()
  {
    return this.uri.hashCode();
  }

  public boolean isImportant()
  {
    return this.priority == 1;
  }

  public boolean isMostlyDead()
  {
    return (0x1 & this.convFlags) != 0;
  }

  public boolean isViewed()
  {
    return this.viewed;
  }

  public void markViewed()
  {
    this.viewed = true;
  }

  public void setRawFolders(String paramString)
  {
    clearCachedFolders();
    this.rawFolders = paramString;
  }

  public String toString()
  {
    return "[conversation id=" + this.id + ", subject =" + this.subject + "]";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    paramParcel.writeLong(this.id);
    paramParcel.writeParcelable(this.uri, paramInt);
    paramParcel.writeString(this.subject);
    paramParcel.writeLong(this.dateMs);
    paramParcel.writeString(this.snippet);
    int j;
    int k;
    label118: int m;
    label134: int n;
    label174: int i1;
    label190: int i2;
    if (this.hasAttachments)
    {
      j = i;
      paramParcel.writeInt(j);
      paramParcel.writeParcelable(this.messageListUri, 0);
      paramParcel.writeString(this.senders);
      paramParcel.writeInt(this.numMessages);
      paramParcel.writeInt(this.numDrafts);
      paramParcel.writeInt(this.sendingState);
      paramParcel.writeInt(this.priority);
      if (!this.read)
        break label268;
      k = i;
      paramParcel.writeInt(k);
      if (!this.starred)
        break label274;
      m = i;
      paramParcel.writeInt(m);
      paramParcel.writeString(this.rawFolders);
      paramParcel.writeInt(this.convFlags);
      paramParcel.writeInt(this.personalLevel);
      if (!this.spam)
        break label280;
      n = i;
      paramParcel.writeInt(n);
      if (!this.phishing)
        break label286;
      i1 = i;
      paramParcel.writeInt(i1);
      if (!this.muted)
        break label292;
      i2 = i;
      label206: paramParcel.writeInt(i2);
      paramParcel.writeInt(this.color);
      paramParcel.writeParcelable(this.accountUri, 0);
      paramParcel.writeString(ConversationInfo.toString(this.conversationInfo));
      paramParcel.writeParcelable(this.conversationBaseUri, 0);
      if (!this.isRemote)
        break label298;
    }
    while (true)
    {
      paramParcel.writeInt(i);
      return;
      j = 0;
      break;
      label268: k = 0;
      break label118;
      label274: m = 0;
      break label134;
      label280: n = 0;
      break label174;
      label286: i1 = 0;
      break label190;
      label292: i2 = 0;
      break label206;
      label298: i = 0;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.Conversation
 * JD-Core Version:    0.6.2
 */