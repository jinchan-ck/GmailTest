package com.android.mail.browse;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.mail.providers.Folder;
import com.android.mail.providers.UIProvider.CursorStatus;
import com.android.mail.ui.ViewMode.ModeChangeListener;
import com.android.mail.utils.Utils;

public final class ConversationListFooterView extends LinearLayout
  implements View.OnClickListener, ViewMode.ModeChangeListener
{
  private static Drawable sNormalBackground;
  private static Drawable sWideBackground;
  private FooterViewClickListener mClickListener;
  private Button mErrorActionButton;
  private int mErrorStatus;
  private TextView mErrorText;
  private Folder mFolder;
  private View mLoadMore;
  private Uri mLoadMoreUri;
  private View mLoading;
  private View mNetworkError;
  private final boolean mTabletDevice;

  public ConversationListFooterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mTabletDevice = Utils.useTabletUI(paramContext);
  }

  private Drawable getBackground(int paramInt)
  {
    return getContext().getResources().getDrawable(paramInt);
  }

  private Drawable getNormalBackground()
  {
    if (sNormalBackground == null)
      sNormalBackground = getBackground(2130837528);
    return sNormalBackground;
  }

  private Drawable getWideBackground()
  {
    if (sWideBackground == null)
      sWideBackground = getBackground(2130837530);
    return sWideBackground;
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    Folder localFolder = (Folder)paramView.getTag();
    switch (i)
    {
    case 2131689564:
    default:
      return;
    case 2131689563:
      this.mClickListener.onFooterViewErrorActionClick(localFolder, this.mErrorStatus);
      return;
    case 2131689565:
    }
    this.mClickListener.onFooterViewLoadMoreClick(localFolder);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mLoading = findViewById(2131689564);
    this.mNetworkError = findViewById(2131689561);
    this.mLoadMore = findViewById(2131689565);
    this.mLoadMore.setOnClickListener(this);
    this.mErrorActionButton = ((Button)findViewById(2131689563));
    this.mErrorActionButton.setOnClickListener(this);
    this.mErrorText = ((TextView)findViewById(2131689562));
  }

  public void onViewModeChanged(int paramInt)
  {
    if ((this.mTabletDevice) && (paramInt == 2));
    for (Drawable localDrawable = getWideBackground(); ; localDrawable = getNormalBackground())
    {
      setBackgroundDrawable(localDrawable);
      return;
    }
  }

  public void setClickListener(FooterViewClickListener paramFooterViewClickListener)
  {
    this.mClickListener = paramFooterViewClickListener;
  }

  public void setFolder(Folder paramFolder)
  {
    this.mFolder = paramFolder;
    this.mErrorActionButton.setTag(this.mFolder);
    this.mLoadMore.setTag(this.mFolder);
    this.mLoadMoreUri = paramFolder.loadMoreUri;
  }

  public boolean updateStatus(ConversationCursor paramConversationCursor)
  {
    if (paramConversationCursor == null)
      return false;
    boolean bool = true;
    Bundle localBundle = paramConversationCursor.getExtras();
    int i = localBundle.getInt("cursor_status");
    int j;
    if (localBundle.containsKey("cursor_error"))
    {
      j = localBundle.getInt("cursor_error");
      this.mErrorStatus = j;
      if (!UIProvider.CursorStatus.isWaitingForResults(i))
        break label86;
      this.mLoading.setVisibility(0);
      this.mNetworkError.setVisibility(8);
      this.mLoadMore.setVisibility(8);
    }
    while (true)
    {
      return bool;
      j = 0;
      break;
      label86: if (this.mErrorStatus != 0)
      {
        this.mNetworkError.setVisibility(0);
        this.mErrorText.setText(Utils.getSyncStatusText(getContext(), this.mErrorStatus));
        this.mLoading.setVisibility(8);
        this.mLoadMore.setVisibility(8);
        Button localButton = this.mErrorActionButton;
        int k = this.mErrorStatus;
        int m = 0;
        int n;
        if (k != 3)
        {
          localButton.setVisibility(m);
          switch (this.mErrorStatus)
          {
          default:
            n = 2131427329;
            this.mNetworkError.setVisibility(8);
          case 1:
          case 2:
          case 3:
          case 4:
          case 5:
          }
        }
        while (true)
        {
          this.mErrorActionButton.setText(n);
          break;
          m = 8;
          break label158;
          n = 2131427329;
          continue;
          n = 2131427576;
          continue;
          n = 2131427329;
          this.mNetworkError.setVisibility(8);
          continue;
          n = 2131427577;
          continue;
          n = 2131427578;
        }
      }
      label158: if (this.mLoadMoreUri != null)
      {
        this.mLoading.setVisibility(8);
        this.mNetworkError.setVisibility(8);
        this.mLoadMore.setVisibility(0);
      }
      else
      {
        bool = false;
      }
    }
  }

  public static abstract interface FooterViewClickListener
  {
    public abstract void onFooterViewErrorActionClick(Folder paramFolder, int paramInt);

    public abstract void onFooterViewLoadMoreClick(Folder paramFolder);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ConversationListFooterView
 * JD-Core Version:    0.6.2
 */