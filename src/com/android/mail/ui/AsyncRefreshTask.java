package com.android.mail.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

public class AsyncRefreshTask extends AsyncTask<Void, Void, Void>
{
  private final Context mContext;
  private final Uri mRefreshUri;

  public AsyncRefreshTask(Context paramContext, Uri paramUri)
  {
    this.mContext = paramContext;
    this.mRefreshUri = paramUri;
  }

  protected Void doInBackground(Void[] paramArrayOfVoid)
  {
    if (this.mRefreshUri != null)
      this.mContext.getContentResolver().query(this.mRefreshUri, null, null, null, null);
    return null;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.AsyncRefreshTask
 * JD-Core Version:    0.6.2
 */