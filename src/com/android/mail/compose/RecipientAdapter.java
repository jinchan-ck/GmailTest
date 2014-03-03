package com.android.mail.compose;

import android.content.Context;
import com.android.ex.chips.BaseRecipientAdapter;
import com.android.mail.providers.Account;

public class RecipientAdapter extends BaseRecipientAdapter
{
  public RecipientAdapter(Context paramContext, Account paramAccount)
  {
    super(paramContext);
    setAccount(paramAccount);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.RecipientAdapter
 * JD-Core Version:    0.6.2
 */