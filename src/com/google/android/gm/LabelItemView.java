package com.google.android.gm;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gm.provider.Label;

public class LabelItemView extends RelativeLayout
{
  private static Drawable DRAG_STEADY_STATE_BACKGROUND;
  private static Drawable DROPPABLE_HOVER_BACKGROUND;
  private static int NON_DROPPABLE_TARGET_TEXT_COLOR;
  private static Bitmap SHORTCUT_ICON;
  private Drawable mBackground;
  private DropHandler mDropHandler;
  private ColorStateList mInitialLabelTextColor;
  private ColorStateList mInitialUnreadCountTextColor;
  private Label mLabel;
  private TextView mLabelTextView;
  private TextView mUnreadCountTextView;

  public LabelItemView(Context paramContext)
  {
    super(paramContext);
  }

  public LabelItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public LabelItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private boolean isDroppableTarget(DragEvent paramDragEvent)
  {
    return (this.mDropHandler != null) && (this.mDropHandler.supportsDrag(paramDragEvent, this.mLabel));
  }

  public void bind(String paramString, Label paramLabel, DropHandler paramDropHandler)
  {
    this.mLabel = paramLabel;
    this.mDropHandler = paramDropHandler;
  }

  public boolean onDragEvent(DragEvent paramDragEvent)
  {
    boolean bool = true;
    switch (paramDragEvent.getAction())
    {
    default:
    case 2:
    case 1:
    case 5:
    case 6:
      do
      {
        do
        {
          bool = false;
          return bool;
          if (!isDroppableTarget(paramDragEvent))
          {
            this.mInitialLabelTextColor = this.mLabelTextView.getTextColors();
            this.mInitialUnreadCountTextColor = this.mUnreadCountTextView.getTextColors();
            this.mLabelTextView.setTextColor(NON_DROPPABLE_TARGET_TEXT_COLOR);
            this.mUnreadCountTextView.setTextColor(NON_DROPPABLE_TARGET_TEXT_COLOR);
          }
          setBackgroundDrawable(DRAG_STEADY_STATE_BACKGROUND);
          return bool;
        }
        while (!isDroppableTarget(paramDragEvent));
        setBackgroundDrawable(DROPPABLE_HOVER_BACKGROUND);
        return bool;
      }
      while (!isDroppableTarget(paramDragEvent));
      setBackgroundDrawable(DRAG_STEADY_STATE_BACKGROUND);
      return bool;
    case 4:
      if (!isDroppableTarget(paramDragEvent))
      {
        this.mLabelTextView.setTextColor(this.mInitialLabelTextColor);
        this.mUnreadCountTextView.setTextColor(this.mInitialUnreadCountTextColor);
      }
      setBackgroundDrawable(this.mBackground);
      return bool;
    case 3:
    }
    if (this.mDropHandler == null)
      return false;
    this.mDropHandler.handleDrop(paramDragEvent, this.mLabel);
    return bool;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    if (SHORTCUT_ICON == null)
    {
      Resources localResources = getResources();
      SHORTCUT_ICON = BitmapFactory.decodeResource(localResources, 2130903042);
      DROPPABLE_HOVER_BACKGROUND = localResources.getDrawable(2130837534);
      DRAG_STEADY_STATE_BACKGROUND = localResources.getDrawable(2130837536);
      NON_DROPPABLE_TARGET_TEXT_COLOR = localResources.getColor(2131230755);
    }
    this.mLabelTextView = ((TextView)findViewById(2131689523));
    this.mUnreadCountTextView = ((TextView)findViewById(2131689522));
    this.mBackground = getBackground();
    this.mInitialLabelTextColor = this.mLabelTextView.getTextColors();
    this.mInitialUnreadCountTextColor = this.mUnreadCountTextView.getTextColors();
  }

  public static abstract interface DropHandler
  {
    public abstract void handleDrop(DragEvent paramDragEvent, Label paramLabel);

    public abstract boolean supportsDrag(DragEvent paramDragEvent, Label paramLabel);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelItemView
 * JD-Core Version:    0.6.2
 */