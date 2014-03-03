package com.android.ex.photo.util;

import android.util.Log;

public class Exif
{
  public static int getOrientation(byte[] paramArrayOfByte)
  {
    int i = 1;
    if (paramArrayOfByte == null)
      return 0;
    int j = 0;
    int i7;
    int i8;
    do
    {
      while (true)
      {
        int k = j + 3;
        int m = paramArrayOfByte.length;
        n = 0;
        if (k >= m)
          break label110;
        i7 = j + 1;
        if ((0xFF & paramArrayOfByte[j]) != 255)
          break label456;
        i8 = 0xFF & paramArrayOfByte[i7];
        if (i8 != 255)
          break;
        j = i7;
      }
      j = i7 + 1;
    }
    while ((i8 == 216) || (i8 == i));
    int n = 0;
    if (i8 != 217)
    {
      n = 0;
      if (i8 != 218)
        break label150;
    }
    while (true)
    {
      label110: if (n > 8)
      {
        int i1 = pack(paramArrayOfByte, j, 4, false);
        if ((i1 != 1229531648) && (i1 != 1296891946))
        {
          Log.e("CameraExif", "Invalid byte order");
          return 0;
          int i9 = pack(paramArrayOfByte, j, 2, false);
          if ((i9 < 2) || (j + i9 > paramArrayOfByte.length))
          {
            Log.e("CameraExif", "Invalid length");
            return 0;
          }
          if ((i8 == 225) && (i9 >= 8) && (pack(paramArrayOfByte, j + 2, 4, false) == 1165519206) && (pack(paramArrayOfByte, j + 6, 2, false) == 0))
          {
            j += 8;
            n = i9 - 8;
            continue;
          }
          j += i9;
          break;
        }
        if (i1 == 1229531648);
        int i2;
        while (true)
        {
          i2 = 2 + pack(paramArrayOfByte, j + 4, 4, i);
          if ((i2 >= 10) && (i2 <= n))
            break;
          Log.e("CameraExif", "Invalid offset");
          return 0;
          i = 0;
        }
        int i3 = j + i2;
        int i4 = n - i2;
        int i6;
        for (int i5 = pack(paramArrayOfByte, i3 - 2, 2, i); ; i5 = i6)
        {
          i6 = i5 - 1;
          if ((i5 <= 0) || (i4 < 12))
            break;
          if (pack(paramArrayOfByte, i3, 2, i) == 274)
          {
            switch (pack(paramArrayOfByte, i3 + 8, 2, i))
            {
            case 1:
            case 2:
            case 4:
            case 5:
            case 7:
            default:
              Log.i("CameraExif", "Unsupported orientation");
              return 0;
            case 3:
              return 180;
            case 6:
              return 90;
            case 8:
            }
            return 270;
          }
          i3 += 12;
          i4 -= 12;
        }
      }
      label150: Log.i("CameraExif", "Orientation not found");
      return 0;
      label456: j = i7;
      n = 0;
    }
  }

  private static int pack(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = 1;
    if (paramBoolean)
    {
      paramInt1 += paramInt2 - 1;
      i = -1;
    }
    int j = 0;
    int m;
    for (int k = paramInt2; ; k = m)
    {
      m = k - 1;
      if (k <= 0)
        break;
      j = j << 8 | 0xFF & paramArrayOfByte[paramInt1];
      paramInt1 += i;
    }
    return j;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.util.Exif
 * JD-Core Version:    0.6.2
 */