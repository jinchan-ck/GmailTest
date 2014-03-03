package com.android.mail.ui;

import android.content.Context;
import android.content.res.Resources;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.utils.LogTag;
import com.google.common.collect.Sets;
import java.util.SortedSet;

public class FolderDisplayer
{
  public static final String LOG_TAG = LogTag.getLogTag();
  protected Context mContext;
  protected final int mDefaultBgColor;
  protected final int mDefaultFgColor;
  protected final SortedSet<Folder> mFoldersSortedSet = Sets.newTreeSet();

  public FolderDisplayer(Context paramContext)
  {
    this.mContext = paramContext;
    this.mDefaultFgColor = paramContext.getResources().getColor(2131230740);
    this.mDefaultBgColor = paramContext.getResources().getColor(2131230739);
  }

  public void loadConversationFolders(Conversation paramConversation, Folder paramFolder)
  {
    this.mFoldersSortedSet.clear();
    this.mFoldersSortedSet.addAll(paramConversation.getRawFoldersForDisplay(paramFolder));
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.FolderDisplayer
 * JD-Core Version:    0.6.2
 */