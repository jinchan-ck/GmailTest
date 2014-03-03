package com.android.common;

import android.database.AbstractCursor;
import android.database.CursorWindow;
import java.util.ArrayList;

public class ArrayListCursor extends AbstractCursor
{
  private String[] mColumnNames;
  private ArrayList<Object>[] mRows;

  public ArrayListCursor(String[] paramArrayOfString, ArrayList<ArrayList> paramArrayList)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; ; j++)
    {
      int k = 0;
      if (j < i)
      {
        if (paramArrayOfString[j].compareToIgnoreCase("_id") == 0)
        {
          this.mColumnNames = paramArrayOfString;
          k = 1;
        }
      }
      else
      {
        if (k == 0)
        {
          this.mColumnNames = new String[i + 1];
          System.arraycopy(paramArrayOfString, 0, this.mColumnNames, 0, paramArrayOfString.length);
          this.mColumnNames[i] = "_id";
        }
        int m = paramArrayList.size();
        this.mRows = new ArrayList[m];
        for (int n = 0; n < m; n++)
        {
          this.mRows[n] = ((ArrayList)paramArrayList.get(n));
          if (k == 0)
            this.mRows[n].add(Integer.valueOf(n));
        }
      }
    }
  }

  public void fillWindow(int paramInt, CursorWindow paramCursorWindow)
  {
    if ((paramInt < 0) || (paramInt > getCount()))
      return;
    paramCursorWindow.acquireReference();
    while (true)
    {
      int i;
      int k;
      try
      {
        i = this.mPos;
        this.mPos = (paramInt - 1);
        paramCursorWindow.clear();
        paramCursorWindow.setStartPosition(paramInt);
        int j = getColumnCount();
        paramCursorWindow.setNumColumns(j);
        if ((!moveToNext()) || (!paramCursorWindow.allocRow()))
          break label192;
        k = 0;
        if (k >= j)
          continue;
        localObject2 = this.mRows[this.mPos].get(k);
        if (localObject2 != null)
          if ((localObject2 instanceof byte[]))
          {
            if (paramCursorWindow.putBlob((byte[])localObject2, this.mPos, k))
              break label203;
            paramCursorWindow.freeLastRow();
            continue;
          }
      }
      catch (IllegalStateException localIllegalStateException)
      {
        Object localObject2;
        return;
        if (paramCursorWindow.putString(localObject2.toString(), this.mPos, k))
          break label203;
        paramCursorWindow.freeLastRow();
        continue;
      }
      finally
      {
        paramCursorWindow.releaseReference();
      }
      if (!paramCursorWindow.putNull(this.mPos, k))
      {
        paramCursorWindow.freeLastRow();
        continue;
        label192: this.mPos = i;
        paramCursorWindow.releaseReference();
      }
      else
      {
        label203: k++;
      }
    }
  }

  public byte[] getBlob(int paramInt)
  {
    return (byte[])this.mRows[this.mPos].get(paramInt);
  }

  public String[] getColumnNames()
  {
    return this.mColumnNames;
  }

  public int getCount()
  {
    return this.mRows.length;
  }

  public double getDouble(int paramInt)
  {
    return ((Number)this.mRows[this.mPos].get(paramInt)).doubleValue();
  }

  public float getFloat(int paramInt)
  {
    return ((Number)this.mRows[this.mPos].get(paramInt)).floatValue();
  }

  public int getInt(int paramInt)
  {
    return ((Number)this.mRows[this.mPos].get(paramInt)).intValue();
  }

  public long getLong(int paramInt)
  {
    return ((Number)this.mRows[this.mPos].get(paramInt)).longValue();
  }

  public short getShort(int paramInt)
  {
    return ((Number)this.mRows[this.mPos].get(paramInt)).shortValue();
  }

  public String getString(int paramInt)
  {
    Object localObject = this.mRows[this.mPos].get(paramInt);
    if (localObject == null)
      return null;
    return localObject.toString();
  }

  public boolean isNull(int paramInt)
  {
    return this.mRows[this.mPos].get(paramInt) == null;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.android.common.ArrayListCursor
 * JD-Core Version:    0.6.2
 */