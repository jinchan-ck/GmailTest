package com.android.mail.providers.protos.mock;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MockUiProvider extends ContentProvider
{
  private static final Uri MOCK_ACCOUNTS_URI = Uri.parse("content://com.android.mail.mockprovider/accounts");
  private static Map<String, List<Map<String, Object>>> MOCK_QUERY_RESULTS = Maps.newHashMap();

  static Uri getAccountsUri()
  {
    return Uri.parse("content://com.android.mail.mockprovider/");
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }

  public String getType(Uri paramUri)
  {
    return null;
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    return paramUri;
  }

  public boolean onCreate()
  {
    return true;
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    List localList = (List)MOCK_QUERY_RESULTS.get(paramUri.toString());
    MatrixCursor localMatrixCursor;
    Iterator localIterator;
    if ((localList != null) && (localList.size() > 0))
    {
      if (paramArrayOfString1 == null)
      {
        Set localSet = ((Map)localList.get(0)).keySet();
        paramArrayOfString1 = (String[])localSet.toArray(new String[localSet.size()]);
      }
      localMatrixCursor = new MatrixCursor(paramArrayOfString1, localList.size());
      localIterator = localList.iterator();
    }
    while (localIterator.hasNext())
    {
      Map localMap = (Map)localIterator.next();
      MatrixCursor.RowBuilder localRowBuilder = localMatrixCursor.newRow();
      String[] arrayOfString = paramArrayOfString1;
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++)
        localRowBuilder.add(localMap.get(arrayOfString[j]));
      continue;
      localMatrixCursor = null;
    }
    return localMatrixCursor;
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.protos.mock.MockUiProvider
 * JD-Core Version:    0.6.2
 */