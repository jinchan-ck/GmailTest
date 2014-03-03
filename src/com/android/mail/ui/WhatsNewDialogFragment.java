package com.android.mail.ui;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.android.mail.preferences.MailPrefs;
import com.android.mail.utils.Utils;

public class WhatsNewDialogFragment extends DialogFragment
{
  private WhatsNewDialogListener mCallback = null;

  public static WhatsNewDialogFragment newInstance()
  {
    return new WhatsNewDialogFragment();
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.mCallback = ((WhatsNewDialogListener)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    throw new ClassCastException(paramActivity.getClass().getSimpleName() + " must implement WhatsNewDialogListener");
  }

  public void onCancel(DialogInterface paramDialogInterface)
  {
    super.onCancel(paramDialogInterface);
    int i = Utils.getVersionCode(getActivity());
    if (i != -1)
      MailPrefs.get(getActivity()).setHasShownWhatsNew(i);
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    final Context localContext = getActivity().getApplicationContext();
    View localView = LayoutInflater.from(getActivity()).inflate(2130968684, null);
    this.mCallback.onWhatsNewDialogLayoutInflated(localView);
    return new AlertDialog.Builder(getActivity()).setTitle(2131427601).setView(localView).setPositiveButton(2131427539, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        int i = Utils.getVersionCode(localContext);
        if (i != -1)
          MailPrefs.get(localContext).setHasShownWhatsNew(i);
        paramAnonymousDialogInterface.dismiss();
      }
    }).create();
  }

  public static abstract interface WhatsNewDialogLauncher
  {
    public abstract void showWhatsNewDialog();
  }

  public static abstract interface WhatsNewDialogListener
  {
    public abstract void onWhatsNewDialogLayoutInflated(View paramView);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.WhatsNewDialogFragment
 * JD-Core Version:    0.6.2
 */