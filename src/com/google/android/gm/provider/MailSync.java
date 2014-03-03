package com.google.android.gm.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.http.AndroidHttpClient;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.android.common.base.Strings;
import com.google.android.common.http.FilePart;
import com.google.android.common.http.MultipartEntity;
import com.google.android.common.http.Part;
import com.google.android.common.http.PartSource;
import com.google.android.gm.perf.Timer;
import com.google.android.gm.provider.protos.GmsProtosMessageTypes;
import com.google.android.gsf.Gservices;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.protocol.ProtoBuf;
import com.google.common.io.protocol.ProtoBufUtil;
import com.google.wireless.gdata2.parser.xml.SimplePullParser;
import com.google.wireless.gdata2.parser.xml.SimplePullParser.ParseException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.CharArrayBuffer;

public final class MailSync
{
  static final Map<String, MailStore.LabelInfo> BUILTIN_LABELS;
  static final Set<String> INITIAL_LABELS_INCLUDED;
  static final Set<String> INITIAL_LABELS_PARTIAL;
  private static final Pattern LABEL_SEPARATOR_PATTERN;
  static final String NAMESPACE;
  public static final SyncRationale NULL_SYNC_RATIONALE;
  static final String SETTING_SERVER_VERSION = "serverVersion";
  private static final String[] SYNC_STATS_LABELS;
  private final Set<Long> dirtyConversations = Sets.newHashSet();
  private final Context mContext;
  private long[] mCounters;
  private Map<String, String> mDirtySettings;
  private long mEarliestAllowedSyncTimeAsElapsedRealtime = 0L;
  private boolean mFakeIoExceptionWhenHandlingMessageSavedOrSent = false;
  private volatile boolean mIsSyncCanceled;
  private final ContentResolver mResolver;
  private final Map<String, String> mSettings;
  private final MailStore mStore;
  Urls mUrls;

  static
  {
    if (!MailSync.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      NULL_SYNC_RATIONALE = null;
      INITIAL_LABELS_INCLUDED = Sets.newHashSet(new String[] { "^r" });
      INITIAL_LABELS_PARTIAL = Sets.newHashSet(new String[] { "^i", "^f", "^iim" });
      NAMESPACE = null;
      LABEL_SEPARATOR_PATTERN = Pattern.compile(" ");
      SYNC_STATS_LABELS = new String[] { "M", "L", "C", "A", "U", "u", "S", "Z", "z" };
      BUILTIN_LABELS = Maps.newHashMap();
      addBuiltInLabelInfo("^f");
      addBuiltInLabelInfo("^^out");
      addBuiltInLabelInfo("^i");
      addBuiltInLabelInfo("^r");
      addBuiltInLabelInfo("^b");
      addBuiltInLabelInfo("^all");
      addBuiltInLabelInfo("^u");
      addBuiltInLabelInfo("^k");
      addBuiltInLabelInfo("^s");
      addBuiltInLabelInfo("^t");
      addBuiltInLabelInfo("^^cached");
      addBuiltInLabelInfo("^^important");
      addBuiltInLabelInfo("^^unimportant");
      return;
    }
  }

  public MailSync(MailStore paramMailStore, Urls paramUrls, Map<String, String> paramMap, Context paramContext, boolean paramBoolean)
  {
    this.mStore = paramMailStore;
    this.mUrls = paramUrls;
    this.mContext = paramContext;
    this.mResolver = paramContext.getContentResolver();
    this.mSettings = paramMap;
    this.mDirtySettings = Maps.newHashMap();
    clearStats();
    if (this.mSettings.size() == 0)
    {
      setLongSetting("clientId", 0L);
      if (getMinServerVersionForConfigInfo() <= 0)
        break label519;
    }
    label519: for (boolean bool = true; ; bool = false)
    {
      setBooleanSetting("needConfigSuggestion", bool);
      setBooleanSetting("configDirty", true);
      setLongSetting("conversationAgeDays", Gmail.getDefaultConversationAgeDays(this.mContext));
      setLongSetting("maxAttachmentSize", 0L);
      setStringSetSetting("labelsIncluded", INITIAL_LABELS_INCLUDED);
      setStringSetSetting("labelsPartial", INITIAL_LABELS_PARTIAL);
      setStringSetting("labelsAll", "");
      setBooleanSetting("startSyncNeeded", false);
      setLongSetting("highestProcessedServerOperationId", 0L);
      setLongSetting("lowestBackwardConversationId", 0L);
      setLongSetting("highestBackwardConversationId", 0L);
      setBooleanSetting("moreForwardSyncNeeded", false);
      saveDirtySettings();
      if (!this.mSettings.containsKey("needConfigSuggestion"))
        setBooleanSetting("needConfigSuggestion", false);
      if (!this.mSettings.containsKey("unackedSentOperations"))
        setBooleanSetting("unackedSentOperations", false);
      if (!this.mSettings.containsKey("nextUnackedSentOp"))
        setLongSetting("nextUnackedSentOp", 0L);
      if (!this.mSettings.containsKey("errorCountNextUnackedSentOp"))
        setLongSetting("errorCountNextUnackedSentOp", 0L);
      if (!this.mSettings.containsKey("nextUnackedOpWriteTime"))
        setLongSetting("nextUnackedOpWriteTime", 0L);
      if (!this.mSettings.containsKey("serverVersion"))
        setLongSetting("serverVersion", 0L);
      if (!this.mSettings.containsKey("clientOpToAck"))
        setLongSetting("clientOpToAck", 0L);
      if (!this.mSettings.containsKey("clientId"))
        setLongSetting("clientId", 0L);
      if (!this.mSettings.containsKey("configDirty"))
        setBooleanSetting("configDirty", true);
      if (!this.mSettings.containsKey("highestProcessedServerOperationId"))
        setLongSetting("highestProcessedServerOperationId", 0L);
      if (!this.mSettings.containsKey("moreForwardSyncNeeded"))
        setBooleanSetting("moreForwardSyncNeeded", false);
      if (!this.mSettings.containsKey("labelsIncluded"))
        setStringSetSetting("labelsIncluded", INITIAL_LABELS_INCLUDED);
      if (!this.mSettings.containsKey("labelsPartial"))
        setStringSetSetting("labelsPartial", INITIAL_LABELS_PARTIAL);
      if (paramBoolean)
      {
        setBooleanSetting("configDirty", true);
        checkLabelsSets(null, null, null);
        saveDirtySettings();
      }
      return;
    }
  }

  private void addAddressesInProto(ProtoBuf paramProtoBuf, int paramInt, List<String> paramList)
  {
    ArrayList localArrayList = Lists.newArrayList();
    ProtoBufHelpers.getAllProtoBufs(paramProtoBuf, paramInt, localArrayList);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
      paramList.add(readEmailFromProto((ProtoBuf)localIterator.next()));
  }

  private static void addBuiltInLabelInfo(String paramString)
  {
    BUILTIN_LABELS.put(paramString, new MailStore.LabelInfo(paramString, paramString, 0, 0, 2147483647, "SHOW"));
  }

