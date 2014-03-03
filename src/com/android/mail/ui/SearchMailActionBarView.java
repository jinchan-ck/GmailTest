package com.android.mail.ui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import com.android.mail.utils.Utils;

public class SearchMailActionBarView extends MailActionBarView
{
  public SearchMailActionBarView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SearchMailActionBarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public SearchMailActionBarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private void clearSearchFocus()
  {
    MenuItem localMenuItem = getSearch();
    if (localMenuItem != null)
      ((SearchView)localMenuItem.getActionView()).clearFocus();
  }

  private void setPopulatedSearchView()
  {
    MenuItem localMenuItem = getSearch();
    if (localMenuItem != null)
    {
      localMenuItem.expandActionView();
      String str = this.mActivity.getIntent().getStringExtra("query");
      SearchView localSearchView = (SearchView)localMenuItem.getActionView();
      if (!TextUtils.isEmpty(str))
        localSearchView.setQuery(str, false);
      localSearchView.clearFocus();
    }
  }

  public boolean onMenuItemActionCollapse(MenuItem paramMenuItem)
  {
    super.onMenuItemActionCollapse(paramMenuItem);
    int i = getMode();
    if ((i == 4) || ((Utils.showTwoPaneSearchResults(getContext())) && (i == 5)))
      this.mController.exitSearchMode();
    return true;
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    super.onPrepareOptionsMenu(paramMenu);
    switch (getMode())
    {
    default:
    case 4:
    case 5:
    }
    while (true)
    {
      return false;
      this.mActionBar.setDisplayHomeAsUpEnabled(true);
      setEmptyMode();
      if (!showConversationSubject())
        setPopulatedSearchView();
      clearSearchFocus();
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.SearchMailActionBarView
 * JD-Core Version:    0.6.2
 */