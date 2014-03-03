package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;

final class NaturalOrdering extends Ordering<Comparable>
  implements Serializable
{
  static final NaturalOrdering INSTANCE = new NaturalOrdering();
  private static final long serialVersionUID;

  private Object readResolve()
  {
    return INSTANCE;
  }

  public int compare(Comparable paramComparable1, Comparable paramComparable2)
  {
    Preconditions.checkNotNull(paramComparable1);
    Preconditions.checkNotNull(paramComparable2);
    if (paramComparable1 == paramComparable2)
      return 0;
    return paramComparable1.compareTo(paramComparable2);
  }

  public String toString()
  {
    return "Ordering.natural()";
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.NaturalOrdering
 * JD-Core Version:    0.6.2
 */