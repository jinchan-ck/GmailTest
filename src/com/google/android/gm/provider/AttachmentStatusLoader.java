package com.google.android.gm.provider;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.Loader.ForceLoadContentObserver;
import android.content.Loader.OnLoadCompleteListener;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gm.provider.uiprovider.UIAttachment;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttachmentStatusLoader extends Loader<List<Result>>
{
  private static final String[] ATTACHMENT_STATUS_LOADER_PROJECTION = { "messages_messageId", "messages_partId", "downloadId", "mimeType", "saveToSd", "filename", "status", "desiredRendition" };
  private final String mAccount;
  private final Loader<Cursor> mAttachmentLoader;
  private List<Result> mAttachments;
  private final long mConversationId;
  private final DownloadStatusLoader mDownloadLoader;
  private final Map<Long, Result> mDownloadMap;

  public AttachmentStatusLoader(Context paramContext, String paramString, long paramLong, DownloadManager paramDownloadManager)
  {
    super(paramContext);
    this.mAccount = paramString;
    this.mConversationId = paramLong;
    this.mDownloadMap = Maps.newHashMap();
    this.mAttachmentLoader = new CursorLoader(paramContext, Gmail.getAttachmentsForConversationUri(this.mAccount, this.mConversationId), ATTACHMENT_STATUS_LOADER_PROJECTION, null, null, null);
    this.mAttachmentLoader.registerListener(this.mAttachmentLoader.getId(), new AttachmentListener(null));
    this.mDownloadLoader = new DownloadStatusLoader(paramContext, paramDownloadManager);
    this.mDownloadLoader.registerListener(this.mAttachmentLoader.getId(), new DownloadListener(null));
  }

  protected void onReset()
  {
    this.mAttachmentLoader.reset();
    this.mDownloadLoader.reset();
    this.mAttachments = null;
  }

  protected void onStartLoading()
  {
    this.mAttachmentLoader.startLoading();
  }

  protected void onStopLoading()
  {
    this.mAttachmentLoader.stopLoading();
    this.mDownloadLoader.stopLoading();
  }

  private class AttachmentListener
    implements Loader.OnLoadCompleteListener<Cursor>
  {
    private AttachmentListener()
    {
    }

    public void onLoadComplete(Loader<Cursor> paramLoader, Cursor paramCursor)
    {
      AttachmentStatusLoader.access$202(AttachmentStatusLoader.this, Lists.newArrayList());
      AttachmentStatusLoader.this.mDownloadMap.clear();
      int i = -1;
      i++;
      if (paramCursor.moveToPosition(i))
      {
        long l1 = paramCursor.getLong(0);
        String str1 = paramCursor.getString(1);
        long l2 = paramCursor.getLong(2);
        String str2 = paramCursor.getString(3);
        if (paramCursor.getInt(4) != 0);
        for (boolean bool = true; ; bool = false)
        {
          String str3 = paramCursor.getString(5);
          int j = paramCursor.getInt(6);
          String str4 = paramCursor.getString(7);
          AttachmentStatusLoader.Result localResult = new AttachmentStatusLoader.Result(AttachmentStatusLoader.this.mConversationId, l1, str1, bool, str3, str2, j, str4);
          int k = AttachmentStatusLoader.this.mAttachments.indexOf(localResult);
          if ((k >= 0) && (!((AttachmentStatusLoader.Result)AttachmentStatusLoader.this.mAttachments.get(k)).saveToSd))
            AttachmentStatusLoader.this.mAttachments.remove(k);
          AttachmentStatusLoader.this.mAttachments.add(localResult);
          if ((l2 < 0L) || ((AttachmentStatusLoader.this.mDownloadMap.containsKey(Long.valueOf(l2))) && (!bool)))
            break;
          AttachmentStatusLoader.this.mDownloadMap.put(Long.valueOf(l2), localResult);
          break;
        }
      }
      if (AttachmentStatusLoader.this.mDownloadMap.isEmpty())
      {
        if (!AttachmentStatusLoader.this.mAttachments.isEmpty())
          AttachmentStatusLoader.this.deliverResult(AttachmentStatusLoader.this.mAttachments);
        return;
      }
      AttachmentStatusLoader.this.mDownloadLoader.reset();
      AttachmentStatusLoader.this.mDownloadLoader.setIds(AttachmentStatusLoader.this.mDownloadMap.keySet());
      AttachmentStatusLoader.this.mDownloadLoader.startLoading();
    }
  }

  private class DownloadListener
    implements Loader.OnLoadCompleteListener<Cursor>
  {
    private DownloadListener()
    {
    }

    public void onLoadComplete(Loader<Cursor> paramLoader, Cursor paramCursor)
    {
      if (paramCursor == null)
        return;
      AttachmentStatusLoader.access$202(AttachmentStatusLoader.this, Lists.newArrayList(AttachmentStatusLoader.this.mAttachments));
      int i = paramCursor.getColumnIndexOrThrow("_id");
      int j = paramCursor.getColumnIndexOrThrow("bytes_so_far");
      int k = paramCursor.getColumnIndexOrThrow("status");
      int m = -1;
      while (true)
      {
        m++;
        if (!paramCursor.moveToPosition(m))
          break;
        long l1 = paramCursor.getLong(i);
        long l2 = paramCursor.getLong(j);
        int n = paramCursor.getInt(k);
        if (AttachmentStatusLoader.this.mDownloadMap.containsKey(Long.valueOf(l1)))
        {
          ((AttachmentStatusLoader.Result)AttachmentStatusLoader.this.mDownloadMap.get(Long.valueOf(l1))).downloadedSize = ((int)l2);
          ((AttachmentStatusLoader.Result)AttachmentStatusLoader.this.mDownloadMap.get(Long.valueOf(l1))).downloadStatus = n;
        }
      }
      AttachmentStatusLoader.this.deliverResult(AttachmentStatusLoader.this.mAttachments);
    }
  }

  private class DownloadStatusLoader extends CursorLoader
  {
    private long[] mDownloadIds;
    private final DownloadManager mDownloadManager;
    private final ContentObserver mObserver;

    public DownloadStatusLoader(Context paramDownloadManager, DownloadManager arg3)
    {
      super();
      Object localObject;
      this.mDownloadManager = localObject;
      this.mObserver = new Loader.ForceLoadContentObserver(this);
    }

    public Cursor loadInBackground()
    {
      Cursor localCursor = this.mDownloadManager.query(new DownloadManager.Query().setFilterById(this.mDownloadIds));
      if (localCursor != null)
        localCursor.registerContentObserver(this.mObserver);
      return localCursor;
    }

    public void setIds(Collection<Long> paramCollection)
    {
      this.mDownloadIds = new long[paramCollection.size()];
      int i = 0;
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        long l = ((Long)localIterator.next()).longValue();
        this.mDownloadIds[i] = l;
        i++;
      }
    }
  }

  public static class Result
    implements UIAttachment
  {
    public String contentType;
    public final long conversationId;
    public int downloadStatus;
    public int downloadedSize;
    public final String fileName;
    public Gmail.Attachment mOriginalAttachment;
    public final long messageId;
    public final String partId;
    public final Gmail.AttachmentRendition rendition;
    public final boolean saveToSd;
    public int size;
    public final int state;
    public final int status;
    private Integer transientDestination;
    private String transientSavedFileUri;
    private Integer transientStatus;

    public Result(long paramLong1, long paramLong2, String paramString1, boolean paramBoolean, String paramString2, String paramString3, int paramInt, String paramString4)
    {
      this.conversationId = paramLong1;
      this.messageId = paramLong2;
      this.partId = paramString1;
      this.saveToSd = paramBoolean;
      this.fileName = paramString2;
      this.contentType = paramString3;
      this.status = paramInt;
      this.rendition = Gmail.AttachmentRendition.valueOf(paramString4);
      if ((AttachmentManager.isStatusSuccess(paramInt)) && (AttachmentManager.isDownloadStillPresent(paramString2)))
      {
        if (paramBoolean);
        for (int i = 3; ; i = 2)
        {
          this.state = i;
          return;
        }
      }
      if (AttachmentManager.isStatusRunning(paramInt))
      {
        this.state = 1;
        return;
      }
      this.state = 0;
    }

    private boolean updatedToCompleteState()
    {
      return (!TextUtils.isEmpty(this.transientSavedFileUri)) && (this.transientStatus != null) && (this.transientStatus.intValue() == 3);
    }

    public boolean downloadCompletedSuccessfully()
    {
      return getStatus() == 3;
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this);
      Result localResult;
      do
      {
        return true;
        if ((paramObject == null) || (paramObject.getClass() != getClass()))
          return false;
        localResult = (Result)paramObject;
      }
      while ((localResult.messageId == this.messageId) && (localResult.partId.equals(this.partId)));
      return false;
    }

    public String getContentType()
    {
      return this.contentType;
    }

    public int getDestination()
    {
      if (this.transientDestination != null)
        return this.transientDestination.intValue();
      if (this.saveToSd)
        return 1;
      return 0;
    }

    public int getDownloadedSize()
    {
      return this.downloadedSize;
    }

    public Uri getExternalFilePath()
    {
      if (isDownloadedToSd())
        return Uri.parse(this.fileName);
      if ((getStatus() == 3) && (getDestination() == 1) && (this.transientSavedFileUri != null))
        return Uri.parse(this.transientSavedFileUri);
      return null;
    }

    public String getJoinedAttachmentInfo()
    {
      if (this.mOriginalAttachment != null)
        return this.mOriginalAttachment.toJoinedString();
      return null;
    }

    public String getName()
    {
      if (this.mOriginalAttachment != null)
      {
        String str = this.mOriginalAttachment.getName();
        if (!TextUtils.isEmpty(str))
          return str;
      }
      return Uri.parse(this.fileName).getLastPathSegment();
    }

    public Gmail.AttachmentOrigin getOrigin()
    {
      return null;
    }

    public String getOriginExtras()
    {
      return null;
    }

    public Gmail.Attachment getOriginal()
    {
      return this.mOriginalAttachment;
    }

    public String getPartId()
    {
      return this.partId;
    }

    public int getSize()
    {
      if ((this.mOriginalAttachment != null) && (this.mOriginalAttachment.getSize() != 0))
        return this.mOriginalAttachment.getSize();
      LogUtils.e("Gmail", "Returning inaccurate attachment size", new Object[0]);
      return this.downloadedSize;
    }

    public int getStatus()
    {
      if (isStatusError())
        return 1;
      if (this.state == 1)
        return 2;
      if ((this.transientStatus != null) && (((AttachmentManager.isStatusSuccess(this.status)) && (!AttachmentManager.isDownloadStillPresent(this.fileName))) || (AttachmentManager.isStatusPending(this.status)) || ((this.transientStatus.intValue() == 2) && ((AttachmentManager.isStatusError(this.status)) || (AttachmentManager.isStatusSuccess(this.status))))))
        return this.transientStatus.intValue();
      if ((isDownloadedToSd()) || (isDownloadedToCache()) || (updatedToCompleteState()))
        return 3;
      return 0;
    }

    public int hashCode()
    {
      return (int)(this.messageId ^ this.partId.hashCode());
    }

    public boolean isDownloadedToCache()
    {
      return this.state == 2;
    }

    public boolean isDownloadedToSd()
    {
      return this.state == 3;
    }

    public boolean isDownloading()
    {
      return getStatus() == 2;
    }

    public boolean isStatusError()
    {
      if (this.transientStatus != null);
      for (int i = this.transientStatus.intValue(); ; i = this.status)
        return AttachmentManager.isStatusError(i);
    }

    public boolean isStatusPaused()
    {
      return (AttachmentManager.isStatusPaused(this.status)) || (this.downloadStatus == 4);
    }

    public boolean isStatusPending()
    {
      return AttachmentManager.isStatusPending(this.status);
    }

    public boolean isStatusSuccess()
    {
      if (this.transientStatus != null);
      for (int i = this.transientStatus.intValue(); ; i = this.status)
        return AttachmentManager.isStatusSuccess(i);
    }

    public boolean isStatusValid()
    {
      return AttachmentManager.isStatusValid(this.status);
    }

    public boolean supportsOrigin()
    {
      return false;
    }

    public String toString()
    {
      Object[] arrayOfObject = new Object[11];
      arrayOfObject[0] = Long.valueOf(this.conversationId);
      arrayOfObject[1] = Long.valueOf(this.messageId);
      arrayOfObject[2] = this.partId;
      arrayOfObject[3] = this.fileName;
      arrayOfObject[4] = Integer.valueOf(this.status);
      arrayOfObject[5] = Integer.valueOf(this.state);
      arrayOfObject[6] = Integer.valueOf(this.size);
      arrayOfObject[7] = Integer.valueOf(this.downloadedSize);
      arrayOfObject[8] = this.transientStatus;
      arrayOfObject[9] = this.transientDestination;
      arrayOfObject[10] = this.transientSavedFileUri;
      return String.format("Result: conv=%d msg=%d part=%s fname=%s status=%d state=%d size=%d dled=%d transientStatus=%s, transientDestination=%s, transientFile=%s", arrayOfObject);
    }

    public void updateState(int paramInt1, int paramInt2, String paramString)
    {
      this.transientStatus = Integer.valueOf(paramInt1);
      this.transientDestination = Integer.valueOf(paramInt2);
      this.transientSavedFileUri = paramString;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.AttachmentStatusLoader
 * JD-Core Version:    0.6.2
 */