package com.google.common.base;

public abstract class Equivalence<T>
{
  protected abstract boolean doEquivalent(T paramT1, T paramT2);

  protected abstract int doHash(T paramT);

  public final boolean equivalent(T paramT1, T paramT2)
  {
    if (paramT1 == paramT2)
      return true;
    if ((paramT1 == null) || (paramT2 == null))
      return false;
    return doEquivalent(paramT1, paramT2);
  }

  public final int hash(T paramT)
  {
    if (paramT == null)
      return 0;
    return doHash(paramT);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Equivalence
 * JD-Core Version:    0.6.2
 */