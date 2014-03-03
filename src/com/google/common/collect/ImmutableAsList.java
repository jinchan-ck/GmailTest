package com.google.common.collect;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

final class ImmutableAsList<E> extends RegularImmutableList<E>
{
  private final transient ImmutableCollection<E> collection;

  ImmutableAsList(Object[] paramArrayOfObject, ImmutableCollection<E> paramImmutableCollection)
  {
    super(paramArrayOfObject, 0, paramArrayOfObject.length);
    this.collection = paramImmutableCollection;
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws InvalidObjectException
  {
    throw new InvalidObjectException("Use SerializedForm");
  }

  public boolean contains(Object paramObject)
  {
    return this.collection.contains(paramObject);
  }

  Object writeReplace()
  {
    return new SerializedForm(this.collection);
  }

  static class SerializedForm
    implements Serializable
  {
    private static final long serialVersionUID;
    final ImmutableCollection<?> collection;

    SerializedForm(ImmutableCollection<?> paramImmutableCollection)
    {
      this.collection = paramImmutableCollection;
    }

    Object readResolve()
    {
      return this.collection.asList();
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableAsList
 * JD-Core Version:    0.6.2
 */