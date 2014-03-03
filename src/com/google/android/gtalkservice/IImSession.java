package com.google.android.gtalkservice;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IImSession extends IInterface
{
  public abstract void addConnectionStateListener(IConnectionStateListener paramIConnectionStateListener)
    throws RemoteException;

  public abstract void addContact(String paramString1, String paramString2, String[] paramArrayOfString)
    throws RemoteException;

  public abstract void addGroupChatInvitationListener(IGroupChatInvitationListener paramIGroupChatInvitationListener)
    throws RemoteException;

  public abstract void addRemoteChatListener(IChatListener paramIChatListener)
    throws RemoteException;

  public abstract void addRemoteRosterListener(IRosterListener paramIRosterListener)
    throws RemoteException;

  public abstract void approveSubscriptionRequest(String paramString1, String paramString2, String[] paramArrayOfString)
    throws RemoteException;

  public abstract void blockContact(String paramString)
    throws RemoteException;

  public abstract void clearContactFlags(String paramString)
    throws RemoteException;

  public abstract void closeAllChatSessions()
    throws RemoteException;

  public abstract IChatSession createChatSession(String paramString)
    throws RemoteException;

  public abstract void createGroupChatSession(String paramString, String[] paramArrayOfString)
    throws RemoteException;

  public abstract void declineGroupChatInvitation(String paramString1, String paramString2)
    throws RemoteException;

  public abstract void declineSubscriptionRequest(String paramString)
    throws RemoteException;

  public abstract void editContact(String paramString1, String paramString2, String[] paramArrayOfString)
    throws RemoteException;

  public abstract long getAccountId()
    throws RemoteException;

  public abstract IChatSession getChatSession(String paramString)
    throws RemoteException;

  public abstract ConnectionState getConnectionState()
    throws RemoteException;

  public abstract Presence getPresence()
    throws RemoteException;

  public abstract long getServiceProviderId()
    throws RemoteException;

  public abstract String getUsername()
    throws RemoteException;

  public abstract void goOffRecordInRoom(String paramString, boolean paramBoolean)
    throws RemoteException;

  public abstract void goOffRecordWithContacts(List paramList, boolean paramBoolean)
    throws RemoteException;

  public abstract void hideContact(String paramString)
    throws RemoteException;

  public abstract boolean isOffRecordWithContact(String paramString)
    throws RemoteException;

  public abstract void joinGroupChatSession(String paramString1, String paramString2, String paramString3)
    throws RemoteException;

  public abstract void login(String paramString1, String paramString2, boolean paramBoolean)
    throws RemoteException;

  public abstract void logout()
    throws RemoteException;

  public abstract void pinContact(String paramString)
    throws RemoteException;

  public abstract void pruneOldChatSessions(long paramLong1, long paramLong2)
    throws RemoteException;

  public abstract void removeConnectionStateListener(IConnectionStateListener paramIConnectionStateListener)
    throws RemoteException;

  public abstract void removeContact(String paramString)
    throws RemoteException;

  public abstract void removeGroupChatInvitationListener(IGroupChatInvitationListener paramIGroupChatInvitationListener)
    throws RemoteException;

  public abstract void removeRemoteChatListener(IChatListener paramIChatListener)
    throws RemoteException;

  public abstract void removeRemoteRosterListener(IRosterListener paramIRosterListener)
    throws RemoteException;

  public abstract void setPresence(Presence paramPresence)
    throws RemoteException;

  public abstract void uploadAvatar(Bitmap paramBitmap)
    throws RemoteException;

  public abstract void uploadAvatarFromDb()
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IImSession
  {
    private static final String DESCRIPTOR = "com.google.android.gtalkservice.IImSession";
    static final int TRANSACTION_addConnectionStateListener = 7;
    static final int TRANSACTION_addContact = 13;
    static final int TRANSACTION_addGroupChatInvitationListener = 27;
    static final int TRANSACTION_addRemoteChatListener = 29;
    static final int TRANSACTION_addRemoteRosterListener = 31;
    static final int TRANSACTION_approveSubscriptionRequest = 20;
    static final int TRANSACTION_blockContact = 16;
    static final int TRANSACTION_clearContactFlags = 19;
    static final int TRANSACTION_closeAllChatSessions = 36;
    static final int TRANSACTION_createChatSession = 22;
    static final int TRANSACTION_createGroupChatSession = 24;
    static final int TRANSACTION_declineGroupChatInvitation = 26;
    static final int TRANSACTION_declineSubscriptionRequest = 21;
    static final int TRANSACTION_editContact = 14;
    static final int TRANSACTION_getAccountId = 2;
    static final int TRANSACTION_getChatSession = 23;
    static final int TRANSACTION_getConnectionState = 6;
    static final int TRANSACTION_getPresence = 10;
    static final int TRANSACTION_getServiceProviderId = 1;
    static final int TRANSACTION_getUsername = 3;
    static final int TRANSACTION_goOffRecordInRoom = 34;
    static final int TRANSACTION_goOffRecordWithContacts = 33;
    static final int TRANSACTION_hideContact = 18;
    static final int TRANSACTION_isOffRecordWithContact = 35;
    static final int TRANSACTION_joinGroupChatSession = 25;
    static final int TRANSACTION_login = 4;
    static final int TRANSACTION_logout = 5;
    static final int TRANSACTION_pinContact = 17;
    static final int TRANSACTION_pruneOldChatSessions = 37;
    static final int TRANSACTION_removeConnectionStateListener = 8;
    static final int TRANSACTION_removeContact = 15;
    static final int TRANSACTION_removeGroupChatInvitationListener = 28;
    static final int TRANSACTION_removeRemoteChatListener = 30;
    static final int TRANSACTION_removeRemoteRosterListener = 32;
    static final int TRANSACTION_setPresence = 9;
    static final int TRANSACTION_uploadAvatar = 11;
    static final int TRANSACTION_uploadAvatarFromDb = 12;

    public Stub()
    {
      attachInterface(this, "com.google.android.gtalkservice.IImSession");
    }

    public static IImSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.gtalkservice.IImSession");
      if ((localIInterface != null) && ((localIInterface instanceof IImSession)))
        return (IImSession)localIInterface;
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
        paramParcel2.writeString("com.google.android.gtalkservice.IImSession");
        return true;
      case 1:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        long l2 = getServiceProviderId();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l2);
        return true;
      case 2:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        long l1 = getAccountId();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l1);
        return true;
      case 3:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        String str4 = getUsername();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str4);
        return true;
      case 4:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        String str2 = paramParcel1.readString();
        String str3 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0);
        for (boolean bool4 = true; ; bool4 = false)
        {
          login(str2, str3, bool4);
          return true;
        }
      case 5:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        logout();
        return true;
      case 6:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        ConnectionState localConnectionState = getConnectionState();
        paramParcel2.writeNoException();
        if (localConnectionState != null)
        {
          paramParcel2.writeInt(1);
          localConnectionState.writeToParcel(paramParcel2, 1);
        }
        while (true)
        {
          return true;
          paramParcel2.writeInt(0);
        }
      case 7:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        addConnectionStateListener(IConnectionStateListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 8:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        removeConnectionStateListener(IConnectionStateListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 9:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        if (paramParcel1.readInt() != 0);
        for (Presence localPresence2 = (Presence)Presence.CREATOR.createFromParcel(paramParcel1); ; localPresence2 = null)
        {
          setPresence(localPresence2);
          paramParcel2.writeNoException();
          return true;
        }
      case 10:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        Presence localPresence1 = getPresence();
        paramParcel2.writeNoException();
        if (localPresence1 != null)
        {
          paramParcel2.writeInt(1);
          localPresence1.writeToParcel(paramParcel2, 1);
        }
        while (true)
        {
          return true;
          paramParcel2.writeInt(0);
        }
      case 11:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        if (paramParcel1.readInt() != 0);
        for (Bitmap localBitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel1); ; localBitmap = null)
        {
          uploadAvatar(localBitmap);
          return true;
        }
      case 12:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        uploadAvatarFromDb();
        return true;
      case 13:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        addContact(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.createStringArray());
        return true;
      case 14:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        editContact(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.createStringArray());
        return true;
      case 15:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        removeContact(paramParcel1.readString());
        return true;
      case 16:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        blockContact(paramParcel1.readString());
        return true;
      case 17:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        pinContact(paramParcel1.readString());
        return true;
      case 18:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        hideContact(paramParcel1.readString());
        return true;
      case 19:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        clearContactFlags(paramParcel1.readString());
        return true;
      case 20:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        approveSubscriptionRequest(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.createStringArray());
        return true;
      case 21:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        declineSubscriptionRequest(paramParcel1.readString());
        return true;
      case 22:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        IChatSession localIChatSession2 = createChatSession(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (localIChatSession2 != null);
        for (IBinder localIBinder2 = localIChatSession2.asBinder(); ; localIBinder2 = null)
        {
          paramParcel2.writeStrongBinder(localIBinder2);
          return true;
        }
      case 23:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        IChatSession localIChatSession1 = getChatSession(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (localIChatSession1 != null);
        for (IBinder localIBinder1 = localIChatSession1.asBinder(); ; localIBinder1 = null)
        {
          paramParcel2.writeStrongBinder(localIBinder1);
          return true;
        }
      case 24:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        createGroupChatSession(paramParcel1.readString(), paramParcel1.createStringArray());
        paramParcel2.writeNoException();
        return true;
      case 25:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        joinGroupChatSession(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 26:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        declineGroupChatInvitation(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 27:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        addGroupChatInvitationListener(IGroupChatInvitationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 28:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        removeGroupChatInvitationListener(IGroupChatInvitationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 29:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        addRemoteChatListener(IChatListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 30:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        removeRemoteChatListener(IChatListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 31:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        addRemoteRosterListener(IRosterListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 32:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        removeRemoteRosterListener(IRosterListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      case 33:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        ArrayList localArrayList = paramParcel1.readArrayList(getClass().getClassLoader());
        if (paramParcel1.readInt() != 0);
        for (boolean bool3 = true; ; bool3 = false)
        {
          goOffRecordWithContacts(localArrayList, bool3);
          return true;
        }
      case 34:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        String str1 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0);
        for (boolean bool2 = true; ; bool2 = false)
        {
          goOffRecordInRoom(str1, bool2);
          paramParcel2.writeNoException();
          return true;
        }
      case 35:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        boolean bool1 = isOffRecordWithContact(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (bool1);
        for (int i = 1; ; i = 0)
        {
          paramParcel2.writeInt(i);
          return true;
        }
      case 36:
        paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
        closeAllChatSessions();
        return true;
      case 37:
      }
      paramParcel1.enforceInterface("com.google.android.gtalkservice.IImSession");
      pruneOldChatSessions(paramParcel1.readLong(), paramParcel1.readLong());
      return true;
    }

    private static class Proxy
      implements IImSession
    {
      private IBinder mRemote;

      Proxy(IBinder paramIBinder)
      {
        this.mRemote = paramIBinder;
      }

      public void addConnectionStateListener(IConnectionStateListener paramIConnectionStateListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramIConnectionStateListener != null);
          for (IBinder localIBinder = paramIConnectionStateListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(7, localParcel1, localParcel2, 0);
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

      public void addContact(String paramString1, String paramString2, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeStringArray(paramArrayOfString);
          this.mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void addGroupChatInvitationListener(IGroupChatInvitationListener paramIGroupChatInvitationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramIGroupChatInvitationListener != null);
          for (IBinder localIBinder = paramIGroupChatInvitationListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(27, localParcel1, localParcel2, 0);
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

      public void addRemoteChatListener(IChatListener paramIChatListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramIChatListener != null);
          for (IBinder localIBinder = paramIChatListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(29, localParcel1, localParcel2, 0);
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

      public void addRemoteRosterListener(IRosterListener paramIRosterListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramIRosterListener != null);
          for (IBinder localIBinder = paramIRosterListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(31, localParcel1, localParcel2, 0);
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

      public void approveSubscriptionRequest(String paramString1, String paramString2, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeStringArray(paramArrayOfString);
          this.mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public IBinder asBinder()
      {
        return this.mRemote;
      }

      public void blockContact(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString);
          this.mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void clearContactFlags(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString);
          this.mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void closeAllChatSessions()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          this.mRemote.transact(36, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public IChatSession createChatSession(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel1.writeString(paramString);
          this.mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IChatSession localIChatSession = IChatSession.Stub.asInterface(localParcel2.readStrongBinder());
          return localIChatSession;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void createGroupChatSession(String paramString, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel1.writeString(paramString);
          localParcel1.writeStringArray(paramArrayOfString);
          this.mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void declineGroupChatInvitation(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          this.mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void declineSubscriptionRequest(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString);
          this.mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void editContact(String paramString1, String paramString2, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeStringArray(paramArrayOfString);
          this.mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public long getAccountId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          this.mRemote.transact(2, localParcel1, localParcel2, 0);
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

      public IChatSession getChatSession(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel1.writeString(paramString);
          this.mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IChatSession localIChatSession = IChatSession.Stub.asInterface(localParcel2.readStrongBinder());
          return localIChatSession;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public ConnectionState getConnectionState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          this.mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0)
          {
            localConnectionState = (ConnectionState)ConnectionState.CREATOR.createFromParcel(localParcel2);
            return localConnectionState;
          }
          ConnectionState localConnectionState = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getInterfaceDescriptor()
      {
        return "com.google.android.gtalkservice.IImSession";
      }

      public Presence getPresence()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          this.mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0)
          {
            localPresence = (Presence)Presence.CREATOR.createFromParcel(localParcel2);
            return localPresence;
          }
          Presence localPresence = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long getServiceProviderId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          this.mRemote.transact(1, localParcel1, localParcel2, 0);
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

      public String getUsername()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
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

      public void goOffRecordInRoom(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel1.writeString(paramString);
          if (paramBoolean);
          for (int i = 1; ; i = 0)
          {
            localParcel1.writeInt(i);
            this.mRemote.transact(34, localParcel1, localParcel2, 0);
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

      public void goOffRecordWithContacts(List paramList, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeList(paramList);
          if (paramBoolean);
          for (int i = 1; ; i = 0)
          {
            localParcel.writeInt(i);
            this.mRemote.transact(33, localParcel, null, 1);
            return;
          }
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void hideContact(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString);
          this.mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public boolean isOffRecordWithContact(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel1.writeString(paramString);
          this.mRemote.transact(35, localParcel1, localParcel2, 0);
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

      public void joinGroupChatSession(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          this.mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void login(String paramString1, String paramString2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramBoolean);
          for (int i = 1; ; i = 0)
          {
            localParcel.writeInt(i);
            this.mRemote.transact(4, localParcel, null, 1);
            return;
          }
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void logout()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          this.mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void pinContact(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString);
          this.mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void pruneOldChatSessions(long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeLong(paramLong1);
          localParcel.writeLong(paramLong2);
          this.mRemote.transact(37, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void removeConnectionStateListener(IConnectionStateListener paramIConnectionStateListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramIConnectionStateListener != null);
          for (IBinder localIBinder = paramIConnectionStateListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(8, localParcel1, localParcel2, 0);
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

      public void removeContact(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          localParcel.writeString(paramString);
          this.mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void removeGroupChatInvitationListener(IGroupChatInvitationListener paramIGroupChatInvitationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramIGroupChatInvitationListener != null);
          for (IBinder localIBinder = paramIGroupChatInvitationListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(28, localParcel1, localParcel2, 0);
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

      public void removeRemoteChatListener(IChatListener paramIChatListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramIChatListener != null);
          for (IBinder localIBinder = paramIChatListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(30, localParcel1, localParcel2, 0);
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

      public void removeRemoteRosterListener(IRosterListener paramIRosterListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramIRosterListener != null);
          for (IBinder localIBinder = paramIRosterListener.asBinder(); ; localIBinder = null)
          {
            localParcel1.writeStrongBinder(localIBinder);
            this.mRemote.transact(32, localParcel1, localParcel2, 0);
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

      public void setPresence(Presence paramPresence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramPresence != null)
          {
            localParcel1.writeInt(1);
            paramPresence.writeToParcel(localParcel1, 0);
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

      public void uploadAvatar(Bitmap paramBitmap)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          if (paramBitmap != null)
          {
            localParcel.writeInt(1);
            paramBitmap.writeToParcel(localParcel, 0);
          }
          while (true)
          {
            this.mRemote.transact(11, localParcel, null, 1);
            return;
            localParcel.writeInt(0);
          }
        }
        finally
        {
          localParcel.recycle();
        }
      }

      public void uploadAvatarFromDb()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.google.android.gtalkservice.IImSession");
          this.mRemote.transact(12, localParcel, null, 1);
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
 * Qualified Name:     com.google.android.gtalkservice.IImSession
 * JD-Core Version:    0.6.2
 */