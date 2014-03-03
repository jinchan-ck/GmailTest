package com.android.mail.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.android.mail.preferences.MailPrefs;
import com.android.mail.providers.Folder;
import com.android.mail.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MailActivity extends AbstractMailActivity
  implements ControllableActivity, WhatsNewDialogFragment.WhatsNewDialogListener
{
  private static MailActivity sForegroundInstance;
  private boolean mAccessibilityEnabled;
  private AccessibilityManager mAccessibilityManager;
  private ActivityController mController;
  private NdefMessage mForegroundNdef;
  private boolean mLaunchedCleanly = false;
  private NfcAdapter mNfcAdapter;
  private ToastBarOperation mPendingToastOp;
  private ViewMode mViewMode;

  public static NdefMessage getMailtoNdef(String paramString)
  {
    try
    {
      byte[] arrayOfByte3 = URLEncoder.encode(paramString, "UTF-8").getBytes("UTF-8");
      arrayOfByte1 = arrayOfByte3;
      byte[] arrayOfByte2 = new byte[1 + arrayOfByte1.length];
      arrayOfByte2[0] = 6;
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 1, arrayOfByte1.length);
      return new NdefMessage(new NdefRecord[] { new NdefRecord(1, NdefRecord.RTD_URI, new byte[0], arrayOfByte2) });
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
        byte[] arrayOfByte1 = paramString.getBytes();
    }
  }

  public static void setForegroundNdef(NdefMessage paramNdefMessage)
  {
    MailActivity localMailActivity = sForegroundInstance;
    if ((localMailActivity != null) && (localMailActivity.mNfcAdapter != null))
      try
      {
        localMailActivity.mForegroundNdef = paramNdefMessage;
        if (sForegroundInstance != null)
        {
          if (paramNdefMessage == null)
            break label48;
          sForegroundInstance.mNfcAdapter.enableForegroundNdefPush(sForegroundInstance, paramNdefMessage);
        }
        while (true)
        {
          return;
          label48: sForegroundInstance.mNfcAdapter.disableForegroundNdefPush(sForegroundInstance);
        }
      }
      finally
      {
      }
  }

  private void setupNfc()
  {
    this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
  }

  private void showWhatsNewDialog()
  {
    WhatsNewDialogFragment.newInstance().show(getFragmentManager(), "WhatsNewDialogFragment");
  }

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    this.mController.onTouchEvent(paramMotionEvent);
    return super.dispatchTouchEvent(paramMotionEvent);
  }

  public void doNothingClickHandler(View paramView)
  {
  }

  public AccountController getAccountController()
  {
    return this.mController;
  }

  public ConversationUpdater getConversationUpdater()
  {
    return this.mController;
  }

  public ErrorListener getErrorListener()
  {
    return this.mController;
  }

  public FolderController getFolderController()
  {
    return this.mController;
  }

  public FolderListFragment.FolderListSelectionListener getFolderListSelectionListener()
  {
    return this.mController;
  }

  public Folder getHierarchyFolder()
  {
    return this.mController.getHierarchyFolder();
  }

  public ConversationListCallbacks getListHandler()
  {
    return this.mController;
  }

  public ToastBarOperation getPendingToastOperation()
  {
    return this.mPendingToastOp;
  }

  public RecentFolderController getRecentFolderController()
  {
    return this.mController;
  }

  public ConversationSelectionSet getSelectedSet()
  {
    return this.mController.getSelectedSet();
  }

  public SubjectDisplayChanger getSubjectDisplayChanger()
  {
    return this.mController.getSubjectDisplayChanger();
  }

  public ViewMode getViewMode()
  {
    return this.mViewMode;
  }

  public void handleDrop(DragEvent paramDragEvent, Folder paramFolder)
  {
    this.mController.handleDrop(paramDragEvent, paramFolder);
  }

  public boolean isAccessibilityEnabled()
  {
    return this.mAccessibilityEnabled;
  }

  public void onAccessibilityStateChanged(boolean paramBoolean)
  {
    this.mAccessibilityEnabled = paramBoolean;
    this.mController.onAccessibilityStateChanged();
  }

  public void onActionModeFinished(ActionMode paramActionMode)
  {
    super.onActionModeFinished(paramActionMode);
  }

  public void onActionModeStarted(ActionMode paramActionMode)
  {
    super.onActionModeStarted(paramActionMode);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mController.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onAnimationEnd(AnimatedAdapter paramAnimatedAdapter)
  {
    this.mController.onAnimationEnd(paramAnimatedAdapter);
  }

  public void onBackPressed()
  {
    if (!this.mController.onBackPressed())
      super.onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mViewMode = new ViewMode(this);
    boolean bool = Utils.useTabletUI(this);
    this.mController = ControllerFactory.forActivity(this, this.mViewMode, bool);
    this.mController.onCreate(paramBundle);
    Intent localIntent = getIntent();
    if ((paramBundle == null) && (localIntent.getAction() != null))
      this.mLaunchedCleanly = true;
    this.mAccessibilityManager = ((AccessibilityManager)getSystemService("accessibility"));
    this.mAccessibilityEnabled = this.mAccessibilityManager.isEnabled();
    setupNfc();
    if ((MailPrefs.get(this).getShouldShowWhatsNew(this)) && (getFragmentManager().findFragmentByTag("WhatsNewDialogFragment") == null))
      showWhatsNewDialog();
  }

  public Dialog onCreateDialog(int paramInt, Bundle paramBundle)
  {
    Dialog localDialog = this.mController.onCreateDialog(paramInt, paramBundle);
    if (localDialog == null)
      localDialog = super.onCreateDialog(paramInt, paramBundle);
    return localDialog;
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    return (this.mController.onCreateOptionsMenu(paramMenu)) || (super.onCreateOptionsMenu(paramMenu));
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.mController.onDestroy();
  }

  public void onFooterViewErrorActionClick(Folder paramFolder, int paramInt)
  {
    this.mController.onFooterViewErrorActionClick(paramFolder, paramInt);
  }

  public void onFooterViewLoadMoreClick(Folder paramFolder)
  {
    this.mController.onFooterViewLoadMoreClick(paramFolder);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    return (this.mController.onKeyDown(paramInt, paramKeyEvent)) || (super.onKeyDown(paramInt, paramKeyEvent));
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    return (this.mController.onOptionsItemSelected(paramMenuItem)) || (super.onOptionsItemSelected(paramMenuItem));
  }

  public void onPause()
  {
    super.onPause();
    this.mController.onPause();
    try
    {
      if ((this.mNfcAdapter != null) && (this.mForegroundNdef != null))
        this.mNfcAdapter.disableForegroundNdefPush(this);
      sForegroundInstance = null;
      return;
    }
    finally
    {
    }
  }

  public void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle)
  {
    super.onPrepareDialog(paramInt, paramDialog, paramBundle);
    this.mController.onPrepareDialog(paramInt, paramDialog, paramBundle);
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    this.mController.onPrepareOptionsMenu(paramMenu);
    return super.onPrepareOptionsMenu(paramMenu);
  }

  protected void onRestart()
  {
    super.onRestart();
    this.mController.onRestart();
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.mController.onRestoreInstanceState(paramBundle);
  }

  public void onResume()
  {
    super.onResume();
    this.mController.onResume();
    try
    {
      sForegroundInstance = this;
      if ((this.mNfcAdapter != null) && (this.mForegroundNdef != null))
        this.mNfcAdapter.enableForegroundNdefPush(this, this.mForegroundNdef);
      boolean bool = this.mAccessibilityManager.isEnabled();
      if (bool != this.mAccessibilityEnabled)
        onAccessibilityStateChanged(bool);
      return;
    }
    finally
    {
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.mController.onSaveInstanceState(paramBundle);
  }

  public boolean onSearchRequested()
  {
    this.mController.startSearch();
    return true;
  }

  public boolean onSearchRequested(String paramString)
  {
    this.mController.onSearchRequested(paramString);
    return true;
  }

  protected void onStart()
  {
    super.onStart();
    if (this.mLaunchedCleanly);
    this.mController.onStart();
  }

  public void onStop()
  {
    super.onStop();
    this.mController.onStop();
  }

  public void onUndoAvailable(ToastBarOperation paramToastBarOperation)
  {
    this.mController.onUndoAvailable(paramToastBarOperation);
  }

  public void onWhatsNewDialogLayoutInflated(View paramView)
  {
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    this.mController.onWindowFocusChanged(paramBoolean);
  }

  public void setPendingToastOperation(ToastBarOperation paramToastBarOperation)
  {
    this.mPendingToastOp = paramToastBarOperation;
  }

  public void startDragMode()
  {
    this.mController.startDragMode();
  }

  public void stopDragMode()
  {
    this.mController.stopDragMode();
  }

  public boolean supportsDrag(DragEvent paramDragEvent, Folder paramFolder)
  {
    return this.mController.supportsDrag(paramDragEvent, paramFolder);
  }

  public void unsetViewModeListener(ViewMode.ModeChangeListener paramModeChangeListener)
  {
    this.mViewMode.removeListener(paramModeChangeListener);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.MailActivity
 * JD-Core Version:    0.6.2
 */