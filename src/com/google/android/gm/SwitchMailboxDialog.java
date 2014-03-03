package com.google.android.gm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.LabelMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwitchMailboxDialog extends AlertDialog
  implements AdapterView.OnItemClickListener
{
  private static final String ACCOUNT = "account";
  private static final String ACCOUNT_STATUS = "account-status";
  private static final String[] COLUMN_NAMES = { "account", "account-status" };
  private static final String IS_ACTIVE = "is-active";
  private static final String UNREAD = "unread";
  private int[] VIEW_IDS = { 2131361862, 2131361863 };
  private Account[] mAccounts;
  private Activity mActivity;

  public SwitchMailboxDialog(Activity paramActivity)
  {
    super(paramActivity);
    Gmail localGmail = LongShadowUtils.getContentProviderMailAccess(paramActivity.getContentResolver());
    this.mAccounts = AccountManager.get(paramActivity).getAccountsByType("com.google");
    String str1 = Persistence.getInstance(paramActivity).getActiveAccount(paramActivity);
    if (this.mAccounts.length == 2)
    {
      if (this.mAccounts[0].name.equals(str1));
      for (String str3 = this.mAccounts[1].name; ; str3 = this.mAccounts[0].name)
      {
        Utils.changeAccount(paramActivity, str3, true);
        paramActivity.finish();
        return;
      }
    }
    this.mActivity = paramActivity;
    ListView localListView = (ListView)((LayoutInflater)paramActivity.getSystemService("layout_inflater")).inflate(2130903075, null);
    localListView.setOnItemClickListener(this);
    setView(localListView, 0, 0, 0, 0);
    setTitle(Utils.formatPlural(paramActivity, 2131427348, this.mAccounts.length));
    String[] arrayOfString = new String[this.mAccounts.length];
    ArrayList localArrayList = Lists.newArrayList();
    int i = 0;
    while (true)
    {
      int j = this.mAccounts.length;
      if (i < j)
      {
        arrayOfString[i] = this.mAccounts[i].name;
        HashMap localHashMap = Maps.newHashMap();
        String str2 = arrayOfString[i];
        localHashMap.put("account", str2);
        try
        {
          Gmail.LabelMap localLabelMap = localGmail.getLabelMap(str2);
          int k = localLabelMap.getNumUnreadConversations(localLabelMap.getLabelIdInbox());
          localHashMap.put("account-status", LongShadowUtils.getDisplayableLabel(paramActivity, localLabelMap, "^i"));
          localHashMap.put("unread", Integer.valueOf(k));
          if (arrayOfString[i].equals(str1))
            localHashMap.put("is-active", "true");
          localArrayList.add(localHashMap);
          i++;
        }
        catch (IllegalStateException localIllegalStateException)
        {
          while (true)
            localHashMap.put("account-status", paramActivity.getResources().getString(2131296487));
        }
      }
    }
    localListView.setAdapter(new SimpleAdapter(paramActivity, localArrayList, 2130903064, COLUMN_NAMES, this.VIEW_IDS)
    {
      public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
      {
        return super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup);
      }
    });
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Utils.changeAccount(this.mActivity, this.mAccounts[paramInt].name, true);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.SwitchMailboxDialog
 * JD-Core Version:    0.6.2
 */