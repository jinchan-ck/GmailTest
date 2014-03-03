package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final class Serialization
{
  static <K, V> void populateMap(Map<K, V> paramMap, ObjectInputStream paramObjectInputStream, int paramInt)
    throws IOException, ClassNotFoundException
  {
    for (int i = 0; i < paramInt; i++)
      paramMap.put(paramObjectInputStream.readObject(), paramObjectInputStream.readObject());
  }

  static int readCount(ObjectInputStream paramObjectInputStream)
    throws IOException
  {
    return paramObjectInputStream.readInt();
  }

  static <K, V> void writeMap(Map<K, V> paramMap, ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeInt(paramMap.size());
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramObjectOutputStream.writeObject(localEntry.getKey());
      paramObjectOutputStream.writeObject(localEntry.getValue());
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Serialization
 * JD-Core Version:    0.6.2
 */