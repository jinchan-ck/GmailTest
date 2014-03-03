package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
public final class Predicates
{
  private static final Joiner commaJoiner = Joiner.on(",");

  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> alwaysFalse()
  {
    return AlwaysFalsePredicate.INSTANCE;
  }

  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> alwaysTrue()
  {
    return AlwaysTruePredicate.INSTANCE;
  }

  public static <T> Predicate<T> and(Predicate<? super T> paramPredicate1, Predicate<? super T> paramPredicate2)
  {
    return new AndPredicate(asList((Predicate)Preconditions.checkNotNull(paramPredicate1), (Predicate)Preconditions.checkNotNull(paramPredicate2)), null);
  }

  public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> paramIterable)
  {
    return new AndPredicate(defensiveCopy(paramIterable), null);
  }

  public static <T> Predicate<T> and(Predicate<? super T>[] paramArrayOfPredicate)
  {
    return new AndPredicate(defensiveCopy(paramArrayOfPredicate), null);
  }

  private static <T> List<Predicate<? super T>> asList(Predicate<? super T> paramPredicate1, Predicate<? super T> paramPredicate2)
  {
    return Arrays.asList(new Predicate[] { paramPredicate1, paramPredicate2 });
  }

  public static <A, B> Predicate<A> compose(Predicate<B> paramPredicate, Function<A, ? extends B> paramFunction)
  {
    return new CompositionPredicate(paramPredicate, paramFunction, null);
  }

  static <T> List<T> defensiveCopy(Iterable<T> paramIterable)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
      localArrayList.add(Preconditions.checkNotNull(localIterator.next()));
    return localArrayList;
  }

  private static <T> List<T> defensiveCopy(T[] paramArrayOfT)
  {
    return defensiveCopy(Arrays.asList(paramArrayOfT));
  }

  public static <T> Predicate<T> equalTo(@Nullable T paramT)
  {
    if (paramT == null)
      return isNull();
    return new IsEqualToPredicate(paramT, null);
  }

  public static <T> Predicate<T> in(Collection<? extends T> paramCollection)
  {
    return new InPredicate(paramCollection, null);
  }

  @GwtIncompatible("Class.isInstance")
  public static Predicate<Object> instanceOf(Class<?> paramClass)
  {
    return new InstanceOfPredicate(paramClass, null);
  }

  public static <T> Predicate<T> isNull()
  {
    return IsNullPredicate.INSTANCE;
  }

  private static boolean iterableElementsEqual(Iterable<?> paramIterable1, Iterable<?> paramIterable2)
  {
    Iterator localIterator1 = paramIterable1.iterator();
    Iterator localIterator2 = paramIterable2.iterator();
    while (localIterator1.hasNext())
    {
      if (!localIterator2.hasNext())
        return false;
      if (!localIterator1.next().equals(localIterator2.next()))
        return false;
    }
    return !localIterator2.hasNext();
  }

  public static <T> Predicate<T> not(Predicate<T> paramPredicate)
  {
    return new NotPredicate(paramPredicate, null);
  }

  public static <T> Predicate<T> notNull()
  {
    return NotNullPredicate.INSTANCE;
  }

  public static <T> Predicate<T> or(Predicate<? super T> paramPredicate1, Predicate<? super T> paramPredicate2)
  {
    return new OrPredicate(asList((Predicate)Preconditions.checkNotNull(paramPredicate1), (Predicate)Preconditions.checkNotNull(paramPredicate2)), null);
  }

  public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> paramIterable)
  {
    return new OrPredicate(defensiveCopy(paramIterable), null);
  }

  public static <T> Predicate<T> or(Predicate<? super T>[] paramArrayOfPredicate)
  {
    return new OrPredicate(defensiveCopy(paramArrayOfPredicate), null);
  }

  static enum AlwaysFalsePredicate
    implements Predicate<Object>
  {
    static
    {
      AlwaysFalsePredicate[] arrayOfAlwaysFalsePredicate = new AlwaysFalsePredicate[1];
      arrayOfAlwaysFalsePredicate[0] = INSTANCE;
    }

    public boolean apply(Object paramObject)
    {
      return false;
    }

    public String toString()
    {
      return "AlwaysFalse";
    }
  }

  static enum AlwaysTruePredicate
    implements Predicate<Object>
  {
    static
    {
      AlwaysTruePredicate[] arrayOfAlwaysTruePredicate = new AlwaysTruePredicate[1];
      arrayOfAlwaysTruePredicate[0] = INSTANCE;
    }

    public boolean apply(Object paramObject)
    {
      return true;
    }

    public String toString()
    {
      return "AlwaysTrue";
    }
  }

  private static class AndPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    private final Iterable<? extends Predicate<? super T>> components;

    private AndPredicate(Iterable<? extends Predicate<? super T>> paramIterable)
    {
      this.components = paramIterable;
    }

    public boolean apply(T paramT)
    {
      Iterator localIterator = this.components.iterator();
      while (localIterator.hasNext())
        if (!((Predicate)localIterator.next()).apply(paramT))
          return false;
      return true;
    }

    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof AndPredicate))
      {
        AndPredicate localAndPredicate = (AndPredicate)paramObject;
        return Predicates.iterableElementsEqual(this.components, localAndPredicate.components);
      }
      return false;
    }

    public int hashCode()
    {
      int i = -1;
      Iterator localIterator = this.components.iterator();
      while (localIterator.hasNext())
        i &= ((Predicate)localIterator.next()).hashCode();
      return i;
    }

    public String toString()
    {
      return "And(" + Predicates.commaJoiner.join(this.components) + ")";
    }
  }

  private static class CompositionPredicate<A, B>
    implements Predicate<A>, Serializable
  {
    private static final long serialVersionUID;
    final Function<A, ? extends B> f;
    final Predicate<B> p;

    private CompositionPredicate(Predicate<B> paramPredicate, Function<A, ? extends B> paramFunction)
    {
      this.p = ((Predicate)Preconditions.checkNotNull(paramPredicate));
      this.f = ((Function)Preconditions.checkNotNull(paramFunction));
    }

    public boolean apply(A paramA)
    {
      return this.p.apply(this.f.apply(paramA));
    }

    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof CompositionPredicate))
      {
        CompositionPredicate localCompositionPredicate = (CompositionPredicate)paramObject;
        return (this.f.equals(localCompositionPredicate.f)) && (this.p.equals(localCompositionPredicate.p));
      }
      return false;
    }

    public int hashCode()
    {
      return this.f.hashCode() ^ this.p.hashCode();
    }

    public String toString()
    {
      return this.p.toString() + "(" + this.f.toString() + ")";
    }
  }

  private static class InPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    private final Collection<?> target;

    private InPredicate(Collection<?> paramCollection)
    {
      this.target = ((Collection)Preconditions.checkNotNull(paramCollection));
    }

    public boolean apply(T paramT)
    {
      try
      {
        boolean bool = this.target.contains(paramT);
        return bool;
      }
      catch (NullPointerException localNullPointerException)
      {
        return false;
      }
      catch (ClassCastException localClassCastException)
      {
      }
      return false;
    }

    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof InPredicate))
      {
        InPredicate localInPredicate = (InPredicate)paramObject;
        return this.target.equals(localInPredicate.target);
      }
      return false;
    }

    public int hashCode()
    {
      return this.target.hashCode();
    }

    public String toString()
    {
      return "In(" + this.target + ")";
    }
  }

  private static class InstanceOfPredicate
    implements Predicate<Object>, Serializable
  {
    private static final long serialVersionUID;
    private final Class<?> clazz;

    private InstanceOfPredicate(Class<?> paramClass)
    {
      this.clazz = ((Class)Preconditions.checkNotNull(paramClass));
    }

    public boolean apply(Object paramObject)
    {
      return Platform.isInstance(this.clazz, paramObject);
    }

    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof InstanceOfPredicate))
      {
        InstanceOfPredicate localInstanceOfPredicate = (InstanceOfPredicate)paramObject;
        return this.clazz == localInstanceOfPredicate.clazz;
      }
      return false;
    }

    public int hashCode()
    {
      return this.clazz.hashCode();
    }

    public String toString()
    {
      return "IsInstanceOf(" + this.clazz.getName() + ")";
    }
  }

  private static class IsEqualToPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    private final T target;

    private IsEqualToPredicate(T paramT)
    {
      this.target = paramT;
    }

    public boolean apply(T paramT)
    {
      return this.target.equals(paramT);
    }

    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof IsEqualToPredicate))
      {
        IsEqualToPredicate localIsEqualToPredicate = (IsEqualToPredicate)paramObject;
        return this.target.equals(localIsEqualToPredicate.target);
      }
      return false;
    }

    public int hashCode()
    {
      return this.target.hashCode();
    }

    public String toString()
    {
      return "IsEqualTo(" + this.target + ")";
    }
  }

  private static enum IsNullPredicate
    implements Predicate<Object>
  {
    static
    {
      IsNullPredicate[] arrayOfIsNullPredicate = new IsNullPredicate[1];
      arrayOfIsNullPredicate[0] = INSTANCE;
    }

    public boolean apply(Object paramObject)
    {
      return paramObject == null;
    }

    public String toString()
    {
      return "IsNull";
    }
  }

  private static enum NotNullPredicate
    implements Predicate<Object>
  {
    static
    {
      NotNullPredicate[] arrayOfNotNullPredicate = new NotNullPredicate[1];
      arrayOfNotNullPredicate[0] = INSTANCE;
    }

    public boolean apply(Object paramObject)
    {
      return paramObject != null;
    }

    public String toString()
    {
      return "NotNull";
    }
  }

  private static class NotPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    private final Predicate<T> predicate;

    private NotPredicate(Predicate<T> paramPredicate)
    {
      this.predicate = ((Predicate)Preconditions.checkNotNull(paramPredicate));
    }

    public boolean apply(T paramT)
    {
      return !this.predicate.apply(paramT);
    }

    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof NotPredicate))
      {
        NotPredicate localNotPredicate = (NotPredicate)paramObject;
        return this.predicate.equals(localNotPredicate.predicate);
      }
      return false;
    }

    public int hashCode()
    {
      return 0xFFFFFFFF ^ this.predicate.hashCode();
    }

    public String toString()
    {
      return "Not(" + this.predicate.toString() + ")";
    }
  }

  private static class OrPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    private final Iterable<? extends Predicate<? super T>> components;

    private OrPredicate(Iterable<? extends Predicate<? super T>> paramIterable)
    {
      this.components = paramIterable;
    }

    public boolean apply(T paramT)
    {
      Iterator localIterator = this.components.iterator();
      while (localIterator.hasNext())
        if (((Predicate)localIterator.next()).apply(paramT))
          return true;
      return false;
    }

    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof OrPredicate))
      {
        OrPredicate localOrPredicate = (OrPredicate)paramObject;
        return Predicates.iterableElementsEqual(this.components, localOrPredicate.components);
      }
      return false;
    }

    public int hashCode()
    {
      int i = 0;
      Iterator localIterator = this.components.iterator();
      while (localIterator.hasNext())
        i |= ((Predicate)localIterator.next()).hashCode();
      return i;
    }

    public String toString()
    {
      return "Or(" + Predicates.commaJoiner.join(this.components) + ")";
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Predicates
 * JD-Core Version:    0.6.2
 */