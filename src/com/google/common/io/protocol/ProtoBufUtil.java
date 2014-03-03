package com.google.common.io.protocol;

import java.io.IOException;
import java.io.InputStream;

public final class ProtoBufUtil
{
  public static int readNextProtoBuf(ProtoBufType paramProtoBufType, InputStream paramInputStream, ProtoBuf paramProtoBuf)
    throws IOException
  {
    long l = ProtoBuf.readVarInt(paramInputStream, true);
    if (l == -1L)
      return -1;
    if ((0x7 & l) != 2L)
      throw new IOException("Message expected");
    int i = (int)(l >>> 3);
    paramProtoBuf.setType((ProtoBufType)paramProtoBufType.getData(i));
    paramProtoBuf.parse(paramInputStream, (int)ProtoBuf.readVarInt(paramInputStream, false));
    return i;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.protocol.ProtoBufUtil
 * JD-Core Version:    0.6.2
 */