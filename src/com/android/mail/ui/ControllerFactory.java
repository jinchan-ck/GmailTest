package com.android.mail.ui;

public class ControllerFactory
{
  public static ActivityController forActivity(MailActivity paramMailActivity, ViewMode paramViewMode, boolean paramBoolean)
  {
    if (paramBoolean)
      return new TwoPaneController(paramMailActivity, paramViewMode);
    return new OnePaneController(paramMailActivity, paramViewMode);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ControllerFactory
 * JD-Core Version:    0.6.2
 */