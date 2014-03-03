package com.google.android.gm;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.util.Rfc822Token;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class EmailAddressAdapter extends ResourceCursorAdapter
{
  public static final int DATA_INDEX = 2;
  public static final int NAME_INDEX = 1;
  private static final String[] PROJECTION = { "_id", "display_name", "data1", "is_super_primary" };
  private static final String SORT_ORDER = "times_contacted DESC, display_name , is_super_primary DESC";
  private ContentResolver mContentResolver;

  public EmailAddressAdapter(Context paramContext)
  {
    super(paramContext, 2130903073, null, false);
    this.mContentResolver = paramContext.getContentResolver();
  }

  public final void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    TextView localTextView1 = (TextView)paramView.findViewById(2131361909);
    TextView localTextView2 = (TextView)paramView.findViewById(2131361910);
    localTextView1.setText(paramCursor.getString(1));
    localTextView2.setText(paramCursor.getString(2));
  }

  public final String convertToString(Cursor paramCursor)
  {
    return new Rfc822Token(paramCursor.getString(1), paramCursor.getString(2), null).toString();
  }

  public Cursor runQueryOnBackgroundThread(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null);
    for (String str = ""; ; str = paramCharSequence.toString())
    {
      Uri localUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_FILTER_URI, Uri.encode(str));
      return this.mContentResolver.query(localUri, PROJECTION, null, null, "times_contacted DESC, display_name , is_super_primary DESC");
    }
  }

  public void unregister()
  {
    changeCursor(null);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.EmailAddressAdapter
 * JD-Core Version:    0.6.2
 */