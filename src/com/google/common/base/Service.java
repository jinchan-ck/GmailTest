package com.google.common.base;

import java.util.concurrent.Future;

public abstract interface Service
{
  public abstract boolean isRunning();

  public abstract Future<State> start();

  public abstract State startAndWait();

  public abstract State state();

  public abstract Future<State> stop();

  public abstract State stopAndWait();

  public static enum State
  {
    static
    {
      RUNNING = new State("RUNNING", 2);
      STOPPING = new State("STOPPING", 3);
      TERMINATED = new State("TERMINATED", 4);
      FAILED = new State("FAILED", 5);
      State[] arrayOfState = new State[6];
      arrayOfState[0] = NEW;
      arrayOfState[1] = STARTING;
      arrayOfState[2] = RUNNING;
      arrayOfState[3] = STOPPING;
      arrayOfState[4] = TERMINATED;
      arrayOfState[5] = FAILED;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Service
 * JD-Core Version:    0.6.2
 */