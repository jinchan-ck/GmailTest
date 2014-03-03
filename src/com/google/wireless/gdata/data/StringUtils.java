package com.google.wireless.gdata.data;

public final class StringUtils
{
  public static boolean isEmpty(String paramString)
  {
    return (paramString == null) || (paramString.length() == 0);
  }

  public static boolean isEmptyOrWhitespace(String paramString)
  {
    if (paramString == null)
      return true;
    int i = paramString.length();
    for (int j = 0; j < i; j++)
      if (!Character.isWhitespace(paramString.charAt(j)))
        return false;
    return true;
  }

  public static int parseInt(String paramString, int paramInt)
  {
    if (paramString != null)
      try
      {
        int i = Integer.parseInt(paramString);
        return i;
      }
      catch (NumberFormatException localNumberFormatException)
      {
      }
    return paramInt;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata.data.StringUtils
 * JD-Core Version:    0.6.2
 */