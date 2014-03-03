package com.android.mail.ui;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.net.Uri;
import android.util.Log;
import com.android.mail.browse.ConversationCursor;
import com.android.mail.providers.Account;
import java.util.ArrayList;
import java.util.Iterator;

public class ConversationCursorLoader extends AsyncTaskLoader<ConversationCursor>
{
  private static final ArrayList<ConversationCursorLoader> sLoaders = new ArrayList();
  private final Activity mActivity;
  private boolean mClosed = false;
  private final ConversationCursor mConversationCursor;
  private boolean mInit = false;
  private boolean mInitialConversationLimit;
  private final String mName;
  private boolean mRetain = false;
  private boolean mRetained = false;
  private final Uri mUri;

  public ConversationCursorLoader(Activity paramActivity, Account paramAccount, Uri paramUri, String paramString)
  {
    super(paramActivity);
    this.mActivity = paramActivity;
    this.mUri = paramUri;
    this.mName = paramString;
    this.mInitialConversationLimit = paramAccount.supportsCapability(262144);
    this.mConversationCursor = new ConversationCursor(this.mActivity, this.mUri, this.mInitialConversationLimit, this.mName);
    addLoader();
  }

  private void addLoader()
  {
    Log.d("ConversationCursorLoader", "Add loader: " + this.mUri);
    sLoaders.add(this);
    if (sLoaders.size() > 1)
      dumpLoaders();
  }

  private void dumpLoaders()
  {
    Log.d("ConversationCursorLoader", "Loaders: ");
    Iterator localIterator = sLoaders.iterator();
    while (localIterator.hasNext())
    {
      ConversationCursorLoader localConversationCursorLoader = (ConversationCursorLoader)localIterator.next();
      Log.d("ConversationCursorLoader", " >> " + localConversationCursorLoader.mName + " (" + localConversationCursorLoader.mUri + ")");
    }
  }

  public ConversationCursor loadInBackground()
  {
    if (!this.mInit)
    {
      this.mConversationCursor.load();
      this.mInit = true;
    }
    return this.mConversationCursor;
  }

  public void onReset()
  {
    if (!this.mRetain)
    {
      Log.d("ConversationCursorLoader", "Reset loader/disable cursor: " + this.mName);
      this.mConversationCursor.disable();
      this.mClosed = true;
      sLoaders.remove(this);
      if (!sLoaders.isEmpty())
        dumpLoaders();
      return;
    }
    Log.d("ConversationCursorLoader", "Reset loader/retain cursor: " + this.mName);
    this.mRetained = true;
  }

  protected void onStartLoading()
  {
    if (this.mClosed)
    {
      this.mClosed = false;
      this.mConversationCursor.load();
      addLoader();
      Log.d("ConversationCursorLoader", "Restarting reset loader: " + this.mName);
    }
    while (true)
    {
      forceLoad();
      this.mConversationCursor.resume();
      return;
      if (this.mRetained)
      {
        this.mRetained = false;
        Log.d("ConversationCursorLoader", "Resuming retained loader: " + this.mName);
      }
    }
  }

  protected void onStopLoading()
  {
    cancelLoad();
    this.mConversationCursor.pause();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ConversationCursorLoader
 * JD-Core Version:    0.6.2
 */