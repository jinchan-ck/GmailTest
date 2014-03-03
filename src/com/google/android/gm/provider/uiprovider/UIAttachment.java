package com.google.android.gm.provider.uiprovider;

import android.net.Uri;
import com.google.android.gm.provider.Gmail.Attachment;
import com.google.android.gm.provider.Gmail.AttachmentOrigin;
import java.util.List;

public abstract interface UIAttachment
{
  public abstract boolean downloadCompletedSuccessfully();

  public abstract String getContentType();

  public abstract int getDestination();

  public abstract int getDownloadedSize();

  public abstract Uri getExternalFilePath();

  public abstract String getJoinedAttachmentInfo();

  public abstract String getName();

  public abstract Gmail.AttachmentOrigin getOrigin();

  public abstract String getOriginExtras();

  public abstract Gmail.Attachment getOriginal();

  public abstract String getPartId();

  public abstract int getSize();

  public abstract int getStatus();

  public abstract boolean isDownloading();

  public abstract boolean supportsOrigin();

  public abstract void updateState(int paramInt1, int paramInt2, String paramString);

  public static class UriParser
  {
    public final String mAccount;
    public final List<String> mContentTypeQueryParameters;
    public final long mConversationId;
    public final long mLocalMessageId;
    public final long mMessageId;
    public final String mPartId;

    private UriParser(String paramString1, long paramLong1, long paramLong2, long paramLong3, String paramString2, List<String> paramList)
    {
      this.mAccount = paramString1;
      this.mConversationId = paramLong1;
      this.mMessageId = paramLong2;
      this.mLocalMessageId = paramLong3;
      this.mPartId = paramString2;
      this.mContentTypeQueryParameters = paramList;
    }

    public static UriParser parse(Uri paramUri)
    {
      List localList1 = paramUri.getPathSegments();
      String str1 = (String)localList1.get(0);
      List localList2 = paramUri.getQueryParameters("contentType");
      long l1 = Long.parseLong((String)localList1.get(2));
      long l2 = Long.parseLong((String)localList1.get(3));
      if (localList1.size() >= 5);
      for (String str2 = (String)localList1.get(4); ; str2 = null)
        return new UriParser(str1, l1, Long.parseLong(paramUri.getQueryParameter("serverMessageId")), l2, str2, localList2);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.uiprovider.UIAttachment
 * JD-Core Version:    0.6.2
 */