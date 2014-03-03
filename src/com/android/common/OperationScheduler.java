package com.android.common;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.http.AndroidHttpClient;
import android.text.format.Time;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class OperationScheduler
{
  private static final String PREFIX = "OperationScheduler_";
  private final SharedPreferences mStorage;

  public OperationScheduler(SharedPreferences paramSharedPreferences)
  {
    this.mStorage = paramSharedPreferences;
  }

  private long getTimeBefore(String paramString, long paramLong)
  {
    long l = this.mStorage.getLong(paramString, 0L);
    if (l > paramLong)
    {
      SharedPreferences.Editor localEditor = this.mStorage.edit();
      l = paramLong;
      localEditor.putLong(paramString, paramLong).commit();
    }
    return l;
  }

  public static Options parseOptions(String paramString, Options paramOptions)
    throws IllegalArgumentException
  {
    String[] arrayOfString = paramString.split(" +");
    int i = arrayOfString.length;
    int j = 0;
    if (j < i)
    {
      String str = arrayOfString[j];
      if (str.length() == 0);
      while (true)
      {
        j++;
        break;
        if (str.startsWith("backoff="))
        {
          int k = str.indexOf('+', 8);
          if (k < 0)
          {
            paramOptions.backoffFixedMillis = parseSeconds(str.substring(8));
          }
          else
          {
            if (k > 8)
              paramOptions.backoffFixedMillis = parseSeconds(str.substring(8, k));
            paramOptions.backoffIncrementalMillis = parseSeconds(str.substring(k + 1));
          }
        }
        else if (str.startsWith("max="))
        {
          paramOptions.maxMoratoriumMillis = parseSeconds(str.substring(4));
        }
        else if (str.startsWith("min="))
        {
          paramOptions.minTriggerMillis = parseSeconds(str.substring(4));
        }
        else if (str.startsWith("period="))
        {
          paramOptions.periodicIntervalMillis = parseSeconds(str.substring(7));
        }
        else
        {
          paramOptions.periodicIntervalMillis = parseSeconds(str);
        }
      }
    }
    return paramOptions;
  }

  private static long parseSeconds(String paramString)
    throws NumberFormatException
  {
    return ()(1000.0F * Float.parseFloat(paramString));
  }

  protected long currentTimeMillis()
  {
    return System.currentTimeMillis();
  }

  public long getLastAttemptTimeMillis()
  {
    return Math.max(this.mStorage.getLong("OperationScheduler_lastSuccessTimeMillis", 0L), this.mStorage.getLong("OperationScheduler_lastErrorTimeMillis", 0L));
  }

  public long getLastSuccessTimeMillis()
  {
    return this.mStorage.getLong("OperationScheduler_lastSuccessTimeMillis", 0L);
  }

  public long getNextTimeMillis(Options paramOptions)
  {
    if (!this.mStorage.getBoolean("OperationScheduler_enabledState", true))
      return 9223372036854775807L;
    if (this.mStorage.getBoolean("OperationScheduler_permanentError", false))
      return 9223372036854775807L;
    int i = this.mStorage.getInt("OperationScheduler_errorCount", 0);
    long l1 = currentTimeMillis();
    long l2 = getTimeBefore("OperationScheduler_lastSuccessTimeMillis", l1);
    long l3 = getTimeBefore("OperationScheduler_lastErrorTimeMillis", l1);
    long l4 = this.mStorage.getLong("OperationScheduler_triggerTimeMillis", 9223372036854775807L);
    long l5 = getTimeBefore("OperationScheduler_moratoriumTimeMillis", getTimeBefore("OperationScheduler_moratoriumSetTimeMillis", l1) + paramOptions.maxMoratoriumMillis);
    long l6 = l4;
    if (paramOptions.periodicIntervalMillis > 0L)
    {
      long l9 = l2 + paramOptions.periodicIntervalMillis;
      l6 = Math.min(l6, l9);
    }
    long l7 = Math.max(Math.max(l6, l5), l2 + paramOptions.minTriggerMillis);
    if (i > 0)
    {
      long l8 = l3 + paramOptions.backoffFixedMillis + paramOptions.backoffIncrementalMillis * i;
      l7 = Math.max(l7, l8);
    }
    return l7;
  }

  public void onPermanentError()
  {
    this.mStorage.edit().putBoolean("OperationScheduler_permanentError", true).commit();
  }

  public void onSuccess()
  {
    resetTransientError();
    resetPermanentError();
    this.mStorage.edit().remove("OperationScheduler_errorCount").remove("OperationScheduler_lastErrorTimeMillis").remove("OperationScheduler_permanentError").remove("OperationScheduler_triggerTimeMillis").putLong("OperationScheduler_lastSuccessTimeMillis", currentTimeMillis()).commit();
  }

  public void onTransientError()
  {
    this.mStorage.edit().putLong("OperationScheduler_lastErrorTimeMillis", currentTimeMillis()).commit();
    this.mStorage.edit().putInt("OperationScheduler_errorCount", 1 + this.mStorage.getInt("OperationScheduler_errorCount", 0)).commit();
  }

  public void resetPermanentError()
  {
    this.mStorage.edit().remove("OperationScheduler_permanentError").commit();
  }

  public void resetTransientError()
  {
    this.mStorage.edit().remove("OperationScheduler_errorCount").commit();
  }

  public void setEnabledState(boolean paramBoolean)
  {
    this.mStorage.edit().putBoolean("OperationScheduler_enabledState", paramBoolean).commit();
  }

  public boolean setMoratoriumTimeHttp(String paramString)
  {
    try
    {
      setMoratoriumTimeMillis(1000L * Long.valueOf(paramString).longValue() + currentTimeMillis());
      return true;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      try
      {
        setMoratoriumTimeMillis(AndroidHttpClient.parseDate(paramString));
        return true;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
      }
    }
    return false;
  }

  public void setMoratoriumTimeMillis(long paramLong)
  {
    this.mStorage.edit().putLong("OperationScheduler_moratoriumTimeMillis", paramLong).putLong("OperationScheduler_moratoriumSetTimeMillis", currentTimeMillis()).commit();
  }

  public void setTriggerTimeMillis(long paramLong)
  {
    this.mStorage.edit().putLong("OperationScheduler_triggerTimeMillis", paramLong).commit();
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("[OperationScheduler:");
    Iterator localIterator = new TreeSet(this.mStorage.getAll().keySet()).iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (str.startsWith("OperationScheduler_"))
        if (str.endsWith("TimeMillis"))
        {
          Time localTime = new Time();
          localTime.set(this.mStorage.getLong(str, 0L));
          localStringBuilder.append(" ").append(str.substring("OperationScheduler_".length(), str.length() - 10));
          localStringBuilder.append("=").append(localTime.format("%Y-%m-%d/%H:%M:%S"));
        }
        else
        {
          localStringBuilder.append(" ").append(str.substring("OperationScheduler_".length()));
          localStringBuilder.append("=").append(this.mStorage.getAll().get(str).toString());
        }
    }
    return "]";
  }

  public static class Options
  {
    public long backoffFixedMillis = 0L;
    public long backoffIncrementalMillis = 5000L;
    public long maxMoratoriumMillis = 86400000L;
    public long minTriggerMillis = 0L;
    public long periodicIntervalMillis = 0L;

    public String toString()
    {
      Object[] arrayOfObject = new Object[5];
      arrayOfObject[0] = Double.valueOf(this.backoffFixedMillis / 1000.0D);
      arrayOfObject[1] = Double.valueOf(this.backoffIncrementalMillis / 1000.0D);
      arrayOfObject[2] = Double.valueOf(this.maxMoratoriumMillis / 1000.0D);
      arrayOfObject[3] = Double.valueOf(this.minTriggerMillis / 1000.0D);
      arrayOfObject[4] = Double.valueOf(this.periodicIntervalMillis / 1000.0D);
      return String.format("OperationScheduler.Options[backoff=%.1f+%.1f max=%.1f min=%.1f period=%.1f]", arrayOfObject);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.android.common.OperationScheduler
 * JD-Core Version:    0.6.2
 */