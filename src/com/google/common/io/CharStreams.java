package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CharStreams
{
  private static final int BUF_SIZE = 2048;

  public static Writer asWriter(Appendable paramAppendable)
  {
    if ((paramAppendable instanceof Writer))
      return (Writer)paramAppendable;
    return new AppendableWriter(paramAppendable);
  }

  public static <R extends Readable,  extends Closeable, W extends Appendable,  extends Closeable> long copy(InputSupplier<R> paramInputSupplier, OutputSupplier<W> paramOutputSupplier)
    throws IOException
  {
    boolean bool = true;
    Readable localReadable = (Readable)paramInputSupplier.getInput();
    try
    {
      Appendable localAppendable = (Appendable)paramOutputSupplier.getOutput();
      try
      {
        long l = copy(localReadable, localAppendable);
        bool = false;
        Closeables.close((Closeable)localAppendable, false);
        Closeables.close((Closeable)localReadable, false);
        return l;
      }
      finally
      {
      }
    }
    finally
    {
      Closeables.close((Closeable)localReadable, bool);
    }
  }

  public static <R extends Readable,  extends Closeable> long copy(InputSupplier<R> paramInputSupplier, Appendable paramAppendable)
    throws IOException
  {
    Readable localReadable = (Readable)paramInputSupplier.getInput();
    try
    {
      long l = copy(localReadable, paramAppendable);
      Closeables.close((Closeable)localReadable, false);
      return l;
    }
    finally
    {
      Closeables.close((Closeable)localReadable, true);
    }
  }

  public static long copy(Readable paramReadable, Appendable paramAppendable)
    throws IOException
  {
    CharBuffer localCharBuffer = CharBuffer.allocate(2048);
    int i;
    for (long l = 0L; ; l += i)
    {
      i = paramReadable.read(localCharBuffer);
      if (i == -1)
        return l;
      localCharBuffer.flip();
      paramAppendable.append(localCharBuffer, 0, i);
    }
  }

  public static InputSupplier<Reader> join(Iterable<? extends InputSupplier<? extends Reader>> paramIterable)
  {
    return new InputSupplier()
    {
      public Reader getInput()
        throws IOException
      {
        return new MultiReader(this.val$suppliers.iterator());
      }
    };
  }

  public static InputSupplier<Reader> join(InputSupplier<? extends Reader>[] paramArrayOfInputSupplier)
  {
    return join(Arrays.asList(paramArrayOfInputSupplier));
  }

  public static InputSupplier<InputStreamReader> newReaderSupplier(InputSupplier<? extends InputStream> paramInputSupplier, final Charset paramCharset)
  {
    Preconditions.checkNotNull(paramInputSupplier);
    Preconditions.checkNotNull(paramCharset);
    return new InputSupplier()
    {
      public InputStreamReader getInput()
        throws IOException
      {
        return new InputStreamReader((InputStream)this.val$in.getInput(), paramCharset);
      }
    };
  }

  public static InputSupplier<StringReader> newReaderSupplier(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    return new InputSupplier()
    {
      public StringReader getInput()
      {
        return new StringReader(this.val$value);
      }
    };
  }

  public static OutputSupplier<OutputStreamWriter> newWriterSupplier(OutputSupplier<? extends OutputStream> paramOutputSupplier, final Charset paramCharset)
  {
    Preconditions.checkNotNull(paramOutputSupplier);
    Preconditions.checkNotNull(paramCharset);
    return new OutputSupplier()
    {
      public OutputStreamWriter getOutput()
        throws IOException
      {
        return new OutputStreamWriter((OutputStream)this.val$out.getOutput(), paramCharset);
      }
    };
  }

  public static <R extends Readable,  extends Closeable> String readFirstLine(InputSupplier<R> paramInputSupplier)
    throws IOException
  {
    Readable localReadable = (Readable)paramInputSupplier.getInput();
    try
    {
      String str = new LineReader(localReadable).readLine();
      Closeables.close((Closeable)localReadable, false);
      return str;
    }
    finally
    {
      Closeables.close((Closeable)localReadable, true);
    }
  }

  public static <R extends Readable,  extends Closeable, T> T readLines(InputSupplier<R> paramInputSupplier, LineProcessor<T> paramLineProcessor)
    throws IOException
  {
    Readable localReadable = (Readable)paramInputSupplier.getInput();
    try
    {
      LineReader localLineReader = new LineReader(localReadable);
      boolean bool;
      do
      {
        String str = localLineReader.readLine();
        if (str == null)
          break;
        bool = paramLineProcessor.processLine(str);
      }
      while (bool);
      Closeables.close((Closeable)localReadable, false);
      return paramLineProcessor.getResult();
    }
    finally
    {
      Closeables.close((Closeable)localReadable, true);
    }
  }

  public static <R extends Readable,  extends Closeable> List<String> readLines(InputSupplier<R> paramInputSupplier)
    throws IOException
  {
    Readable localReadable = (Readable)paramInputSupplier.getInput();
    try
    {
      List localList = readLines(localReadable);
      Closeables.close((Closeable)localReadable, false);
      return localList;
    }
    finally
    {
      Closeables.close((Closeable)localReadable, true);
    }
  }

  public static List<String> readLines(Readable paramReadable)
    throws IOException
  {
    ArrayList localArrayList = new ArrayList();
    LineReader localLineReader = new LineReader(paramReadable);
    while (true)
    {
      String str = localLineReader.readLine();
      if (str == null)
        break;
      localArrayList.add(str);
    }
    return localArrayList;
  }

  public static void skipFully(Reader paramReader, long paramLong)
    throws IOException
  {
    while (paramLong > 0L)
    {
      long l = paramReader.skip(paramLong);
      if (l == 0L)
      {
        if (paramReader.read() == -1)
          throw new EOFException();
        paramLong -= 1L;
      }
      else
      {
        paramLong -= l;
      }
    }
  }

  public static <R extends Readable,  extends Closeable> String toString(InputSupplier<R> paramInputSupplier)
    throws IOException
  {
    return toStringBuilder(paramInputSupplier).toString();
  }

  public static String toString(Readable paramReadable)
    throws IOException
  {
    return toStringBuilder(paramReadable).toString();
  }

  private static <R extends Readable,  extends Closeable> StringBuilder toStringBuilder(InputSupplier<R> paramInputSupplier)
    throws IOException
  {
    Readable localReadable = (Readable)paramInputSupplier.getInput();
    try
    {
      StringBuilder localStringBuilder = toStringBuilder(localReadable);
      Closeables.close((Closeable)localReadable, false);
      return localStringBuilder;
    }
    finally
    {
      Closeables.close((Closeable)localReadable, true);
    }
  }

  private static StringBuilder toStringBuilder(Readable paramReadable)
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    copy(paramReadable, localStringBuilder);
    return localStringBuilder;
  }

  public static <W extends Appendable,  extends Closeable> void write(CharSequence paramCharSequence, OutputSupplier<W> paramOutputSupplier)
    throws IOException
  {
    Preconditions.checkNotNull(paramCharSequence);
    Appendable localAppendable = (Appendable)paramOutputSupplier.getOutput();
    try
    {
      localAppendable.append(paramCharSequence);
      Closeables.close((Closeable)localAppendable, false);
      return;
    }
    finally
    {
      Closeables.close((Closeable)localAppendable, true);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.CharStreams
 * JD-Core Version:    0.6.2
 */