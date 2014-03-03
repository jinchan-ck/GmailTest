package com.android.mail.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.mail.providers.Folder;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FolderSelectorAdapter extends BaseAdapter
{
  private Folder mExcludedFolder;
  protected final List<FolderRow> mFolderRows = Lists.newArrayList();
  private final String mHeader;
  private final LayoutInflater mInflater;
  private final int mLayout;

  public FolderSelectorAdapter(Context paramContext, Cursor paramCursor, int paramInt, String paramString, Folder paramFolder)
  {
    this.mInflater = LayoutInflater.from(paramContext);
    this.mLayout = paramInt;
    this.mHeader = paramString;
    this.mExcludedFolder = paramFolder;
    createFolderRows(paramCursor, null);
  }

  public FolderSelectorAdapter(Context paramContext, Cursor paramCursor, Set<String> paramSet, int paramInt, String paramString)
  {
    this.mInflater = LayoutInflater.from(paramContext);
    this.mLayout = paramInt;
    this.mHeader = paramString;
    createFolderRows(paramCursor, paramSet);
  }

  private final boolean hasHeader()
  {
    return this.mHeader != null;
  }

  public int correctPosition(int paramInt)
  {
    if (hasHeader())
      paramInt--;
    return paramInt;
  }

  protected void createFolderRows(Cursor paramCursor, Set<String> paramSet)
  {
    if (paramCursor == null)
      return;
    ArrayList localArrayList1 = Lists.newArrayList();
    ArrayList localArrayList2 = Lists.newArrayList();
    if (paramCursor.moveToFirst());
    label158: label189: 
    while (true)
    {
      Folder localFolder = new Folder(paramCursor);
      boolean bool;
      FolderRow localFolderRow;
      if ((paramSet != null) && (paramSet.contains(localFolder.uri.toString())))
      {
        bool = true;
        if ((meetsRequirements(localFolder)) && (!Objects.equal(localFolder, this.mExcludedFolder)))
        {
          localFolderRow = new FolderRow(localFolder, bool);
          if (!bool)
            break label158;
          this.mFolderRows.add(localFolderRow);
        }
      }
      while (true)
      {
        if (paramCursor.moveToNext())
          break label189;
        Collections.sort(this.mFolderRows);
        this.mFolderRows.addAll(localArrayList2);
        Collections.sort(localArrayList1);
        this.mFolderRows.addAll(localArrayList1);
        return;
        bool = false;
        break;
        if (localFolder.isProviderFolder())
          localArrayList2.add(localFolderRow);
        else
          localArrayList1.add(localFolderRow);
      }
    }
  }

  public int getCount()
  {
    int i = this.mFolderRows.size();
    if (hasHeader());
    for (int j = 1; ; j = 0)
      return j + i;
  }

  public Object getItem(int paramInt)
  {
    if (isHeader(paramInt))
      return this.mHeader;
    return this.mFolderRows.get(correctPosition(paramInt));
  }

  public long getItemId(int paramInt)
  {
    if (isHeader(paramInt))
      return -1L;
    return paramInt;
  }

  public int getItemViewType(int paramInt)
  {
    if (isHeader(paramInt))
      return 0;
    return 1;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (isHeader(paramInt))
    {
      if (paramView != null);
      for (TextView localTextView2 = (TextView)paramView; ; localTextView2 = (TextView)this.mInflater.inflate(2130968630, paramViewGroup, false))
      {
        localTextView2.setText(this.mHeader);
        return localTextView2;
      }
    }
    View localView1 = paramView;
    if (localView1 == null)
      localView1 = this.mInflater.inflate(this.mLayout, paramViewGroup, false);
    FolderRow localFolderRow = (FolderRow)getItem(paramInt);
    Folder localFolder = localFolderRow.getFolder();
    if (!TextUtils.isEmpty(localFolder.hierarchicalDesc));
    for (String str = localFolder.hierarchicalDesc; ; str = localFolder.name)
    {
      CompoundButton localCompoundButton = (CompoundButton)localView1.findViewById(2131689655);
      TextView localTextView1 = (TextView)localView1.findViewById(2131689696);
      if (localCompoundButton != null)
      {
        localCompoundButton.setClickable(false);
        localCompoundButton.setText(str);
        localCompoundButton.setChecked(localFolderRow.isPresent());
      }
      if (localTextView1 != null)
        localTextView1.setText(str);
      View localView2 = localView1.findViewById(2131689646);
      ImageView localImageView = (ImageView)localView1.findViewById(2131689521);
      Folder.setFolderBlockColor(localFolder, localView2);
      Folder.setIcon(localFolder, localImageView);
      return localView1;
    }
  }

  public int getViewTypeCount()
  {
    return 2;
  }

  protected final boolean isHeader(int paramInt)
  {
    return (paramInt == 0) && (hasHeader());
  }

  protected boolean meetsRequirements(Folder paramFolder)
  {
    return (paramFolder.supportsCapability(8)) && (paramFolder.type != 5) && (!Objects.equal(paramFolder, this.mExcludedFolder));
  }

  public static class FolderRow
    implements Comparable<FolderRow>
  {
    private final Folder mFolder;
    private boolean mIsPresent;

    public FolderRow(Folder paramFolder, boolean paramBoolean)
    {
      this.mFolder = paramFolder;
      this.mIsPresent = paramBoolean;
    }

    public int compareTo(FolderRow paramFolderRow)
    {
      if (equals(paramFolderRow))
        return 0;
      if (this.mIsPresent != paramFolderRow.mIsPresent)
      {
        if (this.mIsPresent)
          return -1;
        return 1;
      }
      return this.mFolder.name.compareToIgnoreCase(paramFolderRow.mFolder.name);
    }

    public Folder getFolder()
    {
      return this.mFolder;
    }

    public boolean isPresent()
    {
      return this.mIsPresent;
    }

    public void setIsPresent(boolean paramBoolean)
    {
      this.mIsPresent = paramBoolean;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.FolderSelectorAdapter
 * JD-Core Version:    0.6.2
 */