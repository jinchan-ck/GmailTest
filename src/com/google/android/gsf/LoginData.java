package com.google.android.gsf;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class LoginData
  implements Parcelable
{
  public static final Parcelable.Creator<LoginData> CREATOR = new Parcelable.Creator()
  {
    public LoginData createFromParcel(Parcel paramAnonymousParcel)
    {
      return new LoginData(paramAnonymousParcel, null);
    }

    public LoginData[] newArray(int paramAnonymousInt)
    {
      return new LoginData[paramAnonymousInt];
    }
  };
  public String mAuthtoken = null;
  public String mCaptchaAnswer = null;
  public byte[] mCaptchaData = null;
  public String mCaptchaMimeType = null;
  public String mCaptchaToken = null;
  public String mEncryptedPassword = null;
  public int mFlags = 0;
  public String mJsonString = null;
  public String mOAuthAccessToken = null;
  public String mPassword = null;
  public String mService = null;
  public String mSid = null;
  public Status mStatus = null;
  public String mUsername = null;

  public LoginData()
  {
  }

  private LoginData(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }

  public LoginData(LoginData paramLoginData)
  {
    this.mUsername = paramLoginData.mUsername;
    this.mEncryptedPassword = paramLoginData.mEncryptedPassword;
    this.mPassword = paramLoginData.mPassword;
    this.mService = paramLoginData.mService;
    this.mCaptchaToken = paramLoginData.mCaptchaToken;
    this.mCaptchaData = paramLoginData.mCaptchaData;
    this.mCaptchaMimeType = paramLoginData.mCaptchaMimeType;
    this.mCaptchaAnswer = paramLoginData.mCaptchaAnswer;
    this.mFlags = paramLoginData.mFlags;
    this.mStatus = paramLoginData.mStatus;
    this.mJsonString = paramLoginData.mJsonString;
    this.mSid = paramLoginData.mSid;
    this.mAuthtoken = paramLoginData.mAuthtoken;
    this.mOAuthAccessToken = paramLoginData.mOAuthAccessToken;
  }

  public int describeContents()
  {
    return 0;
  }

  public String dump()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("         status: ");
    localStringBuilder.append(this.mStatus);
    localStringBuilder.append("\n       username: ");
    localStringBuilder.append(this.mUsername);
    localStringBuilder.append("\n       password: ");
    localStringBuilder.append(this.mPassword);
    localStringBuilder.append("\n   enc password: ");
    localStringBuilder.append(this.mEncryptedPassword);
    localStringBuilder.append("\n        service: ");
    localStringBuilder.append(this.mService);
    localStringBuilder.append("\n      authtoken: ");
    localStringBuilder.append(this.mAuthtoken);
    localStringBuilder.append("\n      oauthAccessToken: ");
    localStringBuilder.append(this.mOAuthAccessToken);
    localStringBuilder.append("\n   captchatoken: ");
    localStringBuilder.append(this.mCaptchaToken);
    localStringBuilder.append("\n  captchaanswer: ");
    localStringBuilder.append(this.mCaptchaAnswer);
    localStringBuilder.append("\n    captchadata: ");
    if (this.mCaptchaData == null);
    for (String str = "null"; ; str = Integer.toString(this.mCaptchaData.length) + " bytes")
    {
      localStringBuilder.append(str);
      return localStringBuilder.toString();
    }
  }

  public void readFromParcel(Parcel paramParcel)
  {
    this.mUsername = paramParcel.readString();
    this.mEncryptedPassword = paramParcel.readString();
    this.mPassword = paramParcel.readString();
    this.mService = paramParcel.readString();
    this.mCaptchaToken = paramParcel.readString();
    int i = paramParcel.readInt();
    String str;
    if (i == -1)
    {
      this.mCaptchaData = null;
      this.mCaptchaMimeType = paramParcel.readString();
      this.mCaptchaAnswer = paramParcel.readString();
      this.mFlags = paramParcel.readInt();
      str = paramParcel.readString();
      if (str != null)
        break label144;
    }
    label144: for (this.mStatus = null; ; this.mStatus = Status.valueOf(str))
    {
      this.mJsonString = paramParcel.readString();
      this.mSid = paramParcel.readString();
      this.mAuthtoken = paramParcel.readString();
      this.mOAuthAccessToken = paramParcel.readString();
      return;
      this.mCaptchaData = new byte[i];
      paramParcel.readByteArray(this.mCaptchaData);
      break;
    }
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.mUsername);
    paramParcel.writeString(this.mEncryptedPassword);
    paramParcel.writeString(this.mPassword);
    paramParcel.writeString(this.mService);
    paramParcel.writeString(this.mCaptchaToken);
    if (this.mCaptchaData == null)
    {
      paramParcel.writeInt(-1);
      paramParcel.writeString(this.mCaptchaMimeType);
      paramParcel.writeString(this.mCaptchaAnswer);
      paramParcel.writeInt(this.mFlags);
      if (this.mStatus != null)
        break label141;
      paramParcel.writeString(null);
    }
    while (true)
    {
      paramParcel.writeString(this.mJsonString);
      paramParcel.writeString(this.mSid);
      paramParcel.writeString(this.mAuthtoken);
      paramParcel.writeString(this.mOAuthAccessToken);
      return;
      paramParcel.writeInt(this.mCaptchaData.length);
      paramParcel.writeByteArray(this.mCaptchaData);
      break;
      label141: paramParcel.writeString(this.mStatus.name());
    }
  }

  public static enum Status
  {
    static
    {
      ACCOUNT_DISABLED = new Status("ACCOUNT_DISABLED", 1);
      BAD_USERNAME = new Status("BAD_USERNAME", 2);
      BAD_REQUEST = new Status("BAD_REQUEST", 3);
      LOGIN_FAIL = new Status("LOGIN_FAIL", 4);
      SERVER_ERROR = new Status("SERVER_ERROR", 5);
      MISSING_APPS = new Status("MISSING_APPS", 6);
      NO_GMAIL = new Status("NO_GMAIL", 7);
      NETWORK_ERROR = new Status("NETWORK_ERROR", 8);
      CAPTCHA = new Status("CAPTCHA", 9);
      CANCELLED = new Status("CANCELLED", 10);
      DELETED_GMAIL = new Status("DELETED_GMAIL", 11);
      OAUTH_MIGRATION_REQUIRED = new Status("OAUTH_MIGRATION_REQUIRED", 12);
      Status[] arrayOfStatus = new Status[13];
      arrayOfStatus[0] = SUCCESS;
      arrayOfStatus[1] = ACCOUNT_DISABLED;
      arrayOfStatus[2] = BAD_USERNAME;
      arrayOfStatus[3] = BAD_REQUEST;
      arrayOfStatus[4] = LOGIN_FAIL;
      arrayOfStatus[5] = SERVER_ERROR;
      arrayOfStatus[6] = MISSING_APPS;
      arrayOfStatus[7] = NO_GMAIL;
      arrayOfStatus[8] = NETWORK_ERROR;
      arrayOfStatus[9] = CAPTCHA;
      arrayOfStatus[10] = CANCELLED;
      arrayOfStatus[11] = DELETED_GMAIL;
      arrayOfStatus[12] = OAUTH_MIGRATION_REQUIRED;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gsf.LoginData
 * JD-Core Version:    0.6.2
 */