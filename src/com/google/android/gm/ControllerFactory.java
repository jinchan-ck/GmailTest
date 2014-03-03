package com.google.android.gm;

import com.android.mail.utils.Utils;

public class ControllerFactory
{
  public static LabelsActivityController forActivity(LabelsActivityController.ControllableLabelsActivity paramControllableLabelsActivity)
  {
    if (Utils.useTabletUI(paramControllableLabelsActivity.getContext()))
      return new TwoPaneLabelsController(paramControllableLabelsActivity);
    return new OnePaneLabelsController(paramControllableLabelsActivity);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ControllerFactory
 * JD-Core Version:    0.6.2
 */