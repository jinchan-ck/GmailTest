package com.google.android.gm;

import android.content.Context;
import android.content.DialogInterface;

public class AssignLabelDialog extends AutoCompletionLabelPickerDialog
{
  private String mDisplayedLabel;

  public AssignLabelDialog(Context paramContext, String paramString)
  {
    super(paramContext);
    setTitle(2131296366);
    this.mDisplayedLabel = paramString;
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    BulkOperationHelper.getInstance(getContext()).addOrRemoveLabel(getContext(), getAccount(), getLabel(), true, getConversations(), true, this.mDisplayedLabel, true, null);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.AssignLabelDialog
 * JD-Core Version:    0.6.2
 */