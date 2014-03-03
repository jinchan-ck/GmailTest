package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable=true)
class EmptyImmutableSetMultimap extends ImmutableSetMultimap<Object, Object>
{
  static final EmptyImmutableSetMultimap INSTANCE = new EmptyImmutableSetMultimap();
  private static final long serialVersionUID;

  private EmptyImmutableSetMultimap()
  {
    super(ImmutableMap.of(), 0);
  }

  private Object readResolve()
  {
    return INSTANCE;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.EmptyImmutableSetMultimap
 * JD-Core Version:    0.6.2
 */