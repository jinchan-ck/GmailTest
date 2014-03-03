package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true)
public class ImmutableSetMultimap<K, V> extends ImmutableMultimap<K, V>
  implements SetMultimap<K, V>
{
  private static final long serialVersionUID;
  private transient ImmutableSet<Map.Entry<K, V>> entries;

  ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> paramImmutableMap, int paramInt)
  {
    super(paramImmutableMap, paramInt);
  }

  public static <K, V> Builder<K, V> builder()
  {
    return new Builder();
  }

  public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> paramMultimap)
  {
    if (paramMultimap.isEmpty())
      return of();
    if ((paramMultimap instanceof ImmutableSetMultimap))
      return (ImmutableSetMultimap)paramMultimap;
    ImmutableMap.Builder localBuilder = ImmutableMap.builder();
    int i = 0;
    Iterator localIterator = paramMultimap.asMap().entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject = localEntry.getKey();
      ImmutableSet localImmutableSet = ImmutableSet.copyOf((Collection)localEntry.getValue());
      if (!localImmutableSet.isEmpty())
      {
        localBuilder.put(localObject, localImmutableSet);
        i += localImmutableSet.size();
      }
    }
    return new ImmutableSetMultimap(localBuilder.build(), i);
  }

  public static <K, V> ImmutableSetMultimap<K, V> of()
  {
    return EmptyImmutableSetMultimap.INSTANCE;
  }

  public static <K, V> ImmutableSetMultimap<K, V> of(K paramK, V paramV)
  {
    Builder localBuilder = builder();
    localBuilder.put(paramK, paramV);
    return localBuilder.build();
  }

  public static <K, V> ImmutableSetMultimap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2)
  {
    Builder localBuilder = builder();
    localBuilder.put(paramK1, paramV1);
    localBuilder.put(paramK2, paramV2);
    return localBuilder.build();
  }

  public static <K, V> ImmutableSetMultimap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2, K paramK3, V paramV3)
  {
    Builder localBuilder = builder();
    localBuilder.put(paramK1, paramV1);
    localBuilder.put(paramK2, paramV2);
    localBuilder.put(paramK3, paramV3);
    return localBuilder.build();
  }

  public static <K, V> ImmutableSetMultimap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2, K paramK3, V paramV3, K paramK4, V paramV4)
  {
    Builder localBuilder = builder();
    localBuilder.put(paramK1, paramV1);
    localBuilder.put(paramK2, paramV2);
    localBuilder.put(paramK3, paramV3);
    localBuilder.put(paramK4, paramV4);
    return localBuilder.build();
  }

  public static <K, V> ImmutableSetMultimap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2, K paramK3, V paramV3, K paramK4, V paramV4, K paramK5, V paramV5)
  {
    Builder localBuilder = builder();
    localBuilder.put(paramK1, paramV1);
    localBuilder.put(paramK2, paramV2);
    localBuilder.put(paramK3, paramV3);
    localBuilder.put(paramK4, paramV4);
    localBuilder.put(paramK5, paramV5);
    return localBuilder.build();
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    int i = paramObjectInputStream.readInt();
    if (i < 0)
      throw new InvalidObjectException("Invalid key count " + i);
    ImmutableMap.Builder localBuilder = ImmutableMap.builder();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      Object localObject = paramObjectInputStream.readObject();
      int m = paramObjectInputStream.readInt();
      if (m <= 0)
        throw new InvalidObjectException("Invalid value count " + m);
      Object[] arrayOfObject = new Object[m];
      for (int n = 0; n < m; n++)
        arrayOfObject[n] = paramObjectInputStream.readObject();
      ImmutableSet localImmutableSet = ImmutableSet.of(arrayOfObject);
      if (localImmutableSet.size() != arrayOfObject.length)
        throw new InvalidObjectException("Duplicate key-value pairs exist for key " + localObject);
      localBuilder.put(localObject, localImmutableSet);
      j += m;
    }
    try
    {
      ImmutableMap localImmutableMap = localBuilder.build();
      ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, localImmutableMap);
      ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, j);
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw ((InvalidObjectException)new InvalidObjectException(localIllegalArgumentException.getMessage()).initCause(localIllegalArgumentException));
    }
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    Serialization.writeMultimap(this, paramObjectOutputStream);
  }

  public ImmutableSet<Map.Entry<K, V>> entries()
  {
    ImmutableSet localImmutableSet1 = this.entries;
    if (localImmutableSet1 == null)
    {
      ImmutableSet localImmutableSet2 = ImmutableSet.copyOf(super.entries());
      this.entries = localImmutableSet2;
      return localImmutableSet2;
    }
    return localImmutableSet1;
  }

  public ImmutableSet<V> get(@Nullable K paramK)
  {
    ImmutableSet localImmutableSet = (ImmutableSet)this.map.get(paramK);
    if (localImmutableSet == null)
      return ImmutableSet.of();
    return localImmutableSet;
  }

  public ImmutableSet<V> removeAll(Object paramObject)
  {
    throw new UnsupportedOperationException();
  }

  public ImmutableSet<V> replaceValues(K paramK, Iterable<? extends V> paramIterable)
  {
    throw new UnsupportedOperationException();
  }

  public static final class Builder<K, V> extends ImmutableMultimap.Builder<K, V>
  {
    private final Multimap<K, V> builderMultimap = new ImmutableSetMultimap.BuilderMultimap();

    public ImmutableSetMultimap<K, V> build()
    {
      return ImmutableSetMultimap.copyOf(this.builderMultimap);
    }

    public Builder<K, V> put(K paramK, V paramV)
    {
      this.builderMultimap.put(Preconditions.checkNotNull(paramK), Preconditions.checkNotNull(paramV));
      return this;
    }

    public Builder<K, V> putAll(Multimap<? extends K, ? extends V> paramMultimap)
    {
      Iterator localIterator = paramMultimap.asMap().entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        putAll(localEntry.getKey(), (Iterable)localEntry.getValue());
      }
      return this;
    }

    public Builder<K, V> putAll(K paramK, Iterable<? extends V> paramIterable)
    {
      Collection localCollection = this.builderMultimap.get(Preconditions.checkNotNull(paramK));
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext())
        localCollection.add(Preconditions.checkNotNull(localIterator.next()));
      return this;
    }

    public Builder<K, V> putAll(K paramK, V[] paramArrayOfV)
    {
      return putAll(paramK, Arrays.asList(paramArrayOfV));
    }
  }

  private static class BuilderMultimap<K, V> extends AbstractMultimap<K, V>
  {
    private static final long serialVersionUID;

    BuilderMultimap()
    {
      super();
    }

    Collection<V> createCollection()
    {
      return Sets.newLinkedHashSet();
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableSetMultimap
 * JD-Core Version:    0.6.2
 */