package com.google.android.gm.provider;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.protocol.ProtoBuf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ProtoBufHelpers
{
  public static void getAllLongs(ProtoBuf paramProtoBuf, int paramInt, Collection<Long> paramCollection)
  {
    int i = paramProtoBuf.getCount(paramInt);
    for (int j = 0; j < i; j++)
      paramCollection.add(Long.valueOf(paramProtoBuf.getLong(paramInt, j)));
  }

  public static void getAllProtoBufs(ProtoBuf paramProtoBuf, int paramInt, Collection<ProtoBuf> paramCollection)
  {
    int i = paramProtoBuf.getCount(paramInt);
    for (int j = 0; j < i; j++)
      paramCollection.add(paramProtoBuf.getProtoBuf(paramInt, j));
  }

  public static void getAllStrings(ProtoBuf paramProtoBuf, int paramInt, Collection<String> paramCollection)
  {
    int i = paramProtoBuf.getCount(paramInt);
    for (int j = 0; j < i; j++)
      paramCollection.add(paramProtoBuf.getString(paramInt, j));
  }

  public static void printConfigInfoProto(ProtoBuf paramProtoBuf)
  {
    long l = paramProtoBuf.getLong(1);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Long.valueOf(l);
    LogUtils.d("Gmail", "ConfigInfoProto: Conversation Age Days: %d", arrayOfObject);
    HashSet localHashSet1 = Sets.newHashSet();
    HashSet localHashSet2 = Sets.newHashSet();
    getAllStrings(paramProtoBuf, 2, localHashSet1);
    LogUtils.d("Gmail", "ConfigInfoProto: Included Canonical Label Name: %s", new Object[] { localHashSet1 });
    getAllStrings(paramProtoBuf, 3, localHashSet2);
    LogUtils.d("Gmail", "ConfigInfoProto: Duration Canonical Label Name: %s", new Object[] { localHashSet2 });
  }

  public static void printForwardSyncProto(ProtoBuf paramProtoBuf)
  {
    long l1 = paramProtoBuf.getLong(1);
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Long.valueOf(l1);
    LogUtils.d("Gmail", "ForwardSyncProto: operationid: %d", arrayOfObject1);
    boolean bool1;
    boolean bool2;
    label75: boolean bool3;
    if (paramProtoBuf.has(2))
    {
      ProtoBuf localProtoBuf3 = paramProtoBuf.getProtoBuf(2);
      long l5 = localProtoBuf3.getLong(2);
      int i = localProtoBuf3.getInt(1);
      if (i == 0)
      {
        bool1 = true;
        if (i != 1)
          break label238;
        bool2 = true;
        if (i != 2)
          break label244;
        bool3 = true;
        label84: Object[] arrayOfObject5 = new Object[4];
        arrayOfObject5[0] = Long.valueOf(l5);
        arrayOfObject5[1] = Boolean.valueOf(bool1);
        arrayOfObject5[2] = Boolean.valueOf(bool2);
        arrayOfObject5[3] = Boolean.valueOf(bool3);
        LogUtils.d("Gmail", "ForwardSyncProto: ThreadLabelOrUnlabeled, conversationId: %d,labelAdded: %b labelRemoved: %b messagesExpunged: %b", arrayOfObject5);
        if ((bool1) || (bool2))
        {
          long l6 = localProtoBuf3.getLong(3);
          Object[] arrayOfObject6 = new Object[1];
          arrayOfObject6[0] = Long.valueOf(l6);
          LogUtils.d("Gmail", "ForwardSyncProto: ThreadLabelOrUnLabel, labelId: %d", arrayOfObject6);
        }
        int j = localProtoBuf3.getInt(4);
        ArrayList localArrayList = Lists.newArrayList();
        getAllLongs(localProtoBuf3, 5, localArrayList);
        Object[] arrayOfObject7 = new Object[2];
        arrayOfObject7[0] = Integer.valueOf(j);
        arrayOfObject7[1] = localArrayList;
        LogUtils.d("Gmail", "ForwardSyncProto: syncRationale: %d, messageIds: %s", arrayOfObject7);
      }
    }
    label238: label244: 
    do
    {
      return;
      bool1 = false;
      break;
      bool2 = false;
      break label75;
      bool3 = false;
      break label84;
      if (paramProtoBuf.has(3))
      {
        ProtoBuf localProtoBuf2 = paramProtoBuf.getProtoBuf(3);
        long l4 = localProtoBuf2.getLong(1);
        String str3 = localProtoBuf2.getString(2);
        String str4 = localProtoBuf2.getString(3);
        Object[] arrayOfObject4 = new Object[3];
        arrayOfObject4[0] = Long.valueOf(l4);
        arrayOfObject4[1] = str3;
        arrayOfObject4[2] = str4;
        LogUtils.d("Gmail", "ForwardSyncProto: LabelCreated, labelId: %d canonicalName: %sdisplayName: %s", arrayOfObject4);
        return;
      }
      if (paramProtoBuf.has(4))
      {
        ProtoBuf localProtoBuf1 = paramProtoBuf.getProtoBuf(4);
        long l3 = localProtoBuf1.getLong(1);
        String str1 = localProtoBuf1.getString(2);
        String str2 = localProtoBuf1.getString(3);
        Object[] arrayOfObject3 = new Object[3];
        arrayOfObject3[0] = Long.valueOf(l3);
        arrayOfObject3[1] = str1;
        arrayOfObject3[2] = str2;
        LogUtils.d("Gmail", "ForwardSyncProto: LabelRenamed, labelId: %d newCanonicalName: %s newDisplayName: %s", arrayOfObject3);
        return;
      }
    }
    while (!paramProtoBuf.has(5));
    long l2 = paramProtoBuf.getProtoBuf(5).getLong(1);
    Object[] arrayOfObject2 = new Object[1];
    arrayOfObject2[0] = Long.valueOf(l2);
    LogUtils.d("Gmail", "ForwardSyncProto: LabelDeleted, labelId: %d", arrayOfObject2);
  }

  public static void printHttpResponseChunkProto(ProtoBuf paramProtoBuf)
  {
    StringBuffer localStringBuffer = new StringBuffer("HttpResponseChunk: ");
    if (paramProtoBuf.has(2))
      localStringBuffer.append("ConfigInfo");
    while (true)
    {
      LogUtils.d("Gmail", localStringBuffer.toString(), new Object[0]);
      return;
      if (paramProtoBuf.has(3))
        localStringBuffer.append("ConfigAccepted");
      else if (paramProtoBuf.has(4))
        localStringBuffer.append("StartSync");
      else if (paramProtoBuf.has(5))
        localStringBuffer.append("UphillSync");
      else if (paramProtoBuf.has(7))
        localStringBuffer.append("ForwardSync");
      else if (paramProtoBuf.has(8))
        localStringBuffer.append("CheckConversation");
      else if (paramProtoBuf.has(9))
        localStringBuffer.append("BeginConversation");
      else if (paramProtoBuf.has(10))
        localStringBuffer.append("BeginMessage");
      else if (paramProtoBuf.has(16))
        localStringBuffer.append("NoConversation");
      else if (paramProtoBuf.has(17))
        localStringBuffer.append("NoMessage");
      else if (paramProtoBuf.has(14))
        localStringBuffer.append("SyncPostamble");
    }
  }

  public static void printHttpResponseProto(ProtoBuf paramProtoBuf)
  {
    int i = -1;
    if (paramProtoBuf.has(2))
      i = paramProtoBuf.getInt(2);
    if ((paramProtoBuf.has(3)) && (paramProtoBuf.getBool(3)));
    for (boolean bool = true; ; bool = false)
    {
      long l = -1L;
      if (paramProtoBuf.has(6))
        l = paramProtoBuf.getInt(6);
      String str = "noWipeDescription";
      if (paramProtoBuf.has(5))
        str = paramProtoBuf.getString(5);
      int j = -1;
      if (paramProtoBuf.has(1))
        j = paramProtoBuf.getInt(1);
      Object[] arrayOfObject = new Object[5];
      arrayOfObject[0] = Integer.valueOf(i);
      arrayOfObject[1] = Boolean.valueOf(bool);
      arrayOfObject[2] = Long.valueOf(l);
      arrayOfObject[3] = Integer.valueOf(j);
      arrayOfObject[4] = str;
      LogUtils.d("Gmail", "HttpProtoResponse: serverVersion: %d, hasVersionError: %b, delay: %d, responseVersion: %d wipeDescription: %s", arrayOfObject);
      return;
    }
  }

  public static void printStartSyncInfoProto(ProtoBuf paramProtoBuf)
  {
    int i = paramProtoBuf.getCount(4);
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Integer.valueOf(i);
    LogUtils.d("Gmail", "StartSyncInfoProto: Labels: numLabels: %d", arrayOfObject1);
    for (int j = 0; j < i; j++)
    {
      ProtoBuf localProtoBuf = paramProtoBuf.getProtoBuf(4, j);
      long l = localProtoBuf.getLong(1);
      String str1 = localProtoBuf.getString(2);
      String str2 = localProtoBuf.getString(3);
      int k = localProtoBuf.getInt(4);
      int m = localProtoBuf.getInt(5);
      int n = -1;
      if (localProtoBuf.has(6))
        n = localProtoBuf.getInt(6);
      String str3 = "Default";
      if (localProtoBuf.has(7))
        str3 = localProtoBuf.getString(7);
      Object[] arrayOfObject2 = new Object[7];
      arrayOfObject2[0] = Long.valueOf(l);
      arrayOfObject2[1] = str1;
      arrayOfObject2[2] = str2;
      arrayOfObject2[3] = Integer.valueOf(k);
      arrayOfObject2[4] = Integer.valueOf(m);
      arrayOfObject2[5] = Integer.valueOf(n);
      arrayOfObject2[6] = str3;
      LogUtils.d("Gmail", "StartSyncInfoProto: Label id: %d canonicalName: %s displayName: %snumConversations: %d numUnreadConversations: %d color: %d visibility: %s", arrayOfObject2);
    }
  }

  public static void printSyncPostambleProto(ProtoBuf paramProtoBuf)
  {
    ArrayList localArrayList = Lists.newArrayList();
    getAllProtoBufs(paramProtoBuf, 5, localArrayList);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      ProtoBuf localProtoBuf = (ProtoBuf)localIterator.next();
      int i = localProtoBuf.getInt(2);
      int j = 2147483647;
      if (localProtoBuf.has(4))
        j = localProtoBuf.getInt(4);
      int k = localProtoBuf.getInt(3);
      long l = localProtoBuf.getLong(1);
      String str = "SHOW";
      if (localProtoBuf.has(5))
        str = localProtoBuf.getString(5);
      Object[] arrayOfObject = new Object[5];
      arrayOfObject[0] = Long.valueOf(l);
      arrayOfObject[1] = Integer.valueOf(i);
      arrayOfObject[2] = Integer.valueOf(k);
      arrayOfObject[3] = Integer.valueOf(j);
      arrayOfObject[4] = str;
      LogUtils.d("Gmail", "SyncPostAmbleProto: labelId: %d, count: %d, unreadCount: %d, color: %d, visibility: %s", arrayOfObject);
    }
  }

  public static void printUphillSyncProto(ProtoBuf paramProtoBuf)
  {
    int i = paramProtoBuf.getCount(1);
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Integer.valueOf(i);
    LogUtils.d("Gmail", "UphillSyncProto: numNothandled: %d", arrayOfObject1);
    for (int j = 0; j < i; j++)
    {
      ProtoBuf localProtoBuf2 = paramProtoBuf.getProtoBuf(1, j);
      long l5 = localProtoBuf2.getLong(1);
      String str = localProtoBuf2.getString(2);
      Object[] arrayOfObject5 = new Object[2];
      arrayOfObject5[0] = Long.valueOf(l5);
      arrayOfObject5[1] = str;
      LogUtils.d("Gmail", "UphillSyncProto: Nothandled: messageId: %d, error: %s", arrayOfObject5);
    }
    int k = paramProtoBuf.getCount(2);
    Object[] arrayOfObject2 = new Object[1];
    arrayOfObject2[0] = Integer.valueOf(k);
    LogUtils.d("Gmail", "UphillSyncProto: numSavedOrSent: %d", arrayOfObject2);
    for (int m = 0; m < k; m++)
    {
      ProtoBuf localProtoBuf1 = paramProtoBuf.getProtoBuf(2, m);
      long l2 = localProtoBuf1.getLong(1);
      long l3 = localProtoBuf1.getLong(2);
      long l4 = localProtoBuf1.getLong(3);
      Object[] arrayOfObject4 = new Object[3];
      arrayOfObject4[0] = Long.valueOf(l2);
      arrayOfObject4[1] = Long.valueOf(l3);
      arrayOfObject4[2] = Long.valueOf(l4);
      LogUtils.d("Gmail", "UphillSyncProto: SavedOrSent: messageIDOnClient: %d messageId: %d conversationId: %d", arrayOfObject4);
    }
    if (paramProtoBuf.has(3))
    {
      long l1 = paramProtoBuf.getLong(3);
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Long.valueOf(l1);
      LogUtils.d("Gmail", "UphillSyncProto: handledOperationId: %d", arrayOfObject3);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.ProtoBufHelpers
 * JD-Core Version:    0.6.2
 */