  private byte[] getBodyAsBytes(Cursor paramCursor)
  {
    try
    {
      byte[] arrayOfByte = paramCursor.getString(1).getBytes("UTF-8");
      return arrayOfByte;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new IllegalStateException("UTF-8 not supported");
  }

  private ArrayList<Long> getDirtyConversations()
  {
    ArrayList localArrayList = this.mStore.getDirtyConversations();
    localArrayList.addAll(this.dirtyConversations);
    return localArrayList;
  }

  private int getMinServerVersionForConfigInfo()
  {
    String str = Gservices.getString(this.mResolver, "gmail_config_info_min_server_version");
    if (str == null)
      return 0;
    try
    {
      int i = Integer.valueOf(str).intValue();
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return 0;
  }

  private Set<String> getStringSetSetting(String paramString)
  {
    return Sets.newHashSet(TextUtils.split(getStringSetting(paramString), Gmail.SPACE_SEPARATOR_PATTERN));
  }

  private String getStringSetting(String paramString)
  {
    if (this.mSettings.containsKey(paramString))
      return (String)this.mSettings.get(paramString);
    throw new IllegalStateException("missing setting: " + paramString);
  }

  private void handleCheckConversationProto(ProtoBuf paramProtoBuf)
  {
    this.mStore.prepare();
    try
    {
      long l1;
      if (paramProtoBuf.has(3))
      {
        Conversation localConversation = new Conversation();
        ProtoBuf localProtoBuf = paramProtoBuf.getProtoBuf(3);
        l1 = localProtoBuf.getLong(1);
        localConversation.conversationId = l1;
        localConversation.sortMessageId = localProtoBuf.getLong(2);
        localConversation.date = localProtoBuf.getLong(3);
        localConversation.subject = localProtoBuf.getString(4);
        localConversation.snippet = localProtoBuf.getString(5);
        localConversation.personalLevel = Gmail.PersonalLevel.fromInt(localProtoBuf.getInt(6));
        localConversation.maxMessageId = localProtoBuf.getLong(7);
        localConversation.numMessages = localProtoBuf.getInt(8);
        localConversation.hasAttachments = localProtoBuf.getBool(9);
        localConversation.fromAddress = CompactSenderInstructions.instructionsStringFromProto(localProtoBuf.getProtoBuf(10));
        localConversation.labelIds = Sets.newHashSet();
        ProtoBufHelpers.getAllLongs(localProtoBuf, 11, localConversation.labelIds);
        this.mStore.handleConversation(localConversation);
      }
      while (true)
      {
        long l2 = paramProtoBuf.getLong(2);
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Long.valueOf(l1);
        arrayOfObject[1] = Long.valueOf(l2);
        LogUtils.v("Gmail", "checkConversationProto: conversationId: %d, messageId: %d", arrayOfObject);
        this.mStore.addSyncedConversationInfoToCheck(l1, l2);
        this.mStore.commit();
        incStats(2);
        return;
        long l3 = paramProtoBuf.getLong(1);
        l1 = l3;
      }
    }
    finally
    {
      this.mStore.commit();
    }
  }

  private void handleConfigAcceptedProto(ProtoBuf paramProtoBuf)
    throws MailSync.ResponseParseException
  {
    if (!paramProtoBuf.has(1))
      throw new ResponseParseException("ConfigAccepted Proto is missing a client_id value");
    handleConfigAcceptedValues(paramProtoBuf.getLong(1));
  }

  private void handleConfigInfoProto(ProtoBuf paramProtoBuf)
  {
    if (LogUtils.isLoggable("Gmail", 2))
      ProtoBufHelpers.printConfigInfoProto(paramProtoBuf);
    long l = paramProtoBuf.getLong(1);
    HashSet localHashSet1 = Sets.newHashSet();
    HashSet localHashSet2 = Sets.newHashSet();
    ProtoBufHelpers.getAllStrings(paramProtoBuf, 2, localHashSet1);
    ProtoBufHelpers.getAllStrings(paramProtoBuf, 3, localHashSet2);
    handleConfigInfoValues(l, localHashSet1, localHashSet2);
  }

  private void handleConversationProto(ProtoBuf paramProtoBuf, MailProtocolInputStream paramMailProtocolInputStream, MailSyncObserver paramMailSyncObserver, long paramLong, MailEngine.SyncInfo paramSyncInfo)
    throws IOException, MailSync.ResponseParseException
  {
    Timer.startTiming("MS.handleConversation");
    long l1 = paramProtoBuf.getLong(1);
    long l2 = 0L;
    if (paramProtoBuf.has(3))
      l2 = paramProtoBuf.getLong(3);
    SyncRationale localSyncRationale = syncRationaleFromProto(paramProtoBuf.getInt(2));
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = Long.valueOf(l1);
    arrayOfObject1[1] = localSyncRationale;
    LogUtils.v("Gmail", "handleConversationProto: conversationId: %d, SyncRationale: %s", arrayOfObject1);
    int i = 1;
    int j = 0;
    this.mStore.prepare();
    int k = 0;
    ProtoBuf localProtoBuf1;
    label160: label346: Message localMessage2;
    while (true)
    {
      Dictionary localDictionary;
      try
      {
        localDictionary = new Dictionary();
        boolean bool1 = this.mIsSyncCanceled;
        if (bool1)
          return;
        try
        {
          localProtoBuf1 = paramMailProtocolInputStream.readNextChunkPart();
          if (localProtoBuf1 != null)
            break label397;
          Object[] arrayOfObject8 = new Object[1];
          arrayOfObject8[0] = Long.valueOf(l1);
          LogUtils.w("Gmail", "handleConversationProto: End of stream while reading next chunk part. conversationId: %d", arrayOfObject8);
          this.mStore.notifyConversationChanged(l1, SyncRationale.UNKNOWN);
          if (i != 0)
          {
            this.mStore.notifyConversationLoaded(l1, localSyncRationale, paramSyncInfo);
            if ((l2 != 0L) && (l2 != l1))
              this.mStore.notifyConversationLoaded(l2, localSyncRationale, paramSyncInfo);
            if (this.dirtyConversations.contains(Long.valueOf(l1)))
            {
              Object[] arrayOfObject3 = new Object[1];
              arrayOfObject3[0] = Long.valueOf(l1);
              LogUtils.w("Gmail", "handleConversationProto: Dirty conversation %d synced successfully. Marking as dirty in DB", arrayOfObject3);
              this.mStore.markConversationDirty(l1);
              this.dirtyConversations.remove(Long.valueOf(l1));
            }
          }
          this.mStore.commit();
          Timer.stopTiming("MS.handleConversation", j);
          return;
        }
        catch (OutOfMemoryError localOutOfMemoryError)
        {
          if (!getDirtyConversations().contains(Long.valueOf(l1)))
            break label346;
        }
        throw localOutOfMemoryError;
      }
      finally
      {
        this.mStore.commit();
      }
      k = 1;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Long.valueOf(l1);
      LogUtils.w("Gmail", "handleConversationProto: Chunk to big to fit in memory. marking conversation %d as dirty", arrayOfObject2);
      this.dirtyConversations.add(Long.valueOf(l1));
      i = 0;
      continue;
      label397: boolean bool2 = localProtoBuf1.has(11);
      if (!bool2)
        break label602;
      if (k == 0)
        try
        {
          localMessage2 = readMessageFromProto(localProtoBuf1.getProtoBuf(11), l1, localDictionary);
          if (localMessage2.body != null)
            break;
          k = 1;
          Object[] arrayOfObject7 = new Object[2];
          arrayOfObject7[0] = Long.valueOf(localMessage2.messageId);
          arrayOfObject7[1] = Long.valueOf(l1);
          LogUtils.w("Gmail", "handleConversationProto: Could not deflate message %d. marking conversation %d as dirty", arrayOfObject7);
          this.dirtyConversations.add(Long.valueOf(l1));
          i = 0;
        }
        catch (CompressedMessageCursor.CorruptedMessageException localCorruptedMessageException2)
        {
          LogUtils.e("Gmail", localCorruptedMessageException2, "Corrupted data while creating dictionary. Deleting corrupted messages and retrying conversation fetch", new Object[0]);
          this.mStore.deleteCorruptedMessage(l1, localCorruptedMessageException2.getMessageId());
          i = 0;
        }
    }
    LogUtils.v("Gmail", "handleConversationProto: message: %s", new Object[] { localMessage2 });
    this.mStore.addOrUpdateMessage(localMessage2, paramLong);
    if (paramMailSyncObserver != null)
      paramMailSyncObserver.onMessageReceived();
    while (true)
    {
      while (true)
      {
        this.mStore.yieldForContention();
        break;
        label602: boolean bool3 = localProtoBuf1.has(18);
        if (bool3)
        {
          MailProtocolInputStream localMailProtocolInputStream = null;
          try
          {
            localMailProtocolInputStream = unzipMessageBatch(localProtoBuf1.getProtoBuf(18), l1);
            ProtoBuf localProtoBuf2;
            if (localMailProtocolInputStream == null)
            {
              k = 1;
              Object[] arrayOfObject6 = new Object[1];
              arrayOfObject6[0] = Long.valueOf(l1);
              LogUtils.w("Gmail", "handleConversationProto: Could not deflate conversation %d. marking conversation as dirty", arrayOfObject6);
              this.dirtyConversations.add(Long.valueOf(l1));
              i = 0;
              if (localMailProtocolInputStream == null)
                break;
              localMailProtocolInputStream.close();
              i = 0;
              break;
              Message localMessage1 = readMessageFromProto(localProtoBuf2.getProtoBuf(11), l1, null);
              this.mStore.addOrUpdateMessage(localMessage1, paramLong);
              if (paramMailSyncObserver != null)
                paramMailSyncObserver.onMessageReceived();
              j++;
            }
            try
            {
              localProtoBuf2 = localMailProtocolInputStream.readNextChunkPart();
              if (localProtoBuf2 != null);
            }
            catch (IOException localIOException)
            {
              while (true)
              {
                k = 1;
                Object[] arrayOfObject4 = new Object[1];
                arrayOfObject4[0] = Long.valueOf(l1);
                LogUtils.w("Gmail", localIOException, "handleConversationProto: Could not deflate conversation %d.marking conversation as dirty", arrayOfObject4);
                this.dirtyConversations.add(Long.valueOf(l1));
                i = 0;
              }
            }
          }
          catch (CompressedMessageCursor.CorruptedMessageException localCorruptedMessageException1)
          {
            while (true)
            {
              LogUtils.e("Gmail", localCorruptedMessageException1, "Corrupted data while creating dictionary. Deleting corrupted messages and retrying conversation fetch", new Object[0]);
              this.mStore.deleteCorruptedMessage(l1, localCorruptedMessageException1.getMessageId());
              i = 0;
              if (localMailProtocolInputStream == null)
                break;
              localMailProtocolInputStream.close();
              i = 0;
              break;
              if (this.mIsSyncCanceled)
              {
                Object[] arrayOfObject5 = new Object[1];
                arrayOfObject5[0] = Long.valueOf(l1);
                LogUtils.v("Gmail", "sync cancelled while processing messages for conversation: %d", arrayOfObject5);
                i = 0;
              }
            }
          }
          finally
          {
            if (localMailProtocolInputStream != null)
              localMailProtocolInputStream.close();
          }
        }
      }
      if (localProtoBuf1.has(12))
      {
        LogUtils.v("Gmail", "handleConversationProto: end conversation", new Object[0]);
        break label160;
      }
      LogUtils.e("Gmail", "Unexpected chunk in conversation", new Object[0]);
      break label160;
      j++;
    }
  }

  private void handleForwardSyncProto(ProtoBuf paramProtoBuf, MailSyncObserver paramMailSyncObserver)
  {
    int i = 1;
    if (LogUtils.isLoggable("Gmail", 2))
      ProtoBufHelpers.printForwardSyncProto(paramProtoBuf);
    long l1 = paramProtoBuf.getLong(i);
    this.mStore.prepare();
    ProtoBuf localProtoBuf4;
    long l5;
    int j;
    boolean bool3;
    boolean bool2;
    while (true)
    {
      try
      {
        if (!paramProtoBuf.has(2))
          break label240;
        localProtoBuf4 = paramProtoBuf.getProtoBuf(2);
        l5 = localProtoBuf4.getLong(2);
        j = localProtoBuf4.getInt(1);
        if (j == 0)
        {
          int k = i;
          break label881;
          if ((k != 0) || (bool3) || (i != 0))
            break;
          throw new IllegalArgumentException("unknown change code: " + j);
        }
      }
      finally
      {
        this.mStore.commit();
      }
      bool2 = false;
      break label881;
      label137: bool3 = false;
      break label890;
      label143: i = 0;
    }
    MailCore.Label localLabel;
    if (!bool2)
    {
      localLabel = null;
      if (!bool3);
    }
    else
    {
      long l6 = localProtoBuf4.getLong(3);
      localLabel = this.mStore.getOrAddLabel(l6);
    }
    SyncRationale localSyncRationale = syncRationaleFromProto(localProtoBuf4.getInt(4));
    ArrayList localArrayList = Lists.newArrayList();
    ProtoBufHelpers.getAllLongs(localProtoBuf4, 5, localArrayList);
    handleOperationConversationLabelsChangedValues(l5, localLabel, bool2, bool3, i, localSyncRationale, localArrayList);
    while (true)
    {
      label223: onFinishedHandlingForwardOperation(l1, paramMailSyncObserver);
      this.mStore.commit();
      return;
      label240: if (paramProtoBuf.has(3))
      {
        ProtoBuf localProtoBuf3 = paramProtoBuf.getProtoBuf(3);
        long l4 = localProtoBuf3.getLong(1);
        handleOperationLabelCreatedValues(this.mStore.getOrAddLabel(l4), localProtoBuf3.getString(2), localProtoBuf3.getString(3));
      }
      else if (paramProtoBuf.has(4))
      {
        ProtoBuf localProtoBuf2 = paramProtoBuf.getProtoBuf(4);
        long l3 = localProtoBuf2.getLong(1);
        handleOperationLabelRenamedValues(this.mStore.getOrAddLabel(l3), localProtoBuf2.getString(2), localProtoBuf2.getString(3));
      }
      else if (paramProtoBuf.has(5))
      {
        long l2 = paramProtoBuf.getProtoBuf(5).getLong(1);
        handleOperationLabelDeletedValues(this.mStore.getLabelOrNull(l2));
      }
      else
      {
        if (!paramProtoBuf.has(8))
          break;
        handleCheckConversationProto(paramProtoBuf.getProtoBuf(8));
      }
    }
    String str1;
    boolean bool1;
    label447: String str2;
    label460: label627: Object localObject2;
    if (paramProtoBuf.has(9))
    {
      ProtoBuf localProtoBuf1 = paramProtoBuf.getProtoBuf(9);
      str1 = localProtoBuf1.getString(1);
      if (!localProtoBuf1.has(3))
        break label875;
      bool1 = localProtoBuf1.getBool(3);
      if (bool1)
        break label868;
      str2 = localProtoBuf1.getString(2);
      LogUtils.v("Gmail", "MainSync: Custom preference name: %s", new Object[] { str1 });
      LogUtils.v("Gmail", "MainSync: Custom preference value: %s", new Object[] { str2 });
      if (str1.equals("sx_clcp"))
      {
        LogUtils.v("Gmail", "MainSync: Custom Color: %s", new Object[] { str2 });
        if (!str2.isEmpty())
          this.mStore.setCustomLabelColorPreference(getCustomLabelColorPrefs(str2));
      }
      if ((str1.equals("bx_rf")) && (!str2.isEmpty()))
      {
        ImmutableMap localImmutableMap3 = ImmutableMap.of("bx_rf", String.valueOf(str2));
        this.mStore.setServerPreferences(localImmutableMap3);
      }
      if (str1.startsWith("/customfrom/"))
      {
        if (!bool1)
          this.mStore.setCustomFromPreference(getCustomFromPrefs(str2, true), false);
      }
      else
      {
        if (str1.equals("sx_rt"))
        {
          if (str2 == null)
            break label899;
          if (!str2.isEmpty())
            break label837;
          break label899;
          label654: ImmutableMap localImmutableMap1 = ImmutableMap.of("sx_rt", localObject2);
          this.mStore.setServerPreferences(localImmutableMap1);
        }
        if (str1.equals("sx_dn"))
        {
          if (str2 == null)
            break label906;
          if (!str2.isEmpty())
            break label847;
          break label906;
        }
      }
    }
    while (true)
    {
      ImmutableMap localImmutableMap2 = ImmutableMap.of("sx_dn", localObject3);
      this.mStore.setServerPreferences(localImmutableMap2);
      if ((str1.equals("sx_ioe")) && (!bool1) && (str2 != null))
      {
        this.mStore.setInfoOverloadEnabledPreference(str2.toString());
        setBooleanSetting("startSyncNeeded", true);
      }
      if ((!str1.equals("bx_ioao")) || (bool1) || (str2 == null))
        break label223;
      this.mStore.setInfoOverloadArrowsOffPreference(str2.toString());
      break label223;
      LogUtils.v("Gmail", "Deleting preference %s", new Object[] { str1 });
      this.mStore.removeCustomFromPreference(str1);
      break label627;
      label837: localObject2 = String.valueOf(str2);
      break label654;
      label847: Object localObject3 = String.valueOf(str2);
      continue;
      throw new IllegalArgumentException("No forward sync operation found");
      label868: str2 = "";
      break label460;
      label875: bool1 = false;
      break label447;
      label881: if (j != i)
        break label137;
      bool3 = i;
      label890: if (j != 2)
        break label143;
      break;
      label899: localObject2 = "";
      break label654;
      label906: localObject3 = "";
    }
  }

  private void handleMessageNotHandledValues(long paramLong, String paramString)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Long.valueOf(paramLong);
    arrayOfObject[1] = paramString;
    LogUtils.w("Gmail", "Message %d not handled because: %s", arrayOfObject);
    this.mStore.notifyMessageNotUpdated(paramLong, paramString);
  }

  private void handleMessageProto(ProtoBuf paramProtoBuf, MailProtocolInputStream paramMailProtocolInputStream, MailSyncObserver paramMailSyncObserver, long paramLong, MailEngine.SyncInfo paramSyncInfo)
    throws IOException, MailSync.ResponseParseException
  {
    long l = paramProtoBuf.getLong(1);
    this.mStore.prepare();
    int i = 0;
    ProtoBuf localProtoBuf;
    try
    {
      while (true)
      {
        boolean bool1 = this.mIsSyncCanceled;
        if (bool1)
          return;
        localProtoBuf = paramMailProtocolInputStream.readNextChunkPart();
        if (localProtoBuf == null)
        {
          Object[] arrayOfObject1 = new Object[1];
          arrayOfObject1[0] = Long.valueOf(l);
          LogUtils.w("Gmail", "handleMessageProto: End of stream while reading next chunk part. conversationId: %d", arrayOfObject1);
          return;
        }
        if (!localProtoBuf.has(11))
          break;
        if (i == 0)
        {
          Message localMessage = readMessageFromProto(localProtoBuf.getProtoBuf(11), l, new Dictionary());
          if (localMessage.body == null)
          {
            this.dirtyConversations.add(Long.valueOf(l));
            Object[] arrayOfObject3 = new Object[2];
            arrayOfObject3[0] = Long.valueOf(localMessage.messageId);
            arrayOfObject3[1] = Long.valueOf(l);
            LogUtils.w("Gmail", "handleMessageProto: Could not deflate message %d. marking conversation %d  as dirty", arrayOfObject3);
            i = 1;
          }
          else
          {
            LogUtils.v("Gmail", "handleMessageProto: message: %s", new Object[] { localMessage });
            this.mStore.addOrUpdateMessage(localMessage, paramLong);
            this.mStore.notifyConversationChanged(localMessage.conversationId, SyncRationale.UNKNOWN);
            if (paramMailSyncObserver != null)
              paramMailSyncObserver.onMessageReceived();
            if (this.dirtyConversations.contains(Long.valueOf(l)))
            {
              Object[] arrayOfObject2 = new Object[2];
              arrayOfObject2[0] = Long.valueOf(localMessage.messageId);
              arrayOfObject2[1] = Long.valueOf(l);
              LogUtils.w("Gmail", "handleMessageProto: Message %d synced successfully. Marking conversation %d as dirty in DB", arrayOfObject2);
              this.mStore.markConversationDirty(l);
              this.dirtyConversations.remove(Long.valueOf(l));
            }
            this.mStore.yieldForContention();
          }
        }
      }
    }
    finally
    {
      this.mStore.commit();
    }
    boolean bool2 = localProtoBuf.has(13);
    if (bool2)
    {
      this.mStore.commit();
      return;
    }
    throw new IllegalArgumentException("Unexpected chunk in conversation");
  }

  private void handleNoConversationProto(ProtoBuf paramProtoBuf)
    throws IOException, MailSync.ResponseParseException
  {
    long l = paramProtoBuf.getLong(1);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Long.valueOf(l);
    LogUtils.v("Gmail", "handleNoConversationProto: conversationId: %d", arrayOfObject);
    handleNoConversationValues(l);
  }

  private void handleNoConversationValues(long paramLong)
  {
    this.mStore.prepare();
    try
    {
      this.mStore.notifyConversationChanged(paramLong, SyncRationale.NONE);
      this.mStore.notifyConversationLoaded(paramLong, SyncRationale.NONE, new MailEngine.SyncInfo());
      return;
    }
    finally
    {
      this.mStore.commit();
    }
  }

  private void handleNoMessageProto(ProtoBuf paramProtoBuf)
    throws IOException, MailSync.ResponseParseException
  {
    long l = paramProtoBuf.getLong(1);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Long.valueOf(l);
    LogUtils.v("Gmail", "handleNoMessageProto: messageId: %d", arrayOfObject);
    handleNoMessageValues(l);
  }

  private void handleNoMessageValues(long paramLong)
  {
    this.mStore.notifyMessageDoesNotExist(paramLong);
  }

  private void handleOperationConversationLabelsChangedValues(long paramLong, MailCore.Label paramLabel, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, SyncRationale paramSyncRationale, List<Long> paramList)
  {
    if ((paramBoolean1) || (paramBoolean2))
      if (paramLabel != null)
        this.mStore.setLabelOnMessages(paramLong, paramList, paramLabel, paramBoolean1, paramSyncRationale);
    do
      while (true)
      {
        incStats(1);
        return;
        if (!paramBoolean3)
          break;
        this.mStore.expungeMessages(paramLong, paramList, paramSyncRationale);
      }
    while ($assertionsDisabled);
    throw new AssertionError();
  }

  private long handleQueryResponseProto(HttpResponse paramHttpResponse, ConversationSink paramConversationSink)
    throws MailSync.ResponseParseException, IOException
  {
    MailProtocolInputStream localMailProtocolInputStream = newParserForProtoResponse(paramHttpResponse);
    while (true)
    {
      try
      {
        ProtoBuf localProtoBuf = localMailProtocolInputStream.readNextChunkPart();
        if ((localProtoBuf != null) && (localProtoBuf.has(15)))
        {
          long l = handleQueryResponseProtoParsed(localProtoBuf.getProtoBuf(15), paramConversationSink);
          return l;
        }
        Object[] arrayOfObject = new Object[1];
        if (localProtoBuf != null)
        {
          str = "null protoBuf";
          arrayOfObject[0] = str;
          LogUtils.w("Gmail", "handleQueryResponseProto: No query result found inside response chunk. reason: %s", arrayOfObject);
          throw new ResponseParseException("No query result found inside response chunk");
        }
      }
      finally
      {
        localMailProtocolInputStream.close();
      }
      String str = "missing query result";
    }
  }

  private long handleQueryResponseProtoParsed(ProtoBuf paramProtoBuf, ConversationSink paramConversationSink)
  {
    Timer.startTiming("MS.handleQueryResponse");
    paramConversationSink.prepareSink();
    try
    {
      ArrayList localArrayList = Lists.newArrayList();
      ProtoBufHelpers.getAllProtoBufs(paramProtoBuf, 3, localArrayList);
      Iterator localIterator = localArrayList.iterator();
      for (int i = 0; localIterator.hasNext(); i++)
      {
        ProtoBuf localProtoBuf = (ProtoBuf)localIterator.next();
        Conversation localConversation = new Conversation();
        localConversation.conversationId = localProtoBuf.getLong(1);
        localConversation.sortMessageId = localProtoBuf.getLong(2);
        localConversation.personalLevel = Gmail.PersonalLevel.fromInt(localProtoBuf.getInt(6));
        localConversation.maxMessageId = localProtoBuf.getLong(7);
        localConversation.numMessages = localProtoBuf.getInt(8);
        localConversation.hasAttachments = localProtoBuf.getBool(9);
        localConversation.date = localProtoBuf.getLong(3);
        localConversation.subject = localProtoBuf.getString(4);
        localConversation.snippet = localProtoBuf.getString(5);
        localConversation.labelIds = Sets.newHashSet();
        ProtoBufHelpers.getAllLongs(localProtoBuf, 11, localConversation.labelIds);
        localConversation.fromAddress = CompactSenderInstructions.instructionsStringFromProto(localProtoBuf.getProtoBuf(10));
        if (LogUtils.isLoggable("Gmail", 2))
        {
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = localConversation.toString();
          LogUtils.d("Gmail", "%s", arrayOfObject);
        }
        paramConversationSink.handleConversation(localConversation);
      }
      paramConversationSink.finalizeSink();
      Timer.stopTiming("MS.handleQueryResponse", i);
      return paramProtoBuf.getLong(2);
    }
    finally
    {
      paramConversationSink.finalizeSink();
    }
  }

  private void handleResponse(HttpResponse paramHttpResponse, MailSyncObserver paramMailSyncObserver, long paramLong, MailEngine.SyncInfo paramSyncInfo)
    throws MailSync.ResponseParseException, IOException
  {
    if (paramHttpResponse == null)
      throw new IOException("null HttpResponse in MailSync.handleResponse");
    Header localHeader = paramHttpResponse.getFirstHeader("Content-Type");
    if (localHeader == null)
      throw new IOException("Missing Content-Type header");
    String str = localHeader.getValue();
    if (str.startsWith("application/vnd.google-x-gms-proto"))
    {
      handleResponseProto(paramHttpResponse, paramMailSyncObserver, paramLong, paramSyncInfo);
      return;
    }
    if (str.startsWith("text/html"))
    {
      int i = paramHttpResponse.getStatusLine().getStatusCode();
      if (LogUtils.isLoggable("Gmail", 2))
        printHtmlResponse(paramHttpResponse);
      throw new IOException("Server returned unhandled response content type (text/html status: " + i + ")");
    }
    throw new IOException("Unknown response content type: " + str);
  }

  private void handleResponseProto(HttpResponse paramHttpResponse, MailSyncObserver paramMailSyncObserver, long paramLong, MailEngine.SyncInfo paramSyncInfo)
    throws MailSync.ResponseParseException, IOException
  {
    MailProtocolInputStream localMailProtocolInputStream = newParserForProtoResponse(paramHttpResponse);
    if (localMailProtocolInputStream.getResponseVersion() < 25)
      throw new IllegalArgumentException("Attempt to use protos for a version-" + localMailProtocolInputStream.getResponseVersion() + " response");
    ProtoBuf localProtoBuf1 = null;
    while (true)
    {
      ProtoBuf localProtoBuf2;
      try
      {
        if (this.mIsSyncCanceled)
        {
          if ((!this.mIsSyncCanceled) && (localProtoBuf1 != null))
            handleSyncPostambleProtoExceptLowestInDuration(localProtoBuf1, paramSyncInfo);
          return;
        }
        localProtoBuf2 = localMailProtocolInputStream.readNextChunkPart();
        if (localProtoBuf2 == null)
          continue;
        if (LogUtils.isLoggable("Gmail", 2))
          ProtoBufHelpers.printHttpResponseChunkProto(localProtoBuf2);
        if (localProtoBuf2.has(2))
        {
          handleConfigInfoProto(localProtoBuf2.getProtoBuf(2));
          continue;
        }
      }
      finally
      {
        localMailProtocolInputStream.close();
      }
      if (localProtoBuf2.has(3))
      {
        handleConfigAcceptedProto(localProtoBuf2.getProtoBuf(3));
      }
      else if (localProtoBuf2.has(4))
      {
        handleStartSyncInfoProto(localProtoBuf2.getProtoBuf(4), paramMailSyncObserver);
        paramSyncInfo.receivedHandledClientOp = true;
      }
      else if (localProtoBuf2.has(5))
      {
        handleUphillSyncProto(localProtoBuf2.getProtoBuf(5), paramSyncInfo);
      }
      else if (localProtoBuf2.has(7))
      {
        handleForwardSyncProto(localProtoBuf2.getProtoBuf(7), paramMailSyncObserver);
      }
      else if (localProtoBuf2.has(8))
      {
        handleCheckConversationProto(localProtoBuf2.getProtoBuf(8));
      }
      else if (localProtoBuf2.has(9))
      {
        handleConversationProto(localProtoBuf2.getProtoBuf(9), localMailProtocolInputStream, paramMailSyncObserver, paramLong, paramSyncInfo);
      }
      else if (localProtoBuf2.has(10))
      {
        handleMessageProto(localProtoBuf2.getProtoBuf(10), localMailProtocolInputStream, paramMailSyncObserver, paramLong, paramSyncInfo);
      }
      else if (localProtoBuf2.has(16))
      {
        handleNoConversationProto(localProtoBuf2.getProtoBuf(16));
      }
      else if (localProtoBuf2.has(17))
      {
        handleNoMessageProto(localProtoBuf2.getProtoBuf(17));
      }
      else
      {
        if (!localProtoBuf2.has(14))
          break;
        localProtoBuf1 = localProtoBuf2.getProtoBuf(14);
        if (localProtoBuf1.has(4))
        {
          setLongSetting("lowestMessageIdInDuration", localProtoBuf1.getLong(4));
          saveDirtySettings();
        }
      }
    }
    throw new ResponseParseException("No protobuf found inside response chunk");
  }

  private void handleStartSyncInfoProto(ProtoBuf paramProtoBuf, MailSyncObserver paramMailSyncObserver)
  {
    long l1 = paramProtoBuf.getLong(1);
    long l2 = paramProtoBuf.getLong(2);
    long l3 = paramProtoBuf.getLong(3);
    this.mStore.prepare();
    while (true)
    {
      HashMap localHashMap;
      int i;
      int j;
      ProtoBuf localProtoBuf;
      MailCore.Label localLabel;
      try
      {
        if (paramProtoBuf.has(6))
        {
          String str12 = paramProtoBuf.getString(6);
          LogUtils.v("Gmail", "StartSyncInfoProto: Custom From prefs: %s", new Object[] { str12 });
          if (!str12.isEmpty())
          {
            Map localMap = getCustomFromPrefs(str12, false);
            this.mStore.setCustomFromPreference(localMap, true);
          }
          if (paramProtoBuf.has(7))
          {
            String str13 = paramProtoBuf.getString(7);
            LogUtils.v("Gmail", "StartSyncInfoProto: Reply From prefs: %s", new Object[] { str13 });
            ImmutableMap localImmutableMap5 = ImmutableMap.of("bx_rf", str13);
            this.mStore.setServerPreferences(localImmutableMap5);
          }
        }
        if (paramProtoBuf.has(5))
        {
          String str11 = paramProtoBuf.getString(5);
          LogUtils.v("Gmail", "StartSyncInfoProto: Custom Color: %s", new Object[] { str11 });
          if (!str11.isEmpty())
            this.mStore.setCustomLabelColorPreference(getCustomLabelColorPrefs(str11));
        }
        if (paramProtoBuf.has(13))
        {
          String str10 = paramProtoBuf.getString(13);
          if (LogUtils.isLoggable("Gmail", 2))
          {
            LogUtils.v("Gmail", "StartSyncInfoProto: Reply To: %s", new Object[] { str10 });
            LogUtils.v("Gmail", "StartSyncInfoProto: Reply To: %s", new Object[] { str10 });
          }
          if ((str10 != null) && (!str10.isEmpty()) && (!str10.equals("null")))
          {
            ImmutableMap localImmutableMap4 = ImmutableMap.of("sx_rt", str10);
            this.mStore.setServerPreferences(localImmutableMap4);
          }
        }
        else
        {
          if (paramProtoBuf.has(14))
          {
            String str9 = paramProtoBuf.getString(14);
            LogUtils.v("Gmail", "StartSyncInfoProto: Display Name: %s", new Object[] { str9 });
            if ((str9 == null) || (str9.isEmpty()) || (str9.equals("null")))
              break label700;
            ImmutableMap localImmutableMap2 = ImmutableMap.of("sx_dn", str9);
            this.mStore.setServerPreferences(localImmutableMap2);
          }
          if (paramProtoBuf.has(8))
          {
            String str4 = paramProtoBuf.getString(8);
            String str5 = paramProtoBuf.getString(10);
            String str6 = paramProtoBuf.getString(9);
            String str7 = paramProtoBuf.getString(11);
            String str8 = paramProtoBuf.getString(15);
            if (LogUtils.isLoggable("Gmail", 2))
            {
              LogUtils.v("Gmail", "StartSyncInfoProto: IO enabled: %s", new Object[] { str4 });
              LogUtils.v("Gmail", "StartSyncInfoProto: IO sections: %s", new Object[] { str5 });
              LogUtils.v("Gmail", "StartSyncInfoProto: IO sizes: %s", new Object[] { str6 });
              LogUtils.v("Gmail", "StartSyncInfoProto: IO default inbox: %s", new Object[] { str7 });
              LogUtils.v("Gmail", "StartSyncInfoProto: IO arrows off: %s", new Object[] { str8 });
            }
            if (str4 != null)
              this.mStore.setInfoOverloadEnabledPreference(str4);
            if (str8 != null)
              this.mStore.setInfoOverloadArrowsOffPreference(str8.toString());
          }
          if (LogUtils.isLoggable("Gmail", 2))
            ProtoBufHelpers.printStartSyncInfoProto(paramProtoBuf);
          localHashMap = Maps.newHashMap();
          i = paramProtoBuf.getCount(4);
          j = 0;
          if (j >= i)
            break label855;
          localProtoBuf = paramProtoBuf.getProtoBuf(4, j);
          long l4 = localProtoBuf.getLong(1);
          localLabel = this.mStore.getOrAddLabel(l4);
          if (localLabel != null)
            break label724;
          break label896;
        }
        ImmutableMap localImmutableMap3 = ImmutableMap.of("sx_rt", "");
        this.mStore.setServerPreferences(localImmutableMap3);
        continue;
      }
      finally
      {
        this.mStore.commit();
      }
      label700: ImmutableMap localImmutableMap1 = ImmutableMap.of("sx_dn", "");
      this.mStore.setServerPreferences(localImmutableMap1);
      continue;
      label724: String str1 = localProtoBuf.getString(2);
      String str2 = localProtoBuf.getString(3);
      int k = localProtoBuf.getInt(4);
      int m = localProtoBuf.getInt(5);
      int n = 2147483647;
      if (localProtoBuf.has(6))
        n = localProtoBuf.getInt(6);
      String str3 = "SHOW";
      if (localProtoBuf.has(7))
      {
        str3 = localProtoBuf.getString(7);
        LogUtils.v("Gmail", "StartSyncInfoProto: LABEL_VISIBILITY: %s", new Object[] { str3 });
      }
      localHashMap.put(localLabel, new MailStore.LabelInfo(str1, str2, k, m, n, str3));
      break label896;
      label855: if ((i > 0) && (paramMailSyncObserver != null))
        paramMailSyncObserver.onOperationReceived();
      handleLabelsValues(localHashMap, paramMailSyncObserver);
      handleStartSyncInfoValues(l3, l2, l1);
      this.mStore.commit();
      return;
      label896: j++;
    }
  }

  private void handleSyncPostambleProtoExceptLowestInDuration(ProtoBuf paramProtoBuf, MailEngine.SyncInfo paramSyncInfo)
  {
    if (LogUtils.isLoggable("Gmail", 2))
      ProtoBufHelpers.printSyncPostambleProto(paramProtoBuf);
    if (paramProtoBuf.has(1))
      handleLastExaminedServerOperationValues(paramProtoBuf.getLong(1));
    if (paramProtoBuf.has(2))
      handleLowestBackwardConversationIdValues(paramProtoBuf.getLong(2));
    if (paramSyncInfo.normalSync)
      setBooleanSetting("moreForwardSyncNeeded", paramProtoBuf.getBool(3));
    HashSet localHashSet;
    while (true)
    {
      ArrayList localArrayList = Lists.newArrayList();
      ProtoBufHelpers.getAllProtoBufs(paramProtoBuf, 5, localArrayList);
      localHashSet = Sets.newHashSet();
      this.mStore.prepare();
      try
      {
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          ProtoBuf localProtoBuf = (ProtoBuf)localIterator.next();
          int i = 2147483647;
          if (localProtoBuf.has(4))
            i = localProtoBuf.getInt(4);
          String str = "SHOW";
          if (localProtoBuf.has(5))
          {
            str = localProtoBuf.getString(5);
            LogUtils.v("Gmail", "ResponseSyncPostamble: LABEL_VISIBILITY: %s", new Object[] { str });
          }
          int j = localProtoBuf.getInt(2);
          int k = localProtoBuf.getInt(3);
          MailCore.Label localLabel = this.mStore.getLabelOrNull(localProtoBuf.getLong(1));
          if (localLabel != null)
          {
            this.mStore.setLabelCounts(localLabel, j, k, i, str);
            localHashSet.add(Long.valueOf(localLabel.id));
          }
        }
      }
      finally
      {
        this.mStore.commit();
      }
      setBooleanSetting("moreForwardSyncNeeded", false);
    }
    this.mStore.commit();
    this.mStore.notifyLabelChanges(localHashSet);
  }

