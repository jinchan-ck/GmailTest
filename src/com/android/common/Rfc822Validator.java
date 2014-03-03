package com.android.common;

import android.text.TextUtils;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import android.widget.AutoCompleteTextView.Validator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class Rfc822Validator
  implements AutoCompleteTextView.Validator
{
  private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[^\\s@]+@([^\\s@\\.]+\\.)+[a-zA-z][a-zA-Z][a-zA-Z]*");
  private String mDomain;
  private boolean mRemoveInvalid = false;

  public Rfc822Validator(String paramString)
  {
    this.mDomain = paramString;
  }

  private String removeIllegalCharacters(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramString.length();
    int j = 0;
    if (j < i)
    {
      char c = paramString.charAt(j);
      if ((c <= ' ') || (c > '~'));
      while (true)
      {
        j++;
        break;
        if ((c != '(') && (c != ')') && (c != '<') && (c != '>') && (c != '@') && (c != ',') && (c != ';') && (c != ':') && (c != '\\') && (c != '"') && (c != '[') && (c != ']'))
          localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }

  public CharSequence fixText(CharSequence paramCharSequence)
  {
    if (TextUtils.getTrimmedLength(paramCharSequence) == 0)
    {
      localObject = "";
      return localObject;
    }
    Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(paramCharSequence);
    Object localObject = new StringBuilder();
    int i = 0;
    label28: String str1;
    if (i < arrayOfRfc822Token.length)
    {
      str1 = arrayOfRfc822Token[i].getAddress();
      if ((!this.mRemoveInvalid) || (isValid(str1)))
        break label66;
    }
    label66: int j;
    label156: String str2;
    do
    {
      while (true)
      {
        i++;
        break label28;
        break;
        j = str1.indexOf('@');
        if (j >= 0)
          break label156;
        if (this.mDomain != null)
          arrayOfRfc822Token[i].setAddress(removeIllegalCharacters(str1) + "@" + this.mDomain);
        ((StringBuilder)localObject).append(arrayOfRfc822Token[i].toString());
        if (i + 1 < arrayOfRfc822Token.length)
          ((StringBuilder)localObject).append(", ");
      }
      str2 = removeIllegalCharacters(str1.substring(0, j));
    }
    while (TextUtils.isEmpty(str2));
    String str3 = removeIllegalCharacters(str1.substring(j + 1));
    int k;
    label204: Rfc822Token localRfc822Token;
    StringBuilder localStringBuilder;
    if (str3.length() == 0)
    {
      k = 1;
      if ((k != 0) && (this.mDomain == null))
        break label268;
      localRfc822Token = arrayOfRfc822Token[i];
      localStringBuilder = new StringBuilder().append(str2).append("@");
      if (k != 0)
        break label270;
    }
    while (true)
    {
      localRfc822Token.setAddress(str3);
      break;
      k = 0;
      break label204;
      label268: break;
      label270: str3 = this.mDomain;
    }
  }

  public boolean isValid(CharSequence paramCharSequence)
  {
    Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(paramCharSequence);
    return (arrayOfRfc822Token.length == 1) && (EMAIL_ADDRESS_PATTERN.matcher(arrayOfRfc822Token[0].getAddress()).matches());
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.common.Rfc822Validator
 * JD-Core Version:    0.6.2
 */