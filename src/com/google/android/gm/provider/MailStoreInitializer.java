package com.google.android.gm.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

class MailStoreInitializer extends DatabaseInitializer
{
  MailStoreInitializer(MailEngine paramMailEngine, SQLiteDatabase paramSQLiteDatabase)
  {
    super(paramMailEngine, paramSQLiteDatabase);
  }

  public static void moveSyncSettingsToInternalDb(SQLiteDatabase paramSQLiteDatabase1, SQLiteDatabase paramSQLiteDatabase2)
  {
    ContentValues[] arrayOfContentValues = new ContentValues[Gmail.SETTINGS_PROJECTION.length];
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("select ");
    for (int i = 0; i < Gmail.SETTINGS_PROJECTION.length; i++)
    {
      String str2 = Gmail.SETTINGS_PROJECTION[i];
      if (i != 0)
        localStringBuilder1.append(", ");
      localStringBuilder1.append("(select value from sync_settings where name ='");
      localStringBuilder1.append(str2);
      localStringBuilder1.append("') as ");
      localStringBuilder1.append(str2);
      arrayOfContentValues[i] = new ContentValues();
      arrayOfContentValues[i].put("name", str2);
    }
    Cursor localCursor = paramSQLiteDatabase1.rawQuery(localStringBuilder1.toString(), null);
    localCursor.moveToNext();
    for (int j = 0; j < Gmail.SETTINGS_PROJECTION.length; j++)
      arrayOfContentValues[j].put("value", localCursor.getString(j));
    localCursor.close();
    paramSQLiteDatabase2.beginTransactionNonExclusive();
    StringBuilder localStringBuilder2;
    try
    {
      paramSQLiteDatabase2.delete("internal_sync_settings", null, null);
      int k = arrayOfContentValues.length;
      for (int m = 0; m < k; m++)
        paramSQLiteDatabase2.insert("internal_sync_settings", null, arrayOfContentValues[m]);
      paramSQLiteDatabase2.setTransactionSuccessful();
      paramSQLiteDatabase2.endTransaction();
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("delete from sync_settings where ");
      for (int n = 0; n < Gmail.SETTINGS_PROJECTION.length; n++)
      {
        String str1 = Gmail.SETTINGS_PROJECTION[n];
        localStringBuilder2.append(" name ='");
        localStringBuilder2.append(str1);
        localStringBuilder2.append("'");
        if (n != 3)
          localStringBuilder2.append(" OR ");
      }
    }
    finally
    {
      paramSQLiteDatabase2.endTransaction();
    }
    paramSQLiteDatabase1.execSQL(localStringBuilder2.toString());
  }

