package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Service;
import com.google.common.base.Service.State;
import com.google.common.base.Throwables;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractService
  implements Service
{
  private final ReentrantLock lock = new ReentrantLock();
  private final Transition shutdown = new Transition(null);
  private boolean shutdownWhenStartupFinishes = false;
  private final Transition startup = new Transition(null);
  private Service.State state = Service.State.NEW;

  protected abstract void doStart();

  protected abstract void doStop();

  public final boolean isRunning()
  {
    return state() == Service.State.RUNNING;
  }

  protected final void notifyFailed(Throwable paramThrowable)
  {
    Preconditions.checkNotNull(paramThrowable);
    this.lock.lock();
    try
    {
      if (this.state == Service.State.STARTING)
      {
        this.startup.transitionFailed(paramThrowable);
        this.shutdown.transitionFailed(new Exception("Service failed to start.", paramThrowable));
      }
      while (true)
      {
        this.state = Service.State.FAILED;
        return;
        if (this.state == Service.State.STOPPING)
          this.shutdown.transitionFailed(paramThrowable);
      }
    }
    finally
    {
      this.lock.unlock();
    }
  }

  protected final void notifyStarted()
  {
    this.lock.lock();
    try
    {
      if (this.state != Service.State.STARTING)
      {
        IllegalStateException localIllegalStateException = new IllegalStateException("Cannot notifyStarted() when the service is " + this.state);
        notifyFailed(localIllegalStateException);
        throw localIllegalStateException;
      }
    }
    finally
    {
      this.lock.unlock();
    }
    this.state = Service.State.RUNNING;
    if (this.shutdownWhenStartupFinishes)
      stop();
    while (true)
    {
      this.lock.unlock();
      return;
      this.startup.transitionSucceeded(Service.State.RUNNING);
    }
  }

  protected final void notifyStopped()
  {
    this.lock.lock();
    try
    {
      if ((this.state != Service.State.STOPPING) && (this.state != Service.State.RUNNING))
      {
        IllegalStateException localIllegalStateException = new IllegalStateException("Cannot notifyStopped() when the service is " + this.state);
        notifyFailed(localIllegalStateException);
        throw localIllegalStateException;
      }
    }
    finally
    {
      this.lock.unlock();
    }
    this.state = Service.State.TERMINATED;
    this.shutdown.transitionSucceeded(Service.State.TERMINATED);
    this.lock.unlock();
  }

  public final Future<Service.State> start()
  {
    this.lock.lock();
    try
    {
      if (this.state == Service.State.NEW)
      {
        this.state = Service.State.STARTING;
        doStart();
      }
      return this.startup;
    }
    catch (Throwable localThrowable)
    {
      while (true)
      {
        notifyFailed(localThrowable);
        this.lock.unlock();
      }
    }
    finally
    {
      this.lock.unlock();
    }
  }

  public Service.State startAndWait()
  {
    try
    {
      Service.State localState = (Service.State)start().get();
      return localState;
    }
    catch (InterruptedException localInterruptedException)
    {
      Thread.currentThread().interrupt();
      throw new RuntimeException(localInterruptedException);
    }
    catch (ExecutionException localExecutionException)
    {
      throw Throwables.propagate(localExecutionException.getCause());
    }
  }

  public final Service.State state()
  {
    this.lock.lock();
    try
    {
      if ((this.shutdownWhenStartupFinishes) && (this.state == Service.State.STARTING))
      {
        Service.State localState2 = Service.State.STOPPING;
        return localState2;
      }
      Service.State localState1 = this.state;
      return localState1;
    }
    finally
    {
      this.lock.unlock();
    }
  }

  public final Future<Service.State> stop()
  {
    this.lock.lock();
    try
    {
      if (this.state == Service.State.NEW)
      {
        this.state = Service.State.TERMINATED;
        this.startup.transitionSucceeded(Service.State.TERMINATED);
        this.shutdown.transitionSucceeded(Service.State.TERMINATED);
      }
      while (true)
      {
        return this.shutdown;
        if (this.state != Service.State.STARTING)
          break;
        this.shutdownWhenStartupFinishes = true;
        this.startup.transitionSucceeded(Service.State.STOPPING);
      }
    }
    catch (Throwable localThrowable)
    {
      while (true)
      {
        notifyFailed(localThrowable);
        this.lock.unlock();
        continue;
        if (this.state == Service.State.RUNNING)
        {
          this.state = Service.State.STOPPING;
          doStop();
        }
      }
    }
    finally
    {
      this.lock.unlock();
    }
  }

  public Service.State stopAndWait()
  {
    try
    {
      Service.State localState = (Service.State)stop().get();
      return localState;
    }
    catch (ExecutionException localExecutionException)
    {
      throw Throwables.propagate(localExecutionException.getCause());
    }
    catch (InterruptedException localInterruptedException)
    {
      Thread.currentThread().interrupt();
      throw new RuntimeException(localInterruptedException);
    }
  }

  private static class Transition
    implements Future<Service.State>
  {
    private final CountDownLatch done = new CountDownLatch(1);
    private Throwable failureCause;
    private Service.State result;

    private Service.State getImmediately()
      throws ExecutionException
    {
      if (this.result == Service.State.FAILED)
        throw new ExecutionException(this.failureCause);
      return this.result;
    }

    public boolean cancel(boolean paramBoolean)
    {
      return false;
    }

    public Service.State get()
      throws InterruptedException, ExecutionException
    {
      this.done.await();
      return getImmediately();
    }

    public Service.State get(long paramLong, TimeUnit paramTimeUnit)
      throws InterruptedException, ExecutionException, TimeoutException
    {
      if (this.done.await(paramLong, paramTimeUnit))
        return getImmediately();
      throw new TimeoutException();
    }

    public boolean isCancelled()
    {
      return false;
    }

    public boolean isDone()
    {
      return this.done.getCount() == 0L;
    }

    void transitionFailed(Throwable paramThrowable)
    {
      if (this.result == null);
      for (boolean bool = true; ; bool = false)
      {
        Preconditions.checkState(bool);
        this.result = Service.State.FAILED;
        this.failureCause = paramThrowable;
        this.done.countDown();
        return;
      }
    }

    void transitionSucceeded(Service.State paramState)
    {
      if (this.result == null);
      for (boolean bool = true; ; bool = false)
      {
        Preconditions.checkState(bool);
        this.result = paramState;
        this.done.countDown();
        return;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.AbstractService
 * JD-Core Version:    0.6.2
 */