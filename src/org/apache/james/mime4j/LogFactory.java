package org.apache.james.mime4j;

public final class LogFactory
{
  public static Log getLog(Class paramClass)
  {
    return new Log(paramClass);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     org.apache.james.mime4j.LogFactory
 * JD-Core Version:    0.6.2
 */