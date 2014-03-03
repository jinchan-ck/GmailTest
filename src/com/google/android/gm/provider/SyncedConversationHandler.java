package com.google.android.gm.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.TimingLogger;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SyncedConversationHandler extends ConversationHandler
{
  protected SyncedConversationHandler(SQLiteDatabase paramSQLiteDatabase, MailCore paramMailCore)
  {
    super(paramSQLiteDatabase, paramMailCore);
  }

  protected boolean onConversationChangedImpl(long paramLong1, MailSync.SyncRationale paramSyncRationale, String paramString, Map<Long, LabelRecord> paramMap1, long paramLong2, Map<Long, LabelRecord> paramMap2, boolean paramBoolean, TimingLogger paramTimingLogger)
  {
    String str1 = Long.toString(paramLong1);
    long l1 = -9223372036854775808L;
    int i = -2147483648;
    HashSet localHashSet = null;
    String str2 = null;
    Object localObject1 = null;
    String str3 = null;
    int j = 0;
    int k = 0;
    MailSync.SyncRationale localSyncRationale1 = MailSync.SyncRationale.NONE;
    long l2 = 0L;
    Cursor localCursor = this.mDb.rawQuery("SELECT \n  messageId,\n  fromAddress,\n  group_concat(labels_id, ' '),\n  subject,\n  snippet,\n  personalLevel,\n  length(joinedAttachmentInfos) > 0 as hasAttachments,\n  dateReceivedMs,\n  error\nFROM\n  messages LEFT OUTER JOIN   message_labels ON messageId = message_messageId\nWHERE\n  synced = 1 AND conversation = ?\nGROUP BY messageId\nORDER BY messageId", new String[] { str1 });
    paramTimingLogger.addSplit("fetch messages");
    while (true)
    {
      CompactSenderInstructions localCompactSenderInstructions;
      long l4;
      long l5;
      long l6;
      int i2;
      int i4;
      boolean bool2;
      boolean bool3;
      int i5;
      int i7;
      label284: long l12;
      try
      {
        localCompactSenderInstructions = new CompactSenderInstructions();
        long l3 = this.mLabelMap.getLabelIdDraft();
        l4 = this.mLabelMap.getLabelIdUnread();
        l5 = this.mLabelMap.getLabelIdSent();
        l6 = this.mLabelMap.getLabelIdOutbox();
        long l7 = this.mLabelMap.getLabelIdSpam();
        long l8 = this.mLabelMap.getLabelIdTrash();
        if (!localCursor.moveToNext())
          break label705;
        long l9 = localCursor.getLong(0);
        String str5 = localCursor.getString(1);
        long l10 = localCursor.getLong(7);
        String str6 = localCursor.getString(2);
        String[] arrayOfString1;
        if (str6 != null)
        {
          arrayOfString1 = TextUtils.split(str6, Gmail.SPACE_SEPARATOR_PATTERN);
          i2 = 0;
          String[] arrayOfString2 = arrayOfString1;
          int i3 = arrayOfString2.length;
          i4 = 0;
          if (i4 < i3)
          {
            long l11 = Long.valueOf(arrayOfString2[i4]).longValue();
            if (l7 == l11)
              break label1070;
            if (l8 != l11)
              break label1073;
            break label1070;
          }
        }
        else
        {
          arrayOfString1 = new String[0];
          continue;
        }
        boolean bool1 = false;
        bool2 = false;
        bool3 = false;
        i5 = 0;
        String[] arrayOfString3 = arrayOfString1;
        int i6 = arrayOfString3.length;
        i7 = 0;
        if (i7 < i6)
        {
          l12 = Long.valueOf(arrayOfString3[i7]).longValue();
          if (l3 != l12)
            break label1085;
          bool1 = true;
          label315: LabelRecord localLabelRecord;
          if (!paramMap2.containsKey(Long.valueOf(l12)))
          {
            localLabelRecord = new LabelRecord();
            paramMap2.put(Long.valueOf(l12), localLabelRecord);
            long l13 = Math.max(localLabelRecord.sortMessageId, l9);
            localLabelRecord.sortMessageId = l13;
            long l14 = Math.max(localLabelRecord.dateReceived, l10);
            localLabelRecord.dateReceived = l14;
            if (i2 == 0)
              localLabelRecord.isZombie = false;
            Boolean localBoolean = (Boolean)this.mMailCore.mLabelIdsIncludedOrPartial.get(Long.valueOf(l12));
            if ((i2 == 0) && (localBoolean != null) && (paramSyncRationale != MailSync.SyncRationale.NONE))
            {
              if (localBoolean.booleanValue())
                localSyncRationale1 = MailSync.SyncRationale.LABEL;
            }
            else
            {
              if (l9 <= paramLong2)
                break label1079;
              if (localHashSet == null)
                localHashSet = Sets.newHashSet();
              Long localLong = Long.valueOf(l12);
              localHashSet.add(localLong);
              break label1079;
            }
          }
          else
          {
            localLabelRecord = (LabelRecord)paramMap2.get(Long.valueOf(l12));
            continue;
          }
          MailSync.SyncRationale localSyncRationale2 = MailSync.SyncRationale.LABEL;
          if (localSyncRationale1 == localSyncRationale2)
            continue;
          localSyncRationale1 = MailSync.SyncRationale.DURATION;
          l2 = Math.max(l2, l9);
          continue;
        }
        if (str2 == null)
          str2 = localCursor.getString(3);
        str3 = localCursor.getString(4);
        if ((localObject1 == null) && (bool2))
          localObject1 = str3;
        int i8 = localCursor.getInt(5);
        i = Math.max(i, i8);
        l1 = Math.max(l1, l9);
        if (localCursor.getInt(6) != 0)
        {
          i9 = 1;
          j |= i9;
          if (localCursor.getInt(8) == 0)
            break label687;
          i10 = 1;
          break label1127;
          label649: localCompactSenderInstructions.addMessage(str5, bool1, bool2, bool3, bool4, bool5);
          continue;
        }
      }
      finally
      {
        localCursor.close();
      }
      int i9 = 0;
      continue;
      label687: int i10 = 0;
      label693: for (boolean bool4 = false; (i5 == 0) || (i10 == 0); bool4 = true)
      {
        bool5 = false;
        break label649;
        label705: if ((l2 != 0L) && ((paramSyncRationale == MailSync.SyncRationale.DURATION) || (paramSyncRationale == MailSync.SyncRationale.LABEL)) && (paramSyncRationale != localSyncRationale1))
        {
          Object[] arrayOfObject = new Object[3];
          arrayOfObject[0] = paramSyncRationale;
          arrayOfObject[1] = localSyncRationale1;
          arrayOfObject[2] = Long.valueOf(l2);
          LogUtils.w("Gmail", "Server sent rational %s but we calculated %s with messageId %d", arrayOfObject);
        }
        String str4 = localCompactSenderInstructions.toInstructionString(5);
        int m = localCursor.getCount();
        localCursor.close();
        paramTimingLogger.addSplit("process messages");
        updateLabelInfo(paramLong1, paramSyncRationale, paramMap1, paramMap2, l1, localHashSet);
        paramTimingLogger.addSplit("process labels");
        if (m == 0)
        {
          this.mDb.delete("conversations", "_id = ? AND queryId = 0", new String[] { str1 });
          return true;
        }
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("_id", Long.valueOf(paramLong1));
        localContentValues.put("queryId", Integer.valueOf(0));
        localContentValues.put("fromAddress", str4);
        localContentValues.put("subject", str2);
        int n;
        if (localObject1 != null)
        {
          localContentValues.put("snippet", localObject1);
          localContentValues.put("personalLevel", Integer.valueOf(i));
          localContentValues.put("numMessages", Integer.valueOf(m));
          localContentValues.put("maxMessageId", Long.valueOf(l1));
          localContentValues.put("labelIds", Gmail.getLabelIdsStringFromLabelIds(paramMap2.keySet()));
          if (j == 0)
            break label1058;
          n = 1;
          label976: localContentValues.put("hasAttachments", Integer.valueOf(n));
          if (k == 0)
            break label1064;
        }
        for (int i1 = 1; ; i1 = 0)
        {
          localContentValues.put("hasMessagesWithErrors", Integer.valueOf(i1));
          localContentValues.put("syncRationale", localSyncRationale1.toString());
          localContentValues.put("syncRationaleMessageId", Long.valueOf(l2));
          this.mDb.replace("conversations", null, localContentValues);
          return false;
          localObject1 = str3;
          break;
          n = 0;
          break label976;
        }
        i2 = 1;
        i4++;
        break;
        label1079: i7++;
        break label284;
        label1085: if (l4 == l12)
        {
          bool2 = true;
          break label315;
        }
        if (l5 == l12)
        {
          bool3 = true;
          break label315;
        }
        if (l6 != l12)
          break label315;
        i5 = 1;
        break label315;
        label1127: k |= i10;
        if ((i5 == 0) || (i10 != 0))
          break label693;
      }
      label1058: label1064: label1070: label1073: boolean bool5 = true;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.SyncedConversationHandler
 * JD-Core Version:    0.6.2
 */