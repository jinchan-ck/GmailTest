package com.google.android.gsf;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGoogleLoginService extends IInterface
{
  public abstract GoogleLoginCredentialsResult blockingGetCredentials(String paramString1, String paramString2, boolean paramBoolean)
    throws RemoteException;

  public abstract void deleteAllAccounts()
    throws RemoteException;

  public abstract void deleteOneAccount(String paramString)
    throws RemoteException;

  public abstract String getAccount(boolean paramBoolean)
    throws RemoteException;

  public abstract String[] getAccounts()
    throws RemoteException;

  public abstract long getAndroidId()
    throws RemoteException;

  public abstract String getPrimaryAccount()
    throws RemoteException;

  public abstract void invalidateAuthToken(String paramString)
    throws RemoteException;

  public abstract String peekCredentials(String paramString1, String paramString2)
    throws RemoteException;

  public abstract void saveAuthToken(String paramString1, String paramString2, String paramString3)
    throws RemoteException;

  public abstract void saveNewAccount(LoginData paramLoginData)
    throws RemoteException;

  public abstract void saveUsernameAndPassword(String paramString1, String paramString2, int paramInt)
    throws RemoteException;

  public abstract void tryNewAccount(LoginData paramLoginData)
    throws RemoteException;

  public abstract void updatePassword(LoginData paramLoginData)
    throws RemoteException;

  public abstract boolean verifyStoredPassword(String paramString1, String paramString2)
    throws RemoteException;

  public abstract int waitForAndroidId()
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IGoogleLoginService
  {
    private static final String DESCRIPTOR = "com.google.android.gsf.IGoogleLoginService";
    static final int TRANSACTION_blockingGetCredentials = 5;
    static final int TRANSACTION_deleteAllAccounts = 15;
    static final int TRANSACTION_deleteOneAccount = 14;
    static final int TRANSACTION_getAccount = 3;
    static final int TRANSACTION_getAccounts = 1;
    static final int TRANSACTION_getAndroidId = 7;
    static final int TRANSACTION_getPrimaryAccount = 2;
    static final int TRANSACTION_invalidateAuthToken = 6;
    static final int TRANSACTION_peekCredentials = 4;
    static final int TRANSACTION_saveAuthToken = 10;
    static final int TRANSACTION_saveNewAccount = 9;
    static final int TRANSACTION_saveUsernameAndPassword = 13;
    static final int TRANSACTION_tryNewAccount = 8;
    static final int TRANSACTION_updatePassword = 11;
    static final int TRANSACTION_verifyStoredPassword = 12;
    static final int TRANSACTION_waitForAndroidId = 16;

    public Stub()
    {
      attachInterface(this, "com.google.android.gsf.IGoogleLoginService");
    }

    public static IGoogleLoginService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gsf.IGoogleLoginService");
      if ((localIInterface != null) && ((localIInterface instanceof IGoogleLoginService)))
        return (IGoogleLoginService)localIInterface;
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
        paramParcel2.writeString("com.google.android.gsf.IGoogleLoginService");
        return true;
      case 1:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        String[] arrayOfString = getAccounts();
        paramParcel2.writeNoException();
        paramParcel2.writeStringArray(arrayOfString);
        return true;
      case 2:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        String str5 = getPrimaryAccount();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str5);
        return true;
      case 3:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        if (paramParcel1.readInt() != 0);
        for (boolean bool3 = true; ; bool3 = false)
        {
          String str4 = getAccount(bool3);
          paramParcel2.writeNoException();
          paramParcel2.writeString(str4);
          return true;
        }
      case 4:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        String str3 = peekCredentials(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeString(str3);
        return true;
      case 5:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        String str1 = paramParcel1.readString();
        String str2 = paramParcel1.readString();
        boolean bool2;
        if (paramParcel1.readInt() != 0)
        {
          bool2 = true;
          GoogleLoginCredentialsResult localGoogleLoginCredentialsResult = blockingGetCredentials(str1, str2, bool2);
          paramParcel2.writeNoException();
          if (localGoogleLoginCredentialsResult == null)
            break label357;
          paramParcel2.writeInt(1);
          localGoogleLoginCredentialsResult.writeToParcel(paramParcel2, 1);
        }
        while (true)
        {
          return true;
          bool2 = false;
          break;
          paramParcel2.writeInt(0);
        }
      case 6:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        invalidateAuthToken(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 7:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        long l = getAndroidId();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l);
        return true;
      case 8:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        LoginData localLoginData3;
        if (paramParcel1.readInt() != 0)
        {
          localLoginData3 = (LoginData)LoginData.CREATOR.createFromParcel(paramParcel1);
          tryNewAccount(localLoginData3);
          paramParcel2.writeNoException();
          if (localLoginData3 == null)
            break label471;
          paramParcel2.writeInt(1);
          localLoginData3.writeToParcel(paramParcel2, 1);
        }
        while (true)
        {
          return true;
          localLoginData3 = null;
          break;
          paramParcel2.writeInt(0);
        }
      case 9:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        if (paramParcel1.readInt() != 0);
        for (LoginData localLoginData2 = (LoginData)LoginData.CREATOR.createFromParcel(paramParcel1); ; localLoginData2 = null)
        {
          saveNewAccount(localLoginData2);
          paramParcel2.writeNoException();
          return true;
        }
      case 10:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        saveAuthToken(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 11:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        LoginData localLoginData1;
        if (paramParcel1.readInt() != 0)
        {
          localLoginData1 = (LoginData)LoginData.CREATOR.createFromParcel(paramParcel1);
          updatePassword(localLoginData1);
          paramParcel2.writeNoException();
          if (localLoginData1 == null)
            break label614;
          paramParcel2.writeInt(1);
          localLoginData1.writeToParcel(paramParcel2, 1);
        }
        while (true)
        {
          return true;
          localLoginData1 = null;
          break;
          paramParcel2.writeInt(0);
        }
      case 12:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        boolean bool1 = verifyStoredPassword(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        if (bool1);
        for (int j = 1; ; j = 0)
        {
          paramParcel2.writeInt(j);
          return true;
        }
      case 13:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        saveUsernameAndPassword(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      case 14:
        paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        deleteOneAccount(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 15:
        label357: label614: paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
        label471: deleteAllAccounts();
        paramParcel2.writeNoException();
        return true;
      case 16:
      }
      paramParcel1.enforceInterface("com.google.android.gsf.IGoogleLoginService");
      int i = waitForAndroidId();
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }

    private static class Proxy
      implements IGoogleLoginService
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

      public GoogleLoginCredentialsResult blockingGetCredentials(String paramString1, String paramString2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          int i;
          if (paramBoolean)
          {
            i = 1;
            localParcel1.writeInt(i);
            this.mRemote.transact(5, localParcel1, localParcel2, 0);
            localParcel2.readException();
            if (localParcel2.readInt() == 0)
              break label106;
          }
          label106: for (GoogleLoginCredentialsResult localGoogleLoginCredentialsResult = (GoogleLoginCredentialsResult)GoogleLoginCredentialsResult.CREATOR.createFromParcel(localParcel2); ; localGoogleLoginCredentialsResult = null)
          {
            return localGoogleLoginCredentialsResult;
            i = 0;
            break;
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void deleteAllAccounts()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          this.mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void deleteOneAccount(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          localParcel1.writeString(paramString);
          this.mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getAccount(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          if (paramBoolean);
          for (int i = 1; ; i = 0)
          {
            localParcel1.writeInt(i);
            this.mRemote.transact(3, localParcel1, localParcel2, 0);
            localParcel2.readException();
            String str = localParcel2.readString();
            return str;
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String[] getAccounts()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          this.mRemote.transact(1, localParcel1, localParcel2, 0);
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

      public long getAndroidId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          this.mRemote.transact(7, localParcel1, localParcel2, 0);
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

      public String getInterfaceDescriptor()
      {
        return "com.google.android.gsf.IGoogleLoginService";
      }

      public String getPrimaryAccount()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
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

      public void invalidateAuthToken(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          localParcel1.writeString(paramString);
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

      public String peekCredentials(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          this.mRemote.transact(4, localParcel1, localParcel2, 0);
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

      public void saveAuthToken(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          this.mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void saveNewAccount(LoginData paramLoginData)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          if (paramLoginData != null)
          {
            localParcel1.writeInt(1);
            paramLoginData.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            this.mRemote.transact(9, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
            localParcel1.writeInt(0);
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void saveUsernameAndPassword(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          this.mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void tryNewAccount(LoginData paramLoginData)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          if (paramLoginData != null)
          {
            localParcel1.writeInt(1);
            paramLoginData.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            this.mRemote.transact(8, localParcel1, localParcel2, 0);
            localParcel2.readException();
            if (localParcel2.readInt() != 0)
              paramLoginData.readFromParcel(localParcel2);
            return;
            localParcel1.writeInt(0);
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void updatePassword(LoginData paramLoginData)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          if (paramLoginData != null)
          {
            localParcel1.writeInt(1);
            paramLoginData.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            this.mRemote.transact(11, localParcel1, localParcel2, 0);
            localParcel2.readException();
            if (localParcel2.readInt() != 0)
              paramLoginData.readFromParcel(localParcel2);
            return;
            localParcel1.writeInt(0);
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean verifyStoredPassword(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          this.mRemote.transact(12, localParcel1, localParcel2, 0);
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

      public int waitForAndroidId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gsf.IGoogleLoginService");
          this.mRemote.transact(16, localParcel1, localParcel2, 0);
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
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gsf.IGoogleLoginService
 * JD-Core Version:    0.6.2
 */