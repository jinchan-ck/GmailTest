package com.android.mail.providers;

import android.text.Html;
import android.text.TextUtils;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import com.android.common.Rfc822Validator;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.james.mime4j.decoder.DecoderUtil;

public class Address
{
  private static final Address[] EMPTY_ADDRESS_ARRAY = new Address[0];
  private static final String LOG_TAG = LogTag.getLogTag();
  private static final Pattern REMOVE_OPTIONAL_BRACKET = Pattern.compile("^<?([^>]+)>?$");
  private static final Pattern REMOVE_OPTIONAL_DQUOTE = Pattern.compile("^\"?([^\"]*)\"?$");
  private static final Pattern UNQUOTE = Pattern.compile("\\\\([\\\\\"])");
  private String mAddress;
  private String mName;
  private String mSimplifiedName;

  public Address(String paramString1, String paramString2)
  {
    setName(paramString1);
    setAddress(paramString2);
  }

  public static Address getEmailAddress(String paramString)
  {
    while (true)
    {
      try
      {
        boolean bool = TextUtils.isEmpty(paramString);
        Address localAddress;
        if (bool)
        {
          localAddress = null;
          return localAddress;
        }
        Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(paramString);
        if (arrayOfRfc822Token.length <= 0)
          break label116;
        String str3 = arrayOfRfc822Token[0].getName();
        if (str3 != null)
        {
          str1 = Html.fromHtml(str3.trim()).toString();
          localObject2 = Html.fromHtml(arrayOfRfc822Token[0].getAddress()).toString();
          localAddress = new Address(str1, (String)localObject2);
          continue;
        }
      }
      finally
      {
      }
      String str1 = "";
      continue;
      label116: 
      do
      {
        String str2 = Html.fromHtml(paramString).toString();
        localObject2 = str2;
        break;
        str1 = "";
      }
      while (paramString != null);
      Object localObject2 = "";
    }
  }

  static boolean isValidAddress(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    int i;
    do
    {
      return false;
      i = paramString.indexOf("@");
    }
    while (i == -1);
    return new Rfc822Validator(paramString.substring(0, i)).isValid(paramString);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof Address))
      return getAddress().equals(((Address)paramObject).getAddress());
    return super.equals(paramObject);
  }

  public String getAddress()
  {
    return this.mAddress;
  }

  public String getName()
  {
    return this.mName;
  }

  public String getSimplifiedName()
  {
    String str2;
    if (this.mSimplifiedName == null)
    {
      if ((!TextUtils.isEmpty(this.mName)) || (TextUtils.isEmpty(this.mAddress)))
        break label74;
      int j = this.mAddress.indexOf('@');
      if (j == -1)
        break label67;
      str2 = this.mAddress.substring(0, j);
      this.mSimplifiedName = str2;
    }
    while (true)
    {
      return this.mSimplifiedName;
      label67: str2 = "";
      break;
      label74: if (!TextUtils.isEmpty(this.mName))
      {
        for (int i = this.mName.indexOf(' '); (i > 0) && (this.mName.charAt(i - 1) == ','); i--);
        if (i < 1);
        for (String str1 = this.mName; ; str1 = this.mName.substring(0, i))
        {
          this.mSimplifiedName = str1;
          break;
        }
      }
      LogUtils.w(LOG_TAG, "Unable to get a simplified name", new Object[0]);
      this.mSimplifiedName = "";
    }
  }

  public void setAddress(String paramString)
  {
    this.mAddress = REMOVE_OPTIONAL_BRACKET.matcher(paramString).replaceAll("$1");
  }

  public void setName(String paramString)
  {
    if (paramString != null)
    {
      String str = REMOVE_OPTIONAL_DQUOTE.matcher(paramString).replaceAll("$1");
      paramString = DecoderUtil.decodeEncodedWords(UNQUOTE.matcher(str).replaceAll("$1"));
      if (paramString.length() == 0)
        paramString = null;
    }
    this.mName = paramString;
  }

  public String toString()
  {
    if ((this.mName != null) && (!this.mName.equals(this.mAddress)))
    {
      if (this.mName.matches(".*[\\(\\)<>@,;:\\\\\".\\[\\]].*"))
        return Utils.ensureQuotedString(this.mName) + " <" + this.mAddress + ">";
      return this.mName + " <" + this.mAddress + ">";
    }
    return this.mAddress;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.Address
 * JD-Core Version:    0.6.2
 */