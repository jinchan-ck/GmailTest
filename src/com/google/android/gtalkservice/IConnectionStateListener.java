package com.google.android.gtalkservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IConnectionStateListener extends IInterface
{
  public abstract void connectionStateChanged(ConnectionState paramConnectionState, ConnectionError paramConnectionError)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IConnectionStateListener
  {
    private static final String DESCRIPTOR = "com.google.android.gtalkservice.IConnectionStateListener";
    static final int TRANSACTION_connectionStateChanged = 1;

    public Stub()
    {
      attachInterface(this, "com.google.android.gtalkservice.IConnectionStateListener");
    }

    public static IConnectionStateListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gtalkservice.IConnectionStateListener");
      if ((localIInterface != null) && ((localIInterface instanceof IConnectionStateListener)))
        return (IConnectionStateListener)localIInterface;
      return new Proxy(paramIBinder);
    }

    public IBinder asBinder()
    {
      return this;
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      switch (paramInt1)
      {
      default:
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
        paramParcel2.writeString("com.google.android.gtalkservice.IConnectionStateListener");
        return true;
      case 1:
      }
      paramParcel1.enforceInterface("com.google.android.gtalkservice.IConnectionStateListener");
      ConnectionState localConnectionState;
      if (paramParcel1.readInt() != 0)
      {
        localConnectionState = (ConnectionState)ConnectionState.CREATOR.createFromParcel(paramParcel1);
        if (paramParcel1.readInt() == 0)
          break label114;
      }
      label114: for (ConnectionError localConnectionError = (ConnectionError)ConnectionError.CREATOR.createFromParcel(paramParcel1); ; localConnectionError = null)
      {
        connectionStateChanged(localConnectionState, localConnectionError);
        paramParcel2.writeNoException();
        return true;
        localConnectionState = null;
        break;
      }
    }

    private static class Proxy
      implements IConnectionStateListener
    {
      private IBinder mRemote;

      Proxy(IBinder paramIBinder)
      {
        this.mRemote = paramIBinder;
      }

      public IBinder asBinder()
      {
        return this.mRemote;
      }

      public void connectionStateChanged(ConnectionState paramConnectionState, ConnectionError paramConnectionError)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IConnectionStateListener");
            if (paramConnectionState != null)
            {
              localParcel1.writeInt(1);
              paramConnectionState.writeToParcel(localParcel1, 0);
              if (paramConnectionError != null)
              {
                localParcel1.writeInt(1);
                paramConnectionError.writeToParcel(localParcel1, 0);
                this.mRemote.transact(1, localParcel1, localParcel2, 0);
                localParcel2.readException();
              }
            }
            else
            {
              localParcel1.writeInt(0);
              continue;
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          localParcel1.writeInt(0);
        }
      }

      public String getInterfaceDescriptor()
      {
        return "com.google.android.gtalkservice.IConnectionStateListener";
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gtalkservice.IConnectionStateListener
 * JD-Core Version:    0.6.2
 */