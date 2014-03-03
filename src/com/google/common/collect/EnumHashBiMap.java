package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public final class EnumHashBiMap<K extends Enum<K>, V> extends AbstractBiMap<K, V>
{
  private static final long serialVersionUID;
  private transient Class<K> keyType;

  private EnumHashBiMap(Class<K> paramClass)
  {
    super(new EnumMap(paramClass), Maps.newHashMapWithExpectedSize(((Enum[])paramClass.getEnumConstants()).length));
    this.keyType = paramClass;
  }

  public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Class<K> paramClass)
  {
    return new EnumHashBiMap(paramClass);
  }

  public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Map<K, ? extends V> paramMap)
  {
    EnumHashBiMap localEnumHashBiMap = create(EnumBiMap.inferKeyType(paramMap));
    localEnumHashBiMap.putAll(paramMap);
    return localEnumHashBiMap;
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    this.keyType = ((Class)paramObjectInputStream.readObject());
    setDelegates(new EnumMap(this.keyType), new HashMap(3 * ((Enum[])this.keyType.getEnumConstants()).length / 2));
    Serialization.populateMap(this, paramObjectInputStream);
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeObject(this.keyType);
    Serialization.writeMap(this, paramObjectOutputStream);
  }

  public V forcePut(K paramK, @Nullable V paramV)
  {
    return super.forcePut(paramK, paramV);
  }

  public Class<K> keyType()
  {
    return this.keyType;
  }

  public V put(K paramK, @Nullable V paramV)
  {
    return super.put(paramK, paramV);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.EnumHashBiMap
 * JD-Core Version:    0.6.2
 */