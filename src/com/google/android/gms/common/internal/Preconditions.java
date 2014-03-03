package com.google.android.gms.common.internal;

public final class Preconditions
{
  private Preconditions()
  {
    throw new AssertionError("Uninstantiable");
  }

  public static <T> T checkNotNull(T paramT)
  {
    if (paramT == null)
      throw new NullPointerException("null reference");
    return paramT;
  }

  public static void checkState(boolean paramBoolean)
  {
    if (!paramBoolean)
      throw new IllegalStateException();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gms.common.internal.Preconditions
 * JD-Core Version:    0.6.2
 */