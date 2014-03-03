package com.android.mail.ui;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import com.android.mail.AccountSpinnerAdapter;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;

public class MailSpinner extends FrameLayout
  implements View.OnClickListener, AdapterView.OnItemClickListener
{
  private static final String LOG_TAG;
  private Account mAccount;
  private final TextView mAccountName;
  private final LinearLayout mContainer;
  private ActivityController mController;
  private Folder mFolder;
  private final TextView mFolderCount;
  private final TextView mFolderName;
  private final ListPopupWindow mListPopupWindow;
  private AccountSpinnerAdapter mSpinnerAdapter;

  static
  {
    if (!MailSpinner.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      LOG_TAG = LogTag.getLogTag();
      return;
    }
  }

  public MailSpinner(Context paramContext)
  {
    this(paramContext, null);
  }

  public MailSpinner(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, -1);
  }

  public MailSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mListPopupWindow = new ListPopupWindow(paramContext);
    this.mListPopupWindow.setOnItemClickListener(this);
    this.mListPopupWindow.setAnchorView(this);
    int i = paramContext.getResources().getDimensionPixelSize(2131361813);
    this.mListPopupWindow.setWidth(i);
    this.mListPopupWindow.setModal(true);
    addView(LayoutInflater.from(getContext()).inflate(2130968581, null));
    this.mAccountName = ((TextView)findViewById(2131689495));
    this.mFolderName = ((TextView)findViewById(2131689494));
    this.mFolderCount = ((TextView)findViewById(2131689492));
    this.mContainer = ((LinearLayout)findViewById(2131689493));
    this.mContainer.setOnClickListener(this);
  }

  public final void changeEnabledState(boolean paramBoolean)
  {
    setEnabled(paramBoolean);
    if (paramBoolean)
    {
      this.mContainer.setBackgroundResource(2130837644);
      return;
    }
    this.mContainer.setBackgroundDrawable(null);
  }

  public void onClick(View paramView)
  {
    if ((isEnabled()) && (!this.mListPopupWindow.isShowing()))
    {
      this.mListPopupWindow.show();
      this.mController.commitDestructiveActions(false);
    }
  }

  public void onFolderUpdated(Folder paramFolder)
  {
    this.mSpinnerAdapter.onFolderUpdated(paramFolder);
    setFolder(paramFolder);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    String str1 = LOG_TAG;
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = Integer.valueOf(paramInt);
    arrayOfObject1[1] = Long.valueOf(paramLong);
    LogUtils.d(str1, "onNavigationItemSelected(%d, %d) called", arrayOfObject1);
    int i = this.mSpinnerAdapter.getItemViewType(paramInt);
    int j = 0;
    switch (i)
    {
    default:
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      if (j != 0)
        this.mListPopupWindow.dismiss();
      return;
      Account localAccount = (Account)this.mSpinnerAdapter.getItem(paramInt);
      String str3 = LOG_TAG;
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = localAccount.name;
      LogUtils.d(str3, "onNavigationItemSelected: Selecting account: %s", arrayOfObject3);
      if (this.mAccount.uri.equals(localAccount.uri))
        this.mController.loadAccountInbox();
      while (true)
      {
        j = 1;
        break;
        this.mController.onAccountChanged(localAccount);
      }
      Object localObject = this.mSpinnerAdapter.getItem(paramInt);
      assert ((localObject instanceof Folder));
      String str2 = LOG_TAG;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = ((Folder)localObject).name;
      LogUtils.d(str2, "onNavigationItemSelected: Selecting folder: %s", arrayOfObject2);
      this.mController.onFolderChanged((Folder)localObject);
      j = 1;
      continue;
      this.mController.showFolderList();
      j = 1;
    }
  }

  public void setAccount(Account paramAccount)
  {
    this.mAccount = paramAccount;
    if (this.mAccount != null)
      this.mAccountName.setText(this.mAccount.name);
  }

  public void setAdapter(AccountSpinnerAdapter paramAccountSpinnerAdapter)
  {
    this.mSpinnerAdapter = paramAccountSpinnerAdapter;
    this.mListPopupWindow.setAdapter(this.mSpinnerAdapter);
  }

  public void setController(ActivityController paramActivityController)
  {
    this.mController = paramActivityController;
  }

  public void setFolder(Folder paramFolder)
  {
    this.mFolder = paramFolder;
    if (this.mFolder != null)
    {
      this.mFolderName.setText(this.mFolder.name);
      this.mFolderCount.setText(Utils.getUnreadCountString(getContext(), Utils.getFolderUnreadDisplayCount(this.mFolder)));
      if (this.mSpinnerAdapter != null)
        this.mSpinnerAdapter.setCurrentFolder(this.mFolder);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.MailSpinner
 * JD-Core Version:    0.6.2
 */