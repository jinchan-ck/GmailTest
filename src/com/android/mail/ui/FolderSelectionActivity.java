package com.android.mail.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import com.android.mail.widget.WidgetProvider;
import java.util.ArrayList;

public class FolderSelectionActivity extends Activity
  implements DialogInterface.OnClickListener, View.OnClickListener, ControllableActivity, FolderChangeListener, FolderListFragment.FolderListSelectionListener
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private Account mAccount;
  private int mAppWidgetId = 0;
  private boolean mConfigureShortcut;
  protected boolean mConfigureWidget;
  private int mMode = -1;
  private Folder mSelectedFolder;

  private void createFolderListFragment(Folder paramFolder, Uri paramUri)
  {
    FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
    localFragmentTransaction.replace(2131689648, FolderListFragment.newInstance(paramFolder, paramUri, false, getExcludedFolderTypes()));
    localFragmentTransaction.commitAllowingStateLoss();
  }

  private void doCancel()
  {
    setResult(0);
    finish();
  }

  protected void createWidget(int paramInt, Account paramAccount, Folder paramFolder)
  {
    WidgetProvider.updateWidget(this, paramInt, paramAccount, paramFolder);
    Intent localIntent = new Intent();
    localIntent.putExtra("appWidgetId", paramInt);
    setResult(-1, localIntent);
    finish();
  }

  public AccountController getAccountController()
  {
    return null;
  }

  public Context getActivityContext()
  {
    return this;
  }

  public ConversationUpdater getConversationUpdater()
  {
    return null;
  }

  public ErrorListener getErrorListener()
  {
    return null;
  }

  protected ArrayList<Integer> getExcludedFolderTypes()
  {
    return new ArrayList();
  }

  public FolderController getFolderController()
  {
    return null;
  }

  public FolderListFragment.FolderListSelectionListener getFolderListSelectionListener()
  {
    return this;
  }

  public Folder getHierarchyFolder()
  {
    return null;
  }

  public ConversationListCallbacks getListHandler()
  {
    return null;
  }

  public ToastBarOperation getPendingToastOperation()
  {
    return null;
  }

  public RecentFolderController getRecentFolderController()
  {
    return null;
  }

  public ConversationSelectionSet getSelectedSet()
  {
    return null;
  }

  public SubjectDisplayChanger getSubjectDisplayChanger()
  {
    return null;
  }

  public ViewMode getViewMode()
  {
    return null;
  }

  public void handleDrop(DragEvent paramDragEvent, Folder paramFolder)
  {
  }

  public boolean isAccessibilityEnabled()
  {
    return true;
  }

  public void onAnimationEnd(AnimatedAdapter paramAnimatedAdapter)
  {
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if (paramInt == -1)
    {
      createWidget(this.mAppWidgetId, this.mAccount, this.mSelectedFolder);
      return;
    }
    doCancel();
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
    case 2131689665:
    }
    do
      return;
    while (this.mMode != 0);
    doCancel();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968635);
    Intent localIntent = getIntent();
    String str = localIntent.getAction();
    this.mConfigureShortcut = "android.intent.action.CREATE_SHORTCUT".equals(str);
    this.mConfigureWidget = "android.appwidget.action.APPWIDGET_CONFIGURE".equals(str);
    if ((!this.mConfigureShortcut) && (!this.mConfigureWidget))
      LogUtils.wtf(LOG_TAG, "unexpected intent: %s", new Object[] { localIntent });
    if ((this.mConfigureShortcut) || (this.mConfigureWidget))
    {
      ActionBar localActionBar = getActionBar();
      if (localActionBar != null)
        localActionBar.setIcon(2130903041);
    }
    for (this.mMode = 0; ; this.mMode = 1)
    {
      if (this.mConfigureWidget)
      {
        this.mAppWidgetId = localIntent.getIntExtra("appWidgetId", 0);
        if (this.mAppWidgetId == 0)
          LogUtils.wtf(LOG_TAG, "invalid widgetId", new Object[0]);
      }
      this.mAccount = ((Account)localIntent.getParcelableExtra("account-shortcut"));
      Button localButton = (Button)findViewById(2131689665);
      localButton.setVisibility(0);
      if (this.mMode == 1)
        localButton.setEnabled(false);
      localButton.setOnClickListener(this);
      createFolderListFragment(null, this.mAccount.folderListUri);
      return;
    }
  }

  public void onFolderChanged(Folder paramFolder)
  {
    if (!paramFolder.equals(this.mSelectedFolder))
    {
      this.mSelectedFolder = paramFolder;
      Intent localIntent1 = new Intent();
      if (!this.mConfigureShortcut)
        break label136;
      localIntent1.putExtra("android.intent.extra.shortcut.INTENT", Utils.createViewFolderIntent(this.mSelectedFolder, this.mAccount));
      localIntent1.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(this, 2130903041));
      str = this.mSelectedFolder.name;
      localIntent1.putExtra("android.intent.extra.shortcut.NAME", str);
      localIntent2 = new Intent(this, ShortcutNameActivity.class);
      localIntent2.setFlags(1107296256);
      localIntent2.putExtra("extra_folder_click_intent", localIntent1);
      localIntent2.putExtra("extra_shortcut_name", str);
      startActivity(localIntent2);
      finish();
    }
    label136: 
    while (!this.mConfigureWidget)
    {
      String str;
      Intent localIntent2;
      return;
    }
    createWidget(this.mAppWidgetId, this.mAccount, this.mSelectedFolder);
  }

  public void onFolderSelected(Folder paramFolder)
  {
    if (paramFolder.hasChildren)
    {
      createFolderListFragment(paramFolder, paramFolder.childFoldersListUri);
      return;
    }
    onFolderChanged(paramFolder);
  }

  public void onFooterViewErrorActionClick(Folder paramFolder, int paramInt)
  {
  }

  public void onFooterViewLoadMoreClick(Folder paramFolder)
  {
  }

  protected void onResume()
  {
    super.onResume();
  }

  public boolean onSearchRequested(String paramString)
  {
    return false;
  }

  public void onUndoAvailable(ToastBarOperation paramToastBarOperation)
  {
  }

  public void setPendingToastOperation(ToastBarOperation paramToastBarOperation)
  {
  }

  public void startDragMode()
  {
  }

  public void stopDragMode()
  {
  }

  public boolean supportsDrag(DragEvent paramDragEvent, Folder paramFolder)
  {
    return false;
  }

  public void unsetViewModeListener(ViewMode.ModeChangeListener paramModeChangeListener)
  {
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.FolderSelectionActivity
 * JD-Core Version:    0.6.2
 */