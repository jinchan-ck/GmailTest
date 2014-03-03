package com.android.mail.browse;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

class AttachmentCommandHandler extends AsyncQueryHandler
{
  public AttachmentCommandHandler(Context paramContext)
  {
    super(paramContext.getContentResolver());
  }

  public void sendCommand(Uri paramUri, ContentValues paramContentValues)
  {
    startUpdate(0, null, paramUri, paramContentValues, null, null);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.AttachmentCommandHandler
 * JD-Core Version:    0.6.2
 */