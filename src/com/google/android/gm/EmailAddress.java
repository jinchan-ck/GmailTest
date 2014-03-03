package com.google.android.gm;

import android.text.Html;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailAddress
{
  private static final Matcher sEmailMatcher = Pattern.compile("\\\"?([^\"<]*?)\\\"?\\s*<(.*)>").matcher("");
  private final String mAddress;
  private final String mName;

  private EmailAddress(String paramString1, String paramString2)
  {
    this.mName = paramString1;
    this.mAddress = paramString2;
  }

  public static EmailAddress getEmailAddress(String paramString)
  {
    while (true)
    {
      String str5;
      String str1;
      Object localObject2;
      try
      {
        Matcher localMatcher = sEmailMatcher.reset(paramString);
        Rfc822Token[] arrayOfRfc822Token;
        String str3;
        String str2;
        if (localMatcher.matches())
        {
          String str4 = localMatcher.group(1);
          str5 = localMatcher.group(2);
          if (str4 == null)
          {
            str1 = "";
            break label167;
            EmailAddress localEmailAddress = new EmailAddress(str1, (String)localObject2);
            return localEmailAddress;
          }
          else
          {
            str1 = Html.fromHtml(str4.trim()).toString();
            break label167;
            localObject2 = Html.fromHtml(str5).toString();
            continue;
          }
        }
        else
        {
          arrayOfRfc822Token = Rfc822Tokenizer.tokenize(paramString);
          if (arrayOfRfc822Token.length <= 0)
            break label186;
          str3 = arrayOfRfc822Token[0].getName();
          if (str3 == null)
            break label179;
          str1 = Html.fromHtml(str3.trim()).toString();
          localObject2 = Html.fromHtml(arrayOfRfc822Token[0].getAddress()).toString();
          continue;
          str2 = Html.fromHtml(paramString).toString();
          localObject2 = str2;
        }
      }
      finally
      {
      }
      label167: if (str5 == null)
      {
        localObject2 = "";
        continue;
        label179: str1 = "";
        continue;
        label186: str1 = "";
        if (paramString == null)
          localObject2 = "";
      }
    }
  }

  public String getAddress()
  {
    return this.mAddress;
  }

  String getName()
  {
    return this.mName;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.EmailAddress
 * JD-Core Version:    0.6.2
 */