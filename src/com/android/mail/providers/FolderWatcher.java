package com.android.mail.providers;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.BaseAdapter;
import com.android.mail.ui.RestrictedActivity;
import com.android.mail.utils.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderWatcher
  implements LoaderManager.LoaderCallbacks<Cursor>
{
  private final RestrictedActivity mActivity;
  private BaseAdapter mConsumer;
  private final Map<Uri, Folder> mFolder = new HashMap();
  private final List<Uri> mUri = new ArrayList();

  public FolderWatcher(RestrictedActivity paramRestrictedActivity, BaseAdapter paramBaseAdapter)
  {
    this.mActivity = paramRestrictedActivity;
    this.mConsumer = paramBaseAdapter;
  }

  private static final int getLoaderFromPosition(int paramInt)
  {
    return paramInt + 100;
  }

  private static final int getPositionFromLoader(int paramInt)
  {
    return paramInt - 100;
  }

  public Folder get(Uri paramUri)
  {
    return (Folder)this.mFolder.get(paramUri);
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    int i = getPositionFromLoader(paramInt);
    if ((i < 0) || (i > this.mUri.size()))
      return null;
    return new CursorLoader(this.mActivity.getActivityContext(), (Uri)this.mUri.get(i), UIProvider.FOLDERS_PROJECTION, null, null, null);
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if ((paramCursor == null) || (paramCursor.getCount() <= 0) || (!paramCursor.moveToFirst()))
      return;
    Uri localUri = (Uri)this.mUri.get(getPositionFromLoader(paramLoader.getId()));
    Folder localFolder = new Folder(paramCursor);
    this.mFolder.put(localUri, localFolder);
    this.mConsumer.notifyDataSetChanged();
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
  }

  public void startWatching(Uri paramUri)
  {
    if ((paramUri == null) || (this.mFolder.containsKey(paramUri)))
      return;
    int i = this.mUri.size();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramUri;
    arrayOfObject[1] = Integer.valueOf(i);
    LogUtils.d("UnifiedEmail", "Watching %s, at position %d.", arrayOfObject);
    this.mFolder.put(paramUri, null);
    this.mUri.add(paramUri);
    this.mActivity.getLoaderManager().initLoader(getLoaderFromPosition(i), null, this);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.FolderWatcher
 * JD-Core Version:    0.6.2
 */