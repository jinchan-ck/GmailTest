package com.android.common.contacts;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DataUsageStatUpdater
{
  private static final String TAG = DataUsageStatUpdater.class.getSimpleName();
  private final ContentResolver mResolver;

  public DataUsageStatUpdater(Context paramContext)
  {
    this.mResolver = paramContext.getContentResolver();
  }

  private boolean update(Collection<Long> paramCollection1, Collection<Long> paramCollection2, String paramString)
  {
    long l = System.currentTimeMillis();
    if (Build.VERSION.SDK_INT >= 14)
      if (paramCollection2.isEmpty())
        if (Log.isLoggable(TAG, 3))
          Log.d(TAG, "Given list for data IDs is null. Ignoring.");
    do
    {
      do
      {
        do
        {
          return false;
          Uri localUri = DataUsageFeedback.FEEDBACK_URI.buildUpon().appendPath(TextUtils.join(",", paramCollection2)).appendQueryParameter("type", paramString).build();
          if (this.mResolver.update(localUri, new ContentValues(), null, null) > 0)
            return true;
        }
        while (!Log.isLoggable(TAG, 3));
        Log.d(TAG, "update toward data rows " + paramCollection2 + " failed");
        return false;
        if (!paramCollection1.isEmpty())
          break;
      }
      while (!Log.isLoggable(TAG, 3));
      Log.d(TAG, "Given list for contact IDs is null. Ignoring.");
      return false;
      StringBuilder localStringBuilder = new StringBuilder();
      ArrayList localArrayList = new ArrayList();
      String[] arrayOfString = new String[paramCollection1.size()];
      Iterator localIterator = paramCollection1.iterator();
      while (localIterator.hasNext())
        localArrayList.add(String.valueOf(((Long)localIterator.next()).longValue()));
      Arrays.fill(arrayOfString, "?");
      localStringBuilder.append("_id IN (").append(TextUtils.join(",", arrayOfString)).append(")");
      if (Log.isLoggable(TAG, 3))
      {
        Log.d(TAG, "contactId where: " + localStringBuilder.toString());
        Log.d(TAG, "contactId selection: " + localArrayList);
      }
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("last_time_contacted", Long.valueOf(l));
      if (this.mResolver.update(ContactsContract.Contacts.CONTENT_URI, localContentValues, localStringBuilder.toString(), (String[])localArrayList.toArray(new String[0])) > 0)
        return true;
    }
    while (!Log.isLoggable(TAG, 3));
    Log.d(TAG, "update toward raw contacts " + paramCollection1 + " failed");
    return false;
  }

  public boolean updateWithAddress(Collection<String> paramCollection)
  {
    if (Log.isLoggable(TAG, 3))
      Log.d(TAG, "updateWithAddress: " + Arrays.toString(paramCollection.toArray()));
    Cursor localCursor;
    if ((paramCollection != null) && (!paramCollection.isEmpty()))
    {
      ArrayList localArrayList = new ArrayList();
      StringBuilder localStringBuilder = new StringBuilder();
      String[] arrayOfString = new String[paramCollection.size()];
      localArrayList.addAll(paramCollection);
      Arrays.fill(arrayOfString, "?");
      localStringBuilder.append("data1 IN (").append(TextUtils.join(",", arrayOfString)).append(")");
      localCursor = this.mResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, new String[] { "contact_id", "_id" }, localStringBuilder.toString(), (String[])localArrayList.toArray(new String[0]), null);
      if (localCursor == null)
        Log.w(TAG, "Cursor for Email.CONTENT_URI became null.");
    }
    else
    {
      return false;
    }
    HashSet localHashSet1 = new HashSet(localCursor.getCount());
    HashSet localHashSet2 = new HashSet(localCursor.getCount());
    try
    {
      localCursor.move(-1);
      while (localCursor.moveToNext())
      {
        localHashSet1.add(Long.valueOf(localCursor.getLong(0)));
        localHashSet2.add(Long.valueOf(localCursor.getLong(1)));
      }
    }
    finally
    {
      localCursor.close();
    }
    localCursor.close();
    return update(localHashSet1, localHashSet2, "long_text");
  }

  public static final class DataUsageFeedback
  {
    static final Uri FEEDBACK_URI = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, "usagefeedback");
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.common.contacts.DataUsageStatUpdater
 * JD-Core Version:    0.6.2
 */