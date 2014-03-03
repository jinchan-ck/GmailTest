package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators
{
  static final UnmodifiableIterator<Object> EMPTY_ITERATOR = new UnmodifiableIterator()
  {
    public boolean hasNext()
    {
      return false;
    }

    public Object next()
    {
      throw new NoSuchElementException();
    }
  };
  private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator()
  {
    public boolean hasNext()
    {
      return false;
    }

    public Object next()
    {
      throw new NoSuchElementException();
    }

    public void remove()
    {
      throw new IllegalStateException();
    }
  };

  public static <T> boolean addAll(Collection<T> paramCollection, Iterator<? extends T> paramIterator)
  {
    Preconditions.checkNotNull(paramCollection);
    boolean bool = false;
    while (paramIterator.hasNext())
      bool |= paramCollection.add(paramIterator.next());
    return bool;
  }

  public static boolean contains(Iterator<?> paramIterator, Object paramObject)
  {
    if (paramObject == null)
    {
      do
        if (!paramIterator.hasNext())
          break;
      while (paramIterator.next() != null);
      return true;
    }
    while (paramIterator.hasNext())
      if (paramObject.equals(paramIterator.next()))
        return true;
    return false;
  }

  public static boolean elementsEqual(Iterator<?> paramIterator1, Iterator<?> paramIterator2)
  {
    if (paramIterator1.hasNext())
      if (paramIterator2.hasNext());
    while (paramIterator2.hasNext())
    {
      return false;
      if (Objects.equal(paramIterator1.next(), paramIterator2.next()))
        break;
      return false;
    }
    return true;
  }

  public static <T> UnmodifiableIterator<T> emptyIterator()
  {
    return EMPTY_ITERATOR;
  }

  public static <T> UnmodifiableIterator<T> forArray(final T[] paramArrayOfT)
  {
    Preconditions.checkNotNull(paramArrayOfT);
    return new AbstractIndexedListIterator(paramArrayOfT.length)
    {
      protected T get(int paramAnonymousInt)
      {
        return paramArrayOfT[paramAnonymousInt];
      }
    };
  }

  static <T> UnmodifiableIterator<T> forArray(final T[] paramArrayOfT, final int paramInt1, int paramInt2)
  {
    if (paramInt2 >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      Preconditions.checkPositionIndexes(paramInt1, paramInt1 + paramInt2, paramArrayOfT.length);
      return new AbstractIndexedListIterator(paramInt2)
      {
        protected T get(int paramAnonymousInt)
        {
          return paramArrayOfT[(paramAnonymousInt + paramInt1)];
        }
      };
    }
  }

  public static <T> T getOnlyElement(Iterator<T> paramIterator)
  {
    Object localObject = paramIterator.next();
    if (!paramIterator.hasNext())
      return localObject;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("expected one element but was: <" + localObject);
    for (int i = 0; (i < 4) && (paramIterator.hasNext()); i++)
      localStringBuilder.append(", " + paramIterator.next());
    if (paramIterator.hasNext())
      localStringBuilder.append(", ...");
    localStringBuilder.append('>');
    throw new IllegalArgumentException(localStringBuilder.toString());
  }

  public static boolean removeAll(Iterator<?> paramIterator, Collection<?> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    boolean bool = false;
    while (paramIterator.hasNext())
      if (paramCollection.contains(paramIterator.next()))
      {
        paramIterator.remove();
        bool = true;
      }
    return bool;
  }

  public static boolean retainAll(Iterator<?> paramIterator, Collection<?> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    boolean bool = false;
    while (paramIterator.hasNext())
      if (!paramCollection.contains(paramIterator.next()))
      {
        paramIterator.remove();
        bool = true;
      }
    return bool;
  }

  public static <T> UnmodifiableIterator<T> singletonIterator(T paramT)
  {
    return new UnmodifiableIterator()
    {
      boolean done;

      public boolean hasNext()
      {
        return !this.done;
      }

      public T next()
      {
        if (this.done)
          throw new NoSuchElementException();
        this.done = true;
        return this.val$value;
      }
    };
  }

  public static String toString(Iterator<?> paramIterator)
  {
    if (!paramIterator.hasNext())
      return "[]";
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[').append(paramIterator.next());
    while (paramIterator.hasNext())
      localStringBuilder.append(", ").append(paramIterator.next());
    return ']';
  }

  public static <F, T> Iterator<T> transform(Iterator<F> paramIterator, final Function<? super F, ? extends T> paramFunction)
  {
    Preconditions.checkNotNull(paramIterator);
    Preconditions.checkNotNull(paramFunction);
    return new Iterator()
    {
      public boolean hasNext()
      {
        return this.val$fromIterator.hasNext();
      }

      public T next()
      {
        Object localObject = this.val$fromIterator.next();
        return paramFunction.apply(localObject);
      }

      public void remove()
      {
        this.val$fromIterator.remove();
      }
    };
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Iterators
 * JD-Core Version:    0.6.2
 */