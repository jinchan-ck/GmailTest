package com.google.common.collect;

public abstract class ForwardingObject
{
  protected abstract Object delegate();

  public String toString()
  {
    return delegate().toString();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingObject
 * JD-Core Version:    0.6.2
 */