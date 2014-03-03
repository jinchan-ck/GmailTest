package com.android.mail.ui;

import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.mail.browse.ConversationCursor;
import com.android.mail.browse.ConversationItemViewCoordinates;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.google.common.collect.ImmutableList;

public class LeaveBehindItem extends FrameLayout
  implements View.OnClickListener, SwipeableItemView
{
  private static int sFadeInAnimationDuration = -1;
  private static float sScrollSlop;
  private static int sShrinkAnimationDuration = -1;
  private Account mAccount;
  private AnimatedAdapter mAdapter;
  private int mAnimatedHeight = -1;
  private boolean mAnimating;
  private Conversation mData;
  private boolean mFadingInText;
  private View mSwipeableContent;
  private TextView mText;
  private ToastBarOperation mUndoOp;
  private int mWidth;
  public int position;

  public LeaveBehindItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public LeaveBehindItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, -1);
  }

  public LeaveBehindItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (sShrinkAnimationDuration == -1)
    {
      Resources localResources = paramContext.getResources();
      sShrinkAnimationDuration = localResources.getInteger(2131296265);
      sFadeInAnimationDuration = localResources.getInteger(2131296267);
      sScrollSlop = localResources.getInteger(2131296286);
    }
  }

  public void bindOperations(int paramInt, Account paramAccount, AnimatedAdapter paramAnimatedAdapter, ToastBarOperation paramToastBarOperation, Conversation paramConversation, Folder paramFolder)
  {
    this.position = paramInt;
    this.mUndoOp = paramToastBarOperation;
    this.mAccount = paramAccount;
    this.mAdapter = paramAnimatedAdapter;
    setData(paramConversation);
    this.mSwipeableContent = findViewById(2131689702);
    this.mSwipeableContent.setOnClickListener(this);
    this.mText = ((TextView)findViewById(2131689703));
    this.mText.setText(Html.fromHtml(this.mUndoOp.getSingularDescription(getContext(), paramFolder)));
    this.mText.setOnClickListener(this);
  }

  public boolean canChildBeDismissed()
  {
    return true;
  }

  public void commit()
  {
    ConversationCursor localConversationCursor = this.mAdapter.getConversationCursor();
    if (localConversationCursor != null)
      localConversationCursor.delete(getContext(), ImmutableList.of(getData()));
  }

  public void dismiss()
  {
    if (this.mAdapter != null)
    {
      this.mAdapter.fadeOutLeaveBehindItems();
      this.mAdapter.notifyDataSetChanged();
    }
  }

  public long getConversationId()
  {
    return getData().id;
  }

  public Conversation getData()
  {
    return this.mData;
  }

  public LeaveBehindData getLeaveBehindData()
  {
    return new LeaveBehindData(getData(), this.mUndoOp);
  }

  public float getMinAllowScrollDistance()
  {
    return sScrollSlop;
  }

  public View getSwipeableView()
  {
    return this.mSwipeableContent;
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
    case 2131689702:
    }
    ConversationCursor localConversationCursor;
    do
    {
      do
        return;
      while (this.mAccount.undoUri == null);
      this.mAdapter.clearLeaveBehind(getConversationId());
      this.mAdapter.setSwipeUndo(true);
      localConversationCursor = this.mAdapter.getConversationCursor();
    }
    while (localConversationCursor == null);
    localConversationCursor.undo(getContext(), this.mAccount.undoUri);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (this.mAnimatedHeight != -1)
    {
      setMeasuredDimension(this.mWidth, this.mAnimatedHeight);
      return;
    }
    super.onMeasure(paramInt1, paramInt2);
  }

  public void setAnimatedHeight(int paramInt)
  {
    this.mAnimatedHeight = paramInt;
    requestLayout();
  }

  public void setData(Conversation paramConversation)
  {
    this.mData = paramConversation;
  }

  public void showTextImmediately()
  {
    this.mFadingInText = true;
  }

  public void startAnimation(ViewMode paramViewMode, Animator.AnimatorListener paramAnimatorListener)
  {
    if (!this.mAnimating)
    {
      this.mAnimating = true;
      int i = ConversationItemViewCoordinates.getMinHeight(getContext(), paramViewMode);
      setMinimumHeight(i);
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofInt(this, "animatedHeight", new int[] { i, 0 });
      this.mAnimatedHeight = i;
      this.mWidth = getMeasuredWidth();
      this.mSwipeableContent.setVisibility(8);
      localObjectAnimator.setInterpolator(new DecelerateInterpolator(2.0F));
      localObjectAnimator.addListener(paramAnimatorListener);
      localObjectAnimator.setDuration(sShrinkAnimationDuration);
      localObjectAnimator.start();
    }
  }

  public void startFadeInAnimation()
  {
    if (!this.mFadingInText)
    {
      this.mFadingInText = true;
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(this.mText, "alpha", new float[] { 0.0F, 1.0F });
      localObjectAnimator.setInterpolator(new DecelerateInterpolator(2.0F));
      localObjectAnimator.setDuration(sFadeInAnimationDuration);
      localObjectAnimator.start();
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.LeaveBehindItem
 * JD-Core Version:    0.6.2
 */