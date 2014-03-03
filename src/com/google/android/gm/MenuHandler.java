package com.google.android.gm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.LabelMap;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MenuHandler
{
  private static final String ACCOUNT_KEY = MenuHandler.class.getName() + "_ACCOUNT_KEY";
  private static final HashSet<String> ARCHIVABLE_LABELS = new HashSet()
  {
  };
  public static final int ASSIGN_LABEL_DIALOG_ID = 3;
  public static final String BULK_OP_CONVERSATION_KEY = "CONVERSATION_KEY";
  public static final String BULK_OP_LABEL_KEY = "LABEL_KEY";
  public static final int GO_TO_LABEL_DIALOG_ID = 2;
  public static final int LABELS_DONE_MESSAGE = 2;
  public static final int LABEL_CHANGED_MESSAGE = 1;
  public static final int LABEL_DIALOG_ID = 1;
  private static final boolean LDEBUG = false;
  protected static final String TAG = "Gmail";
  private String mAccount;
  private final Activity mActivity;
  private final ActivityCallback mActivityCallback;
  Map<String, MenuItem> mAddOrRemoveLabelMenuItems = Maps.newHashMap();
  private final String mDisplayedLabel;
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (MenuHandler.this.mActivity.isFinishing());
      do
      {
        return;
        if (paramAnonymousMessage.arg1 != 0);
        for (boolean bool = true; 1 == paramAnonymousMessage.what; bool = false)
        {
          Bundle localBundle = paramAnonymousMessage.getData();
          String str = localBundle.getString("LABEL_KEY");
          long l = localBundle.getLong("CONVERSATION_KEY");
          MenuHandler.this.mActivityCallback.onLabelChanged(str, l, bool);
          return;
        }
      }
      while (2 != paramAnonymousMessage.what);
      LabelOperations localLabelOperations = LabelOperations.deserialize((String)paramAnonymousMessage.obj);
      BulkOperationHelper localBulkOperationHelper = BulkOperationHelper.getInstance(MenuHandler.this.mActivity);
      localBulkOperationHelper.hideProgressDialog();
      localBulkOperationHelper.clearState();
      MenuHandler.this.mActivityCallback.doneChangingLabels(localLabelOperations);
    }
  };
  private boolean mResetSpannable = true;
  private final SpannableStringBuilder mShortcutSpannable = new SpannableStringBuilder();

  public MenuHandler(Activity paramActivity, ActivityCallback paramActivityCallback, String paramString1, String paramString2)
  {
    this.mActivity = paramActivity;
    this.mActivityCallback = paramActivityCallback;
    this.mDisplayedLabel = paramString1;
    this.mAccount = paramString2;
  }

  private boolean allConversationsHaveLabel(Collection<ConversationInfo> paramCollection, String paramString)
  {
    boolean bool = true;
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      if (!((ConversationInfo)localIterator.next()).getLabels().contains(paramString))
        bool = false;
    return bool;
  }

  private boolean areAllConversationsImportant(Collection<ConversationInfo> paramCollection)
  {
    boolean bool = true;
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      if (!Gmail.isImportant(((ConversationInfo)localIterator.next()).getLabels()))
        bool = false;
    return bool;
  }

  private boolean areAllConversationsMuted(Collection<ConversationInfo> paramCollection)
  {
    return allConversationsHaveLabel(paramCollection, "^g");
  }

  private boolean areAllConversationsStarred(Collection<ConversationInfo> paramCollection)
  {
    return allConversationsHaveLabel(paramCollection, "^t");
  }

  public static String getRemovableLabel(String paramString, Set<String> paramSet)
  {
    String str = getYButtonLabel(paramString);
    if ((paramSet.contains(paramString)) && (Gmail.isLabelUserSettable(paramString)))
      return str;
    return null;
  }

  public static String getYButtonLabel(String paramString)
  {
    if (shouldShowArchiveOption(paramString))
      return "^i";
    return paramString;
  }

  public static String getYButtonText(Context paramContext, Gmail.LabelMap paramLabelMap, String paramString)
  {
    if ("^k".equals(paramString))
      return paramContext.getResources().getString(2131296353);
    if ("^r".equals(paramString))
      return paramContext.getString(2131296321);
    if ("^s".equals(paramString))
      return paramContext.getResources().getString(2131296354);
    if (shouldShowArchiveOption(paramString))
      return paramContext.getString(2131296291);
    if ("^t".equals(paramString))
      return paramContext.getString(2131296290);
    Resources localResources = paramContext.getResources();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = LongShadowUtils.getHumanLabelName(paramContext, paramLabelMap, paramString);
    return localResources.getString(2131296367, arrayOfObject);
  }

  private void performActionWithConfirmation(int paramInt1, CharSequence paramCharSequence, int paramInt2, int paramInt3, DialogInterface.OnClickListener paramOnClickListener, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      paramOnClickListener.onClick(null, 0);
      return;
    }
    new AlertDialog.Builder(this.mActivity).setTitle(paramInt1).setMessage(paramCharSequence).setPositiveButton(paramInt2, paramOnClickListener).setNegativeButton(paramInt3, null).create().show();
  }

  private void performPreferences()
  {
    Intent localIntent = new Intent(this.mActivity, PreferenceActivity.class);
    this.mActivity.startActivity(localIntent);
  }

  public static boolean shouldShowArchiveOption(String paramString)
  {
    return ("^all".equals(paramString)) || (ARCHIVABLE_LABELS.contains(paramString));
  }

  public static boolean shouldShowMuteOption(String paramString, Set<String> paramSet)
  {
    return (("^iim".equals(paramString)) || ("^i".equals(paramString))) && (!paramSet.contains("^g"));
  }

  public void addOrRemoveLabel(LabelOperations paramLabelOperations1, Collection<ConversationInfo> paramCollection, boolean paramBoolean, LabelOperations paramLabelOperations2)
  {
    BulkOperationHelper.getInstance(this.mActivity).addOrRemoveLabel(this.mActivity, this.mAccount, paramLabelOperations1, paramCollection, true, this.mDisplayedLabel, paramBoolean, paramLabelOperations2);
  }

  public void addOrRemoveLabel(String paramString, Boolean paramBoolean, Collection<ConversationInfo> paramCollection)
  {
    addOrRemoveLabel(paramString, paramBoolean, paramCollection, true, null);
  }

  public void addOrRemoveLabel(String paramString, Boolean paramBoolean, Collection<ConversationInfo> paramCollection, boolean paramBoolean1, LabelOperations paramLabelOperations)
  {
    addOrRemoveLabel(new LabelOperations(paramString, paramBoolean.booleanValue()), paramCollection, paramBoolean1, paramLabelOperations);
  }

  public void changeLabels()
  {
    this.mActivity.showDialog(1);
  }

  public ApplyRemoveLabelDialog createLabelDialog()
  {
    return new ApplyRemoveLabelDialog(this.mActivity, this);
  }

  public void delete(final Collection<ConversationInfo> paramCollection)
  {
    String str = Utils.formatPlural(this.mActivity, 2131427328, paramCollection.size());
    if (!Persistence.getInstance(this.mActivity).getConfirmDelete(this.mActivity));
    for (boolean bool = true; ; bool = false)
    {
      performActionWithConfirmation(2131296312, str, 2131296293, 2131296314, new DialogInterface.OnClickListener()
      {
        BulkOperationHelper op = BulkOperationHelper.getInstance(MenuHandler.this.mActivity);

        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          this.op.performOperation(MenuHandler.this.mActivity, MenuHandler.this.mAccount, "^k", true, paramCollection, MenuHandler.this.mDisplayedLabel);
        }
      }
      , bool);
      return;
    }
  }

  public Activity getActivity()
  {
    return this.mActivity;
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent, String paramString, Collection<ConversationInfo> paramCollection, Set<String> paramSet)
  {
    boolean bool;
    switch (paramInt)
    {
    default:
      if (this.mResetSpannable)
      {
        this.mShortcutSpannable.clear();
        this.mShortcutSpannable.clearSpans();
        Selection.setSelection(this.mShortcutSpannable, 0);
      }
      bool = TextKeyListener.getInstance().onKeyDown(null, this.mShortcutSpannable, paramInt, paramKeyEvent);
      if ((bool) && (this.mShortcutSpannable.length() > 0))
      {
        if (TextUtils.indexOf(this.mShortcutSpannable, '!') >= 0)
        {
          onMenuItemSelected(2131361931, paramCollection, paramSet, false);
          this.mResetSpannable = true;
          bool = true;
        }
        return bool;
      }
      break;
    case 53:
      onMenuItemSelected(2131361926, paramCollection, paramSet, false);
      this.mResetSpannable = true;
      return true;
    case 41:
      if (shouldShowMuteOption(this.mDisplayedLabel, paramSet))
        BulkOperationHelper.getInstance(this.mActivity).performOperation(this.mActivity, this.mAccount, "^g", true, paramCollection, this.mDisplayedLabel);
      return true;
    case 40:
      this.mActivity.showDialog(2);
      return true;
    case 50:
      this.mActivity.showDialog(3);
      return true;
    }
    this.mResetSpannable = false;
    return bool;
  }

  public boolean onMenuItemSelected(int paramInt, Collection<ConversationInfo> paramCollection, Set<String> paramSet, boolean paramBoolean)
  {
    BulkOperationHelper localBulkOperationHelper = BulkOperationHelper.getInstance(this.mActivity);
    switch (paramInt)
    {
    default:
    case 2131361926:
    case 2131361927:
    case 2131361853:
    case 2131361930:
    case 2131361928:
    case 2131361931:
    case 2131361929:
    case 2131361933:
    case 2131361859:
    case 2131361937:
    case 2131361932:
    case 2131361935:
    case 2131361936:
    case 2131361924:
    case 2131361938:
    case 2131361939:
    }
    while (true)
    {
      return false;
      performYButtonAction(paramCollection);
      return true;
      if (!paramBoolean)
      {
        localBulkOperationHelper.performOperation(this.mActivity, this.mAccount, "^g", true, paramCollection, this.mDisplayedLabel);
        return true;
        delete(paramCollection);
        return true;
        changeLabels();
        return true;
        localBulkOperationHelper.toggleLabel(this.mActivity, this.mAccount, "^u", paramCollection, paramSet, this.mDisplayedLabel);
        return true;
        if (!paramBoolean)
        {
          localBulkOperationHelper.performOperation(this.mActivity, this.mAccount, "^s", true, paramCollection, this.mDisplayedLabel);
          return true;
          if (!areAllConversationsStarred(paramCollection));
          for (boolean bool = true; ; bool = false)
          {
            localBulkOperationHelper.performOperation(this.mActivity, this.mAccount, "^t", bool, paramCollection, this.mDisplayedLabel);
            return true;
          }
          ComposeActivity.compose(this.mActivity, this.mAccount);
          return true;
          showLabels();
          return true;
          performPreferences();
          return true;
          ConversationListActivity.setUndoOperation(this.mActivity, null, this.mDisplayedLabel);
          Utils.startSync(this.mActivity, this.mAccount);
          return true;
          this.mActivity.onSearchRequested();
          return true;
          Intent localIntent = new Intent(this.mActivity, ConversationListActivity.class);
          this.mActivity.startActivity(localIntent);
          return true;
          Utils.showHelp(this.mActivity);
          return true;
          Utils.showAbout(this.mActivity);
          return true;
          updateImportantStates(paramCollection);
        }
      }
    }
  }

  public boolean onMenuItemSelected(MenuItem paramMenuItem, Collection<ConversationInfo> paramCollection, Set<String> paramSet, boolean paramBoolean)
  {
    return onMenuItemSelected(paramMenuItem.getItemId(), paramCollection, paramSet, paramBoolean);
  }

  public void onPause()
  {
    BulkOperationHelper.getInstance(this.mActivity).detach();
  }

  void onPrepareImportantMenuItem(Menu paramMenu, Collection<ConversationInfo> paramCollection)
  {
    MenuItem localMenuItem = paramMenu.findItem(2131361939);
    if (localMenuItem != null)
    {
      if (!Utils.getPriorityInboxServerEnabled(this.mAccount))
        localMenuItem.setVisible(false);
    }
    else
      return;
    localMenuItem.setVisible(true);
    if (areAllConversationsImportant(paramCollection))
    {
      localMenuItem.setTitle(2131296310);
      localMenuItem.setIcon(2130837585);
      return;
    }
    localMenuItem.setTitle(2131296309);
    localMenuItem.setIcon(2130837584);
  }

  public void onPrepareMenu(Menu paramMenu, Set<String> paramSet, Collection<ConversationInfo> paramCollection, boolean paramBoolean)
  {
    MenuItem localMenuItem1 = paramMenu.findItem(2131361853);
    MenuItem localMenuItem2 = paramMenu.findItem(2131361926);
    onPrepareYMenuItem(paramMenu, paramSet);
    MenuItem localMenuItem3 = paramMenu.findItem(2131361928);
    int i;
    boolean bool4;
    label113: boolean bool3;
    label143: boolean bool2;
    if (localMenuItem3 != null)
    {
      Activity localActivity = this.mActivity;
      if (paramSet.contains("^u"))
      {
        i = 2131296286;
        localMenuItem3.setTitle(localActivity.getText(i));
      }
    }
    else
    {
      MenuItem localMenuItem4 = paramMenu.findItem(2131361931);
      if (localMenuItem4 != null)
      {
        if ("^s".equals(this.mDisplayedLabel))
          break label236;
        bool4 = true;
        localMenuItem4.setVisible(bool4);
      }
      if (localMenuItem1 != null)
      {
        if ("^k".equals(this.mDisplayedLabel))
          break label242;
        bool3 = true;
        localMenuItem1.setVisible(bool3);
      }
      onPrepareMuteMenuItem(paramMenu, paramCollection);
      onPrepareStarMenuItem(paramMenu, paramCollection);
      onPrepareImportantMenuItem(paramMenu, paramCollection);
      if (localMenuItem1 != null)
      {
        if (paramBoolean)
          break label248;
        bool2 = true;
        label184: localMenuItem1.setVisible(bool2);
      }
      if ((localMenuItem2 != null) && (localMenuItem2.isVisible()))
        if (paramBoolean)
          break label254;
    }
    label236: label242: label248: label254: for (boolean bool1 = true; ; bool1 = false)
    {
      localMenuItem2.setVisible(bool1);
      return;
      i = 2131296287;
      break;
      bool4 = false;
      break label113;
      bool3 = false;
      break label143;
      bool2 = false;
      break label184;
    }
  }

  void onPrepareMuteMenuItem(Menu paramMenu, Collection<ConversationInfo> paramCollection)
  {
    MenuItem localMenuItem = paramMenu.findItem(2131361927);
    if (localMenuItem != null)
      if (areAllConversationsMuted(paramCollection))
        break label35;
    label35: for (boolean bool = true; ; bool = false)
    {
      localMenuItem.setVisible(bool);
      return;
    }
  }

  void onPrepareStarMenuItem(Menu paramMenu, Collection<ConversationInfo> paramCollection)
  {
    MenuItem localMenuItem = paramMenu.findItem(2131361929);
    if (localMenuItem != null)
    {
      if ("^t".equals(this.mDisplayedLabel))
        localMenuItem.setVisible(false);
    }
    else
      return;
    localMenuItem.setVisible(true);
    if (areAllConversationsStarred(paramCollection))
    {
      localMenuItem.setTitle(2131296290);
      return;
    }
    localMenuItem.setTitle(2131296289);
  }

  public void onPrepareYMenuItem(Menu paramMenu, Set<String> paramSet)
  {
    MenuItem localMenuItem = paramMenu.findItem(2131361926);
    if (localMenuItem != null)
    {
      String str = getRemovableLabel(this.mDisplayedLabel, paramSet);
      if (str != null)
      {
        Gmail.LabelMap localLabelMap = LongShadowUtils.getLabelMap(this.mActivity.getContentResolver(), this.mAccount);
        localMenuItem.setTitle(getYButtonText(this.mActivity, localLabelMap, str));
        localMenuItem.setVisible(true);
      }
    }
    else
    {
      return;
    }
    localMenuItem.setVisible(false);
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    this.mAccount = paramBundle.getString(ACCOUNT_KEY);
  }

  public void onResume()
  {
    BulkOperationHelper.getInstance(this.mActivity).attach(this);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString(ACCOUNT_KEY, this.mAccount);
  }

  public void performYButtonAction(Collection<ConversationInfo> paramCollection)
  {
    performYButtonAction(paramCollection, null);
  }

  public void performYButtonAction(final Collection<ConversationInfo> paramCollection, final LabelOperations paramLabelOperations)
  {
    String str = Utils.formatPlural(this.mActivity, 2131427329, paramCollection.size());
    if ((!Persistence.getInstance(this.mActivity).getConfirmArchive(this.mActivity)) || (!"^i".equals(this.mDisplayedLabel)));
    for (boolean bool = true; ; bool = false)
    {
      performActionWithConfirmation(2131296311, str, 2131296291, 2131296313, new DialogInterface.OnClickListener()
      {
        BulkOperationHelper op = BulkOperationHelper.getInstance(MenuHandler.this.mActivity);

        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          this.op.performYButtonAction(MenuHandler.this.mActivity, MenuHandler.this.mAccount, MenuHandler.this.mDisplayedLabel, paramCollection, paramLabelOperations);
        }
      }
      , bool);
      return;
    }
  }

  public void post(Runnable paramRunnable)
  {
    this.mHandler.post(paramRunnable);
  }

  public void prepareLabelDialog(ApplyRemoveLabelDialog paramApplyRemoveLabelDialog, Set<String> paramSet, Collection<ConversationInfo> paramCollection)
  {
    paramApplyRemoveLabelDialog.onPrepare(this.mAccount, paramSet, paramCollection);
  }

  public void sendLabelChangedMessage(long paramLong, boolean paramBoolean, String paramString)
  {
    Handler localHandler = this.mHandler;
    if (paramBoolean == true);
    for (int i = 1; ; i = 0)
    {
      Message localMessage = localHandler.obtainMessage(1, i, 0);
      Bundle localBundle = new Bundle();
      localBundle.putLong("CONVERSATION_KEY", paramLong);
      localBundle.putString("LABEL_KEY", paramString);
      localMessage.setData(localBundle);
      this.mHandler.sendMessage(localMessage);
      return;
    }
  }

  public void sendLabelsDoneMessage(LabelOperations paramLabelOperations)
  {
    this.mHandler.sendMessage(this.mHandler.obtainMessage(2, LabelOperations.serialize(paramLabelOperations)));
  }

  public void showLabels()
  {
    Intent localIntent = new Intent(this.mActivity, LabelsActivity.class);
    this.mActivity.startActivity(localIntent);
  }

  void updateImportantStates(Collection<ConversationInfo> paramCollection)
  {
    BulkOperationHelper localBulkOperationHelper = BulkOperationHelper.getInstance(this.mActivity);
    if (areAllConversationsImportant(paramCollection))
    {
      localBulkOperationHelper.performOperation(this.mActivity, this.mAccount, "^^unimportant", true, paramCollection, this.mDisplayedLabel);
      return;
    }
    localBulkOperationHelper.performOperation(this.mActivity, this.mAccount, "^^important", true, paramCollection, this.mDisplayedLabel);
  }

  public static abstract interface ActivityCallback
  {
    public abstract void doneChangingLabels(LabelOperations paramLabelOperations);

    public abstract void onLabelChanged(String paramString, long paramLong, boolean paramBoolean);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.MenuHandler
 * JD-Core Version:    0.6.2
 */