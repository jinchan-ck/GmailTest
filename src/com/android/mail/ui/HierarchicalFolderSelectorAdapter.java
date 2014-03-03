package com.android.mail.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.android.mail.providers.Folder;
import java.util.Set;

public class HierarchicalFolderSelectorAdapter extends FolderSelectorAdapter
{
  private Context mContext;

  public HierarchicalFolderSelectorAdapter(Context paramContext, Cursor paramCursor, int paramInt, String paramString, Folder paramFolder)
  {
    super(paramContext, paramCursor, paramInt, paramString, paramFolder);
    this.mContext = paramContext;
  }

  public HierarchicalFolderSelectorAdapter(Context paramContext, Cursor paramCursor, Set<String> paramSet, int paramInt, String paramString)
  {
    super(paramContext, paramCursor, paramSet, paramInt, paramString);
    this.mContext = paramContext;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = super.getView(paramInt, paramView, paramViewGroup);
    if (isHeader(paramInt))
      return localView;
    Folder localFolder = ((FolderSelectorAdapter.FolderRow)getItem(paramInt)).getFolder();
    CompoundButton localCompoundButton = (CompoundButton)localView.findViewById(2131689655);
    TextView localTextView = (TextView)localView.findViewById(2131689696);
    Object localObject1;
    if (TextUtils.isEmpty(localFolder.hierarchicalDesc))
    {
      localObject1 = localFolder.name;
      if (localCompoundButton == null)
        break label139;
      if (!TextUtils.isEmpty(localFolder.hierarchicalDesc))
        break label125;
    }
    label125: for (Object localObject2 = localFolder.name; ; localObject2 = truncateHierarchy(localFolder.hierarchicalDesc))
    {
      localCompoundButton.setText((CharSequence)localObject2, TextView.BufferType.SPANNABLE);
      return localView;
      localObject1 = truncateHierarchy(localFolder.hierarchicalDesc);
      break;
    }
    label139: localTextView.setText((CharSequence)localObject1, TextView.BufferType.SPANNABLE);
    return localView;
  }

  protected SpannableStringBuilder truncateHierarchy(String paramString)
  {
    SpannableStringBuilder localSpannableStringBuilder;
    if (TextUtils.isEmpty(paramString))
      localSpannableStringBuilder = null;
    String[] arrayOfString;
    do
    {
      return localSpannableStringBuilder;
      arrayOfString = paramString.split("/");
      localSpannableStringBuilder = new SpannableStringBuilder();
    }
    while ((arrayOfString == null) || (arrayOfString.length <= 0));
    int i = arrayOfString.length;
    String str3;
    String str2;
    String str1;
    int j;
    if (i > 2)
    {
      str3 = arrayOfString[0];
      str2 = arrayOfString[(i - 2)];
      str1 = arrayOfString[(i - 1)];
      if (TextUtils.isEmpty(str2))
        break label190;
      if (i <= 3)
        break label183;
      j = 2131427583;
      label84: localSpannableStringBuilder.append(this.mContext.getResources().getString(j, new Object[] { str3, str2 }));
    }
    while (true)
    {
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(2131230759), 0, localSpannableStringBuilder.length(), 33);
      localSpannableStringBuilder.append(str1);
      return localSpannableStringBuilder;
      if (i > 1)
      {
        str3 = arrayOfString[0];
        str1 = arrayOfString[(i - 1)];
        str2 = null;
        break;
      }
      str1 = arrayOfString[0];
      str2 = null;
      str3 = null;
      break;
      label183: j = 2131427582;
      break label84;
      label190: if (!TextUtils.isEmpty(str3))
        localSpannableStringBuilder.append(this.mContext.getResources().getString(2131427584, new Object[] { str3, str2 }));
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.HierarchicalFolderSelectorAdapter
 * JD-Core Version:    0.6.2
 */