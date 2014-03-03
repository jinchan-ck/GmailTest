package com.google.android.gm.provider;

import android.database.sqlite.SQLiteDatabase;

class InternalSettingsDbInitializer extends DatabaseInitializer
{
  InternalSettingsDbInitializer(MailEngine paramMailEngine)
  {
    super(paramMailEngine, paramMailEngine.mInternalDb);
  }

  public void bootstrapDatabase()
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = this.mDb.getPath();
    arrayOfObject[1] = Integer.valueOf(this.mDb.getVersion());
    LogUtils.i("Gmail", "Bootstrapping db:%s Current version is %d", arrayOfObject);
    this.mDb.execSQL("DROP TABLE IF EXISTS internal_sync_settings");
    this.mDb.execSQL("CREATE TABLE internal_sync_settings (_id INTEGER PRIMARY KEY,name TEXT,value TEXT,UNIQUE (name))");
    this.mDb.setVersion(2);
  }

  public void upgradeDbTo2()
  {
    this.mEngine.sendUpgradeSyncWindowIntent();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.InternalSettingsDbInitializer
 * JD-Core Version:    0.6.2
 */