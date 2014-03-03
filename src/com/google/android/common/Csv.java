package com.google.android.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class Csv
{
  public static final String COMMA = ",";
  public static final String NEWLINE = "\n";

  public static boolean parseLine(BufferedReader paramBufferedReader, List<String> paramList)
    throws IOException
  {
    String str = paramBufferedReader.readLine();
    if (str == null)
      return false;
    int i = 0;
    StringBuilder localStringBuilder = new StringBuilder();
    label22: int j = str.indexOf(',', i);
    int k = str.indexOf('"', i);
    if ((k == -1) || ((j != -1) && (j < k)))
      if (j != -1)
        break label226;
    label226: for (int m = str.length(); ; m = j)
    {
      localStringBuilder.append(str, i, m);
      paramList.add(localStringBuilder.toString());
      i = j + 1;
      if (i > 0)
        break;
      return true;
      if ((i > 0) && (str.charAt(i - 1) == '"'))
        localStringBuilder.append('"');
      localStringBuilder.append(str, i, k);
      int n;
      int i1;
      while (true)
      {
        n = k + 1;
        i1 = str.indexOf('"', n);
        if (i1 != -1)
          break;
        localStringBuilder.append(str, n, str.length()).append('\n');
        str = paramBufferedReader.readLine();
        if (str == null)
        {
          paramList.add(localStringBuilder.toString());
          return true;
        }
        k = -1;
      }
      localStringBuilder.append(str, n, i1);
      i = i1 + 1;
      break label22;
    }
  }

  public static void writeValue(String paramString, Appendable paramAppendable)
    throws IOException
  {
    int i = paramString.length();
    if (i == 0)
      return;
    int j = paramString.charAt(0);
    int k = paramString.charAt(i - 1);
    if ((j != 32) && (j != 9) && (k != 32) && (k != 9) && (paramString.indexOf('"') < 0) && (paramString.indexOf(',') < 0) && (paramString.indexOf('\r') < 0) && (paramString.indexOf('\n') < 0))
    {
      paramAppendable.append(paramString);
      return;
    }
    paramAppendable.append('"').append(paramString.replace("\"", "\"\"")).append('"');
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.Csv
 * JD-Core Version:    0.6.2
 */