package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>> extends AbstractBiMap<K, V>
{
  private static final long serialVersionUID;
  private transient Class<K> keyType;
  private transient Class<V> valueType;

  private EnumBiMap(Class<K> paramClass, Class<V> paramClass1)
  {
    super(new EnumMap(paramClass), new EnumMap(paramClass1));
    this.keyType = paramClass;
    this.valueType = paramClass1;
  }

  public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> paramClass, Class<V> paramClass1)
  {
    return new EnumBiMap(paramClass, paramClass1);
  }

  public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> paramMap)
  {
    EnumBiMap localEnumBiMap = create(inferKeyType(paramMap), inferValueType(paramMap));
    localEnumBiMap.putAll(paramMap);
    return localEnumBiMap;
  }

  static <K extends Enum<K>> Class<K> inferKeyType(Map<K, ?> paramMap)
  {
    if ((paramMap instanceof EnumBiMap))
      return ((EnumBiMap)paramMap).keyType();
    if ((paramMap instanceof EnumHashBiMap))
      return ((EnumHashBiMap)paramMap).keyType();
    if (!paramMap.isEmpty());
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return ((Enum)paramMap.keySet().iterator().next()).getDeclaringClass();
    }
  }

  private static <V extends Enum<V>> Class<V> inferValueType(Map<?, V> paramMap)
  {
    if ((paramMap instanceof EnumBiMap))
      return ((EnumBiMap)paramMap).valueType;
    if (!paramMap.isEmpty());
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return ((Enum)paramMap.values().iterator().next()).getDeclaringClass();
    }
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    this.keyType = ((Class)paramObjectInputStream.readObject());
    this.valueType = ((Class)paramObjectInputStream.readObject());
    setDelegates(new EnumMap(this.keyType), new EnumMap(this.valueType));
    Serialization.populateMap(this, paramObjectInputStream);
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeObject(this.keyType);
    paramObjectOutputStream.writeObject(this.valueType);
    Serialization.writeMap(this, paramObjectOutputStream);
  }

  public Class<K> keyType()
  {
    return this.keyType;
  }

  public Class<V> valueType()
  {
    return this.valueType;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.EnumBiMap
 * JD-Core Version:    0.6.2
 */