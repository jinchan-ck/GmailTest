package com.google.android.gm.provider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.TimingLogger;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ConversationUtil
{
  private final SQLiteDatabase mDb;
  private final MailCore mMailCore;

  protected ConversationUtil(SQLiteDatabase paramSQLiteDatabase, MailCore paramMailCore)
  {
    this.mDb = paramSQLiteDatabase;
    this.mMailCore = paramMailCore;
  }

  private void onConversationChanged(long paramLong1, long paramLong2, MailSync.SyncRationale paramSyncRationale, boolean paramBoolean)
  {
    TimingLogger localTimingLogger = new TimingLogger("Gmail", "onConversationChanged");
    while (true)
    {
      try
      {
        String str1 = Long.toString(paramLong1);
        String str2 = Long.toString(paramLong2);
        Map localMap = fetchOldConversationLabels(paramLong1, paramLong2);
        localTimingLogger.addSplit("fetch old labels");
        long l1 = 0L;
        Cursor localCursor = this.mDb.rawQuery("SELECT maxMessageId FROM conversations WHERE _id = ? AND queryId = ?", new String[] { str1, str2 });
        try
        {
          if (localCursor.moveToNext())
          {
            long l2 = localCursor.getLong(0);
            l1 = l2;
          }
          localCursor.close();
          localTimingLogger.addSplit("read old conversation");
          this.mDb.delete("conversation_labels", "queryId = ? AND conversation_id = ?", new String[] { str2, str1 });
          localTimingLogger.addSplit("delete old labels");
          HashMap localHashMap = Maps.newHashMap();
          if (paramLong2 == 0L)
          {
            localObject3 = new SyncedConversationHandler(this.mDb, this.mMailCore);
            ((ConversationHandler)localObject3).onConversationChanged(paramLong1, paramSyncRationale, paramLong2, localMap, l1, localHashMap, paramBoolean, localTimingLogger);
            Object[] arrayOfObject2;
            return;
          }
        }
        finally
        {
          localCursor.close();
        }
      }
      finally
      {
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Long.valueOf(paramLong1);
        LogUtils.v("Gmail", "updated tables for conversation %d", arrayOfObject1);
        localTimingLogger.addSplit("finish");
        localTimingLogger.dumpToLog();
      }
      Object localObject3 = new LiveConversationHandler(this.mDb, this.mMailCore);
    }
  }

  public Map<Long, LabelRecord> fetchOldConversationLabels(long paramLong1, long paramLong2)
  {
    HashMap localHashMap = Maps.newHashMap();
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString1 = { "labels_id", "isZombie", "sortMessageId", "date" };
    String[] arrayOfString2 = new String[2];
    arrayOfString2[0] = Long.toString(paramLong2);
    arrayOfString2[1] = Long.toString(paramLong1);
    Cursor localCursor = localSQLiteDatabase.query("conversation_labels AS cl JOIN labels AS l ON cl.labels_id = l._id", arrayOfString1, "queryId = ? AND conversation_id = ? AND canonicalName NOT LIKE '^^unseen-%'", arrayOfString2, null, null, null);
    while (true)
    {
      try
      {
        if (!localCursor.moveToNext())
          break;
        long l1 = localCursor.getLong(0);
        if (localCursor.getInt(1) != 0)
        {
          bool = true;
          long l2 = localCursor.getLong(2);
          long l3 = localCursor.getLong(3);
          localHashMap.put(Long.valueOf(l1), new LabelRecord(l2, l3, bool));
          continue;
        }
      }
      finally
      {
        localCursor.close();
      }
      boolean bool = false;
    }
    localCursor.close();
    return localHashMap;
  }

  void onConversationChanged(long paramLong, MailSync.SyncRationale paramSyncRationale)
  {
    onConversationChanged(paramLong, paramSyncRationale, false, false);
  }

  void onConversationChanged(long paramLong, MailSync.SyncRationale paramSyncRationale, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!this.mDb.inTransaction())
      throw new IllegalStateException("Must be in transaction");
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Long.valueOf(paramLong);
    LogUtils.v("Gmail", "updateConversationTables: conversationId %d", arrayOfObject);
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString1 = { "queryId" };
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = String.valueOf(paramLong);
    Cursor localCursor = localSQLiteDatabase.query("conversation_labels", arrayOfString1, "conversation_id = ?", arrayOfString2, "queryId", null, null);
    HashSet localHashSet = Sets.newHashSet();
    try
    {
      if (localCursor.moveToNext())
        localHashSet.add(Long.valueOf(localCursor.getLong(0)));
    }
    finally
    {
      localCursor.close();
    }
    if (paramBoolean1)
      localHashSet.add(Long.valueOf(0L));
    Iterator localIterator = localHashSet.iterator();
    while (localIterator.hasNext())
      onConversationChanged(paramLong, ((Long)localIterator.next()).longValue(), paramSyncRationale, paramBoolean2);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.ConversationUtil
 * JD-Core Version:    0.6.2
 */