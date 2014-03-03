package com.google.wireless.gdata2.data.batch;

import com.google.wireless.gdata2.data.Entry;

public class BatchUtils
{
  public static final String OPERATION_DELETE = "delete";
  public static final String OPERATION_INSERT = "insert";
  public static final String OPERATION_QUERY = "query";
  public static final String OPERATION_UPDATE = "update";

  public static String getBatchId(Entry paramEntry)
  {
    BatchInfo localBatchInfo = paramEntry.getBatchInfo();
    if (localBatchInfo == null)
      return null;
    return localBatchInfo.id;
  }

  public static BatchInterrupted getBatchInterrupted(Entry paramEntry)
  {
    BatchInfo localBatchInfo = paramEntry.getBatchInfo();
    if (localBatchInfo == null)
      return null;
    return localBatchInfo.interrupted;
  }

  public static String getBatchOperation(Entry paramEntry)
  {
    BatchInfo localBatchInfo = paramEntry.getBatchInfo();
    if (localBatchInfo == null)
      return null;
    return localBatchInfo.operation;
  }

  public static BatchStatus getBatchStatus(Entry paramEntry)
  {
    BatchInfo localBatchInfo = paramEntry.getBatchInfo();
    if (localBatchInfo == null)
      return null;
    return localBatchInfo.status;
  }

  private static BatchInfo getOrCreateBatchInfo(Entry paramEntry)
  {
    BatchInfo localBatchInfo = paramEntry.getBatchInfo();
    if (localBatchInfo == null)
    {
      localBatchInfo = new BatchInfo();
      paramEntry.setBatchInfo(localBatchInfo);
    }
    return localBatchInfo;
  }

  public static void setBatchId(Entry paramEntry, String paramString)
  {
    getOrCreateBatchInfo(paramEntry).id = paramString;
  }

  public static void setBatchInterrupted(Entry paramEntry, BatchInterrupted paramBatchInterrupted)
  {
    getOrCreateBatchInfo(paramEntry).interrupted = paramBatchInterrupted;
  }

  public static void setBatchOperation(Entry paramEntry, String paramString)
  {
    getOrCreateBatchInfo(paramEntry).operation = paramString;
  }

  public static void setBatchStatus(Entry paramEntry, BatchStatus paramBatchStatus)
  {
    getOrCreateBatchInfo(paramEntry).status = paramBatchStatus;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.data.batch.BatchUtils
 * JD-Core Version:    0.6.2
 */