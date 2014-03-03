package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.Iterator;

@GwtCompatible
public final class EnumMultiset<E extends Enum<E>> extends AbstractMapBasedMultiset<E>
{
  private static final long serialVersionUID;
  private transient Class<E> type;

  private EnumMultiset(Class<E> paramClass)
  {
    super(new EnumMap(paramClass));
    this.type = paramClass;
  }

  public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> paramClass)
  {
    return new EnumMultiset(paramClass);
  }

  public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> paramIterable)
  {
    Iterator localIterator = paramIterable.iterator();
    Preconditions.checkArgument(localIterator.hasNext(), "EnumMultiset constructor passed empty Iterable");
    EnumMultiset localEnumMultiset = new EnumMultiset(((Enum)localIterator.next()).getDeclaringClass());
    Iterables.addAll(localEnumMultiset, paramIterable);
    return localEnumMultiset;
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    this.type = ((Class)paramObjectInputStream.readObject());
    setBackingMap(new EnumMap(this.type));
    Serialization.populateMultiset(this, paramObjectInputStream);
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeObject(this.type);
    Serialization.writeMultiset(this, paramObjectOutputStream);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.EnumMultiset
 * JD-Core Version:    0.6.2
 */