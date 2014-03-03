package com.google.common.io.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class ProtoBuf
{
  public static final Boolean FALSE = new Boolean(false);
  private static Long[] SMALL_NUMBERS = arrayOfLong;
  public static final Boolean TRUE = new Boolean(true);
  private ProtoBufType msgType;
  private final Vector values = new Vector();
  private final StringBuffer wireTypes = new StringBuffer();

  static
  {
    Long[] arrayOfLong = new Long[16];
    arrayOfLong[0] = new Long(0L);
    arrayOfLong[1] = new Long(1L);
    arrayOfLong[2] = new Long(2L);
    arrayOfLong[3] = new Long(3L);
    arrayOfLong[4] = new Long(4L);
    arrayOfLong[5] = new Long(5L);
    arrayOfLong[6] = new Long(6L);
    arrayOfLong[7] = new Long(7L);
    arrayOfLong[8] = new Long(8L);
    arrayOfLong[9] = new Long(9L);
    arrayOfLong[10] = new Long(10L);
    arrayOfLong[11] = new Long(11L);
    arrayOfLong[12] = new Long(12L);
    arrayOfLong[13] = new Long(13L);
    arrayOfLong[14] = new Long(14L);
    arrayOfLong[15] = new Long(15L);
  }

  public ProtoBuf(ProtoBufType paramProtoBufType)
  {
    this.msgType = paramProtoBufType;
  }

  private void assertTypeMatch(int paramInt, Object paramObject)
  {
    int i = getType(paramInt);
    if ((i == 16) && (this.msgType == null));
    label19: 
    do
    {
      return;
      if (!(paramObject instanceof Boolean))
        break;
    }
    while ((i == 24) || (i == 0));
    while (true)
    {
      throw new IllegalArgumentException("Type mismatch type:" + this.msgType + " tag:" + paramInt);
      if ((paramObject instanceof Long))
        switch (i)
        {
        case 0:
        case 1:
        case 5:
        case 17:
        case 18:
        case 19:
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
        case 29:
        case 30:
        case 31:
        case 32:
        case 33:
        case 34:
        case 2:
        case 3:
        case 4:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 16:
        case 25:
        case 26:
        case 27:
        case 28:
        }
      else if ((paramObject instanceof byte[]))
        switch (i)
        {
        case 2:
        case 25:
        case 27:
        case 28:
        case 35:
        case 36:
        }
      else if ((paramObject instanceof ProtoBuf))
        switch (i)
        {
        default:
          break;
        case 2:
        case 3:
        case 25:
        case 26:
        case 27:
        case 28:
          if ((this.msgType == null) || (this.msgType.getData(paramInt) == null) || (((ProtoBuf)paramObject).msgType == null))
            break label19;
          if (((ProtoBuf)paramObject).msgType != this.msgType.getData(paramInt))
            continue;
          return;
        }
      else if ((paramObject instanceof String))
        switch (i)
        {
        case 2:
        case 25:
        case 28:
        case 36:
        }
    }
  }

  private Object convert(Object paramObject, int paramInt)
  {
    boolean bool = true;
    switch (paramInt)
    {
    case 17:
    case 18:
    case 20:
    case 29:
    case 30:
    default:
      throw new RuntimeException("Unsupp.Type");
    case 24:
      if (!(paramObject instanceof Boolean))
        break;
    case 16:
    case 19:
    case 21:
    case 22:
    case 23:
    case 31:
    case 32:
    case 33:
    case 34:
    case 25:
    case 35:
    case 28:
    case 36:
    case 26:
    case 27:
    }
    do
    {
      do
      {
        do
        {
          do
          {
            return paramObject;
            switch ((int)((Long)paramObject).longValue())
            {
            default:
              throw new IllegalArgumentException("Type mismatch");
            case 0:
              return FALSE;
            case 1:
            }
            return TRUE;
          }
          while (!(paramObject instanceof Boolean));
          Long[] arrayOfLong = SMALL_NUMBERS;
          if (((Boolean)paramObject).booleanValue());
          while (true)
          {
            return arrayOfLong[bool];
            bool = false;
          }
          if ((paramObject instanceof String))
            return encodeUtf8((String)paramObject);
        }
        while (!(paramObject instanceof ProtoBuf));
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        try
        {
          ((ProtoBuf)paramObject).outputTo(localByteArrayOutputStream);
          byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
          return arrayOfByte2;
        }
        catch (IOException localIOException2)
        {
          throw new RuntimeException(localIOException2.toString());
        }
      }
      while (!(paramObject instanceof byte[]));
      byte[] arrayOfByte1 = (byte[])paramObject;
      return decodeUtf8(arrayOfByte1, 0, arrayOfByte1.length, bool);
    }
    while (!(paramObject instanceof byte[]));
    try
    {
      ProtoBuf localProtoBuf = new ProtoBuf(null).parse((byte[])paramObject);
      return localProtoBuf;
    }
    catch (IOException localIOException1)
    {
      throw new RuntimeException(localIOException1.toString());
    }
  }

  static String decodeUtf8(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramInt2 - paramInt1);
    int i = paramInt1;
    int j;
    label94: int i1;
    int i3;
    int i4;
    int i5;
    label176: int i7;
    if (i < paramInt2)
    {
      j = i + 1;
      int k = 0xFF & paramArrayOfByte[i];
      if (k <= 127)
        localStringBuffer.append((char)k);
      while (true)
      {
        i = j;
        break;
        if (k < 245)
          break label94;
        if (!paramBoolean)
          throw new IllegalArgumentException("Invalid UTF8");
        localStringBuffer.append((char)k);
      }
      int m = 224;
      int n = 1;
      i1 = 128;
      int i2 = 31;
      if (k >= m)
      {
        m = 0x80 | m >> 1;
        if (n == 1);
        for (int i8 = 4; ; i8 = 5)
        {
          i1 <<= i8;
          n++;
          i2 >>= 1;
          break;
        }
      }
      i3 = k & i2;
      i4 = 0;
      i5 = j;
      if (i4 < n)
      {
        i3 <<= 6;
        if (i5 >= paramInt2)
        {
          if (paramBoolean)
            break label375;
          throw new IllegalArgumentException("Invalid UTF8");
        }
        if ((!paramBoolean) && ((0xC0 & paramArrayOfByte[i5]) != 128))
          throw new IllegalArgumentException("Invalid UTF8");
        i7 = i5 + 1;
        i3 |= 0x3F & paramArrayOfByte[i5];
      }
    }
    while (true)
    {
      i4++;
      i5 = i7;
      break label176;
      if (((!paramBoolean) && (i3 < i1)) || ((i3 >= 55296) && (i3 <= 57343)))
        throw new IllegalArgumentException("Invalid UTF8");
      if (i3 <= 65535)
      {
        localStringBuffer.append((char)i3);
        j = i5;
        break;
      }
      int i6 = i3 - 65536;
      localStringBuffer.append((char)(0xD800 | i6 >> 10));
      localStringBuffer.append((char)(0xDC00 | i6 & 0x3FF));
      j = i5;
      break;
      return localStringBuffer.toString();
      label375: i7 = i5;
    }
  }

  static int encodeUtf8(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramString.length();
    int j = 0;
    if (j < i)
    {
      int k = paramString.charAt(j);
      int n;
      if ((k >= 55296) && (k <= 57343) && (j + 1 < i))
      {
        int m = paramString.charAt(j + 1);
        if ((m & 0xFC00 ^ k & 0xFC00) == 1024)
        {
          j++;
          if ((m & 0xFC00) != 55296)
            break label139;
          n = m;
          m = k;
          label92: k = 65536 + ((n & 0x3FF) << 10 | m & 0x3FF);
        }
      }
      if (k <= 127)
      {
        if (paramArrayOfByte != null)
          paramArrayOfByte[paramInt] = ((byte)k);
        paramInt++;
      }
      while (true)
      {
        j++;
        break;
        label139: n = k;
        break label92;
        if (k <= 2047)
        {
          if (paramArrayOfByte != null)
          {
            paramArrayOfByte[paramInt] = ((byte)(0xC0 | k >> 6));
            paramArrayOfByte[(paramInt + 1)] = ((byte)(0x80 | k & 0x3F));
          }
          paramInt += 2;
        }
        else if (k <= 65535)
        {
          if (paramArrayOfByte != null)
          {
            paramArrayOfByte[paramInt] = ((byte)(0xE0 | k >> 12));
            paramArrayOfByte[(paramInt + 1)] = ((byte)(0x80 | 0x3F & k >> 6));
            paramArrayOfByte[(paramInt + 2)] = ((byte)(0x80 | k & 0x3F));
          }
          paramInt += 3;
        }
        else
        {
          if (paramArrayOfByte != null)
          {
            paramArrayOfByte[paramInt] = ((byte)(0xF0 | k >> 18));
            paramArrayOfByte[(paramInt + 1)] = ((byte)(0x80 | 0x3F & k >> 12));
            paramArrayOfByte[(paramInt + 2)] = ((byte)(0x80 | 0x3F & k >> 6));
            paramArrayOfByte[(paramInt + 3)] = ((byte)(0x80 | k & 0x3F));
          }
          paramInt += 4;
        }
      }
    }
    return paramInt;
  }

  static byte[] encodeUtf8(String paramString)
  {
    byte[] arrayOfByte = new byte[encodeUtf8(paramString, null, 0)];
    encodeUtf8(paramString, arrayOfByte, 0);
    return arrayOfByte;
  }

  private int getDataSize(int paramInt1, int paramInt2)
  {
    int i = getVarIntSize(paramInt1 << 3);
    Object localObject;
    int j;
    switch (getWireType(paramInt1))
    {
    case 2:
    case 4:
    default:
      localObject = getObject(paramInt1, paramInt2, 16);
      if ((localObject instanceof byte[]))
        j = ((byte[])localObject).length;
      break;
    case 5:
    case 1:
    case 0:
    case 3:
    }
    while (true)
    {
      return j + (i + getVarIntSize(j));
      return i + 4;
      return i + 8;
      long l = getLong(paramInt1, paramInt2);
      if (isZigZagEncodedType(paramInt1))
        l = zigZagEncode(l);
      return i + getVarIntSize(l);
      return i + (i + getProtoBuf(paramInt1, paramInt2).getDataSize());
      if ((localObject instanceof String))
        j = encodeUtf8((String)localObject, null, 0);
      else
        j = ((ProtoBuf)localObject).getDataSize();
    }
  }

  private Object getDefault(int paramInt)
  {
    switch (getType(paramInt))
    {
    default:
      return this.msgType.getData(paramInt);
    case 16:
    case 26:
    case 27:
    }
    return null;
  }

  private Object getObject(int paramInt1, int paramInt2)
  {
    int i = getCount(paramInt1);
    if (i == 0)
      return getDefault(paramInt1);
    if (i > 1)
      throw new IllegalArgumentException();
    return getObject(paramInt1, 0, paramInt2);
  }

  private Object getObject(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 >= getCount(paramInt1))
      throw new ArrayIndexOutOfBoundsException();
    Object localObject1 = this.values.elementAt(paramInt1);
    boolean bool = localObject1 instanceof Vector;
    Vector localVector = null;
    if (bool)
    {
      localVector = (Vector)localObject1;
      localObject1 = localVector.elementAt(paramInt2);
    }
    Object localObject2 = convert(localObject1, paramInt3);
    if ((localObject2 != localObject1) && (localObject1 != null))
    {
      if (localVector == null)
        setObject(paramInt1, localObject2);
    }
    else
      return localObject2;
    localVector.setElementAt(localObject2, paramInt2);
    return localObject2;
  }

  private static int getVarIntSize(long paramLong)
  {
    int i;
    if (paramLong < 0L)
      i = 10;
    while (true)
    {
      return i;
      i = 1;
      while (paramLong >= 128L)
      {
        i++;
        paramLong >>= 7;
      }
    }
  }

  private final int getWireType(int paramInt)
  {
    int i = getType(paramInt);
    switch (i)
    {
    case 4:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    default:
      throw new RuntimeException("Unsupp.Type:" + this.msgType + '/' + paramInt + '/' + i);
    case 19:
    case 20:
    case 21:
    case 24:
    case 29:
    case 30:
    case 33:
    case 34:
      i = 0;
    case 0:
    case 1:
    case 2:
    case 3:
    case 5:
    case 16:
      return i;
    case 25:
    case 27:
    case 28:
    case 35:
    case 36:
      return 2;
    case 17:
    case 22:
    case 32:
      return 1;
    case 18:
    case 23:
    case 31:
      return 5;
    case 26:
    }
    return 3;
  }

  private void insertObject(int paramInt1, int paramInt2, Object paramObject)
  {
    assertTypeMatch(paramInt1, paramObject);
    if (getCount(paramInt1) == 0)
    {
      setObject(paramInt1, paramObject);
      return;
    }
    Object localObject = this.values.elementAt(paramInt1);
    Vector localVector;
    if ((localObject instanceof Vector))
      localVector = (Vector)localObject;
    while (true)
    {
      localVector.insertElementAt(paramObject, paramInt2);
      return;
      localVector = new Vector();
      localVector.addElement(localObject);
      this.values.setElementAt(localVector, paramInt1);
    }
  }

  private boolean isZigZagEncodedType(int paramInt)
  {
    int i = getType(paramInt);
    return (i == 33) || (i == 34);
  }

  static long readVarInt(InputStream paramInputStream, boolean paramBoolean)
    throws IOException
  {
    long l = 0L;
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j < 10)
      {
        int k = paramInputStream.read();
        if (k == -1)
        {
          if ((j == 0) && (paramBoolean))
            return -1L;
          throw new IOException("EOF");
        }
        l |= (k & 0x7F) << i;
        if ((k & 0x80) != 0);
      }
      else
      {
        return l;
      }
      i += 7;
    }
  }

  private void setObject(int paramInt, Object paramObject)
  {
    if (this.values.size() <= paramInt)
      this.values.setSize(paramInt + 1);
    if (paramObject != null)
      assertTypeMatch(paramInt, paramObject);
    this.values.setElementAt(paramObject, paramInt);
  }

  static void writeVarInt(OutputStream paramOutputStream, long paramLong)
    throws IOException
  {
    for (int i = 0; ; i++)
    {
      int j;
      if (i < 10)
      {
        j = (int)(0x7F & paramLong);
        paramLong >>>= 7;
        if (paramLong == 0L)
          paramOutputStream.write(j);
      }
      else
      {
        return;
      }
      paramOutputStream.write(j | 0x80);
    }
  }

  private static long zigZagDecode(long paramLong)
  {
    return paramLong >>> 1 ^ -(1L & paramLong);
  }

  private static long zigZagEncode(long paramLong)
  {
    return paramLong << 1 ^ -(paramLong >>> 63);
  }

  public void addLong(int paramInt, long paramLong)
  {
    insertLong(paramInt, getCount(paramInt), paramLong);
  }

  public ProtoBuf addNewProtoBuf(int paramInt)
  {
    ProtoBuf localProtoBuf = newProtoBufForTag(paramInt);
    addProtoBuf(paramInt, localProtoBuf);
    return localProtoBuf;
  }

  public void addProtoBuf(int paramInt, ProtoBuf paramProtoBuf)
  {
    insertProtoBuf(paramInt, getCount(paramInt), paramProtoBuf);
  }

  public void addString(int paramInt, String paramString)
  {
    insertString(paramInt, getCount(paramInt), paramString);
  }

  public void clear()
  {
    this.values.setSize(0);
    this.wireTypes.setLength(0);
  }

  public boolean getBool(int paramInt)
  {
    return ((Boolean)getObject(paramInt, 24)).booleanValue();
  }

  public byte[] getBytes(int paramInt)
  {
    return (byte[])getObject(paramInt, 25);
  }

  public int getCount(int paramInt)
  {
    if (paramInt >= this.values.size());
    Object localObject;
    do
    {
      return 0;
      localObject = this.values.elementAt(paramInt);
    }
    while (localObject == null);
    if ((localObject instanceof Vector))
      return ((Vector)localObject).size();
    return 1;
  }

  public int getDataSize()
  {
    int i = 0;
    for (int j = 0; j <= maxTag(); j++)
      for (int k = 0; k < getCount(j); k++)
        i += getDataSize(j, k);
    return i;
  }

  public int getInt(int paramInt)
  {
    return (int)((Long)getObject(paramInt, 21)).longValue();
  }

  public long getLong(int paramInt)
  {
    return ((Long)getObject(paramInt, 19)).longValue();
  }

  public long getLong(int paramInt1, int paramInt2)
  {
    return ((Long)getObject(paramInt1, paramInt2, 19)).longValue();
  }

  public ProtoBuf getProtoBuf(int paramInt)
  {
    return (ProtoBuf)getObject(paramInt, 26);
  }

  public ProtoBuf getProtoBuf(int paramInt1, int paramInt2)
  {
    return (ProtoBuf)getObject(paramInt1, paramInt2, 26);
  }

  public String getString(int paramInt)
  {
    return (String)getObject(paramInt, 28);
  }

  public String getString(int paramInt1, int paramInt2)
  {
    return (String)getObject(paramInt1, paramInt2, 28);
  }

  public int getType(int paramInt)
  {
    int i = 16;
    if (this.msgType != null)
      i = this.msgType.getType(paramInt);
    if ((i == 16) && (paramInt < this.wireTypes.length()))
      i = this.wireTypes.charAt(paramInt);
    if ((i == 16) && (getCount(paramInt) > 0))
    {
      Object localObject = getObject(paramInt, 0, 16);
      if (((localObject instanceof Long)) || ((localObject instanceof Boolean)))
        i = 0;
    }
    else
    {
      return i;
    }
    return 2;
  }

  public boolean has(int paramInt)
  {
    return (getCount(paramInt) > 0) || (getDefault(paramInt) != null);
  }

  public void insertLong(int paramInt1, int paramInt2, long paramLong)
  {
    if ((paramLong >= 0L) && (paramLong < SMALL_NUMBERS.length));
    for (Long localLong = SMALL_NUMBERS[((int)paramLong)]; ; localLong = new Long(paramLong))
    {
      insertObject(paramInt1, paramInt2, localLong);
      return;
    }
  }

  public void insertProtoBuf(int paramInt1, int paramInt2, ProtoBuf paramProtoBuf)
  {
    insertObject(paramInt1, paramInt2, paramProtoBuf);
  }

  public void insertString(int paramInt1, int paramInt2, String paramString)
  {
    insertObject(paramInt1, paramInt2, paramString);
  }

  public int maxTag()
  {
    return -1 + this.values.size();
  }

  public ProtoBuf newProtoBufForTag(int paramInt)
  {
    return new ProtoBuf((ProtoBufType)this.msgType.getData(paramInt));
  }

  public void outputTo(OutputStream paramOutputStream)
    throws IOException
  {
    for (int i = 0; i <= maxTag(); i++)
    {
      int j = getCount(i);
      int k = getWireType(i);
      int m = 0;
      if (m < j)
      {
        writeVarInt(paramOutputStream, k | i << 3);
        switch (k)
        {
        case 4:
        default:
          throw new IllegalArgumentException();
        case 1:
        case 5:
          long l2 = ((Long)getObject(i, m, 19)).longValue();
          if (k == 5);
          for (int i1 = 4; ; i1 = 8)
            for (int i2 = 0; i2 < i1; i2++)
            {
              paramOutputStream.write((int)(0xFF & l2));
              l2 >>= 8;
            }
        case 0:
          long l1 = ((Long)getObject(i, m, 19)).longValue();
          if (isZigZagEncodedType(i))
            l1 = zigZagEncode(l1);
          writeVarInt(paramOutputStream, l1);
        case 2:
        case 3:
        }
        while (true)
        {
          m++;
          break;
          if (getType(i) == 27);
          Object localObject;
          for (int n = 16; ; n = 25)
          {
            localObject = getObject(i, m, n);
            if (!(localObject instanceof byte[]))
              break label270;
            byte[] arrayOfByte = (byte[])localObject;
            writeVarInt(paramOutputStream, arrayOfByte.length);
            paramOutputStream.write(arrayOfByte);
            break;
          }
          label270: ProtoBuf localProtoBuf = (ProtoBuf)localObject;
          writeVarInt(paramOutputStream, localProtoBuf.getDataSize());
          localProtoBuf.outputTo(paramOutputStream);
          continue;
          ((ProtoBuf)getObject(i, m, 26)).outputTo(paramOutputStream);
          writeVarInt(paramOutputStream, 0x4 | i << 3);
        }
      }
    }
  }

  public int parse(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    clear();
    long l1;
    if (paramInt > 0)
    {
      l1 = readVarInt(paramInputStream, true);
      if (l1 != -1L)
        break label34;
    }
    while (paramInt < 0)
    {
      throw new IOException();
      label34: paramInt -= getVarIntSize(l1);
      int i = 0x7 & (int)l1;
      if (i != 4)
      {
        int j = (int)(l1 >>> 3);
        while (this.wireTypes.length() <= j)
          this.wireTypes.append('\020');
        this.wireTypes.setCharAt(j, (char)i);
        long l3;
        Object localObject;
        switch (i)
        {
        case 4:
        default:
          throw new RuntimeException("Unsupp.Type" + i);
        case 0:
          l3 = readVarInt(paramInputStream, false);
          paramInt -= getVarIntSize(l3);
          if (isZigZagEncodedType(j))
            l3 = zigZagDecode(l3);
          if ((l3 >= 0L) && (l3 < SMALL_NUMBERS.length))
            localObject = SMALL_NUMBERS[((int)l3)];
        case 1:
        case 5:
        case 2:
          while (true)
          {
            insertObject(j, getCount(j), localObject);
            break;
            localObject = new Long(l3);
            continue;
            long l2 = 0L;
            int i1 = 0;
            if (i == 5);
            for (int i2 = 4; ; i2 = 8)
            {
              paramInt -= i2;
              int i4;
              for (int i3 = i2; ; i3 = i4)
              {
                i4 = i3 - 1;
                if (i3 <= 0)
                  break;
                l2 |= paramInputStream.read() << i1;
                i1 += 8;
              }
            }
            if ((l2 >= 0L) && (l2 < SMALL_NUMBERS.length));
            for (localObject = SMALL_NUMBERS[((int)l2)]; ; localObject = new Long(l2))
              break;
            int k = (int)readVarInt(paramInputStream, false);
            paramInt = paramInt - getVarIntSize(k) - k;
            if (getType(j) == 27)
            {
              ProtoBuf localProtoBuf2 = new ProtoBuf((ProtoBufType)this.msgType.getData(j));
              localProtoBuf2.parse(paramInputStream, k);
              localObject = localProtoBuf2;
            }
            else
            {
              byte[] arrayOfByte = new byte[k];
              int m = 0;
              while (m < k)
              {
                int n = paramInputStream.read(arrayOfByte, m, k - m);
                if (n <= 0)
                  throw new IOException("Unexp.EOF");
                m += n;
              }
              localObject = arrayOfByte;
            }
          }
        case 3:
        }
        if (this.msgType == null);
        for (ProtoBufType localProtoBufType = null; ; localProtoBufType = (ProtoBufType)this.msgType.getData(j))
        {
          ProtoBuf localProtoBuf1 = new ProtoBuf(localProtoBufType);
          paramInt = localProtoBuf1.parse(paramInputStream, paramInt);
          localObject = localProtoBuf1;
          break;
        }
      }
    }
    return paramInt;
  }

  public ProtoBuf parse(InputStream paramInputStream)
    throws IOException
  {
    parse(paramInputStream, 2147483647);
    return this;
  }

  public ProtoBuf parse(byte[] paramArrayOfByte)
    throws IOException
  {
    parse(new ByteArrayInputStream(paramArrayOfByte), paramArrayOfByte.length);
    return this;
  }

  public void setBool(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean);
    for (Boolean localBoolean = TRUE; ; localBoolean = FALSE)
    {
      setObject(paramInt, localBoolean);
      return;
    }
  }

  public void setBytes(int paramInt, byte[] paramArrayOfByte)
  {
    setObject(paramInt, paramArrayOfByte);
  }

  public void setInt(int paramInt1, int paramInt2)
  {
    setLong(paramInt1, paramInt2);
  }

  public void setLong(int paramInt, long paramLong)
  {
    if ((paramLong >= 0L) && (paramLong < SMALL_NUMBERS.length));
    for (Long localLong = SMALL_NUMBERS[((int)paramLong)]; ; localLong = new Long(paramLong))
    {
      setObject(paramInt, localLong);
      return;
    }
  }

  public ProtoBuf setNewProtoBuf(int paramInt)
  {
    ProtoBuf localProtoBuf = newProtoBufForTag(paramInt);
    setProtoBuf(paramInt, localProtoBuf);
    return localProtoBuf;
  }

  public void setProtoBuf(int paramInt, ProtoBuf paramProtoBuf)
  {
    setObject(paramInt, paramProtoBuf);
  }

  public void setString(int paramInt, String paramString)
  {
    setObject(paramInt, paramString);
  }

  void setType(ProtoBufType paramProtoBufType)
  {
    if ((this.values.size() != 0) || ((this.msgType != null) && (paramProtoBufType != null) && (paramProtoBufType != this.msgType)))
      throw new IllegalArgumentException();
    this.msgType = paramProtoBufType;
  }

  public byte[] toByteArray()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    outputTo(localByteArrayOutputStream);
    return localByteArrayOutputStream.toByteArray();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.protocol.ProtoBuf
 * JD-Core Version:    0.6.2
 */