package com.android.mail.ui;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;

public class AutoFitPromptDialogFragment extends DialogFragment
{
  public static AutoFitPromptDialogFragment newInstance(Uri paramUri)
  {
    AutoFitPromptDialogFragment localAutoFitPromptDialogFragment = new AutoFitPromptDialogFragment();
    Bundle localBundle = new Bundle(1);
    localBundle.putParcelable("updateSettingsUri", paramUri);
    localAutoFitPromptDialogFragment.setArguments(localBundle);
    return localAutoFitPromptDialogFragment;
  }

  private void saveAutoFitSetting(int paramInt)
  {
    Activity localActivity = getActivity();
    if (localActivity == null)
      return;
    ContentValues localContentValues = new ContentValues(1);
    localContentValues.put("conversation_view_mode", Integer.valueOf(paramInt));
    localActivity.getContentResolver().update((Uri)getArguments().getParcelable("updateSettingsUri"), localContentValues, null, null);
  }

  public void onCancel(DialogInterface paramDialogInterface)
  {
    super.onCancel(paramDialogInterface);
    saveAutoFitSetting(1);
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    return new AlertDialog.Builder(getActivity()).setTitle(2131427602).setMessage(2131427603).setPositiveButton(2131427542, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        AutoFitPromptDialogFragment.this.saveAutoFitSetting(0);
      }
    }).setNegativeButton(2131427543, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        AutoFitPromptDialogFragment.this.saveAutoFitSetting(1);
      }
    }).create();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.AutoFitPromptDialogFragment
 * JD-Core Version:    0.6.2
 */