  private void handleUphillSyncProto(ProtoBuf paramProtoBuf, MailEngine.SyncInfo paramSyncInfo)
    throws IOException
  {
    if (LogUtils.isLoggable("Gmail", 2))
    {
      ProtoBufHelpers.printUphillSyncProto(paramProtoBuf);
      LogUtils.d("Gmail", paramSyncInfo.toString(), new Object[0]);
    }
    int i = paramProtoBuf.getCount(1);
    this.mStore.prepare();
    int j = 0;
    if (j < i);
    while (true)
    {
      int n;
      try
      {
        ProtoBuf localProtoBuf2 = paramProtoBuf.getProtoBuf(1, j);
        handleMessageNotHandledValues(localProtoBuf2.getLong(1), localProtoBuf2.getString(2));
        j++;
        break;
        int k = paramProtoBuf.getCount(2);
        int m = 0;
        n = 0;
        if (n < k)
        {
          ProtoBuf localProtoBuf1 = paramProtoBuf.getProtoBuf(2, n);
          long l1 = localProtoBuf1.getLong(1);
          if ((!paramSyncInfo.normalSync) && (l1 == paramSyncInfo.messageId))
            m = 1;
          long l2 = localProtoBuf1.getLong(2);
          long l3 = localProtoBuf1.getLong(3);
          handleMessageSavedOrSentValues(l1, l2, l3);
          if ((!paramSyncInfo.normalSync) && ((paramSyncInfo.conversationId == l3) || (paramSyncInfo.conversationId == l1)) && (paramSyncInfo.messageId == l1))
            this.mStore.addSendWithoutSyncConversationInfoToCheck(l3, l2);
        }
        else
        {
          if ((paramProtoBuf.has(3)) && (paramSyncInfo.normalSync))
          {
            onServerHasHandledClientOperationdId(paramProtoBuf.getLong(3));
            paramSyncInfo.receivedHandledClientOp = true;
            return;
          }
          if ((paramSyncInfo.normalSync) || (m == 0))
            continue;
          this.mStore.removeOperationByMessageId(paramSyncInfo.messageId);
          paramSyncInfo.receivedHandledClientOp = true;
          continue;
        }
      }
      finally
      {
        this.mStore.commit();
      }
      n++;
    }
  }

