package com.google.android.gtalkservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IChatListener extends IInterface
{
  public abstract void chatClosed(String paramString)
    throws RemoteException;

  public abstract void chatRead(String paramString)
    throws RemoteException;

  public abstract void convertedToGroupChat(String paramString1, String paramString2, long paramLong)
    throws RemoteException;

  public abstract void newMessageReceived(String paramString1, String paramString2)
    throws RemoteException;

  public abstract void newMessageSent(String paramString)
    throws RemoteException;

  public abstract void participantJoined(String paramString1, String paramString2)
    throws RemoteException;

  public abstract void participantLeft(String paramString1, String paramString2)
    throws RemoteException;

  public abstract boolean useLightweightNotify()
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IChatListener
  {
    private static final String DESCRIPTOR = "com.google.android.gtalkservice.IChatListener";
    static final int TRANSACTION_chatClosed = 4;
    static final int TRANSACTION_chatRead = 3;
    static final int TRANSACTION_convertedToGroupChat = 5;
    static final int TRANSACTION_newMessageReceived = 1;
    static final int TRANSACTION_newMessageSent = 2;
    static final int TRANSACTION_participantJoined = 6;
    static final int TRANSACTION_participantLeft = 7;
    static final int TRANSACTION_useLightweightNotify = 8;

    public Stub()
    {
      attachInterface(this, "com.google.android.gtalkservice.IChatListener");
    }

    public static IChatListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gtalkservice.IChatListener");
      if ((localIInterface != null) && ((localIInterface instanceof IChatListener)))
        return (IChatListener)localIInterface;
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
        paramParcel2.writeString("com.google.android.gtalkservice.IChatListener");
        return true;
      case 1:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatListener");
        newMessageReceived(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 2:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatListener");
        newMessageSent(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 3:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatListener");
        chatRead(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 4:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatListener");
        chatClosed(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 5:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatListener");
        convertedToGroupChat(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readLong());
        paramParcel2.writeNoException();
        return true;
      case 6:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatListener");
        participantJoined(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 7:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatListener");
        participantLeft(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 8:
      }
      paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatListener");
      boolean bool = useLightweightNotify();
      paramParcel2.writeNoException();
      if (bool);
      for (int i = 1; ; i = 0)
      {
        paramParcel2.writeInt(i);
        return true;
      }
    }

    private static class Proxy
      implements IChatListener
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

      public void chatClosed(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatListener");
          localParcel1.writeString(paramString);
          this.mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void chatRead(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatListener");
          localParcel1.writeString(paramString);
          this.mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void convertedToGroupChat(String paramString1, String paramString2, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatListener");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeLong(paramLong);
          this.mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getInterfaceDescriptor()
      {
        return "com.google.android.gtalkservice.IChatListener";
      }

      public void newMessageReceived(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatListener");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          this.mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void newMessageSent(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatListener");
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

      public void participantJoined(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatListener");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          this.mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void participantLeft(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatListener");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          this.mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean useLightweightNotify()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatListener");
          this.mRemote.transact(8, localParcel1, localParcel2, 0);
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
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gtalkservice.IChatListener
 * JD-Core Version:    0.6.2
 */