package com.google.android.gm.common.html.parser;

public abstract interface HtmlWhitelist
{
  public abstract HTML.Attribute lookupAttribute(String paramString);

  public abstract HTML.Element lookupElement(String paramString);
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.html.parser.HtmlWhitelist
 * JD-Core Version:    0.6.2
 */