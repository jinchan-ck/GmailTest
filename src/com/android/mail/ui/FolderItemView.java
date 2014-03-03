package com.android.mail.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.mail.providers.Folder;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;

public class FolderItemView extends RelativeLayout
{
  private static Drawable DRAG_STEADY_STATE_BACKGROUND;
  private static Drawable DROPPABLE_HOVER_BACKGROUND;
  private static int NON_DROPPABLE_TARGET_TEXT_COLOR;
  private static Bitmap SHORTCUT_ICON;
  private final String LOG_TAG = LogTag.getLogTag();
  private Drawable mBackground;
  private DropHandler mDropHandler;
  private Folder mFolder;
  private ImageView mFolderParentIcon;
  private TextView mFolderTextView;
  private ColorStateList mInitialFolderTextColor;
  private ColorStateList mInitialUnreadCountTextColor;
  private TextView mUnreadCountTextView;

  public FolderItemView(Context paramContext)
  {
    super(paramContext);
  }

  public FolderItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public FolderItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private boolean isDroppableTarget(DragEvent paramDragEvent)
  {
    return (this.mDropHandler != null) && (this.mDropHandler.supportsDrag(paramDragEvent, this.mFolder));
  }

  private final void setUnreadCount(int paramInt)
  {
    TextView localTextView = this.mUnreadCountTextView;
    if (paramInt > 0);
    for (int i = 0; ; i = 8)
    {
      localTextView.setVisibility(i);
      if (paramInt > 0)
        this.mUnreadCountTextView.setText(Utils.getUnreadCountString(getContext(), paramInt));
      return;
    }
  }

  public void bind(Folder paramFolder, DropHandler paramDropHandler)
  {
    this.mFolder = paramFolder;
    this.mDropHandler = paramDropHandler;
    this.mFolderTextView.setText(paramFolder.name);
    ImageView localImageView = this.mFolderParentIcon;
    if (this.mFolder.hasChildren);
    for (int i = 0; ; i = 8)
    {
      localImageView.setVisibility(i);
      setUnreadCount(Utils.getFolderUnreadDisplayCount(this.mFolder));
      return;
    }
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
            this.mInitialFolderTextColor = this.mFolderTextView.getTextColors();
            this.mInitialUnreadCountTextColor = this.mUnreadCountTextView.getTextColors();
            this.mFolderTextView.setTextColor(NON_DROPPABLE_TARGET_TEXT_COLOR);
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
        this.mFolderTextView.setTextColor(this.mInitialFolderTextColor);
        this.mUnreadCountTextView.setTextColor(this.mInitialUnreadCountTextColor);
      }
      setBackgroundDrawable(this.mBackground);
      return bool;
    case 3:
    }
    if (this.mDropHandler == null)
      return false;
    this.mDropHandler.handleDrop(paramDragEvent, this.mFolder);
    return bool;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    if (SHORTCUT_ICON == null)
    {
      Resources localResources = getResources();
      SHORTCUT_ICON = BitmapFactory.decodeResource(localResources, 2130903041);
      DROPPABLE_HOVER_BACKGROUND = localResources.getDrawable(2130837534);
      DRAG_STEADY_STATE_BACKGROUND = localResources.getDrawable(2130837536);
      NON_DROPPABLE_TARGET_TEXT_COLOR = localResources.getColor(2131230755);
    }
    this.mFolderTextView = ((TextView)findViewById(2131689523));
    this.mUnreadCountTextView = ((TextView)findViewById(2131689522));
    this.mBackground = getBackground();
    this.mInitialFolderTextColor = this.mFolderTextView.getTextColors();
    this.mInitialUnreadCountTextColor = this.mUnreadCountTextView.getTextColors();
    this.mFolderParentIcon = ((ImageView)findViewById(2131689520));
  }

  public final void overrideUnreadCount(int paramInt)
  {
    String str = this.LOG_TAG;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = this.mUnreadCountTextView.getText();
    arrayOfObject[1] = Integer.valueOf(paramInt);
    LogUtils.e(str, "FLF->FolderItem.getFolderView: unread count mismatch found (%s vs %d)", arrayOfObject);
    setUnreadCount(paramInt);
  }

  public static abstract interface DropHandler
  {
    public abstract void handleDrop(DragEvent paramDragEvent, Folder paramFolder);

    public abstract boolean supportsDrag(DragEvent paramDragEvent, Folder paramFolder);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.FolderItemView
 * JD-Core Version:    0.6.2
 */