package com.google.common.util.concurrent;

import com.google.common.base.Service;
import com.google.common.base.Service.State;
import com.google.common.collect.ForwardingObject;
import java.util.concurrent.Future;

public abstract class ForwardingService extends ForwardingObject
  implements Service
{
  protected abstract Service delegate();

  public boolean isRunning()
  {
    return delegate().isRunning();
  }

  public Future<Service.State> start()
  {
    return delegate().start();
  }

  public Service.State startAndWait()
  {
    return delegate().startAndWait();
  }

  public Service.State state()
  {
    return delegate().state();
  }

  public Future<Service.State> stop()
  {
    return delegate().stop();
  }

  public Service.State stopAndWait()
  {
    return delegate().stopAndWait();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ForwardingService
 * JD-Core Version:    0.6.2
 */