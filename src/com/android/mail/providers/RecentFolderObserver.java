package com.android.mail.providers;

import android.database.DataSetObserver;
import com.android.mail.ui.RecentFolderController;
import com.android.mail.ui.RecentFolderList;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;

public abstract class RecentFolderObserver extends DataSetObserver
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private RecentFolderController mController;

  public RecentFolderList initialize(RecentFolderController paramRecentFolderController)
  {
    if (paramRecentFolderController == null)
      LogUtils.wtf(LOG_TAG, "RecentFolderObserver initialized with null controller!", new Object[0]);
    this.mController = paramRecentFolderController;
    this.mController.registerRecentFolderObserver(this);
    return this.mController.getRecentFolders();
  }

  public void unregisterAndDestroy()
  {
    if (this.mController == null)
      return;
    this.mController.unregisterRecentFolderObserver(this);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.RecentFolderObserver
 * JD-Core Version:    0.6.2
 */