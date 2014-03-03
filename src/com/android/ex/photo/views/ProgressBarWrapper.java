package com.android.ex.photo.views;

import android.widget.ProgressBar;

public class ProgressBarWrapper
{
  private final ProgressBar mDeterminate;
  private final ProgressBar mIndeterminate;
  private boolean mIsIndeterminate;

  public ProgressBarWrapper(ProgressBar paramProgressBar1, ProgressBar paramProgressBar2, boolean paramBoolean)
  {
    this.mDeterminate = paramProgressBar1;
    this.mIndeterminate = paramProgressBar2;
    setIndeterminate(paramBoolean);
  }

  private void setVisibility(boolean paramBoolean)
  {
    int i = 8;
    ProgressBar localProgressBar1 = this.mIndeterminate;
    int j;
    ProgressBar localProgressBar2;
    if (paramBoolean)
    {
      j = 0;
      localProgressBar1.setVisibility(j);
      localProgressBar2 = this.mDeterminate;
      if (!paramBoolean)
        break label44;
    }
    while (true)
    {
      localProgressBar2.setVisibility(i);
      return;
      j = i;
      break;
      label44: i = 0;
    }
  }

  public void setIndeterminate(boolean paramBoolean)
  {
    this.mIsIndeterminate = paramBoolean;
    setVisibility(this.mIsIndeterminate);
  }

  public void setMax(int paramInt)
  {
    this.mDeterminate.setMax(paramInt);
  }

  public void setProgress(int paramInt)
  {
    this.mDeterminate.setProgress(paramInt);
  }

  public void setVisibility(int paramInt)
  {
    if ((paramInt == 4) || (paramInt == 8))
    {
      this.mIndeterminate.setVisibility(paramInt);
      this.mDeterminate.setVisibility(paramInt);
      return;
    }
    setVisibility(this.mIsIndeterminate);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.photo.views.ProgressBarWrapper
 * JD-Core Version:    0.6.2
 */