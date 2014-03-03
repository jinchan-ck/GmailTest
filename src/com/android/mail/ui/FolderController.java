package com.android.mail.ui;

import android.database.DataSetObserver;
import com.android.mail.providers.Folder;

public abstract interface FolderController
{
  public abstract Folder getFolder();

  public abstract void registerFolderObserver(DataSetObserver paramDataSetObserver);

  public abstract void unregisterFolderObserver(DataSetObserver paramDataSetObserver);
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.FolderController
 * JD-Core Version:    0.6.2
 */