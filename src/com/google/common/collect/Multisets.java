package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public final class Multisets
{
  static void checkNonnegative(int paramInt, String paramString)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramString;
      arrayOfObject[1] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool, "%s cannot be negative: %s", arrayOfObject);
      return;
    }
  }

  static <E> Multiset<E> forSet(Set<E> paramSet)
  {
    return new SetMultiset(paramSet);
  }

  public static <E> Multiset.Entry<E> immutableEntry(@Nullable E paramE, final int paramInt)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return new AbstractEntry()
      {
        public int getCount()
        {
          return paramInt;
        }

        public E getElement()
        {
          return this.val$e;
        }
      };
    }
  }

  static int inferDistinctElements(Iterable<?> paramIterable)
  {
    if ((paramIterable instanceof Multiset))
      return ((Multiset)paramIterable).elementSet().size();
    return 11;
  }

  public static <E> Multiset<E> intersection(Multiset<E> paramMultiset, final Multiset<?> paramMultiset1)
  {
    Preconditions.checkNotNull(paramMultiset);
    Preconditions.checkNotNull(paramMultiset1);
    return new AbstractMultiset()
    {
      final Set<Multiset.Entry<E>> entrySet = new AbstractSet()
      {
        public boolean contains(Object paramAnonymous2Object)
        {
          if ((paramAnonymous2Object instanceof Multiset.Entry))
          {
            Multiset.Entry localEntry = (Multiset.Entry)paramAnonymous2Object;
            int i = localEntry.getCount();
            return (i > 0) && (Multisets.2.this.count(localEntry.getElement()) == i);
          }
          return false;
        }

        public boolean isEmpty()
        {
          return Multisets.2.this.elementSet().isEmpty();
        }

        public Iterator<Multiset.Entry<E>> iterator()
        {
          return new AbstractIterator()
          {
            protected Multiset.Entry<E> computeNext()
            {
              while (this.val$iterator1.hasNext())
              {
                Multiset.Entry localEntry = (Multiset.Entry)this.val$iterator1.next();
                Object localObject = localEntry.getElement();
                int i = Math.min(localEntry.getCount(), Multisets.2.this.val$multiset2.count(localObject));
                if (i > 0)
                  return Multisets.immutableEntry(localObject, i);
              }
              return (Multiset.Entry)endOfData();
            }
          };
        }

        public int size()
        {
          return Multisets.2.this.elementSet().size();
        }
      };

      public int count(Object paramAnonymousObject)
      {
        int i = this.val$multiset1.count(paramAnonymousObject);
        if (i == 0)
          return 0;
        return Math.min(i, paramMultiset1.count(paramAnonymousObject));
      }

      Set<E> createElementSet()
      {
        return Sets.intersection(this.val$multiset1.elementSet(), paramMultiset1.elementSet());
      }

      public Set<Multiset.Entry<E>> entrySet()
      {
        return this.entrySet;
      }
    };
  }

  static <E> int setCountImpl(Multiset<E> paramMultiset, E paramE, int paramInt)
  {
    checkNonnegative(paramInt, "count");
    int i = paramMultiset.count(paramE);
    int j = paramInt - i;
    if (j > 0)
      paramMultiset.add(paramE, j);
    while (j >= 0)
      return i;
    paramMultiset.remove(paramE, -j);
    return i;
  }

  static <E> boolean setCountImpl(Multiset<E> paramMultiset, E paramE, int paramInt1, int paramInt2)
  {
    checkNonnegative(paramInt1, "oldCount");
    checkNonnegative(paramInt2, "newCount");
    if (paramMultiset.count(paramE) == paramInt1)
    {
      paramMultiset.setCount(paramE, paramInt2);
      return true;
    }
    return false;
  }

  public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> paramMultiset)
  {
    return new UnmodifiableMultiset(paramMultiset);
  }

  static abstract class AbstractEntry<E>
    implements Multiset.Entry<E>
  {
    public boolean equals(@Nullable Object paramObject)
    {
      if ((paramObject instanceof Multiset.Entry))
      {
        Multiset.Entry localEntry = (Multiset.Entry)paramObject;
        return (getCount() == localEntry.getCount()) && (Objects.equal(getElement(), localEntry.getElement()));
      }
      return false;
    }

    public int hashCode()
    {
      Object localObject = getElement();
      if (localObject == null);
      for (int i = 0; ; i = localObject.hashCode())
        return i ^ getCount();
    }

    public String toString()
    {
      String str = String.valueOf(getElement());
      int i = getCount();
      if (i == 1)
        return str;
      return str + " x " + i;
    }
  }

  private static class SetMultiset<E> extends ForwardingCollection<E>
    implements Multiset<E>, Serializable
  {
    private static final long serialVersionUID;
    final Set<E> delegate;
    transient Set<E> elementSet;
    transient Set<Multiset.Entry<E>> entrySet;

    SetMultiset(Set<E> paramSet)
    {
      this.delegate = ((Set)Preconditions.checkNotNull(paramSet));
    }

    public int add(E paramE, int paramInt)
    {
      throw new UnsupportedOperationException();
    }

    public boolean add(E paramE)
    {
      throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public int count(Object paramObject)
    {
      if (this.delegate.contains(paramObject))
        return 1;
      return 0;
    }

    protected Set<E> delegate()
    {
      return this.delegate;
    }

    public Set<E> elementSet()
    {
      Set localSet = this.elementSet;
      if (localSet == null)
      {
        ElementSet localElementSet = new ElementSet();
        this.elementSet = localElementSet;
        return localElementSet;
      }
      return localSet;
    }

    public Set<Multiset.Entry<E>> entrySet()
    {
      Set localSet = this.entrySet;
      if (localSet == null)
      {
        EntrySet localEntrySet = new EntrySet();
        this.entrySet = localEntrySet;
        return localEntrySet;
      }
      return localSet;
    }

    public boolean equals(@Nullable Object paramObject)
    {
      if ((paramObject instanceof Multiset))
      {
        Multiset localMultiset = (Multiset)paramObject;
        return (size() == localMultiset.size()) && (this.delegate.equals(localMultiset.elementSet()));
      }
      return false;
    }

    public int hashCode()
    {
      int i = 0;
      Iterator localIterator = iterator();
      if (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        if (localObject == null);
        for (int j = 0; ; j = localObject.hashCode())
        {
          i += (j ^ 0x1);
          break;
        }
      }
      return i;
    }

    public int remove(Object paramObject, int paramInt)
    {
      if (paramInt == 0)
        return count(paramObject);
      if (paramInt > 0);
      for (boolean bool = true; ; bool = false)
      {
        Preconditions.checkArgument(bool);
        if (!this.delegate.remove(paramObject))
          break;
        return 1;
      }
      return 0;
    }

    public int setCount(E paramE, int paramInt)
    {
      Multisets.checkNonnegative(paramInt, "count");
      if (paramInt == count(paramE))
        return paramInt;
      if (paramInt == 0)
      {
        remove(paramE);
        return 1;
      }
      throw new UnsupportedOperationException();
    }

    public boolean setCount(E paramE, int paramInt1, int paramInt2)
    {
      return Multisets.setCountImpl(this, paramE, paramInt1, paramInt2);
    }

    class ElementSet extends ForwardingSet<E>
    {
      ElementSet()
      {
      }

      public boolean add(E paramE)
      {
        throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection<? extends E> paramCollection)
      {
        throw new UnsupportedOperationException();
      }

      protected Set<E> delegate()
      {
        return Multisets.SetMultiset.this.delegate;
      }
    }

    class EntrySet extends AbstractSet<Multiset.Entry<E>>
    {
      EntrySet()
      {
      }

      public Iterator<Multiset.Entry<E>> iterator()
      {
        return new Iterator()
        {
          final Iterator<E> elements = Multisets.SetMultiset.this.delegate.iterator();

          public boolean hasNext()
          {
            return this.elements.hasNext();
          }

          public Multiset.Entry<E> next()
          {
            return Multisets.immutableEntry(this.elements.next(), 1);
          }

          public void remove()
          {
            this.elements.remove();
          }
        };
      }

      public int size()
      {
        return Multisets.SetMultiset.this.delegate.size();
      }
    }
  }

  private static class UnmodifiableMultiset<E> extends ForwardingMultiset<E>
    implements Serializable
  {
    private static final long serialVersionUID;
    final Multiset<? extends E> delegate;
    transient Set<E> elementSet;
    transient Set<Multiset.Entry<E>> entrySet;

    UnmodifiableMultiset(Multiset<? extends E> paramMultiset)
    {
      this.delegate = paramMultiset;
    }

    public int add(E paramE, int paramInt)
    {
      throw new UnsupportedOperationException();
    }

    public boolean add(E paramE)
    {
      throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public void clear()
    {
      throw new UnsupportedOperationException();
    }

    protected Multiset<E> delegate()
    {
      return this.delegate;
    }

    public Set<E> elementSet()
    {
      Set localSet1 = this.elementSet;
      if (localSet1 == null)
      {
        Set localSet2 = Collections.unmodifiableSet(this.delegate.elementSet());
        this.elementSet = localSet2;
        return localSet2;
      }
      return localSet1;
    }

    public Set<Multiset.Entry<E>> entrySet()
    {
      Set localSet1 = this.entrySet;
      if (localSet1 == null)
      {
        Set localSet2 = Collections.unmodifiableSet(this.delegate.entrySet());
        this.entrySet = localSet2;
        return localSet2;
      }
      return localSet1;
    }

    public Iterator<E> iterator()
    {
      return Iterators.unmodifiableIterator(this.delegate.iterator());
    }

    public int remove(Object paramObject, int paramInt)
    {
      throw new UnsupportedOperationException();
    }

    public boolean remove(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public int setCount(E paramE, int paramInt)
    {
      throw new UnsupportedOperationException();
    }

    public boolean setCount(E paramE, int paramInt1, int paramInt2)
    {
      throw new UnsupportedOperationException();
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Multisets
 * JD-Core Version:    0.6.2
 */