package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.zip.Checksum;

public final class ByteStreams
{
  private static final int BUF_SIZE = 4096;

  public static long copy(InputSupplier<? extends InputStream> paramInputSupplier, OutputSupplier<? extends OutputStream> paramOutputSupplier)
    throws IOException
  {
    boolean bool = true;
    InputStream localInputStream = (InputStream)paramInputSupplier.getInput();
    try
    {
      OutputStream localOutputStream = (OutputStream)paramOutputSupplier.getOutput();
      try
      {
        long l = copy(localInputStream, localOutputStream);
        bool = false;
        Closeables.close(localOutputStream, false);
        Closeables.close(localInputStream, false);
        return l;
      }
      finally
      {
      }
    }
    finally
    {
      Closeables.close(localInputStream, bool);
    }
  }

  public static long copy(InputSupplier<? extends InputStream> paramInputSupplier, OutputStream paramOutputStream)
    throws IOException
  {
    InputStream localInputStream = (InputStream)paramInputSupplier.getInput();
    try
    {
      long l = copy(localInputStream, paramOutputStream);
      Closeables.close(localInputStream, false);
      return l;
    }
    finally
    {
      Closeables.close(localInputStream, true);
    }
  }

  public static long copy(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4096];
    int i;
    for (long l = 0L; ; l += i)
    {
      i = paramInputStream.read(arrayOfByte);
      if (i == -1)
        return l;
      paramOutputStream.write(arrayOfByte, 0, i);
    }
  }

  public static long copy(ReadableByteChannel paramReadableByteChannel, WritableByteChannel paramWritableByteChannel)
    throws IOException
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(4096);
    long l = 0L;
    while (paramReadableByteChannel.read(localByteBuffer) != -1)
    {
      localByteBuffer.flip();
      while (localByteBuffer.hasRemaining())
        l += paramWritableByteChannel.write(localByteBuffer);
      localByteBuffer.clear();
    }
    return l;
  }

  public static boolean equal(InputSupplier<? extends InputStream> paramInputSupplier1, InputSupplier<? extends InputStream> paramInputSupplier2)
    throws IOException
  {
    byte[] arrayOfByte1 = new byte[4096];
    byte[] arrayOfByte2 = new byte[4096];
    boolean bool1 = true;
    InputStream localInputStream1 = (InputStream)paramInputSupplier1.getInput();
    try
    {
      InputStream localInputStream2 = (InputStream)paramInputSupplier2.getInput();
      try
      {
        int i;
        do
        {
          i = read(localInputStream1, arrayOfByte1, 0, 4096);
          if (i == read(localInputStream2, arrayOfByte2, 0, 4096))
          {
            boolean bool2 = Arrays.equals(arrayOfByte1, arrayOfByte2);
            if (bool2);
          }
          else
          {
            bool1 = false;
            Closeables.close(localInputStream2, false);
            Closeables.close(localInputStream1, false);
            return false;
          }
        }
        while (i == 4096);
        bool1 = false;
        Closeables.close(localInputStream2, false);
        Closeables.close(localInputStream1, false);
        return true;
      }
      finally
      {
      }
    }
    finally
    {
      Closeables.close(localInputStream1, bool1);
    }
  }

  public static long getChecksum(InputSupplier<? extends InputStream> paramInputSupplier, Checksum paramChecksum)
    throws IOException
  {
    return ((Long)readBytes(paramInputSupplier, new ByteProcessor()
    {
      public Long getResult()
      {
        long l = this.val$checksum.getValue();
        this.val$checksum.reset();
        return Long.valueOf(l);
      }

      public boolean processBytes(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        this.val$checksum.update(paramAnonymousArrayOfByte, paramAnonymousInt1, paramAnonymousInt2);
        return true;
      }
    })).longValue();
  }

  public static byte[] getDigest(InputSupplier<? extends InputStream> paramInputSupplier, MessageDigest paramMessageDigest)
    throws IOException
  {
    return (byte[])readBytes(paramInputSupplier, new ByteProcessor()
    {
      public byte[] getResult()
      {
        return this.val$md.digest();
      }

      public boolean processBytes(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        this.val$md.update(paramAnonymousArrayOfByte, paramAnonymousInt1, paramAnonymousInt2);
        return true;
      }
    });
  }

  public static InputSupplier<InputStream> join(Iterable<? extends InputSupplier<? extends InputStream>> paramIterable)
  {
    return new InputSupplier()
    {
      public InputStream getInput()
        throws IOException
      {
        return new MultiInputStream(this.val$suppliers.iterator());
      }
    };
  }

  public static InputSupplier<InputStream> join(InputSupplier<? extends InputStream>[] paramArrayOfInputSupplier)
  {
    return join(Arrays.asList(paramArrayOfInputSupplier));
  }

  public static long length(InputSupplier<? extends InputStream> paramInputSupplier)
    throws IOException
  {
    long l1 = 0L;
    InputStream localInputStream = (InputStream)paramInputSupplier.getInput();
    try
    {
      while (true)
      {
        long l2 = localInputStream.skip(2147483647L);
        if (l2 == 0L)
        {
          int i = localInputStream.read();
          if (i == -1)
          {
            Closeables.close(localInputStream, false);
            return l1;
          }
          l1 += 1L;
        }
        else
        {
          l1 += l2;
        }
      }
    }
    finally
    {
      Closeables.close(localInputStream, true);
    }
  }

  public static ByteArrayDataInput newDataInput(byte[] paramArrayOfByte)
  {
    return new ByteArrayDataInputStream(paramArrayOfByte);
  }

  public static ByteArrayDataInput newDataInput(byte[] paramArrayOfByte, int paramInt)
  {
    Preconditions.checkPositionIndex(paramInt, paramArrayOfByte.length);
    return new ByteArrayDataInputStream(paramArrayOfByte, paramInt);
  }

  public static ByteArrayDataOutput newDataOutput()
  {
    return new ByteArrayDataOutputStream();
  }

  public static ByteArrayDataOutput newDataOutput(int paramInt)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool, "Invalid size: %s", arrayOfObject);
      return new ByteArrayDataOutputStream(paramInt);
    }
  }

  public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] paramArrayOfByte)
  {
    return newInputStreamSupplier(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] paramArrayOfByte, final int paramInt1, final int paramInt2)
  {
    return new InputSupplier()
    {
      public ByteArrayInputStream getInput()
      {
        return new ByteArrayInputStream(this.val$b, paramInt1, paramInt2);
      }
    };
  }

  public static int read(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 < 0)
      throw new IndexOutOfBoundsException("len is negative");
    int i = 0;
    while (true)
    {
      int j;
      if (i < paramInt2)
      {
        j = paramInputStream.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
        if (j != -1);
      }
      else
      {
        return i;
      }
      i += j;
    }
  }

  public static <T> T readBytes(InputSupplier<? extends InputStream> paramInputSupplier, ByteProcessor<T> paramByteProcessor)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4096];
    boolean bool1 = true;
    InputStream localInputStream = (InputStream)paramInputSupplier.getInput();
    try
    {
      int i = localInputStream.read(arrayOfByte);
      if (i == -1)
        bool1 = false;
      while (true)
      {
        Object localObject2 = paramByteProcessor.getResult();
        return localObject2;
        boolean bool2 = paramByteProcessor.processBytes(arrayOfByte, 0, i);
        if (bool2)
          break;
      }
    }
    finally
    {
      Closeables.close(localInputStream, bool1);
    }
  }

  public static void readFully(InputStream paramInputStream, byte[] paramArrayOfByte)
    throws IOException
  {
    readFully(paramInputStream, paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public static void readFully(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (read(paramInputStream, paramArrayOfByte, paramInt1, paramInt2) != paramInt2)
      throw new EOFException();
  }

  public static void skipFully(InputStream paramInputStream, long paramLong)
    throws IOException
  {
    while (paramLong > 0L)
    {
      long l = paramInputStream.skip(paramLong);
      if (l == 0L)
      {
        if (paramInputStream.read() == -1)
          throw new EOFException();
        paramLong -= 1L;
      }
      else
      {
        paramLong -= l;
      }
    }
  }

  public static InputSupplier<InputStream> slice(InputSupplier<? extends InputStream> paramInputSupplier, final long paramLong1, long paramLong2)
  {
    Preconditions.checkNotNull(paramInputSupplier);
    boolean bool1;
    if (paramLong1 >= 0L)
    {
      bool1 = true;
      Preconditions.checkArgument(bool1, "offset is negative");
      if (paramLong2 < 0L)
        break label54;
    }
    label54: for (boolean bool2 = true; ; bool2 = false)
    {
      Preconditions.checkArgument(bool2, "length is negative");
      return new InputSupplier()
      {
        public InputStream getInput()
          throws IOException
        {
          InputStream localInputStream = (InputStream)this.val$supplier.getInput();
          if (paramLong1 > 0L);
          try
          {
            ByteStreams.skipFully(localInputStream, paramLong1);
            return new LimitInputStream(localInputStream, this.val$length);
          }
          catch (IOException localIOException)
          {
            Closeables.closeQuietly(localInputStream);
            throw localIOException;
          }
        }
      };
      bool1 = false;
      break;
    }
  }

  public static byte[] toByteArray(InputSupplier<? extends InputStream> paramInputSupplier)
    throws IOException
  {
    InputStream localInputStream = (InputStream)paramInputSupplier.getInput();
    try
    {
      byte[] arrayOfByte = toByteArray(localInputStream);
      Closeables.close(localInputStream, false);
      return arrayOfByte;
    }
    finally
    {
      Closeables.close(localInputStream, true);
    }
  }

  public static byte[] toByteArray(InputStream paramInputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    copy(paramInputStream, localByteArrayOutputStream);
    return localByteArrayOutputStream.toByteArray();
  }

  public static void write(byte[] paramArrayOfByte, OutputSupplier<? extends OutputStream> paramOutputSupplier)
    throws IOException
  {
    Preconditions.checkNotNull(paramArrayOfByte);
    OutputStream localOutputStream = (OutputStream)paramOutputSupplier.getOutput();
    try
    {
      localOutputStream.write(paramArrayOfByte);
      Closeables.close(localOutputStream, false);
      return;
    }
    finally
    {
      Closeables.close(localOutputStream, true);
    }
  }

  private static class ByteArrayDataInputStream
    implements ByteArrayDataInput
  {
    final DataInput input;

    ByteArrayDataInputStream(byte[] paramArrayOfByte)
    {
      this.input = new DataInputStream(new ByteArrayInputStream(paramArrayOfByte));
    }

    ByteArrayDataInputStream(byte[] paramArrayOfByte, int paramInt)
    {
      this.input = new DataInputStream(new ByteArrayInputStream(paramArrayOfByte, paramInt, paramArrayOfByte.length - paramInt));
    }

    public boolean readBoolean()
    {
      try
      {
        boolean bool = this.input.readBoolean();
        return bool;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public byte readByte()
    {
      try
      {
        byte b = this.input.readByte();
        return b;
      }
      catch (EOFException localEOFException)
      {
        throw new IllegalStateException(localEOFException);
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public char readChar()
    {
      try
      {
        char c = this.input.readChar();
        return c;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public double readDouble()
    {
      try
      {
        double d = this.input.readDouble();
        return d;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public float readFloat()
    {
      try
      {
        float f = this.input.readFloat();
        return f;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public void readFully(byte[] paramArrayOfByte)
    {
      try
      {
        this.input.readFully(paramArrayOfByte);
        return;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      try
      {
        this.input.readFully(paramArrayOfByte, paramInt1, paramInt2);
        return;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public int readInt()
    {
      try
      {
        int i = this.input.readInt();
        return i;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public String readLine()
    {
      try
      {
        String str = this.input.readLine();
        return str;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public long readLong()
    {
      try
      {
        long l = this.input.readLong();
        return l;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public short readShort()
    {
      try
      {
        short s = this.input.readShort();
        return s;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public String readUTF()
    {
      try
      {
        String str = this.input.readUTF();
        return str;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public int readUnsignedByte()
    {
      try
      {
        int i = this.input.readUnsignedByte();
        return i;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public int readUnsignedShort()
    {
      try
      {
        int i = this.input.readUnsignedShort();
        return i;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }

    public int skipBytes(int paramInt)
    {
      try
      {
        int i = this.input.skipBytes(paramInt);
        return i;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
    }
  }

  private static class ByteArrayDataOutputStream
    implements ByteArrayDataOutput
  {
    final ByteArrayOutputStream byteArrayOutputSteam;
    final DataOutput output;

    ByteArrayDataOutputStream()
    {
      this(new ByteArrayOutputStream());
    }

    ByteArrayDataOutputStream(int paramInt)
    {
      this(new ByteArrayOutputStream(paramInt));
    }

    ByteArrayDataOutputStream(ByteArrayOutputStream paramByteArrayOutputStream)
    {
      this.byteArrayOutputSteam = paramByteArrayOutputStream;
      this.output = new DataOutputStream(paramByteArrayOutputStream);
    }

    public byte[] toByteArray()
    {
      return this.byteArrayOutputSteam.toByteArray();
    }

    public void write(int paramInt)
    {
      try
      {
        this.output.write(paramInt);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void write(byte[] paramArrayOfByte)
    {
      try
      {
        this.output.write(paramArrayOfByte);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      try
      {
        this.output.write(paramArrayOfByte, paramInt1, paramInt2);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeBoolean(boolean paramBoolean)
    {
      try
      {
        this.output.writeBoolean(paramBoolean);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeByte(int paramInt)
    {
      try
      {
        this.output.writeByte(paramInt);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeBytes(String paramString)
    {
      try
      {
        this.output.writeBytes(paramString);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeChar(int paramInt)
    {
      try
      {
        this.output.writeChar(paramInt);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeChars(String paramString)
    {
      try
      {
        this.output.writeChars(paramString);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeDouble(double paramDouble)
    {
      try
      {
        this.output.writeDouble(paramDouble);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeFloat(float paramFloat)
    {
      try
      {
        this.output.writeFloat(paramFloat);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeInt(int paramInt)
    {
      try
      {
        this.output.writeInt(paramInt);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeLong(long paramLong)
    {
      try
      {
        this.output.writeLong(paramLong);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeShort(int paramInt)
    {
      try
      {
        this.output.writeShort(paramInt);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public void writeUTF(String paramString)
    {
      try
      {
        this.output.writeUTF(paramString);
        return;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.ByteStreams
 * JD-Core Version:    0.6.2
 */