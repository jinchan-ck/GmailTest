package com.android.mail.ui;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import com.android.mail.providers.Conversation;

public class LeaveBehindData
  implements Parcelable
{
  public static final Parcelable.ClassLoaderCreator<LeaveBehindData> CREATOR = new Parcelable.ClassLoaderCreator()
  {
    public LeaveBehindData createFromParcel(Parcel paramAnonymousParcel)
    {
      return new LeaveBehindData(paramAnonymousParcel, null, null);
    }

    public LeaveBehindData createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
    {
      return new LeaveBehindData(paramAnonymousParcel, paramAnonymousClassLoader, null);
    }

    public LeaveBehindData[] newArray(int paramAnonymousInt)
    {
      return new LeaveBehindData[paramAnonymousInt];
    }
  };
  final Conversation data;
  final ToastBarOperation op;

  private LeaveBehindData(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    this.data = ((Conversation)paramParcel.readParcelable(paramClassLoader));
    this.op = ((ToastBarOperation)paramParcel.readParcelable(paramClassLoader));
  }

  public LeaveBehindData(Conversation paramConversation, ToastBarOperation paramToastBarOperation)
  {
    this.data = paramConversation;
    this.op = paramToastBarOperation;
  }

  public int describeContents()
  {
    return 0;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(this.data, 0);
    paramParcel.writeParcelable(this.op, 0);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.LeaveBehindData
 * JD-Core Version:    0.6.2
 */