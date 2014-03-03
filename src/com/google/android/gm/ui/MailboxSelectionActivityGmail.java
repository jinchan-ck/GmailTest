package com.google.android.gm.ui;

import com.android.mail.ui.MailboxSelectionActivity;

public class MailboxSelectionActivityGmail extends MailboxSelectionActivity
{
  protected Class<?> getFolderSelectionActivity()
  {
    return FolderSelectionActivityGmail.class;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ui.MailboxSelectionActivityGmail
 * JD-Core Version:    0.6.2
 */