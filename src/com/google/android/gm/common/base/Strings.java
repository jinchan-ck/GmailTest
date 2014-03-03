package com.google.android.gm.common.base;

public final class Strings
{
  public static String emptyToNull(String paramString)
  {
    if (isNullOrEmpty(paramString))
      return null;
    return paramString;
  }

  public static boolean isNullOrEmpty(String paramString)
  {
    return (paramString == null) || (paramString.length() == 0);
  }

  public static String nullToEmpty(String paramString)
  {
    if (paramString == null)
      return "";
    return paramString;
  }

  public static String padEnd(String paramString, int paramInt, char paramChar)
  {
    Preconditions.checkNotNull(paramString);
    if (paramString.length() >= paramInt)
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder(paramInt);
    localStringBuilder.append(paramString);
    for (int i = paramString.length(); i < paramInt; i++)
      localStringBuilder.append(paramChar);
    return localStringBuilder.toString();
  }

  public static String padStart(String paramString, int paramInt, char paramChar)
  {
    Preconditions.checkNotNull(paramString);
    if (paramString.length() >= paramInt)
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder(paramInt);
    for (int i = paramString.length(); i < paramInt; i++)
      localStringBuilder.append(paramChar);
    localStringBuilder.append(paramString);
    return localStringBuilder.toString();
  }

  public static String repeat(String paramString, int paramInt)
  {
    Preconditions.checkNotNull(paramString);
    if (paramInt >= 0);
    StringBuilder localStringBuilder;
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool, "invalid count: %s", arrayOfObject);
      localStringBuilder = new StringBuilder(paramInt * paramString.length());
      for (int i = 0; i < paramInt; i++)
        localStringBuilder.append(paramString);
    }
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.base.Strings
 * JD-Core Version:    0.6.2
 */