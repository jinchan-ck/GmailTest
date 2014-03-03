package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

public class Futures
{
  public static <I, O> ListenableFuture<O> chain(ListenableFuture<I> paramListenableFuture, Function<? super I, ? extends ListenableFuture<? extends O>> paramFunction)
  {
    return chain(paramListenableFuture, paramFunction, Executors.sameThreadExecutor());
  }

  public static <I, O> ListenableFuture<O> chain(ListenableFuture<I> paramListenableFuture, Function<? super I, ? extends ListenableFuture<? extends O>> paramFunction, Executor paramExecutor)
  {
    ChainingListenableFuture localChainingListenableFuture = new ChainingListenableFuture(paramFunction, paramListenableFuture, null);
    paramListenableFuture.addListener(localChainingListenableFuture, paramExecutor);
    return localChainingListenableFuture;
  }

  public static <I, O> ListenableFuture<O> compose(ListenableFuture<I> paramListenableFuture, Function<? super I, ? extends O> paramFunction)
  {
    return compose(paramListenableFuture, paramFunction, Executors.sameThreadExecutor());
  }

  public static <I, O> ListenableFuture<O> compose(ListenableFuture<I> paramListenableFuture, Function<? super I, ? extends O> paramFunction, Executor paramExecutor)
  {
    return chain(paramListenableFuture, new Function()
    {
      public ListenableFuture<O> apply(I paramAnonymousI)
      {
        return Futures.immediateFuture(this.val$function.apply(paramAnonymousI));
      }
    }
    , paramExecutor);
  }

