package com.android.mail;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.util.Pair;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SenderInfoLoader extends AsyncTaskLoader<ImmutableMap<String, ContactInfo>>
{
  private static final String[] DATA_COLS = { "_id", "data1", "contact_presence", "contact_id", "photo_id" };
  private static final String[] PHOTO_COLS = { "_id", "data15" };
  private final Set<String> mSenders;

  public SenderInfoLoader(Context paramContext, Set<String> paramSet)
  {
    super(paramContext);
    this.mSenders = paramSet;
  }

  static void appendQuestionMarks(StringBuilder paramStringBuilder, Iterable<?> paramIterable)
  {
    int i = 1;
    Iterator localIterator = paramIterable.iterator();
    if (localIterator.hasNext())
    {
      localIterator.next();
      if (i != 0)
        i = 0;
      while (true)
      {
        paramStringBuilder.append('?');
        break;
        paramStringBuilder.append(',');
      }
    }
  }

  static ArrayList<String> getTruncatedQueryParams(Collection<String> paramCollection)
  {
    int i = Math.min(paramCollection.size(), 75);
    ArrayList localArrayList = new ArrayList(i);
    int j = 0;
    Iterator localIterator = paramCollection.iterator();
    do
    {
      if (!localIterator.hasNext())
        break;
      localArrayList.add((String)localIterator.next());
      j++;
    }
    while (j < i);
    return localArrayList;
  }

  private static String[] toStringArray(Collection<String> paramCollection)
  {
    return (String[])paramCollection.toArray(new String[paramCollection.size()]);
  }

  public ImmutableMap<String, ContactInfo> loadInBackground()
  {
    Object localObject1;
    if ((this.mSenders == null) || (this.mSenders.isEmpty()))
      localObject1 = null;
    Object localObject2;
    HashMap localHashMap1;
    HashMap localHashMap2;
    do
    {
      ArrayList localArrayList1;
      StringBuilder localStringBuilder;
      do
      {
        while (true)
        {
          return localObject1;
          localObject2 = null;
          localHashMap1 = Maps.newHashMap();
          localHashMap2 = Maps.newHashMap();
          localArrayList1 = new ArrayList();
          ArrayList localArrayList2 = getTruncatedQueryParams(this.mSenders);
          localStringBuilder = new StringBuilder().append("mimetype").append("='").append("vnd.android.cursor.item/email_v2").append("' AND ").append("data1").append(" IN (");
          appendQuestionMarks(localStringBuilder, localArrayList2);
          localStringBuilder.append(')');
          try
          {
            Cursor localCursor = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, DATA_COLS, localStringBuilder.toString(), toStringArray(localArrayList2), null);
            localObject2 = localCursor;
            if (localObject2 == null)
            {
              localObject1 = null;
              return null;
            }
            int i = -1;
            while (true)
            {
              i++;
              if (!((Cursor)localObject2).moveToPosition(i))
                break;
              String str2 = ((Cursor)localObject2).getString(1);
              long l1 = ((Cursor)localObject2).getLong(3);
              Uri localUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, l1);
              boolean bool = ((Cursor)localObject2).isNull(2);
              Integer localInteger = null;
              if (!bool)
                localInteger = Integer.valueOf(((Cursor)localObject2).getInt(2));
              ContactInfo localContactInfo2 = new ContactInfo(localUri, localInteger, null);
              if (!((Cursor)localObject2).isNull(4))
              {
                long l2 = ((Cursor)localObject2).getLong(4);
                localArrayList1.add(Long.toString(l2));
                localHashMap2.put(Long.valueOf(l2), Pair.create(str2, localContactInfo2));
              }
              localHashMap1.put(str2, localContactInfo2);
            }
          }
          finally
          {
            if (localObject2 != null)
              ((Cursor)localObject2).close();
          }
        }
        ((Cursor)localObject2).close();
        if (!localArrayList1.isEmpty())
          break;
        ImmutableMap localImmutableMap2 = ImmutableMap.copyOf(localHashMap1);
        localObject1 = localImmutableMap2;
      }
      while (localObject2 == null);
      ((Cursor)localObject2).close();
      return localObject1;
      ArrayList localArrayList3 = getTruncatedQueryParams(localArrayList1);
      localStringBuilder.setLength(0);
      localStringBuilder.append("_id").append(" IN (");
      appendQuestionMarks(localStringBuilder, localArrayList3);
      localStringBuilder.append(')');
      localObject2 = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, PHOTO_COLS, localStringBuilder.toString(), toStringArray(localArrayList3), null);
      if (localObject2 != null)
        break;
      ImmutableMap localImmutableMap1 = ImmutableMap.copyOf(localHashMap1);
      localObject1 = localImmutableMap1;
    }
    while (localObject2 == null);
    ((Cursor)localObject2).close();
    return localObject1;
    int j = -1;
    while (true)
    {
      j++;
      if (!((Cursor)localObject2).moveToPosition(j))
        break;
      byte[] arrayOfByte = ((Cursor)localObject2).getBlob(1);
      if (arrayOfByte != null)
      {
        Pair localPair = (Pair)localHashMap2.get(Long.valueOf(((Cursor)localObject2).getLong(0)));
        String str1 = (String)localPair.first;
        ContactInfo localContactInfo1 = (ContactInfo)localPair.second;
        Bitmap localBitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
        localHashMap1.put(str1, new ContactInfo(localContactInfo1.contactUri, localContactInfo1.status, localBitmap));
      }
    }
    if (localObject2 != null)
      ((Cursor)localObject2).close();
    return ImmutableMap.copyOf(localHashMap1);
  }

  protected void onStartLoading()
  {
    forceLoad();
  }

  protected void onStopLoading()
  {
    cancelLoad();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.SenderInfoLoader
 * JD-Core Version:    0.6.2
 */