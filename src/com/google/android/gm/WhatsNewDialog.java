package com.google.android.gm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.view.LayoutInflater;

public class WhatsNewDialog extends AlertDialog
  implements DialogInterface.OnClickListener
{
  public WhatsNewDialog(Context paramContext)
  {
    super(paramContext);
    setTitle(2131296273);
    setIcon(paramContext.getResources().getDrawable(2130837573));
    setButton(-1, paramContext.getString(17039370), this);
    setInverseBackgroundForced(true);
    setView(((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(2130903080, null), 0, 0, 0, 0);
    String str = Utils.getVersionCode(paramContext);
    if (str != null)
      Persistence.getInstance(paramContext).setHasShownWhatsNew(paramContext, str);
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.WhatsNewDialog
 * JD-Core Version:    0.6.2
 */