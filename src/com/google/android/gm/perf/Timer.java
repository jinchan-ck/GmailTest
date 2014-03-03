package com.google.android.gm.perf;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Timer
{
  private static Map<String, Timer.PerformancePoint> sPerformanceCollector = new ConcurrentHashMap();
  private final Map<String, Integer> mCounts = Maps.newHashMap();
  private final boolean mEnabled;
  private final Map<String, ArrayList<Timer.PerformancePoint>> mPoints = Maps.newHashMap();

  public Timer()
  {
    this(false);
  }

  public Timer(boolean paramBoolean)
  {
    this.mEnabled = paramBoolean;
  }

  public static void startTiming(String paramString)
  {
  }

  public static void stopTiming(String paramString)
  {
  }

  public static void stopTiming(String paramString, int paramInt)
  {
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.perf.Timer
 * JD-Core Version:    0.6.2
 */