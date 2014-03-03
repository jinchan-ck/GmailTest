package com.android.mail.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionableToastBar extends LinearLayout
{
  private View mActionButton;
  private ImageView mActionDescriptionIcon;
  private TextView mActionDescriptionView;
  private View mActionIcon;
  private TextView mActionText;
  private final int mBottomMarginSize;
  private final int mBottomMarginSizeInConversation;
  private boolean mHidden = false;
  private Animator mHideAnimation;
  private ToastBarOperation mOperation;
  private Animator mShowAnimation;

  public ActionableToastBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public ActionableToastBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public ActionableToastBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mBottomMarginSize = paramContext.getResources().getDimensionPixelSize(2131361887);
    this.mBottomMarginSizeInConversation = paramContext.getResources().getDimensionPixelSize(2131361888);
    LayoutInflater.from(paramContext).inflate(2130968583, this, true);
  }

  private Animator getHideAnimation()
  {
    if (this.mHideAnimation == null)
    {
      this.mHideAnimation = AnimatorInflater.loadAnimator(getContext(), 2131034115);
      this.mHideAnimation.addListener(new Animator.AnimatorListener()
      {
        public void onAnimationCancel(Animator paramAnonymousAnimator)
        {
        }

        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          ActionableToastBar.this.setVisibility(8);
        }

        public void onAnimationRepeat(Animator paramAnonymousAnimator)
        {
        }

        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
        }
      });
      this.mHideAnimation.setTarget(this);
    }
    return this.mHideAnimation;
  }

  private Animator getShowAnimation()
  {
    if (this.mShowAnimation == null)
    {
      this.mShowAnimation = AnimatorInflater.loadAnimator(getContext(), 2131034114);
      this.mShowAnimation.addListener(new Animator.AnimatorListener()
      {
        public void onAnimationCancel(Animator paramAnonymousAnimator)
        {
        }

        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
        }

        public void onAnimationRepeat(Animator paramAnonymousAnimator)
        {
        }

        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
          ActionableToastBar.this.setVisibility(0);
        }
      });
      this.mShowAnimation.setTarget(this);
    }
    return this.mShowAnimation;
  }

  public ToastBarOperation getOperation()
  {
    return this.mOperation;
  }

  public void hide(boolean paramBoolean)
  {
    this.mHidden = true;
    if (getVisibility() == 0)
    {
      this.mActionDescriptionView.setText("");
      this.mActionButton.setOnClickListener(null);
      if (paramBoolean)
        getHideAnimation().start();
    }
    else
    {
      return;
    }
    setAlpha(0.0F);
    setVisibility(8);
  }

  public boolean isAnimating()
  {
    return (this.mShowAnimation != null) && (this.mShowAnimation.isStarted());
  }

  public boolean isEventInToastBar(MotionEvent paramMotionEvent)
  {
    boolean bool = true;
    if (!isShown())
      return false;
    int[] arrayOfInt = new int[2];
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    getLocationOnScreen(arrayOfInt);
    if ((f1 > arrayOfInt[0]) && (f1 < arrayOfInt[0] + getWidth()) && (f2 > arrayOfInt[bool]) && (f2 < arrayOfInt[bool] + getHeight()));
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mActionDescriptionIcon = ((ImageView)findViewById(2131689496));
    this.mActionDescriptionView = ((TextView)findViewById(2131689497));
    this.mActionButton = findViewById(2131689498);
    this.mActionIcon = findViewById(2131689500);
    this.mActionText = ((TextView)findViewById(2131689501));
  }

  public void setConversationMode(boolean paramBoolean)
  {
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)getLayoutParams();
    if (paramBoolean);
    for (int i = this.mBottomMarginSizeInConversation; ; i = this.mBottomMarginSize)
    {
      localLayoutParams.bottomMargin = i;
      setLayoutParams(localLayoutParams);
      return;
    }
  }

  public void show(final ActionClickedListener paramActionClickedListener, int paramInt1, CharSequence paramCharSequence, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, ToastBarOperation paramToastBarOperation)
  {
    int i = 8;
    if ((!this.mHidden) && (!paramBoolean2))
      return;
    this.mOperation = paramToastBarOperation;
    this.mActionButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramActionClickedListener.onActionClicked();
        ActionableToastBar.this.hide(true);
      }
    });
    if (paramInt1 == 0)
      this.mActionDescriptionIcon.setVisibility(i);
    while (true)
    {
      this.mActionDescriptionView.setText(paramCharSequence);
      View localView = this.mActionIcon;
      if (paramBoolean1)
        i = 0;
      localView.setVisibility(i);
      this.mActionText.setText(paramInt2);
      this.mHidden = false;
      getShowAnimation().start();
      return;
      this.mActionDescriptionIcon.setVisibility(0);
      this.mActionDescriptionIcon.setImageResource(paramInt1);
    }
  }

  public static abstract interface ActionClickedListener
  {
    public abstract void onActionClicked();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ActionableToastBar
 * JD-Core Version:    0.6.2
 */