package com.google.android.gm.provider.uiprovider;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.text.TextUtils;

public class UICursorWapper extends CursorWrapper
{
  protected final String[] mResultProjection;

  public UICursorWapper(Cursor paramCursor, String[] paramArrayOfString)
  {
    super(paramCursor);
    this.mResultProjection = paramArrayOfString;
  }

  private static String toNonnullString(String paramString)
  {
    if (paramString == null)
      paramString = "";
    return paramString;
  }

  public int getColumnIndex(String paramString)
  {
    for (int i = 0; i < this.mResultProjection.length; i++)
      if (TextUtils.equals(paramString, this.mResultProjection[i]))
        return i;
    return -1;
  }

  public int getColumnIndexOrThrow(String paramString)
    throws IllegalArgumentException
  {
    int i = getColumnIndex(paramString);
    if (i == -1)
      throw new IllegalArgumentException("Column not found: " + paramString);
    return i;
  }

  public String getColumnName(int paramInt)
  {
    return this.mResultProjection[paramInt];
  }

  public String[] getColumnNames()
  {
    return this.mResultProjection;
  }

  protected String getStringInColumn(int paramInt)
  {
    return toNonnullString(super.getString(paramInt));
  }

  public boolean move(int paramInt)
  {
    resetCursorRowState();
    return super.move(paramInt);
  }

  public boolean moveToFirst()
  {
    resetCursorRowState();
    return super.moveToFirst();
  }

  public boolean moveToNext()
  {
    resetCursorRowState();
    return super.moveToNext();
  }

  public boolean moveToPosition(int paramInt)
  {
    resetCursorRowState();
    return super.moveToPosition(paramInt);
  }

  public boolean moveToPrevious()
  {
    resetCursorRowState();
    return super.moveToPrevious();
  }

  protected void resetCursorRowState()
  {
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.uiprovider.UICursorWapper
 * JD-Core Version:    0.6.2
 */