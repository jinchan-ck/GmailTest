package com.google.common.util.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public abstract class AbstractFuture<V>
  implements Future<V>
{
  private final Sync<V> sync = new Sync();

  protected final boolean cancel()
  {
    boolean bool = this.sync.cancel();
    if (bool)
      done();
    return bool;
  }

  public boolean cancel(boolean paramBoolean)
  {
    return false;
  }

  protected void done()
  {
  }

  public V get()
    throws InterruptedException, ExecutionException
  {
    return this.sync.get();
  }

  public V get(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, TimeoutException, ExecutionException
  {
    return this.sync.get(paramTimeUnit.toNanos(paramLong));
  }

  public boolean isCancelled()
  {
    return this.sync.isCancelled();
  }

  public boolean isDone()
  {
    return this.sync.isDone();
  }

  protected boolean set(V paramV)
  {
    boolean bool = this.sync.set(paramV);
    if (bool)
      done();
    return bool;
  }

  protected boolean setException(Throwable paramThrowable)
  {
    boolean bool = this.sync.setException(paramThrowable);
    if (bool)
      done();
    if ((paramThrowable instanceof Error))
      throw ((Error)paramThrowable);
    return bool;
  }

  static final class Sync<V> extends AbstractQueuedSynchronizer
  {
    static final int CANCELLED = 4;
    static final int COMPLETED = 2;
    static final int COMPLETING = 1;
    static final int RUNNING;
    private static final long serialVersionUID;
    private ExecutionException exception;
    private V value;

    private boolean complete(V paramV, Throwable paramThrowable, int paramInt)
    {
      if (compareAndSetState(0, 1))
      {
        this.value = paramV;
        if (paramThrowable == null);
        for (ExecutionException localExecutionException = null; ; localExecutionException = new ExecutionException(paramThrowable))
        {
          this.exception = localExecutionException;
          releaseShared(paramInt);
          return true;
        }
      }
      return false;
    }

    private V getValue()
      throws CancellationException, ExecutionException
    {
      int i = getState();
      switch (i)
      {
      case 3:
      default:
        throw new IllegalStateException("Error, synchronizer in invalid state: " + i);
      case 2:
        if (this.exception != null)
          throw this.exception;
        return this.value;
      case 4:
      }
      throw new CancellationException("Task was cancelled.");
    }

    boolean cancel()
    {
      return complete(null, null, 4);
    }

    V get()
      throws CancellationException, ExecutionException, InterruptedException
    {
      acquireSharedInterruptibly(-1);
      return getValue();
    }

    V get(long paramLong)
      throws TimeoutException, CancellationException, ExecutionException, InterruptedException
    {
      if (!tryAcquireSharedNanos(-1, paramLong))
        throw new TimeoutException("Timeout waiting for task.");
      return getValue();
    }

    boolean isCancelled()
    {
      return getState() == 4;
    }

    boolean isDone()
    {
      return (0x6 & getState()) != 0;
    }

    boolean set(V paramV)
    {
      return complete(paramV, null, 2);
    }

    boolean setException(Throwable paramThrowable)
    {
      return complete(null, paramThrowable, 2);
    }

    protected int tryAcquireShared(int paramInt)
    {
      if (isDone())
        return 1;
      return -1;
    }

    protected boolean tryReleaseShared(int paramInt)
    {
      setState(paramInt);
      return true;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.AbstractFuture
 * JD-Core Version:    0.6.2
 */