package com.google.android.gm.provider;

class LabelRecord
{
  public long dateReceived = 0L;
  public boolean isZombie = true;
  public long sortMessageId = 0L;

  public LabelRecord()
  {
  }

  public LabelRecord(long paramLong1, long paramLong2, boolean paramBoolean)
  {
    this.sortMessageId = paramLong1;
    this.dateReceived = paramLong2;
    this.isZombie = paramBoolean;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.LabelRecord
 * JD-Core Version:    0.6.2
 */