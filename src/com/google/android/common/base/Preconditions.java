package com.google.android.common.base;

public class Preconditions
{
  private static String badPositionIndex(int paramInt1, int paramInt2, String paramString)
  {
    if (paramInt1 < 0)
    {
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = paramString;
      arrayOfObject2[1] = Integer.valueOf(paramInt1);
      return format("%s (%s) must not be negative", arrayOfObject2);
    }
    if (paramInt2 < 0)
      throw new IllegalArgumentException("negative size: " + paramInt2);
    Object[] arrayOfObject1 = new Object[3];
    arrayOfObject1[0] = paramString;
    arrayOfObject1[1] = Integer.valueOf(paramInt1);
    arrayOfObject1[2] = Integer.valueOf(paramInt2);
    return format("%s (%s) must not be greater than size (%s)", arrayOfObject1);
  }

  public static void checkArgument(boolean paramBoolean)
  {
    if (!paramBoolean)
      throw new IllegalArgumentException();
  }

  public static void checkArgument(boolean paramBoolean, Object paramObject)
  {
    if (!paramBoolean)
      throw new IllegalArgumentException(String.valueOf(paramObject));
  }

  public static <T> T checkNotNull(T paramT)
  {
    if (paramT == null)
      throw new NullPointerException();
    return paramT;
  }

  public static <T> T checkNotNull(T paramT, Object paramObject)
  {
    if (paramT == null)
      throw new NullPointerException(String.valueOf(paramObject));
    return paramT;
  }

  public static int checkPositionIndex(int paramInt1, int paramInt2)
  {
    return checkPositionIndex(paramInt1, paramInt2, "index");
  }

  public static int checkPositionIndex(int paramInt1, int paramInt2, String paramString)
  {
    if ((paramInt1 < 0) || (paramInt1 > paramInt2))
      throw new IndexOutOfBoundsException(badPositionIndex(paramInt1, paramInt2, paramString));
    return paramInt1;
  }

  static String format(String paramString, Object[] paramArrayOfObject)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString.length() + 16 * paramArrayOfObject.length);
    int i = 0;
    int i2;
    for (int j = 0; ; j = i2)
    {
      int i1;
      if (j < paramArrayOfObject.length)
      {
        i1 = paramString.indexOf("%s", i);
        if (i1 != -1);
      }
      else
      {
        localStringBuilder.append(paramString.substring(i));
        if (j >= paramArrayOfObject.length)
          break label170;
        localStringBuilder.append(" [");
        int k = j + 1;
        localStringBuilder.append(paramArrayOfObject[j]);
        int n;
        for (int m = k; m < paramArrayOfObject.length; m = n)
        {
          localStringBuilder.append(", ");
          n = m + 1;
          localStringBuilder.append(paramArrayOfObject[m]);
        }
      }
      localStringBuilder.append(paramString.substring(i, i1));
      i2 = j + 1;
      localStringBuilder.append(paramArrayOfObject[j]);
      i = i1 + 2;
    }
    localStringBuilder.append("]");
    label170: return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.Preconditions
 * JD-Core Version:    0.6.2
 */