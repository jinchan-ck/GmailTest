package com.google.android.gtalkservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IGTalkConnection extends IInterface
{
  public abstract void clearConnectionStatistics()
    throws RemoteException;

  public abstract IImSession createImSessionForProvider(String paramString)
    throws RemoteException;

  public abstract IImSession createImSessionForProviderId(long paramLong)
    throws RemoteException;

  public abstract int getConnectionUptime()
    throws RemoteException;

  public abstract IImSession getDefaultImSession()
    throws RemoteException;

  public abstract String getDeviceId()
    throws RemoteException;

  public abstract IImSession getImSessionForAccountId(long paramLong)
    throws RemoteException;

  public abstract String getJid()
    throws RemoteException;

  public abstract long getLastActivityFromServerTime()
    throws RemoteException;

  public abstract long getLastActivityToServerTime()
    throws RemoteException;

  public abstract int getNumberOfConnectionsAttempted()
    throws RemoteException;

  public abstract int getNumberOfConnectionsMade()
    throws RemoteException;

  public abstract String getUsername()
    throws RemoteException;

  public abstract boolean isConnected()
    throws RemoteException;

  public abstract void sendHeartbeat()
    throws RemoteException;

  public abstract void sendHttpRequest(byte[] paramArrayOfByte, IHttpRequestCallback paramIHttpRequestCallback)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IGTalkConnection
  {
    private static final String DESCRIPTOR = "com.google.android.gtalkservice.IGTalkConnection";
    static final int TRANSACTION_clearConnectionStatistics = 14;
    static final int TRANSACTION_createImSessionForProvider = 5;
    static final int TRANSACTION_createImSessionForProviderId = 6;
    static final int TRANSACTION_getConnectionUptime = 13;
    static final int TRANSACTION_getDefaultImSession = 8;
    static final int TRANSACTION_getDeviceId = 3;
    static final int TRANSACTION_getImSessionForAccountId = 7;
    static final int TRANSACTION_getJid = 2;
    static final int TRANSACTION_getLastActivityFromServerTime = 9;
    static final int TRANSACTION_getLastActivityToServerTime = 10;
    static final int TRANSACTION_getNumberOfConnectionsAttempted = 12;
    static final int TRANSACTION_getNumberOfConnectionsMade = 11;
    static final int TRANSACTION_getUsername = 1;
    static final int TRANSACTION_isConnected = 4;
    static final int TRANSACTION_sendHeartbeat = 16;
    static final int TRANSACTION_sendHttpRequest = 15;

    public Stub()
    {
      attachInterface(this, "com.google.android.gtalkservice.IGTalkConnection");
    }

    public static IGTalkConnection asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gtalkservice.IGTalkConnection");
      if ((localIInterface != null) && ((localIInterface instanceof IGTalkConnection)))
        return (IGTalkConnection)localIInterface;
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
        paramParcel2.writeString("com.google.android.gtalkservice.IGTalkConnection");
        return true;
      case 1:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        String str3 = getUsername();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str3);
        return true;
      case 2:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        String str2 = getJid();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str2);
        return true;
      case 3:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        String str1 = getDeviceId();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str1);
        return true;
      case 4:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        boolean bool = isConnected();
        paramParcel2.writeNoException();
        if (bool);
        for (int m = 1; ; m = 0)
        {
          paramParcel2.writeInt(m);
          return true;
        }
      case 5:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        IImSession localIImSession4 = createImSessionForProvider(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (localIImSession4 != null);
        for (IBinder localIBinder4 = localIImSession4.asBinder(); ; localIBinder4 = null)
        {
          paramParcel2.writeStrongBinder(localIBinder4);
          return true;
        }
      case 6:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        IImSession localIImSession3 = createImSessionForProviderId(paramParcel1.readLong());
        paramParcel2.writeNoException();
        if (localIImSession3 != null);
        for (IBinder localIBinder3 = localIImSession3.asBinder(); ; localIBinder3 = null)
        {
          paramParcel2.writeStrongBinder(localIBinder3);
          return true;
        }
      case 7:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        IImSession localIImSession2 = getImSessionForAccountId(paramParcel1.readLong());
        paramParcel2.writeNoException();
        if (localIImSession2 != null);
        for (IBinder localIBinder2 = localIImSession2.asBinder(); ; localIBinder2 = null)
        {
          paramParcel2.writeStrongBinder(localIBinder2);
          return true;
        }
      case 8:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        IImSession localIImSession1 = getDefaultImSession();
        paramParcel2.writeNoException();
        if (localIImSession1 != null);
        for (IBinder localIBinder1 = localIImSession1.asBinder(); ; localIBinder1 = null)
        {
          paramParcel2.writeStrongBinder(localIBinder1);
          return true;
        }
      case 9:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        long l2 = getLastActivityFromServerTime();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l2);
        return true;
      case 10:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        long l1 = getLastActivityToServerTime();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l1);
        return true;
      case 11:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        int k = getNumberOfConnectionsMade();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(k);
        return true;
      case 12:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        int j = getNumberOfConnectionsAttempted();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(j);
        return true;
      case 13:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        int i = getConnectionUptime();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(i);
        return true;
      case 14:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        clearConnectionStatistics();
        return true;
      case 15:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
        sendHttpRequest(paramParcel1.createByteArray(), IHttpRequestCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 16:
      }
      paramParcel1.enforceInterface("com.google.android.gtalkservice.IGTalkConnection");
      sendHeartbeat();
      paramParcel2.writeNoException();
      return true;
    }

    private static class Proxy
      implements IGTalkConnection
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

      public void clearConnectionStatistics()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public IImSession createImSessionForProvider(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          localParcel1.writeString(paramString);
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

      public IImSession createImSessionForProviderId(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          localParcel1.writeLong(paramLong);
          this.mRemote.transact(6, localParcel1, localParcel2, 0);
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

      public int getConnectionUptime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public IImSession getDefaultImSession()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(8, localParcel1, localParcel2, 0);
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

      public String getDeviceId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(3, localParcel1, localParcel2, 0);
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

      public IImSession getImSessionForAccountId(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          localParcel1.writeLong(paramLong);
          this.mRemote.transact(7, localParcel1, localParcel2, 0);
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
        return "com.google.android.gtalkservice.IGTalkConnection";
      }

      public String getJid()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(2, localParcel1, localParcel2, 0);
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

      public long getLastActivityFromServerTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long getLastActivityToServerTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getNumberOfConnectionsAttempted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getNumberOfConnectionsMade()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getUsername()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(1, localParcel1, localParcel2, 0);
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

      public boolean isConnected()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
          {
            bool = true;
            return bool;
          }
          boolean bool = false;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void sendHeartbeat()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          this.mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void sendHttpRequest(byte[] paramArrayOfByte, IHttpRequestCallback paramIHttpRequestCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IGTalkConnection");
          localParcel1.writeByteArray(paramArrayOfByte);
          if (paramIHttpRequestCallback != null);
          for (IBinder localIBinder = paramIHttpRequestCallback.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(15, localParcel1, localParcel2, 0);
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
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gtalkservice.IGTalkConnection
 * JD-Core Version:    0.6.2
 */