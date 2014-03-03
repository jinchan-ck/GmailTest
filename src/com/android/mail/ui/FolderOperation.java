package com.android.mail.ui;

import com.android.mail.providers.Folder;
import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class FolderOperation
{
  public static final Collection<FolderOperation> EMPTY = Collections.emptyList();
  public boolean mAdd;
  public Folder mFolder;

  public FolderOperation(Folder paramFolder, Boolean paramBoolean)
  {
    this.mAdd = paramBoolean.booleanValue();
    this.mFolder = paramFolder;
  }

  public static boolean isDestructive(Collection<FolderOperation> paramCollection, Folder paramFolder)
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      FolderOperation localFolderOperation = (FolderOperation)localIterator.next();
      if ((Objects.equal(localFolderOperation.mFolder.uri, paramFolder.uri)) && (!localFolderOperation.mAdd))
        return true;
      if ((paramFolder.isTrash()) && (localFolderOperation.mFolder.type == 1))
        return true;
    }
    return false;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.FolderOperation
 * JD-Core Version:    0.6.2
 */