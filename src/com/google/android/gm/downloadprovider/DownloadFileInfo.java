package com.google.android.gm.downloadprovider;

import java.io.FileOutputStream;

public class DownloadFileInfo
{
  String mFileName;
  int mStatus;
  FileOutputStream mStream;

  public DownloadFileInfo(String paramString, FileOutputStream paramFileOutputStream, int paramInt)
  {
    this.mFileName = paramString;
    this.mStream = paramFileOutputStream;
    this.mStatus = paramInt;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.downloadprovider.DownloadFileInfo
 * JD-Core Version:    0.6.2
 */