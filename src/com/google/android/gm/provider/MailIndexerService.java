package com.google.android.gm.provider;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.database.sqlite.SQLiteException;
import android.os.Process;
import android.text.TextUtils;
import com.google.android.gm.Utils;
import com.google.android.gsf.Gservices;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MailIndexerService extends IntentService
{
  private static int DEFAULT_MESSAGE_BATCH_SIZE = 50;
  private static final String[] FTS_DOCID_PROJECTION = { "docid" };
  private static String GMAIL_FULL_TEXT_SEARCH_MESSAGE_INDEX__BATCH_SIZE = "gmail_full_text_search_message_index_batch_size";
  private static final String[] INDEXABLE_CONVERSATION_CONTENT_PROJECTION;
  private static final String[] INDEXABLE_MESSAGE_CONTENT_PROJECTION = { "messageId", "conversation", "subject", "snippet", "body", "fromAddress", "toAddresses", "ccAddresses", "bccAddresses" };
  private static volatile MailIndexerService sIndexerInstance;
  private static int sTransactionYieldTimeoutMs = -1;
  private String mAccount;
  private volatile boolean mIndexerExternallyYielded = false;

  static
  {
    INDEXABLE_CONVERSATION_CONTENT_PROJECTION = new String[] { "_id", "subject", "snippet", "fromAddress" };
  }

  public MailIndexerService()
  {
    super("MailIndexerService");
  }

  private void addConversationToFtsIndex(MailEngine paramMailEngine, ContentValues paramContentValues)
  {
    SQLiteDatabase localSQLiteDatabase = paramMailEngine.mDb;
    if ((localSQLiteDatabase == null) || (!localSQLiteDatabase.isOpen()));
    while (!paramMailEngine.isFullTextSearchEnabled())
      return;
    ContentValues localContentValues1 = new ContentValues();
    ContentValues localContentValues2 = new ContentValues();
    String str = paramContentValues.getAsString("_id");
    localSQLiteDatabase.delete("conversation_fts_table", "docid = ?", new String[] { str });
    localContentValues1.put("docid", str);
    localContentValues2.put("docid", str);
    localContentValues1.put("subject", paramContentValues.getAsString("subject"));
    localContentValues1.put("snippet", paramContentValues.getAsString("snippet"));
    localContentValues1.put("fromAddress", paramContentValues.getAsString("fromAddress"));
    localSQLiteDatabase.replaceOrThrow("conversation_fts_table", null, localContentValues1);
    localSQLiteDatabase.replaceOrThrow("conversation_fts_table_index", null, localContentValues2);
  }

  private void addMessageToFtsIndex(MailEngine paramMailEngine, ContentValues paramContentValues)
  {
    SQLiteDatabase localSQLiteDatabase = paramMailEngine.mDb;
    if ((localSQLiteDatabase == null) || (!localSQLiteDatabase.isOpen()));
    while (!paramMailEngine.isFullTextSearchEnabled())
      return;
    ContentValues localContentValues1 = new ContentValues();
    ContentValues localContentValues2 = new ContentValues();
    String str1 = paramContentValues.getAsString("messageId");
    deleteMessageFromFtsIndex(localSQLiteDatabase, str1);
    if (str1 != null)
    {
      localContentValues1.put("docid", str1);
      localContentValues2.put("docid", str1);
    }
    localContentValues1.put("conversation", paramContentValues.getAsString("conversation"));
    localContentValues1.put("subject", paramContentValues.getAsString("subject"));
    localContentValues1.put("snippet", paramContentValues.getAsString("snippet"));
    String str2 = paramContentValues.getAsString("body");
    if (str2 != null)
      localContentValues1.put("body", str2);
    localContentValues1.put("fromAddress", paramContentValues.getAsString("fromAddress"));
    localContentValues1.put("toAddresses", paramContentValues.getAsString("toAddresses"));
    localContentValues1.put("ccAddresses", paramContentValues.getAsString("ccAddresses"));
    localContentValues1.put("bccAddresses", paramContentValues.getAsString("bccAddresses"));
    localSQLiteDatabase.replaceOrThrow("message_fts_table", null, localContentValues1);
    localSQLiteDatabase.replaceOrThrow("message_fts_table_index", null, localContentValues2);
  }

  static void createSearchIndexTables(SQLiteDatabase paramSQLiteDatabase)
  {
    deleteSearchIndexTables(paramSQLiteDatabase);
    paramSQLiteDatabase.execSQL("CREATE VIRTUAL TABLE conversation_fts_table USING FTS4 (subject TEXT, snippet TEXT, fromAddress TEXT, )");
    paramSQLiteDatabase.execSQL("CREATE VIRTUAL TABLE message_fts_table USING FTS4 (conversation TEXT, subject TEXT, snippet TEXT, body TEXT, fromAddress TEXT, toAddresses TEXT, ccAddresses TEXT, bccAddresses TEXT, )");
    paramSQLiteDatabase.execSQL("CREATE TABLE message_fts_table_index(docid INTEGER PRIMARY KEY)");
    paramSQLiteDatabase.execSQL("CREATE TABLE conversation_fts_table_index(docid INTEGER PRIMARY KEY)");
  }

  private boolean deleteConversationFtsEntries(SQLiteDatabase paramSQLiteDatabase)
  {
    return deleteEntriesFromFtsTable(paramSQLiteDatabase, "conversation_fts_table", "conversation_fts_table_index", "conversations", "conversation_fts_table_index.docid = conversations._id", "conversations._id is null");
  }

  // ERROR //
  private boolean deleteEntriesFromFtsTable(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    // Byte code:
    //   0: invokestatic 158	com/google/common/collect/Sets:newHashSet	()Ljava/util/HashSet;
    //   3: astore 7
    //   5: aload_1
    //   6: new 160	java/lang/StringBuilder
    //   9: dup
    //   10: invokespecial 161	java/lang/StringBuilder:<init>	()V
    //   13: aload_3
    //   14: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   17: ldc 167
    //   19: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: aload 4
    //   24: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: ldc 169
    //   29: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   32: aload 5
    //   34: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: ldc 171
    //   39: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   42: invokevirtual 175	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   45: getstatic 56	com/google/android/gm/provider/MailIndexerService:FTS_DOCID_PROJECTION	[Ljava/lang/String;
    //   48: aload 6
    //   50: aconst_null
    //   51: aconst_null
    //   52: aconst_null
    //   53: aconst_null
    //   54: invokevirtual 179	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   57: astore 8
    //   59: aload 8
    //   61: invokeinterface 184 1 0
    //   66: ifeq +37 -> 103
    //   69: aload 7
    //   71: aload 8
    //   73: iconst_0
    //   74: invokeinterface 188 2 0
    //   79: invokestatic 194	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   82: invokeinterface 200 2 0
    //   87: pop
    //   88: goto -29 -> 59
    //   91: astore 9
    //   93: aload 8
    //   95: invokeinterface 203 1 0
    //   100: aload 9
    //   102: athrow
    //   103: aload 8
    //   105: invokeinterface 203 1 0
    //   110: aload_0
    //   111: getfield 66	com/google/android/gm/provider/MailIndexerService:mIndexerExternallyYielded	Z
    //   114: ifeq +5 -> 119
    //   117: iconst_1
    //   118: ireturn
    //   119: aload_1
    //   120: invokevirtual 206	android/database/sqlite/SQLiteDatabase:beginTransactionNonExclusive	()V
    //   123: aload_1
    //   124: new 160	java/lang/StringBuilder
    //   127: dup
    //   128: invokespecial 161	java/lang/StringBuilder:<init>	()V
    //   131: ldc 208
    //   133: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: aload_2
    //   137: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: ldc 210
    //   142: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: invokevirtual 175	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   148: invokevirtual 214	android/database/sqlite/SQLiteDatabase:compileStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteStatement;
    //   151: astore 13
    //   153: aload 7
    //   155: invokeinterface 218 1 0
    //   160: astore 14
    //   162: aload 14
    //   164: invokeinterface 223 1 0
    //   169: istore 15
    //   171: iconst_0
    //   172: istore 16
    //   174: iload 15
    //   176: ifeq +140 -> 316
    //   179: aload 14
    //   181: invokeinterface 227 1 0
    //   186: checkcast 190	java/lang/Long
    //   189: astore 17
    //   191: aload 13
    //   193: iconst_1
    //   194: aload 17
    //   196: invokevirtual 231	java/lang/Long:longValue	()J
    //   199: invokevirtual 237	android/database/sqlite/SQLiteStatement:bindLong	(IJ)V
    //   202: aload 13
    //   204: invokevirtual 240	android/database/sqlite/SQLiteStatement:simpleQueryForLong	()J
    //   207: lstore 31
    //   209: lload 31
    //   211: lconst_0
    //   212: lcmp
    //   213: ifle +159 -> 372
    //   216: iconst_1
    //   217: istore 20
    //   219: aload 13
    //   221: invokevirtual 243	android/database/sqlite/SQLiteStatement:clearBindings	()V
    //   224: iload 20
    //   226: ifeq +53 -> 279
    //   229: iconst_1
    //   230: anewarray 28	java/lang/String
    //   233: astore 27
    //   235: aload 27
    //   237: iconst_0
    //   238: aload 17
    //   240: invokevirtual 244	java/lang/Long:toString	()Ljava/lang/String;
    //   243: aastore
    //   244: aload_1
    //   245: aload_2
    //   246: ldc 95
    //   248: aload 27
    //   250: invokevirtual 99	android/database/sqlite/SQLiteDatabase:delete	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   253: pop
    //   254: iconst_1
    //   255: anewarray 28	java/lang/String
    //   258: astore 29
    //   260: aload 29
    //   262: iconst_0
    //   263: aload 17
    //   265: invokevirtual 244	java/lang/Long:toString	()Ljava/lang/String;
    //   268: aastore
    //   269: aload_1
    //   270: aload_3
    //   271: ldc 95
    //   273: aload 29
    //   275: invokevirtual 99	android/database/sqlite/SQLiteDatabase:delete	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   278: pop
    //   279: aload_1
    //   280: getstatic 58	com/google/android/gm/provider/MailIndexerService:sTransactionYieldTimeoutMs	I
    //   283: i2l
    //   284: invokevirtual 248	android/database/sqlite/SQLiteDatabase:yieldIfContendedSafely	(J)Z
    //   287: ifne +10 -> 297
    //   290: aload_0
    //   291: getfield 66	com/google/android/gm/provider/MailIndexerService:mIndexerExternallyYielded	Z
    //   294: ifeq -132 -> 162
    //   297: ldc 250
    //   299: ldc 252
    //   301: iconst_1
    //   302: anewarray 254	java/lang/Object
    //   305: dup
    //   306: iconst_0
    //   307: aload_2
    //   308: aastore
    //   309: invokestatic 260	com/google/android/gm/provider/LogUtils:d	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   312: pop
    //   313: iconst_1
    //   314: istore 16
    //   316: ldc 250
    //   318: iconst_3
    //   319: invokestatic 264	com/google/android/gm/provider/LogUtils:isLoggable	(Ljava/lang/String;I)Z
    //   322: ifeq +39 -> 361
    //   325: ldc 250
    //   327: new 160	java/lang/StringBuilder
    //   330: dup
    //   331: invokespecial 161	java/lang/StringBuilder:<init>	()V
    //   334: ldc_w 266
    //   337: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   340: aload 7
    //   342: invokeinterface 270 1 0
    //   347: invokevirtual 273	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   350: invokevirtual 175	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   353: iconst_0
    //   354: anewarray 254	java/lang/Object
    //   357: invokestatic 260	com/google/android/gm/provider/LogUtils:d	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   360: pop
    //   361: aload_1
    //   362: invokevirtual 276	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   365: aload_1
    //   366: invokevirtual 279	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   369: iload 16
    //   371: ireturn
    //   372: iconst_0
    //   373: istore 20
    //   375: goto -156 -> 219
    //   378: astore 19
    //   380: aload 13
    //   382: invokevirtual 243	android/database/sqlite/SQLiteStatement:clearBindings	()V
    //   385: iconst_0
    //   386: istore 20
    //   388: goto -164 -> 224
    //   391: astore 10
    //   393: aload_1
    //   394: invokevirtual 279	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   397: aload 10
    //   399: athrow
    //   400: astore 18
    //   402: aload 13
    //   404: invokevirtual 243	android/database/sqlite/SQLiteStatement:clearBindings	()V
    //   407: aload 18
    //   409: athrow
    //   410: astore 25
    //   412: ldc 250
    //   414: aload 25
    //   416: ldc_w 281
    //   419: iconst_2
    //   420: anewarray 254	java/lang/Object
    //   423: dup
    //   424: iconst_0
    //   425: aload 17
    //   427: aastore
    //   428: dup
    //   429: iconst_1
    //   430: aload_2
    //   431: aastore
    //   432: invokestatic 285	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   435: pop
    //   436: goto -157 -> 279
    //   439: astore 23
    //   441: new 287	android/database/sqlite/SQLiteDatabaseCorruptException
    //   444: dup
    //   445: aload 23
    //   447: invokevirtual 290	java/lang/IllegalStateException:getMessage	()Ljava/lang/String;
    //   450: invokespecial 291	android/database/sqlite/SQLiteDatabaseCorruptException:<init>	(Ljava/lang/String;)V
    //   453: athrow
    //   454: astore 22
    //   456: new 287	android/database/sqlite/SQLiteDatabaseCorruptException
    //   459: dup
    //   460: aload 22
    //   462: invokevirtual 292	android/database/sqlite/SQLiteException:getMessage	()Ljava/lang/String;
    //   465: invokespecial 291	android/database/sqlite/SQLiteDatabaseCorruptException:<init>	(Ljava/lang/String;)V
    //   468: athrow
    //   469: astore 12
    //   471: new 287	android/database/sqlite/SQLiteDatabaseCorruptException
    //   474: dup
    //   475: aload 12
    //   477: invokevirtual 290	java/lang/IllegalStateException:getMessage	()Ljava/lang/String;
    //   480: invokespecial 291	android/database/sqlite/SQLiteDatabaseCorruptException:<init>	(Ljava/lang/String;)V
    //   483: athrow
    //   484: astore 11
    //   486: new 287	android/database/sqlite/SQLiteDatabaseCorruptException
    //   489: dup
    //   490: aload 11
    //   492: invokevirtual 292	android/database/sqlite/SQLiteException:getMessage	()Ljava/lang/String;
    //   495: invokespecial 291	android/database/sqlite/SQLiteDatabaseCorruptException:<init>	(Ljava/lang/String;)V
    //   498: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   59	88	91	finally
    //   202	209	378	android/database/sqlite/SQLiteDoneException
    //   123	162	391	finally
    //   162	171	391	finally
    //   179	202	391	finally
    //   219	224	391	finally
    //   229	279	391	finally
    //   279	297	391	finally
    //   297	313	391	finally
    //   316	361	391	finally
    //   361	365	391	finally
    //   380	385	391	finally
    //   402	410	391	finally
    //   412	436	391	finally
    //   202	209	400	finally
    //   229	279	410	android/database/sqlite/SQLiteException
    //   365	369	439	java/lang/IllegalStateException
    //   365	369	454	android/database/sqlite/SQLiteException
    //   393	397	469	java/lang/IllegalStateException
    //   393	397	484	android/database/sqlite/SQLiteException
  }

  private void deleteMessageFromFtsIndex(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    if (paramString != null)
      paramSQLiteDatabase.delete("message_fts_table", "docid = ?", new String[] { paramString });
  }

  static void deleteSearchIndexTables(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS conversation_fts_table");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS message_fts_table");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS message_fts_table_index");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS conversation_fts_table_index");
  }

  private boolean deleteStaleMessageFtsEntries(SQLiteDatabase paramSQLiteDatabase)
  {
    return deleteEntriesFromFtsTable(paramSQLiteDatabase, "message_fts_table", "message_fts_table_index", "messages", "message_fts_table_index.docid = messages.messageId", "messages.messageId is null");
  }

  static String getIndexableMessageBodyText(String paramString)
  {
    return Utils.getMessageBodyWithoutElidedText(paramString);
  }

  private boolean indexConversationContent(MailEngine paramMailEngine)
  {
    SQLiteDatabase localSQLiteDatabase = paramMailEngine.mDb;
    boolean bool1 = deleteConversationFtsEntries(localSQLiteDatabase);
    if (bool1)
      return true;
    if (!spaceAvailableToIndexNewContent())
      return false;
    ArrayList localArrayList = Lists.newArrayList();
    Cursor localCursor = localSQLiteDatabase.query("conversations", INDEXABLE_CONVERSATION_CONTENT_PROJECTION, "queryId = 0 AND _id NOT IN (SELECT docid from conversation_fts_table_index)", null, null, null, null);
    if (localCursor == null)
      return false;
    try
    {
      boolean bool2 = this.mIndexerExternallyYielded;
      if (bool2)
        return true;
      while (localCursor.moveToNext())
      {
        ContentValues localContentValues1 = new ContentValues();
        localContentValues1.put("_id", Long.valueOf(localCursor.getLong(0)));
        localContentValues1.put("subject", localCursor.getString(1));
        localContentValues1.put("snippet", localCursor.getString(2));
        localContentValues1.put("fromAddress", localCursor.getString(3));
        localArrayList.add(localContentValues1);
      }
    }
    finally
    {
      localCursor.close();
    }
    localCursor.close();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(localArrayList.size());
    LogUtils.d("Gmail", "Number of conversations to index: %d", arrayOfObject);
    localSQLiteDatabase.beginTransactionNonExclusive();
    try
    {
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        ContentValues localContentValues2 = (ContentValues)localIterator.next();
        if (localContentValues2 != null)
          addConversationToFtsIndex(paramMailEngine, localContentValues2);
        if ((localSQLiteDatabase.yieldIfContendedSafely(sTransactionYieldTimeoutMs)) || (this.mIndexerExternallyYielded))
        {
          LogUtils.d("Gmail", "Yielded for contention while indexing conversations", new Object[0]);
          bool1 = true;
        }
      }
      localSQLiteDatabase.setTransactionSuccessful();
      return bool1;
    }
    catch (SQLiteException localSQLiteException)
    {
      throw new SQLiteDatabaseCorruptException(localSQLiteException.getMessage());
    }
    finally
    {
      localSQLiteDatabase.endTransaction();
    }
  }

  private boolean indexMessageContent(MailEngine paramMailEngine)
  {
    SQLiteDatabase localSQLiteDatabase = paramMailEngine.mDb;
    boolean bool1 = deleteStaleMessageFtsEntries(localSQLiteDatabase);
    if (bool1)
      return true;
    if (!spaceAvailableToIndexNewContent())
      return false;
    HashSet localHashSet = Sets.newHashSet();
    Cursor localCursor1 = localSQLiteDatabase.query("messages", new String[] { "messageId" }, "queryId = 0 AND messageId NOT IN (SELECT docid from message_fts_table_index)", null, null, null, null);
    if (localCursor1 == null)
      return false;
    if (this.mIndexerExternallyYielded)
      return true;
    try
    {
      if (localCursor1.moveToNext())
        localHashSet.add(Long.valueOf(localCursor1.getLong(0)));
    }
    finally
    {
      localCursor1.close();
    }
    int i = localHashSet.size();
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Integer.valueOf(i);
    LogUtils.d("Gmail", "Number of messages to index: %d", arrayOfObject1);
    int j = Gservices.getInt(getContentResolver(), GMAIL_FULL_TEXT_SEARCH_MESSAGE_INDEX__BATCH_SIZE, DEFAULT_MESSAGE_BATCH_SIZE);
    ArrayList localArrayList = Lists.newArrayList();
    int k = 0;
    Iterator localIterator1 = localHashSet.iterator();
    while (true)
    {
      Cursor localCursor2;
      if (localIterator1.hasNext())
      {
        Long localLong = (Long)localIterator1.next();
        k++;
        localCursor2 = paramMailEngine.getMessageCursorForMessageId(INDEXABLE_MESSAGE_CONTENT_PROJECTION, localLong.longValue());
        if (localCursor2 == null);
      }
      try
      {
        if (localCursor2.moveToFirst())
        {
          ContentValues localContentValues2 = new ContentValues();
          localContentValues2.clear();
          localContentValues2.put("messageId", Long.valueOf(localCursor2.getLong(0)));
          localContentValues2.put("conversation", Long.valueOf(localCursor2.getLong(1)));
          localContentValues2.put("subject", localCursor2.getString(2));
          localContentValues2.put("snippet", localCursor2.getString(3));
          String str = localCursor2.getString(4);
          localContentValues2.put("fromAddress", localCursor2.getString(5));
          localContentValues2.put("toAddresses", localCursor2.getString(6));
          localContentValues2.put("ccAddresses", localCursor2.getString(7));
          localContentValues2.put("bccAddresses", localCursor2.getString(8));
          localContentValues2.put("body", getIndexableMessageBodyText(str));
          localArrayList.add(localContentValues2);
        }
        localCursor2.close();
        if (this.mIndexerExternallyYielded)
        {
          LogUtils.d("Gmail", "Yielded for contention, while indexing messages", new Object[0]);
          bool1 = true;
          return bool1;
        }
      }
      catch (CompressedMessageCursor.CorruptedMessageException localCorruptedMessageException)
      {
        while (true)
        {
          LogUtils.e("Gmail", localCorruptedMessageException, "Unable to decompress the message body for indexing", new Object[0]);
          localCursor2.close();
        }
      }
      catch (OutOfMemoryError localOutOfMemoryError)
      {
        while (true)
        {
          LogUtils.e("Gmail", localOutOfMemoryError, "Out of memory error when loading message body", new Object[0]);
          boolean bool2 = localArrayList.isEmpty();
          if (!bool2)
            localCursor2.close();
          else
            localCursor2.close();
        }
      }
      finally
      {
        localCursor2.close();
      }
      if ((localArrayList.size() >= j) || (k == i))
      {
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Integer.valueOf(localArrayList.size());
        LogUtils.d("Gmail", "Indexing batch with %d messages", arrayOfObject2);
        localSQLiteDatabase.beginTransactionNonExclusive();
      }
      try
      {
        Iterator localIterator2 = localArrayList.iterator();
        label603: ContentValues localContentValues1;
        if (localIterator2.hasNext())
        {
          localContentValues1 = (ContentValues)localIterator2.next();
          if (localContentValues1 == null);
        }
        try
        {
          addMessageToFtsIndex(paramMailEngine, localContentValues1);
          if ((!localSQLiteDatabase.yieldIfContendedSafely(sTransactionYieldTimeoutMs)) && (!this.mIndexerExternallyYielded))
            break label603;
          LogUtils.d("Gmail", "Yielded for contention, while indexing messages", new Object[0]);
          bool1 = true;
          localSQLiteDatabase.setTransactionSuccessful();
          localSQLiteDatabase.endTransaction();
          localArrayList.clear();
          if (!bool1)
            continue;
        }
        catch (SQLiteException localSQLiteException)
        {
          throw new SQLiteDatabaseCorruptException(localSQLiteException.getMessage());
        }
      }
      finally
      {
        localSQLiteDatabase.endTransaction();
      }
    }
  }

  static void onContentProviderAccess(String paramString)
  {
    MailIndexerService localMailIndexerService = sIndexerInstance;
    if ((localMailIndexerService != null) && (TextUtils.equals(paramString, localMailIndexerService.mAccount)))
    {
      LogUtils.d("Gmail", "Database access which requesting indexer delay for account: %s", new Object[] { paramString });
      localMailIndexerService.mIndexerExternallyYielded = true;
    }
  }

  private void resetFtsTables(MailEngine paramMailEngine)
  {
    SQLiteDatabase localSQLiteDatabase = paramMailEngine.mDb;
    LogUtils.w("Gmail", "Recreating search index tables", new Object[0]);
    localSQLiteDatabase.beginTransaction();
    try
    {
      createSearchIndexTables(localSQLiteDatabase);
      localSQLiteDatabase.setTransactionSuccessful();
      LogUtils.w("Gmail", "Search index tables created successfully", new Object[0]);
      return;
    }
    finally
    {
      localSQLiteDatabase.endTransaction();
    }
  }

  private boolean spaceAvailableToIndexNewContent()
  {
    File localFile = getFilesDir();
    long l1 = localFile.getUsableSpace();
    boolean bool;
    if (Gmail.deviceHasLargeDataPartition(this))
      if (l1 >= Gservices.getLong(getContentResolver(), "gmail_large_data_partition_min_indexing_available_space", 1000000000L))
        bool = true;
    while (true)
    {
      return bool;
      return false;
      long l2 = localFile.getTotalSpace();
      if ((float)l1 >= 0.3F * (float)l2);
      for (bool = true; !bool; bool = false)
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Long.valueOf(l2);
        arrayOfObject[1] = Long.valueOf(l1);
        LogUtils.w("Gmail", "Data space requirement not met for indexing. Total: %d, Avail: %d", arrayOfObject);
        return bool;
      }
    }
  }

  static void yieldForTesting()
  {
    MailIndexerService localMailIndexerService = sIndexerInstance;
    if (localMailIndexerService != null)
      localMailIndexerService.mIndexerExternallyYielded = true;
  }

  public void onCreate()
  {
    super.onCreate();
    if (sTransactionYieldTimeoutMs == -1)
      sTransactionYieldTimeoutMs = getResources().getInteger(2131296315);
  }

  protected void onHandleIntent(Intent paramIntent)
  {
    String str = paramIntent.getAction();
    LogUtils.d("Gmail", "MailIndexerService handling intent with action: %s", new Object[] { str });
    if ("com.google.android.gm.intent.provider.INDEX_MESSAGE_CONTENT".equals(str));
    try
    {
      this.mIndexerExternallyYielded = false;
      this.mAccount = paramIntent.getStringExtra("account");
      sIndexerInstance = this;
      Process.setThreadPriority(10);
      MailEngine localMailEngine = MailEngine.getOrMakeMailEngineSync(this, this.mAccount);
      if (localMailEngine == null)
      {
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = this.mAccount;
        LogUtils.w("Gmail", "No MailEngine for account: %s", arrayOfObject1);
        return;
      }
      if (localMailEngine.backgroundTasksDisabledForTesting())
      {
        LogUtils.w("Gmail", "Background tasks have been disabled for testing", new Object[0]);
        localMailEngine.cancelScheduledIndexRun();
        return;
      }
      if (!localMailEngine.isFullTextSearchEnabled())
      {
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = this.mAccount;
        LogUtils.w("Gmail", "Full text search has been disabled for this account: %s", arrayOfObject2);
        localMailEngine.cancelScheduledIndexRun();
        return;
      }
      try
      {
        boolean bool = indexMessageContent(localMailEngine);
        if (!bool)
          bool = indexConversationContent(localMailEngine);
        if (!bool)
        {
          LogUtils.w("Gmail", "Successful index run, cancel next scheduled index run", new Object[0]);
          localMailEngine.cancelScheduledIndexRun();
        }
        return;
      }
      catch (SQLiteDatabaseCorruptException localSQLiteDatabaseCorruptException)
      {
        while (true)
        {
          LogUtils.e("Gmail", "Database appears to be corrupt.  Canceling index pass", new Object[] { localSQLiteDatabaseCorruptException });
          localMailEngine.cancelScheduledIndexRun();
          resetFtsTables(localMailEngine);
        }
      }
    }
    finally
    {
      sIndexerInstance = null;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.MailIndexerService
 * JD-Core Version:    0.6.2
 */