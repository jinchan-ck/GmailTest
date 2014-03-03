package com.google.android.gm.provider;

import android.text.TextUtils;

class MessageLabelRecord
{
  private final Long mDateReceived;
  private final String mJoinedLabelIds;
  private final long mMessageId;

  public MessageLabelRecord(String paramString, long paramLong1, long paramLong2)
  {
    this.mJoinedLabelIds = paramString;
    this.mMessageId = paramLong1;
    this.mDateReceived = Long.valueOf(paramLong2);
  }

  public MessageLabelRecord(String paramString, Long paramLong)
  {
    this.mJoinedLabelIds = paramString;
    this.mMessageId = paramLong.longValue();
    this.mDateReceived = null;
  }

  Long getDateReceived()
  {
    return this.mDateReceived;
  }

  String[] getLabelIds()
  {
    if (this.mJoinedLabelIds != null)
      return TextUtils.split(this.mJoinedLabelIds, Gmail.COMMA_SEPARATOR_PATTERN);
    return new String[0];
  }

  long getMessageId()
  {
    return this.mMessageId;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.MessageLabelRecord
 * JD-Core Version:    0.6.2
 */