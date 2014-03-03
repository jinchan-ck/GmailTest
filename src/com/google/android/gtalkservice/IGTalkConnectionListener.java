package com.google.android.gtalkservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IGTalkConnectionListener extends IInterface
{
  public abstract void onConnectionCreated(IGTalkConnection paramIGTalkConnection)
    throws RemoteException;

  public abstract void onConnectionCreationFailed(String paramString)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IGTalkConnectionListener
  {
    private static final String DESCRIPTOR = "com.google.android.gtalkservice.IGTalkConnectionListener";
    static final int TRANSACTION_onConnectionCreated = 1;
    static final int TRANSACTION_onConnectionCreationFailed = 2;

    public Stub()
    {
      attachInterface(this, "com.google.android.gtalkservice.IGTalkConnectionListener");
    }

    public static IGTalkConnectionListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gtalkservice.IGTalkConnectionListener");
      if ((localIInterface != null) && ((localIInterface instanceof IGTalkConnectionListener)))
        return (IGTalkConnectionListener)localIInterface;
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
        paramParcel2.writeString("com.google.android.gtalkservice.IGTalkConnectionListener");
        return true;
      case 1:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnectionListener");
        onConnectionCreated(IGTalkConnection.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 2:
      }
      paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnectionListener");
      onConnectionCreationFailed(paramParcel1.readString());
      paramParcel2.writeNoException();
      return true;
    }

    private static class Proxy
      implements IGTalkConnectionListener
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

      public String getInterfaceDescriptor()
      {
        return "com.google.android.gtalkservice.IGTalkConnectionListener";
      }

      public void onConnectionCreated(IGTalkConnection paramIGTalkConnection)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnectionListener");
          if (paramIGTalkConnection != null);
          for (IBinder localIBinder = paramIGTalkConnection.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(1, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void onConnectionCreationFailed(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnectionListener");
          localParcel1.writeString(paramString);
          this.mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gtalkservice.IGTalkConnectionListener
 * JD-Core Version:    0.6.2
 */