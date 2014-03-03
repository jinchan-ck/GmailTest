package com.android.mail.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.mail.providers.Folder;

public class ToastBarOperation
  implements Parcelable
{
  public static final Parcelable.Creator<ToastBarOperation> CREATOR = new Parcelable.Creator()
  {
    public ToastBarOperation createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ToastBarOperation(paramAnonymousParcel);
    }

    public ToastBarOperation[] newArray(int paramAnonymousInt)
    {
      return new ToastBarOperation[paramAnonymousInt];
    }
  };
  private final int mAction;
  private final boolean mBatch;
  private final int mCount;
  private final int mType;

  public ToastBarOperation(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    this.mCount = paramInt1;
    this.mAction = paramInt2;
    this.mBatch = paramBoolean;
    this.mType = paramInt3;
  }

  public ToastBarOperation(Parcel paramParcel)
  {
    this.mCount = paramParcel.readInt();
    this.mAction = paramParcel.readInt();
    if (paramParcel.readInt() != 0);
    for (boolean bool = true; ; bool = false)
    {
      this.mBatch = bool;
      this.mType = paramParcel.readInt();
      return;
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public String getDescription(Context paramContext, Folder paramFolder)
  {
    int i;
    switch (this.mAction)
    {
    case 2131689754:
    case 2131689756:
    case 2131689757:
    case 2131689763:
    case 2131689764:
    case 2131689765:
    case 2131689766:
    case 2131689767:
    default:
      i = -1;
    case 2131689753:
    case 2131689752:
    case 2131689755:
    case 2131689751:
    case 2131689760:
    case 2131689761:
    case 2131689758:
    case 2131689759:
    case 2131689768:
    case 2131689762:
    }
    while (i == -1)
    {
      return "";
      i = 2131755026;
      continue;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = paramFolder.name;
      return paramContext.getString(2131427491, arrayOfObject2);
      i = 2131755031;
      continue;
      i = 2131755024;
      continue;
      i = 2131755020;
      continue;
      i = 2131755021;
      continue;
      i = 2131755022;
      continue;
      i = 2131755019;
      continue;
      i = 2131755017;
      continue;
      i = 2131755023;
    }
    String str = paramContext.getResources().getQuantityString(i, this.mCount);
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Integer.valueOf(this.mCount);
    return String.format(str, arrayOfObject1);
  }

  public String getSingularDescription(Context paramContext, Folder paramFolder)
  {
    if (this.mAction == 2131689752)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramFolder.name;
      return paramContext.getString(2131427491, arrayOfObject);
    }
    int i = -1;
    switch (this.mAction)
    {
    case 2131689752:
    default:
    case 2131689753:
    case 2131689751:
    }
    while (i == -1)
    {
      return "";
      i = 2131427489;
      continue;
      i = 2131427490;
    }
    return paramContext.getString(i);
  }

  public int getType()
  {
    return this.mType;
  }

  public boolean isBatchUndo()
  {
    return this.mBatch;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mCount);
    paramParcel.writeInt(this.mAction);
    if (this.mBatch);
    for (int i = 1; ; i = 0)
    {
      paramParcel.writeInt(i);
      paramParcel.writeInt(this.mType);
      return;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ToastBarOperation
 * JD-Core Version:    0.6.2
 */