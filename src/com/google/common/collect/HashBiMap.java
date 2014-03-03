package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public final class HashBiMap<K, V> extends AbstractBiMap<K, V>
{
  private static final long serialVersionUID;

  private HashBiMap()
  {
    super(new HashMap(), new HashMap());
  }

  public static <K, V> HashBiMap<K, V> create()
  {
    return new HashBiMap();
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    int i = Serialization.readCount(paramObjectInputStream);
    setDelegates(Maps.newHashMapWithExpectedSize(i), Maps.newHashMapWithExpectedSize(i));
    Serialization.populateMap(this, paramObjectInputStream, i);
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    Serialization.writeMap(this, paramObjectOutputStream);
  }

  public V put(K paramK, V paramV)
  {
    return super.put(paramK, paramV);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.HashBiMap
 * JD-Core Version:    0.6.2
 */