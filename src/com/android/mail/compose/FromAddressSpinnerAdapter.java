package com.android.mail.compose;

import android.content.Context;
import android.text.TextUtils;
import android.text.util.Rfc822Token;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.mail.providers.ReplyFromAccount;
import java.util.Iterator;
import java.util.List;

public class FromAddressSpinnerAdapter extends ArrayAdapter<ReplyFromAccount>
{
  public static int ACCOUNT_ADDRESS = 1;
  public static int ACCOUNT_DISPLAY;
  public static int REAL_ACCOUNT = 2;
  private static String sFormatString;
  private LayoutInflater mInflater;

  static
  {
    ACCOUNT_DISPLAY = 0;
  }

  public FromAddressSpinnerAdapter(Context paramContext)
  {
    super(paramContext, 2130968637, 2131689639);
    sFormatString = getContext().getString(2131427429);
  }

  private static CharSequence formatAddress(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return "";
    String str = sFormatString;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = android.text.util.Rfc822Tokenizer.tokenize(paramString)[0].getAddress();
    return String.format(str, arrayOfObject);
  }

  public void addAccounts(List<ReplyFromAccount> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
      add((ReplyFromAccount)localIterator.next());
  }

  public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    ReplyFromAccount localReplyFromAccount = (ReplyFromAccount)getItem(paramInt);
    if (localReplyFromAccount.isCustomFrom);
    for (int i = 2130968625; ; i = 2130968636)
    {
      View localView = getInflater().inflate(i, null);
      ((TextView)localView.findViewById(2131689639)).setText(localReplyFromAccount.name);
      if (localReplyFromAccount.isCustomFrom)
        ((TextView)localView.findViewById(2131689640)).setText(formatAddress(localReplyFromAccount.address));
      return localView;
    }
  }

  protected LayoutInflater getInflater()
  {
    if (this.mInflater == null)
      this.mInflater = ((LayoutInflater)getContext().getSystemService("layout_inflater"));
    return this.mInflater;
  }

  public int getItemViewType(int paramInt)
  {
    if (((ReplyFromAccount)getItem(paramInt)).isCustomFrom)
      return 1;
    return 0;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    ReplyFromAccount localReplyFromAccount = (ReplyFromAccount)getItem(paramInt);
    int i;
    if (localReplyFromAccount.isCustomFrom)
    {
      i = 2130968626;
      if (paramView != null)
        break label95;
    }
    label95: for (View localView = getInflater().inflate(i, null); ; localView = paramView)
    {
      ((TextView)localView.findViewById(2131689639)).setText(localReplyFromAccount.name);
      if (localReplyFromAccount.isCustomFrom)
        ((TextView)localView.findViewById(2131689640)).setText(formatAddress(localReplyFromAccount.address));
      return localView;
      i = 2130968637;
      break;
    }
  }

  public int getViewTypeCount()
  {
    return 2;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.FromAddressSpinnerAdapter
 * JD-Core Version:    0.6.2
 */