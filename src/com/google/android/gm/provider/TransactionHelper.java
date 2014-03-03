package com.google.android.gm.provider;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;
import java.util.Stack;

public class TransactionHelper
{
  private final SQLiteDatabase mDb;
  private final ThreadLocal<Stack<ThreadTransactionState>> mStates;
  private final ThreadLocal<Boolean> mSuppressUiNotifications = new ThreadLocal()
  {
    protected Boolean initialValue()
    {
      return Boolean.FALSE;
    }
  };

  public TransactionHelper(SQLiteDatabase paramSQLiteDatabase)
  {
    this.mDb = paramSQLiteDatabase;
    this.mStates = new ThreadLocal()
    {
      protected Stack<TransactionHelper.ThreadTransactionState> initialValue()
      {
        return new Stack();
      }
    };
  }

  private Stack<ThreadTransactionState> getStates()
  {
    return (Stack)this.mStates.get();
  }

  public void beginTransactionNonExclusive()
  {
    getStates().push(new ThreadTransactionState(null));
    this.mDb.beginTransactionNonExclusive();
  }

  public void beginTransactionWithListenerNonExclusive(BetterTransactionListener paramBetterTransactionListener)
  {
    getStates().push(new ThreadTransactionState(paramBetterTransactionListener));
    this.mDb.beginTransactionWithListenerNonExclusive(paramBetterTransactionListener);
  }

  public void endTransaction()
  {
    this.mDb.endTransaction();
    ThreadTransactionState localThreadTransactionState = (ThreadTransactionState)getStates().pop();
    int i;
    if ((localThreadTransactionState.mSuccess) && (!localThreadTransactionState.mChildFailed))
    {
      i = 1;
      if (localThreadTransactionState.mListener != null)
      {
        if (i == 0)
          break label100;
        localThreadTransactionState.mListener.onCommitCompleted(((Boolean)this.mSuppressUiNotifications.get()).booleanValue());
      }
    }
    while (true)
    {
      if (i == 0)
      {
        Stack localStack = getStates();
        if (!localStack.empty())
          ((ThreadTransactionState)localStack.peek()).mChildFailed = true;
      }
      return;
      i = 0;
      break;
      label100: localThreadTransactionState.mListener.onRollbackCompleted();
    }
  }

  public void setTransactionSuccessful()
  {
    this.mDb.setTransactionSuccessful();
    ((ThreadTransactionState)getStates().peek()).mSuccess = true;
  }

  void suppressUiNotifications()
  {
    this.mSuppressUiNotifications.set(Boolean.TRUE);
  }

  public static abstract interface BetterTransactionListener extends SQLiteTransactionListener
  {
    public abstract void onCommitCompleted(boolean paramBoolean);

    public abstract void onRollbackCompleted();
  }

  private static class ThreadTransactionState
  {
    public boolean mChildFailed = false;
    public final TransactionHelper.BetterTransactionListener mListener;
    public boolean mSuccess = false;

    public ThreadTransactionState(TransactionHelper.BetterTransactionListener paramBetterTransactionListener)
    {
      this.mListener = paramBetterTransactionListener;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.TransactionHelper
 * JD-Core Version:    0.6.2
 */