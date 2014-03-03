package com.google.common.base;

import java.util.HashMap;
import java.util.Map;

public final class Defaults
{
  private static final Map<Class<?>, Object> DEFAULTS = new HashMap(16);

  static
  {
    put(Boolean.TYPE, Boolean.valueOf(false));
    put(Character.TYPE, Character.valueOf('\000'));
    put(Byte.TYPE, Byte.valueOf((byte)0));
    put(Short.TYPE, Short.valueOf((short)0));
    put(Integer.TYPE, Integer.valueOf(0));
    put(Long.TYPE, Long.valueOf(0L));
    put(Float.TYPE, Float.valueOf(0.0F));
    put(Double.TYPE, Double.valueOf(0.0D));
  }

  public static <T> T defaultValue(Class<T> paramClass)
  {
    return DEFAULTS.get(paramClass);
  }

  private static <T> void put(Class<T> paramClass, T paramT)
  {
    DEFAULTS.put(paramClass, paramT);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Defaults
 * JD-Core Version:    0.6.2
 */