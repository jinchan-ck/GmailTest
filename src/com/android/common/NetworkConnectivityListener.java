package com.android.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class NetworkConnectivityListener
{
  private static final boolean DBG = false;
  private static final String TAG = "NetworkConnectivityListener";
  private Context mContext;
  private HashMap<Handler, Integer> mHandlers = new HashMap();
  private boolean mIsFailover;
  private boolean mListening;
  private NetworkInfo mNetworkInfo;
  private NetworkInfo mOtherNetworkInfo;
  private String mReason;
  private ConnectivityBroadcastReceiver mReceiver = new ConnectivityBroadcastReceiver(null);
  private State mState = State.UNKNOWN;

  public NetworkInfo getNetworkInfo()
  {
    return this.mNetworkInfo;
  }

  public NetworkInfo getOtherNetworkInfo()
  {
    return this.mOtherNetworkInfo;
  }

  public String getReason()
  {
    return this.mReason;
  }

  public State getState()
  {
    return this.mState;
  }

  public boolean isFailover()
  {
    return this.mIsFailover;
  }

  public void registerHandler(Handler paramHandler, int paramInt)
  {
    this.mHandlers.put(paramHandler, Integer.valueOf(paramInt));
  }

  public void startListening(Context paramContext)
  {
    try
    {
      if (!this.mListening)
      {
        this.mContext = paramContext;
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        paramContext.registerReceiver(this.mReceiver, localIntentFilter);
        this.mListening = true;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void stopListening()
  {
    try
    {
      if (this.mListening)
      {
        this.mContext.unregisterReceiver(this.mReceiver);
        this.mContext = null;
        this.mNetworkInfo = null;
        this.mOtherNetworkInfo = null;
        this.mIsFailover = false;
        this.mReason = null;
        this.mListening = false;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void unregisterHandler(Handler paramHandler)
  {
    this.mHandlers.remove(paramHandler);
  }

  private class ConnectivityBroadcastReceiver extends BroadcastReceiver
  {
    private ConnectivityBroadcastReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ((!paramIntent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) || (!NetworkConnectivityListener.this.mListening))
      {
        Log.w("NetworkConnectivityListener", "onReceived() called with " + NetworkConnectivityListener.this.mState.toString() + " and " + paramIntent);
        return;
      }
      if (paramIntent.getBooleanExtra("noConnectivity", false))
        NetworkConnectivityListener.access$102(NetworkConnectivityListener.this, NetworkConnectivityListener.State.NOT_CONNECTED);
      while (true)
      {
        NetworkConnectivityListener.access$202(NetworkConnectivityListener.this, (NetworkInfo)paramIntent.getParcelableExtra("networkInfo"));
        NetworkConnectivityListener.access$302(NetworkConnectivityListener.this, (NetworkInfo)paramIntent.getParcelableExtra("otherNetwork"));
        NetworkConnectivityListener.access$402(NetworkConnectivityListener.this, paramIntent.getStringExtra("reason"));
        NetworkConnectivityListener.access$502(NetworkConnectivityListener.this, paramIntent.getBooleanExtra("isFailover", false));
        Iterator localIterator = NetworkConnectivityListener.this.mHandlers.keySet().iterator();
        while (localIterator.hasNext())
        {
          Handler localHandler = (Handler)localIterator.next();
          localHandler.sendMessage(Message.obtain(localHandler, ((Integer)NetworkConnectivityListener.this.mHandlers.get(localHandler)).intValue()));
        }
        break;
        NetworkConnectivityListener.access$102(NetworkConnectivityListener.this, NetworkConnectivityListener.State.CONNECTED);
      }
    }
  }

  public static enum State
  {
    static
    {
      CONNECTED = new State("CONNECTED", 1);
      NOT_CONNECTED = new State("NOT_CONNECTED", 2);
      State[] arrayOfState = new State[3];
      arrayOfState[0] = UNKNOWN;
      arrayOfState[1] = CONNECTED;
      arrayOfState[2] = NOT_CONNECTED;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.android.common.NetworkConnectivityListener
 * JD-Core Version:    0.6.2
 */