package com.google.android.gsf;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

public class SubscribedFeeds
{
  public static Uri addFeed(ContentResolver paramContentResolver, String paramString1, Account paramAccount, String paramString2, String paramString3)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("feed", paramString1);
    localContentValues.put("_sync_account", paramAccount.name);
    localContentValues.put("_sync_account_type", paramAccount.type);
    localContentValues.put("authority", paramString2);
    localContentValues.put("service", paramString3);
    return paramContentResolver.insert(Feeds.CONTENT_URI, localContentValues);
  }

  public static final class Feeds
    implements BaseColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://subscribedfeeds/feeds");
    public static final Uri DELETED_CONTENT_URI = Uri.parse("content://subscribedfeeds/deleted_feeds");
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gsf.SubscribedFeeds
 * JD-Core Version:    0.6.2
 */