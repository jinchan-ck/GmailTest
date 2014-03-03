package org.apache.james.mime4j;

public class Log
{
  public Log(Class paramClass)
  {
  }

  private static String toString(Object paramObject, Throwable paramThrowable)
  {
    if (paramObject == null);
    for (String str = "(null)"; paramThrowable == null; str = paramObject.toString())
      return str;
    return str + " " + paramThrowable.getMessage();
  }

  public void debug(Object paramObject)
  {
    if (!isDebugEnabled())
      return;
    android.util.Log.d("UnifiedEmail", toString(paramObject, null));
  }

  public void error(Object paramObject)
  {
    android.util.Log.e("UnifiedEmail", toString(paramObject, null));
  }

  public boolean isDebugEnabled()
  {
    return false;
  }

  public boolean isWarnEnabled()
  {
    return true;
  }

  public void warn(Object paramObject)
  {
    android.util.Log.w("UnifiedEmail", toString(paramObject, null));
  }

  public void warn(Object paramObject, Throwable paramThrowable)
  {
    android.util.Log.w("UnifiedEmail", toString(paramObject, paramThrowable));
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     org.apache.james.mime4j.Log
 * JD-Core Version:    0.6.2
 */