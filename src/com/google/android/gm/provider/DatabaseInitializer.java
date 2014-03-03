package com.google.android.gm.provider;

import android.database.sqlite.SQLiteDatabase;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class DatabaseInitializer
{
  protected final SQLiteDatabase mDb;
  protected final MailEngine mEngine;
  protected final int mInitialDbVersion;

  DatabaseInitializer(MailEngine paramMailEngine, SQLiteDatabase paramSQLiteDatabase)
  {
    this.mEngine = paramMailEngine;
    this.mDb = paramSQLiteDatabase;
    this.mInitialDbVersion = paramSQLiteDatabase.getVersion();
  }

  private int upgradeDatabase(int paramInt)
  {
    int i = getTargetDbVersion(paramInt);
    Method localMethod = findUpgradeMethod(i);
    Object localObject = null;
    try
    {
      localMethod.invoke(this, new Object[0]);
      this.mDb.setVersion(i);
      if (localObject != null)
        throw new IllegalStateException("Failed to invoke upgrade Method", (Throwable)localObject);
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      while (true)
        localObject = localInvocationTargetException;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      while (true)
        localObject = localIllegalAccessException;
    }
    return i;
  }

  public abstract void bootstrapDatabase();

  Method findUpgradeMethod(int paramInt)
  {
    Class localClass = getClass();
    try
    {
      Method localMethod = localClass.getMethod("upgradeDbTo" + paramInt, (Class[])null);
      return localMethod;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new IllegalStateException("Missing upgrade to version: " + paramInt, localNoSuchMethodException);
    }
  }

  int getTargetDbVersion(int paramInt)
  {
    return paramInt + 1;
  }

  void performUpgrade(int paramInt)
  {
    for (int i = this.mInitialDbVersion; i < paramInt; i = upgradeDatabase(i));
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.DatabaseInitializer
 * JD-Core Version:    0.6.2
 */