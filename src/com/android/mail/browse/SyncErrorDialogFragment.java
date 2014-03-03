package com.android.mail.browse;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

public class SyncErrorDialogFragment extends DialogFragment
{
  public static SyncErrorDialogFragment newInstance()
  {
    return new SyncErrorDialogFragment();
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    return new AlertDialog.Builder(getActivity()).setTitle(2131427579).setMessage(2131427580).setPositiveButton(2131427539, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface.dismiss();
      }
    }).setNegativeButton(2131427581, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        Intent localIntent = new Intent("android.settings.INTERNAL_STORAGE_SETTINGS");
        localIntent.addFlags(524288);
        SyncErrorDialogFragment.this.startActivity(localIntent);
        paramAnonymousDialogInterface.dismiss();
      }
    }).create();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.SyncErrorDialogFragment
 * JD-Core Version:    0.6.2
 */