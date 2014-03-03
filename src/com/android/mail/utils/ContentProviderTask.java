package com.android.mail.utils;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class ContentProviderTask extends AsyncTask<Void, Void, Result>
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private String mAuthority;
  private ArrayList<ContentProviderOperation> mOps;
  private ContentResolver mResolver;

  protected Result doInBackground(Void[] paramArrayOfVoid)
  {
    Result localResult = new Result(null);
    try
    {
      localResult.setSuccess(this.mResolver.applyBatch(this.mAuthority, this.mOps));
      return localResult;
    }
    catch (Exception localException)
    {
      LogUtils.w(LOG_TAG, localException, "exception executing ContentProviderOperationsTask", new Object[0]);
      localResult.setFailure(localException);
    }
    return localResult;
  }

  public void run(ContentResolver paramContentResolver, String paramString, ArrayList<ContentProviderOperation> paramArrayList)
  {
    this.mResolver = paramContentResolver;
    this.mAuthority = paramString;
    this.mOps = paramArrayList;
    Executor localExecutor = THREAD_POOL_EXECUTOR;
    Void[] arrayOfVoid = new Void[1];
    arrayOfVoid[0] = ((Void)null);
    executeOnExecutor(localExecutor, arrayOfVoid);
  }

  public static class Result
  {
    Exception exception;
    ContentProviderResult[] results;

    private void setFailure(Exception paramException)
    {
      this.results = null;
      this.exception = paramException;
    }

    private void setSuccess(ContentProviderResult[] paramArrayOfContentProviderResult)
    {
      this.exception = null;
      this.results = paramArrayOfContentProviderResult;
    }
  }

  public static class UpdateTask extends ContentProviderTask
  {
    public void run(ContentResolver paramContentResolver, Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
    {
      ContentProviderOperation localContentProviderOperation = ContentProviderOperation.newUpdate(paramUri).withValues(paramContentValues).withSelection(paramString, paramArrayOfString).build();
      super.run(paramContentResolver, paramUri.getAuthority(), Lists.newArrayList(new ContentProviderOperation[] { localContentProviderOperation }));
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.utils.ContentProviderTask
 * JD-Core Version:    0.6.2
 */