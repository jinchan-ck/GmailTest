package com.google.android.gm.ui;

import android.content.Intent;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;
import com.android.mail.ui.FolderSelectionActivity;
import com.android.mail.widget.BaseGmailWidgetProvider;
import java.util.ArrayList;

public class FolderSelectionActivityGmail extends FolderSelectionActivity
{
  protected void createWidget(int paramInt, Account paramAccount, Folder paramFolder)
  {
    BaseGmailWidgetProvider.updateWidget(this, paramInt, paramAccount, paramFolder);
    Intent localIntent = new Intent();
    localIntent.putExtra("appWidgetId", paramInt);
    setResult(-1, localIntent);
    finish();
  }

  protected ArrayList<Integer> getExcludedFolderTypes()
  {
    ArrayList localArrayList = super.getExcludedFolderTypes();
    if (this.mConfigureWidget)
      localArrayList.add(Integer.valueOf(9));
    return localArrayList;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ui.FolderSelectionActivityGmail
 * JD-Core Version:    0.6.2
 */