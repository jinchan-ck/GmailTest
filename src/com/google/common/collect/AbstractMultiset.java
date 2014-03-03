package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultiset<E> extends AbstractCollection<E>
  implements Multiset<E>
{
  private transient Set<E> elementSet;

  public int add(E paramE, int paramInt)
  {
    throw new UnsupportedOperationException();
  }

  public boolean add(@Nullable E paramE)
  {
    add(paramE, 1);
    return true;
  }

  public boolean addAll(Collection<? extends E> paramCollection)
  {
    if (paramCollection.isEmpty())
      return false;
    if ((paramCollection instanceof Multiset))
    {
      Iterator localIterator = ((Multiset)paramCollection).entrySet().iterator();
      while (localIterator.hasNext())
      {
        Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
        add(localEntry.getElement(), localEntry.getCount());
      }
    }
    super.addAll(paramCollection);
    return true;
  }

  public void clear()
  {
    entrySet().clear();
  }

  public boolean contains(@Nullable Object paramObject)
  {
    return elementSet().contains(paramObject);
  }

  public boolean containsAll(Collection<?> paramCollection)
  {
    return elementSet().containsAll(paramCollection);
  }

  public int count(Object paramObject)
  {
    Iterator localIterator = entrySet().iterator();
    while (localIterator.hasNext())
    {
      Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
      if (Objects.equal(localEntry.getElement(), paramObject))
        return localEntry.getCount();
    }
    return 0;
  }

  Set<E> createElementSet()
  {
    return new ElementSet(null);
  }

  public Set<E> elementSet()
  {
    Set localSet = this.elementSet;
    if (localSet == null)
    {
      localSet = createElementSet();
      this.elementSet = localSet;
    }
    return localSet;
  }

  public abstract Set<Multiset.Entry<E>> entrySet();

  public boolean equals(@Nullable Object paramObject)
  {
    if (paramObject == this)
      return true;
    if ((paramObject instanceof Multiset))
    {
      Multiset localMultiset = (Multiset)paramObject;
      if (size() != localMultiset.size())
        return false;
      Iterator localIterator = localMultiset.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
        if (count(localEntry.getElement()) != localEntry.getCount())
          return false;
      }
      return true;
    }
    return false;
  }

  public int hashCode()
  {
    return entrySet().hashCode();
  }

  public boolean isEmpty()
  {
    return entrySet().isEmpty();
  }

  public Iterator<E> iterator()
  {
    return new MultisetIterator();
  }

  public int remove(Object paramObject, int paramInt)
  {
    throw new UnsupportedOperationException();
  }

  public boolean remove(Object paramObject)
  {
    return remove(paramObject, 1) > 0;
  }

  public boolean removeAll(Collection<?> paramCollection)
  {
    if ((paramCollection instanceof Multiset));
    for (Object localObject = ((Multiset)paramCollection).elementSet(); ; localObject = paramCollection)
      return elementSet().removeAll((Collection)localObject);
  }

  public boolean retainAll(Collection<?> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    Iterator localIterator = entrySet().iterator();
    boolean bool = false;
    while (localIterator.hasNext())
      if (!paramCollection.contains(((Multiset.Entry)localIterator.next()).getElement()))
      {
        localIterator.remove();
        bool = true;
      }
    return bool;
  }

  public int setCount(E paramE, int paramInt)
  {
    return Multisets.setCountImpl(this, paramE, paramInt);
  }

  public boolean setCount(E paramE, int paramInt1, int paramInt2)
  {
    return Multisets.setCountImpl(this, paramE, paramInt1, paramInt2);
  }

  public int size()
  {
    long l = 0L;
    Iterator localIterator = entrySet().iterator();
    while (localIterator.hasNext())
      l += ((Multiset.Entry)localIterator.next()).getCount();
    return (int)Math.min(l, 2147483647L);
  }

  public String toString()
  {
    return entrySet().toString();
  }

  private class ElementSet extends AbstractSet<E>
  {
    private ElementSet()
    {
    }

    public Iterator<E> iterator()
    {
      return new Iterator()
      {
        public boolean hasNext()
        {
          return this.val$entryIterator.hasNext();
        }

        public E next()
        {
          return ((Multiset.Entry)this.val$entryIterator.next()).getElement();
        }

        public void remove()
        {
          this.val$entryIterator.remove();
        }
      };
    }

    public int size()
    {
      return AbstractMultiset.this.entrySet().size();
    }
  }

  private class MultisetIterator
    implements Iterator<E>
  {
    private boolean canRemove;
    private Multiset.Entry<E> currentEntry;
    private final Iterator<Multiset.Entry<E>> entryIterator = AbstractMultiset.this.entrySet().iterator();
    private int laterCount;
    private int totalCount;

    MultisetIterator()
    {
    }

    public boolean hasNext()
    {
      return (this.laterCount > 0) || (this.entryIterator.hasNext());
    }

    public E next()
    {
      if (!hasNext())
        throw new NoSuchElementException();
      if (this.laterCount == 0)
      {
        this.currentEntry = ((Multiset.Entry)this.entryIterator.next());
        int i = this.currentEntry.getCount();
        this.laterCount = i;
        this.totalCount = i;
      }
      this.laterCount -= 1;
      this.canRemove = true;
      return this.currentEntry.getElement();
    }

    public void remove()
    {
      Preconditions.checkState(this.canRemove, "no calls to next() since the last call to remove()");
      if (this.totalCount == 1)
        this.entryIterator.remove();
      while (true)
      {
        this.totalCount -= 1;
        this.canRemove = false;
        return;
        AbstractMultiset.this.remove(this.currentEntry.getElement());
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractMultiset
 * JD-Core Version:    0.6.2
 */