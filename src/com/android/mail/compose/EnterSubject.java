package com.android.mail.compose;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

public class EnterSubject extends EditText
{
  public EnterSubject(Context paramContext)
  {
    super(paramContext);
  }

  public EnterSubject(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public EnterSubject(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo)
  {
    InputConnection localInputConnection = super.onCreateInputConnection(paramEditorInfo);
    int i = 0xFF & paramEditorInfo.imeOptions;
    if ((i & 0x5) != 0)
    {
      paramEditorInfo.imeOptions = (i ^ paramEditorInfo.imeOptions);
      paramEditorInfo.imeOptions = (0x5 | paramEditorInfo.imeOptions);
    }
    if ((0x40000000 & paramEditorInfo.imeOptions) != 0)
      paramEditorInfo.imeOptions = (0xBFFFFFFF & paramEditorInfo.imeOptions);
    return localInputConnection;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.EnterSubject
 * JD-Core Version:    0.6.2
 */