  private boolean hasSetting(String paramString)
  {
    return this.mSettings.containsKey(paramString);
  }

  private void incStats(int paramInt)
  {
    long[] arrayOfLong = this.mCounters;
    arrayOfLong[paramInt] = (1L + arrayOfLong[paramInt]);
  }

  private void incStats(int paramInt, long paramLong)
  {
    long[] arrayOfLong = this.mCounters;
    arrayOfLong[paramInt] = (paramLong + arrayOfLong[paramInt]);
  }

  private void initDictionary(Dictionary paramDictionary, long paramLong1, long paramLong2, boolean paramBoolean)
  {
    Cursor localCursor = this.mStore.getMessageCursorForConversationId(new String[] { "messageId", "body" }, paramLong2);
    try
    {
      while (localCursor.moveToNext())
      {
        if (localCursor.getLong(0) == paramLong1)
        {
          if (paramBoolean)
            paramDictionary.append(getBodyAsBytes(localCursor));
          return;
        }
        paramDictionary.append(getBodyAsBytes(localCursor));
      }
    }
    finally
    {
      localCursor.close();
    }
    localCursor.close();
  }

  private void onFinishedHandlingForwardOperation(long paramLong, MailSyncObserver paramMailSyncObserver)
  {
    setLongSetting("highestProcessedServerOperationId", paramLong);
    saveDirtySettings();
    if (paramMailSyncObserver != null)
      paramMailSyncObserver.onOperationReceived();
  }

  private void printHtmlResponse(HttpResponse paramHttpResponse)
    throws IOException
  {
    HttpEntity localHttpEntity = paramHttpResponse.getEntity();
    if (localHttpEntity.getContentLength() < 2147483647L)
    {
      InputStreamReader localInputStreamReader = new InputStreamReader(AndroidHttpClient.getUngzippedContent(localHttpEntity), "UTF-8");
      int i = (int)localHttpEntity.getContentLength();
      if (i < 0)
        i = 4096;
      CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(i);
      try
      {
        char[] arrayOfChar = new char[1024];
        long l = SystemClock.elapsedRealtime();
        do
        {
          int j = localInputStreamReader.read(arrayOfChar);
          if (j == -1)
            break;
          localCharArrayBuffer.append(arrayOfChar, 0, j);
        }
        while (SystemClock.elapsedRealtime() - l <= 3600000L);
        localCharArrayBuffer.append("\nRead timed out...");
        localInputStreamReader.close();
        LogUtils.i("Gmail", "Html Response from html content = %s", new Object[] { localCharArrayBuffer });
        return;
      }
      finally
      {
        localInputStreamReader.close();
      }
    }
    LogUtils.i("Gmail", "Response too large to print", new Object[0]);
  }

  private String readEmailFromProto(ProtoBuf paramProtoBuf)
  {
    String str1 = paramProtoBuf.getString(1);
    if (paramProtoBuf.has(2))
    {
      String str2 = paramProtoBuf.getString(2);
      str1 = "\"" + str2 + "\" <" + str1 + ">";
    }
    return str1;
  }

  private static <E> boolean removeFromSetElementsInSet(Set<E> paramSet1, Set<E> paramSet2)
  {
    boolean bool;
    if (paramSet2 == null)
      bool = false;
    while (true)
    {
      return bool;
      bool = false;
      Iterator localIterator = paramSet1.iterator();
      while (localIterator.hasNext())
        if (paramSet2.contains(localIterator.next()))
        {
          localIterator.remove();
          bool = true;
        }
    }
  }

  private static <E> boolean removeFromSetElementsNotInSet(Set<E> paramSet1, Set<E> paramSet2)
  {
    boolean bool;
    if (paramSet2 == null)
      bool = false;
    while (true)
    {
      return bool;
      bool = false;
      Iterator localIterator = paramSet1.iterator();
      while (localIterator.hasNext())
        if (!paramSet2.contains(localIterator.next()))
        {
          localIterator.remove();
          bool = true;
        }
    }
  }

  private boolean setStringSetSetting(String paramString, Set<String> paramSet)
  {
    Object[] arrayOfObject = paramSet.toArray();
    Arrays.sort(arrayOfObject);
    return setStringSetting(paramString, TextUtils.join(" ", arrayOfObject));
  }

  private boolean setStringSetting(String paramString1, String paramString2)
  {
    if ((!this.mSettings.containsKey(paramString1)) || (!((String)this.mSettings.get(paramString1)).equals(paramString2)))
    {
      this.mSettings.put(paramString1, paramString2);
      this.mDirtySettings.put(paramString1, paramString2);
      return true;
    }
    return false;
  }

