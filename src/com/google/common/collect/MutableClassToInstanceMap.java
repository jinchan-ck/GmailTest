package com.google.common.collect;

import java.util.HashMap;
import java.util.Map;

public final class MutableClassToInstanceMap<B> extends ConstrainedMap<Class<? extends B>, B>
  implements ClassToInstanceMap<B>
{
  private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new ImmutableMap.Builder().put(Boolean.TYPE, Boolean.class).put(Byte.TYPE, Byte.class).put(Character.TYPE, Character.class).put(Double.TYPE, Double.class).put(Float.TYPE, Float.class).put(Integer.TYPE, Integer.class).put(Long.TYPE, Long.class).put(Short.TYPE, Short.class).put(Void.TYPE, Void.class).build();
  private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint()
  {
    public void checkKeyValue(Class<?> paramAnonymousClass, Object paramAnonymousObject)
    {
      MutableClassToInstanceMap.cast(paramAnonymousClass, paramAnonymousObject);
    }
  };
  private static final long serialVersionUID;

  private MutableClassToInstanceMap(Map<Class<? extends B>, B> paramMap)
  {
    super(paramMap, VALUE_CAN_BE_CAST_TO_KEY);
  }

  static <B, T extends B> T cast(Class<T> paramClass, B paramB)
  {
    return wrap(paramClass).cast(paramB);
  }

  public static <B> MutableClassToInstanceMap<B> create()
  {
    return new MutableClassToInstanceMap(new HashMap());
  }

  public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> paramMap)
  {
    return new MutableClassToInstanceMap(paramMap);
  }

  private static <T> Class<T> wrap(Class<T> paramClass)
  {
    if (paramClass.isPrimitive())
      return (Class)PRIMITIVES_TO_WRAPPERS.get(paramClass);
    return paramClass;
  }

  public <T extends B> T getInstance(Class<T> paramClass)
  {
    return cast(paramClass, get(paramClass));
  }

  public <T extends B> T putInstance(Class<T> paramClass, T paramT)
  {
    return cast(paramClass, put(paramClass, paramT));
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MutableClassToInstanceMap
 * JD-Core Version:    0.6.2
 */