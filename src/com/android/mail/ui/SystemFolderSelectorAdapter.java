package com.android.mail.ui;

import android.content.Context;
import android.database.Cursor;
import com.android.mail.providers.Folder;
import java.util.Set;

public final class SystemFolderSelectorAdapter extends FolderSelectorAdapter
{
  public SystemFolderSelectorAdapter(Context paramContext, Cursor paramCursor, int paramInt, String paramString, Folder paramFolder)
  {
    super(paramContext, paramCursor, paramInt, paramString, paramFolder);
  }

  public SystemFolderSelectorAdapter(Context paramContext, Cursor paramCursor, Set<String> paramSet, int paramInt, String paramString)
  {
    super(paramContext, paramCursor, paramSet, paramInt, paramString);
  }

  protected boolean meetsRequirements(Folder paramFolder)
  {
    return (paramFolder.supportsCapability(8)) && (paramFolder.isProviderFolder());
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.SystemFolderSelectorAdapter
 * JD-Core Version:    0.6.2
 */