  public static <I, O> Future<O> compose(Future<I> paramFuture, final Function<? super I, ? extends O> paramFunction)
  {
    return new Future()
    {
      private final Object lock = new Object();
      private boolean set = false;
      private O value = null;

      private O apply(I paramAnonymousI)
      {
        synchronized (this.lock)
        {
          if (!this.set)
          {
            this.value = paramFunction.apply(paramAnonymousI);
            this.set = true;
          }
          Object localObject3 = this.value;
          return localObject3;
        }
      }

      public boolean cancel(boolean paramAnonymousBoolean)
      {
        return this.val$future.cancel(paramAnonymousBoolean);
      }

      public O get()
        throws InterruptedException, ExecutionException
      {
        return apply(this.val$future.get());
      }

      public O get(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
        throws InterruptedException, ExecutionException, TimeoutException
      {
        return apply(this.val$future.get(paramAnonymousLong, paramAnonymousTimeUnit));
      }

      public boolean isCancelled()
      {
        return this.val$future.isCancelled();
      }

      public boolean isDone()
      {
        return this.val$future.isDone();
      }
    };
  }

  public static <T, E extends Exception> CheckedFuture<T, E> immediateCheckedFuture(@Nullable T paramT)
  {
    ValueFuture localValueFuture = ValueFuture.create();
    localValueFuture.set(paramT);
    return makeChecked(localValueFuture, new Function()
    {
      public E apply(Exception paramAnonymousException)
      {
        throw new AssertionError("impossible");
      }
    });
  }

  public static <T, E extends Exception> CheckedFuture<T, E> immediateFailedCheckedFuture(E paramE)
  {
    Preconditions.checkNotNull(paramE);
    return makeChecked(immediateFailedFuture(paramE), new Function()
    {
      public E apply(Exception paramAnonymousException)
      {
        return this.val$exception;
      }
    });
  }

  public static <T> ListenableFuture<T> immediateFailedFuture(Throwable paramThrowable)
  {
    Preconditions.checkNotNull(paramThrowable);
    ValueFuture localValueFuture = ValueFuture.create();
    localValueFuture.setException(paramThrowable);
    return localValueFuture;
  }

  public static <T> ListenableFuture<T> immediateFuture(@Nullable T paramT)
  {
    ValueFuture localValueFuture = ValueFuture.create();
    localValueFuture.set(paramT);
    return localValueFuture;
  }

  public static <T, E extends Exception> CheckedFuture<T, E> makeChecked(Future<T> paramFuture, Function<Exception, E> paramFunction)
  {
    return new MappingCheckedFuture(makeListenable(paramFuture), paramFunction);
  }

  public static <T> ListenableFuture<T> makeListenable(Future<T> paramFuture)
  {
    if ((paramFuture instanceof ListenableFuture))
      return (ListenableFuture)paramFuture;
    return new ListenableFutureAdapter(paramFuture);
  }

  public static <V> UninterruptibleFuture<V> makeUninterruptible(Future<V> paramFuture)
  {
    Preconditions.checkNotNull(paramFuture);
    if ((paramFuture instanceof UninterruptibleFuture))
      return (UninterruptibleFuture)paramFuture;
    return new UninterruptibleFuture()
    {
      public boolean cancel(boolean paramAnonymousBoolean)
      {
        return this.val$future.cancel(paramAnonymousBoolean);
      }

      // ERROR //
      public V get()
        throws ExecutionException
      {
        // Byte code:
        //   0: iconst_0
        //   1: istore_1
        //   2: aload_0
        //   3: getfield 22	com/google/common/util/concurrent/Futures$1:val$future	Ljava/util/concurrent/Future;
        //   6: invokeinterface 37 1 0
        //   11: astore 4
        //   13: iload_1
        //   14: ifeq +9 -> 23
        //   17: invokestatic 43	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   20: invokevirtual 46	java/lang/Thread:interrupt	()V
        //   23: aload 4
        //   25: areturn
        //   26: astore_3
        //   27: iconst_1
        //   28: istore_1
        //   29: goto -27 -> 2
        //   32: astore_2
        //   33: iload_1
        //   34: ifeq +9 -> 43
        //   37: invokestatic 43	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   40: invokevirtual 46	java/lang/Thread:interrupt	()V
        //   43: aload_2
        //   44: athrow
        //
        // Exception table:
        //   from	to	target	type
        //   2	13	26	java/lang/InterruptedException
        //   2	13	32	finally
      }

      public V get(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
        throws TimeoutException, ExecutionException
      {
        int i = 0;
        try
        {
          long l1 = paramAnonymousTimeUnit.toNanos(paramAnonymousLong);
          long l2 = System.nanoTime();
          long l3 = l2 + l1;
          long l4 = l1;
          while (l4 > 0L)
            try
            {
              Object localObject2 = this.val$future.get(l4, TimeUnit.NANOSECONDS);
              return localObject2;
            }
            catch (InterruptedException localInterruptedException)
            {
              i = 1;
              l4 = l3 - System.nanoTime();
            }
          throw new TimeoutException();
        }
        finally
        {
          if (i != 0)
            Thread.currentThread().interrupt();
        }
      }

      public boolean isCancelled()
      {
        return this.val$future.isCancelled();
      }

      public boolean isDone()
      {
        return this.val$future.isDone();
      }
    };
  }

  private static class ChainingListenableFuture<I, O> extends AbstractListenableFuture<O>
    implements Runnable
  {
    private final Function<? super I, ? extends ListenableFuture<? extends O>> function;
    private final UninterruptibleFuture<? extends I> inputFuture;

    private ChainingListenableFuture(Function<? super I, ? extends ListenableFuture<? extends O>> paramFunction, ListenableFuture<? extends I> paramListenableFuture)
    {
      this.function = paramFunction;
      this.inputFuture = Futures.makeUninterruptible(paramListenableFuture);
    }

    public void run()
    {
      try
      {
        Object localObject = this.inputFuture.get();
        final ListenableFuture localListenableFuture = (ListenableFuture)this.function.apply(localObject);
        localListenableFuture.addListener(new Runnable()
        {
          public void run()
          {
            try
            {
              Futures.ChainingListenableFuture.this.set(Futures.makeUninterruptible(localListenableFuture).get());
              return;
            }
            catch (ExecutionException localExecutionException)
            {
              Futures.ChainingListenableFuture.this.setException(localExecutionException.getCause());
            }
          }
        }
        , Executors.sameThreadExecutor());
        return;
      }
      catch (CancellationException localCancellationException)
      {
        cancel();
        return;
      }
      catch (UndeclaredThrowableException localUndeclaredThrowableException)
      {
        setException(localUndeclaredThrowableException.getCause());
        return;
      }
      catch (ExecutionException localExecutionException)
      {
        setException(localExecutionException.getCause());
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        setException(localRuntimeException);
        return;
      }
      catch (Error localError)
      {
        setException(localError);
        throw localError;
      }
    }
  }

  private static class ListenableFutureAdapter<T> extends ForwardingFuture<T>
    implements ListenableFuture<T>
  {
    private static final Executor adapterExecutor = java.util.concurrent.Executors.newCachedThreadPool();
    private final Future<T> delegate;
    private final ExecutionList executionList = new ExecutionList();
    private final AtomicBoolean hasListeners = new AtomicBoolean(false);

    ListenableFutureAdapter(Future<T> paramFuture)
    {
      this.delegate = paramFuture;
    }

    public void addListener(Runnable paramRunnable, Executor paramExecutor)
    {
      if ((!this.hasListeners.get()) && (this.hasListeners.compareAndSet(false, true)))
        adapterExecutor.execute(new Runnable()
        {
          public void run()
          {
            try
            {
              Futures.ListenableFutureAdapter.this.delegate.get();
              label13: Futures.ListenableFutureAdapter.this.executionList.run();
              return;
            }
            catch (InterruptedException localInterruptedException)
            {
              throw new IllegalStateException("Adapter thread interrupted!", localInterruptedException);
            }
            catch (ExecutionException localExecutionException)
            {
              break label13;
            }
            catch (CancellationException localCancellationException)
            {
              break label13;
            }
          }
        });
      this.executionList.add(paramRunnable, paramExecutor);
    }

    protected Future<T> delegate()
    {
      return this.delegate;
    }
  }

  private static class MappingCheckedFuture<T, E extends Exception> extends AbstractCheckedFuture<T, E>
  {
    final Function<Exception, E> mapper;

    MappingCheckedFuture(ListenableFuture<T> paramListenableFuture, Function<Exception, E> paramFunction)
    {
      super();
      this.mapper = paramFunction;
    }

    protected E mapException(Exception paramException)
    {
      return (Exception)this.mapper.apply(paramException);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.Futures
 * JD-Core Version:    0.6.2
 */