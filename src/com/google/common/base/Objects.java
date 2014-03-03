package com.google.common.base;

import java.util.Arrays;

public final class Objects
{
  public static boolean equal(Object paramObject1, Object paramObject2)
  {
    return (paramObject1 == paramObject2) || ((paramObject1 != null) && (paramObject1.equals(paramObject2)));
  }

  public static <T> T firstNonNull(T paramT1, T paramT2)
  {
    if (paramT1 != null)
      return paramT1;
    return Preconditions.checkNotNull(paramT2);
  }

  public static int hashCode(Object[] paramArrayOfObject)
  {
    return Arrays.hashCode(paramArrayOfObject);
  }

  private static String simpleName(Class<?> paramClass)
  {
    String str = paramClass.getName().replaceAll("\\$[0-9]+", "\\$");
    int i = str.lastIndexOf('$');
    if (i == -1)
      i = str.lastIndexOf('.');
    return str.substring(i + 1);
  }

  public static ToStringHelper toStringHelper(Object paramObject)
  {
    return new ToStringHelper(simpleName(paramObject.getClass()), null);
  }

  public static final class ToStringHelper
  {
    private final StringBuilder builder;
    private boolean needsSeparator = false;

    private ToStringHelper(String paramString)
    {
      Preconditions.checkNotNull(paramString);
      this.builder = new StringBuilder(32).append(paramString).append('{');
    }

    private StringBuilder checkNameAndAppend(String paramString)
    {
      Preconditions.checkNotNull(paramString);
      return maybeAppendSeparator().append(paramString).append('=');
    }

    private StringBuilder maybeAppendSeparator()
    {
      if (this.needsSeparator)
        return this.builder.append(", ");
      this.needsSeparator = true;
      return this.builder;
    }

    public ToStringHelper add(String paramString, int paramInt)
    {
      checkNameAndAppend(paramString).append(paramInt);
      return this;
    }

    public ToStringHelper add(String paramString, Object paramObject)
    {
      checkNameAndAppend(paramString).append(paramObject);
      return this;
    }

    public ToStringHelper addValue(Object paramObject)
    {
      maybeAppendSeparator().append(paramObject);
      return this;
    }

    public String toString()
    {
      try
      {
        String str = '}';
        return str;
      }
      finally
      {
        this.builder.setLength(-1 + this.builder.length());
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Objects
 * JD-Core Version:    0.6.2
 */