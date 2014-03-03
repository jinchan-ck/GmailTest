package com.google.android.gm;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class RecentLabelLoader extends AsyncTaskLoader<RecentLabelsCache.RecentLabelList>
{
  private final String mAccount;
  private UiHandler mDefaultTouchHandler;

  public RecentLabelLoader(Context paramContext, String paramString, UiHandler paramUiHandler)
  {
    super(paramContext);
    this.mAccount = paramString;
    this.mDefaultTouchHandler = paramUiHandler;
  }

  public RecentLabelsCache.RecentLabelList loadInBackground()
  {
    return RecentLabelsCache.getInstance(getContext()).getRecentLabelNames(this.mAccount, this.mDefaultTouchHandler);
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
    cancelLoad();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.RecentLabelLoader
 * JD-Core Version:    0.6.2
 */