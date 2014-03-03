package com.google.common.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamingThreadFactory
  implements ThreadFactory
{
  public static final ThreadFactory DEFAULT_FACTORY = Executors.defaultThreadFactory();
  private final ThreadFactory backingFactory;
  private final AtomicInteger count = new AtomicInteger(0);
  private final String format;

  public NamingThreadFactory(String paramString)
  {
    this(paramString, DEFAULT_FACTORY);
  }

  public NamingThreadFactory(String paramString, ThreadFactory paramThreadFactory)
  {
    this.format = paramString;
    this.backingFactory = paramThreadFactory;
    makeName(0);
  }

  private String makeName(int paramInt)
  {
    String str = this.format;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    return String.format(str, arrayOfObject);
  }

  public Thread newThread(Runnable paramRunnable)
  {
    Thread localThread = this.backingFactory.newThread(paramRunnable);
    localThread.setName(makeName(this.count.getAndIncrement()));
    return localThread;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.NamingThreadFactory
 * JD-Core Version:    0.6.2
 */