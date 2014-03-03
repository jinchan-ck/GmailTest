package com.google.android.gm;

import java.util.ArrayList;

public abstract interface LabelSettingsObservable
{
  public abstract ArrayList<String> getIncludedLabels();

  public abstract int getNumberOfSyncDays();

  public abstract ArrayList<String> getPartialLabels();

  public abstract void notifyChanged();

  public abstract void registerObserver(LabelSettingsObserver paramLabelSettingsObserver);

  public abstract void setIncludedLabels(ArrayList<String> paramArrayList);

  public abstract void setPartialLabels(ArrayList<String> paramArrayList);

  public abstract void unregisterObserver(LabelSettingsObserver paramLabelSettingsObserver);
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelSettingsObservable
 * JD-Core Version:    0.6.2
 */