package com.android.mail.providers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.TextUtils;
import java.util.ArrayList;

public class SuggestionsProvider extends SearchRecentSuggestionsProvider
{
  private static final String[] CONTACTS_COLUMNS = { "_id", "suggest_text_1", "suggest_intent_query", "suggest_icon_1" };
  private static final String[] sContract = { "data4", "data1" };
  private ArrayList<String> mFullQueryTerms;
  private final Object mTermsLock = new Object();

  private String createQuery(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject1;
    int i;
    if (this.mFullQueryTerms != null)
    {
      localObject1 = this.mTermsLock;
      i = 0;
    }
    try
    {
      int j = this.mFullQueryTerms.size();
      while (i < j)
      {
        localStringBuilder.append((String)this.mFullQueryTerms.get(i)).append(" ");
        i++;
      }
      localStringBuilder.append(paramString);
      return localStringBuilder.toString();
    }
    finally
    {
    }
  }

  public boolean onCreate()
  {
    setupSuggestions(getContext().getString(2131427346), 1);
    super.onCreate();
    return true;
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    String str1 = paramArrayOfString2[0];
    MergeCursor localMergeCursor;
    String[] arrayOfString;
    String str2;
    synchronized (this.mTermsLock)
    {
      this.mFullQueryTerms = null;
      super.setFullQueryTerms(this.mFullQueryTerms);
      localMergeCursor = null;
      if (str1 != null)
      {
        arrayOfString = TextUtils.split(str1, " ");
        if ((arrayOfString == null) || (arrayOfString.length <= 1))
          break label242;
        str2 = arrayOfString[(-1 + arrayOfString.length)];
      }
    }
    while (true)
    {
      synchronized (this.mTermsLock)
      {
        this.mFullQueryTerms = new ArrayList();
        int i = 0;
        int j = -1 + arrayOfString.length;
        if (i < j)
        {
          this.mFullQueryTerms.add(arrayOfString[i]);
          i++;
          continue;
          localObject2 = finally;
          throw localObject2;
        }
        super.setFullQueryTerms(this.mFullQueryTerms);
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(super.query(paramUri, paramArrayOfString1, paramString1, new String[] { str2 }, paramString2));
        if (str2.length() >= 2)
          localArrayList.add(new ContactsCursor().query(str2));
        localMergeCursor = new MergeCursor((Cursor[])localArrayList.toArray(new Cursor[localArrayList.size()]));
        return localMergeCursor;
      }
      label242: str2 = str1.trim();
    }
  }

  private final class ContactsCursor extends MatrixCursor
  {
    private final Context mContext = SuggestionsProvider.this.getContext();

    public ContactsCursor()
    {
      super();
    }

    public ContactsCursor query(String paramString)
    {
      Uri localUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_FILTER_URI, Uri.encode(paramString));
      Cursor localCursor = this.mContext.getContentResolver().query(localUri, SuggestionsProvider.sContract, null, null, null);
      String str1 = "android.resource://" + this.mContext.getPackageName() + "/" + 2130837532;
      if (localCursor != null)
      {
        int i = localCursor.getColumnIndex("data4");
        int j = localCursor.getColumnIndex("data1");
        if (localCursor.moveToNext())
        {
          String str2 = localCursor.getString(i);
          if (!TextUtils.isEmpty(str2));
          while (true)
          {
            Object[] arrayOfObject = new Object[4];
            arrayOfObject[0] = Integer.valueOf(0);
            arrayOfObject[1] = str2;
            arrayOfObject[2] = SuggestionsProvider.this.createQuery(str2);
            arrayOfObject[3] = str1;
            addRow(arrayOfObject);
            break;
            str2 = localCursor.getString(j);
          }
        }
        localCursor.close();
      }
      return this;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.SuggestionsProvider
 * JD-Core Version:    0.6.2
 */