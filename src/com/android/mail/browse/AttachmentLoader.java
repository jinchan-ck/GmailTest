package com.android.mail.browse;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import com.android.mail.providers.Attachment;
import com.android.mail.providers.UIProvider;
import com.google.common.collect.Maps;
import java.util.Map;

public class AttachmentLoader extends CursorLoader
{
  public AttachmentLoader(Context paramContext, Uri paramUri)
  {
    super(paramContext, paramUri, UIProvider.ATTACHMENT_PROJECTION, null, null, null);
  }

  public Cursor loadInBackground()
  {
    return new AttachmentCursor(super.loadInBackground(), null);
  }

  public static class AttachmentCursor extends CursorWrapper
  {
    private Map<String, Attachment> mCache = Maps.newHashMap();

    private AttachmentCursor(Cursor paramCursor)
    {
      super();
    }

    public Attachment get()
    {
      String str = getWrappedCursor().getString(2);
      Attachment localAttachment = (Attachment)this.mCache.get(str);
      if (localAttachment == null)
      {
        localAttachment = new Attachment(this);
        this.mCache.put(str, localAttachment);
      }
      return localAttachment;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.AttachmentLoader
 * JD-Core Version:    0.6.2
 */