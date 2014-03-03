package com.google.android.gm.common.base;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Splitter
{
  private final boolean omitEmptyStrings;
  private final Strategy strategy;
  private final CharMatcher trimmer;

  private Splitter(Strategy paramStrategy)
  {
    this(paramStrategy, false, CharMatcher.NONE);
  }

  private Splitter(Strategy paramStrategy, boolean paramBoolean, CharMatcher paramCharMatcher)
  {
    this.strategy = paramStrategy;
    this.omitEmptyStrings = paramBoolean;
    this.trimmer = paramCharMatcher;
  }

  public static Splitter fixedLength(int paramInt)
  {
    if (paramInt > 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "The length may not be less than 1");
      return new Splitter(new Strategy()
      {
        public Splitter.SplittingIterator iterator(Splitter paramAnonymousSplitter, CharSequence paramAnonymousCharSequence)
        {
          return new Splitter.SplittingIterator(paramAnonymousSplitter, paramAnonymousCharSequence)
          {
            public int separatorEnd(int paramAnonymous2Int)
            {
              return paramAnonymous2Int;
            }

            public int separatorStart(int paramAnonymous2Int)
            {
              int i = paramAnonymous2Int + Splitter.4.this.val$length;
              if (i < this.toSplit.length())
                return i;
              return -1;
            }
          };
        }
      });
    }
  }

  public static Splitter on(char paramChar)
  {
    return on(CharMatcher.is(paramChar));
  }

  public static Splitter on(CharMatcher paramCharMatcher)
  {
    Preconditions.checkNotNull(paramCharMatcher);
    return new Splitter(new Strategy()
    {
      public Splitter.SplittingIterator iterator(Splitter paramAnonymousSplitter, CharSequence paramAnonymousCharSequence)
      {
        return new Splitter.SplittingIterator(paramAnonymousSplitter, paramAnonymousCharSequence)
        {
          int separatorEnd(int paramAnonymous2Int)
          {
            return paramAnonymous2Int + 1;
          }

          int separatorStart(int paramAnonymous2Int)
          {
            return Splitter.1.this.val$separatorMatcher.indexIn(this.toSplit, paramAnonymous2Int);
          }
        };
      }
    });
  }

  public static Splitter on(String paramString)
  {
    if (paramString.length() != 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "The separator may not be the empty string.");
      return new Splitter(new Strategy()
      {
        public Splitter.SplittingIterator iterator(Splitter paramAnonymousSplitter, CharSequence paramAnonymousCharSequence)
        {
          return new Splitter.SplittingIterator(paramAnonymousSplitter, paramAnonymousCharSequence)
          {
            public int separatorEnd(int paramAnonymous2Int)
            {
              return paramAnonymous2Int + Splitter.2.this.val$separator.length();
            }

            public int separatorStart(int paramAnonymous2Int)
            {
              int i = Splitter.2.this.val$separator.length();
              int j = paramAnonymous2Int;
              int k = this.toSplit.length() - i;
              if (j <= k)
              {
                for (int m = 0; ; m++)
                {
                  if (m >= i)
                    break label81;
                  if (this.toSplit.charAt(m + j) != Splitter.2.this.val$separator.charAt(m))
                  {
                    j++;
                    break;
                  }
                }
                label81: return j;
              }
              return -1;
            }
          };
        }
      });
    }
  }

  public static Splitter on(Pattern paramPattern)
  {
    Preconditions.checkNotNull(paramPattern);
    if (!paramPattern.matcher("").matches());
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "The pattern may not match the empty string: %s", new Object[] { paramPattern });
      return new Splitter(new Strategy()
      {
        public Splitter.SplittingIterator iterator(Splitter paramAnonymousSplitter, CharSequence paramAnonymousCharSequence)
        {
          return new Splitter.SplittingIterator(paramAnonymousSplitter, paramAnonymousCharSequence)
          {
            public int separatorEnd(int paramAnonymous2Int)
            {
              return this.val$matcher.end();
            }

            public int separatorStart(int paramAnonymous2Int)
            {
              if (this.val$matcher.find(paramAnonymous2Int))
                return this.val$matcher.start();
              return -1;
            }
          };
        }
      });
    }
  }

  public static Splitter onPattern(String paramString)
  {
    return on(Pattern.compile(paramString));
  }

  public Splitter omitEmptyStrings()
  {
    return new Splitter(this.strategy, true, this.trimmer);
  }

  public Iterable<String> split(final CharSequence paramCharSequence)
  {
    Preconditions.checkNotNull(paramCharSequence);
    return new Iterable()
    {
      public Iterator<String> iterator()
      {
        return Splitter.this.strategy.iterator(Splitter.this, paramCharSequence);
      }
    };
  }

  public Splitter trimResults()
  {
    return trimResults(CharMatcher.WHITESPACE);
  }

  public Splitter trimResults(CharMatcher paramCharMatcher)
  {
    Preconditions.checkNotNull(paramCharMatcher);
    return new Splitter(this.strategy, this.omitEmptyStrings, paramCharMatcher);
  }

  private static abstract class AbstractIterator<T>
    implements Iterator<T>
  {
    T next;
    State state = State.NOT_READY;

    protected abstract T computeNext();

    protected final T endOfData()
    {
      this.state = State.DONE;
      return null;
    }

    public final boolean hasNext()
    {
      if (this.state != State.FAILED);
      for (boolean bool = true; ; bool = false)
      {
        Preconditions.checkState(bool);
        switch (Splitter.6.$SwitchMap$com$google$android$gm$common$base$Splitter$AbstractIterator$State[this.state.ordinal()])
        {
        default:
          return tryToComputeNext();
        case 1:
        case 2:
        }
      }
      return false;
      return true;
    }

    public final T next()
    {
      if (!hasNext())
        throw new NoSuchElementException();
      this.state = State.NOT_READY;
      return this.next;
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }

    boolean tryToComputeNext()
    {
      this.state = State.FAILED;
      this.next = computeNext();
      if (this.state != State.DONE)
      {
        this.state = State.READY;
        return true;
      }
      return false;
    }

    static enum State
    {
      static
      {
        NOT_READY = new State("NOT_READY", 1);
        DONE = new State("DONE", 2);
        FAILED = new State("FAILED", 3);
        State[] arrayOfState = new State[4];
        arrayOfState[0] = READY;
        arrayOfState[1] = NOT_READY;
        arrayOfState[2] = DONE;
        arrayOfState[3] = FAILED;
      }
    }
  }

  private static abstract class SplittingIterator extends Splitter.AbstractIterator<String>
  {
    int offset = 0;
    final boolean omitEmptyStrings;
    final CharSequence toSplit;
    final CharMatcher trimmer;

    protected SplittingIterator(Splitter paramSplitter, CharSequence paramCharSequence)
    {
      super();
      this.trimmer = paramSplitter.trimmer;
      this.omitEmptyStrings = paramSplitter.omitEmptyStrings;
      this.toSplit = paramCharSequence;
    }

    protected String computeNext()
    {
      while (this.offset != -1)
      {
        int i = this.offset;
        int j = separatorStart(this.offset);
        int k;
        if (j == -1)
        {
          k = this.toSplit.length();
          this.offset = -1;
        }
        while ((i < k) && (this.trimmer.matches(this.toSplit.charAt(i))))
        {
          i++;
          continue;
          k = j;
          this.offset = separatorEnd(j);
        }
        while ((k > i) && (this.trimmer.matches(this.toSplit.charAt(k - 1))))
          k--;
        if ((!this.omitEmptyStrings) || (i != k))
          return this.toSplit.subSequence(i, k).toString();
      }
      return (String)endOfData();
    }

    abstract int separatorEnd(int paramInt);

    abstract int separatorStart(int paramInt);
  }

  private static abstract interface Strategy
  {
    public abstract Iterator<String> iterator(Splitter paramSplitter, CharSequence paramCharSequence);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.base.Splitter
 * JD-Core Version:    0.6.2
 */