package com.android.mail.utils;

import android.database.MatrixCursor;
import android.os.Bundle;

public class MatrixCursorWithExtra extends MatrixCursor
{
  private final Bundle mExtras;

  public MatrixCursorWithExtra(String[] paramArrayOfString, int paramInt, Bundle paramBundle)
  {
    super(paramArrayOfString, paramInt);
    this.mExtras = paramBundle;
  }

  public Bundle getExtras()
  {
    return this.mExtras;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.MatrixCursorWithExtra
 * JD-Core Version:    0.6.2
 */