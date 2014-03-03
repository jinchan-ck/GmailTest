package com.android.mail;

import android.database.DataSetObserver;

public abstract interface ContactInfoSource
{
  public abstract ContactInfo getContactInfo(String paramString);

  public abstract void registerObserver(DataSetObserver paramDataSetObserver);

  public abstract void unregisterObserver(DataSetObserver paramDataSetObserver);
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ContactInfoSource
 * JD-Core Version:    0.6.2
 */