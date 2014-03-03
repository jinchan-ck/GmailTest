package com.android.mail.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public class SearchRecentSuggestionsProvider extends ContentProvider
{
  private String mAuthority;
  private ArrayList<String> mFullQueryTerms;
  private int mMode;
  private SQLiteOpenHelper mOpenHelper;
  private String mSuggestSuggestionClause;
  private String[] mSuggestionProjection;
  private Uri mSuggestionsUri;
  private UriMatcher mUriMatcher;

  private String[] createProjection(String[] paramArrayOfString)
  {
    String[] arrayOfString = new String[this.mSuggestionProjection.length];
    if (this.mFullQueryTerms != null);
    String str2;
    for (int i = this.mFullQueryTerms.size(); ; i = 0)
    {
      if (i <= 0)
        break label171;
      str2 = "'";
      for (int k = 0; k < i; k++)
      {
        str2 = str2 + (String)this.mFullQueryTerms.get(k);
        if (k < i - 1)
          str2 = str2 + " ";
      }
    }
    label171: for (String str1 = str2 + " ' || query AS " + "suggest_intent_query"; ; str1 = "query AS suggest_intent_query")
      for (int j = 0; j < this.mSuggestionProjection.length; j++)
        arrayOfString[j] = this.mSuggestionProjection[j];
    arrayOfString[(-2 + this.mSuggestionProjection.length)] = str1;
    return arrayOfString;
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getWritableDatabase();
    if (paramUri.getPathSegments().size() != 1)
      throw new IllegalArgumentException("Unknown Uri");
    if (((String)paramUri.getPathSegments().get(0)).equals("suggestions"))
    {
      int i = localSQLiteDatabase.delete("suggestions", paramString, paramArrayOfString);
      getContext().getContentResolver().notifyChange(paramUri, null);
      return i;
    }
    throw new IllegalArgumentException("Unknown Uri");
  }

  public String getType(Uri paramUri)
  {
    if (this.mUriMatcher.match(paramUri) == 1)
      return "vnd.android.cursor.dir/vnd.android.search.suggest";
    int i = paramUri.getPathSegments().size();
    if ((i >= 1) && (((String)paramUri.getPathSegments().get(0)).equals("suggestions")))
    {
      if (i == 1)
        return "vnd.android.cursor.dir/suggestion";
      if (i == 2)
        return "vnd.android.cursor.item/suggestion";
    }
    throw new IllegalArgumentException("Unknown Uri");
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getWritableDatabase();
    int i = paramUri.getPathSegments().size();
    if (i < 1)
      throw new IllegalArgumentException("Unknown Uri");
    long l = -1L;
    boolean bool1 = ((String)paramUri.getPathSegments().get(0)).equals("suggestions");
    Uri localUri = null;
    if (bool1)
    {
      localUri = null;
      if (i == 1)
      {
        l = localSQLiteDatabase.insert("suggestions", "query", paramContentValues);
        boolean bool2 = l < 0L;
        localUri = null;
        if (bool2)
          localUri = Uri.withAppendedPath(this.mSuggestionsUri, String.valueOf(l));
      }
    }
    if (l < 0L)
      throw new IllegalArgumentException("Unknown Uri");
    getContext().getContentResolver().notifyChange(localUri, null);
    return localUri;
  }

  public boolean onCreate()
  {
    if ((this.mAuthority == null) || (this.mMode == 0))
      throw new IllegalArgumentException("Provider not configured");
    int i = 512 + this.mMode;
    this.mOpenHelper = new DatabaseHelper(getContext(), i);
    return true;
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getReadableDatabase();
    String str;
    String[] arrayOfString;
    if (TextUtils.isEmpty(paramArrayOfString2[0]))
    {
      str = null;
      arrayOfString = null;
    }
    while (true)
    {
      Cursor localCursor = localSQLiteDatabase.query("suggestions", createProjection(paramArrayOfString2), str, arrayOfString, null, null, "date DESC", null);
      localCursor.setNotificationUri(getContext().getContentResolver(), paramUri);
      return localCursor;
      arrayOfString = new String[] { "%" + paramArrayOfString2[0] + "%" };
      str = this.mSuggestSuggestionClause;
    }
  }

  public void setFullQueryTerms(ArrayList<String> paramArrayList)
  {
    this.mFullQueryTerms = paramArrayList;
  }

  protected void setupSuggestions(String paramString, int paramInt)
  {
    if ((TextUtils.isEmpty(paramString)) || ((paramInt & 0x1) == 0))
      throw new IllegalArgumentException();
    this.mAuthority = new String(paramString);
    this.mMode = paramInt;
    this.mSuggestionsUri = Uri.parse("content://" + this.mAuthority + "/suggestions");
    this.mUriMatcher = new UriMatcher(-1);
    this.mUriMatcher.addURI(this.mAuthority, "search_suggest_query", 1);
    String str = "android.resource://" + getContext().getPackageName() + "/" + 2130837577;
    this.mSuggestSuggestionClause = "display1 LIKE ?";
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "display1 AS suggest_text_1";
    arrayOfString[2] = "query AS suggest_intent_query";
    arrayOfString[3] = ("'" + str + "' AS " + "suggest_icon_1");
    this.mSuggestionProjection = arrayOfString;
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  private static class DatabaseHelper extends SQLiteOpenHelper
  {
    public DatabaseHelper(Context paramContext, int paramInt)
    {
      super("suggestions.db", null, paramInt);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CREATE TABLE suggestions (_id INTEGER PRIMARY KEY,display1 TEXT UNIQUE ON CONFLICT REPLACE,query TEXT,date LONG);");
      paramSQLiteDatabase.execSQL(localStringBuilder.toString());
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS suggestions");
      onCreate(paramSQLiteDatabase);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.SearchRecentSuggestionsProvider
 * JD-Core Version:    0.6.2
 */