package com.android.mail.providers;

import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Attachment
  implements Parcelable
{
  public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator()
  {
    public Attachment createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Attachment(paramAnonymousParcel);
    }

    public Attachment[] newArray(int paramAnonymousInt)
    {
      return new Attachment[paramAnonymousInt];
    }
  };
  public static final String LOG_TAG = LogTag.getLogTag();
  public String contentType;
  public Uri contentUri;
  public int destination;
  public int downloadedSize;
  private transient Uri mIdentifierUri;
  public String name;

  @Deprecated
  public int origin;

  @Deprecated
  public String originExtras;

  @Deprecated
  public String partId;
  public Uri previewIntentUri;
  public int size;
  public int state;
  public Uri thumbnailUri;
  public Uri uri;

  public Attachment()
  {
  }

  public Attachment(Cursor paramCursor)
  {
    if (paramCursor == null)
      return;
    this.name = paramCursor.getString(paramCursor.getColumnIndex("_display_name"));
    this.size = paramCursor.getInt(paramCursor.getColumnIndex("_size"));
    this.uri = Uri.parse(paramCursor.getString(paramCursor.getColumnIndex("uri")));
    this.contentType = paramCursor.getString(paramCursor.getColumnIndex("contentType"));
    this.state = paramCursor.getInt(paramCursor.getColumnIndex("state"));
    this.destination = paramCursor.getInt(paramCursor.getColumnIndex("destination"));
    this.downloadedSize = paramCursor.getInt(paramCursor.getColumnIndex("downloadedSize"));
    this.contentUri = parseOptionalUri(paramCursor.getString(paramCursor.getColumnIndex("contentUri")));
    this.thumbnailUri = parseOptionalUri(paramCursor.getString(paramCursor.getColumnIndex("thumbnailUri")));
    this.previewIntentUri = parseOptionalUri(paramCursor.getString(paramCursor.getColumnIndex("previewIntentUri")));
  }

  public Attachment(Parcel paramParcel)
  {
    this.name = paramParcel.readString();
    this.size = paramParcel.readInt();
    this.uri = ((Uri)paramParcel.readParcelable(null));
    this.contentType = paramParcel.readString();
    this.state = paramParcel.readInt();
    this.destination = paramParcel.readInt();
    this.downloadedSize = paramParcel.readInt();
    this.contentUri = ((Uri)paramParcel.readParcelable(null));
    this.thumbnailUri = ((Uri)paramParcel.readParcelable(null));
    this.previewIntentUri = ((Uri)paramParcel.readParcelable(null));
    this.partId = paramParcel.readString();
    this.origin = paramParcel.readInt();
    this.originExtras = paramParcel.readString();
  }

  public Attachment(JSONObject paramJSONObject)
  {
    this.name = paramJSONObject.optString("_display_name", null);
    this.size = paramJSONObject.optInt("_size");
    this.uri = parseOptionalUri(paramJSONObject, "uri");
    this.contentUri = parseOptionalUri(paramJSONObject, "contentUri");
    this.contentType = paramJSONObject.optString("contentType", null);
    if (paramJSONObject.has("state"))
      this.state = paramJSONObject.optInt("state");
  }

  public static List<Attachment> fromJSONArray(String paramString)
  {
    ArrayList localArrayList = Lists.newArrayList();
    try
    {
      JSONArray localJSONArray = new JSONArray(paramString);
      for (int i = 0; i < localJSONArray.length(); i++)
        localArrayList.add(new Attachment(localJSONArray.getJSONObject(i)));
    }
    catch (JSONException localJSONException)
    {
      throw new IllegalArgumentException(localJSONException);
    }
    return localArrayList;
  }

  private static Uri parseOptionalUri(String paramString)
  {
    if (paramString == null)
      return null;
    return Uri.parse(paramString);
  }

  private static Uri parseOptionalUri(JSONObject paramJSONObject, String paramString)
  {
    String str = paramJSONObject.optString(paramString, null);
    if (str == null)
      return null;
    return Uri.parse(str);
  }

  public static JSONObject toJSON(String paramString1, int paramInt, Uri paramUri1, Uri paramUri2, String paramString2, Integer paramInteger)
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.putOpt("_display_name", paramString1);
    localJSONObject.putOpt("_size", Integer.valueOf(paramInt));
    if (paramUri1 != null)
      localJSONObject.putOpt("uri", paramUri1.toString());
    if (paramUri2 != null)
      localJSONObject.putOpt("contentUri", paramUri2.toString());
    localJSONObject.putOpt("contentType", paramString2);
    if (paramInteger != null)
      localJSONObject.put("state", paramInteger.intValue());
    return localJSONObject;
  }

  public static String toJSONArray(Collection<Attachment> paramCollection)
  {
    JSONArray localJSONArray = new JSONArray();
    try
    {
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
        localJSONArray.put(((Attachment)localIterator.next()).toJSON());
    }
    catch (JSONException localJSONException)
    {
      throw new IllegalArgumentException(localJSONException);
    }
    return localJSONArray.toString();
  }

  public boolean canPreview()
  {
    return this.previewIntentUri != null;
  }

  public boolean canSave()
  {
    return (this.origin != 1) && (this.state != 2) && (!isSavedToExternal());
  }

  public boolean canShare()
  {
    return (isPresentLocally()) && (this.contentUri != null);
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean downloadFailed()
  {
    return this.state == 1;
  }

  public Uri getIdentifierUri()
  {
    if (this.mIdentifierUri == null)
      if (!Utils.isEmpty(this.uri))
        break label31;
    label31: for (Uri localUri = Uri.EMPTY; ; localUri = this.uri.buildUpon().clearQuery().build())
    {
      this.mIdentifierUri = localUri;
      return this.mIdentifierUri;
    }
  }

  public boolean isDownloading()
  {
    return this.state == 2;
  }

  public boolean isImage()
  {
    if (!TextUtils.isEmpty(this.contentType))
      return this.contentType.startsWith("image/");
    return false;
  }

  public boolean isPresentLocally()
  {
    return (this.state == 3) || (this.origin == 1);
  }

  public boolean isSavedToExternal()
  {
    return (this.state == 3) && (this.destination == 1);
  }

  public boolean shouldShowProgress()
  {
    return (this.state == 2) && (this.size > 0) && (this.downloadedSize > 0) && (this.downloadedSize < this.size);
  }

  public JSONObject toJSON()
    throws JSONException
  {
    return toJSON(this.name, this.size, this.uri, this.contentUri, this.contentType, Integer.valueOf(this.state));
  }

  @Deprecated
  public String toJoinedString()
  {
    Comparable[] arrayOfComparable = new Comparable[9];
    String str1;
    String str2;
    label32: int i;
    if (this.partId == null)
    {
      str1 = "";
      arrayOfComparable[0] = str1;
      if (this.name != null)
        break label130;
      str2 = "";
      arrayOfComparable[1] = str2;
      arrayOfComparable[2] = this.contentType;
      arrayOfComparable[3] = Integer.valueOf(this.size);
      arrayOfComparable[4] = this.contentType;
      if (this.contentUri == null)
        break label147;
      i = 1;
      label70: arrayOfComparable[5] = Integer.valueOf(i);
      arrayOfComparable[6] = this.contentUri;
      if (this.originExtras != null)
        break label153;
    }
    label130: label147: label153: for (String str3 = ""; ; str3 = this.originExtras)
    {
      arrayOfComparable[7] = str3;
      arrayOfComparable[8] = "";
      return TextUtils.join("|", Lists.newArrayList(arrayOfComparable));
      str1 = this.partId;
      break;
      str2 = this.name.replaceAll("[|\n]", "");
      break label32;
      i = 0;
      break label70;
    }
  }

  public String toString()
  {
    try
    {
      JSONObject localJSONObject = toJSON();
      localJSONObject.put("downloadedSize", this.downloadedSize);
      localJSONObject.put("destination", this.destination);
      localJSONObject.put("thumbnailUri", this.thumbnailUri);
      localJSONObject.put("previewIntentUri", this.previewIntentUri);
      String str = localJSONObject.toString();
      return str;
    }
    catch (JSONException localJSONException)
    {
      LogUtils.e(LOG_TAG, localJSONException, "JSONException in toString", new Object[0]);
    }
    return super.toString();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.name);
    paramParcel.writeInt(this.size);
    paramParcel.writeParcelable(this.uri, paramInt);
    paramParcel.writeString(this.contentType);
    paramParcel.writeInt(this.state);
    paramParcel.writeInt(this.destination);
    paramParcel.writeInt(this.downloadedSize);
    paramParcel.writeParcelable(this.contentUri, paramInt);
    paramParcel.writeParcelable(this.thumbnailUri, paramInt);
    paramParcel.writeParcelable(this.previewIntentUri, paramInt);
    paramParcel.writeString(this.partId);
    paramParcel.writeInt(this.origin);
    paramParcel.writeString(this.originExtras);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.Attachment
 * JD-Core Version:    0.6.2
 */