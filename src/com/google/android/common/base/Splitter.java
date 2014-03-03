package com.google.android.common.base;

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

  public static Splitter on(char paramChar)
  {
    return on(CharMatcher.is(paramChar));
  }

  public static Splitter on(CharMatcher paramCharMatcher)
  {
    Preconditions.checkNotNull(paramCharMatcher);
    return new Splitter(new Strategy()
    {
    });
  }

  public Splitter omitEmptyStrings()
  {
    return new Splitter(this.strategy, true, this.trimmer);
  }

  private static abstract interface Strategy
  {
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.Splitter
 * JD-Core Version:    0.6.2
 */