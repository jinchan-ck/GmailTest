package com.android.mail.browse;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.ui.AnimatedAdapter;
import com.android.mail.ui.ControllableActivity;
import com.android.mail.ui.ConversationSelectionSet;
import com.android.mail.ui.ViewMode;

public class SwipeableConversationItemView extends FrameLayout
  implements ToggleableItem
{
  private View mBackground;
  private ConversationItemView mConversationItemView;

  public SwipeableConversationItemView(Context paramContext, String paramString)
  {
    super(paramContext);
    this.mConversationItemView = new ConversationItemView(paramContext, paramString);
    addView(this.mConversationItemView);
  }

  public void addBackground(Context paramContext)
  {
    this.mBackground = findViewById(2131689513);
    if (this.mBackground == null)
    {
      this.mBackground = LayoutInflater.from(paramContext).inflate(2130968590, null, true);
      addView(this.mBackground, 0);
    }
  }

  public void bind(Cursor paramCursor, ControllableActivity paramControllableActivity, ConversationSelectionSet paramConversationSelectionSet, Folder paramFolder, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, AnimatedAdapter paramAnimatedAdapter)
  {
    this.mConversationItemView.bind(paramCursor, paramControllableActivity, paramConversationSelectionSet, paramFolder, paramBoolean1, paramBoolean2, paramBoolean3, paramAnimatedAdapter);
  }

  public void bind(Conversation paramConversation, ControllableActivity paramControllableActivity, ConversationSelectionSet paramConversationSelectionSet, Folder paramFolder, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, AnimatedAdapter paramAnimatedAdapter)
  {
    this.mConversationItemView.bind(paramConversation, paramControllableActivity, paramConversationSelectionSet, paramFolder, paramBoolean1, paramBoolean2, paramBoolean3, paramAnimatedAdapter);
  }

  public ListView getListView()
  {
    return (ListView)getParent();
  }

  public ConversationItemView getSwipeableItemView()
  {
    return this.mConversationItemView;
  }

  public void removeBackground()
  {
    if (this.mBackground != null)
      removeView(this.mBackground);
    this.mBackground = null;
  }

  public void reset()
  {
    setBackgroundVisibility(8);
    this.mConversationItemView.reset();
  }

  public void setBackgroundVisibility(int paramInt)
  {
    if (this.mBackground != null)
      this.mBackground.setVisibility(paramInt);
  }

  public void startDeleteAnimation(AnimatedAdapter paramAnimatedAdapter, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mConversationItemView.startDestroyWithSwipeAnimation(paramAnimatedAdapter);
      return;
    }
    this.mConversationItemView.startDestroyAnimation(paramAnimatedAdapter);
  }

  public void startUndoAnimation(ViewMode paramViewMode, AnimatedAdapter paramAnimatedAdapter, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      addBackground(getContext());
      setBackgroundVisibility(0);
      this.mConversationItemView.startSwipeUndoAnimation(paramViewMode, paramAnimatedAdapter);
      return;
    }
    setBackgroundVisibility(8);
    this.mConversationItemView.startUndoAnimation(paramViewMode, paramAnimatedAdapter);
  }

  public void toggleCheckMarkOrBeginDrag()
  {
    if (this.mConversationItemView != null)
      this.mConversationItemView.toggleCheckMarkOrBeginDrag();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.SwipeableConversationItemView
 * JD-Core Version:    0.6.2
 */