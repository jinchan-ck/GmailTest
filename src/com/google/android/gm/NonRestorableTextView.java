package com.google.android.gm;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

public class NonRestorableTextView extends TextView
{
  public NonRestorableTextView(Context paramContext)
  {
    super(paramContext);
  }

  public NonRestorableTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public NonRestorableTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public Parcelable onSaveInstanceState()
  {
    super.onSaveInstanceState();
    return null;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.NonRestorableTextView
 * JD-Core Version:    0.6.2
 */