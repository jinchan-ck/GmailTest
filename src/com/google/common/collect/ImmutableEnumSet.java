package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

@GwtCompatible(serializable=true)
final class ImmutableEnumSet<E> extends ImmutableSet<E>
{
  private final transient Set<E> delegate;
  private transient int hashCode;

  ImmutableEnumSet(Set<E> paramSet)
  {
    this.delegate = paramSet;
  }

  public boolean contains(Object paramObject)
  {
    return this.delegate.contains(paramObject);
  }

  public boolean containsAll(Collection<?> paramCollection)
  {
    return this.delegate.containsAll(paramCollection);
  }

  public boolean equals(Object paramObject)
  {
    return (paramObject == this) || (this.delegate.equals(paramObject));
  }

  public int hashCode()
  {
    int i = this.hashCode;
    if (i == 0)
    {
      int j = this.delegate.hashCode();
      this.hashCode = j;
      return j;
    }
    return i;
  }

  public boolean isEmpty()
  {
    return this.delegate.isEmpty();
  }

  public UnmodifiableIterator<E> iterator()
  {
    return Iterators.unmodifiableIterator(this.delegate.iterator());
  }

  public int size()
  {
    return this.delegate.size();
  }

  public Object[] toArray()
  {
    return this.delegate.toArray();
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    return this.delegate.toArray(paramArrayOfT);
  }

  public String toString()
  {
    return this.delegate.toString();
  }

  Object writeReplace()
  {
    return new EnumSerializedForm((EnumSet)this.delegate);
  }

  private static class EnumSerializedForm<E extends Enum<E>>
    implements Serializable
  {
    private static final long serialVersionUID;
    final EnumSet<E> delegate;

    EnumSerializedForm(EnumSet<E> paramEnumSet)
    {
      this.delegate = paramEnumSet;
    }

    Object readResolve()
    {
      return new ImmutableEnumSet(this.delegate.clone());
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableEnumSet
 * JD-Core Version:    0.6.2
 */