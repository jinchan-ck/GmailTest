package com.google.android.gm.template;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

public class Templates
{
  private static final ConcurrentHashMap<String, Template> sTemplateCache = new ConcurrentHashMap();

  public static final Template get(String paramString)
  {
    return (Template)sTemplateCache.get(paramString);
  }

  public static final Template install(String paramString, InputStream paramInputStream)
  {
    return install(paramString, new InputStreamReader(paramInputStream, Charset.forName("UTF-8")));
  }

  public static final Template install(String paramString, Reader paramReader)
  {
    Template localTemplate = new Parser(paramReader).parse();
    localTemplate.setName(paramString);
    sTemplateCache.putIfAbsent(paramString, localTemplate);
    return localTemplate;
  }

  public static final Template install(String paramString1, String paramString2)
  {
    return install(paramString1, new StringReader(paramString2));
  }

  static final void reset()
  {
    sTemplateCache.clear();
  }

  public static final Template uninstall(String paramString)
  {
    return (Template)sTemplateCache.remove(paramString);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Templates
 * JD-Core Version:    0.6.2
 */