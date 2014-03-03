package com.android.mail.browse;

import com.android.mail.providers.Attachment;
import java.util.List;

public abstract interface AttachmentViewInterface
{
  public abstract List<Attachment> getAttachments();

  public abstract void onUpdateStatus();

  public abstract void updateProgress(boolean paramBoolean);

  public abstract void viewAttachment();
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.AttachmentViewInterface
 * JD-Core Version:    0.6.2
 */