package com.google.android.gm.provider;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class LabelLoader extends AsyncTaskLoader<LabelList>
{
  private final String mAccount;
  private final boolean mAutoRefresh;
  private LabelList mLabelList;

  public LabelLoader(Context paramContext, String paramString, boolean paramBoolean)
  {
    super(paramContext);
    this.mAccount = paramString;
    this.mAutoRefresh = paramBoolean;
  }

  public void deliverResult(LabelList paramLabelList)
  {
    super.deliverResult(paramLabelList);
    if (this.mAutoRefresh)
    {
      if (this.mLabelList != null)
        this.mLabelList.unregisterForLabelChanges();
      this.mLabelList = paramLabelList;
      if (this.mLabelList != null)
        this.mLabelList.registerForLabelChanges();
    }
  }

  public LabelList loadInBackground()
  {
    return LabelManager.getLabelList(getContext(), this.mAccount, null, false);
  }

  protected void onReset()
  {
    stopLoading();
  }

  protected void onStartLoading()
  {
    forceLoad();
  }

  protected void onStopLoading()
  {
    if ((this.mAutoRefresh) && (this.mLabelList != null))
    {
      this.mLabelList.unregisterForLabelChanges();
      this.mLabelList = null;
    }
    cancelLoad();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.LabelLoader
 * JD-Core Version:    0.6.2
 */