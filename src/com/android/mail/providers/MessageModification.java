package com.android.mail.providers;

import android.content.ContentValues;
import android.text.TextUtils;
import java.util.List;

public class MessageModification
{
  public static void putAppendRefMessageContent(ContentValues paramContentValues, boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      paramContentValues.put("appendRefMessageContent", Integer.valueOf(i));
      return;
    }
  }

  public static void putAttachments(ContentValues paramContentValues, List<Attachment> paramList)
  {
    paramContentValues.put("joinedAttachmentInfos", Attachment.toJSONArray(paramList));
  }

  public static void putBccAddresses(ContentValues paramContentValues, String[] paramArrayOfString)
  {
    paramContentValues.put("bccAddresses", TextUtils.join(",", paramArrayOfString));
  }

  public static void putBody(ContentValues paramContentValues, String paramString)
  {
    paramContentValues.put("bodyText", paramString);
  }

  public static void putBodyHtml(ContentValues paramContentValues, String paramString)
  {
    paramContentValues.put("bodyHtml", paramString);
  }

  public static void putCcAddresses(ContentValues paramContentValues, String[] paramArrayOfString)
  {
    paramContentValues.put("ccAddresses", TextUtils.join(",", paramArrayOfString));
  }

  public static void putCustomFromAddress(ContentValues paramContentValues, String paramString)
  {
    paramContentValues.put("customFrom", paramString);
  }

  public static void putDraftType(ContentValues paramContentValues, int paramInt)
  {
    paramContentValues.put("draftType", Integer.valueOf(paramInt));
  }

  public static void putForward(ContentValues paramContentValues, boolean paramBoolean)
  {
    paramContentValues.put("draftType", Integer.valueOf(4));
  }

  public static void putQuoteStartPos(ContentValues paramContentValues, int paramInt)
  {
    paramContentValues.put("quotedTextStartPos", Integer.valueOf(paramInt));
  }

  public static void putRefMessageId(ContentValues paramContentValues, String paramString)
  {
    paramContentValues.put("refMessageId", paramString);
  }

  public static void putSubject(ContentValues paramContentValues, String paramString)
  {
    paramContentValues.put("subject", paramString);
  }

  public static void putToAddresses(ContentValues paramContentValues, String[] paramArrayOfString)
  {
    paramContentValues.put("toAddresses", TextUtils.join(",", paramArrayOfString));
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.MessageModification
 * JD-Core Version:    0.6.2
 */