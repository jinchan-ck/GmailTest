package com.android.mail.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;

public class ConversationListCopy extends View
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private Bitmap mBitmap;

  public ConversationListCopy(Context paramContext)
  {
    this(paramContext, null);
  }

  public ConversationListCopy(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void bind(View paramView)
  {
    unbind();
    if ((paramView.getWidth() == 0) || (paramView.getHeight() == 0))
      return;
    try
    {
      this.mBitmap = Bitmap.createBitmap(paramView.getWidth(), paramView.getHeight(), Bitmap.Config.ARGB_8888);
      paramView.draw(new Canvas(this.mBitmap));
      return;
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      LogUtils.e(LOG_TAG, localOutOfMemoryError, "Unable to create fancy list transition bitmap", new Object[0]);
    }
  }

  protected void onDraw(Canvas paramCanvas)
  {
    if (this.mBitmap == null)
      return;
    paramCanvas.drawBitmap(this.mBitmap, 0.0F, 0.0F, null);
  }

  public void unbind()
  {
    if (this.mBitmap != null)
    {
      this.mBitmap.recycle();
      this.mBitmap = null;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ConversationListCopy
 * JD-Core Version:    0.6.2
 */