package com.google.common.base;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CaseFormat
{
  private final Pattern wordBoundary;
  private final String wordSeparator;

  static
  {
    LOWER_CAMEL = new CaseFormat("LOWER_CAMEL", 2, Pattern.compile("[A-Z]"), "");
    UPPER_CAMEL = new CaseFormat("UPPER_CAMEL", 3, Pattern.compile("[A-Z]"), "");
    UPPER_UNDERSCORE = new CaseFormat("UPPER_UNDERSCORE", 4, Pattern.compile("[_]"), "_");
    CaseFormat[] arrayOfCaseFormat = new CaseFormat[5];
    arrayOfCaseFormat[0] = LOWER_HYPHEN;
    arrayOfCaseFormat[1] = LOWER_UNDERSCORE;
    arrayOfCaseFormat[2] = LOWER_CAMEL;
    arrayOfCaseFormat[3] = UPPER_CAMEL;
    arrayOfCaseFormat[4] = UPPER_UNDERSCORE;
  }

  private CaseFormat(Pattern paramPattern, String paramString)
  {
    this.wordBoundary = paramPattern;
    this.wordSeparator = paramString;
  }

  private String normalizeFirstWord(String paramString)
  {
    switch (1.$SwitchMap$com$google$common$base$CaseFormat[ordinal()])
    {
    default:
      return normalizeWord(paramString);
    case 4:
    }
    return paramString.toLowerCase(Locale.US);
  }

  private String normalizeWord(String paramString)
  {
    switch (1.$SwitchMap$com$google$common$base$CaseFormat[ordinal()])
    {
    default:
      throw new RuntimeException("unknown case: " + this);
    case 3:
      return paramString.toLowerCase(Locale.US);
    case 1:
      return paramString.toLowerCase(Locale.US);
    case 4:
      return toTitleCase(paramString);
    case 5:
      return toTitleCase(paramString);
    case 2:
    }
    return paramString.toUpperCase(Locale.US);
  }

  private static String toTitleCase(String paramString)
  {
    if (paramString.length() < 2)
      return paramString.toUpperCase(Locale.US);
    return Character.toTitleCase(paramString.charAt(0)) + paramString.substring(1).toLowerCase(Locale.US);
  }

  public String to(CaseFormat paramCaseFormat, String paramString)
  {
    if (paramCaseFormat == null)
      throw new NullPointerException();
    if (paramString == null)
      throw new NullPointerException();
    if (paramCaseFormat == this)
      return paramString;
    StringBuilder localStringBuilder;
    int i;
    label79: int j;
    switch (1.$SwitchMap$com$google$common$base$CaseFormat[ordinal()])
    {
    default:
      localStringBuilder = null;
      i = 0;
      Matcher localMatcher = this.wordBoundary.matcher(paramString);
      if (!localMatcher.find())
        break label350;
      j = localMatcher.start();
      if (i == 0)
      {
        localStringBuilder = new StringBuilder(paramString.length() + 4 * this.wordSeparator.length());
        localStringBuilder.append(paramCaseFormat.normalizeFirstWord(paramString.substring(i, j)));
      }
      break;
    case 3:
    case 1:
    case 2:
    }
    while (true)
    {
      localStringBuilder.append(paramCaseFormat.wordSeparator);
      i = j + this.wordSeparator.length();
      break label79;
      switch (1.$SwitchMap$com$google$common$base$CaseFormat[paramCaseFormat.ordinal()])
      {
      default:
        break;
      case 1:
        return paramString.replace("-", "_");
      case 2:
        return paramString.replace("-", "_").toUpperCase(Locale.US);
        switch (1.$SwitchMap$com$google$common$base$CaseFormat[paramCaseFormat.ordinal()])
        {
        default:
          break;
        case 2:
          return paramString.toUpperCase(Locale.US);
        case 3:
          return paramString.replace("_", "-");
          switch (1.$SwitchMap$com$google$common$base$CaseFormat[paramCaseFormat.ordinal()])
          {
          case 2:
          default:
            break;
          case 1:
            return paramString.toLowerCase(Locale.US);
          case 3:
            return paramString.replace("_", "-").toLowerCase(Locale.US);
            localStringBuilder.append(paramCaseFormat.normalizeWord(paramString.substring(i, j)));
          }
          break;
        }
        break;
      }
    }
    label350: if (i == 0)
      return paramCaseFormat.normalizeFirstWord(paramString);
    localStringBuilder.append(paramCaseFormat.normalizeWord(paramString.substring(i)));
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.CaseFormat
 * JD-Core Version:    0.6.2
 */