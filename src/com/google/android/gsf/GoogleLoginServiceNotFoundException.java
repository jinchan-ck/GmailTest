package com.google.android.gsf;

import android.util.AndroidException;

public class GoogleLoginServiceNotFoundException extends AndroidException
{
  private int mErrorCode;

  public GoogleLoginServiceNotFoundException(int paramInt)
  {
    super(GoogleLoginServiceConstants.getErrorCodeMessage(paramInt));
    this.mErrorCode = paramInt;
  }

  int getErrorCode()
  {
    return this.mErrorCode;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gsf.GoogleLoginServiceNotFoundException
 * JD-Core Version:    0.6.2
 */