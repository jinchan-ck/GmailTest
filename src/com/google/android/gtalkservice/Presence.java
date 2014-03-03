package com.google.android.gtalkservice;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Presence
  implements Parcelable
{
  public static final Parcelable.Creator<Presence> CREATOR = new Parcelable.Creator()
  {
    public Presence createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Presence(paramAnonymousParcel);
    }

    public Presence[] newArray(int paramAnonymousInt)
    {
      return new Presence[paramAnonymousInt];
    }
  };
  public static final Presence OFFLINE = new Presence();
  private static final int STATUS_MIN_VERSION_FOR_INVISIBILITY = 2;
  private boolean mAllowInvisibility;
  private boolean mAvailable;
  private List<String> mDefaultStatusList;
  private List<String> mDndStatusList;
  private boolean mInvisible;
  private Show mShow;
  private String mStatus;
  private int mStatusListContentsMax;
  private int mStatusListMax;
  private int mStatusMax;

  public Presence()
  {
    this(false, Show.NONE, null);
  }

  public Presence(Parcel paramParcel)
  {
    setStatusMax(paramParcel.readInt());
    setStatusListMax(paramParcel.readInt());
    setStatusListContentsMax(paramParcel.readInt());
    boolean bool1;
    boolean bool2;
    if (paramParcel.readInt() != 0)
    {
      bool1 = true;
      setAllowInvisibility(bool1);
      if (paramParcel.readInt() == 0)
        break label141;
      bool2 = true;
      label51: setAvailable(bool2);
      setShow((Show)Enum.valueOf(Show.class, paramParcel.readString()));
      this.mStatus = paramParcel.readString();
      if (paramParcel.readInt() == 0)
        break label146;
    }
    label141: label146: for (boolean bool3 = true; ; bool3 = false)
    {
      setInvisible(bool3);
      this.mDefaultStatusList = new ArrayList();
      paramParcel.readStringList(this.mDefaultStatusList);
      this.mDndStatusList = new ArrayList();
      paramParcel.readStringList(this.mDndStatusList);
      return;
      bool1 = false;
      break;
      bool2 = false;
      break label51;
    }
  }

  public Presence(Presence paramPresence)
  {
    this.mStatusMax = paramPresence.mStatusMax;
    this.mStatusListMax = paramPresence.mStatusListMax;
    this.mStatusListContentsMax = paramPresence.mStatusListContentsMax;
    this.mAllowInvisibility = paramPresence.mAllowInvisibility;
    this.mAvailable = paramPresence.mAvailable;
    this.mShow = paramPresence.mShow;
    this.mStatus = paramPresence.mStatus;
    this.mInvisible = paramPresence.mInvisible;
    this.mDefaultStatusList = paramPresence.mDefaultStatusList;
    this.mDndStatusList = paramPresence.mDndStatusList;
  }

  public Presence(boolean paramBoolean, Show paramShow, String paramString)
  {
    this.mAvailable = paramBoolean;
    this.mShow = paramShow;
    this.mStatus = paramString;
    this.mInvisible = false;
    this.mDefaultStatusList = new ArrayList();
    this.mDndStatusList = new ArrayList();
  }

  private boolean addToList(List<String> paramList, String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return false;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
      if (((String)localIterator.next()).trim().equals(paramString.trim()))
        return false;
    int i = getStatusMax();
    if (paramString.length() > i)
      paramString = paramString.substring(0, i);
    paramList.add(0, paramString);
    checkListContentsLength(paramList);
    return true;
  }

  private List<String> checkListContentsLength(List<String> paramList)
  {
    int i = getStatusListContentsMax();
    int j = paramList.size();
    if (j > i)
      for (int k = j - 1; k >= i; k--)
        paramList.remove(k);
    return paramList;
  }

  private boolean listEqual(List<String> paramList1, List<String> paramList2)
  {
    int i = paramList1.size();
    if (i != paramList2.size())
      return false;
    for (int j = 0; j < i; j++)
      if (!((String)paramList1.get(j)).equals((String)paramList2.get(j)))
        return false;
    return true;
  }

  private void setStatus(String paramString, boolean paramBoolean)
  {
    this.mStatus = paramString;
    if (paramBoolean);
    switch (2.$SwitchMap$com$google$android$gtalkservice$Presence$Show[this.mShow.ordinal()])
    {
    default:
      return;
    case 1:
      addToList(this.mDndStatusList, paramString);
      return;
    case 2:
    }
    addToList(this.mDefaultStatusList, paramString);
  }

  public boolean allowInvisibility()
  {
    return this.mAllowInvisibility;
  }

  public void clearStatusLists()
  {
    this.mDefaultStatusList.clear();
    this.mDndStatusList.clear();
    this.mStatus = "";
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(Presence paramPresence)
  {
    if (paramPresence == null)
      return false;
    if (this.mAvailable != paramPresence.mAvailable)
      return false;
    if (this.mShow != paramPresence.mShow)
      return false;
    if (this.mStatus != null)
    {
      if (!this.mStatus.equals(paramPresence.mStatus))
        return false;
    }
    else if (paramPresence.mStatus != null)
      return false;
    if (this.mInvisible != paramPresence.mInvisible)
      return false;
    if (this.mStatusMax != paramPresence.mStatusMax)
      return false;
    if (this.mStatusListMax != paramPresence.mStatusListMax)
      return false;
    if (this.mStatusListContentsMax != paramPresence.mStatusListContentsMax)
      return false;
    if (this.mAllowInvisibility != paramPresence.mAllowInvisibility)
      return false;
    if (!listEqual(this.mDefaultStatusList, paramPresence.mDefaultStatusList))
      return false;
    return listEqual(this.mDndStatusList, paramPresence.mDndStatusList);
  }

  public List<String> getDefaultStatusList()
  {
    return new ArrayList(this.mDefaultStatusList);
  }

  public List<String> getDndStatusList()
  {
    return new ArrayList(this.mDndStatusList);
  }

  public Show getShow()
  {
    return this.mShow;
  }

  public String getStatus()
  {
    return this.mStatus;
  }

  public int getStatusListContentsMax()
  {
    return this.mStatusListContentsMax;
  }

  public int getStatusListMax()
  {
    return this.mStatusListMax;
  }

  public int getStatusMax()
  {
    return this.mStatusMax;
  }

  public boolean isAvailable()
  {
    return this.mAvailable;
  }

  public boolean isInvisible()
  {
    return this.mInvisible;
  }

  public String printDetails()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{ available=");
    localStringBuilder.append(this.mAvailable);
    localStringBuilder.append(", show=");
    localStringBuilder.append(this.mShow);
    localStringBuilder.append(", ");
    if (this.mStatus == null);
    for (String str1 = ""; ; str1 = this.mStatus)
    {
      localStringBuilder.append(str1);
      localStringBuilder.append(", invisible=" + this.mInvisible);
      localStringBuilder.append(", allowInvisible=");
      localStringBuilder.append(this.mAllowInvisibility);
      localStringBuilder.append(", default={");
      int i = 0;
      if (this.mDefaultStatusList == null)
        break;
      Iterator localIterator2 = this.mDefaultStatusList.iterator();
      while (localIterator2.hasNext())
      {
        String str3 = (String)localIterator2.next();
        int m = i + 1;
        if (i > 0)
          localStringBuilder.append(", ");
        localStringBuilder.append(str3);
        i = m;
      }
    }
    localStringBuilder.append("}, dnd={");
    if (this.mDndStatusList != null)
    {
      int j = 0;
      Iterator localIterator1 = this.mDndStatusList.iterator();
      while (localIterator1.hasNext())
      {
        String str2 = (String)localIterator1.next();
        int k = j + 1;
        if (j > 0)
          localStringBuilder.append(", ");
        localStringBuilder.append(str2);
        j = k;
      }
    }
    localStringBuilder.append("}");
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }

  public void setAllowInvisibility(boolean paramBoolean)
  {
    this.mAllowInvisibility = paramBoolean;
  }

  public void setAvailable(boolean paramBoolean)
  {
    this.mAvailable = paramBoolean;
  }

  public boolean setInvisible(boolean paramBoolean)
  {
    this.mInvisible = paramBoolean;
    return (!paramBoolean) || (allowInvisibility());
  }

  public void setShow(Show paramShow)
  {
    this.mShow = paramShow;
  }

  public void setStatus(Show paramShow, String paramString)
  {
    setShow(paramShow);
    setStatus(paramString, true);
  }

  public void setStatus(String paramString)
  {
    setStatus(paramString, false);
  }

  public void setStatusListContentsMax(int paramInt)
  {
    this.mStatusListContentsMax = paramInt;
  }

  public void setStatusListMax(int paramInt)
  {
    this.mStatusListMax = paramInt;
  }

  public void setStatusMax(int paramInt)
  {
    this.mStatusMax = paramInt;
  }

  public String toString()
  {
    if (!isAvailable())
      return "UNAVAILABLE";
    if (isInvisible())
      return "INVISIBLE";
    if (this.mShow == Show.NONE);
    for (String str = "AVAILABLE(x)"; ; str = this.mShow.toString())
      return str;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(getStatusMax());
    paramParcel.writeInt(getStatusListMax());
    paramParcel.writeInt(getStatusListContentsMax());
    int i;
    int j;
    if (allowInvisibility())
    {
      i = 1;
      paramParcel.writeInt(i);
      if (!this.mAvailable)
        break label111;
      j = 1;
      label48: paramParcel.writeInt(j);
      paramParcel.writeString(this.mShow.toString());
      paramParcel.writeString(this.mStatus);
      if (!this.mInvisible)
        break label117;
    }
    label111: label117: for (int k = 1; ; k = 0)
    {
      paramParcel.writeInt(k);
      paramParcel.writeStringList(this.mDefaultStatusList);
      paramParcel.writeStringList(this.mDndStatusList);
      return;
      i = 0;
      break;
      j = 0;
      break label48;
    }
  }

  public static enum Show
  {
    static
    {
      AWAY = new Show("AWAY", 1);
      EXTENDED_AWAY = new Show("EXTENDED_AWAY", 2);
      DND = new Show("DND", 3);
      AVAILABLE = new Show("AVAILABLE", 4);
      Show[] arrayOfShow = new Show[5];
      arrayOfShow[0] = NONE;
      arrayOfShow[1] = AWAY;
      arrayOfShow[2] = EXTENDED_AWAY;
      arrayOfShow[3] = DND;
      arrayOfShow[4] = AVAILABLE;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gtalkservice.Presence
 * JD-Core Version:    0.6.2
 */