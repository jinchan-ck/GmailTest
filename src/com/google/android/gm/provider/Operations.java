package com.google.android.gm.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import com.google.android.gsf.Gservices;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Operations
{
  private static final Integer NUM_RETRY_UPHILL_OPS = Integer.valueOf(20);
  private final String[] PROJECTION_PROVIDE_OPERATIONS = { "_id", "action", "message_messageId", "value1", "value2", "numAttempts", "nextTimeToAttempt", "delay" };
  private Context mContext;
  private final SQLiteDatabase mDb;

  public Operations(Context paramContext, SQLiteDatabase paramSQLiteDatabase)
  {
    this.mDb = paramSQLiteDatabase;
    this.mContext = paramContext;
  }

  private boolean calculateAndUpdateOpDelay(long paramLong1, long paramLong2, int paramInt1, int paramInt2, long paramLong3, OperationInfo paramOperationInfo, MailEngine.SyncInfo paramSyncInfo, MailEngine paramMailEngine)
  {
    if (Gservices.getInt(this.mContext.getContentResolver(), "gmail_delay_bad_op", 1) != 0);
    for (int i = 1; i == 0; i = 0)
      return true;
    Object[] arrayOfObject1 = new Object[5];
    arrayOfObject1[0] = Long.valueOf(paramLong1);
    arrayOfObject1[1] = Long.valueOf(paramLong3);
    arrayOfObject1[2] = Integer.valueOf(paramInt1);
    arrayOfObject1[3] = Integer.valueOf(paramInt2);
    arrayOfObject1[4] = paramSyncInfo;
    LogUtils.d("Gmail", "calculateAndUpdateOpDelay: currentTime = %d, nextTimeToAttempt = %d, numAttempts = %d delay=%d %s", arrayOfObject1);
    if (paramLong3 > paramLong1)
    {
      moveOperationToEnd(paramLong2, paramOperationInfo, paramSyncInfo, paramInt1, paramLong3, paramInt2);
      return false;
    }
    if ((!paramSyncInfo.receivedHandledClientOp) && (paramInt1 > 0))
    {
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Long.valueOf(paramLong2);
      LogUtils.i("Gmail", "Not retrying this operation id %d as we have not received what client operations the server has handled.", arrayOfObject3);
      paramMailEngine.mMailSync.setBooleanSetting("unackedSentOperations", true);
      paramMailEngine.mMailSync.saveDirtySettings();
      moveOperationToEnd(paramLong2, paramOperationInfo, paramSyncInfo, paramInt1, paramLong3, paramInt2);
      return false;
    }
    if (paramInt1 >= 3)
    {
      if (paramInt2 == 0);
      for (int j = 30; ; j = Math.min(86400, paramInt2 * 2))
      {
        long l1 = paramLong1 + j;
        long l2 = moveOperationToEnd(paramLong2, paramOperationInfo, paramSyncInfo, 2, l1, j);
        Object[] arrayOfObject2 = new Object[5];
        arrayOfObject2[0] = Long.valueOf(paramLong2);
        arrayOfObject2[1] = Integer.valueOf(2);
        arrayOfObject2[2] = Integer.valueOf(j);
        arrayOfObject2[3] = Long.valueOf(l1);
        arrayOfObject2[4] = Long.valueOf(l2);
        LogUtils.i("Gmail", "Backing off operation %d with newAttempts %d, delay %d, newBackOffTime %d, newOpId %d", arrayOfObject2);
        return false;
      }
    }
    String[] arrayOfString = new String[2];
    arrayOfString[0] = Integer.toString(paramInt1 + 1);
    arrayOfString[1] = Long.toString(paramLong2);
    this.mDb.execSQL("UPDATE operations SET numAttempts = ? WHERE _id = ?", arrayOfString);
    return true;
  }

  private void checkForMessageToDiscard(MailEngine paramMailEngine)
  {
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    Integer localInteger = Integer.valueOf(Gservices.getInt(localContentResolver, "gmail_num_retry_uphill_op", NUM_RETRY_UPHILL_OPS.intValue()));
    int i = nextOperationId();
    int j = paramMailEngine.mMailSync.getIntegerSetting("nextUnackedSentOp");
    int k = paramMailEngine.mMailSync.getIntegerSetting("errorCountNextUnackedSentOp");
    long l1 = paramMailEngine.mMailSync.getLongSetting("nextUnackedOpWriteTime");
    long l2 = System.currentTimeMillis() / 1000L;
    long l3 = Gservices.getLong(localContentResolver, "gmail_wait_time_retry_uphill_op", 36400L);
    if ((i == j) && (k >= localInteger.intValue()) && (l2 - l1 > l3))
      this.mDb.delete("operations", "_id == " + i, null);
  }

  private boolean doesLabelMatter(String paramString)
  {
    return (!"messageSaved".equals(paramString)) && (!"messageSent".equals(paramString)) && (!"messageExpunged".equals(paramString));
  }

  private long moveOperationToEnd(long paramLong1, OperationInfo paramOperationInfo, MailEngine.SyncInfo paramSyncInfo, int paramInt1, long paramLong2, int paramInt2)
  {
    String str = paramOperationInfo.mAction;
    long l;
    if (paramSyncInfo.normalSync)
    {
      SQLiteDatabase localSQLiteDatabase2 = this.mDb;
      String[] arrayOfString2 = new String[1];
      arrayOfString2[0] = Long.toString(paramLong1);
      localSQLiteDatabase2.execSQL("DELETE FROM operations where _id = ?", arrayOfString2);
      if (doesLabelMatter(str))
        l = recordOperationWithLabelId(paramOperationInfo.mConversationId, paramOperationInfo.mMessageId, paramOperationInfo.mAction, paramOperationInfo.mLabelId, paramInt1, paramInt2, paramLong2);
    }
    while (true)
    {
      Object[] arrayOfObject = new Object[5];
      arrayOfObject[0] = Long.valueOf(paramLong1);
      arrayOfObject[1] = Integer.valueOf(paramInt1);
      arrayOfObject[2] = Integer.valueOf(paramInt2);
      arrayOfObject[3] = Long.valueOf(paramLong2);
      arrayOfObject[4] = Long.valueOf(l);
      LogUtils.i("Gmail", "Moving delayed operation %d to end of list with newAttempts %d, delay %d, newBackOffTime %d, newOpId %d", arrayOfObject);
      return l;
      l = recordOperation(paramOperationInfo.mConversationId, paramOperationInfo.mMessageId, paramOperationInfo.mAction, paramInt1, paramInt2, paramLong2);
      continue;
      SQLiteDatabase localSQLiteDatabase1 = this.mDb;
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = Long.toString(paramLong1);
      localSQLiteDatabase1.execSQL("DELETE FROM operations where _id = ?", arrayOfString1);
      l = incrementAndAddOperations(new OperationInfo(paramSyncInfo.conversationId, paramSyncInfo.messageId, paramOperationInfo.mAction, 0L, paramInt1, paramInt2, paramLong2));
    }
  }

  public static void updateLabelId(SQLiteDatabase paramSQLiteDatabase, long paramLong1, long paramLong2)
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = Long.toString(paramLong2);
    arrayOfString[1] = Long.toString(paramLong1);
    paramSQLiteDatabase.execSQL("UPDATE operations SET value1 = ? WHERE action IN ('messageLabelAdded', 'messageLabelRemoved', 'conversationLabelAdded', 'conversationLabelRemoved') AND value1 = ?", arrayOfString);
  }

  public void deleteOperationsForLabelId(long paramLong)
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong);
    this.mDb.delete("operations", "action IN ('messageLabelAdded', 'messageLabelRemoved', 'conversationLabelAdded', 'conversationLabelRemoved') AND value1 = ?", arrayOfString);
  }

  public void deleteOperationsForMessageId(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong);
    localSQLiteDatabase.delete("operations", "message_messageId = ?", arrayOfString);
  }

  public void deleteOperationsForMessageIds(List<Long> paramList)
  {
    String str = TextUtils.join(", ", paramList);
    this.mDb.delete("operations", "message_messageId IN (" + str + ")", null);
  }

  public boolean hasUnackedSendOrSaveOperationsForConversation(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong);
    return 0L != DatabaseUtils.longForQuery(localSQLiteDatabase, "SELECT COUNT(*) FROM operations WHERE ACTION IN ('messageSaved', 'messageSent') AND value2 = ?", arrayOfString);
  }

  public long incrementAndAddOperations(OperationInfo paramOperationInfo)
  {
    this.mDb.beginTransactionNonExclusive();
    ArrayList localArrayList;
    Cursor localCursor;
    try
    {
      localArrayList = new ArrayList();
      SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
      localSQLiteQueryBuilder.setTables("operations");
      localCursor = localSQLiteQueryBuilder.query(this.mDb, this.PROJECTION_PROVIDE_OPERATIONS, null, null, null, null, "_id");
      int i = localCursor.getColumnIndexOrThrow("_id");
      int j = localCursor.getColumnIndexOrThrow("action");
      int k = localCursor.getColumnIndexOrThrow("message_messageId");
      int m = localCursor.getColumnIndexOrThrow("value1");
      int n = localCursor.getColumnIndexOrThrow("value2");
      int i1 = localCursor.getColumnIndexOrThrow("numAttempts");
      int i2 = localCursor.getColumnIndexOrThrow("nextTimeToAttempt");
      int i3 = localCursor.getColumnIndexOrThrow("delay");
      while (localCursor.moveToNext())
      {
        localCursor.getInt(i);
        String str = localCursor.getString(j);
        long l2 = localCursor.getLong(k);
        int i4 = localCursor.getInt(i1);
        long l3 = localCursor.getLong(i2);
        int i5 = localCursor.getInt(i3);
        localArrayList.add(new OperationInfo(localCursor.getLong(n), l2, str, localCursor.getLong(m), i4, i5, l3));
      }
    }
    finally
    {
      this.mDb.endTransaction();
    }
    localCursor.close();
    this.mDb.execSQL("DELETE FROM operations");
    long l1 = recordOperation(paramOperationInfo);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
      recordOperation((OperationInfo)localIterator.next());
    this.mDb.setTransactionSuccessful();
    this.mDb.endTransaction();
    return l1;
  }

  public int nextOperationId()
  {
    try
    {
      long l = DatabaseUtils.longForQuery(this.mDb, "SELECT _id FROM operations LIMIT 1", null);
      return (int)l;
    }
    catch (SQLiteDoneException localSQLiteDoneException)
    {
    }
    return 0;
  }

  public void provideNormalOperations(MailStore.OperationSink paramOperationSink, MailEngine paramMailEngine, MailEngine.SyncInfo paramSyncInfo, long paramLong)
  {
    int i;
    Cursor localCursor;
    int j;
    int k;
    int m;
    int n;
    int i1;
    int i2;
    int i3;
    int i4;
    if (Gservices.getInt(this.mContext.getContentResolver(), "gmail_discard_error_uphill_op_old_froyo", 0) != 0)
    {
      i = 1;
      if (i != 0)
        checkForMessageToDiscard(paramMailEngine);
      SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
      localSQLiteQueryBuilder.setTables("operations");
      localCursor = localSQLiteQueryBuilder.query(this.mDb, this.PROJECTION_PROVIDE_OPERATIONS, null, null, null, null, "_id", "50");
      j = localCursor.getColumnIndexOrThrow("_id");
      k = localCursor.getColumnIndexOrThrow("action");
      m = localCursor.getColumnIndexOrThrow("message_messageId");
      n = localCursor.getColumnIndexOrThrow("value1");
      i1 = localCursor.getColumnIndexOrThrow("value2");
      i2 = localCursor.getColumnIndexOrThrow("numAttempts");
      i3 = localCursor.getColumnIndexOrThrow("nextTimeToAttempt");
      i4 = localCursor.getColumnIndexOrThrow("delay");
    }
    while (true)
      if (localCursor.moveToNext())
      {
        long l1 = localCursor.getLong(j);
        String str = localCursor.getString(k);
        long l2 = localCursor.getLong(m);
        int i5 = localCursor.getInt(i2);
        long l3 = localCursor.getLong(i3);
        int i6 = localCursor.getInt(i4);
        long l4 = localCursor.getLong(i1);
        long l5 = localCursor.getLong(n);
        if (calculateAndUpdateOpDelay(paramLong, l1, i5, i6, l3, new OperationInfo(l4, l2, str, l5), paramSyncInfo, paramMailEngine))
        {
          if ("messageLabelAdded".equals(str))
          {
            paramOperationSink.messageLabelAdded(l1, l2, l5);
            continue;
            i = 0;
            break;
          }
          if ("messageLabelRemoved".equals(str))
          {
            paramOperationSink.messageLabelRemoved(l1, l2, l5);
          }
          else if ("conversationLabelAdded".equals(str))
          {
            paramOperationSink.conversationLabelAddedOrRemoved(l1, l2, l5, true);
          }
          else if ("conversationLabelRemoved".equals(str))
          {
            paramOperationSink.conversationLabelAddedOrRemoved(l1, l2, l5, false);
          }
          else if (("messageSaved".equals(str)) || ("messageSent".equals(str)))
          {
            MailSync.Message localMessage = paramMailEngine.getMessage(l2, true);
            if (localMessage == null)
            {
              Object[] arrayOfObject = new Object[1];
              arrayOfObject[0] = Long.valueOf(l2);
              LogUtils.e("Gmail", "Cannot find message with id = %d for operations!", arrayOfObject);
              this.mDb.delete("operations", "_id == " + l1, null);
            }
            else
            {
              paramOperationSink.messageSavedOrSent(l1, localMessage, l2, localMessage.refMessageId, "messageSaved".equals(str));
            }
          }
          else if ("messageExpunged".equals(str))
          {
            paramOperationSink.messageExpunged(l1, l2);
          }
          else
          {
            throw new RuntimeException("Unknown action: " + str);
          }
        }
      }
    localCursor.close();
  }

  public void provideOperations(MailStore.OperationSink paramOperationSink, MailEngine paramMailEngine, MailEngine.SyncInfo paramSyncInfo, long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString = new String[2];
    arrayOfString[0] = Long.toString(paramSyncInfo.messageId);
    arrayOfString[1] = Long.toString(paramSyncInfo.conversationId);
    Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT \n  _id,\n  action,\n  numAttempts,\n  nextTimeToAttempt,\n  delay\nFROM\n operations\nWHERE\n  message_messageId = ? AND value2 = ?\n", arrayOfString);
    int i = localCursor.getColumnIndexOrThrow("_id");
    int j = localCursor.getColumnIndexOrThrow("action");
    int k = localCursor.getColumnIndexOrThrow("numAttempts");
    int m = localCursor.getColumnIndexOrThrow("nextTimeToAttempt");
    int n = localCursor.getColumnIndexOrThrow("delay");
    while (localCursor.moveToNext())
    {
      long l1 = localCursor.getLong(i);
      String str = localCursor.getString(j);
      int i1 = localCursor.getInt(k);
      long l2 = localCursor.getLong(m);
      int i2 = localCursor.getInt(n);
      OperationInfo localOperationInfo = new OperationInfo(paramSyncInfo.conversationId, paramSyncInfo.messageId, str, 0L);
      if (!"messageSent".equals(str));
      while (!calculateAndUpdateOpDelay(paramLong, l1, i1, i2, l2, localOperationInfo, paramSyncInfo, paramMailEngine))
        return;
      MailSync.Message localMessage = paramMailEngine.getMessage(paramSyncInfo.messageId, true);
      if (localMessage == null)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Long.valueOf(paramSyncInfo.messageId);
        LogUtils.e("Gmail", "Cannot find message with id = %d for operations!", arrayOfObject);
        this.mDb.delete("operations", "_id == " + l1, null);
      }
      else
      {
        paramOperationSink.messageSavedOrSent(l1, localMessage, paramSyncInfo.messageId, localMessage.refMessageId, false);
      }
    }
    localCursor.close();
  }

  public long recordOperation(long paramLong1, long paramLong2, String paramString)
  {
    return recordOperation(paramLong1, paramLong2, paramString, 0L, 0, 0L);
  }

  public long recordOperation(long paramLong1, long paramLong2, String paramString, long paramLong3, int paramInt, long paramLong4)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("action", paramString);
    localContentValues.put("message_messageId", Long.valueOf(paramLong2));
    localContentValues.put("value2", Long.valueOf(paramLong1));
    if ((paramLong3 > 0L) && (paramLong4 > 0L))
    {
      localContentValues.put("numAttempts", Long.valueOf(paramLong3));
      localContentValues.put("nextTimeToAttempt", Long.valueOf(paramLong4));
      localContentValues.put("delay", Integer.valueOf(paramInt));
    }
    return this.mDb.insert("operations", null, localContentValues);
  }

  public long recordOperation(OperationInfo paramOperationInfo)
  {
    if (doesLabelMatter(paramOperationInfo.mAction))
      return recordOperationWithLabelId(paramOperationInfo.mConversationId, paramOperationInfo.mMessageId, paramOperationInfo.mAction, paramOperationInfo.mLabelId, paramOperationInfo.mNumAttempts, paramOperationInfo.mDelay, paramOperationInfo.mNextTimeToAttempt);
    return recordOperation(paramOperationInfo.mConversationId, paramOperationInfo.mMessageId, paramOperationInfo.mAction, paramOperationInfo.mNumAttempts, paramOperationInfo.mDelay, paramOperationInfo.mNextTimeToAttempt);
  }

  public long recordOperationWithLabelId(long paramLong1, long paramLong2, String paramString, long paramLong3)
  {
    return recordOperationWithLabelId(paramLong1, paramLong2, paramString, paramLong3, 0L, 0, 0L);
  }

  public long recordOperationWithLabelId(long paramLong1, long paramLong2, String paramString, long paramLong3, long paramLong4, int paramInt, long paramLong5)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("action", paramString);
    localContentValues.put("message_messageId", Long.valueOf(paramLong2));
    localContentValues.put("value1", Long.valueOf(paramLong3));
    localContentValues.put("value2", Long.valueOf(paramLong1));
    if ((paramLong4 > 0L) && (paramLong5 > 0L))
    {
      localContentValues.put("numAttempts", Long.valueOf(paramLong4));
      localContentValues.put("nextTimeToAttempt", Long.valueOf(paramLong5));
      localContentValues.put("delay", Integer.valueOf(paramInt));
    }
    return this.mDb.insert("operations", null, localContentValues);
  }

  public void updateMessageId(long paramLong1, long paramLong2)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("message_messageId", Long.valueOf(paramLong2));
    this.mDb.update("operations", localContentValues, "message_messageId = " + paramLong1, null);
  }

  public static class OperationInfo
  {
    public String mAction;
    public long mConversationId;
    public int mDelay;
    public long mLabelId;
    public long mMessageId;
    public long mNextTimeToAttempt;
    public int mNumAttempts;

    public OperationInfo(long paramLong1, long paramLong2, String paramString, long paramLong3)
    {
      this(paramLong1, paramLong2, paramString, paramLong3, 0, 0, 0L);
    }

    public OperationInfo(long paramLong1, long paramLong2, String paramString, long paramLong3, int paramInt1, int paramInt2, long paramLong4)
    {
      this.mConversationId = paramLong1;
      this.mMessageId = paramLong2;
      this.mAction = paramString;
      this.mLabelId = paramLong3;
      this.mNumAttempts = paramInt1;
      this.mDelay = paramInt2;
      this.mNextTimeToAttempt = paramLong4;
    }
  }

  public static enum RecordHistory
  {
    static
    {
      RecordHistory[] arrayOfRecordHistory = new RecordHistory[2];
      arrayOfRecordHistory[0] = FALSE;
      arrayOfRecordHistory[1] = TRUE;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.Operations
 * JD-Core Version:    0.6.2
 */