package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Queue;

@GwtCompatible
public abstract class ForwardingQueue<E> extends ForwardingCollection<E>
  implements Queue<E>
{
  protected abstract Queue<E> delegate();

  public E element()
  {
    return delegate().element();
  }

  public boolean offer(E paramE)
  {
    return delegate().offer(paramE);
  }

  public E peek()
  {
    return delegate().peek();
  }

  public E poll()
  {
    return delegate().poll();
  }

  public E remove()
  {
    return delegate().remove();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingQueue
 * JD-Core Version:    0.6.2
 */