package com.google.android.gm.provider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.TimingLogger;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LiveConversationHandler extends ConversationHandler
{
  protected LiveConversationHandler(SQLiteDatabase paramSQLiteDatabase, MailCore paramMailCore)
  {
    super(paramSQLiteDatabase, paramMailCore);
  }

  private List<MessageLabelRecord> getMessageLabelRecordsFromConversationTable(String paramString1, String paramString2)
  {
    ArrayList localArrayList = Lists.newArrayList();
    Cursor localCursor = this.mDb.rawQuery("SELECT   labelIds, \n  maxMessageId FROM   conversations WHERE   _id = ? AND queryId = ?", new String[] { paramString2, paramString1 });
    try
    {
      if (localCursor.moveToNext())
        localArrayList.add(new MessageLabelRecord(localCursor.getString(0), Long.valueOf(localCursor.getLong(1))));
      return localArrayList;
    }
    finally
    {
      localCursor.close();
    }
  }

  private List<MessageLabelRecord> getMessageLabelRecordsFromLiveMessageTable(String paramString)
  {
    Cursor localCursor = this.mDb.rawQuery("SELECT \n  messageId,\n  group_concat(labels_id, ','),\n  dateReceivedMs \nFROM\n  messages LEFT OUTER JOIN   message_labels ON messageId = message_messageId\nWHERE\n  synced = 0 AND conversation = ?\nGROUP BY messageId\nORDER BY messageId", new String[] { paramString });
    ArrayList localArrayList;
    try
    {
      int i = localCursor.getCount();
      if (i == 0)
        return null;
      localArrayList = Lists.newArrayList();
      while (localCursor.moveToNext())
      {
        long l = localCursor.getLong(0);
        localArrayList.add(new MessageLabelRecord(localCursor.getString(1), l, localCursor.getLong(2)));
      }
    }
    finally
    {
      localCursor.close();
    }
    localCursor.close();
    return localArrayList;
  }

  protected boolean onConversationChangedImpl(long paramLong1, MailSync.SyncRationale paramSyncRationale, String paramString, Map<Long, LabelRecord> paramMap1, long paramLong2, Map<Long, LabelRecord> paramMap2, boolean paramBoolean, TimingLogger paramTimingLogger)
  {
    String str1 = Long.toString(paramLong1);
    long l1 = -9223372036854775808L;
    Iterator localIterator1 = paramMap1.values().iterator();
    while (localIterator1.hasNext())
    {
      long l9 = ((LabelRecord)localIterator1.next()).dateReceived;
      l1 = Math.max(l1, l9);
    }
    List localList = null;
    if (paramBoolean)
      localList = getMessageLabelRecordsFromLiveMessageTable(str1);
    if (localList == null)
      localList = getMessageLabelRecordsFromConversationTable(paramString, str1);
    long l2 = this.mLabelMap.getLabelIdSpam();
    long l3 = this.mLabelMap.getLabelIdTrash();
    long l4 = -9223372036854775808L;
    Iterator localIterator2 = localList.iterator();
    if (localIterator2.hasNext())
    {
      MessageLabelRecord localMessageLabelRecord = (MessageLabelRecord)localIterator2.next();
      l4 = localMessageLabelRecord.getMessageId();
      String[] arrayOfString = localMessageLabelRecord.getLabelIds();
      ArrayList localArrayList = Lists.newArrayList();
      int i = 0;
      int j = 0;
      int k = arrayOfString.length;
      int m = 0;
      if (m < k)
      {
        String str2 = arrayOfString[m];
        long l8;
        if (!TextUtils.isEmpty(str2))
        {
          l8 = Long.valueOf(str2).longValue();
          localArrayList.add(Long.valueOf(l8));
          if (l2 != l8)
            break label242;
          j = 1;
        }
        while (true)
        {
          m++;
          break;
          label242: if (l3 == l8)
            i = 1;
        }
      }
      Iterator localIterator3 = localArrayList.iterator();
      label265: Long localLong1;
      LabelRecord localLabelRecord1;
      label320: Long localLong2;
      if (localIterator3.hasNext())
      {
        localLong1 = (Long)localIterator3.next();
        if (paramMap2.containsKey(localLong1))
          break label452;
        localLabelRecord1 = new LabelRecord();
        paramMap2.put(localLong1, localLabelRecord1);
        long l5 = Math.max(localLabelRecord1.sortMessageId, l4);
        localLabelRecord1.sortMessageId = l5;
        LabelRecord localLabelRecord2 = (LabelRecord)paramMap1.get(localLong1);
        if (localLabelRecord2 != null)
        {
          long l7 = Math.max(localLabelRecord1.sortMessageId, localLabelRecord2.sortMessageId);
          localLabelRecord1.sortMessageId = l7;
        }
        localLong2 = localMessageLabelRecord.getDateReceived();
        if (localLong2 == null)
          break label469;
      }
      label452: label469: for (long l6 = localLong2.longValue(); ; l6 = l1)
      {
        localLabelRecord1.dateReceived = l6;
        if (((j != 0) || (i != 0)) && ((localLong1.longValue() != l2) || (i != 0)) && (localLong1.longValue() != l3))
          break label265;
        localLabelRecord1.isZombie = false;
        break label265;
        break;
        localLabelRecord1 = (LabelRecord)paramMap2.get(localLong1);
        break label320;
      }
    }
    updateLabelInfo(paramLong1, paramSyncRationale, paramMap1, paramMap2, l4, null);
    paramTimingLogger.addSplit("process labels");
    return false;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.LiveConversationHandler
 * JD-Core Version:    0.6.2
 */