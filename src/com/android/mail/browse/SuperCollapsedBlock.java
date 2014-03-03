package com.android.mail.browse;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.mail.utils.LogTag;

public class SuperCollapsedBlock extends FrameLayout
  implements View.OnClickListener
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private View mBackgroundView;
  private OnClickListener mClick;
  private TextView mCountView;
  private View mIconView;
  private ConversationViewAdapter.SuperCollapsedBlockItem mModel;

  public SuperCollapsedBlock(Context paramContext)
  {
    this(paramContext, null);
  }

  public SuperCollapsedBlock(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setActivated(false);
    setOnClickListener(this);
  }

  public void bind(ConversationViewAdapter.SuperCollapsedBlockItem paramSuperCollapsedBlockItem)
  {
    this.mModel = paramSuperCollapsedBlockItem;
    setCount(1 + (paramSuperCollapsedBlockItem.getEnd() - paramSuperCollapsedBlockItem.getStart()));
  }

  public void initialize(OnClickListener paramOnClickListener)
  {
    this.mClick = paramOnClickListener;
  }

  public void onClick(View paramView)
  {
    ((TextView)findViewById(2131689701)).setText(2131427550);
    this.mCountView.setVisibility(8);
    if (this.mClick != null)
      getHandler().post(new Runnable()
      {
        public void run()
        {
          SuperCollapsedBlock.this.mClick.onSuperCollapsedClick(SuperCollapsedBlock.this.mModel);
        }
      });
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mIconView = findViewById(2131689699);
    this.mCountView = ((TextView)findViewById(2131689700));
    this.mBackgroundView = findViewById(2131689698);
    BitmapDrawable localBitmapDrawable = (BitmapDrawable)getResources().getDrawable(2130837542);
    localBitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    this.mBackgroundView.setBackgroundDrawable(localBitmapDrawable);
  }

  public void setCount(int paramInt)
  {
    this.mCountView.setText(Integer.toString(paramInt));
    this.mIconView.getBackground().setLevel(paramInt);
  }

  public static abstract interface OnClickListener
  {
    public abstract void onSuperCollapsedClick(ConversationViewAdapter.SuperCollapsedBlockItem paramSuperCollapsedBlockItem);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.SuperCollapsedBlock
 * JD-Core Version:    0.6.2
 */