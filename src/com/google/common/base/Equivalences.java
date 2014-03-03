package com.google.common.base;

import java.io.Serializable;

public final class Equivalences
{
  public static Equivalence<Object> equals()
  {
    return Equals.INSTANCE;
  }

  public static Equivalence<Object> identity()
  {
    return Identity.INSTANCE;
  }

  private static final class Equals extends Equivalence<Object>
    implements Serializable
  {
    static final Equals INSTANCE = new Equals();
    private static final long serialVersionUID = 1L;

    private Object readResolve()
    {
      return INSTANCE;
    }

    protected boolean doEquivalent(Object paramObject1, Object paramObject2)
    {
      return paramObject1.equals(paramObject2);
    }

    public int doHash(Object paramObject)
    {
      return paramObject.hashCode();
    }
  }

  private static final class Identity extends Equivalence<Object>
    implements Serializable
  {
    static final Identity INSTANCE = new Identity();
    private static final long serialVersionUID = 1L;

    private Object readResolve()
    {
      return INSTANCE;
    }

    protected boolean doEquivalent(Object paramObject1, Object paramObject2)
    {
      return false;
    }

    protected int doHash(Object paramObject)
    {
      return System.identityHashCode(paramObject);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Equivalences
 * JD-Core Version:    0.6.2
 */