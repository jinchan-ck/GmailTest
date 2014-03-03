package com.android.mail.perf;

import com.android.mail.utils.LogTag;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Timer
{
  private static final String LOG_TAG = LogTag.getLogTag();
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

  public void dumpResults()
  {
  }

  public void pause(String paramString)
  {
  }

  public void start(String paramString)
  {
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.perf.Timer
 * JD-Core Version:    0.6.2
 */