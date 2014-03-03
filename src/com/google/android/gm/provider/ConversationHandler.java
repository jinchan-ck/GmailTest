package com.google.android.gm.provider;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.TimingLogger;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class ConversationHandler
{
  protected final SQLiteDatabase mDb;
  protected final Gmail.LabelMap mLabelMap;
  protected final MailCore mMailCore;

  static
  {
    if (!ConversationHandler.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      return;
    }
  }

  protected ConversationHandler(SQLiteDatabase paramSQLiteDatabase, MailCore paramMailCore)
  {
    this.mDb = paramSQLiteDatabase;
    this.mMailCore = paramMailCore;
    this.mLabelMap = this.mMailCore.getLabelMap();
  }

  private static void calculateSetIntersectionAndDifferences(Set<Long> paramSet1, Set<Long> paramSet2, Set<Long> paramSet3, Set<Long> paramSet4, Set<Long> paramSet5)
  {
    if (paramSet3 != null)
    {
      assert (paramSet3.size() == 0);
      paramSet3.addAll(paramSet1);
      paramSet3.removeAll(paramSet2);
    }
    if (paramSet4 != null)
    {
      assert (paramSet4.size() == 0);
      paramSet4.addAll(paramSet2);
      paramSet4.removeAll(paramSet1);
    }
    if (paramSet5 != null)
    {
      assert (paramSet5.size() == 0);
      paramSet5.addAll(paramSet1);
      paramSet5.retainAll(paramSet2);
    }
  }

  private void updateLabelCounts(MailSync.SyncRationale paramSyncRationale, Map<Long, LabelRecord> paramMap1, Map<Long, LabelRecord> paramMap2, Set<Long> paramSet1, Set<Long> paramSet2, Set<Long> paramSet3)
  {
    long l1 = this.mLabelMap.getLabelIdUnread();
    if (paramMap1.containsKey(Long.valueOf(l1)) != paramMap2.containsKey(Long.valueOf(l1)))
    {
      Iterator localIterator4 = paramMap1.keySet().iterator();
      while (localIterator4.hasNext())
        paramSet1.add((Long)localIterator4.next());
      Iterator localIterator5 = paramMap2.keySet().iterator();
      while (localIterator5.hasNext())
        paramSet2.add((Long)localIterator5.next());
    }
    Iterator localIterator1 = paramSet3.iterator();
    while (localIterator1.hasNext())
    {
      long l2 = ((Long)localIterator1.next()).longValue();
      boolean bool3 = ((LabelRecord)paramMap1.get(Long.valueOf(l2))).isZombie;
      boolean bool4 = ((LabelRecord)paramMap2.get(Long.valueOf(l2))).isZombie;
      if ((!bool3) && (bool4))
        paramSet1.add(Long.valueOf(l2));
      if ((bool3) && (!bool4))
        paramSet2.add(Long.valueOf(l2));
    }
    HashSet localHashSet = Sets.newHashSet();
    boolean bool1 = paramMap1.containsKey(Long.valueOf(l1));
    Iterator localIterator2 = paramSet1.iterator();
    while (localIterator2.hasNext())
    {
      Long localLong2 = (Long)localIterator2.next();
      if ((l1 != localLong2.longValue()) && ((paramSyncRationale == MailSync.SyncRationale.LOCAL_CHANGE) || (MailCore.isLabelIdLocal(localLong2.longValue()))) && ((!paramMap1.containsKey(localLong2)) || (!((LabelRecord)paramMap1.get(localLong2)).isZombie)))
      {
        if (bool1)
        {
          SQLiteDatabase localSQLiteDatabase4 = this.mDb;
          String[] arrayOfString4 = new String[1];
          arrayOfString4[0] = Long.toString(localLong2.longValue());
          localSQLiteDatabase4.execSQL("UPDATE labels SET\n  numConversations = max(numConversations - 1, 0),\n  numUnreadConversations =     max(numUnreadConversations - 1, 0)\nWHERE _id = ?", arrayOfString4);
          LogUtils.v("Gmail", "onConversationChanged decreased total and unread, label %d", new Object[] { localLong2 });
        }
        while (true)
        {
          localHashSet.add(localLong2);
          break;
          SQLiteDatabase localSQLiteDatabase3 = this.mDb;
          String[] arrayOfString3 = new String[1];
          arrayOfString3[0] = Long.toString(localLong2.longValue());
          localSQLiteDatabase3.execSQL("UPDATE labels SET\n  numConversations = max(numConversations - 1, 0)\nWHERE _id = ?", arrayOfString3);
          LogUtils.v("Gmail", "onConversationChanged decreased total, label %d", new Object[] { localLong2 });
        }
      }
    }
    boolean bool2 = paramMap2.containsKey(Long.valueOf(l1));
    Iterator localIterator3 = paramSet2.iterator();
    while (localIterator3.hasNext())
    {
      Long localLong1 = (Long)localIterator3.next();
      if ((l1 != localLong1.longValue()) && ((paramSyncRationale == MailSync.SyncRationale.LOCAL_CHANGE) || (MailCore.isLabelIdLocal(localLong1.longValue()))) && ((!paramMap2.containsKey(localLong1)) || (!((LabelRecord)paramMap2.get(localLong1)).isZombie)))
      {
        if (bool2)
        {
          SQLiteDatabase localSQLiteDatabase2 = this.mDb;
          String[] arrayOfString2 = new String[1];
          arrayOfString2[0] = Long.toString(localLong1.longValue());
          localSQLiteDatabase2.execSQL("UPDATE labels SET\n  numConversations = numConversations + 1,\n  numUnreadConversations = numUnreadConversations + 1\nWHERE _id = ?", arrayOfString2);
          LogUtils.v("Gmail", "onConversationChanged increased total and unread, label %d", new Object[] { localLong1 });
        }
        while (true)
        {
          localHashSet.add(localLong1);
          break;
          SQLiteDatabase localSQLiteDatabase1 = this.mDb;
          String[] arrayOfString1 = new String[1];
          arrayOfString1[0] = Long.toString(localLong1.longValue());
          localSQLiteDatabase1.execSQL("UPDATE labels SET\n  numConversations = numConversations + 1\nWHERE _id = ?", arrayOfString1);
          LogUtils.v("Gmail", "onConversationChanged increased total, label %d", new Object[] { localLong1 });
        }
      }
    }
    if (localHashSet.size() > 0)
    {
      this.mLabelMap.requery();
      this.mMailCore.mListener.onLabelsUpdated(localHashSet);
    }
  }

  private void updateLabels(long paramLong1, long paramLong2, Set<Long> paramSet1, Map<Long, LabelRecord> paramMap, Set<Long> paramSet2, Set<Long> paramSet3, Set<Long> paramSet4, Set<Long> paramSet5, MailSync.SyncRationale paramSyncRationale)
  {
    Iterator localIterator = this.mMailCore.mNotificationRequests.iterator();
    while (localIterator.hasNext())
    {
      MailCore.NotificationRequest localNotificationRequest = (MailCore.NotificationRequest)localIterator.next();
      long l = localNotificationRequest.getTagLabelId();
      boolean bool1 = localNotificationRequest.conversationMatches(paramMap.keySet());
      if (bool1 != paramMap.containsKey(Long.valueOf(l)))
      {
        boolean bool2;
        if ((bool1) && (paramSyncRationale != MailSync.SyncRationale.LOCAL_CHANGE))
        {
          bool2 = localNotificationRequest.conversationMatches(paramSet1);
          if ((paramSet2 == null) || (!localNotificationRequest.conversationMatches(paramSet2)))
            break label300;
        }
        label300: for (int i = 1; ; i = 0)
        {
          if ((!bool2) && (i != 0))
          {
            LabelRecord localLabelRecord = (LabelRecord)paramMap.get(Long.valueOf(localNotificationRequest.getLabelId()));
            if (localLabelRecord != null)
              updateLabelsAddLabel(l, paramLong2, paramMap, paramSet3, paramSet4, paramSet5, localLabelRecord, Operations.RecordHistory.FALSE);
            this.mMailCore.mListener.onConversationNewlyMatchesNotificationRequest(localNotificationRequest);
            Object[] arrayOfObject2 = new Object[3];
            arrayOfObject2[0] = Long.valueOf(paramLong1);
            arrayOfObject2[1] = Long.valueOf(l);
            arrayOfObject2[2] = Long.valueOf(localNotificationRequest.getLabelId());
            LogUtils.v("Gmail", "onConversationChanged %d added tag label %d for label %d", arrayOfObject2);
          }
          if (bool1)
            break;
          updateLabelsRemoveLabel(l, paramLong1, paramLong2, paramMap, paramSet3, paramSet4, paramSet5, Operations.RecordHistory.FALSE);
          Object[] arrayOfObject1 = new Object[3];
          arrayOfObject1[0] = Long.valueOf(paramLong1);
          arrayOfObject1[1] = Long.valueOf(l);
          arrayOfObject1[2] = Long.valueOf(localNotificationRequest.getLabelId());
          LogUtils.v("Gmail", "onConversationChanged %d removed tag label %d for label %d", arrayOfObject1);
          break;
        }
      }
    }
  }

  private void updateLabelsAddLabel(long paramLong1, long paramLong2, Map<Long, LabelRecord> paramMap, Set<Long> paramSet1, Set<Long> paramSet2, Set<Long> paramSet3, LabelRecord paramLabelRecord, Operations.RecordHistory paramRecordHistory)
  {
    this.mMailCore.setLabelOnMessage(paramLong2, this.mMailCore.getLabelOrThrow(paramLong1), true, paramRecordHistory);
    paramMap.put(Long.valueOf(paramLong1), paramLabelRecord);
    paramSet1.remove(Long.valueOf(paramLong1));
    paramSet2.add(Long.valueOf(paramLong1));
    paramSet3.remove(Long.valueOf(paramLong1));
  }

  private void updateLabelsRemoveLabel(long paramLong1, long paramLong2, long paramLong3, Map<Long, LabelRecord> paramMap, Set<Long> paramSet1, Set<Long> paramSet2, Set<Long> paramSet3, Operations.RecordHistory paramRecordHistory)
  {
    this.mMailCore.setLabelOnConversation(paramLong2, paramLong3, this.mMailCore.getLabelOrThrow(paramLong1), false, paramRecordHistory);
    paramMap.remove(Long.valueOf(paramLong1));
    paramSet1.add(Long.valueOf(paramLong1));
    paramSet2.remove(Long.valueOf(paramLong1));
    paramSet3.remove(Long.valueOf(paramLong1));
  }

  public void insertConversationLabels(long paramLong1, long paramLong2, Map<Long, LabelRecord> paramMap)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("queryId", Long.valueOf(paramLong2));
    localContentValues.put("conversation_id", Long.valueOf(paramLong1));
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      long l = ((Long)localEntry.getKey()).longValue();
      LabelRecord localLabelRecord = (LabelRecord)localEntry.getValue();
      localContentValues.put("labels_id", Long.valueOf(l));
      localContentValues.put("isZombie", Boolean.valueOf(localLabelRecord.isZombie));
      localContentValues.put("sortMessageId", Long.valueOf(localLabelRecord.sortMessageId));
      localContentValues.put("date", Long.valueOf(localLabelRecord.dateReceived));
      this.mDb.replace("conversation_labels", null, localContentValues);
    }
  }

  final void onConversationChanged(long paramLong1, MailSync.SyncRationale paramSyncRationale, long paramLong2, Map<Long, LabelRecord> paramMap1, long paramLong3, Map<Long, LabelRecord> paramMap2, boolean paramBoolean, TimingLogger paramTimingLogger)
  {
    boolean bool = onConversationChangedImpl(paramLong1, paramSyncRationale, Long.toString(paramLong2), paramMap1, paramLong3, paramMap2, paramBoolean, paramTimingLogger);
    paramTimingLogger.addSplit("process messages");
    if (!bool)
    {
      insertConversationLabels(paramLong1, paramLong2, paramMap2);
      paramTimingLogger.addSplit("write labels");
    }
  }

  protected abstract boolean onConversationChangedImpl(long paramLong1, MailSync.SyncRationale paramSyncRationale, String paramString, Map<Long, LabelRecord> paramMap1, long paramLong2, Map<Long, LabelRecord> paramMap2, boolean paramBoolean, TimingLogger paramTimingLogger);

  public void updateLabelInfo(long paramLong1, MailSync.SyncRationale paramSyncRationale, Map<Long, LabelRecord> paramMap1, Map<Long, LabelRecord> paramMap2, long paramLong2, Set<Long> paramSet)
  {
    HashSet localHashSet1 = Sets.newHashSet();
    HashSet localHashSet2 = Sets.newHashSet();
    HashSet localHashSet3 = Sets.newHashSet();
    calculateSetIntersectionAndDifferences(paramMap1.keySet(), paramMap2.keySet(), localHashSet1, localHashSet2, localHashSet3);
    Object[] arrayOfObject1 = new Object[4];
    arrayOfObject1[0] = Long.valueOf(paramLong1);
    arrayOfObject1[1] = localHashSet1;
    arrayOfObject1[2] = localHashSet2;
    arrayOfObject1[3] = localHashSet3;
    LogUtils.v("Gmail", "onConversationChanged %d removedLabels (%s), addedLabels (%s), keptLabels (%s)", arrayOfObject1);
    updateLabels(paramLong1, paramLong2, paramMap1.keySet(), paramMap2, paramSet, localHashSet1, localHashSet2, localHashSet3, paramSyncRationale);
    Object[] arrayOfObject2 = new Object[4];
    arrayOfObject2[0] = Long.valueOf(paramLong1);
    arrayOfObject2[1] = localHashSet1;
    arrayOfObject2[2] = localHashSet2;
    arrayOfObject2[3] = localHashSet3;
    LogUtils.v("Gmail", "onConversationChanged after updateLabels %d removedLabels (%s), addedLabels (%s), keptLabels (%s)", arrayOfObject2);
    updateLabelCounts(paramSyncRationale, paramMap1, paramMap2, localHashSet1, localHashSet2, localHashSet3);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.ConversationHandler
 * JD-Core Version:    0.6.2
 */