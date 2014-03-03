package com.android.mail.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.Iterator;

public class SeparatedFolderListAdapter extends BaseAdapter
{
  public final ArrayList<FolderSelectorAdapter> sections = new ArrayList();

  public SeparatedFolderListAdapter(Context paramContext)
  {
  }

  public void addSection(FolderSelectorAdapter paramFolderSelectorAdapter)
  {
    this.sections.add(paramFolderSelectorAdapter);
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public int getCount()
  {
    int i = 0;
    Iterator localIterator = this.sections.iterator();
    while (localIterator.hasNext())
      i += ((FolderSelectorAdapter)localIterator.next()).getCount();
    return i;
  }

  public Object getItem(int paramInt)
  {
    Iterator localIterator = this.sections.iterator();
    while (localIterator.hasNext())
    {
      FolderSelectorAdapter localFolderSelectorAdapter = (FolderSelectorAdapter)localIterator.next();
      int i = localFolderSelectorAdapter.getCount();
      if ((paramInt == 0) || (paramInt < i))
        return localFolderSelectorAdapter.getItem(paramInt);
      paramInt -= i;
    }
    return null;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public int getItemViewType(int paramInt)
  {
    int i = 0;
    Iterator localIterator = this.sections.iterator();
    while (localIterator.hasNext())
    {
      FolderSelectorAdapter localFolderSelectorAdapter = (FolderSelectorAdapter)localIterator.next();
      int j = localFolderSelectorAdapter.getCount();
      if ((paramInt == 0) || (paramInt < j))
        return i + localFolderSelectorAdapter.getItemViewType(paramInt);
      paramInt -= j;
      i += localFolderSelectorAdapter.getViewTypeCount();
    }
    return -1;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Iterator localIterator = this.sections.iterator();
    while (localIterator.hasNext())
    {
      FolderSelectorAdapter localFolderSelectorAdapter = (FolderSelectorAdapter)localIterator.next();
      int i = localFolderSelectorAdapter.getCount();
      if ((paramInt == 0) || (paramInt < i))
        return localFolderSelectorAdapter.getView(paramInt, paramView, paramViewGroup);
      paramInt -= i;
    }
    return null;
  }

  public int getViewTypeCount()
  {
    int i = 0;
    Iterator localIterator = this.sections.iterator();
    while (localIterator.hasNext())
      i += ((FolderSelectorAdapter)localIterator.next()).getViewTypeCount();
    return i;
  }

  public boolean isEnabled(int paramInt)
  {
    return getItemViewType(paramInt) != 0;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.SeparatedFolderListAdapter
 * JD-Core Version:    0.6.2
 */