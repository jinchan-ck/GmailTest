package com.google.android.gm.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

class GmailTransactionListener
  implements TransactionHelper.BetterTransactionListener
{
  private final boolean DEBUG = false;
  private final String mAccount;
  private final Context mContext;
  private final SQLiteDatabase mDb;
  private final MailEngine mEngine;
  private final ThreadLocal<GmailTransactionState> mState;
  private SQLiteTransactionListener mTestTransactionListener;

  public GmailTransactionListener(Context paramContext, MailEngine paramMailEngine, String paramString)
  {
    this.mContext = paramContext;
    this.mAccount = paramString;
    this.mEngine = paramMailEngine;
    this.mDb = this.mEngine.mDb;
    this.mState = new ThreadLocal()
    {
      protected GmailTransactionListener.GmailTransactionState initialValue()
      {
        return new GmailTransactionListener.GmailTransactionState(GmailTransactionListener.this);
      }
    };
  }

  private Set<Long> getConversationIdsSet()
  {
    return ((GmailTransactionState)this.mState.get()).mConversationIds;
  }

  private Set<Long> getLabelIdsSet()
  {
    return ((GmailTransactionState)this.mState.get()).mLabelIds;
  }

  public void addConversationToNotify(long paramLong)
  {
    if (!this.mDb.inTransaction())
    {
      arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Long.valueOf(paramLong);
      LogUtils.e("Gmail", "Must already be in a transaction with listener to add conversation to notify. (id=%d)", arrayOfObject2);
    }
    while (!getConversationIdsSet().add(Long.valueOf(paramLong)))
    {
      Object[] arrayOfObject2;
      return;
    }
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Long.valueOf(paramLong);
    LogUtils.d("Gmail", "adding convId (%d) to notify", arrayOfObject1);
  }

  public void addLabelToNotify(Set<Long> paramSet)
  {
    if (!this.mDb.inTransaction())
      LogUtils.e("Gmail", "Must already be in a transaction with listener to add label to notify. (ids=%s)", new Object[] { paramSet });
    while (!getLabelIdsSet().addAll(paramSet))
      return;
    LogUtils.d("Gmail", "adding labelIds (%s) to notify", new Object[] { paramSet });
  }

  public void enableGmailAccountNotifications(boolean paramBoolean)
  {
    if (!this.mDb.inTransaction())
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = this.mAccount;
      LogUtils.e("Gmail", "Must already be in a transaction with listener to enable notifications for account %s.", arrayOfObject);
    }
    GmailTransactionState localGmailTransactionState;
    do
    {
      return;
      localGmailTransactionState = (GmailTransactionState)this.mState.get();
      localGmailTransactionState.mSendGmailAccountNotifications = true;
    }
    while ((!paramBoolean) || (localGmailTransactionState.mScheduleSyncOnAccountNotification));
    localGmailTransactionState.mScheduleSyncOnAccountNotification = true;
  }

  public void onBegin()
  {
    MailIndexerService.onContentProviderAccess(this.mAccount);
    if (this.mTestTransactionListener != null)
      this.mTestTransactionListener.onBegin();
  }

  public void onCommit()
  {
    if (this.mTestTransactionListener != null)
      this.mTestTransactionListener.onCommit();
  }

  public void onCommitCompleted(boolean paramBoolean)
  {
    if (!this.mDb.inTransaction())
    {
      Set localSet1 = getConversationIdsSet();
      if (!paramBoolean)
      {
        if (!localSet1.isEmpty())
        {
          LogUtils.d("Gmail", "Outermost commit complete, notifying on conversations: %s", new Object[] { localSet1 });
          Iterator localIterator = localSet1.iterator();
          while (localIterator.hasNext())
          {
            Long localLong = (Long)localIterator.next();
            UiProvider.onConversationChanged(this.mContext, this.mAccount, localLong.longValue());
          }
        }
        UiProvider.broadcastAccountChangeNotification(this.mContext, this.mAccount);
      }
      localSet1.clear();
      Set localSet2 = getLabelIdsSet();
      this.mEngine.broadcastLabelNotificationsImpl(localSet2);
      localSet2.clear();
      GmailTransactionState localGmailTransactionState = (GmailTransactionState)this.mState.get();
      if (localGmailTransactionState.mSendGmailAccountNotifications)
        this.mEngine.sendAccountNotifications(localGmailTransactionState.mScheduleSyncOnAccountNotification);
      localGmailTransactionState.mSendGmailAccountNotifications = false;
      localGmailTransactionState.mScheduleSyncOnAccountNotification = false;
    }
  }

  public void onRollback()
  {
    if (this.mTestTransactionListener != null)
      this.mTestTransactionListener.onRollback();
  }

  public void onRollbackCompleted()
  {
    if (!this.mDb.inTransaction())
    {
      Set localSet1 = getConversationIdsSet();
      if (!localSet1.isEmpty())
        LogUtils.d("Gmail", "Rolled back outermost conversation commit, NOT notifying on: %s", new Object[] { localSet1 });
      localSet1.clear();
      Set localSet2 = getLabelIdsSet();
      if (!localSet2.isEmpty())
        LogUtils.d("Gmail", "Rolled back outermost label commit, NOT notifying on: %s", new Object[] { localSet2 });
      localSet2.clear();
    }
  }

  public void setTestTransactionListener(SQLiteTransactionListener paramSQLiteTransactionListener)
  {
    this.mTestTransactionListener = paramSQLiteTransactionListener;
  }

  private class GmailTransactionState
  {
    public final Set<Long> mConversationIds = Sets.newHashSet();
    public final Set<Long> mLabelIds = Sets.newHashSet();
    public boolean mScheduleSyncOnAccountNotification = false;
    public boolean mSendGmailAccountNotifications = false;
    private final Stack<Throwable> mStackTraces = (Stack)null;

    public GmailTransactionState()
    {
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.GmailTransactionListener
 * JD-Core Version:    0.6.2
 */