package com.google.android.gm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.ConversationCursor;
import com.google.android.gm.provider.Gmail.LabelMap;
import com.google.android.gm.provider.Label;
import com.google.android.gm.provider.LabelManager;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Set;

public class ConversationInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ConversationInfo> CREATOR = new Parcelable.Creator()
  {
    public ConversationInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ConversationInfo(paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong(), LabelManager.parseLabelQueryResult(paramAnonymousParcel.readString()));
    }

    public ConversationInfo[] newArray(int paramAnonymousInt)
    {
      return new ConversationInfo[paramAnonymousInt];
    }
  };
  private final long mConversationId;
  private final boolean mIsImportant;
  private final Map<String, Label> mLabels;
  private final long mLocalMessageId;
  private long mMaxMessageId;
  private final long mServerMessageId;

  ConversationInfo(long paramLong1, long paramLong2, long paramLong3, long paramLong4, Map<String, Label> paramMap)
  {
    this(paramLong1, paramLong2, paramLong3, paramLong4, paramMap, Gmail.isImportant(paramMap));
  }

  public ConversationInfo(long paramLong1, long paramLong2, long paramLong3, long paramLong4, Map<String, Label> paramMap, Set<Long> paramSet, Gmail.LabelMap paramLabelMap)
  {
  }

  private ConversationInfo(long paramLong1, long paramLong2, long paramLong3, long paramLong4, Map<String, Label> paramMap, boolean paramBoolean)
  {
    this.mConversationId = paramLong1;
    this.mLocalMessageId = paramLong2;
    this.mServerMessageId = paramLong3;
    this.mLabels = paramMap;
    this.mMaxMessageId = paramLong4;
    this.mIsImportant = paramBoolean;
  }

  private ConversationInfo(long paramLong1, long paramLong2, Map<String, Label> paramMap, Set<Long> paramSet, Gmail.LabelMap paramLabelMap)
  {
    this(paramLong1, 0L, 0L, paramLong2, paramMap, paramSet, paramLabelMap);
  }

  public static ConversationInfo forCursor(Gmail.ConversationCursor paramConversationCursor, Gmail.LabelMap paramLabelMap)
  {
    if (paramConversationCursor.count() == 0)
      return null;
    return new ConversationInfo(paramConversationCursor.getConversationId(), paramConversationCursor.getMaxServerMessageId(), paramConversationCursor.getLabels(), paramConversationCursor.getLabelIds(), paramLabelMap);
  }

  public int describeContents()
  {
    return 0;
  }

  // ERROR //
  public boolean equals(Object paramObject)
  {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_1
    //   5: aload_0
    //   6: if_acmpne +7 -> 13
    //   9: aload_0
    //   10: monitorexit
    //   11: iload_2
    //   12: ireturn
    //   13: aload_1
    //   14: ifnull +14 -> 28
    //   17: aload_1
    //   18: invokevirtual 103	java/lang/Object:getClass	()Ljava/lang/Class;
    //   21: aload_0
    //   22: invokevirtual 103	java/lang/Object:getClass	()Ljava/lang/Class;
    //   25: if_acmpeq +7 -> 32
    //   28: aload_0
    //   29: monitorexit
    //   30: iconst_0
    //   31: ireturn
    //   32: aload_1
    //   33: checkcast 2	com/google/android/gm/ConversationInfo
    //   36: astore 4
    //   38: aload_0
    //   39: getfield 60	com/google/android/gm/ConversationInfo:mConversationId	J
    //   42: aload 4
    //   44: getfield 60	com/google/android/gm/ConversationInfo:mConversationId	J
    //   47: lcmp
    //   48: ifne +76 -> 124
    //   51: aload_0
    //   52: getfield 62	com/google/android/gm/ConversationInfo:mLocalMessageId	J
    //   55: aload 4
    //   57: getfield 62	com/google/android/gm/ConversationInfo:mLocalMessageId	J
    //   60: lcmp
    //   61: ifne +63 -> 124
    //   64: aload_0
    //   65: getfield 64	com/google/android/gm/ConversationInfo:mServerMessageId	J
    //   68: aload 4
    //   70: getfield 64	com/google/android/gm/ConversationInfo:mServerMessageId	J
    //   73: lcmp
    //   74: ifne +50 -> 124
    //   77: aload_0
    //   78: getfield 68	com/google/android/gm/ConversationInfo:mMaxMessageId	J
    //   81: aload 4
    //   83: getfield 68	com/google/android/gm/ConversationInfo:mMaxMessageId	J
    //   86: lcmp
    //   87: ifne +37 -> 124
    //   90: aload_0
    //   91: getfield 66	com/google/android/gm/ConversationInfo:mLabels	Ljava/util/Map;
    //   94: invokeinterface 108 1 0
    //   99: aload 4
    //   101: getfield 66	com/google/android/gm/ConversationInfo:mLabels	Ljava/util/Map;
    //   104: invokeinterface 108 1 0
    //   109: invokevirtual 110	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   112: ifeq +12 -> 124
    //   115: aload_0
    //   116: monitorexit
    //   117: iload_2
    //   118: ireturn
    //   119: astore_3
    //   120: aload_0
    //   121: monitorexit
    //   122: aload_3
    //   123: athrow
    //   124: iconst_0
    //   125: istore_2
    //   126: goto -11 -> 115
    //
    // Exception table:
    //   from	to	target	type
    //   9	11	119	finally
    //   17	28	119	finally
    //   28	30	119	finally
    //   32	115	119	finally
    //   115	117	119	finally
    //   120	122	119	finally
  }

  public long getConversationId()
  {
    return this.mConversationId;
  }

  public Map<String, Label> getLabels()
  {
    try
    {
      Map localMap = this.mLabels;
      if (localMap == null);
      ImmutableMap localImmutableMap;
      for (Object localObject2 = null; ; localObject2 = localImmutableMap)
      {
        return localObject2;
        localImmutableMap = ImmutableMap.copyOf(this.mLabels);
      }
    }
    finally
    {
    }
  }

  public long getLocalMessageId()
  {
    return this.mLocalMessageId;
  }

  public long getMaxMessageId()
  {
    return this.mMaxMessageId;
  }

  public long getServerMessageId()
  {
    return this.mServerMessageId;
  }

  public int hashCode()
  {
    try
    {
      Object[] arrayOfObject = new Object[5];
      arrayOfObject[0] = Long.valueOf(this.mConversationId);
      arrayOfObject[1] = Long.valueOf(this.mLocalMessageId);
      arrayOfObject[2] = Long.valueOf(this.mServerMessageId);
      arrayOfObject[3] = Long.valueOf(this.mMaxMessageId);
      arrayOfObject[4] = this.mLabels.keySet();
      int i = Objects.hashCode(arrayOfObject);
      return i;
    }
    finally
    {
    }
  }

  public boolean isImportant()
  {
    try
    {
      boolean bool = this.mIsImportant;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(this.mConversationId);
    paramParcel.writeLong(this.mLocalMessageId);
    paramParcel.writeLong(this.mServerMessageId);
    paramParcel.writeLong(this.mMaxMessageId);
    try
    {
      paramParcel.writeString(LabelManager.serialize(this.mLabels));
      return;
    }
    finally
    {
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ConversationInfo
 * JD-Core Version:    0.6.2
 */