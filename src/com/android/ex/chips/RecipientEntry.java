package com.android.ex.chips;

import android.net.Uri;

public class RecipientEntry
{
  private final long mContactId;
  private final long mDataId;
  private final String mDestination;
  private final String mDestinationLabel;
  private final int mDestinationType;
  private final String mDisplayName;
  private final int mEntryType;
  private final boolean mIsDivider;
  private boolean mIsFirstLevel;
  private byte[] mPhotoBytes;
  private final Uri mPhotoThumbnailUri;

  private RecipientEntry(int paramInt1, String paramString1, String paramString2, int paramInt2, String paramString3, long paramLong1, long paramLong2, Uri paramUri, boolean paramBoolean)
  {
    this.mEntryType = paramInt1;
    this.mIsFirstLevel = paramBoolean;
    this.mDisplayName = paramString1;
    this.mDestination = paramString2;
    this.mDestinationType = paramInt2;
    this.mDestinationLabel = paramString3;
    this.mContactId = paramLong1;
    this.mDataId = paramLong2;
    this.mPhotoThumbnailUri = paramUri;
    this.mPhotoBytes = null;
    this.mIsDivider = false;
  }

  public static RecipientEntry constructFakeEntry(String paramString)
  {
    return new RecipientEntry(0, paramString, paramString, -1, null, -1L, -1L, null, true);
  }

  public static RecipientEntry constructGeneratedEntry(String paramString1, String paramString2)
  {
    return new RecipientEntry(0, paramString1, paramString2, -1, null, -2L, -2L, null, true);
  }

  public static RecipientEntry constructSecondLevelEntry(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, long paramLong1, long paramLong2, String paramString4)
  {
    String str = pickDisplayName(paramInt1, paramString1, paramString2);
    if (paramString4 != null);
    for (Uri localUri = Uri.parse(paramString4); ; localUri = null)
      return new RecipientEntry(0, str, paramString2, paramInt2, paramString3, paramLong1, paramLong2, localUri, false);
  }

  public static RecipientEntry constructTopLevelEntry(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, long paramLong1, long paramLong2, String paramString4)
  {
    String str = pickDisplayName(paramInt1, paramString1, paramString2);
    if (paramString4 != null);
    for (Uri localUri = Uri.parse(paramString4); ; localUri = null)
      return new RecipientEntry(0, str, paramString2, paramInt2, paramString3, paramLong1, paramLong2, localUri, true);
  }

  public static boolean isCreatedRecipient(long paramLong)
  {
    return (paramLong == -1L) || (paramLong == -2L);
  }

  private static String pickDisplayName(int paramInt, String paramString1, String paramString2)
  {
    if (paramInt > 20)
      return paramString1;
    return paramString2;
  }

  public long getContactId()
  {
    return this.mContactId;
  }

  public long getDataId()
  {
    return this.mDataId;
  }

  public String getDestination()
  {
    return this.mDestination;
  }

  public String getDestinationLabel()
  {
    return this.mDestinationLabel;
  }

  public int getDestinationType()
  {
    return this.mDestinationType;
  }

  public String getDisplayName()
  {
    return this.mDisplayName;
  }

  public int getEntryType()
  {
    return this.mEntryType;
  }

  public byte[] getPhotoBytes()
  {
    try
    {
      byte[] arrayOfByte = this.mPhotoBytes;
      return arrayOfByte;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public Uri getPhotoThumbnailUri()
  {
    return this.mPhotoThumbnailUri;
  }

  public boolean isFirstLevel()
  {
    return this.mIsFirstLevel;
  }

  public boolean isSelectable()
  {
    return this.mEntryType == 0;
  }

  public void setPhotoBytes(byte[] paramArrayOfByte)
  {
    try
    {
      this.mPhotoBytes = paramArrayOfByte;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.chips.RecipientEntry
 * JD-Core Version:    0.6.2
 */