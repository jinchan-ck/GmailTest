package com.google.android.gm.provider.uiprovider;

import android.database.Cursor;
import android.net.Uri;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Label;
import com.google.android.gm.provider.LogUtils;
import com.google.android.gm.provider.MailEngine;
import com.google.android.gm.provider.UiProvider;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class UILabelCursor extends UICursorWapper
{
  private static final Set<String> ALLOW_MARK_NOT_SPAM_LABELS = HIDDEN_REPORT_SPAM_LABELS;
  private static final Set<String> ARCHIVABLE_LABELS = ImmutableSet.of("^i", "^iim");
  private static final Set<String> DELETE_PROHIBITED_LABELS = ImmutableSet.of("^k", "^r");
  private static final Set<String> DESTRUCTIVE_MUTE_LABELS = ImmutableSet.of("^i", "^iim");
  private static final Set<String> HIDDEN_REPORT_PHISHING_LABELS = ImmutableSet.of("^s");
  private static final Set<String> HIDDEN_REPORT_SPAM_LABELS;
  private static final Set<String> SETTINGS_PROHIBITED_LABELS = ImmutableSet.of("^k", "^b", "^^out", "^r", "^all");
  private final String mAccount;
  private String mCanonicalName;
  private final int mCanonicalNameIndex;
  private Uri mConversationListUri;
  private final MailEngine mEngine;
  private String mGmailLabelColor;
  private final int mGmailLabelColorIndex;
  private final int mIdColumnIndex;
  private final int mNameColumnIndex;
  private final int mNumConversationsIndex;
  private final int mNumUnreadConversationsIndex;
  private final Set<String> mSynchronizedLabelSet;
  private final int mSystemLabelIndex;

  static
  {
    HIDDEN_REPORT_SPAM_LABELS = ImmutableSet.of("^s");
  }

  public UILabelCursor(Cursor paramCursor, MailEngine paramMailEngine, String paramString, String[] paramArrayOfString)
  {
    super(paramCursor, paramArrayOfString);
    this.mAccount = paramString;
    this.mEngine = paramMailEngine;
    if (this.mEngine != null);
    for (ImmutableSet localImmutableSet = ImmutableSet.copyOf(this.mEngine.getSynchronizedLabelSet()); ; localImmutableSet = null)
    {
      this.mSynchronizedLabelSet = localImmutableSet;
      this.mIdColumnIndex = paramCursor.getColumnIndex("_id");
      this.mNameColumnIndex = paramCursor.getColumnIndex("name");
      this.mCanonicalNameIndex = paramCursor.getColumnIndex("canonicalName");
      this.mNumConversationsIndex = paramCursor.getColumnIndex("numConversations");
      this.mNumUnreadConversationsIndex = paramCursor.getColumnIndex("numUnreadConversations");
      this.mSystemLabelIndex = paramCursor.getColumnIndex("systemLabel");
      this.mGmailLabelColorIndex = paramCursor.getColumnIndex("color");
      return;
    }
  }

  private void cachePositionValues()
  {
    int i;
    if (this.mCanonicalName == null)
    {
      this.mCanonicalName = super.getString(this.mCanonicalNameIndex);
      i = super.getInt(this.mIdColumnIndex);
      if (i == -1)
        break label58;
    }
    label58: for (this.mConversationListUri = UiProvider.getLabelConversationListUri(this.mAccount, i); ; this.mConversationListUri = UiProvider.getLabelConversationListFromNameUri(this.mAccount, this.mCanonicalName))
    {
      this.mGmailLabelColor = super.getString(this.mGmailLabelColorIndex);
      return;
    }
  }

  public int getInt(int paramInt)
  {
    int i = 1;
    cachePositionValues();
    int j = 0;
    switch (paramInt)
    {
    case 1:
    case 2:
    case 6:
    case 7:
    case 10:
    default:
      Object[] arrayOfObject = new Object[i];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      LogUtils.e("Gmail", "UILabelCursor.getInt(%d): Unexpected column", arrayOfObject);
      j = super.getInt(paramInt);
    case 3:
      return j;
    case 0:
      return this.mCanonicalName.hashCode();
    case 8:
      return super.getInt(this.mNumUnreadConversationsIndex);
    case 9:
      return super.getInt(this.mNumConversationsIndex);
    case 4:
      int i3 = 5;
      if (ARCHIVABLE_LABELS.contains(this.mCanonicalName))
        i3 |= 16;
      if (!SETTINGS_PROHIBITED_LABELS.contains(this.mCanonicalName))
        i3 |= 512;
      if (!DELETE_PROHIBITED_LABELS.contains(this.mCanonicalName))
        i3 |= 32;
      if (!HIDDEN_REPORT_SPAM_LABELS.contains(this.mCanonicalName))
        i3 |= 64;
      if (ALLOW_MARK_NOT_SPAM_LABELS.contains(this.mCanonicalName))
        i3 |= 128;
      if (!HIDDEN_REPORT_PHISHING_LABELS.contains(this.mCanonicalName))
        i3 |= 8192;
      if (DESTRUCTIVE_MUTE_LABELS.contains(this.mCanonicalName))
        i3 |= 256;
      if (Gmail.isLabelUserSettable(this.mCanonicalName))
        i3 |= 8;
      if (("^im".equals(this.mCanonicalName)) || ("^iim".equals(this.mCanonicalName)))
        i3 |= 1024;
      return i3;
    case 5:
      int i2;
      if ((this.mSynchronizedLabelSet != null) && (this.mSynchronizedLabelSet.contains(this.mCanonicalName)))
      {
        i2 = i;
        if (i2 == 0)
          break label390;
      }
      while (true)
      {
        return i;
        i2 = 0;
        break;
        i = 0;
      }
    case 11:
      MailEngine localMailEngine = this.mEngine;
      int i1 = 0;
      if (localMailEngine != null)
      {
        boolean bool = this.mEngine.isBackgroundSyncInProgress();
        i1 = 0;
        if (bool)
          i1 = 0x0 | 0x4;
        if (this.mEngine.isLiveQueryInProgress())
          i1 |= 2;
        if (this.mEngine.isHandlingUserRefresh())
          i1 |= 1;
      }
      return i1;
    case 12:
      label390: if (this.mEngine != null);
      for (int n = this.mEngine.getLastSyncResult(); ; n = 0)
        return n;
    case 13:
    }
    if (super.getInt(this.mSystemLabelIndex) != 0);
    int m;
    for (int k = i; ; m = 0)
      return UiProvider.getFolderType(k, this.mCanonicalName);
  }

  public long getLong(int paramInt)
  {
    cachePositionValues();
    switch (paramInt)
    {
    default:
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      LogUtils.e("Gmail", "UILabelCursor.getLong(%d): Unexpected column", arrayOfObject);
      return super.getLong(paramInt);
    case 0:
      return this.mCanonicalName.hashCode();
    case 14:
    }
    return 0L;
  }

  public String getString(int paramInt)
  {
    cachePositionValues();
    String str = null;
    switch (paramInt)
    {
    case 3:
    case 4:
    case 5:
    case 8:
    case 9:
    case 11:
    case 12:
    case 13:
    case 14:
    default:
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      LogUtils.e("Gmail", "UILabelCursor.getString(%d): Unexpected column", arrayOfObject);
      str = super.getString(paramInt);
    case 7:
    case 17:
    case 18:
      return str;
    case 1:
      return UiProvider.getAccountLabelUri(this.mAccount, this.mCanonicalName).toString();
    case 2:
      return super.getString(this.mNameColumnIndex);
    case 6:
      return this.mConversationListUri.toString();
    case 10:
      return UiProvider.getLabelRefreshUri(this.mAccount, this.mConversationListUri).toString();
    case 15:
      return Label.getBackgroundColor(this.mAccount, this.mCanonicalName, this.mGmailLabelColor) + "";
    case 16:
    }
    return Label.getTextColor(this.mAccount, this.mCanonicalName, this.mGmailLabelColor) + "";
  }

  protected void resetCursorRowState()
  {
    super.resetCursorRowState();
    this.mCanonicalName = null;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.uiprovider.UILabelCursor
 * JD-Core Version:    0.6.2
 */