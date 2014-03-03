package com.android.ex.chips;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RecipientAlternatesAdapter extends CursorAdapter
{
  private OnCheckedItemChangedListener mCheckedItemChangedListener;
  private int mCheckedItemPosition = -1;
  private final long mCurrentId;
  private final LayoutInflater mLayoutInflater;
  private Queries.Query mQuery;

  public RecipientAlternatesAdapter(Context paramContext, long paramLong1, long paramLong2, int paramInt1, int paramInt2, OnCheckedItemChangedListener paramOnCheckedItemChangedListener)
  {
    super(paramContext, getCursorForConstruction(paramContext, paramLong1, paramInt2), 0);
    this.mLayoutInflater = LayoutInflater.from(paramContext);
    this.mCurrentId = paramLong2;
    this.mCheckedItemChangedListener = paramOnCheckedItemChangedListener;
    if (paramInt2 == 0)
    {
      this.mQuery = Queries.EMAIL;
      return;
    }
    if (paramInt2 == 1)
    {
      this.mQuery = Queries.PHONE;
      return;
    }
    this.mQuery = Queries.EMAIL;
    Log.e("RecipAlternates", "Unsupported query type: " + paramInt2);
  }

  private static Cursor getCursorForConstruction(Context paramContext, long paramLong, int paramInt)
  {
    ContentResolver localContentResolver2;
    Uri localUri2;
    String[] arrayOfString3;
    String str2;
    String[] arrayOfString4;
    if (paramInt == 0)
    {
      localContentResolver2 = paramContext.getContentResolver();
      localUri2 = Queries.EMAIL.getContentUri();
      arrayOfString3 = Queries.EMAIL.getProjection();
      str2 = Queries.EMAIL.getProjection()[4] + " =?";
      arrayOfString4 = new String[1];
      arrayOfString4[0] = String.valueOf(paramLong);
    }
    ContentResolver localContentResolver1;
    Uri localUri1;
    String[] arrayOfString1;
    String str1;
    String[] arrayOfString2;
    for (Cursor localCursor = localContentResolver2.query(localUri2, arrayOfString3, str2, arrayOfString4, null); ; localCursor = localContentResolver1.query(localUri1, arrayOfString1, str1, arrayOfString2, null))
    {
      return removeDuplicateDestinations(localCursor);
      localContentResolver1 = paramContext.getContentResolver();
      localUri1 = Queries.PHONE.getContentUri();
      arrayOfString1 = Queries.PHONE.getProjection();
      str1 = Queries.PHONE.getProjection()[4] + " =?";
      arrayOfString2 = new String[1];
      arrayOfString2[0] = String.valueOf(paramLong);
    }
  }

  public static HashMap<String, RecipientEntry> getMatchingRecipients(Context paramContext, ArrayList<String> paramArrayList)
  {
    return getMatchingRecipients(paramContext, paramArrayList, 0);
  }

  public static HashMap<String, RecipientEntry> getMatchingRecipients(Context paramContext, ArrayList<String> paramArrayList, int paramInt)
  {
    Queries.Query localQuery;
    int i;
    String[] arrayOfString;
    StringBuilder localStringBuilder;
    int j;
    label38: Rfc822Token[] arrayOfRfc822Token;
    if (paramInt == 0)
    {
      localQuery = Queries.EMAIL;
      i = Math.min(50, paramArrayList.size());
      arrayOfString = new String[i];
      localStringBuilder = new StringBuilder();
      j = 0;
      if (j >= i)
        break label140;
      arrayOfRfc822Token = Rfc822Tokenizer.tokenize(((String)paramArrayList.get(j)).toLowerCase());
      if (arrayOfRfc822Token.length <= 0)
        break label126;
    }
    label126: for (String str2 = arrayOfRfc822Token[0].getAddress(); ; str2 = (String)paramArrayList.get(j))
    {
      arrayOfString[j] = str2;
      localStringBuilder.append("?");
      int k = i - 1;
      if (j < k)
        localStringBuilder.append(",");
      j++;
      break label38;
      localQuery = Queries.PHONE;
      break;
    }
    label140: if (Log.isLoggable("RecipAlternates", 3))
      Log.d("RecipAlternates", "Doing reverse lookup for " + arrayOfString.toString());
    HashMap localHashMap = new HashMap();
    Cursor localCursor = paramContext.getContentResolver().query(localQuery.getContentUri(), localQuery.getProjection(), localQuery.getProjection()[1] + " IN (" + localStringBuilder.toString() + ")", arrayOfString, null);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        boolean bool;
        do
        {
          String str1 = localCursor.getString(1);
          localHashMap.put(str1, RecipientEntry.constructTopLevelEntry(localCursor.getString(0), localCursor.getInt(7), localCursor.getString(1), localCursor.getInt(2), localCursor.getString(3), localCursor.getLong(4), localCursor.getLong(5), localCursor.getString(6)));
          if (Log.isLoggable("RecipAlternates", 3))
            Log.d("RecipAlternates", "Received reverse look up information for " + str1 + " RESULTS: " + " NAME : " + localCursor.getString(0) + " CONTACT ID : " + localCursor.getLong(4) + " ADDRESS :" + localCursor.getString(1));
          bool = localCursor.moveToNext();
        }
        while (bool);
      }
      return localHashMap;
    }
    finally
    {
      localCursor.close();
    }
  }

  private View newView()
  {
    return this.mLayoutInflater.inflate(R.layout.chips_recipient_dropdown_item, null);
  }

  static Cursor removeDuplicateDestinations(Cursor paramCursor)
  {
    MatrixCursor localMatrixCursor = new MatrixCursor(paramCursor.getColumnNames(), paramCursor.getCount());
    HashSet localHashSet = new HashSet();
    paramCursor.moveToPosition(-1);
    while (paramCursor.moveToNext())
    {
      String str = paramCursor.getString(1);
      if (!localHashSet.contains(str))
      {
        localHashSet.add(str);
        Object[] arrayOfObject = new Object[8];
        arrayOfObject[0] = paramCursor.getString(0);
        arrayOfObject[1] = paramCursor.getString(1);
        arrayOfObject[2] = Integer.valueOf(paramCursor.getInt(2));
        arrayOfObject[3] = paramCursor.getString(3);
        arrayOfObject[4] = Long.valueOf(paramCursor.getLong(4));
        arrayOfObject[5] = Long.valueOf(paramCursor.getLong(5));
        arrayOfObject[6] = paramCursor.getString(6);
        arrayOfObject[7] = Integer.valueOf(paramCursor.getInt(7));
        localMatrixCursor.addRow(arrayOfObject);
      }
    }
    return localMatrixCursor;
  }

  public void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    int i = paramCursor.getPosition();
    TextView localTextView1 = (TextView)paramView.findViewById(16908310);
    ImageView localImageView = (ImageView)paramView.findViewById(16908294);
    RecipientEntry localRecipientEntry = getRecipientEntry(i);
    if (i == 0)
    {
      localTextView1.setText(paramCursor.getString(0));
      localTextView1.setVisibility(0);
      localImageView.setImageURI(localRecipientEntry.getPhotoThumbnailUri());
      localImageView.setVisibility(0);
    }
    while (true)
    {
      ((TextView)paramView.findViewById(16908308)).setText(paramCursor.getString(1));
      TextView localTextView2 = (TextView)paramView.findViewById(16908309);
      if (localTextView2 != null)
        localTextView2.setText(this.mQuery.getTypeLabel(paramContext.getResources(), paramCursor.getInt(2), paramCursor.getString(3)).toString().toUpperCase());
      return;
      localTextView1.setVisibility(8);
      localImageView.setVisibility(8);
    }
  }

  public long getItemId(int paramInt)
  {
    Cursor localCursor = getCursor();
    if (localCursor.moveToPosition(paramInt))
      localCursor.getLong(5);
    return -1L;
  }

  public RecipientEntry getRecipientEntry(int paramInt)
  {
    Cursor localCursor = getCursor();
    localCursor.moveToPosition(paramInt);
    return RecipientEntry.constructTopLevelEntry(localCursor.getString(0), localCursor.getInt(7), localCursor.getString(1), localCursor.getInt(2), localCursor.getString(3), localCursor.getLong(4), localCursor.getLong(5), localCursor.getString(6));
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Cursor localCursor = getCursor();
    localCursor.moveToPosition(paramInt);
    if (paramView == null)
      paramView = newView();
    if (localCursor.getLong(5) == this.mCurrentId)
    {
      this.mCheckedItemPosition = paramInt;
      if (this.mCheckedItemChangedListener != null)
        this.mCheckedItemChangedListener.onCheckedItemChanged(this.mCheckedItemPosition);
    }
    bindView(paramView, paramView.getContext(), localCursor);
    return paramView;
  }

  public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    return newView();
  }

  static abstract interface OnCheckedItemChangedListener
  {
    public abstract void onCheckedItemChanged(int paramInt);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.chips.RecipientAlternatesAdapter
 * JD-Core Version:    0.6.2
 */