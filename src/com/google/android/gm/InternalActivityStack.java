package com.google.android.gm;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

public class InternalActivityStack
{
  private static List<Activity> sActivityStack = new ArrayList();

  public static void finishAllOtherActivities(Activity paramActivity)
  {
    while (true)
    {
      int i;
      synchronized (sActivityStack)
      {
        i = sActivityStack.size() - 1;
        if (i >= 0)
        {
          Activity localActivity = (Activity)sActivityStack.get(i);
          if ((localActivity != null) && (!localActivity.isFinishing()) && (localActivity != paramActivity))
          {
            localActivity.finish();
            sActivityStack.remove(localActivity);
          }
        }
        else
        {
          return;
        }
      }
      i--;
    }
  }

  public static void pushActivity(Activity paramActivity)
  {
    sActivityStack.add(paramActivity);
  }

  public static void removeActivity(Activity paramActivity)
  {
    sActivityStack.remove(paramActivity);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.InternalActivityStack
 * JD-Core Version:    0.6.2
 */