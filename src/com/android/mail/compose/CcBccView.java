package com.android.mail.compose;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class CcBccView extends RelativeLayout
{
  private final View mBcc;
  private final View mCc;

  public CcBccView(Context paramContext)
  {
    this(paramContext, null);
  }

  public CcBccView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, -1);
  }

  public CcBccView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    LayoutInflater.from(paramContext).inflate(2130968591, this);
    this.mCc = findViewById(2131689514);
    this.mBcc = findViewById(2131689517);
  }

  private void animate(Boolean paramBoolean, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = getResources().getInteger(2131296261);
    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(this.mBcc, "alpha", new float[] { 0.0F, 1.0F });
    localObjectAnimator1.setDuration(i);
    Object localObject;
    if (!paramBoolean2)
    {
      ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this.mCc, "alpha", new float[] { 0.0F, 1.0F });
      localObjectAnimator2.setDuration(i);
      localObject = new AnimatorSet();
      ((AnimatorSet)localObject).playTogether(new Animator[] { localObjectAnimator2, localObjectAnimator1 });
    }
    while (true)
    {
      ((Animator)localObject).start();
      return;
      localObject = localObjectAnimator1;
    }
  }

  public boolean isBccVisible()
  {
    return this.mBcc.getVisibility() == 0;
  }

  public boolean isCcVisible()
  {
    return this.mCc.getVisibility() == 0;
  }

  public void show(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    boolean bool = this.mCc.isShown();
    View localView1 = this.mCc;
    int i;
    View localView2;
    int j;
    if (paramBoolean2)
    {
      i = 0;
      localView1.setVisibility(i);
      localView2 = this.mBcc;
      j = 0;
      if (!paramBoolean3)
        break label72;
    }
    while (true)
    {
      localView2.setVisibility(j);
      if (!paramBoolean1)
        break label79;
      animate(Boolean.valueOf(paramBoolean2), paramBoolean3, bool);
      return;
      i = 8;
      break;
      label72: j = 8;
    }
    label79: if (paramBoolean2)
      this.mCc.setAlpha(1.0F);
    if (paramBoolean3)
      this.mBcc.setAlpha(1.0F);
    requestLayout();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.CcBccView
 * JD-Core Version:    0.6.2
 */