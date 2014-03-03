package com.google.android.gm;

public abstract interface IProgressMonitor
{
  public static final int UNKNOWN = -1;

  public abstract void beginTask(CharSequence paramCharSequence, int paramInt);

  public abstract void done();
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.IProgressMonitor
 * JD-Core Version:    0.6.2
 */