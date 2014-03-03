package com.google.android.gm.downloadprovider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.util.Random;

public class DownloadInfo
{
  public String mClass;
  public int mControl;
  public String mCookies;
  public int mCurrentBytes;
  public int mDestination;
  public String mETag;
  public String mExtras;
  public String mFileName;
  public int mFuzz;
  public volatile boolean mHasActiveThread;
  public String mHint;
  public int mId;
  public long mLastMod;
  public boolean mMediaScanned;
  public String mMimeType;
  public boolean mNoIntegrity;
  public int mNumFailed;
  public String mPackage;
  public int mRedirectCount;
  public String mReferer;
  public int mRetryAfter;
  public int mStatus;
  public int mTotalBytes;
  public String mUri;
  public String mUserAgent;
  public int mVisibility;

  public DownloadInfo(int paramInt1, String paramString1, boolean paramBoolean1, String paramString2, String paramString3, String paramString4, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, int paramInt9, int paramInt10, String paramString11, boolean paramBoolean2)
  {
    this.mId = paramInt1;
    this.mUri = paramString1;
    this.mNoIntegrity = paramBoolean1;
    this.mHint = paramString2;
    this.mFileName = paramString3;
    this.mMimeType = paramString4;
    this.mDestination = paramInt2;
    this.mVisibility = paramInt3;
    this.mControl = paramInt4;
    this.mStatus = paramInt5;
    this.mNumFailed = paramInt6;
    this.mRetryAfter = paramInt7;
    this.mRedirectCount = paramInt8;
    this.mLastMod = paramLong;
    this.mPackage = paramString5;
    this.mClass = paramString6;
    this.mExtras = paramString7;
    this.mCookies = paramString8;
    this.mUserAgent = paramString9;
    this.mReferer = paramString10;
    this.mTotalBytes = paramInt9;
    this.mCurrentBytes = paramInt10;
    this.mETag = paramString11;
    this.mMediaScanned = paramBoolean2;
    this.mFuzz = Helpers.sRandom.nextInt(1001);
  }

  public boolean canUseNetwork(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!paramBoolean1)
      return false;
    if (this.mDestination == 3)
      return !paramBoolean2;
    return true;
  }

  public boolean hasCompletionNotification()
  {
    if (!Downloads.Impl.isStatusCompleted(this.mStatus))
      return false;
    return this.mVisibility == 1;
  }

  public boolean isReadyToRestart(long paramLong)
  {
    if (this.mControl == 1)
      return false;
    if (this.mStatus == 0)
      return true;
    if (this.mStatus == 190)
      return true;
    if (this.mStatus == 193)
    {
      if (this.mNumFailed == 0)
        return true;
      if (restartTime() < paramLong)
        return true;
    }
    return false;
  }

  public boolean isReadyToStart(long paramLong)
  {
    if (this.mControl == 1)
      return false;
    if (this.mStatus == 0)
      return true;
    if (this.mStatus == 190)
      return true;
    if (this.mStatus == 192)
      return true;
    if (this.mStatus == 193)
    {
      if (this.mNumFailed == 0)
        return true;
      if (restartTime() < paramLong)
        return true;
    }
    return false;
  }

  public long restartTime()
  {
    if (this.mRetryAfter > 0)
      return this.mLastMod + this.mRetryAfter;
    return this.mLastMod + 30 * (1000 + this.mFuzz) * (1 << this.mNumFailed - 1);
  }

  public void sendIntentIfRequested(Uri paramUri, Context paramContext)
  {
    if ((this.mPackage != null) && (this.mClass != null))
    {
      Intent localIntent = new Intent("android.intent.action.DOWNLOAD_COMPLETED");
      localIntent.setClassName(this.mPackage, this.mClass);
      if (this.mExtras != null)
        localIntent.putExtra("notificationextras", this.mExtras);
      localIntent.setData(paramUri);
      paramContext.sendBroadcast(localIntent);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.downloadprovider.DownloadInfo
 * JD-Core Version:    0.6.2
 */