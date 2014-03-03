package com.google.android.gm;

import android.content.Context;
import android.view.MenuItem;
import com.google.android.gm.persistence.Persistence;

public class ApplicationMenuHandler
{
  public static boolean handleApplicationMenu(int paramInt, Context paramContext, HelpCallback paramHelpCallback)
  {
    Persistence.getInstance();
    switch (paramInt)
    {
    default:
      return false;
    case 2131689769:
      Utils.showSettings(paramContext);
      return true;
    case 2131689764:
      Utils.startSync(Persistence.getInstance().getActiveAccount(paramContext));
      return true;
    case 2131689750:
      Utils.showHelp(paramContext, paramHelpCallback);
      return true;
    case 2131689749:
    }
    Utils.showAbout(paramContext);
    return true;
  }

  public static boolean handleApplicationMenu(MenuItem paramMenuItem, Context paramContext, HelpCallback paramHelpCallback)
  {
    return handleApplicationMenu(paramMenuItem.getItemId(), paramContext, paramHelpCallback);
  }

  public static abstract interface HelpCallback
  {
    public abstract String getHelpContext();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ApplicationMenuHandler
 * JD-Core Version:    0.6.2
 */