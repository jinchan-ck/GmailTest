package com.google.android.gtalkservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IGTalkService extends IInterface
{
  public abstract void createGTalkConnection(String paramString, IGTalkConnectionListener paramIGTalkConnectionListener)
    throws RemoteException;

  public abstract void dismissNotificationFor(String paramString, long paramLong)
    throws RemoteException;

  public abstract void dismissNotifications(long paramLong)
    throws RemoteException;

  public abstract List getActiveConnections()
    throws RemoteException;

  public abstract IGTalkConnection getConnectionForUser(String paramString)
    throws RemoteException;

  public abstract IGTalkConnection getDefaultConnection()
    throws RemoteException;

  public abstract IImSession getImSessionForAccountId(long paramLong)
    throws RemoteException;

  public abstract String printDiagnostics()
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IGTalkService
  {
    private static final String DESCRIPTOR = "com.google.android.gtalkservice.IGTalkService";
    static final int TRANSACTION_createGTalkConnection = 1;
    static final int TRANSACTION_dismissNotificationFor = 7;
    static final int TRANSACTION_dismissNotifications = 6;
    static final int TRANSACTION_getActiveConnections = 2;
    static final int TRANSACTION_getConnectionForUser = 3;
    static final int TRANSACTION_getDefaultConnection = 4;
    static final int TRANSACTION_getImSessionForAccountId = 5;
    static final int TRANSACTION_printDiagnostics = 8;

    public Stub()
    {
      attachInterface(this, "com.google.android.gtalkservice.IGTalkService");
    }

    public static IGTalkService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gtalkservice.IGTalkService");
      if ((localIInterface != null) && ((localIInterface instanceof IGTalkService)))
        return (IGTalkService)localIInterface;
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
        paramParcel2.writeString("com.google.android.gtalkservice.IGTalkService");
        return true;
      case 1:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkService");
        createGTalkConnection(paramParcel1.readString(), IGTalkConnectionListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      case 2:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkService");
        List localList = getActiveConnections();
        paramParcel2.writeNoException();
        paramParcel2.writeList(localList);
        return true;
      case 3:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkService");
        IGTalkConnection localIGTalkConnection2 = getConnectionForUser(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (localIGTalkConnection2 != null);
        for (IBinder localIBinder3 = localIGTalkConnection2.asBinder(); ; localIBinder3 = null)
        {
          paramParcel2.writeStrongBinder(localIBinder3);
          return true;
        }
      case 4:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkService");
        IGTalkConnection localIGTalkConnection1 = getDefaultConnection();
        paramParcel2.writeNoException();
        if (localIGTalkConnection1 != null);
        for (IBinder localIBinder2 = localIGTalkConnection1.asBinder(); ; localIBinder2 = null)
        {
          paramParcel2.writeStrongBinder(localIBinder2);
          return true;
        }
      case 5:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkService");
        IImSession localIImSession = getImSessionForAccountId(paramParcel1.readLong());
        paramParcel2.writeNoException();
        if (localIImSession != null);
        for (IBinder localIBinder1 = localIImSession.asBinder(); ; localIBinder1 = null)
        {
          paramParcel2.writeStrongBinder(localIBinder1);
          return true;
        }
      case 6:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkService");
        dismissNotifications(paramParcel1.readLong());
        return true;
      case 7:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkService");
        dismissNotificationFor(paramParcel1.readString(), paramParcel1.readLong());
        return true;
      case 8:
      }
      paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkService");
      String str = printDiagnostics();
      paramParcel2.writeNoException();
      paramParcel2.writeString(str);
      return true;
    }

    private static class Proxy
      implements IGTalkService
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

      public void createGTalkConnection(String paramString, IGTalkConnectionListener paramIGTalkConnectionListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IGTalkService");
          localParcel.writeString(paramString);
          if (paramIGTalkConnectionListener != null);
          for (IBinder localIBinder = paramIGTalkConnectionListener.asBinder(); ; localIBinder = null)
          {
            localParcel.writeStrongBinder(localIBinder);
            this.mRemote.transact(1, localParcel, null, 1);
            return;
          }
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void dismissNotificationFor(String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IGTalkService");
          localParcel.writeString(paramString);
          localParcel.writeLong(paramLong);
          this.mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void dismissNotifications(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IGTalkService");
          localParcel.writeLong(paramLong);
          this.mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public List getActiveConnections()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkService");
          this.mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.readArrayList(getClass().getClassLoader());
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public IGTalkConnection getConnectionForUser(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkService");
          localParcel1.writeString(paramString);
          this.mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IGTalkConnection localIGTalkConnection = IGTalkConnection.Stub.asInterface(localParcel2.readStrongBinder());
          return localIGTalkConnection;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public IGTalkConnection getDefaultConnection()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkService");
          this.mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IGTalkConnection localIGTalkConnection = IGTalkConnection.Stub.asInterface(localParcel2.readStrongBinder());
          return localIGTalkConnection;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public IImSession getImSessionForAccountId(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkService");
          localParcel1.writeLong(paramLong);
          this.mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImSession localIImSession = IImSession.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImSession;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getInterfaceDescriptor()
      {
        return "com.google.android.gtalkservice.IGTalkService";
      }

      public String printDiagnostics()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkService");
          this.mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
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
 * Qualified Name:     com.google.android.gtalkservice.IGTalkService
 * JD-Core Version:    0.6.2
 */