package com.google.android.gm.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gm.contentprovider.GmailAccess;
import com.google.android.gm.contentprovider.PrivateGmailAccess;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PublicContentProvider extends ContentProvider
{
  private static final Set<String> VALID_PUBLIC_LABEL_PROJECTION_COLUMNS = ImmutableSet.copyOf(GmailAccess.LABEL_PROJECTION);
  private static final UriMatcher sUrlMatcher = new UriMatcher(-1);
  private ContentResolver mContentResolver;

  static
  {
    sUrlMatcher.addURI("com.google.android.gm", "*/labels", 1);
    sUrlMatcher.addURI("com.google.android.gm", "*/label/#", 2);
    sUrlMatcher.addURI("com.google.android.gm", "*/label/*", 3);
  }

  static void broadcastLabelNotifications(Context paramContext, String paramString, Set<Long> paramSet)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      Long localLong = (Long)localIterator.next();
      if (localLong != null)
        localContentResolver.notifyChange(PrivateGmailAccess.getLabelUriForId(paramString, localLong.longValue()), null, false);
    }
    localContentResolver.notifyChange(GmailAccess.getLabelsUri(paramString), null, false);
  }

  private Cursor getCursorForLabel(String[] paramArrayOfString, MailEngine paramMailEngine, String paramString)
  {
    Cursor localCursor = null;
    if (paramString != null)
      localCursor = paramMailEngine.getLabelQueryBuilder(paramArrayOfString).filterCanonicalName(ImmutableList.of(paramString)).showHidden(false).query();
    return localCursor;
  }

  private MailEngine getOrMakeMailEngine(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("account is empty");
    return MailEngine.getOrMakeMailEngine(getContext(), paramString);
  }

  private Cursor getPublicLabelCursor(String paramString, String[] paramArrayOfString, Cursor paramCursor)
  {
    Object localObject;
    if (paramCursor == null)
    {
      LogUtils.d("GmailCP", "null private cursor", new Object[0]);
      localObject = null;
    }
    String[] arrayOfString1;
    do
    {
      return localObject;
      validateQueryProjection(paramArrayOfString);
      if (paramArrayOfString == null)
        break;
      arrayOfString1 = paramArrayOfString;
      int i = paramCursor.getCount();
      localObject = new MatrixCursor(arrayOfString1, i);
    }
    while (!paramCursor.moveToFirst());
    int j = paramCursor.getColumnIndex("_id");
    int k = paramCursor.getColumnIndex("canonicalName");
    int m = paramCursor.getColumnIndex("name");
    int n = paramCursor.getColumnIndex("numConversations");
    int i1 = paramCursor.getColumnIndex("numUnreadConversations");
    int i2 = paramCursor.getColumnIndex("color");
    label173: 
    do
    {
      MatrixCursor.RowBuilder localRowBuilder = ((MatrixCursor)localObject).newRow();
      int i3 = paramCursor.getInt(j);
      String str1 = paramCursor.getString(i2);
      String str2 = paramCursor.getString(k);
      String[] arrayOfString2 = arrayOfString1;
      int i4 = arrayOfString2.length;
      int i5 = 0;
      if (i5 < i4)
      {
        String str3 = arrayOfString2[i5];
        if (TextUtils.equals(str3, "_id"))
          localRowBuilder.add(Integer.valueOf(i3));
        while (true)
        {
          i5++;
          break label173;
          arrayOfString1 = GmailAccess.LABEL_PROJECTION;
          break;
          if (TextUtils.equals(str3, "canonicalName"))
            localRowBuilder.add(str2);
          else if (TextUtils.equals(str3, "name"))
            localRowBuilder.add(paramCursor.getString(m));
          else if (TextUtils.equals(str3, "numConversations"))
            localRowBuilder.add(Integer.valueOf(paramCursor.getInt(n)));
          else if (TextUtils.equals(str3, "numUnreadConversations"))
            localRowBuilder.add(Integer.valueOf(paramCursor.getInt(i1)));
          else if (TextUtils.equals(str3, "labelUri"))
            localRowBuilder.add(PrivateGmailAccess.getLabelUriForId(paramString, i3).toString());
          else if (TextUtils.equals(str3, "background_color"))
            localRowBuilder.add(Integer.valueOf(Label.getBackgroundColor(paramString, str2, str1)));
          else if (TextUtils.equals(str3, "text_color"))
            localRowBuilder.add(Integer.valueOf(Label.getTextColor(paramString, str2, str1)));
        }
      }
    }
    while (paramCursor.moveToNext());
    return localObject;
  }

  private void validateQueryProjection(String[] paramArrayOfString)
  {
    if (paramArrayOfString != null)
    {
      int i = paramArrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        String str = paramArrayOfString[j];
        if (!VALID_PUBLIC_LABEL_PROJECTION_COLUMNS.contains(str))
          throw new IllegalArgumentException("Invalid projection");
      }
    }
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }

  public String getType(Uri paramUri)
  {
    switch (sUrlMatcher.match(paramUri))
    {
    default:
      return null;
    case 1:
      return "vnd.android.cursor.dir/vnd.com.google.android.gm.label";
    case 2:
    case 3:
    }
    return "vnd.android.cursor.item/vnd.com.google.android.gm.label";
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    return paramUri;
  }

  public boolean onCreate()
  {
    this.mContentResolver = getContext().getContentResolver();
    return true;
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    if (LogUtils.isLoggable("GmailCP", 3))
    {
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = LogUtils.contentUriToString(paramUri);
      arrayOfObject2[1] = paramString1;
      arrayOfObject2[2] = Arrays.toString(paramArrayOfString2);
      LogUtils.d("GmailCP", "PublicContentProvider.query: %s(%s, %s)", arrayOfObject2);
    }
    if (!TextUtils.isEmpty(paramString2))
      throw new IllegalArgumentException("sortOrder must be empty");
    int i = sUrlMatcher.match(paramUri);
    String str1 = (String)paramUri.getPathSegments().get(0);
    MailEngine localMailEngine = getOrMakeMailEngine(str1);
    int j = 1;
    MailIndexerService.onContentProviderAccess(str1);
    Object localObject2;
    switch (i)
    {
    default:
      if (LogUtils.isLoggable("GmailCP", 3))
      {
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = LogUtils.contentUriToString(paramUri);
        LogUtils.d("GmailCP", "Unsupported query uri: %s", arrayOfObject1);
      }
      localObject2 = null;
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      if ((localObject2 != null) && (j != 0))
        ((Cursor)localObject2).setNotificationUri(this.mContentResolver, paramUri);
      return localObject2;
      LogUtils.d("GmailCP", "Query for list of labels", new Object[0]);
      Cursor localCursor4 = localMailEngine.getLabelQueryBuilder(Gmail.LABEL_PROJECTION).showHidden(false).query();
      long l;
      String str3;
      try
      {
        Cursor localCursor5 = getPublicLabelCursor(str1, paramArrayOfString1, localCursor4);
        localObject2 = localCursor5;
        if (localCursor4 == null)
          continue;
        localCursor4.close();
      }
      finally
      {
        if (localCursor4 != null)
          localCursor4.close();
      }
      Cursor localCursor2 = getCursorForLabel(Gmail.LABEL_PROJECTION, localMailEngine, str3);
      String str2;
      Cursor localCursor1;
      try
      {
        Cursor localCursor3 = getPublicLabelCursor(str1, paramArrayOfString1, localCursor2);
        localObject2 = localCursor3;
        if (localCursor2 == null)
          continue;
        localCursor2.close();
      }
      finally
      {
        if (localCursor2 != null)
          localCursor2.close();
      }
      try
      {
        localObject2 = getPublicLabelCursor(str1, paramArrayOfString1, localCursor1);
        Object localObject3 = null;
        if (localCursor1 != null)
        {
          int k = localCursor1.getCount();
          localObject3 = null;
          if (k > 0)
          {
            boolean bool = localCursor1.moveToFirst();
            localObject3 = null;
            if (bool)
            {
              Long localLong = Long.valueOf(localCursor1.getLong(localCursor1.getColumnIndex("_id")));
              localObject3 = localLong;
            }
          }
        }
        if (localCursor1 != null)
          localCursor1.close();
        if ((localObject2 == null) || (localObject3 == null))
          continue;
        ((Cursor)localObject2).setNotificationUri(this.mContentResolver, PrivateGmailAccess.getLabelUriForId(str1, localObject3.longValue()));
        j = 0;
      }
      finally
      {
        if (localCursor1 != null)
          localCursor1.close();
      }
    }
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.PublicContentProvider
 * JD-Core Version:    0.6.2
 */