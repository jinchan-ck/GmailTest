package com.google.android.gm;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

public class ReplyInstrumentation extends Instrumentation
{
  private void pause()
  {
    try
    {
      Thread.currentThread();
      Thread.sleep(5000L);
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      finish(0, null);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    start();
  }

  public void onStart()
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.setClass(getTargetContext(), ConversationListActivity.class);
    localIntent.addFlags(268435456);
    startActivitySync(localIntent);
    sendCharacterSync(20);
    sendCharacterSync(23);
    pause();
    sendCharacterSync(20);
    sendCharacterSync(20);
    sendCharacterSync(20);
    sendCharacterSync(21);
    new Bundle();
    Bundle localBundle = new Bundle();
    Log.d("Gmail", "START PROFILING");
    Debug.startMethodTracing("/data/data/com.google.android.gm/gmail-profiling", 8388608);
    sendCharacterSync(23);
    waitForIdleSync();
    Debug.stopMethodTracing();
    finish(-1, localBundle);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ReplyInstrumentation
 * JD-Core Version:    0.6.2
 */