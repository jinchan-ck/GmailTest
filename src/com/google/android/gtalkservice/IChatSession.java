package com.google.android.gtalkservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IChatSession extends IInterface
{
  public abstract void addRemoteChatListener(IChatListener paramIChatListener)
    throws RemoteException;

  public abstract String[] getParticipants()
    throws RemoteException;

  public abstract String getUnsentComposedMessage()
    throws RemoteException;

  public abstract void inviteContact(String paramString)
    throws RemoteException;

  public abstract boolean isGroupChat()
    throws RemoteException;

  public abstract boolean isOffTheRecord()
    throws RemoteException;

  public abstract void leave()
    throws RemoteException;

  public abstract void markAsRead()
    throws RemoteException;

  public abstract void removeRemoteChatListener(IChatListener paramIChatListener)
    throws RemoteException;

  public abstract void saveUnsentComposedMessage(String paramString)
    throws RemoteException;

  public abstract void sendChatMessage(String paramString)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IChatSession
  {
    private static final String DESCRIPTOR = "com.google.android.gtalkservice.IChatSession";
    static final int TRANSACTION_addRemoteChatListener = 9;
    static final int TRANSACTION_getParticipants = 3;
    static final int TRANSACTION_getUnsentComposedMessage = 8;
    static final int TRANSACTION_inviteContact = 4;
    static final int TRANSACTION_isGroupChat = 1;
    static final int TRANSACTION_isOffTheRecord = 11;
    static final int TRANSACTION_leave = 5;
    static final int TRANSACTION_markAsRead = 2;
    static final int TRANSACTION_removeRemoteChatListener = 10;
    static final int TRANSACTION_saveUnsentComposedMessage = 7;
    static final int TRANSACTION_sendChatMessage = 6;

    public Stub()
    {
      attachInterface(this, "com.google.android.gtalkservice.IChatSession");
    }

    public static IChatSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gtalkservice.IChatSession");
      if ((localIInterface != null) && ((localIInterface instanceof IChatSession)))
        return (IChatSession)localIInterface;
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
        paramParcel2.writeString("com.google.android.gtalkservice.IChatSession");
        return true;
      case 1:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        boolean bool2 = isGroupChat();
        paramParcel2.writeNoException();
        if (bool2);
        for (int j = 1; ; j = 0)
        {
          paramParcel2.writeInt(j);
          return true;
        }
      case 2:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        markAsRead();
        return true;
      case 3:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        String[] arrayOfString = getParticipants();
        paramParcel2.writeNoException();
        paramParcel2.writeStringArray(arrayOfString);
        return true;
      case 4:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        inviteContact(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 5:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        leave();
        paramParcel2.writeNoException();
        return true;
      case 6:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        sendChatMessage(paramParcel1.readString());
        return true;
      case 7:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        saveUnsentComposedMessage(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 8:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        String str = getUnsentComposedMessage();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str);
        return true;
      case 9:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        addRemoteChatListener(IChatListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 10:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
        removeRemoteChatListener(IChatListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 11:
      }
      paramParcel1.enforceInterface("com.google.android.gtalkservice.IChatSession");
      boolean bool1 = isOffTheRecord();
      paramParcel2.writeNoException();
      if (bool1);
      for (int i = 1; ; i = 0)
      {
        paramParcel2.writeInt(i);
        return true;
      }
    }

    private static class Proxy
      implements IChatSession
    {
      private IBinder mRemote;

      Proxy(IBinder paramIBinder)
      {
        this.mRemote = paramIBinder;
      }

      public void addRemoteChatListener(IChatListener paramIChatListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
          if (paramIChatListener != null);
          for (IBinder localIBinder = paramIChatListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(9, localParcel1, localParcel2, 0);
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

      public IBinder asBinder()
      {
        return this.mRemote;
      }

      public String getInterfaceDescriptor()
      {
        return "com.google.android.gtalkservice.IChatSession";
      }

      public String[] getParticipants()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
          this.mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getUnsentComposedMessage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
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

      public void inviteContact(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
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

      public boolean isGroupChat()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
          this.mRemote.transact(1, localParcel1, localParcel2, 0);
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

      public boolean isOffTheRecord()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
          this.mRemote.transact(11, localParcel1, localParcel2, 0);
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

      public void leave()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
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

      public void markAsRead()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
          this.mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void removeRemoteChatListener(IChatListener paramIChatListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
          if (paramIChatListener != null);
          for (IBinder localIBinder = paramIChatListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(10, localParcel1, localParcel2, 0);
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

      public void saveUnsentComposedMessage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
          localParcel1.writeString(paramString);
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

      public void sendChatMessage(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IChatSession");
          localParcel.writeString(paramString);
          this.mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gtalkservice.IChatSession
 * JD-Core Version:    0.6.2
 */