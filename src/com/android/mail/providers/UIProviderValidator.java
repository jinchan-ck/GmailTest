package com.android.mail.providers;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Set;

public class UIProviderValidator
{
  private static String[] getValidProjection(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if (paramArrayOfString1 != null)
    {
      if (isValidProjection(paramArrayOfString1, ImmutableSet.copyOf(paramArrayOfString2)))
        return paramArrayOfString1;
      throw new IllegalArgumentException("Invalid projection: " + Arrays.toString(paramArrayOfString1));
    }
    return paramArrayOfString2;
  }

  private static boolean isValidProjection(String[] paramArrayOfString, Set<String> paramSet)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
      if (!paramSet.contains(paramArrayOfString[j]))
        return false;
    return true;
  }

  public static String[] validateAccountCookieProjection(String[] paramArrayOfString)
  {
    return getValidProjection(paramArrayOfString, UIProvider.ACCOUNT_COOKIE_PROJECTION);
  }

  public static String[] validateAccountProjection(String[] paramArrayOfString)
  {
    return getValidProjection(paramArrayOfString, UIProvider.ACCOUNTS_PROJECTION);
  }

  public static String[] validateAttachmentProjection(String[] paramArrayOfString)
  {
    return getValidProjection(paramArrayOfString, UIProvider.ATTACHMENT_PROJECTION);
  }

  public static String[] validateConversationProjection(String[] paramArrayOfString)
  {
    return getValidProjection(paramArrayOfString, UIProvider.CONVERSATION_PROJECTION);
  }

  public static String[] validateFolderProjection(String[] paramArrayOfString)
  {
    return getValidProjection(paramArrayOfString, UIProvider.FOLDERS_PROJECTION);
  }

  public static String[] validateMessageProjection(String[] paramArrayOfString)
  {
    return getValidProjection(paramArrayOfString, UIProvider.MESSAGE_PROJECTION);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.UIProviderValidator
 * JD-Core Version:    0.6.2
 */