  private static SyncRationale syncRationaleFromProto(int paramInt)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("Unknown proto rationale: " + paramInt);
    case 0:
      return SyncRationale.NONE;
    case 1:
      return SyncRationale.DURATION;
    case 2:
    }
    return SyncRationale.LABEL;
  }

  public static String unEscapeString(String paramString, char paramChar1, char paramChar2)
  {
    if (paramString == null)
      return null;
    StringBuilder localStringBuilder = new StringBuilder(paramString.length());
    int i = 0;
    int j = 0;
    if (j < paramString.length())
    {
      char c = paramString.charAt(j);
      if ((i != 0) && (c != paramChar1))
      {
        localStringBuilder.append(c);
        i = 0;
      }
      while (true)
      {
        j++;
        break;
        if ((c == paramChar1) && (paramString.charAt(j + 1) == paramChar2))
          i = 1;
        else
          localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }

  private MailProtocolInputStream unzipMessageBatch(ProtoBuf paramProtoBuf, long paramLong)
  {
    incStats(7);
    byte[] arrayOfByte = paramProtoBuf.getBytes(1);
    long l1 = paramProtoBuf.getLong(2);
    long l2 = paramProtoBuf.getLong(3);
    Dictionary localDictionary = new Dictionary();
    try
    {
      initDictionary(localDictionary, l1, paramLong, true);
      long l3 = localDictionary.getChecksum();
      if (l2 != l3)
      {
        Object[] arrayOfObject2 = new Object[3];
        arrayOfObject2[0] = Long.valueOf(paramLong);
        arrayOfObject2[1] = Long.valueOf(l2);
        arrayOfObject2[2] = Long.valueOf(l3);
        LogUtils.e("Gmail", "Dictionary checksum mismatch for conversation %d. Expected %x but was %x", arrayOfObject2);
        incStats(8);
        return null;
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Long.valueOf(paramLong);
      LogUtils.e("Gmail", localOutOfMemoryError, "Out of memory while creating dictionary for conversation %d", arrayOfObject1);
      return null;
    }
    return new MailProtocolInputStream(ZipUtils.inflateToStream(arrayOfByte, localDictionary.getBytes()));
  }

  // ERROR //
  private String unzipMessageBody(byte[] paramArrayOfByte, long paramLong1, long paramLong2, long paramLong3, Dictionary paramDictionary)
  {
    // Byte code:
    //   0: ldc_w 1186
    //   3: invokestatic 539	com/google/android/gm/perf/Timer:startTiming	(Ljava/lang/String;)V
    //   6: aload_0
    //   7: bipush 7
    //   9: invokespecial 499	com/google/android/gm/provider/MailSync:incStats	(I)V
    //   12: new 1188	java/util/zip/Inflater
    //   15: dup
    //   16: invokespecial 1189	java/util/zip/Inflater:<init>	()V
    //   19: astore 9
    //   21: aload 9
    //   23: aload_1
    //   24: invokevirtual 1192	java/util/zip/Inflater:setInput	([B)V
    //   27: aload 9
    //   29: invokestatic 1196	com/google/android/gm/provider/ZipUtils:inflate	(Ljava/util/zip/Inflater;)[B
    //   32: astore 13
    //   34: aload 13
    //   36: arraylength
    //   37: ifne +129 -> 166
    //   40: aload 9
    //   42: invokevirtual 1199	java/util/zip/Inflater:needsDictionary	()Z
    //   45: ifeq +121 -> 166
    //   48: aload 8
    //   50: invokevirtual 1200	com/google/android/gm/provider/Dictionary:size	()I
    //   53: ifne +14 -> 67
    //   56: aload_0
    //   57: aload 8
    //   59: lload 4
    //   61: lload 6
    //   63: iconst_0
    //   64: invokespecial 1161	com/google/android/gm/provider/MailSync:initDictionary	(Lcom/google/android/gm/provider/Dictionary;JJZ)V
    //   67: aload 8
    //   69: invokevirtual 1164	com/google/android/gm/provider/Dictionary:getChecksum	()J
    //   72: lstore 18
    //   74: lload_2
    //   75: lload 18
    //   77: lcmp
    //   78: ifeq +64 -> 142
    //   81: iconst_4
    //   82: anewarray 4	java/lang/Object
    //   85: astore 20
    //   87: aload 20
    //   89: iconst_0
    //   90: lload 4
    //   92: invokestatic 478	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   95: aastore
    //   96: aload 20
    //   98: iconst_1
    //   99: lload 6
    //   101: invokestatic 478	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   104: aastore
    //   105: aload 20
    //   107: iconst_2
    //   108: lload_2
    //   109: invokestatic 478	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   112: aastore
    //   113: aload 20
    //   115: iconst_3
    //   116: lload 18
    //   118: invokestatic 478	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   121: aastore
    //   122: ldc_w 480
    //   125: ldc_w 1202
    //   128: aload 20
    //   130: invokestatic 658	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   133: pop
    //   134: aload_0
    //   135: bipush 8
    //   137: invokespecial 499	com/google/android/gm/provider/MailSync:incStats	(I)V
    //   140: aconst_null
    //   141: areturn
    //   142: aload 9
    //   144: aload 8
    //   146: invokevirtual 1171	com/google/android/gm/provider/Dictionary:getBytes	()[B
    //   149: invokevirtual 1205	java/util/zip/Inflater:setDictionary	([B)V
    //   152: aload 9
    //   154: invokestatic 1196	com/google/android/gm/provider/ZipUtils:inflate	(Ljava/util/zip/Inflater;)[B
    //   157: astore 13
    //   159: aload 8
    //   161: aload 13
    //   163: invokevirtual 1054	com/google/android/gm/provider/Dictionary:append	([B)V
    //   166: new 58	java/lang/String
    //   169: dup
    //   170: aload 13
    //   172: ldc_w 321
    //   175: invokespecial 1208	java/lang/String:<init>	([BLjava/lang/String;)V
    //   178: astore 14
    //   180: iconst_3
    //   181: anewarray 4	java/lang/Object
    //   184: astore 15
    //   186: aload 15
    //   188: iconst_0
    //   189: aload 14
    //   191: invokevirtual 1148	java/lang/String:length	()I
    //   194: invokestatic 1211	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   197: aastore
    //   198: aload 15
    //   200: iconst_1
    //   201: aload_1
    //   202: arraylength
    //   203: invokestatic 1211	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   206: aastore
    //   207: aload 15
    //   209: iconst_2
    //   210: aload 14
    //   212: invokevirtual 1148	java/lang/String:length	()I
    //   215: i2f
    //   216: aload_1
    //   217: arraylength
    //   218: i2f
    //   219: fdiv
    //   220: invokestatic 1216	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   223: aastore
    //   224: ldc_w 480
    //   227: ldc_w 1218
    //   230: aload 15
    //   232: invokestatic 488	com/google/android/gm/provider/LogUtils:v	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   235: pop
    //   236: ldc_w 1186
    //   239: invokestatic 1220	com/google/android/gm/perf/Timer:stopTiming	(Ljava/lang/String;)V
    //   242: aload 14
    //   244: areturn
    //   245: astore 10
    //   247: iconst_1
    //   248: anewarray 4	java/lang/Object
    //   251: astore 11
    //   253: aload 11
    //   255: iconst_0
    //   256: lload 4
    //   258: invokestatic 478	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   261: aastore
    //   262: ldc_w 480
    //   265: aload 10
    //   267: ldc_w 1222
    //   270: aload 11
    //   272: invokestatic 616	com/google/android/gm/provider/LogUtils:e	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   275: pop
    //   276: aload_0
    //   277: bipush 8
    //   279: invokespecial 499	com/google/android/gm/provider/MailSync:incStats	(I)V
    //   282: aconst_null
    //   283: areturn
    //   284: astore 17
    //   286: new 327	java/lang/IllegalStateException
    //   289: dup
    //   290: ldc_w 329
    //   293: invokespecial 331	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   296: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   27	67	245	java/util/zip/DataFormatException
    //   67	74	245	java/util/zip/DataFormatException
    //   81	140	245	java/util/zip/DataFormatException
    //   142	166	245	java/util/zip/DataFormatException
    //   166	180	284	java/io/UnsupportedEncodingException
  }

  private void waitUntilEarliestAllowedSyncTime()
  {
    while (true)
    {
      long l1 = SystemClock.elapsedRealtime();
      if (l1 >= this.mEarliestAllowedSyncTimeAsElapsedRealtime)
        return;
      try
      {
        long l2 = this.mEarliestAllowedSyncTimeAsElapsedRealtime - l1;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Long.valueOf(l2);
        LogUtils.i("Gmail", "Sync waiting for %d ms", arrayOfObject);
        Thread.sleep(l2);
      }
      catch (InterruptedException localInterruptedException)
      {
      }
    }
  }

  void checkLabelsSets(Set<String> paramSet1, Set<String> paramSet2, Set<String> paramSet3)
  {
    Set localSet1 = getStringSetSetting("labelsIncluded");
    Set localSet2 = getStringSetSetting("labelsPartial");
    if (removeFromSetElementsInSet(localSet1, paramSet2));
    for (boolean bool1 = true; ; bool1 = false)
    {
      if (removeFromSetElementsInSet(localSet2, paramSet2))
        bool1 = true;
      if (removeFromSetElementsNotInSet(localSet1, paramSet3))
        bool1 = true;
      if (removeFromSetElementsNotInSet(localSet2, paramSet3))
        bool1 = true;
      if (paramSet3 == null)
        if (hasSetting("labelsAll"))
        {
          paramSet3 = getStringSetSetting("labelsAll");
          if (paramSet1 != null)
            bool1 |= paramSet3.addAll(paramSet1);
          if (paramSet2 != null)
            bool1 |= paramSet3.removeAll(paramSet2);
        }
      while ((paramSet3 == null) || (paramSet3.size() == 0))
      {
        return;
        bool1 = true;
      }
      Iterator localIterator1 = LabelManager.getForcedIncludedLabels().iterator();
      boolean bool2 = bool1;
      label150: String str2;
      int j;
      if (localIterator1.hasNext())
      {
        str2 = (String)localIterator1.next();
        if ((paramSet3.contains(str2)) || (MailCore.isCanonicalLabelNameLocal(str2)))
        {
          j = 1;
          label194: if (j == 0)
            break label425;
        }
      }
      label425: for (boolean bool3 = bool2 | localSet1.add(str2) | localSet2.remove(str2); ; bool3 = bool2)
      {
        bool2 = bool3;
        break label150;
        j = 0;
        break label194;
        Iterator localIterator2 = LabelManager.getForcedIncludedOrPartialLabels().iterator();
        label328: 
        while (localIterator2.hasNext())
        {
          String str1 = (String)localIterator2.next();
          if ((paramSet3.contains(str1)) || (MailCore.isCanonicalLabelNameLocal(str1)));
          for (int i = 1; ; i = 0)
          {
            if ((i == 0) || (localSet1.contains(str1)))
              break label328;
            bool2 |= localSet2.add(str1);
            break;
          }
        }
        if (!bool2)
          break;
        if (LogUtils.isLoggable("Gmail", 4))
        {
          Object[] arrayOfObject = new Object[3];
          arrayOfObject[0] = LogUtils.labelSetToString(localSet1);
          arrayOfObject[1] = LogUtils.labelSetToString(localSet2);
          arrayOfObject[2] = LogUtils.labelSetToString(paramSet3);
          LogUtils.i("Gmail", "checkLabelsSets changed the label sets to: included(%s), partial(%s), all(%s)", arrayOfObject);
        }
        setStringSetSetting("labelsIncluded", localSet1);
        setStringSetSetting("labelsPartial", localSet2);
        setStringSetSetting("labelsAll", paramSet3);
        this.mStore.updateNotificationLabels();
        return;
      }
    }
  }

  public void clearStats()
  {
    this.mCounters = new long[SYNC_STATS_LABELS.length];
  }

  boolean getBooleanSetting(String paramString)
  {
    if (this.mSettings.containsKey(paramString))
      return Long.parseLong((String)this.mSettings.get(paramString)) != 0L;
    throw new IllegalStateException("missing setting: " + paramString);
  }

  public long getClientId()
  {
    return getLongSetting("clientId");
  }

  long getClientOperationToAck()
  {
    return getLongSetting("clientOpToAck");
  }

  public long getConversationAgeDays()
  {
    return getLongSetting("conversationAgeDays");
  }

  Map<String, MailStore.CustomFromPreference> getCustomFromPrefs(String paramString, boolean paramBoolean)
  {
    HashMap localHashMap = new HashMap();
    String[] arrayOfString1;
    if (!paramBoolean)
      arrayOfString1 = TextUtils.split(paramString, "(?<!\\\\)#");
    while (true)
    {
      int i = 0;
      label24: if (i < arrayOfString1.length)
      {
        String[] arrayOfString2 = TextUtils.split(unEscapeString(arrayOfString1[i], '\\', '#'), "(?<!\\\\),");
        String str;
        if ((arrayOfString2.length < 4) || (TextUtils.isEmpty(arrayOfString2[3])))
          str = arrayOfString2[1];
        try
        {
          while (true)
          {
            MailStore.CustomFromPreference localCustomFromPreference = new MailStore.CustomFromPreference(unEscapeString(arrayOfString2[0], '\\', ','), arrayOfString2[1], arrayOfString2[2], str);
            if (arrayOfString2[1].contains("@"))
              localHashMap.put(arrayOfString2[1], localCustomFromPreference);
            i++;
            break label24;
            arrayOfString1 = new String[] { paramString };
            break;
            str = arrayOfString2[3];
          }
        }
        catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
        {
          while (true)
          {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = arrayOfString1[i];
            LogUtils.v("Gmail", "Unexpected Custom from preference received: %s", arrayOfObject);
          }
        }
      }
    }
    return localHashMap;
  }

  Map<String, MailStore.CustomLabelColorPreference> getCustomLabelColorPrefs(String paramString)
  {
    HashMap localHashMap = new HashMap();
    String[] arrayOfString1 = TextUtils.split(paramString, ",");
    int i = 0;
    while (true)
      if (i < arrayOfString1.length)
      {
        String[] arrayOfString2 = TextUtils.split(arrayOfString1[i], ":");
        try
        {
          MailStore.CustomLabelColorPreference localCustomLabelColorPreference = new MailStore.CustomLabelColorPreference(arrayOfString2[1], arrayOfString2[2]);
          localHashMap.put(arrayOfString2[0], localCustomLabelColorPreference);
          i++;
        }
        catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
        {
          while (true)
            LogUtils.v("Gmail", "Unexpected Custom Color preference received: %s", new Object[] { paramString });
        }
      }
    return localHashMap;
  }

  int getIntegerSetting(String paramString)
  {
    if (this.mSettings.containsKey(paramString))
      return Integer.parseInt((String)this.mSettings.get(paramString));
    throw new IllegalStateException("missing setting: " + paramString);
  }

  public String[] getLabelsAllArray()
  {
    return TextUtils.split(getStringSetting("labelsAll"), LABEL_SEPARATOR_PATTERN);
  }

  public String getLabelsIncluded()
  {
    return getStringSetting("labelsIncluded");
  }

  public String[] getLabelsIncludedArray()
  {
    return TextUtils.split(getStringSetting("labelsIncluded"), LABEL_SEPARATOR_PATTERN);
  }

  public String getLabelsPartial()
  {
    return getStringSetting("labelsPartial");
  }

  public String[] getLabelsPartialArray()
  {
    return TextUtils.split(getStringSetting("labelsPartial"), LABEL_SEPARATOR_PATTERN);
  }

  long getLongSetting(String paramString)
  {
    if (this.mSettings.containsKey(paramString))
      return Long.parseLong((String)this.mSettings.get(paramString));
    throw new IllegalStateException("missing setting: " + paramString);
  }

  public long getLowestMessageIdInDurationOrZero()
  {
    if (this.mSettings.containsKey("lowestMessageIdInDuration"))
      return getLongSetting("lowestMessageIdInDuration");
    return 0L;
  }

  public long getMaxAttachmentSizeMb()
  {
    return getLongSetting("maxAttachmentSize");
  }

  public long getMaxUnsyncedMessageIdForLabel(MailCore.Label paramLabel)
  {
    Set localSet1 = getStringSetSetting("labelsIncluded");
    Set localSet2 = getStringSetSetting("labelsPartial");
    String str = this.mStore.getLabelCanonicalNameOrNull(paramLabel);
    if (getLongSetting("clientId") == 0L);
    while ((paramLabel != null) && (MailCore.isLabelIdLocal(paramLabel.id)))
      return 0L;
    long l1;
    if (str == null)
      l1 = 9223372036854775807L;
    while (true)
    {
      long l2 = 9223372036854775807L;
      if (this.mSettings.containsKey("lowestBackwardConversationId"))
        l2 = getLongSetting("lowestBackwardConversationId");
      return Math.max(l1, l2);
      if (localSet1.contains(str))
        l1 = 0L;
      else if (localSet2.contains(str))
      {
        if (this.mSettings.containsKey("lowestMessageIdInDuration"))
          l1 = getLongSetting("lowestMessageIdInDuration") - 1L;
        else
          l1 = 9223372036854775807L;
      }
      else
        l1 = 9223372036854775807L;
    }
  }

  MailCore.Label getOrAddLabelIfUserMeaningful(MailPullParser paramMailPullParser, String paramString)
    throws SimplePullParser.ParseException
  {
    long l = paramMailPullParser.getLongAttribute(NAMESPACE, "labelId");
    return this.mStore.getOrAddLabel(l);
  }

  public int getRequestVersion()
  {
    int i = (int)getLongSetting("serverVersion");
    if (i == 0)
      return 25;
    if (i < 10)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(i);
      arrayOfObject[1] = Integer.valueOf(10);
      LogUtils.w("Gmail", "Server version (%d) is too old to talk to. Minimum supported version is %d", arrayOfObject);
      return 10;
    }
    return Math.min(i, 25);
  }

  public long getServerVersion()
  {
    return getLongSetting("serverVersion");
  }

  protected String getStats(SyncResult paramSyncResult)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < SYNC_STATS_LABELS.length; i++)
      if (this.mCounters[i] > 0L)
        localStringBuffer.append(SYNC_STATS_LABELS[i]).append(this.mCounters[i]);
    localStringBuffer.append(paramSyncResult.toDebugString());
    return localStringBuffer.toString();
  }

  public boolean getUnackedSentOperations()
  {
    return getBooleanSetting("unackedSentOperations");
  }

  void handleConfigAcceptedValues(long paramLong)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Long.valueOf(paramLong);
    LogUtils.d("Gmail", "handleConfigAcceptedValues: %d", arrayOfObject);
    long l = getLongSetting("clientId");
    if ((l != 0L) && (paramLong != l))
      throw new IllegalStateException("Client id is already set but response has different id");
    setLongSetting("clientId", paramLong);
    setBooleanSetting("configDirty", false);
    setBooleanSetting("startSyncNeeded", true);
    setLongSetting("highestBackwardConversationId", 0L);
    setLongSetting("lowestBackwardConversationId", 0L);
    saveDirtySettings();
  }

  void handleConfigInfoValues(long paramLong, Set<String> paramSet1, Set<String> paramSet2)
  {
    int i = getMinServerVersionForConfigInfo();
    long l = getServerVersion();
    if (i > l)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Long.valueOf(l);
      arrayOfObject[1] = Integer.valueOf(i);
      LogUtils.w("Gmail", "Ignoring config info from server because server version is %d but gservices says that the min server version is %d", arrayOfObject);
      setBooleanSetting("needConfigSuggestion", false);
      return;
    }
    setConfig(TextUtils.join(" ", paramSet1), TextUtils.join(" ", paramSet2), Long.valueOf(paramLong), Long.valueOf(0L));
    setBooleanSetting("needConfigSuggestion", false);
    setBooleanSetting("configDirty", true);
  }

  public void handleFetchConversationResponse(HttpResponse paramHttpResponse, long paramLong)
    throws MailSync.ResponseParseException, IOException
  {
    handleResponse(paramHttpResponse, null, paramLong, new MailEngine.SyncInfo());
  }

  void handleLabelsValues(Map<MailCore.Label, MailStore.LabelInfo> paramMap, MailSyncObserver paramMailSyncObserver)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramMap.size());
    LogUtils.d("Gmail", "handleLabelsValues: receiving set of %d labels", arrayOfObject);
    HashMap localHashMap = Maps.newHashMap();
    HashSet localHashSet = Sets.newHashSet();
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      MailCore.Label localLabel = (MailCore.Label)localEntry.getKey();
      MailStore.LabelInfo localLabelInfo = (MailStore.LabelInfo)localEntry.getValue();
      localHashMap.put(Long.valueOf(localLabel.id), localLabelInfo);
      localHashSet.add(localLabelInfo.canonicalName);
    }
    this.mStore.setLabels(localHashMap);
    checkLabelsSets(null, null, localHashSet);
    saveDirtySettings();
    if (paramMailSyncObserver != null)
      paramMailSyncObserver.onOperationReceived();
  }

  void handleLastExaminedServerOperationValues(long paramLong)
  {
    if (paramLong == 0L)
      this.mStore.wipeAndResync("Received operationId of 0 as last-examined-server-op. Wiping.");
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Long.valueOf(paramLong);
    LogUtils.d("Gmail", "last-examined-server-operation operationId: %d", arrayOfObject);
    setLongSetting("highestProcessedServerOperationId", paramLong);
    saveDirtySettings();
  }

  void handleLowestBackwardConversationIdValues(long paramLong)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Long.valueOf(paramLong);
    LogUtils.v("Gmail", "lowest-backward-convesation-id conversationid: %d", arrayOfObject);
    setLongSetting("lowestBackwardConversationId", paramLong);
  }

  void handleMessageSavedOrSentValues(long paramLong1, long paramLong2, long paramLong3)
    throws IOException
  {
    if (this.mFakeIoExceptionWhenHandlingMessageSavedOrSent)
    {
      this.mFakeIoExceptionWhenHandlingMessageSavedOrSent = false;
      throw new IOException("Faked by mFakeIoExceptionWhenHandlingMessageSavedOrSent");
    }
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Long.valueOf(paramLong1);
    arrayOfObject[1] = Long.valueOf(paramLong2);
    LogUtils.d("Gmail", "message id changed from %d to %d", arrayOfObject);
    this.mStore.updateSentOrSavedMessage(paramLong1, paramLong2, paramLong3);
  }

  void handleOperationLabelCreatedValues(MailCore.Label paramLabel, String paramString1, String paramString2)
  {
    if (paramLabel != null)
    {
      setBooleanSetting("startSyncNeeded", true);
      this.mStore.renameLabel(paramLabel, paramString1, paramString2, 2147483647, "SHOW");
      checkLabelsSets(Sets.newHashSet(new String[] { paramString1 }), null, null);
      saveDirtySettings();
    }
    incStats(1);
  }

  void handleOperationLabelDeletedValues(MailCore.Label paramLabel)
  {
    if (paramLabel != null)
    {
      String str = this.mStore.getLabelCanonicalNameOrNull(paramLabel);
      this.mStore.deleteLabel(paramLabel);
      if (str != null)
        checkLabelsSets(null, Sets.newHashSet(new String[] { str }), null);
      saveDirtySettings();
    }
    incStats(1);
  }

  void handleOperationLabelRenamedValues(MailCore.Label paramLabel, String paramString1, String paramString2)
  {
    if (paramLabel != null)
    {
      setBooleanSetting("startSyncNeeded", true);
      this.mStore.renameLabel(paramLabel, paramString1, paramString2, 2147483647, "SHOW");
      ArrayList localArrayList = Lists.newArrayList(new String[] { "labelsIncluded", "labelsPartial", "labelsAll" });
      String str1 = this.mStore.getLabelCanonicalNameOrNull(paramLabel);
      if (str1 != null)
      {
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          String str2 = (String)localIterator.next();
          Set localSet = getStringSetSetting(str2);
          if (localSet.contains(str1))
          {
            localSet.remove(str1);
            localSet.add(paramString1);
            setStringSetSetting(str2, localSet);
          }
        }
      }
    }
    incStats(1);
  }

  public long handleQueryResponse(HttpResponse paramHttpResponse, ConversationSink paramConversationSink)
    throws MailSync.ResponseParseException, IOException
  {
    String str = paramHttpResponse.getFirstHeader("Content-Type").getValue();
    if (str.startsWith("application/vnd.google-x-gms-proto"))
      return handleQueryResponseProto(paramHttpResponse, paramConversationSink);
    if (str.startsWith("text/html"))
    {
      int i = paramHttpResponse.getStatusLine().getStatusCode();
      if (LogUtils.isLoggable("Gmail", 2))
        printHtmlResponse(paramHttpResponse);
      throw new ResponseParseException("Server returned unhandled response content type (text/html status: " + i + ")");
    }
    throw new ResponseParseException("Unknown response content type: " + str);
  }

  void handleStartSyncInfoValues(long paramLong1, long paramLong2, long paramLong3)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Long.valueOf(paramLong1);
    arrayOfObject[1] = Long.valueOf(paramLong2);
    arrayOfObject[2] = Long.valueOf(paramLong3);
    LogUtils.v("Gmail", "handleStartSyncInfoValues: highestServerOperation: %d highestServerConversation: %d handledClientOperation: %d", arrayOfObject);
    if (getBooleanSetting("startSyncNeeded"))
    {
      setBooleanSetting("startSyncNeeded", false);
      if (getLongSetting("highestProcessedServerOperationId") == 0L)
        setLongSetting("highestProcessedServerOperationId", paramLong1);
      setLongSetting("lowestBackwardConversationId", 1L + paramLong2);
      setLongSetting("highestBackwardConversationId", paramLong2);
    }
    onServerHasHandledClientOperationdId(paramLong3);
    saveDirtySettings();
  }

  public void handleSyncResponse(HttpResponse paramHttpResponse, MailSyncObserver paramMailSyncObserver, MailEngine.SyncInfo paramSyncInfo)
    throws MailSync.ResponseParseException, IOException
  {
    incStats(6);
    handleResponse(paramHttpResponse, paramMailSyncObserver, 0L, paramSyncInfo);
  }

  public HttpUriRequest newFetchConversationRequest(ConversationInfo paramConversationInfo)
  {
    ArrayList localArrayList1 = new ArrayList();
    localArrayList1.add(paramConversationInfo);
    long l1 = getLongSetting("clientId");
    long l2 = getLongSetting("lowestBackwardConversationId");
    long l3 = getLongSetting("highestProcessedServerOperationId");
    ArrayList localArrayList2 = new ArrayList();
    ProtoBuf localProtoBuf = this.mUrls.getMainSyncRequestProto(this.mResolver, l2, l3, getClientOperationToAck(), localArrayList1, localArrayList2, getDirtyConversations(), new MailEngine.SyncInfo());
    return this.mUrls.newProtoRequest(this.mResolver, getRequestVersion(), l1, localProtoBuf, true);
  }

  MailProtocolInputStream newParserForProtoResponse(HttpResponse paramHttpResponse)
    throws MailSync.ResponseParseException, IOException
  {
    int i = paramHttpResponse.getStatusLine().getStatusCode();
    if (paramHttpResponse.getEntity() == null)
      throw new ResponseParseException("No response body received. Status is " + i);
    if (i != 200)
      throw new ResponseParseException("Error returned from server: " + i);
    MailProtocolInputStream localMailProtocolInputStream = new MailProtocolInputStream(AndroidHttpClient.getUngzippedContent(paramHttpResponse.getEntity()));
    ProtoBuf localProtoBuf1 = localMailProtocolInputStream.readNextChunkPart();
    if (localProtoBuf1 == null)
      throw new ResponseParseException("End of stream while reading next chunk part");
    ProtoBuf localProtoBuf2 = localProtoBuf1.getProtoBuf(1);
    if (LogUtils.isLoggable("Gmail", 2))
      ProtoBufHelpers.printHttpResponseProto(localProtoBuf2);
    int j = localProtoBuf2.getInt(2);
    setLongSetting("serverVersion", j);
    if ((localProtoBuf2.has(3)) && (localProtoBuf2.getBool(3)))
      throw new IOException("The server (version " + j + ") does not support the protocol version that we used");
    if ((localProtoBuf2.has(4)) && (localProtoBuf2.getBool(4)))
      throw new ResponseParseException("Abuse error reported");
    long l = localProtoBuf2.getInt(6);
    if (l != 0L)
      this.mEarliestAllowedSyncTimeAsElapsedRealtime = (l + SystemClock.elapsedRealtime());
    String str = localProtoBuf2.getString(5);
    if (!TextUtils.isEmpty(str))
      this.mStore.wipeAndResync("Received mustWipe error from server: " + str);
    localMailProtocolInputStream.setResponseVersion(localProtoBuf2.getInt(1));
    return localMailProtocolInputStream;
  }

  public HttpUriRequest newQueryRequest(String paramString, long paramLong, int paramInt1, int paramInt2)
  {
    return this.mUrls.getConversationListUrl(this.mResolver, getRequestVersion(), paramString, paramLong, paramInt1, paramInt2);
  }

  public HttpUriRequest nextSyncRequest(boolean paramBoolean1, boolean paramBoolean2, MailEngine.SyncInfo paramSyncInfo, ArrayList<ConversationInfo> paramArrayList)
    throws IOException
  {
    waitUntilEarliestAllowedSyncTime();
    this.mIsSyncCanceled = false;
    if (getBooleanSetting("needConfigSuggestion"))
      return this.mUrls.getGetSyncConfigSuggestionRequest(this.mResolver, getRequestVersion(), 200, 50, 0.8D, 5L);
    long l1 = getLongSetting("clientId");
    boolean bool1 = getBooleanSetting("configDirty");
    if ((l1 == 0L) || ((bool1) && (!paramBoolean2)))
    {
      if (l1 == 0L)
        l1 = System.currentTimeMillis();
      return this.mUrls.getSyncConfigRequest(this.mResolver, getRequestVersion(), l1, getStringSetSetting("labelsIncluded"), getStringSetSetting("labelsPartial"), getLongSetting("conversationAgeDays"), getLongSetting("maxAttachmentSize"));
    }
    long l2 = getLongSetting("highestProcessedServerOperationId");
    long l3 = getLongSetting("highestBackwardConversationId");
    long l4 = getLongSetting("lowestBackwardConversationId");
    int i = Gservices.getInt(this.mResolver, "gmail_start_sync_interval", 100);
    Random localRandom = new Random();
    if ((paramBoolean1) && ((1 + localRandom.nextInt(i)) % i == 0));
    for (int j = 1; (getBooleanSetting("startSyncNeeded")) || (getBooleanSetting("unackedSentOperations")) || (j != 0); j = 0)
    {
      if (l3 < l4)
      {
        l3 = 0L;
        l4 = 0L;
      }
      return this.mUrls.getStartSyncRequest(this.mResolver, getRequestVersion(), l1, l2, l3, l4, getClientOperationToAck());
    }
    Object localObject1;
    ArrayList localArrayList1;
    ProtoBuf localProtoBuf1;
    if (paramBoolean2)
    {
      localObject1 = Lists.newArrayList();
      localArrayList1 = Lists.newArrayList();
      if (LogUtils.isLoggable("Gmail", 2))
        LogUtils.v("Gmail", "highestMessageIds: " + Arrays.toString(((ArrayList)localObject1).toArray()), new Object[0]);
      if ((Gservices.getBoolean(this.mResolver, "gmail-backwards-sync-enabled", false)) && (((ArrayList)localObject1).size() != 0) && (l4 == 0L))
        l4 = ((ConversationInfo)((ArrayList)localObject1).get(0)).highestFetchedMessageId;
      Urls localUrls = this.mUrls;
      ContentResolver localContentResolver1 = this.mResolver;
      long l5 = getClientOperationToAck();
      ArrayList localArrayList2 = getDirtyConversations();
      localProtoBuf1 = localUrls.getMainSyncRequestProto(localContentResolver1, l4, l2, l5, (ArrayList)localObject1, localArrayList1, localArrayList2, paramSyncInfo);
      if (this.mIsSyncCanceled)
        return null;
    }
    else
    {
      if (paramArrayList != null);
      for (localObject1 = paramArrayList; ; localObject1 = this.mStore.getConversationInfosToFetch(paramSyncInfo))
      {
        localArrayList1 = this.mStore.getMessageIdsToFetch();
        break;
      }
    }
    ProtoBuf localProtoBuf2 = localProtoBuf1.getProtoBuf(7);
    ProtoOperationSink localProtoOperationSink = new ProtoOperationSink(localProtoBuf2);
    this.mStore.provideOperations(localProtoOperationSink, paramSyncInfo, System.currentTimeMillis() / 1000L);
    ArrayList localArrayList3 = localProtoOperationSink.getParts();
    HttpPost localHttpPost = this.mUrls.newProtoRequest(this.mResolver, getRequestVersion(), l1, localProtoBuf1, false);
    int k;
    if (Gservices.getInt(this.mResolver, "gmail_use_multipart_protobuf", 1) != 0)
    {
      k = 1;
      if (k == 0)
        break label757;
    }
    while (true)
    {
      label757: ByteArrayOutputStream localByteArrayOutputStream;
      try
      {
        if ((localProtoOperationSink.hasAttachments()) && (localArrayList3 != null))
        {
          ProtoBufPartSource localProtoBufPartSource = new ProtoBufPartSource("PROTOBUFDATA", localProtoBuf1.getDataSize(), localProtoBuf1.toByteArray());
          FilePart localFilePart = new FilePart("PROTOBUFDATA", localProtoBufPartSource, null, null);
          localArrayList3.add(localFilePart);
          localHttpPost.setEntity(new MultipartEntity((Part[])localArrayList3.toArray(new Part[0])));
          incStats(5, localProtoOperationSink.getNumOperations());
          boolean bool2 = getBooleanSetting("moreForwardSyncNeeded");
          if ((paramBoolean2) || ((l4 == 0L) && (((ArrayList)localObject1).size() == 0) && (!bool2) && (localArrayList1.size() == 0) && (!paramBoolean1)))
            break label865;
          n = 1;
          if ((localProtoOperationSink.getNumOperations() == 0) && (n == 0))
            break label871;
          setBooleanSetting("moreForwardSyncNeeded", true);
          if (localProtoOperationSink.getNumOperations() != 0)
            setBooleanSetting("unackedSentOperations", true);
          saveDirtySettings();
          return localHttpPost;
          k = 0;
          break;
        }
        localByteArrayOutputStream = new ByteArrayOutputStream();
        localProtoBuf1.outputTo(localByteArrayOutputStream);
        ContentResolver localContentResolver2 = this.mResolver;
        int m = Gservices.getInt(localContentResolver2, "gmail_max_gzip_size_bytes", 250000);
        if (localByteArrayOutputStream.size() <= m)
        {
          localObject2 = AndroidHttpClient.getCompressedEntity(localByteArrayOutputStream.toByteArray(), this.mResolver);
          localHttpPost.setEntity((HttpEntity)localObject2);
          continue;
        }
      }
      catch (IOException localIOException)
      {
        throw new RuntimeException("Should not get IO errors while writing to ram");
      }
      byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
      Object localObject2 = new ByteArrayEntity(arrayOfByte);
      continue;
      label865: int n = 0;
    }
    label871: LogUtils.i("Gmail", "lowestBackward conversation id " + l4, new Object[0]);
    return null;
  }

  void onServerHasHandledClientOperationdId(long paramLong)
  {
    LogUtils.v("Gmail", "Received handled client operation id from server", new Object[0]);
    this.mStore.removeOperations(paramLong);
    setBooleanSetting("unackedSentOperations", false);
    setLongSetting("clientOpToAck", paramLong);
  }

  public void onSyncCanceled()
  {
    this.mIsSyncCanceled = true;
  }

  public void onSyncLoopEnd()
  {
    this.dirtyConversations.clear();
  }

  Message readMessageFromProto(ProtoBuf paramProtoBuf, long paramLong, Dictionary paramDictionary)
    throws MailSync.ResponseParseException
  {
    Message localMessage = new Message();
    localMessage.messageId = paramProtoBuf.getLong(1);
    localMessage.conversationId = paramLong;
    localMessage.fromAddress = readEmailFromProto(paramProtoBuf.getProtoBuf(2));
    localMessage.dateSentMs = paramProtoBuf.getLong(3);
    localMessage.dateReceivedMs = paramProtoBuf.getLong(4);
    localMessage.subject = paramProtoBuf.getString(5);
    localMessage.snippet = paramProtoBuf.getString(6);
    localMessage.listInfo = "";
    localMessage.personalLevel = Gmail.PersonalLevel.fromInt(paramProtoBuf.getInt(7));
    localMessage.bodyEmbedsExternalResources = paramProtoBuf.getBool(8);
    if (paramProtoBuf.has(19));
    for (long l1 = Long.parseLong(paramProtoBuf.getString(19), 16); ; l1 = 0L)
    {
      localMessage.refMsgId = l1;
      ProtoBufHelpers.getAllLongs(paramProtoBuf, 14, localMessage.labelIds);
      Iterator localIterator1 = localMessage.labelIds.iterator();
      while (localIterator1.hasNext())
      {
        long l2 = ((Long)localIterator1.next()).longValue();
        this.mStore.getOrAddLabel(l2);
      }
    }
    localMessage.toAddresses = Lists.newArrayList();
    addAddressesInProto(paramProtoBuf, 9, localMessage.toAddresses);
    localMessage.ccAddresses = Lists.newArrayList();
    addAddressesInProto(paramProtoBuf, 10, localMessage.ccAddresses);
    localMessage.bccAddresses = Lists.newArrayList();
    addAddressesInProto(paramProtoBuf, 11, localMessage.bccAddresses);
    localMessage.replyToAddresses = Lists.newArrayList();
    addAddressesInProto(paramProtoBuf, 12, localMessage.replyToAddresses);
    if (paramProtoBuf.has(16));
    for (localMessage.body = unzipMessageBody(paramProtoBuf.getBytes(16), paramProtoBuf.getLong(17), localMessage.messageId, localMessage.conversationId, paramDictionary); ; localMessage.body = paramProtoBuf.getString(13))
    {
      incStats(0);
      ArrayList localArrayList = Lists.newArrayList();
      ProtoBufHelpers.getAllProtoBufs(paramProtoBuf, 15, localArrayList);
      Iterator localIterator2 = localArrayList.iterator();
      while (localIterator2.hasNext())
      {
        ProtoBuf localProtoBuf = (ProtoBuf)localIterator2.next();
        Gmail.Attachment localAttachment = new Gmail.Attachment();
        localAttachment.partId = localProtoBuf.getString(1);
        localAttachment.name = localProtoBuf.getString(2);
        localAttachment.contentType = localProtoBuf.getString(3);
        localAttachment.size = localProtoBuf.getInt(5);
        localAttachment.simpleContentType = localProtoBuf.getString(4);
        localAttachment.origin = Gmail.AttachmentOrigin.SERVER_ATTACHMENT;
        localAttachment.originExtras = Gmail.AttachmentOrigin.serverExtras(localMessage.conversationId, localMessage.messageId, localAttachment.partId);
        if (LogUtils.isLoggable("Gmail", 2))
        {
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = localAttachment.toJoinedString();
          LogUtils.d("Gmail", "readMessageFromProto: attachment = %s", arrayOfObject);
        }
        localMessage.attachments.add(localAttachment);
        incStats(3);
      }
    }
    LogUtils.d("Gmail", "readMessageFromProto: message = %s", new Object[] { localMessage });
    return localMessage;
  }

  public boolean responseContainsAuthFailure(HttpResponse paramHttpResponse)
  {
    return paramHttpResponse.getStatusLine().getStatusCode() == 401;
  }

  void saveDirtySettings()
  {
    if ((this.mDirtySettings.containsKey("labelsIncluded")) || (this.mDirtySettings.containsKey("labelsPartial")) || (this.mDirtySettings.containsKey("conversationAgeDays")) || (this.mDirtySettings.containsKey("maxAttachmentSize")));
    for (boolean bool = true; ; bool = false)
    {
      Map localMap = this.mDirtySettings;
      this.mDirtySettings = Maps.newHashMap();
      this.mStore.setSettings(localMap, bool);
      return;
    }
  }

  boolean setBooleanSetting(String paramString, boolean paramBoolean)
  {
    if (paramBoolean);
    for (long l = 1L; ; l = 0L)
      return setStringSetting(paramString, Long.toString(l));
  }

  public boolean setConfig(String paramString1, String paramString2, Long paramLong1, Long paramLong2)
  {
    boolean bool = false;
    ImmutableSet localImmutableSet1 = null;
    if (paramString1 != null)
    {
      localImmutableSet1 = ImmutableSet.copyOf(TextUtils.split(paramString1, LABEL_SEPARATOR_PATTERN));
      bool = false | setStringSetSetting("labelsIncluded", localImmutableSet1);
    }
    ImmutableSet localImmutableSet2 = null;
    if (paramString2 != null)
    {
      localImmutableSet2 = ImmutableSet.copyOf(TextUtils.split(paramString2, LABEL_SEPARATOR_PATTERN));
      bool |= setStringSetSetting("labelsPartial", localImmutableSet2);
    }
    if (paramLong1 != null)
      bool |= setLongSetting("conversationAgeDays", paramLong1.longValue());
    if (paramLong2 != null)
      bool |= setLongSetting("maxAttachmentSize", paramLong2.longValue());
    if ((bool | setBooleanSetting("needConfigSuggestion", false)))
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = LogUtils.labelSetToString(localImmutableSet1);
      arrayOfObject[1] = LogUtils.labelSetToString(localImmutableSet2);
      LogUtils.i("Gmail", "config changed locally to changed the label sets to: included(%s), partial(%s)", arrayOfObject);
      setBooleanSetting("configDirty", true);
      checkLabelsSets(null, null, null);
      saveDirtySettings();
      return true;
    }
    return false;
  }

  boolean setIntegerSetting(String paramString, Integer paramInteger)
  {
    return setStringSetting(paramString, Integer.toString(paramInteger.intValue()));
  }

  boolean setLongSetting(String paramString, long paramLong)
  {
    return setStringSetting(paramString, Long.toString(paramLong));
  }

  public void throwOneIoExceptionWhenHandlingSavedOrSentForTesting()
  {
    this.mFakeIoExceptionWhenHandlingMessageSavedOrSent = true;
  }

  private abstract class AbstractOperationSink
    implements MailStore.OperationSink
  {
    private AbstractOperationSink()
    {
    }

    public abstract int getNumOperations();
  }

  class AttachmentPartSource
    implements PartSource
  {
    private final Gmail.Attachment mAttachment;
    InputStream mInputStream;
    long mLength;
    private final long mMessageId;

    public AttachmentPartSource(long arg2, Gmail.Attachment arg4)
      throws IOException, SecurityException
    {
      this.mMessageId = ???;
      Object localObject;
      this.mAttachment = localObject;
      this.mInputStream = newInputStream();
      this.mLength = this.mInputStream.available();
    }

    private InputStream newInputStream()
      throws IOException, SecurityException
    {
      return MailSync.this.mStore.getInputStreamForUploadedAttachment(this.mMessageId, this.mAttachment);
    }

    public InputStream createInputStream()
      throws IOException, SecurityException
    {
      if (this.mInputStream != null)
      {
        InputStream localInputStream = this.mInputStream;
        this.mInputStream = null;
        return localInputStream;
      }
      return newInputStream();
    }

    public String getFileName()
    {
      return this.mAttachment.name;
    }

    public long getLength()
    {
      return this.mLength;
    }
  }

  public static class Conversation
  {
    public long conversationId;
    public long date;
    public String fromAddress;
    public boolean hasAttachments;
    public Set<Long> labelIds;
    public long maxMessageId;
    public int numMessages;
    public Gmail.PersonalLevel personalLevel;
    public String snippet;
    public long sortMessageId;
    public String subject;

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("conversationId: ").append(this.conversationId);
      localStringBuilder.append(", sortMessageId: ").append(this.sortMessageId);
      localStringBuilder.append(", maxMessageId: ").append(this.maxMessageId);
      localStringBuilder.append(", numMessage: ").append(this.numMessages);
      localStringBuilder.append(", date: ").append(this.date);
      if (this.subject != null)
        localStringBuilder.append(", subject: ").append(this.subject);
      if (this.snippet != null)
        localStringBuilder.append(", snippet: ").append(this.snippet);
      localStringBuilder.append(", hasAttachments: ").append(this.hasAttachments);
      if (this.fromAddress != null)
        localStringBuilder.append(", fromAddress: '").append(this.fromAddress.replace('\n', '|')).append('\'');
      localStringBuilder.append(", labelIds: ").append(TextUtils.join("|", this.labelIds));
      return localStringBuilder.toString();
    }
  }

  public static class ConversationInfo
  {
    public final long highestFetchedMessageId;
    public final long id;

    public ConversationInfo(long paramLong1, long paramLong2)
    {
      this.id = paramLong1;
      this.highestFetchedMessageId = paramLong2;
    }

    public boolean equals(Object paramObject)
    {
      return toString().equals(paramObject.toString());
    }

    public String toString()
    {
      return "[ConversationInfo id: " + this.id + ", highest: " + this.highestFetchedMessageId + "]";
    }
  }

  public static abstract interface ConversationSink
  {
    public abstract void finalizeSink();

    public abstract void handleConversation(MailSync.Conversation paramConversation);

    public abstract void prepareSink();
  }

  static class MailProtocolInputStream
  {
    private final InputStream mIs;
    private int mResponseVersion = 0;

    public MailProtocolInputStream(InputStream paramInputStream)
    {
      this.mIs = paramInputStream;
    }

    public void close()
      throws IOException
    {
      this.mIs.close();
    }

    public int getResponseVersion()
    {
      return this.mResponseVersion;
    }

    public ProtoBuf readNextChunkPart()
      throws IOException
    {
      ProtoBuf localProtoBuf1 = new ProtoBuf(null);
      int i = ProtoBufUtil.readNextProtoBuf(GmsProtosMessageTypes.RESPONSE_CHUNK, this.mIs, localProtoBuf1);
      if (i == -1)
        return null;
      ProtoBuf localProtoBuf2 = new ProtoBuf(GmsProtosMessageTypes.RESPONSE_CHUNK);
      try
      {
        localProtoBuf2.setProtoBuf(i, localProtoBuf1);
        return localProtoBuf2;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        LogUtils.e("Gmail", "Response proto has an unexpected field", new Object[0]);
      }
      return localProtoBuf2;
    }

    public void setResponseVersion(int paramInt)
    {
      this.mResponseVersion = paramInt;
    }
  }

  static class MailPullParser extends SimplePullParser
  {
    private int mResponseVersion = 0;

    public MailPullParser(InputStream paramInputStream, String paramString)
      throws SimplePullParser.ParseException, IOException
    {
      super(paramString);
    }

    public MailPullParser(String paramString)
      throws IOException, SimplePullParser.ParseException
    {
      super();
    }

    public int getResponseVersion()
    {
      return this.mResponseVersion;
    }

    public void setResponseVersion(int paramInt)
    {
      this.mResponseVersion = paramInt;
    }
  }

  public static class Message
  {
    public List<Gmail.Attachment> attachments = Lists.newArrayList();
    public List<String> bccAddresses = Collections.emptyList();
    public String body;
    public boolean bodyEmbedsExternalResources;
    public List<String> ccAddresses = Collections.emptyList();
    public boolean clientCreated;
    public long conversationId;
    public String customFromAddress;
    public long dateReceivedMs;
    public long dateSentMs;
    public boolean forward;
    public String fromAddress = "";
    public boolean includeQuotedText;
    public Set<Long> labelIds = Sets.newHashSet();
    public String listInfo;
    public long localMessageId;
    public long messageId;
    public Gmail.PersonalLevel personalLevel;
    public long quoteStartPos;
    public long refMessageId;
    public long refMsgId;
    public List<String> replyToAddresses = Collections.emptyList();
    public String snippet;
    public String subject;
    public List<String> toAddresses = Collections.emptyList();

    public Gmail.Attachment getAttachmentOrNull(String paramString)
    {
      Iterator localIterator = this.attachments.iterator();
      while (localIterator.hasNext())
      {
        Gmail.Attachment localAttachment = (Gmail.Attachment)localIterator.next();
        if (paramString.equals(localAttachment.partId))
          return localAttachment;
      }
      return null;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Message: ").append(this.messageId);
      localStringBuilder.append(", ").append(this.conversationId);
      localStringBuilder.append(", from: ").append(this.fromAddress);
      localStringBuilder.append(", to: ").append(TextUtils.join(", ", this.toAddresses));
      if (this.ccAddresses.size() != 0)
        localStringBuilder.append(", cc: ").append(TextUtils.join(", ", this.ccAddresses));
      if (this.bccAddresses.size() != 0)
        localStringBuilder.append(", bcc: ").append(TextUtils.join(", ", this.bccAddresses));
      if (this.replyToAddresses.size() != 0)
        localStringBuilder.append(", replyTo: ").append(TextUtils.join(", ", this.replyToAddresses));
      localStringBuilder.append(", subject: ").append(this.subject);
      localStringBuilder.append(", snippet: ").append(this.snippet);
      localStringBuilder.append(", forward: ").append(this.forward);
      localStringBuilder.append(", includeQuotedText: ").append(this.includeQuotedText);
      localStringBuilder.append(", quoteStartPos: ").append(this.quoteStartPos);
      localStringBuilder.append(", clientCreated: ").append(this.clientCreated);
      return localStringBuilder.toString();
    }
  }

  class ProtoBufPartSource
    implements PartSource
  {
    private final String mFilename;
    InputStream mInputStream;
    long mLength;
    private final byte[] mProtoBufData;

    public ProtoBufPartSource(String paramLong, long arg3, byte[] arg5)
      throws IOException
    {
      this.mFilename = paramLong;
      Object localObject;
      this.mProtoBufData = localObject;
      this.mLength = ???;
      this.mInputStream = newInputStream();
    }

    private InputStream newInputStream()
      throws IOException
    {
      return new ByteArrayInputStream(this.mProtoBufData);
    }

    public InputStream createInputStream()
      throws IOException
    {
      if (this.mInputStream != null)
      {
        InputStream localInputStream = this.mInputStream;
        this.mInputStream = null;
        return localInputStream;
      }
      return newInputStream();
    }

    public String getFileName()
    {
      return this.mFilename;
    }

    public long getLength()
    {
      return this.mLength;
    }
  }

  class ProtoOperationSink extends MailSync.AbstractOperationSink
  {
    private final ArrayList<NameValuePair> mParams;
    private ArrayList<Part> mParts;
    ProtoBuf mUphillSyncProto;

    ProtoOperationSink(ProtoBuf arg2)
    {
      super(null);
      Object localObject;
      this.mUphillSyncProto = localObject;
      this.mParams = Lists.newArrayList();
      this.mParts = null;
    }

    private ProtoBuf newOperationProto(long paramLong)
    {
      ProtoBuf localProtoBuf = this.mUphillSyncProto.addNewProtoBuf(1);
      localProtoBuf.setLong(1, paramLong);
      return localProtoBuf;
    }

    private void notifyAttachmentFailed(MailSync.Message paramMessage, Gmail.Attachment paramAttachment)
    {
      Intent localIntent = new Intent("com.google.android.gm.intent.ACTION_POST_SEND_ERROR");
      localIntent.putExtra("account", MailSync.this.mStore.getAccount());
      localIntent.putExtra("extraMessageSubject", paramMessage.subject);
      localIntent.putExtra("extraConversationId", paramMessage.conversationId);
      MailSync.this.mContext.sendBroadcast(localIntent);
    }

    private boolean shouldIgnoreLabelOperation(long paramLong)
    {
      return paramLong == 0L;
    }

    public void conversationLabelAddedOrRemoved(long paramLong1, long paramLong2, long paramLong3, boolean paramBoolean)
    {
      if (shouldIgnoreLabelOperation(paramLong3))
        return;
      ProtoBuf localProtoBuf = newOperationProto(paramLong1).setNewProtoBuf(3);
      localProtoBuf.setBool(3, paramBoolean);
      localProtoBuf.setLong(1, paramLong2);
      if (!MailCore.isLabelIdLocal(paramLong3))
      {
        localProtoBuf.setLong(2, paramLong3);
        return;
      }
      localProtoBuf.setString(4, (String)Gmail.LOCAL_PRIORITY_LABELS.get(Long.valueOf(paramLong3)));
    }

    public int getNumOperations()
    {
      return this.mUphillSyncProto.getCount(1);
    }

    public ArrayList<Part> getParts()
    {
      return this.mParts;
    }

    public boolean hasAttachments()
    {
      return (this.mParts != null) && (this.mParts.size() > 0);
    }

    public void messageExpunged(long paramLong1, long paramLong2)
    {
      newOperationProto(paramLong1).setLong(5, paramLong2);
    }

    public void messageLabelAdded(long paramLong1, long paramLong2, long paramLong3)
    {
      if (shouldIgnoreLabelOperation(paramLong3))
        return;
      ProtoBuf localProtoBuf = newOperationProto(paramLong1).setNewProtoBuf(2);
      localProtoBuf.setBool(3, true);
      localProtoBuf.setLong(1, paramLong2);
      if (!MailCore.isLabelIdLocal(paramLong3))
      {
        localProtoBuf.setLong(2, paramLong3);
        return;
      }
      localProtoBuf.setString(4, (String)Gmail.LOCAL_PRIORITY_LABELS.get(Long.valueOf(paramLong3)));
    }

    public void messageLabelRemoved(long paramLong1, long paramLong2, long paramLong3)
    {
      if (shouldIgnoreLabelOperation(paramLong3))
        return;
      ProtoBuf localProtoBuf = newOperationProto(paramLong1).setNewProtoBuf(2);
      localProtoBuf.setBool(3, false);
      localProtoBuf.setLong(1, paramLong2);
      localProtoBuf.setLong(2, paramLong3);
    }

    public void messageSavedOrSent(long paramLong1, MailSync.Message paramMessage, long paramLong2, long paramLong3, boolean paramBoolean)
    {
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Long.valueOf(paramLong1);
      LogUtils.v("Gmail", "MailSync posting operation %d (messageSavedOrSent)", arrayOfObject1);
      ProtoBuf localProtoBuf1 = newOperationProto(paramLong1).setNewProtoBuf(4);
      localProtoBuf1.setBool(10, paramBoolean);
      localProtoBuf1.setLong(1, paramLong2);
      localProtoBuf1.setLong(2, paramLong3);
      localProtoBuf1.setString(3, TextUtils.join(", ", paramMessage.toAddresses));
      localProtoBuf1.setString(4, TextUtils.join(", ", paramMessage.ccAddresses));
      localProtoBuf1.setString(5, TextUtils.join(", ", paramMessage.bccAddresses));
      String str1;
      String str2;
      label140: int i;
      Iterator localIterator;
      if (paramMessage.subject != null)
      {
        str1 = paramMessage.subject;
        localProtoBuf1.setString(6, str1);
        if (paramMessage.body == null)
          break label360;
        str2 = paramMessage.body;
        localProtoBuf1.setString(7, str2);
        if ((!paramMessage.clientCreated) || (!paramMessage.includeQuotedText))
          break label368;
        localProtoBuf1.setBool(11, true);
        localProtoBuf1.setLong(13, paramMessage.quoteStartPos);
        if (paramMessage.forward)
          localProtoBuf1.setBool(12, true);
        if (!TextUtils.isEmpty(paramMessage.customFromAddress))
        {
          localProtoBuf1.setBool(14, true);
          localProtoBuf1.setString(15, paramMessage.customFromAddress);
          Object[] arrayOfObject2 = new Object[1];
          arrayOfObject2[0] = paramMessage.customFromAddress;
          LogUtils.v("Gmail", "Uphill Sync operation with custom from address: %s", arrayOfObject2);
        }
        i = 1;
        localIterator = paramMessage.attachments.iterator();
      }
      while (true)
      {
        if (!localIterator.hasNext())
          break label830;
        Gmail.Attachment localAttachment = (Gmail.Attachment)localIterator.next();
        switch (MailSync.2.$SwitchMap$com$google$android$gm$provider$Gmail$AttachmentOrigin[localAttachment.origin.ordinal()])
        {
        default:
          throw new IllegalArgumentException("Unknown origin: " + localAttachment.origin);
          str1 = "";
          break;
          str2 = "";
          break label140;
          localProtoBuf1.setBool(11, false);
          break;
        case 1:
          localProtoBuf1.addString(8, localAttachment.originExtras);
          break;
        case 2:
          label360: label368: String str3;
          label444: int j;
          byte[] arrayOfByte1;
          if (Strings.isNullOrEmpty(localAttachment.partId))
          {
            str3 = paramMessage.messageId + "-" + i;
            i++;
            if (Gservices.getInt(MailSync.this.mResolver, "gmail_use_multipart_protobuf", 1) == 0)
              break label629;
            j = 1;
            arrayOfByte1 = null;
            if (j == 0)
              break label699;
          }
          while (true)
          {
            try
            {
              MailSync.AttachmentPartSource localAttachmentPartSource = new MailSync.AttachmentPartSource(MailSync.this, paramMessage.messageId, localAttachment);
              FilePart localFilePart = new FilePart(str3, localAttachmentPartSource, localAttachment.contentType, null);
              if (this.mParts == null)
                this.mParts = new ArrayList();
              this.mParts.add(localFilePart);
              ProtoBuf localProtoBuf2 = localProtoBuf1.addNewProtoBuf(9);
              localProtoBuf2.setString(1, str3);
              localProtoBuf2.setString(2, localAttachment.name);
              localProtoBuf2.setString(3, localAttachment.contentType);
              if (j != 0)
                break;
              localProtoBuf2.setBytes(4, arrayOfByte1);
              break;
              str3 = paramMessage.messageId + "-" + localAttachment.partId;
              break label444;
              label629: j = 0;
            }
            catch (IOException localIOException2)
            {
              LogUtils.e("Gmail", localIOException2, "IO error while reading attachment: %s", new Object[] { str3 });
              notifyAttachmentFailed(paramMessage, localAttachment);
              break;
            }
            catch (SecurityException localSecurityException)
            {
              LogUtils.e("Gmail", localSecurityException, "SecurityException while reading attachment: %S", new Object[] { str3 });
              notifyAttachmentFailed(paramMessage, localAttachment);
            }
            break;
            try
            {
              label699: InputStream localInputStream = MailSync.this.mStore.getInputStreamForUploadedAttachment(paramMessage.messageId, localAttachment);
              ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
              byte[] arrayOfByte2 = new byte[1024];
              long l = SystemClock.elapsedRealtime();
              do
              {
                int k = localInputStream.read(arrayOfByte2);
                if (k < 0)
                {
                  arrayOfByte1 = localByteArrayOutputStream.toByteArray();
                  break;
                }
                localByteArrayOutputStream.write(arrayOfByte2, 0, k);
              }
              while (SystemClock.elapsedRealtime() - l <= 3600000L);
              throw new IOException("Timed out reading attachment");
            }
            catch (IOException localIOException1)
            {
              LogUtils.e("Gmail", localIOException1, "IO error while reading attachment: %s", new Object[] { str3 });
              notifyAttachmentFailed(paramMessage, localAttachment);
            }
          }
        }
      }
      label830: LogUtils.v("Gmail", "messageSavedOrSent: message = %s", new Object[] { localProtoBuf1 });
    }
  }

  public static class ResponseParseException extends Exception
  {
    private static final long serialVersionUID = 1L;

    public ResponseParseException(String paramString)
    {
      super();
    }

    public ResponseParseException(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }

    public ResponseParseException(Throwable paramThrowable)
    {
      super();
    }
  }

  public static enum SyncRationale
  {
    static
    {
      DURATION = new SyncRationale("DURATION", 1);
      NONE = new SyncRationale("NONE", 2);
      UNKNOWN = new SyncRationale("UNKNOWN", 3);
      LOCAL_CHANGE = new SyncRationale("LOCAL_CHANGE", 4);
      SyncRationale[] arrayOfSyncRationale = new SyncRationale[5];
      arrayOfSyncRationale[0] = LABEL;
      arrayOfSyncRationale[1] = DURATION;
      arrayOfSyncRationale[2] = NONE;
      arrayOfSyncRationale[3] = UNKNOWN;
      arrayOfSyncRationale[4] = LOCAL_CHANGE;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.MailSync
 * JD-Core Version:    0.6.2
 */