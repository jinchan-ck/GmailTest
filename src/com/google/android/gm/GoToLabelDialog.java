package com.google.android.gm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class GoToLabelDialog extends AutoCompletionLabelPickerDialog
{
  protected GoToLabelDialog(Context paramContext)
  {
    super(paramContext);
    setTitle(2131296365);
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    Intent localIntent = new Intent();
    Context localContext = getContext();
    localIntent.setClass(localContext, ConversationListActivity.class);
    localIntent.putExtra("label", getLabel().trim());
    localContext.startActivity(localIntent);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.GoToLabelDialog
 * JD-Core Version:    0.6.2
 */