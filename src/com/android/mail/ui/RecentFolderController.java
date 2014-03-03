package com.android.mail.ui;

import android.database.DataSetObserver;

public abstract interface RecentFolderController
{
  public abstract RecentFolderList getRecentFolders();

  public abstract void registerRecentFolderObserver(DataSetObserver paramDataSetObserver);

  public abstract void unregisterRecentFolderObserver(DataSetObserver paramDataSetObserver);
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.RecentFolderController
 * JD-Core Version:    0.6.2
 */