  public void bootstrapDatabase()
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = this.mDb.getPath();
    arrayOfObject[1] = Integer.valueOf(this.mDb.getVersion());
    LogUtils.i("Gmail", "Bootstrapping db: %s Current version is %d", arrayOfObject);
    this.mDb.execSQL("DROP TABLE IF EXISTS sync_settings");
    this.mDb.execSQL("CREATE TABLE sync_settings (_id INTEGER PRIMARY KEY,name TEXT,value TEXT,UNIQUE (name))");
    this.mDb.execSQL("DROP TABLE IF EXISTS engine_settings");
    this.mDb.execSQL("CREATE TABLE engine_settings (_id INTEGER PRIMARY KEY,name TEXT,value TEXT,UNIQUE (name))");
    this.mDb.execSQL("DROP TABLE IF EXISTS messages");
    this.mDb.execSQL("CREATE TABLE messages (_id INTEGER PRIMARY KEY,messageId INTEGER,conversation INTEGER,fromAddress TEXT,toAddresses TEXT,ccAddresses TEXT,bccAddresses TEXT,replyToAddresses TEXT,dateSentMs INTEGER,dateReceivedMs INTEGER,subject TEXT,snippet TEXT,listInfo TEXT,personalLevel INTEGER,body TEXT,bodyEmbedsExternalResources INTEGER,joinedAttachmentInfos STRING,synced INTEGER,error TEXT, clientCreated INTEGER, refMessageId INTEGER DEFAULT 0, forward INTEGER DEFAULT 0, includeQuotedText INTEGER DEFAULT 0, quoteStartPos INTEGER DEFAULT 0, bodyCompressed BLOB DEFAULT NULL, customFromAddress TEXT DEFAULT NULL, queryId INTEGER DEFAULT 1, UNIQUE(messageId))");
    this.mDb.execSQL("DROP TABLE IF EXISTS attachments");
    this.mDb.execSQL("CREATE TABLE attachments (\n  _id INTEGER PRIMARY KEY,\n  messages_conversation INTEGER,\n  messages_messageId INTEGER,\n  messages_partId TEXT,\n  originExtras TEXT,\n  desiredRendition TEXT,  automatic INTEGER,\n  downloadedRendition TEXT,  downloadId INTEGER,\n  status TEXT,\n  saveToSd INTEGER,\n  filename TEXT, priority INTEGER DEFAULT 0, mimeType TEXT DEFAULT NULL, UNIQUE(\n    messages_conversation, messages_messageId,\n    messages_partId, desiredRendition, saveToSd),\n  UNIQUE(messages_messageId, messages_partId, desiredRendition, saveToSd))");
    this.mDb.execSQL("CREATE INDEX attachment_downloadid ON attachments (downloadId)");
    this.mDb.execSQL("DROP TABLE IF EXISTS labels");
    this.mDb.execSQL("CREATE TABLE labels ( _id INTEGER PRIMARY KEY, canonicalName TEXT, name TEXT, numConversations TEXT, numUnreadConversations TEXT, color INTEGER DEFAULT 2147483647, systemLabel INTEGER DEFAULT 0, systemLabelOrder INTEGER DEFAULT 0, hidden INTEGER DEFAULT 0, labelCountDisplayBehavior INTEGER DEFAULT 0, labelSyncPolicy INTEGER DEFAULT 0, visibility TEXT, lastTouched INTEGER DEFAULT 0)");
    this.mDb.execSQL("CREATE INDEX labels_index ON labels (_id, canonicalName, numConversations, numUnreadConversations)");
    this.mDb.execSQL("DROP TABLE IF EXISTS message_labels");
    this.mDb.execSQL("CREATE TABLE message_labels (_id INTEGER PRIMARY KEY,labels_id INTEGER not null,message_messageId INTEGER not null,message_conversation INTEGER,UNIQUE (labels_id, message_messageId))");
    this.mDb.execSQL("CREATE INDEX message_labels_index ON message_labels (labels_id, message_messageId, message_conversation)");
    this.mDb.execSQL("CREATE INDEX message_labels_conversation ON message_labels (message_conversation, labels_id)");
    this.mDb.execSQL("DROP TABLE IF EXISTS operations");
    this.mDb.execSQL("CREATE TABLE operations (_id INTEGER PRIMARY KEY AUTOINCREMENT,action TEXT,message_messageId INTEGER,value1 INTEGER,value2 INTEGER, numAttempts INTEGER DEFAULT 0, nextTimeToAttempt INTEGER DEFAULT 0, delay INTEGER DEFAULT 0)");
    this.mDb.execSQL("CREATE INDEX operations_messageid_action_index ON operations (message_messageId, action)");
    this.mDb.execSQL("DROP TABLE IF EXISTS conversations_to_fetch");
    this.mDb.execSQL("CREATE TABLE conversations_to_fetch (_id INTEGER PRIMARY KEY, nextAttemptDateMs INTEGER DEFAULT 0, numAttempts INTEGER DEFAULT 0)");
    this.mDb.execSQL("DROP TABLE IF EXISTS send_without_sync_conversations_to_fetch");
    this.mDb.execSQL("CREATE TABLE send_without_sync_conversations_to_fetch (_id INTEGER PRIMARY KEY)");
    this.mDb.execSQL("DROP TABLE IF EXISTS messages_to_fetch");
    this.mDb.execSQL("CREATE TABLE messages_to_fetch (_id INTEGER PRIMARY KEY)");
    this.mDb.execSQL("DROP TABLE IF EXISTS conversation_labels;");
    this.mDb.execSQL("CREATE TABLE conversation_labels (  labels_id TEXT,   queryId INTEGER,   isZombie INTEGER,   sortMessageId INTEGER,   date INTEGER,   conversation_id INTEGER,   UNIQUE(labels_id, queryId, conversation_id));");
    this.mDb.execSQL("CREATE INDEX conversation_labels_index ON conversation_labels (labels_id, sortMessageId, queryId, isZombie, date, conversation_id);");
    this.mDb.execSQL("CREATE INDEX conversationLabels_conversationIndex ON conversation_labels (conversation_id, labels_id)");
    this.mDb.execSQL("CREATE INDEX conversationLabels_queryId ON conversation_labels (queryId)");
    this.mDb.execSQL("DROP TABLE IF EXISTS conversations");
    this.mDb.execSQL("CREATE TABLE conversations (_id INTEGER, queryId INTEGER, subject TEXT, snippet TEXT, fromAddress TEXT, personalLevel INTEGER, labelIds TEXT, numMessages INTEGER, maxMessageId INTEGER, hasAttachments INTEGER, hasMessagesWithErrors INTEGER, syncRationale STRING, syncRationaleMessageId INTEGER, forceAllUnread INTEGER, dirty INTEGER DEFAULT 0, UNIQUE(_id, queryId));");
    this.mDb.execSQL("CREATE INDEX conversations_syncRationale on conversations (syncRationale, syncRationaleMessageId);");
    this.mDb.execSQL("CREATE INDEX conversations_queryId on conversations (queryId);");
    this.mDb.execSQL("CREATE INDEX labels_name on labels (canonicalName);");
    this.mDb.execSQL("CREATE INDEX labels_id on labels (_id);");
    this.mDb.execSQL("CREATE INDEX message_labels_message_messageId_labels_id on message_labels (message_messageId, labels_id);");
    this.mDb.execSQL("CREATE INDEX messages_messageId on messages (messageId);");
    this.mDb.execSQL("CREATE INDEX messages_queryId on messages (queryId);");
    this.mDb.execSQL("CREATE INDEX messages_conversation on messages (conversation, messageId);");
    this.mDb.execSQL("CREATE INDEX messages_messageId_queryId on messages (messageId, queryId);");
    this.mDb.execSQL("CREATE INDEX messages_joinedAttachmentInfos ON messages (joinedAttachmentInfos);");
    this.mDb.execSQL("CREATE INDEX messages_conversation_queryId on messages (conversation, queryId)");
    this.mDb.execSQL("DROP TABLE IF EXISTS custom_label_color_prefs");
    this.mDb.execSQL("CREATE TABLE custom_label_color_prefs (\n  _id INTEGER PRIMARY KEY,\n  color_index TEXT,\n  text_color TEXT,\n  background_color TEXT);");
    this.mDb.execSQL("DROP TABLE IF EXISTS custom_from_prefs");
    this.mDb.execSQL("CREATE TABLE custom_from_prefs (\n  _id TEXT PRIMARY KEY,\n  name TEXT,\n  address TEXT,\n  is_default TEXT,\n  reply_to TEXT);");
    this.mDb.execSQL("DROP TABLE IF EXISTS server_preferences");
    this.mDb.execSQL("CREATE TABLE server_preferences (\n  _id TEXT PRIMARY KEY,\n  name TEXT,\n  value TEXT);");
    this.mDb.execSQL("DROP TABLE IF EXISTS info_overload");
    this.mDb.execSQL("CREATE TABLE info_overload (enabled_pref TEXT);");
    MailIndexerService.createSearchIndexTables(this.mDb);
    this.mDb.setVersion(127);
  }

  int getTargetDbVersion(int paramInt)
  {
    if ((paramInt >= 52) && (paramInt < 100))
      return 100;
    return paramInt + 1;
  }

  public void upgradeDbTo100()
  {
    this.mDb.execSQL("ALTER TABLE labels ADD COLUMN systemLabel INTEGER DEFAULT 0");
    this.mDb.execSQL("UPDATE labels SET systemLabel = 1 WHERE substr(canonicalName, 1, 1) = '^'");
    this.mDb.execSQL("ALTER TABLE labels ADD COLUMN systemLabelOrder INTEGER DEFAULT 0");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 1 WHERE canonicalName = '^i'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 2 WHERE canonicalName = '^t'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 3 WHERE canonicalName = '^b'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 4 WHERE canonicalName = '^f'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 5 WHERE canonicalName = '^^out'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 6 WHERE canonicalName = '^r'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 7 WHERE canonicalName = '^all'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 8 WHERE canonicalName = '^s'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 9 WHERE canonicalName = '^k'");
    this.mDb.execSQL("ALTER TABLE labels ADD COLUMN hidden INTEGER DEFAULT 0");
    this.mDb.execSQL("UPDATE labels SET hidden = 1 WHERE substr(canonicalName, 1, 1) = '^' AND canonicalName NOT IN ('^i', '^t', '^b', '^f', '^^out', '^r', '^all', '^s', '^k')");
  }

  public void upgradeDbTo101()
  {
    this.mDb.execSQL("ALTER TABLE labels ADD COLUMN labelCountDisplayBehavior INTEGER DEFAULT 0");
    this.mDb.execSQL("UPDATE labels SET labelCountDisplayBehavior = 1 WHERE canonicalName IN ('^^out', '^r', '^s')");
    this.mDb.execSQL("UPDATE labels SET labelCountDisplayBehavior = 2 WHERE canonicalName IN ('^f', '^t', '^b', '^all', '^k')");
  }

  public void upgradeDbTo102()
  {
    this.mDb.execSQL("ALTER TABLE labels ADD COLUMN labelSyncPolicy INTEGER DEFAULT 0");
    this.mDb.execSQL("UPDATE labels SET labelSyncPolicy = 1 WHERE canonicalName IN ('^^out', '^r')");
    this.mDb.execSQL("UPDATE labels SET labelSyncPolicy = 2 WHERE canonicalName IN ('^s', '^b', '^all', '^k')");
    this.mDb.execSQL("UPDATE labels SET labelSyncPolicy = 3 WHERE canonicalName IN ('^i', '^f')");
  }

  public void upgradeDbTo103()
  {
    moveSyncSettingsToInternalDb(this.mDb, this.mEngine.mInternalDb);
    this.mEngine.sendUpgradeSyncWindowIntent();
  }

  public void upgradeDbTo104()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
    this.mDb.execSQL("DROP TABLE IF EXISTS custom_label_color_prefs");
    this.mDb.execSQL("CREATE TABLE custom_label_color_prefs (\n  _id INTEGER PRIMARY KEY,\n  color_index TEXT,\n  text_color TEXT,\n  background_color TEXT);");
  }

  public void upgradeDbTo105()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
    this.mDb.execSQL("DROP TABLE IF EXISTS custom_from_prefs");
    this.mDb.execSQL("CREATE TABLE custom_from_prefs (\n  _id TEXT PRIMARY KEY,\n  name TEXT,\n  is_default TEXT,\n  reply_to TEXT);");
  }

  public void upgradeDbTo106()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
    this.mDb.execSQL("DROP TABLE IF EXISTS server_preferences");
    this.mDb.execSQL("CREATE TABLE server_preferences (\n  _id TEXT PRIMARY KEY,\n  name TEXT,\n  value TEXT);");
  }

  public void upgradeDbTo107()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
    this.mDb.execSQL("ALTER TABLE labels ADD COLUMN visibility TEXT");
  }

  public void upgradeDbTo108()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 3 WHERE canonicalName = '^io_im'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 4 WHERE canonicalName = '^b'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 5 WHERE canonicalName = '^f'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 6 WHERE canonicalName = '^^out'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 7 WHERE canonicalName = '^r'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 8 WHERE canonicalName = '^all'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 9 WHERE canonicalName = '^s'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 10 WHERE canonicalName = '^k'");
    this.mDb.execSQL("DROP TABLE IF EXISTS info_overload");
    this.mDb.execSQL("CREATE TABLE info_overload (enabled_pref TEXT);");
  }

  public void upgradeDbTo109()
  {
    this.mDb.execSQL("ALTER TABLE attachments ADD COLUMN priority INTEGER DEFAULT 0");
  }

  public void upgradeDbTo110()
  {
    if ((this.mInitialDbVersion < 100) && (this.mInitialDbVersion >= 53))
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(this.mInitialDbVersion);
      LogUtils.w("Gmail", "skipping v110 mailstore upgrade due to initial version %d", arrayOfObject);
      return;
    }
    this.mDb.execSQL("ALTER TABLE conversations_to_fetch ADD COLUMN nextAttemptDateMs INTEGER DEFAULT 0");
    this.mDb.execSQL("ALTER TABLE conversations_to_fetch ADD COLUMN numAttempts INTEGER DEFAULT 0");
  }

  public void upgradeDbTo111()
  {
    if ((this.mInitialDbVersion < 100) && (this.mInitialDbVersion >= 54))
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(this.mInitialDbVersion);
      LogUtils.w("Gmail", "skipping v111 mailstore upgrade due to initial version %d", arrayOfObject);
      return;
    }
    this.mDb.execSQL("ALTER TABLE messages ADD COLUMN customFromAddress TEXT DEFAULT NULL");
  }

  public void upgradeDbTo112()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 2 WHERE canonicalName = '^iim'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 3 WHERE canonicalName = '^t'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 4 WHERE canonicalName = '^io_im'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 5 WHERE canonicalName = '^b'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 6 WHERE canonicalName = '^f'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 7 WHERE canonicalName = '^^out'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 8 WHERE canonicalName = '^r'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 9 WHERE canonicalName = '^all'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 10 WHERE canonicalName = '^s'");
    this.mDb.execSQL("UPDATE labels SET systemLabelOrder = 11 WHERE canonicalName = '^k'");
  }

  public void upgradeDbTo113()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
  }

  public void upgradeDbTo114()
  {
    this.mEngine.setValidateSyncSets(true);
    this.mEngine.requestSync();
  }

  public void upgradeDbTo115()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
  }

  public void upgradeDbTo116()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mDb.execSQL("UPDATE labels SET labelCountDisplayBehavior = 2 WHERE canonicalName = '^io_im'");
  }

  public void upgradeDbTo117()
  {
    this.mDb.execSQL("ALTER TABLE attachments ADD COLUMN mimeType TEXT DEFAULT NULL");
  }

  public void upgradeDbTo118()
  {
    this.mDb.execSQL("UPDATE labels SET labelSyncPolicy = 0 WHERE canonicalName = '^i'");
  }

  public void upgradeDbTo119()
  {
    this.mDb.execSQL("ALTER TABLE messages ADD COLUMN queryId INTEGER DEFAULT 1");
    this.mDb.execSQL("UPDATE messages SET queryId = 0 WHERE synced = 1");
    this.mDb.execSQL("DELETE FROM messages where synced = 0");
    this.mDb.execSQL("CREATE INDEX IF NOT EXISTS messages_queryId on messages (queryId);");
    this.mDb.execSQL("DROP INDEX IF EXISTS messages_synced;");
  }

  public void upgradeDbTo120()
  {
    MailIndexerService.createSearchIndexTables(this.mDb);
  }

  public void upgradeDbTo121()
  {
    this.mDb.execSQL("ALTER TABLE labels ADD COLUMN lastTouched INTEGER DEFAULT 0");
  }

  public void upgradeDbTo122()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
  }

  public void upgradeDbTo123()
  {
    this.mDb.execSQL("CREATE INDEX IF NOT EXISTS messages_messageId_queryId on messages (messageId, queryId);");
  }

  public void upgradeDbTo124()
  {
    if ((this.mInitialDbVersion < 100) && (this.mInitialDbVersion >= 60))
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(this.mInitialDbVersion);
      LogUtils.w("Gmail", "skipping v124 mailstore upgrade due to initial version %d", arrayOfObject);
      return;
    }
    this.mDb.execSQL("ALTER TABLE operations ADD COLUMN delay INTEGER DEFAULT 0");
  }

  public void upgradeDbTo125()
  {
    this.mDb.execSQL("DROP TABLE IF EXISTS message_fts_table_index");
    this.mDb.execSQL("CREATE TABLE message_fts_table_index(docid INTEGER PRIMARY KEY)");
    this.mDb.execSQL("DROP TABLE IF EXISTS conversation_fts_table_index");
    this.mDb.execSQL("CREATE TABLE conversation_fts_table_index(docid INTEGER PRIMARY KEY)");
  }

  public void upgradeDbTo126()
  {
    this.mDb.execSQL("CREATE INDEX IF NOT EXISTS messages_conversation_queryId on messages (conversation, queryId)");
  }

  public void upgradeDbTo127()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
    this.mDb.execSQL("DROP TABLE IF EXISTS custom_from_prefs");
    this.mDb.execSQL("CREATE TABLE custom_from_prefs (\n  _id TEXT PRIMARY KEY,\n  name TEXT,\n  address TEXT,\n  is_default TEXT,\n  reply_to TEXT);");
  }

  public void upgradeDbTo37()
  {
    this.mDb.execSQL("UPDATE conversations SET labelIds = ',' || labelIds");
  }

  public void upgradeDbTo38()
  {
    this.mDb.execSQL("ALTER TABLE messages ADD COLUMN clientCreated INTEGER");
    this.mDb.execSQL("ALTER TABLE messages ADD COLUMN refMessageId INTEGER DEFAULT 0");
  }

  public void upgradeDbTo39()
  {
    MailCore.correctLocalLabelIds(this.mDb);
  }

  public void upgradeDbTo40()
  {
  }

  public void upgradeDbTo41()
  {
    this.mDb.execSQL("ALTER TABLE operations ADD COLUMN numAttempts INTEGER DEFAULT 0");
    this.mDb.execSQL("ALTER TABLE operations ADD COLUMN nextTimeToAttempt INTEGER DEFAULT 0");
  }

  public void upgradeDbTo42()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
    this.mDb.execSQL("ALTER TABLE labels ADD COLUMN color INTEGER DEFAULT 2147483647");
  }

  public void upgradeDbTo43()
  {
    this.mDb.execSQL("DROP TABLE IF EXISTS send_without_sync_conversations_to_fetch");
    this.mDb.execSQL("CREATE TABLE send_without_sync_conversations_to_fetch (_id INTEGER PRIMARY KEY)");
  }

  public void upgradeDbTo44()
  {
    this.mDb.execSQL("ALTER TABLE messages ADD COLUMN forward INTEGER DEFAULT 0");
    this.mDb.execSQL("ALTER TABLE messages ADD COLUMN includeQuotedText INTEGER DEFAULT 0");
    this.mDb.execSQL("ALTER TABLE messages ADD COLUMN quoteStartPos INTEGER DEFAULT 0");
  }

  public void upgradeDbTo45()
  {
    this.mDb.execSQL("DROP TABLE IF EXISTS attachments");
    this.mDb.execSQL("CREATE TABLE attachments (\n  _id INTEGER PRIMARY KEY,\n  messages_conversation INTEGER,\n  messages_messageId INTEGER,\n  messages_partId TEXT,\n  originExtras TEXT,\n  desiredRendition TEXT,  automatic INTEGER,\n  downloadedRendition TEXT,  downloadId INTEGER,\n  status TEXT,\n  saveToSd INTEGER,\n  filename TEXT, UNIQUE(\n    messages_conversation, messages_messageId,\n    messages_partId, desiredRendition, saveToSd),\n  UNIQUE(messages_messageId, messages_partId, desiredRendition, saveToSd))");
  }

  public void upgradeDbTo46()
  {
    this.mDb.execSQL("CREATE INDEX IF NOT EXISTS attachment_downloadid ON attachments (downloadId)");
  }

  public void upgradeDbTo47()
  {
    this.mDb.execSQL("CREATE INDEX IF NOT EXISTS messages_joinedAttachmentInfos ON messages (joinedAttachmentInfos)");
  }

  public void upgradeDbTo48()
  {
    this.mDb.execSQL("ALTER TABLE conversations ADD COLUMN dirty INTEGER DEFAULT 0");
  }

  public void upgradeDbTo49()
  {
    this.mDb.execSQL("ALTER TABLE messages ADD COLUMN bodyCompressed BLOB DEFAULT NULL");
  }

  public void upgradeDbTo50()
  {
    this.mDb.execSQL("CREATE INDEX IF NOT EXISTS conversations_queryId on conversations (queryId);");
    this.mDb.execSQL("CREATE INDEX IF NOT EXISTS messages_synced on messages (synced);");
  }

  public void upgradeDbTo51()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
    this.mDb.execSQL("DROP TABLE IF EXISTS info_overload");
    this.mDb.execSQL("CREATE TABLE info_overload (enabled_pref TEXT);");
  }

  public void upgradeDbTo52()
  {
    MailEngine.setSyncSetting(this.mDb, "startSyncNeeded", "1");
    this.mEngine.requestSync();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.MailStoreInitializer
 * JD-Core Version:    0.6.2
 */