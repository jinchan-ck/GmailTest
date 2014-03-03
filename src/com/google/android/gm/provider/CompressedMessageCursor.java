package com.google.android.gm.provider;

import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.CursorWrapper;

public class CompressedMessageCursor extends CursorWrapper
  implements CrossProcessCursor
{
  private final int bodyIndex;
  private final CrossProcessCursor mCursor;

  public CompressedMessageCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mCursor = ((CrossProcessCursor)paramCursor);
    this.bodyIndex = super.getColumnIndex("body");
  }

  // ERROR //
  private String getMessageBody()
  {
    // Byte code:
    //   0: new 32	android/util/TimingLogger
    //   3: dup
    //   4: ldc 34
    //   6: ldc 35
    //   8: invokespecial 38	android/util/TimingLogger:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   11: astore_1
    //   12: aload_0
    //   13: aload_0
    //   14: getfield 24	com/google/android/gm/provider/CompressedMessageCursor:bodyIndex	I
    //   17: invokespecial 42	android/database/CursorWrapper:getBlob	(I)[B
    //   20: astore_3
    //   21: aload_3
    //   22: ifnonnull +9 -> 31
    //   25: aload_1
    //   26: invokevirtual 46	android/util/TimingLogger:dumpToLog	()V
    //   29: aconst_null
    //   30: areturn
    //   31: aload_3
    //   32: iconst_0
    //   33: baload
    //   34: bipush 48
    //   36: if_icmpne +39 -> 75
    //   39: iconst_m1
    //   40: aload_3
    //   41: arraylength
    //   42: iadd
    //   43: istore 10
    //   45: aload_3
    //   46: iload 10
    //   48: baload
    //   49: ifne +6 -> 55
    //   52: iinc 10 255
    //   55: new 48	java/lang/String
    //   58: dup
    //   59: aload_3
    //   60: iconst_1
    //   61: iload 10
    //   63: invokespecial 51	java/lang/String:<init>	([BII)V
    //   66: astore 11
    //   68: aload_1
    //   69: invokevirtual 46	android/util/TimingLogger:dumpToLog	()V
    //   72: aload 11
    //   74: areturn
    //   75: aload_3
    //   76: iconst_1
    //   77: iconst_m1
    //   78: aload_3
    //   79: arraylength
    //   80: iadd
    //   81: invokestatic 57	com/google/android/gm/provider/ZipUtils:inflateToUTF8	([BII)Ljava/lang/String;
    //   84: astore 9
    //   86: aload_1
    //   87: invokevirtual 46	android/util/TimingLogger:dumpToLog	()V
    //   90: aload 9
    //   92: areturn
    //   93: astore 5
    //   95: aload_0
    //   96: ldc 59
    //   98: invokevirtual 60	com/google/android/gm/provider/CompressedMessageCursor:getColumnIndex	(Ljava/lang/String;)I
    //   101: istore 6
    //   103: iload 6
    //   105: ifge +28 -> 133
    //   108: ldc2_w 61
    //   111: lstore 7
    //   113: new 64	com/google/android/gm/provider/CompressedMessageCursor$CorruptedMessageException
    //   116: dup
    //   117: aload_0
    //   118: lload 7
    //   120: aload 5
    //   122: invokespecial 67	com/google/android/gm/provider/CompressedMessageCursor$CorruptedMessageException:<init>	(Lcom/google/android/gm/provider/CompressedMessageCursor;JLjava/lang/Throwable;)V
    //   125: athrow
    //   126: astore_2
    //   127: aload_1
    //   128: invokevirtual 46	android/util/TimingLogger:dumpToLog	()V
    //   131: aload_2
    //   132: athrow
    //   133: aload_0
    //   134: iload 6
    //   136: invokevirtual 71	com/google/android/gm/provider/CompressedMessageCursor:getLong	(I)J
    //   139: lstore 7
    //   141: goto -28 -> 113
    //   144: astore 4
    //   146: new 73	java/lang/IllegalStateException
    //   149: dup
    //   150: ldc 75
    //   152: invokespecial 78	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   155: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   75	86	93	java/util/zip/DataFormatException
    //   12	21	126	finally
    //   31	52	126	finally
    //   55	68	126	finally
    //   75	86	126	finally
    //   95	103	126	finally
    //   113	126	126	finally
    //   133	141	126	finally
    //   146	156	126	finally
    //   75	86	144	java/io/UnsupportedEncodingException
  }

  public void fillWindow(int paramInt, CursorWindow paramCursorWindow)
  {
    this.mCursor.fillWindow(paramInt, paramCursorWindow);
    if ((paramInt < 0) || (paramInt > getCount()) || (this.bodyIndex == -1))
      return;
    paramCursorWindow.acquireReference();
    int i;
    try
    {
      i = getPosition();
      moveToPosition(paramInt - 1);
      while (moveToNext())
        paramCursorWindow.putString(getMessageBody(), getPosition(), this.bodyIndex);
    }
    finally
    {
      paramCursorWindow.releaseReference();
    }
    moveToPosition(i);
    paramCursorWindow.releaseReference();
  }

  public String getString(int paramInt)
  {
    if (paramInt != this.bodyIndex)
      return super.getString(paramInt);
    return getMessageBody();
  }

  public CursorWindow getWindow()
  {
    return null;
  }

  public boolean onMove(int paramInt1, int paramInt2)
  {
    return this.mCursor.onMove(paramInt1, paramInt2);
  }

  public class CorruptedMessageException extends RuntimeException
  {
    private final long mMessageId;

    public CorruptedMessageException(long arg2, Throwable arg4)
    {
    }

    public long getMessageId()
    {
      return this.mMessageId;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.CompressedMessageCursor
 * JD-Core Version:    0.6.2
 */