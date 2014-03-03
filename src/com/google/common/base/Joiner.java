package com.google.common.base;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Joiner
{
  private final String separator;

  private Joiner(String paramString)
  {
    this.separator = ((String)Preconditions.checkNotNull(paramString));
  }

  public static Joiner on(String paramString)
  {
    return new Joiner(paramString);
  }

  public <A extends Appendable> A appendTo(A paramA, Iterator<?> paramIterator)
    throws IOException
  {
    Preconditions.checkNotNull(paramA);
    if (paramIterator.hasNext())
    {
      paramA.append(toString(paramIterator.next()));
      while (paramIterator.hasNext())
      {
        paramA.append(this.separator);
        paramA.append(toString(paramIterator.next()));
      }
    }
    return paramA;
  }

  public final StringBuilder appendTo(StringBuilder paramStringBuilder, Iterable<?> paramIterable)
  {
    return appendTo(paramStringBuilder, paramIterable.iterator());
  }

  public final StringBuilder appendTo(StringBuilder paramStringBuilder, Iterator<?> paramIterator)
  {
    try
    {
      appendTo(paramStringBuilder, paramIterator);
      return paramStringBuilder;
    }
    catch (IOException localIOException)
    {
      throw new AssertionError(localIOException);
    }
  }

  public final StringBuilder appendTo(StringBuilder paramStringBuilder, Object[] paramArrayOfObject)
  {
    return appendTo(paramStringBuilder, Arrays.asList(paramArrayOfObject));
  }

  CharSequence toString(Object paramObject)
  {
    Preconditions.checkNotNull(paramObject);
    if ((paramObject instanceof CharSequence))
      return (CharSequence)paramObject;
    return paramObject.toString();
  }

  public MapJoiner withKeyValueSeparator(String paramString)
  {
    return new MapJoiner(this, paramString, null);
  }

  public static final class MapJoiner
  {
    private final Joiner joiner;
    private final String keyValueSeparator;

    private MapJoiner(Joiner paramJoiner, String paramString)
    {
      this.joiner = paramJoiner;
      this.keyValueSeparator = ((String)Preconditions.checkNotNull(paramString));
    }

    public <A extends Appendable> A appendTo(A paramA, Iterator<? extends Map.Entry<?, ?>> paramIterator)
      throws IOException
    {
      Preconditions.checkNotNull(paramA);
      if (paramIterator.hasNext())
      {
        Map.Entry localEntry1 = (Map.Entry)paramIterator.next();
        paramA.append(this.joiner.toString(localEntry1.getKey()));
        paramA.append(this.keyValueSeparator);
        paramA.append(this.joiner.toString(localEntry1.getValue()));
        while (paramIterator.hasNext())
        {
          paramA.append(this.joiner.separator);
          Map.Entry localEntry2 = (Map.Entry)paramIterator.next();
          paramA.append(this.joiner.toString(localEntry2.getKey()));
          paramA.append(this.keyValueSeparator);
          paramA.append(this.joiner.toString(localEntry2.getValue()));
        }
      }
      return paramA;
    }

    public StringBuilder appendTo(StringBuilder paramStringBuilder, Iterable<? extends Map.Entry<?, ?>> paramIterable)
    {
      return appendTo(paramStringBuilder, paramIterable.iterator());
    }

    public StringBuilder appendTo(StringBuilder paramStringBuilder, Iterator<? extends Map.Entry<?, ?>> paramIterator)
    {
      try
      {
        appendTo(paramStringBuilder, paramIterator);
        return paramStringBuilder;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public StringBuilder appendTo(StringBuilder paramStringBuilder, Map<?, ?> paramMap)
    {
      return appendTo(paramStringBuilder, paramMap.entrySet());
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Joiner
 * JD-Core Version:    0.6.2
 */