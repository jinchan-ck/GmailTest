package com.google.android.gms.panorama.internal;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPanoramaService extends IInterface
{
  public abstract void loadPanoramaInfo(IPanoramaCallbacks paramIPanoramaCallbacks, Uri paramUri, Bundle paramBundle, boolean paramBoolean)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IPanoramaService
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.gms.panorama.internal.IPanoramaService");
    }

    public static IPanoramaService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gms.panorama.internal.IPanoramaService");
      if ((localIInterface != null) && ((localIInterface instanceof IPanoramaService)))
        return (IPanoramaService)localIInterface;
      return new Proxy(paramIBinder);
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      switch (paramInt1)
      {
      default:
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
        paramParcel2.writeString("com.google.android.gms.panorama.internal.IPanoramaService");
        return true;
      case 1:
      }
      paramParcel1.enforceInterface("com.google.android.gms.panorama.internal.IPanoramaService");
      IPanoramaCallbacks localIPanoramaCallbacks = IPanoramaCallbacks.Stub.asInterface(paramParcel1.readStrongBinder());
      Uri localUri;
      Bundle localBundle;
      if (paramParcel1.readInt() != 0)
      {
        localUri = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
        if (paramParcel1.readInt() == 0)
          break label133;
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        label103: if (paramParcel1.readInt() == 0)
          break label139;
      }
      label133: label139: for (boolean bool = true; ; bool = false)
      {
        loadPanoramaInfo(localIPanoramaCallbacks, localUri, localBundle, bool);
        return true;
        localUri = null;
        break;
        localBundle = null;
        break label103;
      }
    }

    private static class Proxy
      implements IPanoramaService
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

      public void loadPanoramaInfo(IPanoramaCallbacks paramIPanoramaCallbacks, Uri paramUri, Bundle paramBundle, boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel.writeInterfaceToken("com.google.android.gms.panorama.internal.IPanoramaService");
            IBinder localIBinder = null;
            if (paramIPanoramaCallbacks != null)
              localIBinder = paramIPanoramaCallbacks.asBinder();
            localParcel.writeStrongBinder(localIBinder);
            if (paramUri != null)
            {
              localParcel.writeInt(1);
              paramUri.writeToParcel(localParcel, 0);
              if (paramBundle != null)
              {
                localParcel.writeInt(1);
                paramBundle.writeToParcel(localParcel, 0);
                break label136;
                localParcel.writeInt(i);
                this.mRemote.transact(1, localParcel, null, 1);
              }
            }
            else
            {
              localParcel.writeInt(0);
              continue;
            }
          }
          finally
          {
            localParcel.recycle();
          }
          localParcel.writeInt(0);
          label136: 
          while (!paramBoolean)
          {
            i = 0;
            break;
          }
        }
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gms.panorama.internal.IPanoramaService
 * JD-Core Version:    0.6.2
 */