package com.google.android.gm;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;

public class GmailProfilingActivity extends Instrumentation
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    start();
  }

  public void onStart()
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.setClass(getTargetContext(), ConversationListActivity.class);
    startActivitySync(localIntent);
    try
    {
      Thread.currentThread();
      Thread.sleep(5000L);
      startPerformanceSnapshot();
      startProfiling();
      sendCharacterSync(23);
    }
    catch (InterruptedException localInterruptedException1)
    {
      try
      {
        Thread.currentThread();
        Thread.sleep(5000L);
        stopProfiling();
        endPerformanceSnapshot();
        finish(-1, new Bundle());
        return;
        localInterruptedException1 = localInterruptedException1;
        finish(0, null);
      }
      catch (InterruptedException localInterruptedException2)
      {
        while (true)
          finish(0, null);
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.GmailProfilingActivity
 * JD-Core Version:    0.6.2
 */