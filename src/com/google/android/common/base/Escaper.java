package com.google.android.common.base;

public abstract class Escaper
{
  private final Function<String, String> asFunction = new Function()
  {
    public String apply(String paramAnonymousString)
    {
      return Escaper.this.escape(paramAnonymousString);
    }
  };

  public abstract String escape(String paramString);
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.Escaper
 * JD-Core Version:    0.6.2
 */