package com.google.android.gm.common.base;

public abstract class Escaper
{
  private final Function<String, String> asFunction = new Function()
  {
    public String apply(String paramAnonymousString)
    {
      return Escaper.this.escape(paramAnonymousString);
    }
  };

  public Function<String, String> asFunction()
  {
    return this.asFunction;
  }

  public abstract Appendable escape(Appendable paramAppendable);

  public abstract String escape(String paramString);
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.base.Escaper
 * JD-Core Version:    0.6.2
 */