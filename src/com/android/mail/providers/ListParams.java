package com.android.mail.providers;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ListParams
  implements Parcelable
{
  public static final Parcelable.Creator<ListParams> CREATOR = new Parcelable.Creator()
  {
    public ListParams createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ListParams(paramAnonymousParcel);
    }

    public ListParams[] newArray(int paramAnonymousInt)
    {
      return new ListParams[paramAnonymousInt];
    }
  };
  private static final String LOG_TAG = LogTag.getLogTag();
  public final int mLimit;
  public final boolean mUseNetwork;

  public ListParams(int paramInt, boolean paramBoolean)
  {
    this.mLimit = paramInt;
    this.mUseNetwork = paramBoolean;
  }

  public ListParams(Parcel paramParcel)
  {
    this.mLimit = paramParcel.readInt();
    if (paramParcel.readInt() != 0);
    for (boolean bool = true; ; bool = false)
    {
      this.mUseNetwork = bool;
      return;
    }
  }

  // ERROR //
  public static ListParams newinstance(String paramString)
  {
    // Byte code:
    //   0: new 51	org/json/JSONObject
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 54	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   8: astore_1
    //   9: new 2	com/android/mail/providers/ListParams
    //   12: dup
    //   13: aload_1
    //   14: ldc 56
    //   16: invokevirtual 60	org/json/JSONObject:getInt	(Ljava/lang/String;)I
    //   19: aload_1
    //   20: ldc 62
    //   22: invokevirtual 66	org/json/JSONObject:getBoolean	(Ljava/lang/String;)Z
    //   25: invokespecial 68	com/android/mail/providers/ListParams:<init>	(IZ)V
    //   28: astore_2
    //   29: aload_2
    //   30: areturn
    //   31: astore_3
    //   32: getstatic 25	com/android/mail/providers/ListParams:LOG_TAG	Ljava/lang/String;
    //   35: aload_3
    //   36: new 70	java/lang/StringBuilder
    //   39: dup
    //   40: invokespecial 71	java/lang/StringBuilder:<init>	()V
    //   43: ldc 73
    //   45: invokevirtual 77	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: aload_0
    //   49: invokevirtual 77	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   55: iconst_0
    //   56: anewarray 4	java/lang/Object
    //   59: invokestatic 86	com/android/mail/utils/LogUtils:wtf	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
    //   62: pop
    //   63: aconst_null
    //   64: areturn
    //   65: astore_3
    //   66: goto -34 -> 32
    //
    // Exception table:
    //   from	to	target	type
    //   0	9	31	org/json/JSONException
    //   9	29	65	org/json/JSONException
  }

  public int describeContents()
  {
    return 0;
  }

  public String serialize()
  {
    try
    {
      JSONObject localJSONObject = new JSONObject();
      try
      {
        localJSONObject.put("limit", this.mLimit);
        localJSONObject.put("use-network", this.mUseNetwork);
        String str = localJSONObject.toString();
        return str;
      }
      catch (JSONException localJSONException)
      {
        while (true)
          LogUtils.wtf(LOG_TAG, localJSONException, "Could not serialize ListParams", new Object[0]);
      }
    }
    finally
    {
    }
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mLimit);
    if (this.mUseNetwork);
    for (int i = 1; ; i = 0)
    {
      paramParcel.writeInt(i);
      return;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.ListParams
 * JD-Core Version:    0.6.2
 */