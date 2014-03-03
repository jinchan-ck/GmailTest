package com.google.android.gm;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.MessageModification;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BulkOperationHelper
{
  private static final String ADD_LABEL_PERF_TAG = "BOH_addOrRemoveLabel";
  private static final int BACKGROUND_THRESHOLD = 3;
  private static final boolean LDEBUG = false;
  private static final String TAG = "ConversationListActivity";
  private static BulkOperationHelper sInstance;
  private Gmail mGmail;
  private Collection<ConversationInfo> mInfo;
  private LabelOperations mLabelOperations;
  private volatile MenuHandler mMenuHandler;
  private ProgressDialog mProgressDialog;
  private volatile ThreadState mState = ThreadState.IDLE;

  private void addOrRemoveLabelInternal(Context paramContext, String paramString1, LabelOperations paramLabelOperations1, Collection<ConversationInfo> paramCollection, boolean paramBoolean, String paramString2, LabelOperations paramLabelOperations2)
  {
    Gmail.startTiming("BOH_addOrRemoveLabel");
    ArrayList localArrayList1 = Lists.newArrayList();
    ArrayList localArrayList2 = Lists.newArrayList();
    Iterator localIterator = paramCollection.iterator();
    if (localIterator.hasNext())
    {
      ConversationInfo localConversationInfo = (ConversationInfo)localIterator.next();
      long l1 = localConversationInfo.getMessageId();
      long l2 = localConversationInfo.getConversationId();
      LabelOperations localLabelOperations;
      if (!paramLabelOperations1.hasApplyOperation("^i"))
      {
        localLabelOperations = null;
        if (paramLabelOperations2 != null)
        {
          boolean bool2 = paramLabelOperations2.hasApplyOperation("^i");
          localLabelOperations = null;
          if (!bool2);
        }
      }
      else
      {
        boolean bool3 = localConversationInfo.getLabels().contains("^io_im");
        localLabelOperations = null;
        if (bool3)
          localLabelOperations = new LabelOperations("^iim", true);
      }
      label169: int j;
      label223: ContentValues localContentValues;
      String str;
      boolean bool1;
      if ((paramLabelOperations1.hasApplyOperation("^imi")) && (localConversationInfo.getLabels().contains("^i")))
      {
        if (localLabelOperations == null)
          localLabelOperations = new LabelOperations("^iim", true);
      }
      else
      {
        List localList = paramLabelOperations1.getOperationList();
        if (localLabelOperations != null)
          localList.addAll(localLabelOperations.getOperationList());
        if (paramLabelOperations2 != null)
          localList.addAll(paramLabelOperations2.getOperationList());
        int i = localList.size();
        j = 0;
        if (j < i)
        {
          localContentValues = new ContentValues();
          str = ((LabelOperations.Operation)localList.get(j)).mCanonicalName;
          bool1 = ((LabelOperations.Operation)localList.get(j)).mAdd;
          if (l1 == 0L)
            break label405;
          localContentValues.put("canonicalName", str);
          localContentValues.put("messageId", Long.valueOf(l1));
          localContentValues.put("conversation", Long.valueOf(l2));
          localContentValues.put("add_label_action", Boolean.valueOf(bool1));
          localArrayList2.add(localContentValues);
        }
      }
      while (true)
      {
        if (this.mMenuHandler != null)
          this.mMenuHandler.sendLabelChangedMessage(l2, bool1, str);
        if (("^^out".equals(paramString2)) && ("^k".equals(str)) && (bool1))
          Gmail.MessageModification.expungeMessage(paramContext.getContentResolver(), paramString1, l2);
        j++;
        break label223;
        break;
        localLabelOperations.add("^iim", true);
        break label169;
        label405: localContentValues.put("_id", Long.valueOf(l2));
        localContentValues.put("canonicalName", str);
        localContentValues.put("maxMessageId", Long.valueOf(localConversationInfo.getMaxMessageId()));
        localContentValues.put("add_label_action", Boolean.valueOf(bool1));
        localArrayList1.add(localContentValues);
      }
    }
    if (localArrayList1.size() > 0)
      this.mGmail.addOrRemoveLabelOnConversationBulk(paramString1, (ContentValues[])localArrayList1.toArray(new ContentValues[localArrayList1.size()]));
    if (localArrayList2.size() > 0)
      this.mGmail.addOrRemoveLabelOnMessageBulk(paramString1, (ContentValues[])localArrayList2.toArray(new ContentValues[localArrayList2.size()]));
    if (paramBoolean)
    {
      setUndoOperation(paramContext, paramString1, paramLabelOperations1, paramCollection, paramString2);
      if (this.mMenuHandler != null)
        this.mMenuHandler.sendLabelsDoneMessage(paramLabelOperations1);
    }
    Gmail.stopTiming("BOH_addOrRemoveLabel", localArrayList1.size() + localArrayList2.size());
  }

  private static String getActionDescription(Context paramContext, String paramString1, LabelOperations paramLabelOperations, Collection<ConversationInfo> paramCollection, String paramString2)
  {
    boolean bool1 = paramLabelOperations.hasApplyTrash();
    boolean bool2 = paramLabelOperations.hasApplySpam();
    boolean bool3 = paramLabelOperations.hasApplyMute();
    boolean bool4 = paramLabelOperations.hasApplyArchive();
    int i = -1;
    int j = paramCollection.size();
    if (bool1)
      i = 2131427344;
    while (i != -1)
    {
      return Utils.formatPlural(paramContext, i, j);
      if (bool2)
        i = 2131427342;
      else if (bool3)
        i = 2131427341;
      else if (bool4)
        i = 2131427343;
    }
    return null;
  }

  public static BulkOperationHelper getInstance(Context paramContext)
  {
    if (sInstance == null)
    {
      sInstance = new BulkOperationHelper();
      sInstance.mGmail = LongShadowUtils.getContentProviderMailAccess(paramContext.getContentResolver());
    }
    return sInstance;
  }

  private String getLabelOperationMessage(Context paramContext, LabelOperations paramLabelOperations, int paramInt)
  {
    boolean bool1 = paramLabelOperations.hasApplyTrash();
    boolean bool2 = paramLabelOperations.hasApplySpam();
    boolean bool3 = paramLabelOperations.hasApplyMute();
    boolean bool4 = paramLabelOperations.hasApplyArchive();
    boolean bool5 = paramLabelOperations.hasApplyRead();
    boolean bool6 = paramLabelOperations.hasApplyUnread();
    boolean bool7 = paramLabelOperations.hasApplyStar();
    int i;
    if (bool1)
      i = 2131427336;
    while (true)
    {
      return Utils.formatPlural(paramContext, i, paramInt);
      if (bool2)
        i = 2131427334;
      else if (bool4)
        i = 2131427335;
      else if (bool3)
        i = 2131427333;
      else if (bool5)
        i = 2131427337;
      else if (bool6)
        i = 2131427338;
      else if (bool7)
        i = 2131427339;
      else
        i = 2131427340;
    }
  }

  private void maybeRunInBackground(final Context paramContext, final String paramString1, final LabelOperations paramLabelOperations, Collection<ConversationInfo> paramCollection, final Runnable paramRunnable, final String paramString2, boolean paramBoolean)
  {
    if (this.mState == ThreadState.ACTIVE)
    {
      Log.e("ConversationListActivity", "startOperation() called when last operation hasn't finished");
      return;
    }
    if (paramCollection.size() < 3)
    {
      paramRunnable.run();
      maybeShowToast(paramContext, paramString1, paramLabelOperations, paramCollection, paramString2);
      return;
    }
    if (paramBoolean)
      showProgressDialog(paramContext, paramLabelOperations, paramCollection);
    this.mState = ThreadState.ACTIVE;
    Thread local5 = new Thread()
    {
      public void run()
      {
        paramContext.startService(new Intent(paramContext, EmptyService.class));
        paramRunnable.run();
        synchronized (BulkOperationHelper.this.mState)
        {
          BulkOperationHelper.access$102(BulkOperationHelper.this, BulkOperationHelper.ThreadState.FINISHED);
          if (BulkOperationHelper.this.mMenuHandler != null)
            BulkOperationHelper.this.mMenuHandler.post(new Runnable()
            {
              public void run()
              {
                BulkOperationHelper.maybeShowToast(BulkOperationHelper.5.this.val$context, BulkOperationHelper.5.this.val$account, BulkOperationHelper.5.this.val$labelOperation, BulkOperationHelper.5.this.val$clonedConversations, BulkOperationHelper.5.this.val$displayedLabel);
              }
            });
          paramContext.stopService(new Intent(paramContext, EmptyService.class));
          return;
        }
      }
    };
    local5.setPriority(1);
    local5.start();
  }

  private static void maybeShowToast(Context paramContext, String paramString1, LabelOperations paramLabelOperations, Collection<ConversationInfo> paramCollection, String paramString2)
  {
    boolean bool1 = paramLabelOperations.hasApplyTrash();
    boolean bool2 = paramLabelOperations.hasApplySpam();
    boolean bool3 = paramLabelOperations.hasApplyMute();
    boolean bool4 = paramLabelOperations.hasApplyArchive();
    if (((bool1) || (bool2) || (bool3)) && (!bool4) && (paramCollection.size() >= 3))
    {
      String str = getActionDescription(paramContext, paramString1, paramLabelOperations, paramCollection, paramString2);
      if (str != null)
        Toast.makeText(paramContext, str, 0).show();
    }
  }

  private void performYButtonActionInternal(Context paramContext, String paramString1, String paramString2, Collection<ConversationInfo> paramCollection, LabelOperations paramLabelOperations)
  {
    if ((MenuHandler.shouldShowArchiveOption(paramString2)) || (Gmail.isLabelUserSettable(paramString2)))
      performOperationInternal(paramContext, paramString1, MenuHandler.getYButtonLabel(paramString2), false, paramCollection, paramString2, paramLabelOperations);
    do
    {
      do
        return;
      while (!"^r".equals(paramString2));
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        ConversationInfo localConversationInfo = (ConversationInfo)localIterator.next();
        long l = LongShadowUtils.getComposableMessageId(this.mGmail, paramString1, localConversationInfo.getConversationId());
        if (l != -1L)
          Gmail.MessageModification.expungeMessage(paramContext.getContentResolver(), paramString1, l);
      }
    }
    while (this.mMenuHandler == null);
    this.mMenuHandler.sendLabelsDoneMessage(new LabelOperations(paramString2, false));
  }

  private static void setUndoOperation(Context paramContext, String paramString1, LabelOperations paramLabelOperations, Collection<ConversationInfo> paramCollection, String paramString2)
  {
    String str = getActionDescription(paramContext, paramString1, paramLabelOperations, paramCollection, paramString2);
    if (str != null)
    {
      ConversationListActivity.setUndoOperation(paramContext, new UndoOperation(paramString1, new HashSet(paramCollection), paramLabelOperations, str), paramString2);
      return;
    }
    ConversationListActivity.setUndoOperation(paramContext, null, paramString2);
  }

  private void showProgressDialog(Context paramContext, LabelOperations paramLabelOperations, Collection<ConversationInfo> paramCollection)
  {
    this.mLabelOperations = paramLabelOperations;
    this.mInfo = paramCollection;
    if (this.mProgressDialog != null)
    {
      Log.w("ConversationListActivity", "Progress dialog was still active!");
      this.mProgressDialog.dismiss();
    }
    this.mProgressDialog = ProgressDialog.show(paramContext, null, getLabelOperationMessage(paramContext, paramLabelOperations, paramCollection.size()), false, false);
    this.mProgressDialog.getWindow().clearFlags(2);
    this.mProgressDialog.setMax(paramCollection.size());
    this.mProgressDialog.setProgress(0);
    this.mProgressDialog.setIndeterminate(false);
  }

  public void addOrRemoveLabel(final Context paramContext, final String paramString1, final LabelOperations paramLabelOperations1, final Collection<ConversationInfo> paramCollection, final boolean paramBoolean1, final String paramString2, boolean paramBoolean2, final LabelOperations paramLabelOperations2)
  {
    maybeRunInBackground(paramContext, paramString1, paramLabelOperations1, paramCollection, new Runnable()
    {
      public void run()
      {
        BulkOperationHelper.this.performOperationInternal(paramContext, paramString1, paramLabelOperations1, paramCollection, paramBoolean1, paramString2, paramLabelOperations2);
      }
    }
    , paramString2, paramBoolean2);
  }

  public void addOrRemoveLabel(Context paramContext, String paramString1, String paramString2, boolean paramBoolean1, Collection<ConversationInfo> paramCollection, boolean paramBoolean2, String paramString3, boolean paramBoolean3, LabelOperations paramLabelOperations)
  {
    addOrRemoveLabel(paramContext, paramString1, new LabelOperations(paramString2, paramBoolean1), paramCollection, paramBoolean2, paramString3, paramBoolean3, paramLabelOperations);
  }

  public void attach(MenuHandler paramMenuHandler)
  {
    synchronized (this.mState)
    {
      this.mMenuHandler = paramMenuHandler;
      if (this.mState == ThreadState.FINISHED)
        paramMenuHandler.sendLabelsDoneMessage(this.mLabelOperations);
      while ((this.mState != ThreadState.ACTIVE) || (this.mInfo == null) || (this.mLabelOperations == null))
        return;
      showProgressDialog(this.mMenuHandler.getActivity(), this.mLabelOperations, this.mInfo);
    }
  }

  public void clearState()
  {
    synchronized (this.mState)
    {
      if (this.mState == ThreadState.ACTIVE)
      {
        Log.e("ConversationListActivity", "Thread still active!");
        this.mLabelOperations = null;
        this.mInfo = null;
        return;
      }
      this.mState = ThreadState.IDLE;
    }
  }

  public Object clone()
    throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException();
  }

  public void detach()
  {
    synchronized (this.mState)
    {
      this.mMenuHandler = null;
      hideProgressDialog();
      return;
    }
  }

  public void hideProgressDialog()
  {
    if (this.mProgressDialog == null)
      return;
    this.mProgressDialog.dismiss();
    this.mProgressDialog = null;
  }

  public void performOperation(final Context paramContext, final String paramString1, final String paramString2, final boolean paramBoolean, final Collection<ConversationInfo> paramCollection, final String paramString3)
  {
    maybeRunInBackground(paramContext, paramString1, new LabelOperations(paramString2, paramBoolean), paramCollection, new Runnable()
    {
      public void run()
      {
        BulkOperationHelper.this.performOperationInternal(paramContext, paramString1, paramString2, paramBoolean, paramCollection, paramString3, null);
      }
    }
    , paramString3, true);
  }

  void performOperationInternal(Context paramContext, String paramString1, LabelOperations paramLabelOperations, Collection<ConversationInfo> paramCollection, String paramString2)
  {
    performOperationInternal(paramContext, paramString1, paramLabelOperations, paramCollection, true, paramString2, null);
  }

  void performOperationInternal(Context paramContext, String paramString1, LabelOperations paramLabelOperations1, Collection<ConversationInfo> paramCollection, boolean paramBoolean, String paramString2, LabelOperations paramLabelOperations2)
  {
    Object localObject = paramCollection;
    int i;
    boolean bool1;
    label73: boolean bool4;
    if ((paramLabelOperations1.hasOperation("^k")) || (paramLabelOperations1.hasOperation("^g")))
    {
      if ((!paramLabelOperations1.hasApplyTrash()) && (!paramLabelOperations1.hasApplyMute()))
        break label184;
      i = 1;
      localObject = new HashSet(paramCollection);
      if (paramLabelOperations2 == null)
        paramLabelOperations2 = new LabelOperations();
      if (i != 0)
        break label190;
      bool1 = true;
      paramLabelOperations2.add("^i", bool1);
      if (i != 0)
      {
        if (i != 0)
          break label196;
        bool4 = true;
        label95: paramLabelOperations2.add("^iim", bool4);
      }
    }
    if ((paramLabelOperations1.hasOperation("^i")) && (paramLabelOperations1.hasApplyArchive()))
    {
      boolean bool2 = paramLabelOperations1.hasApplyArchive();
      localObject = new HashSet(paramCollection);
      if (paramLabelOperations2 == null)
        paramLabelOperations2 = new LabelOperations();
      if (bool2)
        break label202;
    }
    label184: label190: label196: label202: for (boolean bool3 = true; ; bool3 = false)
    {
      paramLabelOperations2.add("^iim", bool3);
      addOrRemoveLabelInternal(paramContext, paramString1, paramLabelOperations1, (Collection)localObject, paramBoolean, paramString2, paramLabelOperations2);
      return;
      i = 0;
      break;
      bool1 = false;
      break label73;
      bool4 = false;
      break label95;
    }
  }

  void performOperationInternal(Context paramContext, String paramString1, String paramString2, boolean paramBoolean, Collection<ConversationInfo> paramCollection, String paramString3, LabelOperations paramLabelOperations)
  {
    performOperationInternal(paramContext, paramString1, new LabelOperations(paramString2, paramBoolean), paramCollection, true, paramString3, paramLabelOperations);
  }

  public void performYButtonAction(final Context paramContext, final String paramString1, final String paramString2, final Collection<ConversationInfo> paramCollection, final LabelOperations paramLabelOperations)
  {
    maybeRunInBackground(paramContext, paramString1, new LabelOperations(paramString2, false), paramCollection, new Runnable()
    {
      public void run()
      {
        BulkOperationHelper.this.performYButtonActionInternal(paramContext, paramString1, paramString2, paramCollection, paramLabelOperations);
      }
    }
    , paramString2, true);
  }

  public void toggleLabel(final Context paramContext, final String paramString1, final String paramString2, final Collection<ConversationInfo> paramCollection, Set<String> paramSet, final String paramString3)
  {
    if (!paramSet.contains(paramString2));
    for (final boolean bool = true; ; bool = false)
    {
      LabelOperations localLabelOperations = new LabelOperations(paramString2, bool);
      maybeRunInBackground(paramContext, paramString1, localLabelOperations, paramCollection, new Runnable()
      {
        public void run()
        {
          BulkOperationHelper.this.performOperationInternal(paramContext, paramString1, paramString2, bool, paramCollection, paramString3, null);
        }
      }
      , paramString3, true);
      return;
    }
  }

  private static enum ThreadState
  {
    static
    {
      ACTIVE = new ThreadState("ACTIVE", 1);
      FINISHED = new ThreadState("FINISHED", 2);
      ThreadState[] arrayOfThreadState = new ThreadState[3];
      arrayOfThreadState[0] = IDLE;
      arrayOfThreadState[1] = ACTIVE;
      arrayOfThreadState[2] = FINISHED;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.BulkOperationHelper
 * JD-Core Version:    0.6.2
 */