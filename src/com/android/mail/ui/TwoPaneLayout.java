package com.android.mail.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.android.mail.utils.LogUtils;

final class TwoPaneLayout extends FrameLayout
  implements ViewMode.ModeChangeListener
{
  private AbstractActivityController mController;
  private final double mConversationListWeight;
  private View mConversationView;
  private int mCurrentMode = 0;
  private final double mFolderListWeight;
  private View mFoldersView;
  private boolean mIsSearchResult;
  private final boolean mListCollapsible;
  private ConversationListCopy mListCopyView;
  private Integer mListCopyWidthOnComplete;
  private View mListView;
  private LayoutListener mListener;
  private int mPositionedMode = 0;
  private final TimeInterpolator mSlideInterpolator;

  public TwoPaneLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public TwoPaneLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = getResources();
    this.mListCollapsible = localResources.getBoolean(2131623936);
    this.mSlideInterpolator = AnimationUtils.loadInterpolator(paramContext, 17563651);
    int i = localResources.getInteger(2131296281);
    int j = localResources.getInteger(2131296282);
    int k = localResources.getInteger(2131296283);
    this.mFolderListWeight = (i / (i + j));
    this.mConversationListWeight = (j / (j + k));
  }

  private void animatePanes(int paramInt1, int paramInt2, int paramInt3)
  {
    if (this.mPositionedMode == 0)
    {
      this.mConversationView.setX(paramInt3);
      this.mFoldersView.setX(paramInt1);
      this.mListView.setX(paramInt2);
      post(new Runnable()
      {
        public void run()
        {
          TwoPaneLayout.this.onTransitionComplete();
        }
      });
      return;
    }
    this.mListCopyView.bind(this.mListView);
    this.mListCopyView.setX(this.mListView.getX());
    this.mListCopyView.setAlpha(1.0F);
    this.mListView.setAlpha(0.0F);
    useHardwareLayer(true);
    this.mConversationView.animate().x(paramInt3);
    this.mFoldersView.animate().x(paramInt1);
    this.mListCopyView.animate().x(paramInt2).alpha(0.0F);
    this.mListView.animate().x(paramInt2).alpha(1.0F).setListener(new AnimatorListenerAdapter()
    {
      public void onAnimationCancel(Animator paramAnonymousAnimator)
      {
        TwoPaneLayout.this.mListCopyView.unbind();
        TwoPaneLayout.this.useHardwareLayer(false);
      }

      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        TwoPaneLayout.this.mListCopyView.unbind();
        TwoPaneLayout.this.useHardwareLayer(false);
        TwoPaneLayout.this.fixupListCopyWidth();
        TwoPaneLayout.this.onTransitionComplete();
      }
    });
    View[] arrayOfView = new View[4];
    arrayOfView[0] = this.mConversationView;
    arrayOfView[1] = this.mFoldersView;
    arrayOfView[2] = this.mListView;
    arrayOfView[3] = this.mListCopyView;
    configureAnimations(arrayOfView);
  }

  private int computeConversationListWidth(int paramInt)
  {
    switch (this.mCurrentMode)
    {
    case 3:
    default:
      return 0;
    case 2:
    case 4:
      return paramInt - computeFolderListWidth(paramInt);
    case 1:
    case 5:
    }
    return (int)(paramInt * this.mConversationListWeight);
  }

  private int computeConversationWidth(int paramInt)
  {
    if (this.mListCollapsible)
      return paramInt;
    return paramInt - (int)(paramInt * this.mConversationListWeight);
  }

  private int computeFolderListWidth(int paramInt)
  {
    if (this.mIsSearchResult)
      return 0;
    return (int)(paramInt * this.mFolderListWeight);
  }

  private void configureAnimations(View[] paramArrayOfView)
  {
    int i = paramArrayOfView.length;
    for (int j = 0; j < i; j++)
      paramArrayOfView[j].animate().setInterpolator(this.mSlideInterpolator).setDuration(300L);
  }

  private void dispatchConversationListVisibilityChange(boolean paramBoolean)
  {
    if (this.mListener != null)
      this.mListener.onConversationListVisibilityChanged(paramBoolean);
  }

  private void dispatchConversationVisibilityChanged(boolean paramBoolean)
  {
    if (this.mListener != null)
      this.mListener.onConversationVisibilityChanged(paramBoolean);
  }

  private void fixupListCopyWidth()
  {
    if ((this.mListCopyWidthOnComplete == null) || (getPaneWidth(this.mListCopyView) == this.mListCopyWidthOnComplete.intValue()))
    {
      this.mListCopyWidthOnComplete = null;
      return;
    }
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mListCopyWidthOnComplete;
    LogUtils.i("TwoPaneLayout", "onAnimationEnd of list view, setting copy width to %d", arrayOfObject);
    setPaneWidth(this.mListCopyView, this.mListCopyWidthOnComplete.intValue());
    this.mListCopyWidthOnComplete = null;
  }

  private int getPaneWidth(View paramView)
  {
    return paramView.getLayoutParams().width;
  }

  private void onTransitionComplete()
  {
    boolean bool = true;
    if (this.mController.isDestroyed())
    {
      LogUtils.i("TwoPaneLayout", "IN TPL.onTransitionComplete, activity destroyed->quitting early", new Object[0]);
      return;
    }
    switch (this.mCurrentMode)
    {
    case 3:
    default:
      return;
    case 1:
    case 5:
      dispatchConversationVisibilityChanged(bool);
      if (!isConversationListCollapsed());
      while (true)
      {
        dispatchConversationListVisibilityChange(bool);
        return;
        bool = false;
      }
    case 2:
    case 4:
    }
    dispatchConversationVisibilityChanged(false);
    dispatchConversationListVisibilityChange(bool);
  }

  private void positionPanes(int paramInt)
  {
    if (this.mPositionedMode == this.mCurrentMode)
      return;
    int i = this.mCurrentMode;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    switch (i)
    {
    case 3:
    default:
    case 1:
    case 5:
    case 2:
    case 4:
    }
    while (true)
    {
      if (m != 0)
        animatePanes(k, n, j);
      this.mPositionedMode = this.mCurrentMode;
      return;
      int i1 = getPaneWidth(this.mFoldersView);
      int i2 = getPaneWidth(this.mListView);
      if (this.mListCollapsible)
      {
        j = 0;
        n = -i2;
        k = n - i1;
      }
      while (true)
      {
        m = 1;
        Object[] arrayOfObject2 = new Object[3];
        arrayOfObject2[0] = Integer.valueOf(k);
        arrayOfObject2[1] = Integer.valueOf(n);
        arrayOfObject2[2] = Integer.valueOf(j);
        LogUtils.i("TwoPaneLayout", "conversation mode layout, x=%d/%d/%d", arrayOfObject2);
        break;
        j = i2;
        k = -i1;
        n = 0;
      }
      j = paramInt;
      n = getPaneWidth(this.mFoldersView);
      m = 1;
      Object[] arrayOfObject1 = new Object[3];
      arrayOfObject1[0] = Integer.valueOf(0);
      arrayOfObject1[1] = Integer.valueOf(n);
      arrayOfObject1[2] = Integer.valueOf(j);
      LogUtils.i("TwoPaneLayout", "conv-list mode layout, x=%d/%d/%d", arrayOfObject1);
      k = 0;
    }
  }

  private void setPaneWidth(View paramView, int paramInt)
  {
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    if (localLayoutParams.width == paramInt)
      return;
    localLayoutParams.width = paramInt;
    paramView.setLayoutParams(localLayoutParams);
  }

  private void setupPaneWidths(int paramInt)
  {
    int i = computeFolderListWidth(paramInt);
    int j = computeConversationWidth(paramInt);
    if (paramInt != getMeasuredWidth())
    {
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = Integer.valueOf(paramInt);
      arrayOfObject2[1] = Integer.valueOf(i);
      arrayOfObject2[2] = Integer.valueOf(j);
      LogUtils.i("TwoPaneLayout", "setting up new TPL, w=%d fw=%d cv=%d", arrayOfObject2);
      setPaneWidth(this.mFoldersView, i);
      setPaneWidth(this.mConversationView, j);
    }
    int k = getPaneWidth(this.mListView);
    switch (this.mCurrentMode)
    {
    case 3:
    default:
    case 1:
    case 5:
    case 2:
    case 4:
    }
    while (true)
    {
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Integer.valueOf(k);
      LogUtils.d("TwoPaneLayout", "conversation list width change, w=%d", arrayOfObject1);
      setPaneWidth(this.mListView, k);
      if (((this.mCurrentMode == this.mPositionedMode) || (this.mPositionedMode == 0)) && (this.mListCopyWidthOnComplete == null))
        break;
      this.mListCopyWidthOnComplete = Integer.valueOf(k);
      return;
      if (!this.mListCollapsible)
      {
        k = paramInt - j;
        continue;
        k = paramInt - i;
      }
    }
    setPaneWidth(this.mListCopyView, k);
  }

  private void useHardwareLayer(boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 2; ; i = 0)
    {
      this.mFoldersView.setLayerType(i, null);
      this.mListView.setLayerType(i, null);
      this.mListCopyView.setLayerType(i, null);
      this.mConversationView.setLayerType(i, null);
      if (paramBoolean)
      {
        this.mFoldersView.buildLayer();
        this.mListView.buildLayer();
        this.mListCopyView.buildLayer();
        this.mConversationView.buildLayer();
      }
      return;
    }
  }

  protected int computeConversationListWidth()
  {
    return computeConversationListWidth(getMeasuredWidth());
  }

  public boolean isConversationListCollapsed()
  {
    return (!ViewMode.isListMode(this.mCurrentMode)) && (this.mListCollapsible);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mFoldersView = findViewById(2131689648);
    this.mListView = findViewById(2131689711);
    this.mListCopyView = ((ConversationListCopy)findViewById(2131689712));
    this.mConversationView = findViewById(2131689632);
    this.mCurrentMode = 0;
    this.mFoldersView.setVisibility(8);
    this.mListView.setVisibility(8);
    this.mListCopyView.setVisibility(8);
    this.mConversationView.setVisibility(8);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    LogUtils.d("MailBlankFragment", "TPL(%s).onLayout()", new Object[] { this });
    if ((paramBoolean) || (this.mCurrentMode != this.mPositionedMode))
      positionPanes(getMeasuredWidth());
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    LogUtils.d("MailBlankFragment", "TPL(%s).onMeasure()", new Object[] { this });
    setupPaneWidths(View.MeasureSpec.getSize(paramInt1));
    super.onMeasure(paramInt1, paramInt2);
  }

  public void onViewModeChanged(int paramInt)
  {
    if (this.mCurrentMode == 0)
    {
      this.mFoldersView.setVisibility(0);
      this.mListView.setVisibility(0);
      this.mListCopyView.setVisibility(0);
      this.mConversationView.setVisibility(0);
    }
    if (ViewMode.isConversationMode(this.mCurrentMode))
      this.mController.disablePagerUpdates();
    this.mCurrentMode = paramInt;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    LogUtils.i("TwoPaneLayout", "onViewModeChanged(%d)", arrayOfObject);
    requestLayout();
  }

  public void setController(AbstractActivityController paramAbstractActivityController, boolean paramBoolean)
  {
    this.mController = paramAbstractActivityController;
    this.mListener = paramAbstractActivityController;
    this.mIsSearchResult = paramBoolean;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.TwoPaneLayout
 * JD-Core Version:    0.6.2
 */