package com.android.mail.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import com.google.common.collect.Lists;
import java.util.Deque;
import java.util.Iterator;

public class InputSmoother
{
  private final float mDensity;
  private final Deque<Sample> mRecentSamples = Lists.newLinkedList();

  public InputSmoother(Context paramContext)
  {
    this.mDensity = paramContext.getResources().getDisplayMetrics().density;
  }

  public Float getSmoothedVelocity()
  {
    if (this.mRecentSamples.size() < 2)
      return null;
    int i = 0;
    int j = ((Sample)this.mRecentSamples.getFirst()).pos;
    long l = ((Sample)this.mRecentSamples.getLast()).millis - ((Sample)this.mRecentSamples.getFirst()).millis;
    if (l <= 0L)
      return null;
    Iterator localIterator = this.mRecentSamples.iterator();
    while (localIterator.hasNext())
    {
      Sample localSample = (Sample)localIterator.next();
      i += Math.abs(localSample.pos - j);
      j = localSample.pos;
    }
    return Float.valueOf(1000.0F * (i / this.mDensity) / (float)l);
  }

  public void onInput(int paramInt)
  {
    long l = SystemClock.uptimeMillis();
    Sample localSample1 = (Sample)this.mRecentSamples.peekLast();
    if ((localSample1 != null) && (l - localSample1.millis > 200L))
      this.mRecentSamples.clear();
    if (this.mRecentSamples.size() == 5);
    for (Sample localSample2 = (Sample)this.mRecentSamples.removeFirst(); ; localSample2 = new Sample(null))
    {
      localSample2.pos = paramInt;
      localSample2.millis = l;
      this.mRecentSamples.add(localSample2);
      return;
    }
  }

  private static class Sample
  {
    long millis;
    int pos;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.InputSmoother
 * JD-Core Version:    0.6.2
 */