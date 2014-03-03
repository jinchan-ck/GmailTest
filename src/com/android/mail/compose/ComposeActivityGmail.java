package com.android.mail.compose;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gm.provider.UiProvider;

public class ComposeActivityGmail extends ComposeActivity
{
  public void onCreate(Bundle paramBundle)
  {
    Intent localIntent = getIntent();
    if ((localIntent.getExtras() != null) && (localIntent.getExtras().containsKey("in-reference-to")))
    {
      localIntent.putExtra("in-reference-to-message-uri", UiProvider.getMessageByIdUri(localIntent.getStringExtra("account"), localIntent.getExtras().getLong("in-reference-to")));
      int i = localIntent.getIntExtra("action", -1);
      if ((i == 1) || (i == -1))
        i = 0;
      localIntent.putExtra("action", i);
    }
    super.onCreate(paramBundle);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.ComposeActivityGmail
 * JD-Core Version:    0.6.2
 */