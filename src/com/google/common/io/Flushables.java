package com.google.common.io;

import java.io.Flushable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Flushables
{
  private static final Logger logger = Logger.getLogger(Flushables.class.getName());

  public static void flush(Flushable paramFlushable, boolean paramBoolean)
    throws IOException
  {
    try
    {
      paramFlushable.flush();
      return;
    }
    catch (IOException localIOException)
    {
      do
        logger.log(Level.WARNING, "IOException thrown while flushing Flushable.", localIOException);
      while (paramBoolean);
      throw localIOException;
    }
  }

  public static void flushQuietly(Flushable paramFlushable)
  {
    try
    {
      flush(paramFlushable, true);
      return;
    }
    catch (IOException localIOException)
    {
      logger.log(Level.SEVERE, "IOException should not have been thrown.", localIOException);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.Flushables
 * JD-Core Version:    0.6.2
 */